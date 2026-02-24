/*    */ package net.minecraft.gizmos;
/*    */ 
/*    */ 
/*    */ public final class TextGizmo extends Record implements Gizmo {
/*    */   private final net.minecraft.world.phys.Vec3 pos;
/*    */   private final String text;
/*    */   private final Style style;
/*    */   
/*  9 */   public TextGizmo(net.minecraft.world.phys.Vec3 pos, String text, Style style) { this.pos = pos; this.text = text; this.style = style; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo; } public net.minecraft.world.phys.Vec3 pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public String text() { return this.text; } public Style style() { return this.style; }
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/*    */     Style newStyle;
/* 13 */     if (alphaMultiplier < 1.0F) {
/* 14 */       newStyle = new Style(net.minecraft.util.ARGB.multiplyAlpha(this.style.color, alphaMultiplier), this.style.scale, this.style.adjustLeft);
/*    */     } else {
/* 16 */       newStyle = this.style;
/*    */     } 
/* 18 */     primitives.addText(this.pos, this.text, newStyle);
/*    */   }
/*    */   public static final class Style extends Record { private final int color; private final float scale; private final java.util.OptionalDouble adjustLeft; public static final float DEFAULT_SCALE = 0.32F;
/* 21 */     public Style(int color, float scale, java.util.OptionalDouble adjustLeft) { this.color = color; this.scale = scale; this.adjustLeft = adjustLeft; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/TextGizmo$Style;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/TextGizmo$Style;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/gizmos/TextGizmo$Style; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/TextGizmo$Style;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/gizmos/TextGizmo$Style;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public int color() { return this.color; } public float scale() { return this.scale; } public java.util.OptionalDouble adjustLeft() { return this.adjustLeft; }
/*    */ 
/*    */     
/*    */     public static Style whiteAndCentered() {
/* 25 */       return new Style(-1, 0.32F, java.util.OptionalDouble.empty());
/*    */     }
/*    */     
/*    */     public static Style forColorAndCentered(int argb) {
/* 29 */       return new Style(argb, 0.32F, java.util.OptionalDouble.empty());
/*    */     }
/*    */     
/*    */     public static Style forColor(int argb) {
/* 33 */       return new Style(argb, 0.32F, java.util.OptionalDouble.of(0.0D));
/*    */     }
/*    */     
/*    */     public Style withScale(float scale) {
/* 37 */       return new Style(this.color, scale, this.adjustLeft);
/*    */     }
/*    */     
/*    */     public Style withLeftAlignment(float adjustLeft) {
/* 41 */       return new Style(this.color, this.scale, java.util.OptionalDouble.of(adjustLeft));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gizmos/TextGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */