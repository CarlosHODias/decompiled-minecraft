/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MipmapGenerator
/*     */ {
/*     */   private static final String ITEM_PREFIX = "item/";
/*     */   private static final float ALPHA_CUTOFF = 0.5F;
/*     */   private static final float STRICT_ALPHA_CUTOFF = 0.3F;
/*     */   
/*     */   private static float alphaTestCoverage(NativeImage image, float alphaRef, float alphaScale) {
/*  20 */     int width = image.getWidth();
/*  21 */     int height = image.getHeight();
/*     */     
/*  23 */     float coverage = 0.0F;
/*  24 */     int subsample_count = 4;
/*  25 */     for (int y = 0; y < height - 1; y++) {
/*  26 */       for (int x = 0; x < width - 1; x++) {
/*  27 */         float alpha00 = Math.clamp(ARGB.alphaFloat(image.getPixel(x, y)) * alphaScale, 0.0F, 1.0F);
/*  28 */         float alpha10 = Math.clamp(ARGB.alphaFloat(image.getPixel(x + 1, y)) * alphaScale, 0.0F, 1.0F);
/*  29 */         float alpha01 = Math.clamp(ARGB.alphaFloat(image.getPixel(x, y + 1)) * alphaScale, 0.0F, 1.0F);
/*  30 */         float alpha11 = Math.clamp(ARGB.alphaFloat(image.getPixel(x + 1, y + 1)) * alphaScale, 0.0F, 1.0F);
/*     */         
/*  32 */         float texelCoverage = 0.0F;
/*  33 */         for (int subsample_y = 0; subsample_y < 4; subsample_y++) {
/*  34 */           float fy = (subsample_y + 0.5F) / 4.0F;
/*  35 */           for (int subsample_x = 0; subsample_x < 4; subsample_x++) {
/*  36 */             float fx = (subsample_x + 0.5F) / 4.0F;
/*  37 */             float alpha = alpha00 * (1.0F - fx) * (1.0F - fy) + alpha10 * fx * (1.0F - fy) + alpha01 * (1.0F - fx) * fy + alpha11 * fx * fy;
/*  38 */             if (alpha > alphaRef) {
/*  39 */               texelCoverage++;
/*     */             }
/*     */           } 
/*     */         } 
/*  43 */         coverage += texelCoverage / 16.0F;
/*     */       } 
/*     */     } 
/*     */     
/*  47 */     return coverage / ((width - 1) * (height - 1));
/*     */   }
/*     */   
/*     */   private static void scaleAlphaToCoverage(NativeImage image, float desiredCoverage, float alphaRef, float alphaCutoffBias) {
/*  51 */     float minAlphaScale = 0.0F;
/*  52 */     float maxAlphaScale = 4.0F;
/*  53 */     float alphaScale = 1.0F;
/*  54 */     float bestAlphaScale = 1.0F;
/*  55 */     float bestError = Float.MAX_VALUE;
/*  56 */     int width = image.getWidth();
/*  57 */     int height = image.getHeight();
/*     */     
/*  59 */     for (int i = 0; i < 5; i++) {
/*  60 */       float currentCoverage = alphaTestCoverage(image, alphaRef, alphaScale);
/*     */       
/*  62 */       float error = Math.abs(currentCoverage - desiredCoverage);
/*  63 */       if (error < bestError) {
/*  64 */         bestError = error;
/*  65 */         bestAlphaScale = alphaScale;
/*     */       } 
/*     */       
/*  68 */       if (currentCoverage < desiredCoverage) {
/*  69 */         minAlphaScale = alphaScale;
/*  70 */       } else if (currentCoverage > desiredCoverage) {
/*  71 */         maxAlphaScale = alphaScale;
/*     */       } else {
/*     */         break;
/*     */       } 
/*     */       
/*  76 */       alphaScale = (minAlphaScale + maxAlphaScale) * 0.5F;
/*     */     } 
/*     */     
/*  79 */     for (int y = 0; y < height; y++) {
/*  80 */       for (int x = 0; x < width; x++) {
/*  81 */         int pixel = image.getPixel(x, y);
/*  82 */         float alpha = ARGB.alphaFloat(pixel);
/*  83 */         alpha = alpha * bestAlphaScale + alphaCutoffBias + 0.025F;
/*  84 */         alpha = Math.clamp(alpha, 0.0F, 1.0F);
/*  85 */         image.setPixel(x, y, ARGB.color(alpha, pixel));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NativeImage[] generateMipLevels(Identifier name, NativeImage[] currentMips, int newMipLevel, MipmapStrategy mipmapStrategy, float alphaCutoffBias) {
/*  91 */     if (mipmapStrategy == MipmapStrategy.AUTO) {
/*  92 */       mipmapStrategy = hasTransparentPixel(currentMips[0]) ? MipmapStrategy.CUTOUT : MipmapStrategy.MEAN;
/*     */     }
/*     */     
/*  95 */     if (currentMips.length == 1 && !name.getPath().startsWith("item/")) {
/*  96 */       if (mipmapStrategy == MipmapStrategy.CUTOUT || mipmapStrategy == MipmapStrategy.STRICT_CUTOUT) {
/*  97 */         TextureUtil.solidify(currentMips[0]);
/*  98 */       } else if (mipmapStrategy == MipmapStrategy.DARK_CUTOUT) {
/*  99 */         TextureUtil.fillEmptyAreasWithDarkColor(currentMips[0]);
/*     */       } 
/*     */     }
/*     */     
/* 103 */     if (newMipLevel + 1 <= currentMips.length) {
/* 104 */       return currentMips;
/*     */     }
/*     */     
/* 107 */     NativeImage[] result = new NativeImage[newMipLevel + 1];
/*     */     
/* 109 */     result[0] = currentMips[0];
/*     */     
/* 111 */     boolean isCutoutMip = (mipmapStrategy == MipmapStrategy.CUTOUT || mipmapStrategy == MipmapStrategy.STRICT_CUTOUT || mipmapStrategy == MipmapStrategy.DARK_CUTOUT);
/* 112 */     float cutoutRef = (mipmapStrategy == MipmapStrategy.STRICT_CUTOUT) ? 0.3F : 0.5F;
/* 113 */     float originalCoverage = isCutoutMip ? alphaTestCoverage(currentMips[0], cutoutRef, 1.0F) : 0.0F;
/*     */     
/* 115 */     for (int level = 1; level <= newMipLevel; level++) {
/* 116 */       if (level < currentMips.length) {
/* 117 */         result[level] = currentMips[level];
/*     */       } else {
/* 119 */         NativeImage lastData = result[level - 1];
/* 120 */         NativeImage data = new NativeImage(lastData.getWidth() >> 1, lastData.getHeight() >> 1, false);
/*     */         
/* 122 */         int width = data.getWidth();
/* 123 */         int height = data.getHeight();
/*     */         
/* 125 */         for (int x = 0; x < width; x++) {
/* 126 */           for (int y = 0; y < height; y++) {
/* 127 */             int color; int color1 = lastData.getPixel(x * 2 + 0, y * 2 + 0);
/* 128 */             int color2 = lastData.getPixel(x * 2 + 1, y * 2 + 0);
/* 129 */             int color3 = lastData.getPixel(x * 2 + 0, y * 2 + 1);
/* 130 */             int color4 = lastData.getPixel(x * 2 + 1, y * 2 + 1);
/*     */             
/* 132 */             if (mipmapStrategy == MipmapStrategy.DARK_CUTOUT) {
/* 133 */               color = darkenedAlphaBlend(color1, color2, color3, color4);
/*     */             } else {
/* 135 */               color = ARGB.meanLinear(color1, color2, color3, color4);
/*     */             } 
/* 137 */             data.setPixel(x, y, color);
/*     */           } 
/*     */         } 
/*     */         
/* 141 */         result[level] = data;
/*     */       } 
/*     */       
/* 144 */       if (isCutoutMip) {
/* 145 */         scaleAlphaToCoverage(result[level], originalCoverage, cutoutRef, alphaCutoffBias);
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean hasTransparentPixel(NativeImage image) {
/* 153 */     for (int x = 0; x < image.getWidth(); x++) {
/* 154 */       for (int y = 0; y < image.getHeight(); y++) {
/* 155 */         if (ARGB.alpha(image.getPixel(x, y)) == 0) {
/* 156 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int darkenedAlphaBlend(int color1, int color2, int color3, int color4) {
/* 168 */     float aTotal = 0.0F;
/* 169 */     float rTotal = 0.0F;
/* 170 */     float gTotal = 0.0F;
/* 171 */     float bTotal = 0.0F;
/*     */     
/* 173 */     if (ARGB.alpha(color1) != 0) {
/* 174 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color1));
/* 175 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color1));
/* 176 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color1));
/* 177 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color1));
/*     */     } 
/* 179 */     if (ARGB.alpha(color2) != 0) {
/* 180 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color2));
/* 181 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color2));
/* 182 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color2));
/* 183 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color2));
/*     */     } 
/* 185 */     if (ARGB.alpha(color3) != 0) {
/* 186 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color3));
/* 187 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color3));
/* 188 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color3));
/* 189 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color3));
/*     */     } 
/* 191 */     if (ARGB.alpha(color4) != 0) {
/* 192 */       aTotal += ARGB.srgbToLinearChannel(ARGB.alpha(color4));
/* 193 */       rTotal += ARGB.srgbToLinearChannel(ARGB.red(color4));
/* 194 */       gTotal += ARGB.srgbToLinearChannel(ARGB.green(color4));
/* 195 */       bTotal += ARGB.srgbToLinearChannel(ARGB.blue(color4));
/*     */     } 
/* 197 */     aTotal /= 4.0F;
/* 198 */     rTotal /= 4.0F;
/* 199 */     gTotal /= 4.0F;
/* 200 */     bTotal /= 4.0F;
/*     */     
/* 202 */     return ARGB.color(
/* 203 */         ARGB.linearToSrgbChannel(aTotal), 
/* 204 */         ARGB.linearToSrgbChannel(rTotal), 
/* 205 */         ARGB.linearToSrgbChannel(gTotal), 
/* 206 */         ARGB.linearToSrgbChannel(bTotal));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/MipmapGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */