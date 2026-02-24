/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import com.mojang.blaze3d.font.GlyphProvider;
/*    */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSets;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*    */ import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;
/*    */ 
/*    */ public class AllMissingGlyphProvider
/*    */   implements GlyphProvider {
/* 13 */   private static final UnbakedGlyph MISSING_INSTANCE = new UnbakedGlyph()
/*    */     {
/*    */       public GlyphInfo info() {
/* 16 */         return (GlyphInfo)SpecialGlyphs.MISSING;
/*    */       }
/*    */ 
/*    */       
/*    */       public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 21 */         return stitcher.getMissing();
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public UnbakedGlyph getGlyph(int codepoint) {
/* 27 */     return MISSING_INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public IntSet getSupportedGlyphs() {
/* 33 */     return (IntSet)IntSets.EMPTY_SET;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/AllMissingGlyphProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */