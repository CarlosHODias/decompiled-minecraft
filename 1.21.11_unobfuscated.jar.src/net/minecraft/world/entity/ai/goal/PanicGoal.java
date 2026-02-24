/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class PanicGoal
/*     */   extends Goal
/*     */ {
/*     */   public static final int WATER_CHECK_DISTANCE_VERTICAL = 1;
/*     */   protected final PathfinderMob mob;
/*     */   protected final double speedModifier;
/*     */   protected double posX;
/*     */   protected double posY;
/*     */   protected double posZ;
/*     */   protected boolean isRunning;
/*     */   private final Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes;
/*     */   
/*     */   public PanicGoal(PathfinderMob mob, double speedModifier) {
/*  30 */     this(mob, speedModifier, DamageTypeTags.PANIC_CAUSES);
/*     */   }
/*     */   
/*     */   public PanicGoal(PathfinderMob mob, double speedModifier, TagKey<DamageType> panicCausingDamageTypes) {
/*  34 */     this(mob, speedModifier, entity -> panicCausingDamageTypes);
/*     */   }
/*     */   
/*     */   public PanicGoal(PathfinderMob mob, double speedModifier, Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes) {
/*  38 */     this.mob = mob;
/*  39 */     this.speedModifier = speedModifier;
/*  40 */     this.panicCausingDamageTypes = panicCausingDamageTypes;
/*  41 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  46 */     if (!shouldPanic()) {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     if (this.mob.isOnFire()) {
/*  51 */       BlockPos blockPos = lookForWater((BlockGetter)this.mob.level(), (Entity)this.mob, 5);
/*  52 */       if (blockPos != null) {
/*  53 */         this.posX = blockPos.getX();
/*  54 */         this.posY = blockPos.getY();
/*  55 */         this.posZ = blockPos.getZ();
/*     */         
/*  57 */         return true;
/*     */       } 
/*     */     } 
/*  60 */     return findRandomPosition();
/*     */   }
/*     */   
/*     */   protected boolean shouldPanic() {
/*  64 */     return (this.mob.getLastDamageSource() != null && this.mob.getLastDamageSource().is(this.panicCausingDamageTypes.apply(this.mob)));
/*     */   }
/*     */   
/*     */   protected boolean findRandomPosition() {
/*  68 */     Vec3 pos = DefaultRandomPos.getPos(this.mob, 5, 4);
/*  69 */     if (pos == null) {
/*  70 */       return false;
/*     */     }
/*  72 */     this.posX = pos.x;
/*  73 */     this.posY = pos.y;
/*  74 */     this.posZ = pos.z;
/*     */     
/*  76 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/*  80 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  85 */     this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
/*  86 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  91 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  96 */     return !this.mob.getNavigation().isDone();
/*     */   }
/*     */   
/*     */   protected BlockPos lookForWater(BlockGetter level, Entity mob, int xzDist) {
/* 100 */     BlockPos mobPosition = mob.blockPosition();
/* 101 */     if (!level.getBlockState(mobPosition).getCollisionShape(level, mobPosition).isEmpty()) {
/* 102 */       return null;
/*     */     }
/* 104 */     return BlockPos.findClosestMatch(mob.blockPosition(), xzDist, 1, pos -> level.getFluidState(pos).is(FluidTags.WATER)).orElse(null);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/PanicGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */