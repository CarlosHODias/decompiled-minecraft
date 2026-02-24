/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ExperienceOrbRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ExperienceOrbRenderer extends EntityRenderer<ExperienceOrb, ExperienceOrbRenderState> {
/* 18 */   private static final Identifier EXPERIENCE_ORB_LOCATION = Identifier.withDefaultNamespace("textures/entity/experience_orb.png");
/* 19 */   private static final RenderType RENDER_TYPE = net.minecraft.client.renderer.rendertype.RenderTypes.itemEntityTranslucentCull(EXPERIENCE_ORB_LOCATION);
/*    */   
/*    */   public ExperienceOrbRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context);
/* 23 */     this.shadowRadius = 0.15F;
/* 24 */     this.shadowStrength = 0.75F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(ExperienceOrb entity, BlockPos blockPos) {
/* 29 */     return Mth.clamp(super.getBlockLightLevel(entity, blockPos) + 7, 0, 15);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(ExperienceOrbRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 34 */     poseStack.pushPose();
/*    */     
/* 36 */     int icon = state.icon;
/* 37 */     float u0 = (icon % 4 * 16 + 0) / 64.0F;
/* 38 */     float u1 = (icon % 4 * 16 + 16) / 64.0F;
/* 39 */     float v0 = (icon / 4 * 16 + 0) / 64.0F;
/* 40 */     float v1 = (icon / 4 * 16 + 16) / 64.0F;
/*    */     
/* 42 */     float r = 1.0F;
/* 43 */     float xo = 0.5F;
/* 44 */     float yo = 0.25F;
/*    */     
/* 46 */     float br = 255.0F;
/* 47 */     float rr = state.ageInTicks / 2.0F;
/* 48 */     int rc = (int)((Mth.sin((rr + 0.0F)) + 1.0F) * 0.5F * 255.0F);
/* 49 */     int gc = 255;
/* 50 */     int bc = (int)((Mth.sin((rr + 4.1887903F)) + 1.0F) * 0.1F * 255.0F);
/*    */     
/* 52 */     poseStack.translate(0.0F, 0.1F, 0.0F);
/* 53 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/*    */     
/* 55 */     float s = 0.3F;
/* 56 */     poseStack.scale(0.3F, 0.3F, 0.3F);
/*    */     
/* 58 */     submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
/*    */           vertex(buffer, pose, -0.5F, -0.25F, rc, 255, bc, u0, v1, state.lightCoords);
/*    */           
/*    */           vertex(buffer, pose, 0.5F, -0.25F, rc, 255, bc, u1, v1, state.lightCoords);
/*    */           vertex(buffer, pose, 0.5F, 0.75F, rc, 255, bc, u1, v0, state.lightCoords);
/*    */           vertex(buffer, pose, -0.5F, 0.75F, rc, 255, bc, u0, v0, state.lightCoords);
/*    */         });
/* 65 */     poseStack.popPose();
/*    */     
/* 67 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, int r, int g, int b, float u, float v, int lightCoords) {
/* 71 */     buffer.addVertex(pose, x, y, 0.0F).setColor(r, g, b, 128).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public ExperienceOrbRenderState createRenderState() {
/* 76 */     return new ExperienceOrbRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ExperienceOrb entity, ExperienceOrbRenderState state, float partialTicks) {
/* 81 */     super.extractRenderState(entity, state, partialTicks);
/* 82 */     state.icon = entity.getIcon();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ExperienceOrbRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */