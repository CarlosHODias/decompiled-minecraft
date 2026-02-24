/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ 
/*    */ class GameTestEvent
/*    */ {
/*    */   public final Long expectedDelay;
/*    */   public final Runnable assertion;
/*    */   
/*    */   private GameTestEvent(Long expectedDelay, Runnable assertion) {
/* 10 */     this.expectedDelay = expectedDelay;
/* 11 */     this.assertion = assertion;
/*    */   }
/*    */   
/*    */   static GameTestEvent create(Runnable runnable) {
/* 15 */     return new GameTestEvent(null, runnable);
/*    */   }
/*    */   
/*    */   static GameTestEvent create(long expectedTick, Runnable runnable) {
/* 19 */     return new GameTestEvent(expectedTick, runnable);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */