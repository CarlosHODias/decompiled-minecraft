/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gameevent.GameEventListenerRegistry;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.NoiseChunk;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.ChunkSkyLightSources;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.ticks.SavedTick;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ChunkAccess
/*     */   implements LightChunk, StructureAccess, BiomeManager.NoiseBiomeSource {
/*     */   public static final int NO_FILLED_SECTION = -1;
/*  63 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  64 */   private static final LongSet EMPTY_REFERENCE_SET = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   protected final ShortList[] postProcessing;
/*     */   
/*     */   private volatile boolean unsaved;
/*     */   
/*     */   private volatile boolean isLightCorrect;
/*     */   
/*     */   protected final ChunkPos chunkPos;
/*     */   
/*     */   private long inhabitedTime;
/*     */   @Deprecated
/*     */   private BiomeGenerationSettings carverBiomeSettings;
/*     */   protected NoiseChunk noiseChunk;
/*     */   protected final UpgradeData upgradeData;
/*     */   protected BlendingData blendingData;
/*  80 */   protected final Map<Heightmap.Types, Heightmap> heightmaps = Maps.newEnumMap(Heightmap.Types.class);
/*     */   
/*     */   protected ChunkSkyLightSources skyLightSources;
/*  83 */   private final Map<Structure, StructureStart> structureStarts = Maps.newHashMap();
/*  84 */   private final Map<Structure, LongSet> structuresRefences = Maps.newHashMap();
/*     */   
/*  86 */   protected final Map<BlockPos, CompoundTag> pendingBlockEntities = Maps.newHashMap();
/*  87 */   protected final Map<BlockPos, BlockEntity> blockEntities = (Map<BlockPos, BlockEntity>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   protected final LevelHeightAccessor levelHeightAccessor;
/*     */   protected final LevelChunkSection[] sections;
/*     */   
/*     */   public ChunkAccess(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, long inhabitedTime, LevelChunkSection[] sections, BlendingData blendingData) {
/*  93 */     this.chunkPos = chunkPos;
/*  94 */     this.upgradeData = upgradeData;
/*  95 */     this.levelHeightAccessor = levelHeightAccessor;
/*  96 */     this.sections = new LevelChunkSection[levelHeightAccessor.getSectionsCount()];
/*  97 */     this.inhabitedTime = inhabitedTime;
/*  98 */     this.postProcessing = new ShortList[levelHeightAccessor.getSectionsCount()];
/*  99 */     this.blendingData = blendingData;
/* 100 */     this.skyLightSources = new ChunkSkyLightSources(levelHeightAccessor);
/*     */     
/* 102 */     if (sections != null) {
/* 103 */       if (this.sections.length == sections.length) {
/* 104 */         System.arraycopy(sections, 0, this.sections, 0, this.sections.length);
/*     */       } else {
/* 106 */         LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", sections.length, this.sections.length);
/*     */       } 
/*     */     }
/*     */     
/* 110 */     replaceMissingSections(containerFactory, this.sections);
/*     */   }
/*     */   
/*     */   private static void replaceMissingSections(PalettedContainerFactory containerFactory, LevelChunkSection[] sections) {
/* 114 */     for (int i = 0; i < sections.length; i++) {
/* 115 */       if (sections[i] == null) {
/* 116 */         sections[i] = new LevelChunkSection(containerFactory);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public GameEventListenerRegistry getListenerRegistry(int section) {
/* 122 */     return GameEventListenerRegistry.NOOP;
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(BlockPos pos, BlockState state) {
/* 126 */     return setBlockState(pos, state, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHighestFilledSectionIndex() {
/* 136 */     LevelChunkSection[] sections = getSections();
/* 137 */     for (int sectionIndex = sections.length - 1; sectionIndex >= 0; sectionIndex--) {
/* 138 */       LevelChunkSection section = sections[sectionIndex];
/* 139 */       if (!section.hasOnlyAir()) {
/* 140 */         return sectionIndex;
/*     */       }
/*     */     } 
/* 143 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public int getHighestSectionPosition() {
/* 149 */     int sectionIndex = getHighestFilledSectionIndex();
/* 150 */     return (sectionIndex == -1) ? getMinY() : SectionPos.sectionToBlockCoord(getSectionYFromSectionIndex(sectionIndex));
/*     */   }
/*     */   
/*     */   public Set<BlockPos> getBlockEntitiesPos() {
/* 154 */     Set<BlockPos> result = Sets.newHashSet(this.pendingBlockEntities.keySet());
/* 155 */     result.addAll(this.blockEntities.keySet());
/* 156 */     return result;
/*     */   }
/*     */   
/*     */   public LevelChunkSection[] getSections() {
/* 160 */     return this.sections;
/*     */   }
/*     */   
/*     */   public LevelChunkSection getSection(int sectionIndex) {
/* 164 */     return getSections()[sectionIndex];
/*     */   }
/*     */   
/*     */   public Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps() {
/* 168 */     return Collections.unmodifiableSet(this.heightmaps.entrySet());
/*     */   }
/*     */   
/*     */   public void setHeightmap(Heightmap.Types key, long[] data) {
/* 172 */     getOrCreateHeightmapUnprimed(key).setRawData(this, key, data);
/*     */   }
/*     */   
/*     */   public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types type) {
/* 176 */     return this.heightmaps.computeIfAbsent(type, k -> new Heightmap(this, k));
/*     */   }
/*     */   
/*     */   public boolean hasPrimedHeightmap(Heightmap.Types type) {
/* 180 */     return (this.heightmaps.get(type) != null);
/*     */   }
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/* 184 */     Heightmap heightmap = this.heightmaps.get(type);
/* 185 */     if (heightmap == null) {
/* 186 */       if (SharedConstants.IS_RUNNING_IN_IDE && this instanceof LevelChunk) {
/* 187 */         LOGGER.error("Unprimed heightmap: {} {} {}", new Object[] { type, x, z });
/*     */       }
/* 189 */       Heightmap.primeHeightmaps(this, EnumSet.of(type));
/* 190 */       heightmap = this.heightmaps.get(type);
/*     */     } 
/* 192 */     return heightmap.getFirstAvailable(x & 0xF, z & 0xF) - 1;
/*     */   }
/*     */   
/*     */   public ChunkPos getPos() {
/* 196 */     return this.chunkPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureStart getStartForStructure(Structure structure) {
/* 201 */     return this.structureStarts.get(structure);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStartForStructure(Structure structure, StructureStart structureStart) {
/* 206 */     this.structureStarts.put(structure, structureStart);
/* 207 */     markUnsaved();
/*     */   }
/*     */   
/*     */   public Map<Structure, StructureStart> getAllStarts() {
/* 211 */     return Collections.unmodifiableMap(this.structureStarts);
/*     */   }
/*     */   
/*     */   public void setAllStarts(Map<Structure, StructureStart> starts) {
/* 215 */     this.structureStarts.clear();
/* 216 */     this.structureStarts.putAll(starts);
/* 217 */     markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public LongSet getReferencesForStructure(Structure structure) {
/* 222 */     return this.structuresRefences.getOrDefault(structure, EMPTY_REFERENCE_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addReferenceForStructure(Structure structure, long reference) {
/* 227 */     ((LongSet)this.structuresRefences.computeIfAbsent(structure, k -> new LongOpenHashSet())).add(reference);
/* 228 */     markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Structure, LongSet> getAllReferences() {
/* 233 */     return Collections.unmodifiableMap(this.structuresRefences);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<Structure, LongSet> data) {
/* 238 */     this.structuresRefences.clear();
/* 239 */     this.structuresRefences.putAll(data);
/* 240 */     markUnsaved();
/*     */   }
/*     */   
/*     */   public boolean isYSpaceEmpty(int yStartInclusive, int yEndInclusive) {
/* 244 */     if (yStartInclusive < getMinY()) {
/* 245 */       yStartInclusive = getMinY();
/*     */     }
/* 247 */     if (yEndInclusive > getMaxY()) {
/* 248 */       yEndInclusive = getMaxY();
/*     */     }
/* 250 */     for (int y = yStartInclusive; y <= yEndInclusive; y += 16) {
/* 251 */       if (!getSection(getSectionIndex(y)).hasOnlyAir()) {
/* 252 */         return false;
/*     */       }
/*     */     } 
/* 255 */     return true;
/*     */   }
/*     */   
/*     */   public void markUnsaved() {
/* 259 */     this.unsaved = true;
/*     */   }
/*     */   
/*     */   public boolean tryMarkSaved() {
/* 263 */     if (this.unsaved) {
/* 264 */       this.unsaved = false;
/* 265 */       return true;
/*     */     } 
/* 267 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isUnsaved() {
/* 271 */     return this.unsaved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkStatus getHighestGeneratedStatus() {
/* 278 */     ChunkStatus status = getPersistedStatus();
/* 279 */     BelowZeroRetrogen belowZeroRetrogen = getBelowZeroRetrogen();
/* 280 */     if (belowZeroRetrogen != null) {
/* 281 */       ChunkStatus targetStatus = belowZeroRetrogen.targetStatus();
/* 282 */       return ChunkStatus.max(targetStatus, status);
/*     */     } 
/* 284 */     return status;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos blockPos) {
/* 290 */     LOGGER.warn("Trying to mark a block for PostProcessing @ {}, but this operation is not supported.", blockPos);
/*     */   }
/*     */   
/*     */   public ShortList[] getPostProcessing() {
/* 294 */     return this.postProcessing;
/*     */   }
/*     */   
/*     */   public void addPackedPostProcess(ShortList packedOffsets, int sectionIndex) {
/* 298 */     getOrCreateOffsetList(getPostProcessing(), sectionIndex).addAll(packedOffsets);
/*     */   }
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag entityTag) {
/* 302 */     BlockPos posFromTag = BlockEntity.getPosFromTag(this.chunkPos, entityTag);
/* 303 */     if (!this.blockEntities.containsKey(posFromTag)) {
/* 304 */       this.pendingBlockEntities.put(posFromTag, entityTag);
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag getBlockEntityNbt(BlockPos blockPos) {
/* 309 */     return this.pendingBlockEntities.get(blockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void findBlockLightSources(BiConsumer<BlockPos, BlockState> consumer) {
/* 316 */     findBlocks(state -> (state.getLightEmission() != 0), consumer);
/*     */   }
/*     */   
/*     */   public void findBlocks(Predicate<BlockState> predicate, BiConsumer<BlockPos, BlockState> consumer) {
/* 320 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/* 321 */     for (int sectionY = getMinSectionY(); sectionY <= getMaxSectionY(); sectionY++) {
/* 322 */       LevelChunkSection section = getSection(getSectionIndexFromSectionY(sectionY));
/* 323 */       if (section.maybeHas(predicate)) {
/*     */ 
/*     */         
/* 326 */         BlockPos origin = SectionPos.of(this.chunkPos, sectionY).origin();
/* 327 */         for (int y = 0; y < 16; y++) {
/* 328 */           for (int z = 0; z < 16; z++) {
/* 329 */             for (int x = 0; x < 16; x++) {
/* 330 */               BlockState state = section.getBlockState(x, y, z);
/* 331 */               if (predicate.test(state)) {
/* 332 */                 consumer.accept(mutablePos.setWithOffset((Vec3i)origin, x, y, z), state);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeSerialized() {
/* 345 */     return true;
/*     */   }
/*     */   public static final class PackedTicks extends Record { private final List<SavedTick<Block>> blocks; private final List<SavedTick<Fluid>> fluids;
/* 348 */     public PackedTicks(List<SavedTick<Block>> blocks, List<SavedTick<Fluid>> fluids) { this.blocks = blocks; this.fluids = fluids; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 348 */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks; } public List<SavedTick<Block>> blocks() { return this.blocks; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #348	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$PackedTicks;
/* 348 */       //   0	8	1	o	Ljava/lang/Object; } public List<SavedTick<Fluid>> fluids() { return this.fluids; }
/*     */      }
/*     */ 
/*     */   
/*     */   public UpgradeData getUpgradeData() {
/* 353 */     return this.upgradeData;
/*     */   }
/*     */   
/*     */   public boolean isOldNoiseGeneration() {
/* 357 */     return (this.blendingData != null);
/*     */   }
/*     */   
/*     */   public BlendingData getBlendingData() {
/* 361 */     return this.blendingData;
/*     */   }
/*     */   
/*     */   public long getInhabitedTime() {
/* 365 */     return this.inhabitedTime;
/*     */   }
/*     */   
/*     */   public void incrementInhabitedTime(long inhabitedTimeDelta) {
/* 369 */     this.inhabitedTime += inhabitedTimeDelta;
/*     */   }
/*     */   
/*     */   public void setInhabitedTime(long inhabitedTime) {
/* 373 */     this.inhabitedTime = inhabitedTime;
/*     */   }
/*     */   public static ShortList getOrCreateOffsetList(ShortList[] list, int sectionIndex) {
/*     */     ShortArrayList shortArrayList;
/* 377 */     ShortList result = list[sectionIndex];
/* 378 */     if (result == null) {
/* 379 */       shortArrayList = new ShortArrayList();
/* 380 */       list[sectionIndex] = (ShortList)shortArrayList;
/*     */     } 
/* 382 */     return (ShortList)shortArrayList;
/*     */   }
/*     */   
/*     */   public boolean isLightCorrect() {
/* 386 */     return this.isLightCorrect;
/*     */   }
/*     */   
/*     */   public void setLightCorrect(boolean isLightCorrect) {
/* 390 */     this.isLightCorrect = isLightCorrect;
/* 391 */     markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMinY() {
/* 396 */     return this.levelHeightAccessor.getMinY();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 401 */     return this.levelHeightAccessor.getHeight();
/*     */   }
/*     */   
/*     */   public NoiseChunk getOrCreateNoiseChunk(Function<ChunkAccess, NoiseChunk> factory) {
/* 405 */     if (this.noiseChunk == null) {
/* 406 */       this.noiseChunk = factory.apply(this);
/*     */     }
/* 408 */     return this.noiseChunk;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BiomeGenerationSettings carverBiome(Supplier<BiomeGenerationSettings> source) {
/* 413 */     if (this.carverBiomeSettings == null) {
/* 414 */       this.carverBiomeSettings = source.get();
/*     */     }
/* 416 */     return this.carverBiomeSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/*     */     try {
/* 422 */       int quartMinY = QuartPos.fromBlock(getMinY());
/* 423 */       int quartMaxY = quartMinY + QuartPos.fromBlock(getHeight()) - 1;
/* 424 */       int clampedQuartY = Mth.clamp(quartY, quartMinY, quartMaxY);
/* 425 */       int sectionIndex = getSectionIndex(QuartPos.toBlock(clampedQuartY));
/* 426 */       return this.sections[sectionIndex].getNoiseBiome(quartX & 0x3, clampedQuartY & 0x3, quartZ & 0x3);
/* 427 */     } catch (Throwable t) {
/* 428 */       CrashReport report = CrashReport.forThrowable(t, "Getting biome");
/* 429 */       CrashReportCategory category = report.addCategory("Biome being got");
/* 430 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, quartX, quartY, quartZ));
/* 431 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fillBiomesFromNoise(BiomeResolver biomeResolver, Climate.Sampler sampler) {
/* 436 */     ChunkPos pos = getPos();
/* 437 */     int quartMinX = QuartPos.fromBlock(pos.getMinBlockX());
/* 438 */     int quartMinZ = QuartPos.fromBlock(pos.getMinBlockZ());
/* 439 */     LevelHeightAccessor heightAccessor = getHeightAccessorForGeneration();
/* 440 */     for (int sectionY = heightAccessor.getMinSectionY(); sectionY <= heightAccessor.getMaxSectionY(); sectionY++) {
/* 441 */       LevelChunkSection section = getSection(getSectionIndexFromSectionY(sectionY));
/* 442 */       int quartMinY = QuartPos.fromSection(sectionY);
/* 443 */       section.fillBiomesFromNoise(biomeResolver, sampler, quartMinX, quartMinY, quartMinZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasAnyStructureReferences() {
/* 448 */     return !getAllReferences().isEmpty();
/*     */   }
/*     */   
/*     */   public BelowZeroRetrogen getBelowZeroRetrogen() {
/* 452 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isUpgrading() {
/* 456 */     return (getBelowZeroRetrogen() != null);
/*     */   }
/*     */   
/*     */   public LevelHeightAccessor getHeightAccessorForGeneration() {
/* 460 */     return (LevelHeightAccessor)this;
/*     */   }
/*     */   
/*     */   public void initializeLightSources() {
/* 464 */     this.skyLightSources.fillFrom(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkSkyLightSources getSkyLightSources() {
/* 469 */     return this.skyLightSources;
/*     */   }
/*     */   private static final class ChunkPathElement extends Record implements ProblemReporter.PathElement { private final ChunkPos pos;
/* 472 */     private ChunkPathElement(ChunkPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #472	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;
/* 472 */       //   0	8	1	o	Ljava/lang/Object; } public ChunkPos pos() { return this.pos; }
/*     */     
/*     */     public String get() {
/* 475 */       return "chunk@" + String.valueOf(this.pos);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static ProblemReporter.PathElement problemPath(ChunkPos pos) {
/* 480 */     return new ChunkPathElement(pos);
/*     */   }
/*     */   
/*     */   public ProblemReporter.PathElement problemPath() {
/* 484 */     return problemPath(getPos());
/*     */   }
/*     */   
/*     */   public abstract BlockState setBlockState(BlockPos paramBlockPos, BlockState paramBlockState, @Block.UpdateFlags int paramInt);
/*     */   
/*     */   public abstract void setBlockEntity(BlockEntity paramBlockEntity);
/*     */   
/*     */   public abstract void addEntity(Entity paramEntity);
/*     */   
/*     */   public abstract ChunkStatus getPersistedStatus();
/*     */   
/*     */   public abstract void removeBlockEntity(BlockPos paramBlockPos);
/*     */   
/*     */   public abstract CompoundTag getBlockEntityNbtForSaving(BlockPos paramBlockPos, HolderLookup.Provider paramProvider);
/*     */   
/*     */   public abstract TickContainerAccess<Block> getBlockTicks();
/*     */   
/*     */   public abstract TickContainerAccess<Fluid> getFluidTicks();
/*     */   
/*     */   public abstract PackedTicks getTicksForSerialization(long paramLong);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/ChunkAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */