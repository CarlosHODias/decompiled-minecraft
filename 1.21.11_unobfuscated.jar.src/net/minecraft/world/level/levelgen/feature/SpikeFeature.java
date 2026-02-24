/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FireBlock;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class SpikeFeature extends Feature<SpikeConfiguration> {
/*     */   public static final int NUMBER_OF_SPIKES = 10;
/*  35 */   private static final LoadingCache<Long, List<EndSpike>> SPIKE_CACHE = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(new SpikeCacheLoader()); private static final int SPIKE_DISTANCE = 42;
/*     */   
/*     */   public SpikeFeature(Codec<SpikeConfiguration> codec) {
/*  38 */     super(codec);
/*     */   }
/*     */   
/*     */   public static List<EndSpike> getSpikesForLevel(WorldGenLevel level) {
/*  42 */     RandomSource random = RandomSource.create(level.getSeed());
/*  43 */     long key = random.nextLong() & 0xFFFFL;
/*  44 */     return (List<EndSpike>)SPIKE_CACHE.getUnchecked(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<SpikeConfiguration> context) {
/*  49 */     SpikeConfiguration config = context.config();
/*  50 */     WorldGenLevel level = context.level();
/*  51 */     RandomSource random = context.random();
/*  52 */     BlockPos origin = context.origin();
/*  53 */     List<EndSpike> spikes = config.getSpikes();
/*  54 */     if (spikes.isEmpty()) {
/*  55 */       spikes = getSpikesForLevel(level);
/*     */     }
/*     */     
/*  58 */     for (EndSpike spike : spikes) {
/*  59 */       if (spike.isCenterWithinChunk(origin)) {
/*  60 */         placeSpike((ServerLevelAccessor)level, random, config, spike);
/*     */       }
/*     */     } 
/*     */     
/*  64 */     return true;
/*     */   }
/*     */   
/*     */   private void placeSpike(ServerLevelAccessor level, RandomSource random, SpikeConfiguration config, EndSpike spike) {
/*  68 */     int radius = spike.getRadius();
/*  69 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(new BlockPos(spike.getCenterX() - radius, level.getMinY(), spike.getCenterZ() - radius), new BlockPos(spike.getCenterX() + radius, spike.getHeight() + 10, spike.getCenterZ() + radius))) {
/*  70 */       if (pos.distToLowCornerSqr(spike.getCenterX(), pos.getY(), spike.getCenterZ()) <= (radius * radius + 1) && pos.getY() < spike.getHeight()) {
/*  71 */         setBlock((LevelWriter)level, pos, Blocks.OBSIDIAN.defaultBlockState()); continue;
/*  72 */       }  if (pos.getY() > 65) {
/*  73 */         setBlock((LevelWriter)level, pos, Blocks.AIR.defaultBlockState());
/*     */       }
/*     */     } 
/*     */     
/*  77 */     if (spike.isGuarded()) {
/*  78 */       int start = -2;
/*  79 */       int end = 2;
/*  80 */       int yEnd = 3;
/*     */       
/*  82 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*  83 */       for (int dx = -2; dx <= 2; dx++) {
/*  84 */         for (int dz = -2; dz <= 2; dz++) {
/*  85 */           for (int dy = 0; dy <= 3; dy++) {
/*  86 */             boolean isXSide = (Mth.abs(dx) == 2);
/*  87 */             boolean isZSide = (Mth.abs(dz) == 2);
/*  88 */             boolean top = (dy == 3);
/*     */             
/*  90 */             if (isXSide || isZSide || top) {
/*  91 */               boolean xEdge = (dx == -2 || dx == 2 || top);
/*  92 */               boolean zEdge = (dz == -2 || dz == 2 || top);
/*     */               
/*  94 */               BlockState state = (BlockState)((BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState()
/*  95 */                 .setValue((Property)IronBarsBlock.NORTH, (xEdge && dz != -2)))
/*  96 */                 .setValue((Property)IronBarsBlock.SOUTH, (xEdge && dz != 2)))
/*  97 */                 .setValue((Property)IronBarsBlock.WEST, (zEdge && dx != -2)))
/*  98 */                 .setValue((Property)IronBarsBlock.EAST, (zEdge && dx != 2));
/*     */               
/* 100 */               setBlock((LevelWriter)level, (BlockPos)pos.set(spike.getCenterX() + dx, spike.getHeight() + dy, spike.getCenterZ() + dz), state);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     EndCrystal endCrystal = (EndCrystal)EntityType.END_CRYSTAL.create((net.minecraft.world.level.Level)level.getLevel(), net.minecraft.world.entity.EntitySpawnReason.STRUCTURE);
/* 108 */     if (endCrystal != null) {
/* 109 */       endCrystal.setBeamTarget(config.getCrystalBeamTarget());
/* 110 */       endCrystal.setInvulnerable(config.isCrystalInvulnerable());
/* 111 */       endCrystal.snapTo(spike.getCenterX() + 0.5D, (spike.getHeight() + 1), spike.getCenterZ() + 0.5D, random.nextFloat() * 360.0F, 0.0F);
/* 112 */       level.addFreshEntity((Entity)endCrystal);
/*     */       
/* 114 */       BlockPos crystalPos = endCrystal.blockPosition();
/* 115 */       setBlock((LevelWriter)level, crystalPos.below(), Blocks.BEDROCK.defaultBlockState());
/* 116 */       setBlock((LevelWriter)level, crystalPos, FireBlock.getState((BlockGetter)level, crystalPos));
/*     */     } 
/*     */   }
/*     */   public static class EndSpike { public static final Codec<EndSpike> CODEC; private final int centerX; private final int centerZ;
/*     */     static {
/* 121 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("centerX").orElse(0).forGetter(()), (App)Codec.INT.fieldOf("centerZ").orElse(0).forGetter(()), (App)Codec.INT.fieldOf("radius").orElse(0).forGetter(()), (App)Codec.INT.fieldOf("height").orElse(0).forGetter(()), (App)Codec.BOOL.fieldOf("guarded").orElse(false).forGetter(())).apply((Applicative)i, EndSpike::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final int radius;
/*     */ 
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final boolean guarded;
/*     */     
/*     */     private final AABB topBoundingBox;
/*     */ 
/*     */     
/*     */     public EndSpike(int centerX, int centerZ, int radius, int height, boolean guarded) {
/* 137 */       this.centerX = centerX;
/* 138 */       this.centerZ = centerZ;
/* 139 */       this.radius = radius;
/* 140 */       this.height = height;
/* 141 */       this.guarded = guarded;
/*     */       
/* 143 */       this.topBoundingBox = new AABB((centerX - radius), DimensionType.MIN_Y, (centerZ - radius), (centerX + radius), DimensionType.MAX_Y, (centerZ + radius));
/*     */     }
/*     */     
/*     */     public boolean isCenterWithinChunk(BlockPos chunkOrigin) {
/* 147 */       return (SectionPos.blockToSectionCoord(chunkOrigin.getX()) == SectionPos.blockToSectionCoord(this.centerX) && 
/* 148 */         SectionPos.blockToSectionCoord(chunkOrigin.getZ()) == SectionPos.blockToSectionCoord(this.centerZ));
/*     */     }
/*     */     
/*     */     public int getCenterX() {
/* 152 */       return this.centerX;
/*     */     }
/*     */     
/*     */     public int getCenterZ() {
/* 156 */       return this.centerZ;
/*     */     }
/*     */     
/*     */     public int getRadius() {
/* 160 */       return this.radius;
/*     */     }
/*     */     
/*     */     public int getHeight() {
/* 164 */       return this.height;
/*     */     }
/*     */     
/*     */     public boolean isGuarded() {
/* 168 */       return this.guarded;
/*     */     }
/*     */     
/*     */     public AABB getTopBoundingBox() {
/* 172 */       return this.topBoundingBox;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class SpikeCacheLoader
/*     */     extends CacheLoader<Long, List<EndSpike>> {
/*     */     public List<SpikeFeature.EndSpike> load(Long seed) {
/* 179 */       IntArrayList sizes = Util.toShuffledList(java.util.stream.IntStream.range(0, 10), RandomSource.create(seed));
/*     */       
/* 181 */       List<SpikeFeature.EndSpike> result = com.google.common.collect.Lists.newArrayList();
/* 182 */       for (int i = 0; i < 10; i++) {
/* 183 */         int x = Mth.floor(42.0D * Math.cos(2.0D * (-3.141592653589793D + 0.3141592653589793D * i)));
/* 184 */         int z = Mth.floor(42.0D * Math.sin(2.0D * (-3.141592653589793D + 0.3141592653589793D * i)));
/* 185 */         int size = sizes.get(i);
/* 186 */         int radius = 2 + size / 3;
/* 187 */         int height = 76 + size * 3;
/* 188 */         boolean guarded = (size == 1 || size == 2);
/* 189 */         result.add(new SpikeFeature.EndSpike(x, z, radius, height, guarded));
/*     */       } 
/* 191 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/SpikeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */