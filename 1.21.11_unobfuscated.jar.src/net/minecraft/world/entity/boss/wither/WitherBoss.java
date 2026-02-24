/*     */ package net.minecraft.world.entity.boss.wither;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WitherBoss
/*     */   extends Monster
/*     */   implements RangedAttackMob {
/*  64 */   private static final EntityDataAccessor<Integer> DATA_TARGET_A = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*  65 */   private static final EntityDataAccessor<Integer> DATA_TARGET_B = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*  66 */   private static final EntityDataAccessor<Integer> DATA_TARGET_C = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*  67 */   private static final List<EntityDataAccessor<Integer>> DATA_TARGETS = (List<EntityDataAccessor<Integer>>)ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
/*  68 */   private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int INVULNERABLE_TICKS = 220;
/*     */   
/*     */   private static final int DEFAULT_INVULNERABLE_TICKS = 0;
/*  73 */   private final float[] xRotHeads = new float[2];
/*  74 */   private final float[] yRotHeads = new float[2];
/*  75 */   private final float[] xRotOHeads = new float[2];
/*  76 */   private final float[] yRotOHeads = new float[2];
/*  77 */   private final int[] nextHeadUpdate = new int[2];
/*  78 */   private final int[] idleHeadUpdates = new int[2]; private int destroyBlocksTick;
/*     */   private static final TargetingConditions.Selector LIVING_ENTITY_SELECTOR;
/*  80 */   private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
/*     */   static {
/*  82 */     LIVING_ENTITY_SELECTOR = ((target, level) -> (!target.getType().is(EntityTypeTags.WITHER_FRIENDS) && target.attackable()));
/*  83 */   } private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0D).selector(LIVING_ENTITY_SELECTOR);
/*     */   
/*     */   public WitherBoss(EntityType<? extends WitherBoss> type, Level level) {
/*  86 */     super(type, level);
/*     */     
/*  88 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 10, false);
/*     */     
/*  90 */     setHealth(getMaxHealth());
/*     */     
/*  92 */     this.xpReward = 50;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  97 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level);
/*  98 */     flyingPathNavigation.setCanOpenDoors(false);
/*  99 */     flyingPathNavigation.setCanFloat(true);
/* 100 */     return (PathNavigation)flyingPathNavigation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 105 */     this.goalSelector.addGoal(0, new WitherDoNothingGoal());
/* 106 */     this.goalSelector.addGoal(2, (Goal)new RangedAttackGoal(this, 1.0D, 40, 20.0F));
/*     */     
/* 108 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomFlyingGoal((PathfinderMob)this, 1.0D));
/* 109 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 110 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/* 112 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class<?>[0]));
/* 113 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 118 */     super.defineSynchedData(entityData);
/*     */     
/* 120 */     entityData.define(DATA_TARGET_A, 0);
/* 121 */     entityData.define(DATA_TARGET_B, 0);
/* 122 */     entityData.define(DATA_TARGET_C, 0);
/* 123 */     entityData.define(DATA_ID_INV, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 128 */     super.addAdditionalSaveData(output);
/*     */     
/* 130 */     output.putInt("Invul", getInvulnerableTicks());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 135 */     super.readAdditionalSaveData(input);
/*     */     
/* 137 */     setInvulnerableTicks(input.getIntOr("Invul", 0));
/* 138 */     if (hasCustomName()) {
/* 139 */       this.bossEvent.setName(getDisplayName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(Component name) {
/* 145 */     super.setCustomName(name);
/* 146 */     this.bossEvent.setName(getDisplayName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 151 */     return SoundEvents.WITHER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 156 */     return SoundEvents.WITHER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 161 */     return SoundEvents.WITHER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 166 */     Vec3 deltaMovement = getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
/*     */     
/* 168 */     if (!level().isClientSide() && getAlternativeTarget(0) > 0) {
/* 169 */       Entity entity = level().getEntity(getAlternativeTarget(0));
/* 170 */       if (entity != null) {
/* 171 */         double yd = deltaMovement.y;
/* 172 */         if (getY() < entity.getY() || (!isPowered() && getY() < entity.getY() + 5.0D)) {
/* 173 */           yd = Math.max(0.0D, yd);
/*     */           
/* 175 */           yd += 0.3D - yd * 0.6000000238418579D;
/*     */         } 
/* 177 */         deltaMovement = new Vec3(deltaMovement.x, yd, deltaMovement.z);
/*     */         
/* 179 */         Vec3 delta = new Vec3(entity.getX() - getX(), 0.0D, entity.getZ() - getZ());
/* 180 */         if (delta.horizontalDistanceSqr() > 9.0D) {
/* 181 */           Vec3 scale = delta.normalize();
/* 182 */           deltaMovement = deltaMovement.add(scale.x * 0.3D - deltaMovement.x * 0.6D, 0.0D, scale.z * 0.3D - deltaMovement.z * 0.6D);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     setDeltaMovement(deltaMovement);
/* 192 */     if (deltaMovement.horizontalDistanceSqr() > 0.05D) {
/* 193 */       setYRot((float)Mth.atan2(deltaMovement.z, deltaMovement.x) * 57.295776F - 90.0F);
/*     */     }
/* 195 */     super.aiStep();
/*     */     
/* 197 */     for (int i = 0; i < 2; i++) {
/* 198 */       this.yRotOHeads[i] = this.yRotHeads[i];
/* 199 */       this.xRotOHeads[i] = this.xRotHeads[i];
/*     */     } 
/*     */     
/* 202 */     for (int j = 0; j < 2; j++) {
/* 203 */       int entityId = getAlternativeTarget(j + 1);
/* 204 */       Entity entity = null;
/* 205 */       if (entityId > 0) {
/* 206 */         entity = level().getEntity(entityId);
/*     */       }
/* 208 */       if (entity != null) {
/* 209 */         double hx = getHeadX(j + 1);
/* 210 */         double hy = getHeadY(j + 1);
/* 211 */         double hz = getHeadZ(j + 1);
/*     */         
/* 213 */         double xd = entity.getX() - hx;
/* 214 */         double yd = entity.getEyeY() - hy;
/* 215 */         double zd = entity.getZ() - hz;
/* 216 */         double sd = Math.sqrt(xd * xd + zd * zd);
/*     */         
/* 218 */         float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 219 */         float xRotD = (float)-(Mth.atan2(yd, sd) * 57.2957763671875D);
/* 220 */         this.xRotHeads[j] = rotlerp(this.xRotHeads[j], xRotD, 40.0F);
/* 221 */         this.yRotHeads[j] = rotlerp(this.yRotHeads[j], yRotD, 10.0F);
/*     */       } else {
/* 223 */         this.yRotHeads[j] = rotlerp(this.yRotHeads[j], this.yBodyRot, 10.0F);
/*     */       } 
/*     */     } 
/* 226 */     boolean isPowered = isPowered();
/* 227 */     for (int k = 0; k < 3; k++) {
/* 228 */       double hx = getHeadX(k);
/* 229 */       double hy = getHeadY(k);
/* 230 */       double hz = getHeadZ(k);
/*     */       
/* 232 */       float radius = 0.3F * getScale();
/* 233 */       level().addParticle((ParticleOptions)ParticleTypes.SMOKE, hx + this.random.nextGaussian() * radius, hy + this.random.nextGaussian() * radius, hz + this.random.nextGaussian() * radius, 0.0D, 0.0D, 0.0D);
/* 234 */       if (isPowered && (level()).random.nextInt(4) == 0) {
/* 235 */         level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.5F), hx + this.random.nextGaussian() * radius, hy + this.random.nextGaussian() * radius, hz + this.random.nextGaussian() * radius, 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/* 238 */     if (getInvulnerableTicks() > 0) {
/* 239 */       float height = 3.3F * getScale();
/* 240 */       for (int m = 0; m < 3; m++) {
/* 241 */         level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.9F), getX() + this.random.nextGaussian(), getY() + (this.random.nextFloat() * height), getZ() + this.random.nextGaussian(), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 248 */     if (getInvulnerableTicks() > 0) {
/* 249 */       int newCount = getInvulnerableTicks() - 1;
/* 250 */       this.bossEvent.setProgress(1.0F - newCount / 220.0F);
/*     */       
/* 252 */       if (newCount <= 0) {
/* 253 */         level.explode((Entity)this, getX(), getEyeY(), getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
/* 254 */         if (!isSilent()) {
/* 255 */           level.globalLevelEvent(1023, blockPosition(), 0);
/*     */         }
/*     */       } 
/*     */       
/* 259 */       setInvulnerableTicks(newCount);
/* 260 */       if (this.tickCount % 10 == 0) {
/* 261 */         heal(10.0F);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 267 */     super.customServerAiStep(level);
/*     */     
/* 269 */     for (int i = 1; i < 3; i++) {
/* 270 */       if (this.tickCount >= this.nextHeadUpdate[i - 1]) {
/* 271 */         this.nextHeadUpdate[i - 1] = this.tickCount + 10 + this.random.nextInt(10);
/*     */         
/* 273 */         this.idleHeadUpdates[i - 1] = this.idleHeadUpdates[i - 1] + 1; if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && this.idleHeadUpdates[i - 1] > 15) {
/* 274 */           float hrange = 10.0F;
/* 275 */           float vrange = 5.0F;
/* 276 */           double xt = Mth.nextDouble(this.random, getX() - 10.0D, getX() + 10.0D);
/* 277 */           double yt = Mth.nextDouble(this.random, getY() - 5.0D, getY() + 5.0D);
/* 278 */           double zt = Mth.nextDouble(this.random, getZ() - 10.0D, getZ() + 10.0D);
/* 279 */           performRangedAttack(i + 1, xt, yt, zt, true);
/* 280 */           this.idleHeadUpdates[i - 1] = 0;
/*     */         } 
/*     */         
/* 283 */         int headTarget = getAlternativeTarget(i);
/* 284 */         if (headTarget > 0) {
/* 285 */           LivingEntity current = (LivingEntity)level.getEntity(headTarget);
/* 286 */           if (current == null || !canAttack(current) || distanceToSqr((Entity)current) > 900.0D || !hasLineOfSight((Entity)current)) {
/* 287 */             setAlternativeTarget(i, 0);
/*     */           } else {
/* 289 */             performRangedAttack(i + 1, current);
/* 290 */             this.nextHeadUpdate[i - 1] = this.tickCount + 40 + this.random.nextInt(20);
/* 291 */             this.idleHeadUpdates[i - 1] = 0;
/*     */           } 
/*     */         } else {
/* 294 */           List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, (LivingEntity)this, getBoundingBox().inflate(20.0D, 8.0D, 20.0D));
/*     */           
/* 296 */           if (!entities.isEmpty()) {
/* 297 */             LivingEntity selected = entities.get(this.random.nextInt(entities.size()));
/* 298 */             setAlternativeTarget(i, selected.getId());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 303 */     if (getTarget() != null) {
/* 304 */       setAlternativeTarget(0, getTarget().getId());
/*     */     } else {
/* 306 */       setAlternativeTarget(0, 0);
/*     */     } 
/*     */     
/* 309 */     if (this.destroyBlocksTick > 0) {
/* 310 */       this.destroyBlocksTick--;
/*     */       
/* 312 */       if (this.destroyBlocksTick == 0 && (Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)) {
/*     */         boolean destroyed = false;
/*     */         
/* 315 */         int width = Mth.floor(getBbWidth() / 2.0F + 1.0F);
/* 316 */         int height = Mth.floor(getBbHeight());
/*     */         
/* 318 */         for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(
/* 319 */             getBlockX() - width, getBlockY(), getBlockZ() - width, 
/* 320 */             getBlockX() + width, getBlockY() + height, getBlockZ() + width)) {
/*     */           
/* 322 */           BlockState state = level.getBlockState(blockPos);
/* 323 */           if (canDestroy(state)) {
/* 324 */             destroyed = (level.destroyBlock(blockPos, true, (Entity)this) || destroyed);
/*     */           }
/*     */         } 
/* 327 */         if (destroyed) {
/* 328 */           level.levelEvent(null, 1022, blockPosition(), 0);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 333 */     if (this.tickCount % 20 == 0) {
/* 334 */       heal(1.0F);
/*     */     }
/*     */     
/* 337 */     this.bossEvent.setProgress(getHealth() / getMaxHealth());
/*     */   }
/*     */   
/*     */   public static boolean canDestroy(BlockState state) {
/* 341 */     return (!state.isAir() && !state.is(BlockTags.WITHER_IMMUNE));
/*     */   }
/*     */   
/*     */   public void makeInvulnerable() {
/* 345 */     setInvulnerableTicks(220);
/* 346 */     this.bossEvent.setProgress(0.0F);
/* 347 */     setHealth(getMaxHealth() / 3.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeStuckInBlock(BlockState blockState, Vec3 speedMultiplier) {}
/*     */ 
/*     */   
/*     */   public void startSeenByPlayer(ServerPlayer player) {
/* 356 */     super.startSeenByPlayer(player);
/* 357 */     this.bossEvent.addPlayer(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopSeenByPlayer(ServerPlayer player) {
/* 362 */     super.stopSeenByPlayer(player);
/* 363 */     this.bossEvent.removePlayer(player);
/*     */   }
/*     */   
/*     */   private double getHeadX(int index) {
/* 367 */     if (index <= 0) {
/* 368 */       return getX();
/*     */     }
/* 370 */     float headAngle = (this.yBodyRot + (180 * (index - 1))) * 0.017453292F;
/* 371 */     float cos = Mth.cos(headAngle);
/* 372 */     return getX() + cos * 1.3D * getScale();
/*     */   }
/*     */   
/*     */   private double getHeadY(int index) {
/* 376 */     float height = (index <= 0) ? 3.0F : 2.2F;
/* 377 */     return getY() + (height * getScale());
/*     */   }
/*     */   
/*     */   private double getHeadZ(int index) {
/* 381 */     if (index <= 0) {
/* 382 */       return getZ();
/*     */     }
/* 384 */     float headAngle = (this.yBodyRot + (180 * (index - 1))) * 0.017453292F;
/* 385 */     float sin = Mth.sin(headAngle);
/* 386 */     return getZ() + sin * 1.3D * getScale();
/*     */   }
/*     */   
/*     */   private float rotlerp(float a, float b, float max) {
/* 390 */     float diff = Mth.wrapDegrees(b - a);
/* 391 */     if (diff > max) {
/* 392 */       diff = max;
/*     */     }
/* 394 */     if (diff < -max) {
/* 395 */       diff = -max;
/*     */     }
/* 397 */     return a + diff;
/*     */   }
/*     */   
/*     */   private void performRangedAttack(int head, LivingEntity target) {
/* 401 */     performRangedAttack(head, target.getX(), target.getY() + target.getEyeHeight() * 0.5D, target.getZ(), (head == 0 && this.random.nextFloat() < 0.001F));
/*     */   }
/*     */   
/*     */   private void performRangedAttack(int head, double tx, double ty, double tz, boolean dangerous) {
/* 405 */     if (!isSilent()) {
/* 406 */       level().levelEvent(null, 1024, blockPosition(), 0);
/*     */     }
/*     */     
/* 409 */     double hx = getHeadX(head);
/* 410 */     double hy = getHeadY(head);
/* 411 */     double hz = getHeadZ(head);
/*     */     
/* 413 */     double xd = tx - hx;
/* 414 */     double yd = ty - hy;
/* 415 */     double zd = tz - hz;
/* 416 */     Vec3 direction = new Vec3(xd, yd, zd);
/*     */     
/* 418 */     WitherSkull entity = new WitherSkull(level(), (LivingEntity)this, direction.normalize());
/* 419 */     entity.setOwner((Entity)this);
/* 420 */     if (dangerous) {
/* 421 */       entity.setDangerous(true);
/*     */     }
/*     */     
/* 424 */     entity.setPos(hx, hy, hz);
/* 425 */     level().addFreshEntity((Entity)entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 430 */     performRangedAttack(0, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 435 */     if (isInvulnerableTo(level, source)) {
/* 436 */       return false;
/*     */     }
/* 438 */     if (source.is(DamageTypeTags.WITHER_IMMUNE_TO) || source.getEntity() instanceof WitherBoss) {
/* 439 */       return false;
/*     */     }
/* 441 */     if (getInvulnerableTicks() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/* 442 */       return false;
/*     */     }
/*     */     
/* 445 */     if (isPowered()) {
/* 446 */       Entity directEntity = source.getDirectEntity();
/* 447 */       if (directEntity instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow || directEntity instanceof net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge) {
/* 448 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 452 */     Entity sourceEntity = source.getEntity();
/* 453 */     if (sourceEntity != null && 
/* 454 */       sourceEntity.getType().is(EntityTypeTags.WITHER_FRIENDS))
/*     */     {
/* 456 */       return false;
/*     */     }
/*     */     
/* 459 */     if (this.destroyBlocksTick <= 0) {
/* 460 */       this.destroyBlocksTick = 20;
/*     */     }
/*     */     
/* 463 */     for (int i = 0; i < this.idleHeadUpdates.length; i++) {
/* 464 */       this.idleHeadUpdates[i] = this.idleHeadUpdates[i] + 3;
/*     */     }
/* 466 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/* 471 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/* 472 */     ItemEntity netherStar = spawnAtLocation(level, (ItemLike)Items.NETHER_STAR);
/* 473 */     if (netherStar != null) {
/* 474 */       netherStar.setExtendedLifetime();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkDespawn() {
/* 480 */     if (level().getDifficulty() == Difficulty.PEACEFUL && !getType().isAllowedInPeaceful()) {
/* 481 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/* 485 */     this.noActionTime = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addEffect(MobEffectInstance newEffect, Entity source) {
/* 490 */     return false;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 494 */     return Monster.createMonsterAttributes()
/* 495 */       .add(Attributes.MAX_HEALTH, 300.0D)
/* 496 */       .add(Attributes.MOVEMENT_SPEED, 0.6000000238418579D)
/* 497 */       .add(Attributes.FLYING_SPEED, 0.6000000238418579D)
/* 498 */       .add(Attributes.FOLLOW_RANGE, 40.0D)
/* 499 */       .add(Attributes.ARMOR, 4.0D);
/*     */   }
/*     */   
/*     */   public float[] getHeadYRots() {
/* 503 */     return this.yRotHeads;
/*     */   }
/*     */   
/*     */   public float[] getHeadXRots() {
/* 507 */     return this.xRotHeads;
/*     */   }
/*     */   
/*     */   public int getInvulnerableTicks() {
/* 511 */     return (Integer)this.entityData.get(DATA_ID_INV);
/*     */   }
/*     */   
/*     */   public void setInvulnerableTicks(int invulnerableTicks) {
/* 515 */     this.entityData.set(DATA_ID_INV, invulnerableTicks);
/*     */   }
/*     */   
/*     */   public int getAlternativeTarget(int headIndex) {
/* 519 */     return (Integer)this.entityData.get(DATA_TARGETS.get(headIndex));
/*     */   }
/*     */   
/*     */   public void setAlternativeTarget(int headIndex, int entityId) {
/* 523 */     this.entityData.set(DATA_TARGETS.get(headIndex), entityId);
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/* 527 */     return (getHealth() <= getMaxHealth() / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRide(Entity vehicle) {
/* 532 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/* 537 */     return false;
/*     */   }
/*     */   
/*     */   private class WitherDoNothingGoal extends Goal {
/*     */     public WitherDoNothingGoal() {
/* 542 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 547 */       return (WitherBoss.this.getInvulnerableTicks() > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 553 */     if (newEffect.is(MobEffects.WITHER)) {
/* 554 */       return false;
/*     */     }
/* 556 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/boss/wither/WitherBoss.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */