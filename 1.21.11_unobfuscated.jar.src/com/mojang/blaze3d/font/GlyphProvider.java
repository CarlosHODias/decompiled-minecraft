/*    */ package com.mojang.blaze3d.font;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import net.minecraft.client.gui.font.FontOption;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GlyphProvider
/*    */   extends AutoCloseable
/*    */ {
/*    */   public static final float BASELINE = 7.0F;
/*    */   
/*    */   default void close() {}
/*    */   
/*    */   default UnbakedGlyph getGlyph(int codepoint) {
/* 22 */     return null;
/*    */   }
/*    */   IntSet getSupportedGlyphs();
/*    */   public static final class Conditional extends Record implements AutoCloseable { private final GlyphProvider provider; private final FontOption.Filter filter;
/*    */     
/* 27 */     public Conditional(GlyphProvider provider, FontOption.Filter filter) { this.provider = provider; this.filter = filter; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 27 */       //   0	7	0	this	Lcom/mojang/blaze3d/font/GlyphProvider$Conditional; } public GlyphProvider provider() { return this.provider; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/font/GlyphProvider$Conditional; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;
/* 27 */       //   0	8	1	o	Ljava/lang/Object; } public FontOption.Filter filter() { return this.filter; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void close() {
/* 33 */       this.provider.close();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/font/GlyphProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */