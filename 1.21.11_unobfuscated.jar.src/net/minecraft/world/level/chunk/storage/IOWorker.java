/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.BitSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.SequencedMap;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.CollectFields;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.PriorityConsecutiveExecutor;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
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
/*     */ public class IOWorker
/*     */   implements AutoCloseable, ChunkScanAccess
/*     */ {
/*     */   public static final Supplier<CompoundTag> STORE_EMPTY = () -> null;
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private enum Priority {
/*  52 */     FOREGROUND, BACKGROUND, SHUTDOWN;
/*     */   }
/*     */   @FunctionalInterface
/*     */   private static interface ThrowingSupplier<T> { T get() throws Exception; }
/*     */   
/*  57 */   private static class PendingStore { private final CompletableFuture<Void> result = new CompletableFuture<>(); private CompoundTag data;
/*     */     
/*     */     public PendingStore(CompoundTag data) {
/*  60 */       this.data = data;
/*     */     }
/*     */     
/*     */     private CompoundTag copyData() {
/*  64 */       CompoundTag data = this.data;
/*  65 */       return (data == null) ? null : data.copy();
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*  70 */   private final AtomicBoolean shutdownRequested = new AtomicBoolean();
/*     */   
/*     */   private final PriorityConsecutiveExecutor consecutiveExecutor;
/*     */   
/*     */   private final RegionFileStorage storage;
/*  75 */   private final SequencedMap<ChunkPos, PendingStore> pendingWrites = new LinkedHashMap<>();
/*     */   
/*  77 */   private final Long2ObjectLinkedOpenHashMap<CompletableFuture<BitSet>> regionCacheForBlender = new Long2ObjectLinkedOpenHashMap();
/*     */   private static final int REGION_CACHE_SIZE = 1024;
/*     */   
/*     */   protected IOWorker(RegionStorageInfo info, Path dir, boolean sync) {
/*  81 */     this.storage = new RegionFileStorage(info, dir, sync);
/*  82 */     this.consecutiveExecutor = new PriorityConsecutiveExecutor((Priority.values()).length, (Executor)Util.ioPool(), "IOWorker-" + info.type());
/*     */   }
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos pos, int range) {
/*  86 */     ChunkPos from = new ChunkPos(pos.x - range, pos.z - range);
/*  87 */     ChunkPos to = new ChunkPos(pos.x + range, pos.z + range);
/*     */     
/*  89 */     for (int regionX = from.getRegionX(); regionX <= to.getRegionX(); regionX++) {
/*  90 */       for (int regionZ = from.getRegionZ(); regionZ <= to.getRegionZ(); regionZ++) {
/*     */         
/*  92 */         BitSet data = getOrCreateOldDataForRegion(regionX, regionZ).join();
/*  93 */         if (!data.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  98 */           ChunkPos minChunkPos = ChunkPos.minFromRegion(regionX, regionZ);
/*  99 */           int startChunkX = Math.max(from.x - minChunkPos.x, 0);
/* 100 */           int startChunkZ = Math.max(from.z - minChunkPos.z, 0);
/* 101 */           int endChunkX = Math.min(to.x - minChunkPos.x, 31);
/* 102 */           int endChunkZ = Math.min(to.z - minChunkPos.z, 31);
/*     */           
/* 104 */           for (int x = startChunkX; x <= endChunkX; x++) {
/* 105 */             for (int z = startChunkZ; z <= endChunkZ; z++) {
/* 106 */               int chunkIndex = z * 32 + x;
/* 107 */               if (data.get(chunkIndex)) {
/* 108 */                 return true;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<BitSet> getOrCreateOldDataForRegion(int regionX, int regionZ) {
/* 119 */     long regionPos = ChunkPos.asLong(regionX, regionZ);
/* 120 */     synchronized (this.regionCacheForBlender) {
/* 121 */       CompletableFuture<BitSet> result = (CompletableFuture<BitSet>)this.regionCacheForBlender.getAndMoveToFirst(regionPos);
/* 122 */       if (result == null) {
/* 123 */         result = createOldDataForRegion(regionX, regionZ);
/*     */         
/* 125 */         this.regionCacheForBlender.putAndMoveToFirst(regionPos, result);
/* 126 */         if (this.regionCacheForBlender.size() > 1024) {
/* 127 */           this.regionCacheForBlender.removeLast();
/*     */         }
/*     */       } 
/* 130 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<BitSet> createOldDataForRegion(int regionX, int regionZ) {
/* 135 */     return CompletableFuture.supplyAsync(() -> {
/*     */           ChunkPos from = ChunkPos.minFromRegion(regionX, regionZ), to = ChunkPos.maxFromRegion(regionX, regionZ);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           BitSet resultSet = new BitSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           ChunkPos.rangeClosed(from, to).forEach(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return resultSet;
/* 163 */         }, (Executor)Util.backgroundExecutor());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isOldChunk(CompoundTag tag) {
/* 168 */     if (tag.getIntOr("DataVersion", 0) < 4295) {
/* 169 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 173 */     return tag.getCompound("blending_data").isPresent();
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> store(ChunkPos pos, CompoundTag value) {
/* 177 */     return store(pos, () -> value);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> store(ChunkPos pos, Supplier<CompoundTag> supplier) {
/* 181 */     return submitTask(() -> {
/*     */           CompoundTag data = supplier.get();
/*     */           PendingStore pendingStore = this.pendingWrites.computeIfAbsent(pos, ());
/*     */           pendingStore.data = data;
/*     */           return pendingStore.result;
/* 186 */         }).thenCompose(Function.identity());
/*     */   }
/*     */   
/*     */   public CompletableFuture<Optional<CompoundTag>> loadAsync(ChunkPos pos) {
/* 190 */     return submitThrowingTask(() -> {
/*     */           PendingStore pendingStore = this.pendingWrites.get(pos);
/*     */           if (pendingStore != null) {
/*     */             return Optional.ofNullable(pendingStore.copyData());
/*     */           }
/*     */           try {
/*     */             CompoundTag data = this.storage.read(pos);
/*     */             return Optional.ofNullable(data);
/* 198 */           } catch (Exception e) {
/*     */             LOGGER.warn("Failed to read chunk {}", pos, e);
/*     */             throw e;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> synchronize(boolean flush) {
/* 207 */     CompletableFuture<Void> currentWrites = submitTask(() -> CompletableFuture.allOf((CompletableFuture<?>[])this.pendingWrites.values().stream().map(()).toArray(()))).thenCompose(Function.identity());
/* 208 */     if (flush) {
/* 209 */       return currentWrites.thenCompose(ignore -> submitThrowingTask(()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     return currentWrites.thenCompose(ignore -> submitTask(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> scanChunk(ChunkPos pos, StreamTagVisitor visitor) {
/* 225 */     return submitThrowingTask(() -> {
/*     */           try {
/*     */             PendingStore pendingStore = this.pendingWrites.get(pos);
/*     */             if (pendingStore != null) {
/*     */               if (pendingStore.data != null) {
/*     */                 pendingStore.data.acceptAsRoot(visitor);
/*     */               }
/*     */             } else {
/*     */               this.storage.scanChunk(pos, visitor);
/*     */             } 
/*     */             return null;
/* 236 */           } catch (Exception e) {
/*     */             LOGGER.warn("Failed to bulk scan chunk {}", pos, e);
/*     */             throw e;
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private <T> CompletableFuture<T> submitThrowingTask(ThrowingSupplier<T> task) {
/* 244 */     return this.consecutiveExecutor.scheduleWithResult(Priority.FOREGROUND.ordinal(), future -> {
/*     */           if (!this.shutdownRequested.get()) {
/*     */             try {
/*     */               task.complete(task.get());
/* 248 */             } catch (Exception e) {
/*     */               task.completeExceptionally(e);
/*     */             } 
/*     */           }
/*     */           tellStorePending();
/*     */         });
/*     */   }
/*     */   
/*     */   private <T> CompletableFuture<T> submitTask(Supplier<T> task) {
/* 257 */     return this.consecutiveExecutor.scheduleWithResult(Priority.FOREGROUND.ordinal(), future -> {
/*     */           if (!this.shutdownRequested.get()) {
/*     */             task.complete(task.get());
/*     */           }
/*     */           tellStorePending();
/*     */         });
/*     */   }
/*     */   
/*     */   private void storePendingChunk() {
/* 266 */     Map.Entry<ChunkPos, PendingStore> entry = this.pendingWrites.pollFirstEntry();
/* 267 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 270 */     runStore(entry.getKey(), entry.getValue());
/* 271 */     tellStorePending();
/*     */   }
/*     */   
/*     */   private void tellStorePending() {
/* 275 */     this.consecutiveExecutor.schedule((Runnable)new StrictQueue.RunnableWithPriority(Priority.BACKGROUND.ordinal(), this::storePendingChunk));
/*     */   }
/*     */   
/*     */   private void runStore(ChunkPos pos, PendingStore write) {
/*     */     try {
/* 280 */       this.storage.write(pos, write.data);
/* 281 */       write.result.complete(null);
/* 282 */     } catch (Exception e) {
/* 283 */       LOGGER.error("Failed to store chunk {}", pos, e);
/* 284 */       write.result.completeExceptionally(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 290 */     if (!this.shutdownRequested.compareAndSet(false, true)) {
/*     */       return;
/*     */     }
/*     */     
/* 294 */     waitForShutdown();
/* 295 */     this.consecutiveExecutor.close();
/*     */     
/*     */     try {
/* 298 */       this.storage.close();
/* 299 */     } catch (Exception e) {
/* 300 */       LOGGER.error("Failed to close storage", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void waitForShutdown() {
/* 306 */     this.consecutiveExecutor.scheduleWithResult(Priority.SHUTDOWN.ordinal(), future -> future.complete(Unit.INSTANCE)).join();
/*     */   }
/*     */   
/*     */   public RegionStorageInfo storageInfo() {
/* 310 */     return this.storage.info();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/storage/IOWorker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */