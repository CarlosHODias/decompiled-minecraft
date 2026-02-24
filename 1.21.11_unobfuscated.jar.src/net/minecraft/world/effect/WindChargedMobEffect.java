/*    */ package net.minecraft.world.effect;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class WindChargedMobEffect extends MobEffect {
/*    */   protected WindChargedMobEffect(MobEffectCategory category, int color) {
/* 14 */     super(category, color, (ParticleOptions)ParticleTypes.SMALL_GUST);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 19 */     if (reason == Entity.RemovalReason.KILLED) {
/* 20 */       double x = mob.getX();
/* 21 */       double y = mob.getY() + (mob.getBbHeight() / 2.0F);
/* 22 */       double z = mob.getZ();
/* 23 */       float gustStrength = 3.0F + mob.getRandom().nextFloat() * 2.0F;
/* 24 */       level.explode((Entity)mob, null, net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR, x, y, z, gustStrength, false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), (Holder)SoundEvents.BREEZE_WIND_CHARGE_BURST);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/WindChargedMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */