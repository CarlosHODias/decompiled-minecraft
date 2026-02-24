/*     */ package com.mojang.realmsclient.gui.task;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.TimeSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFetcher
/*     */ {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Executor executor;
/*     */   private final TimeUnit resolution;
/*     */   private final TimeSource timeSource;
/*     */   
/*     */   public DataFetcher(Executor executor, TimeUnit resolution, TimeSource timeSource) {
/*  30 */     this.executor = executor;
/*  31 */     this.resolution = resolution;
/*  32 */     this.timeSource = timeSource;
/*     */   }
/*     */   
/*     */   public <T> Task<T> createTask(String id, Callable<T> updater, Duration period, RepeatedDelayStrategy repeatStrategy) {
/*  36 */     long periodInUnit = this.resolution.convert(period);
/*  37 */     if (periodInUnit == 0L) {
/*  38 */       throw new IllegalArgumentException("Period of " + String.valueOf(period) + " too short for selected resolution of " + String.valueOf(this.resolution));
/*     */     }
/*  40 */     return new Task<>(id, updater, periodInUnit, repeatStrategy);
/*     */   }
/*     */   
/*     */   public Subscription createSubscription() {
/*  44 */     return new Subscription();
/*     */   }
/*     */   private static final class ComputationResult<T> extends Record { private final Either<T, Exception> value; private final long time;
/*  47 */     private ComputationResult(Either<T, Exception> value, long time) { this.value = value; this.time = time; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  47 */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>; } public Either<T, Exception> value() { return this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  47 */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>; } public long time() { return this.time; }
/*     */      } private static final class SuccessfulComputationResult<T> extends Record { private final T value; private final long time;
/*  49 */     private SuccessfulComputationResult(T value, long time) { this.value = value; this.time = time; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #49	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  49 */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>; } public T value() { return this.value; } public long time() { return this.time; }
/*     */      }
/*     */   
/*     */   public class Task<T> {
/*     */     private final String id;
/*     */     private final Callable<T> updater;
/*     */     private final long period;
/*     */     private final RepeatedDelayStrategy repeatStrategy;
/*     */     private CompletableFuture<DataFetcher.ComputationResult<T>> pendingTask;
/*     */     private DataFetcher.SuccessfulComputationResult<T> lastResult;
/*  59 */     private long nextUpdate = -1L;
/*     */     
/*     */     private Task(String id, Callable<T> updater, long period, RepeatedDelayStrategy repeatStrategy) {
/*  62 */       this.id = id;
/*  63 */       this.updater = updater;
/*  64 */       this.period = period;
/*  65 */       this.repeatStrategy = repeatStrategy;
/*     */     }
/*     */     
/*     */     private void updateIfNeeded(long currentTime) {
/*  69 */       if (this.pendingTask != null) {
/*  70 */         DataFetcher.ComputationResult<T> result = this.pendingTask.getNow(null);
/*  71 */         if (result == null) {
/*     */           return;
/*     */         }
/*     */         
/*  75 */         this.pendingTask = null;
/*  76 */         long completionTime = result.time;
/*  77 */         result.value()
/*  78 */           .ifLeft(value -> {
/*     */               this.lastResult = new DataFetcher.SuccessfulComputationResult<>((T)completionTime, completionTime);
/*     */               
/*     */               this.nextUpdate = completionTime + this.period * this.repeatStrategy.delayCyclesAfterSuccess();
/*  82 */             }).ifRight(e -> {
/*     */               long cycles = this.repeatStrategy.delayCyclesAfterFailure();
/*     */               
/*     */               DataFetcher.LOGGER.warn("Failed to process task {}, will repeat after {} cycles", new Object[] { this.id, cycles, completionTime });
/*     */               this.nextUpdate = completionTime + this.period * cycles;
/*     */             });
/*     */       } 
/*  89 */       if (this.nextUpdate <= currentTime) {
/*  90 */         this.pendingTask = CompletableFuture.supplyAsync(() -> {
/*     */               try {
/*     */                 T result = this.updater.call();
/*     */                 long completionTime = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
/*     */                 return new DataFetcher.ComputationResult(Either.left(result), completionTime);
/*  95 */               } catch (Exception e) {
/*     */                 long completionTime = DataFetcher.this.timeSource.get(DataFetcher.this.resolution);
/*     */                 return new DataFetcher.ComputationResult(Either.right(e), completionTime);
/*     */               } 
/*     */             }, DataFetcher.this.executor);
/*     */       }
/*     */     }
/*     */     
/*     */     public void reset() {
/* 104 */       this.pendingTask = null;
/* 105 */       this.lastResult = null;
/* 106 */       this.nextUpdate = -1L;
/*     */     }
/*     */   }
/*     */   
/*     */   private class SubscribedTask<T> {
/*     */     private final DataFetcher.Task<T> task;
/*     */     private final Consumer<T> output;
/* 113 */     private long lastCheckTime = -1L;
/*     */     
/*     */     private SubscribedTask(DataFetcher this$0, DataFetcher.Task<T> task, Consumer<T> output) {
/* 116 */       this.task = task;
/* 117 */       this.output = output;
/*     */     }
/*     */     
/*     */     private void update(long currentTime) {
/* 121 */       this.task.updateIfNeeded(currentTime);
/* 122 */       runCallbackIfNeeded();
/*     */     }
/*     */     
/*     */     private void runCallbackIfNeeded() {
/* 126 */       DataFetcher.SuccessfulComputationResult<T> lastResult = this.task.lastResult;
/* 127 */       if (lastResult != null && this.lastCheckTime < lastResult.time) {
/* 128 */         this.output.accept(lastResult.value);
/* 129 */         this.lastCheckTime = lastResult.time;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void runCallback() {
/* 134 */       DataFetcher.SuccessfulComputationResult<T> lastResult = this.task.lastResult;
/* 135 */       if (lastResult != null) {
/* 136 */         this.output.accept(lastResult.value);
/* 137 */         this.lastCheckTime = lastResult.time;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void reset() {
/* 142 */       this.task.reset();
/* 143 */       this.lastCheckTime = -1L;
/*     */     } }
/*     */   public class Subscription { private final List<DataFetcher.SubscribedTask<?>> subscriptions;
/*     */     
/*     */     public Subscription() {
/* 148 */       this.subscriptions = new ArrayList<>();
/*     */     }
/*     */     public <T> void subscribe(DataFetcher.Task<T> task, Consumer<T> output) {
/* 151 */       DataFetcher.SubscribedTask<T> subscription = new DataFetcher.SubscribedTask<>(DataFetcher.this, task, output);
/* 152 */       this.subscriptions.add(subscription);
/* 153 */       subscription.runCallbackIfNeeded();
/*     */     }
/*     */     
/*     */     public void forceUpdate() {
/* 157 */       for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions) {
/* 158 */         subscription.runCallback();
/*     */       }
/*     */     }
/*     */     
/*     */     public void tick() {
/* 163 */       for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions) {
/* 164 */         subscription.update(DataFetcher.this.timeSource.get(DataFetcher.this.resolution));
/*     */       }
/*     */     }
/*     */     
/*     */     public void reset() {
/* 169 */       for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 170 */         subscription.reset(); 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/task/DataFetcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */