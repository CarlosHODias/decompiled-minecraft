/*    */ package net.minecraft.gametest.framework;public final class RetryOptions extends Record { private final int numberOfTries; private final boolean haltOnFailure;
/*    */   
/*  3 */   public RetryOptions(int numberOfTries, boolean haltOnFailure) { this.numberOfTries = numberOfTries; this.haltOnFailure = haltOnFailure; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/RetryOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  3 */     //   0	7	0	this	Lnet/minecraft/gametest/framework/RetryOptions; } public int numberOfTries() { return this.numberOfTries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/RetryOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gametest/framework/RetryOptions; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/RetryOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gametest/framework/RetryOptions;
/*  3 */     //   0	8	1	o	Ljava/lang/Object; } public boolean haltOnFailure() { return this.haltOnFailure; }
/*  4 */    private static final RetryOptions NO_RETRIES = new RetryOptions(1, true);
/*    */   
/*    */   public static RetryOptions noRetries() {
/*  7 */     return NO_RETRIES;
/*    */   }
/*    */   
/*    */   public boolean unlimitedTries() {
/* 11 */     return (this.numberOfTries < 1);
/*    */   }
/*    */   
/*    */   public boolean hasTriesLeft(int attempts, int successes) {
/* 15 */     boolean hasFailures = (attempts != successes);
/* 16 */     boolean hasMoreAttempts = (unlimitedTries() || attempts < this.numberOfTries);
/*    */     
/* 18 */     return (hasMoreAttempts && (!hasFailures || !this.haltOnFailure));
/*    */   }
/*    */   
/*    */   public boolean hasRetries() {
/* 22 */     return (this.numberOfTries != 1);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/RetryOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */