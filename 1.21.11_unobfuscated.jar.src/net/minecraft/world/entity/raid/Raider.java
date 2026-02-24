/*     */ package net.minecraft.world.entity.raid;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class Raider extends PatrollingMonster {
/*  48 */   protected static final EntityDataAccessor<Boolean> IS_CELEBRATING = SynchedEntityData.defineId(Raider.class, EntityDataSerializers.BOOLEAN); private static final Predicate<ItemEntity> ALLOWED_ITEMS; private static final int DEFAULT_WAVE = 0;
/*     */   static {
/*  50 */     ALLOWED_ITEMS = (e -> (!e.hasPickUpDelay() && e.isAlive() && ItemStack.matches(e.getItem(), Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)e.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean DEFAULT_CAN_JOIN_RAID = false;
/*     */   
/*     */   protected Raid raid;
/*     */   
/*  58 */   private int wave = 0;
/*     */   private boolean canJoinRaid = false;
/*     */   private int ticksOutsideRaid;
/*     */   
/*     */   protected Raider(EntityType<? extends Raider> type, Level level) {
/*  63 */     super(type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  68 */     super.registerGoals();
/*  69 */     this.goalSelector.addGoal(1, new ObtainRaidLeaderBannerGoal<>(this));
/*  70 */     this.goalSelector.addGoal(3, (Goal)new net.minecraft.world.entity.ai.goal.PathfindToRaidGoal(this));
/*  71 */     this.goalSelector.addGoal(4, new RaiderMoveThroughVillageGoal(this, 1.0499999523162842D, 1));
/*  72 */     this.goalSelector.addGoal(5, new RaiderCelebration(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  77 */     super.defineSynchedData(entityData);
/*     */     
/*  79 */     entityData.define(IS_CELEBRATING, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canJoinRaid() {
/*  85 */     return this.canJoinRaid;
/*     */   }
/*     */   
/*     */   public void setCanJoinRaid(boolean canJoinRaid) {
/*  89 */     this.canJoinRaid = canJoinRaid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  97 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (isAlive()) {
/*  98 */         Raid currentRaid = getCurrentRaid();
/*  99 */         if (canJoinRaid())
/* 100 */           if (currentRaid == null) {
/* 101 */             if (level().getGameTime() % 20L == 0L) {
/* 102 */               Raid nearbyRaid = serverLevel.getRaidAt(blockPosition());
/* 103 */               if (nearbyRaid != null && Raids.canJoinRaid(this)) {
/* 104 */                 nearbyRaid.joinRaid(serverLevel, nearbyRaid.getGroupsSpawned(), this, null, true);
/*     */               }
/*     */             } 
/*     */           } else {
/*     */             
/* 109 */             LivingEntity target = getTarget();
/* 110 */             if (target != null && (target.getType() == EntityType.PLAYER || target.getType() == EntityType.IRON_GOLEM)) {
/* 111 */               this.noActionTime = 0;
/*     */             }
/*     */           }  
/*     */       }  }
/*     */     
/* 116 */     super.aiStep();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateNoActionTime() {
/* 122 */     this.noActionTime += 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource source) {
/* 127 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 128 */       Entity killer = source.getEntity();
/* 129 */       Raid raidWhenKilled = getCurrentRaid();
/* 130 */       if (raidWhenKilled != null) {
/* 131 */         if (isPatrolLeader()) {
/* 132 */           raidWhenKilled.removeLeader(getWave());
/*     */         }
/*     */         
/* 135 */         if (killer != null && killer.getType() == EntityType.PLAYER) {
/* 136 */           raidWhenKilled.addHeroOfTheVillage(killer);
/*     */         }
/*     */         
/* 139 */         raidWhenKilled.removeFromRaid(serverLevel, this, false);
/*     */       }  }
/*     */ 
/*     */     
/* 143 */     super.die(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canJoinPatrol() {
/* 148 */     return !hasActiveRaid();
/*     */   }
/*     */   
/*     */   public void setCurrentRaid(Raid raid) {
/* 152 */     this.raid = raid;
/*     */   }
/*     */   
/*     */   public Raid getCurrentRaid() {
/* 156 */     return this.raid;
/*     */   }
/*     */   
/*     */   public boolean isCaptain() {
/* 160 */     ItemStack banner = getItemBySlot(EquipmentSlot.HEAD);
/* 161 */     boolean hasCaptainBanner = (!banner.isEmpty() && ItemStack.matches(banner, Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)registryAccess().lookupOrThrow(Registries.BANNER_PATTERN))));
/* 162 */     boolean patrolLeader = isPatrolLeader();
/*     */     
/* 164 */     return (hasCaptainBanner && patrolLeader);
/*     */   }
/*     */   public boolean hasRaid() {
/*     */     ServerLevel serverLevel;
/* 168 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 169 */     else { return false; }
/*     */     
/* 171 */     return (getCurrentRaid() != null || serverLevel.getRaidAt(blockPosition()) != null);
/*     */   }
/*     */   
/*     */   public boolean hasActiveRaid() {
/* 175 */     return (getCurrentRaid() != null && getCurrentRaid().isActive());
/*     */   }
/*     */   
/*     */   public void setWave(int wave) {
/* 179 */     this.wave = wave;
/*     */   }
/*     */   
/*     */   public int getWave() {
/* 183 */     return this.wave;
/*     */   }
/*     */   
/*     */   public boolean isCelebrating() {
/* 187 */     return (Boolean)this.entityData.get(IS_CELEBRATING);
/*     */   }
/*     */   
/*     */   public void setCelebrating(boolean celebrating) {
/* 191 */     this.entityData.set(IS_CELEBRATING, celebrating);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 196 */     super.addAdditionalSaveData(output);
/* 197 */     output.putInt("Wave", this.wave);
/* 198 */     output.putBoolean("CanJoinRaid", this.canJoinRaid);
/* 199 */     if (this.raid != null) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 200 */         serverLevel.getRaids().getId(this.raid).ifPresent(id -> output.putInt("RaidId", id)); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 206 */     super.readAdditionalSaveData(input);
/* 207 */     this.wave = input.getIntOr("Wave", 0);
/* 208 */     this.canJoinRaid = input.getBooleanOr("CanJoinRaid", false);
/* 209 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 210 */       input.getInt("RaidId").ifPresent(raidId -> {
/*     */             this.raid = level.getRaids().get(level);
/*     */             if (this.raid != null) {
/*     */               this.raid.addWaveMob(level, this.wave, this, false);
/*     */               if (isPatrolLeader()) {
/*     */                 this.raid.setLeader(this.wave, this);
/*     */               }
/*     */             } 
/*     */           }); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 225 */     ItemStack itemStack = entity.getItem();
/* 226 */     boolean hasRaidLeader = (hasActiveRaid() && getCurrentRaid().getLeader(getWave()) != null);
/*     */ 
/*     */     
/* 229 */     if (hasActiveRaid() && !hasRaidLeader && ItemStack.matches(itemStack, Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))) {
/* 230 */       EquipmentSlot slot = EquipmentSlot.HEAD;
/* 231 */       ItemStack current = getItemBySlot(slot);
/* 232 */       double dropChance = getDropChances().byEquipment(slot);
/* 233 */       if (!current.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < dropChance) {
/* 234 */         spawnAtLocation(level, current);
/*     */       }
/* 236 */       onItemPickup(entity);
/* 237 */       setItemSlot(slot, itemStack);
/* 238 */       take((Entity)entity, itemStack.getCount());
/* 239 */       entity.discard();
/* 240 */       getCurrentRaid().setLeader(getWave(), this);
/* 241 */       setPatrolLeader(true);
/*     */     } else {
/* 243 */       super.pickUpItem(level, entity);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 249 */     if (getCurrentRaid() == null) {
/* 250 */       return super.removeWhenFarAway(distSqr);
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 257 */     return (super.requiresCustomPersistence() || getCurrentRaid() != null);
/*     */   }
/*     */   
/*     */   public int getTicksOutsideRaid() {
/* 261 */     return this.ticksOutsideRaid;
/*     */   }
/*     */   
/*     */   public void setTicksOutsideRaid(int ticksOutsideRaid) {
/* 265 */     this.ticksOutsideRaid = ticksOutsideRaid;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 270 */     if (hasActiveRaid()) {
/* 271 */       getCurrentRaid().updateBossbar();
/*     */     }
/* 273 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 279 */     setCanJoinRaid((getType() != EntityType.WITCH || spawnReason != EntitySpawnReason.NATURAL));
/*     */     
/* 281 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public abstract void applyRaidBuffs(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean);
/*     */   
/*     */   public abstract net.minecraft.sounds.SoundEvent getCelebrateSound();
/*     */   
/*     */   public class ObtainRaidLeaderBannerGoal<T extends Raider> extends Goal { private final T mob;
/* 289 */     private Int2LongOpenHashMap unreachableBannerCache = new Int2LongOpenHashMap();
/*     */     private Path pathToBanner;
/*     */     private ItemEntity pursuedBannerItemEntity;
/*     */     
/*     */     public ObtainRaidLeaderBannerGoal(T mob) {
/* 294 */       this.mob = mob;
/* 295 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 300 */       if (cannotPickUpBanner()) {
/* 301 */         return false;
/*     */       }
/*     */       
/* 304 */       Int2LongOpenHashMap tempCache = new Int2LongOpenHashMap();
/* 305 */       double followRange = Raider.this.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 306 */       List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(followRange, 8.0D, followRange), Raider.ALLOWED_ITEMS);
/*     */       
/* 308 */       for (ItemEntity banner : items) {
/* 309 */         long unreachableUntilTime = this.unreachableBannerCache.getOrDefault(banner.getId(), Long.MIN_VALUE);
/* 310 */         if (Raider.this.level().getGameTime() < unreachableUntilTime) {
/* 311 */           tempCache.put(banner.getId(), unreachableUntilTime);
/*     */           
/*     */           continue;
/*     */         } 
/* 315 */         Path path = this.mob.getNavigation().createPath((Entity)banner, 1);
/* 316 */         if (path != null && path.canReach()) {
/* 317 */           this.pathToBanner = path;
/* 318 */           this.pursuedBannerItemEntity = banner;
/* 319 */           return true;
/*     */         } 
/* 321 */         tempCache.put(banner.getId(), Raider.this.level().getGameTime() + 600L);
/*     */       } 
/*     */ 
/*     */       
/* 325 */       this.unreachableBannerCache = tempCache;
/*     */       
/* 327 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 332 */       if (this.pursuedBannerItemEntity == null || this.pathToBanner == null) {
/* 333 */         return false;
/*     */       }
/* 335 */       if (this.pursuedBannerItemEntity.isRemoved()) {
/* 336 */         return false;
/*     */       }
/* 338 */       if (this.pathToBanner.isDone()) {
/* 339 */         return false;
/*     */       }
/* 341 */       if (cannotPickUpBanner()) {
/* 342 */         return false;
/*     */       }
/* 344 */       return true;
/*     */     }
/*     */     
/*     */     private boolean cannotPickUpBanner() {
/* 348 */       if (!this.mob.hasActiveRaid()) {
/* 349 */         return true;
/*     */       }
/* 351 */       if (this.mob.getCurrentRaid().isOver()) {
/* 352 */         return true;
/*     */       }
/* 354 */       if (!this.mob.canBeLeader()) {
/* 355 */         return true;
/*     */       }
/* 357 */       if (ItemStack.matches(this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)this.mob.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))) {
/* 358 */         return true;
/*     */       }
/* 360 */       Raider leader = Raider.this.raid.getLeader(this.mob.getWave());
/* 361 */       if (leader != null && leader.isAlive()) {
/* 362 */         return true;
/*     */       }
/* 364 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 369 */       this.mob.getNavigation().moveTo(this.pathToBanner, 1.149999976158142D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 374 */       this.pathToBanner = null;
/* 375 */       this.pursuedBannerItemEntity = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 380 */       if (this.pursuedBannerItemEntity != null && this.pursuedBannerItemEntity.closerThan((Entity)this.mob, 1.414D))
/* 381 */         this.mob.pickUpItem(getServerLevel(Raider.this.level()), this.pursuedBannerItemEntity); 
/*     */     } }
/*     */ 
/*     */   
/*     */   public class RaiderCelebration
/*     */     extends Goal {
/*     */     private final Raider mob;
/*     */     
/*     */     RaiderCelebration(Raider mob) {
/* 390 */       this.mob = mob;
/* 391 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 396 */       Raid currentRaid = this.mob.getCurrentRaid();
/* 397 */       return (this.mob.isAlive() && this.mob.getTarget() == null && currentRaid != null && currentRaid.isLoss());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 402 */       this.mob.setCelebrating(true);
/* 403 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 408 */       this.mob.setCelebrating(false);
/* 409 */       super.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 414 */       if (!this.mob.isSilent() && this.mob.random.nextInt(adjustedTickDelay(100)) == 0) {
/* 415 */         Raider.this.makeSound(Raider.this.getCelebrateSound());
/*     */       }
/*     */       
/* 418 */       if (!this.mob.isPassenger() && this.mob.random.nextInt(adjustedTickDelay(50)) == 0) {
/* 419 */         this.mob.getJumpControl().jump();
/*     */       }
/*     */       
/* 422 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class HoldGroundAttackGoal extends Goal {
/*     */     private final Raider mob;
/*     */     private final float hostileRadiusSqr;
/* 429 */     public final TargetingConditions shoutTargeting = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */     
/*     */     public HoldGroundAttackGoal(AbstractIllager mob, float hostileRadius) {
/* 432 */       this.mob = (Raider)mob;
/* 433 */       this.hostileRadiusSqr = hostileRadius * hostileRadius;
/* 434 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 440 */       LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
/* 441 */       return (this.mob.getCurrentRaid() == null && this.mob.isPatrolling() && this.mob.getTarget() != null && !this.mob.isAggressive() && (lastHurtByMob == null || lastHurtByMob.getType() != EntityType.PLAYER));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 446 */       super.start();
/* 447 */       this.mob.getNavigation().stop();
/*     */       
/* 449 */       List<Raider> nearbyEntities = getServerLevel((Entity)this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 450 */       for (Raider entity : nearbyEntities) {
/* 451 */         entity.setTarget(this.mob.getTarget());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 457 */       super.stop();
/*     */       
/* 459 */       LivingEntity target = this.mob.getTarget();
/* 460 */       if (target != null) {
/* 461 */         List<Raider> nearbyEntities = getServerLevel((Entity)this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 462 */         for (Raider entity : nearbyEntities) {
/* 463 */           entity.setTarget(target);
/* 464 */           entity.setAggressive(true);
/*     */         } 
/* 466 */         this.mob.setAggressive(true);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 472 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 477 */       LivingEntity target = this.mob.getTarget();
/* 478 */       if (target == null) {
/*     */         return;
/*     */       }
/*     */       
/* 482 */       if (this.mob.distanceToSqr((Entity)target) > this.hostileRadiusSqr) {
/* 483 */         this.mob.getLookControl().setLookAt((Entity)target, 30.0F, 30.0F);
/*     */         
/* 485 */         if (this.mob.random.nextInt(50) == 0) {
/* 486 */           this.mob.playAmbientSound();
/*     */         }
/*     */       } else {
/* 489 */         this.mob.setAggressive(true);
/*     */       } 
/*     */       
/* 492 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RaiderMoveThroughVillageGoal extends Goal {
/*     */     private final Raider raider;
/*     */     private final double speedModifier;
/*     */     private BlockPos poiPos;
/* 500 */     private final List<BlockPos> visited = Lists.newArrayList();
/*     */     private final int distanceToPoi;
/*     */     private boolean stuck;
/*     */     
/*     */     public RaiderMoveThroughVillageGoal(Raider mob, double speedModifier, int distanceToPoi) {
/* 505 */       this.raider = mob;
/* 506 */       this.speedModifier = speedModifier;
/* 507 */       this.distanceToPoi = distanceToPoi;
/* 508 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 513 */       updateVisited();
/* 514 */       return (isValidRaid() && hasSuitablePoi() && this.raider.getTarget() == null);
/*     */     }
/*     */     
/*     */     private boolean isValidRaid() {
/* 518 */       return (this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver());
/*     */     }
/*     */     
/*     */     private boolean hasSuitablePoi() {
/* 522 */       ServerLevel level = (ServerLevel)this.raider.level();
/* 523 */       BlockPos pos = this.raider.blockPosition();
/* 524 */       Optional<BlockPos> homePos = level.getPoiManager().getRandom(p -> p.is(PoiTypes.HOME), this::hasNotVisited, PoiManager.Occupancy.ANY, pos, 48, this.raider.random);
/* 525 */       if (homePos.isEmpty()) {
/* 526 */         return false;
/*     */       }
/*     */       
/* 529 */       this.poiPos = ((BlockPos)homePos.get()).immutable();
/*     */       
/* 531 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 536 */       if (this.raider.getNavigation().isDone()) {
/* 537 */         return false;
/*     */       }
/* 539 */       return (this.raider.getTarget() == null && !this.poiPos.closerToCenterThan((Position)this.raider.position(), (this.raider.getBbWidth() + this.distanceToPoi)) && !this.stuck);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 544 */       if (this.poiPos.closerToCenterThan((Position)this.raider.position(), this.distanceToPoi)) {
/* 545 */         this.visited.add(this.poiPos);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 551 */       super.start();
/* 552 */       this.raider.setNoActionTime(0);
/* 553 */       this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
/* 554 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 559 */       if (this.raider.getNavigation().isDone()) {
/* 560 */         Vec3 poiVec = Vec3.atBottomCenterOf((Vec3i)this.poiPos);
/* 561 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.raider, 16, 7, poiVec, 0.3141592741012573D);
/* 562 */         if (nextPos == null) {
/* 563 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.raider, 8, 7, poiVec, 1.5707963705062866D);
/*     */         }
/*     */         
/* 566 */         if (nextPos == null) {
/* 567 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 571 */         this.raider.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean hasNotVisited(BlockPos poi) {
/* 576 */       for (BlockPos visitedPoi : this.visited) {
/* 577 */         if (Objects.equals(poi, visitedPoi)) {
/* 578 */           return false;
/*     */         }
/*     */       } 
/* 581 */       return true;
/*     */     }
/*     */     
/*     */     private void updateVisited() {
/* 585 */       if (this.visited.size() > 2)
/* 586 */         this.visited.remove(0); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/raid/Raider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */