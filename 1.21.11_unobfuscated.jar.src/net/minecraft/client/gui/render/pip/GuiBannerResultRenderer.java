/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.render.state.pip.GuiBannerResultRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*    */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.client.resources.model.ModelBakery;
/*    */ 
/*    */ public class GuiBannerResultRenderer extends PictureInPictureRenderer<GuiBannerResultRenderState> {
/*    */   private final MaterialSet materials;
/*    */   
/*    */   public GuiBannerResultRenderer(MultiBufferSource.BufferSource bufferSource, MaterialSet materials) {
/* 22 */     super(bufferSource);
/* 23 */     this.materials = materials;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<GuiBannerResultRenderState> getRenderStateClass() {
/* 28 */     return GuiBannerResultRenderState.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderToTexture(GuiBannerResultRenderState renderState, PoseStack poseStack) {
/* 33 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
/* 34 */     poseStack.translate(0.0F, 0.25F, 0.0F);
/* 35 */     FeatureRenderDispatcher featureRenderDispatcher = (Minecraft.getInstance()).gameRenderer.getFeatureRenderDispatcher();
/* 36 */     SubmitNodeStorage submitNodeStorage = featureRenderDispatcher.getSubmitNodeStorage();
/* 37 */     BannerRenderer.submitPatterns(this.materials, poseStack, (SubmitNodeCollector)submitNodeStorage, 15728880, OverlayTexture.NO_OVERLAY, (Model)renderState.flag(), 0.0F, ModelBakery.BANNER_BASE, true, renderState.baseColor(), renderState.resultBannerPatterns(), false, null, 0);
/* 38 */     featureRenderDispatcher.renderAllFeatures();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 43 */     return "banner result";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/GuiBannerResultRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */