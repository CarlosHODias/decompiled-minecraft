/*    */ package net.minecraft.client.gui.font;
/*    */ public final class SingleSpriteSource extends Record implements net.minecraft.client.gui.GlyphSource { private final net.minecraft.client.gui.font.glyphs.BakedGlyph glyph;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/SingleSpriteSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/SingleSpriteSource;
/*    */   }
/*    */   
/*  7 */   public SingleSpriteSource(net.minecraft.client.gui.font.glyphs.BakedGlyph glyph) { this.glyph = glyph; } public net.minecraft.client.gui.font.glyphs.BakedGlyph glyph() { return this.glyph; }
/*    */    public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/SingleSpriteSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/SingleSpriteSource;
/*    */   } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/SingleSpriteSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/SingleSpriteSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } public net.minecraft.client.gui.font.glyphs.BakedGlyph getGlyph(int codepoint) {
/* 13 */     return this.glyph;
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.client.gui.font.glyphs.BakedGlyph getRandomGlyph(net.minecraft.util.RandomSource random, int width) {
/* 18 */     return this.glyph;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/SingleSpriteSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */