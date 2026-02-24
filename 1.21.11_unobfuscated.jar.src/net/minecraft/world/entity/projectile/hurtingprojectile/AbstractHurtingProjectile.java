/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractHurtingProjectile
/*     */   extends Projectile {
/*     */   public static final double INITAL_ACCELERATION_POWER = 0.1D;
/*     */   public static final double DEFLECTION_SCALE = 0.5D;
/*  24 */   public double accelerationPower = 0.1D;
/*     */   
/*     */   protected AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
/*  27 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> type, double x, double y, double z, Level level) {
/*  31 */     this(type, level);
/*  32 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   public AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> type, double x, double y, double z, Vec3 direction, Level level) {
/*  36 */     this(type, level);
/*  37 */     snapTo(x, y, z, getYRot(), getXRot());
/*  38 */     reapplyPosition();
/*  39 */     assignDirectionalMovement(direction, this.accelerationPower);
/*     */   }
/*     */   
/*     */   public AbstractHurtingProjectile(EntityType<? extends AbstractHurtingProjectile> type, LivingEntity mob, Vec3 direction, Level level) {
/*  43 */     this(type, mob.getX(), mob.getY(), mob.getZ(), direction, level);
/*  44 */     setOwner((Entity)mob);
/*  45 */     setRot(mob.getYRot(), mob.getXRot());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/*  54 */     double size = getBoundingBox().getSize() * 4.0D;
/*  55 */     if (Double.isNaN(size)) {
/*  56 */       size = 4.0D;
/*     */     }
/*  58 */     size *= 64.0D;
/*  59 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   protected ClipContext.Block getClipType() {
/*  63 */     return ClipContext.Block.COLLIDER;
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     Vec3 newPosition;
/*  68 */     Entity owner = getOwner();
/*     */     
/*  70 */     applyInertia();
/*     */     
/*  72 */     if (!level().isClientSide() && ((owner != null && owner.isRemoved()) || !level().hasChunkAt(blockPosition()))) {
/*  73 */       discard();
/*     */       
/*     */       return;
/*     */     } 
/*  77 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector((Entity)this, this::canHitEntity, getClipType());
/*     */ 
/*     */     
/*  80 */     if (hitResult.getType() != HitResult.Type.MISS) {
/*  81 */       newPosition = hitResult.getLocation();
/*     */     } else {
/*  83 */       newPosition = position().add(getDeltaMovement());
/*     */     } 
/*     */     
/*  86 */     ProjectileUtil.rotateTowardsMovement((Entity)this, 0.2F);
/*  87 */     setPos(newPosition);
/*     */     
/*  89 */     applyEffectsFromBlocks();
/*  90 */     super.tick();
/*     */     
/*  92 */     if (shouldBurn()) {
/*  93 */       igniteForSeconds(1.0F);
/*     */     }
/*     */     
/*  96 */     if (hitResult.getType() != HitResult.Type.MISS && isAlive())
/*     */     {
/*     */       
/*  99 */       hitTargetOrDeflectSelf(hitResult);
/*     */     }
/*     */     
/* 102 */     createParticleTrail();
/*     */   }
/*     */   private void applyInertia() {
/*     */     float inertia;
/* 106 */     Vec3 movement = getDeltaMovement();
/* 107 */     Vec3 position = position();
/*     */     
/* 109 */     if (isInWater()) {
/* 110 */       for (int i = 0; i < 4; i++) {
/* 111 */         float s = 0.25F;
/* 112 */         level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, position.x - movement.x * 0.25D, position.y - movement.y * 0.25D, position.z - movement.z * 0.25D, movement.x, movement.y, movement.z);
/*     */       } 
/* 114 */       inertia = getLiquidInertia();
/*     */     } else {
/* 116 */       inertia = getInertia();
/*     */     } 
/*     */     
/* 119 */     setDeltaMovement(movement.add(movement.normalize().scale(this.accelerationPower)).scale(inertia));
/*     */   }
/*     */   
/*     */   private void createParticleTrail() {
/* 123 */     ParticleOptions trailParticle = getTrailParticle();
/* 124 */     Vec3 position = position();
/* 125 */     if (trailParticle != null) {
/* 126 */       level().addParticle(trailParticle, position.x, position.y + 0.5D, position.z, 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/* 137 */     return (super.canHitEntity(entity) && !entity.noPhysics);
/*     */   }
/*     */   
/*     */   protected boolean shouldBurn() {
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   protected ParticleOptions getTrailParticle() {
/* 145 */     return (ParticleOptions)ParticleTypes.SMOKE;
/*     */   }
/*     */   
/*     */   protected float getInertia() {
/* 149 */     return 0.95F;
/*     */   }
/*     */   
/*     */   protected float getLiquidInertia() {
/* 153 */     return 0.8F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 158 */     super.addAdditionalSaveData(output);
/* 159 */     output.putDouble("acceleration_power", this.accelerationPower);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 164 */     super.readAdditionalSaveData(input);
/* 165 */     this.accelerationPower = input.getDoubleOr("acceleration_power", 0.1D);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 170 */     return 1.0F;
/*     */   }
/*     */   
/*     */   private void assignDirectionalMovement(Vec3 direction, double speed) {
/* 174 */     setDeltaMovement(direction.normalize().scale(speed));
/* 175 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDeflection(boolean byAttack) {
/* 180 */     super.onDeflection(byAttack);
/* 181 */     if (byAttack) {
/* 182 */       this.accelerationPower = 0.1D;
/*     */     } else {
/* 184 */       this.accelerationPower *= 0.5D;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */