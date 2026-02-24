/*     */ package net.minecraft.world.entity.animal.goat;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.InstrumentTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Instrument;
/*     */ import net.minecraft.world.item.InstrumentItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Goat extends Animal {
/*  58 */   public static final EntityDimensions LONG_JUMPING_DIMENSIONS = EntityDimensions.scalable(0.9F, 1.3F).scale(0.7F);
/*     */   
/*     */   private static final int ADULT_ATTACK_DAMAGE = 2;
/*     */   
/*     */   private static final int BABY_ATTACK_DAMAGE = 1;
/*  63 */   protected static final ImmutableList<SensorType<? extends net.minecraft.world.entity.ai.sensing.Sensor<? super Goat>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_ADULT, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.BREED_TARGET, MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, (Object[])new MemoryModuleType[] { MemoryModuleType.IS_TEMPTED, MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleType.RAM_TARGET, MemoryModuleType.IS_PANICKING });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int GOAT_FALL_DAMAGE_REDUCTION = 10;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final double GOAT_SCREAMING_CHANCE = 0.02D;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final double UNIHORN_CHANCE = 0.10000000149011612D;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
/*  95 */   private static final EntityDataAccessor<Boolean> DATA_HAS_LEFT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
/*  96 */   private static final EntityDataAccessor<Boolean> DATA_HAS_RIGHT_HORN = SynchedEntityData.defineId(Goat.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_IS_SCREAMING = false;
/*     */   
/*     */   private static final boolean DEFAULT_HAS_LEFT_HORN = true;
/*     */   private static final boolean DEFAULT_HAS_RIGHT_HORN = true;
/*     */   private boolean isLoweringHead;
/*     */   private int lowerHeadTick;
/*     */   
/*     */   public Goat(EntityType<? extends Goat> type, Level level) {
/* 106 */     super(type, level);
/*     */     
/* 108 */     getNavigation().setCanFloat(true);
/* 109 */     setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
/* 110 */     setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
/*     */   }
/*     */   
/*     */   public ItemStack createHorn() {
/* 114 */     RandomSource random = RandomSource.create(getUUID().hashCode());
/* 115 */     TagKey<Instrument> key = isScreamingGoat() ? InstrumentTags.SCREAMING_GOAT_HORNS : InstrumentTags.REGULAR_GOAT_HORNS;
/* 116 */     return level().registryAccess().lookupOrThrow(Registries.INSTRUMENT)
/* 117 */       .getRandomElementOf(key, random)
/* 118 */       .map(instrument -> InstrumentItem.create(Items.GOAT_HORN, instrument))
/* 119 */       .orElseGet(() -> new ItemStack((ItemLike)Items.GOAT_HORN));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Goat> brainProvider() {
/* 124 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> input) {
/* 129 */     return GoatAi.makeBrain(brainProvider().makeBrain(input));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 133 */     return Animal.createAnimalAttributes()
/* 134 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 135 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 136 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 141 */     if (isBaby()) {
/* 142 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.0D);
/* 143 */       removeHorns();
/*     */     } else {
/* 145 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
/* 146 */       addHorns();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int calculateFallDamage(double fallDistance, float damageModifier) {
/* 152 */     return super.calculateFallDamage(fallDistance, damageModifier) - 10;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 157 */     if (isScreamingGoat()) {
/* 158 */       return SoundEvents.GOAT_SCREAMING_AMBIENT;
/*     */     }
/* 160 */     return SoundEvents.GOAT_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source) {
/* 166 */     if (isScreamingGoat()) {
/* 167 */       return SoundEvents.GOAT_SCREAMING_HURT;
/*     */     }
/* 169 */     return SoundEvents.GOAT_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 175 */     if (isScreamingGoat()) {
/* 176 */       return SoundEvents.GOAT_SCREAMING_DEATH;
/*     */     }
/* 178 */     return SoundEvents.GOAT_DEATH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState blockState) {
/* 184 */     playSound(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected SoundEvent getMilkingSound() {
/* 188 */     if (isScreamingGoat()) {
/* 189 */       return SoundEvents.GOAT_SCREAMING_MILK;
/*     */     }
/* 191 */     return SoundEvents.GOAT_MILK;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Goat getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*     */     // Byte code:
/*     */     //   0: getstatic net/minecraft/world/entity/EntityType.GOAT : Lnet/minecraft/world/entity/EntityType;
/*     */     //   3: aload_1
/*     */     //   4: getstatic net/minecraft/world/entity/EntitySpawnReason.BREEDING : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   7: invokevirtual create : (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;
/*     */     //   10: checkcast net/minecraft/world/entity/animal/goat/Goat
/*     */     //   13: astore_3
/*     */     //   14: aload_3
/*     */     //   15: ifnull -> 97
/*     */     //   18: aload_3
/*     */     //   19: aload_1
/*     */     //   20: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   23: invokestatic initMemories : (Lnet/minecraft/world/entity/animal/goat/Goat;Lnet/minecraft/util/RandomSource;)V
/*     */     //   26: aload_1
/*     */     //   27: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   30: invokeinterface nextBoolean : ()Z
/*     */     //   35: ifeq -> 42
/*     */     //   38: aload_0
/*     */     //   39: goto -> 43
/*     */     //   42: aload_2
/*     */     //   43: astore #4
/*     */     //   45: aload #4
/*     */     //   47: instanceof net/minecraft/world/entity/animal/goat/Goat
/*     */     //   50: ifeq -> 68
/*     */     //   53: aload #4
/*     */     //   55: checkcast net/minecraft/world/entity/animal/goat/Goat
/*     */     //   58: astore #6
/*     */     //   60: aload #6
/*     */     //   62: invokevirtual isScreamingGoat : ()Z
/*     */     //   65: ifne -> 84
/*     */     //   68: aload_1
/*     */     //   69: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   72: invokeinterface nextDouble : ()D
/*     */     //   77: ldc2_w 0.02
/*     */     //   80: dcmpg
/*     */     //   81: ifge -> 88
/*     */     //   84: iconst_1
/*     */     //   85: goto -> 89
/*     */     //   88: iconst_0
/*     */     //   89: istore #5
/*     */     //   91: aload_3
/*     */     //   92: iload #5
/*     */     //   94: invokevirtual setScreamingGoat : (Z)V
/*     */     //   97: aload_3
/*     */     //   98: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #197	-> 0
/*     */     //   #199	-> 14
/*     */     //   #200	-> 18
/*     */     //   #201	-> 26
/*     */     //   #202	-> 45
/*     */     //   #203	-> 91
/*     */     //   #206	-> 97
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   60	8	6	goat	Lnet/minecraft/world/entity/animal/goat/Goat;
/*     */     //   45	52	4	selectedParent	Lnet/minecraft/world/entity/AgeableMob;
/*     */     //   91	6	5	babyIsScreaming	Z
/*     */     //   0	99	0	this	Lnet/minecraft/world/entity/animal/goat/Goat;
/*     */     //   0	99	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	99	2	partner	Lnet/minecraft/world/entity/AgeableMob;
/*     */     //   14	85	3	newGoat	Lnet/minecraft/world/entity/animal/goat/Goat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Goat> getBrain() {
/* 211 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 216 */     ProfilerFiller profiler = Profiler.get();
/* 217 */     profiler.push("goatBrain");
/* 218 */     getBrain().tick(level, (LivingEntity)this);
/* 219 */     profiler.pop();
/*     */     
/* 221 */     profiler.push("goatActivityUpdate");
/* 222 */     GoatAi.updateActivity(this);
/* 223 */     profiler.pop();
/*     */     
/* 225 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 230 */     return 15;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYHeadRot(float yHeadRot) {
/* 235 */     int maxHeadYRot = getMaxHeadYRot();
/* 236 */     float deltaFromBody = Mth.degreesDifference(this.yBodyRot, yHeadRot);
/* 237 */     float deltaFromBodyClamped = Mth.clamp(deltaFromBody, -maxHeadYRot, maxHeadYRot);
/*     */     
/* 239 */     super.setYHeadRot(this.yBodyRot + deltaFromBodyClamped);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playEatingSound() {
/* 244 */     level().playSound(null, (Entity)this, isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_EAT : SoundEvents.GOAT_EAT, SoundSource.NEUTRAL, 1.0F, Mth.randomBetween((level()).random, 0.8F, 1.2F));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 249 */     return itemStack.is(ItemTags.GOAT_FOOD);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 254 */     ItemStack heldItem = player.getItemInHand(hand);
/* 255 */     if (heldItem.is(Items.BUCKET) && !isBaby()) {
/* 256 */       player.playSound(getMilkingSound(), 1.0F, 1.0F);
/* 257 */       ItemStack bucketOrMilkBucket = ItemUtils.createFilledResult(heldItem, player, Items.MILK_BUCKET.getDefaultInstance());
/* 258 */       player.setItemInHand(hand, bucketOrMilkBucket);
/* 259 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 262 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 263 */     if (interactionResult.consumesAction() && isFood(heldItem)) {
/* 264 */       playEatingSound();
/*     */     }
/*     */     
/* 267 */     return interactionResult;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 272 */     RandomSource random = level.getRandom();
/* 273 */     GoatAi.initMemories(this, random);
/*     */     
/* 275 */     setScreamingGoat((random.nextDouble() < 0.02D));
/* 276 */     ageBoundaryReached();
/* 277 */     if (!isBaby() && random.nextFloat() < 0.10000000149011612D) {
/* 278 */       EntityDataAccessor<Boolean> hornToRemove = random.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
/* 279 */       this.entityData.set(hornToRemove, false);
/*     */     } 
/*     */     
/* 282 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 287 */     return (pose == Pose.LONG_JUMPING) ? LONG_JUMPING_DIMENSIONS.scale(getAgeScale()) : super.getDefaultDimensions(pose);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 292 */     super.addAdditionalSaveData(output);
/*     */     
/* 294 */     output.putBoolean("IsScreamingGoat", isScreamingGoat());
/* 295 */     output.putBoolean("HasLeftHorn", hasLeftHorn());
/* 296 */     output.putBoolean("HasRightHorn", hasRightHorn());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 301 */     super.readAdditionalSaveData(input);
/*     */     
/* 303 */     setScreamingGoat(input.getBooleanOr("IsScreamingGoat", false));
/* 304 */     this.entityData.set(DATA_HAS_LEFT_HORN, input.getBooleanOr("HasLeftHorn", true));
/* 305 */     this.entityData.set(DATA_HAS_RIGHT_HORN, input.getBooleanOr("HasRightHorn", true));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 310 */     if (id == 58) {
/* 311 */       this.isLoweringHead = true;
/* 312 */     } else if (id == 59) {
/* 313 */       this.isLoweringHead = false;
/*     */     } else {
/* 315 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 321 */     if (this.isLoweringHead) {
/* 322 */       this.lowerHeadTick++;
/*     */     } else {
/* 324 */       this.lowerHeadTick -= 2;
/*     */     } 
/* 326 */     this.lowerHeadTick = Mth.clamp(this.lowerHeadTick, 0, 20);
/*     */     
/* 328 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 333 */     super.defineSynchedData(entityData);
/* 334 */     entityData.define(DATA_IS_SCREAMING_GOAT, false);
/* 335 */     entityData.define(DATA_HAS_LEFT_HORN, true);
/* 336 */     entityData.define(DATA_HAS_RIGHT_HORN, true);
/*     */   }
/*     */   
/*     */   public boolean hasLeftHorn() {
/* 340 */     return (Boolean)this.entityData.get(DATA_HAS_LEFT_HORN);
/*     */   }
/*     */   
/*     */   public boolean hasRightHorn() {
/* 344 */     return (Boolean)this.entityData.get(DATA_HAS_RIGHT_HORN);
/*     */   }
/*     */   public boolean dropHorn() {
/*     */     EntityDataAccessor<Boolean> hornToDrop;
/* 348 */     boolean hasLeft = hasLeftHorn();
/* 349 */     boolean hasRight = hasRightHorn();
/*     */     
/* 351 */     if (!hasLeft && !hasRight) {
/* 352 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (!hasLeft) {
/* 358 */       hornToDrop = DATA_HAS_RIGHT_HORN;
/* 359 */     } else if (!hasRight) {
/* 360 */       hornToDrop = DATA_HAS_LEFT_HORN;
/*     */     } else {
/* 362 */       hornToDrop = this.random.nextBoolean() ? DATA_HAS_LEFT_HORN : DATA_HAS_RIGHT_HORN;
/*     */     } 
/* 364 */     this.entityData.set(hornToDrop, false);
/*     */     
/* 366 */     Vec3 bodyPosition = position();
/* 367 */     ItemStack item = createHorn();
/* 368 */     double deltaX = Mth.randomBetween(this.random, -0.2F, 0.2F);
/* 369 */     double deltaY = Mth.randomBetween(this.random, 0.3F, 0.7F);
/* 370 */     double deltaZ = Mth.randomBetween(this.random, -0.2F, 0.2F);
/* 371 */     ItemEntity itemEntity = new ItemEntity(level(), bodyPosition.x(), bodyPosition.y(), bodyPosition.z(), item, deltaX, deltaY, deltaZ);
/* 372 */     level().addFreshEntity((Entity)itemEntity);
/* 373 */     return true;
/*     */   }
/*     */   
/*     */   public void addHorns() {
/* 377 */     this.entityData.set(DATA_HAS_LEFT_HORN, true);
/* 378 */     this.entityData.set(DATA_HAS_RIGHT_HORN, true);
/*     */   }
/*     */   
/*     */   public void removeHorns() {
/* 382 */     this.entityData.set(DATA_HAS_LEFT_HORN, false);
/* 383 */     this.entityData.set(DATA_HAS_RIGHT_HORN, false);
/*     */   }
/*     */   
/*     */   public boolean isScreamingGoat() {
/* 387 */     return (Boolean)this.entityData.get(DATA_IS_SCREAMING_GOAT);
/*     */   }
/*     */   
/*     */   public void setScreamingGoat(boolean isScreamingGoat) {
/* 391 */     this.entityData.set(DATA_IS_SCREAMING_GOAT, isScreamingGoat);
/*     */   }
/*     */   
/*     */   public float getRammingXHeadRot() {
/* 395 */     return this.lowerHeadTick / 20.0F * 30.0F * 0.017453292F;
/*     */   }
/*     */   
/*     */   public static boolean checkGoatSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 399 */     return (level.getBlockState(pos.below()).is(BlockTags.GOATS_SPAWNABLE_ON) && 
/* 400 */       isBrightEnoughToSpawn((BlockAndTintGetter)level, pos));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/goat/Goat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */