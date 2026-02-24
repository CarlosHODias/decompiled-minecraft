/*    */ package net.minecraft.client.gui.font;public final class EmptyArea extends Record implements ActiveArea { private final float x; private final float y; private final float advance; private final float ascent; private final float height;
/*    */   private final net.minecraft.network.chat.Style style;
/*    */   public static final float DEFAULT_HEIGHT = 9.0F;
/*    */   public static final float DEFAULT_ASCENT = 7.0F;
/*    */   
/*  6 */   public EmptyArea(float x, float y, float advance, float ascent, float height, net.minecraft.network.chat.Style style) { this.x = x; this.y = y; this.advance = advance; this.ascent = ascent; this.height = height; this.style = style; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/EmptyArea;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/client/gui/font/EmptyArea; } public float x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/EmptyArea;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/EmptyArea; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/EmptyArea;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/EmptyArea;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public float y() { return this.y; } public float advance() { return this.advance; } public float ascent() { return this.ascent; } public float height() { return this.height; } public net.minecraft.network.chat.Style style() { return this.style; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float activeLeft() {
/* 19 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public float activeTop() {
/* 24 */     return this.y + 7.0F - this.ascent;
/*    */   }
/*    */ 
/*    */   
/*    */   public float activeRight() {
/* 29 */     return this.x + this.advance;
/*    */   }
/*    */ 
/*    */   
/*    */   public float activeBottom() {
/* 34 */     return activeTop() + this.height;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/EmptyArea.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */