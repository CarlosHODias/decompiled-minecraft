/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.ArmedModel;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.HeadedModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class PlayerItemInHandLayer<S extends AvatarRenderState, M extends EntityModel<S> & ArmedModel & HeadedModel> extends ItemInHandLayer<S, M> {
/*    */   private static final float X_ROT_MIN = -0.5235988F;
/*    */   private static final float X_ROT_MAX = 1.5707964F;
/*    */   
/*    */   public PlayerItemInHandLayer(RenderLayerParent<S, M> renderer) {
/* 24 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void submitArmWithItem(S state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 29 */     if (item.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     InteractionHand currentHand = (arm == ((AvatarRenderState)state).mainArm) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
/* 34 */     if (((AvatarRenderState)state).isUsingItem && ((AvatarRenderState)state).useItemHand == currentHand && ((AvatarRenderState)state).attackTime < 1.0E-5F && !((AvatarRenderState)state).heldOnHead.isEmpty()) {
/* 35 */       renderItemHeldToEye(state, arm, poseStack, submitNodeCollector, lightCoords);
/*    */     } else {
/* 37 */       super.submitArmWithItem(state, item, itemStack, arm, poseStack, submitNodeCollector, lightCoords);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void renderItemHeldToEye(S state, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 42 */     poseStack.pushPose();
/*    */     
/* 44 */     getParentModel().root().translateAndRotate(poseStack);
/* 45 */     ModelPart head = ((HeadedModel)getParentModel()).getHead();
/* 46 */     float previousXRot = head.xRot;
/* 47 */     head.xRot = Mth.clamp(head.xRot, -0.5235988F, 1.5707964F);
/* 48 */     head.translateAndRotate(poseStack);
/* 49 */     head.xRot = previousXRot;
/*    */     
/* 51 */     CustomHeadLayer.translateToHead(poseStack, CustomHeadLayer.Transforms.DEFAULT);
/*    */     
/* 53 */     boolean isLeftHand = (arm == HumanoidArm.LEFT);
/* 54 */     poseStack.translate((isLeftHand ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
/*    */     
/* 56 */     ((AvatarRenderState)state).heldOnHead.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((AvatarRenderState)state).outlineColor);
/*    */     
/* 58 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/PlayerItemInHandLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */