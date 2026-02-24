/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ public class GlobalTestReporter {
/*  4 */   private static TestReporter DELEGATE = new LogTestReporter();
/*    */   
/*    */   public static void replaceWith(TestReporter testReporter) {
/*  7 */     DELEGATE = testReporter;
/*    */   }
/*    */   
/*    */   public static void onTestFailed(GameTestInfo testInfo) {
/* 11 */     DELEGATE.onTestFailed(testInfo);
/*    */   }
/*    */   
/*    */   public static void onTestSuccess(GameTestInfo testInfo) {
/* 15 */     DELEGATE.onTestSuccess(testInfo);
/*    */   }
/*    */   
/*    */   public static void finish() {
/* 19 */     DELEGATE.finish();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GlobalTestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */