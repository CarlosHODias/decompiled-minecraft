/*     */ package net.minecraft.server.level;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ import net.minecraft.world.level.chunk.status.ChunkType;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.LevelTickAccess;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import net.minecraft.world.ticks.WorldGenTickAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldGenRegion implements WorldGenLevel {
/*  66 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   private final ChunkAccess center;
/*     */   private final ServerLevel level;
/*     */   private final long seed;
/*     */   private final LevelData levelData;
/*     */   private final RandomSource random;
/*     */   private final DimensionType dimensionType;
/*     */   private final WorldGenTickAccess<Block> blockTicks;
/*     */   private final WorldGenTickAccess<Fluid> fluidTicks;
/*     */   private final BiomeManager biomeManager;
/*     */   private final ChunkStep generatingStep;
/*     */   private Supplier<String> currentlyGenerating;
/*     */   private final AtomicLong subTickCount;
/*  81 */   private static final Identifier WORLDGEN_REGION_RANDOM = Identifier.withDefaultNamespace("worldgen_region_random"); public WorldGenRegion(ServerLevel level, StaticCache2D<GenerationChunkHolder> cache, ChunkStep generatingStep, ChunkAccess center) { this.blockTicks = new WorldGenTickAccess(pos -> getChunk(pos).getBlockTicks());
/*     */     this.fluidTicks = new WorldGenTickAccess(pos -> getChunk(pos).getFluidTicks());
/*     */     this.subTickCount = new AtomicLong();
/*  84 */     this.generatingStep = generatingStep;
/*  85 */     this.cache = cache;
/*  86 */     this.center = center;
/*  87 */     this.level = level;
/*  88 */     this.seed = level.getSeed();
/*  89 */     this.levelData = level.getLevelData();
/*  90 */     this.random = level.getChunkSource().randomState().getOrCreateRandomFactory(WORLDGEN_REGION_RANDOM).at(this.center.getPos().getWorldPosition());
/*     */     
/*  92 */     this.dimensionType = level.dimensionType();
/*  93 */     this.biomeManager = new BiomeManager((BiomeManager.NoiseBiomeSource)this, BiomeManager.obfuscateSeed(this.seed)); }
/*     */ 
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos pos, int range) {
/*  97 */     return (this.level.getChunkSource()).chunkMap.isOldChunkAround(pos, range);
/*     */   }
/*     */   
/*     */   public ChunkPos getCenter() {
/* 101 */     return this.center.getPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentlyGenerating(Supplier<String> currentlyGenerating) {
/* 106 */     this.currentlyGenerating = currentlyGenerating;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ) {
/* 111 */     return getChunk(chunkX, chunkZ, ChunkStatus.EMPTY);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*     */     GenerationChunkHolder chunkHolder;
/* 116 */     int distance = this.center.getPos().getChessboardDistance(chunkX, chunkZ);
/* 117 */     ChunkStatus maxAllowedStatus = (distance >= this.generatingStep.directDependencies().size()) ? null : this.generatingStep.directDependencies().get(distance);
/*     */     
/* 119 */     if (maxAllowedStatus != null) {
/* 120 */       chunkHolder = (GenerationChunkHolder)this.cache.get(chunkX, chunkZ);
/* 121 */       if (targetStatus.isOrBefore(maxAllowedStatus)) {
/* 122 */         ChunkAccess chunk = chunkHolder.getChunkIfPresentUnchecked(maxAllowedStatus);
/* 123 */         if (chunk != null) {
/* 124 */           return chunk;
/*     */         }
/*     */       } 
/*     */     } else {
/* 128 */       chunkHolder = null;
/*     */     } 
/* 130 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Requested chunk unavailable during world generation"), "Exception generating new chunk");
/* 131 */     CrashReportCategory category = report.addCategory("Chunk request details");
/* 132 */     category.setDetail("Requested chunk", String.format(Locale.ROOT, "%d, %d", new Object[] { chunkX, chunkZ }));
/* 133 */     category.setDetail("Generating status", () -> this.generatingStep.targetStatus().getName());
/* 134 */     Objects.requireNonNull(targetStatus); category.setDetail("Requested status", targetStatus::getName);
/* 135 */     category.setDetail("Actual status", () -> (chunkHolder == null) ? "[out of cache bounds]" : chunkHolder.getPersistedStatus().getName());
/* 136 */     category.setDetail("Maximum allowed status", () -> (maxAllowedStatus == null) ? "null" : maxAllowedStatus.getName());
/* 137 */     Objects.requireNonNull(this.generatingStep.directDependencies()); category.setDetail("Dependencies", this.generatingStep.directDependencies()::toString);
/* 138 */     category.setDetail("Requested distance", distance);
/* 139 */     Objects.requireNonNull(this.center.getPos()); category.setDetail("Generating chunk", this.center.getPos()::toString);
/* 140 */     throw new ReportedException(report);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int chunkX, int chunkZ) {
/* 145 */     int distance = this.center.getPos().getChessboardDistance(chunkX, chunkZ);
/* 146 */     return (distance < this.generatingStep.directDependencies().size());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 151 */     return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())).getBlockState(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 156 */     return getChunk(pos).getFluidState(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public Player getNearestPlayer(double x, double y, double z, double maxDist, Predicate<Entity> predicate) {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkyDarken() {
/* 166 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeManager getBiomeManager() {
/* 171 */     return this.biomeManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
/* 176 */     return this.level.getUncachedNoiseBiome(quartX, quartY, quartZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getShade(net.minecraft.core.Direction direction, boolean shade) {
/* 181 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/* 186 */     return this.level.getLightEngine();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
/* 191 */     BlockState blockState = getBlockState(pos);
/* 192 */     if (blockState.isAir()) {
/* 193 */       return false;
/*     */     }
/*     */     
/* 196 */     if (dropResources) {
/* 197 */       BlockEntity blockEntity = blockState.hasBlockEntity() ? getBlockEntity(pos) : null;
/* 198 */       Block.dropResources(blockState, this.level, pos, blockEntity, breaker, ItemStack.EMPTY);
/*     */     } 
/* 200 */     return setBlock(pos, Blocks.AIR.defaultBlockState(), 3, updateLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 206 */     ChunkAccess chunk = getChunk(pos);
/* 207 */     BlockEntity blockEntity = chunk.getBlockEntity(pos);
/*     */     
/* 209 */     if (blockEntity != null) {
/* 210 */       return blockEntity;
/*     */     }
/*     */     
/* 213 */     CompoundTag tag = chunk.getBlockEntityNbt(pos);
/* 214 */     BlockState state = chunk.getBlockState(pos);
/* 215 */     if (tag != null) {
/* 216 */       if ("DUMMY".equals(tag.getStringOr("id", ""))) {
/* 217 */         if (!state.hasBlockEntity()) {
/* 218 */           return null;
/*     */         }
/* 220 */         blockEntity = ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */       } else {
/* 222 */         blockEntity = BlockEntity.loadStatic(pos, state, tag, (HolderLookup.Provider)this.level.registryAccess());
/*     */       } 
/*     */       
/* 225 */       if (blockEntity != null) {
/* 226 */         chunk.setBlockEntity(blockEntity);
/* 227 */         return blockEntity;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (state.hasBlockEntity()) {
/* 232 */       LOGGER.warn("Tried to access a block entity before it was created. {}", pos);
/*     */     }
/*     */     
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ensureCanWrite(BlockPos pos) {
/* 240 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 241 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/*     */     
/* 243 */     ChunkPos centerPos = getCenter();
/* 244 */     int distanceX = Math.abs(centerPos.x - chunkX);
/* 245 */     int distanceZ = Math.abs(centerPos.z - chunkZ);
/*     */     
/* 247 */     if (distanceX > this.generatingStep.blockStateWriteRadius() || distanceZ > this.generatingStep.blockStateWriteRadius()) {
/* 248 */       Util.logAndPauseIfInIde("Detected setBlock in a far chunk [" + chunkX + ", " + chunkZ + "], pos: " + String.valueOf(pos) + ", status: " + String.valueOf(this.generatingStep.targetStatus()) + ((this.currentlyGenerating == null) ? "" : (", currently generating: " + (String)this.currentlyGenerating.get())));
/* 249 */       return false;
/*     */     } 
/*     */     
/* 252 */     if (this.center.isUpgrading()) {
/* 253 */       LevelHeightAccessor levelHeightAccessor = this.center.getHeightAccessorForGeneration();
/* 254 */       if (levelHeightAccessor.isOutsideBuildHeight(pos.getY())) {
/* 255 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 259 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
/* 264 */     if (!ensureCanWrite(pos)) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     ChunkAccess chunk = getChunk(pos);
/* 269 */     BlockState oldState = chunk.setBlockState(pos, blockState, updateFlags);
/*     */     
/* 271 */     if (oldState != null) {
/* 272 */       this.level.updatePOIOnBlockStateChange(pos, oldState, blockState);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 277 */     if (blockState.hasBlockEntity()) {
/* 278 */       if (chunk.getPersistedStatus().getChunkType() == ChunkType.LEVELCHUNK) {
/* 279 */         BlockEntity blockEntity = ((EntityBlock)blockState.getBlock()).newBlockEntity(pos, blockState);
/* 280 */         if (blockEntity != null) {
/* 281 */           chunk.setBlockEntity(blockEntity);
/*     */         } else {
/* 283 */           chunk.removeBlockEntity(pos);
/*     */         } 
/*     */       } else {
/* 286 */         CompoundTag tag = new CompoundTag();
/* 287 */         tag.putInt("x", pos.getX());
/* 288 */         tag.putInt("y", pos.getY());
/* 289 */         tag.putInt("z", pos.getZ());
/* 290 */         tag.putString("id", "DUMMY");
/* 291 */         chunk.setBlockEntityNbt(tag);
/*     */       } 
/* 293 */     } else if (oldState != null && oldState.hasBlockEntity()) {
/* 294 */       chunk.removeBlockEntity(pos);
/*     */     } 
/*     */     
/* 297 */     if (blockState.hasPostProcess((BlockGetter)this, pos) && (updateFlags & 0x10) == 0) {
/* 298 */       markPosForPostprocessing(pos);
/*     */     }
/*     */     
/* 301 */     return true;
/*     */   }
/*     */   
/*     */   private void markPosForPostprocessing(BlockPos blockPos) {
/* 305 */     getChunk(blockPos).markPosForPostprocessing(blockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addFreshEntity(Entity entity) {
/* 313 */     int xc = SectionPos.blockToSectionCoord(entity.getBlockX());
/* 314 */     int zc = SectionPos.blockToSectionCoord(entity.getBlockZ());
/*     */     
/* 316 */     getChunk(xc, zc).addEntity(entity);
/* 317 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
/* 322 */     return setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/* 327 */     return this.level.getWorldBorder();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClientSide() {
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerLevel getLevel() {
/* 338 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 343 */     return this.level.registryAccess();
/*     */   }
/*     */ 
/*     */   
/*     */   public FeatureFlagSet enabledFeatures() {
/* 348 */     return this.level.enabledFeatures();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelData getLevelData() {
/* 353 */     return this.levelData;
/*     */   }
/*     */ 
/*     */   
/*     */   public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
/* 358 */     if (!hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
/* 359 */       throw new RuntimeException("We are asking a region for a chunk out of bound");
/*     */     }
/*     */     
/* 362 */     return new DifficultyInstance(this.level.getDifficulty(), this.level.getDayTime(), 0L, this.level.getMoonBrightness(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftServer getServer() {
/* 367 */     return this.level.getServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkSource getChunkSource() {
/* 372 */     return this.level.getChunkSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSeed() {
/* 377 */     return this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelTickAccess<Block> getBlockTicks() {
/* 382 */     return (LevelTickAccess<Block>)this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelTickAccess<Fluid> getFluidTicks() {
/* 387 */     return (LevelTickAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSeaLevel() {
/* 392 */     return this.level.getSeaLevel();
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource getRandom() {
/* 397 */     return this.random;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/* 402 */     return getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)).getHeight(type, x & 0xF, z & 0xF) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {}
/*     */ 
/*     */   
/*     */   public DimensionType dimensionType() {
/* 423 */     return this.dimensionType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
/* 428 */     return predicate.test(getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
/* 433 */     return predicate.test(getFluidState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 438 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) {
/* 443 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Player> players() {
/* 448 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 453 */     return this.level.getMinY();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 458 */     return this.level.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextSubTickCount() {
/* 463 */     return this.subTickCount.getAndIncrement();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnvironmentAttributeReader environmentAttributes() {
/* 468 */     return EnvironmentAttributeReader.EMPTY;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/WorldGenRegion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */