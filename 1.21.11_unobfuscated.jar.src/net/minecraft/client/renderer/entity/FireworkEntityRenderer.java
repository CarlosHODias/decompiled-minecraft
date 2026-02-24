/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FireworkRocketRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class FireworkEntityRenderer extends EntityRenderer<FireworkRocketEntity, FireworkRocketRenderState> {
/*    */   public FireworkEntityRenderer(EntityRendererProvider.Context context) {
/* 18 */     super(context);
/* 19 */     this.itemModelResolver = context.getItemModelResolver();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(FireworkRocketRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 24 */     poseStack.pushPose();
/* 25 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/*    */     
/* 27 */     if (state.isShotAtAngle) {
/* 28 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/* 29 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 30 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0F));
/*    */     } 
/*    */     
/* 33 */     state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*    */     
/* 35 */     poseStack.popPose();
/*    */     
/* 37 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   private final ItemModelResolver itemModelResolver;
/*    */   
/*    */   public FireworkRocketRenderState createRenderState() {
/* 42 */     return new FireworkRocketRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(FireworkRocketEntity entity, FireworkRocketRenderState state, float partialTicks) {
/* 47 */     super.extractRenderState(entity, state, partialTicks);
/* 48 */     state.isShotAtAngle = entity.isShotAtAngle();
/* 49 */     this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.GROUND, (Entity)entity);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/FireworkEntityRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */