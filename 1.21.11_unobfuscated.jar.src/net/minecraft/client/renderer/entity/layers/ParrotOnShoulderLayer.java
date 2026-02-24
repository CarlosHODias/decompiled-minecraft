/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.parrot.ParrotModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.ParrotRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ParrotRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*    */ 
/*    */ public class ParrotOnShoulderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
/*    */   public ParrotOnShoulderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
/* 20 */     super(renderer);
/* 21 */     this.model = new ParrotModel(modelSet.bakeLayer(ModelLayers.PARROT));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
/* 26 */     Parrot.Variant parrotOnLeftShoulder = state.parrotOnLeftShoulder;
/* 27 */     if (parrotOnLeftShoulder != null) {
/* 28 */       submitOnShoulder(poseStack, submitNodeCollector, lightCoords, state, parrotOnLeftShoulder, yRot, xRot, true);
/*    */     }
/* 30 */     Parrot.Variant parrotOnRightShoulder = state.parrotOnRightShoulder;
/* 31 */     if (parrotOnRightShoulder != null)
/* 32 */       submitOnShoulder(poseStack, submitNodeCollector, lightCoords, state, parrotOnRightShoulder, yRot, xRot, false); 
/*    */   }
/*    */   private final ParrotModel model;
/*    */   
/*    */   private void submitOnShoulder(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState playerState, Parrot.Variant parrotVariant, float yRot, float xRot, boolean isLeft) {
/* 37 */     poseStack.pushPose();
/* 38 */     poseStack.translate(
/* 39 */         isLeft ? 0.4F : -0.4F, 
/* 40 */         playerState.isCrouching ? -1.3F : -1.5F, 0.0F);
/*    */ 
/*    */ 
/*    */     
/* 44 */     ParrotRenderState parrotState = new ParrotRenderState();
/* 45 */     parrotState.pose = ParrotModel.Pose.ON_SHOULDER;
/* 46 */     parrotState.ageInTicks = playerState.ageInTicks;
/* 47 */     parrotState.walkAnimationPos = playerState.walkAnimationPos;
/* 48 */     parrotState.walkAnimationSpeed = playerState.walkAnimationSpeed;
/* 49 */     parrotState.yRot = yRot;
/* 50 */     parrotState.xRot = xRot;
/*    */     
/* 52 */     submitNodeCollector.submitModel((Model)this.model, parrotState, poseStack, this.model.renderType(ParrotRenderer.getVariantTexture(parrotVariant)), lightCoords, OverlayTexture.NO_OVERLAY, playerState.outlineColor, null);
/*    */     
/* 54 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/ParrotOnShoulderLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */