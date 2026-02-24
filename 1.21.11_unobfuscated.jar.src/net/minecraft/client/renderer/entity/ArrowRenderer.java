/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.ArrowModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public abstract class ArrowRenderer<T extends AbstractArrow, S extends ArrowRenderState> extends EntityRenderer<T, S> {
/*    */   public ArrowRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context);
/* 20 */     this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
/*    */   }
/*    */   private final ArrowModel model;
/*    */   
/*    */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 25 */     poseStack.pushPose();
/*    */     
/* 27 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((ArrowRenderState)state).yRot - 90.0F));
/* 28 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(((ArrowRenderState)state).xRot));
/*    */     
/* 30 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, state, poseStack, RenderTypes.entityCutout(getTextureLocation(state)), ((ArrowRenderState)state).lightCoords, OverlayTexture.NO_OVERLAY, ((ArrowRenderState)state).outlineColor, null);
/*    */     
/* 32 */     poseStack.popPose();
/*    */     
/* 34 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 41 */     super.extractRenderState(entity, state, partialTicks);
/* 42 */     ((ArrowRenderState)state).xRot = entity.getXRot(partialTicks);
/* 43 */     ((ArrowRenderState)state).yRot = entity.getYRot(partialTicks);
/* 44 */     ((ArrowRenderState)state).shake = ((AbstractArrow)entity).shakeTime - partialTicks;
/*    */   }
/*    */   
/*    */   protected abstract Identifier getTextureLocation(S paramS);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ArrowRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */