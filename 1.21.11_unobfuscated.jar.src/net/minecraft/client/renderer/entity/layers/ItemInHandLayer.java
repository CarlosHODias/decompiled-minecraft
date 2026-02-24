/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.ArmedModel;
/*    */ import net.minecraft.client.model.effects.SpearAnimations;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ItemInHandLayer<S extends ArmedEntityRenderState, M extends net.minecraft.client.model.EntityModel<S> & ArmedModel> extends RenderLayer<S, M> {
/*    */   public ItemInHandLayer(RenderLayerParent<S, M> renderer) {
/* 19 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 24 */     submitArmWithItem(state, ((ArmedEntityRenderState)state).rightHandItemState, ((ArmedEntityRenderState)state).rightHandItemStack, HumanoidArm.RIGHT, poseStack, submitNodeCollector, lightCoords);
/* 25 */     submitArmWithItem(state, ((ArmedEntityRenderState)state).leftHandItemState, ((ArmedEntityRenderState)state).leftHandItemStack, HumanoidArm.LEFT, poseStack, submitNodeCollector, lightCoords);
/*    */   }
/*    */   
/*    */   protected void submitArmWithItem(S state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 29 */     if (item.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     poseStack.pushPose();
/* 34 */     ((ArmedModel)getParentModel()).translateToHand((EntityRenderState)state, arm, poseStack);
/*    */ 
/*    */     
/* 37 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F));
/* 38 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/*    */ 
/*    */     
/* 41 */     boolean isLeftHand = (arm == HumanoidArm.LEFT);
/* 42 */     poseStack.translate((isLeftHand ? -1 : true) / 16.0F, 0.125F, -0.625F);
/* 43 */     if (((ArmedEntityRenderState)state).attackTime > 0.0F && ((ArmedEntityRenderState)state).mainArm == arm && ((ArmedEntityRenderState)state).swingAnimationType == SwingAnimationType.STAB) {
/* 44 */       SpearAnimations.thirdPersonAttackItem((ArmedEntityRenderState)state, poseStack);
/*    */     }
/*    */     
/* 47 */     float ticksUsingItem = state.ticksUsingItem(arm);
/* 48 */     if (ticksUsingItem != 0.0F) {
/* 49 */       ((arm == HumanoidArm.RIGHT) ? ((ArmedEntityRenderState)state).rightArmPose : ((ArmedEntityRenderState)state).leftArmPose).animateUseItem((ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, itemStack);
/*    */     }
/*    */     
/* 52 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((ArmedEntityRenderState)state).outlineColor);
/* 53 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/ItemInHandLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */