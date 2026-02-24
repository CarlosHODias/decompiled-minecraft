/*    */ package net.minecraft.client.input;
/*    */ public final class MouseButtonInfo extends Record implements InputWithModifiers { @MouseButton
/*    */   private final int button; @InputWithModifiers.Modifiers
/*    */   private final int modifiers;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/MouseButtonInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonInfo;
/*    */   }
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/MouseButtonInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonInfo;
/*    */   }
/* 11 */   public MouseButtonInfo(@MouseButton int button, @InputWithModifiers.Modifiers int modifiers) { this.button = button; this.modifiers = modifiers; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/MouseButtonInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/MouseButtonInfo;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } @MouseButton public int button() { return this.button; } @InputWithModifiers.Modifiers public int modifiers() { return this.modifiers; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @MouseButton
/*    */   public int input() {
/* 18 */     return this.button;
/*    */   }
/*    */   
/*    */   @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
/*    */   @java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE_USE})
/*    */   public static @interface MouseButton {}
/*    */   
/*    */   @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
/*    */   @java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE_USE})
/*    */   public static @interface Action {} }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/input/MouseButtonInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */