/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*     */ import net.minecraft.client.model.object.cart.MinecartModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.MinecartRenderState;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
/*     */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*     */ import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public abstract class AbstractMinecartRenderer<T extends AbstractMinecart, S extends MinecartRenderState> extends EntityRenderer<T, S> {
/*  24 */   private static final Identifier MINECART_LOCATION = Identifier.withDefaultNamespace("textures/entity/minecart.png");
/*     */   
/*     */   private static final float DISPLAY_BLOCK_SCALE = 0.75F;
/*     */   protected final MinecartModel model;
/*     */   
/*     */   public AbstractMinecartRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
/*  30 */     super(context);
/*  31 */     this.shadowRadius = 0.7F;
/*  32 */     this.model = new MinecartModel(context.bakeLayer(model));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  37 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */     
/*  39 */     poseStack.pushPose();
/*     */     
/*  41 */     long seed = ((MinecartRenderState)state).offsetSeed;
/*     */     
/*  43 */     float offsetX = (((float)(seed >> 16L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  44 */     float offsetY = (((float)(seed >> 20L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*  45 */     float offsetZ = (((float)(seed >> 24L & 0x7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
/*     */     
/*  47 */     poseStack.translate(offsetX, offsetY, offsetZ);
/*     */     
/*  49 */     if (((MinecartRenderState)state).isNewRender) {
/*  50 */       newRender(state, poseStack);
/*     */     } else {
/*  52 */       oldRender(state, poseStack);
/*     */     } 
/*     */     
/*  55 */     float hurt = ((MinecartRenderState)state).hurtTime;
/*  56 */     if (hurt > 0.0F) {
/*  57 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(net.minecraft.util.Mth.sin(hurt) * hurt * ((MinecartRenderState)state).damageTime / 10.0F * ((MinecartRenderState)state).hurtDir));
/*     */     }
/*     */     
/*  60 */     BlockState blockState = ((MinecartRenderState)state).displayBlockState;
/*  61 */     if (blockState.getRenderShape() != net.minecraft.world.level.block.RenderShape.INVISIBLE) {
/*  62 */       poseStack.pushPose();
/*     */       
/*  64 */       poseStack.scale(0.75F, 0.75F, 0.75F);
/*  65 */       poseStack.translate(-0.5F, (((MinecartRenderState)state).displayOffset - 8) / 16.0F, 0.5F);
/*  66 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/*  67 */       submitMinecartContents(state, blockState, poseStack, submitNodeCollector, ((MinecartRenderState)state).lightCoords);
/*     */       
/*  69 */       poseStack.popPose();
/*     */     } 
/*     */     
/*  72 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*  73 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, state, poseStack, this.model.renderType(MINECART_LOCATION), ((MinecartRenderState)state).lightCoords, OverlayTexture.NO_OVERLAY, ((MinecartRenderState)state).outlineColor, null);
/*  74 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private static <S extends MinecartRenderState> void newRender(S state, PoseStack poseStack) {
/*  78 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((MinecartRenderState)state).yRot));
/*  79 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(-((MinecartRenderState)state).xRot));
/*     */     
/*  81 */     poseStack.translate(0.0F, 0.375F, 0.0F);
/*     */   }
/*     */   
/*     */   private static <S extends MinecartRenderState> void oldRender(S state, PoseStack poseStack) {
/*  85 */     double entityX = ((MinecartRenderState)state).x;
/*  86 */     double entityY = ((MinecartRenderState)state).y;
/*  87 */     double entityZ = ((MinecartRenderState)state).z;
/*     */     
/*  89 */     float xRot = ((MinecartRenderState)state).xRot;
/*  90 */     float rotation = ((MinecartRenderState)state).yRot;
/*     */     
/*  92 */     if (((MinecartRenderState)state).posOnRail != null && ((MinecartRenderState)state).frontPos != null && ((MinecartRenderState)state).backPos != null) {
/*  93 */       Vec3 frontPos = ((MinecartRenderState)state).frontPos;
/*  94 */       Vec3 backPos = ((MinecartRenderState)state).backPos;
/*  95 */       poseStack.translate(((MinecartRenderState)state).posOnRail.x - entityX, (frontPos.y + backPos.y) / 2.0D - entityY, ((MinecartRenderState)state).posOnRail.z - entityZ);
/*     */       
/*  97 */       Vec3 direction = backPos.add(-frontPos.x, -frontPos.y, -frontPos.z);
/*  98 */       if (direction.length() != 0.0D) {
/*  99 */         direction = direction.normalize();
/* 100 */         rotation = (float)(Math.atan2(direction.z, direction.x) * 180.0D / Math.PI);
/* 101 */         xRot = (float)(Math.atan(direction.y) * 73.0D);
/*     */       } 
/*     */     } 
/* 104 */     poseStack.translate(0.0F, 0.375F, 0.0F);
/*     */     
/* 106 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - rotation));
/* 107 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(-xRot));
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 112 */     super.extractRenderState(entity, state, partialTicks);
/*     */     
/* 114 */     MinecartBehavior minecartBehavior = entity.getBehavior(); if (minecartBehavior instanceof NewMinecartBehavior) { NewMinecartBehavior behavior = (NewMinecartBehavior)minecartBehavior;
/* 115 */       newExtractState(entity, behavior, state, partialTicks);
/* 116 */       ((MinecartRenderState)state).isNewRender = true; }
/* 117 */     else { minecartBehavior = entity.getBehavior(); if (minecartBehavior instanceof OldMinecartBehavior) { OldMinecartBehavior behavior = (OldMinecartBehavior)minecartBehavior;
/* 118 */         oldExtractState(entity, behavior, state, partialTicks);
/* 119 */         ((MinecartRenderState)state).isNewRender = false; }
/*     */        }
/*     */     
/* 122 */     long seed = entity.getId() * 493286711L;
/* 123 */     ((MinecartRenderState)state).offsetSeed = seed * seed * 4392167121L + seed * 98761L;
/*     */     
/* 125 */     ((MinecartRenderState)state).hurtTime = entity.getHurtTime() - partialTicks;
/* 126 */     ((MinecartRenderState)state).hurtDir = entity.getHurtDir();
/* 127 */     ((MinecartRenderState)state).damageTime = Math.max(entity.getDamage() - partialTicks, 0.0F);
/*     */     
/* 129 */     ((MinecartRenderState)state).displayOffset = entity.getDisplayOffset();
/* 130 */     ((MinecartRenderState)state).displayBlockState = entity.getDisplayBlockState();
/*     */   }
/*     */   
/*     */   private static <T extends AbstractMinecart, S extends MinecartRenderState> void newExtractState(T entity, NewMinecartBehavior behavior, S state, float partialTicks) {
/* 134 */     if (behavior.cartHasPosRotLerp()) {
/* 135 */       ((MinecartRenderState)state).renderPos = behavior.getCartLerpPosition(partialTicks);
/* 136 */       ((MinecartRenderState)state).xRot = behavior.getCartLerpXRot(partialTicks);
/* 137 */       ((MinecartRenderState)state).yRot = behavior.getCartLerpYRot(partialTicks);
/*     */     } else {
/* 139 */       ((MinecartRenderState)state).renderPos = null;
/* 140 */       ((MinecartRenderState)state).xRot = entity.getXRot();
/* 141 */       ((MinecartRenderState)state).yRot = entity.getYRot();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends AbstractMinecart, S extends MinecartRenderState> void oldExtractState(T entity, OldMinecartBehavior behavior, S state, float partialTicks) {
/* 146 */     float HALF_LENGTH = 0.3F;
/* 147 */     ((MinecartRenderState)state).xRot = entity.getXRot(partialTicks);
/* 148 */     ((MinecartRenderState)state).yRot = entity.getYRot(partialTicks);
/*     */     
/* 150 */     double entityX = ((MinecartRenderState)state).x;
/* 151 */     double entityY = ((MinecartRenderState)state).y;
/* 152 */     double entityZ = ((MinecartRenderState)state).z;
/* 153 */     Vec3 pos = behavior.getPos(entityX, entityY, entityZ);
/* 154 */     if (pos != null) {
/* 155 */       ((MinecartRenderState)state).posOnRail = pos;
/* 156 */       Vec3 p0 = behavior.getPosOffs(entityX, entityY, entityZ, 0.30000001192092896D);
/* 157 */       Vec3 p1 = behavior.getPosOffs(entityX, entityY, entityZ, -0.30000001192092896D);
/* 158 */       ((MinecartRenderState)state).frontPos = Objects.<Vec3>requireNonNullElse(p0, pos);
/* 159 */       ((MinecartRenderState)state).backPos = Objects.<Vec3>requireNonNullElse(p1, pos);
/*     */     } else {
/* 161 */       ((MinecartRenderState)state).posOnRail = null;
/* 162 */       ((MinecartRenderState)state).frontPos = null;
/* 163 */       ((MinecartRenderState)state).backPos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void submitMinecartContents(S state, BlockState blockState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 168 */     submitNodeCollector.submitBlock(poseStack, blockState, lightCoords, OverlayTexture.NO_OVERLAY, ((MinecartRenderState)state).outlineColor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/* 173 */     AABB aabb = super.getBoundingBoxForCulling(entity);
/* 174 */     if (!entity.getDisplayBlockState().isAir()) {
/* 175 */       return aabb.expandTowards(0.0D, (entity.getDisplayOffset() * 0.75F / 16.0F), 0.0D);
/*     */     }
/* 177 */     return aabb;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRenderOffset(S state) {
/* 182 */     Vec3 offset = super.getRenderOffset(state);
/* 183 */     if (((MinecartRenderState)state).isNewRender && ((MinecartRenderState)state).renderPos != null) {
/* 184 */       return offset.add(((MinecartRenderState)state).renderPos.x - ((MinecartRenderState)state).x, ((MinecartRenderState)state).renderPos.y - ((MinecartRenderState)state).y, ((MinecartRenderState)state).renderPos.z - ((MinecartRenderState)state).z);
/*     */     }
/* 186 */     return offset;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AbstractMinecartRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */