/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParallelMapTransform
/*     */ {
/*     */   private static final int DEFAULT_TASKS_PER_THREAD = 16;
/*     */   
/*     */   public static <K, U, V> CompletableFuture<Map<K, V>> schedule(Map<K, U> input, BiFunction<K, U, V> operation, int maxTaskCount, Executor executor) {
/*  23 */     int inputSize = input.size();
/*     */     
/*  25 */     if (inputSize == 0) {
/*  26 */       return CompletableFuture.completedFuture(Map.of());
/*     */     }
/*     */     
/*  29 */     if (inputSize == 1) {
/*  30 */       Map.Entry<K, U> element = input.entrySet().iterator().next();
/*  31 */       K key = element.getKey();
/*  32 */       U value = element.getValue();
/*  33 */       return CompletableFuture.supplyAsync(() -> { V result = operation.apply(key, value); return (result != null) ? Map.<Object, V>of(key, result) : Map.of(); }, executor);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     SplitterBase<K, U, V> splitter = (inputSize <= maxTaskCount) ? 
/*  40 */       new SingleTaskSplitter<>(operation, inputSize) : 
/*  41 */       new BatchedTaskSplitter<>(operation, inputSize, maxTaskCount);
/*     */     
/*  43 */     return splitter.scheduleTasks(input, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, U, V> CompletableFuture<Map<K, V>> schedule(Map<K, U> input, BiFunction<K, U, V> operation, Executor executor) {
/*  50 */     int maxTaskCount = Util.maxAllowedExecutorThreads() * 16;
/*  51 */     return schedule(input, operation, maxTaskCount, executor);
/*     */   }
/*     */   private static final class Container<K, U, V> extends Record { private final BiFunction<K, U, V> operation; private final Object[] keys; private final Object[] values;
/*  54 */     private Container(BiFunction<K, U, V> operation, Object[] keys, Object[] values) { this.operation = operation; this.keys = keys; this.values = values; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  54 */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; } public BiFunction<K, U, V> operation() { return this.operation; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #54	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  54 */       //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>; } public Object[] keys() { return this.keys; } public Object[] values() { return this.values; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Container(BiFunction<K, U, V> operation, int size) {
/*  62 */       this(operation, new Object[size], new Object[size]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void put(int index, K key, U input) {
/*  70 */       this.keys[index] = key;
/*  71 */       this.values[index] = input;
/*     */     }
/*     */ 
/*     */     
/*     */     private K key(int index) {
/*  76 */       return (K)this.keys[index];
/*     */     }
/*     */ 
/*     */     
/*     */     private V output(int index) {
/*  81 */       return (V)this.values[index];
/*     */     }
/*     */ 
/*     */     
/*     */     private U input(int index) {
/*  86 */       return (U)this.values[index];
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyOperation(int index) {
/*  91 */       this.values[index] = this.operation.apply(key(index), input(index));
/*     */     }
/*     */     
/*     */     public void copyOut(int index, Map<K, V> output) {
/*  95 */       V value = output(index);
/*  96 */       if (value != null) {
/*  97 */         K key = key(index);
/*  98 */         output.put(key, value);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int size() {
/* 103 */       return this.keys.length;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class SplitterBase<K, U, V>
/*     */   {
/*     */     private int lastScheduledIndex;
/*     */     private int currentIndex;
/*     */     private final CompletableFuture<?>[] tasks;
/*     */     private int batchIndex;
/*     */     private final ParallelMapTransform.Container<K, U, V> container;
/*     */     
/*     */     private SplitterBase(BiFunction<K, U, V> operation, int size, int taskCount) {
/* 117 */       this.container = new ParallelMapTransform.Container<>(operation, size);
/* 118 */       this.tasks = (CompletableFuture<?>[])new CompletableFuture[taskCount];
/*     */     }
/*     */     
/*     */     private int pendingBatchSize() {
/* 122 */       return this.currentIndex - this.lastScheduledIndex;
/*     */     }
/*     */     
/*     */     public CompletableFuture<Map<K, V>> scheduleTasks(Map<K, U> input, Executor executor) {
/* 126 */       input.forEach((key, inputValue) -> {
/*     */             this.container.put(this.currentIndex++, (K)executor, (U)inputValue);
/*     */             
/*     */             if (pendingBatchSize() == batchSize(this.batchIndex)) {
/*     */               this.tasks[this.batchIndex++] = scheduleBatch(this.container, this.lastScheduledIndex, this.currentIndex, executor);
/*     */               this.lastScheduledIndex = this.currentIndex;
/*     */             } 
/*     */           });
/* 134 */       assert this.currentIndex == this.container.size();
/* 135 */       assert this.lastScheduledIndex == this.currentIndex;
/* 136 */       assert this.batchIndex == this.tasks.length;
/*     */       
/* 138 */       return scheduleFinalOperation(CompletableFuture.allOf(this.tasks), this.container);
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract int batchSize(int param1Int);
/*     */     
/*     */     protected abstract CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> param1Container, int param1Int1, int param1Int2, Executor param1Executor);
/*     */     
/*     */     protected abstract CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> param1CompletableFuture, ParallelMapTransform.Container<K, U, V> param1Container);
/*     */   }
/*     */   
/*     */   private static class SingleTaskSplitter<K, U, V>
/*     */     extends SplitterBase<K, U, V>
/*     */   {
/*     */     private SingleTaskSplitter(BiFunction<K, U, V> operation, int size) {
/* 153 */       super(operation, size, size);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int batchSize(int index) {
/* 158 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 163 */       assert startIndex + 1 == endIndex;
/* 164 */       return CompletableFuture.runAsync(() -> container.applyOperation(startIndex), executor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 169 */       return allTasksDone.thenApply(ignored -> {
/*     */             Map<K, V> result = new HashMap<>(container.size());
/*     */             for (int i = 0; i < container.size(); i++) {
/*     */               container.copyOut(i, result);
/*     */             }
/*     */             return result;
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BatchedTaskSplitter<K, U, V>
/*     */     extends SplitterBase<K, U, V>
/*     */   {
/*     */     private final Map<K, V> result;
/*     */     
/*     */     private final int batchSize;
/*     */     
/*     */     private final int firstUndersizedBatchIndex;
/*     */     
/*     */     private BatchedTaskSplitter(BiFunction<K, U, V> operation, int size, int maxTasks) {
/* 190 */       super(operation, size, maxTasks);
/* 191 */       this.result = new HashMap<>(size);
/* 192 */       this.batchSize = Mth.positiveCeilDiv(size, maxTasks);
/*     */       
/* 194 */       int fullCapacity = this.batchSize * maxTasks;
/* 195 */       int leftoverCapacity = fullCapacity - size;
/*     */ 
/*     */       
/* 198 */       this.firstUndersizedBatchIndex = maxTasks - leftoverCapacity;
/* 199 */       assert this.firstUndersizedBatchIndex > 0 && this.firstUndersizedBatchIndex <= maxTasks;
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> container, int startIndex, int endIndex, Executor executor) {
/* 204 */       int batchSize = endIndex - startIndex;
/*     */       
/* 206 */       assert batchSize == this.batchSize || batchSize == this.batchSize - 1;
/* 207 */       return CompletableFuture.runAsync(createTask(this.result, startIndex, endIndex, container), executor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int batchSize(int index) {
/* 212 */       return (index < this.firstUndersizedBatchIndex) ? this.batchSize : (this.batchSize - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     private static <K, U, V> Runnable createTask(Map<K, V> result, int startIndex, int endIndex, ParallelMapTransform.Container<K, U, V> container) {
/* 217 */       return () -> {
/*     */           for (int i = startIndex; i < endIndex; i++) {
/*     */             container.applyOperation(i);
/*     */           }
/*     */           synchronized (result) {
/*     */             for (int j = startIndex; j < endIndex; j++) {
/*     */               container.copyOut(j, result);
/*     */             }
/*     */           } 
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> allTasksDone, ParallelMapTransform.Container<K, U, V> container) {
/* 234 */       Map<K, V> result = this.result;
/* 235 */       return allTasksDone.thenApply(ignored -> result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/thread/ParallelMapTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */