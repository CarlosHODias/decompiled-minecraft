/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenerationChunkHolder
/*     */ {
/*  26 */   private static final List<ChunkStatus> CHUNK_STATUSES = ChunkStatus.getStatusList();
/*     */   
/*  28 */   private static final ChunkResult<ChunkAccess> NOT_DONE_YET = ChunkResult.error("Not done yet");
/*  29 */   public static final ChunkResult<ChunkAccess> UNLOADED_CHUNK = ChunkResult.error("Unloaded chunk");
/*  30 */   public static final CompletableFuture<ChunkResult<ChunkAccess>> UNLOADED_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK);
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ChunkPos pos;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ChunkStatus highestAllowedStatus;
/*     */ 
/*     */   
/*  41 */   private final AtomicReference<ChunkStatus> startedWork = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final AtomicReferenceArray<CompletableFuture<ChunkResult<ChunkAccess>>> futures = new AtomicReferenceArray<>(CHUNK_STATUSES.size());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final AtomicReference<ChunkGenerationTask> task = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */   
/*  61 */   private final AtomicInteger generationRefCount = new AtomicInteger();
/*     */   
/*  63 */   private volatile CompletableFuture<Void> generationSaveSyncFuture = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public GenerationChunkHolder(ChunkPos pos) {
/*  66 */     this.pos = pos;
/*  67 */     if (!pos.isValid()) {
/*  68 */       throw new IllegalStateException("Trying to create chunk out of reasonable bounds: " + String.valueOf(pos));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkResult<ChunkAccess>> scheduleChunkGenerationTask(ChunkStatus status, ChunkMap scheduler) {
/*  76 */     if (isStatusDisallowed(status)) {
/*  77 */       return UNLOADED_CHUNK_FUTURE;
/*     */     }
/*  79 */     CompletableFuture<ChunkResult<ChunkAccess>> future = getOrCreateFuture(status);
/*  80 */     if (future.isDone()) {
/*  81 */       return future;
/*     */     }
/*  83 */     ChunkGenerationTask task = this.task.get();
/*  84 */     if (task == null || status.isAfter(task.targetStatus)) {
/*  85 */       rescheduleChunkTask(scheduler, status);
/*     */     }
/*  87 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompletableFuture<ChunkResult<ChunkAccess>> applyStep(ChunkStep step, GeneratingChunkMap chunkMap, StaticCache2D<GenerationChunkHolder> cache) {
/*  94 */     if (isStatusDisallowed(step.targetStatus())) {
/*  95 */       return UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */ 
/*     */     
/*  99 */     if (acquireStatusBump(step.targetStatus())) {
/* 100 */       return chunkMap.applyStep(this, step, cache).handle((chunk, exception) -> {
/*     */             if (exception != null) {
/*     */               CrashReport report = CrashReport.forThrowable(exception, "Exception chunk generation/loading");
/*     */               
/*     */               MinecraftServer.setFatalException((RuntimeException)new ReportedException(report));
/*     */             } else {
/*     */               completeFuture(step.targetStatus(), step);
/*     */             } 
/*     */             return ChunkResult.of(step);
/*     */           });
/*     */     }
/* 111 */     return getOrCreateFuture(step.targetStatus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateHighestAllowedStatus(ChunkMap scheduler) {
/* 118 */     ChunkStatus oldStatus = this.highestAllowedStatus;
/* 119 */     ChunkStatus newStatus = ChunkLevel.generationStatus(getTicketLevel());
/* 120 */     this.highestAllowedStatus = newStatus;
/* 121 */     boolean statusDropped = (oldStatus != null && (newStatus == null || newStatus.isBefore(oldStatus)));
/* 122 */     if (statusDropped) {
/* 123 */       failAndClearPendingFuturesBetween(newStatus, oldStatus);
/*     */       
/* 125 */       if (this.task.get() != null) {
/* 126 */         rescheduleChunkTask(scheduler, findHighestStatusWithPendingFuture(newStatus));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceProtoChunk(ImposterProtoChunk chunk) {
/* 135 */     CompletableFuture<ChunkResult<ChunkAccess>> imposterFuture = CompletableFuture.completedFuture((ChunkResult)ChunkResult.of(chunk));
/*     */     
/* 137 */     for (int i = 0; i < this.futures.length() - 1; i++) {
/* 138 */       CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(i);
/* 139 */       Objects.requireNonNull(future);
/*     */       
/* 141 */       ChunkAccess maybeProtoChunk = ((ChunkResult<ChunkAccess>)future.getNow(NOT_DONE_YET)).orElse(null);
/* 142 */       if (maybeProtoChunk instanceof net.minecraft.world.level.chunk.ProtoChunk) {
/* 143 */         if (!this.futures.compareAndSet(i, future, imposterFuture)) {
/* 144 */           throw new IllegalStateException("Future changed by other thread while trying to replace it");
/*     */         }
/*     */       } else {
/* 147 */         throw new IllegalStateException("Trying to replace a ProtoChunk, but found " + String.valueOf(maybeProtoChunk));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeTask(ChunkGenerationTask task) {
/* 156 */     this.task.compareAndSet(task, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rescheduleChunkTask(ChunkMap scheduler, ChunkStatus status) {
/*     */     ChunkGenerationTask newTask;
/* 164 */     if (status != null) {
/* 165 */       newTask = scheduler.scheduleGenerationTask(status, getPos());
/*     */     } else {
/* 167 */       newTask = null;
/*     */     } 
/* 169 */     ChunkGenerationTask oldTask = this.task.getAndSet(newTask);
/* 170 */     if (oldTask != null) {
/* 171 */       oldTask.markForCancellation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CompletableFuture<ChunkResult<ChunkAccess>> getOrCreateFuture(ChunkStatus status) {
/* 179 */     if (isStatusDisallowed(status)) {
/* 180 */       return UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */     
/* 183 */     int index = status.getIndex();
/* 184 */     CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(index);
/* 185 */     while (future == null) {
/* 186 */       CompletableFuture<ChunkResult<ChunkAccess>> newValue = new CompletableFuture<>();
/* 187 */       future = this.futures.compareAndExchange(index, null, newValue);
/* 188 */       if (future == null) {
/* 189 */         if (isStatusDisallowed(status)) {
/* 190 */           failAndClearPendingFuture(index, newValue);
/* 191 */           return UNLOADED_CHUNK_FUTURE;
/*     */         } 
/* 193 */         return newValue;
/*     */       } 
/*     */     } 
/* 196 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void failAndClearPendingFuturesBetween(ChunkStatus fromExclusive, ChunkStatus toInclusive) {
/* 203 */     int start = (fromExclusive == null) ? 0 : (fromExclusive.getIndex() + 1);
/* 204 */     int end = toInclusive.getIndex();
/* 205 */     for (int i = start; i <= end; i++) {
/* 206 */       CompletableFuture<ChunkResult<ChunkAccess>> previous = this.futures.get(i);
/* 207 */       if (previous != null)
/*     */       {
/*     */         
/* 210 */         failAndClearPendingFuture(i, previous);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void failAndClearPendingFuture(int index, CompletableFuture<ChunkResult<ChunkAccess>> previous) {
/* 219 */     if (previous.complete(UNLOADED_CHUNK) && 
/* 220 */       !this.futures.compareAndSet(index, previous, null)) {
/* 221 */       throw new IllegalStateException("Nothing else should replace the future here");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void completeFuture(ChunkStatus status, ChunkAccess chunk) {
/* 230 */     ChunkResult<ChunkAccess> result = ChunkResult.of(chunk);
/* 231 */     int index = status.getIndex();
/*     */ 
/*     */     
/*     */     while (true) {
/* 235 */       CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(index);
/* 236 */       if (future == null) {
/* 237 */         if (this.futures.compareAndSet(index, null, CompletableFuture.completedFuture(result)))
/*     */           return; 
/*     */         continue;
/*     */       } 
/* 241 */       if (future.complete(result)) {
/*     */         return;
/*     */       }
/* 244 */       if (((ChunkResult)future.getNow(NOT_DONE_YET)).isSuccess()) {
/* 245 */         throw new IllegalStateException("Trying to complete a future but found it to be completed successfully already");
/*     */       }
/*     */       
/* 248 */       Thread.yield();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChunkStatus findHighestStatusWithPendingFuture(ChunkStatus newStatus) {
/* 257 */     if (newStatus == null) {
/* 258 */       return null;
/*     */     }
/* 260 */     ChunkStatus highestStatus = newStatus;
/* 261 */     ChunkStatus alreadyStarted = this.startedWork.get();
/* 262 */     while (alreadyStarted == null || highestStatus.isAfter(alreadyStarted)) {
/*     */       
/* 264 */       if (this.futures.get(highestStatus.getIndex()) != null)
/*     */       {
/* 266 */         return highestStatus;
/*     */       }
/* 268 */       if (highestStatus == ChunkStatus.EMPTY) {
/*     */         break;
/*     */       }
/* 271 */       highestStatus = highestStatus.getParent();
/*     */     } 
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean acquireStatusBump(ChunkStatus status) {
/* 280 */     ChunkStatus parent = (status == ChunkStatus.EMPTY) ? null : status.getParent();
/*     */     
/* 282 */     ChunkStatus previousStarted = this.startedWork.compareAndExchange(parent, status);
/* 283 */     if (previousStarted == parent) {
/* 284 */       return true;
/*     */     }
/*     */     
/* 287 */     if (previousStarted == null || status.isAfter(previousStarted)) {
/* 288 */       throw new IllegalStateException("Unexpected last startedWork status: " + String.valueOf(previousStarted) + " while trying to start: " + String.valueOf(status));
/*     */     }
/*     */     
/* 291 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isStatusDisallowed(ChunkStatus status) {
/* 295 */     ChunkStatus highestAllowedStatus = this.highestAllowedStatus;
/* 296 */     return (highestAllowedStatus == null || status.isAfter(highestAllowedStatus));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseGenerationRefCount() {
/* 305 */     if (this.generationRefCount.getAndIncrement() == 0) {
/* 306 */       this.generationSaveSyncFuture = new CompletableFuture<>();
/* 307 */       addSaveDependency(this.generationSaveSyncFuture);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseGenerationRefCount() {
/* 315 */     CompletableFuture<Void> future = this.generationSaveSyncFuture;
/* 316 */     int newValue = this.generationRefCount.decrementAndGet();
/* 317 */     if (newValue == 0) {
/* 318 */       future.complete(null);
/*     */     }
/* 320 */     if (newValue < 0) {
/* 321 */       throw new IllegalStateException("More releases than claims. Count: " + newValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunkIfPresentUnchecked(ChunkStatus status) {
/* 326 */     CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(status.getIndex());
/* 327 */     return (future == null) ? null : ((ChunkResult<ChunkAccess>)future.getNow(NOT_DONE_YET)).orElse(null);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunkIfPresent(ChunkStatus status) {
/* 331 */     if (isStatusDisallowed(status)) {
/* 332 */       return null;
/*     */     }
/* 334 */     return getChunkIfPresentUnchecked(status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkAccess getLatestChunk() {
/* 341 */     ChunkStatus status = this.startedWork.get();
/* 342 */     if (status == null) {
/* 343 */       return null;
/*     */     }
/* 345 */     ChunkAccess chunk = getChunkIfPresentUnchecked(status);
/* 346 */     if (chunk != null) {
/* 347 */       return chunk;
/*     */     }
/* 349 */     return getChunkIfPresentUnchecked(status.getParent());
/*     */   }
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 353 */     CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(ChunkStatus.EMPTY.getIndex());
/* 354 */     ChunkAccess chunkAccess = (future == null) ? null : ((ChunkResult<ChunkAccess>)future.getNow(NOT_DONE_YET)).orElse(null);
/* 355 */     return (chunkAccess == null) ? null : chunkAccess.getPersistedStatus();
/*     */   }
/*     */   
/*     */   public ChunkPos getPos() {
/* 359 */     return this.pos;
/*     */   }
/*     */   
/*     */   public FullChunkStatus getFullStatus() {
/* 363 */     return ChunkLevel.fullStatus(getTicketLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public List<Pair<ChunkStatus, CompletableFuture<ChunkResult<ChunkAccess>>>> getAllFutures() {
/* 375 */     List<Pair<ChunkStatus, CompletableFuture<ChunkResult<ChunkAccess>>>> result = new ArrayList<>();
/*     */     
/* 377 */     for (int i = 0; i < CHUNK_STATUSES.size(); i++) {
/* 378 */       result.add(Pair.of(CHUNK_STATUSES.get(i), this.futures.get(i)));
/*     */     }
/* 380 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public ChunkStatus getLatestStatus() {
/* 388 */     ChunkStatus status = this.startedWork.get();
/* 389 */     if (status == null) {
/* 390 */       return null;
/*     */     }
/* 392 */     ChunkAccess chunk = getChunkIfPresentUnchecked(status);
/* 393 */     if (chunk != null) {
/* 394 */       return status;
/*     */     }
/* 396 */     return status.getParent();
/*     */   }
/*     */   
/*     */   protected abstract void addSaveDependency(CompletableFuture<?> paramCompletableFuture);
/*     */   
/*     */   public abstract int getTicketLevel();
/*     */   
/*     */   public abstract int getQueueLevel();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/GenerationChunkHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */