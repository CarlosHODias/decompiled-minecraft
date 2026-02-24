/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OverlayTexture
/*    */   implements AutoCloseable
/*    */ {
/*    */   private static final int SIZE = 16;
/*    */   public static final int NO_WHITE_U = 0;
/*    */   public static final int RED_OVERLAY_V = 3;
/*    */   public static final int WHITE_OVERLAY_V = 10;
/* 16 */   public static final int NO_OVERLAY = pack(0, 10);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   private final DynamicTexture texture = new DynamicTexture("Entity Color Overlay", 16, 16, false); public OverlayTexture() {
/* 22 */     NativeImage pixels = this.texture.getPixels();
/*    */ 
/*    */     
/* 25 */     for (int y = 0; y < 16; y++) {
/* 26 */       for (int x = 0; x < 16; x++) {
/* 27 */         if (y < 8) {
/*    */           
/* 29 */           pixels.setPixel(x, y, -1291911168);
/*    */         } else {
/*    */           
/* 32 */           int a = (int)((1.0F - x / 15.0F * 0.75F) * 255.0F);
/* 33 */           pixels.setPixel(x, y, ARGB.white(a));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 38 */     this.texture.upload();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 43 */     this.texture.close();
/*    */   }
/*    */   
/*    */   public static int u(float whiteOverlayProgress) {
/* 47 */     return (int)(whiteOverlayProgress * 15.0F);
/*    */   }
/*    */   
/*    */   public static int v(boolean hurtOverlay) {
/* 51 */     return hurtOverlay ? 3 : 10;
/*    */   }
/*    */   
/*    */   public static int pack(int u, int v) {
/* 55 */     return u | v << 16;
/*    */   }
/*    */   
/*    */   public static int pack(float whiteOverlayProgress, boolean redOverlay) {
/* 59 */     return pack(u(whiteOverlayProgress), v(redOverlay));
/*    */   }
/*    */   
/*    */   public GpuTextureView getTextureView() {
/* 63 */     return this.texture.getTextureView();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/OverlayTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */