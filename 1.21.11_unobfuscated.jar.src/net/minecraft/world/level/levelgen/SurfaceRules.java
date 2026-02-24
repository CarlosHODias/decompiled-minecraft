/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SurfaceRules
/*     */ {
/*     */   protected static final class Context
/*     */   {
/*     */     private static final int HOW_FAR_BELOW_PRELIMINARY_SURFACE_LEVEL_TO_BUILD_SURFACE = 8;
/*     */     private static final int SURFACE_CELL_BITS = 4;
/*     */     private static final int SURFACE_CELL_SIZE = 16;
/*     */     private static final int SURFACE_CELL_MASK = 15;
/*     */     private final SurfaceSystem system;
/*  49 */     private final SurfaceRules.Condition temperature = new TemperatureHelperCondition(this);
/*  50 */     private final SurfaceRules.Condition steep = new SteepMaterialCondition(this);
/*  51 */     private final SurfaceRules.Condition hole = new HoleCondition(this);
/*  52 */     private final SurfaceRules.Condition abovePreliminarySurface = new AbovePreliminarySurfaceCondition();
/*     */     
/*     */     private final RandomState randomState;
/*     */     
/*     */     private final ChunkAccess chunk;
/*     */     private final NoiseChunk noiseChunk;
/*     */     private final Function<BlockPos, Holder<Biome>> biomeGetter;
/*     */     private final WorldGenerationContext context;
/*  60 */     private long lastPreliminarySurfaceCellOrigin = Long.MAX_VALUE;
/*  61 */     private final int[] preliminarySurfaceCache = new int[4];
/*     */ 
/*     */     
/*  64 */     private long lastUpdateXZ = -9223372036854775807L;
/*     */     
/*     */     private int blockX;
/*     */     
/*     */     private int blockZ;
/*     */     private int surfaceDepth;
/*  70 */     private long lastSurfaceDepth2Update = this.lastUpdateXZ - 1L;
/*     */     
/*     */     private double surfaceSecondary;
/*  73 */     private long lastMinSurfaceLevelUpdate = this.lastUpdateXZ - 1L;
/*     */     
/*     */     private int minSurfaceLevel;
/*     */     
/*  77 */     private long lastUpdateY = -9223372036854775807L;
/*  78 */     private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */     private Supplier<Holder<Biome>> biome;
/*     */     private int blockY;
/*     */     private int waterHeight;
/*     */     private int stoneDepthBelow;
/*     */     private int stoneDepthAbove;
/*     */     
/*     */     protected Context(SurfaceSystem system, RandomState randomState, ChunkAccess chunk, NoiseChunk noiseChunk, Function<BlockPos, Holder<Biome>> biomeGetter, Registry<Biome> biomes, WorldGenerationContext context) {
/*  86 */       this.system = system;
/*  87 */       this.randomState = randomState;
/*  88 */       this.chunk = chunk;
/*  89 */       this.noiseChunk = noiseChunk;
/*  90 */       this.biomeGetter = biomeGetter;
/*  91 */       this.context = context;
/*     */     }
/*     */     
/*     */     protected void updateXZ(int blockX, int blockZ) {
/*  95 */       this.lastUpdateXZ++;
/*  96 */       this.lastUpdateY++;
/*  97 */       this.blockX = blockX;
/*  98 */       this.blockZ = blockZ;
/*  99 */       this.surfaceDepth = this.system.getSurfaceDepth(blockX, blockZ);
/*     */     }
/*     */     
/*     */     protected void updateY(int stoneDepthAbove, int stoneDepthBelow, int waterHeight, int blockX, int blockY, int blockZ) {
/* 103 */       this.lastUpdateY++;
/* 104 */       this.biome = (Supplier<Holder<Biome>>)Suppliers.memoize(() -> (Holder)this.biomeGetter.apply(this.pos.set(blockX, blockY, blockZ)));
/* 105 */       this.blockY = blockY;
/* 106 */       this.waterHeight = waterHeight;
/* 107 */       this.stoneDepthBelow = stoneDepthBelow;
/* 108 */       this.stoneDepthAbove = stoneDepthAbove;
/*     */     }
/*     */     
/*     */     protected double getSurfaceSecondary() {
/* 112 */       if (this.lastSurfaceDepth2Update != this.lastUpdateXZ) {
/* 113 */         this.lastSurfaceDepth2Update = this.lastUpdateXZ;
/* 114 */         this.surfaceSecondary = this.system.getSurfaceSecondary(this.blockX, this.blockZ);
/*     */       } 
/* 116 */       return this.surfaceSecondary;
/*     */     }
/*     */     
/*     */     public int getSeaLevel() {
/* 120 */       return this.system.getSeaLevel();
/*     */     }
/*     */     
/*     */     private static int blockCoordToSurfaceCell(int blockCoord) {
/* 124 */       return blockCoord >> 4;
/*     */     }
/*     */     
/*     */     private static int surfaceCellToBlockCoord(int cellCoord) {
/* 128 */       return cellCoord << 4;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getMinSurfaceLevel() {
/* 133 */       if (this.lastMinSurfaceLevelUpdate != this.lastUpdateXZ) {
/* 134 */         this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ;
/* 135 */         int cornerCellX = blockCoordToSurfaceCell(this.blockX);
/* 136 */         int cornerCellZ = blockCoordToSurfaceCell(this.blockZ);
/*     */         
/* 138 */         long preliminarySurfaceCellOrigin = ChunkPos.asLong(cornerCellX, cornerCellZ);
/* 139 */         if (this.lastPreliminarySurfaceCellOrigin != preliminarySurfaceCellOrigin) {
/* 140 */           this.lastPreliminarySurfaceCellOrigin = preliminarySurfaceCellOrigin;
/*     */           
/* 142 */           this.preliminarySurfaceCache[0] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ));
/* 143 */           this.preliminarySurfaceCache[1] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ));
/* 144 */           this.preliminarySurfaceCache[2] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ + 1));
/* 145 */           this.preliminarySurfaceCache[3] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ + 1));
/*     */         } 
/* 147 */         int preliminarySurfaceLevel = Mth.floor(Mth.lerp2(((this.blockX & 0xF) / 16.0F), ((this.blockZ & 0xF) / 16.0F), this.preliminarySurfaceCache[0], this.preliminarySurfaceCache[1], this.preliminarySurfaceCache[2], this.preliminarySurfaceCache[3]));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 155 */         this.minSurfaceLevel = preliminarySurfaceLevel + this.surfaceDepth - 8;
/*     */       } 
/* 157 */       return this.minSurfaceLevel;
/*     */     }
/*     */     
/*     */     private static final class HoleCondition extends SurfaceRules.LazyXZCondition {
/*     */       private HoleCondition(SurfaceRules.Context context) {
/* 162 */         super(context);
/*     */       }
/*     */ 
/*     */       
/*     */       protected boolean compute() {
/* 167 */         return (this.context.surfaceDepth <= 0);
/*     */       }
/*     */     }
/*     */     
/*     */     private final class AbovePreliminarySurfaceCondition
/*     */       implements SurfaceRules.Condition {
/*     */       public boolean test() {
/* 174 */         return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel());
/*     */       }
/*     */     }
/*     */     
/*     */     private static class TemperatureHelperCondition extends SurfaceRules.LazyYCondition {
/*     */       private TemperatureHelperCondition(SurfaceRules.Context context) {
/* 180 */         super(context);
/*     */       }
/*     */ 
/*     */       
/*     */       protected boolean compute() {
/* 185 */         return ((Biome)((Holder)this.context.biome.get()).value()).coldEnoughToSnow((BlockPos)this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel());
/*     */       }
/*     */     }
/*     */     
/*     */     private static class SteepMaterialCondition extends SurfaceRules.LazyXZCondition {
/*     */       private SteepMaterialCondition(SurfaceRules.Context context) {
/* 191 */         super(context);
/*     */       }
/*     */       
/*     */       protected boolean compute()
/*     */       {
/* 196 */         int chunkBlockX = this.context.blockX & 0xF;
/* 197 */         int chunkBlockZ = this.context.blockZ & 0xF;
/*     */         
/* 199 */         int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 200 */         int zSouth = Math.min(chunkBlockZ + 1, 15);
/*     */         
/* 202 */         ChunkAccess chunk = this.context.chunk;
/* 203 */         int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 204 */         int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/*     */         
/* 206 */         if (heightSouth >= heightNorth + 4) {
/* 207 */           return true;
/*     */         }
/*     */         
/* 210 */         int xWest = Math.max(chunkBlockX - 1, 0);
/* 211 */         int xEast = Math.min(chunkBlockX + 1, 15);
/* 212 */         int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 213 */         int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/*     */         
/* 215 */         return (heightWest >= heightEast + 4); } } } private static final class HoleCondition extends LazyXZCondition { private HoleCondition(SurfaceRules.Context context) { super(context); } protected boolean compute() { return (this.context.surfaceDepth <= 0); } } private final class AbovePreliminarySurfaceCondition implements Condition { public boolean test() { return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel()); } } private static class TemperatureHelperCondition extends LazyYCondition { private TemperatureHelperCondition(SurfaceRules.Context context) { super(context); } protected boolean compute() { return ((Biome)((Holder)this.context.biome.get()).value()).coldEnoughToSnow((BlockPos)this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel()); } } private static class SteepMaterialCondition extends LazyXZCondition { protected boolean compute() { int chunkBlockX = this.context.blockX & 0xF; int chunkBlockZ = this.context.blockZ & 0xF; int zNorth = Math.max(chunkBlockZ - 1, 0); int zSouth = Math.min(chunkBlockZ + 1, 15); ChunkAccess chunk = this.context.chunk; int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth); int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth); if (heightSouth >= heightNorth + 4) return true;  int xWest = Math.max(chunkBlockX - 1, 0); int xEast = Math.min(chunkBlockX + 1, 15); int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ); int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ); return (heightWest >= heightEast + 4); }
/*     */ 
/*     */ 
/*     */     
/*     */     private SteepMaterialCondition(SurfaceRules.Context context) {
/*     */       super(context);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static abstract class LazyCondition
/*     */     implements Condition
/*     */   {
/*     */     protected final SurfaceRules.Context context;
/*     */     private long lastUpdate;
/*     */     Boolean result;
/*     */     
/*     */     protected LazyCondition(SurfaceRules.Context context) {
/* 232 */       this.context = context;
/* 233 */       this.lastUpdate = getContextLastUpdate() - 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test() {
/* 238 */       long lastContextUpdate = getContextLastUpdate();
/*     */       
/* 240 */       if (lastContextUpdate == this.lastUpdate) {
/* 241 */         if (this.result == null) {
/* 242 */           throw new IllegalStateException("Update triggered but the result is null");
/*     */         }
/* 244 */         return this.result;
/*     */       } 
/* 246 */       this.lastUpdate = lastContextUpdate;
/*     */       
/* 248 */       this.result = compute();
/*     */       
/* 250 */       return this.result;
/*     */     }
/*     */     
/*     */     protected abstract long getContextLastUpdate();
/*     */     
/*     */     protected abstract boolean compute();
/*     */   }
/*     */   
/*     */   private static abstract class LazyXZCondition extends LazyCondition {
/*     */     protected LazyXZCondition(SurfaceRules.Context context) {
/* 260 */       super(context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected long getContextLastUpdate() {
/* 265 */       return this.context.lastUpdateXZ;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class LazyYCondition extends LazyCondition {
/*     */     protected LazyYCondition(SurfaceRules.Context context) {
/* 271 */       super(context);
/*     */     }
/*     */ 
/*     */     
/*     */     protected long getContextLastUpdate() {
/* 276 */       return this.context.lastUpdateY;
/*     */     } }
/*     */   private static final class NotCondition extends Record implements Condition { private final SurfaceRules.Condition target;
/*     */     
/* 280 */     private NotCondition(SurfaceRules.Condition target) { this.target = target; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;
/* 280 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.Condition target() { return this.target; }
/*     */     
/*     */     public boolean test() {
/* 283 */       return !this.target.test();
/*     */     } }
/*     */ 
/*     */   
/* 287 */   public static final ConditionSource ON_FLOOR = stoneDepthCheck(0, false, CaveSurface.FLOOR);
/* 288 */   public static final ConditionSource UNDER_FLOOR = stoneDepthCheck(0, true, CaveSurface.FLOOR);
/* 289 */   public static final ConditionSource DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 6, CaveSurface.FLOOR);
/* 290 */   public static final ConditionSource VERY_DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 30, CaveSurface.FLOOR);
/*     */   
/* 292 */   public static final ConditionSource ON_CEILING = stoneDepthCheck(0, false, CaveSurface.CEILING);
/* 293 */   public static final ConditionSource UNDER_CEILING = stoneDepthCheck(0, true, CaveSurface.CEILING);
/*     */   
/*     */   public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth1, CaveSurface surfaceType) {
/* 296 */     return new StoneDepthCheck(offset, addSurfaceDepth1, 0, surfaceType);
/*     */   }
/*     */   
/*     */   public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth1, int secondaryDepthRange, CaveSurface surfaceType) {
/* 300 */     return new StoneDepthCheck(offset, addSurfaceDepth1, secondaryDepthRange, surfaceType);
/*     */   }
/*     */   
/*     */   public static ConditionSource not(ConditionSource target) {
/* 304 */     return new NotConditionSource(target);
/*     */   }
/*     */   
/*     */   public static ConditionSource yBlockCheck(VerticalAnchor anchor, int surfaceDepthMultiplier) {
/* 308 */     return new YConditionSource(anchor, surfaceDepthMultiplier, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConditionSource yStartCheck(VerticalAnchor anchor, int surfaceDepthMultiplier) {
/* 315 */     return new YConditionSource(anchor, surfaceDepthMultiplier, true);
/*     */   }
/*     */   
/*     */   public static ConditionSource waterBlockCheck(int offset, int surfaceDepthMultiplier) {
/* 319 */     return new WaterConditionSource(offset, surfaceDepthMultiplier, false);
/*     */   }
/*     */   
/*     */   public static ConditionSource waterStartCheck(int offset, int surfaceDepthMultiplier) {
/* 323 */     return new WaterConditionSource(offset, surfaceDepthMultiplier, true);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static ConditionSource isBiome(ResourceKey<Biome>... target) {
/* 328 */     return isBiome(List.of(target));
/*     */   }
/*     */   
/*     */   private static BiomeConditionSource isBiome(List<ResourceKey<Biome>> target) {
/* 332 */     return new BiomeConditionSource(target);
/*     */   }
/*     */   
/*     */   public static ConditionSource noiseCondition(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) {
/* 336 */     return noiseCondition(noise, minRange, Double.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public static ConditionSource noiseCondition(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
/* 340 */     return new NoiseThresholdConditionSource(noise, minRange, maxRange);
/*     */   }
/*     */   
/*     */   public static ConditionSource verticalGradient(String randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) {
/* 344 */     return new VerticalGradientConditionSource(Identifier.parse(randomName), trueAtAndBelow, falseAtAndAbove);
/*     */   }
/*     */   
/*     */   public static ConditionSource steep() {
/* 348 */     return Steep.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ConditionSource hole() {
/* 352 */     return Hole.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ConditionSource abovePreliminarySurface() {
/* 356 */     return AbovePreliminarySurface.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ConditionSource temperature() {
/* 360 */     return Temperature.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class StateRule extends Record implements SurfaceRule
/*     */   {
/*     */     private final BlockState state;
/*     */     
/* 367 */     private StateRule(BlockState state) { this.state = state; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;
/* 367 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState state() { return this.state; }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 370 */       return this.state;
/*     */     } }
/*     */   private static final class TestRule extends Record implements SurfaceRule { private final SurfaceRules.Condition condition; private final SurfaceRules.SurfaceRule followup;
/*     */     
/* 374 */     private TestRule(SurfaceRules.Condition condition, SurfaceRules.SurfaceRule followup) { this.condition = condition; this.followup = followup; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/* 374 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.Condition condition() { return this.condition; } public SurfaceRules.SurfaceRule followup() { return this.followup; }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 377 */       if (!this.condition.test()) {
/* 378 */         return null;
/*     */       }
/* 380 */       return this.followup.tryApply(blockX, blockY, blockZ);
/*     */     } }
/*     */   private static final class SequenceRule extends Record implements SurfaceRule { private final List<SurfaceRules.SurfaceRule> rules;
/*     */     
/* 384 */     private SequenceRule(List<SurfaceRules.SurfaceRule> rules) { this.rules = rules; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #384	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #384	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #384	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/* 384 */       //   0	8	1	o	Ljava/lang/Object; } public List<SurfaceRules.SurfaceRule> rules() { return this.rules; }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 387 */       for (SurfaceRules.SurfaceRule rule : this.rules) {
/* 388 */         BlockState state = rule.tryApply(blockX, blockY, blockZ);
/* 389 */         if (state != null) {
/* 390 */           return state;
/*     */         }
/*     */       } 
/* 393 */       return null;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static RuleSource ifTrue(ConditionSource condition, RuleSource next) {
/* 398 */     return new TestRuleSource(condition, next);
/*     */   }
/*     */   
/*     */   public static RuleSource sequence(RuleSource... rules) {
/* 402 */     if (rules.length == 0) {
/* 403 */       throw new IllegalArgumentException("Need at least 1 rule for a sequence");
/*     */     }
/* 405 */     return new SequenceRuleSource(Arrays.asList(rules));
/*     */   }
/*     */   
/*     */   public static RuleSource state(BlockState state) {
/* 409 */     return new BlockRuleSource(state);
/*     */   }
/*     */   
/*     */   public static RuleSource bandlands() {
/* 413 */     return Bandlands.INSTANCE;
/*     */   }
/*     */   
/*     */   private static <A> MapCodec<? extends A> register(Registry<MapCodec<? extends A>> registry, String name, KeyDispatchDataCodec<? extends A> codec) {
/* 417 */     return (MapCodec<? extends A>)Registry.register(registry, name, codec.codec());
/*     */   }
/*     */   public static interface ConditionSource extends Function<Context, Condition> { public static final Codec<ConditionSource> CODEC;
/*     */     static {
/* 421 */       CODEC = BuiltInRegistries.MATERIAL_CONDITION.byNameCodec().dispatch(source -> source.codec().codec(), Function.identity());
/*     */     } KeyDispatchDataCodec<? extends ConditionSource> codec();
/*     */     static MapCodec<? extends ConditionSource> bootstrap(Registry<MapCodec<? extends ConditionSource>> registry) {
/* 424 */       SurfaceRules.register(registry, "biome", (KeyDispatchDataCodec)SurfaceRules.BiomeConditionSource.CODEC);
/* 425 */       SurfaceRules.register(registry, "noise_threshold", (KeyDispatchDataCodec)SurfaceRules.NoiseThresholdConditionSource.CODEC);
/* 426 */       SurfaceRules.register(registry, "vertical_gradient", (KeyDispatchDataCodec)SurfaceRules.VerticalGradientConditionSource.CODEC);
/* 427 */       SurfaceRules.register(registry, "y_above", (KeyDispatchDataCodec)SurfaceRules.YConditionSource.CODEC);
/* 428 */       SurfaceRules.register(registry, "water", (KeyDispatchDataCodec)SurfaceRules.WaterConditionSource.CODEC);
/* 429 */       SurfaceRules.register(registry, "temperature", (KeyDispatchDataCodec)SurfaceRules.Temperature.CODEC);
/* 430 */       SurfaceRules.register(registry, "steep", (KeyDispatchDataCodec)SurfaceRules.Steep.CODEC);
/* 431 */       SurfaceRules.register(registry, "not", (KeyDispatchDataCodec)SurfaceRules.NotConditionSource.CODEC);
/* 432 */       SurfaceRules.register(registry, "hole", (KeyDispatchDataCodec)SurfaceRules.Hole.CODEC);
/* 433 */       SurfaceRules.register(registry, "above_preliminary_surface", (KeyDispatchDataCodec)SurfaceRules.AbovePreliminarySurface.CODEC);
/* 434 */       return SurfaceRules.register(registry, "stone_depth", (KeyDispatchDataCodec)SurfaceRules.StoneDepthCheck.CODEC);
/*     */     } }
/*     */   
/*     */   public static interface RuleSource extends Function<Context, SurfaceRule> {
/*     */     public static final Codec<RuleSource> CODEC;
/*     */     
/*     */     static {
/* 441 */       CODEC = BuiltInRegistries.MATERIAL_RULE.byNameCodec().dispatch(source -> source.codec().codec(), Function.identity());
/*     */     } KeyDispatchDataCodec<? extends RuleSource> codec();
/*     */     static MapCodec<? extends RuleSource> bootstrap(Registry<MapCodec<? extends RuleSource>> registry) {
/* 444 */       SurfaceRules.register(registry, "bandlands", (KeyDispatchDataCodec)SurfaceRules.Bandlands.CODEC);
/* 445 */       SurfaceRules.register(registry, "block", (KeyDispatchDataCodec)SurfaceRules.BlockRuleSource.CODEC);
/* 446 */       SurfaceRules.register(registry, "sequence", (KeyDispatchDataCodec)SurfaceRules.SequenceRuleSource.CODEC);
/* 447 */       return SurfaceRules.register(registry, "condition", (KeyDispatchDataCodec)SurfaceRules.TestRuleSource.CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NotConditionSource extends Record implements ConditionSource { private final SurfaceRules.ConditionSource target;
/*     */     
/* 453 */     private NotConditionSource(SurfaceRules.ConditionSource target) { this.target = target; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #453	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #453	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #453	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/* 453 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.ConditionSource target() { return this.target; }
/* 454 */      private static final KeyDispatchDataCodec<NotConditionSource> CODEC = KeyDispatchDataCodec.of(SurfaceRules.ConditionSource.CODEC.xmap(NotConditionSource::new, NotConditionSource::target).fieldOf("invert"));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 458 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 463 */       return new SurfaceRules.NotCondition(this.target.apply(context));
/*     */     } }
/*     */   private static final class StoneDepthCheck extends Record implements ConditionSource { private final int offset; private final boolean addSurfaceDepth; private final int secondaryDepthRange; private final CaveSurface surfaceType; private static final KeyDispatchDataCodec<StoneDepthCheck> CODEC;
/*     */     
/* 467 */     private StoneDepthCheck(int offset, boolean addSurfaceDepth, int secondaryDepthRange, CaveSurface surfaceType) { this.offset = offset; this.addSurfaceDepth = addSurfaceDepth; this.secondaryDepthRange = secondaryDepthRange; this.surfaceType = surfaceType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #467	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #467	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #467	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/* 467 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; } public boolean addSurfaceDepth() { return this.addSurfaceDepth; } public int secondaryDepthRange() { return this.secondaryDepthRange; } public CaveSurface surfaceType() { return this.surfaceType; } static {
/* 468 */       CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("offset").forGetter(StoneDepthCheck::offset), (App)Codec.BOOL.fieldOf("add_surface_depth").forGetter(StoneDepthCheck::addSurfaceDepth), (App)Codec.INT.fieldOf("secondary_depth_range").forGetter(StoneDepthCheck::secondaryDepthRange), (App)CaveSurface.CODEC.fieldOf("surface_type").forGetter(StoneDepthCheck::surfaceType)).apply((Applicative)i, StoneDepthCheck::new)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 478 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext)
/*     */     {
/* 483 */       final boolean ceiling = (this.surfaceType == CaveSurface.CEILING);
/*     */       class StoneDepthCondition
/*     */         extends SurfaceRules.LazyYCondition {
/*     */         private StoneDepthCondition() {
/* 487 */           super(param2Context);
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 492 */           int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/* 493 */           int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/* 494 */           int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/*     */           
/* 496 */           return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth);
/*     */         }
/*     */       };
/*     */       
/* 500 */       return new StoneDepthCondition();
/*     */     } }
/*     */    class StoneDepthCondition extends LazyYCondition { private StoneDepthCondition() { super(param1Context); } protected boolean compute() { int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove; int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/*     */       int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/*     */       return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth); } }
/* 505 */   private enum AbovePreliminarySurface implements ConditionSource { INSTANCE;
/* 506 */     private static final KeyDispatchDataCodec<AbovePreliminarySurface> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 510 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 515 */       return context.abovePreliminarySurface;
/*     */     } }
/*     */ 
/*     */   
/*     */   private enum Hole implements ConditionSource {
/* 520 */     INSTANCE;
/* 521 */     private static final KeyDispatchDataCodec<Hole> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 525 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 530 */       return context.hole;
/*     */     } }
/*     */   private static final class YConditionSource extends Record implements ConditionSource { private final VerticalAnchor anchor; private final int surfaceDepthMultiplier; private final boolean addStoneDepth; private static final KeyDispatchDataCodec<YConditionSource> CODEC;
/*     */     
/* 534 */     private YConditionSource(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) { this.anchor = anchor; this.surfaceDepthMultiplier = surfaceDepthMultiplier; this.addStoneDepth = addStoneDepth; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #534	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #534	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #534	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/* 534 */       //   0	8	1	o	Ljava/lang/Object; } public VerticalAnchor anchor() { return this.anchor; } public int surfaceDepthMultiplier() { return this.surfaceDepthMultiplier; } public boolean addStoneDepth() { return this.addStoneDepth; } static {
/* 535 */       CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("anchor").forGetter(YConditionSource::anchor), (App)Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(YConditionSource::surfaceDepthMultiplier), (App)Codec.BOOL.fieldOf("add_stone_depth").forGetter(YConditionSource::addStoneDepth)).apply((Applicative)i, YConditionSource::new)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 543 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class YCondition
/*     */         extends SurfaceRules.LazyYCondition {
/*     */         private YCondition() {
/* 550 */           super(param2Context);
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 555 */           return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier);
/*     */         }
/*     */       };
/*     */       
/* 559 */       return new YCondition(); } }
/*     */    class YCondition extends LazyYCondition { private YCondition() { super(param1Context); }
/*     */     protected boolean compute() { return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier); } }
/*     */   private static final class WaterConditionSource extends Record implements ConditionSource { private final int offset; private final int surfaceDepthMultiplier; private final boolean addStoneDepth; private static final KeyDispatchDataCodec<WaterConditionSource> CODEC;
/* 563 */     private WaterConditionSource(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) { this.offset = offset; this.surfaceDepthMultiplier = surfaceDepthMultiplier; this.addStoneDepth = addStoneDepth; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/* 563 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; } public int surfaceDepthMultiplier() { return this.surfaceDepthMultiplier; } public boolean addStoneDepth() { return this.addStoneDepth; } static {
/* 564 */       CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("offset").forGetter(WaterConditionSource::offset), (App)Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(WaterConditionSource::surfaceDepthMultiplier), (App)Codec.BOOL.fieldOf("add_stone_depth").forGetter(WaterConditionSource::addStoneDepth)).apply((Applicative)i, WaterConditionSource::new)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 572 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class WaterCondition
/*     */         extends SurfaceRules.LazyYCondition {
/*     */         private WaterCondition() {
/* 579 */           super(param2Context);
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 584 */           if (this.context.waterHeight != Integer.MIN_VALUE) { if (this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier); return false; }
/*     */         
/*     */         }
/*     */       };
/* 588 */       return new WaterCondition(); } } class WaterCondition extends LazyYCondition { private WaterCondition() { super(param1Context); } protected boolean compute() { if (this.context.waterHeight != Integer.MIN_VALUE) {
/*     */         if (this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier); return false;
/*     */       }  } }
/*     */   private static final class BiomeConditionSource implements ConditionSource {
/*     */     private static final KeyDispatchDataCodec<BiomeConditionSource> CODEC; private final List<ResourceKey<Biome>> biomes; private final Predicate<ResourceKey<Biome>> biomeNameTest;
/* 593 */     static { CODEC = KeyDispatchDataCodec.of(ResourceKey.codec(Registries.BIOME).listOf().fieldOf("biome_is").xmap(SurfaceRules::isBiome, e -> e.biomes)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BiomeConditionSource(List<ResourceKey<Biome>> biomes) {
/* 599 */       this.biomes = biomes;
/* 600 */       Objects.requireNonNull(Set.copyOf(biomes)); this.biomeNameTest = Set.copyOf(biomes)::contains;
/*     */     }
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 605 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class BiomeCondition
/*     */         extends SurfaceRules.LazyYCondition {
/*     */         private BiomeCondition() {
/* 612 */           super(param2Context);
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 617 */           return ((Holder)this.context.biome.get()).is(SurfaceRules.BiomeConditionSource.this.biomeNameTest);
/*     */         }
/*     */       };
/*     */       
/* 621 */       return new BiomeCondition();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 626 */       if (this == o) {
/* 627 */         return true;
/*     */       }
/* 629 */       if (o instanceof BiomeConditionSource) { BiomeConditionSource that = (BiomeConditionSource)o;
/* 630 */         return this.biomes.equals(that.biomes); }
/*     */       
/* 632 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 637 */       return this.biomes.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 642 */       return "BiomeConditionSource[biomes=" + String.valueOf(this.biomes) + "]";
/*     */     }
/*     */   } class BiomeCondition extends LazyYCondition { private BiomeCondition() { super(param1Context); } protected boolean compute() { return ((Holder)this.context.biome.get()).is(SurfaceRules.BiomeConditionSource.this.biomeNameTest); } }
/*     */   private static final class NoiseThresholdConditionSource extends Record implements ConditionSource { private final ResourceKey<NormalNoise.NoiseParameters> noise; private final double minThreshold; private final double maxThreshold; private static final KeyDispatchDataCodec<NoiseThresholdConditionSource> CODEC;
/* 646 */     private NoiseThresholdConditionSource(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold) { this.noise = noise; this.minThreshold = minThreshold; this.maxThreshold = maxThreshold; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #646	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #646	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #646	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/* 646 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<NormalNoise.NoiseParameters> noise() { return this.noise; } public double minThreshold() { return this.minThreshold; } public double maxThreshold() { return this.maxThreshold; } static {
/* 647 */       CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseThresholdConditionSource::noise), (App)Codec.DOUBLE.fieldOf("min_threshold").forGetter(NoiseThresholdConditionSource::minThreshold), (App)Codec.DOUBLE.fieldOf("max_threshold").forGetter(NoiseThresholdConditionSource::maxThreshold)).apply((Applicative)i, NoiseThresholdConditionSource::new)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 656 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext)
/*     */     {
/* 661 */       final NormalNoise noise = ruleContext.randomState.getOrCreateNoise(this.noise);
/*     */       class NoiseThresholdCondition extends SurfaceRules.LazyXZCondition {
/*     */         private NoiseThresholdCondition() {
/* 664 */           super(param2Context);
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 669 */           double value = noise.getValue(this.context.blockX, 0.0D, this.context.blockZ);
/* 670 */           return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/*     */         }
/*     */       };
/*     */       
/* 674 */       return new NoiseThresholdCondition();
/*     */     } } class NoiseThresholdCondition extends LazyXZCondition { private NoiseThresholdCondition() { super(param1Context); } protected boolean compute() { double value = noise.getValue(this.context.blockX, 0.0D, this.context.blockZ);
/*     */       return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold); } } private static final class VerticalGradientConditionSource extends Record implements ConditionSource {
/*     */     private final Identifier randomName; private final VerticalAnchor trueAtAndBelow; private final VerticalAnchor falseAtAndAbove; private static final KeyDispatchDataCodec<VerticalGradientConditionSource> CODEC;
/* 678 */     private VerticalGradientConditionSource(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) { this.randomName = randomName; this.trueAtAndBelow = trueAtAndBelow; this.falseAtAndAbove = falseAtAndAbove; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #678	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #678	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #678	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/* 678 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier randomName() { return this.randomName; } public VerticalAnchor trueAtAndBelow() { return this.trueAtAndBelow; } public VerticalAnchor falseAtAndAbove() { return this.falseAtAndAbove; } static {
/* 679 */       CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("random_name").forGetter(VerticalGradientConditionSource::randomName), (App)VerticalAnchor.CODEC.fieldOf("true_at_and_below").forGetter(VerticalGradientConditionSource::trueAtAndBelow), (App)VerticalAnchor.CODEC.fieldOf("false_at_and_above").forGetter(VerticalGradientConditionSource::falseAtAndAbove)).apply((Applicative)i, VerticalGradientConditionSource::new)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 687 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext)
/*     */     {
/* 692 */       final int trueAtAndBelow = trueAtAndBelow().resolveY(ruleContext.context);
/* 693 */       final int falseAtAndAbove = falseAtAndAbove().resolveY(ruleContext.context);
/* 694 */       final PositionalRandomFactory randomFactory = ruleContext.randomState.getOrCreateRandomFactory(randomName());
/*     */       class VerticalGradientCondition
/*     */         extends SurfaceRules.LazyYCondition {
/*     */         private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) {
/* 698 */           super(param2Context);
/*     */         }
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 703 */           int blockY = this.context.blockY;
/* 704 */           if (blockY <= trueAtAndBelow) {
/* 705 */             return true;
/*     */           }
/* 707 */           if (blockY >= falseAtAndAbove) {
/* 708 */             return false;
/*     */           }
/* 710 */           double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D);
/* 711 */           RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/* 712 */           return (random.nextFloat() < probability);
/*     */         }
/*     */       };
/* 715 */       return new VerticalGradientCondition(this);
/*     */     }
/*     */   } class VerticalGradientCondition extends LazyYCondition { private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) { super(param1Context); } protected boolean compute() { int blockY = this.context.blockY; if (blockY <= trueAtAndBelow)
/*     */         return true;  if (blockY >= falseAtAndAbove)
/*     */         return false;  double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D); RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ); return (random.nextFloat() < probability); } }
/* 720 */   private enum Temperature implements ConditionSource { INSTANCE;
/* 721 */     private static final KeyDispatchDataCodec<Temperature> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 725 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 730 */       return context.temperature;
/*     */     } }
/*     */ 
/*     */   
/*     */   private enum Steep implements ConditionSource {
/* 735 */     INSTANCE;
/* 736 */     private static final KeyDispatchDataCodec<Steep> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
/* 740 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(SurfaceRules.Context context) {
/* 745 */       return context.steep;
/*     */     } }
/*     */   private static final class BlockRuleSource extends Record implements RuleSource { private final BlockState resultState; private final SurfaceRules.StateRule rule;
/*     */     
/* 749 */     private BlockRuleSource(BlockState resultState, SurfaceRules.StateRule rule) { this.resultState = resultState; this.rule = rule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/* 749 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState resultState() { return this.resultState; } public SurfaceRules.StateRule rule() { return this.rule; }
/* 750 */      private static final KeyDispatchDataCodec<BlockRuleSource> CODEC = KeyDispatchDataCodec.of(BlockState.CODEC.xmap(BlockRuleSource::new, BlockRuleSource::resultState).fieldOf("result_state"));
/*     */     
/*     */     private BlockRuleSource(BlockState state) {
/* 753 */       this(state, new SurfaceRules.StateRule(state));
/*     */     }
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
/* 758 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 763 */       return this.rule;
/*     */     } }
/*     */   private static final class TestRuleSource extends Record implements RuleSource { private final SurfaceRules.ConditionSource ifTrue; private final SurfaceRules.RuleSource thenRun; private static final KeyDispatchDataCodec<TestRuleSource> CODEC;
/*     */     
/* 767 */     private TestRuleSource(SurfaceRules.ConditionSource ifTrue, SurfaceRules.RuleSource thenRun) { this.ifTrue = ifTrue; this.thenRun = thenRun; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #767	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #767	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #767	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/* 767 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.ConditionSource ifTrue() { return this.ifTrue; } public SurfaceRules.RuleSource thenRun() { return this.thenRun; } static {
/* 768 */       CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)SurfaceRules.ConditionSource.CODEC.fieldOf("if_true").forGetter(TestRuleSource::ifTrue), (App)SurfaceRules.RuleSource.CODEC.fieldOf("then_run").forGetter(TestRuleSource::thenRun)).apply((Applicative)i, TestRuleSource::new)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
/* 775 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 780 */       return new SurfaceRules.TestRule(this.ifTrue.apply(context), this.thenRun.apply(context));
/*     */     } }
/*     */   private static final class SequenceRuleSource extends Record implements RuleSource { private final List<SurfaceRules.RuleSource> sequence;
/*     */     
/* 784 */     private SequenceRuleSource(List<SurfaceRules.RuleSource> sequence) { this.sequence = sequence; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #784	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #784	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #784	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/* 784 */       //   0	8	1	o	Ljava/lang/Object; } public List<SurfaceRules.RuleSource> sequence() { return this.sequence; }
/* 785 */      private static final KeyDispatchDataCodec<SequenceRuleSource> CODEC = KeyDispatchDataCodec.of(SurfaceRules.RuleSource.CODEC.listOf().xmap(SequenceRuleSource::new, SequenceRuleSource::sequence).fieldOf("sequence"));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
/* 789 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 794 */       if (this.sequence.size() == 1) {
/* 795 */         return ((SurfaceRules.RuleSource)this.sequence.get(0)).apply(context);
/*     */       }
/*     */       
/* 798 */       ImmutableList.Builder<SurfaceRules.SurfaceRule> builder = ImmutableList.builder();
/* 799 */       for (SurfaceRules.RuleSource rule : this.sequence) {
/* 800 */         builder.add(rule.apply(context));
/*     */       }
/* 802 */       return new SurfaceRules.SequenceRule((List<SurfaceRules.SurfaceRule>)builder.build());
/*     */     } }
/*     */ 
/*     */   
/*     */   private enum Bandlands implements RuleSource {
/* 807 */     INSTANCE;
/* 808 */     private static final KeyDispatchDataCodec<Bandlands> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */ 
/*     */     
/*     */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
/* 812 */       return (KeyDispatchDataCodec)CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 817 */       Objects.requireNonNull(context.system); return context.system::getBand;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static interface SurfaceRule {
/*     */     BlockState tryApply(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */   
/*     */   private static interface Condition {
/*     */     boolean test();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/SurfaceRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */