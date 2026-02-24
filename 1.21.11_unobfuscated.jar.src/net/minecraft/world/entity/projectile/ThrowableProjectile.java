/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class ThrowableProjectile
/*     */   extends Projectile {
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> type, Level level) {
/*  17 */     super((EntityType)type, level);
/*     */   }
/*     */   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
/*     */   protected ThrowableProjectile(EntityType<? extends ThrowableProjectile> type, double x, double y, double z, Level level) {
/*  21 */     this(type, level);
/*  22 */     setPos(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/*  27 */     if (this.tickCount < 2 && distance < 12.25D) {
/*  28 */       return false;
/*     */     }
/*  30 */     double size = getBoundingBox().getSize() * 4.0D;
/*  31 */     if (Double.isNaN(size)) {
/*  32 */       size = 4.0D;
/*     */     }
/*  34 */     size *= 64.0D;
/*  35 */     return (distance < size * size);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/*  40 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     Vec3 newPosition;
/*  45 */     handleFirstTickBubbleColumn();
/*     */     
/*  47 */     applyGravity();
/*  48 */     applyInertia();
/*     */     
/*  50 */     HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */ 
/*     */     
/*  53 */     if (result.getType() != HitResult.Type.MISS) {
/*  54 */       newPosition = result.getLocation();
/*     */     } else {
/*  56 */       newPosition = position().add(getDeltaMovement());
/*     */     } 
/*     */     
/*  59 */     setPos(newPosition);
/*  60 */     updateRotation();
/*  61 */     applyEffectsFromBlocks();
/*  62 */     super.tick();
/*     */     
/*  64 */     if (result.getType() != HitResult.Type.MISS && isAlive())
/*     */     {
/*     */       
/*  67 */       hitTargetOrDeflectSelf(result); } 
/*     */   }
/*     */   
/*     */   private void applyInertia() {
/*     */     float inertia;
/*  72 */     Vec3 movement = getDeltaMovement();
/*  73 */     Vec3 position = position();
/*     */     
/*  75 */     if (isInWater()) {
/*  76 */       for (int i = 0; i < 4; i++) {
/*  77 */         float s = 0.25F;
/*  78 */         level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, position.x - movement.x * 0.25D, position.y - movement.y * 0.25D, position.z - movement.z * 0.25D, movement.x, movement.y, movement.z);
/*     */       } 
/*  80 */       inertia = 0.8F;
/*     */     } else {
/*  82 */       inertia = 0.99F;
/*     */     } 
/*     */     
/*  85 */     setDeltaMovement(movement.scale(inertia));
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleFirstTickBubbleColumn() {
/*  90 */     if (this.firstTick) {
/*  91 */       for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(getBoundingBox())) {
/*  92 */         BlockState state = level().getBlockState(pos);
/*  93 */         if (state.is(Blocks.BUBBLE_COLUMN)) {
/*  94 */           state.entityInside(level(), pos, this, InsideBlockEffectApplier.NOOP, true);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/* 102 */     return 0.03D;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/ThrowableProjectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */