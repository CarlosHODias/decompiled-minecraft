/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
/*     */ import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.BinaryHeap;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class EnderDragon
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  60 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  62 */   public static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.INT);
/*     */   
/*  64 */   private static final TargetingConditions CRYSTAL_DESTROY_TARGETING = TargetingConditions.forCombat().range(64.0D);
/*     */   
/*     */   private static final int GROWL_INTERVAL_MIN = 200;
/*     */   
/*     */   private static final int GROWL_INTERVAL_MAX = 400;
/*     */   private static final float SITTING_ALLOWED_DAMAGE_PERCENTAGE = 0.25F;
/*     */   private static final String DRAGON_DEATH_TIME_KEY = "DragonDeathTime";
/*     */   private static final String DRAGON_PHASE_KEY = "DragonPhase";
/*     */   private static final int DEFAULT_DEATH_TIME = 0;
/*  73 */   public final DragonFlightHistory flightHistory = new DragonFlightHistory();
/*     */   
/*     */   private final EnderDragonPart[] subEntities;
/*     */   
/*     */   public final EnderDragonPart head;
/*     */   private final EnderDragonPart neck;
/*     */   private final EnderDragonPart body;
/*     */   private final EnderDragonPart tail1;
/*     */   private final EnderDragonPart tail2;
/*     */   private final EnderDragonPart tail3;
/*     */   private final EnderDragonPart wing1;
/*     */   private final EnderDragonPart wing2;
/*     */   public float oFlapTime;
/*     */   public float flapTime;
/*     */   public boolean inWall;
/*  88 */   public int dragonDeathTime = 0;
/*     */   
/*     */   public float yRotA;
/*     */   
/*     */   public EndCrystal nearestCrystal;
/*     */   private EndDragonFight dragonFight;
/*  94 */   private BlockPos fightOrigin = BlockPos.ZERO;
/*     */   private final EnderDragonPhaseManager phaseManager;
/*  96 */   private int growlTime = 100;
/*     */   private float sittingDamageReceived;
/*  98 */   private final Node[] nodes = new Node[24];
/*  99 */   private final int[] nodeAdjacency = new int[24];
/* 100 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   public EnderDragon(EntityType<? extends EnderDragon> type, Level level) {
/* 103 */     super(EntityType.ENDER_DRAGON, level);
/*     */     
/* 105 */     this.head = new EnderDragonPart(this, "head", 1.0F, 1.0F);
/* 106 */     this.neck = new EnderDragonPart(this, "neck", 3.0F, 3.0F);
/* 107 */     this.body = new EnderDragonPart(this, "body", 5.0F, 3.0F);
/* 108 */     this.tail1 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 109 */     this.tail2 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 110 */     this.tail3 = new EnderDragonPart(this, "tail", 2.0F, 2.0F);
/* 111 */     this.wing1 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/* 112 */     this.wing2 = new EnderDragonPart(this, "wing", 4.0F, 2.0F);
/*     */     
/* 114 */     this.subEntities = new EnderDragonPart[] { this.head, this.neck, this.body, this.tail1, this.tail2, this.tail3, this.wing1, this.wing2 };
/*     */     
/* 116 */     setHealth(getMaxHealth());
/*     */     
/* 118 */     this.noPhysics = true;
/*     */     
/* 120 */     this.phaseManager = new EnderDragonPhaseManager(this);
/*     */   }
/*     */   
/*     */   public void setDragonFight(EndDragonFight fight) {
/* 124 */     this.dragonFight = fight;
/*     */   }
/*     */   
/*     */   public void setFightOrigin(BlockPos fightOrigin) {
/* 128 */     this.fightOrigin = fightOrigin;
/*     */   }
/*     */   
/*     */   public BlockPos getFightOrigin() {
/* 132 */     return this.fightOrigin;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 136 */     return Mob.createMobAttributes()
/* 137 */       .add(Attributes.MAX_HEALTH, 200.0D)
/* 138 */       .add(Attributes.CAMERA_DISTANCE, 16.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlapping() {
/* 143 */     float flap = Mth.cos((this.flapTime * 6.2831855F));
/* 144 */     float oldFlap = Mth.cos((this.oFlapTime * 6.2831855F));
/*     */     
/* 146 */     return (oldFlap <= -0.3F && flap >= -0.3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFlap() {
/* 151 */     if (level().isClientSide() && !isSilent()) {
/* 152 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_FLAP, getSoundSource(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 158 */     super.defineSynchedData(entityData);
/* 159 */     entityData.define(DATA_PHASE, EnderDragonPhase.HOVERING.getId());
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
/*     */   public void aiStep() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual processFlappingMovement : ()V
/*     */     //   4: aload_0
/*     */     //   5: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   8: invokevirtual isClientSide : ()Z
/*     */     //   11: ifeq -> 123
/*     */     //   14: aload_0
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual getHealth : ()F
/*     */     //   19: invokevirtual setHealth : (F)V
/*     */     //   22: aload_0
/*     */     //   23: invokevirtual isSilent : ()Z
/*     */     //   26: ifne -> 123
/*     */     //   29: aload_0
/*     */     //   30: getfield phaseManager : Lnet/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager;
/*     */     //   33: invokevirtual getCurrentPhase : ()Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   36: invokeinterface isSitting : ()Z
/*     */     //   41: ifne -> 123
/*     */     //   44: aload_0
/*     */     //   45: dup
/*     */     //   46: getfield growlTime : I
/*     */     //   49: iconst_1
/*     */     //   50: isub
/*     */     //   51: dup_x1
/*     */     //   52: putfield growlTime : I
/*     */     //   55: ifge -> 123
/*     */     //   58: aload_0
/*     */     //   59: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   62: aload_0
/*     */     //   63: invokevirtual getX : ()D
/*     */     //   66: aload_0
/*     */     //   67: invokevirtual getY : ()D
/*     */     //   70: aload_0
/*     */     //   71: invokevirtual getZ : ()D
/*     */     //   74: getstatic net/minecraft/sounds/SoundEvents.ENDER_DRAGON_GROWL : Lnet/minecraft/sounds/SoundEvent;
/*     */     //   77: aload_0
/*     */     //   78: invokevirtual getSoundSource : ()Lnet/minecraft/sounds/SoundSource;
/*     */     //   81: ldc_w 2.5
/*     */     //   84: ldc 0.8
/*     */     //   86: aload_0
/*     */     //   87: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   90: invokeinterface nextFloat : ()F
/*     */     //   95: ldc 0.3
/*     */     //   97: fmul
/*     */     //   98: fadd
/*     */     //   99: iconst_0
/*     */     //   100: invokevirtual playLocalSound : (DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V
/*     */     //   103: aload_0
/*     */     //   104: sipush #200
/*     */     //   107: aload_0
/*     */     //   108: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   111: sipush #200
/*     */     //   114: invokeinterface nextInt : (I)I
/*     */     //   119: iadd
/*     */     //   120: putfield growlTime : I
/*     */     //   123: aload_0
/*     */     //   124: getfield dragonFight : Lnet/minecraft/world/level/dimension/end/EndDragonFight;
/*     */     //   127: ifnonnull -> 175
/*     */     //   130: aload_0
/*     */     //   131: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   134: astore_2
/*     */     //   135: aload_2
/*     */     //   136: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   139: ifeq -> 175
/*     */     //   142: aload_2
/*     */     //   143: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   146: astore_1
/*     */     //   147: aload_1
/*     */     //   148: invokevirtual getDragonFight : ()Lnet/minecraft/world/level/dimension/end/EndDragonFight;
/*     */     //   151: astore_2
/*     */     //   152: aload_2
/*     */     //   153: ifnull -> 175
/*     */     //   156: aload_0
/*     */     //   157: invokevirtual getUUID : ()Ljava/util/UUID;
/*     */     //   160: aload_2
/*     */     //   161: invokevirtual getDragonUUID : ()Ljava/util/UUID;
/*     */     //   164: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   167: ifeq -> 175
/*     */     //   170: aload_0
/*     */     //   171: aload_2
/*     */     //   172: putfield dragonFight : Lnet/minecraft/world/level/dimension/end/EndDragonFight;
/*     */     //   175: aload_0
/*     */     //   176: aload_0
/*     */     //   177: getfield flapTime : F
/*     */     //   180: putfield oFlapTime : F
/*     */     //   183: aload_0
/*     */     //   184: invokevirtual isDeadOrDying : ()Z
/*     */     //   187: ifeq -> 282
/*     */     //   190: aload_0
/*     */     //   191: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   194: invokeinterface nextFloat : ()F
/*     */     //   199: ldc_w 0.5
/*     */     //   202: fsub
/*     */     //   203: ldc_w 8.0
/*     */     //   206: fmul
/*     */     //   207: fstore_1
/*     */     //   208: aload_0
/*     */     //   209: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   212: invokeinterface nextFloat : ()F
/*     */     //   217: ldc_w 0.5
/*     */     //   220: fsub
/*     */     //   221: ldc 4.0
/*     */     //   223: fmul
/*     */     //   224: fstore_2
/*     */     //   225: aload_0
/*     */     //   226: getfield random : Lnet/minecraft/util/RandomSource;
/*     */     //   229: invokeinterface nextFloat : ()F
/*     */     //   234: ldc_w 0.5
/*     */     //   237: fsub
/*     */     //   238: ldc_w 8.0
/*     */     //   241: fmul
/*     */     //   242: fstore_3
/*     */     //   243: aload_0
/*     */     //   244: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   247: getstatic net/minecraft/core/particles/ParticleTypes.EXPLOSION : Lnet/minecraft/core/particles/SimpleParticleType;
/*     */     //   250: aload_0
/*     */     //   251: invokevirtual getX : ()D
/*     */     //   254: fload_1
/*     */     //   255: f2d
/*     */     //   256: dadd
/*     */     //   257: aload_0
/*     */     //   258: invokevirtual getY : ()D
/*     */     //   261: ldc2_w 2.0
/*     */     //   264: dadd
/*     */     //   265: fload_2
/*     */     //   266: f2d
/*     */     //   267: dadd
/*     */     //   268: aload_0
/*     */     //   269: invokevirtual getZ : ()D
/*     */     //   272: fload_3
/*     */     //   273: f2d
/*     */     //   274: dadd
/*     */     //   275: dconst_0
/*     */     //   276: dconst_0
/*     */     //   277: dconst_0
/*     */     //   278: invokevirtual addParticle : (Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V
/*     */     //   281: return
/*     */     //   282: aload_0
/*     */     //   283: invokevirtual checkCrystals : ()V
/*     */     //   286: aload_0
/*     */     //   287: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   290: astore_1
/*     */     //   291: ldc_w 0.2
/*     */     //   294: aload_1
/*     */     //   295: invokevirtual horizontalDistance : ()D
/*     */     //   298: d2f
/*     */     //   299: ldc_w 10.0
/*     */     //   302: fmul
/*     */     //   303: fconst_1
/*     */     //   304: fadd
/*     */     //   305: fdiv
/*     */     //   306: fstore_2
/*     */     //   307: fload_2
/*     */     //   308: ldc2_w 2.0
/*     */     //   311: aload_1
/*     */     //   312: getfield y : D
/*     */     //   315: invokestatic pow : (DD)D
/*     */     //   318: d2f
/*     */     //   319: fmul
/*     */     //   320: fstore_2
/*     */     //   321: aload_0
/*     */     //   322: getfield phaseManager : Lnet/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager;
/*     */     //   325: invokevirtual getCurrentPhase : ()Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   328: invokeinterface isSitting : ()Z
/*     */     //   333: ifeq -> 351
/*     */     //   336: aload_0
/*     */     //   337: dup
/*     */     //   338: getfield flapTime : F
/*     */     //   341: ldc_w 0.1
/*     */     //   344: fadd
/*     */     //   345: putfield flapTime : F
/*     */     //   348: goto -> 385
/*     */     //   351: aload_0
/*     */     //   352: getfield inWall : Z
/*     */     //   355: ifeq -> 375
/*     */     //   358: aload_0
/*     */     //   359: dup
/*     */     //   360: getfield flapTime : F
/*     */     //   363: fload_2
/*     */     //   364: ldc_w 0.5
/*     */     //   367: fmul
/*     */     //   368: fadd
/*     */     //   369: putfield flapTime : F
/*     */     //   372: goto -> 385
/*     */     //   375: aload_0
/*     */     //   376: dup
/*     */     //   377: getfield flapTime : F
/*     */     //   380: fload_2
/*     */     //   381: fadd
/*     */     //   382: putfield flapTime : F
/*     */     //   385: aload_0
/*     */     //   386: aload_0
/*     */     //   387: invokevirtual getYRot : ()F
/*     */     //   390: invokestatic wrapDegrees : (F)F
/*     */     //   393: invokevirtual setYRot : (F)V
/*     */     //   396: aload_0
/*     */     //   397: invokevirtual isNoAi : ()Z
/*     */     //   400: ifeq -> 411
/*     */     //   403: aload_0
/*     */     //   404: ldc_w 0.5
/*     */     //   407: putfield flapTime : F
/*     */     //   410: return
/*     */     //   411: aload_0
/*     */     //   412: getfield flightHistory : Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory;
/*     */     //   415: aload_0
/*     */     //   416: invokevirtual getY : ()D
/*     */     //   419: aload_0
/*     */     //   420: invokevirtual getYRot : ()F
/*     */     //   423: invokevirtual record : (DF)V
/*     */     //   426: aload_0
/*     */     //   427: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   430: astore #4
/*     */     //   432: aload #4
/*     */     //   434: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   437: ifeq -> 449
/*     */     //   440: aload #4
/*     */     //   442: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   445: astore_3
/*     */     //   446: goto -> 471
/*     */     //   449: aload_0
/*     */     //   450: getfield interpolation : Lnet/minecraft/world/entity/InterpolationHandler;
/*     */     //   453: invokevirtual interpolate : ()V
/*     */     //   456: aload_0
/*     */     //   457: getfield phaseManager : Lnet/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager;
/*     */     //   460: invokevirtual getCurrentPhase : ()Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   463: invokeinterface doClientTick : ()V
/*     */     //   468: goto -> 995
/*     */     //   471: aload_0
/*     */     //   472: getfield phaseManager : Lnet/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager;
/*     */     //   475: invokevirtual getCurrentPhase : ()Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   478: astore #4
/*     */     //   480: aload #4
/*     */     //   482: aload_3
/*     */     //   483: invokeinterface doServerTick : (Lnet/minecraft/server/level/ServerLevel;)V
/*     */     //   488: aload_0
/*     */     //   489: getfield phaseManager : Lnet/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager;
/*     */     //   492: invokevirtual getCurrentPhase : ()Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   495: aload #4
/*     */     //   497: if_acmpeq -> 517
/*     */     //   500: aload_0
/*     */     //   501: getfield phaseManager : Lnet/minecraft/world/entity/boss/enderdragon/phases/EnderDragonPhaseManager;
/*     */     //   504: invokevirtual getCurrentPhase : ()Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   507: astore #4
/*     */     //   509: aload #4
/*     */     //   511: aload_3
/*     */     //   512: invokeinterface doServerTick : (Lnet/minecraft/server/level/ServerLevel;)V
/*     */     //   517: aload #4
/*     */     //   519: invokeinterface getFlyTargetLocation : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   524: astore #5
/*     */     //   526: aload #5
/*     */     //   528: ifnull -> 995
/*     */     //   531: aload #5
/*     */     //   533: getfield x : D
/*     */     //   536: aload_0
/*     */     //   537: invokevirtual getX : ()D
/*     */     //   540: dsub
/*     */     //   541: dstore #6
/*     */     //   543: aload #5
/*     */     //   545: getfield y : D
/*     */     //   548: aload_0
/*     */     //   549: invokevirtual getY : ()D
/*     */     //   552: dsub
/*     */     //   553: dstore #8
/*     */     //   555: aload #5
/*     */     //   557: getfield z : D
/*     */     //   560: aload_0
/*     */     //   561: invokevirtual getZ : ()D
/*     */     //   564: dsub
/*     */     //   565: dstore #10
/*     */     //   567: dload #6
/*     */     //   569: dload #6
/*     */     //   571: dmul
/*     */     //   572: dload #8
/*     */     //   574: dload #8
/*     */     //   576: dmul
/*     */     //   577: dadd
/*     */     //   578: dload #10
/*     */     //   580: dload #10
/*     */     //   582: dmul
/*     */     //   583: dadd
/*     */     //   584: dstore #12
/*     */     //   586: aload #4
/*     */     //   588: invokeinterface getFlySpeed : ()F
/*     */     //   593: fstore #14
/*     */     //   595: dload #6
/*     */     //   597: dload #6
/*     */     //   599: dmul
/*     */     //   600: dload #10
/*     */     //   602: dload #10
/*     */     //   604: dmul
/*     */     //   605: dadd
/*     */     //   606: invokestatic sqrt : (D)D
/*     */     //   609: dstore #15
/*     */     //   611: dload #15
/*     */     //   613: dconst_0
/*     */     //   614: dcmpl
/*     */     //   615: ifle -> 635
/*     */     //   618: dload #8
/*     */     //   620: dload #15
/*     */     //   622: ddiv
/*     */     //   623: fload #14
/*     */     //   625: fneg
/*     */     //   626: f2d
/*     */     //   627: fload #14
/*     */     //   629: f2d
/*     */     //   630: invokestatic clamp : (DDD)D
/*     */     //   633: dstore #8
/*     */     //   635: aload_0
/*     */     //   636: aload_0
/*     */     //   637: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   640: dconst_0
/*     */     //   641: dload #8
/*     */     //   643: ldc2_w 0.01
/*     */     //   646: dmul
/*     */     //   647: dconst_0
/*     */     //   648: invokevirtual add : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   651: invokevirtual setDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   654: aload_0
/*     */     //   655: aload_0
/*     */     //   656: invokevirtual getYRot : ()F
/*     */     //   659: invokestatic wrapDegrees : (F)F
/*     */     //   662: invokevirtual setYRot : (F)V
/*     */     //   665: aload #5
/*     */     //   667: aload_0
/*     */     //   668: invokevirtual getX : ()D
/*     */     //   671: aload_0
/*     */     //   672: invokevirtual getY : ()D
/*     */     //   675: aload_0
/*     */     //   676: invokevirtual getZ : ()D
/*     */     //   679: invokevirtual subtract : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   682: invokevirtual normalize : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   685: astore #17
/*     */     //   687: new net/minecraft/world/phys/Vec3
/*     */     //   690: dup
/*     */     //   691: aload_0
/*     */     //   692: invokevirtual getYRot : ()F
/*     */     //   695: ldc_w 0.017453292
/*     */     //   698: fmul
/*     */     //   699: f2d
/*     */     //   700: invokestatic sin : (D)F
/*     */     //   703: f2d
/*     */     //   704: aload_0
/*     */     //   705: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   708: getfield y : D
/*     */     //   711: aload_0
/*     */     //   712: invokevirtual getYRot : ()F
/*     */     //   715: ldc_w 0.017453292
/*     */     //   718: fmul
/*     */     //   719: f2d
/*     */     //   720: invokestatic cos : (D)F
/*     */     //   723: fneg
/*     */     //   724: f2d
/*     */     //   725: invokespecial <init> : (DDD)V
/*     */     //   728: invokevirtual normalize : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   731: astore #18
/*     */     //   733: aload #18
/*     */     //   735: aload #17
/*     */     //   737: invokevirtual dot : (Lnet/minecraft/world/phys/Vec3;)D
/*     */     //   740: d2f
/*     */     //   741: ldc_w 0.5
/*     */     //   744: fadd
/*     */     //   745: ldc_w 1.5
/*     */     //   748: fdiv
/*     */     //   749: fconst_0
/*     */     //   750: invokestatic max : (FF)F
/*     */     //   753: fstore #19
/*     */     //   755: dload #6
/*     */     //   757: invokestatic abs : (D)D
/*     */     //   760: ldc2_w 9.999999747378752E-6
/*     */     //   763: dcmpl
/*     */     //   764: ifgt -> 779
/*     */     //   767: dload #10
/*     */     //   769: invokestatic abs : (D)D
/*     */     //   772: ldc2_w 9.999999747378752E-6
/*     */     //   775: dcmpl
/*     */     //   776: ifle -> 861
/*     */     //   779: ldc_w 180.0
/*     */     //   782: dload #6
/*     */     //   784: dload #10
/*     */     //   786: invokestatic atan2 : (DD)D
/*     */     //   789: d2f
/*     */     //   790: ldc_w 57.295776
/*     */     //   793: fmul
/*     */     //   794: fsub
/*     */     //   795: aload_0
/*     */     //   796: invokevirtual getYRot : ()F
/*     */     //   799: fsub
/*     */     //   800: invokestatic wrapDegrees : (F)F
/*     */     //   803: ldc_w -50.0
/*     */     //   806: ldc_w 50.0
/*     */     //   809: invokestatic clamp : (FFF)F
/*     */     //   812: fstore #20
/*     */     //   814: aload_0
/*     */     //   815: dup
/*     */     //   816: getfield yRotA : F
/*     */     //   819: ldc 0.8
/*     */     //   821: fmul
/*     */     //   822: putfield yRotA : F
/*     */     //   825: aload_0
/*     */     //   826: dup
/*     */     //   827: getfield yRotA : F
/*     */     //   830: fload #20
/*     */     //   832: aload #4
/*     */     //   834: invokeinterface getTurnSpeed : ()F
/*     */     //   839: fmul
/*     */     //   840: fadd
/*     */     //   841: putfield yRotA : F
/*     */     //   844: aload_0
/*     */     //   845: aload_0
/*     */     //   846: invokevirtual getYRot : ()F
/*     */     //   849: aload_0
/*     */     //   850: getfield yRotA : F
/*     */     //   853: ldc_w 0.1
/*     */     //   856: fmul
/*     */     //   857: fadd
/*     */     //   858: invokevirtual setYRot : (F)V
/*     */     //   861: ldc2_w 2.0
/*     */     //   864: dload #12
/*     */     //   866: dconst_1
/*     */     //   867: dadd
/*     */     //   868: ddiv
/*     */     //   869: d2f
/*     */     //   870: fstore #20
/*     */     //   872: ldc_w 0.06
/*     */     //   875: fstore #21
/*     */     //   877: aload_0
/*     */     //   878: ldc_w 0.06
/*     */     //   881: fload #19
/*     */     //   883: fload #20
/*     */     //   885: fmul
/*     */     //   886: fconst_1
/*     */     //   887: fload #20
/*     */     //   889: fsub
/*     */     //   890: fadd
/*     */     //   891: fmul
/*     */     //   892: new net/minecraft/world/phys/Vec3
/*     */     //   895: dup
/*     */     //   896: dconst_0
/*     */     //   897: dconst_0
/*     */     //   898: ldc2_w -1.0
/*     */     //   901: invokespecial <init> : (DDD)V
/*     */     //   904: invokevirtual moveRelative : (FLnet/minecraft/world/phys/Vec3;)V
/*     */     //   907: aload_0
/*     */     //   908: getfield inWall : Z
/*     */     //   911: ifeq -> 934
/*     */     //   914: aload_0
/*     */     //   915: getstatic net/minecraft/world/entity/MoverType.SELF : Lnet/minecraft/world/entity/MoverType;
/*     */     //   918: aload_0
/*     */     //   919: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   922: ldc2_w 0.800000011920929
/*     */     //   925: invokevirtual scale : (D)Lnet/minecraft/world/phys/Vec3;
/*     */     //   928: invokevirtual move : (Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   931: goto -> 945
/*     */     //   934: aload_0
/*     */     //   935: getstatic net/minecraft/world/entity/MoverType.SELF : Lnet/minecraft/world/entity/MoverType;
/*     */     //   938: aload_0
/*     */     //   939: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   942: invokevirtual move : (Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   945: aload_0
/*     */     //   946: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   949: invokevirtual normalize : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   952: astore #22
/*     */     //   954: ldc2_w 0.8
/*     */     //   957: ldc2_w 0.15
/*     */     //   960: aload #22
/*     */     //   962: aload #18
/*     */     //   964: invokevirtual dot : (Lnet/minecraft/world/phys/Vec3;)D
/*     */     //   967: dconst_1
/*     */     //   968: dadd
/*     */     //   969: dmul
/*     */     //   970: ldc2_w 2.0
/*     */     //   973: ddiv
/*     */     //   974: dadd
/*     */     //   975: dstore #23
/*     */     //   977: aload_0
/*     */     //   978: aload_0
/*     */     //   979: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   982: dload #23
/*     */     //   984: ldc2_w 0.9100000262260437
/*     */     //   987: dload #23
/*     */     //   989: invokevirtual multiply : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   992: invokevirtual setDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   995: aload_0
/*     */     //   996: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   999: invokevirtual isClientSide : ()Z
/*     */     //   1002: ifne -> 1009
/*     */     //   1005: aload_0
/*     */     //   1006: invokevirtual applyEffectsFromBlocks : ()V
/*     */     //   1009: aload_0
/*     */     //   1010: aload_0
/*     */     //   1011: invokevirtual getYRot : ()F
/*     */     //   1014: putfield yBodyRot : F
/*     */     //   1017: aload_0
/*     */     //   1018: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1021: arraylength
/*     */     //   1022: anewarray net/minecraft/world/phys/Vec3
/*     */     //   1025: astore_3
/*     */     //   1026: iconst_0
/*     */     //   1027: istore #4
/*     */     //   1029: iload #4
/*     */     //   1031: aload_0
/*     */     //   1032: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1035: arraylength
/*     */     //   1036: if_icmpge -> 1086
/*     */     //   1039: aload_3
/*     */     //   1040: iload #4
/*     */     //   1042: new net/minecraft/world/phys/Vec3
/*     */     //   1045: dup
/*     */     //   1046: aload_0
/*     */     //   1047: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1050: iload #4
/*     */     //   1052: aaload
/*     */     //   1053: invokevirtual getX : ()D
/*     */     //   1056: aload_0
/*     */     //   1057: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1060: iload #4
/*     */     //   1062: aaload
/*     */     //   1063: invokevirtual getY : ()D
/*     */     //   1066: aload_0
/*     */     //   1067: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1070: iload #4
/*     */     //   1072: aaload
/*     */     //   1073: invokevirtual getZ : ()D
/*     */     //   1076: invokespecial <init> : (DDD)V
/*     */     //   1079: aastore
/*     */     //   1080: iinc #4, 1
/*     */     //   1083: goto -> 1029
/*     */     //   1086: aload_0
/*     */     //   1087: getfield flightHistory : Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory;
/*     */     //   1090: iconst_5
/*     */     //   1091: invokevirtual get : (I)Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
/*     */     //   1094: invokevirtual y : ()D
/*     */     //   1097: aload_0
/*     */     //   1098: getfield flightHistory : Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory;
/*     */     //   1101: bipush #10
/*     */     //   1103: invokevirtual get : (I)Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
/*     */     //   1106: invokevirtual y : ()D
/*     */     //   1109: dsub
/*     */     //   1110: d2f
/*     */     //   1111: ldc_w 10.0
/*     */     //   1114: fmul
/*     */     //   1115: ldc_w 0.017453292
/*     */     //   1118: fmul
/*     */     //   1119: fstore #4
/*     */     //   1121: fload #4
/*     */     //   1123: f2d
/*     */     //   1124: invokestatic cos : (D)F
/*     */     //   1127: fstore #5
/*     */     //   1129: fload #4
/*     */     //   1131: f2d
/*     */     //   1132: invokestatic sin : (D)F
/*     */     //   1135: fstore #6
/*     */     //   1137: aload_0
/*     */     //   1138: invokevirtual getYRot : ()F
/*     */     //   1141: ldc_w 0.017453292
/*     */     //   1144: fmul
/*     */     //   1145: fstore #7
/*     */     //   1147: fload #7
/*     */     //   1149: f2d
/*     */     //   1150: invokestatic sin : (D)F
/*     */     //   1153: fstore #8
/*     */     //   1155: fload #7
/*     */     //   1157: f2d
/*     */     //   1158: invokestatic cos : (D)F
/*     */     //   1161: fstore #9
/*     */     //   1163: aload_0
/*     */     //   1164: aload_0
/*     */     //   1165: getfield body : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1168: fload #8
/*     */     //   1170: ldc_w 0.5
/*     */     //   1173: fmul
/*     */     //   1174: f2d
/*     */     //   1175: dconst_0
/*     */     //   1176: fload #9
/*     */     //   1178: fneg
/*     */     //   1179: ldc_w 0.5
/*     */     //   1182: fmul
/*     */     //   1183: f2d
/*     */     //   1184: invokevirtual tickPart : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;DDD)V
/*     */     //   1187: aload_0
/*     */     //   1188: aload_0
/*     */     //   1189: getfield wing1 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1192: fload #9
/*     */     //   1194: ldc_w 4.5
/*     */     //   1197: fmul
/*     */     //   1198: f2d
/*     */     //   1199: ldc2_w 2.0
/*     */     //   1202: fload #8
/*     */     //   1204: ldc_w 4.5
/*     */     //   1207: fmul
/*     */     //   1208: f2d
/*     */     //   1209: invokevirtual tickPart : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;DDD)V
/*     */     //   1212: aload_0
/*     */     //   1213: aload_0
/*     */     //   1214: getfield wing2 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1217: fload #9
/*     */     //   1219: ldc_w -4.5
/*     */     //   1222: fmul
/*     */     //   1223: f2d
/*     */     //   1224: ldc2_w 2.0
/*     */     //   1227: fload #8
/*     */     //   1229: ldc_w -4.5
/*     */     //   1232: fmul
/*     */     //   1233: f2d
/*     */     //   1234: invokevirtual tickPart : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;DDD)V
/*     */     //   1237: aload_0
/*     */     //   1238: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   1241: astore #11
/*     */     //   1243: aload #11
/*     */     //   1245: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   1248: ifeq -> 1401
/*     */     //   1251: aload #11
/*     */     //   1253: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   1256: astore #10
/*     */     //   1258: aload_0
/*     */     //   1259: getfield hurtTime : I
/*     */     //   1262: ifne -> 1401
/*     */     //   1265: aload_0
/*     */     //   1266: aload #10
/*     */     //   1268: aload #10
/*     */     //   1270: aload_0
/*     */     //   1271: aload_0
/*     */     //   1272: getfield wing1 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1275: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1278: ldc2_w 4.0
/*     */     //   1281: ldc2_w 2.0
/*     */     //   1284: ldc2_w 4.0
/*     */     //   1287: invokevirtual inflate : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */     //   1290: dconst_0
/*     */     //   1291: ldc2_w -2.0
/*     */     //   1294: dconst_0
/*     */     //   1295: invokevirtual move : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */     //   1298: getstatic net/minecraft/world/entity/EntitySelector.NO_CREATIVE_OR_SPECTATOR : Ljava/util/function/Predicate;
/*     */     //   1301: invokevirtual getEntities : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */     //   1304: invokevirtual knockBack : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V
/*     */     //   1307: aload_0
/*     */     //   1308: aload #10
/*     */     //   1310: aload #10
/*     */     //   1312: aload_0
/*     */     //   1313: aload_0
/*     */     //   1314: getfield wing2 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1317: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1320: ldc2_w 4.0
/*     */     //   1323: ldc2_w 2.0
/*     */     //   1326: ldc2_w 4.0
/*     */     //   1329: invokevirtual inflate : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */     //   1332: dconst_0
/*     */     //   1333: ldc2_w -2.0
/*     */     //   1336: dconst_0
/*     */     //   1337: invokevirtual move : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */     //   1340: getstatic net/minecraft/world/entity/EntitySelector.NO_CREATIVE_OR_SPECTATOR : Ljava/util/function/Predicate;
/*     */     //   1343: invokevirtual getEntities : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */     //   1346: invokevirtual knockBack : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V
/*     */     //   1349: aload_0
/*     */     //   1350: aload #10
/*     */     //   1352: aload #10
/*     */     //   1354: aload_0
/*     */     //   1355: aload_0
/*     */     //   1356: getfield head : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1359: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1362: dconst_1
/*     */     //   1363: invokevirtual inflate : (D)Lnet/minecraft/world/phys/AABB;
/*     */     //   1366: getstatic net/minecraft/world/entity/EntitySelector.NO_CREATIVE_OR_SPECTATOR : Ljava/util/function/Predicate;
/*     */     //   1369: invokevirtual getEntities : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */     //   1372: invokevirtual hurt : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V
/*     */     //   1375: aload_0
/*     */     //   1376: aload #10
/*     */     //   1378: aload #10
/*     */     //   1380: aload_0
/*     */     //   1381: aload_0
/*     */     //   1382: getfield neck : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1385: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1388: dconst_1
/*     */     //   1389: invokevirtual inflate : (D)Lnet/minecraft/world/phys/AABB;
/*     */     //   1392: getstatic net/minecraft/world/entity/EntitySelector.NO_CREATIVE_OR_SPECTATOR : Ljava/util/function/Predicate;
/*     */     //   1395: invokevirtual getEntities : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */     //   1398: invokevirtual hurt : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V
/*     */     //   1401: aload_0
/*     */     //   1402: invokevirtual getYRot : ()F
/*     */     //   1405: ldc_w 0.017453292
/*     */     //   1408: fmul
/*     */     //   1409: aload_0
/*     */     //   1410: getfield yRotA : F
/*     */     //   1413: ldc_w 0.01
/*     */     //   1416: fmul
/*     */     //   1417: fsub
/*     */     //   1418: f2d
/*     */     //   1419: invokestatic sin : (D)F
/*     */     //   1422: fstore #10
/*     */     //   1424: aload_0
/*     */     //   1425: invokevirtual getYRot : ()F
/*     */     //   1428: ldc_w 0.017453292
/*     */     //   1431: fmul
/*     */     //   1432: aload_0
/*     */     //   1433: getfield yRotA : F
/*     */     //   1436: ldc_w 0.01
/*     */     //   1439: fmul
/*     */     //   1440: fsub
/*     */     //   1441: f2d
/*     */     //   1442: invokestatic cos : (D)F
/*     */     //   1445: fstore #11
/*     */     //   1447: aload_0
/*     */     //   1448: invokevirtual getHeadYOffset : ()F
/*     */     //   1451: fstore #12
/*     */     //   1453: aload_0
/*     */     //   1454: aload_0
/*     */     //   1455: getfield head : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1458: fload #10
/*     */     //   1460: ldc_w 6.5
/*     */     //   1463: fmul
/*     */     //   1464: fload #5
/*     */     //   1466: fmul
/*     */     //   1467: f2d
/*     */     //   1468: fload #12
/*     */     //   1470: fload #6
/*     */     //   1472: ldc_w 6.5
/*     */     //   1475: fmul
/*     */     //   1476: fadd
/*     */     //   1477: f2d
/*     */     //   1478: fload #11
/*     */     //   1480: fneg
/*     */     //   1481: ldc_w 6.5
/*     */     //   1484: fmul
/*     */     //   1485: fload #5
/*     */     //   1487: fmul
/*     */     //   1488: f2d
/*     */     //   1489: invokevirtual tickPart : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;DDD)V
/*     */     //   1492: aload_0
/*     */     //   1493: aload_0
/*     */     //   1494: getfield neck : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1497: fload #10
/*     */     //   1499: ldc_w 5.5
/*     */     //   1502: fmul
/*     */     //   1503: fload #5
/*     */     //   1505: fmul
/*     */     //   1506: f2d
/*     */     //   1507: fload #12
/*     */     //   1509: fload #6
/*     */     //   1511: ldc_w 5.5
/*     */     //   1514: fmul
/*     */     //   1515: fadd
/*     */     //   1516: f2d
/*     */     //   1517: fload #11
/*     */     //   1519: fneg
/*     */     //   1520: ldc_w 5.5
/*     */     //   1523: fmul
/*     */     //   1524: fload #5
/*     */     //   1526: fmul
/*     */     //   1527: f2d
/*     */     //   1528: invokevirtual tickPart : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;DDD)V
/*     */     //   1531: aload_0
/*     */     //   1532: getfield flightHistory : Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory;
/*     */     //   1535: iconst_5
/*     */     //   1536: invokevirtual get : (I)Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
/*     */     //   1539: astore #13
/*     */     //   1541: iconst_0
/*     */     //   1542: istore #14
/*     */     //   1544: iload #14
/*     */     //   1546: iconst_3
/*     */     //   1547: if_icmpge -> 1736
/*     */     //   1550: aconst_null
/*     */     //   1551: astore #15
/*     */     //   1553: iload #14
/*     */     //   1555: ifne -> 1564
/*     */     //   1558: aload_0
/*     */     //   1559: getfield tail1 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1562: astore #15
/*     */     //   1564: iload #14
/*     */     //   1566: iconst_1
/*     */     //   1567: if_icmpne -> 1576
/*     */     //   1570: aload_0
/*     */     //   1571: getfield tail2 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1574: astore #15
/*     */     //   1576: iload #14
/*     */     //   1578: iconst_2
/*     */     //   1579: if_icmpne -> 1588
/*     */     //   1582: aload_0
/*     */     //   1583: getfield tail3 : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1586: astore #15
/*     */     //   1588: aload_0
/*     */     //   1589: getfield flightHistory : Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory;
/*     */     //   1592: bipush #12
/*     */     //   1594: iload #14
/*     */     //   1596: iconst_2
/*     */     //   1597: imul
/*     */     //   1598: iadd
/*     */     //   1599: invokevirtual get : (I)Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
/*     */     //   1602: astore #16
/*     */     //   1604: aload_0
/*     */     //   1605: invokevirtual getYRot : ()F
/*     */     //   1608: ldc_w 0.017453292
/*     */     //   1611: fmul
/*     */     //   1612: aload_0
/*     */     //   1613: aload #16
/*     */     //   1615: invokevirtual yRot : ()F
/*     */     //   1618: aload #13
/*     */     //   1620: invokevirtual yRot : ()F
/*     */     //   1623: fsub
/*     */     //   1624: f2d
/*     */     //   1625: invokevirtual rotWrap : (D)F
/*     */     //   1628: ldc_w 0.017453292
/*     */     //   1631: fmul
/*     */     //   1632: fadd
/*     */     //   1633: fstore #17
/*     */     //   1635: fload #17
/*     */     //   1637: f2d
/*     */     //   1638: invokestatic sin : (D)F
/*     */     //   1641: fstore #18
/*     */     //   1643: fload #17
/*     */     //   1645: f2d
/*     */     //   1646: invokestatic cos : (D)F
/*     */     //   1649: fstore #19
/*     */     //   1651: ldc_w 1.5
/*     */     //   1654: fstore #20
/*     */     //   1656: iload #14
/*     */     //   1658: iconst_1
/*     */     //   1659: iadd
/*     */     //   1660: i2f
/*     */     //   1661: fconst_2
/*     */     //   1662: fmul
/*     */     //   1663: fstore #21
/*     */     //   1665: aload_0
/*     */     //   1666: aload #15
/*     */     //   1668: fload #8
/*     */     //   1670: ldc_w 1.5
/*     */     //   1673: fmul
/*     */     //   1674: fload #18
/*     */     //   1676: fload #21
/*     */     //   1678: fmul
/*     */     //   1679: fadd
/*     */     //   1680: fneg
/*     */     //   1681: fload #5
/*     */     //   1683: fmul
/*     */     //   1684: f2d
/*     */     //   1685: aload #16
/*     */     //   1687: invokevirtual y : ()D
/*     */     //   1690: aload #13
/*     */     //   1692: invokevirtual y : ()D
/*     */     //   1695: dsub
/*     */     //   1696: fload #21
/*     */     //   1698: ldc_w 1.5
/*     */     //   1701: fadd
/*     */     //   1702: fload #6
/*     */     //   1704: fmul
/*     */     //   1705: f2d
/*     */     //   1706: dsub
/*     */     //   1707: ldc2_w 1.5
/*     */     //   1710: dadd
/*     */     //   1711: fload #9
/*     */     //   1713: ldc_w 1.5
/*     */     //   1716: fmul
/*     */     //   1717: fload #19
/*     */     //   1719: fload #21
/*     */     //   1721: fmul
/*     */     //   1722: fadd
/*     */     //   1723: fload #5
/*     */     //   1725: fmul
/*     */     //   1726: f2d
/*     */     //   1727: invokevirtual tickPart : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;DDD)V
/*     */     //   1730: iinc #14, 1
/*     */     //   1733: goto -> 1544
/*     */     //   1736: aload_0
/*     */     //   1737: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   1740: astore #15
/*     */     //   1742: aload #15
/*     */     //   1744: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   1747: ifeq -> 1817
/*     */     //   1750: aload #15
/*     */     //   1752: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   1755: astore #14
/*     */     //   1757: aload_0
/*     */     //   1758: aload_0
/*     */     //   1759: aload #14
/*     */     //   1761: aload_0
/*     */     //   1762: getfield head : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1765: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1768: invokevirtual checkWalls : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   1771: aload_0
/*     */     //   1772: aload #14
/*     */     //   1774: aload_0
/*     */     //   1775: getfield neck : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1778: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1781: invokevirtual checkWalls : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   1784: ior
/*     */     //   1785: aload_0
/*     */     //   1786: aload #14
/*     */     //   1788: aload_0
/*     */     //   1789: getfield body : Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1792: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   1795: invokevirtual checkWalls : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   1798: ior
/*     */     //   1799: putfield inWall : Z
/*     */     //   1802: aload_0
/*     */     //   1803: getfield dragonFight : Lnet/minecraft/world/level/dimension/end/EndDragonFight;
/*     */     //   1806: ifnull -> 1817
/*     */     //   1809: aload_0
/*     */     //   1810: getfield dragonFight : Lnet/minecraft/world/level/dimension/end/EndDragonFight;
/*     */     //   1813: aload_0
/*     */     //   1814: invokevirtual updateDragon : (Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;)V
/*     */     //   1817: iconst_0
/*     */     //   1818: istore #14
/*     */     //   1820: iload #14
/*     */     //   1822: aload_0
/*     */     //   1823: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1826: arraylength
/*     */     //   1827: if_icmpge -> 1938
/*     */     //   1830: aload_0
/*     */     //   1831: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1834: iload #14
/*     */     //   1836: aaload
/*     */     //   1837: aload_3
/*     */     //   1838: iload #14
/*     */     //   1840: aaload
/*     */     //   1841: getfield x : D
/*     */     //   1844: putfield xo : D
/*     */     //   1847: aload_0
/*     */     //   1848: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1851: iload #14
/*     */     //   1853: aaload
/*     */     //   1854: aload_3
/*     */     //   1855: iload #14
/*     */     //   1857: aaload
/*     */     //   1858: getfield y : D
/*     */     //   1861: putfield yo : D
/*     */     //   1864: aload_0
/*     */     //   1865: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1868: iload #14
/*     */     //   1870: aaload
/*     */     //   1871: aload_3
/*     */     //   1872: iload #14
/*     */     //   1874: aaload
/*     */     //   1875: getfield z : D
/*     */     //   1878: putfield zo : D
/*     */     //   1881: aload_0
/*     */     //   1882: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1885: iload #14
/*     */     //   1887: aaload
/*     */     //   1888: aload_3
/*     */     //   1889: iload #14
/*     */     //   1891: aaload
/*     */     //   1892: getfield x : D
/*     */     //   1895: putfield xOld : D
/*     */     //   1898: aload_0
/*     */     //   1899: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1902: iload #14
/*     */     //   1904: aaload
/*     */     //   1905: aload_3
/*     */     //   1906: iload #14
/*     */     //   1908: aaload
/*     */     //   1909: getfield y : D
/*     */     //   1912: putfield yOld : D
/*     */     //   1915: aload_0
/*     */     //   1916: getfield subEntities : [Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1919: iload #14
/*     */     //   1921: aaload
/*     */     //   1922: aload_3
/*     */     //   1923: iload #14
/*     */     //   1925: aaload
/*     */     //   1926: getfield z : D
/*     */     //   1929: putfield zOld : D
/*     */     //   1932: iinc #14, 1
/*     */     //   1935: goto -> 1820
/*     */     //   1938: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #164	-> 0
/*     */     //   #166	-> 4
/*     */     //   #167	-> 14
/*     */     //   #169	-> 22
/*     */     //   #170	-> 29
/*     */     //   #171	-> 58
/*     */     //   #172	-> 103
/*     */     //   #177	-> 123
/*     */     //   #178	-> 147
/*     */     //   #179	-> 152
/*     */     //   #180	-> 170
/*     */     //   #184	-> 175
/*     */     //   #186	-> 183
/*     */     //   #187	-> 190
/*     */     //   #188	-> 208
/*     */     //   #189	-> 225
/*     */     //   #190	-> 243
/*     */     //   #191	-> 281
/*     */     //   #194	-> 282
/*     */     //   #196	-> 286
/*     */     //   #197	-> 291
/*     */     //   #198	-> 307
/*     */     //   #199	-> 321
/*     */     //   #200	-> 336
/*     */     //   #201	-> 351
/*     */     //   #202	-> 358
/*     */     //   #204	-> 375
/*     */     //   #207	-> 385
/*     */     //   #209	-> 396
/*     */     //   #210	-> 403
/*     */     //   #211	-> 410
/*     */     //   #214	-> 411
/*     */     //   #216	-> 426
/*     */     //   #217	-> 449
/*     */     //   #219	-> 456
/*     */     //   #221	-> 471
/*     */     //   #222	-> 480
/*     */     //   #224	-> 488
/*     */     //   #225	-> 500
/*     */     //   #226	-> 509
/*     */     //   #229	-> 517
/*     */     //   #231	-> 526
/*     */     //   #232	-> 531
/*     */     //   #233	-> 543
/*     */     //   #234	-> 555
/*     */     //   #236	-> 567
/*     */     //   #237	-> 586
/*     */     //   #238	-> 595
/*     */     //   #239	-> 611
/*     */     //   #240	-> 618
/*     */     //   #242	-> 635
/*     */     //   #243	-> 654
/*     */     //   #245	-> 665
/*     */     //   #246	-> 687
/*     */     //   #247	-> 733
/*     */     //   #249	-> 755
/*     */     //   #250	-> 779
/*     */     //   #251	-> 814
/*     */     //   #252	-> 825
/*     */     //   #253	-> 844
/*     */     //   #256	-> 861
/*     */     //   #257	-> 872
/*     */     //   #258	-> 877
/*     */     //   #259	-> 907
/*     */     //   #260	-> 914
/*     */     //   #262	-> 934
/*     */     //   #265	-> 945
/*     */     //   #266	-> 954
/*     */     //   #268	-> 977
/*     */     //   #272	-> 995
/*     */     //   #273	-> 1005
/*     */     //   #276	-> 1009
/*     */     //   #278	-> 1017
/*     */     //   #279	-> 1026
/*     */     //   #280	-> 1039
/*     */     //   #279	-> 1080
/*     */     //   #283	-> 1086
/*     */     //   #284	-> 1121
/*     */     //   #285	-> 1129
/*     */     //   #287	-> 1137
/*     */     //   #288	-> 1147
/*     */     //   #289	-> 1155
/*     */     //   #291	-> 1163
/*     */     //   #292	-> 1187
/*     */     //   #293	-> 1212
/*     */     //   #295	-> 1237
/*     */     //   #296	-> 1265
/*     */     //   #297	-> 1307
/*     */     //   #298	-> 1349
/*     */     //   #299	-> 1375
/*     */     //   #302	-> 1401
/*     */     //   #303	-> 1424
/*     */     //   #304	-> 1447
/*     */     //   #305	-> 1453
/*     */     //   #306	-> 1492
/*     */     //   #309	-> 1531
/*     */     //   #310	-> 1541
/*     */     //   #311	-> 1550
/*     */     //   #313	-> 1553
/*     */     //   #314	-> 1558
/*     */     //   #316	-> 1564
/*     */     //   #317	-> 1570
/*     */     //   #319	-> 1576
/*     */     //   #320	-> 1582
/*     */     //   #323	-> 1588
/*     */     //   #325	-> 1604
/*     */     //   #326	-> 1635
/*     */     //   #327	-> 1643
/*     */     //   #329	-> 1651
/*     */     //   #330	-> 1656
/*     */     //   #331	-> 1665
/*     */     //   #310	-> 1730
/*     */     //   #334	-> 1736
/*     */     //   #336	-> 1757
/*     */     //   #338	-> 1802
/*     */     //   #339	-> 1809
/*     */     //   #342	-> 1817
/*     */     //   #343	-> 1830
/*     */     //   #344	-> 1847
/*     */     //   #345	-> 1864
/*     */     //   #346	-> 1881
/*     */     //   #347	-> 1898
/*     */     //   #348	-> 1915
/*     */     //   #342	-> 1932
/*     */     //   #350	-> 1938
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   152	23	2	maybeOurFight	Lnet/minecraft/world/level/dimension/end/EndDragonFight;
/*     */     //   147	28	1	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   208	74	1	xo	F
/*     */     //   225	57	2	yo	F
/*     */     //   243	39	3	zo	F
/*     */     //   446	3	3	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   814	47	20	yRotD	F
/*     */     //   543	452	6	xdd	D
/*     */     //   555	440	8	ydd	D
/*     */     //   567	428	10	zdd	D
/*     */     //   586	409	12	distToTarget	D
/*     */     //   595	400	14	max	F
/*     */     //   611	384	15	horizontalDist	D
/*     */     //   687	308	17	aim	Lnet/minecraft/world/phys/Vec3;
/*     */     //   733	262	18	dir	Lnet/minecraft/world/phys/Vec3;
/*     */     //   755	240	19	dot	F
/*     */     //   872	123	20	span	F
/*     */     //   877	118	21	speed	F
/*     */     //   954	41	22	actual	Lnet/minecraft/world/phys/Vec3;
/*     */     //   977	18	23	slide	D
/*     */     //   480	515	4	currentPhase	Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;
/*     */     //   526	469	5	targetLocation	Lnet/minecraft/world/phys/Vec3;
/*     */     //   471	524	3	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   1029	57	4	i	I
/*     */     //   1258	143	10	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   1553	177	15	part	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*     */     //   1604	126	16	p0	Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
/*     */     //   1635	95	17	rot	F
/*     */     //   1643	87	18	ss	F
/*     */     //   1651	79	19	cc	F
/*     */     //   1656	74	20	dd1	F
/*     */     //   1665	65	21	dd	F
/*     */     //   1544	192	14	i	I
/*     */     //   1757	60	14	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   1820	118	14	i	I
/*     */     //   0	1939	0	this	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*     */     //   291	1648	1	movement	Lnet/minecraft/world/phys/Vec3;
/*     */     //   307	1632	2	flapSpeed	F
/*     */     //   1026	913	3	oldPos	[Lnet/minecraft/world/phys/Vec3;
/*     */     //   1121	818	4	tilt	F
/*     */     //   1129	810	5	ccTilt	F
/*     */     //   1137	802	6	ssTilt	F
/*     */     //   1147	792	7	rot1	F
/*     */     //   1155	784	8	ss1	F
/*     */     //   1163	776	9	cc1	F
/*     */     //   1424	515	10	ss2	F
/*     */     //   1447	492	11	cc2	F
/*     */     //   1453	486	12	yOffset	F
/*     */     //   1541	398	13	p1	Lnet/minecraft/world/entity/boss/enderdragon/DragonFlightHistory$Sample;
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
/*     */   private void tickPart(EnderDragonPart part, double x, double y, double z) {
/* 353 */     part.setPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */   private float getHeadYOffset() {
/* 357 */     if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 358 */       return -1.0F;
/*     */     }
/* 360 */     DragonFlightHistory.Sample p0 = this.flightHistory.get(5);
/* 361 */     DragonFlightHistory.Sample p1 = this.flightHistory.get(0);
/* 362 */     return (float)(p0.y() - p1.y());
/*     */   }
/*     */   
/*     */   private void checkCrystals() {
/* 366 */     if (this.nearestCrystal != null) {
/* 367 */       if (this.nearestCrystal.isRemoved()) {
/* 368 */         this.nearestCrystal = null;
/* 369 */       } else if (this.tickCount % 10 == 0 && 
/* 370 */         getHealth() < getMaxHealth()) {
/* 371 */         setHealth(getHealth() + 1.0F);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 376 */     if (this.random.nextInt(10) == 0) {
/* 377 */       List<EndCrystal> crystals = level().getEntitiesOfClass(EndCrystal.class, getBoundingBox().inflate(32.0D));
/*     */       
/* 379 */       EndCrystal nearest = null;
/* 380 */       double distance = Double.MAX_VALUE;
/* 381 */       for (EndCrystal crystal : crystals) {
/* 382 */         double dist = crystal.distanceToSqr((Entity)this);
/* 383 */         if (dist < distance) {
/* 384 */           distance = dist;
/* 385 */           nearest = crystal;
/*     */         } 
/*     */       } 
/*     */       
/* 389 */       this.nearestCrystal = nearest;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void knockBack(ServerLevel serverLevel, List<Entity> entities) {
/* 394 */     double xm = ((this.body.getBoundingBox()).minX + (this.body.getBoundingBox()).maxX) / 2.0D;
/* 395 */     double zm = ((this.body.getBoundingBox()).minZ + (this.body.getBoundingBox()).maxZ) / 2.0D;
/*     */     
/* 397 */     for (Entity entity : entities) {
/* 398 */       if (entity instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)entity;
/* 399 */         double xd = entity.getX() - xm;
/* 400 */         double zd = entity.getZ() - zm;
/* 401 */         double dd = Math.max(xd * xd + zd * zd, 0.1D);
/* 402 */         entity.push(xd / dd * 4.0D, 0.20000000298023224D, zd / dd * 4.0D);
/* 403 */         if (!this.phaseManager.getCurrentPhase().isSitting() && livingTarget.getLastHurtByMobTimestamp() < entity.tickCount - 2) {
/* 404 */           DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 405 */           entity.hurtServer(serverLevel, damageSource, 5.0F);
/* 406 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hurt(ServerLevel level, List<Entity> entities) {
/* 413 */     for (Entity target : entities) {
/* 414 */       if (target instanceof LivingEntity) {
/* 415 */         DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 416 */         target.hurtServer(level, damageSource, 10.0F);
/* 417 */         EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float rotWrap(double d) {
/* 423 */     return (float)Mth.wrapDegrees(d);
/*     */   }
/*     */   
/*     */   private boolean checkWalls(ServerLevel level, AABB bb) {
/* 427 */     int x0 = Mth.floor(bb.minX);
/* 428 */     int y0 = Mth.floor(bb.minY);
/* 429 */     int z0 = Mth.floor(bb.minZ);
/* 430 */     int x1 = Mth.floor(bb.maxX);
/* 431 */     int y1 = Mth.floor(bb.maxY);
/* 432 */     int z1 = Mth.floor(bb.maxZ);
/*     */     boolean hitWall = false;
/*     */     boolean destroyedBlock = false;
/* 435 */     for (int x = x0; x <= x1; x++) {
/* 436 */       for (int y = y0; y <= y1; y++) {
/* 437 */         for (int z = z0; z <= z1; z++) {
/* 438 */           BlockPos blockPos = new BlockPos(x, y, z);
/* 439 */           BlockState state = level.getBlockState(blockPos);
/* 440 */           if (!state.isAir() && !state.is(BlockTags.DRAGON_TRANSPARENT))
/*     */           {
/* 442 */             if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)) || state.is(BlockTags.DRAGON_IMMUNE)) {
/* 443 */               hitWall = true;
/*     */             } else {
/* 445 */               destroyedBlock = (level.removeBlock(blockPos, false) || destroyedBlock);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 451 */     if (destroyedBlock) {
/* 452 */       BlockPos randomPos = new BlockPos(x0 + 
/* 453 */           this.random.nextInt(x1 - x0 + 1), y0 + 
/* 454 */           this.random.nextInt(y1 - y0 + 1), z0 + 
/* 455 */           this.random.nextInt(z1 - z0 + 1));
/*     */       
/* 457 */       level.levelEvent(2008, randomPos, 0);
/*     */     } 
/*     */     
/* 460 */     return hitWall;
/*     */   }
/*     */   
/*     */   public boolean hurt(ServerLevel level, EnderDragonPart part, DamageSource source, float damage) {
/* 464 */     if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
/* 465 */       return false;
/*     */     }
/*     */     
/* 468 */     damage = this.phaseManager.getCurrentPhase().onHurt(source, damage);
/*     */     
/* 470 */     if (part != this.head) {
/* 471 */       damage = damage / 4.0F + Math.min(damage, 1.0F);
/*     */     }
/*     */     
/* 474 */     if (damage < 0.01F) {
/* 475 */       return false;
/*     */     }
/*     */     
/* 478 */     if (source.getEntity() instanceof Player || source.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
/* 479 */       float healthBefore = getHealth();
/* 480 */       reallyHurt(level, source, damage);
/*     */       
/* 482 */       if (isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
/* 483 */         setHealth(1.0F);
/* 484 */         this.phaseManager.setPhase(EnderDragonPhase.DYING);
/*     */       } 
/*     */       
/* 487 */       if (this.phaseManager.getCurrentPhase().isSitting()) {
/* 488 */         this.sittingDamageReceived = this.sittingDamageReceived + healthBefore - getHealth();
/*     */         
/* 490 */         if (this.sittingDamageReceived > 0.25F * getMaxHealth()) {
/* 491 */           this.sittingDamageReceived = 0.0F;
/* 492 */           this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 497 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 502 */     return hurt(level, this.body, source, damage);
/*     */   }
/*     */   
/*     */   protected void reallyHurt(ServerLevel level, DamageSource source, float damage) {
/* 506 */     super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 511 */     remove(Entity.RemovalReason.KILLED);
/* 512 */     gameEvent((Holder)GameEvent.ENTITY_DIE);
/*     */     
/* 514 */     if (this.dragonFight != null) {
/* 515 */       this.dragonFight.updateDragon(this);
/* 516 */       this.dragonFight.setDragonKilled(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickDeath() {
/* 522 */     if (this.dragonFight != null) {
/* 523 */       this.dragonFight.updateDragon(this);
/*     */     }
/*     */     
/* 526 */     this.dragonDeathTime++;
/* 527 */     if (this.dragonDeathTime >= 180 && this.dragonDeathTime <= 200) {
/* 528 */       float xo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 529 */       float yo = (this.random.nextFloat() - 0.5F) * 4.0F;
/* 530 */       float zo = (this.random.nextFloat() - 0.5F) * 8.0F;
/* 531 */       level().addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, getX() + xo, getY() + 2.0D + yo, getZ() + zo, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */     
/* 534 */     int xpCount = 500;
/* 535 */     if (this.dragonFight != null && !this.dragonFight.hasPreviouslyKilledDragon()) {
/* 536 */       xpCount = 12000;
/*     */     }
/*     */     
/* 539 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 540 */       if (this.dragonDeathTime > 150 && this.dragonDeathTime % 5 == 0 && (Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)) {
/* 541 */         ExperienceOrb.award(serverLevel, position(), Mth.floor(xpCount * 0.08F));
/*     */       }
/* 543 */       if (this.dragonDeathTime == 1 && !isSilent()) {
/* 544 */         serverLevel.globalLevelEvent(1028, blockPosition(), 0);
/*     */       } }
/*     */ 
/*     */     
/* 548 */     Vec3 deathMove = new Vec3(0.0D, 0.10000000149011612D, 0.0D);
/* 549 */     move(MoverType.SELF, deathMove);
/* 550 */     for (EnderDragonPart dragonPart : this.subEntities) {
/* 551 */       dragonPart.setOldPosAndRot();
/* 552 */       dragonPart.setPos(dragonPart.position().add(deathMove));
/*     */     } 
/*     */     
/* 555 */     if (this.dragonDeathTime == 200) { Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 556 */         if ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)) {
/* 557 */           ExperienceOrb.award(serverLevel, position(), Mth.floor(xpCount * 0.2F));
/*     */         }
/* 559 */         if (this.dragonFight != null) {
/* 560 */           this.dragonFight.setDragonKilled(this);
/*     */         }
/* 562 */         remove(Entity.RemovalReason.KILLED);
/* 563 */         gameEvent((Holder)GameEvent.ENTITY_DIE); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public int findClosestNode() {
/* 569 */     if (this.nodes[0] == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 575 */       for (int i = 0; i < 24; i++) {
/* 576 */         int nodeX, nodeZ; int yAdjustment = 5;
/* 577 */         int multiplier = i;
/*     */ 
/*     */ 
/*     */         
/* 581 */         if (i < 12) {
/* 582 */           nodeX = Mth.floor(60.0F * Mth.cos((2.0F * (-3.1415927F + 0.2617994F * multiplier))));
/* 583 */           nodeZ = Mth.floor(60.0F * Mth.sin((2.0F * (-3.1415927F + 0.2617994F * multiplier))));
/* 584 */         } else if (i < 20) {
/* 585 */           multiplier -= 12;
/* 586 */           nodeX = Mth.floor(40.0F * Mth.cos((2.0F * (-3.1415927F + 0.3926991F * multiplier))));
/* 587 */           nodeZ = Mth.floor(40.0F * Mth.sin((2.0F * (-3.1415927F + 0.3926991F * multiplier))));
/* 588 */           yAdjustment += 10;
/*     */         } else {
/* 590 */           multiplier -= 20;
/* 591 */           nodeX = Mth.floor(20.0F * Mth.cos((2.0F * (-3.1415927F + 0.7853982F * multiplier))));
/* 592 */           nodeZ = Mth.floor(20.0F * Mth.sin((2.0F * (-3.1415927F + 0.7853982F * multiplier))));
/*     */         } 
/*     */ 
/*     */         
/* 596 */         int nodeY = Math.max(73, level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(nodeX, 0, nodeZ)).getY() + yAdjustment);
/*     */         
/* 598 */         this.nodes[i] = new Node(nodeX, nodeY, nodeZ);
/*     */       } 
/*     */       
/* 601 */       this.nodeAdjacency[0] = 6146;
/* 602 */       this.nodeAdjacency[1] = 8197;
/* 603 */       this.nodeAdjacency[2] = 8202;
/* 604 */       this.nodeAdjacency[3] = 16404;
/* 605 */       this.nodeAdjacency[4] = 32808;
/* 606 */       this.nodeAdjacency[5] = 32848;
/* 607 */       this.nodeAdjacency[6] = 65696;
/* 608 */       this.nodeAdjacency[7] = 131392;
/* 609 */       this.nodeAdjacency[8] = 131712;
/* 610 */       this.nodeAdjacency[9] = 263424;
/* 611 */       this.nodeAdjacency[10] = 526848;
/* 612 */       this.nodeAdjacency[11] = 525313;
/*     */       
/* 614 */       this.nodeAdjacency[12] = 1581057;
/* 615 */       this.nodeAdjacency[13] = 3166214;
/* 616 */       this.nodeAdjacency[14] = 2138120;
/* 617 */       this.nodeAdjacency[15] = 6373424;
/* 618 */       this.nodeAdjacency[16] = 4358208;
/* 619 */       this.nodeAdjacency[17] = 12910976;
/* 620 */       this.nodeAdjacency[18] = 9044480;
/* 621 */       this.nodeAdjacency[19] = 9706496;
/*     */       
/* 623 */       this.nodeAdjacency[20] = 15216640;
/* 624 */       this.nodeAdjacency[21] = 13688832;
/* 625 */       this.nodeAdjacency[22] = 11763712;
/* 626 */       this.nodeAdjacency[23] = 8257536;
/*     */     } 
/*     */     
/* 629 */     return findClosestNode(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public int findClosestNode(double tX, double tY, double tZ) {
/* 633 */     float closestDist = 10000.0F;
/* 634 */     int closestIndex = 0;
/* 635 */     Node currentPos = new Node(Mth.floor(tX), Mth.floor(tY), Mth.floor(tZ));
/* 636 */     int startIndex = 0;
/*     */     
/* 638 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 640 */       startIndex = 12;
/*     */     }
/*     */     
/* 643 */     for (int i = startIndex; i < 24; i++) {
/* 644 */       if (this.nodes[i] != null) {
/* 645 */         float dist = this.nodes[i].distanceToSqr(currentPos);
/* 646 */         if (dist < closestDist) {
/* 647 */           closestDist = dist;
/* 648 */           closestIndex = i;
/*     */         } 
/*     */       } 
/*     */     } 
/* 652 */     return closestIndex;
/*     */   }
/*     */   
/*     */   public Path findPath(int startIndex, int endIndex, Node finalNode) {
/* 656 */     for (int i = 0; i < 24; i++) {
/* 657 */       Node node = this.nodes[i];
/* 658 */       node.closed = false;
/* 659 */       node.f = 0.0F;
/* 660 */       node.g = 0.0F;
/* 661 */       node.h = 0.0F;
/* 662 */       node.cameFrom = null;
/* 663 */       node.heapIdx = -1;
/*     */     } 
/*     */     
/* 666 */     Node from = this.nodes[startIndex];
/* 667 */     Node to = this.nodes[endIndex];
/*     */     
/* 669 */     from.g = 0.0F;
/* 670 */     from.h = from.distanceTo(to);
/* 671 */     from.f = from.h;
/*     */     
/* 673 */     this.openSet.clear();
/* 674 */     this.openSet.insert(from);
/*     */     
/* 676 */     Node closest = from;
/*     */     
/* 678 */     int minimumNodeIndex = 0;
/* 679 */     if (this.dragonFight == null || this.dragonFight.getCrystalsAlive() == 0)
/*     */     {
/* 681 */       minimumNodeIndex = 12;
/*     */     }
/*     */     
/* 684 */     while (!this.openSet.isEmpty()) {
/* 685 */       Node openNode = this.openSet.pop();
/*     */       
/* 687 */       if (openNode.equals(to)) {
/* 688 */         if (finalNode != null) {
/* 689 */           finalNode.cameFrom = to;
/* 690 */           to = finalNode;
/*     */         } 
/* 692 */         return reconstructPath(from, to);
/*     */       } 
/*     */       
/* 695 */       if (openNode.distanceTo(to) < closest.distanceTo(to)) {
/* 696 */         closest = openNode;
/*     */       }
/* 698 */       openNode.closed = true;
/*     */       
/* 700 */       int xIndex = 0;
/* 701 */       for (int k = 0; k < 24; k++) {
/* 702 */         if (this.nodes[k] == openNode) {
/* 703 */           xIndex = k;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 708 */       for (int j = minimumNodeIndex; j < 24; j++) {
/* 709 */         if ((this.nodeAdjacency[xIndex] & 1 << j) > 0) {
/* 710 */           Node adjacentNode = this.nodes[j];
/*     */           
/* 712 */           if (!adjacentNode.closed) {
/*     */ 
/*     */ 
/*     */             
/* 716 */             float tentativeGScore = openNode.g + openNode.distanceTo(adjacentNode);
/* 717 */             if (!adjacentNode.inOpenSet() || tentativeGScore < adjacentNode.g) {
/* 718 */               adjacentNode.cameFrom = openNode;
/* 719 */               adjacentNode.g = tentativeGScore;
/* 720 */               adjacentNode.h = adjacentNode.distanceTo(to);
/* 721 */               if (adjacentNode.inOpenSet()) {
/* 722 */                 this.openSet.changeCost(adjacentNode, adjacentNode.g + adjacentNode.h);
/*     */               } else {
/* 724 */                 adjacentNode.f = adjacentNode.g + adjacentNode.h;
/* 725 */                 this.openSet.insert(adjacentNode);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 732 */     if (closest == from) {
/* 733 */       return null;
/*     */     }
/* 735 */     LOGGER.debug("Failed to find path from {} to {}", startIndex, endIndex);
/* 736 */     if (finalNode != null) {
/* 737 */       finalNode.cameFrom = closest;
/* 738 */       closest = finalNode;
/*     */     } 
/* 740 */     return reconstructPath(from, closest);
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node from, Node to) {
/* 744 */     List<Node> nodes = Lists.newArrayList();
/* 745 */     Node node = to;
/* 746 */     nodes.add(0, node);
/* 747 */     while (node.cameFrom != null) {
/* 748 */       node = node.cameFrom;
/* 749 */       nodes.add(0, node);
/*     */     } 
/* 751 */     return new Path(nodes, new BlockPos(to.x, to.y, to.z), true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 756 */     super.addAdditionalSaveData(output);
/* 757 */     output.putInt("DragonPhase", this.phaseManager.getCurrentPhase().getPhase().getId());
/* 758 */     output.putInt("DragonDeathTime", this.dragonDeathTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 763 */     super.readAdditionalSaveData(input);
/* 764 */     input.getInt("DragonPhase").ifPresent(phaseId -> this.phaseManager.setPhase(EnderDragonPhase.getById(phaseId)));
/*     */ 
/*     */     
/* 767 */     this.dragonDeathTime = input.getIntOr("DragonDeathTime", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {}
/*     */ 
/*     */   
/*     */   public EnderDragonPart[] getSubEntities() {
/* 775 */     return this.subEntities;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 780 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 785 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 790 */     return SoundEvents.ENDER_DRAGON_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 795 */     return SoundEvents.ENDER_DRAGON_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 800 */     return 5.0F;
/*     */   }
/*     */   public Vec3 getHeadLookVector(float a) {
/*     */     Vec3 result;
/* 804 */     DragonPhaseInstance phaseInstance = this.phaseManager.getCurrentPhase();
/* 805 */     EnderDragonPhase<? extends DragonPhaseInstance> phase = phaseInstance.getPhase();
/*     */ 
/*     */     
/* 808 */     if (phase == EnderDragonPhase.LANDING || phase == EnderDragonPhase.TAKEOFF) {
/* 809 */       BlockPos egg = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.fightOrigin));
/* 810 */       float dist = Math.max((float)Math.sqrt(egg.distToCenterSqr((Position)position())) / 4.0F, 1.0F);
/* 811 */       float yOffset = 6.0F / dist;
/*     */       
/* 813 */       float xRotOld = getXRot();
/* 814 */       float rotScale = 1.5F;
/* 815 */       setXRot(-yOffset * 1.5F * 5.0F);
/*     */       
/* 817 */       result = getViewVector(a);
/* 818 */       setXRot(xRotOld);
/* 819 */     } else if (phaseInstance.isSitting()) {
/* 820 */       float xRotOld = getXRot();
/* 821 */       float rotScale = 1.5F;
/* 822 */       setXRot(-45.0F);
/*     */       
/* 824 */       result = getViewVector(a);
/* 825 */       setXRot(xRotOld);
/*     */     } else {
/* 827 */       result = getViewVector(a);
/*     */     } 
/*     */     
/* 830 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(ServerLevel level, EndCrystal crystal, BlockPos pos, DamageSource source) {
/*     */     Player player;
/* 836 */     Entity entity = source.getEntity(); if (entity instanceof Player) { Player playerSource = (Player)entity;
/* 837 */       player = playerSource; }
/*     */     else
/* 839 */     { player = level.getNearestPlayer(CRYSTAL_DESTROY_TARGETING, pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */     
/* 842 */     if (crystal == this.nearestCrystal) {
/* 843 */       hurt(level, this.head, damageSources().explosion(crystal, (Entity)player), 10.0F);
/*     */     }
/*     */     
/* 846 */     this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, source, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 851 */     if (DATA_PHASE.equals(accessor) && level().isClientSide()) {
/* 852 */       this.phaseManager.setPhase(EnderDragonPhase.getById((Integer)getEntityData().get(DATA_PHASE)));
/*     */     }
/*     */     
/* 855 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public EnderDragonPhaseManager getPhaseManager() {
/* 859 */     return this.phaseManager;
/*     */   }
/*     */   
/*     */   public EndDragonFight getDragonFight() {
/* 863 */     return this.dragonFight;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addEffect(MobEffectInstance newEffect, Entity source) {
/* 868 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRide(Entity vehicle) {
/* 873 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/* 878 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 883 */     super.recreateFromPacket(packet);
/* 884 */     EnderDragonPart[] subEntities = getSubEntities();
/* 885 */     for (int i = 0; i < subEntities.length; i++) {
/* 886 */       subEntities[i].setId(i + packet.getId() + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 893 */     return target.canBeSeenAsEnemy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float sanitizeScale(float scale) {
/* 899 */     return 1.0F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/boss/enderdragon/EnderDragon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */