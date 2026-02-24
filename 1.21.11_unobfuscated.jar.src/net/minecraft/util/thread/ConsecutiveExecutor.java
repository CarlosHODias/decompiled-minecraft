/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ public class ConsecutiveExecutor
/*    */   extends AbstractConsecutiveExecutor<Runnable> {
/*    */   public ConsecutiveExecutor(Executor dispatcher, String name) {
/*  9 */     super(new StrictQueue.QueueStrictQueue(new ConcurrentLinkedQueue<>()), dispatcher, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Runnable wrapRunnable(Runnable runnable) {
/* 14 */     return runnable;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/thread/ConsecutiveExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */