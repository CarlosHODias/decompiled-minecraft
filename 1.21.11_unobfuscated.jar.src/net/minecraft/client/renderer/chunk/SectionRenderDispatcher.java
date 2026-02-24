/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.TracingExecutor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.client.renderer.RenderBuffers;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPool;
/*     */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*     */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SectionRenderDispatcher
/*     */ {
/*  42 */   private final CompileTaskDynamicQueue compileQueue = new CompileTaskDynamicQueue();
/*     */   
/*  44 */   private final Queue<Runnable> toUpload = Queues.newConcurrentLinkedQueue(); private final Executor mainThreadUploadExecutor; private final Queue<SectionMesh> toClose; private final SectionBufferBuilderPack fixedBuffers; private final SectionBufferBuilderPool bufferPool; private volatile boolean closed; private final ConsecutiveExecutor consecutiveExecutor; private final TracingExecutor executor; private ClientLevel level; private final LevelRenderer renderer; private Vec3 cameraPosition; private final SectionCompiler sectionCompiler;
/*     */   
/*     */   public SectionRenderDispatcher(ClientLevel level, LevelRenderer renderer, TracingExecutor executor, RenderBuffers renderBuffers, BlockRenderDispatcher blockRenderer, BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
/*  47 */     Objects.requireNonNull(this.toUpload); this.mainThreadUploadExecutor = this.toUpload::add;
/*     */     
/*  49 */     this.toClose = Queues.newConcurrentLinkedQueue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     this.cameraPosition = Vec3.ZERO;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.level = level;
/*  65 */     this.renderer = renderer;
/*  66 */     this.fixedBuffers = renderBuffers.fixedBufferPack();
/*  67 */     this.bufferPool = renderBuffers.sectionBufferPool();
/*  68 */     this.executor = executor;
/*  69 */     this.consecutiveExecutor = new ConsecutiveExecutor((Executor)executor, "Section Renderer");
/*  70 */     this.consecutiveExecutor.schedule(this::runTask);
/*     */     
/*  72 */     this.sectionCompiler = new SectionCompiler(blockRenderer, blockEntityRenderDispatcher);
/*     */   }
/*     */   
/*     */   public void setLevel(ClientLevel level) {
/*  76 */     this.level = level;
/*     */   }
/*     */ 
/*     */   
/*     */   private void runTask() {
/*  81 */     if (this.closed || this.bufferPool.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     RenderSection.CompileTask task = this.compileQueue.poll(this.cameraPosition);
/*  86 */     if (task == null) {
/*     */       return;
/*     */     }
/*  89 */     SectionBufferBuilderPack buffer = Objects.<SectionBufferBuilderPack>requireNonNull(this.bufferPool.acquire());
/*     */ 
/*     */     
/*  92 */     CompletableFuture.supplyAsync(() -> task.doTask(buffer), this.executor.forName(task.name()))
/*  93 */       .thenCompose(f -> f)
/*  94 */       .whenComplete((result, throwable) -> {
/*     */           if (throwable != null) {
/*     */             Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Batching sections"));
/*     */             return;
/*     */           } 
/*     */           task.isCompleted.set(true);
/*     */           this.consecutiveExecutor.schedule(());
/*     */         });
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
/*     */   public void setCameraPosition(Vec3 cameraPosition) {
/* 113 */     this.cameraPosition = cameraPosition;
/*     */   }
/*     */   
/*     */   public void uploadAllPendingUploads() {
/*     */     Runnable upload;
/* 118 */     while ((upload = this.toUpload.poll()) != null) {
/* 119 */       upload.run();
/*     */     }
/*     */     SectionMesh mesh;
/* 122 */     while ((mesh = this.toClose.poll()) != null) {
/* 123 */       mesh.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void rebuildSectionSync(RenderSection section, RenderRegionCache cache) {
/* 128 */     section.compileSync(cache);
/*     */   }
/*     */   
/*     */   public void schedule(RenderSection.CompileTask task) {
/* 132 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 135 */     this.consecutiveExecutor.schedule(() -> {
/*     */           if (this.closed) {
/*     */             return;
/*     */           }
/*     */           this.compileQueue.add(task);
/*     */           runTask();
/*     */         });
/*     */   }
/*     */   
/*     */   public void clearCompileQueue() {
/* 145 */     this.compileQueue.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isQueueEmpty() {
/* 150 */     return (this.compileQueue.size() == 0 && this.toUpload.isEmpty());
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 154 */     this.closed = true;
/* 155 */     clearCompileQueue();
/*     */     
/* 157 */     uploadAllPendingUploads();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public String getStats() {
/* 162 */     return String.format(Locale.ROOT, "pC: %03d, pU: %02d, aB: %02d", new Object[] { this.compileQueue.size(), this.toUpload.size(), this.bufferPool.getFreeBufferCount() });
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int getCompileQueueSize() {
/* 167 */     return this.compileQueue.size();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int getToUpload() {
/* 172 */     return this.toUpload.size();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int getFreeBufferCount() {
/* 177 */     return this.bufferPool.getFreeBufferCount();
/*     */   }
/*     */   
/*     */   public class RenderSection
/*     */   {
/*     */     public static final int SIZE = 16;
/*     */     public final int index;
/* 184 */     public final AtomicReference<SectionMesh> sectionMesh = new AtomicReference<>(CompiledSectionMesh.UNCOMPILED);
/*     */     
/*     */     private RebuildTask lastRebuildTask;
/*     */     
/*     */     private ResortTransparencyTask lastResortTransparencyTask;
/*     */     
/*     */     private AABB bb;
/*     */     private boolean dirty = true;
/* 192 */     private volatile long sectionNode = SectionPos.asLong(-1, -1, -1);
/* 193 */     private final BlockPos.MutableBlockPos renderOrigin = new BlockPos.MutableBlockPos(-1, -1, -1);
/*     */     
/*     */     private boolean playerChanged;
/*     */     private long uploadedTime;
/*     */     private long fadeDuration;
/*     */     private boolean wasPreviouslyEmpty;
/*     */     
/*     */     public RenderSection(int index, long sectionNode) {
/* 201 */       this.index = index;
/* 202 */       setSectionNode(sectionNode);
/*     */     }
/*     */     
/*     */     public float getVisibility(long now) {
/* 206 */       long elapsed = now - this.uploadedTime;
/* 207 */       if (elapsed >= this.fadeDuration) {
/* 208 */         return 1.0F;
/*     */       }
/* 210 */       return (float)elapsed / (float)this.fadeDuration;
/*     */     }
/*     */     
/*     */     public void setFadeDuration(long fadeDuration) {
/* 214 */       this.fadeDuration = fadeDuration;
/*     */     }
/*     */     
/*     */     public void setWasPreviouslyEmpty(boolean wasPreviouslyEmpty) {
/* 218 */       this.wasPreviouslyEmpty = wasPreviouslyEmpty;
/*     */     }
/*     */     
/*     */     public boolean wasPreviouslyEmpty() {
/* 222 */       return this.wasPreviouslyEmpty;
/*     */     }
/*     */     
/*     */     private boolean doesChunkExistAt(long sectionNode) {
/* 226 */       ChunkAccess chunk = SectionRenderDispatcher.this.level.getChunk(SectionPos.x(sectionNode), SectionPos.z(sectionNode), ChunkStatus.FULL, false);
/* 227 */       return (chunk != null && SectionRenderDispatcher.this.level.getLightEngine().lightOnInColumn(SectionPos.getZeroNode(sectionNode)));
/*     */     }
/*     */     
/*     */     public boolean hasAllNeighbors() {
/* 231 */       return (doesChunkExistAt(SectionPos.offset(this.sectionNode, Direction.WEST)) && 
/* 232 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, Direction.NORTH)) && 
/* 233 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, Direction.EAST)) && 
/* 234 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, Direction.SOUTH)) && 
/* 235 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, -1, 0, -1)) && 
/* 236 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, -1, 0, 1)) && 
/* 237 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, 1, 0, -1)) && 
/* 238 */         doesChunkExistAt(SectionPos.offset(this.sectionNode, 1, 0, 1)));
/*     */     }
/*     */     
/*     */     public AABB getBoundingBox() {
/* 242 */       return this.bb;
/*     */     }
/*     */     
/*     */     public CompletableFuture<Void> upload(Map<ChunkSectionLayer, MeshData> renderedLayers, CompiledSectionMesh compiledSectionMesh) {
/* 246 */       if (SectionRenderDispatcher.this.closed) {
/* 247 */         renderedLayers.values().forEach(MeshData::close);
/* 248 */         return CompletableFuture.completedFuture(null);
/*     */       } 
/* 250 */       return CompletableFuture.runAsync(() -> renderedLayers.forEach(()), SectionRenderDispatcher.this.mainThreadUploadExecutor);
/*     */     }
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
/*     */     public CompletableFuture<Void> uploadSectionIndexBuffer(CompiledSectionMesh compiledSectionMesh, ByteBufferBuilder.Result indexBuffer, ChunkSectionLayer layer) {
/* 264 */       if (SectionRenderDispatcher.this.closed) {
/* 265 */         indexBuffer.close();
/* 266 */         return CompletableFuture.completedFuture(null);
/*     */       } 
/* 268 */       return CompletableFuture.runAsync(() -> { Zone ignored = Profiler.get().zone("Upload Section Indices"); try { compiledSectionMesh.uploadLayerIndexBuffer(layer, indexBuffer, this.sectionNode); indexBuffer.close(); if (ignored != null)
/* 269 */                 ignored.close();  } catch (Throwable throwable) { if (ignored != null) try { ignored.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */                  
/*     */               throw throwable; }
/*     */           
/*     */           }, SectionRenderDispatcher.this.mainThreadUploadExecutor);
/*     */     }
/*     */     public void setSectionNode(long sectionNode) {
/* 277 */       reset();
/* 278 */       this.sectionNode = sectionNode;
/* 279 */       int x = SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode));
/* 280 */       int y = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 281 */       int z = SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode));
/* 282 */       this.renderOrigin.set(x, y, z);
/* 283 */       this.bb = new AABB(x, y, z, (x + 16), (y + 16), (z + 16));
/*     */     }
/*     */     
/*     */     public SectionMesh getSectionMesh() {
/* 287 */       return this.sectionMesh.get();
/*     */     }
/*     */     
/*     */     public void reset() {
/* 291 */       cancelTasks();
/* 292 */       ((SectionMesh)this.sectionMesh.getAndSet(CompiledSectionMesh.UNCOMPILED)).close();
/* 293 */       this.dirty = true;
/* 294 */       this.uploadedTime = 0L;
/* 295 */       this.wasPreviouslyEmpty = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos getRenderOrigin() {
/* 300 */       return (BlockPos)this.renderOrigin;
/*     */     }
/*     */     
/*     */     public long getSectionNode() {
/* 304 */       return this.sectionNode;
/*     */     }
/*     */     
/*     */     public void setDirty(boolean fromPlayer) {
/* 308 */       boolean wasDirty = this.dirty;
/* 309 */       this.dirty = true;
/* 310 */       this.playerChanged = fromPlayer | ((wasDirty && this.playerChanged));
/*     */     }
/*     */     
/*     */     public void setNotDirty() {
/* 314 */       this.dirty = false;
/* 315 */       this.playerChanged = false;
/*     */     }
/*     */     
/*     */     public boolean isDirty() {
/* 319 */       return this.dirty;
/*     */     }
/*     */     
/*     */     public boolean isDirtyFromPlayer() {
/* 323 */       return (this.dirty && this.playerChanged);
/*     */     }
/*     */     
/*     */     public long getNeighborSectionNode(Direction direction) {
/* 327 */       return SectionPos.offset(this.sectionNode, direction);
/*     */     }
/*     */     
/*     */     public void resortTransparency(SectionRenderDispatcher dispatcher) {
/* 331 */       SectionMesh sectionMesh = getSectionMesh(); if (sectionMesh instanceof CompiledSectionMesh) { CompiledSectionMesh mesh = (CompiledSectionMesh)sectionMesh;
/* 332 */         this.lastResortTransparencyTask = new ResortTransparencyTask(mesh);
/* 333 */         dispatcher.schedule(this.lastResortTransparencyTask); }
/*     */     
/*     */     }
/*     */     
/*     */     public boolean hasTranslucentGeometry() {
/* 338 */       return getSectionMesh().hasTranslucentGeometry();
/*     */     }
/*     */     
/*     */     public boolean transparencyResortingScheduled() {
/* 342 */       return (this.lastResortTransparencyTask != null && !this.lastResortTransparencyTask.isCompleted.get());
/*     */     }
/*     */     
/*     */     protected void cancelTasks() {
/* 346 */       if (this.lastRebuildTask != null) {
/* 347 */         this.lastRebuildTask.cancel();
/* 348 */         this.lastRebuildTask = null;
/*     */       } 
/* 350 */       if (this.lastResortTransparencyTask != null) {
/* 351 */         this.lastResortTransparencyTask.cancel();
/* 352 */         this.lastResortTransparencyTask = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public CompileTask createCompileTask(RenderRegionCache cache) {
/* 357 */       cancelTasks();
/* 358 */       RenderSectionRegion region = cache.createRegion((Level)SectionRenderDispatcher.this.level, this.sectionNode);
/* 359 */       boolean isRecompile = (this.sectionMesh.get() != CompiledSectionMesh.UNCOMPILED);
/* 360 */       this.lastRebuildTask = new RebuildTask(region, isRecompile);
/* 361 */       return this.lastRebuildTask;
/*     */     }
/*     */     
/*     */     public void rebuildSectionAsync(RenderRegionCache cache) {
/* 365 */       CompileTask task = createCompileTask(cache);
/* 366 */       SectionRenderDispatcher.this.schedule(task);
/*     */     }
/*     */     
/*     */     public void compileSync(RenderRegionCache cache) {
/* 370 */       CompileTask task = createCompileTask(cache);
/*     */ 
/*     */       
/* 373 */       task.doTask(SectionRenderDispatcher.this.fixedBuffers);
/*     */     }
/*     */     
/*     */     private void setSectionMesh(SectionMesh sectionMesh) {
/* 377 */       SectionMesh oldMesh = this.sectionMesh.getAndSet(sectionMesh);
/* 378 */       SectionRenderDispatcher.this.toClose.add(oldMesh);
/* 379 */       SectionRenderDispatcher.this.renderer.addRecentlyCompiledSection(this);
/*     */     }
/*     */     
/*     */     private VertexSorting createVertexSorting(SectionPos sectionPos) {
/* 383 */       Vec3 camera = SectionRenderDispatcher.this.cameraPosition;
/* 384 */       return VertexSorting.byDistance(
/* 385 */           (float)(camera.x - sectionPos.minBlockX()), 
/* 386 */           (float)(camera.y - sectionPos.minBlockY()), 
/* 387 */           (float)(camera.z - sectionPos.minBlockZ()));
/*     */     }
/*     */     
/*     */     private class RebuildTask
/*     */       extends CompileTask {
/*     */       protected final RenderSectionRegion region;
/*     */       
/*     */       public RebuildTask(RenderSectionRegion region, boolean isRecompile) {
/* 395 */         super(isRecompile);
/* 396 */         this.region = region;
/*     */       }
/*     */ 
/*     */       
/*     */       protected String name() {
/* 401 */         return "rend_chk_rebuild";
/*     */       }
/*     */       
/*     */       public CompletableFuture<SectionRenderDispatcher.SectionTaskResult> doTask(SectionBufferBuilderPack buffers) {
/*     */         SectionCompiler.Results results;
/* 406 */         if (this.isCancelled.get()) {
/* 407 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         }
/*     */         
/* 410 */         long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 411 */         SectionPos sectionPos = SectionPos.of(sectionNode);
/*     */ 
/*     */ 
/*     */         
/* 415 */         if (this.isCancelled.get()) {
/* 416 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         }
/*     */ 
/*     */         
/* 420 */         Zone ignored = Profiler.get().zone("Compile Section"); 
/* 421 */         try { results = SectionRenderDispatcher.this.sectionCompiler.compile(sectionPos, this.region, SectionRenderDispatcher.RenderSection.this.createVertexSorting(sectionPos), buffers);
/* 422 */           if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/* 423 */             try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(SectionRenderDispatcher.this.cameraPosition, sectionNode);
/*     */         
/* 425 */         if (this.isCancelled.get()) {
/* 426 */           results.release();
/* 427 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         } 
/*     */         
/* 430 */         CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results);
/*     */         
/* 432 */         CompletableFuture<Void> uploadFuture = SectionRenderDispatcher.RenderSection.this.upload(results.renderedLayers, compiledSectionMesh);
/*     */         
/* 434 */         return uploadFuture.handle((ignored, throwable) -> {
/*     */               if (throwable != null && !(throwable instanceof java.util.concurrent.CancellationException) && !(throwable instanceof InterruptedException)) {
/*     */                 Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Rendering section"));
/*     */               }
/*     */               if (this.isCancelled.get() || SectionRenderDispatcher.this.closed) {
/*     */                 SectionRenderDispatcher.this.toClose.add(compiledSectionMesh);
/*     */                 return SectionRenderDispatcher.SectionTaskResult.CANCELLED;
/*     */               } 
/*     */               SectionRenderDispatcher.RenderSection.this.setSectionMesh(compiledSectionMesh);
/*     */               return SectionRenderDispatcher.SectionTaskResult.SUCCESSFUL;
/*     */             });
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void cancel() {
/* 450 */         if (this.isCancelled.compareAndSet(false, true))
/* 451 */           SectionRenderDispatcher.RenderSection.this.setDirty(false); 
/*     */       }
/*     */     }
/*     */     
/*     */     private class ResortTransparencyTask
/*     */       extends CompileTask {
/*     */       private final CompiledSectionMesh compiledSectionMesh;
/*     */       
/*     */       public ResortTransparencyTask(CompiledSectionMesh compiledSectionMesh) {
/* 460 */         super(true);
/* 461 */         this.compiledSectionMesh = compiledSectionMesh;
/*     */       }
/*     */ 
/*     */       
/*     */       protected String name() {
/* 466 */         return "rend_chk_sort";
/*     */       }
/*     */ 
/*     */       
/*     */       public CompletableFuture<SectionRenderDispatcher.SectionTaskResult> doTask(SectionBufferBuilderPack buffers) {
/* 471 */         if (this.isCancelled.get()) {
/* 472 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         }
/*     */         
/* 475 */         MeshData.SortState state = this.compiledSectionMesh.getTransparencyState();
/* 476 */         if (state == null || this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT)) {
/* 477 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         }
/*     */         
/* 480 */         long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode;
/* 481 */         VertexSorting vertexSorting = SectionRenderDispatcher.RenderSection.this.createVertexSorting(SectionPos.of(sectionNode));
/* 482 */         TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(SectionRenderDispatcher.this.cameraPosition, sectionNode);
/*     */         
/* 484 */         if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned()) {
/* 485 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         }
/*     */         
/* 488 */         ByteBufferBuilder.Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting);
/* 489 */         if (indexBuffer == null) {
/* 490 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         }
/*     */         
/* 493 */         if (this.isCancelled.get()) {
/* 494 */           indexBuffer.close();
/* 495 */           return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);
/*     */         } 
/*     */         
/* 498 */         CompletableFuture<Void> future = SectionRenderDispatcher.RenderSection.this.uploadSectionIndexBuffer(this.compiledSectionMesh, indexBuffer, ChunkSectionLayer.TRANSLUCENT);
/*     */         
/* 500 */         return future.handle((ignored, throwable) -> {
/*     */               if (throwable != null && !(throwable instanceof java.util.concurrent.CancellationException) && !(throwable instanceof InterruptedException)) {
/*     */                 Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Rendering section"));
/*     */               }
/*     */               if (this.isCancelled.get()) {
/*     */                 return SectionRenderDispatcher.SectionTaskResult.CANCELLED;
/*     */               }
/*     */               this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView);
/*     */               return SectionRenderDispatcher.SectionTaskResult.SUCCESSFUL;
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public void cancel() {
/* 514 */         this.isCancelled.set(true);
/*     */       }
/*     */     }
/*     */     
/*     */     public abstract class CompileTask {
/* 519 */       protected final AtomicBoolean isCancelled = new AtomicBoolean(false);
/* 520 */       protected final AtomicBoolean isCompleted = new AtomicBoolean(false);
/*     */       protected final boolean isRecompile;
/*     */       
/*     */       public CompileTask(boolean isRecompile) {
/* 524 */         this.isRecompile = isRecompile;
/*     */       }
/*     */       
/*     */       public abstract CompletableFuture<SectionRenderDispatcher.SectionTaskResult> doTask(SectionBufferBuilderPack param2SectionBufferBuilderPack);
/*     */       
/*     */       public abstract void cancel();
/*     */       
/*     */       protected abstract String name();
/*     */       
/*     */       public boolean isRecompile() {
/* 534 */         return this.isRecompile;
/*     */       }
/*     */       
/*     */       public BlockPos getRenderOrigin()
/*     */       {
/* 539 */         return (BlockPos)SectionRenderDispatcher.RenderSection.this.renderOrigin; } } } private class RebuildTask extends RenderSection.CompileTask { protected final RenderSectionRegion region; public RebuildTask(RenderSectionRegion region, boolean isRecompile) { super((SectionRenderDispatcher.RenderSection)this$0, isRecompile); this.region = region; } protected String name() { return "rend_chk_rebuild"; } public CompletableFuture<SectionRenderDispatcher.SectionTaskResult> doTask(SectionBufferBuilderPack buffers) { SectionCompiler.Results results; if (this.isCancelled.get()) return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);  long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode; SectionPos sectionPos = SectionPos.of(sectionNode); if (this.isCancelled.get()) return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);  Zone ignored = Profiler.get().zone("Compile Section"); try { results = SectionRenderDispatcher.this.sectionCompiler.compile(sectionPos, this.region, SectionRenderDispatcher.RenderSection.this.createVertexSorting(sectionPos), buffers); if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null) try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(SectionRenderDispatcher.this.cameraPosition, sectionNode); if (this.isCancelled.get()) { results.release(); return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED); }  CompiledSectionMesh compiledSectionMesh = new CompiledSectionMesh(translucencyPointOfView, results); CompletableFuture<Void> uploadFuture = SectionRenderDispatcher.RenderSection.this.upload(results.renderedLayers, compiledSectionMesh); return uploadFuture.handle((ignored, throwable) -> { if (throwable != null && !(throwable instanceof java.util.concurrent.CancellationException) && !(throwable instanceof InterruptedException)) Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Rendering section"));  if (this.isCancelled.get() || SectionRenderDispatcher.this.closed) { SectionRenderDispatcher.this.toClose.add(compiledSectionMesh); return SectionRenderDispatcher.SectionTaskResult.CANCELLED; }  SectionRenderDispatcher.RenderSection.this.setSectionMesh(compiledSectionMesh); return SectionRenderDispatcher.SectionTaskResult.SUCCESSFUL; }); } public void cancel() { if (this.isCancelled.compareAndSet(false, true)) SectionRenderDispatcher.RenderSection.this.setDirty(false);  } } private class ResortTransparencyTask extends RenderSection.CompileTask { private final CompiledSectionMesh compiledSectionMesh; public ResortTransparencyTask(CompiledSectionMesh compiledSectionMesh) { super((SectionRenderDispatcher.RenderSection)this$0, true); this.compiledSectionMesh = compiledSectionMesh; } protected String name() { return "rend_chk_sort"; } public CompletableFuture<SectionRenderDispatcher.SectionTaskResult> doTask(SectionBufferBuilderPack buffers) { if (this.isCancelled.get()) return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);  MeshData.SortState state = this.compiledSectionMesh.getTransparencyState(); if (state == null || this.compiledSectionMesh.isEmpty(ChunkSectionLayer.TRANSLUCENT)) return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);  long sectionNode = SectionRenderDispatcher.RenderSection.this.sectionNode; VertexSorting vertexSorting = SectionRenderDispatcher.RenderSection.this.createVertexSorting(SectionPos.of(sectionNode)); TranslucencyPointOfView translucencyPointOfView = TranslucencyPointOfView.of(SectionRenderDispatcher.this.cameraPosition, sectionNode); if (!this.compiledSectionMesh.isDifferentPointOfView(translucencyPointOfView) && !translucencyPointOfView.isAxisAligned()) return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);  ByteBufferBuilder.Result indexBuffer = state.buildSortedIndexBuffer(buffers.buffer(ChunkSectionLayer.TRANSLUCENT), vertexSorting); if (indexBuffer == null) return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED);  if (this.isCancelled.get()) { indexBuffer.close(); return CompletableFuture.completedFuture(SectionRenderDispatcher.SectionTaskResult.CANCELLED); }  CompletableFuture<Void> future = SectionRenderDispatcher.RenderSection.this.uploadSectionIndexBuffer(this.compiledSectionMesh, indexBuffer, ChunkSectionLayer.TRANSLUCENT); return future.handle((ignored, throwable) -> { if (throwable != null && !(throwable instanceof java.util.concurrent.CancellationException) && !(throwable instanceof InterruptedException)) Minecraft.getInstance().delayCrash(CrashReport.forThrowable(throwable, "Rendering section"));  if (this.isCancelled.get()) return SectionRenderDispatcher.SectionTaskResult.CANCELLED;  this.compiledSectionMesh.setTranslucencyPointOfView(translucencyPointOfView); return SectionRenderDispatcher.SectionTaskResult.SUCCESSFUL; }); } public void cancel() { this.isCancelled.set(true); } } public abstract class CompileTask { protected final AtomicBoolean isCancelled = new AtomicBoolean(false); protected final AtomicBoolean isCompleted = new AtomicBoolean(false); public BlockPos getRenderOrigin() { return (BlockPos)SectionRenderDispatcher.RenderSection.this.renderOrigin; }
/*     */      protected final boolean isRecompile; public CompileTask(boolean isRecompile) {
/*     */       this.isRecompile = isRecompile;
/*     */     } public abstract CompletableFuture<SectionRenderDispatcher.SectionTaskResult> doTask(SectionBufferBuilderPack param1SectionBufferBuilderPack); public abstract void cancel(); protected abstract String name(); public boolean isRecompile() {
/*     */       return this.isRecompile;
/*     */     } }
/* 545 */   private enum SectionTaskResult { SUCCESSFUL,
/* 546 */     CANCELLED; }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/SectionRenderDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */