/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
/*    */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class GuiEntityRenderer extends PictureInPictureRenderer<GuiEntityRenderState> {
/*    */   public GuiEntityRenderer(MultiBufferSource.BufferSource bufferSource, EntityRenderDispatcher entityRenderDispatcher) {
/* 19 */     super(bufferSource);
/* 20 */     this.entityRenderDispatcher = entityRenderDispatcher;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<GuiEntityRenderState> getRenderStateClass() {
/* 25 */     return GuiEntityRenderState.class;
/*    */   }
/*    */   private final EntityRenderDispatcher entityRenderDispatcher;
/*    */   
/*    */   protected void renderToTexture(GuiEntityRenderState entityState, PoseStack poseStack) {
/* 30 */     (Minecraft.getInstance()).gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
/* 31 */     Vector3f translation = entityState.translation();
/* 32 */     poseStack.translate(translation.x, translation.y, translation.z);
/* 33 */     poseStack.mulPose((Quaternionfc)entityState.rotation());
/*    */     
/* 35 */     Quaternionf overriddenCameraAngle = entityState.overrideCameraAngle();
/* 36 */     FeatureRenderDispatcher featureRenderDispatcher = (Minecraft.getInstance()).gameRenderer.getFeatureRenderDispatcher();
/* 37 */     CameraRenderState cameraRenderState = new CameraRenderState();
/* 38 */     if (overriddenCameraAngle != null) {
/* 39 */       cameraRenderState.orientation = overriddenCameraAngle.conjugate(new Quaternionf()).rotateY(3.1415927F);
/*    */     }
/* 41 */     this.entityRenderDispatcher.submit(entityState.renderState(), cameraRenderState, 0.0D, 0.0D, 0.0D, poseStack, (SubmitNodeCollector)featureRenderDispatcher.getSubmitNodeStorage());
/* 42 */     featureRenderDispatcher.renderAllFeatures();
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 47 */     return height / 2.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTextureLabel() {
/* 52 */     return "entity";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/GuiEntityRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */