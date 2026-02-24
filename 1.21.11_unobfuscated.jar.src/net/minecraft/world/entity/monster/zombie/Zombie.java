/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SpearUseGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Zombie extends Monster {
/*  71 */   private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
/*  72 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_ID, 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*  73 */   private static final Identifier REINFORCEMENT_CALLER_CHARGE_ID = Identifier.withDefaultNamespace("reinforcement_caller_charge");
/*  74 */   private static final AttributeModifier ZOMBIE_REINFORCEMENT_CALLEE_CHARGE = new AttributeModifier(Identifier.withDefaultNamespace("reinforcement_callee_charge"), -0.05000000074505806D, AttributeModifier.Operation.ADD_VALUE);
/*  75 */   private static final Identifier LEADER_ZOMBIE_BONUS_ID = Identifier.withDefaultNamespace("leader_zombie_bonus");
/*  76 */   private static final Identifier ZOMBIE_RANDOM_SPAWN_BONUS_ID = Identifier.withDefaultNamespace("zombie_random_spawn_bonus");
/*     */   
/*  78 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
/*  79 */   private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.INT);
/*  80 */   private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
/*     */   public static final float ZOMBIE_LEADER_CHANCE = 0.05F;
/*     */   public static final int REINFORCEMENT_ATTEMPTS = 50;
/*     */   public static final int REINFORCEMENT_RANGE_MAX = 40;
/*     */   public static final int REINFORCEMENT_RANGE_MIN = 7;
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   private static final float BREAK_DOOR_CHANCE = 0.1F;
/*  87 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ZOMBIE.getDimensions().scale(0.5F).withEyeHeight(0.93F);
/*     */   
/*     */   static {
/*  90 */     DOOR_BREAKING_PREDICATE = (d -> (d == Difficulty.HARD));
/*     */   }
/*     */   private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
/*     */   private static final boolean DEFAULT_BABY = false;
/*  94 */   private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal((Mob)this, DOOR_BREAKING_PREDICATE); private static final boolean DEFAULT_CAN_BREAK_DOORS = false;
/*     */   private static final int DEFAULT_IN_WATER_TIME = 0;
/*     */   private boolean canBreakDoors = false;
/*  97 */   private int inWaterTime = 0;
/*     */   private int conversionTime;
/*     */   
/*     */   public Zombie(EntityType<? extends Zombie> type, Level level) {
/* 101 */     super(type, level);
/*     */   }
/*     */   
/*     */   public Zombie(Level level) {
/* 105 */     this(EntityType.ZOMBIE, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 110 */     this.goalSelector.addGoal(4, (Goal)new ZombieAttackTurtleEggGoal((PathfinderMob)this, 1.0D, 3));
/* 111 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 112 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/* 114 */     addBehaviourGoals();
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/* 118 */     this.goalSelector.addGoal(2, (Goal)new SpearUseGoal(this, 1.0D, 1.0D, 10.0F, 2.0F));
/* 119 */     this.goalSelector.addGoal(3, (Goal)new ZombieAttackGoal(this, 1.0D, false));
/* 120 */     this.goalSelector.addGoal(6, (Goal)new MoveThroughVillageGoal((PathfinderMob)this, 1.0D, true, 4, this::canBreakDoors));
/* 121 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*     */     
/* 123 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class<?>[0]).setAlertOthers(new Class<?>[] { ZombifiedPiglin.class }));
/* 124 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/* 125 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/* 126 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/* 127 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 131 */     return Monster.createMonsterAttributes()
/* 132 */       .add(Attributes.FOLLOW_RANGE, 35.0D)
/* 133 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/* 134 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 135 */       .add(Attributes.ARMOR, 2.0D)
/* 136 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 141 */     super.defineSynchedData(entityData);
/*     */     
/* 143 */     entityData.define(DATA_BABY_ID, false);
/* 144 */     entityData.define(DATA_SPECIAL_TYPE_ID, 0);
/* 145 */     entityData.define(DATA_DROWNED_CONVERSION_ID, false);
/*     */   }
/*     */   
/*     */   public boolean isUnderWaterConverting() {
/* 149 */     return (Boolean)getEntityData().get(DATA_DROWNED_CONVERSION_ID);
/*     */   }
/*     */   
/*     */   public boolean canBreakDoors() {
/* 153 */     return this.canBreakDoors;
/*     */   }
/*     */   
/*     */   public void setCanBreakDoors(boolean canBreakDoors) {
/* 157 */     if (this.navigation.canNavigateGround()) {
/* 158 */       if (this.canBreakDoors != canBreakDoors) {
/* 159 */         this.canBreakDoors = canBreakDoors;
/* 160 */         this.navigation.setCanOpenDoors(canBreakDoors);
/*     */         
/* 162 */         if (canBreakDoors) {
/* 163 */           this.goalSelector.addGoal(1, (Goal)this.breakDoorGoal);
/*     */         } else {
/* 165 */           this.goalSelector.removeGoal((Goal)this.breakDoorGoal);
/*     */         }
/*     */       
/*     */       } 
/* 169 */     } else if (this.canBreakDoors) {
/* 170 */       this.goalSelector.removeGoal((Goal)this.breakDoorGoal);
/* 171 */       this.canBreakDoors = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 178 */     return (Boolean)getEntityData().get(DATA_BABY_ID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel level) {
/* 183 */     if (isBaby()) {
/* 184 */       this.xpReward = (int)(this.xpReward * 2.5D);
/*     */     }
/*     */     
/* 187 */     return super.getBaseExperienceReward(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 192 */     getEntityData().set(DATA_BABY_ID, baby);
/*     */     
/* 194 */     if (level() != null && !level().isClientSide()) {
/* 195 */       AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 196 */       speed.removeModifier(SPEED_MODIFIER_BABY_ID);
/* 197 */       if (baby) {
/* 198 */         speed.addTransientModifier(SPEED_MODIFIER_BABY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 205 */     if (DATA_BABY_ID.equals(accessor)) {
/* 206 */       refreshDimensions();
/*     */     }
/*     */     
/* 209 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 218 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (isAlive() && !isNoAi()) {
/* 219 */         if (isUnderWaterConverting()) {
/* 220 */           this.conversionTime--;
/*     */           
/* 222 */           if (this.conversionTime < 0) {
/* 223 */             doUnderWaterConversion(serverLevel);
/*     */           }
/* 225 */         } else if (convertsInWater()) {
/* 226 */           if (isEyeInFluid(FluidTags.WATER)) {
/* 227 */             this.inWaterTime++;
/*     */             
/* 229 */             if (this.inWaterTime >= 600) {
/* 230 */               startUnderWaterConversion(300);
/*     */             }
/*     */           } else {
/* 233 */             this.inWaterTime = -1;
/*     */           } 
/*     */         } 
/*     */       } }
/*     */     
/* 238 */     super.tick();
/*     */   }
/*     */   
/*     */   private void startUnderWaterConversion(int time) {
/* 242 */     this.conversionTime = time;
/* 243 */     getEntityData().set(DATA_DROWNED_CONVERSION_ID, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doUnderWaterConversion(ServerLevel level) {
/* 248 */     convertToZombieType(level, EntityType.DROWNED);
/* 249 */     if (!isSilent()) {
/* 250 */       level.levelEvent(null, 1040, blockPosition(), 0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void convertToZombieType(ServerLevel level, EntityType<? extends Zombie> zombieType) {
/* 255 */     convertTo(zombieType, ConversionParams.single((Mob)this, true, true), newZombie -> newZombie.handleAttributes(level.getCurrentDifficultyAt(newZombie.blockPosition()).getSpecialMultiplier()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public boolean convertVillagerToZombieVillager(ServerLevel level, Villager villager) {
/* 262 */     ZombieVillager zombieVillager = (ZombieVillager)villager.convertTo(EntityType.ZOMBIE_VILLAGER, ConversionParams.single((Mob)villager, true, true), zombie -> {
/*     */           level.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(level.blockPosition()), EntitySpawnReason.CONVERSION, new ZombieGroupData(false, true));
/*     */           
/*     */           level.setVillagerData(level.getVillagerData());
/*     */           
/*     */           level.setGossips(level.getGossips().copy());
/*     */           level.setTradeOffers(level.getOffers().copy());
/*     */           level.setVillagerXp(level.getVillagerXp());
/*     */           if (!isSilent()) {
/*     */             level.levelEvent(null, 1026, blockPosition(), 0);
/*     */           }
/*     */         });
/* 274 */     return (zombieVillager != null);
/*     */   }
/*     */   
/*     */   protected boolean isSunSensitive() {
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 283 */     if (!super.hurtServer(level, source, damage)) {
/* 284 */       return false;
/*     */     }
/*     */     
/* 287 */     LivingEntity target = getTarget();
/* 288 */     if (target == null && source.getEntity() instanceof LivingEntity) {
/* 289 */       target = (LivingEntity)source.getEntity();
/*     */     }
/*     */     
/* 292 */     if (target != null && level.getDifficulty() == Difficulty.HARD && this.random.nextFloat() < getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && level.isSpawningMonsters()) {
/* 293 */       int x = Mth.floor(getX());
/* 294 */       int y = Mth.floor(getY());
/* 295 */       int z = Mth.floor(getZ());
/* 296 */       EntityType<? extends Zombie> type = getType();
/* 297 */       Zombie reinforcement = (Zombie)type.create((Level)level, EntitySpawnReason.REINFORCEMENT);
/* 298 */       if (reinforcement == null) {
/* 299 */         return true;
/*     */       }
/* 301 */       for (int i = 0; i < 50; i++) {
/* 302 */         int xt = x + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 303 */         int yt = y + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 304 */         int zt = z + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/*     */         
/* 306 */         BlockPos spawnPos = new BlockPos(xt, yt, zt);
/*     */         
/* 308 */         if (SpawnPlacements.isSpawnPositionOk(type, (LevelReader)level, spawnPos) && 
/* 309 */           SpawnPlacements.checkSpawnRules(type, (ServerLevelAccessor)level, EntitySpawnReason.REINFORCEMENT, spawnPos, level.random)) {
/*     */           
/* 311 */           reinforcement.setPos(xt, yt, zt);
/*     */ 
/*     */           
/* 314 */           if (!level.hasNearbyAlivePlayer(xt, yt, zt, 7.0D) && level.isUnobstructed((Entity)reinforcement) && level.noCollision((Entity)reinforcement) && (reinforcement.canSpawnInLiquids() || !level.containsAnyLiquid(reinforcement.getBoundingBox()))) {
/* 315 */             reinforcement.setTarget(target);
/* 316 */             reinforcement.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(reinforcement.blockPosition()), EntitySpawnReason.REINFORCEMENT, null);
/* 317 */             level.addFreshEntityWithPassengers((Entity)reinforcement);
/*     */             
/* 319 */             AttributeInstance attribute = getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
/* 320 */             AttributeModifier modifier = attribute.getModifier(REINFORCEMENT_CALLER_CHARGE_ID);
/* 321 */             double existingAmount = (modifier != null) ? modifier.amount() : 0.0D;
/* 322 */             attribute.removeModifier(REINFORCEMENT_CALLER_CHARGE_ID);
/* 323 */             attribute.addPermanentModifier(new AttributeModifier(REINFORCEMENT_CALLER_CHARGE_ID, existingAmount - 0.05D, AttributeModifier.Operation.ADD_VALUE));
/* 324 */             reinforcement.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(ZOMBIE_REINFORCEMENT_CALLEE_CHARGE);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 331 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 336 */     boolean result = super.doHurtTarget(level, target);
/*     */     
/* 338 */     if (result) {
/* 339 */       float difficulty = level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/*     */ 
/*     */       
/* 342 */       if (getMainHandItem().isEmpty() && 
/* 343 */         isOnFire() && this.random.nextFloat() < difficulty * 0.3F) {
/* 344 */         target.igniteForSeconds((2 * (int)difficulty));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 349 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 354 */     return SoundEvents.ZOMBIE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 359 */     return SoundEvents.ZOMBIE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 364 */     return SoundEvents.ZOMBIE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 368 */     return SoundEvents.ZOMBIE_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 373 */     playSound(getStepSound(), 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityType<? extends Zombie> getType() {
/* 378 */     return super.getType();
/*     */   }
/*     */   
/*     */   protected boolean canSpawnInLiquids() {
/* 382 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 387 */     super.populateDefaultEquipmentSlots(random, difficulty);
/*     */     
/* 389 */     if (random.nextFloat() < ((level().getDifficulty() == Difficulty.HARD) ? 0.05F : 0.01F)) {
/* 390 */       int rand = random.nextInt(6);
/* 391 */       if (rand == 0) {
/* 392 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SWORD));
/* 393 */       } else if (rand == 1) {
/* 394 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SPEAR));
/*     */       } else {
/* 396 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SHOVEL));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 403 */     super.addAdditionalSaveData(output);
/*     */     
/* 405 */     output.putBoolean("IsBaby", isBaby());
/* 406 */     output.putBoolean("CanBreakDoors", canBreakDoors());
/*     */     
/* 408 */     output.putInt("InWaterTime", isInWater() ? this.inWaterTime : -1);
/* 409 */     output.putInt("DrownedConversionTime", isUnderWaterConverting() ? this.conversionTime : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 414 */     super.readAdditionalSaveData(input);
/*     */     
/* 416 */     setBaby(input.getBooleanOr("IsBaby", false));
/* 417 */     setCanBreakDoors(input.getBooleanOr("CanBreakDoors", false));
/*     */     
/* 419 */     this.inWaterTime = input.getIntOr("InWaterTime", 0);
/*     */     
/* 421 */     int conversionTime = input.getIntOr("DrownedConversionTime", -1);
/* 422 */     if (conversionTime != -1) {
/* 423 */       startUnderWaterConversion(conversionTime);
/*     */     } else {
/* 425 */       getEntityData().set(DATA_DROWNED_CONVERSION_ID, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
/* 431 */     boolean perished = super.killedEntity(level, entity, source);
/*     */     
/* 433 */     if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && entity instanceof Villager) { Villager villager = (Villager)entity;
/* 434 */       if (level.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
/* 435 */         return perished;
/*     */       }
/*     */       
/* 438 */       if (convertVillagerToZombieVillager(level, villager)) {
/* 439 */         perished = false;
/*     */       } }
/*     */     
/* 442 */     return perished;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 447 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHoldItem(ItemStack itemStack) {
/* 452 */     if (itemStack.is(ItemTags.EGGS) && isBaby() && isPassenger()) {
/* 453 */       return false;
/*     */     }
/* 455 */     return super.canHoldItem(itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 460 */     if (itemStack.is(Items.GLOW_INK_SAC)) {
/* 461 */       return false;
/*     */     }
/* 463 */     return super.wantsToPickUp(level, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 468 */     RandomSource random = level.getRandom();
/*     */     
/* 470 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 471 */     float difficultyModifier = difficulty.getSpecialMultiplier();
/*     */     
/* 473 */     if (spawnReason != EntitySpawnReason.CONVERSION) {
/* 474 */       setCanPickUpLoot((random.nextFloat() < 0.55F * difficultyModifier));
/*     */     }
/*     */     
/* 477 */     if (groupData == null) {
/* 478 */       groupData = new ZombieGroupData(getSpawnAsBabyOdds(random), true);
/*     */     }
/*     */     
/* 481 */     if (groupData instanceof ZombieGroupData) { ZombieGroupData zombieData = (ZombieGroupData)groupData;
/*     */       
/* 483 */       if (zombieData.isBaby) {
/* 484 */         setBaby(true);
/*     */         
/* 486 */         if (zombieData.canSpawnJockey) {
/* 487 */           if (random.nextFloat() < 0.05D) {
/* 488 */             List<Chicken> chickens = level.getEntitiesOfClass(Chicken.class, getBoundingBox().inflate(5.0D, 3.0D, 5.0D), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
/*     */             
/* 490 */             if (!chickens.isEmpty()) {
/* 491 */               Chicken chicken = chickens.get(0);
/* 492 */               chicken.setChickenJockey(true);
/* 493 */               startRiding((Entity)chicken, false, false);
/*     */             } 
/* 495 */           } else if (random.nextFloat() < 0.05D) {
/* 496 */             Chicken chicken = (Chicken)EntityType.CHICKEN.create(level(), EntitySpawnReason.JOCKEY);
/* 497 */             if (chicken != null) {
/* 498 */               chicken.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 499 */               chicken.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, null);
/* 500 */               chicken.setChickenJockey(true);
/* 501 */               startRiding((Entity)chicken, false, false);
/*     */ 
/*     */ 
/*     */               
/* 505 */               level.addFreshEntity((Entity)chicken);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 511 */       setCanBreakDoors((random.nextFloat() < difficultyModifier * 0.1F));
/*     */       
/* 513 */       if (spawnReason != EntitySpawnReason.CONVERSION) {
/* 514 */         populateDefaultEquipmentSlots(random, difficulty);
/* 515 */         populateDefaultEquipmentEnchantments(level, random, difficulty);
/*     */       }  }
/*     */ 
/*     */     
/* 519 */     if (getItemBySlot(EquipmentSlot.HEAD).isEmpty() && 
/* 520 */       SpecialDates.isHalloween() && random.nextFloat() < 0.25F) {
/*     */       
/* 522 */       setItemSlot(EquipmentSlot.HEAD, new ItemStack((random.nextFloat() < 0.1F) ? (ItemLike)Blocks.JACK_O_LANTERN : (ItemLike)Blocks.CARVED_PUMPKIN));
/* 523 */       setDropChance(EquipmentSlot.HEAD, 0.0F);
/*     */     } 
/*     */ 
/*     */     
/* 527 */     handleAttributes(difficultyModifier);
/*     */     
/* 529 */     return groupData;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setInWaterTime(int inWaterTime) {
/* 534 */     this.inWaterTime = inWaterTime;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setConversionTime(int conversionTime) {
/* 539 */     this.conversionTime = conversionTime;
/*     */   }
/*     */   
/*     */   public static boolean getSpawnAsBabyOdds(RandomSource random) {
/* 543 */     return (random.nextFloat() < 0.05F);
/*     */   }
/*     */   
/*     */   protected void handleAttributes(float difficultyModifier) {
/* 547 */     randomizeReinforcementsChance();
/* 548 */     getAttribute(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(new AttributeModifier(RANDOM_SPAWN_BONUS_ID, this.random.nextDouble() * 0.05000000074505806D, AttributeModifier.Operation.ADD_VALUE));
/* 549 */     double followRangeModifier = this.random.nextDouble() * 1.5D * difficultyModifier;
/* 550 */     if (followRangeModifier > 1.0D) {
/* 551 */       getAttribute(Attributes.FOLLOW_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ZOMBIE_RANDOM_SPAWN_BONUS_ID, followRangeModifier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
/*     */     }
/*     */     
/* 554 */     if (this.random.nextFloat() < difficultyModifier * 0.05F) {
/* 555 */       getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, this.random.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADD_VALUE));
/* 556 */       getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, this.random.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
/* 557 */       setCanBreakDoors(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void randomizeReinforcementsChance() {
/* 562 */     getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.random.nextDouble() * 0.10000000149011612D);
/*     */   }
/*     */   
/*     */   public static class ZombieGroupData implements SpawnGroupData {
/*     */     public final boolean isBaby;
/*     */     public final boolean canSpawnJockey;
/*     */     
/*     */     public ZombieGroupData(boolean baby, boolean canSpawnJockey) {
/* 570 */       this.isBaby = baby;
/* 571 */       this.canSpawnJockey = canSpawnJockey;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
/*     */     ZombieAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
/* 577 */       super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
/*     */     }
/*     */ 
/*     */     
/*     */     public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
/* 582 */       level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + Zombie.this.random.nextFloat() * 0.2F);
/*     */     }
/*     */ 
/*     */     
/*     */     public void playBreakSound(Level level, BlockPos pos) {
/* 587 */       level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
/*     */     }
/*     */ 
/*     */     
/*     */     public double acceptedDistance() {
/* 592 */       return 1.14D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/zombie/Zombie.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */