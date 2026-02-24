/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractArrow
/*     */   extends Projectile
/*     */ {
/*     */   private static final double ARROW_BASE_DAMAGE = 2.0D;
/*     */   private static final int SHAKE_TIME = 7;
/*     */   private static final float WATER_INERTIA = 0.6F;
/*     */   private static final float INERTIA = 0.99F;
/*     */   private static final short DEFAULT_LIFE = 0;
/*     */   private static final byte DEFAULT_SHAKE = 0;
/*     */   private static final boolean DEFAULT_IN_GROUND = false;
/*     */   private static final boolean DEFAULT_CRIT = false;
/*     */   private static final byte DEFAULT_PIERCE_LEVEL = 0;
/*     */   
/*     */   public enum Pickup
/*     */   {
/*  73 */     DISALLOWED, ALLOWED, CREATIVE_ONLY; public static final Codec<Pickup> LEGACY_CODEC;
/*     */     static {
/*  75 */       LEGACY_CODEC = Codec.BYTE.xmap(Pickup::byOrdinal, p -> (byte)p.ordinal());
/*     */     }
/*     */     public static Pickup byOrdinal(int ordinal) {
/*  78 */       if (ordinal < 0 || ordinal > (values()).length) {
/*  79 */         ordinal = 0;
/*     */       }
/*     */       
/*  82 */       return values()[ordinal];
/*     */     }
/*     */   }
/*     */   
/*  86 */   private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
/*  87 */   private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
/*  88 */   private static final EntityDataAccessor<Boolean> IN_GROUND = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int FLAG_CRIT = 1;
/*     */   private static final int FLAG_NOPHYSICS = 2;
/*     */   private BlockState lastState;
/*     */   protected int inGroundTime;
/*  94 */   public Pickup pickup = Pickup.DISALLOWED;
/*  95 */   public int shakeTime = 0;
/*  96 */   private int life = 0;
/*  97 */   private double baseDamage = 2.0D;
/*     */   
/*     */   private SoundEvent soundEvent;
/*     */   
/*     */   private IntOpenHashSet piercingIgnoreEntityIds;
/*     */   private List<Entity> piercedAndKilledEntities;
/*     */   private ItemStack pickupItemStack;
/*     */   private ItemStack firedFromWeapon;
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> type, Level level) {
/* 107 */     super(type, level);
/* 108 */     this.soundEvent = getDefaultHitGroundSoundEvent();
/* 109 */     this.pickupItemStack = getDefaultPickupItem();
/* 110 */     this.firedFromWeapon = null;
/*     */   }
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 114 */     this(type, level);
/* 115 */     this.pickupItemStack = pickupItemStack.copy();
/* 116 */     applyComponentsFromItemStack(pickupItemStack);
/*     */     
/* 118 */     Unit intangible = (Unit)pickupItemStack.remove(DataComponents.INTANGIBLE_PROJECTILE);
/* 119 */     if (intangible != null) {
/* 120 */       this.pickup = Pickup.CREATIVE_ONLY;
/*     */     }
/*     */     
/* 123 */     setPos(x, y, z);
/* 124 */     if (firedFromWeapon != null && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 125 */       if (firedFromWeapon.isEmpty()) {
/* 126 */         throw new IllegalArgumentException("Invalid weapon firing an arrow");
/*     */       }
/* 128 */       this.firedFromWeapon = firedFromWeapon.copy();
/* 129 */       int pierceLevel = EnchantmentHelper.getPiercingCount(serverLevel, firedFromWeapon, this.pickupItemStack);
/* 130 */       if (pierceLevel > 0) {
/* 131 */         setPierceLevel((byte)pierceLevel);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> type, LivingEntity mob, Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 137 */     this(type, mob.getX(), mob.getEyeY() - 0.10000000149011612D, mob.getZ(), level, pickupItemStack, firedFromWeapon);
/*     */     
/* 139 */     setOwner((Entity)mob);
/*     */   }
/*     */   
/*     */   public void setSoundEvent(SoundEvent soundEvent) {
/* 143 */     this.soundEvent = soundEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 148 */     double size = getBoundingBox().getSize() * 10.0D;
/* 149 */     if (Double.isNaN(size)) {
/* 150 */       size = 1.0D;
/*     */     }
/* 152 */     size *= 64.0D * getViewScale();
/* 153 */     return (distance < size * size);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 158 */     entityData.define(ID_FLAGS, (byte)0);
/* 159 */     entityData.define(PIERCE_LEVEL, (byte)0);
/* 160 */     entityData.define(IN_GROUND, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shoot(double xd, double yd, double zd, float pow, float uncertainty) {
/* 165 */     super.shoot(xd, yd, zd, pow, uncertainty);
/* 166 */     this.life = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void lerpMotion(Vec3 movement) {
/* 171 */     super.lerpMotion(movement);
/* 172 */     this.life = 0;
/* 173 */     if (isInGround() && movement.lengthSqr() > 0.0D)
/*     */     {
/* 175 */       setInGround(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 181 */     super.onSyncedDataUpdated(accessor);
/* 182 */     if (!this.firstTick && this.shakeTime <= 0 && accessor.equals(IN_GROUND) && isInGround()) {
/* 183 */       this.shakeTime = 7;
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     float yRot;
/* 189 */     boolean physicsEnabled = !isNoPhysics();
/*     */     
/* 191 */     Vec3 movement = getDeltaMovement();
/*     */     
/* 193 */     BlockPos blockPos = blockPosition();
/* 194 */     BlockState blockState = level().getBlockState(blockPos);
/* 195 */     if (!blockState.isAir() && physicsEnabled) {
/* 196 */       VoxelShape shape = blockState.getCollisionShape((BlockGetter)level(), blockPos);
/* 197 */       if (!shape.isEmpty()) {
/* 198 */         Vec3 position = position();
/* 199 */         for (AABB aabb : (Iterable<AABB>)shape.toAabbs()) {
/* 200 */           if (aabb.move(blockPos).contains(position)) {
/* 201 */             setDeltaMovement(Vec3.ZERO);
/* 202 */             setInGround(true);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 209 */     if (this.shakeTime > 0) {
/* 210 */       this.shakeTime--;
/*     */     }
/*     */     
/* 213 */     if (isInWaterOrRain()) {
/* 214 */       clearFire();
/*     */     }
/*     */     
/* 217 */     if (isInGround() && physicsEnabled) {
/* 218 */       if (!level().isClientSide()) {
/* 219 */         if (this.lastState != blockState && shouldFall()) {
/* 220 */           startFalling();
/*     */         } else {
/* 222 */           tickDespawn();
/*     */         } 
/*     */       }
/*     */       
/* 226 */       this.inGroundTime++;
/* 227 */       if (isAlive())
/*     */       {
/* 229 */         applyEffectsFromBlocks();
/*     */       }
/*     */       
/* 232 */       if (!level().isClientSide()) {
/* 233 */         setSharedFlagOnFire((getRemainingFireTicks() > 0));
/*     */       }
/*     */       return;
/*     */     } 
/* 237 */     this.inGroundTime = 0;
/*     */     
/* 239 */     Vec3 originalPosition = position();
/*     */     
/* 241 */     if (isInWater()) {
/* 242 */       applyInertia(getWaterInertia());
/* 243 */       addBubbleParticles(originalPosition);
/*     */     } 
/*     */     
/* 246 */     if (isCritArrow()) {
/* 247 */       for (int i = 0; i < 4; i++) {
/* 248 */         level().addParticle((ParticleOptions)ParticleTypes.CRIT, originalPosition.x + movement.x * i / 4.0D, originalPosition.y + movement.y * i / 4.0D, originalPosition.z + movement.z * i / 4.0D, -movement.x, -movement.y + 0.2D, -movement.z);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 253 */     if (!physicsEnabled) {
/* 254 */       yRot = (float)(Mth.atan2(-movement.x, -movement.z) * 57.2957763671875D);
/*     */     } else {
/* 256 */       yRot = (float)(Mth.atan2(movement.x, movement.z) * 57.2957763671875D);
/*     */     } 
/* 258 */     float xRot = (float)(Mth.atan2(movement.y, movement.horizontalDistance()) * 57.2957763671875D);
/* 259 */     setXRot(lerpRotation(getXRot(), xRot));
/* 260 */     setYRot(lerpRotation(getYRot(), yRot));
/*     */ 
/*     */     
/* 263 */     checkLeftOwner();
/*     */     
/* 265 */     if (physicsEnabled) {
/* 266 */       BlockHitResult blockHitResult = level().clipIncludingBorder(new ClipContext(originalPosition, originalPosition.add(movement), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this));
/* 267 */       stepMoveAndHit(blockHitResult);
/*     */     } else {
/* 269 */       setPos(originalPosition.add(movement));
/* 270 */       applyEffectsFromBlocks();
/*     */     } 
/*     */     
/* 273 */     if (!isInWater()) {
/* 274 */       applyInertia(0.99F);
/*     */     }
/* 276 */     if (physicsEnabled && !isInGround()) {
/* 277 */       applyGravity();
/*     */     }
/* 279 */     super.tick();
/*     */   }
/*     */   
/*     */   private void stepMoveAndHit(BlockHitResult blockHitResult) {
/* 283 */     while (isAlive()) {
/* 284 */       Vec3 initialPosition = position();
/* 285 */       ArrayList<EntityHitResult> entitiesHit = new ArrayList<>(findHitEntities(initialPosition, blockHitResult.getLocation()));
/* 286 */       entitiesHit.sort(Comparator.comparingDouble(c -> initialPosition.distanceToSqr(c.getEntity().position())));
/*     */       
/* 288 */       EntityHitResult firstEntityHit = entitiesHit.isEmpty() ? null : entitiesHit.getFirst();
/* 289 */       Vec3 nextLocation = ((HitResult)Objects.requireNonNullElse(firstEntityHit, blockHitResult)).getLocation();
/*     */       
/* 291 */       setPos(nextLocation);
/* 292 */       applyEffectsFromBlocks(initialPosition, nextLocation);
/* 293 */       if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick()) {
/* 294 */         handlePortal();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 299 */       if (entitiesHit.isEmpty()) {
/* 300 */         if (isAlive() && blockHitResult.getType() != HitResult.Type.MISS) {
/* 301 */           hitTargetOrDeflectSelf((HitResult)blockHitResult);
/* 302 */           this.needsSync = true;
/*     */         }  break;
/*     */       } 
/* 305 */       if (isAlive() && !this.noPhysics) {
/* 306 */         ProjectileDeflection deflection = hitTargetsOrDeflectSelf(entitiesHit);
/* 307 */         this.needsSync = true;
/* 308 */         if (getPierceLevel() <= 0 || deflection != ProjectileDeflection.NONE) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ProjectileDeflection hitTargetsOrDeflectSelf(Collection<EntityHitResult> entityHitResults) {
/* 316 */     for (EntityHitResult e : entityHitResults) {
/* 317 */       ProjectileDeflection deflection = hitTargetOrDeflectSelf((HitResult)e);
/* 318 */       if (!isAlive() || deflection != ProjectileDeflection.NONE) {
/* 319 */         return deflection;
/*     */       }
/*     */     } 
/* 322 */     return ProjectileDeflection.NONE;
/*     */   }
/*     */   
/*     */   private void applyInertia(float inertia) {
/* 326 */     Vec3 movement = getDeltaMovement();
/* 327 */     setDeltaMovement(movement.scale(inertia));
/*     */   }
/*     */   
/*     */   private void addBubbleParticles(Vec3 position) {
/* 331 */     Vec3 movement = getDeltaMovement();
/* 332 */     for (int i = 0; i < 4; i++) {
/* 333 */       float s = 0.25F;
/* 334 */       level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, position.x - movement.x * 0.25D, position.y - movement.y * 0.25D, position.z - movement.z * 0.25D, movement.x, movement.y, movement.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 340 */     return 0.05D;
/*     */   }
/*     */   
/*     */   private boolean shouldFall() {
/* 344 */     return (isInGround() && level().noCollision(new AABB(position(), position()).inflate(0.06D)));
/*     */   }
/*     */   
/*     */   private void startFalling() {
/* 348 */     setInGround(false);
/* 349 */     Vec3 deltaMovement = getDeltaMovement();
/* 350 */     setDeltaMovement(deltaMovement.multiply((
/* 351 */           this.random.nextFloat() * 0.2F), (
/* 352 */           this.random.nextFloat() * 0.2F), (
/* 353 */           this.random.nextFloat() * 0.2F)));
/*     */     
/* 355 */     this.life = 0;
/*     */   }
/*     */   
/*     */   protected boolean isInGround() {
/* 359 */     return (Boolean)this.entityData.get(IN_GROUND);
/*     */   }
/*     */   
/*     */   protected void setInGround(boolean inGround) {
/* 363 */     this.entityData.set(IN_GROUND, inGround);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 368 */     return !isInGround();
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 373 */     super.move(moverType, delta);
/* 374 */     if (moverType != MoverType.SELF && shouldFall()) {
/* 375 */       startFalling();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void tickDespawn() {
/* 380 */     this.life++;
/* 381 */     if (this.life >= 1200) {
/* 382 */       discard();
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetPiercedEntities() {
/* 387 */     if (this.piercedAndKilledEntities != null) {
/* 388 */       this.piercedAndKilledEntities.clear();
/*     */     }
/* 390 */     if (this.piercingIgnoreEntityIds != null) {
/* 391 */       this.piercingIgnoreEntityIds.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onItemBreak(Item item) {
/* 397 */     this.firedFromWeapon = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/* 402 */     if (isInGround()) {
/*     */       return;
/*     */     }
/* 405 */     super.onAboveBubbleColumn(dragDown, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInsideBubbleColumn(boolean dragDown) {
/* 410 */     if (isInGround()) {
/*     */       return;
/*     */     }
/* 413 */     super.onInsideBubbleColumn(dragDown);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 418 */     if (isInGround()) {
/*     */       return;
/*     */     }
/* 421 */     super.push(xa, ya, za);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 426 */     super.onHitEntity(hitResult);
/* 427 */     Entity entity = hitResult.getEntity();
/* 428 */     float pow = (float)getDeltaMovement().length();
/* 429 */     double arrowDamage = this.baseDamage;
/* 430 */     Entity currentOwner = getOwner();
/* 431 */     DamageSource damageSource = damageSources().arrow(this, (currentOwner != null) ? currentOwner : (Entity)this);
/*     */     
/* 433 */     if (getWeaponItem() != null) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 434 */         arrowDamage = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), entity, damageSource, (float)arrowDamage); }
/*     */        }
/* 436 */      int damage = Mth.ceil(Mth.clamp(pow * arrowDamage, 0.0D, 2.147483647E9D));
/*     */     
/* 438 */     if (getPierceLevel() > 0) {
/* 439 */       if (this.piercingIgnoreEntityIds == null) {
/* 440 */         this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
/*     */       }
/*     */       
/* 443 */       if (this.piercedAndKilledEntities == null) {
/* 444 */         this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
/*     */       }
/*     */ 
/*     */       
/* 448 */       if (this.piercingIgnoreEntityIds.size() < getPierceLevel() + 1) {
/* 449 */         this.piercingIgnoreEntityIds.add(entity.getId());
/*     */       } else {
/* 451 */         discard();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 456 */     if (isCritArrow()) {
/* 457 */       long dmgIncrease = this.random.nextInt(damage / 2 + 2);
/* 458 */       damage = (int)Math.min(dmgIncrease + damage, 2147483647L);
/*     */     } 
/*     */     
/* 461 */     if (currentOwner instanceof LivingEntity) { LivingEntity livingOwner = (LivingEntity)currentOwner;
/* 462 */       livingOwner.setLastHurtMob(entity); }
/*     */ 
/*     */     
/* 465 */     boolean isEnderman = (entity.getType() == EntityType.ENDERMAN);
/* 466 */     int remainingFireTicks = entity.getRemainingFireTicks();
/*     */ 
/*     */     
/* 469 */     if (isOnFire() && !isEnderman) {
/* 470 */       entity.igniteForSeconds(5.0F);
/*     */     }
/*     */     
/* 473 */     if (entity.hurtOrSimulate(damageSource, damage)) {
/*     */       
/* 475 */       if (isEnderman) {
/*     */         return;
/*     */       }
/* 478 */       if (entity instanceof LivingEntity) { LivingEntity mob = (LivingEntity)entity;
/* 479 */         if (!level().isClientSide() && getPierceLevel() <= 0) {
/* 480 */           mob.setArrowCount(mob.getArrowCount() + 1);
/*     */         }
/*     */         
/* 483 */         doKnockback(mob, damageSource);
/*     */         
/* 485 */         Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 486 */           EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, (Entity)mob, damageSource, getWeaponItem()); }
/*     */ 
/*     */         
/* 489 */         doPostHurtEffects(mob);
/*     */         
/* 491 */         if (mob instanceof Player && currentOwner instanceof ServerPlayer) { ServerPlayer ownerPlayer = (ServerPlayer)currentOwner; if (!isSilent() && mob != ownerPlayer) {
/* 492 */             ownerPlayer.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, 0.0F));
/*     */           } }
/*     */         
/* 495 */         if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
/* 496 */           this.piercedAndKilledEntities.add(mob);
/*     */         }
/*     */         
/* 499 */         if (!level().isClientSide() && currentOwner instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)currentOwner;
/* 500 */           if (this.piercedAndKilledEntities != null) {
/* 501 */             CriteriaTriggers.KILLED_BY_ARROW.trigger(player, this.piercedAndKilledEntities, this.firedFromWeapon);
/* 502 */           } else if (!entity.isAlive()) {
/* 503 */             CriteriaTriggers.KILLED_BY_ARROW.trigger(player, List.of(entity), this.firedFromWeapon);
/*     */           }  }
/*     */          }
/*     */       
/* 507 */       playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
/* 508 */       if (getPierceLevel() <= 0) {
/* 509 */         discard();
/*     */       }
/*     */     } else {
/*     */       
/* 513 */       entity.setRemainingFireTicks(remainingFireTicks);
/* 514 */       deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
/* 515 */       setDeltaMovement(getDeltaMovement().scale(0.2D));
/*     */       
/* 517 */       Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (getDeltaMovement().lengthSqr() < 1.0E-7D) {
/* 518 */           if (this.pickup == Pickup.ALLOWED) {
/* 519 */             spawnAtLocation(serverLevel, getPickupItem(), 0.1F);
/*     */           }
/* 521 */           discard();
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doKnockback(LivingEntity mob, DamageSource damageSource) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield firedFromWeapon : Lnet/minecraft/world/item/ItemStack;
/*     */     //   4: ifnull -> 43
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   11: astore #6
/*     */     //   13: aload #6
/*     */     //   15: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   18: ifeq -> 43
/*     */     //   21: aload #6
/*     */     //   23: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   26: astore #5
/*     */     //   28: aload #5
/*     */     //   30: aload_0
/*     */     //   31: getfield firedFromWeapon : Lnet/minecraft/world/item/ItemStack;
/*     */     //   34: aload_1
/*     */     //   35: aload_2
/*     */     //   36: fconst_0
/*     */     //   37: invokestatic modifyKnockback : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;F)F
/*     */     //   40: goto -> 44
/*     */     //   43: fconst_0
/*     */     //   44: f2d
/*     */     //   45: dstore_3
/*     */     //   46: dload_3
/*     */     //   47: dconst_0
/*     */     //   48: dcmpl
/*     */     //   49: ifle -> 120
/*     */     //   52: dconst_0
/*     */     //   53: dconst_1
/*     */     //   54: aload_1
/*     */     //   55: getstatic net/minecraft/world/entity/ai/attributes/Attributes.KNOCKBACK_RESISTANCE : Lnet/minecraft/core/Holder;
/*     */     //   58: invokevirtual getAttributeValue : (Lnet/minecraft/core/Holder;)D
/*     */     //   61: dsub
/*     */     //   62: invokestatic max : (DD)D
/*     */     //   65: dstore #5
/*     */     //   67: aload_0
/*     */     //   68: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   71: dconst_1
/*     */     //   72: dconst_0
/*     */     //   73: dconst_1
/*     */     //   74: invokevirtual multiply : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   77: invokevirtual normalize : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   80: dload_3
/*     */     //   81: ldc2_w 0.6
/*     */     //   84: dmul
/*     */     //   85: dload #5
/*     */     //   87: dmul
/*     */     //   88: invokevirtual scale : (D)Lnet/minecraft/world/phys/Vec3;
/*     */     //   91: astore #7
/*     */     //   93: aload #7
/*     */     //   95: invokevirtual lengthSqr : ()D
/*     */     //   98: dconst_0
/*     */     //   99: dcmpl
/*     */     //   100: ifle -> 120
/*     */     //   103: aload_1
/*     */     //   104: aload #7
/*     */     //   106: getfield x : D
/*     */     //   109: ldc2_w 0.1
/*     */     //   112: aload #7
/*     */     //   114: getfield z : D
/*     */     //   117: invokevirtual push : (DDD)V
/*     */     //   120: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #527	-> 0
/*     */     //   #528	-> 46
/*     */     //   #529	-> 52
/*     */     //   #530	-> 67
/*     */     //   #531	-> 93
/*     */     //   #532	-> 103
/*     */     //   #535	-> 120
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   28	15	5	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   67	53	5	knockbackResistance	D
/*     */     //   93	27	7	movement	Lnet/minecraft/world/phys/Vec3;
/*     */     //   0	121	0	this	Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;
/*     */     //   0	121	1	mob	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	121	2	damageSource	Lnet/minecraft/world/damagesource/DamageSource;
/*     */     //   46	75	3	knockback	D
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 539 */     this.lastState = level().getBlockState(hitResult.getBlockPos());
/* 540 */     super.onHitBlock(hitResult);
/*     */     
/* 542 */     ItemStack weaponItem = getWeaponItem();
/* 543 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (weaponItem != null) {
/* 544 */         hitBlockEnchantmentEffects(serverLevel, hitResult, weaponItem);
/*     */       } }
/*     */     
/* 547 */     Vec3 movement = getDeltaMovement();
/* 548 */     Vec3 offsetDirection = new Vec3(Math.signum(movement.x), Math.signum(movement.y), Math.signum(movement.z));
/* 549 */     Vec3 scaledMovement = offsetDirection.scale(0.05000000074505806D);
/* 550 */     setPos(position().subtract(scaledMovement));
/* 551 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/* 553 */     playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
/* 554 */     setInGround(true);
/* 555 */     this.shakeTime = 7;
/* 556 */     setCritArrow(false);
/* 557 */     setPierceLevel((byte)0);
/* 558 */     setSoundEvent(SoundEvents.ARROW_HIT);
/* 559 */     resetPiercedEntities();
/*     */   }
/*     */   
/*     */   protected void hitBlockEnchantmentEffects(ServerLevel serverLevel, BlockHitResult hitResult, ItemStack weapon) {
/* 563 */     Vec3 compensatedHitPosition = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
/* 564 */     Entity entity = getOwner(); LivingEntity livingOwner = (LivingEntity)entity; EnchantmentHelper.onHitBlock(serverLevel, weapon, (entity instanceof LivingEntity) ? livingOwner : null, (Entity)this, null, compensatedHitPosition, serverLevel.getBlockState(hitResult.getBlockPos()), item -> this.firedFromWeapon = null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getWeaponItem() {
/* 569 */     return this.firedFromWeapon;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 573 */     return SoundEvents.ARROW_HIT;
/*     */   }
/*     */   
/*     */   protected final SoundEvent getHitGroundSoundEvent() {
/* 577 */     return this.soundEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity mob) {}
/*     */   
/*     */   protected EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
/* 584 */     return ProjectileUtil.getEntityHitResult(level(), this, from, to, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), this::canHitEntity);
/*     */   }
/*     */   
/*     */   protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
/* 588 */     return ProjectileUtil.getManyEntityHitResult(level(), (Entity)this, from, to, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), this::canHitEntity, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/* 593 */     if (entity instanceof Player) { Entity entity1 = getOwner(); if (entity1 instanceof Player) { Player player = (Player)entity1; if (!player.canHarmPlayer((Player)entity))
/* 594 */           return false;  }
/*     */        }
/* 596 */      return (super.canHitEntity(entity) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(entity.getId())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 601 */     super.addAdditionalSaveData(output);
/*     */     
/* 603 */     output.putShort("life", (short)this.life);
/* 604 */     output.storeNullable("inBlockState", BlockState.CODEC, this.lastState);
/* 605 */     output.putByte("shake", (byte)this.shakeTime);
/* 606 */     output.putBoolean("inGround", isInGround());
/* 607 */     output.store("pickup", Pickup.LEGACY_CODEC, this.pickup);
/* 608 */     output.putDouble("damage", this.baseDamage);
/* 609 */     output.putBoolean("crit", isCritArrow());
/* 610 */     output.putByte("PierceLevel", getPierceLevel());
/* 611 */     output.store("SoundEvent", BuiltInRegistries.SOUND_EVENT.byNameCodec(), this.soundEvent);
/* 612 */     output.store("item", ItemStack.CODEC, this.pickupItemStack);
/* 613 */     output.storeNullable("weapon", ItemStack.CODEC, this.firedFromWeapon);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 618 */     super.readAdditionalSaveData(input);
/*     */     
/* 620 */     this.life = input.getShortOr("life", (short)0);
/* 621 */     this.lastState = input.read("inBlockState", BlockState.CODEC).orElse(null);
/* 622 */     this.shakeTime = input.getByteOr("shake", (byte)0) & 0xFF;
/* 623 */     setInGround(input.getBooleanOr("inGround", false));
/* 624 */     this.baseDamage = input.getDoubleOr("damage", 2.0D);
/* 625 */     this.pickup = input.read("pickup", Pickup.LEGACY_CODEC).orElse(Pickup.DISALLOWED);
/*     */     
/* 627 */     setCritArrow(input.getBooleanOr("crit", false));
/* 628 */     setPierceLevel(input.getByteOr("PierceLevel", (byte)0));
/*     */     
/* 630 */     this.soundEvent = input.read("SoundEvent", BuiltInRegistries.SOUND_EVENT.byNameCodec()).orElse(getDefaultHitGroundSoundEvent());
/*     */     
/* 632 */     setPickupItemStack(input.read("item", ItemStack.CODEC).orElse(getDefaultPickupItem()));
/* 633 */     this.firedFromWeapon = input.read("weapon", ItemStack.CODEC).orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwner(Entity owner) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial setOwner : (Lnet/minecraft/world/entity/Entity;)V
/*     */     //   5: aload_0
/*     */     //   6: aload_1
/*     */     //   7: astore_2
/*     */     //   8: iconst_0
/*     */     //   9: istore_3
/*     */     //   10: aload_2
/*     */     //   11: iload_3
/*     */     //   12: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   17: tableswitch default -> 83, -1 -> 83, 0 -> 44, 1 -> 71
/*     */     //   44: aload_2
/*     */     //   45: checkcast net/minecraft/world/entity/player/Player
/*     */     //   48: astore #4
/*     */     //   50: aload_0
/*     */     //   51: getfield pickup : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   54: getstatic net/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup.DISALLOWED : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   57: if_acmpeq -> 65
/*     */     //   60: iconst_1
/*     */     //   61: istore_3
/*     */     //   62: goto -> 10
/*     */     //   65: getstatic net/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup.ALLOWED : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   68: goto -> 90
/*     */     //   71: aload_2
/*     */     //   72: checkcast net/minecraft/world/entity/OminousItemSpawner
/*     */     //   75: astore #5
/*     */     //   77: getstatic net/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup.DISALLOWED : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   80: goto -> 90
/*     */     //   83: aload_0
/*     */     //   84: getfield pickup : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   87: goto -> 90
/*     */     //   90: putfield pickup : Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow$Pickup;
/*     */     //   93: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #638	-> 0
/*     */     //   #640	-> 5
/*     */     //   #641	-> 44
/*     */     //   #642	-> 71
/*     */     //   #643	-> 83
/*     */     //   #645	-> 93
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   50	21	4	ignored	Lnet/minecraft/world/entity/player/Player;
/*     */     //   77	6	5	ignored	Lnet/minecraft/world/entity/OminousItemSpawner;
/*     */     //   0	94	0	this	Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;
/*     */     //   0	94	1	owner	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void playerTouch(Player player) {
/* 649 */     if (level().isClientSide() || (!isInGround() && !isNoPhysics()) || this.shakeTime > 0) {
/*     */       return;
/*     */     }
/*     */     
/* 653 */     if (tryPickup(player)) {
/* 654 */       player.take((Entity)this, 1);
/* 655 */       discard();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean tryPickup(Player player) {
/* 661 */     switch (this.pickup.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*     */ 
/*     */       
/* 664 */       player.hasInfiniteMaterials();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getPickupItem() {
/* 669 */     return this.pickupItemStack.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 676 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   public ItemStack getPickupItemStackOrigin() {
/* 680 */     return this.pickupItemStack;
/*     */   }
/*     */   
/*     */   public void setBaseDamage(double baseDamage) {
/* 684 */     this.baseDamage = baseDamage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 689 */     return getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE);
/*     */   }
/*     */   
/*     */   public void setCritArrow(boolean critArrow) {
/* 693 */     setFlag(1, critArrow);
/*     */   }
/*     */   
/*     */   private void setPierceLevel(byte pieceLevel) {
/* 697 */     this.entityData.set(PIERCE_LEVEL, pieceLevel);
/*     */   }
/*     */   
/*     */   private void setFlag(int flag, boolean value) {
/* 701 */     byte flags = (Byte)this.entityData.get(ID_FLAGS);
/* 702 */     if (value) {
/* 703 */       this.entityData.set(ID_FLAGS, (byte)(flags | flag));
/*     */     } else {
/* 705 */       this.entityData.set(ID_FLAGS, (byte)(flags & (flag ^ 0xFFFFFFFF)));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setPickupItemStack(ItemStack itemStack) {
/* 710 */     if (!itemStack.isEmpty()) {
/* 711 */       this.pickupItemStack = itemStack;
/*     */     } else {
/* 713 */       this.pickupItemStack = getDefaultPickupItem();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCritArrow() {
/* 718 */     byte flags = (Byte)this.entityData.get(ID_FLAGS);
/* 719 */     return ((flags & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public byte getPierceLevel() {
/* 723 */     return (Byte)this.entityData.get(PIERCE_LEVEL);
/*     */   }
/*     */   
/*     */   public void setBaseDamageFromMob(float power) {
/* 727 */     setBaseDamage((power * 2.0F) + this.random.triangle(level().getDifficulty().getId() * 0.11D, 0.57425D));
/*     */   }
/*     */   
/*     */   protected float getWaterInertia() {
/* 731 */     return 0.6F;
/*     */   }
/*     */   
/*     */   public void setNoPhysics(boolean noPhysics) {
/* 735 */     this.noPhysics = noPhysics;
/* 736 */     setFlag(2, noPhysics);
/*     */   }
/*     */   
/*     */   public boolean isNoPhysics() {
/* 740 */     if (!level().isClientSide()) {
/* 741 */       return this.noPhysics;
/*     */     }
/* 743 */     return (((Byte)this.entityData.get(ID_FLAGS) & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 748 */     return (super.isPickable() && !isInGround());
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 753 */     if (slot == 0) {
/* 754 */       return SlotAccess.of(this::getPickupItemStackOrigin, this::setPickupItemStack);
/*     */     }
/* 756 */     return super.getSlot(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldBounceOnWorldBorder() {
/* 761 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract ItemStack getDefaultPickupItem();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/arrow/AbstractArrow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */