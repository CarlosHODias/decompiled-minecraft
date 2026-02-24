/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.crystal.EndCrystalModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EndCrystalRenderer extends EntityRenderer<EndCrystal, EndCrystalRenderState> {
/* 20 */   private static final Identifier END_CRYSTAL_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_crystal/end_crystal.png");
/* 21 */   private static final RenderType RENDER_TYPE = net.minecraft.client.renderer.rendertype.RenderTypes.entityCutoutNoCull(END_CRYSTAL_LOCATION);
/*    */   
/*    */   private final EndCrystalModel model;
/*    */   
/*    */   public EndCrystalRenderer(EntityRendererProvider.Context context) {
/* 26 */     super(context);
/* 27 */     this.shadowRadius = 0.5F;
/* 28 */     this.model = new EndCrystalModel(context.bakeLayer(ModelLayers.END_CRYSTAL));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EndCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 33 */     poseStack.pushPose();
/* 34 */     poseStack.scale(2.0F, 2.0F, 2.0F);
/* 35 */     poseStack.translate(0.0F, -0.5F, 0.0F);
/*    */     
/* 37 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, state, poseStack, RENDER_TYPE, state.lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 39 */     poseStack.popPose();
/*    */     
/* 41 */     Vec3 beamOffset = state.beamOffset;
/* 42 */     if (beamOffset != null) {
/* 43 */       float crystalY = getY(state.ageInTicks);
/* 44 */       float deltaX = (float)beamOffset.x;
/* 45 */       float deltaY = (float)beamOffset.y;
/* 46 */       float deltaZ = (float)beamOffset.z;
/* 47 */       poseStack.translate(beamOffset);
/* 48 */       EnderDragonRenderer.submitCrystalBeams(-deltaX, -deltaY + crystalY, -deltaZ, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords);
/*    */     } 
/*    */     
/* 51 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   public static float getY(float timeInTicks) {
/* 55 */     float hh = Mth.sin((timeInTicks * 0.2F)) / 2.0F + 0.5F;
/* 56 */     hh = (hh * hh + hh) * 0.4F;
/* 57 */     return hh - 1.4F;
/*    */   }
/*    */ 
/*    */   
/*    */   public EndCrystalRenderState createRenderState() {
/* 62 */     return new EndCrystalRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(EndCrystal entity, EndCrystalRenderState state, float partialTicks) {
/* 67 */     super.extractRenderState(entity, state, partialTicks);
/* 68 */     state.ageInTicks = entity.time + partialTicks;
/* 69 */     state.showsBottom = entity.showsBottom();
/* 70 */     BlockPos beamTarget = entity.getBeamTarget();
/* 71 */     if (beamTarget != null) {
/* 72 */       state.beamOffset = Vec3.atCenterOf((Vec3i)beamTarget).subtract(entity.getPosition(partialTicks));
/*    */     } else {
/* 74 */       state.beamOffset = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRender(EndCrystal entity, Frustum culler, double camX, double camY, double camZ) {
/* 80 */     return (super.shouldRender(entity, culler, camX, camY, camZ) || entity.getBeamTarget() != null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EndCrystalRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */