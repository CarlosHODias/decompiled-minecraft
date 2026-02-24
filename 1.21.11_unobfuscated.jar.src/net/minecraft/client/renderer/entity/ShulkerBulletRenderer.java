/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.ShulkerBulletModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ShulkerBulletRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ShulkerBulletRenderer extends EntityRenderer<ShulkerBullet, ShulkerBulletRenderState> {
/* 20 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/shulker/spark.png");
/* 21 */   private static final net.minecraft.client.renderer.rendertype.RenderType RENDER_TYPE = net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(TEXTURE_LOCATION);
/*    */   
/*    */   private final ShulkerBulletModel model;
/*    */   
/*    */   public ShulkerBulletRenderer(EntityRendererProvider.Context context) {
/* 26 */     super(context);
/* 27 */     this.model = new ShulkerBulletModel(context.bakeLayer(ModelLayers.SHULKER_BULLET));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(ShulkerBullet entity, BlockPos blockPos) {
/* 32 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(ShulkerBulletRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 37 */     poseStack.pushPose();
/*    */     
/* 39 */     float tc = state.ageInTicks;
/*    */     
/* 41 */     poseStack.translate(0.0F, 0.15F, 0.0F);
/* 42 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(Mth.sin((tc * 0.1F)) * 180.0F));
/* 43 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.cos((tc * 0.1F)) * 180.0F));
/* 44 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(Mth.sin((tc * 0.15F)) * 360.0F));
/*    */     
/* 46 */     poseStack.scale(-0.5F, -0.5F, 0.5F);
/*    */     
/* 48 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, this.model.renderType(TEXTURE_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 50 */     poseStack.scale(1.5F, 1.5F, 1.5F);
/*    */     
/* 52 */     submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, 654311423, null, state.outlineColor, null);
/*    */     
/* 54 */     poseStack.popPose();
/*    */     
/* 56 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public ShulkerBulletRenderState createRenderState() {
/* 61 */     return new ShulkerBulletRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ShulkerBullet entity, ShulkerBulletRenderState state, float partialTicks) {
/* 66 */     super.extractRenderState(entity, state, partialTicks);
/* 67 */     state.yRot = entity.getYRot(partialTicks);
/* 68 */     state.xRot = entity.getXRot(partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ShulkerBulletRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */