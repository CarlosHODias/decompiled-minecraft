/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.LightTexture;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EntityRenderer<T extends Entity, S extends EntityRenderState>
/*     */ {
/*     */   private static final float SHADOW_POWER_FALLOFF_Y = 0.5F;
/*     */   private static final float MAX_SHADOW_RADIUS = 32.0F;
/*     */   public static final float NAMETAG_SCALE = 0.025F;
/*     */   protected final EntityRenderDispatcher entityRenderDispatcher;
/*     */   private final Font font;
/*     */   protected float shadowRadius;
/*  43 */   protected float shadowStrength = 1.0F;
/*     */   
/*     */   protected EntityRenderer(EntityRendererProvider.Context context) {
/*  46 */     this.entityRenderDispatcher = context.getEntityRenderDispatcher();
/*  47 */     this.font = context.getFont();
/*     */   }
/*     */   
/*     */   public final int getPackedLightCoords(T entity, float partialTickTime) {
/*  51 */     BlockPos blockPos = BlockPos.containing((Position)entity.getLightProbePosition(partialTickTime));
/*  52 */     return LightTexture.pack(getBlockLightLevel(entity, blockPos), getSkyLightLevel(entity, blockPos));
/*     */   }
/*     */   
/*     */   protected int getSkyLightLevel(T entity, BlockPos blockPos) {
/*  56 */     return entity.level().getBrightness(LightLayer.SKY, blockPos);
/*     */   }
/*     */   
/*     */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/*  60 */     if (entity.isOnFire()) {
/*  61 */       return 15;
/*     */     }
/*  63 */     return entity.level().getBrightness(LightLayer.BLOCK, blockPos);
/*     */   }
/*     */   
/*     */   public boolean shouldRender(T entity, Frustum culler, double camX, double camY, double camZ) {
/*  67 */     if (!entity.shouldRender(camX, camY, camZ)) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!affectedByCulling(entity)) {
/*  71 */       return true;
/*     */     }
/*  73 */     AABB boundingBox = getBoundingBoxForCulling(entity).inflate(0.5D);
/*  74 */     if (boundingBox.hasNaN() || boundingBox.getSize() == 0.0D) {
/*  75 */       boundingBox = new AABB(entity.getX() - 2.0D, entity.getY() - 2.0D, entity.getZ() - 2.0D, entity.getX() + 2.0D, entity.getY() + 2.0D, entity.getZ() + 2.0D);
/*     */     }
/*  77 */     if (culler.isVisible(boundingBox)) {
/*  78 */       return true;
/*     */     }
/*     */     
/*  81 */     if (entity instanceof Leashable) { Leashable leashable = (Leashable)entity;
/*  82 */       Entity leashHolder = leashable.getLeashHolder();
/*  83 */       if (leashHolder != null) {
/*  84 */         AABB leasherBox = this.entityRenderDispatcher.<T>getRenderer((T)leashHolder).getBoundingBoxForCulling((T)leashHolder);
/*  85 */         return (culler.isVisible(leasherBox) || culler.isVisible(boundingBox.minmax(leasherBox)));
/*     */       }  }
/*     */     
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/*  92 */     return entity.getBoundingBox();
/*     */   }
/*     */   
/*     */   protected boolean affectedByCulling(T entity) {
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3 getRenderOffset(S state) {
/* 100 */     if (((EntityRenderState)state).passengerOffset != null) {
/* 101 */       return ((EntityRenderState)state).passengerOffset;
/*     */     }
/* 103 */     return Vec3.ZERO;
/*     */   }
/*     */   
/*     */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 107 */     if (((EntityRenderState)state).leashStates != null) {
/* 108 */       for (EntityRenderState.LeashState leashState : (Iterable<EntityRenderState.LeashState>)((EntityRenderState)state).leashStates) {
/* 109 */         submitNodeCollector.submitLeash(poseStack, leashState);
/*     */       }
/*     */     }
/*     */     
/* 113 */     submitNameTag(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   protected boolean shouldShowName(T entity, double distanceToCameraSq) {
/* 117 */     return (entity.shouldShowName() || (entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity));
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 121 */     return this.font;
/*     */   }
/*     */   
/*     */   protected void submitNameTag(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 125 */     if (((EntityRenderState)state).nameTag != null) {
/* 126 */       submitNodeCollector.submitNameTag(poseStack, ((EntityRenderState)state).nameTagAttachment, 0, ((EntityRenderState)state).nameTag, !((EntityRenderState)state).isDiscrete, ((EntityRenderState)state).lightCoords, ((EntityRenderState)state).distanceToCameraSq, camera);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Component getNameTag(T entity) {
/* 131 */     return entity.getDisplayName();
/*     */   }
/*     */   
/*     */   protected float getShadowRadius(S state) {
/* 135 */     return this.shadowRadius;
/*     */   }
/*     */   
/*     */   protected float getShadowStrength(S state) {
/* 139 */     return this.shadowStrength;
/*     */   }
/*     */   
/*     */   public abstract S createRenderState();
/*     */   
/*     */   public final S createRenderState(T entity, float partialTicks) {
/* 145 */     S state = createRenderState();
/* 146 */     extractRenderState(entity, state, partialTicks);
/*     */     
/* 148 */     finalizeRenderState(entity, state);
/* 149 */     return state;
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
/*     */     //   0: aload_2
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*     */     //   5: putfield entityType : Lnet/minecraft/world/entity/EntityType;
/*     */     //   8: aload_2
/*     */     //   9: fload_3
/*     */     //   10: f2d
/*     */     //   11: aload_1
/*     */     //   12: getfield xOld : D
/*     */     //   15: aload_1
/*     */     //   16: invokevirtual getX : ()D
/*     */     //   19: invokestatic lerp : (DDD)D
/*     */     //   22: putfield x : D
/*     */     //   25: aload_2
/*     */     //   26: fload_3
/*     */     //   27: f2d
/*     */     //   28: aload_1
/*     */     //   29: getfield yOld : D
/*     */     //   32: aload_1
/*     */     //   33: invokevirtual getY : ()D
/*     */     //   36: invokestatic lerp : (DDD)D
/*     */     //   39: putfield y : D
/*     */     //   42: aload_2
/*     */     //   43: fload_3
/*     */     //   44: f2d
/*     */     //   45: aload_1
/*     */     //   46: getfield zOld : D
/*     */     //   49: aload_1
/*     */     //   50: invokevirtual getZ : ()D
/*     */     //   53: invokestatic lerp : (DDD)D
/*     */     //   56: putfield z : D
/*     */     //   59: aload_2
/*     */     //   60: aload_1
/*     */     //   61: invokevirtual isInvisible : ()Z
/*     */     //   64: putfield isInvisible : Z
/*     */     //   67: aload_2
/*     */     //   68: aload_1
/*     */     //   69: getfield tickCount : I
/*     */     //   72: i2f
/*     */     //   73: fload_3
/*     */     //   74: fadd
/*     */     //   75: putfield ageInTicks : F
/*     */     //   78: aload_2
/*     */     //   79: aload_1
/*     */     //   80: invokevirtual getBbWidth : ()F
/*     */     //   83: putfield boundingBoxWidth : F
/*     */     //   86: aload_2
/*     */     //   87: aload_1
/*     */     //   88: invokevirtual getBbHeight : ()F
/*     */     //   91: putfield boundingBoxHeight : F
/*     */     //   94: aload_2
/*     */     //   95: aload_1
/*     */     //   96: invokevirtual getEyeHeight : ()F
/*     */     //   99: putfield eyeHeight : F
/*     */     //   102: aload_1
/*     */     //   103: invokevirtual isPassenger : ()Z
/*     */     //   106: ifeq -> 240
/*     */     //   109: aload_1
/*     */     //   110: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   113: astore #6
/*     */     //   115: aload #6
/*     */     //   117: instanceof net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   120: ifeq -> 240
/*     */     //   123: aload #6
/*     */     //   125: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*     */     //   128: astore #4
/*     */     //   130: aload #4
/*     */     //   132: invokevirtual getBehavior : ()Lnet/minecraft/world/entity/vehicle/minecart/MinecartBehavior;
/*     */     //   135: astore #6
/*     */     //   137: aload #6
/*     */     //   139: instanceof net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   142: ifeq -> 240
/*     */     //   145: aload #6
/*     */     //   147: checkcast net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   150: astore #5
/*     */     //   152: aload #5
/*     */     //   154: invokevirtual cartHasPosRotLerp : ()Z
/*     */     //   157: ifeq -> 240
/*     */     //   160: fload_3
/*     */     //   161: f2d
/*     */     //   162: aload #4
/*     */     //   164: getfield xOld : D
/*     */     //   167: aload #4
/*     */     //   169: invokevirtual getX : ()D
/*     */     //   172: invokestatic lerp : (DDD)D
/*     */     //   175: dstore #6
/*     */     //   177: fload_3
/*     */     //   178: f2d
/*     */     //   179: aload #4
/*     */     //   181: getfield yOld : D
/*     */     //   184: aload #4
/*     */     //   186: invokevirtual getY : ()D
/*     */     //   189: invokestatic lerp : (DDD)D
/*     */     //   192: dstore #8
/*     */     //   194: fload_3
/*     */     //   195: f2d
/*     */     //   196: aload #4
/*     */     //   198: getfield zOld : D
/*     */     //   201: aload #4
/*     */     //   203: invokevirtual getZ : ()D
/*     */     //   206: invokestatic lerp : (DDD)D
/*     */     //   209: dstore #10
/*     */     //   211: aload_2
/*     */     //   212: aload #5
/*     */     //   214: fload_3
/*     */     //   215: invokevirtual getCartLerpPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   218: new net/minecraft/world/phys/Vec3
/*     */     //   221: dup
/*     */     //   222: dload #6
/*     */     //   224: dload #8
/*     */     //   226: dload #10
/*     */     //   228: invokespecial <init> : (DDD)V
/*     */     //   231: invokevirtual subtract : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   234: putfield passengerOffset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   237: goto -> 245
/*     */     //   240: aload_2
/*     */     //   241: aconst_null
/*     */     //   242: putfield passengerOffset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   245: aload_0
/*     */     //   246: getfield entityRenderDispatcher : Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;
/*     */     //   249: getfield camera : Lnet/minecraft/client/Camera;
/*     */     //   252: ifnull -> 339
/*     */     //   255: aload_2
/*     */     //   256: aload_0
/*     */     //   257: getfield entityRenderDispatcher : Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;
/*     */     //   260: aload_1
/*     */     //   261: invokevirtual distanceToSqr : (Lnet/minecraft/world/entity/Entity;)D
/*     */     //   264: putfield distanceToCameraSq : D
/*     */     //   267: aload_2
/*     */     //   268: getfield distanceToCameraSq : D
/*     */     //   271: ldc2_w 4096.0
/*     */     //   274: dcmpg
/*     */     //   275: ifge -> 294
/*     */     //   278: aload_0
/*     */     //   279: aload_1
/*     */     //   280: aload_2
/*     */     //   281: getfield distanceToCameraSq : D
/*     */     //   284: invokevirtual shouldShowName : (Lnet/minecraft/world/entity/Entity;D)Z
/*     */     //   287: ifeq -> 294
/*     */     //   290: iconst_1
/*     */     //   291: goto -> 295
/*     */     //   294: iconst_0
/*     */     //   295: istore #4
/*     */     //   297: iload #4
/*     */     //   299: ifeq -> 334
/*     */     //   302: aload_2
/*     */     //   303: aload_0
/*     */     //   304: aload_1
/*     */     //   305: invokevirtual getNameTag : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/Component;
/*     */     //   308: putfield nameTag : Lnet/minecraft/network/chat/Component;
/*     */     //   311: aload_2
/*     */     //   312: aload_1
/*     */     //   313: invokevirtual getAttachments : ()Lnet/minecraft/world/entity/EntityAttachments;
/*     */     //   316: getstatic net/minecraft/world/entity/EntityAttachment.NAME_TAG : Lnet/minecraft/world/entity/EntityAttachment;
/*     */     //   319: iconst_0
/*     */     //   320: aload_1
/*     */     //   321: fload_3
/*     */     //   322: invokevirtual getYRot : (F)F
/*     */     //   325: invokevirtual getNullable : (Lnet/minecraft/world/entity/EntityAttachment;IF)Lnet/minecraft/world/phys/Vec3;
/*     */     //   328: putfield nameTagAttachment : Lnet/minecraft/world/phys/Vec3;
/*     */     //   331: goto -> 339
/*     */     //   334: aload_2
/*     */     //   335: aconst_null
/*     */     //   336: putfield nameTag : Lnet/minecraft/network/chat/Component;
/*     */     //   339: aload_2
/*     */     //   340: aload_1
/*     */     //   341: invokevirtual isDiscrete : ()Z
/*     */     //   344: putfield isDiscrete : Z
/*     */     //   347: aload_1
/*     */     //   348: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   351: astore #4
/*     */     //   353: aload_1
/*     */     //   354: instanceof net/minecraft/world/entity/Leashable
/*     */     //   357: ifeq -> 837
/*     */     //   360: aload_1
/*     */     //   361: checkcast net/minecraft/world/entity/Leashable
/*     */     //   364: astore #5
/*     */     //   366: aload #5
/*     */     //   368: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   373: astore #7
/*     */     //   375: aload #7
/*     */     //   377: instanceof net/minecraft/world/entity/Entity
/*     */     //   380: ifeq -> 837
/*     */     //   383: aload #7
/*     */     //   385: astore #6
/*     */     //   387: aload_1
/*     */     //   388: fload_3
/*     */     //   389: invokevirtual getPreciseBodyRotation : (F)F
/*     */     //   392: ldc_w 0.017453292
/*     */     //   395: fmul
/*     */     //   396: fstore #7
/*     */     //   398: aload #5
/*     */     //   400: fload_3
/*     */     //   401: invokeinterface getLeashOffset : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   406: astore #8
/*     */     //   408: aload_1
/*     */     //   409: fload_3
/*     */     //   410: invokevirtual getEyePosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   413: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   416: astore #9
/*     */     //   418: aload #6
/*     */     //   420: fload_3
/*     */     //   421: invokevirtual getEyePosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   424: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   427: astore #10
/*     */     //   429: aload_0
/*     */     //   430: aload_1
/*     */     //   431: aload #9
/*     */     //   433: invokevirtual getBlockLightLevel : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)I
/*     */     //   436: istore #11
/*     */     //   438: aload_0
/*     */     //   439: getfield entityRenderDispatcher : Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;
/*     */     //   442: aload #6
/*     */     //   444: invokevirtual getRenderer : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;
/*     */     //   447: aload #6
/*     */     //   449: aload #10
/*     */     //   451: invokevirtual getBlockLightLevel : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;)I
/*     */     //   454: istore #12
/*     */     //   456: aload #4
/*     */     //   458: getstatic net/minecraft/world/level/LightLayer.SKY : Lnet/minecraft/world/level/LightLayer;
/*     */     //   461: aload #9
/*     */     //   463: invokevirtual getBrightness : (Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I
/*     */     //   466: istore #13
/*     */     //   468: aload #4
/*     */     //   470: getstatic net/minecraft/world/level/LightLayer.SKY : Lnet/minecraft/world/level/LightLayer;
/*     */     //   473: aload #10
/*     */     //   475: invokevirtual getBrightness : (Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/BlockPos;)I
/*     */     //   478: istore #14
/*     */     //   480: aload #6
/*     */     //   482: invokevirtual supportQuadLeashAsHolder : ()Z
/*     */     //   485: ifeq -> 502
/*     */     //   488: aload #5
/*     */     //   490: invokeinterface supportQuadLeash : ()Z
/*     */     //   495: ifeq -> 502
/*     */     //   498: iconst_1
/*     */     //   499: goto -> 503
/*     */     //   502: iconst_0
/*     */     //   503: istore #15
/*     */     //   505: iload #15
/*     */     //   507: ifeq -> 514
/*     */     //   510: iconst_4
/*     */     //   511: goto -> 515
/*     */     //   514: iconst_1
/*     */     //   515: istore #16
/*     */     //   517: aload_2
/*     */     //   518: getfield leashStates : Ljava/util/List;
/*     */     //   521: ifnull -> 538
/*     */     //   524: aload_2
/*     */     //   525: getfield leashStates : Ljava/util/List;
/*     */     //   528: invokeinterface size : ()I
/*     */     //   533: iload #16
/*     */     //   535: if_icmpeq -> 584
/*     */     //   538: aload_2
/*     */     //   539: new java/util/ArrayList
/*     */     //   542: dup
/*     */     //   543: iload #16
/*     */     //   545: invokespecial <init> : (I)V
/*     */     //   548: putfield leashStates : Ljava/util/List;
/*     */     //   551: iconst_0
/*     */     //   552: istore #17
/*     */     //   554: iload #17
/*     */     //   556: iload #16
/*     */     //   558: if_icmpge -> 584
/*     */     //   561: aload_2
/*     */     //   562: getfield leashStates : Ljava/util/List;
/*     */     //   565: new net/minecraft/client/renderer/entity/state/EntityRenderState$LeashState
/*     */     //   568: dup
/*     */     //   569: invokespecial <init> : ()V
/*     */     //   572: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   577: pop
/*     */     //   578: iinc #17, 1
/*     */     //   581: goto -> 554
/*     */     //   584: iload #15
/*     */     //   586: ifeq -> 749
/*     */     //   589: aload #6
/*     */     //   591: fload_3
/*     */     //   592: invokevirtual getPreciseBodyRotation : (F)F
/*     */     //   595: ldc_w 0.017453292
/*     */     //   598: fmul
/*     */     //   599: fstore #17
/*     */     //   601: aload #6
/*     */     //   603: fload_3
/*     */     //   604: invokevirtual getPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   607: astore #18
/*     */     //   609: aload #5
/*     */     //   611: invokeinterface getQuadLeashOffsets : ()[Lnet/minecraft/world/phys/Vec3;
/*     */     //   616: astore #19
/*     */     //   618: aload #6
/*     */     //   620: invokevirtual getQuadLeashHolderOffsets : ()[Lnet/minecraft/world/phys/Vec3;
/*     */     //   623: astore #20
/*     */     //   625: iconst_0
/*     */     //   626: istore #21
/*     */     //   628: iload #21
/*     */     //   630: iload #16
/*     */     //   632: if_icmpge -> 746
/*     */     //   635: aload_2
/*     */     //   636: getfield leashStates : Ljava/util/List;
/*     */     //   639: iload #21
/*     */     //   641: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   646: checkcast net/minecraft/client/renderer/entity/state/EntityRenderState$LeashState
/*     */     //   649: astore #22
/*     */     //   651: aload #22
/*     */     //   653: aload #19
/*     */     //   655: iload #21
/*     */     //   657: aaload
/*     */     //   658: fload #7
/*     */     //   660: fneg
/*     */     //   661: invokevirtual yRot : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   664: putfield offset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   667: aload #22
/*     */     //   669: aload_1
/*     */     //   670: fload_3
/*     */     //   671: invokevirtual getPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   674: aload #22
/*     */     //   676: getfield offset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   679: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   682: putfield start : Lnet/minecraft/world/phys/Vec3;
/*     */     //   685: aload #22
/*     */     //   687: aload #18
/*     */     //   689: aload #20
/*     */     //   691: iload #21
/*     */     //   693: aaload
/*     */     //   694: fload #17
/*     */     //   696: fneg
/*     */     //   697: invokevirtual yRot : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   700: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   703: putfield end : Lnet/minecraft/world/phys/Vec3;
/*     */     //   706: aload #22
/*     */     //   708: iload #11
/*     */     //   710: putfield startBlockLight : I
/*     */     //   713: aload #22
/*     */     //   715: iload #12
/*     */     //   717: putfield endBlockLight : I
/*     */     //   720: aload #22
/*     */     //   722: iload #13
/*     */     //   724: putfield startSkyLight : I
/*     */     //   727: aload #22
/*     */     //   729: iload #14
/*     */     //   731: putfield endSkyLight : I
/*     */     //   734: aload #22
/*     */     //   736: iconst_0
/*     */     //   737: putfield slack : Z
/*     */     //   740: iinc #21, 1
/*     */     //   743: goto -> 628
/*     */     //   746: goto -> 834
/*     */     //   749: aload #8
/*     */     //   751: fload #7
/*     */     //   753: fneg
/*     */     //   754: invokevirtual yRot : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   757: astore #17
/*     */     //   759: aload_2
/*     */     //   760: getfield leashStates : Ljava/util/List;
/*     */     //   763: invokeinterface getFirst : ()Ljava/lang/Object;
/*     */     //   768: checkcast net/minecraft/client/renderer/entity/state/EntityRenderState$LeashState
/*     */     //   771: astore #18
/*     */     //   773: aload #18
/*     */     //   775: aload #17
/*     */     //   777: putfield offset : Lnet/minecraft/world/phys/Vec3;
/*     */     //   780: aload #18
/*     */     //   782: aload_1
/*     */     //   783: fload_3
/*     */     //   784: invokevirtual getPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   787: aload #17
/*     */     //   789: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   792: putfield start : Lnet/minecraft/world/phys/Vec3;
/*     */     //   795: aload #18
/*     */     //   797: aload #6
/*     */     //   799: fload_3
/*     */     //   800: invokevirtual getRopeHoldPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   803: putfield end : Lnet/minecraft/world/phys/Vec3;
/*     */     //   806: aload #18
/*     */     //   808: iload #11
/*     */     //   810: putfield startBlockLight : I
/*     */     //   813: aload #18
/*     */     //   815: iload #12
/*     */     //   817: putfield endBlockLight : I
/*     */     //   820: aload #18
/*     */     //   822: iload #13
/*     */     //   824: putfield startSkyLight : I
/*     */     //   827: aload #18
/*     */     //   829: iload #14
/*     */     //   831: putfield endSkyLight : I
/*     */     //   834: goto -> 842
/*     */     //   837: aload_2
/*     */     //   838: aconst_null
/*     */     //   839: putfield leashStates : Ljava/util/List;
/*     */     //   842: aload_2
/*     */     //   843: aload_1
/*     */     //   844: invokevirtual displayFireAnimation : ()Z
/*     */     //   847: putfield displayFireAnimation : Z
/*     */     //   850: invokestatic getInstance : ()Lnet/minecraft/client/Minecraft;
/*     */     //   853: astore #5
/*     */     //   855: aload #5
/*     */     //   857: aload_1
/*     */     //   858: invokevirtual shouldEntityAppearGlowing : (Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   861: istore #6
/*     */     //   863: aload_2
/*     */     //   864: iload #6
/*     */     //   866: ifeq -> 879
/*     */     //   869: aload_1
/*     */     //   870: invokevirtual getTeamColor : ()I
/*     */     //   873: invokestatic opaque : (I)I
/*     */     //   876: goto -> 880
/*     */     //   879: iconst_0
/*     */     //   880: putfield outlineColor : I
/*     */     //   883: aload_2
/*     */     //   884: aload_0
/*     */     //   885: aload_1
/*     */     //   886: fload_3
/*     */     //   887: invokevirtual getPackedLightCoords : (Lnet/minecraft/world/entity/Entity;F)I
/*     */     //   890: putfield lightCoords : I
/*     */     //   893: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #153	-> 0
/*     */     //   #154	-> 8
/*     */     //   #155	-> 25
/*     */     //   #156	-> 42
/*     */     //   #158	-> 59
/*     */     //   #159	-> 67
/*     */     //   #160	-> 78
/*     */     //   #161	-> 86
/*     */     //   #162	-> 94
/*     */     //   #164	-> 102
/*     */     //   #165	-> 109
/*     */     //   #166	-> 130
/*     */     //   #167	-> 154
/*     */     //   #168	-> 160
/*     */     //   #169	-> 177
/*     */     //   #170	-> 194
/*     */     //   #171	-> 211
/*     */     //   #172	-> 237
/*     */     //   #173	-> 240
/*     */     //   #176	-> 245
/*     */     //   #177	-> 255
/*     */     //   #178	-> 267
/*     */     //   #179	-> 297
/*     */     //   #180	-> 302
/*     */     //   #181	-> 311
/*     */     //   #183	-> 334
/*     */     //   #187	-> 339
/*     */     //   #189	-> 347
/*     */     //   #190	-> 353
/*     */     //   #191	-> 387
/*     */     //   #192	-> 398
/*     */     //   #194	-> 408
/*     */     //   #195	-> 418
/*     */     //   #197	-> 429
/*     */     //   #198	-> 438
/*     */     //   #199	-> 456
/*     */     //   #200	-> 468
/*     */     //   #202	-> 480
/*     */     //   #203	-> 505
/*     */     //   #204	-> 517
/*     */     //   #205	-> 538
/*     */     //   #206	-> 551
/*     */     //   #207	-> 561
/*     */     //   #206	-> 578
/*     */     //   #211	-> 584
/*     */     //   #213	-> 589
/*     */     //   #214	-> 601
/*     */     //   #215	-> 609
/*     */     //   #216	-> 618
/*     */     //   #217	-> 625
/*     */     //   #218	-> 635
/*     */     //   #219	-> 651
/*     */     //   #220	-> 667
/*     */     //   #221	-> 685
/*     */     //   #222	-> 706
/*     */     //   #223	-> 713
/*     */     //   #224	-> 720
/*     */     //   #225	-> 727
/*     */     //   #226	-> 734
/*     */     //   #217	-> 740
/*     */     //   #228	-> 746
/*     */     //   #229	-> 749
/*     */     //   #230	-> 759
/*     */     //   #231	-> 773
/*     */     //   #232	-> 780
/*     */     //   #233	-> 795
/*     */     //   #234	-> 806
/*     */     //   #235	-> 813
/*     */     //   #236	-> 820
/*     */     //   #237	-> 827
/*     */     //   #239	-> 834
/*     */     //   #240	-> 837
/*     */     //   #243	-> 842
/*     */     //   #245	-> 850
/*     */     //   #246	-> 855
/*     */     //   #247	-> 863
/*     */     //   #248	-> 883
/*     */     //   #249	-> 893
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   177	60	6	cartLerpX	D
/*     */     //   194	43	8	cartLerpY	D
/*     */     //   211	26	10	cartLerpZ	D
/*     */     //   130	110	4	minecart	Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;
/*     */     //   152	88	5	behavior	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;
/*     */     //   297	42	4	shouldShowName	Z
/*     */     //   554	30	17	i	I
/*     */     //   651	89	22	leashState	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$LeashState;
/*     */     //   628	118	21	i	I
/*     */     //   601	145	17	roperYRot	F
/*     */     //   609	137	18	holderPos	Lnet/minecraft/world/phys/Vec3;
/*     */     //   618	128	19	leashableAttachmentPoints	[Lnet/minecraft/world/phys/Vec3;
/*     */     //   625	121	20	roperAttachmentPoints	[Lnet/minecraft/world/phys/Vec3;
/*     */     //   759	75	17	rotatedAttachOffset	Lnet/minecraft/world/phys/Vec3;
/*     */     //   773	61	18	leashState	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$LeashState;
/*     */     //   398	436	7	entityYRot	F
/*     */     //   408	426	8	attachOffset	Lnet/minecraft/world/phys/Vec3;
/*     */     //   418	416	9	entityEyePos	Lnet/minecraft/core/BlockPos;
/*     */     //   429	405	10	roperEyePos	Lnet/minecraft/core/BlockPos;
/*     */     //   438	396	11	startBlockLight	I
/*     */     //   456	378	12	endBlockLight	I
/*     */     //   468	366	13	startSkyLight	I
/*     */     //   480	354	14	endSkyLight	I
/*     */     //   505	329	15	quadConnection	Z
/*     */     //   517	317	16	leashCount	I
/*     */     //   366	471	5	leashable	Lnet/minecraft/world/entity/Leashable;
/*     */     //   387	450	6	roper	Lnet/minecraft/world/entity/Entity;
/*     */     //   0	894	0	this	Lnet/minecraft/client/renderer/entity/EntityRenderer;
/*     */     //   0	894	1	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   0	894	2	state	Lnet/minecraft/client/renderer/entity/state/EntityRenderState;
/*     */     //   0	894	3	partialTicks	F
/*     */     //   353	541	4	level	Lnet/minecraft/world/level/Level;
/*     */     //   855	39	5	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   863	31	6	appearsGlowing	Z
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	894	0	this	Lnet/minecraft/client/renderer/entity/EntityRenderer<TT;TS;>;
/*     */     //   0	894	1	entity	TT;
/*     */     //   0	894	2	state	TS;
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
/*     */   protected void finalizeRenderState(T entity, S state) {
/* 252 */     Minecraft minecraft = Minecraft.getInstance();
/* 253 */     Level level = entity.level();
/* 254 */     extractShadow(state, minecraft, level);
/*     */   }
/*     */   
/*     */   private void extractShadow(S state, Minecraft minecraft, Level level) {
/* 258 */     ((EntityRenderState)state).shadowPieces.clear();
/* 259 */     if ((Boolean)minecraft.options.entityShadows().get() && !((EntityRenderState)state).isInvisible) {
/* 260 */       float shadowRadius = Math.min(getShadowRadius(state), 32.0F);
/* 261 */       ((EntityRenderState)state).shadowRadius = shadowRadius;
/*     */       
/* 263 */       if (shadowRadius > 0.0F) {
/* 264 */         double distSq = ((EntityRenderState)state).distanceToCameraSq;
/* 265 */         float pow = (float)((1.0D - distSq / 256.0D) * getShadowStrength(state));
/* 266 */         if (pow > 0.0F) {
/* 267 */           int x0 = Mth.floor(((EntityRenderState)state).x - shadowRadius);
/* 268 */           int x1 = Mth.floor(((EntityRenderState)state).x + shadowRadius);
/* 269 */           int z0 = Mth.floor(((EntityRenderState)state).z - shadowRadius);
/* 270 */           int z1 = Mth.floor(((EntityRenderState)state).z + shadowRadius);
/*     */ 
/*     */           
/* 273 */           float depth = Math.min(pow / 0.5F - 1.0F, shadowRadius);
/* 274 */           int y0 = Mth.floor(((EntityRenderState)state).y - depth);
/* 275 */           int y1 = Mth.floor(((EntityRenderState)state).y);
/*     */           
/* 277 */           BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 278 */           for (int z = z0; z <= z1; z++) {
/* 279 */             for (int x = x0; x <= x1; x++) {
/* 280 */               pos.set(x, 0, z);
/* 281 */               ChunkAccess chunk = level.getChunk((BlockPos)pos);
/* 282 */               for (int y = y0; y <= y1; y++) {
/* 283 */                 pos.setY(y);
/* 284 */                 extractShadowPiece(state, level, pow, pos, chunk);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 291 */       ((EntityRenderState)state).shadowRadius = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractShadowPiece(S state, Level level, float pow, BlockPos.MutableBlockPos pos, ChunkAccess chunk) {
/* 296 */     float powerAtDepth = pow - (float)(((EntityRenderState)state).y - pos.getY()) * 0.5F;
/* 297 */     BlockPos belowPos = pos.below();
/* 298 */     BlockState belowState = chunk.getBlockState(belowPos);
/* 299 */     if (belowState.getRenderShape() == RenderShape.INVISIBLE) {
/*     */       return;
/*     */     }
/*     */     
/* 303 */     int brightness = level.getMaxLocalRawBrightness((BlockPos)pos);
/* 304 */     if (brightness <= 3) {
/*     */       return;
/*     */     }
/*     */     
/* 308 */     if (!belowState.isCollisionShapeFullBlock((BlockGetter)chunk, belowPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 312 */     VoxelShape belowShape = belowState.getShape((BlockGetter)chunk, belowPos);
/* 313 */     if (belowShape.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 318 */     float alpha = Mth.clamp(powerAtDepth * 0.5F * LightTexture.getBrightness(level.dimensionType(), brightness), 0.0F, 1.0F);
/* 319 */     float relativeX = (float)(pos.getX() - ((EntityRenderState)state).x);
/* 320 */     float relativeY = (float)(pos.getY() - ((EntityRenderState)state).y);
/* 321 */     float relativeZ = (float)(pos.getZ() - ((EntityRenderState)state).z);
/* 322 */     ((EntityRenderState)state).shadowPieces.add(new EntityRenderState.ShadowPiece(relativeX, relativeY, relativeZ, belowShape, alpha));
/*     */   }
/*     */   
/*     */   private static Entity getServerSideEntity(Entity entity) {
/* 326 */     IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
/* 327 */     if (server != null) {
/* 328 */       ServerLevel level = server.getLevel(entity.level().dimension());
/* 329 */       if (level != null) {
/* 330 */         return level.getEntity(entity.getId());
/*     */       }
/*     */     } 
/* 333 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/EntityRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */