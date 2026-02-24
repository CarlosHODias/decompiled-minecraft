/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ public class PiglinBrute
/*     */   extends AbstractPiglin
/*     */ {
/*     */   private static final int MAX_HEALTH = 50;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
/*     */   private static final int ATTACK_DAMAGE = 7;
/*     */   private static final double TARGETING_RANGE = 12.0D;
/*  43 */   protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinBrute>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, (Object[])new MemoryModuleType[] { MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PiglinBrute(EntityType<? extends PiglinBrute> type, Level level) {
/*  74 */     super((EntityType)type, level);
/*  75 */     this.xpReward = 20;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  79 */     return Monster.createMonsterAttributes()
/*  80 */       .add(Attributes.MAX_HEALTH, 50.0D)
/*  81 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  82 */       .add(Attributes.ATTACK_DAMAGE, 7.0D)
/*  83 */       .add(Attributes.FOLLOW_RANGE, 12.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  88 */     PiglinBruteAi.initMemories(this);
/*  89 */     populateDefaultEquipmentSlots(level.getRandom(), difficulty);
/*  90 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/*  95 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.GOLDEN_AXE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<PiglinBrute> brainProvider() {
/* 100 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/* 105 */     return PiglinBruteAi.makeBrain(this, brainProvider().makeBrain(input));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<PiglinBrute> getBrain() {
/* 111 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHunt() {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 121 */     if (itemStack.is(Items.GOLDEN_AXE)) {
/* 122 */       return super.wantsToPickUp(level, itemStack);
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 129 */     ProfilerFiller profiler = Profiler.get();
/* 130 */     profiler.push("piglinBruteBrain");
/* 131 */     getBrain().tick(level, (LivingEntity)this);
/* 132 */     profiler.pop();
/*     */     
/* 134 */     PiglinBruteAi.updateActivity(this);
/* 135 */     PiglinBruteAi.maybePlayActivitySound(this);
/*     */     
/* 137 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 142 */     if (isAggressive() && isHoldingMeleeWeapon()) {
/* 143 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
/*     */     }
/* 145 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 151 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 152 */     if (wasHurt) { Entity entity = source.getEntity(); if (entity instanceof LivingEntity) { LivingEntity sourceEntity = (LivingEntity)entity;
/* 153 */         PiglinBruteAi.wasHurtBy(level, this, sourceEntity); }
/*     */        }
/* 155 */      return wasHurt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     return SoundEvents.PIGLIN_BRUTE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 165 */     return SoundEvents.PIGLIN_BRUTE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 170 */     return SoundEvents.PIGLIN_BRUTE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 175 */     playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 179 */     makeSound(SoundEvents.PIGLIN_BRUTE_ANGRY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playConvertedSound() {
/* 184 */     makeSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/piglin/PiglinBrute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */