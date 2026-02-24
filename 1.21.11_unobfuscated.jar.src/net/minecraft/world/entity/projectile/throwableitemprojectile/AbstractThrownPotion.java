/*     */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.AbstractCandleBlock;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public abstract class AbstractThrownPotion extends ThrowableItemProjectile {
/*     */   public static final double SPLASH_RANGE = 4.0D;
/*     */   
/*     */   static {
/*  31 */     WATER_SENSITIVE_OR_ON_FIRE = (livingEntity -> 
/*  32 */       (livingEntity.isSensitiveToWater() || livingEntity.isOnFire()));
/*     */   } protected static final double SPLASH_RANGE_SQ = 16.0D; public static final Predicate<LivingEntity> WATER_SENSITIVE_OR_ON_FIRE;
/*     */   public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> type, Level level) {
/*  35 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> type, Level level, LivingEntity owner, ItemStack itemStack) {
/*  39 */     super((EntityType)type, owner, level, itemStack);
/*     */   }
/*     */   
/*     */   public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> type, Level level, double x, double y, double z, ItemStack itemStack) {
/*  43 */     super((EntityType)type, x, y, z, level, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/*  48 */     return 0.05D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/*  53 */     super.onHitBlock(hitResult);
/*  54 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/*  57 */     ItemStack potionItemStack = getItem();
/*  58 */     Direction hitDirection = hitResult.getDirection();
/*  59 */     BlockPos blockHitPos = hitResult.getBlockPos();
/*  60 */     BlockPos blockEffectPos = blockHitPos.relative(hitDirection);
/*     */     
/*  62 */     PotionContents potion = (PotionContents)potionItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*  63 */     if (potion.is(Potions.WATER)) {
/*  64 */       dowseFire(blockEffectPos);
/*  65 */       dowseFire(blockEffectPos.relative(hitDirection.getOpposite()));
/*  66 */       for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/*  67 */         dowseFire(blockEffectPos.relative(direction));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/*     */     ServerLevel level;
/*  74 */     super.onHit(hitResult);
/*  75 */     Level level1 = level(); if (level1 instanceof ServerLevel) { level = (ServerLevel)level1; }
/*     */     else
/*     */     { return; }
/*  78 */      ItemStack potionItemStack = getItem();
/*     */     
/*  80 */     PotionContents potion = (PotionContents)potionItemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*     */     
/*  82 */     if (potion.is(Potions.WATER)) {
/*  83 */       onHitAsWater(level);
/*  84 */     } else if (potion.hasEffects()) {
/*  85 */       onHitAsPotion(level, potionItemStack, hitResult);
/*     */     } 
/*  87 */     int type = (potion.potion().isPresent() && ((net.minecraft.world.item.alchemy.Potion)((net.minecraft.core.Holder)potion.potion().get()).value()).hasInstantEffects()) ? 2007 : 2002;
/*  88 */     level.levelEvent(type, blockPosition(), potion.getColor());
/*     */     
/*  90 */     discard();
/*     */   }
/*     */   
/*     */   private void onHitAsWater(ServerLevel level) {
/*  94 */     AABB aabb = getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
/*  95 */     List<LivingEntity> burningOrWaterSensitiveEntities = level().getEntitiesOfClass(LivingEntity.class, aabb, WATER_SENSITIVE_OR_ON_FIRE);
/*  96 */     for (LivingEntity entity : burningOrWaterSensitiveEntities) {
/*  97 */       double dist = distanceToSqr((Entity)entity);
/*  98 */       if (dist < 16.0D) {
/*  99 */         if (entity.isSensitiveToWater()) {
/* 100 */           entity.hurtServer(level, damageSources().indirectMagic((Entity)this, getOwner()), 1.0F);
/*     */         }
/* 102 */         if (entity.isOnFire() && entity.isAlive()) {
/* 103 */           entity.extinguishFire();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     List<Axolotl> axolotlEntities = level().getEntitiesOfClass(Axolotl.class, aabb);
/* 109 */     for (Axolotl axolotl : axolotlEntities) {
/* 110 */       axolotl.rehydrate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dowseFire(BlockPos pos) {
/* 117 */     BlockState blockState = level().getBlockState(pos);
/* 118 */     if (blockState.is(BlockTags.FIRE)) {
/* 119 */       level().destroyBlock(pos, false, (Entity)this);
/* 120 */     } else if (AbstractCandleBlock.isLit(blockState)) {
/* 121 */       AbstractCandleBlock.extinguish(null, blockState, (LevelAccessor)level(), pos);
/* 122 */     } else if (CampfireBlock.isLitCampfire(blockState)) {
/* 123 */       level().levelEvent(null, 1009, pos, 0);
/* 124 */       CampfireBlock.dowse(getOwner(), (LevelAccessor)level(), pos, blockState);
/* 125 */       level().setBlockAndUpdate(pos, (BlockState)blockState.setValue((net.minecraft.world.level.block.state.properties.Property)CampfireBlock.LIT, false));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity hurtEntity, DamageSource damageSource) {
/* 131 */     double dx = (hurtEntity.position()).x - (position()).x;
/* 132 */     double dz = (hurtEntity.position()).z - (position()).z;
/* 133 */     return DoubleDoubleImmutablePair.of(dx, dz);
/*     */   }
/*     */   
/*     */   protected abstract void onHitAsPotion(ServerLevel paramServerLevel, ItemStack paramItemStack, HitResult paramHitResult);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/throwableitemprojectile/AbstractThrownPotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */