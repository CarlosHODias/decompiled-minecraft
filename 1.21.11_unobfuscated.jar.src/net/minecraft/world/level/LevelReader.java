/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface LevelReader
/*     */   extends BlockAndTintGetter, CollisionGetter, SignalGetter, BiomeManager.NoiseBiomeSource
/*     */ {
/*     */   ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*     */   
/*     */   @Deprecated
/*     */   boolean hasChunk(int paramInt1, int paramInt2);
/*     */   
/*     */   int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2);
/*     */   
/*     */   default int getHeight(Heightmap.Types type, BlockPos pos) {
/*  39 */     return getHeight(type, pos.getX(), pos.getZ());
/*     */   }
/*     */   
/*     */   int getSkyDarken();
/*     */   
/*     */   BiomeManager getBiomeManager();
/*     */   
/*     */   default Holder<Biome> getBiome(BlockPos pos) {
/*  47 */     return getBiomeManager().getBiome(pos);
/*     */   }
/*     */   
/*     */   default Stream<BlockState> getBlockStatesIfLoaded(AABB box) {
/*  51 */     int x0 = Mth.floor(box.minX);
/*  52 */     int x1 = Mth.floor(box.maxX);
/*  53 */     int y0 = Mth.floor(box.minY);
/*  54 */     int y1 = Mth.floor(box.maxY);
/*  55 */     int z0 = Mth.floor(box.minZ);
/*  56 */     int z1 = Mth.floor(box.maxZ);
/*     */     
/*  58 */     if (hasChunksAt(x0, y0, z0, x1, y1, z1)) {
/*  59 */       return getBlockStates(box);
/*     */     }
/*  61 */     return Stream.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   default int getBlockTint(BlockPos pos, ColorResolver resolver) {
/*  66 */     return resolver.getColor((Biome)getBiome(pos).value(), pos.getX(), pos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   default Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/*  71 */     ChunkAccess chunk = getChunk(QuartPos.toSection(quartX), QuartPos.toSection(quartZ), ChunkStatus.BIOMES, false);
/*  72 */     if (chunk != null) {
/*  73 */       return chunk.getNoiseBiome(quartX, quartY, quartZ);
/*     */     }
/*  75 */     return getUncachedNoiseBiome(quartX, quartY, quartZ);
/*     */   }
/*     */ 
/*     */   
/*     */   Holder<Biome> getUncachedNoiseBiome(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   boolean isClientSide();
/*     */   
/*     */   int getSeaLevel();
/*     */   
/*     */   DimensionType dimensionType();
/*     */   
/*     */   default int getMinY() {
/*  88 */     return dimensionType().minY();
/*     */   }
/*     */ 
/*     */   
/*     */   default int getHeight() {
/*  93 */     return dimensionType().height();
/*     */   }
/*     */   
/*     */   default BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) {
/*  97 */     return new BlockPos(pos.getX(), getHeight(type, pos.getX(), pos.getZ()), pos.getZ());
/*     */   }
/*     */   
/*     */   default boolean isEmptyBlock(BlockPos pos) {
/* 101 */     return getBlockState(pos).isAir();
/*     */   }
/*     */   
/*     */   default boolean canSeeSkyFromBelowWater(BlockPos pos) {
/* 105 */     if (pos.getY() >= getSeaLevel()) {
/* 106 */       return canSeeSky(pos);
/*     */     }
/* 108 */     BlockPos scanPoint = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ());
/* 109 */     if (!canSeeSky(scanPoint)) {
/* 110 */       return false;
/*     */     }
/* 112 */     scanPoint = scanPoint.below();
/* 113 */     while (scanPoint.getY() > pos.getY()) {
/* 114 */       BlockState state = getBlockState(scanPoint);
/* 115 */       if (state.getLightBlock() > 0 && !state.liquid()) {
/* 116 */         return false;
/*     */       }
/* 118 */       scanPoint = scanPoint.below();
/*     */     } 
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   default float getPathfindingCostFromLightLevels(BlockPos pos) {
/* 125 */     return getLightLevelDependentMagicValue(pos) - 0.5F;
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
/*     */   @Deprecated
/*     */   default float getLightLevelDependentMagicValue(BlockPos pos) {
/* 141 */     float v = getMaxLocalRawBrightness(pos) / 15.0F;
/*     */     
/* 143 */     float curvedV = v / (4.0F - 3.0F * v);
/* 144 */     return Mth.lerp(dimensionType().ambientLight(), curvedV, 1.0F);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(BlockPos pos) {
/* 148 */     return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int chunkX, int chunkZ) {
/* 152 */     return getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status) {
/* 156 */     return getChunk(chunkX, chunkZ, status, true);
/*     */   }
/*     */ 
/*     */   
/*     */   default BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
/* 161 */     return (BlockGetter)getChunk(chunkX, chunkZ, ChunkStatus.EMPTY, false);
/*     */   }
/*     */   
/*     */   default boolean isWaterAt(BlockPos pos) {
/* 165 */     return getFluidState(pos).is(FluidTags.WATER);
/*     */   }
/*     */   
/*     */   default boolean containsAnyLiquid(AABB box) {
/* 169 */     int x0 = Mth.floor(box.minX);
/* 170 */     int x1 = Mth.ceil(box.maxX);
/* 171 */     int y0 = Mth.floor(box.minY);
/* 172 */     int y1 = Mth.ceil(box.maxY);
/* 173 */     int z0 = Mth.floor(box.minZ);
/* 174 */     int z1 = Mth.ceil(box.maxZ);
/*     */     
/* 176 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 177 */     for (int x = x0; x < x1; x++) {
/* 178 */       for (int y = y0; y < y1; y++) {
/* 179 */         for (int z = z0; z < z1; z++) {
/* 180 */           BlockState blockState = getBlockState((BlockPos)pos.set(x, y, z));
/* 181 */           if (!blockState.getFluidState().isEmpty()) {
/* 182 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 187 */     return false;
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos pos) {
/* 191 */     return getMaxLocalRawBrightness(pos, getSkyDarken());
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos pos, int skyDarkening) {
/* 195 */     if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000) {
/* 196 */       return 15;
/*     */     }
/*     */     
/* 199 */     return getRawBrightness(pos, skyDarkening);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(int blockX, int blockZ) {
/* 207 */     return hasChunk(SectionPos.blockToSectionCoord(blockX), SectionPos.blockToSectionCoord(blockZ));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(BlockPos pos) {
/* 215 */     return hasChunkAt(pos.getX(), pos.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(BlockPos pos0, BlockPos pos1) {
/* 223 */     return hasChunksAt(pos0.getX(), pos0.getY(), pos0.getZ(), pos1.getX(), pos1.getY(), pos1.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int x0, int y0, int z0, int x1, int y1, int z1) {
/* 231 */     if (y1 < getMinY() || y0 > getMaxY()) {
/* 232 */       return false;
/*     */     }
/*     */     
/* 235 */     return hasChunksAt(x0, z0, x1, z1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int x0, int z0, int x1, int z1) {
/* 243 */     int chunkX0 = SectionPos.blockToSectionCoord(x0);
/* 244 */     int chunkX1 = SectionPos.blockToSectionCoord(x1);
/* 245 */     int chunkZ0 = SectionPos.blockToSectionCoord(z0);
/* 246 */     int chunkZ1 = SectionPos.blockToSectionCoord(z1);
/*     */     
/* 248 */     for (int chunkX = chunkX0; chunkX <= chunkX1; chunkX++) {
/* 249 */       for (int chunkZ = chunkZ0; chunkZ <= chunkZ1; chunkZ++) {
/* 250 */         if (!hasChunk(chunkX, chunkZ)) {
/* 251 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     return true;
/*     */   }
/*     */   
/*     */   RegistryAccess registryAccess();
/*     */   
/*     */   FeatureFlagSet enabledFeatures();
/*     */   
/*     */   default <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 264 */     Registry<T> registry = registryAccess().lookupOrThrow(key);
/* 265 */     return (HolderLookup<T>)registry.filterFeatures(enabledFeatures());
/*     */   }
/*     */   
/*     */   EnvironmentAttributeReader environmentAttributes();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/LevelReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */