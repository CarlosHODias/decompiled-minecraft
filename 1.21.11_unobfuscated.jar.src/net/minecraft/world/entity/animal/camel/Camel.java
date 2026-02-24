/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Camel
/*     */   extends AbstractHorse
/*     */ {
/*     */   public static final float BABY_SCALE = 0.45F;
/*     */   public static final int DASH_COOLDOWN_TICKS = 55;
/*     */   public static final int MAX_HEAD_Y_ROT = 30;
/*     */   private static final float RUNNING_SPEED_BONUS = 0.1F;
/*     */   private static final float DASH_VERTICAL_MOMENTUM = 1.4285F;
/*     */   private static final float DASH_HORIZONTAL_MOMENTUM = 22.2222F;
/*     */   private static final int DASH_MINIMUM_DURATION_TICKS = 5;
/*     */   private static final int SITDOWN_DURATION_TICKS = 40;
/*     */   private static final int STANDUP_DURATION_TICKS = 52;
/*     */   private static final int IDLE_MINIMAL_DURATION_TICKS = 80;
/*     */   private static final float SITTING_HEIGHT_DIFFERENCE = 1.43F;
/*     */   private static final long DEFAULT_LAST_POSE_CHANGE_TICK = 0L;
/*  80 */   public static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(Camel.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  82 */   public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(Camel.class, EntityDataSerializers.LONG);
/*     */   
/*  84 */   public final AnimationState sitAnimationState = new AnimationState();
/*  85 */   public final AnimationState sitPoseAnimationState = new AnimationState();
/*  86 */   public final AnimationState sitUpAnimationState = new AnimationState();
/*  87 */   public final AnimationState idleAnimationState = new AnimationState();
/*  88 */   public final AnimationState dashAnimationState = new AnimationState();
/*     */   
/*  90 */   private static final EntityDimensions SITTING_DIMENSIONS = EntityDimensions.scalable(EntityType.CAMEL.getWidth(), EntityType.CAMEL.getHeight() - 1.43F).withEyeHeight(0.845F);
/*     */   
/*  92 */   private int dashCooldown = 0;
/*     */ 
/*     */   
/*  95 */   private int idleAnimationTimeout = 0;
/*     */   
/*     */   public Camel(EntityType<? extends Camel> type, Level level) {
/*  98 */     super(type, level);
/*  99 */     this.moveControl = new CamelMoveControl();
/* 100 */     this.lookControl = new CamelLookControl();
/* 101 */     GroundPathNavigation navigation = (GroundPathNavigation)getNavigation();
/* 102 */     navigation.setCanFloat(true);
/* 103 */     navigation.setCanWalkOverFences(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 108 */     super.addAdditionalSaveData(output);
/* 109 */     output.putLong("LastPoseTick", (Long)this.entityData.get(LAST_POSE_CHANGE_TICK));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 114 */     super.readAdditionalSaveData(input);
/* 115 */     long poseTick = input.getLongOr("LastPoseTick", 0L);
/* 116 */     if (poseTick < 0L) {
/* 117 */       setPose(Pose.SITTING);
/*     */     }
/* 119 */     resetLastPoseChangeTick(poseTick);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 123 */     return createBaseHorseAttributes()
/* 124 */       .add(Attributes.MAX_HEALTH, 32.0D)
/* 125 */       .add(Attributes.MOVEMENT_SPEED, 0.09000000357627869D)
/* 126 */       .add(Attributes.JUMP_STRENGTH, 0.41999998688697815D)
/* 127 */       .add(Attributes.STEP_HEIGHT, 1.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 132 */     super.defineSynchedData(entityData);
/* 133 */     entityData.define(DASH, false);
/* 134 */     entityData.define(LAST_POSE_CHANGE_TICK, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 139 */     CamelAi.initMemories(this, level.getRandom());
/* 140 */     resetLastPoseChangeTickToFullStand(level.getLevel().getGameTime());
/* 141 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static boolean checkCamelSpawnRules(EntityType<Camel> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 145 */     return (level.getBlockState(pos.below()).is(BlockTags.CAMELS_SPAWNABLE_ON) && isBrightEnoughToSpawn((BlockAndTintGetter)level, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Camel> brainProvider() {
/* 150 */     return CamelAi.brainProvider();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/* 161 */     return CamelAi.makeBrain(brainProvider().makeBrain(input));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 166 */     return (pose == Pose.SITTING) ? SITTING_DIMENSIONS.scale(getAgeScale()) : super.getDefaultDimensions(pose);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 171 */     ProfilerFiller profiler = Profiler.get();
/* 172 */     profiler.push("camelBrain");
/* 173 */     Brain<?> brain = getBrain();
/* 174 */     brain.tick(level, (LivingEntity)this);
/* 175 */     profiler.pop();
/*     */     
/* 177 */     profiler.push("camelActivityUpdate");
/* 178 */     CamelAi.updateActivity(this);
/* 179 */     profiler.pop();
/*     */     
/* 181 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 186 */     super.tick();
/* 187 */     if (isDashing() && this.dashCooldown < 50 && (onGround() || isInLiquid() || isPassenger())) {
/* 188 */       setDashing(false);
/*     */     }
/* 190 */     if (this.dashCooldown > 0) {
/* 191 */       this.dashCooldown--;
/* 192 */       if (this.dashCooldown == 0) {
/* 193 */         level().playSound(null, blockPosition(), getDashReadySound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
/*     */       }
/*     */     } 
/* 196 */     if (level().isClientSide()) {
/* 197 */       setupAnimationStates();
/*     */     }
/*     */     
/* 200 */     if (refuseToMove()) {
/* 201 */       clampHeadRotationToBody();
/*     */     }
/*     */     
/* 204 */     if (isCamelSitting() && isInWater()) {
/* 205 */       standUpInstantly();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 210 */     if (this.idleAnimationTimeout <= 0) {
/* 211 */       this.idleAnimationTimeout = this.random.nextInt(40) + 80;
/* 212 */       this.idleAnimationState.start(this.tickCount);
/*     */     } else {
/* 214 */       this.idleAnimationTimeout--;
/*     */     } 
/* 216 */     if (isCamelVisuallySitting()) {
/* 217 */       this.sitUpAnimationState.stop();
/* 218 */       this.dashAnimationState.stop();
/* 219 */       if (isVisuallySittingDown()) {
/* 220 */         this.sitAnimationState.startIfStopped(this.tickCount);
/* 221 */         this.sitPoseAnimationState.stop();
/*     */       } else {
/* 223 */         this.sitAnimationState.stop();
/* 224 */         this.sitPoseAnimationState.startIfStopped(this.tickCount);
/*     */       } 
/*     */     } else {
/* 227 */       this.sitAnimationState.stop();
/* 228 */       this.sitPoseAnimationState.stop();
/* 229 */       this.dashAnimationState.animateWhen(isDashing(), this.tickCount);
/* 230 */       this.sitUpAnimationState.animateWhen((isInPoseTransition() && getPoseTime() >= 0L), this.tickCount);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateWalkAnimation(float distance) {
/*     */     float targetSpeed;
/* 237 */     if (getPose() == Pose.STANDING && !this.dashAnimationState.isStarted()) {
/* 238 */       targetSpeed = Math.min(distance * 6.0F, 1.0F);
/*     */     } else {
/* 240 */       targetSpeed = 0.0F;
/*     */     } 
/* 242 */     this.walkAnimation.update(targetSpeed, 0.2F, isBaby() ? 3.0F : 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 input) {
/* 247 */     if (refuseToMove() && onGround()) {
/* 248 */       setDeltaMovement(getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
/* 249 */       input = input.multiply(0.0D, 1.0D, 0.0D);
/*     */     } 
/* 251 */     super.travel(input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 256 */     super.tickRidden(controller, riddenInput);
/* 257 */     if (controller.zza > 0.0F && 
/* 258 */       isCamelSitting() && !isInPoseTransition()) {
/* 259 */       standUp();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean refuseToMove() {
/* 265 */     return (isCamelSitting() || isInPoseTransition());
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getRiddenSpeed(Player controller) {
/* 270 */     float movementBonus = (controller.isSprinting() && getJumpCooldown() == 0) ? 0.1F : 0.0F;
/* 271 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED) + movementBonus;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec2 getRiddenRotation(LivingEntity controller) {
/* 276 */     if (refuseToMove()) {
/* 277 */       return new Vec2(getXRot(), getYRot());
/*     */     }
/* 279 */     return super.getRiddenRotation(controller);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 284 */     if (refuseToMove()) {
/* 285 */       return Vec3.ZERO;
/*     */     }
/* 287 */     return super.getRiddenInput(controller, selfInput);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canJump() {
/* 292 */     return (!refuseToMove() && super.canJump());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerJump(int jumpAmount) {
/* 297 */     if (!isSaddled() || this.dashCooldown > 0 || !onGround()) {
/*     */       return;
/*     */     }
/* 300 */     super.onPlayerJump(jumpAmount);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSprint() {
/* 305 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void executeRidersJump(float amount, Vec3 input) {
/* 310 */     double jumpMomentum = getJumpPower();
/*     */     
/* 312 */     addDeltaMovement(getLookAngle().multiply(1.0D, 0.0D, 1.0D).normalize()
/* 313 */         .scale((22.2222F * amount) * getAttributeValue(Attributes.MOVEMENT_SPEED) * getBlockSpeedFactor())
/* 314 */         .add(0.0D, (1.4285F * amount) * jumpMomentum, 0.0D));
/*     */ 
/*     */     
/* 317 */     this.dashCooldown = 55;
/* 318 */     setDashing(true);
/* 319 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   public boolean isDashing() {
/* 323 */     return (Boolean)this.entityData.get(DASH);
/*     */   }
/*     */   
/*     */   public void setDashing(boolean isDashing) {
/* 327 */     this.entityData.set(DASH, isDashing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStartJump(int jumpScale) {
/* 332 */     makeSound(getDashingSound());
/* 333 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 334 */     setDashing(true);
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashingSound() {
/* 338 */     return SoundEvents.CAMEL_DASH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 342 */     return SoundEvents.CAMEL_DASH_READY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleStopJump() {}
/*     */ 
/*     */   
/*     */   public int getJumpCooldown() {
/* 351 */     return this.dashCooldown;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 356 */     return SoundEvents.CAMEL_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 361 */     return SoundEvents.CAMEL_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 366 */     return SoundEvents.CAMEL_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 371 */     if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
/* 372 */       playSound(SoundEvents.CAMEL_STEP_SAND, 1.0F, 1.0F);
/*     */     } else {
/* 374 */       playSound(SoundEvents.CAMEL_STEP, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 380 */     return itemStack.is(ItemTags.CAMEL_FOOD);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 385 */     ItemStack itemStack = player.getItemInHand(hand);
/*     */     
/* 387 */     if (player.isSecondaryUseActive() && !isBaby()) {
/* 388 */       openCustomInventoryScreen(player);
/* 389 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/* 393 */     InteractionResult interactionResult = itemStack.interactLivingEntity(player, (LivingEntity)this, hand);
/* 394 */     if (interactionResult.consumesAction()) {
/* 395 */       return interactionResult;
/*     */     }
/*     */     
/* 398 */     if (isFood(itemStack)) {
/* 399 */       return fedFood(player, itemStack);
/*     */     }
/*     */     
/* 402 */     if (getPassengers().size() < 2 && !isBaby()) {
/* 403 */       doPlayerRide(player);
/*     */     }
/* 405 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onElasticLeashPull() {
/* 410 */     super.onElasticLeashPull();
/* 411 */     if (isCamelSitting() && !isInPoseTransition() && canCamelChangePose()) {
/* 412 */       standUp();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3[] getQuadLeashOffsets() {
/* 418 */     return Leashable.createQuadLeashOffsets((Entity)this, 0.02D, 0.48D, 0.25D, 0.82D);
/*     */   }
/*     */   
/*     */   public boolean canCamelChangePose() {
/* 422 */     return wouldNotSuffocateAtTargetPose(isCamelSitting() ? Pose.STANDING : Pose.SITTING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean handleEating(Player player, ItemStack itemStack) {
/* 427 */     if (!isFood(itemStack)) {
/* 428 */       return false;
/*     */     }
/*     */     
/* 431 */     boolean couldHeal = (getHealth() < getMaxHealth());
/* 432 */     if (couldHeal) {
/* 433 */       heal(2.0F);
/*     */     }
/*     */     
/* 436 */     boolean couldSetInLove = (isTamed() && getAge() == 0 && canFallInLove());
/* 437 */     if (couldSetInLove) {
/* 438 */       setInLove(player);
/*     */     }
/*     */     
/* 441 */     boolean couldAgeUp = isBaby();
/* 442 */     if (couldAgeUp) {
/* 443 */       level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/* 444 */       if (!level().isClientSide()) {
/* 445 */         ageUp(10);
/*     */       }
/*     */     } 
/*     */     
/* 449 */     if (couldHeal || couldSetInLove || couldAgeUp) {
/* 450 */       if (!isSilent()) {
/* 451 */         SoundEvent eatingSound = getEatingSound();
/* 452 */         if (eatingSound != null) {
/* 453 */           level().playSound(null, getX(), getY(), getZ(), eatingSound, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */         }
/*     */       } 
/* 456 */       gameEvent((Holder)GameEvent.EAT);
/* 457 */       return true;
/*     */     } 
/* 459 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPerformRearing() {
/* 464 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 469 */     if (partner != this && partner instanceof Camel) { Camel camel = (Camel)partner; if (canParent() && camel.canParent()); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Camel getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 474 */     return (Camel)EntityType.CAMEL.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 479 */     return SoundEvents.CAMEL_EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 484 */     standUpInstantly();
/* 485 */     super.actuallyHurt(level, source, dmg);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 490 */     int index = Math.max(getPassengers().indexOf(passenger), 0);
/* 491 */     boolean driver = (index == 0);
/* 492 */     float offset = 0.5F;
/* 493 */     float height = (float)(isRemoved() ? 0.009999999776482582D : getBodyAnchorAnimationYOffset(driver, 0.0F, dimensions, scale));
/*     */     
/* 495 */     if (getPassengers().size() > 1) {
/* 496 */       if (!driver) {
/* 497 */         offset = -0.7F;
/*     */       }
/*     */       
/* 500 */       if (passenger instanceof Animal) {
/* 501 */         offset += 0.2F;
/*     */       }
/*     */     } 
/*     */     
/* 505 */     return new Vec3(0.0D, height, (offset * scale)).yRot(-getYRot() * 0.017453292F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAgeScale() {
/* 510 */     return isBaby() ? 0.45F : 1.0F;
/*     */   }
/*     */   
/*     */   private double getBodyAnchorAnimationYOffset(boolean isFront, float partialTicks, EntityDimensions dimensions, float scale) {
/* 514 */     double baseSitOffset = (dimensions.height() - 0.375F * scale);
/* 515 */     float sittingHeightDifference = scale * 1.43F;
/* 516 */     float verticalDrop = sittingHeightDifference - scale * 0.2F;
/* 517 */     float bottomPoint = sittingHeightDifference - verticalDrop;
/* 518 */     boolean isInTransition = isInPoseTransition();
/* 519 */     boolean isSitting = isCamelSitting();
/* 520 */     if (isInTransition) {
/*     */       int halfPoint; float flexPointOffset;
/* 522 */       int animationDuration = isSitting ? 40 : 52;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 527 */       if (isSitting) {
/* 528 */         halfPoint = 28;
/* 529 */         flexPointOffset = isFront ? 0.5F : 0.1F;
/*     */       } else {
/* 531 */         halfPoint = isFront ? 24 : 32;
/* 532 */         flexPointOffset = isFront ? 0.6F : 0.35F;
/*     */       } 
/* 534 */       float poseTime = Mth.clamp((float)getPoseTime() + partialTicks, 0.0F, animationDuration);
/* 535 */       boolean isFirstPart = (poseTime < halfPoint);
/* 536 */       float part = isFirstPart ? (poseTime / halfPoint) : ((poseTime - halfPoint) / (animationDuration - halfPoint));
/* 537 */       float flexPoint = sittingHeightDifference - flexPointOffset * verticalDrop;
/* 538 */       baseSitOffset += isSitting ? 
/* 539 */         Mth.lerp(part, isFirstPart ? sittingHeightDifference : flexPoint, isFirstPart ? flexPoint : bottomPoint) : 
/* 540 */         Mth.lerp(part, isFirstPart ? (bottomPoint - sittingHeightDifference) : (bottomPoint - flexPoint), isFirstPart ? (bottomPoint - flexPoint) : 0.0F);
/*     */     } 
/* 542 */     if (isSitting && !isInTransition) {
/* 543 */       baseSitOffset += bottomPoint;
/*     */     }
/* 545 */     return baseSitOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLeashOffset(float partialTicks) {
/* 550 */     EntityDimensions dimensions = getDimensions(getPose());
/* 551 */     float scale = getAgeScale();
/* 552 */     return new Vec3(0.0D, getBodyAnchorAnimationYOffset(true, partialTicks, dimensions, scale) - (0.2F * scale), (dimensions.width() * 0.56F));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 557 */     return 30;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canAddPassenger(Entity passenger) {
/* 562 */     return (getPassengers().size() <= 2);
/*     */   }
/*     */   
/*     */   public boolean isCamelSitting() {
/* 566 */     return ((Long)this.entityData.get(LAST_POSE_CHANGE_TICK) < 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCamelVisuallySitting() {
/* 571 */     return (((getPoseTime() < 0L)) != isCamelSitting());
/*     */   }
/*     */   
/*     */   public boolean isInPoseTransition() {
/* 575 */     long poseTime = getPoseTime();
/* 576 */     return (poseTime < (isCamelSitting() ? 40L : 52L));
/*     */   }
/*     */   
/*     */   private boolean isVisuallySittingDown() {
/* 580 */     return (isCamelSitting() && getPoseTime() < 40L && getPoseTime() >= 0L);
/*     */   }
/*     */   
/*     */   public void sitDown() {
/* 584 */     if (isCamelSitting()) {
/*     */       return;
/*     */     }
/* 587 */     makeSound(getSitDownSound());
/* 588 */     setPose(Pose.SITTING);
/* 589 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 590 */     resetLastPoseChangeTick(-level().getGameTime());
/*     */   }
/*     */   
/*     */   public void standUp() {
/* 594 */     if (!isCamelSitting()) {
/*     */       return;
/*     */     }
/* 597 */     makeSound(getStandUpSound());
/* 598 */     setPose(Pose.STANDING);
/* 599 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 600 */     resetLastPoseChangeTick(level().getGameTime());
/*     */   }
/*     */   
/*     */   protected SoundEvent getStandUpSound() {
/* 604 */     return SoundEvents.CAMEL_STAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSitDownSound() {
/* 608 */     return SoundEvents.CAMEL_SIT;
/*     */   }
/*     */   
/*     */   public void standUpInstantly() {
/* 612 */     setPose(Pose.STANDING);
/* 613 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 614 */     resetLastPoseChangeTickToFullStand(level().getGameTime());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void resetLastPoseChangeTick(long syncedPoseTickTime) {
/* 619 */     this.entityData.set(LAST_POSE_CHANGE_TICK, syncedPoseTickTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetLastPoseChangeTickToFullStand(long currentTime) {
/* 625 */     resetLastPoseChangeTick(Math.max(0L, currentTime - 52L - 1L));
/*     */   }
/*     */   
/*     */   public long getPoseTime() {
/* 629 */     return level().getGameTime() - Math.abs((Long)this.entityData.get(LAST_POSE_CHANGE_TICK));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 634 */     if (slot == EquipmentSlot.SADDLE) {
/* 635 */       return (Holder<SoundEvent>)getSaddleSound();
/*     */     }
/* 637 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */   
/*     */   protected Holder.Reference<SoundEvent> getSaddleSound() {
/* 641 */     return SoundEvents.CAMEL_SADDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 646 */     if (!this.firstTick && 
/* 647 */       DASH.equals(accessor)) {
/* 648 */       this.dashCooldown = (this.dashCooldown == 0) ? 55 : this.dashCooldown;
/*     */     }
/*     */     
/* 651 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTamed() {
/* 656 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/* 661 */     if (!level().isClientSide()) {
/* 662 */       player.openHorseInventory(this, (Container)this.inventory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 668 */     return new CamelBodyRotationControl(this);
/*     */   }
/*     */   
/*     */   private class CamelBodyRotationControl extends BodyRotationControl {
/*     */     public CamelBodyRotationControl(Camel camel) {
/* 673 */       super((Mob)camel);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clientTick() {
/* 678 */       if (!Camel.this.refuseToMove())
/* 679 */         super.clientTick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CamelLookControl
/*     */     extends LookControl {
/*     */     private CamelLookControl() {
/* 686 */       super((Mob)Camel.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 691 */       if (!Camel.this.hasControllingPassenger())
/* 692 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CamelMoveControl
/*     */     extends MoveControl {
/*     */     public CamelMoveControl() {
/* 699 */       super((Mob)Camel.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 704 */       if (this.operation == MoveControl.Operation.MOVE_TO && !Camel.this.isLeashed() && Camel.this.isCamelSitting() && !Camel.this.isInPoseTransition() && Camel.this.canCamelChangePose()) {
/* 705 */         Camel.this.standUp();
/*     */       }
/* 707 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/camel/Camel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */