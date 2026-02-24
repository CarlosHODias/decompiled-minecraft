/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Drowned
/*     */   extends Zombie implements RangedAttackMob {
/*     */   public static final float NAUTILUS_SHELL_CHANCE = 0.03F;
/*     */   
/*     */   public Drowned(EntityType<? extends Drowned> type, Level level) {
/*  71 */     super((EntityType)type, level);
/*  72 */     this.moveControl = new DrownedMoveControl(this);
/*     */     
/*  74 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */   private static final float ZOMBIE_NAUTILUS_JOCKEY_CHANCE = 0.5F; private boolean searchingForLand;
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  78 */     return Zombie.createAttributes()
/*  79 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  84 */     return (PathNavigation)new AmphibiousPathNavigation((Mob)this, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  89 */     this.goalSelector.addGoal(1, new DrownedGoToWaterGoal((PathfinderMob)this, 1.0D));
/*  90 */     this.goalSelector.addGoal(2, (Goal)new DrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
/*  91 */     this.goalSelector.addGoal(2, (Goal)new DrownedAttackGoal(this, 1.0D, false));
/*  92 */     this.goalSelector.addGoal(5, (Goal)new DrownedGoToBeachGoal(this, 1.0D));
/*  93 */     this.goalSelector.addGoal(6, new DrownedSwimUpGoal(this, 1.0D, level().getSeaLevel()));
/*  94 */     this.goalSelector.addGoal(7, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D));
/*     */     
/*  96 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class<?>[] { Drowned.class }).setAlertOthers(new Class<?>[] { ZombifiedPiglin.class }));
/*  97 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, (target, level) -> okTarget(target)));
/*  98 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/*  99 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/* 100 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Axolotl.class, true, false));
/* 101 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 106 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/* 108 */     if (getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && 
/* 109 */       level.getRandom().nextFloat() < 0.03F) {
/* 110 */       setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike)Items.NAUTILUS_SHELL));
/* 111 */       setGuaranteedDrop(EquipmentSlot.OFFHAND);
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if ((spawnReason == EntitySpawnReason.NATURAL || spawnReason == EntitySpawnReason.STRUCTURE) && 
/* 116 */       getMainHandItem().is(Items.TRIDENT) && 
/* 117 */       level.getRandom().nextFloat() < 0.5F && 
/* 118 */       !isBaby() && 
/* 119 */       !level.getBiome(blockPosition()).is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
/* 120 */       ZombieNautilus zombieNautilus = (ZombieNautilus)EntityType.ZOMBIE_NAUTILUS.create(level(), EntitySpawnReason.JOCKEY);
/* 121 */       if (zombieNautilus != null) {
/* 122 */         if (spawnReason == EntitySpawnReason.STRUCTURE) {
/* 123 */           zombieNautilus.setPersistenceRequired();
/*     */         }
/* 125 */         zombieNautilus.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 126 */         zombieNautilus.finalizeSpawn(level, difficulty, spawnReason, null);
/* 127 */         startRiding((Entity)zombieNautilus, false, false);
/* 128 */         level.addFreshEntity((Entity)zombieNautilus);
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     return groupData;
/*     */   }
/*     */   
/*     */   public static boolean checkDrownedSpawnRules(EntityType<Drowned> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 136 */     if (!level.getFluidState(pos.below()).is(FluidTags.WATER) && !EntitySpawnReason.isSpawner(spawnReason)) {
/* 137 */       return false;
/*     */     }
/*     */     
/* 140 */     Holder<Biome> biome = level.getBiome(pos);
/* 141 */     boolean canMonsterSpawn = (level.getDifficulty() != Difficulty.PEACEFUL && (
/* 142 */       EntitySpawnReason.ignoresLightRequirements(spawnReason) || isDarkEnoughToSpawn(level, pos, random)) && (
/* 143 */       EntitySpawnReason.isSpawner(spawnReason) || level.getFluidState(pos).is(FluidTags.WATER)));
/*     */     
/* 145 */     if (canMonsterSpawn && (EntitySpawnReason.isSpawner(spawnReason) || spawnReason == EntitySpawnReason.REINFORCEMENT))
/* 146 */       return true; 
/* 147 */     if (biome.is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
/* 148 */       return (random.nextInt(15) == 0 && canMonsterSpawn);
/*     */     }
/* 150 */     return (random.nextInt(40) == 0 && isDeepEnoughToSpawn((LevelAccessor)level, pos) && canMonsterSpawn);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDeepEnoughToSpawn(LevelAccessor level, BlockPos pos) {
/* 155 */     return (pos.getY() < level.getSeaLevel() - 5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     if (isInWater()) {
/* 161 */       return SoundEvents.DROWNED_AMBIENT_WATER;
/*     */     }
/* 163 */     return SoundEvents.DROWNED_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 168 */     if (isInWater()) {
/* 169 */       return SoundEvents.DROWNED_HURT_WATER;
/*     */     }
/* 171 */     return SoundEvents.DROWNED_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 176 */     if (isInWater()) {
/* 177 */       return SoundEvents.DROWNED_DEATH_WATER;
/*     */     }
/* 179 */     return SoundEvents.DROWNED_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 184 */     return SoundEvents.DROWNED_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 189 */     return SoundEvents.DROWNED_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSpawnInLiquids() {
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 199 */     if (random.nextFloat() > 0.9D) {
/* 200 */       int rand = random.nextInt(16);
/* 201 */       if (rand < 10) {
/* 202 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.TRIDENT));
/*     */       } else {
/* 204 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.FISHING_ROD));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/* 211 */     if (currentItemStack.is(Items.NAUTILUS_SHELL)) {
/* 212 */       return false;
/*     */     }
/*     */     
/* 215 */     return super.canReplaceCurrentItem(newItemStack, currentItemStack, slot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/* 220 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 225 */     return level.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public boolean okTarget(LivingEntity target) {
/* 229 */     if (target != null) {
/* 230 */       if (level().isBrightOutside() && !target.isInWater()) {
/* 231 */         return false;
/*     */       }
/*     */       
/* 234 */       return true;
/*     */     } 
/* 236 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 241 */     return !isSwimming();
/*     */   }
/*     */   
/*     */   private boolean wantsToSwim() {
/* 245 */     if (this.searchingForLand) {
/* 246 */       return true;
/*     */     }
/*     */     
/* 249 */     LivingEntity target = getTarget();
/* 250 */     if (target != null && target.isInWater()) {
/* 251 */       return true;
/*     */     }
/*     */     
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 259 */     if (isUnderWater() && wantsToSwim()) {
/* 260 */       moveRelative(0.01F, input);
/* 261 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 263 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     } else {
/* 265 */       super.travelInWater(input, baseGravity, isFalling, oldY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSwimming() {
/* 271 */     if (!level().isClientSide()) {
/* 272 */       setSwimming((isEffectiveAi() && isUnderWater() && wantsToSwim()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVisuallySwimming() {
/* 278 */     return (isSwimming() && !isPassenger());
/*     */   }
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 282 */     Path path = getNavigation().getPath();
/* 283 */     if (path != null) {
/* 284 */       BlockPos pos = path.getTarget();
/* 285 */       if (pos != null) {
/* 286 */         double sqrDistToNextPos = distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
/* 287 */         if (sqrDistToNextPos < 4.0D) {
/* 288 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 297 */     ItemStack mainHandItem = getMainHandItem();
/* 298 */     ItemStack tridentItemStack = mainHandItem.is(Items.TRIDENT) ? mainHandItem : new ItemStack((ItemLike)Items.TRIDENT);
/* 299 */     ThrownTrident trident = new ThrownTrident(level(), (LivingEntity)this, tridentItemStack);
/*     */     
/* 301 */     double xd = target.getX() - getX();
/* 302 */     double yd = target.getY(0.3333333333333333D) - trident.getY();
/* 303 */     double zd = target.getZ() - getZ();
/* 304 */     double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
/* 305 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 306 */       Projectile.spawnProjectileUsingShoot((Projectile)trident, serverLevel, tridentItemStack, xd, yd + distanceToTarget * 0.20000000298023224D, zd, 1.6F, (14 - 
/*     */ 
/*     */ 
/*     */           
/* 310 */           level().getDifficulty().getId() * 4)); }
/*     */     
/* 312 */     playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 317 */     return ItemTags.DROWNED_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   public void setSearchingForLand(boolean searchingForLand) {
/* 321 */     this.searchingForLand = searchingForLand;
/*     */   }
/*     */   
/*     */   private static class DrownedTridentAttackGoal extends RangedAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedTridentAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
/* 328 */       super(mob, speedModifier, attackInterval, attackRadius);
/* 329 */       this.drowned = (Drowned)mob;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 334 */       return (super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 339 */       super.start();
/* 340 */       this.drowned.setAggressive(true);
/* 341 */       this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 346 */       super.stop();
/* 347 */       this.drowned.stopUsingItem();
/* 348 */       this.drowned.setAggressive(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedSwimUpGoal extends Goal {
/*     */     private final Drowned drowned;
/*     */     private final double speedModifier;
/*     */     private final int seaLevel;
/*     */     private boolean stuck;
/*     */     
/*     */     public DrownedSwimUpGoal(Drowned drowned, double speedModifier, int seaLevel) {
/* 359 */       this.drowned = drowned;
/* 360 */       this.speedModifier = speedModifier;
/* 361 */       this.seaLevel = seaLevel;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 366 */       return (!this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() < (this.seaLevel - 2));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 371 */       return (canUse() && !this.stuck);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 376 */       if (this.drowned.getY() < (this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
/*     */         
/* 378 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.drowned, 4, 8, new Vec3(this.drowned.getX(), (this.seaLevel - 1), this.drowned.getZ()), 1.5707963705062866D);
/*     */         
/* 380 */         if (nextPos == null) {
/* 381 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 385 */         this.drowned.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 391 */       this.drowned.setSearchingForLand(true);
/* 392 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 397 */       this.drowned.setSearchingForLand(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedGoToBeachGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedGoToBeachGoal(Drowned drowned, double speedModifier) {
/* 406 */       super((PathfinderMob)drowned, speedModifier, 8, 2);
/* 407 */       this.drowned = drowned;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 412 */       return (super.canUse() && !this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() >= (this.drowned.level().getSeaLevel() - 3));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 417 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 422 */       BlockPos above = pos.above();
/* 423 */       if (!level.isEmptyBlock(above) || !level.isEmptyBlock(above.above())) {
/* 424 */         return false;
/*     */       }
/*     */       
/* 427 */       return level.getBlockState(pos).entityCanStandOn((BlockGetter)level, pos, (Entity)this.drowned);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 432 */       this.drowned.setSearchingForLand(false);
/* 433 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 438 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedGoToWaterGoal extends Goal {
/*     */     private final PathfinderMob mob;
/*     */     private double wantedX;
/*     */     private double wantedY;
/*     */     private double wantedZ;
/*     */     private final double speedModifier;
/*     */     private final Level level;
/*     */     
/*     */     public DrownedGoToWaterGoal(PathfinderMob mob, double speedModifier) {
/* 451 */       this.mob = mob;
/* 452 */       this.speedModifier = speedModifier;
/* 453 */       this.level = mob.level();
/* 454 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 459 */       if (!this.level.isBrightOutside()) {
/* 460 */         return false;
/*     */       }
/* 462 */       if (this.mob.isInWater()) {
/* 463 */         return false;
/*     */       }
/*     */       
/* 466 */       Vec3 pos = getWaterPos();
/* 467 */       if (pos == null) {
/* 468 */         return false;
/*     */       }
/* 470 */       this.wantedX = pos.x;
/* 471 */       this.wantedY = pos.y;
/* 472 */       this.wantedZ = pos.z;
/* 473 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 478 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 483 */       this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*     */     }
/*     */     
/*     */     private Vec3 getWaterPos() {
/* 487 */       RandomSource random = this.mob.getRandom();
/* 488 */       BlockPos pos = this.mob.blockPosition();
/*     */       
/* 490 */       for (int i = 0; i < 10; i++) {
/* 491 */         BlockPos randomPos = pos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
/*     */         
/* 493 */         if (this.level.getBlockState(randomPos).is(Blocks.WATER)) {
/* 494 */           return Vec3.atBottomCenterOf((Vec3i)randomPos);
/*     */         }
/*     */       } 
/* 497 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedAttackGoal extends ZombieAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedAttackGoal(Drowned drowned, double speedModifier, boolean trackTarget) {
/* 505 */       super(drowned, speedModifier, trackTarget);
/* 506 */       this.drowned = drowned;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 511 */       return (super.canUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 516 */       return (super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedMoveControl extends MoveControl {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedMoveControl(Drowned drowned) {
/* 524 */       super((Mob)drowned);
/* 525 */       this.drowned = drowned;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 530 */       LivingEntity target = this.drowned.getTarget();
/* 531 */       if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
/* 532 */         if ((target != null && target.getY() > this.drowned.getY()) || this.drowned.searchingForLand)
/*     */         {
/* 534 */           this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
/*     */         }
/*     */         
/* 537 */         if (this.operation != MoveControl.Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
/* 538 */           this.drowned.setSpeed(0.0F);
/*     */           
/*     */           return;
/*     */         } 
/* 542 */         double xd = this.wantedX - this.drowned.getX();
/* 543 */         double yd = this.wantedY - this.drowned.getY();
/* 544 */         double zd = this.wantedZ - this.drowned.getZ();
/* 545 */         double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 546 */         yd /= dd;
/*     */         
/* 548 */         float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 549 */         this.drowned.setYRot(rotlerp(this.drowned.getYRot(), yRotD, 90.0F));
/* 550 */         this.drowned.yBodyRot = this.drowned.getYRot();
/*     */         
/* 552 */         float targetSpeed = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 553 */         float newSpeed = Mth.lerp(0.125F, this.drowned.getSpeed(), targetSpeed);
/* 554 */         this.drowned.setSpeed(newSpeed);
/* 555 */         this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(newSpeed * xd * 0.005D, newSpeed * yd * 0.1D, newSpeed * zd * 0.005D));
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 561 */         if (!this.drowned.onGround()) {
/* 562 */           this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
/*     */         }
/* 564 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void rideTick() {
/* 571 */     super.rideTick();
/*     */     
/* 573 */     Entity entity = getControlledVehicle(); if (entity instanceof PathfinderMob) { PathfinderMob pathfinderMob = (PathfinderMob)entity;
/* 574 */       this.yBodyRot = pathfinderMob.yBodyRot; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 580 */     if (itemStack.is(ItemTags.SPEARS)) {
/* 581 */       return false;
/*     */     }
/* 583 */     return super.wantsToPickUp(level, itemStack);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/zombie/Drowned.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */