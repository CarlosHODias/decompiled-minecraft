/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ 
/*    */ class ExhaustedAttemptsException
/*    */   extends Throwable
/*    */ {
/*    */   public ExhaustedAttemptsException(int attempts, int successes, GameTestInfo testInfo) {
/*  8 */     super("Not enough successes: " + successes + " out of " + attempts + " attempts. Required successes: " + 
/*    */         
/* 10 */         testInfo.requiredSuccesses() + ". max attempts: " + testInfo.maxAttempts() + ".", testInfo.getError());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/ExhaustedAttemptsException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */