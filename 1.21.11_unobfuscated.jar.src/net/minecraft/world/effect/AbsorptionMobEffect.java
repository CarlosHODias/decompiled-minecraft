/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class AbsorptionMobEffect extends MobEffect {
/*    */   protected AbsorptionMobEffect(MobEffectCategory category, int color) {
/*  8 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 13 */     return (mob.getAbsorptionAmount() > 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEffectStarted(LivingEntity mob, int amplifier) {
/* 23 */     super.onEffectStarted(mob, amplifier);
/* 24 */     mob.setAbsorptionAmount(Math.max(mob.getAbsorptionAmount(), (4 * (1 + amplifier))));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/AbsorptionMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */