/*    */ package net.minecraft.server;
/*    */ 
/*    */ public class TickTask implements Runnable {
/*    */   private final int tick;
/*    */   private final Runnable runnable;
/*    */   
/*    */   public TickTask(int tick, Runnable runnable) {
/*  8 */     this.tick = tick;
/*  9 */     this.runnable = runnable;
/*    */   }
/*    */   
/*    */   public int getTick() {
/* 13 */     return this.tick;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 18 */     this.runnable.run();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/TickTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */