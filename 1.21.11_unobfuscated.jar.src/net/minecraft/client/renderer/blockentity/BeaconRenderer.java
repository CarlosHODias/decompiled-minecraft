/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.entity.BeaconBeamOwner;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class BeaconRenderer<T extends BlockEntity & BeaconBeamOwner> implements BlockEntityRenderer<T, BeaconRenderState> {
/*  24 */   public static final Identifier BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/beacon_beam.png");
/*     */   
/*     */   public static final int MAX_RENDER_Y = 2048;
/*     */   private static final float BEAM_SCALE_THRESHOLD = 96.0F;
/*     */   public static final float SOLID_BEAM_RADIUS = 0.2F;
/*     */   public static final float BEAM_GLOW_RADIUS = 0.25F;
/*     */   
/*     */   public BeaconRenderState createRenderState() {
/*  32 */     return new BeaconRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(T blockEntity, BeaconRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  37 */     super.extractRenderState((BlockEntity)blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  38 */     extract(blockEntity, state, partialTicks, cameraPosition);
/*     */   }
/*     */   
/*     */   public static <T extends BlockEntity & BeaconBeamOwner> void extract(T blockEntity, BeaconRenderState state, float partialTicks, Vec3 cameraPosition) {
/*  42 */     state.animationTime = (blockEntity.getLevel() != null) ? (Math.floorMod(blockEntity.getLevel().getGameTime(), 40) + partialTicks) : 0.0F;
/*  43 */     state.sections = ((BeaconBeamOwner)blockEntity).getBeamSections().stream().map(section -> new BeaconRenderState.Section(section.getColor(), section.getHeight())).toList();
/*     */     
/*  45 */     float distanceToBeacon = (float)cameraPosition.subtract(state.blockPos.getCenter()).horizontalDistance();
/*     */     
/*  47 */     LocalPlayer player = (Minecraft.getInstance()).player;
/*  48 */     state.beamRadiusScale = (player != null && player.isScoping()) ? 1.0F : Math.max(1.0F, distanceToBeacon / 96.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(BeaconRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  53 */     int beamStart = 0;
/*  54 */     for (int i = 0; i < state.sections.size(); i++) {
/*  55 */       BeaconRenderState.Section beamSection = state.sections.get(i);
/*  56 */       submitBeaconBeam(poseStack, submitNodeCollector, state.beamRadiusScale, state.animationTime, beamStart, (i == state.sections.size() - 1) ? 2048 : beamSection.height(), beamSection.color());
/*  57 */       beamStart += beamSection.height();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void submitBeaconBeam(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float beamRadiusScale, float animationTime, int beamStart, int height, int color) {
/*  62 */     submitBeaconBeam(poseStack, submitNodeCollector, BEAM_LOCATION, 1.0F, animationTime, beamStart, height, color, 0.2F * beamRadiusScale, 0.25F * beamRadiusScale);
/*     */   }
/*     */   
/*     */   public static void submitBeaconBeam(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Identifier beamLocation, float scale, float animationTime, int beamStart, int height, int color, float solidBeamRadius, float beamGlowRadius) {
/*  66 */     int beamEnd = beamStart + height;
/*     */     
/*  68 */     poseStack.pushPose();
/*  69 */     poseStack.translate(0.5D, 0.0D, 0.5D);
/*     */     
/*  71 */     float scroll = (height < 0) ? animationTime : -animationTime;
/*  72 */     float texVOff = Mth.frac(scroll * 0.2F - Mth.floor(scroll * 0.1F));
/*     */     
/*  74 */     poseStack.pushPose();
/*     */     
/*  76 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(animationTime * 2.25F - 45.0F));
/*     */     
/*  78 */     float wnx = 0.0F;
/*  79 */     float wnz = solidBeamRadius;
/*  80 */     float enx = solidBeamRadius;
/*  81 */     float enz = 0.0F;
/*     */     
/*  83 */     float wsx = -solidBeamRadius;
/*  84 */     float wsz = 0.0F;
/*  85 */     float esx = 0.0F;
/*  86 */     float esz = -solidBeamRadius;
/*     */     
/*  88 */     float uu1 = 0.0F;
/*  89 */     float uu2 = 1.0F;
/*  90 */     float vv2 = -1.0F + texVOff;
/*  91 */     float vv1 = height * scale * 0.5F / solidBeamRadius + vv2;
/*     */     
/*  93 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.beaconBeam(beamLocation, false), (pose, buffer) -> renderPart(pose, buffer, color, beamStart, beamEnd, 0.0F, wnz, enx, 0.0F, wsx, 0.0F, 0.0F, esz, 0.0F, 1.0F, vv1, vv2));
/*     */ 
/*     */ 
/*     */     
/*  97 */     poseStack.popPose();
/*     */     
/*  99 */     wnx = -beamGlowRadius;
/* 100 */     wnz = -beamGlowRadius;
/* 101 */     enx = beamGlowRadius;
/* 102 */     enz = -beamGlowRadius;
/*     */     
/* 104 */     wsx = -beamGlowRadius;
/* 105 */     wsz = beamGlowRadius;
/* 106 */     esx = beamGlowRadius;
/* 107 */     esz = beamGlowRadius;
/*     */     
/* 109 */     uu1 = 0.0F;
/* 110 */     uu2 = 1.0F;
/* 111 */     vv2 = -1.0F + texVOff;
/* 112 */     vv1 = height * scale + vv2;
/*     */     
/* 114 */     submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.beaconBeam(beamLocation, true), (pose, buffer) -> renderPart(pose, buffer, ARGB.color(32, color), beamStart, beamEnd, wnx, wnz, enx, enz, wsx, wsz, esx, esz, 0.0F, 1.0F, vv1, vv2));
/*     */ 
/*     */ 
/*     */     
/* 118 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private static void renderPart(PoseStack.Pose pose, VertexConsumer builder, int color, int beamStart, int beamEnd, float wnx, float wnz, float enx, float enz, float wsx, float wsz, float esx, float esz, float uu1, float uu2, float vv1, float vv2) {
/* 122 */     renderQuad(pose, builder, color, beamStart, beamEnd, wnx, wnz, enx, enz, uu1, uu2, vv1, vv2);
/* 123 */     renderQuad(pose, builder, color, beamStart, beamEnd, esx, esz, wsx, wsz, uu1, uu2, vv1, vv2);
/* 124 */     renderQuad(pose, builder, color, beamStart, beamEnd, enx, enz, esx, esz, uu1, uu2, vv1, vv2);
/* 125 */     renderQuad(pose, builder, color, beamStart, beamEnd, wsx, wsz, wnx, wnz, uu1, uu2, vv1, vv2);
/*     */   }
/*     */   
/*     */   private static void renderQuad(PoseStack.Pose pose, VertexConsumer builder, int color, int beamStart, int beamEnd, float wnx, float wnz, float enx, float enz, float uu1, float uu2, float vv1, float vv2) {
/* 129 */     addVertex(pose, builder, color, beamEnd, wnx, wnz, uu2, vv1);
/* 130 */     addVertex(pose, builder, color, beamStart, wnx, wnz, uu2, vv2);
/* 131 */     addVertex(pose, builder, color, beamStart, enx, enz, uu1, vv2);
/* 132 */     addVertex(pose, builder, color, beamEnd, enx, enz, uu1, vv1);
/*     */   }
/*     */   
/*     */   private static void addVertex(PoseStack.Pose pose, VertexConsumer builder, int color, int y, float x, float z, float u, float v) {
/* 136 */     builder.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderOffScreen() {
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getViewDistance() {
/* 146 */     return (Minecraft.getInstance()).options.getEffectiveRenderDistance() * 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(T blockEntity, Vec3 cameraPosition) {
/* 151 */     return Vec3.atCenterOf((Vec3i)blockEntity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan((Position)cameraPosition.multiply(1.0D, 0.0D, 1.0D), getViewDistance());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BeaconRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */