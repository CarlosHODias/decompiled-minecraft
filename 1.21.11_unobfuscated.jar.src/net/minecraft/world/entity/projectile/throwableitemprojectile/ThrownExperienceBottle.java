/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ThrownExperienceBottle
/*    */   extends ThrowableItemProjectile
/*    */ {
/*    */   public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> type, Level level) {
/* 19 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level level, LivingEntity mob, ItemStack itemStack) {
/* 23 */     super(EntityType.EXPERIENCE_BOTTLE, mob, level, itemStack);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level level, double x, double y, double z, ItemStack itemStack) {
/* 27 */     super(EntityType.EXPERIENCE_BOTTLE, x, y, z, level, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 32 */     return Items.EXPERIENCE_BOTTLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getDefaultGravity() {
/* 37 */     return 0.07D;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 42 */     super.onHit(hitResult);
/*    */     
/* 44 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 45 */       serverLevel.levelEvent(2002, blockPosition(), -13083194);
/*    */       
/* 47 */       int xpCount = 3 + serverLevel.random.nextInt(5) + serverLevel.random.nextInt(5);
/* 48 */       if (hitResult instanceof BlockHitResult) { BlockHitResult blockHitResult = (BlockHitResult)hitResult;
/* 49 */         Vec3 blockNormalHit = blockHitResult.getDirection().getUnitVec3();
/* 50 */         ExperienceOrb.awardWithDirection(serverLevel, hitResult.getLocation(), blockNormalHit, xpCount); }
/*    */       else
/* 52 */       { ExperienceOrb.awardWithDirection(serverLevel, hitResult.getLocation(), getDeltaMovement().scale(-1.0D), xpCount); }
/*    */       
/* 54 */       discard(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/throwableitemprojectile/ThrownExperienceBottle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */