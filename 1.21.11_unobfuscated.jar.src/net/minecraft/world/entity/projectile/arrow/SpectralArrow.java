/*    */ package net.minecraft.world.entity.projectile.arrow;
/*    */ 
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SpellParticleOption;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class SpectralArrow
/*    */   extends AbstractArrow
/*    */ {
/*    */   private static final int DEFAULT_DURATION = 200;
/* 21 */   private int duration = 200;
/*    */   
/*    */   public SpectralArrow(EntityType<? extends SpectralArrow> type, Level level) {
/* 24 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 28 */     super(EntityType.SPECTRAL_ARROW, owner, level, pickupItemStack, firedFromWeapon);
/*    */   }
/*    */   
/*    */   public SpectralArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/* 32 */     super(EntityType.SPECTRAL_ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 37 */     super.tick();
/*    */     
/* 39 */     if (level().isClientSide() && !isInGround()) {
/* 40 */       level().addParticle((ParticleOptions)SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F), getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doPostHurtEffects(LivingEntity mob) {
/* 46 */     super.doPostHurtEffects(mob);
/*    */     
/* 48 */     MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
/* 49 */     mob.addEffect(effect, getEffectSource());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 54 */     super.readAdditionalSaveData(input);
/* 55 */     this.duration = input.getIntOr("Duration", 200);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 60 */     super.addAdditionalSaveData(output);
/* 61 */     output.putInt("Duration", this.duration);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getDefaultPickupItem() {
/* 66 */     return new ItemStack((ItemLike)Items.SPECTRAL_ARROW);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/projectile/arrow/SpectralArrow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */