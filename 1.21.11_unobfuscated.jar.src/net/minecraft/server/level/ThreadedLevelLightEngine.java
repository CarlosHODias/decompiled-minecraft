/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ThreadedLevelLightEngine
/*     */   extends LevelLightEngine
/*     */   implements AutoCloseable
/*     */ {
/*     */   public static final int DEFAULT_BATCH_SIZE = 1000;
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final ConsecutiveExecutor consecutiveExecutor;
/*  32 */   private final ObjectList<Pair<TaskType, Runnable>> lightTasks = (ObjectList<Pair<TaskType, Runnable>>)new ObjectArrayList();
/*     */   private final ChunkMap chunkMap;
/*     */   private final ChunkTaskDispatcher taskDispatcher;
/*  35 */   private final int taskPerBatch = 1000;
/*  36 */   private final AtomicBoolean scheduled = new AtomicBoolean();
/*     */   
/*     */   public ThreadedLevelLightEngine(LightChunkGetter lightChunkGetter, ChunkMap chunkMap, boolean hasSkyLight, ConsecutiveExecutor consecutiveExecutor, ChunkTaskDispatcher taskDispatcher) {
/*  39 */     super(lightChunkGetter, true, hasSkyLight);
/*  40 */     this.chunkMap = chunkMap;
/*  41 */     this.taskDispatcher = taskDispatcher;
/*  42 */     this.consecutiveExecutor = consecutiveExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */   
/*     */   public int runLightUpdates() {
/*  51 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Ran automatically on a different thread!"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos pos) {
/*  56 */     BlockPos immutable = pos.immutable();
/*  57 */     addTask(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), TaskType.PRE_UPDATE, Util.name(() -> super.checkBlock(immutable), () -> "checkBlock " + String.valueOf(immutable)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateChunkStatus(ChunkPos pos) {
/*  62 */     addTask(pos.x, pos.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> {
/*     */             super.retainData(pos, false);
/*     */             super.setLightEnabled(pos, false);
/*     */             for (int sectionY = getMinLightSection(); sectionY < getMaxLightSection(); sectionY++) {
/*     */               super.queueSectionData(LightLayer.BLOCK, SectionPos.of(pos, sectionY), null);
/*     */               super.queueSectionData(LightLayer.SKY, SectionPos.of(pos, sectionY), null);
/*     */             } 
/*     */             for (int i = this.levelHeightAccessor.getMinSectionY(); i <= this.levelHeightAccessor.getMaxSectionY(); i++) {
/*     */               super.updateSectionStatus(SectionPos.of(pos, i), true);
/*     */             }
/*     */           }, () -> "updateChunkStatus " + String.valueOf(pos) + " true"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) {
/*  81 */     addTask(pos.x(), pos.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.updateSectionStatus(pos, sectionEmpty), () -> "updateSectionStatus " + String.valueOf(pos) + " " + sectionEmpty));
/*     */   }
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos pos) {
/*  86 */     addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> super.propagateLightSources(pos), () -> "propagateLight " + String.valueOf(pos)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos pos, boolean enable) {
/*  91 */     addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> super.setLightEnabled(pos, enable), () -> "enableLight " + String.valueOf(pos) + " " + enable));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueSectionData(LightLayer layer, SectionPos pos, DataLayer data) {
/*  97 */     addTask(pos.x(), pos.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.queueSectionData(layer, pos, data), () -> "queueData " + String.valueOf(pos)));
/*     */   }
/*     */   
/*     */   private void addTask(int chunkX, int chunkZ, TaskType type, Runnable runnable) {
/* 101 */     addTask(chunkX, chunkZ, this.chunkMap.getChunkQueueLevel(ChunkPos.asLong(chunkX, chunkZ)), type, runnable);
/*     */   }
/*     */   
/*     */   private void addTask(int chunkX, int chunkZ, IntSupplier level, TaskType type, Runnable runnable) {
/* 105 */     this.taskDispatcher.submit(() -> {
/*     */           this.lightTasks.add(Pair.of(type, runnable));
/*     */           if (this.lightTasks.size() >= 1000) {
/*     */             runUpdate();
/*     */           }
/* 110 */         }, ChunkPos.asLong(chunkX, chunkZ), level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void retainData(ChunkPos pos, boolean retain) {
/* 115 */     addTask(pos.x, pos.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.retainData(pos, retain), () -> "retainData " + String.valueOf(pos)));
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> initializeLight(ChunkAccess chunk, boolean lighted) {
/* 119 */     ChunkPos pos = chunk.getPos();
/* 120 */     addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> {
/*     */             LevelChunkSection[] sections = chunk.getSections();
/*     */             for (int sectionIndex = 0; sectionIndex < chunk.getSectionsCount(); sectionIndex++) {
/*     */               LevelChunkSection section = sections[sectionIndex];
/*     */               if (!section.hasOnlyAir()) {
/*     */                 int sectionY = this.levelHeightAccessor.getSectionYFromSectionIndex(sectionIndex);
/*     */                 super.updateSectionStatus(SectionPos.of(pos, sectionY), false);
/*     */               } 
/*     */             } 
/*     */           }, () -> "initializeLight: " + String.valueOf(pos)));
/* 130 */     return CompletableFuture.supplyAsync(() -> {
/*     */           super.setLightEnabled(pos, lighted);
/*     */           super.retainData(pos, false);
/*     */           return chunk;
/*     */         }, r -> addTask(pos.x, pos.z, TaskType.POST_UPDATE, pos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> lightChunk(ChunkAccess centerChunk, boolean lighted) {
/* 141 */     ChunkPos pos = centerChunk.getPos();
/* 142 */     centerChunk.setLightCorrect(false);
/* 143 */     addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> {
/*     */             if (!lighted) {
/*     */               super.propagateLightSources(pos);
/*     */             }
/*     */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*     */               LOGGER.debug("LIT {}", pos);
/*     */             }
/*     */           }, () -> "lightChunk " + String.valueOf(pos) + " " + lighted));
/* 151 */     return CompletableFuture.supplyAsync(() -> {
/*     */           centerChunk.setLightCorrect(true);
/*     */           return centerChunk;
/*     */         }, r -> addTask(pos.x, pos.z, TaskType.POST_UPDATE, pos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tryScheduleUpdate() {
/* 161 */     if ((!this.lightTasks.isEmpty() || hasLightWork()) && this.scheduled.compareAndSet(false, true)) {
/* 162 */       this.consecutiveExecutor.schedule(() -> {
/*     */             runUpdate();
/*     */             this.scheduled.set(false);
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private void runUpdate() {
/* 170 */     int totalSize = Math.min(this.lightTasks.size(), 1000);
/*     */     
/* 172 */     ObjectListIterator<Pair<TaskType, Runnable>> iterator = this.lightTasks.iterator();
/*     */     
/* 174 */     int count = 0;
/* 175 */     while (iterator.hasNext() && count < totalSize) {
/* 176 */       Pair<TaskType, Runnable> task = (Pair<TaskType, Runnable>)iterator.next();
/* 177 */       if (task.getFirst() == TaskType.PRE_UPDATE) {
/* 178 */         ((Runnable)task.getSecond()).run();
/*     */       }
/* 180 */       count++;
/*     */     } 
/* 182 */     iterator.back(count);
/*     */     
/* 184 */     super.runLightUpdates();
/*     */     
/* 186 */     count = 0;
/* 187 */     while (iterator.hasNext() && count < totalSize) {
/* 188 */       Pair<TaskType, Runnable> task = (Pair<TaskType, Runnable>)iterator.next();
/* 189 */       if (task.getFirst() == TaskType.POST_UPDATE) {
/* 190 */         ((Runnable)task.getSecond()).run();
/*     */       }
/* 192 */       iterator.remove();
/* 193 */       count++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> waitForPendingTasks(int chunkX, int chunkZ) {
/* 198 */     return CompletableFuture.runAsync(() -> {
/*     */         
/*     */         }, r -> addTask(chunkX, chunkX, TaskType.POST_UPDATE, chunkZ));
/*     */   }
/*     */   
/*     */   private enum TaskType
/*     */   {
/* 205 */     PRE_UPDATE, POST_UPDATE;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ThreadedLevelLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */