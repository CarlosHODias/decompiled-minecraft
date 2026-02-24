/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.FishingHook;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class FishingHookRenderer extends EntityRenderer<FishingHook, FishingHookRenderState> {
/*  23 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/fishing_hook.png");
/*  24 */   private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(TEXTURE_LOCATION);
/*     */   
/*     */   private static final double VIEW_BOBBING_SCALE = 960.0D;
/*     */   
/*     */   public FishingHookRenderer(EntityRendererProvider.Context context) {
/*  29 */     super(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(FishingHook entity, Frustum culler, double camX, double camY, double camZ) {
/*  34 */     return (super.shouldRender(entity, culler, camX, camY, camZ) && entity.getPlayerOwner() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(FishingHookRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  39 */     poseStack.pushPose();
/*     */     
/*  41 */     poseStack.pushPose();
/*  42 */     poseStack.scale(0.5F, 0.5F, 0.5F);
/*     */     
/*  44 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/*     */     
/*  46 */     submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
/*     */           vertex(buffer, pose, state.lightCoords, 0.0F, 0, 0, 1);
/*     */           
/*     */           vertex(buffer, pose, state.lightCoords, 1.0F, 0, 1, 1);
/*     */           vertex(buffer, pose, state.lightCoords, 1.0F, 1, 1, 0);
/*     */           vertex(buffer, pose, state.lightCoords, 0.0F, 1, 0, 0);
/*     */         });
/*  53 */     poseStack.popPose();
/*     */     
/*  55 */     float xa = (float)state.lineOriginOffset.x;
/*  56 */     float ya = (float)state.lineOriginOffset.y;
/*  57 */     float za = (float)state.lineOriginOffset.z;
/*  58 */     float width = Minecraft.getInstance().getWindow().getAppropriateLineWidth();
/*     */     
/*  60 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
/*     */           int steps = 16;
/*     */           
/*     */           for (int i = 0; i < 16; i++) {
/*     */             float a0 = fraction(i, 16), a1 = fraction(i + 1, 16);
/*     */             
/*     */             stringVertex(xa, ya, za, buffer, pose, a0, a1, width);
/*     */             stringVertex(xa, ya, za, buffer, pose, a1, a0, width);
/*     */           } 
/*     */         });
/*  70 */     poseStack.popPose();
/*  71 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HumanoidArm getHoldingArm(Player owner) {
/*  81 */     return (owner.getMainHandItem().getItem() instanceof net.minecraft.world.item.FishingRodItem) ? 
/*  82 */       owner.getMainArm() : 
/*  83 */       owner.getMainArm().getOpposite();
/*     */   }
/*     */   
/*     */   private Vec3 getPlayerHandPos(Player owner, float swing, float partialTicks) {
/*  87 */     int invert = (getHoldingArm(owner) == HumanoidArm.RIGHT) ? 1 : -1;
/*     */     
/*  89 */     if (!this.entityRenderDispatcher.options.getCameraType().isFirstPerson() || owner != (Minecraft.getInstance()).player) {
/*  90 */       float ownerYRot = Mth.lerp(partialTicks, owner.yBodyRotO, owner.yBodyRot) * 0.017453292F;
/*  91 */       double sin = Mth.sin(ownerYRot);
/*  92 */       double cos = Mth.cos(ownerYRot);
/*  93 */       float playerScale = owner.getScale();
/*  94 */       double rightOffset = invert * 0.35D * playerScale;
/*  95 */       double forwardOffset = 0.8D * playerScale;
/*     */ 
/*     */       
/*  98 */       float yOffset = owner.isCrouching() ? -0.1875F : 0.0F;
/*     */ 
/*     */       
/* 101 */       return owner.getEyePosition(partialTicks).add(-cos * rightOffset - sin * forwardOffset, yOffset - 0.45D * playerScale, -sin * rightOffset + cos * forwardOffset);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     double viewBobbingScale = 960.0D / (Integer)this.entityRenderDispatcher.options.fov().get();
/*     */     
/* 110 */     Vec3 viewVec = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane(invert * 0.525F, -0.1F)
/* 111 */       .scale(viewBobbingScale)
/* 112 */       .yRot(swing * 0.5F)
/* 113 */       .xRot(-swing * 0.7F);
/*     */     
/* 115 */     return owner.getEyePosition(partialTicks).add(viewVec);
/*     */   }
/*     */ 
/*     */   
/*     */   private static float fraction(int i, int steps) {
/* 120 */     return i / steps;
/*     */   }
/*     */   
/*     */   private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int lightCoords, float x, int y, int u, int v) {
/* 124 */     builder.addVertex(pose, x - 0.5F, y - 0.5F, 0.0F).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */   
/*     */   private static void stringVertex(float xa, float ya, float za, VertexConsumer stringBuffer, PoseStack.Pose stringPose, float aa, float nexta, float width) {
/* 128 */     float x = xa * aa;
/* 129 */     float y = ya * (aa * aa + aa) * 0.5F + 0.25F;
/* 130 */     float z = za * aa;
/*     */     
/* 132 */     float nx = xa * nexta - x;
/* 133 */     float ny = ya * (nexta * nexta + nexta) * 0.5F + 0.25F - y;
/* 134 */     float nz = za * nexta - z;
/* 135 */     float length = Mth.sqrt(nx * nx + ny * ny + nz * nz);
/* 136 */     nx /= length;
/* 137 */     ny /= length;
/* 138 */     nz /= length;
/*     */     
/* 140 */     stringBuffer.addVertex(stringPose, x, y, z).setColor(-16777216).setNormal(stringPose, nx, ny, nz).setLineWidth(width);
/*     */   }
/*     */ 
/*     */   
/*     */   public FishingHookRenderState createRenderState() {
/* 145 */     return new FishingHookRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(FishingHook entity, FishingHookRenderState state, float partialTicks) {
/* 150 */     super.extractRenderState(entity, state, partialTicks);
/*     */     
/* 152 */     Player owner = entity.getPlayerOwner();
/* 153 */     if (owner == null) {
/* 154 */       state.lineOriginOffset = Vec3.ZERO;
/*     */       
/*     */       return;
/*     */     } 
/* 158 */     float swing = owner.getAttackAnim(partialTicks);
/* 159 */     float swing2 = Mth.sin((Mth.sqrt(swing) * 3.1415927F));
/*     */     
/* 161 */     Vec3 playerPos = getPlayerHandPos(owner, swing2, partialTicks);
/* 162 */     Vec3 hookPos = entity.getPosition(partialTicks).add(0.0D, 0.25D, 0.0D);
/* 163 */     state.lineOriginOffset = playerPos.subtract(hookPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean affectedByCulling(FishingHook entity) {
/* 168 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/FishingHookRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */