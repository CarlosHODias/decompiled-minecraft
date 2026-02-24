/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Skeleton
/*     */   extends AbstractSkeleton
/*     */ {
/*     */   private static final int TOTAL_CONVERSION_TIME = 300;
/*  21 */   private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(Skeleton.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final String CONVERSION_TAG = "StrayConversionTime";
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   private int inPowderSnowTime;
/*     */   private int conversionTime;
/*     */   
/*     */   public Skeleton(EntityType<? extends Skeleton> type, Level level) {
/*  29 */     super((EntityType)type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  34 */     super.defineSynchedData(entityData);
/*     */     
/*  36 */     entityData.define(DATA_STRAY_CONVERSION_ID, false);
/*     */   }
/*     */   
/*     */   public boolean isFreezeConverting() {
/*  40 */     return (Boolean)getEntityData().get(DATA_STRAY_CONVERSION_ID);
/*     */   }
/*     */   
/*     */   public void setFreezeConverting(boolean isConverting) {
/*  44 */     this.entityData.set(DATA_STRAY_CONVERSION_ID, isConverting);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShaking() {
/*  49 */     return isFreezeConverting();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  54 */     if (!level().isClientSide() && isAlive() && !isNoAi()) {
/*  55 */       if (this.isInPowderSnow) {
/*  56 */         if (isFreezeConverting()) {
/*  57 */           this.conversionTime--;
/*     */           
/*  59 */           if (this.conversionTime < 0) {
/*  60 */             doFreezeConversion();
/*     */           }
/*     */         } else {
/*  63 */           this.inPowderSnowTime++;
/*     */           
/*  65 */           if (this.inPowderSnowTime >= 140) {
/*  66 */             startFreezeConversion(300);
/*     */           }
/*     */         } 
/*     */       } else {
/*  70 */         this.inPowderSnowTime = -1;
/*  71 */         setFreezeConverting(false);
/*     */       } 
/*     */     }
/*     */     
/*  75 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  80 */     super.addAdditionalSaveData(output);
/*     */     
/*  82 */     output.putInt("StrayConversionTime", isFreezeConverting() ? this.conversionTime : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  87 */     super.readAdditionalSaveData(input);
/*     */     
/*  89 */     int conversionTime = input.getIntOr("StrayConversionTime", -1);
/*  90 */     if (conversionTime != -1) {
/*  91 */       startFreezeConversion(conversionTime);
/*     */     } else {
/*  93 */       setFreezeConverting(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void startFreezeConversion(int time) {
/*  99 */     this.conversionTime = time;
/* 100 */     setFreezeConverting(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doFreezeConversion() {
/* 105 */     convertTo(EntityType.STRAY, ConversionParams.single((Mob)this, true, true), stray -> {
/*     */           if (!isSilent()) {
/*     */             level().levelEvent(null, 1048, blockPosition(), 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canFreeze() {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 120 */     return SoundEvents.SKELETON_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 125 */     return SoundEvents.SKELETON_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 130 */     return SoundEvents.SKELETON_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   SoundEvent getStepSound() {
/* 135 */     return SoundEvents.SKELETON_STEP;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/skeleton/Skeleton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */