/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RegenerationMobEffect extends MobEffect {
/*    */   protected RegenerationMobEffect(MobEffectCategory category, int color) {
/*  8 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 13 */     if (mob.getHealth() < mob.getMaxHealth()) {
/* 14 */       mob.heal(1.0F);
/*    */     }
/* 16 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 21 */     int interval = 50 >> amplification;
/* 22 */     if (interval > 0) {
/* 23 */       return (tickCount % interval == 0);
/*    */     }
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/RegenerationMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */