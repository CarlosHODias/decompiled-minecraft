/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.PoiTypeTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.util.debug.DebugBeeInfo;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.valueproviders.UniformInt;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.GoalSelector;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*      */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*      */ import net.minecraft.world.entity.ai.util.AirRandomPos;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.animal.FlyingAnimal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.BonemealableBlock;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.DoublePlantBlock;
/*      */ import net.minecraft.world.level.block.FlowerBlock;
/*      */ import net.minecraft.world.level.block.StemBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Bee extends Animal implements FlyingAnimal, NeutralMob {
/*  102 */   public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
/*      */   public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
/*  104 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.BYTE);
/*  105 */   private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.LONG);
/*      */   
/*      */   private static final int FLAG_ROLL = 2;
/*      */   
/*      */   private static final int FLAG_HAS_STUNG = 4;
/*      */   
/*      */   private static final int FLAG_HAS_NECTAR = 8;
/*      */   
/*      */   private static final int STING_DEATH_COUNTDOWN = 1200;
/*      */   
/*      */   private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 600;
/*      */   
/*      */   private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600;
/*      */   
/*      */   private static final int MIN_ATTACK_DIST = 4;
/*      */   
/*      */   private static final int MAX_CROPS_GROWABLE = 10;
/*      */   
/*      */   private static final int POISON_SECONDS_NORMAL = 10;
/*      */   
/*      */   private static final int POISON_SECONDS_HARD = 18;
/*      */   
/*      */   private static final int TOO_FAR_DISTANCE = 48;
/*      */   
/*      */   private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;
/*      */   private static final int RESTRICTED_WANDER_DISTANCE_REDUCTION = 24;
/*      */   private static final int DEFAULT_WANDER_DISTANCE_REDUCTION = 16;
/*      */   private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;
/*      */   private static final int HIVE_SEARCH_DISTANCE = 20;
/*      */   public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
/*      */   public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
/*      */   public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
/*      */   public static final String TAG_HAS_STUNG = "HasStung";
/*      */   public static final String TAG_HAS_NECTAR = "HasNectar";
/*      */   public static final String TAG_FLOWER_POS = "flower_pos";
/*      */   public static final String TAG_HIVE_POS = "hive_pos";
/*      */   public static final boolean DEFAULT_HAS_NECTAR = false;
/*      */   private static final boolean DEFAULT_HAS_STUNG = false;
/*      */   private static final int DEFAULT_TICKS_SINCE_POLLINATION = 0;
/*      */   private static final int DEFAULT_CANNOT_ENTER_HIVE_TICKS = 0;
/*      */   private static final int DEFAULT_CROPS_GROWN_SINCE_POLLINATION = 0;
/*  146 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*      */   
/*      */   private EntityReference<LivingEntity> persistentAngerTarget;
/*      */   
/*      */   private float rollAmount;
/*      */   
/*      */   private float rollAmountO;
/*      */   private int timeSinceSting;
/*  154 */   private int ticksWithoutNectarSinceExitingHive = 0;
/*      */ 
/*      */   
/*  157 */   private int stayOutOfHiveCountdown = 0;
/*      */ 
/*      */   
/*  160 */   private int numCropsGrownSincePollination = 0;
/*      */ 
/*      */   
/*      */   private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
/*      */ 
/*      */   
/*      */   private int remainingCooldownBeforeLocatingNewHive;
/*      */   
/*      */   private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
/*      */   
/*      */   private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
/*      */   
/*      */   private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
/*      */   
/*  174 */   private int remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
/*      */   
/*      */   private BlockPos savedFlowerPos;
/*      */   
/*      */   private BlockPos hivePos;
/*      */   
/*      */   private BeePollinateGoal beePollinateGoal;
/*      */   
/*      */   private BeeGoToHiveGoal goToHiveGoal;
/*      */   private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
/*      */   private int underWaterTicks;
/*      */   
/*      */   public Bee(EntityType<? extends Bee> type, Level level) {
/*  187 */     super(type, level);
/*  188 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 20, true);
/*  189 */     this.lookControl = new BeeLookControl((Mob)this);
/*      */     
/*  191 */     setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
/*  192 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*  193 */     setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
/*  194 */     setPathfindingMalus(PathType.COCOA, -1.0F);
/*  195 */     setPathfindingMalus(PathType.FENCE, -1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  200 */     super.defineSynchedData(entityData);
/*  201 */     entityData.define(DATA_FLAGS_ID, (byte)0);
/*  202 */     entityData.define(DATA_ANGER_END_TIME, -1L);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/*  208 */     if (level.getBlockState(pos).isAir()) {
/*  209 */       return 10.0F;
/*      */     }
/*  211 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  216 */     this.goalSelector.addGoal(0, (Goal)new BeeAttackGoal((PathfinderMob)this, 1.399999976158142D, true));
/*  217 */     this.goalSelector.addGoal(1, new BeeEnterHiveGoal());
/*  218 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  219 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, i -> i.is(ItemTags.BEE_FOOD), false));
/*      */     
/*  221 */     this.goalSelector.addGoal(3, new ValidateHiveGoal());
/*  222 */     this.goalSelector.addGoal(3, new ValidateFlowerGoal());
/*      */     
/*  224 */     this.beePollinateGoal = new BeePollinateGoal();
/*  225 */     this.goalSelector.addGoal(4, this.beePollinateGoal);
/*      */     
/*  227 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.25D));
/*      */     
/*  229 */     this.goalSelector.addGoal(5, new BeeLocateHiveGoal());
/*      */     
/*  231 */     this.goToHiveGoal = new BeeGoToHiveGoal();
/*  232 */     this.goalSelector.addGoal(5, this.goToHiveGoal);
/*      */     
/*  234 */     this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal();
/*  235 */     this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
/*      */     
/*  237 */     this.goalSelector.addGoal(7, new BeeGrowCropGoal());
/*  238 */     this.goalSelector.addGoal(8, new BeeWanderGoal());
/*  239 */     this.goalSelector.addGoal(9, (Goal)new FloatGoal((Mob)this));
/*      */     
/*  241 */     this.targetSelector.addGoal(1, (Goal)new BeeHurtByOtherGoal(this).setAlertOthers(new Class<?>[0]));
/*  242 */     this.targetSelector.addGoal(2, (Goal)new BeeBecomeAngryTargetGoal(this));
/*  243 */     this.targetSelector.addGoal(3, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  248 */     super.addAdditionalSaveData(output);
/*      */     
/*  250 */     output.storeNullable("hive_pos", BlockPos.CODEC, this.hivePos);
/*  251 */     output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
/*  252 */     output.putBoolean("HasNectar", hasNectar());
/*  253 */     output.putBoolean("HasStung", hasStung());
/*  254 */     output.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
/*  255 */     output.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
/*  256 */     output.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
/*      */     
/*  258 */     addPersistentAngerSaveData(output);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  263 */     super.readAdditionalSaveData(input);
/*  264 */     setHasNectar(input.getBooleanOr("HasNectar", false));
/*  265 */     setHasStung(input.getBooleanOr("HasStung", false));
/*  266 */     this.ticksWithoutNectarSinceExitingHive = input.getIntOr("TicksSincePollination", 0);
/*  267 */     this.stayOutOfHiveCountdown = input.getIntOr("CannotEnterHiveTicks", 0);
/*  268 */     this.numCropsGrownSincePollination = input.getIntOr("CropsGrownSincePollination", 0);
/*      */     
/*  270 */     this.hivePos = input.read("hive_pos", BlockPos.CODEC).orElse(null);
/*  271 */     this.savedFlowerPos = input.read("flower_pos", BlockPos.CODEC).orElse(null);
/*      */     
/*  273 */     readPersistentAngerSaveData(level(), input);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  278 */     DamageSource damageSource = damageSources().sting((LivingEntity)this);
/*  279 */     boolean wasHurt = target.hurtServer(level, damageSource, (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/*  280 */     if (wasHurt) {
/*  281 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*  282 */       if (target instanceof LivingEntity) { LivingEntity livingTarget = (LivingEntity)target;
/*  283 */         livingTarget.setStingerCount(livingTarget.getStingerCount() + 1);
/*  284 */         int poisonTime = 0;
/*  285 */         if (level().getDifficulty() == Difficulty.NORMAL) {
/*  286 */           poisonTime = 10;
/*  287 */         } else if (level().getDifficulty() == Difficulty.HARD) {
/*  288 */           poisonTime = 18;
/*      */         } 
/*      */         
/*  291 */         if (poisonTime > 0) {
/*  292 */           livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0), (Entity)this);
/*      */         } }
/*      */       
/*  295 */       setHasStung(true);
/*  296 */       stopBeingAngry();
/*      */       
/*  298 */       playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
/*      */     } 
/*  300 */     return wasHurt;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  305 */     super.tick();
/*      */ 
/*      */     
/*  308 */     if (hasNectar() && getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
/*  309 */       for (int i = 0; i < this.random.nextInt(2) + 1; i++) {
/*  310 */         spawnFluidParticle(level(), getX() - 0.30000001192092896D, getX() + 0.30000001192092896D, getZ() - 0.30000001192092896D, getZ() + 0.30000001192092896D, getY(0.5D), (ParticleOptions)ParticleTypes.FALLING_NECTAR);
/*      */       }
/*      */     }
/*      */     
/*  314 */     updateRollAmount();
/*      */   }
/*      */   
/*      */   private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y, ParticleOptions dripParticle) {
/*  318 */     level.addParticle(dripParticle, Mth.lerp(level.random.nextDouble(), x1, x2), y, Mth.lerp(level.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
/*      */   }
/*      */   
/*      */   private void pathfindRandomlyTowards(BlockPos targetPos) {
/*  322 */     Vec3 targetVec = Vec3.atBottomCenterOf((Vec3i)targetPos);
/*  323 */     int yAdjust = 0;
/*  324 */     BlockPos beePos = blockPosition();
/*  325 */     int yDelta = (int)targetVec.y - beePos.getY();
/*  326 */     if (yDelta > 2) {
/*  327 */       yAdjust = 4;
/*  328 */     } else if (yDelta < -2) {
/*  329 */       yAdjust = -4;
/*      */     } 
/*      */     
/*  332 */     int xzDist = 6;
/*  333 */     int yDist = 8;
/*  334 */     int dist = beePos.distManhattan((Vec3i)targetPos);
/*  335 */     if (dist < 15) {
/*  336 */       xzDist = dist / 2;
/*  337 */       yDist = dist / 2;
/*      */     } 
/*      */     
/*  340 */     Vec3 nextPosTowards = AirRandomPos.getPosTowards((PathfinderMob)this, xzDist, yDist, yAdjust, targetVec, 0.3141592741012573D);
/*  341 */     if (nextPosTowards == null) {
/*      */       return;
/*      */     }
/*      */     
/*  345 */     this.navigation.setMaxVisitedNodesMultiplier(0.5F);
/*  346 */     this.navigation.moveTo(nextPosTowards.x, nextPosTowards.y, nextPosTowards.z, 1.0D);
/*      */   }
/*      */   
/*      */   public BlockPos getSavedFlowerPos() {
/*  350 */     return this.savedFlowerPos;
/*      */   }
/*      */   
/*      */   public boolean hasSavedFlowerPos() {
/*  354 */     return (this.savedFlowerPos != null);
/*      */   }
/*      */   
/*      */   public void setSavedFlowerPos(BlockPos savedFlowerPos) {
/*  358 */     this.savedFlowerPos = savedFlowerPos;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public int getTravellingTicks() {
/*  363 */     return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public List<BlockPos> getBlacklistedHives() {
/*  368 */     return this.goToHiveGoal.blacklistedTargets;
/*      */   }
/*      */   
/*      */   private boolean isTiredOfLookingForNectar() {
/*  372 */     return (this.ticksWithoutNectarSinceExitingHive > 3600);
/*      */   }
/*      */   
/*      */   private void dropHive() {
/*  376 */     this.hivePos = null;
/*  377 */     this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */   }
/*      */   
/*      */   private void dropFlower() {
/*  381 */     this.savedFlowerPos = null;
/*  382 */     this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean wantsToEnterHive() {
/*  387 */     if (this.stayOutOfHiveCountdown > 0 || this.beePollinateGoal.isPollinating() || hasStung() || getTarget() != null) {
/*  388 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  392 */     boolean wantsToEnterHive = (hasNectar() || 
/*  393 */       isTiredOfLookingForNectar() || (Boolean)
/*  394 */       level().environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, position()));
/*      */ 
/*      */     
/*  397 */     return (wantsToEnterHive && !isHiveNearFire());
/*      */   }
/*      */   
/*      */   public void setStayOutOfHiveCountdown(int ticks) {
/*  401 */     this.stayOutOfHiveCountdown = ticks;
/*      */   }
/*      */   
/*      */   public float getRollAmount(float a) {
/*  405 */     return Mth.lerp(a, this.rollAmountO, this.rollAmount);
/*      */   }
/*      */   
/*      */   private void updateRollAmount() {
/*  409 */     this.rollAmountO = this.rollAmount;
/*  410 */     if (isRolling()) {
/*  411 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
/*      */     } else {
/*  413 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {
/*  419 */     boolean hasStung = hasStung();
/*      */     
/*  421 */     if (isInWater()) {
/*  422 */       this.underWaterTicks++;
/*      */     } else {
/*  424 */       this.underWaterTicks = 0;
/*      */     } 
/*      */     
/*  427 */     if (this.underWaterTicks > 20) {
/*  428 */       hurtServer(level, damageSources().drown(), 1.0F);
/*      */     }
/*      */     
/*  431 */     if (hasStung) {
/*  432 */       this.timeSinceSting++;
/*      */ 
/*      */ 
/*      */       
/*  436 */       if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
/*  437 */         hurtServer(level, damageSources().generic(), getHealth());
/*      */       }
/*      */     } 
/*      */     
/*  441 */     if (!hasNectar()) {
/*  442 */       this.ticksWithoutNectarSinceExitingHive++;
/*      */     }
/*      */     
/*  445 */     updatePersistentAnger(level, false);
/*      */   }
/*      */   
/*      */   public void resetTicksWithoutNectarSinceExitingHive() {
/*  449 */     this.ticksWithoutNectarSinceExitingHive = 0;
/*      */   }
/*      */   
/*      */   private boolean isHiveNearFire() {
/*  453 */     BeehiveBlockEntity beehiveBlockEntity = getBeehiveBlockEntity();
/*  454 */     return (beehiveBlockEntity != null && beehiveBlockEntity.isFireNearby());
/*      */   }
/*      */ 
/*      */   
/*      */   public long getPersistentAngerEndTime() {
/*  459 */     return (Long)this.entityData.get(DATA_ANGER_END_TIME);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPersistentAngerEndTime(long endTime) {
/*  464 */     this.entityData.set(DATA_ANGER_END_TIME, endTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/*  469 */     return this.persistentAngerTarget;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/*  474 */     this.persistentAngerTarget = persistentAngerTarget;
/*      */   }
/*      */ 
/*      */   
/*      */   public void startPersistentAngerTimer() {
/*  479 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*      */   }
/*      */   
/*      */   private boolean doesHiveHaveSpace(BlockPos hivePos) {
/*  483 */     BlockEntity blockEntity = level().getBlockEntity(hivePos);
/*  484 */     if (blockEntity instanceof BeehiveBlockEntity) {
/*  485 */       return !((BeehiveBlockEntity)blockEntity).isFull();
/*      */     }
/*  487 */     return false;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public boolean hasHive() {
/*  492 */     return (this.hivePos != null);
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public BlockPos getHivePos() {
/*  497 */     return this.hivePos;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public GoalSelector getGoalSelector() {
/*  502 */     return this.goalSelector;
/*      */   }
/*      */   
/*      */   private int getCropsGrownSincePollination() {
/*  506 */     return this.numCropsGrownSincePollination;
/*      */   }
/*      */   
/*      */   private void resetNumCropsGrownSincePollination() {
/*  510 */     this.numCropsGrownSincePollination = 0;
/*      */   }
/*      */   
/*      */   private void incrementNumCropsGrownSincePollination() {
/*  514 */     this.numCropsGrownSincePollination++;
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  519 */     super.aiStep();
/*      */     
/*  521 */     if (!level().isClientSide()) {
/*  522 */       if (this.stayOutOfHiveCountdown > 0) {
/*  523 */         this.stayOutOfHiveCountdown--;
/*      */       }
/*  525 */       if (this.remainingCooldownBeforeLocatingNewHive > 0) {
/*  526 */         this.remainingCooldownBeforeLocatingNewHive--;
/*      */       }
/*  528 */       if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
/*  529 */         this.remainingCooldownBeforeLocatingNewFlower--;
/*      */       }
/*      */ 
/*      */       
/*  533 */       boolean shouldRoll = (isAngry() && !hasStung() && getTarget() != null && getTarget().distanceToSqr((Entity)this) < 4.0D);
/*  534 */       setRolling(shouldRoll);
/*      */       
/*  536 */       if (this.tickCount % 20 == 0)
/*      */       {
/*  538 */         if (!isHiveValid()) {
/*  539 */           this.hivePos = null;
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private BeehiveBlockEntity getBeehiveBlockEntity() {
/*  546 */     if (this.hivePos == null) {
/*  547 */       return null;
/*      */     }
/*  549 */     if (isTooFarAway(this.hivePos)) {
/*  550 */       return null;
/*      */     }
/*  552 */     return level().getBlockEntity(this.hivePos, net.minecraft.world.level.block.entity.BlockEntityType.BEEHIVE).orElse(null);
/*      */   }
/*      */   
/*      */   private boolean isHiveValid() {
/*  556 */     return (getBeehiveBlockEntity() != null);
/*      */   }
/*      */   
/*      */   public boolean hasNectar() {
/*  560 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   private void setHasNectar(boolean hasNectar) {
/*  564 */     if (hasNectar) {
/*  565 */       resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*  567 */     setFlag(8, hasNectar);
/*      */   }
/*      */   
/*      */   public boolean hasStung() {
/*  571 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   private void setHasStung(boolean hasStung) {
/*  575 */     setFlag(4, hasStung);
/*      */   }
/*      */   
/*      */   private boolean isRolling() {
/*  579 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   private void setRolling(boolean rolling) {
/*  583 */     setFlag(2, rolling);
/*      */   }
/*      */   
/*      */   private boolean isTooFarAway(BlockPos targetPos) {
/*  587 */     return !closerThan(targetPos, 48);
/*      */   }
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  591 */     if (value) {
/*  592 */       this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) | flag));
/*      */     } else {
/*  594 */       this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) & (flag ^ 0xFFFFFFFF)));
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean getFlag(int flag) {
/*  599 */     return (((Byte)this.entityData.get(DATA_FLAGS_ID) & flag) != 0);
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  603 */     return Animal.createAnimalAttributes()
/*  604 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  605 */       .add(Attributes.FLYING_SPEED, 0.6000000238418579D)
/*  606 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  607 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   protected PathNavigation createNavigation(Level level) {
/*  612 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level)
/*      */       {
/*      */         public boolean isStableDestination(BlockPos pos) {
/*  615 */           return !this.level.getBlockState(pos.below()).isAir();
/*      */         }
/*      */ 
/*      */         
/*      */         public void tick() {
/*  620 */           if (Bee.this.beePollinateGoal.isPollinating()) {
/*      */             return;
/*      */           }
/*      */           
/*  624 */           super.tick();
/*      */         }
/*      */       };
/*  627 */     flyingPathNavigation.setCanOpenDoors(false);
/*  628 */     flyingPathNavigation.setCanFloat(false);
/*  629 */     flyingPathNavigation.setRequiredPathLength(48.0F);
/*  630 */     return (PathNavigation)flyingPathNavigation;
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  635 */     ItemStack heldItem = player.getItemInHand(hand);
/*  636 */     if (isFood(heldItem)) { Item item = heldItem.getItem(); if (item instanceof BlockItem) { BlockItem blockItem = (BlockItem)item; Block block = blockItem.getBlock(); if (block instanceof FlowerBlock) { FlowerBlock flower = (FlowerBlock)block;
/*  637 */           MobEffectInstance effect = flower.getBeeInteractionEffect();
/*  638 */           if (effect != null)
/*  639 */           { usePlayerItem(player, hand, heldItem);
/*  640 */             if (!level().isClientSide()) {
/*  641 */               addEffect(effect);
/*      */             }
/*  643 */             return (InteractionResult)InteractionResult.SUCCESS; }  }
/*      */          }
/*      */        }
/*  646 */      return super.mobInteract(player, hand);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFood(ItemStack itemStack) {
/*  651 */     return itemStack.is(ItemTags.BEE_FOOD);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  661 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  666 */     return SoundEvents.BEE_HURT;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  671 */     return SoundEvents.BEE_DEATH;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getSoundVolume() {
/*  676 */     return 0.4F;
/*      */   }
/*      */ 
/*      */   
/*      */   public Bee getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  681 */     return (Bee)EntityType.BEE.create((Level)level, EntitySpawnReason.BREEDING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFlapping() {
/*  693 */     return (isFlying() && this.tickCount % TICKS_PER_FLAP == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFlying() {
/*  698 */     return !onGround();
/*      */   }
/*      */   
/*      */   public void dropOffNectar() {
/*  702 */     setHasNectar(false);
/*  703 */     resetNumCropsGrownSincePollination();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  708 */     if (isInvulnerableTo(level, source)) {
/*  709 */       return false;
/*      */     }
/*      */     
/*  712 */     this.beePollinateGoal.stopPollinating();
/*  713 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void jumpInLiquid(TagKey<Fluid> type) {
/*  718 */     setDeltaMovement(getDeltaMovement().add(0.0D, 0.01D, 0.0D));
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getLeashOffset() {
/*  723 */     return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.2F));
/*      */   }
/*      */   
/*      */   private boolean closerThan(BlockPos targetPos, int distance) {
/*  727 */     return targetPos.closerThan((Vec3i)blockPosition(), distance);
/*      */   }
/*      */   
/*      */   public void setHivePos(BlockPos hivePos) {
/*  731 */     this.hivePos = hivePos;
/*      */   }
/*      */   
/*      */   public static boolean attractsBees(BlockState state) {
/*  735 */     if (state.is(BlockTags.BEE_ATTRACTIVE)) {
/*  736 */       if ((Boolean)state.getValueOrElse((Property)BlockStateProperties.WATERLOGGED, false)) {
/*  737 */         return false;
/*      */       }
/*  739 */       if (state.is(Blocks.SUNFLOWER)) {
/*  740 */         return (state.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER);
/*      */       }
/*  742 */       return true;
/*      */     } 
/*  744 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/*  749 */     super.registerDebugValues(level, registration);
/*  750 */     registration.register(DebugSubscriptions.BEES, () -> new DebugBeeInfo(Optional.ofNullable(getHivePos()), Optional.ofNullable(getSavedFlowerPos()), getTravellingTicks(), getBlacklistedHives()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class BeeHurtByOtherGoal
/*      */     extends HurtByTargetGoal
/*      */   {
/*      */     BeeHurtByOtherGoal(Bee bee) {
/*  760 */       super((PathfinderMob)bee, new Class<?>[0]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  766 */       return (Bee.this.isAngry() && super.canContinueToUse());
/*      */     }
/*      */ 
/*      */     
/*      */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/*  771 */       if (other instanceof Bee && this.mob.hasLineOfSight((Entity)hurtByMob))
/*  772 */         other.setTarget(hurtByMob); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BeeBecomeAngryTargetGoal
/*      */     extends NearestAttackableTargetGoal<Player> {
/*      */     BeeBecomeAngryTargetGoal(Bee bee) {
/*  779 */       super((Mob)bee, Player.class, 10, true, false, bee::isAngryAt);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  784 */       return (beeCanTarget() && super.canUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  789 */       boolean beeCanTarget = beeCanTarget();
/*  790 */       if (!beeCanTarget || this.mob.getTarget() == null) {
/*  791 */         this.targetMob = null;
/*  792 */         return false;
/*      */       } 
/*  794 */       return super.canContinueToUse();
/*      */     }
/*      */     
/*      */     private boolean beeCanTarget() {
/*  798 */       Bee bee = (Bee)this.mob;
/*  799 */       return (bee.isAngry() && !bee.hasStung());
/*      */     }
/*      */   }
/*      */   
/*      */   private abstract class BaseBeeGoal
/*      */     extends Goal
/*      */   {
/*      */     public abstract boolean canBeeUse();
/*      */     
/*      */     public abstract boolean canBeeContinueToUse();
/*      */     
/*      */     public boolean canUse() {
/*  811 */       return (canBeeUse() && !Bee.this.isAngry());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  816 */       return (canBeeContinueToUse() && !Bee.this.isAngry());
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeWanderGoal
/*      */     extends Goal {
/*      */     BeeWanderGoal() {
/*  823 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  828 */       return (Bee.this.navigation.isDone() && Bee.this.random.nextInt(10) == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  833 */       return Bee.this.navigation.isInProgress();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  838 */       Vec3 targetPos = findPos();
/*  839 */       if (targetPos != null) {
/*  840 */         Bee.this.navigation.moveTo(Bee.this.navigation.createPath(BlockPos.containing((Position)targetPos), 1), 1.0D);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private Vec3 findPos() {
/*      */       Vec3 wanderDirection;
/*  847 */       if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, getWanderThreshold())) {
/*      */         
/*  849 */         Vec3 hivePosVec = Vec3.atCenterOf((Vec3i)Bee.this.hivePos);
/*  850 */         wanderDirection = hivePosVec.subtract(Bee.this.position()).normalize();
/*      */       } else {
/*  852 */         wanderDirection = Bee.this.getViewVector(0.0F);
/*      */       } 
/*      */       
/*  855 */       int xzDist = 8;
/*  856 */       Vec3 groundBasedPosition = net.minecraft.world.entity.ai.util.HoverRandomPos.getPos((PathfinderMob)Bee.this, 8, 7, wanderDirection.x, wanderDirection.z, 1.5707964F, 3, 1);
/*  857 */       if (groundBasedPosition != null) {
/*  858 */         return groundBasedPosition;
/*      */       }
/*      */ 
/*      */       
/*  862 */       return AirAndWaterRandomPos.getPos((PathfinderMob)Bee.this, 8, 4, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866D);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int getWanderThreshold() {
/*  869 */       int distanceReduction = (Bee.this.hasHive() || Bee.this.hasSavedFlowerPos()) ? 24 : 16;
/*  870 */       return 48 - distanceReduction;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForDebug
/*      */   public class BeeGoToHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     public static final int MAX_TRAVELLING_TICKS = 2400;
/*      */     
/*      */     private int travellingTicks;
/*      */     
/*      */     private static final int MAX_BLACKLISTED_TARGETS = 3;
/*      */     
/*  886 */     private final List<BlockPos> blacklistedTargets = Lists.newArrayList();
/*      */     
/*      */     private Path lastPath;
/*      */     
/*      */     private static final int TICKS_BEFORE_HIVE_DROP = 60;
/*      */     private int ticksStuck;
/*      */     
/*      */     BeeGoToHiveGoal() {
/*  894 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/*  899 */       return (Bee.this.hivePos != null && 
/*  900 */         !Bee.this.isTooFarAway(Bee.this.hivePos) && 
/*  901 */         !Bee.this.hasHome() && 
/*  902 */         Bee.this.wantsToEnterHive() && 
/*  903 */         !hasReachedTarget(Bee.this.hivePos) && 
/*  904 */         Bee.this.level().getBlockState(Bee.this.hivePos).is(BlockTags.BEEHIVES));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/*  909 */       return canBeeUse();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  914 */       this.travellingTicks = 0;
/*  915 */       this.ticksStuck = 0;
/*  916 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/*  921 */       this.travellingTicks = 0;
/*  922 */       this.ticksStuck = 0;
/*  923 */       Bee.this.navigation.stop();
/*  924 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  929 */       if (Bee.this.hivePos == null) {
/*      */         return;
/*      */       }
/*      */       
/*  933 */       this.travellingTicks++;
/*      */       
/*  935 */       if (this.travellingTicks > adjustedTickDelay(2400)) {
/*      */         
/*  937 */         dropAndBlacklistHive();
/*      */         
/*      */         return;
/*      */       } 
/*  941 */       if (Bee.this.navigation.isInProgress()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  947 */       if (Bee.this.closerThan(Bee.this.hivePos, 16)) {
/*      */         
/*  949 */         boolean canReachAllTheWayToTarget = pathfindDirectlyTowards(Bee.this.hivePos);
/*  950 */         if (!canReachAllTheWayToTarget) {
/*      */           
/*  952 */           dropAndBlacklistHive();
/*      */         }
/*  954 */         else if (this.lastPath != null && Bee.this.navigation.getPath().sameAs(this.lastPath)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  960 */           this.ticksStuck++;
/*      */           
/*  962 */           if (this.ticksStuck > 60) {
/*  963 */             Bee.this.dropHive();
/*  964 */             this.ticksStuck = 0;
/*      */           } 
/*      */         } else {
/*      */           
/*  968 */           this.lastPath = Bee.this.navigation.getPath();
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  974 */       if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
/*      */         
/*  976 */         Bee.this.dropHive();
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/*  983 */       Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean pathfindDirectlyTowards(BlockPos targetPos) {
/*  990 */       int closeEnough = Bee.this.closerThan(targetPos, 3) ? 1 : 2;
/*  991 */       Bee.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
/*  992 */       Bee.this.navigation.moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), closeEnough, 1.0D);
/*  993 */       return (Bee.this.navigation.getPath() != null && Bee.this.navigation.getPath().canReach());
/*      */     }
/*      */     
/*      */     private boolean isTargetBlacklisted(BlockPos targetPos) {
/*  997 */       return this.blacklistedTargets.contains(targetPos);
/*      */     }
/*      */     
/*      */     private void blacklistTarget(BlockPos targetPos) {
/* 1001 */       this.blacklistedTargets.add(targetPos);
/* 1002 */       while (this.blacklistedTargets.size() > 3) {
/* 1003 */         this.blacklistedTargets.remove(0);
/*      */       }
/*      */     }
/*      */     
/*      */     private void clearBlacklist() {
/* 1008 */       this.blacklistedTargets.clear();
/*      */     }
/*      */     
/*      */     private void dropAndBlacklistHive() {
/* 1012 */       if (Bee.this.hivePos != null) {
/* 1013 */         blacklistTarget(Bee.this.hivePos);
/*      */       }
/* 1015 */       Bee.this.dropHive();
/*      */     }
/*      */     
/*      */     private boolean hasReachedTarget(BlockPos targetPos) {
/* 1019 */       if (Bee.this.closerThan(targetPos, 2)) {
/* 1020 */         return true;
/*      */       }
/* 1022 */       Path path = Bee.this.navigation.getPath();
/* 1023 */       return (path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class BeeGoToKnownFlowerGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private static final int MAX_TRAVELLING_TICKS = 2400;
/*      */ 
/*      */     
/*      */     private int travellingTicks;
/*      */ 
/*      */     
/*      */     BeeGoToKnownFlowerGoal() {
/* 1039 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1044 */       return (Bee.this.savedFlowerPos != null && 
/* 1045 */         !Bee.this.hasHome() && 
/* 1046 */         wantsToGoToKnownFlower() && 
/* 1047 */         !Bee.this.closerThan(Bee.this.savedFlowerPos, 2));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1052 */       return canBeeUse();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1057 */       this.travellingTicks = 0;
/* 1058 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1063 */       this.travellingTicks = 0;
/* 1064 */       Bee.this.navigation.stop();
/* 1065 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1070 */       if (Bee.this.savedFlowerPos == null) {
/*      */         return;
/*      */       }
/* 1073 */       this.travellingTicks++;
/*      */       
/* 1075 */       if (this.travellingTicks > adjustedTickDelay(2400)) {
/*      */         
/* 1077 */         Bee.this.dropFlower();
/*      */         
/*      */         return;
/*      */       } 
/* 1081 */       if (Bee.this.navigation.isInProgress()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1086 */       if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
/*      */         
/* 1088 */         Bee.this.dropFlower();
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/* 1095 */       Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
/*      */     }
/*      */     
/*      */     private boolean wantsToGoToKnownFlower() {
/* 1099 */       return (Bee.this.ticksWithoutNectarSinceExitingHive > 600);
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeLookControl extends LookControl {
/*      */     BeeLookControl(Mob mob) {
/* 1105 */       super(mob);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1110 */       if (Bee.this.isAngry()) {
/*      */         return;
/*      */       }
/* 1113 */       super.tick();
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean resetXRotOnTick() {
/* 1118 */       return !Bee.this.beePollinateGoal.isPollinating();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class BeePollinateGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private static final int MIN_POLLINATION_TICKS = 400;
/*      */     
/*      */     private static final double ARRIVAL_THRESHOLD = 0.1D;
/*      */     
/*      */     private static final int POSITION_CHANGE_CHANCE = 25;
/*      */     
/*      */     private static final float SPEED_MODIFIER = 0.35F;
/*      */     
/*      */     private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
/*      */     
/*      */     private static final float HOVER_POS_OFFSET = 0.33333334F;
/*      */     private static final int FLOWER_SEARCH_RADIUS = 5;
/*      */     private int successfulPollinatingTicks;
/*      */     private int lastSoundPlayedTick;
/*      */     private boolean pollinating;
/*      */     private Vec3 hoverPos;
/*      */     private int pollinatingTicks;
/*      */     private static final int MAX_POLLINATING_TICKS = 600;
/* 1144 */     private Long2LongOpenHashMap unreachableFlowerCache = new Long2LongOpenHashMap();
/*      */     
/*      */     BeePollinateGoal() {
/* 1147 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1152 */       if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
/* 1153 */         return false;
/*      */       }
/*      */       
/* 1156 */       if (Bee.this.hasNectar()) {
/* 1157 */         return false;
/*      */       }
/* 1159 */       if (Bee.this.level().isRaining()) {
/* 1160 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1164 */       Optional<BlockPos> nearbyPos = findNearbyFlower();
/* 1165 */       if (nearbyPos.isPresent()) {
/* 1166 */         Bee.this.savedFlowerPos = nearbyPos.get();
/*      */         
/* 1168 */         Bee.this.navigation.moveTo(Bee.this.savedFlowerPos.getX() + 0.5D, Bee.this.savedFlowerPos.getY() + 0.5D, Bee.this.savedFlowerPos.getZ() + 0.5D, 1.2000000476837158D);
/* 1169 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1173 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.this.random, 20, 60);
/* 1174 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1179 */       if (!this.pollinating) {
/* 1180 */         return false;
/*      */       }
/* 1182 */       if (!Bee.this.hasSavedFlowerPos()) {
/* 1183 */         return false;
/*      */       }
/* 1185 */       if (Bee.this.level().isRaining()) {
/* 1186 */         return false;
/*      */       }
/* 1188 */       if (hasPollinatedLongEnough()) {
/* 1189 */         return (Bee.this.random.nextFloat() < 0.2F);
/*      */       }
/* 1191 */       return true;
/*      */     }
/*      */     
/*      */     private boolean hasPollinatedLongEnough() {
/* 1195 */       return (this.successfulPollinatingTicks > 400);
/*      */     }
/*      */     
/*      */     private boolean isPollinating() {
/* 1199 */       return this.pollinating;
/*      */     }
/*      */     
/*      */     private void stopPollinating() {
/* 1203 */       this.pollinating = false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1208 */       this.successfulPollinatingTicks = 0;
/* 1209 */       this.pollinatingTicks = 0;
/* 1210 */       this.lastSoundPlayedTick = 0;
/* 1211 */       this.pollinating = true;
/* 1212 */       Bee.this.resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1217 */       if (hasPollinatedLongEnough()) {
/* 1218 */         Bee.this.setHasNectar(true);
/*      */       }
/* 1220 */       this.pollinating = false;
/* 1221 */       Bee.this.navigation.stop();
/*      */       
/* 1223 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean requiresUpdateEveryTick() {
/* 1228 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1234 */       if (!Bee.this.hasSavedFlowerPos()) {
/*      */         return;
/*      */       }
/*      */       
/* 1238 */       this.pollinatingTicks++;
/* 1239 */       if (this.pollinatingTicks > 600) {
/*      */         
/* 1241 */         Bee.this.dropFlower();
/* 1242 */         this.pollinating = false;
/* 1243 */         Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */         
/*      */         return;
/*      */       } 
/* 1247 */       Vec3 flowerPos = Vec3.atBottomCenterOf((Vec3i)Bee.this.savedFlowerPos).add(0.0D, 0.6000000238418579D, 0.0D);
/*      */       
/* 1249 */       if (flowerPos.distanceTo(Bee.this.position()) > 1.0D) {
/* 1250 */         this.hoverPos = flowerPos;
/* 1251 */         setWantedPos();
/*      */         
/*      */         return;
/*      */       } 
/* 1255 */       if (this.hoverPos == null) {
/* 1256 */         this.hoverPos = flowerPos;
/*      */       }
/*      */       
/* 1259 */       boolean arrivedAtHoverPos = (Bee.this.position().distanceTo(this.hoverPos) <= 0.1D);
/*      */       
/*      */       boolean shouldSetWantedPos = true;
/* 1262 */       if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
/*      */         
/* 1264 */         Bee.this.dropFlower();
/*      */         
/*      */         return;
/*      */       } 
/* 1268 */       if (arrivedAtHoverPos) {
/* 1269 */         boolean shouldChangeHoverPositions = (Bee.this.random.nextInt(25) == 0);
/* 1270 */         if (shouldChangeHoverPositions) {
/* 1271 */           this.hoverPos = new Vec3(flowerPos.x() + getOffset(), flowerPos.y(), flowerPos.z() + getOffset());
/*      */           
/* 1273 */           Bee.this.navigation.stop();
/*      */         } else {
/* 1275 */           shouldSetWantedPos = false;
/*      */         } 
/*      */         
/* 1278 */         Bee.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
/*      */       } 
/*      */       
/* 1281 */       if (shouldSetWantedPos) {
/* 1282 */         setWantedPos();
/*      */       }
/*      */       
/* 1285 */       this.successfulPollinatingTicks++;
/*      */       
/* 1287 */       if (Bee.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
/* 1288 */         this.lastSoundPlayedTick = this.successfulPollinatingTicks;
/* 1289 */         Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void setWantedPos() {
/* 1294 */       Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D);
/*      */     }
/*      */     
/*      */     private float getOffset() {
/* 1298 */       return (Bee.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
/*      */     }
/*      */     
/*      */     private Optional<BlockPos> findNearbyFlower() {
/* 1302 */       Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(Bee.this.blockPosition(), 5, 5, 5);
/* 1303 */       Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();
/*      */       
/* 1305 */       for (BlockPos pos : closestNearbyFlowers) {
/*      */         
/* 1307 */         long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Long.MIN_VALUE);
/* 1308 */         if (Bee.this.level().getGameTime() < unreachableUntilTime) {
/* 1309 */           tempCache.put(pos.asLong(), unreachableUntilTime);
/*      */           continue;
/*      */         } 
/* 1312 */         if (Bee.attractsBees(Bee.this.level().getBlockState(pos))) {
/* 1313 */           Path path = Bee.this.navigation.createPath(pos, 1);
/* 1314 */           if (path != null && path.canReach()) {
/* 1315 */             return Optional.of(pos);
/*      */           }
/* 1317 */           tempCache.put(pos.asLong(), Bee.this.level().getGameTime() + 600L);
/*      */         } 
/*      */       } 
/*      */       
/* 1321 */       this.unreachableFlowerCache = tempCache;
/* 1322 */       return Optional.empty();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class BeeLocateHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     public boolean canBeeUse() {
/* 1332 */       return (Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && 
/* 1333 */         !Bee.this.hasHive() && 
/* 1334 */         Bee.this.wantsToEnterHive());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1339 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1344 */       Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */ 
/*      */       
/* 1347 */       List<BlockPos> hivesWithSpace = findNearbyHivesWithSpace();
/*      */       
/* 1349 */       if (hivesWithSpace.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1355 */       for (BlockPos posToCheck : hivesWithSpace) {
/* 1356 */         if (!Bee.this.goToHiveGoal.isTargetBlacklisted(posToCheck)) {
/*      */           
/* 1358 */           Bee.this.hivePos = posToCheck;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1366 */       Bee.this.goToHiveGoal.clearBlacklist();
/* 1367 */       Bee.this.hivePos = hivesWithSpace.get(0);
/*      */     }
/*      */     
/*      */     private List<BlockPos> findNearbyHivesWithSpace() {
/* 1371 */       BlockPos beePos = Bee.this.blockPosition();
/* 1372 */       PoiManager poiManager = ((ServerLevel)Bee.this.level()).getPoiManager();
/* 1373 */       Stream<PoiRecord> nearbyHives = poiManager.getInRange(p -> p.is(PoiTypeTags.BEE_HOME), beePos, 20, PoiManager.Occupancy.ANY);
/* 1374 */       return (List<BlockPos>)nearbyHives.map(PoiRecord::getPos)
/* 1375 */         .filter(Bee.this::doesHiveHaveSpace)
/* 1376 */         .sorted(Comparator.comparingDouble(pos -> pos.distSqr((Vec3i)beePos))).collect(Collectors.toList());
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeGrowCropGoal
/*      */     extends BaseBeeGoal {
/*      */     static final int GROW_CHANCE = 30;
/*      */     
/*      */     public boolean canBeeUse() {
/* 1385 */       if (Bee.this.getCropsGrownSincePollination() >= 10) {
/* 1386 */         return false;
/*      */       }
/*      */       
/* 1389 */       if (Bee.this.random.nextFloat() < 0.3F) {
/* 1390 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1394 */       return (Bee.this.hasNectar() && Bee.this.isHiveValid());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1399 */       return canBeeUse();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1404 */       if (Bee.this.random.nextInt(adjustedTickDelay(30)) != 0) {
/*      */         return;
/*      */       }
/*      */       
/* 1408 */       for (int i = 1; i <= 2; i++) {
/* 1409 */         BlockPos belowPos = Bee.this.blockPosition().below(i);
/* 1410 */         BlockState belowState = Bee.this.level().getBlockState(belowPos);
/* 1411 */         Block belowBlock = belowState.getBlock();
/* 1412 */         BlockState growState = null;
/* 1413 */         if (belowState.is(BlockTags.BEE_GROWABLES)) {
/* 1414 */           if (belowBlock instanceof CropBlock) { CropBlock cropBlockBelow = (CropBlock)belowBlock;
/* 1415 */             if (!cropBlockBelow.isMaxAge(belowState)) {
/* 1416 */               growState = cropBlockBelow.getStateForAge(cropBlockBelow.getAge(belowState) + 1);
/*      */             } }
/* 1418 */           else if (belowBlock instanceof StemBlock)
/* 1419 */           { int age = (Integer)belowState.getValue((Property)StemBlock.AGE);
/* 1420 */             if (age < 7) {
/* 1421 */               growState = (BlockState)belowState.setValue((Property)StemBlock.AGE, age + 1);
/*      */             } }
/* 1423 */           else if (belowState.is(Blocks.SWEET_BERRY_BUSH))
/* 1424 */           { int age = (Integer)belowState.getValue((Property)SweetBerryBushBlock.AGE);
/* 1425 */             if (age < 3) {
/* 1426 */               growState = (BlockState)belowState.setValue((Property)SweetBerryBushBlock.AGE, age + 1);
/*      */             } }
/* 1428 */           else if (belowState.is(Blocks.CAVE_VINES) || belowState.is(Blocks.CAVE_VINES_PLANT))
/* 1429 */           { BonemealableBlock bonemealableBlock = (BonemealableBlock)belowState.getBlock();
/* 1430 */             if (bonemealableBlock.isValidBonemealTarget((LevelReader)Bee.this.level(), belowPos, belowState)) {
/* 1431 */               bonemealableBlock.performBonemeal((ServerLevel)Bee.this.level(), Bee.this.random, belowPos, belowState);
/* 1432 */               growState = Bee.this.level().getBlockState(belowPos);
/*      */             }  }
/*      */ 
/*      */           
/* 1436 */           if (growState != null) {
/* 1437 */             Bee.this.level().levelEvent(2011, belowPos, 15);
/* 1438 */             Bee.this.level().setBlockAndUpdate(belowPos, growState);
/* 1439 */             Bee.this.incrementNumCropsGrownSincePollination();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeAttackGoal extends MeleeAttackGoal {
/*      */     BeeAttackGoal(PathfinderMob mob, double speedModifier, boolean trackTarget) {
/* 1448 */       super(mob, speedModifier, trackTarget);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1453 */       return (super.canUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1458 */       return (super.canContinueToUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class BeeEnterHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     public boolean canBeeUse() {
/* 1468 */       if (Bee.this.hivePos != null && Bee.this.wantsToEnterHive() && Bee.this.hivePos.closerToCenterThan((Position)Bee.this.position(), 2.0D)) {
/* 1469 */         BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1470 */         if (beehiveBlockEntity != null) {
/* 1471 */           if (beehiveBlockEntity.isFull()) {
/* 1472 */             Bee.this.hivePos = null;
/*      */           } else {
/* 1474 */             return true;
/*      */           } 
/*      */         }
/*      */       } 
/* 1478 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1483 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1488 */       BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1489 */       if (beehiveBlockEntity != null)
/* 1490 */         beehiveBlockEntity.addOccupant(Bee.this); 
/*      */     } }
/*      */   private class ValidateFlowerGoal extends BaseBeeGoal { private final int validateFlowerCooldown;
/*      */     private long lastValidateTick;
/*      */     
/*      */     private ValidateFlowerGoal() {
/* 1496 */       this.validateFlowerCooldown = Mth.nextInt(Bee.this.random, 20, 40);
/*      */       
/* 1498 */       this.lastValidateTick = -1L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1503 */       if (Bee.this.savedFlowerPos != null && Bee.this.level().isLoaded(Bee.this.savedFlowerPos) && !isFlower(Bee.this.savedFlowerPos)) {
/* 1504 */         Bee.this.dropFlower();
/*      */       }
/* 1506 */       this.lastValidateTick = Bee.this.level().getGameTime();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1511 */       return (Bee.this.level().getGameTime() > this.lastValidateTick + this.validateFlowerCooldown);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1516 */       return false;
/*      */     }
/*      */     
/*      */     private boolean isFlower(BlockPos flowerPos) {
/* 1520 */       return Bee.attractsBees(Bee.this.level().getBlockState(flowerPos));
/*      */     } }
/*      */   private class ValidateHiveGoal extends BaseBeeGoal { private final int VALIDATE_HIVE_COOLDOWN;
/*      */     
/*      */     private ValidateHiveGoal() {
/* 1525 */       this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(Bee.this.random, 20, 40);
/*      */       
/* 1527 */       this.lastValidateTick = -1L;
/*      */     }
/*      */     private long lastValidateTick;
/*      */     public void start() {
/* 1531 */       if (Bee.this.hivePos != null && Bee.this.level().isLoaded(Bee.this.hivePos) && !Bee.this.isHiveValid()) {
/* 1532 */         Bee.this.dropHive();
/*      */       }
/* 1534 */       this.lastValidateTick = Bee.this.level().getGameTime();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1539 */       return (Bee.this.level().getGameTime() > this.lastValidateTick + this.VALIDATE_HIVE_COOLDOWN);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1544 */       return false;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/bee/Bee.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */