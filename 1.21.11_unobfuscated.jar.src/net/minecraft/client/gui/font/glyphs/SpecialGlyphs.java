/*    */ package net.minecraft.client.gui.font.glyphs;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphBitmap;
/*    */ import com.mojang.blaze3d.font.GlyphInfo;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.font.GlyphStitcher;
/*    */ 
/*    */ 
/*    */ public enum SpecialGlyphs
/*    */   implements GlyphInfo
/*    */ {
/* 15 */   WHITE(() -> generate(5, 8, ())),
/* 16 */   MISSING(() -> {
/*    */       int width = 5, height = 8;
/*    */       return generate(5, 8, ());
/*    */     });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final NativeImage image;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static NativeImage generate(int width, int height, PixelProvider pixelProvider) {
/* 33 */     NativeImage result = new NativeImage(NativeImage.Format.RGBA, width, height, false);
/* 34 */     for (int y = 0; y < height; y++) {
/* 35 */       for (int x = 0; x < width; x++) {
/* 36 */         result.setPixel(x, y, pixelProvider.getColor(x, y));
/*    */       }
/*    */     } 
/* 39 */     result.untrack();
/* 40 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   SpecialGlyphs(Supplier<NativeImage> image) {
/* 46 */     this.image = image.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getAdvance() {
/* 51 */     return (this.image.getWidth() + 1);
/*    */   }
/*    */   
/*    */   public BakedSheetGlyph bake(GlyphStitcher stitcher) {
/* 55 */     return stitcher.stitch(this, new GlyphBitmap()
/*    */         {
/*    */           
/*    */           public int getPixelWidth()
/*    */           {
/* 60 */             return SpecialGlyphs.this.image.getWidth();
/*    */           }
/*    */ 
/*    */           
/*    */           public int getPixelHeight() {
/* 65 */             return SpecialGlyphs.this.image.getHeight();
/*    */           }
/*    */ 
/*    */           
/*    */           public float getOversample() {
/* 70 */             return 1.0F;
/*    */           }
/*    */ 
/*    */           
/*    */           public void upload(int x, int y, GpuTexture texture) {
/* 75 */             RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, SpecialGlyphs.this.image, 0, 0, x, y, SpecialGlyphs.this.image.getWidth(), SpecialGlyphs.this.image.getHeight(), 0, 0);
/*    */           }
/*    */ 
/*    */           
/*    */           public boolean isColored() {
/* 80 */             return true;
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface PixelProvider {
/*    */     int getColor(int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/glyphs/SpecialGlyphs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */