/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.monster.guardian.GuardianModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.GuardianRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class GuardianRenderer extends MobRenderer<Guardian, GuardianRenderState, GuardianModel> {
/*  28 */   private static final Identifier GUARDIAN_LOCATION = Identifier.withDefaultNamespace("textures/entity/guardian.png");
/*  29 */   private static final Identifier GUARDIAN_BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/guardian_beam.png");
/*     */   
/*  31 */   private static final RenderType BEAM_RENDER_TYPE = RenderTypes.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);
/*     */   
/*     */   public GuardianRenderer(EntityRendererProvider.Context context) {
/*  34 */     this(context, 0.5F, ModelLayers.GUARDIAN);
/*     */   }
/*     */   
/*     */   protected GuardianRenderer(EntityRendererProvider.Context context, float shadow, ModelLayerLocation modelId) {
/*  38 */     super(context, new GuardianModel(context.bakeLayer(modelId)), shadow);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(Guardian entity, Frustum culler, double camX, double camY, double camZ) {
/*  43 */     if (super.shouldRender(entity, culler, camX, camY, camZ)) {
/*  44 */       return true;
/*     */     }
/*     */     
/*  47 */     if (entity.hasActiveAttackTarget()) {
/*  48 */       LivingEntity lookAtEntity = entity.getActiveAttackTarget();
/*  49 */       if (lookAtEntity != null) {
/*     */         
/*  51 */         Vec3 targetPos = getPosition(lookAtEntity, lookAtEntity.getBbHeight() * 0.5D, 1.0F);
/*  52 */         Vec3 startPos = getPosition((LivingEntity)entity, entity.getEyeHeight(), 1.0F);
/*     */         
/*  54 */         return culler.isVisible(new AABB(startPos.x, startPos.y, startPos.z, targetPos.x, targetPos.y, targetPos.z));
/*     */       } 
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   private Vec3 getPosition(LivingEntity entity, double yOffset, float partialTicks) {
/*  61 */     double sx = Mth.lerp(partialTicks, entity.xOld, entity.getX());
/*  62 */     double sy = Mth.lerp(partialTicks, entity.yOld, entity.getY()) + yOffset;
/*  63 */     double sz = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
/*  64 */     return new Vec3(sx, sy, sz);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(GuardianRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  69 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */     
/*  71 */     Vec3 targetPosition = state.attackTargetPosition;
/*  72 */     if (targetPosition != null) {
/*  73 */       float texVOff = state.attackTime * 0.5F % 1.0F;
/*  74 */       poseStack.pushPose();
/*  75 */       poseStack.translate(0.0F, state.eyeHeight, 0.0F);
/*  76 */       renderBeam(poseStack, submitNodeCollector, targetPosition.subtract(state.eyePosition), state.attackTime, state.attackScale, texVOff);
/*  77 */       poseStack.popPose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void renderBeam(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 beamVector, float timeInTicks, float scale, float texVOff) {
/*  82 */     float length = (float)(beamVector.length() + 1.0D);
/*  83 */     beamVector = beamVector.normalize();
/*     */ 
/*     */     
/*  86 */     float xRot = (float)Math.acos(beamVector.y);
/*  87 */     float yRot = 1.5707964F - (float)Math.atan2(beamVector.z, beamVector.x);
/*  88 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot * 57.295776F));
/*  89 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xRot * 57.295776F));
/*     */     
/*  91 */     float rot = timeInTicks * 0.05F * -1.5F;
/*     */     
/*  93 */     float colorScale = scale * scale;
/*  94 */     int red = 64 + (int)(colorScale * 191.0F);
/*  95 */     int green = 32 + (int)(colorScale * 191.0F);
/*  96 */     int blue = 128 - (int)(colorScale * 64.0F);
/*     */     
/*  98 */     float rr1 = 0.2F;
/*  99 */     float rr2 = 0.282F;
/*     */     
/* 101 */     float wnx = Mth.cos((rot + 2.3561945F)) * 0.282F;
/* 102 */     float wnz = Mth.sin((rot + 2.3561945F)) * 0.282F;
/* 103 */     float enx = Mth.cos((rot + 0.7853982F)) * 0.282F;
/* 104 */     float enz = Mth.sin((rot + 0.7853982F)) * 0.282F;
/* 105 */     float wsx = Mth.cos((rot + 3.926991F)) * 0.282F;
/* 106 */     float wsz = Mth.sin((rot + 3.926991F)) * 0.282F;
/* 107 */     float esx = Mth.cos((rot + 5.4977875F)) * 0.282F;
/* 108 */     float esz = Mth.sin((rot + 5.4977875F)) * 0.282F;
/*     */     
/* 110 */     float wx = Mth.cos((rot + 3.1415927F)) * 0.2F;
/* 111 */     float wz = Mth.sin((rot + 3.1415927F)) * 0.2F;
/* 112 */     float ex = Mth.cos((rot + 0.0F)) * 0.2F;
/* 113 */     float ez = Mth.sin((rot + 0.0F)) * 0.2F;
/*     */     
/* 115 */     float nx = Mth.cos((rot + 1.5707964F)) * 0.2F;
/* 116 */     float nz = Mth.sin((rot + 1.5707964F)) * 0.2F;
/* 117 */     float sx = Mth.cos((rot + 4.712389F)) * 0.2F;
/* 118 */     float sz = Mth.sin((rot + 4.712389F)) * 0.2F;
/*     */     
/* 120 */     float top = length;
/*     */     
/* 122 */     float minU = 0.0F;
/* 123 */     float maxU = 0.4999F;
/* 124 */     float minV = -1.0F + texVOff;
/* 125 */     float maxV = minV + length * 2.5F;
/*     */     
/* 127 */     submitNodeCollector.submitCustomGeometry(poseStack, BEAM_RENDER_TYPE, (pose, buffer) -> {
/*     */           vertex(buffer, pose, wx, top, wz, red, green, blue, 0.4999F, maxV);
/*     */           vertex(buffer, pose, wx, 0.0F, wz, red, green, blue, 0.4999F, minV);
/*     */           vertex(buffer, pose, ex, 0.0F, ez, red, green, blue, 0.0F, minV);
/*     */           vertex(buffer, pose, ex, top, ez, red, green, blue, 0.0F, maxV);
/*     */           vertex(buffer, pose, nx, top, nz, red, green, blue, 0.4999F, maxV);
/*     */           vertex(buffer, pose, nx, 0.0F, nz, red, green, blue, 0.4999F, minV);
/*     */           vertex(buffer, pose, sx, 0.0F, sz, red, green, blue, 0.0F, minV);
/*     */           vertex(buffer, pose, sx, top, sz, red, green, blue, 0.0F, maxV);
/*     */           float vBase = (Mth.floor(timeInTicks) % 2 == 0) ? 0.5F : 0.0F;
/*     */           vertex(buffer, pose, wnx, top, wnz, red, green, blue, 0.5F, vBase + 0.5F);
/*     */           vertex(buffer, pose, enx, top, enz, red, green, blue, 1.0F, vBase + 0.5F);
/*     */           vertex(buffer, pose, esx, top, esz, red, green, blue, 1.0F, vBase);
/*     */           vertex(buffer, pose, wsx, top, wsz, red, green, blue, 0.5F, vBase);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void vertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, float z, int red, int green, int blue, float u, float v) {
/* 147 */     builder.addVertex(pose, x, y, z).setColor(red, green, blue, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getTextureLocation(GuardianRenderState state) {
/* 152 */     return GUARDIAN_LOCATION;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuardianRenderState createRenderState() {
/* 157 */     return new GuardianRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(Guardian entity, GuardianRenderState state, float partialTicks) {
/* 162 */     super.extractRenderState(entity, state, partialTicks);
/* 163 */     state.spikesAnimation = entity.getSpikesAnimation(partialTicks);
/* 164 */     state.tailAnimation = entity.getTailAnimation(partialTicks);
/*     */     
/* 166 */     state.eyePosition = entity.getEyePosition(partialTicks);
/*     */     
/* 168 */     Entity lookAtEntity = getEntityToLookAt(entity);
/* 169 */     if (lookAtEntity != null) {
/* 170 */       state.lookDirection = entity.getViewVector(partialTicks);
/* 171 */       state.lookAtPosition = lookAtEntity.getEyePosition(partialTicks);
/*     */     } else {
/* 173 */       state.lookDirection = null;
/* 174 */       state.lookAtPosition = null;
/*     */     } 
/*     */     
/* 177 */     LivingEntity targetEntity = entity.getActiveAttackTarget();
/* 178 */     if (targetEntity != null) {
/* 179 */       state.attackScale = entity.getAttackAnimationScale(partialTicks);
/* 180 */       state.attackTime = entity.getClientSideAttackTime() + partialTicks;
/* 181 */       state.attackTargetPosition = getPosition(targetEntity, targetEntity.getBbHeight() * 0.5D, partialTicks);
/*     */     } else {
/* 183 */       state.attackTargetPosition = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Entity getEntityToLookAt(Guardian entity) {
/* 188 */     Entity lookAtEntity = Minecraft.getInstance().getCameraEntity();
/* 189 */     if (entity.hasActiveAttackTarget()) {
/* 190 */       return (Entity)entity.getActiveAttackTarget();
/*     */     }
/* 192 */     return lookAtEntity;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/GuardianRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */