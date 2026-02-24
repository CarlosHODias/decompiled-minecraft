/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ public abstract class ReentrantBlockableEventLoop<R extends Runnable> extends BlockableEventLoop<R> {
/*    */   private int reentrantCount;
/*    */   
/*    */   public ReentrantBlockableEventLoop(String name) {
/*  7 */     super(name);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean scheduleExecutables() {
/* 12 */     return (runningTask() || super.scheduleExecutables());
/*    */   }
/*    */   
/*    */   protected boolean runningTask() {
/* 16 */     return (this.reentrantCount != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doRunTask(R task) {
/* 21 */     this.reentrantCount++;
/*    */     try {
/* 23 */       super.doRunTask(task);
/*    */     } finally {
/* 25 */       this.reentrantCount--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/thread/ReentrantBlockableEventLoop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */