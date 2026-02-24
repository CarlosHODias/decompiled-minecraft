/*    */ package com.mojang.blaze3d.font;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ 
/*    */ public interface GlyphBitmap {
/*    */   int getPixelWidth();
/*    */   
/*    */   int getPixelHeight();
/*    */   
/*    */   void upload(int paramInt1, int paramInt2, GpuTexture paramGpuTexture);
/*    */   
/*    */   boolean isColored();
/*    */   
/*    */   float getOversample();
/*    */   
/*    */   default float getLeft() {
/* 17 */     return getBearingLeft();
/*    */   }
/*    */   
/*    */   default float getRight() {
/* 21 */     return getLeft() + getPixelWidth() / getOversample();
/*    */   }
/*    */   
/*    */   default float getTop() {
/* 25 */     return 7.0F - getBearingTop();
/*    */   }
/*    */   
/*    */   default float getBottom() {
/* 29 */     return getTop() + getPixelHeight() / getOversample();
/*    */   }
/*    */   
/*    */   default float getBearingLeft() {
/* 33 */     return 0.0F;
/*    */   }
/*    */   
/*    */   default float getBearingTop() {
/* 37 */     return 7.0F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/font/GlyphBitmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */