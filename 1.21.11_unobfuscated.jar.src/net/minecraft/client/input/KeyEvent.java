/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ 
/*    */ public final class KeyEvent extends Record implements InputWithModifiers {
/*    */   @com.mojang.blaze3d.platform.InputConstants.Value
/*    */   private final int key;
/*    */   private final int scancode;
/*    */   @InputWithModifiers.Modifiers
/*    */   private final int modifiers;
/*    */   
/* 11 */   public KeyEvent(@com.mojang.blaze3d.platform.InputConstants.Value int key, int scancode, @InputWithModifiers.Modifiers int modifiers) { this.key = key; this.scancode = scancode; this.modifiers = modifiers; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/KeyEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/client/input/KeyEvent; } @com.mojang.blaze3d.platform.InputConstants.Value public int key() { return this.key; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/KeyEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/KeyEvent; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/KeyEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/KeyEvent;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public int scancode() { return this.scancode; } @InputWithModifiers.Modifiers public int modifiers() { return this.modifiers; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int input() {
/* 19 */     return this.key;
/*    */   }
/*    */   
/*    */   @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
/*    */   @java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE_USE})
/*    */   public static @interface Action {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/input/KeyEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */