/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.AbortableIterationConsumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.TickRateManager;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageSources;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.crafting.RecipeAccess;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.PalettedContainerFactory;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.WritableLevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ 
/*     */ public abstract class Level
/*     */   implements LevelAccessor, AutoCloseable
/*     */ {
/*  92 */   public static final Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC = ResourceKey.codec(Registries.DIMENSION);
/*     */   
/*  94 */   public static final ResourceKey<Level> OVERWORLD = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("overworld"));
/*  95 */   public static final ResourceKey<Level> NETHER = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("the_nether"));
/*  96 */   public static final ResourceKey<Level> END = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("the_end"));
/*     */   
/*     */   public static final int MAX_LEVEL_SIZE = 30000000;
/*     */   
/*     */   public static final int LONG_PARTICLE_CLIP_RANGE = 512;
/*     */   
/*     */   public static final int SHORT_PARTICLE_CLIP_RANGE = 32;
/*     */   
/*     */   public static final int MAX_BRIGHTNESS = 15;
/*     */   public static final int MAX_ENTITY_SPAWN_Y = 20000000;
/*     */   public static final int MIN_ENTITY_SPAWN_Y = -20000000;
/* 107 */   private static final WeightedList<ExplosionParticleInfo> DEFAULT_EXPLOSION_BLOCK_PARTICLES = WeightedList.builder()
/* 108 */     .add(new ExplosionParticleInfo((ParticleOptions)ParticleTypes.POOF, 0.5F, 1.0F))
/* 109 */     .add(new ExplosionParticleInfo((ParticleOptions)ParticleTypes.SMOKE, 1.0F, 1.0F))
/* 110 */     .build();
/*     */   
/* 112 */   protected final List<TickingBlockEntity> blockEntityTickers = Lists.newArrayList();
/*     */   protected final CollectingNeighborUpdater neighborUpdater;
/* 114 */   private final List<TickingBlockEntity> pendingBlockEntityTickers = Lists.newArrayList();
/*     */   
/*     */   private boolean tickingBlockEntities;
/*     */   
/*     */   private final Thread thread;
/*     */   
/*     */   private final boolean isDebug;
/*     */   private int skyDarken;
/* 122 */   protected int randValue = RandomSource.create().nextInt();
/* 123 */   protected final int addend = 1013904223;
/*     */   
/*     */   protected float oRainLevel;
/*     */   protected float rainLevel;
/*     */   protected float oThunderLevel;
/*     */   protected float thunderLevel;
/* 129 */   public final RandomSource random = RandomSource.create();
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 134 */   private final RandomSource threadSafeRandom = RandomSource.createThreadSafe();
/*     */   
/*     */   private final Holder<DimensionType> dimensionTypeRegistration;
/*     */   
/*     */   protected final WritableLevelData levelData;
/*     */   
/*     */   private final boolean isClientSide;
/*     */   
/*     */   private final BiomeManager biomeManager;
/*     */   private final ResourceKey<Level> dimension;
/*     */   private final RegistryAccess registryAccess;
/*     */   private final DamageSources damageSources;
/*     */   private final PalettedContainerFactory palettedContainerFactory;
/*     */   private long subTickCount;
/*     */   
/*     */   protected Level(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
/* 150 */     this.levelData = levelData;
/* 151 */     this.dimensionTypeRegistration = dimensionTypeRegistration;
/* 152 */     this.dimension = dimension;
/* 153 */     this.isClientSide = isClientSide;
/* 154 */     this.thread = Thread.currentThread();
/* 155 */     this.biomeManager = new BiomeManager(this, biomeZoomSeed);
/* 156 */     this.isDebug = isDebug;
/* 157 */     this.neighborUpdater = new CollectingNeighborUpdater(this, maxChainedNeighborUpdates);
/* 158 */     this.registryAccess = registryAccess;
/* 159 */     this.palettedContainerFactory = PalettedContainerFactory.create(registryAccess);
/* 160 */     this.damageSources = new DamageSources(registryAccess);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClientSide() {
/* 165 */     return this.isClientSide;
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftServer getServer() {
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isInWorldBounds(BlockPos pos) {
/* 174 */     return (!isOutsideBuildHeight(pos) && isInWorldBoundsHorizontal(pos));
/*     */   }
/*     */   
/*     */   public boolean isInValidBounds(BlockPos pos) {
/* 178 */     return (!isOutsideBuildHeight(pos) && isInValidBoundsHorizontal(pos));
/*     */   }
/*     */   
/*     */   public static boolean isInSpawnableBounds(BlockPos pos) {
/* 182 */     return (!isOutsideSpawnableHeight(pos.getY()) && isInWorldBoundsHorizontal(pos));
/*     */   }
/*     */   
/*     */   private static boolean isInWorldBoundsHorizontal(BlockPos pos) {
/* 186 */     return (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000);
/*     */   }
/*     */   
/*     */   private static boolean isInValidBoundsHorizontal(BlockPos pos) {
/* 190 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 191 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 192 */     return ChunkPos.isValid(chunkX, chunkZ);
/*     */   }
/*     */   
/*     */   private static boolean isOutsideSpawnableHeight(int y) {
/* 196 */     return (y < -20000000 || y >= 20000000);
/*     */   }
/*     */   
/*     */   public LevelChunk getChunkAt(BlockPos pos) {
/* 200 */     return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunk getChunk(int chunkX, int chunkZ) {
/* 205 */     return (LevelChunk)getChunk(chunkX, chunkZ, ChunkStatus.FULL);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status, boolean loadOrGenerate) {
/* 210 */     ChunkAccess chunk = getChunkSource().getChunk(chunkX, chunkZ, status, loadOrGenerate);
/* 211 */     if (chunk == null && loadOrGenerate) {
/* 212 */       throw new IllegalStateException("Should always be able to create a chunk!");
/*     */     }
/* 214 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags) {
/* 219 */     return setBlock(pos, blockState, updateFlags, 512);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
/* 224 */     if (!isInValidBounds(pos)) {
/* 225 */       return false;
/*     */     }
/*     */     
/* 228 */     if (!isClientSide() && isDebug()) {
/* 229 */       return false;
/*     */     }
/*     */     
/* 232 */     LevelChunk chunk = getChunkAt(pos);
/* 233 */     Block block = blockState.getBlock();
/* 234 */     BlockState oldState = chunk.setBlockState(pos, blockState, updateFlags);
/*     */ 
/*     */     
/* 237 */     if (oldState != null) {
/*     */       
/* 239 */       BlockState newState = getBlockState(pos);
/*     */ 
/*     */       
/* 242 */       if (newState == blockState) {
/* 243 */         if (oldState != newState) {
/* 244 */           setBlocksDirty(pos, oldState, newState);
/*     */         }
/*     */         
/* 247 */         if ((updateFlags & 0x2) != 0 && (!isClientSide() || (updateFlags & 0x4) == 0) && (isClientSide() || (chunk.getFullStatus() != null && chunk.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING)))) {
/* 248 */           sendBlockUpdated(pos, oldState, blockState, updateFlags);
/*     */         }
/*     */         
/* 251 */         if ((updateFlags & 0x1) != 0) {
/* 252 */           updateNeighborsAt(pos, oldState.getBlock());
/* 253 */           if (!isClientSide() && blockState.hasAnalogOutputSignal()) {
/* 254 */             updateNeighbourForOutputSignal(pos, block);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 259 */         if ((updateFlags & 0x10) == 0 && updateLimit > 0) {
/* 260 */           int neighbourUpdateFlags = updateFlags & 0xFFFFFFDE;
/* 261 */           oldState.updateIndirectNeighbourShapes(this, pos, neighbourUpdateFlags, updateLimit - 1);
/* 262 */           blockState.updateNeighbourShapes(this, pos, neighbourUpdateFlags, updateLimit - 1);
/* 263 */           blockState.updateIndirectNeighbourShapes(this, pos, neighbourUpdateFlags, updateLimit - 1);
/*     */         } 
/*     */         
/* 266 */         updatePOIOnBlockStateChange(pos, oldState, newState);
/*     */       } 
/*     */       
/* 269 */       return true;
/*     */     } 
/*     */     
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updatePOIOnBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
/* 287 */     FluidState fluidState = getFluidState(pos);
/* 288 */     return setBlock(pos, fluidState.createLegacyBlock(), 0x3 | (movedByPiston ? 64 : 0));
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
/*     */   public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
/* 301 */     BlockState blockState = getBlockState(pos);
/* 302 */     if (blockState.isAir()) {
/* 303 */       return false;
/*     */     }
/*     */     
/* 306 */     FluidState fluidState = getFluidState(pos);
/* 307 */     if (!(blockState.getBlock() instanceof net.minecraft.world.level.block.BaseFireBlock)) {
/* 308 */       levelEvent(2001, pos, Block.getId(blockState));
/*     */     }
/* 310 */     if (dropResources) {
/* 311 */       BlockEntity blockEntity = blockState.hasBlockEntity() ? getBlockEntity(pos) : null;
/* 312 */       Block.dropResources(blockState, this, pos, blockEntity, breaker, ItemStack.EMPTY);
/*     */     } 
/*     */     
/* 315 */     boolean destroyed = setBlock(pos, fluidState.createLegacyBlock(), 3, updateLimit);
/*     */     
/* 317 */     if (destroyed) {
/* 318 */       gameEvent((Holder<GameEvent>)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(breaker, blockState));
/*     */     }
/*     */     
/* 321 */     return destroyed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDestroyBlockEffect(BlockPos pos, BlockState blockState) {}
/*     */   
/*     */   public boolean setBlockAndUpdate(BlockPos pos, BlockState blockState) {
/* 328 */     return setBlock(pos, blockState, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlocksDirty(BlockPos pos, BlockState oldState, BlockState newState) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNeighborsAt(BlockPos pos, Block sourceBlock, Orientation orientation) {}
/*     */ 
/*     */   
/*     */   public void updateNeighborsAtExceptFromFacing(BlockPos pos, Block blockObject, Direction skipDirection, Orientation orientation) {}
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) {}
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {}
/*     */ 
/*     */   
/*     */   public void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, @Block.UpdateFlags int updateFlags, int updateLimit) {
/* 350 */     this.neighborUpdater.shapeUpdate(direction, neighborState, pos, neighborPos, updateFlags, updateLimit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/*     */     int y;
/* 356 */     if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000) {
/* 357 */       y = getSeaLevel() + 1;
/* 358 */     } else if (hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))) {
/* 359 */       y = getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)).getHeight(type, x & 0xF, z & 0xF) + 1;
/*     */     } else {
/* 361 */       y = getMinY();
/*     */     } 
/* 363 */     return y;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/* 368 */     return getChunkSource().getLightEngine();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 373 */     if (!isInValidBounds(pos)) {
/* 374 */       return Blocks.VOID_AIR.defaultBlockState();
/*     */     }
/* 376 */     LevelChunk chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/* 377 */     return chunk.getBlockState(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 382 */     if (!isInValidBounds(pos)) {
/* 383 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/* 385 */     LevelChunk chunk = getChunkAt(pos);
/* 386 */     return chunk.getFluidState(pos);
/*     */   }
/*     */   
/*     */   public boolean isBrightOutside() {
/* 390 */     return (!dimensionType().hasFixedTime() && this.skyDarken < 4);
/*     */   }
/*     */   
/*     */   public boolean isDarkOutside() {
/* 394 */     return (!dimensionType().hasFixedTime() && !isBrightOutside());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
/* 400 */     playSound(except, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, sound, source, volume, pitch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSeededSound(Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, long seed) {
/* 407 */     playSeededSound(except, x, y, z, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, volume, pitch, seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, double x, double y, double z, SoundEvent sound, SoundSource source) {
/* 415 */     playSound(except, x, y, z, sound, source, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch) {
/* 420 */     playSeededSound(except, x, y, z, sound, source, volume, pitch, this.threadSafeRandom.nextLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
/* 425 */     playSeededSound(except, x, y, z, sound, source, volume, pitch, this.threadSafeRandom.nextLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public void playSound(Entity except, Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {
/* 430 */     playSeededSound(except, sourceEntity, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, volume, pitch, this.threadSafeRandom.nextLong());
/*     */   }
/*     */   
/*     */   public void playLocalSound(BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
/* 434 */     playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, sound, source, volume, pitch, distanceDelay);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playLocalSound(Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {}
/*     */ 
/*     */   
/*     */   public void playPlayerSound(SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */ 
/*     */   
/*     */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */ 
/*     */   
/*     */   public void addParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, double xd, double yd, double zd) {}
/*     */ 
/*     */   
/*     */   public void addAlwaysVisibleParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */ 
/*     */   
/*     */   public void addAlwaysVisibleParticle(ParticleOptions particle, boolean overrideLimiter, double x, double y, double z, double xd, double yd, double zd) {}
/*     */ 
/*     */   
/*     */   public void addBlockEntityTicker(TickingBlockEntity ticker) {
/* 462 */     (this.tickingBlockEntities ? this.pendingBlockEntityTickers : this.blockEntityTickers).add(ticker);
/*     */   }
/*     */   
/*     */   public void tickBlockEntities() {
/* 466 */     this.tickingBlockEntities = true;
/*     */     
/* 468 */     if (!this.pendingBlockEntityTickers.isEmpty()) {
/* 469 */       this.blockEntityTickers.addAll(this.pendingBlockEntityTickers);
/* 470 */       this.pendingBlockEntityTickers.clear();
/*     */     } 
/*     */     
/* 473 */     Iterator<TickingBlockEntity> iterator = this.blockEntityTickers.iterator();
/* 474 */     boolean tickBlockEntities = tickRateManager().runsNormally();
/* 475 */     while (iterator.hasNext()) {
/* 476 */       TickingBlockEntity ticker = iterator.next();
/* 477 */       if (ticker.isRemoved()) {
/* 478 */         iterator.remove(); continue;
/*     */       } 
/* 480 */       if (tickBlockEntities && shouldTickBlocksAt(ticker.getPos())) {
/* 481 */         ticker.tick();
/*     */       }
/*     */     } 
/*     */     
/* 485 */     this.tickingBlockEntities = false;
/*     */   }
/*     */   
/*     */   public <T extends Entity> void guardEntityTick(Consumer<T> tick, T entity) {
/*     */     try {
/* 490 */       tick.accept(entity);
/* 491 */     } catch (Throwable t) {
/* 492 */       CrashReport report = CrashReport.forThrowable(t, "Ticking entity");
/* 493 */       CrashReportCategory category = report.addCategory("Entity being ticked");
/*     */       
/* 495 */       entity.fillCrashReportCategory(category);
/*     */       
/* 497 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldTickDeath(Entity entity) {
/* 502 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldTickBlocksAt(long chunkPos) {
/* 506 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldTickBlocksAt(BlockPos pos) {
/* 510 */     return shouldTickBlocksAt(ChunkPos.asLong(pos));
/*     */   }
/*     */   
/*     */   public enum ExplosionInteraction implements StringRepresentable {
/* 514 */     NONE("none"),
/* 515 */     BLOCK("block"),
/* 516 */     MOB("mob"),
/* 517 */     TNT("tnt"),
/* 518 */     TRIGGER("trigger");
/*     */     
/* 520 */     public static final Codec<ExplosionInteraction> CODEC = (Codec<ExplosionInteraction>)StringRepresentable.fromEnum(ExplosionInteraction::values);
/*     */     
/*     */     private final String id;
/*     */     
/*     */     ExplosionInteraction(String id) {
/* 525 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 530 */       return this.id;
/*     */     }
/*     */   }
/*     */   
/*     */   public void explode(Entity source, double x, double y, double z, float r, ExplosionInteraction blockInteraction) {
/* 535 */     explode(source, Explosion.getDefaultDamageSource(this, source), null, x, y, z, r, false, blockInteraction, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public void explode(Entity source, double x, double y, double z, float r, boolean fire, ExplosionInteraction blockInteraction) {
/* 539 */     explode(source, Explosion.getDefaultDamageSource(this, source), null, x, y, z, r, fire, blockInteraction, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, Vec3 boomPos, float r, boolean fire, ExplosionInteraction blockInteraction) {
/* 543 */     explode(source, damageSource, damageCalculator, boomPos.x(), boomPos.y(), boomPos.z(), r, fire, blockInteraction, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, ExplosionInteraction interactionType) {
/* 547 */     explode(source, damageSource, damageCalculator, x, y, z, r, fire, interactionType, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 556 */     if (!isInValidBounds(pos)) {
/* 557 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 561 */     if (!isClientSide() && Thread.currentThread() != this.thread) {
/* 562 */       return null;
/*     */     }
/*     */     
/* 565 */     return getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
/*     */   }
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/* 569 */     BlockPos pos = blockEntity.getBlockPos();
/* 570 */     if (!isInValidBounds(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 574 */     getChunkAt(pos).addAndRegisterBlockEntity(blockEntity);
/*     */   }
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {
/* 578 */     if (!isInValidBounds(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 582 */     getChunkAt(pos).removeBlockEntity(pos);
/*     */   }
/*     */   
/*     */   public boolean isLoaded(BlockPos pos) {
/* 586 */     if (!isInValidBounds(pos)) {
/* 587 */       return false;
/*     */     }
/* 589 */     return getChunkSource().hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadedAndEntityCanStandOnFace(BlockPos pos, Entity entity, Direction faceDirection) {
/* 598 */     if (!isInValidBounds(pos)) {
/* 599 */       return false;
/*     */     }
/*     */     
/* 602 */     ChunkAccess chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
/* 603 */     if (chunk == null) {
/* 604 */       return false;
/*     */     }
/*     */     
/* 607 */     return chunk.getBlockState(pos).entityCanStandOnFace(this, pos, entity, faceDirection);
/*     */   }
/*     */   
/*     */   public boolean loadedAndEntityCanStandOn(BlockPos pos, Entity entity) {
/* 611 */     return loadedAndEntityCanStandOnFace(pos, entity, Direction.UP);
/*     */   }
/*     */   
/*     */   public void updateSkyBrightness() {
/* 615 */     this.skyDarken = (int)(15.0F - (Float)environmentAttributes().getDimensionValue(EnvironmentAttributes.SKY_LIGHT_LEVEL));
/*     */   }
/*     */   
/*     */   public void setSpawnSettings(boolean spawnEnemies) {
/* 619 */     getChunkSource().setSpawnSettings(spawnEnemies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LevelData.RespawnData getWorldBorderAdjustedRespawnData(LevelData.RespawnData respawnData) {
/* 627 */     WorldBorder worldBorder = getWorldBorder();
/* 628 */     if (!worldBorder.isWithinBounds(respawnData.pos())) {
/* 629 */       BlockPos newPos = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(worldBorder.getCenterX(), 0.0D, worldBorder.getCenterZ()));
/* 630 */       return LevelData.RespawnData.of(respawnData.dimension(), newPos, respawnData.yaw(), respawnData.pitch());
/*     */     } 
/* 632 */     return respawnData;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void prepareWeather() {
/* 637 */     if (this.levelData.isRaining()) {
/* 638 */       this.rainLevel = 1.0F;
/* 639 */       if (this.levelData.isThundering()) {
/* 640 */         this.thunderLevel = 1.0F;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 647 */     getChunkSource().close();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
/* 652 */     return (BlockGetter)getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) {
/* 657 */     Profiler.get().incrementCounter("getEntities");
/*     */     
/* 659 */     List<Entity> output = Lists.newArrayList();
/* 660 */     getEntities().get(bb, entity -> {
/*     */           if (entity != except && selector.test(entity)) {
/*     */             output.add(entity);
/*     */           }
/*     */         });
/*     */     
/* 666 */     for (EnderDragonPart dragonPart : dragonParts()) {
/* 667 */       if (dragonPart != except && dragonPart.parentMob != except && 
/* 668 */         selector.test(dragonPart) && 
/* 669 */         bb.intersects(dragonPart.getBoundingBox())) {
/* 670 */         output.add(dragonPart);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 676 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 681 */     List<T> output = Lists.newArrayList();
/* 682 */     getEntities(type, bb, selector, output);
/* 683 */     return output;
/*     */   }
/*     */   
/*     */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector, List<? super T> output) {
/* 687 */     getEntities(type, bb, selector, output, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector, List<? super T> output, int maxResults) {
/* 691 */     Profiler.get().incrementCounter("getEntities");
/*     */     
/* 693 */     getEntities().get(type, bb, e -> {
/*     */           if (selector.test(e)) {
/*     */             output.add(e);
/*     */             if (output.size() >= maxResults) {
/*     */               return AbortableIterationConsumer.Continuation.ABORT;
/*     */             }
/*     */           } 
/*     */           if (e instanceof EnderDragon) {
/*     */             EnderDragon enderDragon = (EnderDragon)e;
/*     */             for (EnderDragonPart subEntity : enderDragon.getSubEntities()) {
/*     */               Entity entity = (Entity)type.tryCast(subEntity);
/*     */               if (entity != null && selector.test(entity)) {
/*     */                 output.add(entity);
/*     */                 if (output.size() >= maxResults) {
/*     */                   return AbortableIterationConsumer.Continuation.ABORT;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           return AbortableIterationConsumer.Continuation.CONTINUE;
/*     */         });
/*     */   }
/*     */   
/*     */   public <T extends Entity> boolean hasEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 717 */     Profiler.get().incrementCounter("hasEntities");
/*     */     
/* 719 */     MutableBoolean hasEntities = new MutableBoolean();
/* 720 */     getEntities().get(type, bb, e -> {
/*     */           if (selector.test(e)) {
/*     */             hasEntities.setTrue();
/*     */             return AbortableIterationConsumer.Continuation.ABORT;
/*     */           } 
/*     */           if (e instanceof EnderDragon) {
/*     */             EnderDragon enderDragon = (EnderDragon)e;
/*     */             for (EnderDragonPart subEntity : enderDragon.getSubEntities()) {
/*     */               Entity entity = (Entity)type.tryCast(subEntity);
/*     */               if (entity != null && selector.test(entity)) {
/*     */                 hasEntities.setTrue();
/*     */                 return AbortableIterationConsumer.Continuation.ABORT;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           return AbortableIterationConsumer.Continuation.CONTINUE;
/*     */         });
/* 737 */     return hasEntities.isTrue();
/*     */   }
/*     */   
/*     */   public List<Entity> getPushableEntities(Entity pusher, AABB boundingBox) {
/* 741 */     return getEntities(pusher, boundingBox, EntitySelector.pushableBy(pusher));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getEntity(UUID uuid) {
/* 747 */     return (Entity)getEntities().get(uuid);
/*     */   }
/*     */   
/*     */   public Entity getEntityInAnyDimension(UUID uuid) {
/* 751 */     return getEntity(uuid);
/*     */   }
/*     */   
/*     */   public Player getPlayerInAnyDimension(UUID uuid) {
/* 755 */     return getPlayerByUUID(uuid);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void blockEntityChanged(BlockPos pos) {
/* 761 */     if (hasChunkAt(pos)) {
/* 762 */       getChunkAt(pos).markUnsaved();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockEntityAdded(BlockEntity blockEntity) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDayTime() {
/* 774 */     return this.levelData.getDayTime();
/*     */   }
/*     */   
/*     */   public boolean mayInteract(Entity entity, BlockPos pos) {
/* 778 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void broadcastEntityEvent(Entity entity, byte event) {}
/*     */ 
/*     */   
/*     */   public void broadcastDamageEvent(Entity entity, DamageSource source) {}
/*     */   
/*     */   public void blockEvent(BlockPos pos, Block block, int b0, int b1) {
/* 788 */     getBlockState(pos).triggerEvent(this, pos, b0, b1);
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelData getLevelData() {
/* 793 */     return (LevelData)this.levelData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getThunderLevel(float a) {
/* 799 */     return Mth.lerp(a, this.oThunderLevel, this.thunderLevel) * getRainLevel(a);
/*     */   }
/*     */   
/*     */   public void setThunderLevel(float thunderLevel) {
/* 803 */     float clampedThunderLevel = Mth.clamp(thunderLevel, 0.0F, 1.0F);
/* 804 */     this.oThunderLevel = clampedThunderLevel;
/* 805 */     this.thunderLevel = clampedThunderLevel;
/*     */   }
/*     */   
/*     */   public float getRainLevel(float a) {
/* 809 */     return Mth.lerp(a, this.oRainLevel, this.rainLevel);
/*     */   }
/*     */   
/*     */   public void setRainLevel(float rainLevel) {
/* 813 */     float clampedRainLevel = Mth.clamp(rainLevel, 0.0F, 1.0F);
/* 814 */     this.oRainLevel = clampedRainLevel;
/* 815 */     this.rainLevel = clampedRainLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHaveWeather() {
/* 820 */     return (dimensionType().hasSkyLight() && !dimensionType().hasCeiling() && dimension() != END);
/*     */   }
/*     */   
/*     */   public boolean isThundering() {
/* 824 */     return (canHaveWeather() && getThunderLevel(1.0F) > 0.9D);
/*     */   }
/*     */   
/*     */   public boolean isRaining() {
/* 828 */     return (canHaveWeather() && getRainLevel(1.0F) > 0.2D);
/*     */   }
/*     */   
/*     */   public boolean isRainingAt(BlockPos pos) {
/* 832 */     return (precipitationAt(pos) == Biome.Precipitation.RAIN);
/*     */   }
/*     */   
/*     */   public Biome.Precipitation precipitationAt(BlockPos pos) {
/* 836 */     if (!isRaining()) {
/* 837 */       return Biome.Precipitation.NONE;
/*     */     }
/* 839 */     if (!canSeeSky(pos)) {
/* 840 */       return Biome.Precipitation.NONE;
/*     */     }
/* 842 */     if (getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
/* 843 */       return Biome.Precipitation.NONE;
/*     */     }
/*     */     
/* 846 */     Biome biome = (Biome)getBiome(pos).value();
/*     */     
/* 848 */     return biome.getPrecipitationAt(pos, getSeaLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void globalLevelEvent(int type, BlockPos pos, int data) {}
/*     */ 
/*     */   
/*     */   public CrashReportCategory fillReportDetails(CrashReport report) {
/* 857 */     CrashReportCategory category = report.addCategory("Affected level", 1);
/*     */     
/* 859 */     category.setDetail("All players", () -> {
/*     */           List<? extends Player> players = players();
/*     */           return "" + players.size() + " total; " + players.size();
/*     */         });
/* 863 */     Objects.requireNonNull(getChunkSource()); category.setDetail("Chunk stats", getChunkSource()::gatherStats);
/* 864 */     category.setDetail("Level dimension", () -> dimension().identifier().toString());
/*     */     
/*     */     try {
/* 867 */       this.levelData.fillCrashReportCategory(category, this);
/* 868 */     } catch (Throwable t) {
/* 869 */       category.setDetailError("Level Data Unobtainable", t);
/*     */     } 
/*     */     
/* 872 */     return category;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createFireworks(double x, double y, double z, double xd, double yd, double zd, List<FireworkExplosion> explosions) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNeighbourForOutputSignal(BlockPos pos, Block changedBlock) {
/* 883 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 884 */       BlockPos relativePos = pos.relative(direction);
/*     */       
/* 886 */       if (hasChunkAt(relativePos)) {
/* 887 */         BlockState state = getBlockState(relativePos);
/* 888 */         if (state.is(Blocks.COMPARATOR)) {
/* 889 */           neighborChanged(state, relativePos, changedBlock, null, false); continue;
/* 890 */         }  if (state.isRedstoneConductor(this, relativePos)) {
/* 891 */           relativePos = relativePos.relative(direction);
/* 892 */           state = getBlockState(relativePos);
/*     */           
/* 894 */           if (state.is(Blocks.COMPARATOR)) {
/* 895 */             neighborChanged(state, relativePos, changedBlock, null, false);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkyDarken() {
/* 904 */     return this.skyDarken;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSkyFlashTime(int skyFlashTime) {}
/*     */   
/*     */   public void sendPacketToServer(Packet<?> packet) {
/* 911 */     throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
/*     */   }
/*     */ 
/*     */   
/*     */   public DimensionType dimensionType() {
/* 916 */     return (DimensionType)this.dimensionTypeRegistration.value();
/*     */   }
/*     */   
/*     */   public Holder<DimensionType> dimensionTypeRegistration() {
/* 920 */     return this.dimensionTypeRegistration;
/*     */   }
/*     */   
/*     */   public ResourceKey<Level> dimension() {
/* 924 */     return this.dimension;
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource getRandom() {
/* 929 */     return this.random;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
/* 934 */     return predicate.test(getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
/* 939 */     return predicate.test(getFluidState(pos));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getBlockRandomPos(int xo, int yo, int zo, int yMask) {
/* 945 */     this.randValue = this.randValue * 3 + 1013904223;
/* 946 */     int val = this.randValue >> 2;
/*     */     
/* 948 */     return new BlockPos(xo + (val & 0xF), yo + (val >> 16 & yMask), zo + (val >> 8 & 0xF));
/*     */   }
/*     */   
/*     */   public boolean noSave() {
/* 952 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeManager getBiomeManager() {
/* 957 */     return this.biomeManager;
/*     */   }
/*     */   
/*     */   public final boolean isDebug() {
/* 961 */     return this.isDebug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long nextSubTickCount() {
/* 968 */     return this.subTickCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 973 */     return this.registryAccess;
/*     */   }
/*     */   
/*     */   public DamageSources damageSources() {
/* 977 */     return this.damageSources;
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
/*     */   public int getClientLeafTintColor(BlockPos pos) {
/* 990 */     return 0;
/*     */   }
/*     */   
/*     */   public PalettedContainerFactory palettedContainerFactory() {
/* 994 */     return this.palettedContainerFactory;
/*     */   }
/*     */   
/*     */   public abstract void sendBlockUpdated(BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, @Block.UpdateFlags int paramInt);
/*     */   
/*     */   public abstract void playSeededSound(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, Holder<SoundEvent> paramHolder, SoundSource paramSoundSource, float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public abstract void playSeededSound(Entity paramEntity1, Entity paramEntity2, Holder<SoundEvent> paramHolder, SoundSource paramSoundSource, float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public abstract void explode(Entity paramEntity, DamageSource paramDamageSource, ExplosionDamageCalculator paramExplosionDamageCalculator, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, boolean paramBoolean, ExplosionInteraction paramExplosionInteraction, ParticleOptions paramParticleOptions1, ParticleOptions paramParticleOptions2, WeightedList<ExplosionParticleInfo> paramWeightedList, Holder<SoundEvent> paramHolder);
/*     */   
/*     */   public abstract String gatherChunkSourceStats();
/*     */   
/*     */   public abstract void setRespawnData(LevelData.RespawnData paramRespawnData);
/*     */   
/*     */   public abstract LevelData.RespawnData getRespawnData();
/*     */   
/*     */   public abstract Entity getEntity(int paramInt);
/*     */   
/*     */   public abstract Collection<EnderDragonPart> dragonParts();
/*     */   
/*     */   public abstract TickRateManager tickRateManager();
/*     */   
/*     */   public abstract MapItemSavedData getMapData(MapId paramMapId);
/*     */   
/*     */   public abstract void destroyBlockProgress(int paramInt1, BlockPos paramBlockPos, int paramInt2);
/*     */   
/*     */   public abstract Scoreboard getScoreboard();
/*     */   
/*     */   public abstract RecipeAccess recipeAccess();
/*     */   
/*     */   protected abstract LevelEntityGetter<Entity> getEntities();
/*     */   
/*     */   public abstract EnvironmentAttributeSystem environmentAttributes();
/*     */   
/*     */   public abstract PotionBrewing potionBrewing();
/*     */   
/*     */   public abstract FuelValues fuelValues();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/Level.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */