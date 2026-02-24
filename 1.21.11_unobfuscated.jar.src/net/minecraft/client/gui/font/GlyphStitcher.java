/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphBitmap;
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedSheetGlyph;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class GlyphStitcher
/*    */   implements AutoCloseable
/*    */ {
/*    */   private final TextureManager textureManager;
/*    */   private final Identifier texturePrefix;
/* 17 */   private final List<FontTexture> textures = new ArrayList<>();
/*    */   
/*    */   public GlyphStitcher(TextureManager textureManager, Identifier texturePrefix) {
/* 20 */     this.textureManager = textureManager;
/* 21 */     this.texturePrefix = texturePrefix;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 25 */     int textureCount = this.textures.size();
/* 26 */     this.textures.clear();
/* 27 */     for (int i = 0; i < textureCount; i++) {
/* 28 */       this.textureManager.release(textureName(i));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 34 */     reset();
/*    */   }
/*    */   
/*    */   public BakedSheetGlyph stitch(GlyphInfo info, GlyphBitmap glyphBitmap) {
/* 38 */     for (FontTexture fontTexture : this.textures) {
/* 39 */       BakedSheetGlyph glyph = fontTexture.add(info, glyphBitmap);
/* 40 */       if (glyph != null) {
/* 41 */         return glyph;
/*    */       }
/*    */     } 
/*    */     
/* 45 */     int nextIndex = this.textures.size();
/* 46 */     Identifier name = textureName(nextIndex);
/* 47 */     boolean isColored = glyphBitmap.isColored();
/* 48 */     GlyphRenderTypes renderTypes = isColored ? GlyphRenderTypes.createForColorTexture(name) : GlyphRenderTypes.createForIntensityTexture(name);
/* 49 */     Objects.requireNonNull(name); FontTexture texture = new FontTexture(name::toString, renderTypes, isColored);
/* 50 */     this.textures.add(texture);
/* 51 */     this.textureManager.register(name, texture);
/* 52 */     return texture.add(info, glyphBitmap);
/*    */   }
/*    */   
/*    */   private Identifier textureName(int index) {
/* 56 */     return this.texturePrefix.withSuffix("/" + index);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/GlyphStitcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */