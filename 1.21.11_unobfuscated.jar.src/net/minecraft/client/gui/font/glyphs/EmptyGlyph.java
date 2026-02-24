/*    */ package net.minecraft.client.gui.font.glyphs;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*    */ import net.minecraft.client.gui.font.TextRenderable;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public class EmptyGlyph
/*    */   implements UnbakedGlyph {
/*    */   private final GlyphInfo info;
/*    */   
/*    */   public EmptyGlyph(float advance) {
/* 13 */     this.info = GlyphInfo.simple(advance);
/*    */   }
/*    */ 
/*    */   
/*    */   public GlyphInfo info() {
/* 18 */     return this.info;
/*    */   }
/*    */ 
/*    */   
/*    */   public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 23 */     return new BakedGlyph()
/*    */       {
/*    */         public GlyphInfo info() {
/* 26 */           return EmptyGlyph.this.info;
/*    */         }
/*    */ 
/*    */         
/*    */         public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
/* 31 */           return null;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/glyphs/EmptyGlyph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */