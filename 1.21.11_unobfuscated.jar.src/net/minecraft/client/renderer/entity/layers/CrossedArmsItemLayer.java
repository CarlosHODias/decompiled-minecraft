/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.VillagerLikeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CrossedArmsItemLayer<S extends HoldingEntityRenderState, M extends EntityModel<S> & VillagerLikeModel>
/*    */   extends RenderLayer<S, M> {
/*    */   public CrossedArmsItemLayer(RenderLayerParent<S, M> renderer) {
/* 18 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 23 */     ItemStackRenderState item = ((HoldingEntityRenderState)state).heldItem;
/* 24 */     if (item.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 28 */     poseStack.pushPose();
/* 29 */     applyTranslation(state, poseStack);
/*    */     
/* 31 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((HoldingEntityRenderState)state).outlineColor);
/*    */     
/* 33 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   protected void applyTranslation(S state, PoseStack poseStack) {
/* 37 */     ((VillagerLikeModel)getParentModel()).translateToArms((EntityRenderState)state, poseStack);
/* 38 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(0.75F));
/* 39 */     poseStack.scale(1.07F, 1.07F, 1.07F);
/* 40 */     poseStack.translate(0.0F, 0.13F, -0.34F);
/* 41 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(3.1415927F));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/CrossedArmsItemLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */