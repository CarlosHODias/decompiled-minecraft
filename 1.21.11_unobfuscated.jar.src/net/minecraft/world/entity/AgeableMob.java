/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public abstract class AgeableMob
/*     */   extends PathfinderMob {
/*  19 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final int BABY_START_AGE = -24000;
/*     */   
/*     */   private static final int FORCED_AGE_PARTICLE_TICKS = 40;
/*     */   
/*     */   protected static final int DEFAULT_AGE = 0;
/*     */   protected static final int DEFAULT_FORCED_AGE = 0;
/*  27 */   protected int age = 0;
/*  28 */   protected int forcedAge = 0;
/*     */   protected int forcedAgeTimer;
/*     */   
/*     */   protected AgeableMob(EntityType<? extends AgeableMob> type, Level level) {
/*  32 */     super((EntityType)type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  37 */     if (groupData == null) {
/*  38 */       groupData = new AgeableMobGroupData(true);
/*     */     }
/*     */     
/*  41 */     AgeableMobGroupData ageableMobGroupData = (AgeableMobGroupData)groupData;
/*     */     
/*  43 */     if (ageableMobGroupData.isShouldSpawnBaby() && ageableMobGroupData.getGroupSize() > 0 && level.getRandom().nextFloat() <= ageableMobGroupData.getBabySpawnChance()) {
/*  44 */       setAge(-24000);
/*     */     }
/*     */     
/*  47 */     ageableMobGroupData.increaseGroupSizeByOne();
/*     */     
/*  49 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob);
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  56 */     super.defineSynchedData(entityData);
/*  57 */     entityData.define(DATA_BABY_ID, false);
/*     */   }
/*     */   
/*     */   public boolean canBreed() {
/*  61 */     return false;
/*     */   }
/*     */   
/*     */   public int getAge() {
/*  65 */     if (level().isClientSide()) {
/*  66 */       return (Boolean)this.entityData.get(DATA_BABY_ID) ? -1 : 1;
/*     */     }
/*  68 */     return this.age;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ageUp(int seconds, boolean forced) {
/*  73 */     int age = getAge();
/*  74 */     int oldAge = age;
/*  75 */     age += seconds * 20;
/*  76 */     if (age > 0) {
/*  77 */       age = 0;
/*     */     }
/*  79 */     int delta = age - oldAge;
/*  80 */     setAge(age);
/*  81 */     if (forced) {
/*  82 */       this.forcedAge += delta;
/*  83 */       if (this.forcedAgeTimer == 0) {
/*  84 */         this.forcedAgeTimer = 40;
/*     */       }
/*     */     } 
/*  87 */     if (getAge() == 0) {
/*  88 */       setAge(this.forcedAge);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ageUp(int seconds) {
/*  93 */     ageUp(seconds, false);
/*     */   }
/*     */   
/*     */   public void setAge(int newAge) {
/*  97 */     int oldAge = getAge();
/*  98 */     this.age = newAge;
/*     */     
/* 100 */     if ((oldAge < 0 && newAge >= 0) || (oldAge >= 0 && newAge < 0)) {
/* 101 */       this.entityData.set(DATA_BABY_ID, (newAge < 0));
/* 102 */       ageBoundaryReached();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 108 */     super.addAdditionalSaveData(output);
/* 109 */     output.putInt("Age", getAge());
/* 110 */     output.putInt("ForcedAge", this.forcedAge);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 115 */     super.readAdditionalSaveData(input);
/* 116 */     setAge(input.getIntOr("Age", 0));
/* 117 */     this.forcedAge = input.getIntOr("ForcedAge", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 122 */     if (DATA_BABY_ID.equals(accessor)) {
/* 123 */       refreshDimensions();
/*     */     }
/* 125 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 130 */     super.aiStep();
/*     */     
/* 132 */     if (level().isClientSide()) {
/* 133 */       if (this.forcedAgeTimer > 0) {
/* 134 */         if (this.forcedAgeTimer % 4 == 0) {
/* 135 */           level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*     */         }
/* 137 */         this.forcedAgeTimer--;
/*     */       } 
/* 139 */     } else if (isAlive()) {
/* 140 */       int age = getAge();
/* 141 */       if (age < 0) {
/* 142 */         age++;
/* 143 */         setAge(age);
/* 144 */       } else if (age > 0) {
/* 145 */         age--;
/* 146 */         setAge(age);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 152 */     if (!isBaby() && 
/* 153 */       isPassenger()) {
/* 154 */       Entity entity = getVehicle(); if (entity instanceof AbstractBoat) { AbstractBoat boat = (AbstractBoat)entity;
/* 155 */         if (!boat.hasEnoughSpaceFor(this))
/* 156 */           stopRiding();  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBaby() {
/* 162 */     return (getAge() < 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 167 */     setAge(baby ? -24000 : 0);
/*     */   }
/*     */   
/*     */   public static int getSpeedUpSecondsWhenFeeding(int ticksUntilAdult) {
/* 171 */     return (int)((ticksUntilAdult / 20) * 0.1F);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getForcedAge() {
/* 176 */     return this.forcedAge;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getForcedAgeTimer() {
/* 181 */     return this.forcedAgeTimer;
/*     */   }
/*     */   
/*     */   public static class AgeableMobGroupData implements SpawnGroupData {
/*     */     private int groupSize;
/*     */     private final boolean shouldSpawnBaby;
/*     */     private final float babySpawnChance;
/*     */     
/*     */     public AgeableMobGroupData(boolean shouldSpawnBaby, float babySpawnChance) {
/* 190 */       this.shouldSpawnBaby = shouldSpawnBaby;
/* 191 */       this.babySpawnChance = babySpawnChance;
/*     */     }
/*     */     
/*     */     public AgeableMobGroupData(boolean shouldSpawnBaby) {
/* 195 */       this(shouldSpawnBaby, 0.05F);
/*     */     }
/*     */     
/*     */     public AgeableMobGroupData(float babySpawnChance) {
/* 199 */       this(true, babySpawnChance);
/*     */     }
/*     */     
/*     */     public int getGroupSize() {
/* 203 */       return this.groupSize;
/*     */     }
/*     */     
/*     */     public void increaseGroupSizeByOne() {
/* 207 */       this.groupSize++;
/*     */     }
/*     */     
/*     */     public boolean isShouldSpawnBaby() {
/* 211 */       return this.shouldSpawnBaby;
/*     */     }
/*     */     
/*     */     public float getBabySpawnChance() {
/* 215 */       return this.babySpawnChance;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/AgeableMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */