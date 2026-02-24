/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.dolphin.DolphinModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.DolphinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DolphinCarryingItemLayer extends RenderLayer<DolphinRenderState, DolphinModel> {
/*    */   public DolphinCarryingItemLayer(RenderLayerParent<DolphinRenderState, DolphinModel> renderer) {
/* 15 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, DolphinRenderState state, float yRot, float xRot) {
/* 20 */     ItemStackRenderState item = state.heldItem;
/* 21 */     if (item.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     poseStack.pushPose();
/*    */     
/* 27 */     float y = 1.0F;
/* 28 */     float z = -1.0F;
/* 29 */     float angleXPercent = Mth.abs(state.xRot) / 60.0F;
/* 30 */     if (state.xRot < 0.0F) {
/* 31 */       poseStack.translate(0.0F, 1.0F - angleXPercent * 0.5F, -1.0F + angleXPercent * 0.5F);
/*    */     } else {
/* 33 */       poseStack.translate(0.0F, 1.0F + angleXPercent * 0.8F, -1.0F + angleXPercent * 0.2F);
/*    */     } 
/*    */     
/* 36 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*    */     
/* 38 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/DolphinCarryingItemLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */