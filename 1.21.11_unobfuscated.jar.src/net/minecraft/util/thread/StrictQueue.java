/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import java.util.Locale;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ public interface StrictQueue<T extends Runnable>
/*    */ {
/*    */   Runnable pop();
/*    */   
/*    */   boolean push(T paramT);
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   int size();
/*    */   
/*    */   public static final class QueueStrictQueue
/*    */     implements StrictQueue<Runnable> {
/*    */     private final Queue<Runnable> queue;
/*    */     
/*    */     public QueueStrictQueue(Queue<Runnable> queue) {
/* 23 */       this.queue = queue;
/*    */     }
/*    */ 
/*    */     
/*    */     public Runnable pop() {
/* 28 */       return this.queue.poll();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean push(Runnable t) {
/* 33 */       return this.queue.add(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isEmpty() {
/* 38 */       return this.queue.isEmpty();
/*    */     }
/*    */ 
/*    */     
/*    */     public int size() {
/* 43 */       return this.queue.size();
/*    */     } }
/*    */   public static final class RunnableWithPriority extends Record implements Runnable { private final int priority; private final Runnable task;
/*    */     
/* 47 */     public RunnableWithPriority(int priority, Runnable task) { this.priority = priority; this.task = task; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 47 */       //   0	7	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority; } public int priority() { return this.priority; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;
/* 47 */       //   0	8	1	o	Ljava/lang/Object; } public Runnable task() { return this.task; }
/*    */     
/*    */     public void run() {
/* 50 */       this.task.run();
/*    */     } }
/*    */ 
/*    */   
/*    */   public static final class FixedPriorityQueue implements StrictQueue<RunnableWithPriority> {
/*    */     private final Queue<Runnable>[] queues;
/* 56 */     private final AtomicInteger size = new AtomicInteger();
/*    */ 
/*    */     
/*    */     public FixedPriorityQueue(int size) {
/* 60 */       this.queues = (Queue<Runnable>[])new Queue[size];
/* 61 */       for (int i = 0; i < size; i++) {
/* 62 */         this.queues[i] = Queues.newConcurrentLinkedQueue();
/*    */       }
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Runnable pop() {
/* 69 */       for (Queue<Runnable> queue : this.queues) {
/* 70 */         Runnable task = queue.poll();
/* 71 */         if (task != null) {
/* 72 */           this.size.decrementAndGet();
/* 73 */           return task;
/*    */         } 
/*    */       } 
/* 76 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean push(StrictQueue.RunnableWithPriority task) {
/* 81 */       int priority = task.priority;
/*    */       
/* 83 */       if (priority >= this.queues.length || priority < 0) {
/* 84 */         throw new IndexOutOfBoundsException(String.format(Locale.ROOT, "Priority %d not supported. Expected range [0-%d]", new Object[] { priority, this.queues.length - 1 }));
/*    */       }
/*    */       
/* 87 */       this.queues[priority].add(task);
/* 88 */       this.size.incrementAndGet();
/* 89 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isEmpty() {
/* 94 */       return (this.size.get() == 0);
/*    */     }
/*    */ 
/*    */     
/*    */     public int size() {
/* 99 */       return this.size.get();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/thread/StrictQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */