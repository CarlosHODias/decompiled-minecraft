/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.effects.EvokerFangsModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EvokerFangsRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class EvokerFangsRenderer extends EntityRenderer<EvokerFangs, EvokerFangsRenderState> {
/* 16 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/illager/evoker_fangs.png");
/*    */   
/*    */   private final EvokerFangsModel model;
/*    */   
/*    */   public EvokerFangsRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context);
/*    */     
/* 23 */     this.model = new EvokerFangsModel(context.bakeLayer(ModelLayers.EVOKER_FANGS));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EvokerFangsRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 28 */     float biteProgress = state.biteProgress;
/* 29 */     if (biteProgress == 0.0F) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     poseStack.pushPose();
/* 34 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(90.0F - state.yRot));
/*    */     
/* 36 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 37 */     poseStack.translate(0.0F, -1.501F, 0.0F);
/*    */     
/* 39 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, state, poseStack, this.model.renderType(TEXTURE_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/* 40 */     poseStack.popPose();
/*    */     
/* 42 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public EvokerFangsRenderState createRenderState() {
/* 47 */     return new EvokerFangsRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(EvokerFangs entity, EvokerFangsRenderState state, float partialTicks) {
/* 52 */     super.extractRenderState(entity, state, partialTicks);
/* 53 */     state.yRot = entity.getYRot();
/* 54 */     state.biteProgress = entity.getAnimationProgress(partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EvokerFangsRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */