/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Parched
/*    */   extends AbstractSkeleton
/*    */ {
/*    */   public Parched(EntityType<? extends AbstractSkeleton> type, Level level) {
/* 20 */     super(type, level);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 25 */     AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
/* 26 */     if (arrow instanceof Arrow) {
/* 27 */       ((Arrow)arrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
/*    */     }
/* 29 */     return arrow;
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 33 */     return AbstractSkeleton.createAttributes()
/* 34 */       .add(Attributes.MAX_HEALTH, 16.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 39 */     return SoundEvents.PARCHED_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 44 */     return SoundEvents.PARCHED_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 49 */     return SoundEvents.PARCHED_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   SoundEvent getStepSound() {
/* 54 */     return SoundEvents.PARCHED_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getHardAttackInterval() {
/* 59 */     return 50;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAttackInterval() {
/* 64 */     return 70;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 69 */     if (newEffect.getEffect() == MobEffects.WEAKNESS) {
/* 70 */       return false;
/*    */     }
/* 72 */     return super.canBeAffected(newEffect);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/skeleton/Parched.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */