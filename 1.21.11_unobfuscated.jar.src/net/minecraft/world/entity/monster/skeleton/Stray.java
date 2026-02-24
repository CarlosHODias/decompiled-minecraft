/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.Monster;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class Stray
/*    */   extends AbstractSkeleton
/*    */ {
/*    */   public Stray(EntityType<? extends Stray> type, Level level) {
/* 24 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public static boolean checkStraySpawnRules(EntityType<Stray> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 28 */     BlockPos checkSkyPos = pos;
/*    */     while (true) {
/* 30 */       checkSkyPos = checkSkyPos.above();
/* 31 */       if (!level.getBlockState(checkSkyPos).is(Blocks.POWDER_SNOW))
/* 32 */         return (Monster.checkMonsterSpawnRules(type, level, spawnReason, pos, random) && (
/* 33 */           EntitySpawnReason.isSpawner(spawnReason) || level.canSeeSky(checkSkyPos.below()))); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 38 */     return SoundEvents.STRAY_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 43 */     return SoundEvents.STRAY_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 48 */     return SoundEvents.STRAY_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   SoundEvent getStepSound() {
/* 53 */     return SoundEvents.STRAY_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 58 */     AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
/* 59 */     if (arrow instanceof Arrow) {
/* 60 */       ((Arrow)arrow).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600));
/*    */     }
/* 62 */     return arrow;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/skeleton/Stray.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */