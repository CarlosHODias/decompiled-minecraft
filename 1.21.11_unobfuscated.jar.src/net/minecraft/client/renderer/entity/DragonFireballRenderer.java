/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class DragonFireballRenderer extends EntityRenderer<DragonFireball, EntityRenderState> {
/* 18 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_fireball.png");
/* 19 */   private static final RenderType RENDER_TYPE = RenderTypes.entityCutoutNoCull(TEXTURE_LOCATION);
/*    */   
/*    */   public DragonFireballRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(DragonFireball entity, BlockPos blockPos) {
/* 27 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 32 */     poseStack.pushPose();
/* 33 */     poseStack.scale(2.0F, 2.0F, 2.0F);
/*    */     
/* 35 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/*    */     
/* 37 */     submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
/*    */           vertex(buffer, pose, state.lightCoords, 0.0F, 0, 0, 1);
/*    */           
/*    */           vertex(buffer, pose, state.lightCoords, 1.0F, 0, 1, 1);
/*    */           vertex(buffer, pose, state.lightCoords, 1.0F, 1, 1, 0);
/*    */           vertex(buffer, pose, state.lightCoords, 0.0F, 1, 0, 0);
/*    */         });
/* 44 */     poseStack.popPose();
/*    */     
/* 46 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int lightCoords, float x, int y, int u, int v) {
/* 50 */     builder.addVertex(pose, x - 0.5F, y - 0.25F, 0.0F).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityRenderState createRenderState() {
/* 55 */     return new EntityRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/DragonFireballRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */