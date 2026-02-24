/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.HexFormat;
/*    */ 
/*    */ public final class ColorRGBA extends Record {
/*    */   private final int rgba;
/*    */   
/*  8 */   public ColorRGBA(int rgba) { this.rgba = rgba; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ColorRGBA;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/util/ColorRGBA; } public int rgba() { return this.rgba; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ColorRGBA;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/ColorRGBA;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.Codec<ColorRGBA> CODEC = ExtraCodecs.STRING_ARGB_COLOR.xmap(ColorRGBA::new, ColorRGBA::rgba);
/*    */ 
/*    */   
/*    */   public String toString() {
/* 13 */     return HexFormat.of().toHexDigits(this.rgba, 8);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/ColorRGBA.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */