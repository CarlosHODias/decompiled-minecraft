/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Bucketable;
/*     */ import net.minecraft.world.entity.animal.fish.AbstractFish;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Tadpole extends AbstractFish {
/*     */   @com.google.common.annotations.VisibleForTesting
/*  49 */   public static int ticksToBeFrog = Math.abs(-24000); private static final int DEFAULT_AGE = 0;
/*     */   public static final float HITBOX_WIDTH = 0.4F;
/*     */   public static final float HITBOX_HEIGHT = 0.3F;
/*  52 */   private int age = 0;
/*     */   
/*  54 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Tadpole>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.FROG_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tadpole(EntityType<? extends AbstractFish> type, Level level) {
/*  76 */     super(type, level);
/*     */     
/*  78 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.02F, 0.1F, true);
/*  79 */     this.lookControl = (LookControl)new SmoothSwimmingLookControl((Mob)this, 10);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  84 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Tadpole> brainProvider() {
/*  89 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/*  94 */     return TadpoleAi.makeBrain(brainProvider().makeBrain(input));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Tadpole> getBrain() {
/* 100 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 105 */     return SoundEvents.TADPOLE_FLOP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 110 */     ProfilerFiller profiler = Profiler.get();
/* 111 */     profiler.push("tadpoleBrain");
/* 112 */     getBrain().tick(level, (LivingEntity)this);
/* 113 */     profiler.pop();
/*     */     
/* 115 */     profiler.push("tadpoleActivityUpdate");
/* 116 */     TadpoleAi.updateActivity(this);
/* 117 */     profiler.pop();
/*     */     
/* 119 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 123 */     return net.minecraft.world.entity.animal.Animal.createAnimalAttributes()
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 125 */       .add(Attributes.MAX_HEALTH, 6.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 130 */     super.aiStep();
/*     */     
/* 132 */     if (!level().isClientSide()) {
/* 133 */       setAge(this.age + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 139 */     super.addAdditionalSaveData(output);
/* 140 */     output.putInt("Age", this.age);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 145 */     super.readAdditionalSaveData(input);
/* 146 */     setAge(input.getIntOr("Age", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 156 */     return SoundEvents.TADPOLE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 161 */     return SoundEvents.TADPOLE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 166 */     ItemStack itemStack = player.getItemInHand(hand);
/* 167 */     if (isFood(itemStack)) {
/* 168 */       feed(player, itemStack);
/* 169 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 171 */     return Bucketable.bucketMobPickup(player, hand, (LivingEntity)this).orElse(super.mobInteract(player, hand));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fromBucket() {
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromBucket(boolean fromBucket) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 187 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, bucket);
/*     */     
/* 189 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> tag.putInt("Age", getAge()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag tag) {
/* 194 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, tag);
/* 195 */     tag.getInt("Age").ifPresent(this::setAge);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/* 200 */     return new ItemStack((net.minecraft.world.level.ItemLike)Items.TADPOLE_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getPickupSound() {
/* 205 */     return SoundEvents.BUCKET_FILL_TADPOLE;
/*     */   }
/*     */   
/*     */   private boolean isFood(ItemStack itemStack) {
/* 209 */     return itemStack.is(ItemTags.FROG_FOOD);
/*     */   }
/*     */   
/*     */   private void feed(Player player, ItemStack itemStack) {
/* 213 */     usePlayerItem(player, itemStack);
/* 214 */     ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(getTicksLeftUntilAdult()));
/* 215 */     level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private void usePlayerItem(Player player, ItemStack itemStack) {
/* 219 */     itemStack.consume(1, (LivingEntity)player);
/*     */   }
/*     */   
/*     */   private int getAge() {
/* 223 */     return this.age;
/*     */   }
/*     */   
/*     */   private void ageUp(int ticksToAgeUp) {
/* 227 */     setAge(this.age + ticksToAgeUp * 20);
/*     */   }
/*     */   
/*     */   private void setAge(int newAge) {
/* 231 */     this.age = newAge;
/*     */     
/* 233 */     if (this.age >= ticksToBeFrog) {
/* 234 */       ageUp();
/*     */     }
/*     */   }
/*     */   
/*     */   private void ageUp() {
/* 239 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 240 */       convertTo(EntityType.FROG, ConversionParams.single((Mob)this, false, false), frog -> {
/*     */             serverLevel.finalizeSpawn((ServerLevelAccessor)serverLevel, serverLevel.getCurrentDifficultyAt(serverLevel.blockPosition()), EntitySpawnReason.CONVERSION, null);
/*     */             serverLevel.setPersistenceRequired();
/*     */             serverLevel.fudgePositionAfterSizeChange(getDimensions(getPose()));
/*     */             playSound(SoundEvents.TADPOLE_GROW_UP, 0.15F, 1.0F);
/*     */           }); }
/*     */   
/*     */   }
/*     */   
/*     */   private int getTicksLeftUntilAdult() {
/* 250 */     return Math.max(0, ticksToBeFrog - this.age);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDropExperience() {
/* 255 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/frog/Tadpole.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */