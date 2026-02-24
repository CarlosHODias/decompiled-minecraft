/*     */ package net.minecraft.world.level.levelgen.blending;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Direction8;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.data.worldgen.NoiseData;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import org.apache.commons.lang3.mutable.MutableDouble;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class Blender
/*     */ {
/*  39 */   private static final Blender EMPTY = new Blender(new Long2ObjectOpenHashMap(), new Long2ObjectOpenHashMap())
/*     */     {
/*     */       public Blender.BlendingOutput blendOffsetAndFactor(int blockX, int blockZ) {
/*  42 */         return new Blender.BlendingOutput(1.0D, 0.0D);
/*     */       }
/*     */ 
/*     */       
/*     */       public double blendDensity(DensityFunction.FunctionContext context, double noiseValue) {
/*  47 */         return noiseValue;
/*     */       }
/*     */ 
/*     */       
/*     */       public BiomeResolver getBiomeResolver(BiomeResolver biomeResolver) {
/*  52 */         return biomeResolver;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  57 */   private static final NormalNoise SHIFT_NOISE = NormalNoise.create((RandomSource)new XoroshiroRandomSource(42L), NoiseData.DEFAULT_SHIFT);
/*     */   
/*  59 */   private static final int HEIGHT_BLENDING_RANGE_CELLS = QuartPos.fromSection(7) - 1;
/*  60 */   private static final int HEIGHT_BLENDING_RANGE_CHUNKS = QuartPos.toSection(HEIGHT_BLENDING_RANGE_CELLS + 3);
/*     */   private static final int DENSITY_BLENDING_RANGE_CELLS = 2;
/*  62 */   private static final int DENSITY_BLENDING_RANGE_CHUNKS = QuartPos.toSection(5);
/*     */   
/*     */   private static final double OLD_CHUNK_XZ_RADIUS = 8.0D;
/*     */   private final Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData;
/*     */   private final Long2ObjectOpenHashMap<BlendingData> densityBlendingData;
/*     */   
/*     */   public static Blender empty() {
/*  69 */     return EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Blender of(WorldGenRegion region) {
/*  74 */     if (SharedConstants.DEBUG_DISABLE_BLENDING || region == null) {
/*  75 */       return EMPTY;
/*     */     }
/*     */     
/*  78 */     ChunkPos centerPos = region.getCenter();
/*  79 */     if (!region.isOldChunkAround(centerPos, HEIGHT_BLENDING_RANGE_CHUNKS)) {
/*  80 */       return EMPTY;
/*     */     }
/*     */     
/*  83 */     Long2ObjectOpenHashMap<BlendingData> heightAndBiomeData = new Long2ObjectOpenHashMap();
/*  84 */     Long2ObjectOpenHashMap<BlendingData> densityData = new Long2ObjectOpenHashMap();
/*     */     
/*  86 */     int maxDistSq = Mth.square(HEIGHT_BLENDING_RANGE_CHUNKS + 1);
/*  87 */     for (int dx = -HEIGHT_BLENDING_RANGE_CHUNKS; dx <= HEIGHT_BLENDING_RANGE_CHUNKS; dx++) {
/*  88 */       for (int dz = -HEIGHT_BLENDING_RANGE_CHUNKS; dz <= HEIGHT_BLENDING_RANGE_CHUNKS; dz++) {
/*  89 */         if (dx * dx + dz * dz <= maxDistSq) {
/*     */ 
/*     */           
/*  92 */           int chunkX = centerPos.x + dx;
/*  93 */           int chunkZ = centerPos.z + dz;
/*  94 */           BlendingData blendingData = BlendingData.getOrUpdateBlendingData(region, chunkX, chunkZ);
/*  95 */           if (blendingData != null) {
/*     */ 
/*     */             
/*  98 */             heightAndBiomeData.put(ChunkPos.asLong(chunkX, chunkZ), blendingData);
/*  99 */             if (dx >= -DENSITY_BLENDING_RANGE_CHUNKS && dx <= DENSITY_BLENDING_RANGE_CHUNKS && dz >= -DENSITY_BLENDING_RANGE_CHUNKS && dz <= DENSITY_BLENDING_RANGE_CHUNKS)
/* 100 */               densityData.put(ChunkPos.asLong(chunkX, chunkZ), blendingData); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     if (heightAndBiomeData.isEmpty() && densityData.isEmpty()) {
/* 106 */       return EMPTY;
/*     */     }
/* 108 */     return new Blender(heightAndBiomeData, densityData);
/*     */   }
/*     */   
/*     */   private Blender(Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData, Long2ObjectOpenHashMap<BlendingData> densityBlendingData) {
/* 112 */     this.heightAndBiomeBlendingData = heightAndBiomeBlendingData;
/* 113 */     this.densityBlendingData = densityBlendingData;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 117 */     return (this.heightAndBiomeBlendingData.isEmpty() && this.densityBlendingData.isEmpty());
/*     */   } public static interface DistanceGetter {
/*     */     double getDistance(double param1Double1, double param1Double2, double param1Double3); } public static final class BlendingOutput extends Record { private final double alpha; private final double blendingOffset;
/* 120 */     public BlendingOutput(double alpha, double blendingOffset) { this.alpha = alpha; this.blendingOffset = blendingOffset; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/blending/Blender$BlendingOutput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 120 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/Blender$BlendingOutput; } public double alpha() { return this.alpha; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/blending/Blender$BlendingOutput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 120 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/Blender$BlendingOutput; } public double blendingOffset() { return this.blendingOffset; } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/blending/Blender$BlendingOutput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/blending/Blender$BlendingOutput;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } } public BlendingOutput blendOffsetAndFactor(int blockX, int blockZ) {
/* 123 */     int cellX = QuartPos.fromBlock(blockX);
/* 124 */     int cellZ = QuartPos.fromBlock(blockZ);
/*     */     
/* 126 */     double fixedHeight = getBlendingDataValue(cellX, 0, cellZ, BlendingData::getHeight);
/* 127 */     if (fixedHeight != Double.MAX_VALUE) {
/* 128 */       return new BlendingOutput(0.0D, heightToOffset(fixedHeight));
/*     */     }
/*     */ 
/*     */     
/* 132 */     MutableDouble totalWeight = new MutableDouble(0.0D);
/* 133 */     MutableDouble weightedHeights = new MutableDouble(0.0D);
/* 134 */     MutableDouble closestDistance = new MutableDouble(Double.POSITIVE_INFINITY);
/*     */     
/* 136 */     this.heightAndBiomeBlendingData.forEach((chunkPos, blendingData) -> blendingData.iterateHeights(QuartPos.fromSection(ChunkPos.getX(chunkPos)), QuartPos.fromSection(ChunkPos.getZ(chunkPos)), ()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     if (closestDistance.doubleValue() == Double.POSITIVE_INFINITY) {
/* 159 */       return new BlendingOutput(1.0D, 0.0D);
/*     */     }
/*     */     
/* 162 */     double averageHeight = weightedHeights.doubleValue() / totalWeight.doubleValue();
/* 163 */     double alpha = Mth.clamp(closestDistance.doubleValue() / (HEIGHT_BLENDING_RANGE_CELLS + 1), 0.0D, 1.0D);
/*     */     
/* 165 */     alpha = 3.0D * alpha * alpha - 2.0D * alpha * alpha * alpha;
/*     */     
/* 167 */     return new BlendingOutput(alpha, heightToOffset(averageHeight));
/*     */   }
/*     */ 
/*     */   
/*     */   private static double heightToOffset(double height) {
/* 172 */     double dimensionFactor = 1.0D;
/* 173 */     double targetY = height + 0.5D;
/* 174 */     double targetYMod = Mth.positiveModulo(targetY, 8.0D);
/*     */     
/* 176 */     return 1.0D * (32.0D * (targetY - 128.0D) - 3.0D * (targetY - 120.0D) * targetYMod + 3.0D * targetYMod * targetYMod) / 128.0D * (32.0D - 3.0D * targetYMod);
/*     */   }
/*     */   
/*     */   public double blendDensity(DensityFunction.FunctionContext context, double noiseValue) {
/* 180 */     int cellX = QuartPos.fromBlock(context.blockX());
/* 181 */     int cellY = context.blockY() / 8;
/* 182 */     int cellZ = QuartPos.fromBlock(context.blockZ());
/*     */     
/* 184 */     double fixedDensity = getBlendingDataValue(cellX, cellY, cellZ, BlendingData::getDensity);
/* 185 */     if (fixedDensity != Double.MAX_VALUE) {
/* 186 */       return fixedDensity;
/*     */     }
/*     */     
/* 189 */     MutableDouble totalWeight = new MutableDouble(0.0D);
/* 190 */     MutableDouble weightedHeights = new MutableDouble(0.0D);
/* 191 */     MutableDouble closestDistance = new MutableDouble(Double.POSITIVE_INFINITY);
/*     */     
/* 193 */     this.densityBlendingData.forEach((chunkPos, blendingData) -> blendingData.iterateDensities(QuartPos.fromSection(ChunkPos.getX(chunkPos)), QuartPos.fromSection(ChunkPos.getZ(chunkPos)), cellY - 1, cellY + 1, ()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     if (closestDistance.doubleValue() == Double.POSITIVE_INFINITY) {
/* 218 */       return noiseValue;
/*     */     }
/* 220 */     double averageDensity = weightedHeights.doubleValue() / totalWeight.doubleValue();
/* 221 */     double alpha = Mth.clamp(closestDistance.doubleValue() / 3.0D, 0.0D, 1.0D);
/*     */     
/* 223 */     return Mth.lerp(alpha, averageDensity, noiseValue);
/*     */   }
/*     */   
/*     */   private double getBlendingDataValue(int cellX, int cellY, int cellZ, CellValueGetter cellValueGetter) {
/* 227 */     int chunkX = QuartPos.toSection(cellX);
/* 228 */     int chunkZ = QuartPos.toSection(cellZ);
/*     */     
/* 230 */     boolean minX = ((cellX & 0x3) == 0);
/* 231 */     boolean minZ = ((cellZ & 0x3) == 0);
/*     */ 
/*     */     
/* 234 */     double value = getBlendingDataValue(cellValueGetter, chunkX, chunkZ, cellX, cellY, cellZ);
/* 235 */     if (value == Double.MAX_VALUE) {
/*     */       
/* 237 */       if (minX && minZ) {
/* 238 */         value = getBlendingDataValue(cellValueGetter, chunkX - 1, chunkZ - 1, cellX, cellY, cellZ);
/*     */       }
/*     */       
/* 241 */       if (value == Double.MAX_VALUE) {
/* 242 */         if (minX) {
/* 243 */           value = getBlendingDataValue(cellValueGetter, chunkX - 1, chunkZ, cellX, cellY, cellZ);
/*     */         }
/*     */         
/* 246 */         if (value == Double.MAX_VALUE && minZ) {
/* 247 */           value = getBlendingDataValue(cellValueGetter, chunkX, chunkZ - 1, cellX, cellY, cellZ);
/*     */         }
/*     */       } 
/*     */     } 
/* 251 */     return value;
/*     */   }
/*     */   
/*     */   private double getBlendingDataValue(CellValueGetter cellValueGetter, int chunkX, int chunkZ, int cellX, int cellY, int cellZ) {
/* 255 */     BlendingData blendingData = (BlendingData)this.heightAndBiomeBlendingData.get(ChunkPos.asLong(chunkX, chunkZ));
/* 256 */     if (blendingData != null) {
/* 257 */       return cellValueGetter.get(blendingData, cellX - QuartPos.fromSection(chunkX), cellY, cellZ - QuartPos.fromSection(chunkZ));
/*     */     }
/* 259 */     return Double.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public BiomeResolver getBiomeResolver(BiomeResolver biomeResolver) {
/* 263 */     return (quartX, quartY, quartZ, sampler) -> {
/*     */         Holder<Biome> biome = blendBiome(biomeResolver, quartY, quartZ);
/*     */         return (biome == null) ? biomeResolver.getNoiseBiome(biomeResolver, quartY, quartZ, sampler) : biome;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Holder<Biome> blendBiome(int quartX, int quartY, int quartZ) {
/* 273 */     MutableDouble closestDistance = new MutableDouble(Double.POSITIVE_INFINITY);
/* 274 */     MutableObject<Holder<Biome>> closestBiome = new MutableObject();
/*     */     
/* 276 */     this.heightAndBiomeBlendingData.forEach((chunkPos, blendingData) -> blendingData.iterateBiomes(QuartPos.fromSection(ChunkPos.getX(chunkPos)), quartY, QuartPos.fromSection(ChunkPos.getZ(chunkPos)), ()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     if (closestDistance.doubleValue() == Double.POSITIVE_INFINITY) {
/* 296 */       return null;
/*     */     }
/*     */     
/* 299 */     double shiftNoise = SHIFT_NOISE.getValue(quartX, 0.0D, quartZ) * 12.0D;
/* 300 */     double alpha = Mth.clamp((closestDistance.doubleValue() + shiftNoise) / (HEIGHT_BLENDING_RANGE_CELLS + 1), 0.0D, 1.0D);
/* 301 */     if (alpha > 0.5D) {
/* 302 */       return null;
/*     */     }
/*     */     
/* 305 */     return (Holder<Biome>)closestBiome.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generateBorderTicks(WorldGenRegion region, ChunkAccess chunk) {
/* 313 */     if (SharedConstants.DEBUG_DISABLE_BLENDING) {
/*     */       return;
/*     */     }
/*     */     
/* 317 */     ChunkPos chunkPos = chunk.getPos();
/* 318 */     boolean oldNoiseGeneration = chunk.isOldNoiseGeneration();
/* 319 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 320 */     BlockPos chunkOrigin = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ());
/*     */     
/* 322 */     BlendingData blendingData = chunk.getBlendingData();
/* 323 */     if (blendingData == null) {
/*     */       return;
/*     */     }
/* 326 */     int oldMinY = blendingData.getAreaWithOldGeneration().getMinY();
/* 327 */     int oldMaxY = blendingData.getAreaWithOldGeneration().getMaxY();
/*     */ 
/*     */     
/* 330 */     if (oldNoiseGeneration) {
/* 331 */       for (int x = 0; x < 16; x++) {
/* 332 */         for (int z = 0; z < 16; z++) {
/* 333 */           generateBorderTick(chunk, (BlockPos)pos.setWithOffset((Vec3i)chunkOrigin, x, oldMinY - 1, z));
/* 334 */           generateBorderTick(chunk, (BlockPos)pos.setWithOffset((Vec3i)chunkOrigin, x, oldMinY, z));
/* 335 */           generateBorderTick(chunk, (BlockPos)pos.setWithOffset((Vec3i)chunkOrigin, x, oldMaxY, z));
/* 336 */           generateBorderTick(chunk, (BlockPos)pos.setWithOffset((Vec3i)chunkOrigin, x, oldMaxY + 1, z));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 341 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 342 */       if (region.getChunk(chunkPos.x + direction.getStepX(), chunkPos.z + direction.getStepZ()).isOldNoiseGeneration() == oldNoiseGeneration) {
/*     */         continue;
/*     */       }
/*     */       
/* 346 */       int minX = (direction == Direction.EAST) ? 15 : 0;
/* 347 */       int maxX = (direction == Direction.WEST) ? 0 : 15;
/* 348 */       int minZ = (direction == Direction.SOUTH) ? 15 : 0;
/* 349 */       int maxZ = (direction == Direction.NORTH) ? 0 : 15;
/*     */       
/* 351 */       for (int x = minX; x <= maxX; x++) {
/* 352 */         for (int z = minZ; z <= maxZ; z++) {
/* 353 */           int maxY = Math.min(oldMaxY, chunk.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z)) + 1;
/*     */           
/* 355 */           for (int y = oldMinY; y < maxY; y++) {
/* 356 */             generateBorderTick(chunk, (BlockPos)pos.setWithOffset((Vec3i)chunkOrigin, x, y, z));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void generateBorderTick(ChunkAccess chunk, BlockPos pos) {
/* 365 */     BlockState blockState = chunk.getBlockState(pos);
/* 366 */     if (blockState.is(BlockTags.LEAVES)) {
/* 367 */       chunk.markPosForPostprocessing(pos);
/*     */     }
/*     */     
/* 370 */     FluidState fluidState = chunk.getFluidState(pos);
/* 371 */     if (!fluidState.isEmpty()) {
/* 372 */       chunk.markPosForPostprocessing(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addAroundOldChunksCarvingMaskFilter(WorldGenLevel region, ProtoChunk chunk) {
/* 377 */     if (SharedConstants.DEBUG_DISABLE_BLENDING) {
/*     */       return;
/*     */     }
/* 380 */     ChunkPos chunkPos = chunk.getPos();
/* 381 */     ImmutableMap.Builder<Direction8, BlendingData> builder = ImmutableMap.builder();
/* 382 */     for (Direction8 direction8 : Direction8.values()) {
/* 383 */       int testChunkX = chunkPos.x + direction8.getStepX();
/* 384 */       int testChunkZ = chunkPos.z + direction8.getStepZ();
/*     */       
/* 386 */       BlendingData blendingData = region.getChunk(testChunkX, testChunkZ).getBlendingData();
/* 387 */       if (blendingData != null) {
/* 388 */         builder.put(direction8, blendingData);
/*     */       }
/*     */     } 
/* 391 */     ImmutableMap<Direction8, BlendingData> oldSidesBlendingData = builder.build();
/*     */ 
/*     */     
/* 394 */     if (!chunk.isOldNoiseGeneration() && oldSidesBlendingData.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 398 */     DistanceGetter distanceGetter = makeOldChunkDistanceGetter(chunk.getBlendingData(), (Map<Direction8, BlendingData>)oldSidesBlendingData);
/*     */ 
/*     */     
/*     */     CarvingMask.Mask filter = (x, y, z) -> {
/*     */         double shiftedX = x + 0.5D + SHIFT_NOISE.getValue(x, y, z) * 4.0D, shiftedY = y + 0.5D + SHIFT_NOISE.getValue(y, z, x) * 4.0D, shiftedZ = z + 0.5D + SHIFT_NOISE.getValue(z, x, y) * 4.0D;
/*     */ 
/*     */         
/*     */         return (distanceGetter.getDistance(shiftedX, shiftedY, shiftedZ) < 4.0D);
/*     */       };
/*     */     
/* 408 */     chunk.getOrCreateCarvingMask().setAdditionalMask(filter);
/*     */   }
/*     */   
/*     */   public static DistanceGetter makeOldChunkDistanceGetter(BlendingData centerBlendingData, Map<Direction8, BlendingData> oldSidesBlendingData) {
/* 412 */     List<DistanceGetter> distanceGetters = Lists.newArrayList();
/* 413 */     if (centerBlendingData != null) {
/* 414 */       distanceGetters.add(makeOffsetOldChunkDistanceGetter(null, centerBlendingData));
/*     */     }
/*     */     
/* 417 */     oldSidesBlendingData.forEach((side, blendingData) -> distanceGetters.add(makeOffsetOldChunkDistanceGetter(side, blendingData)));
/*     */     
/* 419 */     return (x, y, z) -> {
/*     */         double closest = Double.POSITIVE_INFINITY;
/*     */         for (DistanceGetter getter : (Iterable<DistanceGetter>)distanceGetters) {
/*     */           double distance = getter.getDistance(x, y, z);
/*     */           if (distance < closest) {
/*     */             closest = distance;
/*     */           }
/*     */         } 
/*     */         return closest;
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static DistanceGetter makeOffsetOldChunkDistanceGetter(Direction8 offset, BlendingData blendingData) {
/* 433 */     double offsetX = 0.0D;
/* 434 */     double offsetZ = 0.0D;
/*     */     
/* 436 */     if (offset != null)
/*     */     {
/* 438 */       for (Direction direction : (Iterable<Direction>)offset.getDirections()) {
/* 439 */         offsetX += (direction.getStepX() * 16);
/* 440 */         offsetZ += (direction.getStepZ() * 16);
/*     */       } 
/*     */     }
/*     */     
/* 444 */     double finalOffsetX = offsetX;
/* 445 */     double finalOffsetZ = offsetZ;
/*     */     
/* 447 */     double oldChunkYRadius = blendingData.getAreaWithOldGeneration().getHeight() / 2.0D;
/* 448 */     double oldChunkCenterY = blendingData.getAreaWithOldGeneration().getMinY() + oldChunkYRadius;
/*     */     
/* 450 */     return (x, y, z) -> distanceToCube(x - 8.0D - finalOffsetX, y - oldChunkCenterY, z - 8.0D - finalOffsetZ, 8.0D, oldChunkYRadius, 8.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double distanceToCube(double x, double y, double z, double radiusX, double radiusY, double radiusZ) {
/* 467 */     double deltaX = Math.abs(x) - radiusX;
/* 468 */     double deltaY = Math.abs(y) - radiusY;
/* 469 */     double deltaZ = Math.abs(z) - radiusZ;
/*     */     
/* 471 */     return Mth.length(Math.max(0.0D, deltaX), Math.max(0.0D, deltaY), Math.max(0.0D, deltaZ));
/*     */   }
/*     */   
/*     */   private static interface CellValueGetter {
/*     */     double get(BlendingData param1BlendingData, int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blending/Blender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */