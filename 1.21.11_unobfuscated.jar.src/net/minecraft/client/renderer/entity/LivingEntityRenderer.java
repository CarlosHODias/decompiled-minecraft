/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
/*     */   extends EntityRenderer<T, S>
/*     */   implements RenderLayerParent<S, M>
/*     */ {
/*     */   private static final float EYE_BED_OFFSET = 0.1F;
/*     */   protected M model;
/*     */   protected final ItemModelResolver itemModelResolver;
/*  45 */   protected final List<RenderLayer<S, M>> layers = Lists.newArrayList();
/*     */   
/*     */   public LivingEntityRenderer(EntityRendererProvider.Context context, M model, float shadow) {
/*  48 */     super(context);
/*  49 */     this.itemModelResolver = context.getItemModelResolver();
/*  50 */     this.model = model;
/*  51 */     this.shadowRadius = shadow;
/*     */   }
/*     */   
/*     */   protected final boolean addLayer(RenderLayer<S, M> layer) {
/*  55 */     return this.layers.add(layer);
/*     */   }
/*     */ 
/*     */   
/*     */   public M getModel() {
/*  60 */     return this.model;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/*  65 */     AABB aabb = super.getBoundingBoxForCulling(entity);
/*  66 */     if (entity.getItemBySlot(EquipmentSlot.HEAD).is(Items.DRAGON_HEAD)) {
/*  67 */       float extraSize = 0.5F;
/*  68 */       return aabb.inflate(0.5D, 0.5D, 0.5D);
/*     */     } 
/*  70 */     return aabb;
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  75 */     poseStack.pushPose();
/*     */     
/*  77 */     if (state.hasPose(Pose.SLEEPING)) {
/*  78 */       Direction bedOrientation = ((LivingEntityRenderState)state).bedOrientation;
/*  79 */       if (bedOrientation != null) {
/*  80 */         float headOffset = ((LivingEntityRenderState)state).eyeHeight - 0.1F;
/*  81 */         poseStack.translate(-bedOrientation.getStepX() * headOffset, 0.0F, -bedOrientation.getStepZ() * headOffset);
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     float scale = ((LivingEntityRenderState)state).scale;
/*  86 */     poseStack.scale(scale, scale, scale);
/*     */     
/*  88 */     setupRotations(state, poseStack, ((LivingEntityRenderState)state).bodyRot, scale);
/*     */ 
/*     */ 
/*     */     
/*  92 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*  93 */     scale(state, poseStack);
/*     */ 
/*     */     
/*  96 */     poseStack.translate(0.0F, -1.501F, 0.0F);
/*     */     
/*  98 */     boolean isBodyVisible = isBodyVisible(state);
/*  99 */     boolean forceTransparent = (!isBodyVisible && !((LivingEntityRenderState)state).isInvisibleToPlayer);
/*     */     
/* 101 */     RenderType renderType = getRenderType(state, isBodyVisible, forceTransparent, state.appearsGlowing());
/* 102 */     if (renderType != null) {
/* 103 */       int overlayCoords = getOverlayCoords((LivingEntityRenderState)state, getWhiteOverlayProgress(state));
/* 104 */       int baseColor = forceTransparent ? 654311423 : -1;
/* 105 */       int tintedColor = ARGB.multiply(baseColor, getModelTint(state));
/* 106 */       submitNodeCollector.submitModel((Model)this.model, state, poseStack, renderType, ((LivingEntityRenderState)state).lightCoords, overlayCoords, tintedColor, null, ((LivingEntityRenderState)state).outlineColor, null);
/*     */     } 
/*     */     
/* 109 */     if (shouldRenderLayers(state) && !this.layers.isEmpty()) {
/*     */       
/* 111 */       this.model.setupAnim(state);
/* 112 */       for (RenderLayer<S, M> layer : this.layers) {
/* 113 */         layer.submit(poseStack, submitNodeCollector, ((LivingEntityRenderState)state).lightCoords, (EntityRenderState)state, ((LivingEntityRenderState)state).yRot, ((LivingEntityRenderState)state).xRot);
/*     */       }
/*     */     } 
/*     */     
/* 117 */     poseStack.popPose();
/* 118 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   protected boolean shouldRenderLayers(S state) {
/* 122 */     return true;
/*     */   }
/*     */   
/*     */   protected int getModelTint(S state) {
/* 126 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RenderType getRenderType(S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
/* 132 */     Identifier texture = getTextureLocation(state);
/* 133 */     if (forceTransparent) {
/* 134 */       return RenderTypes.itemEntityTranslucentCull(texture);
/*     */     }
/* 136 */     if (isBodyVisible) {
/* 137 */       return this.model.renderType(texture);
/*     */     }
/* 139 */     if (appearGlowing) {
/* 140 */       return RenderTypes.outline(texture);
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */   
/*     */   public static int getOverlayCoords(LivingEntityRenderState state, float whiteOverlayProgress) {
/* 146 */     return OverlayTexture.pack(OverlayTexture.u(whiteOverlayProgress), OverlayTexture.v(state.hasRedOverlay));
/*     */   }
/*     */   
/*     */   protected boolean isBodyVisible(S state) {
/* 150 */     return !((LivingEntityRenderState)state).isInvisible;
/*     */   }
/*     */   
/*     */   private static float sleepDirectionToRotation(Direction direction) {
/* 154 */     switch (direction) {
/*     */       case SOUTH:
/* 156 */         return 90.0F;
/*     */       case WEST:
/* 158 */         return 0.0F;
/*     */       case NORTH:
/* 160 */         return 270.0F;
/*     */       case EAST:
/* 162 */         return 180.0F;
/*     */     } 
/* 164 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isShaking(S state) {
/* 169 */     return ((LivingEntityRenderState)state).isFullyFrozen;
/*     */   }
/*     */   
/*     */   protected void setupRotations(S state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 173 */     if (isShaking(state))
/*     */     {
/* 175 */       bodyRot += (float)(Math.cos((Mth.floor(((LivingEntityRenderState)state).ageInTicks) * 3.25F)) * Math.PI * 0.4000000059604645D);
/*     */     }
/*     */     
/* 178 */     if (!state.hasPose(Pose.SLEEPING)) {
/* 179 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - bodyRot));
/*     */     }
/* 181 */     if (((LivingEntityRenderState)state).deathTime > 0.0F) {
/* 182 */       float fall = (((LivingEntityRenderState)state).deathTime - 1.0F) / 20.0F * 1.6F;
/* 183 */       fall = Mth.sqrt(fall);
/* 184 */       if (fall > 1.0F) {
/* 185 */         fall = 1.0F;
/*     */       }
/* 187 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(fall * getFlipDegrees()));
/* 188 */     } else if (((LivingEntityRenderState)state).isAutoSpinAttack) {
/* 189 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F - ((LivingEntityRenderState)state).xRot));
/* 190 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((LivingEntityRenderState)state).ageInTicks * -75.0F));
/* 191 */     } else if (state.hasPose(Pose.SLEEPING)) {
/* 192 */       Direction bedOrientation = ((LivingEntityRenderState)state).bedOrientation;
/* 193 */       float angle = (bedOrientation != null) ? sleepDirectionToRotation(bedOrientation) : bodyRot;
/* 194 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(angle));
/* 195 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(getFlipDegrees()));
/* 196 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(270.0F));
/* 197 */     } else if (((LivingEntityRenderState)state).isUpsideDown) {
/* 198 */       poseStack.translate(0.0F, (((LivingEntityRenderState)state).boundingBoxHeight + 0.1F) / entityScale, 0.0F);
/* 199 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected float getFlipDegrees() {
/* 204 */     return 90.0F;
/*     */   }
/*     */   
/*     */   protected float getWhiteOverlayProgress(S state) {
/* 208 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void scale(S state, PoseStack poseStack) {}
/*     */ 
/*     */   
/*     */   protected boolean shouldShowName(T entity, double distanceToCameraSq) {
/* 216 */     if (entity.isDiscrete()) {
/* 217 */       float maxDist = 32.0F;
/* 218 */       if (distanceToCameraSq >= 1024.0D) {
/* 219 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 223 */     Minecraft minecraft = Minecraft.getInstance();
/* 224 */     LocalPlayer player = minecraft.player;
/* 225 */     boolean isVisibleToPlayer = !entity.isInvisibleTo((Player)player);
/* 226 */     if (entity != player) {
/* 227 */       PlayerTeam playerTeam1 = entity.getTeam();
/* 228 */       PlayerTeam playerTeam2 = player.getTeam();
/* 229 */       if (playerTeam1 != null) {
/* 230 */         Team.Visibility visibility = playerTeam1.getNameTagVisibility();
/* 231 */         switch (visibility) {
/*     */           case ALWAYS:
/* 233 */             return isVisibleToPlayer;
/*     */           case NEVER:
/* 235 */             return false;
/*     */           case HIDE_FOR_OTHER_TEAMS:
/* 237 */             return (playerTeam2 == null) ? isVisibleToPlayer : ((playerTeam1.isAlliedTo((Team)playerTeam2) && (playerTeam1.canSeeFriendlyInvisibles() || isVisibleToPlayer)));
/*     */           case HIDE_FOR_OWN_TEAM:
/* 239 */             return (playerTeam2 == null) ? isVisibleToPlayer : ((!playerTeam1.isAlliedTo((Team)playerTeam2) && isVisibleToPlayer));
/*     */         } 
/* 241 */         return true;
/*     */       } 
/*     */     } 
/* 244 */     return (Minecraft.renderNames() && entity != minecraft.getCameraEntity() && isVisibleToPlayer && !entity.isVehicle());
/*     */   }
/*     */   
/*     */   public boolean isEntityUpsideDown(T mob) {
/* 248 */     Component customName = mob.getCustomName();
/* 249 */     return (customName != null && isUpsideDownName(customName.getString()));
/*     */   }
/*     */   
/*     */   protected static boolean isUpsideDownName(String name) {
/* 253 */     return ("Dinnerbone".equals(name) || "Grumm".equals(name));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getShadowRadius(S state) {
/* 258 */     return super.getShadowRadius(state) * ((LivingEntityRenderState)state).scale;
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
/*     */   public void extractRenderState(T entity, S state, float partialTicks) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: aload_2
/*     */     //   3: fload_3
/*     */     //   4: invokespecial extractRenderState : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V
/*     */     //   7: fload_3
/*     */     //   8: aload_1
/*     */     //   9: getfield yHeadRotO : F
/*     */     //   12: aload_1
/*     */     //   13: getfield yHeadRot : F
/*     */     //   16: invokestatic rotLerp : (FFF)F
/*     */     //   19: fstore #4
/*     */     //   21: aload_2
/*     */     //   22: aload_1
/*     */     //   23: fload #4
/*     */     //   25: fload_3
/*     */     //   26: invokestatic solveBodyRot : (Lnet/minecraft/world/entity/LivingEntity;FF)F
/*     */     //   29: putfield bodyRot : F
/*     */     //   32: aload_2
/*     */     //   33: fload #4
/*     */     //   35: aload_2
/*     */     //   36: getfield bodyRot : F
/*     */     //   39: fsub
/*     */     //   40: invokestatic wrapDegrees : (F)F
/*     */     //   43: putfield yRot : F
/*     */     //   46: aload_2
/*     */     //   47: aload_1
/*     */     //   48: fload_3
/*     */     //   49: invokevirtual getXRot : (F)F
/*     */     //   52: putfield xRot : F
/*     */     //   55: aload_2
/*     */     //   56: aload_0
/*     */     //   57: aload_1
/*     */     //   58: invokevirtual isEntityUpsideDown : (Lnet/minecraft/world/entity/LivingEntity;)Z
/*     */     //   61: putfield isUpsideDown : Z
/*     */     //   64: aload_2
/*     */     //   65: getfield isUpsideDown : Z
/*     */     //   68: ifeq -> 93
/*     */     //   71: aload_2
/*     */     //   72: dup
/*     */     //   73: getfield xRot : F
/*     */     //   76: ldc -1.0
/*     */     //   78: fmul
/*     */     //   79: putfield xRot : F
/*     */     //   82: aload_2
/*     */     //   83: dup
/*     */     //   84: getfield yRot : F
/*     */     //   87: ldc -1.0
/*     */     //   89: fmul
/*     */     //   90: putfield yRot : F
/*     */     //   93: aload_1
/*     */     //   94: invokevirtual isPassenger : ()Z
/*     */     //   97: ifne -> 134
/*     */     //   100: aload_1
/*     */     //   101: invokevirtual isAlive : ()Z
/*     */     //   104: ifeq -> 134
/*     */     //   107: aload_2
/*     */     //   108: aload_1
/*     */     //   109: getfield walkAnimation : Lnet/minecraft/world/entity/WalkAnimationState;
/*     */     //   112: fload_3
/*     */     //   113: invokevirtual position : (F)F
/*     */     //   116: putfield walkAnimationPos : F
/*     */     //   119: aload_2
/*     */     //   120: aload_1
/*     */     //   121: getfield walkAnimation : Lnet/minecraft/world/entity/WalkAnimationState;
/*     */     //   124: fload_3
/*     */     //   125: invokevirtual speed : (F)F
/*     */     //   128: putfield walkAnimationSpeed : F
/*     */     //   131: goto -> 144
/*     */     //   134: aload_2
/*     */     //   135: fconst_0
/*     */     //   136: putfield walkAnimationPos : F
/*     */     //   139: aload_2
/*     */     //   140: fconst_0
/*     */     //   141: putfield walkAnimationSpeed : F
/*     */     //   144: aload_1
/*     */     //   145: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   148: astore #6
/*     */     //   150: aload #6
/*     */     //   152: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   155: ifeq -> 181
/*     */     //   158: aload #6
/*     */     //   160: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   163: astore #5
/*     */     //   165: aload_2
/*     */     //   166: aload #5
/*     */     //   168: getfield walkAnimation : Lnet/minecraft/world/entity/WalkAnimationState;
/*     */     //   171: fload_3
/*     */     //   172: invokevirtual position : (F)F
/*     */     //   175: putfield wornHeadAnimationPos : F
/*     */     //   178: goto -> 189
/*     */     //   181: aload_2
/*     */     //   182: aload_2
/*     */     //   183: getfield walkAnimationPos : F
/*     */     //   186: putfield wornHeadAnimationPos : F
/*     */     //   189: aload_2
/*     */     //   190: aload_1
/*     */     //   191: invokevirtual getScale : ()F
/*     */     //   194: putfield scale : F
/*     */     //   197: aload_2
/*     */     //   198: aload_1
/*     */     //   199: invokevirtual getAgeScale : ()F
/*     */     //   202: putfield ageScale : F
/*     */     //   205: aload_2
/*     */     //   206: aload_1
/*     */     //   207: invokevirtual getPose : ()Lnet/minecraft/world/entity/Pose;
/*     */     //   210: putfield pose : Lnet/minecraft/world/entity/Pose;
/*     */     //   213: aload_2
/*     */     //   214: aload_1
/*     */     //   215: invokevirtual getBedOrientation : ()Lnet/minecraft/core/Direction;
/*     */     //   218: putfield bedOrientation : Lnet/minecraft/core/Direction;
/*     */     //   221: aload_2
/*     */     //   222: getfield bedOrientation : Lnet/minecraft/core/Direction;
/*     */     //   225: ifnull -> 239
/*     */     //   228: aload_2
/*     */     //   229: aload_1
/*     */     //   230: getstatic net/minecraft/world/entity/Pose.STANDING : Lnet/minecraft/world/entity/Pose;
/*     */     //   233: invokevirtual getEyeHeight : (Lnet/minecraft/world/entity/Pose;)F
/*     */     //   236: putfield eyeHeight : F
/*     */     //   239: aload_2
/*     */     //   240: aload_1
/*     */     //   241: invokevirtual isFullyFrozen : ()Z
/*     */     //   244: putfield isFullyFrozen : Z
/*     */     //   247: aload_2
/*     */     //   248: aload_1
/*     */     //   249: invokevirtual isBaby : ()Z
/*     */     //   252: putfield isBaby : Z
/*     */     //   255: aload_2
/*     */     //   256: aload_1
/*     */     //   257: invokevirtual isInWater : ()Z
/*     */     //   260: putfield isInWater : Z
/*     */     //   263: aload_2
/*     */     //   264: aload_1
/*     */     //   265: invokevirtual isAutoSpinAttack : ()Z
/*     */     //   268: putfield isAutoSpinAttack : Z
/*     */     //   271: aload_2
/*     */     //   272: aload_1
/*     */     //   273: fload_3
/*     */     //   274: invokevirtual getTicksSinceLastKineticHitFeedback : (F)F
/*     */     //   277: putfield ticksSinceKineticHitFeedback : F
/*     */     //   280: aload_2
/*     */     //   281: aload_1
/*     */     //   282: getfield hurtTime : I
/*     */     //   285: ifgt -> 295
/*     */     //   288: aload_1
/*     */     //   289: getfield deathTime : I
/*     */     //   292: ifle -> 299
/*     */     //   295: iconst_1
/*     */     //   296: goto -> 300
/*     */     //   299: iconst_0
/*     */     //   300: putfield hasRedOverlay : Z
/*     */     //   303: aload_1
/*     */     //   304: getstatic net/minecraft/world/entity/EquipmentSlot.HEAD : Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   307: invokevirtual getItemBySlot : (Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;
/*     */     //   310: astore #5
/*     */     //   312: aload #5
/*     */     //   314: invokevirtual getItem : ()Lnet/minecraft/world/item/Item;
/*     */     //   317: astore #8
/*     */     //   319: aload #8
/*     */     //   321: instanceof net/minecraft/world/item/BlockItem
/*     */     //   324: ifeq -> 390
/*     */     //   327: aload #8
/*     */     //   329: checkcast net/minecraft/world/item/BlockItem
/*     */     //   332: astore #6
/*     */     //   334: aload #6
/*     */     //   336: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   339: astore #8
/*     */     //   341: aload #8
/*     */     //   343: instanceof net/minecraft/world/level/block/AbstractSkullBlock
/*     */     //   346: ifeq -> 390
/*     */     //   349: aload #8
/*     */     //   351: checkcast net/minecraft/world/level/block/AbstractSkullBlock
/*     */     //   354: astore #7
/*     */     //   356: aload_2
/*     */     //   357: aload #7
/*     */     //   359: invokevirtual getType : ()Lnet/minecraft/world/level/block/SkullBlock$Type;
/*     */     //   362: putfield wornHeadType : Lnet/minecraft/world/level/block/SkullBlock$Type;
/*     */     //   365: aload_2
/*     */     //   366: aload #5
/*     */     //   368: getstatic net/minecraft/core/component/DataComponents.PROFILE : Lnet/minecraft/core/component/DataComponentType;
/*     */     //   371: invokevirtual get : (Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;
/*     */     //   374: checkcast net/minecraft/world/item/component/ResolvableProfile
/*     */     //   377: putfield wornHeadProfile : Lnet/minecraft/world/item/component/ResolvableProfile;
/*     */     //   380: aload_2
/*     */     //   381: getfield headItem : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   384: invokevirtual clear : ()V
/*     */     //   387: goto -> 438
/*     */     //   390: aload_2
/*     */     //   391: aconst_null
/*     */     //   392: putfield wornHeadType : Lnet/minecraft/world/level/block/SkullBlock$Type;
/*     */     //   395: aload_2
/*     */     //   396: aconst_null
/*     */     //   397: putfield wornHeadProfile : Lnet/minecraft/world/item/component/ResolvableProfile;
/*     */     //   400: aload #5
/*     */     //   402: getstatic net/minecraft/world/entity/EquipmentSlot.HEAD : Lnet/minecraft/world/entity/EquipmentSlot;
/*     */     //   405: invokestatic shouldRender : (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z
/*     */     //   408: ifne -> 431
/*     */     //   411: aload_0
/*     */     //   412: getfield itemModelResolver : Lnet/minecraft/client/renderer/item/ItemModelResolver;
/*     */     //   415: aload_2
/*     */     //   416: getfield headItem : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   419: aload #5
/*     */     //   421: getstatic net/minecraft/world/item/ItemDisplayContext.HEAD : Lnet/minecraft/world/item/ItemDisplayContext;
/*     */     //   424: aload_1
/*     */     //   425: invokevirtual updateForLiving : (Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V
/*     */     //   428: goto -> 438
/*     */     //   431: aload_2
/*     */     //   432: getfield headItem : Lnet/minecraft/client/renderer/item/ItemStackRenderState;
/*     */     //   435: invokevirtual clear : ()V
/*     */     //   438: aload_2
/*     */     //   439: aload_1
/*     */     //   440: getfield deathTime : I
/*     */     //   443: ifle -> 456
/*     */     //   446: aload_1
/*     */     //   447: getfield deathTime : I
/*     */     //   450: i2f
/*     */     //   451: fload_3
/*     */     //   452: fadd
/*     */     //   453: goto -> 457
/*     */     //   456: fconst_0
/*     */     //   457: putfield deathTime : F
/*     */     //   460: invokestatic getInstance : ()Lnet/minecraft/client/Minecraft;
/*     */     //   463: astore #6
/*     */     //   465: aload_2
/*     */     //   466: aload_2
/*     */     //   467: getfield isInvisible : Z
/*     */     //   470: ifeq -> 489
/*     */     //   473: aload_1
/*     */     //   474: aload #6
/*     */     //   476: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   479: invokevirtual isInvisibleTo : (Lnet/minecraft/world/entity/player/Player;)Z
/*     */     //   482: ifeq -> 489
/*     */     //   485: iconst_1
/*     */     //   486: goto -> 490
/*     */     //   489: iconst_0
/*     */     //   490: putfield isInvisibleToPlayer : Z
/*     */     //   493: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #263	-> 0
/*     */     //   #265	-> 7
/*     */     //   #266	-> 21
/*     */     //   #268	-> 32
/*     */     //   #269	-> 46
/*     */     //   #271	-> 55
/*     */     //   #272	-> 64
/*     */     //   #273	-> 71
/*     */     //   #274	-> 82
/*     */     //   #277	-> 93
/*     */     //   #278	-> 107
/*     */     //   #279	-> 119
/*     */     //   #281	-> 134
/*     */     //   #282	-> 139
/*     */     //   #284	-> 144
/*     */     //   #285	-> 165
/*     */     //   #287	-> 181
/*     */     //   #290	-> 189
/*     */     //   #291	-> 197
/*     */     //   #292	-> 205
/*     */     //   #293	-> 213
/*     */     //   #294	-> 221
/*     */     //   #295	-> 228
/*     */     //   #298	-> 239
/*     */     //   #299	-> 247
/*     */     //   #300	-> 255
/*     */     //   #301	-> 263
/*     */     //   #302	-> 271
/*     */     //   #303	-> 280
/*     */     //   #305	-> 303
/*     */     //   #308	-> 312
/*     */     //   #309	-> 356
/*     */     //   #310	-> 365
/*     */     //   #311	-> 380
/*     */     //   #313	-> 390
/*     */     //   #314	-> 395
/*     */     //   #316	-> 400
/*     */     //   #317	-> 411
/*     */     //   #320	-> 431
/*     */     //   #324	-> 438
/*     */     //   #326	-> 460
/*     */     //   #327	-> 465
/*     */     //   #328	-> 493
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   165	16	5	vehicle	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   334	56	6	blockItem	Lnet/minecraft/world/item/BlockItem;
/*     */     //   356	34	7	skullBlock	Lnet/minecraft/world/level/block/AbstractSkullBlock;
/*     */     //   0	494	0	this	Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;
/*     */     //   0	494	1	entity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	494	2	state	Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;
/*     */     //   0	494	3	partialTicks	F
/*     */     //   21	473	4	headRot	F
/*     */     //   312	182	5	headItem	Lnet/minecraft/world/item/ItemStack;
/*     */     //   465	29	6	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	494	0	this	Lnet/minecraft/client/renderer/entity/LivingEntityRenderer<TT;TS;TM;>;
/*     */     //   0	494	1	entity	TT;
/*     */     //   0	494	2	state	TS;
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
/*     */   private static float solveBodyRot(LivingEntity entity, float headRot, float partialTicks) {
/* 331 */     Entity entity1 = entity.getVehicle(); if (entity1 instanceof LivingEntity) { LivingEntity riding = (LivingEntity)entity1;
/* 332 */       float bodyRot = Mth.rotLerp(partialTicks, riding.yBodyRotO, riding.yBodyRot);
/*     */       
/* 334 */       float maxHeadDiff = 85.0F;
/* 335 */       float headDiff = Mth.clamp(Mth.wrapDegrees(headRot - bodyRot), -85.0F, 85.0F);
/* 336 */       bodyRot = headRot - headDiff;
/*     */       
/* 338 */       if (Math.abs(headDiff) > 50.0F) {
/* 339 */         bodyRot += headDiff * 0.2F;
/*     */       }
/*     */       
/* 342 */       return bodyRot; }
/*     */ 
/*     */     
/* 345 */     return Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
/*     */   }
/*     */   
/*     */   public abstract Identifier getTextureLocation(S paramS);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/LivingEntityRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */