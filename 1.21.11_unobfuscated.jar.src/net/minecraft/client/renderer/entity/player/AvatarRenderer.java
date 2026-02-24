/*     */ package net.minecraft.client.renderer.entity.player;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.entity.ClientAvatarEntity;
/*     */ import net.minecraft.client.entity.ClientAvatarState;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.player.PlayerModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.EntityRendererProvider;
/*     */ import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
/*     */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*     */ import net.minecraft.client.renderer.entity.layers.ArrowLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.WingsLayer;
/*     */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class AvatarRenderer<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends net.minecraft.client.renderer.entity.LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
/*     */   public AvatarRenderer(EntityRendererProvider.Context context, boolean slimSteve) {
/*  51 */     super(context, (EntityModel)new PlayerModel(context.bakeLayer(slimSteve ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimSteve), 0.5F);
/*     */     
/*  53 */     addLayer((RenderLayer)new HumanoidArmorLayer((RenderLayerParent)this, 
/*  54 */           ArmorModelSet.bake(slimSteve ? ModelLayers.PLAYER_SLIM_ARMOR : ModelLayers.PLAYER_ARMOR, context.getModelSet(), part -> new PlayerModel(part, slimSteve)), 
/*  55 */           context.getEquipmentRenderer()));
/*     */     
/*  57 */     addLayer((RenderLayer)new net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer((RenderLayerParent)this));
/*  58 */     addLayer((RenderLayer)new ArrowLayer(this, context));
/*  59 */     addLayer((RenderLayer)new Deadmau5EarsLayer((RenderLayerParent)this, context.getModelSet()));
/*  60 */     addLayer((RenderLayer)new net.minecraft.client.renderer.entity.layers.CapeLayer((RenderLayerParent)this, context.getModelSet(), context.getEquipmentAssets()));
/*  61 */     addLayer((RenderLayer)new net.minecraft.client.renderer.entity.layers.CustomHeadLayer((RenderLayerParent)this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/*  62 */     addLayer((RenderLayer)new WingsLayer((RenderLayerParent)this, context.getModelSet(), context.getEquipmentRenderer()));
/*  63 */     addLayer((RenderLayer)new ParrotOnShoulderLayer((RenderLayerParent)this, context.getModelSet()));
/*  64 */     addLayer((RenderLayer)new SpinAttackEffectLayer((RenderLayerParent)this, context.getModelSet()));
/*  65 */     addLayer((RenderLayer)new BeeStingerLayer(this, context));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldRenderLayers(AvatarRenderState state) {
/*  70 */     return !state.isSpectator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRenderOffset(AvatarRenderState state) {
/*  75 */     Vec3 offset = super.getRenderOffset((EntityRenderState)state);
/*  76 */     if (state.isCrouching) {
/*  77 */       return offset.add(0.0D, (state.scale * -2.0F) / 16.0D, 0.0D);
/*     */     }
/*     */     
/*  80 */     return offset;
/*     */   }
/*     */   
/*     */   private static HumanoidModel.ArmPose getArmPose(Avatar avatar, HumanoidArm arm) {
/*  84 */     ItemStack mainHandItem = avatar.getItemInHand(InteractionHand.MAIN_HAND);
/*  85 */     ItemStack offHandItem = avatar.getItemInHand(InteractionHand.OFF_HAND);
/*     */     
/*  87 */     HumanoidModel.ArmPose mainHandPose = getArmPose(avatar, mainHandItem, InteractionHand.MAIN_HAND);
/*  88 */     HumanoidModel.ArmPose offHandPose = getArmPose(avatar, offHandItem, InteractionHand.OFF_HAND);
/*     */ 
/*     */     
/*  91 */     if (mainHandPose.isTwoHanded()) {
/*  92 */       offHandPose = offHandItem.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
/*     */     }
/*     */     
/*  95 */     if (avatar.getMainArm() == arm) {
/*  96 */       return mainHandPose;
/*     */     }
/*  98 */     return offHandPose;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static HumanoidModel.ArmPose getArmPose(Avatar avatar, ItemStack itemInHand, InteractionHand hand) {
/* 104 */     if (itemInHand.isEmpty()) {
/* 105 */       return HumanoidModel.ArmPose.EMPTY;
/*     */     }
/*     */     
/* 108 */     if (!avatar.swinging && itemInHand.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemInHand)) {
/* 109 */       return HumanoidModel.ArmPose.CROSSBOW_HOLD;
/*     */     }
/*     */     
/* 112 */     if (avatar.getUsedItemHand() == hand && avatar.getUseItemRemainingTicks() > 0) {
/* 113 */       ItemUseAnimation anim = itemInHand.getUseAnimation();
/* 114 */       if (anim == ItemUseAnimation.BLOCK)
/* 115 */         return HumanoidModel.ArmPose.BLOCK; 
/* 116 */       if (anim == ItemUseAnimation.BOW)
/* 117 */         return HumanoidModel.ArmPose.BOW_AND_ARROW; 
/* 118 */       if (anim == ItemUseAnimation.TRIDENT)
/* 119 */         return HumanoidModel.ArmPose.THROW_TRIDENT; 
/* 120 */       if (anim == ItemUseAnimation.CROSSBOW)
/* 121 */         return HumanoidModel.ArmPose.CROSSBOW_CHARGE; 
/* 122 */       if (anim == ItemUseAnimation.SPYGLASS)
/* 123 */         return HumanoidModel.ArmPose.SPYGLASS; 
/* 124 */       if (anim == ItemUseAnimation.TOOT_HORN)
/* 125 */         return HumanoidModel.ArmPose.TOOT_HORN; 
/* 126 */       if (anim == ItemUseAnimation.BRUSH)
/* 127 */         return HumanoidModel.ArmPose.BRUSH; 
/* 128 */       if (anim == ItemUseAnimation.SPEAR) {
/* 129 */         return HumanoidModel.ArmPose.SPEAR;
/*     */       }
/*     */     } 
/* 132 */     SwingAnimation attack = (SwingAnimation)itemInHand.get(DataComponents.SWING_ANIMATION);
/* 133 */     if (attack != null && attack.type() == net.minecraft.world.item.SwingAnimationType.STAB && avatar.swinging) {
/* 134 */       return HumanoidModel.ArmPose.SPEAR;
/*     */     }
/* 136 */     if (itemInHand.is(ItemTags.SPEARS)) {
/* 137 */       return HumanoidModel.ArmPose.SPEAR;
/*     */     }
/* 139 */     return HumanoidModel.ArmPose.ITEM;
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getTextureLocation(AvatarRenderState state) {
/* 144 */     return state.skin.body().texturePath();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void scale(AvatarRenderState state, PoseStack poseStack) {
/* 149 */     float s = 0.9375F;
/* 150 */     poseStack.scale(0.9375F, 0.9375F, 0.9375F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void submitNameTag(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 155 */     poseStack.pushPose();
/* 156 */     int offset = state.showExtraEars ? -10 : 0;
/*     */     
/* 158 */     if (state.scoreText != null) {
/* 159 */       submitNodeCollector.submitNameTag(poseStack, state.nameTagAttachment, offset, state.scoreText, !state.isDiscrete, state.lightCoords, state.distanceToCameraSq, camera);
/* 160 */       java.util.Objects.requireNonNull(getFont()); poseStack.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
/*     */     } 
/* 162 */     if (state.nameTag != null) {
/* 163 */       submitNodeCollector.submitNameTag(poseStack, state.nameTagAttachment, offset, state.nameTag, !state.isDiscrete, state.lightCoords, state.distanceToCameraSq, camera);
/*     */     }
/* 165 */     poseStack.popPose();
/*     */   }
/*     */ 
/*     */   
/*     */   public AvatarRenderState createRenderState() {
/* 170 */     return new AvatarRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks) {
/* 175 */     super.extractRenderState((LivingEntity)entity, (LivingEntityRenderState)state, partialTicks);
/* 176 */     HumanoidMobRenderer.extractHumanoidRenderState((LivingEntity)entity, (net.minecraft.client.renderer.entity.state.HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/* 177 */     state.leftArmPose = getArmPose((Avatar)entity, HumanoidArm.LEFT);
/* 178 */     state.rightArmPose = getArmPose((Avatar)entity, HumanoidArm.RIGHT);
/* 179 */     state.skin = ((ClientAvatarEntity)entity).getSkin();
/* 180 */     state.arrowCount = entity.getArrowCount();
/* 181 */     state.stingerCount = entity.getStingerCount();
/* 182 */     state.isSpectator = entity.isSpectator();
/* 183 */     state.showHat = entity.isModelPartShown(PlayerModelPart.HAT);
/* 184 */     state.showJacket = entity.isModelPartShown(PlayerModelPart.JACKET);
/* 185 */     state.showLeftPants = entity.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
/* 186 */     state.showRightPants = entity.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
/* 187 */     state.showLeftSleeve = entity.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
/* 188 */     state.showRightSleeve = entity.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
/* 189 */     state.showCape = entity.isModelPartShown(PlayerModelPart.CAPE);
/* 190 */     extractFlightData(entity, state, partialTicks);
/* 191 */     extractCapeState(entity, state, partialTicks);
/*     */     
/* 193 */     if (state.distanceToCameraSq < 100.0D) {
/* 194 */       state.scoreText = ((ClientAvatarEntity)entity).belowNameDisplay();
/*     */     } else {
/* 196 */       state.scoreText = null;
/*     */     } 
/*     */     
/* 199 */     state.parrotOnLeftShoulder = ((ClientAvatarEntity)entity).getParrotVariantOnShoulder(true);
/* 200 */     state.parrotOnRightShoulder = ((ClientAvatarEntity)entity).getParrotVariantOnShoulder(false);
/* 201 */     state.id = entity.getId();
/* 202 */     state.showExtraEars = ((ClientAvatarEntity)entity).showExtraEars();
/*     */     
/* 204 */     state.heldOnHead.clear();
/* 205 */     if (state.isUsingItem) {
/* 206 */       ItemStack useItem = entity.getItemInHand(state.useItemHand);
/* 207 */       if (useItem.is(Items.SPYGLASS)) {
/* 208 */         this.itemModelResolver.updateForLiving(state.heldOnHead, useItem, net.minecraft.world.item.ItemDisplayContext.HEAD, (LivingEntity)entity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldShowName(AvatarlikeEntity entity, double distanceToCameraSq) {
/* 215 */     return (super.shouldShowName((LivingEntity)entity, distanceToCameraSq) && (entity.shouldShowName() || (entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity)));
/*     */   }
/*     */   
/*     */   private void extractFlightData(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks) {
/* 219 */     state.fallFlyingTimeInTicks = entity.getFallFlyingTicks() + partialTicks;
/* 220 */     Vec3 lookAngle = entity.getViewVector(partialTicks);
/* 221 */     Vec3 movement = ((ClientAvatarEntity)entity).avatarState().deltaMovementOnPreviousTick().lerp(entity.getDeltaMovement(), partialTicks);
/* 222 */     if (movement.horizontalDistanceSqr() > 9.999999747378752E-6D && lookAngle.horizontalDistanceSqr() > 9.999999747378752E-6D) {
/* 223 */       state.shouldApplyFlyingYRot = true;
/* 224 */       double dot = movement.horizontal().normalize().dot(lookAngle.horizontal().normalize());
/* 225 */       double sign = movement.x * lookAngle.z - movement.z * lookAngle.x;
/* 226 */       state.flyingYRot = (float)(Math.signum(sign) * Math.acos(Math.min(1.0D, Math.abs(dot))));
/*     */     } else {
/* 228 */       state.shouldApplyFlyingYRot = false;
/* 229 */       state.flyingYRot = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractCapeState(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks) {
/* 234 */     ClientAvatarState clientState = ((ClientAvatarEntity)entity).avatarState();
/* 235 */     double deltaX = clientState.getInterpolatedCloakX(partialTicks) - Mth.lerp(partialTicks, ((Avatar)entity).xo, entity.getX());
/* 236 */     double deltaY = clientState.getInterpolatedCloakY(partialTicks) - Mth.lerp(partialTicks, ((Avatar)entity).yo, entity.getY());
/* 237 */     double deltaZ = clientState.getInterpolatedCloakZ(partialTicks) - Mth.lerp(partialTicks, ((Avatar)entity).zo, entity.getZ());
/*     */     
/* 239 */     float yBodyRot = Mth.rotLerp(partialTicks, ((Avatar)entity).yBodyRotO, ((Avatar)entity).yBodyRot);
/*     */     
/* 241 */     double forwardX = Mth.sin((yBodyRot * 0.017453292F));
/* 242 */     double forwardZ = -Mth.cos((yBodyRot * 0.017453292F));
/*     */     
/* 244 */     state.capeFlap = (float)deltaY * 10.0F;
/* 245 */     state.capeFlap = Mth.clamp(state.capeFlap, -6.0F, 32.0F);
/* 246 */     state.capeLean = (float)(deltaX * forwardX + deltaZ * forwardZ) * 100.0F;
/* 247 */     state.capeLean *= 1.0F - state.fallFlyingScale();
/* 248 */     state.capeLean = Mth.clamp(state.capeLean, 0.0F, 150.0F);
/* 249 */     state.capeLean2 = (float)(deltaX * forwardZ - deltaZ * forwardX) * 100.0F;
/* 250 */     state.capeLean2 = Mth.clamp(state.capeLean2, -20.0F, 20.0F);
/*     */     
/* 252 */     float pow = clientState.getInterpolatedBob(partialTicks);
/* 253 */     float walkDistance = clientState.getInterpolatedWalkDistance(partialTicks);
/* 254 */     state.capeFlap += Mth.sin((walkDistance * 6.0F)) * 32.0F * pow;
/*     */   }
/*     */   
/*     */   public void renderRightHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve) {
/* 258 */     renderHand(poseStack, submitNodeCollector, lightCoords, skinTexture, ((PlayerModel)this.model).rightArm, hasSleeve);
/*     */   }
/*     */   
/*     */   public void renderLeftHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve) {
/* 262 */     renderHand(poseStack, submitNodeCollector, lightCoords, skinTexture, ((PlayerModel)this.model).leftArm, hasSleeve);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, ModelPart arm, boolean hasSleeve) {
/* 268 */     PlayerModel model = (PlayerModel)getModel();
/* 269 */     arm.resetPose();
/* 270 */     arm.visible = true;
/* 271 */     model.leftSleeve.visible = hasSleeve;
/* 272 */     model.rightSleeve.visible = hasSleeve;
/* 273 */     model.leftArm.zRot = -0.1F;
/* 274 */     model.rightArm.zRot = 0.1F;
/* 275 */     submitNodeCollector.submitModelPart(arm, poseStack, RenderTypes.entityTranslucent(skinTexture), lightCoords, OverlayTexture.NO_OVERLAY, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupRotations(AvatarRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 280 */     float swimAmount = state.swimAmount;
/* 281 */     float xRot = state.xRot;
/*     */     
/* 283 */     if (state.isFallFlying) {
/* 284 */       super.setupRotations((LivingEntityRenderState)state, poseStack, bodyRot, entityScale);
/*     */       
/* 286 */       float scale = state.fallFlyingScale();
/*     */       
/* 288 */       if (!state.isAutoSpinAttack) {
/* 289 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(scale * (-90.0F - xRot)));
/*     */       }
/*     */       
/* 292 */       if (state.shouldApplyFlyingYRot) {
/* 293 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotation(state.flyingYRot));
/*     */       }
/* 295 */     } else if (swimAmount > 0.0F) {
/* 296 */       super.setupRotations((LivingEntityRenderState)state, poseStack, bodyRot, entityScale);
/*     */ 
/*     */ 
/*     */       
/* 300 */       float targetXRot = state.isInWater ? (-90.0F - xRot) : -90.0F;
/*     */ 
/*     */       
/* 303 */       float xAngle = Mth.lerp(swimAmount, 0.0F, targetXRot);
/* 304 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xAngle));
/*     */       
/* 306 */       if (state.isVisuallySwimming)
/*     */       {
/* 308 */         poseStack.translate(0.0F, -1.0F, 0.3F);
/*     */       }
/*     */     } else {
/* 311 */       super.setupRotations((LivingEntityRenderState)state, poseStack, bodyRot, entityScale);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEntityUpsideDown(AvatarlikeEntity mob) {
/* 318 */     if (mob.isModelPartShown(PlayerModelPart.CAPE)) {
/* 319 */       if (mob instanceof Player) { Player player = (Player)mob;
/* 320 */         return isPlayerUpsideDown(player); }
/*     */       
/* 322 */       return super.isEntityUpsideDown((LivingEntity)mob);
/*     */     } 
/*     */     
/* 325 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isPlayerUpsideDown(Player player) {
/* 329 */     return isUpsideDownName(player.getGameProfile().name());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/player/AvatarRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */