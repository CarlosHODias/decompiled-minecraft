/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.panda.PandaModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PandaRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class PandaHoldsItemLayer extends RenderLayer<PandaRenderState, PandaModel> {
/*    */   public PandaHoldsItemLayer(RenderLayerParent<PandaRenderState, PandaModel> renderer) {
/* 15 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, PandaRenderState state, float yRot, float xRot) {
/* 20 */     ItemStackRenderState item = state.heldItem;
/* 21 */     if (item.isEmpty() || !state.isSitting || state.isScared) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     float z = -0.6F;
/* 26 */     float y = 1.4F;
/* 27 */     if (state.isEating) {
/* 28 */       z -= 0.2F * Mth.sin((state.ageInTicks * 0.6F)) + 0.2F;
/* 29 */       y -= 0.09F * Mth.sin((state.ageInTicks * 0.6F));
/*    */     } 
/* 31 */     poseStack.pushPose();
/* 32 */     poseStack.translate(0.1F, y, z);
/* 33 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 34 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/PandaHoldsItemLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */