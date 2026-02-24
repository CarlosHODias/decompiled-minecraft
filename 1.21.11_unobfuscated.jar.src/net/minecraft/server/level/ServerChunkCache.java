/*     */ package net.minecraft.server.level;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.LocalMobCapCalculator;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.TicketStorage;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
/*     */ import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerChunkCache extends ChunkSource {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final DistanceManager distanceManager;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Thread mainThread;
/*     */   
/*     */   private final ThreadedLevelLightEngine lightEngine;
/*     */   
/*     */   private final MainThreadExecutor mainThreadProcessor;
/*     */   
/*     */   public final ChunkMap chunkMap;
/*     */   private final DimensionDataStorage dataStorage;
/*     */   private final TicketStorage ticketStorage;
/*     */   private long lastInhabitedUpdate;
/*     */   private boolean spawnEnemies = true;
/*     */   private static final int CACHE_SIZE = 4;
/*  77 */   private final long[] lastChunkPos = new long[4];
/*  78 */   private final ChunkStatus[] lastChunkStatus = new ChunkStatus[4];
/*  79 */   private final ChunkAccess[] lastChunk = new ChunkAccess[4];
/*  80 */   private final List<LevelChunk> spawningChunks = (List<LevelChunk>)new ObjectArrayList();
/*  81 */   private final Set<ChunkHolder> chunkHoldersToBroadcast = (Set<ChunkHolder>)new it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet();
/*     */   
/*     */   @VisibleForDebug
/*     */   private NaturalSpawner.SpawnState lastSpawnState;
/*     */   
/*     */   public ServerChunkCache(ServerLevel level, LevelStorageSource.LevelStorageAccess levelStorage, DataFixer fixerUpper, StructureTemplateManager structureTemplateManager, Executor executor, ChunkGenerator generator, int viewDistance, int simulationDistance, boolean syncWrites, ChunkStatusUpdateListener chunkStatusListener, Supplier<DimensionDataStorage> overworldDataStorage) {
/*  87 */     this.level = level;
/*  88 */     this.mainThreadProcessor = new MainThreadExecutor(level);
/*  89 */     this.mainThread = Thread.currentThread();
/*     */     
/*  91 */     Path dataFolder = levelStorage.getDimensionPath(level.dimension()).resolve("data");
/*     */     try {
/*  93 */       FileUtil.createDirectoriesSafe(dataFolder);
/*  94 */     } catch (IOException e) {
/*  95 */       LOGGER.error("Failed to create dimension data storage directory", e);
/*     */     } 
/*     */     
/*  98 */     this.dataStorage = new DimensionDataStorage(dataFolder, fixerUpper, (HolderLookup.Provider)level.registryAccess());
/*  99 */     this.ticketStorage = (TicketStorage)this.dataStorage.computeIfAbsent(TicketStorage.TYPE);
/*     */     
/* 101 */     this.chunkMap = new ChunkMap(level, levelStorage, fixerUpper, structureTemplateManager, executor, this.mainThreadProcessor, (LightChunkGetter)this, generator, chunkStatusListener, overworldDataStorage, this.ticketStorage, viewDistance, syncWrites);
/* 102 */     this.lightEngine = this.chunkMap.getLightEngine();
/* 103 */     this.distanceManager = this.chunkMap.getDistanceManager();
/* 104 */     this.distanceManager.updateSimulationDistance(simulationDistance);
/* 105 */     clearCache();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadedLevelLightEngine getLightEngine() {
/* 110 */     return this.lightEngine;
/*     */   }
/*     */   
/*     */   private ChunkHolder getVisibleChunkIfPresent(long key) {
/* 114 */     return this.chunkMap.getVisibleChunkIfPresent(key);
/*     */   }
/*     */   
/*     */   private void storeInCache(long pos, ChunkAccess chunk, ChunkStatus status) {
/* 118 */     for (int i = 3; i > 0; i--) {
/* 119 */       this.lastChunkPos[i] = this.lastChunkPos[i - 1];
/* 120 */       this.lastChunkStatus[i] = this.lastChunkStatus[i - 1];
/* 121 */       this.lastChunk[i] = this.lastChunk[i - 1];
/*     */     } 
/* 123 */     this.lastChunkPos[0] = pos;
/* 124 */     this.lastChunkStatus[0] = status;
/* 125 */     this.lastChunk[0] = chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess getChunk(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/* 130 */     if (Thread.currentThread() != this.mainThread) {
/* 131 */       return CompletableFuture.<ChunkAccess>supplyAsync(() -> getChunk(x, z, targetStatus, loadOrGenerate), (Executor)this.mainThreadProcessor).join();
/*     */     }
/* 133 */     ProfilerFiller profiler = Profiler.get();
/* 134 */     profiler.incrementCounter("getChunk");
/*     */     
/* 136 */     long pos = ChunkPos.asLong(x, z);
/* 137 */     for (int i = 0; i < 4; i++) {
/* 138 */       if (pos == this.lastChunkPos[i] && targetStatus == this.lastChunkStatus[i]) {
/* 139 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 140 */         if (chunkAccess != null || !loadOrGenerate) {
/* 141 */           return chunkAccess;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     profiler.incrementCounter("getChunkCacheMiss");
/* 147 */     CompletableFuture<ChunkResult<ChunkAccess>> serverFuture = getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate);
/* 148 */     Objects.requireNonNull(serverFuture); this.mainThreadProcessor.managedBlock(serverFuture::isDone);
/*     */     
/* 150 */     ChunkResult<ChunkAccess> chunkResult = serverFuture.join();
/* 151 */     ChunkAccess chunk = chunkResult.orElse(null);
/* 152 */     if (chunk == null && 
/* 153 */       loadOrGenerate) {
/* 154 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Chunk not there when requested: " + chunkResult.getError()));
/*     */     }
/*     */ 
/*     */     
/* 158 */     storeInCache(pos, chunk, targetStatus);
/* 159 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunk getChunkNow(int x, int z) {
/* 164 */     if (Thread.currentThread() != this.mainThread)
/*     */     {
/* 166 */       return null;
/*     */     }
/* 168 */     Profiler.get().incrementCounter("getChunkNow");
/*     */     
/* 170 */     long pos = ChunkPos.asLong(x, z);
/* 171 */     for (int i = 0; i < 4; i++) {
/* 172 */       if (pos == this.lastChunkPos[i] && this.lastChunkStatus[i] == ChunkStatus.FULL) {
/* 173 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 174 */         return (chunkAccess instanceof LevelChunk) ? (LevelChunk)chunkAccess : null;
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos);
/* 179 */     if (chunkHolder == null) {
/* 180 */       return null;
/*     */     }
/* 182 */     ChunkAccess chunk = chunkHolder.getChunkIfPresent(ChunkStatus.FULL);
/* 183 */     if (chunk != null) {
/* 184 */       storeInCache(pos, chunk, ChunkStatus.FULL);
/* 185 */       if (chunk instanceof LevelChunk) {
/* 186 */         return (LevelChunk)chunk;
/*     */       }
/*     */     } 
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   private void clearCache() {
/* 193 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 194 */     Arrays.fill((Object[])this.lastChunkStatus, null);
/* 195 */     Arrays.fill((Object[])this.lastChunk, null);
/*     */   }
/*     */   public CompletableFuture<ChunkResult<ChunkAccess>> getChunkFuture(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*     */     CompletableFuture<ChunkResult<ChunkAccess>> serverFuture;
/* 199 */     boolean isMainThread = (Thread.currentThread() == this.mainThread);
/*     */     
/* 201 */     if (isMainThread) {
/* 202 */       serverFuture = getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate);
/* 203 */       Objects.requireNonNull(serverFuture); this.mainThreadProcessor.managedBlock(serverFuture::isDone);
/*     */     } else {
/* 205 */       serverFuture = CompletableFuture.supplyAsync(() -> getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate), (Executor)this.mainThreadProcessor).thenCompose(chunk -> chunk);
/*     */     } 
/* 207 */     return serverFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CompletableFuture<ChunkResult<ChunkAccess>> getChunkFutureMainThread(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/* 214 */     ChunkPos pos = new ChunkPos(x, z);
/* 215 */     long key = pos.toLong();
/* 216 */     int targetTicketLevel = ChunkLevel.byStatus(targetStatus);
/*     */     
/* 218 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/* 219 */     if (loadOrGenerate) {
/*     */       
/* 221 */       addTicket(new Ticket(TicketType.UNKNOWN, targetTicketLevel), pos);
/*     */       
/* 223 */       if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 224 */         ProfilerFiller profiler = Profiler.get();
/* 225 */         profiler.push("chunkLoad");
/* 226 */         runDistanceManagerUpdates();
/* 227 */         chunkHolder = getVisibleChunkIfPresent(key);
/* 228 */         profiler.pop();
/* 229 */         if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 230 */           throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("No chunk holder after ticket has been added"));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 236 */       return GenerationChunkHolder.UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */     
/* 239 */     return chunkHolder.scheduleChunkGenerationTask(targetStatus, this.chunkMap);
/*     */   }
/*     */   
/*     */   private boolean chunkAbsent(ChunkHolder chunkHolder, int targetTicketLevel) {
/* 243 */     return (chunkHolder == null || chunkHolder.getTicketLevel() > targetTicketLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int x, int z) {
/* 248 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(new ChunkPos(x, z).toLong());
/* 249 */     int targetTicketLevel = ChunkLevel.byStatus(ChunkStatus.FULL);
/*     */     
/* 251 */     return !chunkAbsent(chunkHolder, targetTicketLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LightChunk getChunkForLighting(int x, int z) {
/* 257 */     long key = ChunkPos.asLong(x, z);
/* 258 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/* 259 */     if (chunkHolder == null) {
/* 260 */       return null;
/*     */     }
/*     */     
/* 263 */     return (LightChunk)chunkHolder.getChunkIfPresentUnchecked(ChunkStatus.INITIALIZE_LIGHT.getParent());
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 268 */     return this.level;
/*     */   }
/*     */   
/*     */   public boolean pollTask() {
/* 272 */     return this.mainThreadProcessor.pollTask();
/*     */   }
/*     */   
/*     */   boolean runDistanceManagerUpdates() {
/* 276 */     boolean updated = this.distanceManager.runAllUpdates(this.chunkMap);
/* 277 */     boolean promoted = this.chunkMap.promoteChunkMap();
/* 278 */     this.chunkMap.runGenerationTasks();
/* 279 */     if (updated || promoted) {
/* 280 */       clearCache();
/* 281 */       return true;
/*     */     } 
/* 283 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPositionTicking(long chunkKey) {
/* 287 */     if (!this.level.shouldTickBlocksAt(chunkKey)) {
/* 288 */       return false;
/*     */     }
/* 290 */     ChunkHolder holder = getVisibleChunkIfPresent(chunkKey);
/* 291 */     if (holder == null) {
/* 292 */       return false;
/*     */     }
/* 294 */     return ((ChunkResult)holder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).isSuccess();
/*     */   }
/*     */   
/*     */   public void save(boolean flushStorage) {
/* 298 */     runDistanceManagerUpdates();
/* 299 */     this.chunkMap.saveAllChunks(flushStorage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 304 */     save(true);
/* 305 */     this.dataStorage.close();
/* 306 */     this.lightEngine.close();
/* 307 */     this.chunkMap.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BooleanSupplier haveTime, boolean tickChunks) {
/* 312 */     ProfilerFiller profiler = Profiler.get();
/* 313 */     profiler.push("purge");
/* 314 */     if (this.level.tickRateManager().runsNormally() || !tickChunks)
/*     */     {
/* 316 */       this.ticketStorage.purgeStaleTickets(this.chunkMap);
/*     */     }
/* 318 */     runDistanceManagerUpdates();
/* 319 */     profiler.popPush("chunks");
/* 320 */     if (tickChunks) {
/* 321 */       tickChunks();
/* 322 */       this.chunkMap.tick();
/*     */     } 
/* 324 */     profiler.popPush("unload");
/* 325 */     this.chunkMap.tick(haveTime);
/* 326 */     profiler.pop();
/* 327 */     clearCache();
/*     */   }
/*     */   
/*     */   private void tickChunks() {
/* 331 */     long time = this.level.getGameTime();
/* 332 */     long timeDiff = time - this.lastInhabitedUpdate;
/* 333 */     this.lastInhabitedUpdate = time;
/*     */     
/* 335 */     if (this.level.isDebug()) {
/*     */       return;
/*     */     }
/*     */     
/* 339 */     ProfilerFiller profiler = Profiler.get();
/* 340 */     profiler.push("pollingChunks");
/*     */     
/* 342 */     if (this.level.tickRateManager().runsNormally()) {
/* 343 */       profiler.push("tickingChunks");
/* 344 */       tickChunks(profiler, timeDiff);
/* 345 */       profiler.pop();
/*     */     } 
/*     */     
/* 348 */     broadcastChangedChunks(profiler);
/*     */     
/* 350 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void broadcastChangedChunks(ProfilerFiller profiler) {
/* 354 */     profiler.push("broadcast");
/*     */     
/* 356 */     for (ChunkHolder chunkHolder : this.chunkHoldersToBroadcast) {
/* 357 */       LevelChunk chunk = chunkHolder.getTickingChunk();
/* 358 */       if (chunk != null) {
/* 359 */         chunkHolder.broadcastChanges(chunk);
/*     */       }
/*     */     } 
/* 362 */     this.chunkHoldersToBroadcast.clear();
/*     */     
/* 364 */     profiler.pop();
/*     */   }
/*     */   private void tickChunks(ProfilerFiller profiler, long timeDiff) {
/*     */     List<MobCategory> spawningCategories;
/* 368 */     profiler.push("naturalSpawnCount");
/* 369 */     int chunkCount = this.distanceManager.getNaturalSpawnChunkCount();
/* 370 */     NaturalSpawner.SpawnState spawnCookie = NaturalSpawner.createState(chunkCount, this.level.getAllEntities(), this::getFullChunk, new LocalMobCapCalculator(this.chunkMap));
/* 371 */     this.lastSpawnState = spawnCookie;
/*     */     
/* 373 */     boolean doMobSpawning = (Boolean)this.level.getGameRules().get(GameRules.SPAWN_MOBS);
/* 374 */     int tickSpeed = (Integer)this.level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
/*     */ 
/*     */     
/* 377 */     if (doMobSpawning) {
/* 378 */       boolean spawnPersistent = (this.level.getGameTime() % 400L == 0L);
/* 379 */       spawningCategories = NaturalSpawner.getFilteredSpawningCategories(spawnCookie, true, this.spawnEnemies, spawnPersistent);
/*     */     } else {
/* 381 */       spawningCategories = List.of();
/*     */     } 
/*     */     
/* 384 */     List<LevelChunk> spawningChunks = this.spawningChunks;
/*     */     try {
/* 386 */       profiler.popPush("filteringSpawningChunks");
/* 387 */       this.chunkMap.collectSpawningChunks(spawningChunks);
/* 388 */       profiler.popPush("shuffleSpawningChunks");
/*     */       
/* 390 */       Util.shuffle(spawningChunks, this.level.random);
/*     */       
/* 392 */       profiler.popPush("tickSpawningChunks");
/* 393 */       for (LevelChunk chunk : spawningChunks) {
/* 394 */         tickSpawningChunk(chunk, timeDiff, spawningCategories, spawnCookie);
/*     */       }
/*     */     } finally {
/* 397 */       spawningChunks.clear();
/*     */     } 
/*     */     
/* 400 */     profiler.popPush("tickTickingChunks");
/* 401 */     this.chunkMap.forEachBlockTickingChunk(chunk -> this.level.tickChunk(tickSpeed, tickSpeed));
/*     */     
/* 403 */     if (doMobSpawning) {
/* 404 */       profiler.popPush("customSpawners");
/* 405 */       this.level.tickCustomSpawners(this.spawnEnemies);
/*     */     } 
/* 407 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void tickSpawningChunk(LevelChunk chunk, long timeDiff, List<MobCategory> spawningCategories, NaturalSpawner.SpawnState spawnCookie) {
/* 411 */     ChunkPos chunkPos = chunk.getPos();
/* 412 */     chunk.incrementInhabitedTime(timeDiff);
/* 413 */     if (this.distanceManager.inEntityTickingRange(chunkPos.toLong())) {
/* 414 */       this.level.tickThunder(chunk);
/*     */     }
/* 416 */     if (spawningCategories.isEmpty()) {
/*     */       return;
/*     */     }
/* 419 */     if (this.level.canSpawnEntitiesInChunk(chunkPos)) {
/* 420 */       NaturalSpawner.spawnForChunk(this.level, chunk, spawnCookie, spawningCategories);
/*     */     }
/*     */   }
/*     */   
/*     */   private void getFullChunk(long chunkKey, Consumer<LevelChunk> output) {
/* 425 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(chunkKey);
/*     */     
/* 427 */     if (chunkHolder != null) {
/* 428 */       ((ChunkResult<LevelChunk>)chunkHolder.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).ifSuccess(output);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String gatherStats() {
/* 434 */     return Integer.toString(getLoadedChunksCount());
/*     */   }
/*     */   
/*     */   @com.google.common.annotations.VisibleForTesting
/*     */   public int getPendingTasksCount() {
/* 439 */     return this.mainThreadProcessor.getPendingTasksCount();
/*     */   }
/*     */   
/*     */   public ChunkGenerator getGenerator() {
/* 443 */     return this.chunkMap.generator();
/*     */   }
/*     */   
/*     */   public ChunkGeneratorStructureState getGeneratorState() {
/* 447 */     return this.chunkMap.generatorState();
/*     */   }
/*     */   
/*     */   public RandomState randomState() {
/* 451 */     return this.chunkMap.randomState();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunksCount() {
/* 456 */     return this.chunkMap.size();
/*     */   }
/*     */   
/*     */   public void blockChanged(BlockPos pos) {
/* 460 */     int xc = SectionPos.blockToSectionCoord(pos.getX());
/* 461 */     int zc = SectionPos.blockToSectionCoord(pos.getZ());
/* 462 */     ChunkHolder chunk = getVisibleChunkIfPresent(ChunkPos.asLong(xc, zc));
/* 463 */     if (chunk != null && chunk.blockChanged(pos)) {
/* 464 */       this.chunkHoldersToBroadcast.add(chunk);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLightUpdate(LightLayer layer, SectionPos pos) {
/* 470 */     this.mainThreadProcessor.execute(() -> {
/*     */           ChunkHolder chunk = getVisibleChunkIfPresent(pos.chunk().toLong());
/*     */           if (chunk != null && chunk.sectionLightChanged(layer, pos.y())) {
/*     */             this.chunkHoldersToBroadcast.add(chunk);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean hasActiveTickets() {
/* 479 */     return this.ticketStorage.shouldKeepDimensionActive();
/*     */   }
/*     */   
/*     */   public void addTicket(Ticket ticket, ChunkPos pos) {
/* 483 */     this.ticketStorage.addTicket(ticket, pos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> addTicketAndLoadWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 487 */     if (!type.doesLoad())
/* 488 */       throw new IllegalStateException("Ticket type " + String.valueOf(type) + " does not trigger chunk loading"); 
/* 489 */     if (type.canExpireIfUnloaded()) {
/* 490 */       throw new IllegalStateException("Ticket type " + String.valueOf(type) + " can expire before it loads, cannot fetch asynchronously");
/*     */     }
/* 492 */     addTicketWithRadius(type, pos, radius);
/* 493 */     runDistanceManagerUpdates();
/* 494 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos.toLong());
/* 495 */     Objects.requireNonNull(chunkHolder, "No chunk was scheduled for loading");
/* 496 */     return this.chunkMap.getChunkRangeFuture(chunkHolder, radius, distance -> ChunkStatus.FULL);
/*     */   }
/*     */   
/*     */   public void addTicketWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 500 */     this.ticketStorage.addTicketWithRadius(type, pos, radius);
/*     */   }
/*     */   
/*     */   public void removeTicketWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 504 */     this.ticketStorage.removeTicketWithRadius(type, pos, radius);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updateChunkForced(ChunkPos pos, boolean forced) {
/* 509 */     return this.ticketStorage.updateChunkForced(pos, forced);
/*     */   }
/*     */ 
/*     */   
/*     */   public LongSet getForceLoadedChunks() {
/* 514 */     return this.ticketStorage.getForceLoadedChunks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(ServerPlayer player) {
/* 521 */     if (!player.isRemoved()) {
/* 522 */       this.chunkMap.move(player);
/* 523 */       if (player.isReceivingWaypoints()) {
/* 524 */         this.level.getWaypointManager().updatePlayer(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeEntity(Entity entity) {
/* 532 */     this.chunkMap.removeEntity(entity);
/*     */   }
/*     */   
/*     */   public void addEntity(Entity entity) {
/* 536 */     this.chunkMap.addEntity(entity);
/*     */   }
/*     */   
/*     */   public void sendToTrackingPlayersAndSelf(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 540 */     this.chunkMap.sendToTrackingPlayersAndSelf(entity, packet);
/*     */   }
/*     */   
/*     */   public void sendToTrackingPlayers(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 544 */     this.chunkMap.sendToTrackingPlayers(entity, packet);
/*     */   }
/*     */   
/*     */   public void setViewDistance(int newDistance) {
/* 548 */     this.chunkMap.setServerViewDistance(newDistance);
/*     */   }
/*     */   
/*     */   public void setSimulationDistance(int simulationDistance) {
/* 552 */     this.distanceManager.updateSimulationDistance(simulationDistance);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnSettings(boolean spawnEnemies) {
/* 557 */     this.spawnEnemies = spawnEnemies;
/*     */   }
/*     */   
/*     */   public String getChunkDebugData(ChunkPos pos) {
/* 561 */     return this.chunkMap.getChunkDebugData(pos);
/*     */   }
/*     */   
/*     */   public DimensionDataStorage getDataStorage() {
/* 565 */     return this.dataStorage;
/*     */   }
/*     */   
/*     */   public PoiManager getPoiManager() {
/* 569 */     return this.chunkMap.getPoiManager();
/*     */   }
/*     */   
/*     */   public ChunkScanAccess chunkScanner() {
/* 573 */     return this.chunkMap.chunkScanner();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public NaturalSpawner.SpawnState getLastSpawnState() {
/* 578 */     return this.lastSpawnState;
/*     */   }
/*     */   
/*     */   public void deactivateTicketsOnClosing() {
/* 582 */     this.ticketStorage.deactivateTicketsOnClosing();
/*     */   }
/*     */   
/*     */   public void onChunkReadyToSend(ChunkHolder chunk) {
/* 586 */     if (chunk.hasChangesToBroadcast())
/* 587 */       this.chunkHoldersToBroadcast.add(chunk); 
/*     */   }
/*     */   
/*     */   private final class MainThreadExecutor
/*     */     extends BlockableEventLoop<Runnable> {
/*     */     private MainThreadExecutor(Level level) {
/* 593 */       super("Chunk source main thread executor for " + String.valueOf(level.dimension().identifier()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void managedBlock(BooleanSupplier condition) {
/* 598 */       super.managedBlock(() -> (MinecraftServer.throwIfFatalException() && condition.getAsBoolean()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Runnable wrapRunnable(Runnable runnable) {
/* 603 */       return runnable;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean shouldRun(Runnable task) {
/* 608 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean scheduleExecutables() {
/* 614 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Thread getRunningThread() {
/* 619 */       return ServerChunkCache.this.mainThread;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doRunTask(Runnable task) {
/* 624 */       Profiler.get().incrementCounter("runTask");
/* 625 */       super.doRunTask(task);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean pollTask() {
/* 630 */       if (ServerChunkCache.this.runDistanceManagerUpdates()) {
/* 631 */         return true;
/*     */       }
/* 633 */       ServerChunkCache.this.lightEngine.tryScheduleUpdate();
/* 634 */       return super.pollTask();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ServerChunkCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */