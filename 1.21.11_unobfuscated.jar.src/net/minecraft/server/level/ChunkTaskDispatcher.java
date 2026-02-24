/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.thread.PriorityConsecutiveExecutor;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.util.thread.TaskScheduler;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkTaskDispatcher
/*     */   implements ChunkHolder.LevelChangeListener, AutoCloseable
/*     */ {
/*     */   public static final int DISPATCHER_PRIORITY_COUNT = 4;
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final ChunkTaskPriorityQueue queue;
/*     */   private final TaskScheduler<Runnable> executor;
/*     */   private final PriorityConsecutiveExecutor dispatcher;
/*     */   protected boolean sleeping;
/*     */   
/*     */   public ChunkTaskDispatcher(TaskScheduler<Runnable> executor, Executor dispatcherExecutor) {
/*  27 */     this.queue = new ChunkTaskPriorityQueue(executor.name() + "_queue");
/*  28 */     this.executor = executor;
/*  29 */     this.dispatcher = new PriorityConsecutiveExecutor(4, dispatcherExecutor, "dispatcher");
/*  30 */     this.sleeping = true;
/*     */   }
/*     */   
/*     */   public boolean hasWork() {
/*  34 */     return (this.dispatcher.hasWork() || this.queue.hasWork());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLevelChange(ChunkPos pos, IntSupplier oldLevel, int newLevel, IntConsumer setQueueLevel) {
/*  39 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(0, () -> {
/*     */             int oldTicketLevel = oldLevel.getAsInt();
/*     */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*     */               LOGGER.debug("RES {} {} -> {}", new Object[] { pos, oldTicketLevel, newLevel });
/*     */             }
/*     */             this.queue.resortChunkTasks(oldTicketLevel, pos, newLevel);
/*     */             setQueueLevel.accept(newLevel);
/*     */           }));
/*     */   }
/*     */   
/*     */   public void release(long pos, Runnable whenReleased, boolean clearQueue) {
/*  50 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(1, () -> {
/*     */             this.queue.release(pos, clearQueue);
/*     */             onRelease(pos);
/*     */             if (this.sleeping) {
/*     */               this.sleeping = false;
/*     */               pollTask();
/*     */             } 
/*     */             whenReleased.run();
/*     */           }));
/*     */   }
/*     */   
/*     */   public void submit(Runnable task, long pos, IntSupplier level) {
/*  62 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(2, () -> {
/*     */             int ticketLevel = level.getAsInt();
/*     */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*     */               LOGGER.debug("SUB {} {} {} {}", new Object[] { new ChunkPos(pos), ticketLevel, this.executor, this.queue });
/*     */             }
/*     */             this.queue.submit(task, pos, ticketLevel);
/*     */             if (this.sleeping) {
/*     */               this.sleeping = false;
/*     */               pollTask();
/*     */             } 
/*     */           }));
/*     */   }
/*     */   
/*     */   protected void pollTask() {
/*  76 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(3, () -> {
/*     */             ChunkTaskPriorityQueue.TasksForChunk tasksForChunk = popTasks();
/*     */             if (tasksForChunk == null) {
/*     */               this.sleeping = true;
/*     */             } else {
/*     */               scheduleForExecution(tasksForChunk);
/*     */             } 
/*     */           }));
/*     */   }
/*     */   
/*     */   protected void scheduleForExecution(ChunkTaskPriorityQueue.TasksForChunk tasksForChunk) {
/*  87 */     CompletableFuture.allOf((CompletableFuture<?>[])tasksForChunk.tasks().stream().map(message -> this.executor.scheduleWithResult(()))
/*     */ 
/*     */         
/*  90 */         .toArray(x$0 -> new CompletableFuture[x$0])).thenAccept(r -> pollTask());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRelease(long key) {}
/*     */   
/*     */   protected ChunkTaskPriorityQueue.TasksForChunk popTasks() {
/*  97 */     return this.queue.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 102 */     this.executor.close();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ChunkTaskDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */