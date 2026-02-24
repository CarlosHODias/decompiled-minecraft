/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public abstract class AbstractBoatRenderer extends EntityRenderer<AbstractBoat, BoatRenderState> {
/*    */   public AbstractBoatRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context);
/* 18 */     this.shadowRadius = 0.8F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 23 */     poseStack.pushPose();
/*    */     
/* 25 */     poseStack.translate(0.0F, 0.375F, 0.0F);
/* 26 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - state.yRot));
/* 27 */     float hurt = state.hurtTime;
/* 28 */     if (hurt > 0.0F) {
/* 29 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.sin(hurt) * hurt * state.damageTime / 10.0F * state.hurtDir));
/*    */     }
/*    */     
/* 32 */     if (!state.isUnderWater && !Mth.equal(state.bubbleAngle, 0.0F))
/*    */     {
/* 34 */       poseStack.mulPose((Quaternionfc)new Quaternionf().setAngleAxis(state.bubbleAngle * 0.017453292F, 1.0F, 0.0F, 1.0F));
/*    */     }
/*    */     
/* 37 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 38 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/*    */     
/* 40 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)model(), state, poseStack, renderType(), state.lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 42 */     submitTypeAdditions(state, poseStack, submitNodeCollector, state.lightCoords);
/*    */     
/* 44 */     poseStack.popPose();
/*    */     
/* 46 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BoatRenderState createRenderState() {
/* 58 */     return new BoatRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(AbstractBoat entity, BoatRenderState state, float partialTicks) {
/* 63 */     super.extractRenderState(entity, state, partialTicks);
/* 64 */     state.yRot = entity.getYRot(partialTicks);
/* 65 */     state.hurtTime = entity.getHurtTime() - partialTicks;
/* 66 */     state.hurtDir = entity.getHurtDir();
/* 67 */     state.damageTime = Math.max(entity.getDamage() - partialTicks, 0.0F);
/* 68 */     state.bubbleAngle = entity.getBubbleAngle(partialTicks);
/* 69 */     state.isUnderWater = entity.isUnderWater();
/* 70 */     state.rowingTimeLeft = entity.getRowingTime(0, partialTicks);
/* 71 */     state.rowingTimeRight = entity.getRowingTime(1, partialTicks);
/*    */   }
/*    */   
/*    */   protected abstract net.minecraft.client.model.EntityModel<BoatRenderState> model();
/*    */   
/*    */   protected abstract RenderType renderType();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AbstractBoatRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */