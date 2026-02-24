/*     */ package net.minecraft.world.entity.animal.polarbear;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class PolarBear extends Animal implements NeutralMob {
/*  59 */   private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float STAND_ANIMATION_TICKS = 6.0F;
/*     */   
/*     */   private float clientSideStandAnimationO;
/*     */   private float clientSideStandAnimation;
/*     */   private int warningSoundTicks;
/*  66 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public PolarBear(EntityType<? extends PolarBear> type, Level level) {
/*  71 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  76 */     return (AgeableMob)EntityType.POLAR_BEAR.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  86 */     super.registerGoals();
/*     */     
/*  88 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  89 */     this.goalSelector.addGoal(1, (Goal)new PolarBearMeleeAttackGoal());
/*  90 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D, bear -> bear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
/*  91 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.25D));
/*  92 */     this.goalSelector.addGoal(5, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D));
/*  93 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  94 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  96 */     this.targetSelector.addGoal(1, (Goal)new PolarBearHurtByTargetGoal());
/*  97 */     this.targetSelector.addGoal(2, (Goal)new PolarBearAttackPlayersGoal());
/*  98 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  99 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Fox.class, 10, true, true, null));
/* 100 */     this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 104 */     return Animal.createAnimalAttributes()
/* 105 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 106 */       .add(Attributes.FOLLOW_RANGE, 20.0D)
/* 107 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 108 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPolarBearSpawnRules(EntityType<PolarBear> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 112 */     Holder<Biome> biome = level.getBiome(pos);
/*     */     
/* 114 */     if (biome.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
/* 115 */       return (isBrightEnoughToSpawn((BlockAndTintGetter)level, pos) && level.getBlockState(pos.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE));
/*     */     }
/*     */     
/* 118 */     return checkAnimalSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 123 */     super.readAdditionalSaveData(input);
/* 124 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 129 */     super.addAdditionalSaveData(output);
/* 130 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 135 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerEndTime(long endTime) {
/* 140 */     this.persistentAngerEndTime = endTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 145 */     return this.persistentAngerEndTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/* 150 */     this.persistentAngerTarget = persistentAngerTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 155 */     return this.persistentAngerTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     if (isBaby()) {
/* 161 */       return SoundEvents.POLAR_BEAR_AMBIENT_BABY;
/*     */     }
/* 163 */     return SoundEvents.POLAR_BEAR_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 168 */     return SoundEvents.POLAR_BEAR_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 173 */     return SoundEvents.POLAR_BEAR_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState blockState) {
/* 178 */     playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playWarningSound() {
/* 182 */     if (this.warningSoundTicks <= 0) {
/* 183 */       makeSound(SoundEvents.POLAR_BEAR_WARNING);
/*     */       
/* 185 */       this.warningSoundTicks = 40;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 191 */     super.defineSynchedData(entityData);
/*     */     
/* 193 */     entityData.define(DATA_STANDING_ID, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 198 */     super.tick();
/*     */     
/* 200 */     if (level().isClientSide()) {
/* 201 */       if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
/* 202 */         refreshDimensions();
/*     */       }
/* 204 */       this.clientSideStandAnimationO = this.clientSideStandAnimation;
/* 205 */       if (isStanding()) {
/* 206 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
/*     */       } else {
/* 208 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     if (this.warningSoundTicks > 0) {
/* 213 */       this.warningSoundTicks--;
/*     */     }
/*     */     
/* 216 */     if (!level().isClientSide()) {
/* 217 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 223 */     if (this.clientSideStandAnimation > 0.0F) {
/*     */       
/* 225 */       float standFactor = this.clientSideStandAnimation / 6.0F;
/* 226 */       float heightScaleFactor = 1.0F + standFactor;
/* 227 */       return super.getDefaultDimensions(pose).scale(1.0F, heightScaleFactor);
/*     */     } 
/* 229 */     return super.getDefaultDimensions(pose);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStanding() {
/* 234 */     return (Boolean)this.entityData.get(DATA_STANDING_ID);
/*     */   }
/*     */   
/*     */   public void setStanding(boolean value) {
/* 238 */     this.entityData.set(DATA_STANDING_ID, value);
/*     */   }
/*     */   
/*     */   public float getStandingAnimationScale(float a) {
/* 242 */     return Mth.lerp(a, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getWaterSlowDown() {
/* 247 */     return 0.98F;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 252 */     if (groupData == null) {
/* 253 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(1.0F);
/*     */     }
/*     */     
/* 256 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class PolarBearHurtByTargetGoal
/*     */     extends HurtByTargetGoal
/*     */   {
/*     */     public PolarBearHurtByTargetGoal() {
/* 265 */       super((PathfinderMob)PolarBear.this, new Class<?>[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 270 */       super.start();
/* 271 */       if (PolarBear.this.isBaby()) {
/* 272 */         alertOthers();
/* 273 */         stop();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 279 */       if (other instanceof PolarBear && 
/* 280 */         !other.isBaby()) {
/* 281 */         super.alertOther(other, hurtByMob);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class PolarBearAttackPlayersGoal
/*     */     extends NearestAttackableTargetGoal<Player>
/*     */   {
/*     */     public PolarBearAttackPlayersGoal() {
/* 293 */       super((Mob)PolarBear.this, Player.class, 20, true, true, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 298 */       if (PolarBear.this.isBaby()) {
/* 299 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 303 */       if (super.canUse()) {
/* 304 */         List<PolarBear> bears = PolarBear.this.level().getEntitiesOfClass(PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/* 305 */         for (PolarBear bear : bears) {
/* 306 */           if (bear.isBaby()) {
/* 307 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 312 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getFollowDistance() {
/* 317 */       return super.getFollowDistance() * 0.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   private class PolarBearMeleeAttackGoal extends MeleeAttackGoal {
/*     */     public PolarBearMeleeAttackGoal() {
/* 323 */       super((PathfinderMob)PolarBear.this, 1.25D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void checkAndPerformAttack(LivingEntity target) {
/* 328 */       if (canPerformAttack(target)) {
/* 329 */         resetAttackCooldown();
/* 330 */         this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)target);
/* 331 */         PolarBear.this.setStanding(false);
/* 332 */       } else if (this.mob.distanceToSqr((Entity)target) < ((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
/* 333 */         if (isTimeToAttack()) {
/* 334 */           PolarBear.this.setStanding(false);
/* 335 */           resetAttackCooldown();
/*     */         } 
/* 337 */         if (getTicksUntilNextAttack() <= 10) {
/* 338 */           PolarBear.this.setStanding(true);
/* 339 */           PolarBear.this.playWarningSound();
/*     */         } 
/*     */       } else {
/*     */         
/* 343 */         resetAttackCooldown();
/* 344 */         PolarBear.this.setStanding(false);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 350 */       PolarBear.this.setStanding(false);
/* 351 */       super.stop();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/polarbear/PolarBear.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */