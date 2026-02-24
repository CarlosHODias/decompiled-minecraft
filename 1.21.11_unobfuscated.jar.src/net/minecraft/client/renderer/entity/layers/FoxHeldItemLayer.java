/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.fox.FoxModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class FoxHeldItemLayer extends RenderLayer<FoxRenderState, FoxModel> {
/*    */   public FoxHeldItemLayer(RenderLayerParent<FoxRenderState, FoxModel> renderer) {
/* 15 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, FoxRenderState state, float yRot, float xRot) {
/* 20 */     ItemStackRenderState item = state.heldItem;
/* 21 */     if (item.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     boolean sleeping = state.isSleeping;
/* 26 */     boolean isBaby = state.isBaby;
/*    */     
/* 28 */     poseStack.pushPose();
/*    */     
/* 30 */     poseStack.translate((getParentModel()).head.x / 16.0F, (getParentModel()).head.y / 16.0F, (getParentModel()).head.z / 16.0F);
/*    */ 
/*    */     
/* 33 */     if (isBaby) {
/* 34 */       float hs = 0.75F;
/* 35 */       poseStack.scale(0.75F, 0.75F, 0.75F);
/*    */     } 
/*    */ 
/*    */     
/* 39 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotation(state.headRollAngle));
/* 40 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot));
/* 41 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xRot));
/*    */ 
/*    */     
/* 44 */     if (state.isBaby) {
/* 45 */       if (sleeping) {
/* 46 */         poseStack.translate(0.4F, 0.26F, 0.15F);
/*    */       } else {
/* 48 */         poseStack.translate(0.06F, 0.26F, -0.5F);
/*    */       }
/*    */     
/* 51 */     } else if (sleeping) {
/* 52 */       poseStack.translate(0.46F, 0.26F, 0.22F);
/*    */     } else {
/* 54 */       poseStack.translate(0.06F, 0.27F, -0.5F);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 59 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0F));
/*    */     
/* 61 */     if (sleeping) {
/* 62 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(90.0F));
/*    */     }
/*    */     
/* 65 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*    */     
/* 67 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/FoxHeldItemLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */