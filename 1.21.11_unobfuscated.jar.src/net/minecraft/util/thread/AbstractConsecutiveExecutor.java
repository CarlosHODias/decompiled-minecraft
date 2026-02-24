/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.ProfilerMeasured;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractConsecutiveExecutor<T extends Runnable>
/*     */   implements Runnable, TaskScheduler<T>, ProfilerMeasured {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  19 */   private final AtomicReference<Status> status = new AtomicReference<>(Status.SLEEPING);
/*     */   private final StrictQueue<T> queue;
/*     */   private final Executor executor;
/*     */   private final String name;
/*     */   
/*     */   public AbstractConsecutiveExecutor(StrictQueue<T> queue, Executor executor, String name) {
/*  25 */     this.executor = executor;
/*  26 */     this.queue = queue;
/*  27 */     this.name = name;
/*  28 */     MetricsRegistry.INSTANCE.add(this);
/*     */   }
/*     */   
/*     */   private boolean canBeScheduled() {
/*  32 */     return (!isClosed() && !this.queue.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  37 */     this.status.set(Status.CLOSED);
/*     */   }
/*     */   
/*     */   private boolean pollTask() {
/*  41 */     if (!isRunning()) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     Runnable runnable = this.queue.pop();
/*  46 */     if (runnable == null) {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     Util.runNamed(runnable, this.name);
/*     */     
/*  52 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  62 */       pollTask();
/*     */     } finally {
/*  64 */       setSleeping();
/*  65 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void runAll() {
/*     */     try {
/*  76 */       while (pollTask());
/*     */     } finally {
/*     */       
/*  79 */       setSleeping();
/*  80 */       registerForExecution();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(T task) {
/*  86 */     this.queue.push(task);
/*  87 */     registerForExecution();
/*     */   }
/*     */   
/*     */   private void registerForExecution() {
/*  91 */     if (canBeScheduled() && 
/*  92 */       setRunning()) {
/*     */       try {
/*  94 */         this.executor.execute(this);
/*  95 */       } catch (RejectedExecutionException e) {
/*     */         
/*     */         try {
/*  98 */           this.executor.execute(this);
/*  99 */         } catch (RejectedExecutionException e2) {
/* 100 */           LOGGER.error("Could not schedule ConsecutiveExecutor", e2);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 108 */     return this.queue.size();
/*     */   }
/*     */   
/*     */   public boolean hasWork() {
/* 112 */     return (isRunning() && !this.queue.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return this.name + " " + this.name + " " + String.valueOf(this.status.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/* 122 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MetricSampler> profiledMetrics() {
/* 127 */     return (List<MetricSampler>)ImmutableList.of(
/* 128 */         MetricSampler.create(this.name + "-queue-size", MetricCategory.CONSECUTIVE_EXECUTORS, this::size));
/*     */   }
/*     */   
/*     */   private enum Status
/*     */   {
/* 133 */     SLEEPING,
/* 134 */     RUNNING,
/* 135 */     CLOSED;
/*     */   }
/*     */   
/*     */   private boolean setRunning() {
/* 139 */     return this.status.compareAndSet(Status.SLEEPING, Status.RUNNING);
/*     */   }
/*     */   
/*     */   private void setSleeping() {
/* 143 */     this.status.compareAndSet(Status.RUNNING, Status.SLEEPING);
/*     */   }
/*     */   
/*     */   private boolean isRunning() {
/* 147 */     return (this.status.get() == Status.RUNNING);
/*     */   }
/*     */   
/*     */   private boolean isClosed() {
/* 151 */     return (this.status.get() == Status.CLOSED);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/thread/AbstractConsecutiveExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */