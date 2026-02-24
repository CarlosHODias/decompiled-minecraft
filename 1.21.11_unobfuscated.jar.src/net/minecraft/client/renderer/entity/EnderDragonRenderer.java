/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.monster.dragon.EnderDragonModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class EnderDragonRenderer extends EntityRenderer<EnderDragon, EnderDragonRenderState> {
/*  31 */   public static final Identifier CRYSTAL_BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_crystal/end_crystal_beam.png");
/*  32 */   private static final Identifier DRAGON_EXPLODING_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_exploding.png");
/*  33 */   private static final Identifier DRAGON_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
/*  34 */   private static final Identifier DRAGON_EYES_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_eyes.png");
/*     */   
/*  36 */   private static final RenderType RENDER_TYPE = RenderTypes.entityCutoutNoCull(DRAGON_LOCATION);
/*  37 */   private static final RenderType DECAL = RenderTypes.entityDecal(DRAGON_LOCATION);
/*  38 */   private static final RenderType EYES = RenderTypes.eyes(DRAGON_EYES_LOCATION);
/*  39 */   private static final RenderType BEAM = RenderTypes.entitySmoothCutout(CRYSTAL_BEAM_LOCATION);
/*     */   
/*  41 */   private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);
/*     */   
/*     */   private final EnderDragonModel model;
/*     */   
/*     */   public EnderDragonRenderer(EntityRendererProvider.Context context) {
/*  46 */     super(context);
/*  47 */     this.shadowRadius = 0.5F;
/*     */     
/*  49 */     this.model = new EnderDragonModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.ENDER_DRAGON));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  54 */     poseStack.pushPose();
/*     */     
/*  56 */     float yr = state.getHistoricalPos(7).yRot();
/*  57 */     float rot2 = (float)(state.getHistoricalPos(5).y() - state.getHistoricalPos(10).y());
/*  58 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-yr));
/*  59 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(rot2 * 10.0F));
/*  60 */     poseStack.translate(0.0F, 0.0F, 1.0F);
/*     */ 
/*     */ 
/*     */     
/*  64 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*     */ 
/*     */     
/*  67 */     poseStack.translate(0.0F, -1.501F, 0.0F);
/*     */     
/*  69 */     int overlayCoords = OverlayTexture.pack(0.0F, state.hasRedOverlay);
/*  70 */     if (state.deathTime > 0.0F) {
/*     */       
/*  72 */       int color = ARGB.white(state.deathTime / 200.0F);
/*  73 */       submitNodeCollector.order(0).submitModel((Model)this.model, state, poseStack, RenderTypes.dragonExplosionAlpha(DRAGON_EXPLODING_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
/*  74 */       submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, DECAL, state.lightCoords, overlayCoords, -1, null, state.outlineColor, null);
/*     */     } else {
/*  76 */       submitNodeCollector.order(0).submitModel((Model)this.model, state, poseStack, RENDER_TYPE, state.lightCoords, overlayCoords, -1, null, state.outlineColor, null);
/*     */     } 
/*     */     
/*  79 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, EYES, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*     */     
/*  81 */     if (state.deathTime > 0.0F) {
/*  82 */       float deathTime = state.deathTime / 200.0F;
/*  83 */       poseStack.pushPose();
/*  84 */       poseStack.translate(0.0F, -1.0F, -2.0F);
/*  85 */       submitRays(poseStack, deathTime, submitNodeCollector, RenderTypes.dragonRays());
/*  86 */       submitRays(poseStack, deathTime, submitNodeCollector, RenderTypes.dragonRaysDepth());
/*  87 */       poseStack.popPose();
/*     */     } 
/*     */     
/*  90 */     poseStack.popPose();
/*     */     
/*  92 */     if (state.beamOffset != null) {
/*  93 */       submitCrystalBeams((float)state.beamOffset.x, (float)state.beamOffset.y, (float)state.beamOffset.z, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords);
/*     */     }
/*     */     
/*  96 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   private static void submitRays(PoseStack poseStack, float deathTime, SubmitNodeCollector submitNodeCollector, RenderType renderType) {
/* 100 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
/*     */           float overDrive = Math.min((deathTime > 0.8F) ? ((deathTime - 0.8F) / 0.2F) : 0.0F, 1.0F);
/*     */           int innerColor = ARGB.colorFromFloat(1.0F - overDrive, 1.0F, 1.0F, 1.0F), outerColor = 16711935;
/*     */           RandomSource random = RandomSource.create(432L);
/*     */           Vector3f origin = new Vector3f(), outerLeft = new Vector3f(), outerRight = new Vector3f(), outerBottom = new Vector3f();
/*     */           Quaternionf rayRotation = new Quaternionf();
/*     */           int rayCount = Mth.floor((deathTime + deathTime * deathTime) / 2.0F * 60.0F);
/*     */           for (int i = 0; i < rayCount; i++) {
/*     */             rayRotation.rotationXYZ(random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F).rotateXYZ(random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F, random.nextFloat() * 6.2831855F + deathTime * 1.5707964F);
/*     */             pose.rotate((Quaternionfc)rayRotation);
/*     */             float length = random.nextFloat() * 20.0F + 5.0F + overDrive * 10.0F, width = random.nextFloat() * 2.0F + 1.0F + overDrive * 2.0F;
/*     */             outerLeft.set(-HALF_SQRT_3 * width, length, -0.5F * width);
/*     */             outerRight.set(HALF_SQRT_3 * width, length, -0.5F * width);
/*     */             outerBottom.set(0.0F, length, width);
/*     */             buffer.addVertex(pose, origin).setColor(innerColor);
/*     */             buffer.addVertex(pose, outerLeft).setColor(16711935);
/*     */             buffer.addVertex(pose, outerRight).setColor(16711935);
/*     */             buffer.addVertex(pose, origin).setColor(innerColor);
/*     */             buffer.addVertex(pose, outerRight).setColor(16711935);
/*     */             buffer.addVertex(pose, outerBottom).setColor(16711935);
/*     */             buffer.addVertex(pose, origin).setColor(innerColor);
/*     */             buffer.addVertex(pose, outerBottom).setColor(16711935);
/*     */             buffer.addVertex(pose, outerLeft).setColor(16711935);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void submitCrystalBeams(float deltaX, float deltaY, float deltaZ, float timeInTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 149 */     float horizontalLength = Mth.sqrt(deltaX * deltaX + deltaZ * deltaZ);
/* 150 */     float length = Mth.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
/*     */     
/* 152 */     poseStack.pushPose();
/* 153 */     poseStack.translate(0.0F, 2.0F, 0.0F);
/* 154 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotation((float)-Math.atan2(deltaZ, deltaX) - 1.5707964F));
/* 155 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation((float)-Math.atan2(horizontalLength, deltaY) - 1.5707964F));
/*     */     
/* 157 */     float v0 = 0.0F - timeInTicks * 0.01F;
/* 158 */     float v1 = length / 32.0F - timeInTicks * 0.01F;
/*     */     
/* 160 */     submitNodeCollector.submitCustomGeometry(poseStack, BEAM, (pose, buffer) -> {
/*     */           int steps = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           float lastSin = 0.0F, lastCos = 0.75F, lastU = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           for (int i = 1; i <= 8; i++) {
/*     */             float sin = Mth.sin((i * 6.2831855F / 8.0F)) * 0.75F, cos = Mth.cos((i * 6.2831855F / 8.0F)) * 0.75F, u = i / 8.0F;
/*     */ 
/*     */ 
/*     */             
/*     */             buffer.addVertex(pose, lastSin * 0.2F, lastCos * 0.2F, 0.0F).setColor(-16777216).setUv(lastU, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */ 
/*     */ 
/*     */             
/*     */             buffer.addVertex(pose, lastSin, lastCos, length).setColor(-1).setUv(lastU, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */ 
/*     */ 
/*     */             
/*     */             buffer.addVertex(pose, sin, cos, length).setColor(-1).setUv(u, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */ 
/*     */ 
/*     */             
/*     */             buffer.addVertex(pose, sin * 0.2F, cos * 0.2F, 0.0F).setColor(-16777216).setUv(u, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, -1.0F, 0.0F);
/*     */ 
/*     */ 
/*     */             
/*     */             lastSin = sin;
/*     */ 
/*     */ 
/*     */             
/*     */             lastCos = cos;
/*     */ 
/*     */ 
/*     */             
/*     */             lastU = u;
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 206 */     poseStack.popPose();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnderDragonRenderState createRenderState() {
/* 211 */     return new EnderDragonRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(EnderDragon entity, EnderDragonRenderState state, float partialTicks) {
/* 216 */     super.extractRenderState(entity, state, partialTicks);
/* 217 */     state.flapTime = Mth.lerp(partialTicks, entity.oFlapTime, entity.flapTime);
/* 218 */     state.deathTime = (entity.dragonDeathTime > 0) ? (entity.dragonDeathTime + partialTicks) : 0.0F;
/* 219 */     state.hasRedOverlay = (entity.hurtTime > 0);
/*     */     
/* 221 */     EndCrystal nearestCrystal = entity.nearestCrystal;
/* 222 */     if (nearestCrystal != null) {
/* 223 */       Vec3 crystalPosition = nearestCrystal.getPosition(partialTicks)
/* 224 */         .add(0.0D, EndCrystalRenderer.getY(nearestCrystal.time + partialTicks), 0.0D);
/* 225 */       state.beamOffset = crystalPosition.subtract(entity.getPosition(partialTicks));
/*     */     } else {
/* 227 */       state.beamOffset = null;
/*     */     } 
/*     */     
/* 230 */     DragonPhaseInstance phase = entity.getPhaseManager().getCurrentPhase();
/* 231 */     state.isLandingOrTakingOff = (phase == EnderDragonPhase.LANDING || phase == EnderDragonPhase.TAKEOFF);
/* 232 */     state.isSitting = phase.isSitting();
/*     */     
/* 234 */     BlockPos egg = entity.level().getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(entity.getFightOrigin()));
/* 235 */     state.distanceToEgg = egg.distToCenterSqr((net.minecraft.core.Position)entity.position());
/*     */     
/* 237 */     state.partialTicks = entity.isDeadOrDying() ? 0.0F : partialTicks;
/* 238 */     state.flightHistory.copyFrom(entity.flightHistory);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean affectedByCulling(EnderDragon entity) {
/* 243 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EnderDragonRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */