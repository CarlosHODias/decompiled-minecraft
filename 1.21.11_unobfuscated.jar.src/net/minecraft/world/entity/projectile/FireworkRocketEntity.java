/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FireworkRocketEntity
/*     */   extends Projectile implements ItemSupplier {
/*  41 */   private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.ITEM_STACK);
/*  42 */   private static final EntityDataAccessor<OptionalInt> DATA_ATTACHED_TO_TARGET = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*  43 */   private static final EntityDataAccessor<Boolean> DATA_SHOT_AT_ANGLE = SynchedEntityData.defineId(FireworkRocketEntity.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int DEFAULT_LIFE = 0;
/*     */   private static final int DEFAULT_LIFE_TIME = 0;
/*     */   private static final boolean DEFAULT_SHOT_AT_ANGLE = false;
/*  48 */   private int life = 0;
/*  49 */   private int lifetime = 0;
/*     */   private LivingEntity attachedToEntity;
/*     */   
/*     */   public FireworkRocketEntity(EntityType<? extends FireworkRocketEntity> type, Level level) {
/*  53 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, double x, double y, double z, ItemStack sourceItemStack) {
/*  57 */     super(EntityType.FIREWORK_ROCKET, level);
/*  58 */     this.life = 0;
/*     */     
/*  60 */     setPos(x, y, z);
/*     */     
/*  62 */     this.entityData.set(DATA_ID_FIREWORKS_ITEM, sourceItemStack.copy());
/*  63 */     int flightCount = 1;
/*  64 */     Fireworks fireworks = (Fireworks)sourceItemStack.get(DataComponents.FIREWORKS);
/*  65 */     if (fireworks != null) {
/*  66 */       flightCount += fireworks.flightDuration();
/*     */     }
/*  68 */     setDeltaMovement(
/*  69 */         this.random.triangle(0.0D, 0.002297D), 0.05D, 
/*     */         
/*  71 */         this.random.triangle(0.0D, 0.002297D));
/*     */ 
/*     */     
/*  74 */     this.lifetime = 10 * flightCount + this.random.nextInt(6) + this.random.nextInt(7);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, Entity owner, double x, double y, double z, ItemStack sourceItemStack) {
/*  78 */     this(level, x, y, z, sourceItemStack);
/*  79 */     setOwner(owner);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, ItemStack sourceItemStack, LivingEntity stuckTo) {
/*  83 */     this(level, (Entity)stuckTo, stuckTo.getX(), stuckTo.getY(), stuckTo.getZ(), sourceItemStack);
/*  84 */     this.entityData.set(DATA_ATTACHED_TO_TARGET, OptionalInt.of(stuckTo.getId()));
/*  85 */     this.attachedToEntity = stuckTo;
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, ItemStack sourceItemStack, double x, double y, double z, boolean shotAtAngle) {
/*  89 */     this(level, x, y, z, sourceItemStack);
/*  90 */     this.entityData.set(DATA_SHOT_AT_ANGLE, shotAtAngle);
/*     */   }
/*     */   
/*     */   public FireworkRocketEntity(Level level, ItemStack sourceItemStack, Entity owner, double x, double y, double z, boolean shotAtAngle) {
/*  94 */     this(level, sourceItemStack, x, y, z, shotAtAngle);
/*  95 */     setOwner(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 100 */     entityData.define(DATA_ID_FIREWORKS_ITEM, getDefaultItem());
/* 101 */     entityData.define(DATA_ATTACHED_TO_TARGET, OptionalInt.empty());
/* 102 */     entityData.define(DATA_SHOT_AT_ANGLE, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 107 */     return (distance < 4096.0D && !isAttachedToEntity());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(double camX, double camY, double camZ) {
/* 112 */     return (super.shouldRender(camX, camY, camZ) && !isAttachedToEntity());
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     HitResult hitResult;
/* 117 */     super.tick();
/*     */ 
/*     */     
/* 120 */     if (isAttachedToEntity()) {
/* 121 */       if (this.attachedToEntity == null) {
/* 122 */         ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).ifPresent(id -> {
/*     */               Entity ent = level().getEntity(id);
/*     */               if (ent instanceof LivingEntity) {
/*     */                 this.attachedToEntity = (LivingEntity)ent;
/*     */               }
/*     */             });
/*     */       }
/* 129 */       if (this.attachedToEntity != null) {
/*     */         Vec3 handAngle;
/* 131 */         if (this.attachedToEntity.isFallFlying()) {
/* 132 */           Vec3 lookAngle = this.attachedToEntity.getLookAngle();
/* 133 */           double power = 1.5D;
/* 134 */           double powerAdd = 0.1D;
/*     */           
/* 136 */           Vec3 movement = this.attachedToEntity.getDeltaMovement();
/* 137 */           this.attachedToEntity.setDeltaMovement(movement.add(lookAngle.x * 0.1D + (lookAngle.x * 1.5D - movement.x) * 0.5D, lookAngle.y * 0.1D + (lookAngle.y * 1.5D - movement.y) * 0.5D, lookAngle.z * 0.1D + (lookAngle.z * 1.5D - movement.z) * 0.5D));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           handAngle = this.attachedToEntity.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
/*     */         } else {
/* 144 */           handAngle = Vec3.ZERO;
/*     */         } 
/* 146 */         setPos(this.attachedToEntity.getX() + handAngle.x, this.attachedToEntity.getY() + handAngle.y, this.attachedToEntity.getZ() + handAngle.z);
/*     */         
/* 148 */         setDeltaMovement(this.attachedToEntity.getDeltaMovement());
/*     */       } 
/* 150 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */     } else {
/* 152 */       if (!isShotAtAngle()) {
/*     */         
/* 154 */         double horizontalAcceleration = this.horizontalCollision ? 1.0D : 1.15D;
/* 155 */         setDeltaMovement(getDeltaMovement().multiply(horizontalAcceleration, 1.0D, horizontalAcceleration).add(0.0D, 0.04D, 0.0D));
/*     */       } 
/* 157 */       Vec3 movement = getDeltaMovement();
/* 158 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */       
/* 160 */       move(MoverType.SELF, movement);
/* 161 */       applyEffectsFromBlocks();
/* 162 */       setDeltaMovement(movement);
/*     */     } 
/*     */     
/* 165 */     if (!this.noPhysics && isAlive() && hitResult.getType() != HitResult.Type.MISS) {
/* 166 */       hitTargetOrDeflectSelf(hitResult);
/* 167 */       this.needsSync = true;
/*     */     } 
/*     */     
/* 170 */     updateRotation();
/*     */     
/* 172 */     if (this.life == 0 && !isSilent()) {
/* 173 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
/*     */     }
/*     */     
/* 176 */     this.life++;
/* 177 */     if (level().isClientSide() && this.life % 2 < 2) {
/* 178 */       level().addParticle((ParticleOptions)ParticleTypes.FIREWORK, getX(), getY(), getZ(), this.random.nextGaussian() * 0.05D, -(getDeltaMovement()).y * 0.5D, this.random.nextGaussian() * 0.05D);
/*     */     }
/* 180 */     if (this.life > this.lifetime) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 181 */         explode(serverLevel); }
/*     */        }
/*     */   
/*     */   }
/*     */   private void explode(ServerLevel level) {
/* 186 */     level.broadcastEntityEvent(this, (byte)17);
/* 187 */     gameEvent((Holder)GameEvent.EXPLODE, getOwner());
/* 188 */     dealExplosionDamage(level);
/* 189 */     discard();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 194 */     super.onHitEntity(hitResult);
/* 195 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 196 */       explode(serverLevel); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 202 */     BlockPos pos = new BlockPos((Vec3i)hitResult.getBlockPos());
/* 203 */     level().getBlockState(pos).entityInside(level(), pos, this, InsideBlockEffectApplier.NOOP, true);
/* 204 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (hasExplosion())
/* 205 */         explode(serverLevel);  }
/*     */     
/* 207 */     super.onHitBlock(hitResult);
/*     */   }
/*     */   
/*     */   private boolean hasExplosion() {
/* 211 */     return !getExplosions().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealExplosionDamage(ServerLevel level) {
/* 216 */     float damageAmount = 0.0F;
/* 217 */     List<FireworkExplosion> explosions = getExplosions();
/* 218 */     if (!explosions.isEmpty()) {
/* 219 */       damageAmount = 5.0F + (explosions.size() * 2);
/*     */     }
/* 221 */     if (damageAmount > 0.0F) {
/* 222 */       if (this.attachedToEntity != null) {
/* 223 */         this.attachedToEntity.hurtServer(level, damageSources().fireworks(this, getOwner()), 5.0F + (explosions.size() * 2));
/*     */       }
/*     */       
/* 226 */       double radius = 5.0D;
/* 227 */       Vec3 rocketPos = position();
/* 228 */       List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(5.0D));
/* 229 */       for (LivingEntity target : targets) {
/* 230 */         if (target == this.attachedToEntity) {
/*     */           continue;
/*     */         }
/* 233 */         if (distanceToSqr((Entity)target) > 25.0D) {
/*     */           continue;
/*     */         }
/*     */         
/*     */         boolean canSee = false;
/* 238 */         for (int testStep = 0; testStep < 2; testStep++) {
/* 239 */           Vec3 to = new Vec3(target.getX(), target.getY(0.5D * testStep), target.getZ());
/* 240 */           BlockHitResult blockHitResult = level().clip(new ClipContext(rocketPos, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
/* 241 */           if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 242 */             canSee = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 246 */         if (canSee) {
/* 247 */           float damage = damageAmount * (float)Math.sqrt((5.0D - distanceTo((Entity)target)) / 5.0D);
/* 248 */           target.hurtServer(level, damageSources().fireworks(this, getOwner()), damage);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isAttachedToEntity() {
/* 255 */     return ((OptionalInt)this.entityData.get(DATA_ATTACHED_TO_TARGET)).isPresent();
/*     */   }
/*     */   
/*     */   public boolean isShotAtAngle() {
/* 259 */     return (Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 264 */     if (id == 17 && level().isClientSide()) {
/* 265 */       Vec3 movement = getDeltaMovement();
/* 266 */       level().createFireworks(getX(), getY(), getZ(), movement.x, movement.y, movement.z, getExplosions());
/*     */     } 
/* 268 */     super.handleEntityEvent(id);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 273 */     super.addAdditionalSaveData(output);
/* 274 */     output.putInt("Life", this.life);
/* 275 */     output.putInt("LifeTime", this.lifetime);
/* 276 */     output.store("FireworksItem", ItemStack.CODEC, getItem());
/* 277 */     output.putBoolean("ShotAtAngle", (Boolean)this.entityData.get(DATA_SHOT_AT_ANGLE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 282 */     super.readAdditionalSaveData(input);
/* 283 */     this.life = input.getIntOr("Life", 0);
/* 284 */     this.lifetime = input.getIntOr("LifeTime", 0);
/*     */     
/* 286 */     this.entityData.set(DATA_ID_FIREWORKS_ITEM, input.read("FireworksItem", ItemStack.CODEC).orElse(getDefaultItem()));
/*     */     
/* 288 */     this.entityData.set(DATA_SHOT_AT_ANGLE, input.getBooleanOr("ShotAtAngle", false));
/*     */   }
/*     */   
/*     */   private List<FireworkExplosion> getExplosions() {
/* 292 */     ItemStack sourceItemStack = (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/* 293 */     Fireworks fireworks = (Fireworks)sourceItemStack.get(DataComponents.FIREWORKS);
/* 294 */     return (fireworks != null) ? fireworks.explosions() : List.<FireworkExplosion>of();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/* 299 */     return (ItemStack)this.entityData.get(DATA_ID_FIREWORKS_ITEM);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 304 */     return false;
/*     */   }
/*     */   
/*     */   private static ItemStack getDefaultItem() {
/* 308 */     return new ItemStack((ItemLike)Items.FIREWORK_ROCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity hurtEntity, DamageSource damageSource) {
/* 313 */     double dx = (hurtEntity.position()).x - (position()).x;
/* 314 */     double dz = (hurtEntity.position()).z - (position()).z;
/* 315 */     return DoubleDoubleImmutablePair.of(dx, dz);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/FireworkRocketEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */