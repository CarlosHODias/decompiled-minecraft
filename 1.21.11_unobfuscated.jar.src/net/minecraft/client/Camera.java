/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeProbe;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Camera
/*     */   implements TrackedWaypoint.Camera
/*     */ {
/*     */   private static final float DEFAULT_CAMERA_DISTANCE = 4.0F;
/*  33 */   private static final Vector3f FORWARDS = new Vector3f(0.0F, 0.0F, -1.0F);
/*  34 */   private static final Vector3f UP = new Vector3f(0.0F, 1.0F, 0.0F);
/*  35 */   private static final Vector3f LEFT = new Vector3f(-1.0F, 0.0F, 0.0F);
/*     */   
/*     */   private boolean initialized;
/*     */   private Level level;
/*     */   private Entity entity;
/*  40 */   private Vec3 position = Vec3.ZERO;
/*  41 */   private final BlockPos.MutableBlockPos blockPosition = new BlockPos.MutableBlockPos();
/*  42 */   private final Vector3f forwards = new Vector3f((Vector3fc)FORWARDS);
/*  43 */   private final Vector3f up = new Vector3f((Vector3fc)UP);
/*  44 */   private final Vector3f left = new Vector3f((Vector3fc)LEFT);
/*     */   private float xRot;
/*     */   private float yRot;
/*  47 */   private final Quaternionf rotation = new Quaternionf();
/*     */   
/*     */   private boolean detached;
/*     */   private float eyeHeight;
/*     */   private float eyeHeightOld;
/*     */   private float partialTickTime;
/*  53 */   private final EnvironmentAttributeProbe attributeProbe = new EnvironmentAttributeProbe();
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
/*     */   public void setup(Level level, Entity entity, boolean detached, boolean mirror, float a) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_1
/*     */     //   2: putfield initialized : Z
/*     */     //   5: aload_0
/*     */     //   6: aload_1
/*     */     //   7: putfield level : Lnet/minecraft/world/level/Level;
/*     */     //   10: aload_0
/*     */     //   11: aload_2
/*     */     //   12: putfield entity : Lnet/minecraft/world/entity/Entity;
/*     */     //   15: aload_0
/*     */     //   16: iload_3
/*     */     //   17: putfield detached : Z
/*     */     //   20: aload_0
/*     */     //   21: fload #5
/*     */     //   23: putfield partialTickTime : F
/*     */     //   26: aload_2
/*     */     //   27: invokevirtual isPassenger : ()Z
/*     */     //   30: ifeq -> 170
/*     */     //   33: aload_2
/*     */     //   34: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   37: astore #8
/*     */     //   39: aload #8
/*     */     //   41: instanceof net/minecraft/world/entity/vehicle/minecart/Minecart
/*     */     //   44: ifeq -> 170
/*     */     //   47: aload #8
/*     */     //   49: checkcast net/minecraft/world/entity/vehicle/minecart/Minecart
/*     */     //   52: astore #6
/*     */     //   54: aload #6
/*     */     //   56: invokevirtual getBehavior : ()Lnet/minecraft/world/entity/vehicle/minecart/MinecartBehavior;
/*     */     //   59: astore #8
/*     */     //   61: aload #8
/*     */     //   63: instanceof net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   66: ifeq -> 170
/*     */     //   69: aload #8
/*     */     //   71: checkcast net/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior
/*     */     //   74: astore #7
/*     */     //   76: aload #7
/*     */     //   78: invokevirtual cartHasPosRotLerp : ()Z
/*     */     //   81: ifeq -> 170
/*     */     //   84: aload #6
/*     */     //   86: aload_2
/*     */     //   87: invokevirtual getPassengerRidingPosition : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   90: aload #6
/*     */     //   92: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   95: invokevirtual subtract : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   98: aload_2
/*     */     //   99: aload #6
/*     */     //   101: invokevirtual getVehicleAttachmentPoint : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   104: invokevirtual subtract : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   107: new net/minecraft/world/phys/Vec3
/*     */     //   110: dup
/*     */     //   111: dconst_0
/*     */     //   112: fload #5
/*     */     //   114: aload_0
/*     */     //   115: getfield eyeHeightOld : F
/*     */     //   118: aload_0
/*     */     //   119: getfield eyeHeight : F
/*     */     //   122: invokestatic lerp : (FFF)F
/*     */     //   125: f2d
/*     */     //   126: dconst_0
/*     */     //   127: invokespecial <init> : (DDD)V
/*     */     //   130: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   133: astore #8
/*     */     //   135: aload_0
/*     */     //   136: aload_2
/*     */     //   137: fload #5
/*     */     //   139: invokevirtual getViewYRot : (F)F
/*     */     //   142: aload_2
/*     */     //   143: fload #5
/*     */     //   145: invokevirtual getViewXRot : (F)F
/*     */     //   148: invokevirtual setRotation : (FF)V
/*     */     //   151: aload_0
/*     */     //   152: aload #7
/*     */     //   154: fload #5
/*     */     //   156: invokevirtual getCartLerpPosition : (F)Lnet/minecraft/world/phys/Vec3;
/*     */     //   159: aload #8
/*     */     //   161: invokevirtual add : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;
/*     */     //   164: invokevirtual setPosition : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   167: goto -> 247
/*     */     //   170: aload_0
/*     */     //   171: aload_2
/*     */     //   172: fload #5
/*     */     //   174: invokevirtual getViewYRot : (F)F
/*     */     //   177: aload_2
/*     */     //   178: fload #5
/*     */     //   180: invokevirtual getViewXRot : (F)F
/*     */     //   183: invokevirtual setRotation : (FF)V
/*     */     //   186: aload_0
/*     */     //   187: fload #5
/*     */     //   189: f2d
/*     */     //   190: aload_2
/*     */     //   191: getfield xo : D
/*     */     //   194: aload_2
/*     */     //   195: invokevirtual getX : ()D
/*     */     //   198: invokestatic lerp : (DDD)D
/*     */     //   201: fload #5
/*     */     //   203: f2d
/*     */     //   204: aload_2
/*     */     //   205: getfield yo : D
/*     */     //   208: aload_2
/*     */     //   209: invokevirtual getY : ()D
/*     */     //   212: invokestatic lerp : (DDD)D
/*     */     //   215: fload #5
/*     */     //   217: aload_0
/*     */     //   218: getfield eyeHeightOld : F
/*     */     //   221: aload_0
/*     */     //   222: getfield eyeHeight : F
/*     */     //   225: invokestatic lerp : (FFF)F
/*     */     //   228: f2d
/*     */     //   229: dadd
/*     */     //   230: fload #5
/*     */     //   232: f2d
/*     */     //   233: aload_2
/*     */     //   234: getfield zo : D
/*     */     //   237: aload_2
/*     */     //   238: invokevirtual getZ : ()D
/*     */     //   241: invokestatic lerp : (DDD)D
/*     */     //   244: invokevirtual setPosition : (DDD)V
/*     */     //   247: iload_3
/*     */     //   248: ifeq -> 391
/*     */     //   251: iload #4
/*     */     //   253: ifeq -> 272
/*     */     //   256: aload_0
/*     */     //   257: aload_0
/*     */     //   258: getfield yRot : F
/*     */     //   261: ldc 180.0
/*     */     //   263: fadd
/*     */     //   264: aload_0
/*     */     //   265: getfield xRot : F
/*     */     //   268: fneg
/*     */     //   269: invokevirtual setRotation : (FF)V
/*     */     //   272: ldc 4.0
/*     */     //   274: fstore #6
/*     */     //   276: fconst_1
/*     */     //   277: fstore #7
/*     */     //   279: aload_2
/*     */     //   280: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   283: ifeq -> 310
/*     */     //   286: aload_2
/*     */     //   287: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   290: astore #8
/*     */     //   292: aload #8
/*     */     //   294: invokevirtual getScale : ()F
/*     */     //   297: fstore #7
/*     */     //   299: aload #8
/*     */     //   301: getstatic net/minecraft/world/entity/ai/attributes/Attributes.CAMERA_DISTANCE : Lnet/minecraft/core/Holder;
/*     */     //   304: invokevirtual getAttributeValue : (Lnet/minecraft/core/Holder;)D
/*     */     //   307: d2f
/*     */     //   308: fstore #6
/*     */     //   310: fload #7
/*     */     //   312: fstore #8
/*     */     //   314: fload #6
/*     */     //   316: fstore #9
/*     */     //   318: aload_2
/*     */     //   319: invokevirtual isPassenger : ()Z
/*     */     //   322: ifeq -> 364
/*     */     //   325: aload_2
/*     */     //   326: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   329: astore #11
/*     */     //   331: aload #11
/*     */     //   333: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   336: ifeq -> 364
/*     */     //   339: aload #11
/*     */     //   341: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   344: astore #10
/*     */     //   346: aload #10
/*     */     //   348: invokevirtual getScale : ()F
/*     */     //   351: fstore #8
/*     */     //   353: aload #10
/*     */     //   355: getstatic net/minecraft/world/entity/ai/attributes/Attributes.CAMERA_DISTANCE : Lnet/minecraft/core/Holder;
/*     */     //   358: invokevirtual getAttributeValue : (Lnet/minecraft/core/Holder;)D
/*     */     //   361: d2f
/*     */     //   362: fstore #9
/*     */     //   364: aload_0
/*     */     //   365: aload_0
/*     */     //   366: fload #7
/*     */     //   368: fload #6
/*     */     //   370: fmul
/*     */     //   371: fload #8
/*     */     //   373: fload #9
/*     */     //   375: fmul
/*     */     //   376: invokestatic max : (FF)F
/*     */     //   379: invokevirtual getMaxZoom : (F)F
/*     */     //   382: fneg
/*     */     //   383: fconst_0
/*     */     //   384: fconst_0
/*     */     //   385: invokevirtual move : (FFF)V
/*     */     //   388: goto -> 447
/*     */     //   391: aload_2
/*     */     //   392: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   395: ifeq -> 447
/*     */     //   398: aload_2
/*     */     //   399: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   402: invokevirtual isSleeping : ()Z
/*     */     //   405: ifeq -> 447
/*     */     //   408: aload_2
/*     */     //   409: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   412: invokevirtual getBedOrientation : ()Lnet/minecraft/core/Direction;
/*     */     //   415: astore #6
/*     */     //   417: aload_0
/*     */     //   418: aload #6
/*     */     //   420: ifnull -> 434
/*     */     //   423: aload #6
/*     */     //   425: invokevirtual toYRot : ()F
/*     */     //   428: ldc 180.0
/*     */     //   430: fsub
/*     */     //   431: goto -> 435
/*     */     //   434: fconst_0
/*     */     //   435: fconst_0
/*     */     //   436: invokevirtual setRotation : (FF)V
/*     */     //   439: aload_0
/*     */     //   440: fconst_0
/*     */     //   441: ldc 0.3
/*     */     //   443: fconst_0
/*     */     //   444: invokevirtual move : (FFF)V
/*     */     //   447: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #56	-> 0
/*     */     //   #57	-> 5
/*     */     //   #58	-> 10
/*     */     //   #59	-> 15
/*     */     //   #60	-> 20
/*     */     //   #62	-> 26
/*     */     //   #63	-> 54
/*     */     //   #64	-> 78
/*     */     //   #67	-> 84
/*     */     //   #68	-> 92
/*     */     //   #69	-> 101
/*     */     //   #70	-> 122
/*     */     //   #72	-> 135
/*     */     //   #73	-> 151
/*     */     //   #74	-> 167
/*     */     //   #75	-> 170
/*     */     //   #76	-> 186
/*     */     //   #78	-> 247
/*     */     //   #79	-> 251
/*     */     //   #80	-> 256
/*     */     //   #82	-> 272
/*     */     //   #83	-> 276
/*     */     //   #84	-> 279
/*     */     //   #85	-> 292
/*     */     //   #86	-> 299
/*     */     //   #88	-> 310
/*     */     //   #89	-> 314
/*     */     //   #90	-> 318
/*     */     //   #91	-> 346
/*     */     //   #92	-> 353
/*     */     //   #94	-> 364
/*     */     //   #95	-> 388
/*     */     //   #96	-> 408
/*     */     //   #97	-> 417
/*     */     //   #98	-> 439
/*     */     //   #100	-> 447
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   135	32	8	positionOffset	Lnet/minecraft/world/phys/Vec3;
/*     */     //   54	116	6	minecart	Lnet/minecraft/world/entity/vehicle/minecart/Minecart;
/*     */     //   76	94	7	behavior	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;
/*     */     //   292	18	8	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   346	18	10	mount	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   276	112	6	cameraDistance	F
/*     */     //   279	109	7	cameraScale	F
/*     */     //   314	74	8	mountScale	F
/*     */     //   318	70	9	mountDistance	F
/*     */     //   417	30	6	bedOrientation	Lnet/minecraft/core/Direction;
/*     */     //   0	448	0	this	Lnet/minecraft/client/Camera;
/*     */     //   0	448	1	level	Lnet/minecraft/world/level/Level;
/*     */     //   0	448	2	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   0	448	3	detached	Z
/*     */     //   0	448	4	mirror	Z
/*     */     //   0	448	5	a	F
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
/*     */   public void tick() {
/* 103 */     if (this.entity != null) {
/* 104 */       this.eyeHeightOld = this.eyeHeight;
/* 105 */       this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
/* 106 */       this.attributeProbe.tick(this.level, this.position);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getMaxZoom(float cameraDist) {
/* 111 */     float jitterScale = 0.1F;
/* 112 */     for (int i = 0; i < 8; i++) {
/* 113 */       float offsetX = ((i & 0x1) * 2 - 1);
/* 114 */       float offsetY = ((i >> 1 & 0x1) * 2 - 1);
/* 115 */       float offsetZ = ((i >> 2 & 0x1) * 2 - 1);
/* 116 */       Vec3 from = this.position.add((offsetX * 0.1F), (offsetY * 0.1F), (offsetZ * 0.1F));
/* 117 */       Vec3 to = from.add(new Vec3((Vector3fc)this.forwards).scale(-cameraDist));
/* 118 */       BlockHitResult blockHitResult = this.level.clip(new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, this.entity));
/* 119 */       if (blockHitResult.getType() != HitResult.Type.MISS) {
/* 120 */         float distSq = (float)blockHitResult.getLocation().distanceToSqr(this.position);
/* 121 */         if (distSq < Mth.square(cameraDist)) {
/* 122 */           cameraDist = Mth.sqrt(distSq);
/*     */         }
/*     */       } 
/*     */     } 
/* 126 */     return cameraDist;
/*     */   }
/*     */   
/*     */   protected void move(float forwards, float up, float right) {
/* 130 */     Vector3f offset = new Vector3f(right, up, -forwards).rotate((Quaternionfc)this.rotation);
/* 131 */     setPosition(new Vec3(this.position.x + offset.x, this.position.y + offset.y, this.position.z + offset.z));
/*     */   }
/*     */   
/*     */   protected void setRotation(float yRot, float xRot) {
/* 135 */     this.xRot = xRot;
/* 136 */     this.yRot = yRot;
/*     */     
/* 138 */     this.rotation.rotationYXZ(3.1415927F - yRot * 0.017453292F, -xRot * 0.017453292F, 0.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     FORWARDS.rotate((Quaternionfc)this.rotation, this.forwards);
/* 145 */     UP.rotate((Quaternionfc)this.rotation, this.up);
/* 146 */     LEFT.rotate((Quaternionfc)this.rotation, this.left);
/*     */   }
/*     */   
/*     */   protected void setPosition(double x, double y, double z) {
/* 150 */     setPosition(new Vec3(x, y, z));
/*     */   }
/*     */   
/*     */   protected void setPosition(Vec3 position) {
/* 154 */     this.position = position;
/* 155 */     this.blockPosition.set(position.x, position.y, position.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 position() {
/* 160 */     return this.position;
/*     */   }
/*     */   
/*     */   public BlockPos blockPosition() {
/* 164 */     return (BlockPos)this.blockPosition;
/*     */   }
/*     */   
/*     */   public float xRot() {
/* 168 */     return this.xRot;
/*     */   }
/*     */   
/*     */   public float yRot() {
/* 172 */     return this.yRot;
/*     */   }
/*     */ 
/*     */   
/*     */   public float yaw() {
/* 177 */     return Mth.wrapDegrees(yRot());
/*     */   }
/*     */   
/*     */   public Quaternionf rotation() {
/* 181 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Entity entity() {
/* 185 */     return this.entity;
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/* 189 */     return this.initialized;
/*     */   }
/*     */   
/*     */   public boolean isDetached() {
/* 193 */     return this.detached;
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeProbe attributeProbe() {
/* 197 */     return this.attributeProbe;
/*     */   }
/*     */   
/*     */   public NearPlane getNearPlane() {
/* 201 */     Minecraft minecraft = Minecraft.getInstance();
/* 202 */     double aspectRatio = minecraft.getWindow().getWidth() / minecraft.getWindow().getHeight();
/* 203 */     double planeHeight = Math.tan(((Integer)minecraft.options.fov().get() * 0.017453292F) / 2.0D) * 0.05000000074505806D;
/* 204 */     double planeWidth = planeHeight * aspectRatio;
/*     */     
/* 206 */     Vec3 forwardsVec3 = new Vec3((Vector3fc)this.forwards).scale(0.05000000074505806D);
/* 207 */     Vec3 leftVec3 = new Vec3((Vector3fc)this.left).scale(planeWidth);
/* 208 */     Vec3 upVec3 = new Vec3((Vector3fc)this.up).scale(planeHeight);
/*     */     
/* 210 */     return new NearPlane(forwardsVec3, leftVec3, upVec3);
/*     */   }
/*     */   
/*     */   public FogType getFluidInCamera() {
/* 214 */     if (!this.initialized) {
/* 215 */       return FogType.NONE;
/*     */     }
/*     */     
/* 218 */     FluidState fluidState1 = this.level.getFluidState((BlockPos)this.blockPosition);
/* 219 */     if (fluidState1.is(FluidTags.WATER) && 
/* 220 */       this.position.y < (this.blockPosition.getY() + fluidState1.getHeight((BlockGetter)this.level, (BlockPos)this.blockPosition))) {
/* 221 */       return FogType.WATER;
/*     */     }
/*     */ 
/*     */     
/* 225 */     NearPlane plane = getNearPlane();
/*     */ 
/*     */     
/* 228 */     List<Vec3> points = Arrays.asList(new Vec3[] { plane.forward, plane.getTopLeft(), plane.getTopRight(), plane.getBottomLeft(), plane.getBottomRight() });
/* 229 */     for (Vec3 point : points) {
/* 230 */       Vec3 offsetPos = this.position.add(point);
/* 231 */       BlockPos checkPos = BlockPos.containing((Position)offsetPos);
/* 232 */       FluidState fluidState = this.level.getFluidState(checkPos);
/* 233 */       if (fluidState.is(FluidTags.LAVA)) {
/* 234 */         if (offsetPos.y <= (fluidState.getHeight((BlockGetter)this.level, checkPos) + checkPos.getY()))
/* 235 */           return FogType.LAVA; 
/*     */         continue;
/*     */       } 
/* 238 */       BlockState state = this.level.getBlockState(checkPos);
/* 239 */       if (state.is(Blocks.POWDER_SNOW)) {
/* 240 */         return FogType.POWDER_SNOW;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 245 */     return FogType.NONE;
/*     */   }
/*     */   
/*     */   public Vector3fc forwardVector() {
/* 249 */     return (Vector3fc)this.forwards;
/*     */   }
/*     */   
/*     */   public Vector3fc upVector() {
/* 253 */     return (Vector3fc)this.up;
/*     */   }
/*     */   
/*     */   public Vector3fc leftVector() {
/* 257 */     return (Vector3fc)this.left;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 261 */     this.level = null;
/* 262 */     this.entity = null;
/* 263 */     this.attributeProbe.reset();
/* 264 */     this.initialized = false;
/*     */   }
/*     */   
/*     */   public float getPartialTickTime() {
/* 268 */     return this.partialTickTime;
/*     */   }
/*     */   
/*     */   public static class NearPlane {
/*     */     private final Vec3 forward;
/*     */     private final Vec3 left;
/*     */     private final Vec3 up;
/*     */     
/*     */     private NearPlane(Vec3 forward, Vec3 left, Vec3 up) {
/* 277 */       this.forward = forward;
/* 278 */       this.left = left;
/* 279 */       this.up = up;
/*     */     }
/*     */     
/*     */     public Vec3 getTopLeft() {
/* 283 */       return this.forward.add(this.up).add(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getTopRight() {
/* 287 */       return this.forward.add(this.up).subtract(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getBottomLeft() {
/* 291 */       return this.forward.subtract(this.up).add(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getBottomRight() {
/* 295 */       return this.forward.subtract(this.up).subtract(this.left);
/*     */     }
/*     */     
/*     */     public Vec3 getPointOnPlane(float x, float y) {
/* 299 */       return this.forward.add(this.up.scale(y)).subtract(this.left.scale(x));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/Camera.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */