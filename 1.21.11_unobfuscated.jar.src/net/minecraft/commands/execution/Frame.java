/*    */ package net.minecraft.commands.execution;public final class Frame extends Record { private final int depth;
/*    */   private final net.minecraft.commands.CommandResultCallback returnValueConsumer;
/*    */   private final FrameControl frameControl;
/*    */   
/*  5 */   public Frame(int depth, net.minecraft.commands.CommandResultCallback returnValueConsumer, FrameControl frameControl) { this.depth = depth; this.returnValueConsumer = returnValueConsumer; this.frameControl = frameControl; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/execution/Frame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/commands/execution/Frame; } public int depth() { return this.depth; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/execution/Frame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/execution/Frame; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/execution/Frame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/execution/Frame;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.commands.CommandResultCallback returnValueConsumer() { return this.returnValueConsumer; } public FrameControl frameControl() { return this.frameControl; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void returnSuccess(int value) {
/* 11 */     this.returnValueConsumer.onSuccess(value);
/*    */   }
/*    */   
/*    */   public void returnFailure() {
/* 15 */     this.returnValueConsumer.onFailure();
/*    */   }
/*    */   
/*    */   public void discard() {
/* 19 */     this.frameControl.discard();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface FrameControl {
/*    */     void discard();
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/Frame.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */