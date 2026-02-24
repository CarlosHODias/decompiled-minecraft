/*    */ package net.minecraft.client.input;public final class MouseButtonEvent extends Record implements InputWithModifiers { private final double x; private final double y; private final MouseButtonInfo buttonInfo;
/*    */   
/*  3 */   public MouseButtonEvent(double x, double y, MouseButtonInfo buttonInfo) { this.x = x; this.y = y; this.buttonInfo = buttonInfo; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/MouseButtonEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  3 */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonEvent; } public double x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/MouseButtonEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonEvent; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/MouseButtonEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/MouseButtonEvent;
/*  3 */     //   0	8	1	o	Ljava/lang/Object; } public double y() { return this.y; } public MouseButtonInfo buttonInfo() { return this.buttonInfo; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int input() {
/* 10 */     return button();
/*    */   }
/*    */   
/*    */   @MouseButtonInfo.MouseButton
/*    */   public int button() {
/* 15 */     return buttonInfo().button();
/*    */   }
/*    */ 
/*    */   
/*    */   @InputWithModifiers.Modifiers
/*    */   public int modifiers() {
/* 21 */     return buttonInfo().modifiers();
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/input/MouseButtonEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */