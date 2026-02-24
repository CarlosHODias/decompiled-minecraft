/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PanoramaRenderer {
/*  9 */   public static final Identifier PANORAMA_OVERLAY = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_overlay.png");
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   private final CubeMap cubeMap;
/*    */   private float spin;
/*    */   
/*    */   public PanoramaRenderer(CubeMap cubeMap) {
/* 16 */     this.cubeMap = cubeMap;
/* 17 */     this.minecraft = Minecraft.getInstance();
/*    */   }
/*    */   
/*    */   public void render(GuiGraphics graphics, int width, int height, boolean shouldSpin) {
/* 21 */     if (shouldSpin) {
/* 22 */       float a = this.minecraft.getDeltaTracker().getRealtimeDeltaTicks();
/* 23 */       float delta = (float)(a * (Double)this.minecraft.options.panoramaSpeed().get());
/* 24 */       this.spin = wrap(this.spin + delta * 0.1F, 360.0F);
/*    */     } 
/*    */     
/* 27 */     this.cubeMap.render(this.minecraft, 10.0F, -this.spin);
/* 28 */     graphics.blit(RenderPipelines.GUI_TEXTURED, PANORAMA_OVERLAY, 0, 0, 0.0F, 0.0F, width, height, 16, 128, 16, 128);
/*    */   }
/*    */   
/*    */   private static float wrap(float value, float limit) {
/* 32 */     return (value > limit) ? (value - limit) : value;
/*    */   }
/*    */   
/*    */   public void registerTextures(TextureManager textureManager) {
/* 36 */     this.cubeMap.registerTextures(textureManager);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/PanoramaRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */