/*     */ package net.minecraft.world.entity.monster.spider;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.skeleton.Skeleton;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Spider extends Monster {
/*  48 */   private static final net.minecraft.network.syncher.EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE); private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1F;
/*     */   
/*     */   public Spider(EntityType<? extends Spider> type, Level level) {
/*  51 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  56 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*     */     
/*  58 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal((PathfinderMob)this, Armadillo.class, 6.0F, 1.0D, 1.2D, entity -> !((Armadillo)entity).isScared()));
/*     */     
/*  60 */     this.goalSelector.addGoal(3, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/*  61 */     this.goalSelector.addGoal(4, (Goal)new SpiderAttackGoal(this));
/*     */     
/*  63 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D));
/*  64 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  65 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  67 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class<?>[0]));
/*  68 */     this.targetSelector.addGoal(2, (Goal)new SpiderTargetGoal<>(this, Player.class));
/*  69 */     this.targetSelector.addGoal(3, (Goal)new SpiderTargetGoal<>(this, IronGolem.class));
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  74 */     return (PathNavigation)new WallClimberNavigation((Mob)this, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  79 */     super.defineSynchedData(entityData);
/*     */     
/*  81 */     entityData.define(DATA_FLAGS_ID, (byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  86 */     super.tick();
/*     */     
/*  88 */     if (!level().isClientSide())
/*     */     {
/*     */       
/*  91 */       setClimbing(this.horizontalCollision);
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  96 */     return Monster.createMonsterAttributes()
/*  97 */       .add(Attributes.MAX_HEALTH, 16.0D)
/*  98 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 103 */     return SoundEvents.SPIDER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 108 */     return SoundEvents.SPIDER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 113 */     return SoundEvents.SPIDER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 118 */     playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onClimbable() {
/* 127 */     return isClimbing();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeStuckInBlock(BlockState state, Vec3 speedMultiplier) {
/* 133 */     if (!state.is(Blocks.COBWEB)) {
/* 134 */       super.makeStuckInBlock(state, speedMultiplier);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 140 */     if (newEffect.is(MobEffects.POISON)) {
/* 141 */       return false;
/*     */     }
/* 143 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */   
/*     */   public boolean isClimbing() {
/* 147 */     return (((Byte)this.entityData.get(DATA_FLAGS_ID) & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setClimbing(boolean value) {
/* 151 */     byte flags = (Byte)this.entityData.get(DATA_FLAGS_ID);
/* 152 */     if (value) {
/* 153 */       flags = (byte)(flags | 0x1);
/*     */     } else {
/* 155 */       flags = (byte)(flags & 0xFFFFFFFE);
/*     */     } 
/* 157 */     this.entityData.set(DATA_FLAGS_ID, flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 162 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/* 164 */     RandomSource random = level.getRandom();
/* 165 */     if (random.nextInt(100) == 0) {
/* 166 */       Skeleton skeleton = (Skeleton)EntityType.SKELETON.create(level(), EntitySpawnReason.JOCKEY);
/* 167 */       if (skeleton != null) {
/* 168 */         skeleton.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 169 */         skeleton.finalizeSpawn(level, difficulty, spawnReason, null);
/* 170 */         skeleton.startRiding((Entity)this, false, false);
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     if (groupData == null) {
/* 175 */       groupData = new SpiderEffectsGroupData();
/*     */       
/* 177 */       if (level.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
/* 178 */         ((SpiderEffectsGroupData)groupData).setRandomEffect(random);
/*     */       }
/*     */     } 
/* 181 */     if (groupData instanceof SpiderEffectsGroupData) { SpiderEffectsGroupData spiderEffectsGroupData = (SpiderEffectsGroupData)groupData;
/* 182 */       Holder<MobEffect> effect = spiderEffectsGroupData.effect;
/* 183 */       if (effect != null) {
/* 184 */         addEffect(new MobEffectInstance(effect, -1));
/*     */       } }
/*     */ 
/*     */     
/* 188 */     return groupData;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
/* 193 */     if (vehicle.getBbWidth() <= getBbWidth()) {
/* 194 */       return new Vec3(0.0D, 0.3125D * getScale(), 0.0D);
/*     */     }
/* 196 */     return super.getVehicleAttachmentPoint(vehicle);
/*     */   }
/*     */   
/*     */   public static class SpiderEffectsGroupData
/*     */     implements SpawnGroupData
/*     */   {
/*     */     public Holder<MobEffect> effect;
/*     */     
/*     */     public void setRandomEffect(RandomSource random) {
/* 205 */       int selection = random.nextInt(5);
/* 206 */       if (selection <= 1) {
/* 207 */         this.effect = MobEffects.SPEED;
/* 208 */       } else if (selection <= 2) {
/* 209 */         this.effect = MobEffects.STRENGTH;
/* 210 */       } else if (selection <= 3) {
/* 211 */         this.effect = MobEffects.REGENERATION;
/* 212 */       } else if (selection <= 4) {
/* 213 */         this.effect = MobEffects.INVISIBILITY;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpiderAttackGoal extends MeleeAttackGoal {
/*     */     public SpiderAttackGoal(Spider mob) {
/* 220 */       super((PathfinderMob)mob, 1.0D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 225 */       return (super.canUse() && !this.mob.isVehicle());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 230 */       float br = this.mob.getLightLevelDependentMagicValue();
/* 231 */       if (br >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
/* 232 */         this.mob.setTarget(null);
/* 233 */         return false;
/*     */       } 
/* 235 */       return super.canContinueToUse();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*     */     public SpiderTargetGoal(Spider mob, Class<T> targetType) {
/* 241 */       super((Mob)mob, targetType, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 246 */       float br = this.mob.getLightLevelDependentMagicValue();
/* 247 */       if (br >= 0.5F) {
/* 248 */         return false;
/*     */       }
/*     */       
/* 251 */       return super.canUse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/spider/Spider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */