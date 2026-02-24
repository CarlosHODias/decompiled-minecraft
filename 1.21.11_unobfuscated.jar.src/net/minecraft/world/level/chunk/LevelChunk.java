/*     */ package net.minecraft.world.level.chunk;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.debug.DebugStructureInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gameevent.EuclideanGameEventListenerRegistry;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.GameEventListenerRegistry;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.ticks.LevelChunkTicks;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LevelChunk extends ChunkAccess implements DebugValueSource {
/*  67 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  69 */   private static final TickingBlockEntity NULL_TICKER = new TickingBlockEntity()
/*     */     {
/*     */       public void tick() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean isRemoved() {
/*  76 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public BlockPos getPos() {
/*  81 */         return BlockPos.ZERO;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getType() {
/*  86 */         return "<null>";
/*     */       }
/*     */     };
/*     */   
/*  90 */   private final Map<BlockPos, RebindableTickingBlockEntityWrapper> tickersInLevel = Maps.newHashMap();
/*     */   
/*     */   private boolean loaded;
/*     */   
/*     */   private final Level level;
/*     */   private Supplier<FullChunkStatus> fullStatus;
/*     */   private PostLoadProcessor postLoad;
/*     */   private final Int2ObjectMap<GameEventListenerRegistry> gameEventListenerRegistrySections;
/*     */   private final LevelChunkTicks<Block> blockTicks;
/*     */   private final LevelChunkTicks<Fluid> fluidTicks;
/*     */   private UnsavedListener unsavedListener = chunkPos -> {
/*     */     
/*     */     };
/*     */   
/*     */   public LevelChunk(Level level, ChunkPos pos) {
/* 105 */     this(level, pos, UpgradeData.EMPTY, new LevelChunkTicks(), new LevelChunkTicks(), 0L, null, null, null);
/*     */   }
/*     */   
/*     */   public LevelChunk(Level level, ChunkPos pos, UpgradeData upgradeData, LevelChunkTicks<Block> blockTicks, LevelChunkTicks<Fluid> fluidTicks, long inhabitedTime, LevelChunkSection[] sections, PostLoadProcessor postLoad, BlendingData blendingData) {
/* 109 */     super(pos, upgradeData, (LevelHeightAccessor)level, level.palettedContainerFactory(), inhabitedTime, sections, blendingData);
/* 110 */     this.level = level;
/*     */     
/* 112 */     this.gameEventListenerRegistrySections = (Int2ObjectMap<GameEventListenerRegistry>)new Int2ObjectOpenHashMap();
/*     */     
/* 114 */     for (Heightmap.Types type : Heightmap.Types.values()) {
/* 115 */       if (ChunkStatus.FULL.heightmapsAfter().contains(type)) {
/* 116 */         this.heightmaps.put(type, new Heightmap(this, type));
/*     */       }
/*     */     } 
/*     */     
/* 120 */     this.postLoad = postLoad;
/* 121 */     this.blockTicks = blockTicks;
/* 122 */     this.fluidTicks = fluidTicks;
/*     */   }
/*     */   
/*     */   public LevelChunk(ServerLevel level, ProtoChunk protoChunk, PostLoadProcessor postLoad) {
/* 126 */     this((Level)level, protoChunk.getPos(), protoChunk.getUpgradeData(), protoChunk.unpackBlockTicks(), protoChunk.unpackFluidTicks(), protoChunk.getInhabitedTime(), protoChunk.getSections(), postLoad, protoChunk.getBlendingData());
/*     */     
/* 128 */     if (!Collections.disjoint(protoChunk.pendingBlockEntities.keySet(), protoChunk.blockEntities.keySet())) {
/* 129 */       LOGGER.error("Chunk at {} contains duplicated block entities", protoChunk.getPos());
/*     */     }
/*     */     
/* 132 */     for (BlockEntity blockEntity : protoChunk.getBlockEntities().values()) {
/* 133 */       setBlockEntity(blockEntity);
/*     */     }
/*     */     
/* 136 */     this.pendingBlockEntities.putAll(protoChunk.getBlockEntityNbts());
/*     */     
/* 138 */     for (int i = 0; i < (protoChunk.getPostProcessing()).length; i++) {
/* 139 */       this.postProcessing[i] = protoChunk.getPostProcessing()[i];
/*     */     }
/*     */     
/* 142 */     setAllStarts(protoChunk.getAllStarts());
/* 143 */     setAllReferences(protoChunk.getAllReferences());
/*     */     
/* 145 */     for (Map.Entry<Heightmap.Types, Heightmap> entry : protoChunk.getHeightmaps()) {
/* 146 */       if (ChunkStatus.FULL.heightmapsAfter().contains(entry.getKey())) {
/* 147 */         setHeightmap(entry.getKey(), ((Heightmap)entry.getValue()).getRawData());
/*     */       }
/*     */     } 
/*     */     
/* 151 */     this.skyLightSources = protoChunk.skyLightSources;
/* 152 */     setLightCorrect(protoChunk.isLightCorrect());
/*     */     
/* 154 */     markUnsaved();
/*     */   }
/*     */   
/*     */   public void setUnsavedListener(UnsavedListener unsavedListener) {
/* 158 */     this.unsavedListener = unsavedListener;
/* 159 */     if (isUnsaved()) {
/* 160 */       unsavedListener.setUnsaved(this.chunkPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void markUnsaved() {
/* 166 */     boolean wasUnsaved = isUnsaved();
/* 167 */     super.markUnsaved();
/* 168 */     if (!wasUnsaved) {
/* 169 */       this.unsavedListener.setUnsaved(this.chunkPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Block> getBlockTicks() {
/* 175 */     return (TickContainerAccess<Block>)this.blockTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Fluid> getFluidTicks() {
/* 180 */     return (TickContainerAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess.PackedTicks getTicksForSerialization(long currentTick) {
/* 185 */     return new ChunkAccess.PackedTicks(this.blockTicks.pack(currentTick), this.fluidTicks.pack(currentTick));
/*     */   }
/*     */ 
/*     */   
/*     */   public GameEventListenerRegistry getListenerRegistry(int section) {
/* 190 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 191 */       return (GameEventListenerRegistry)this.gameEventListenerRegistrySections.computeIfAbsent(section, key -> new EuclideanGameEventListenerRegistry(serverLevel, serverLevel, this::removeGameEventListenerRegistry)); }
/*     */     
/* 193 */     return super.getListenerRegistry(section);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 198 */     int x = pos.getX();
/* 199 */     int y = pos.getY();
/* 200 */     int z = pos.getZ();
/* 201 */     if (this.level.isDebug()) {
/* 202 */       BlockState blockState = null;
/* 203 */       if (y == 60) {
/* 204 */         blockState = Blocks.BARRIER.defaultBlockState();
/*     */       }
/* 206 */       if (y == 70) {
/* 207 */         blockState = DebugLevelSource.getBlockStateFor(x, z);
/*     */       }
/* 209 */       return (blockState == null) ? Blocks.AIR.defaultBlockState() : blockState;
/*     */     } 
/*     */     
/*     */     try {
/* 213 */       int sectionIndex = getSectionIndex(y);
/* 214 */       if (sectionIndex >= 0 && sectionIndex < this.sections.length) {
/* 215 */         LevelChunkSection currentSection = this.sections[sectionIndex];
/* 216 */         if (!currentSection.hasOnlyAir()) {
/* 217 */           return currentSection.getBlockState(x & 0xF, y & 0xF, z & 0xF);
/*     */         }
/*     */       } 
/* 220 */       return Blocks.AIR.defaultBlockState();
/* 221 */     } catch (Throwable t) {
/* 222 */       CrashReport report = CrashReport.forThrowable(t, "Getting block state");
/* 223 */       CrashReportCategory category = report.addCategory("Block being got");
/* 224 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, x, y, z));
/* 225 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 231 */     return getFluidState(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(int x, int y, int z) {
/*     */     try {
/* 236 */       int sectionIndex = getSectionIndex(y);
/* 237 */       if (sectionIndex >= 0 && sectionIndex < this.sections.length) {
/* 238 */         LevelChunkSection currentSection = this.sections[sectionIndex];
/* 239 */         if (!currentSection.hasOnlyAir()) {
/* 240 */           return currentSection.getFluidState(x & 0xF, y & 0xF, z & 0xF);
/*     */         }
/*     */       } 
/* 243 */       return Fluids.EMPTY.defaultFluidState();
/* 244 */     } catch (Throwable t) {
/* 245 */       CrashReport report = CrashReport.forThrowable(t, "Getting fluid state");
/* 246 */       CrashReportCategory category = report.addCategory("Block being got");
/* 247 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, x, y, z));
/* 248 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState setBlockState(BlockPos pos, BlockState state, @Block.UpdateFlags int flags) {
/* 254 */     int y = pos.getY();
/*     */     
/* 256 */     LevelChunkSection section = getSection(getSectionIndex(y));
/* 257 */     boolean wasEmpty = section.hasOnlyAir();
/*     */     
/* 259 */     if (wasEmpty && state.isAir()) {
/* 260 */       return null;
/*     */     }
/*     */     
/* 263 */     int localX = pos.getX() & 0xF;
/* 264 */     int localY = y & 0xF;
/* 265 */     int localZ = pos.getZ() & 0xF;
/* 266 */     BlockState oldState = section.setBlockState(localX, localY, localZ, state);
/*     */     
/* 268 */     if (oldState == state) {
/* 269 */       return null;
/*     */     }
/*     */     
/* 272 */     Block newBlock = state.getBlock();
/*     */     
/* 274 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING)).update(localX, y, localZ, state);
/* 275 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)).update(localX, y, localZ, state);
/* 276 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR)).update(localX, y, localZ, state);
/* 277 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.WORLD_SURFACE)).update(localX, y, localZ, state);
/* 278 */     boolean isEmpty = section.hasOnlyAir();
/* 279 */     if (wasEmpty != isEmpty) {
/* 280 */       this.level.getChunkSource().getLightEngine().updateSectionStatus(pos, isEmpty);
/* 281 */       this.level.getChunkSource().onSectionEmptinessChanged(this.chunkPos.x, SectionPos.blockToSectionCoord(y), this.chunkPos.z, isEmpty);
/*     */     } 
/*     */     
/* 284 */     if (LightEngine.hasDifferentLightProperties(oldState, state)) {
/* 285 */       ProfilerFiller profiler = Profiler.get();
/* 286 */       profiler.push("updateSkyLightSources");
/* 287 */       this.skyLightSources.update(this, localX, y, localZ);
/* 288 */       profiler.popPush("queueCheckLight");
/* 289 */       this.level.getChunkSource().getLightEngine().checkBlock(pos);
/* 290 */       profiler.pop();
/*     */     } 
/*     */     
/* 293 */     boolean blockChanged = !oldState.is(newBlock);
/* 294 */     boolean movedByPiston = ((flags & 0x40) != 0);
/* 295 */     boolean sideEffects = ((flags & 0x100) == 0);
/*     */     
/* 297 */     if (blockChanged && oldState.hasBlockEntity() && !state.shouldChangedStateKeepBlockEntity(oldState)) {
/* 298 */       if (!this.level.isClientSide() && sideEffects) {
/* 299 */         BlockEntity blockEntity = this.level.getBlockEntity(pos);
/* 300 */         if (blockEntity != null) {
/* 301 */           blockEntity.preRemoveSideEffects(pos, oldState);
/*     */         }
/*     */       } 
/* 304 */       removeBlockEntity(pos);
/*     */     } 
/*     */     
/* 307 */     if (blockChanged || newBlock instanceof net.minecraft.world.level.block.BaseRailBlock) {
/* 308 */       Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 316 */         if ((flags & 0x1) != 0 || movedByPiston) {
/* 317 */           oldState.affectNeighborsAfterRemoval(serverLevel, pos, movedByPiston);
/*     */         } }
/*     */     
/*     */     } 
/*     */     
/* 322 */     if (!section.getBlockState(localX, localY, localZ).is(newBlock)) {
/* 323 */       return null;
/*     */     }
/*     */     
/* 326 */     if (!this.level.isClientSide() && (flags & 0x200) == 0) {
/* 327 */       state.onPlace(this.level, pos, oldState, movedByPiston);
/*     */     }
/* 329 */     if (state.hasBlockEntity()) {
/* 330 */       BlockEntity blockEntity = getBlockEntity(pos, EntityCreationType.CHECK);
/*     */ 
/*     */ 
/*     */       
/* 334 */       if (blockEntity != null && !blockEntity.isValidBlockState(state)) {
/* 335 */         LOGGER.warn("Found mismatched block entity @ {}: type = {}, state = {}", new Object[] { pos, blockEntity.getType().builtInRegistryHolder().key().identifier(), state });
/* 336 */         removeBlockEntity(pos);
/* 337 */         blockEntity = null;
/*     */       } 
/*     */       
/* 340 */       if (blockEntity == null) {
/* 341 */         blockEntity = ((EntityBlock)newBlock).newBlockEntity(pos, state);
/* 342 */         if (blockEntity != null) {
/* 343 */           addAndRegisterBlockEntity(blockEntity);
/*     */         }
/*     */       } else {
/* 346 */         blockEntity.setBlockState(state);
/* 347 */         updateBlockEntityTicker(blockEntity);
/*     */       } 
/*     */     } 
/*     */     
/* 351 */     markUnsaved();
/* 352 */     return oldState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void addEntity(Entity entity) {}
/*     */ 
/*     */   
/*     */   private BlockEntity createBlockEntity(BlockPos pos) {
/* 362 */     BlockState state = getBlockState(pos);
/* 363 */     if (!state.hasBlockEntity()) {
/* 364 */       return null;
/*     */     }
/*     */     
/* 367 */     return ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 372 */     return getBlockEntity(pos, EntityCreationType.CHECK);
/*     */   }
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos, EntityCreationType creationType) {
/* 376 */     BlockEntity blockEntity = this.blockEntities.get(pos);
/* 377 */     if (blockEntity == null) {
/* 378 */       CompoundTag tag = this.pendingBlockEntities.remove(pos);
/* 379 */       if (tag != null) {
/* 380 */         BlockEntity promoted = promotePendingBlockEntity(pos, tag);
/* 381 */         if (promoted != null) {
/* 382 */           return promoted;
/*     */         }
/*     */       } 
/*     */     } 
/* 386 */     if (blockEntity == null) {
/* 387 */       if (creationType == EntityCreationType.IMMEDIATE) {
/* 388 */         blockEntity = createBlockEntity(pos);
/* 389 */         if (blockEntity != null) {
/* 390 */           addAndRegisterBlockEntity(blockEntity);
/*     */         }
/*     */       } 
/* 393 */     } else if (blockEntity.isRemoved()) {
/* 394 */       this.blockEntities.remove(pos);
/* 395 */       return null;
/*     */     } 
/*     */     
/* 398 */     return blockEntity;
/*     */   }
/*     */   
/*     */   public void addAndRegisterBlockEntity(BlockEntity blockEntity) {
/* 402 */     setBlockEntity(blockEntity);
/*     */     
/* 404 */     if (isInLevel()) {
/* 405 */       Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 406 */         addGameEventListener(blockEntity, serverLevel); }
/*     */       
/* 408 */       this.level.onBlockEntityAdded(blockEntity);
/* 409 */       updateBlockEntityTicker(blockEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isInLevel() {
/* 414 */     return (this.loaded || this.level.isClientSide());
/*     */   }
/*     */   
/*     */   private boolean isTicking(BlockPos pos) {
/* 418 */     if (!this.level.getWorldBorder().isWithinBounds(pos)) {
/* 419 */       return false;
/*     */     }
/*     */     
/* 422 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 423 */       return (getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING) && 
/* 424 */         serverLevel.areEntitiesLoaded(ChunkPos.asLong(pos))); }
/*     */ 
/*     */     
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/* 432 */     BlockPos pos = blockEntity.getBlockPos();
/* 433 */     BlockState blockState = getBlockState(pos);
/*     */ 
/*     */     
/* 436 */     if (!blockState.hasBlockEntity()) {
/* 437 */       LOGGER.warn("Trying to set block entity {} at position {}, but state {} does not allow it", new Object[] { blockEntity, pos, blockState });
/*     */       
/*     */       return;
/*     */     } 
/* 441 */     BlockState cachedBlockState = blockEntity.getBlockState();
/* 442 */     if (blockState != cachedBlockState) {
/*     */       
/* 444 */       if (!blockEntity.getType().isValid(blockState)) {
/* 445 */         LOGGER.warn("Trying to set block entity {} at position {}, but state {} does not allow it", new Object[] { blockEntity, pos, blockState });
/*     */         
/*     */         return;
/*     */       } 
/* 449 */       if (blockState.getBlock() != cachedBlockState.getBlock())
/*     */       {
/* 451 */         LOGGER.warn("Block state mismatch on block entity {} in position {}, {} != {}, updating", new Object[] { blockEntity, pos, blockState, cachedBlockState });
/*     */       }
/* 453 */       blockEntity.setBlockState(blockState);
/*     */     } 
/*     */     
/* 456 */     blockEntity.setLevel(this.level);
/* 457 */     blockEntity.clearRemoved();
/*     */     
/* 459 */     BlockEntity previousEntry = this.blockEntities.put(pos.immutable(), blockEntity);
/* 460 */     if (previousEntry != null && previousEntry != blockEntity) {
/* 461 */       previousEntry.setRemoved();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos, HolderLookup.Provider registryAccess) {
/* 467 */     BlockEntity blockEntity = getBlockEntity(blockPos);
/* 468 */     if (blockEntity != null && !blockEntity.isRemoved()) {
/* 469 */       CompoundTag compoundTag = blockEntity.saveWithFullMetadata((HolderLookup.Provider)this.level.registryAccess());
/* 470 */       compoundTag.putBoolean("keepPacked", false);
/* 471 */       return compoundTag;
/*     */     } 
/* 473 */     CompoundTag result = this.pendingBlockEntities.get(blockPos);
/* 474 */     if (result != null) {
/* 475 */       result = result.copy();
/* 476 */       result.putBoolean("keepPacked", true);
/*     */     } 
/* 478 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {
/* 483 */     if (isInLevel()) {
/* 484 */       BlockEntity removeThis = this.blockEntities.remove(pos);
/* 485 */       if (removeThis != null) {
/* 486 */         Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 487 */           removeGameEventListener(removeThis, serverLevel);
/* 488 */           serverLevel.debugSynchronizers().dropBlockEntity(pos); }
/*     */         
/* 490 */         removeThis.setRemoved();
/*     */       } 
/*     */     } 
/*     */     
/* 494 */     removeBlockEntityTicker(pos);
/*     */   }
/*     */   
/*     */   private <T extends BlockEntity> void removeGameEventListener(T blockEntity, ServerLevel level) {
/* 498 */     Block block = blockEntity.getBlockState().getBlock();
/*     */     
/* 500 */     if (block instanceof EntityBlock) {
/* 501 */       GameEventListener listener = ((EntityBlock)block).getListener(level, (BlockEntity)blockEntity);
/* 502 */       if (listener != null) {
/* 503 */         int section = SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getY());
/*     */         
/* 505 */         GameEventListenerRegistry listenerRegistry = getListenerRegistry(section);
/* 506 */         listenerRegistry.unregister(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeGameEventListenerRegistry(int sectionY) {
/* 512 */     this.gameEventListenerRegistrySections.remove(sectionY);
/*     */   }
/*     */   
/*     */   private void removeBlockEntityTicker(BlockPos pos) {
/* 516 */     RebindableTickingBlockEntityWrapper ticker = this.tickersInLevel.remove(pos);
/* 517 */     if (ticker != null) {
/* 518 */       ticker.rebind(NULL_TICKER);
/*     */     }
/*     */   }
/*     */   
/*     */   public void runPostLoad() {
/* 523 */     if (this.postLoad != null) {
/* 524 */       this.postLoad.run(this);
/* 525 */       this.postLoad = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 530 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceWithPacketData(FriendlyByteBuf buffer, Map<Heightmap.Types, long[]> heightmaps, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> blockEntities) {
/* 535 */     clearAllBlockEntities();
/*     */     
/* 537 */     for (LevelChunkSection section : this.sections) {
/* 538 */       section.read(buffer);
/*     */     }
/*     */     
/* 541 */     heightmaps.forEach(this::setHeightmap);
/*     */     
/* 543 */     initializeLightSources();
/*     */     
/* 545 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 546 */     try { blockEntities.accept((pos, type, tag) -> {
/*     */             BlockEntity blockEntity = getBlockEntity(reporter, EntityCreationType.IMMEDIATE);
/*     */             
/*     */             if (blockEntity != null && tag != null && blockEntity.getType() == type) {
/*     */               blockEntity.loadWithComponents(TagValueInput.create(reporter.forChild(blockEntity.problemPath()), (HolderLookup.Provider)this.level.registryAccess(), tag));
/*     */             }
/*     */           });
/*     */       
/* 554 */       reporter.close(); }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 558 */      } public void replaceBiomes(FriendlyByteBuf buffer) { for (LevelChunkSection section : this.sections) {
/* 559 */       section.readBiomes(buffer);
/*     */     } }
/*     */ 
/*     */   
/*     */   public void setLoaded(boolean loaded) {
/* 564 */     this.loaded = loaded;
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 568 */     return this.level;
/*     */   }
/*     */   
/*     */   public Map<BlockPos, BlockEntity> getBlockEntities() {
/* 572 */     return this.blockEntities;
/*     */   }
/*     */   
/*     */   public void postProcessGeneration(ServerLevel level) {
/* 576 */     ChunkPos chunkPos = getPos();
/* 577 */     for (int sectionIndex = 0; sectionIndex < this.postProcessing.length; sectionIndex++) {
/* 578 */       ShortList postProcessingSection = this.postProcessing[sectionIndex];
/* 579 */       if (postProcessingSection != null) {
/* 580 */         for (ShortListIterator<Short> shortListIterator = postProcessingSection.iterator(); shortListIterator.hasNext(); ) { Short packedOffset = shortListIterator.next();
/* 581 */           BlockPos blockPos = ProtoChunk.unpackOffsetCoordinates(packedOffset, getSectionYFromSectionIndex(sectionIndex), chunkPos);
/* 582 */           BlockState blockState = getBlockState(blockPos);
/* 583 */           FluidState fluidState = blockState.getFluidState();
/* 584 */           if (!fluidState.isEmpty()) {
/* 585 */             fluidState.tick(level, blockPos, blockState);
/*     */           }
/*     */           
/* 588 */           if (!(blockState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock)) {
/* 589 */             BlockState blockStateNew = Block.updateFromNeighbourShapes(blockState, (LevelAccessor)level, blockPos);
/* 590 */             if (blockStateNew != blockState) {
/* 591 */               level.setBlock(blockPos, blockStateNew, 276);
/*     */             }
/*     */           }  }
/*     */         
/* 595 */         postProcessingSection.clear();
/*     */       } 
/*     */     } 
/*     */     
/* 599 */     for (UnmodifiableIterator<BlockPos> unmodifiableIterator = ImmutableList.copyOf(this.pendingBlockEntities.keySet()).iterator(); unmodifiableIterator.hasNext(); ) { BlockPos pos = unmodifiableIterator.next();
/* 600 */       getBlockEntity(pos); }
/*     */     
/* 602 */     this.pendingBlockEntities.clear();
/* 603 */     this.upgradeData.upgrade(this);
/*     */   }
/*     */   
/*     */   private BlockEntity promotePendingBlockEntity(BlockPos pos, CompoundTag tag) {
/*     */     BlockEntity blockEntity;
/* 608 */     BlockState state = getBlockState(pos);
/* 609 */     if ("DUMMY".equals(tag.getStringOr("id", ""))) {
/* 610 */       if (state.hasBlockEntity()) {
/* 611 */         blockEntity = ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */       } else {
/* 613 */         blockEntity = null;
/* 614 */         LOGGER.warn("Tried to load a DUMMY block entity @ {} but found not block entity block {} at location", pos, state);
/*     */       } 
/*     */     } else {
/* 617 */       blockEntity = BlockEntity.loadStatic(pos, state, tag, (HolderLookup.Provider)this.level.registryAccess());
/*     */     } 
/*     */     
/* 620 */     if (blockEntity != null) {
/* 621 */       blockEntity.setLevel(this.level);
/* 622 */       addAndRegisterBlockEntity(blockEntity);
/*     */     } else {
/* 624 */       LOGGER.warn("Tried to load a block entity for block {} but failed at location {}", state, pos);
/*     */     } 
/*     */     
/* 627 */     return blockEntity;
/*     */   }
/*     */   
/*     */   public void unpackTicks(long currentTick) {
/* 631 */     this.blockTicks.unpack(currentTick);
/* 632 */     this.fluidTicks.unpack(currentTick);
/*     */   }
/*     */   
/*     */   public void registerTickContainerInLevel(ServerLevel level) {
/* 636 */     level.getBlockTicks().addContainer(this.chunkPos, this.blockTicks);
/* 637 */     level.getFluidTicks().addContainer(this.chunkPos, this.fluidTicks);
/*     */   }
/*     */   
/*     */   public void unregisterTickContainerFromLevel(ServerLevel level) {
/* 641 */     level.getBlockTicks().removeContainer(this.chunkPos);
/* 642 */     level.getFluidTicks().removeContainer(this.chunkPos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 648 */     if (!getAllStarts().isEmpty()) {
/* 649 */       registration.register(DebugSubscriptions.STRUCTURES, () -> {
/*     */             List<DebugStructureInfo> structures = new ArrayList<>();
/*     */             for (StructureStart start : getAllStarts().values()) {
/*     */               BoundingBox boundingBox = start.getBoundingBox();
/*     */               List<StructurePiece> pieces = start.getPieces();
/*     */               List<DebugStructureInfo.Piece> pieceInfos = new ArrayList<>(pieces.size());
/*     */               for (int i = 0; i < pieces.size(); i++) {
/*     */                 boolean isStart = (i == 0);
/*     */                 pieceInfos.add(new DebugStructureInfo.Piece(((StructurePiece)pieces.get(i)).getBoundingBox(), isStart));
/*     */               } 
/*     */               structures.add(new DebugStructureInfo(boundingBox, pieceInfos));
/*     */             } 
/*     */             return structures;
/*     */           });
/*     */     }
/* 664 */     registration.register(DebugSubscriptions.RAIDS, () -> level.getRaids().getRaidCentersInChunk(this.chunkPos));
/*     */   }
/*     */   
/*     */   public enum EntityCreationType {
/* 668 */     IMMEDIATE,
/* 669 */     QUEUED,
/* 670 */     CHECK;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 675 */     return ChunkStatus.FULL;
/*     */   }
/*     */   
/*     */   public FullChunkStatus getFullStatus() {
/* 679 */     if (this.fullStatus == null) {
/* 680 */       return FullChunkStatus.FULL;
/*     */     }
/* 682 */     return this.fullStatus.get();
/*     */   }
/*     */   
/*     */   public void setFullStatus(Supplier<FullChunkStatus> fullStatus) {
/* 686 */     this.fullStatus = fullStatus;
/*     */   }
/*     */   
/*     */   public void clearAllBlockEntities() {
/* 690 */     this.blockEntities.values().forEach(BlockEntity::setRemoved);
/* 691 */     this.blockEntities.clear();
/*     */     
/* 693 */     this.tickersInLevel.values().forEach(ticker -> ticker.rebind(NULL_TICKER));
/* 694 */     this.tickersInLevel.clear();
/*     */   }
/*     */   
/*     */   public void registerAllBlockEntitiesAfterLevelLoad() {
/* 698 */     this.blockEntities.values().forEach(blockEntity -> {
/*     */           Level patt0$temp = this.level;
/*     */           if (patt0$temp instanceof ServerLevel) {
/*     */             ServerLevel serverLevel = (ServerLevel)patt0$temp;
/*     */             addGameEventListener(blockEntity, serverLevel);
/*     */           } 
/*     */           this.level.onBlockEntityAdded(blockEntity);
/*     */           updateBlockEntityTicker(blockEntity);
/*     */         });
/*     */   } private <T extends BlockEntity> void addGameEventListener(T blockEntity, ServerLevel level) {
/* 708 */     Block block = blockEntity.getBlockState().getBlock();
/*     */     
/* 710 */     if (block instanceof EntityBlock) {
/* 711 */       GameEventListener listener = ((EntityBlock)block).getListener(level, (BlockEntity)blockEntity);
/* 712 */       if (listener != null) {
/* 713 */         getListenerRegistry(SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getY())).register(listener);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T extends BlockEntity> void updateBlockEntityTicker(T blockEntity) {
/* 719 */     BlockState state = blockEntity.getBlockState();
/* 720 */     BlockEntityTicker<T> ticker = state.getTicker(this.level, blockEntity.getType());
/* 721 */     if (ticker == null) {
/* 722 */       removeBlockEntityTicker(blockEntity.getBlockPos());
/*     */     } else {
/* 724 */       this.tickersInLevel.compute(blockEntity.getBlockPos(), (blockPos, existingTicker) -> {
/*     */             TickingBlockEntity actualTicker = createTicker(blockEntity, blockEntity);
/*     */             if (existingTicker != null) {
/*     */               existingTicker.rebind(actualTicker);
/*     */               return existingTicker;
/*     */             } 
/*     */             if (isInLevel()) {
/*     */               RebindableTickingBlockEntityWrapper result = new RebindableTickingBlockEntityWrapper(actualTicker);
/*     */               this.level.addBlockEntityTicker(result);
/*     */               return result;
/*     */             } 
/*     */             return null;
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends BlockEntity> TickingBlockEntity createTicker(T blockEntity, BlockEntityTicker<T> ticker) {
/* 743 */     return new BoundTickingBlockEntity<>(blockEntity, ticker);
/*     */   }
/*     */   
/*     */   private class BoundTickingBlockEntity<T extends BlockEntity> implements TickingBlockEntity {
/*     */     private final T blockEntity;
/*     */     private final BlockEntityTicker<T> ticker;
/*     */     private boolean loggedInvalidBlockState;
/*     */     
/*     */     private BoundTickingBlockEntity(T blockEntity, BlockEntityTicker<T> ticker) {
/* 752 */       this.blockEntity = blockEntity;
/* 753 */       this.ticker = ticker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 758 */       if (!this.blockEntity.isRemoved() && this.blockEntity.hasLevel()) {
/* 759 */         BlockPos pos = this.blockEntity.getBlockPos();
/* 760 */         if (LevelChunk.this.isTicking(pos)) {
/*     */           try {
/* 762 */             ProfilerFiller profiler = Profiler.get();
/* 763 */             profiler.push(this::getType);
/* 764 */             BlockState blockState = LevelChunk.this.getBlockState(pos);
/* 765 */             if (this.blockEntity.getType().isValid(blockState)) {
/* 766 */               this.ticker.tick(LevelChunk.this.level, this.blockEntity.getBlockPos(), blockState, (BlockEntity)this.blockEntity);
/* 767 */               this.loggedInvalidBlockState = false;
/*     */             }
/* 769 */             else if (!this.loggedInvalidBlockState) {
/* 770 */               this.loggedInvalidBlockState = true;
/* 771 */               LevelChunk.LOGGER.warn("Block entity {} @ {} state {} invalid for ticking:", new Object[] { LogUtils.defer(this::getType), LogUtils.defer(this::getPos), blockState });
/*     */             } 
/*     */             
/* 774 */             profiler.pop();
/* 775 */           } catch (Throwable t) {
/* 776 */             CrashReport report = CrashReport.forThrowable(t, "Ticking block entity");
/* 777 */             CrashReportCategory category = report.addCategory("Block entity being ticked");
/* 778 */             this.blockEntity.fillCrashReportCategory(category);
/*     */             
/* 780 */             throw new ReportedException(report);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRemoved() {
/* 788 */       return this.blockEntity.isRemoved();
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos getPos() {
/* 793 */       return this.blockEntity.getBlockPos();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getType() {
/* 798 */       return BlockEntityType.getKey(this.blockEntity.getType()).toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 803 */       return "Level ticker for " + getType() + "@" + String.valueOf(getPos());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RebindableTickingBlockEntityWrapper implements TickingBlockEntity {
/*     */     private TickingBlockEntity ticker;
/*     */     
/*     */     private RebindableTickingBlockEntityWrapper(TickingBlockEntity ticker) {
/* 811 */       this.ticker = ticker;
/*     */     }
/*     */     
/*     */     private void rebind(TickingBlockEntity ticker) {
/* 815 */       this.ticker = ticker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 820 */       this.ticker.tick();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRemoved() {
/* 825 */       return this.ticker.isRemoved();
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos getPos() {
/* 830 */       return this.ticker.getPos();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getType() {
/* 835 */       return this.ticker.getType();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 840 */       return String.valueOf(this.ticker) + " <wrapped>";
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PostLoadProcessor {
/*     */     void run(LevelChunk param1LevelChunk);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface UnsavedListener {
/*     */     void setUnsaved(ChunkPos param1ChunkPos);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/LevelChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */