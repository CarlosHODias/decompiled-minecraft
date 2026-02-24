/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ 
/*     */ public class DebugLevelSource extends ChunkGenerator {
/*     */   public static final MapCodec<DebugLevelSource> CODEC;
/*     */   
/*     */   static {
/*  36 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryOps.retrieveElement(Biomes.PLAINS)).apply((Applicative)i, i.stable(DebugLevelSource::new)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     ALL_BLOCKS = (List<BlockState>)java.util.stream.StreamSupport.stream(net.minecraft.core.registries.BuiltInRegistries.BLOCK.spliterator(), false).flatMap(b -> b.getStateDefinition().getPossibleStates().stream()).collect(java.util.stream.Collectors.toList());
/*  42 */   } private static final int BLOCK_MARGIN = 2; private static final List<BlockState> ALL_BLOCKS; private static final int GRID_WIDTH = Mth.ceil(Mth.sqrt(ALL_BLOCKS.size()));
/*  43 */   private static final int GRID_HEIGHT = Mth.ceil(ALL_BLOCKS.size() / GRID_WIDTH);
/*     */   
/*  45 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  46 */   protected static final BlockState BARRIER = Blocks.BARRIER.defaultBlockState();
/*     */   
/*     */   public static final int HEIGHT = 70;
/*     */   public static final int BARRIER_HEIGHT = 60;
/*     */   
/*     */   public DebugLevelSource(Holder.Reference<Biome> plains) {
/*  52 */     super((net.minecraft.world.level.biome.BiomeSource)new FixedBiomeSource((Holder)plains));
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends ChunkGenerator> codec() {
/*  57 */     return (MapCodec)CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {}
/*     */ 
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
/*  66 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */     
/*  68 */     ChunkPos centerPos = chunk.getPos();
/*  69 */     int chunkX = centerPos.x;
/*  70 */     int chunkZ = centerPos.z;
/*     */     
/*  72 */     for (int x = 0; x < 16; x++) {
/*  73 */       for (int z = 0; z < 16; z++) {
/*  74 */         int worldX = SectionPos.sectionToBlockCoord(chunkX, x);
/*  75 */         int worldZ = SectionPos.sectionToBlockCoord(chunkZ, z);
/*  76 */         level.setBlock((BlockPos)blockPos.set(worldX, 60, worldZ), BARRIER, 2);
/*  77 */         BlockState state = getBlockStateFor(worldX, worldZ);
/*  78 */         level.setBlock((BlockPos)blockPos.set(worldX, 70, worldZ), state, 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) {
/*  85 */     return CompletableFuture.completedFuture(centerChunk);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
/*  90 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
/*  95 */     return new NoiseColumn(0, new BlockState[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {}
/*     */ 
/*     */   
/*     */   public static BlockState getBlockStateFor(int worldX, int worldZ) {
/* 103 */     BlockState state = AIR;
/*     */     
/* 105 */     if (worldX > 0 && worldZ > 0 && worldX % 2 != 0 && worldZ % 2 != 0) {
/* 106 */       worldX /= 2;
/* 107 */       worldZ /= 2;
/*     */       
/* 109 */       if (worldX <= GRID_WIDTH && worldZ <= GRID_HEIGHT) {
/* 110 */         int index = Mth.abs(worldX * GRID_WIDTH + worldZ);
/* 111 */         if (index < ALL_BLOCKS.size()) {
/* 112 */           state = ALL_BLOCKS.get(index);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {}
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 130 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGenDepth() {
/* 135 */     return 384;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 140 */     return 63;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/DebugLevelSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */