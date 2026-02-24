/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.effects.SpearAnimations;
/*     */ import net.minecraft.client.player.AbstractClientPlayer;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ItemInHandRenderer
/*     */ {
/*  44 */   private static final RenderType MAP_BACKGROUND = RenderTypes.text(Identifier.withDefaultNamespace("textures/map/map_background.png"));
/*  45 */   private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderTypes.text(Identifier.withDefaultNamespace("textures/map/map_background_checkerboard.png"));
/*     */   
/*     */   private static final float ITEM_SWING_X_POS_SCALE = -0.4F;
/*     */   
/*     */   private static final float ITEM_SWING_Y_POS_SCALE = 0.2F;
/*     */   
/*     */   private static final float ITEM_SWING_Z_POS_SCALE = -0.2F;
/*     */   
/*     */   private static final float ITEM_HEIGHT_SCALE = -0.6F;
/*     */   
/*     */   private static final float ITEM_POS_X = 0.56F;
/*     */   
/*     */   private static final float ITEM_POS_Y = -0.52F;
/*     */   
/*     */   private static final float ITEM_POS_Z = -0.72F;
/*     */   
/*     */   private static final float ITEM_PRESWING_ROT_Y = 45.0F;
/*     */   
/*     */   private static final float ITEM_SWING_X_ROT_AMOUNT = -80.0F;
/*     */   
/*     */   private static final float ITEM_SWING_Y_ROT_AMOUNT = -20.0F;
/*     */   
/*     */   private static final float ITEM_SWING_Z_ROT_AMOUNT = -20.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_X_ROT_AMOUNT = 10.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Y_ROT_AMOUNT = 90.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Z_ROT_AMOUNT = 30.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_X_POS_SCALE = 0.6F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Y_POS_SCALE = -0.5F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Z_POS_SCALE = 0.0F;
/*     */   
/*     */   private static final double EAT_JIGGLE_EXPONENT = 27.0D;
/*     */   
/*     */   private static final float EAT_EXTRA_JIGGLE_CUTOFF = 0.8F;
/*     */   
/*     */   private static final float EAT_EXTRA_JIGGLE_SCALE = 0.1F;
/*     */   
/*     */   private static final float ARM_SWING_X_POS_SCALE = -0.3F;
/*     */   
/*     */   private static final float ARM_SWING_Y_POS_SCALE = 0.4F;
/*     */   
/*     */   private static final float ARM_SWING_Z_POS_SCALE = -0.4F;
/*     */   
/*     */   private static final float ARM_SWING_Y_ROT_AMOUNT = 70.0F;
/*     */   
/*     */   private static final float ARM_SWING_Z_ROT_AMOUNT = -20.0F;
/*     */   
/*     */   private static final float ARM_HEIGHT_SCALE = -0.6F;
/*     */   
/*     */   private static final float ARM_POS_SCALE = 0.8F;
/*     */   
/*     */   private static final float ARM_POS_X = 0.8F;
/*     */   
/*     */   private static final float ARM_POS_Y = -0.75F;
/*     */   
/*     */   private static final float ARM_POS_Z = -0.9F;
/*     */   
/*     */   private static final float ARM_PRESWING_ROT_Y = 45.0F;
/*     */   
/*     */   private static final float ARM_PREROTATION_X_OFFSET = -1.0F;
/*     */   
/*     */   private static final float ARM_PREROTATION_Y_OFFSET = 3.6F;
/*     */   
/*     */   private static final float ARM_PREROTATION_Z_OFFSET = 3.5F;
/*     */   
/*     */   private static final float ARM_POSTROTATION_X_OFFSET = 5.6F;
/*     */   
/*     */   private static final int ARM_ROT_X = 200;
/*     */   
/*     */   private static final int ARM_ROT_Y = -135;
/*     */   
/*     */   private static final int ARM_ROT_Z = 120;
/*     */   private static final float MAP_SWING_X_POS_SCALE = -0.4F;
/*     */   private static final float MAP_SWING_Z_POS_SCALE = -0.2F;
/*     */   private static final float MAP_HANDS_POS_X = 0.0F;
/*     */   private static final float MAP_HANDS_POS_Y = 0.04F;
/*     */   private static final float MAP_HANDS_POS_Z = -0.72F;
/*     */   private static final float MAP_HANDS_HEIGHT_SCALE = -1.2F;
/*     */   private static final float MAP_HANDS_TILT_SCALE = -0.5F;
/*     */   private static final float MAP_PLAYER_PITCH_SCALE = 45.0F;
/*     */   private static final float MAP_HANDS_Z_ROT_AMOUNT = -85.0F;
/*     */   private static final float MAPHAND_X_ROT_AMOUNT = 45.0F;
/*     */   private static final float MAPHAND_Y_ROT_AMOUNT = 92.0F;
/*     */   private static final float MAPHAND_Z_ROT_AMOUNT = -41.0F;
/*     */   private static final float MAP_HAND_X_POS = 0.3F;
/*     */   private static final float MAP_HAND_Y_POS = -1.1F;
/*     */   private static final float MAP_HAND_Z_POS = 0.45F;
/*     */   private static final float MAP_SWING_X_ROT_AMOUNT = 20.0F;
/*     */   private static final float MAP_PRE_ROT_SCALE = 0.38F;
/*     */   private static final float MAP_GLOBAL_X_POS = -0.5F;
/*     */   private static final float MAP_GLOBAL_Y_POS = -0.5F;
/*     */   private static final float MAP_GLOBAL_Z_POS = 0.0F;
/*     */   private static final float MAP_FINAL_SCALE = 0.0078125F;
/*     */   private static final int MAP_BORDER = 7;
/*     */   private static final int MAP_HEIGHT = 128;
/*     */   private static final int MAP_WIDTH = 128;
/*     */   private static final float BOW_CHARGE_X_POS_SCALE = 0.0F;
/*     */   private static final float BOW_CHARGE_Y_POS_SCALE = 0.0F;
/*     */   private static final float BOW_CHARGE_Z_POS_SCALE = 0.04F;
/*     */   private static final float BOW_CHARGE_SHAKE_X_SCALE = 0.0F;
/*     */   private static final float BOW_CHARGE_SHAKE_Y_SCALE = 0.004F;
/*     */   private static final float BOW_CHARGE_SHAKE_Z_SCALE = 0.0F;
/*     */   private static final float BOW_CHARGE_Z_SCALE = 0.2F;
/*     */   private static final float BOW_MIN_SHAKE_CHARGE = 0.1F;
/*     */   private final Minecraft minecraft;
/* 155 */   private final MapRenderState mapRenderState = new MapRenderState();
/* 156 */   private ItemStack mainHandItem = ItemStack.EMPTY;
/* 157 */   private ItemStack offHandItem = ItemStack.EMPTY;
/*     */   private float mainHandHeight;
/*     */   private float oMainHandHeight;
/*     */   private float offHandHeight;
/*     */   private float oOffHandHeight;
/*     */   private final EntityRenderDispatcher entityRenderDispatcher;
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   
/*     */   public ItemInHandRenderer(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, ItemModelResolver itemModelResolver) {
/* 166 */     this.minecraft = minecraft;
/* 167 */     this.entityRenderDispatcher = entityRenderDispatcher;
/* 168 */     this.itemModelResolver = itemModelResolver;
/*     */   }
/*     */   
/*     */   public void renderItem(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 172 */     if (itemStack.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 176 */     ItemStackRenderState renderState = new ItemStackRenderState();
/* 177 */     this.itemModelResolver.updateForTopItem(renderState, itemStack, type, mob.level(), (ItemOwner)mob, mob.getId() + type.ordinal());
/* 178 */     renderState.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
/*     */   }
/*     */   
/*     */   private float calculateMapTilt(float xRot) {
/* 182 */     float tilt = 1.0F - xRot / 45.0F + 0.1F;
/* 183 */     tilt = Mth.clamp(tilt, 0.0F, 1.0F);
/* 184 */     tilt = -Mth.cos((tilt * 3.1415927F)) * 0.5F + 0.5F;
/* 185 */     return tilt;
/*     */   }
/*     */   
/*     */   private void renderMapHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm arm) {
/* 189 */     AvatarRenderer<AbstractClientPlayer> avatarRenderer = this.entityRenderDispatcher.getPlayerRenderer((AbstractClientPlayer)this.minecraft.player);
/*     */     
/* 191 */     poseStack.pushPose();
/*     */     
/* 193 */     float invert = (arm == HumanoidArm.RIGHT) ? 1.0F : -1.0F;
/*     */     
/* 195 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(92.0F));
/* 196 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(45.0F));
/* 197 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -41.0F));
/* 198 */     poseStack.translate(invert * 0.3F, -1.1F, 0.45F);
/*     */     
/* 200 */     Identifier skinTexture = this.minecraft.player.getSkin().body().texturePath();
/* 201 */     if (arm == HumanoidArm.RIGHT) {
/* 202 */       avatarRenderer.renderRightHand(poseStack, submitNodeCollector, lightCoords, skinTexture, this.minecraft.player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
/*     */     } else {
/* 204 */       avatarRenderer.renderLeftHand(poseStack, submitNodeCollector, lightCoords, skinTexture, this.minecraft.player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
/*     */     } 
/* 206 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void renderOneHandedMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, HumanoidArm arm, float attackValue, ItemStack map) {
/* 210 */     float invert = (arm == HumanoidArm.RIGHT) ? 1.0F : -1.0F;
/*     */     
/* 212 */     poseStack.translate(invert * 0.125F, -0.125F, 0.0F);
/*     */     
/* 214 */     if (!this.minecraft.player.isInvisible()) {
/* 215 */       poseStack.pushPose();
/* 216 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * 10.0F));
/* 217 */       renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attackValue, arm);
/* 218 */       poseStack.popPose();
/*     */     } 
/*     */     
/* 221 */     poseStack.pushPose();
/* 222 */     poseStack.translate(invert * 0.51F, -0.08F + inverseArmHeight * -1.2F, -0.75F);
/*     */     
/* 224 */     float sqrtAttackValue = Mth.sqrt(attackValue);
/* 225 */     float xSwing = Mth.sin((sqrtAttackValue * 3.1415927F));
/* 226 */     float xSwingPosition = -0.5F * xSwing;
/* 227 */     float ySwingPosition = 0.4F * Mth.sin((sqrtAttackValue * 6.2831855F));
/* 228 */     float zSwingPosition = -0.3F * Mth.sin((attackValue * 3.1415927F));
/* 229 */     poseStack.translate(invert * xSwingPosition, ySwingPosition - 0.3F * xSwing, zSwingPosition);
/*     */     
/* 231 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xSwing * -45.0F));
/* 232 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * xSwing * -30.0F));
/* 233 */     renderMap(poseStack, submitNodeCollector, lightCoords, map);
/* 234 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void renderTwoHandedMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float xRot, float inverseArmHeight, float attackValue) {
/* 238 */     float sqrtAttackValue = Mth.sqrt(attackValue);
/* 239 */     float ySwingPosition = -0.2F * Mth.sin((attackValue * 3.1415927F));
/* 240 */     float zSwingPosition = -0.4F * Mth.sin((sqrtAttackValue * 3.1415927F));
/* 241 */     poseStack.translate(0.0F, -ySwingPosition / 2.0F, zSwingPosition);
/*     */     
/* 243 */     float mapTilt = calculateMapTilt(xRot);
/* 244 */     poseStack.translate(0.0F, 0.04F + inverseArmHeight * -1.2F + mapTilt * -0.5F, -0.72F);
/* 245 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(mapTilt * -85.0F));
/*     */     
/* 247 */     if (!this.minecraft.player.isInvisible()) {
/* 248 */       poseStack.pushPose();
/* 249 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/*     */       
/* 251 */       renderMapHand(poseStack, submitNodeCollector, lightCoords, HumanoidArm.RIGHT);
/* 252 */       renderMapHand(poseStack, submitNodeCollector, lightCoords, HumanoidArm.LEFT);
/*     */       
/* 254 */       poseStack.popPose();
/*     */     } 
/*     */     
/* 257 */     float xzSwingRotation = Mth.sin((sqrtAttackValue * 3.1415927F));
/* 258 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xzSwingRotation * 20.0F));
/*     */     
/* 260 */     poseStack.scale(2.0F, 2.0F, 2.0F);
/*     */     
/* 262 */     renderMap(poseStack, submitNodeCollector, lightCoords, this.mainHandItem);
/*     */   }
/*     */   
/*     */   private void renderMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemStack itemStack) {
/* 266 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 267 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/*     */     
/* 269 */     poseStack.scale(0.38F, 0.38F, 0.38F);
/*     */     
/* 271 */     poseStack.translate(-0.5F, -0.5F, 0.0F);
/* 272 */     poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
/*     */     
/* 274 */     MapId id = (MapId)itemStack.get(DataComponents.MAP_ID);
/* 275 */     MapItemSavedData data = MapItem.getSavedData(id, (Level)this.minecraft.level);
/*     */     
/* 277 */     RenderType renderType = (data == null) ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD;
/*     */     
/* 279 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
/*     */           buffer.addVertex(pose, -7.0F, 135.0F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setLight(lightCoords);
/*     */           
/*     */           buffer.addVertex(pose, 135.0F, 135.0F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setLight(lightCoords);
/*     */           
/*     */           buffer.addVertex(pose, 135.0F, -7.0F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setLight(lightCoords);
/*     */           buffer.addVertex(pose, -7.0F, -7.0F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setLight(lightCoords);
/*     */         });
/* 287 */     if (data != null) {
/* 288 */       MapRenderer mapRenderer = this.minecraft.getMapRenderer();
/* 289 */       mapRenderer.extractRenderState(id, data, this.mapRenderState);
/* 290 */       mapRenderer.render(this.mapRenderState, poseStack, submitNodeCollector, false, lightCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attackValue, HumanoidArm arm) {
/* 295 */     boolean isRightArm = (arm != HumanoidArm.LEFT);
/* 296 */     float invert = isRightArm ? 1.0F : -1.0F;
/* 297 */     float sqrtAttackValue = Mth.sqrt(attackValue);
/* 298 */     float xSwingPosition = -0.3F * Mth.sin((sqrtAttackValue * 3.1415927F));
/* 299 */     float ySwingPosition = 0.4F * Mth.sin((sqrtAttackValue * 6.2831855F));
/* 300 */     float zSwingPosition = -0.4F * Mth.sin((attackValue * 3.1415927F));
/* 301 */     poseStack.translate(invert * (xSwingPosition + 0.64000005F), ySwingPosition + -0.6F + inverseArmHeight * -0.6F, zSwingPosition + -0.71999997F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 45.0F));
/*     */     
/* 309 */     float zSwingRotation = Mth.sin((attackValue * attackValue * 3.1415927F));
/* 310 */     float ySwingRotation = Mth.sin((sqrtAttackValue * 3.1415927F));
/* 311 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * ySwingRotation * 70.0F));
/* 312 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * zSwingRotation * -20.0F));
/*     */     
/* 314 */     LocalPlayer localPlayer = this.minecraft.player;
/*     */     
/* 316 */     poseStack.translate(invert * -1.0F, 3.6F, 3.5F);
/* 317 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * 120.0F));
/* 318 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(200.0F));
/* 319 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * -135.0F));
/* 320 */     poseStack.translate(invert * 5.6F, 0.0F, 0.0F);
/*     */     
/* 322 */     AvatarRenderer<AbstractClientPlayer> avatarRenderer = this.entityRenderDispatcher.getPlayerRenderer((AbstractClientPlayer)localPlayer);
/* 323 */     Identifier skinTexture = localPlayer.getSkin().body().texturePath();
/* 324 */     if (isRightArm) {
/* 325 */       avatarRenderer.renderRightHand(poseStack, submitNodeCollector, lightCoords, skinTexture, localPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
/*     */     } else {
/* 327 */       avatarRenderer.renderLeftHand(poseStack, submitNodeCollector, lightCoords, skinTexture, localPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyEatTransform(PoseStack poseStack, float frameInterp, HumanoidArm arm, ItemStack itemStack, Player player) {
/* 332 */     float currUsageTime = player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 333 */     float scaledUsageTime = currUsageTime / itemStack.getUseDuration((LivingEntity)player);
/*     */     
/* 335 */     if (scaledUsageTime < 0.8F) {
/* 336 */       float extraHeightOffset = Mth.abs(Mth.cos((currUsageTime / 4.0F * 3.1415927F)) * 0.1F);
/* 337 */       poseStack.translate(0.0F, extraHeightOffset, 0.0F);
/*     */     } 
/*     */     
/* 340 */     float eatJiggle = 1.0F - (float)Math.pow(scaledUsageTime, 27.0D);
/*     */     
/* 342 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/* 343 */     poseStack.translate(eatJiggle * 0.6F * invert, eatJiggle * -0.5F, eatJiggle * 0.0F);
/*     */     
/* 345 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * eatJiggle * 90.0F));
/* 346 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(eatJiggle * 10.0F));
/* 347 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * eatJiggle * 30.0F));
/*     */   }
/*     */   
/*     */   private void applyBrushTransform(PoseStack poseStack, float frameInterp, HumanoidArm arm, Player player) {
/* 351 */     float brushAnimationRemainingTicks = (player.getUseItemRemainingTicks() % 10);
/* 352 */     float deltaSinceLastUpdate = brushAnimationRemainingTicks - frameInterp + 1.0F;
/* 353 */     float scaledUsageTime = 1.0F - deltaSinceLastUpdate / 10.0F;
/*     */     
/* 355 */     float minSwipeAngle = -90.0F;
/* 356 */     float maxSwipeAngle = 60.0F;
/* 357 */     float swipeRange = 150.0F;
/* 358 */     float swipeCenter = -15.0F;
/*     */     
/* 360 */     int swipeSpeed = 2;
/*     */     
/* 362 */     float currentSwipeAngle = -15.0F + 75.0F * Mth.cos((scaledUsageTime * 2.0F * 3.1415927F));
/* 363 */     if (arm != HumanoidArm.RIGHT) {
/* 364 */       poseStack.translate(0.1D, 0.83D, 0.35D);
/*     */       
/* 366 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-80.0F));
/* 367 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-90.0F));
/*     */       
/* 369 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(currentSwipeAngle));
/* 370 */       poseStack.translate(-0.3D, 0.22D, 0.35D);
/*     */     } else {
/* 372 */       poseStack.translate(-0.25D, 0.22D, 0.35D);
/*     */       
/* 374 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-80.0F));
/* 375 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/* 376 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(0.0F));
/*     */       
/* 378 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(currentSwipeAngle));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float attackValue) {
/* 383 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/*     */     
/* 385 */     float ySwingRotation = Mth.sin((attackValue * attackValue * 3.1415927F));
/* 386 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * (45.0F + ySwingRotation * -20.0F)));
/*     */     
/* 388 */     float xzSwingRotation = Mth.sin((Mth.sqrt(attackValue) * 3.1415927F));
/* 389 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * xzSwingRotation * -20.0F));
/* 390 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xzSwingRotation * -80.0F));
/* 391 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * -45.0F));
/*     */   }
/*     */   
/*     */   private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float inverseArmHeight) {
/* 395 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/* 396 */     poseStack.translate(invert * 0.56F, -0.52F + inverseArmHeight * -0.6F, -0.72F);
/*     */   }
/*     */   
/*     */   public void renderHandsWithItems(float frameInterp, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LocalPlayer player, int lightCoords) {
/* 400 */     float attackValue = player.getAttackAnim(frameInterp);
/* 401 */     InteractionHand attackHand = (InteractionHand)MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND);
/* 402 */     float xRot = player.getXRot(frameInterp);
/*     */     
/* 404 */     HandRenderSelection handRenderSelection = evaluateWhichHandsToRender(player);
/*     */     
/* 406 */     float xBob = Mth.lerp(frameInterp, player.xBobO, player.xBob);
/* 407 */     float yBob = Mth.lerp(frameInterp, player.yBobO, player.yBob);
/* 408 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees((player.getViewXRot(frameInterp) - xBob) * 0.1F));
/* 409 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees((player.getViewYRot(frameInterp) - yBob) * 0.1F));
/*     */     
/* 411 */     if (handRenderSelection.renderMainHand) {
/* 412 */       float mainHandAttack = (attackHand == InteractionHand.MAIN_HAND) ? attackValue : 0.0F;
/* 413 */       float mainhandInverseArmHeight = this.itemModelResolver.swapAnimationScale(this.mainHandItem) * (1.0F - Mth.lerp(frameInterp, this.oMainHandHeight, this.mainHandHeight));
/* 414 */       renderArmWithItem((AbstractClientPlayer)player, frameInterp, xRot, InteractionHand.MAIN_HAND, mainHandAttack, this.mainHandItem, mainhandInverseArmHeight, poseStack, submitNodeCollector, lightCoords);
/*     */     } 
/*     */     
/* 417 */     if (handRenderSelection.renderOffHand) {
/* 418 */       float offHandAttack = (attackHand == InteractionHand.OFF_HAND) ? attackValue : 0.0F;
/* 419 */       float offhandInverseArmHeight = this.itemModelResolver.swapAnimationScale(this.offHandItem) * (1.0F - Mth.lerp(frameInterp, this.oOffHandHeight, this.offHandHeight));
/* 420 */       renderArmWithItem((AbstractClientPlayer)player, frameInterp, xRot, InteractionHand.OFF_HAND, offHandAttack, this.offHandItem, offhandInverseArmHeight, poseStack, submitNodeCollector, lightCoords);
/*     */     } 
/* 422 */     this.minecraft.gameRenderer.getFeatureRenderDispatcher().renderAllFeatures();
/* 423 */     this.minecraft.renderBuffers().bufferSource().endBatch();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   enum HandRenderSelection {
/* 428 */     RENDER_BOTH_HANDS(true, true),
/* 429 */     RENDER_MAIN_HAND_ONLY(true, false),
/* 430 */     RENDER_OFF_HAND_ONLY(false, true);
/*     */     final boolean renderMainHand;
/*     */     final boolean renderOffHand;
/*     */     
/*     */     HandRenderSelection(boolean renderMainHand, boolean renderOffHand) {
/* 435 */       this.renderMainHand = renderMainHand;
/* 436 */       this.renderOffHand = renderOffHand;
/*     */     }
/*     */     
/*     */     public static HandRenderSelection onlyForHand(InteractionHand hand) {
/* 440 */       return (hand == InteractionHand.MAIN_HAND) ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static HandRenderSelection evaluateWhichHandsToRender(LocalPlayer player) {
/* 447 */     ItemStack mainHandItem = player.getMainHandItem();
/* 448 */     ItemStack offhandItem = player.getOffhandItem();
/*     */     
/* 450 */     boolean holdsBow = (mainHandItem.is(Items.BOW) || offhandItem.is(Items.BOW));
/* 451 */     boolean holdsCrossbow = (mainHandItem.is(Items.CROSSBOW) || offhandItem.is(Items.CROSSBOW));
/*     */     
/* 453 */     if (!holdsBow && !holdsCrossbow) {
/* 454 */       return HandRenderSelection.RENDER_BOTH_HANDS;
/*     */     }
/*     */     
/* 457 */     if (player.isUsingItem()) {
/* 458 */       return selectionUsingItemWhileHoldingBowLike(player);
/*     */     }
/*     */     
/* 461 */     if (isChargedCrossbow(mainHandItem)) {
/* 462 */       return HandRenderSelection.RENDER_MAIN_HAND_ONLY;
/*     */     }
/*     */     
/* 465 */     return HandRenderSelection.RENDER_BOTH_HANDS;
/*     */   }
/*     */   
/*     */   private static HandRenderSelection selectionUsingItemWhileHoldingBowLike(LocalPlayer player) {
/* 469 */     ItemStack usedItemStack = player.getUseItem();
/* 470 */     InteractionHand usedHand = player.getUsedItemHand();
/*     */     
/* 472 */     if (usedItemStack.is(Items.BOW) || usedItemStack.is(Items.CROSSBOW)) {
/* 473 */       return HandRenderSelection.onlyForHand(usedHand);
/*     */     }
/*     */ 
/*     */     
/* 477 */     return (usedHand == InteractionHand.MAIN_HAND && isChargedCrossbow(player.getOffhandItem())) ? 
/* 478 */       HandRenderSelection.RENDER_MAIN_HAND_ONLY : 
/* 479 */       HandRenderSelection.RENDER_BOTH_HANDS;
/*     */   }
/*     */   
/*     */   private static boolean isChargedCrossbow(ItemStack item) {
/* 483 */     return (item.is(Items.CROSSBOW) && CrossbowItem.isCharged(item));
/*     */   }
/*     */   
/*     */   private void renderArmWithItem(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 487 */     if (player.isScoping()) {
/*     */       return;
/*     */     }
/*     */     
/* 491 */     boolean isMainHand = (hand == InteractionHand.MAIN_HAND);
/* 492 */     HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
/*     */     
/* 494 */     poseStack.pushPose();
/* 495 */     if (itemStack.isEmpty()) {
/* 496 */       if (isMainHand && !player.isInvisible()) {
/* 497 */         renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attack, arm);
/*     */       }
/*     */     }
/* 500 */     else if (itemStack.has(DataComponents.MAP_ID)) {
/* 501 */       if (isMainHand && this.offHandItem.isEmpty()) {
/* 502 */         renderTwoHandedMap(poseStack, submitNodeCollector, lightCoords, xRot, inverseArmHeight, attack);
/*     */       } else {
/* 504 */         renderOneHandedMap(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, arm, attack, itemStack);
/*     */       } 
/* 506 */     } else if (itemStack.is(Items.CROSSBOW)) {
/* 507 */       applyItemArmTransform(poseStack, arm, inverseArmHeight);
/*     */       
/* 509 */       boolean charged = CrossbowItem.isCharged(itemStack);
/* 510 */       boolean isRightArm = (arm == HumanoidArm.RIGHT);
/* 511 */       int invert = isRightArm ? 1 : -1;
/*     */       
/* 513 */       if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand && !charged) {
/* 514 */         poseStack.translate(invert * -0.4785682F, -0.094387F, 0.05731531F);
/* 515 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-11.935F));
/* 516 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 65.3F));
/* 517 */         poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -9.785F));
/*     */         
/* 519 */         float timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 520 */         float power = timeHeld / CrossbowItem.getChargeDuration(itemStack, (LivingEntity)player);
/* 521 */         if (power > 1.0F) {
/* 522 */           power = 1.0F;
/*     */         }
/* 524 */         if (power > 0.1F) {
/* 525 */           float shakeOffset = Mth.sin(((timeHeld - 0.1F) * 1.3F));
/* 526 */           float shakeIntensity = power - 0.1F;
/* 527 */           float shake = shakeOffset * shakeIntensity;
/* 528 */           poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
/*     */         } 
/* 530 */         poseStack.translate(power * 0.0F, power * 0.0F, power * 0.04F);
/*     */         
/* 532 */         poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
/* 533 */         poseStack.mulPose((Quaternionfc)Axis.YN.rotationDegrees(invert * 45.0F));
/*     */       } else {
/* 535 */         swingArm(attack, poseStack, invert, arm);
/*     */ 
/*     */         
/* 538 */         if (charged && attack < 0.001F && isMainHand) {
/* 539 */           poseStack.translate(invert * -0.641864F, 0.0F, 0.0F);
/* 540 */           poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 10.0F));
/*     */         } 
/*     */       } 
/*     */       
/* 544 */       renderItem((LivingEntity)player, itemStack, isRightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, poseStack, submitNodeCollector, lightCoords);
/*     */     } else {
/* 546 */       boolean isRightArm = (arm == HumanoidArm.RIGHT);
/* 547 */       int invert = isRightArm ? 1 : -1;
/*     */       
/* 549 */       if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
/* 550 */         float timeHeld, power; ItemUseAnimation useAnimation = itemStack.getUseAnimation();
/* 551 */         if (!useAnimation.hasCustomArmTransform()) {
/* 552 */           applyItemArmTransform(poseStack, arm, inverseArmHeight);
/*     */         }
/* 554 */         switch (useAnimation) {
/*     */ 
/*     */           
/*     */           case EAT:
/*     */           case DRINK:
/* 559 */             applyEatTransform(poseStack, frameInterp, arm, itemStack, (Player)player);
/* 560 */             applyItemArmTransform(poseStack, arm, inverseArmHeight);
/*     */             break;
/*     */           case BLOCK:
/* 563 */             if (!(itemStack.getItem() instanceof net.minecraft.world.item.ShieldItem)) {
/* 564 */               poseStack.translate(invert * -0.14142136F, 0.08F, 0.14142136F);
/* 565 */               poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-102.25F));
/* 566 */               poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 13.365F));
/* 567 */               poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * 78.05F));
/*     */             } 
/*     */             break;
/*     */           case BOW:
/* 571 */             poseStack.translate(invert * -0.2785682F, 0.18344387F, 0.15731531F);
/* 572 */             poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-13.935F));
/* 573 */             poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 35.3F));
/* 574 */             poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -9.785F));
/*     */             
/* 576 */             timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 577 */             power = timeHeld / 20.0F;
/* 578 */             power = (power * power + power * 2.0F) / 3.0F;
/* 579 */             if (power > 1.0F) {
/* 580 */               power = 1.0F;
/*     */             }
/* 582 */             if (power > 0.1F) {
/* 583 */               float shakeOffset = Mth.sin(((timeHeld - 0.1F) * 1.3F));
/* 584 */               float shakeIntensity = power - 0.1F;
/* 585 */               float shake = shakeOffset * shakeIntensity;
/* 586 */               poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
/*     */             } 
/* 588 */             poseStack.translate(power * 0.0F, power * 0.0F, power * 0.04F);
/*     */             
/* 590 */             poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
/* 591 */             poseStack.mulPose((Quaternionfc)Axis.YN.rotationDegrees(invert * 45.0F));
/*     */             break;
/*     */           
/*     */           case TRIDENT:
/* 595 */             poseStack.translate(invert * -0.5F, 0.7F, 0.1F);
/* 596 */             poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-55.0F));
/* 597 */             poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 35.3F));
/* 598 */             poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -9.785F));
/*     */             
/* 600 */             timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 601 */             power = timeHeld / 10.0F;
/* 602 */             if (power > 1.0F) {
/* 603 */               power = 1.0F;
/*     */             }
/* 605 */             if (power > 0.1F) {
/* 606 */               float shakeOffset = Mth.sin(((timeHeld - 0.1F) * 1.3F));
/* 607 */               float shakeIntensity = power - 0.1F;
/* 608 */               float shake = shakeOffset * shakeIntensity;
/* 609 */               poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
/*     */             } 
/* 611 */             poseStack.translate(0.0F, 0.0F, power * 0.2F);
/*     */             
/* 613 */             poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
/* 614 */             poseStack.mulPose((Quaternionfc)Axis.YN.rotationDegrees(invert * 45.0F));
/*     */             break;
/*     */           
/*     */           case BRUSH:
/* 618 */             applyBrushTransform(poseStack, frameInterp, arm, (Player)player);
/*     */             break;
/*     */           
/*     */           case BUNDLE:
/* 622 */             swingArm(attack, poseStack, invert, arm);
/*     */             break;
/*     */           
/*     */           case SPEAR:
/* 626 */             poseStack.translate(invert * 0.56F, -0.52F, -0.72F);
/* 627 */             timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 628 */             SpearAnimations.firstPersonUse(player.getTicksSinceLastKineticHitFeedback(frameInterp), poseStack, timeHeld, arm, itemStack);
/*     */             break;
/*     */         } 
/*     */       
/* 632 */       } else if (player.isAutoSpinAttack()) {
/* 633 */         applyItemArmTransform(poseStack, arm, inverseArmHeight);
/* 634 */         poseStack.translate(invert * -0.4F, 0.8F, 0.3F);
/* 635 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 65.0F));
/* 636 */         poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -85.0F));
/*     */       } else {
/* 638 */         applyItemArmTransform(poseStack, arm, inverseArmHeight);
/* 639 */         switch (itemStack.getSwingAnimation().type()) {
/*     */           
/*     */           case WHACK:
/* 642 */             swingArm(attack, poseStack, invert, arm); break;
/* 643 */           case STAB: SpearAnimations.firstPersonAttack(attack, poseStack, invert, arm); break;
/*     */         } 
/*     */       } 
/* 646 */       renderItem((LivingEntity)player, itemStack, isRightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, poseStack, submitNodeCollector, lightCoords);
/*     */     } 
/*     */     
/* 649 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void swingArm(float attack, PoseStack poseStack, int invert, HumanoidArm arm) {
/* 653 */     float xSwingPosition = -0.4F * Mth.sin((Mth.sqrt(attack) * 3.1415927F));
/* 654 */     float ySwingPosition = 0.2F * Mth.sin((Mth.sqrt(attack) * 6.2831855F));
/* 655 */     float zSwingPosition = -0.2F * Mth.sin((attack * 3.1415927F));
/* 656 */     poseStack.translate(invert * xSwingPosition, ySwingPosition, zSwingPosition);
/* 657 */     applyItemArmAttackTransform(poseStack, arm, attack);
/*     */   }
/*     */   
/*     */   private boolean shouldInstantlyReplaceVisibleItem(ItemStack currentlyVisibleItem, ItemStack expectedItem) {
/* 661 */     if (ItemStack.matchesIgnoringComponents(currentlyVisibleItem, expectedItem, DataComponentType::ignoreSwapAnimation)) {
/* 662 */       return true;
/*     */     }
/*     */     
/* 665 */     return !this.itemModelResolver.shouldPlaySwapAnimation(expectedItem);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 670 */     this.oMainHandHeight = this.mainHandHeight;
/* 671 */     this.oOffHandHeight = this.offHandHeight;
/*     */     
/* 673 */     LocalPlayer player = this.minecraft.player;
/* 674 */     ItemStack nextMainHand = player.getMainHandItem();
/* 675 */     ItemStack nextOffHand = player.getOffhandItem();
/*     */ 
/*     */     
/* 678 */     if (shouldInstantlyReplaceVisibleItem(this.mainHandItem, nextMainHand)) {
/* 679 */       this.mainHandItem = nextMainHand;
/*     */     }
/* 681 */     if (shouldInstantlyReplaceVisibleItem(this.offHandItem, nextOffHand)) {
/* 682 */       this.offHandItem = nextOffHand;
/*     */     }
/*     */     
/* 685 */     if (player.isHandsBusy()) {
/* 686 */       this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4F, 0.0F, 1.0F);
/* 687 */       this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4F, 0.0F, 1.0F);
/*     */     } else {
/* 689 */       float attackAnim = player.getItemSwapScale(1.0F);
/*     */       
/* 691 */       float mainHandTargetHeight = (this.mainHandItem != nextMainHand) ? 0.0F : (attackAnim * attackAnim * attackAnim);
/* 692 */       float offHandTargetHeight = (this.offHandItem != nextOffHand) ? 0.0F : 1.0F;
/*     */       
/* 694 */       this.mainHandHeight += Mth.clamp(mainHandTargetHeight - this.mainHandHeight, -0.4F, 0.4F);
/* 695 */       this.offHandHeight += Mth.clamp(offHandTargetHeight - this.offHandHeight, -0.4F, 0.4F);
/*     */     } 
/*     */ 
/*     */     
/* 699 */     if (this.mainHandHeight < 0.1F) {
/* 700 */       this.mainHandItem = nextMainHand;
/*     */     }
/*     */     
/* 703 */     if (this.offHandHeight < 0.1F) {
/* 704 */       this.offHandItem = nextOffHand;
/*     */     }
/*     */   }
/*     */   
/*     */   public void itemUsed(InteractionHand hand) {
/* 709 */     if (hand == InteractionHand.MAIN_HAND) {
/* 710 */       this.mainHandHeight = 0.0F;
/*     */     } else {
/* 712 */       this.offHandHeight = 0.0F;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/ItemInHandRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */