/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RaidOmenMobEffect extends MobEffect {
/*    */   protected RaidOmenMobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) {
/* 11 */     super(category, color, particleOptions);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
/* 16 */     return (remainingDuration == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 21 */     if (mob instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)mob; if (!mob.isSpectator()) {
/* 22 */         BlockPos raidOmenPosition = player.getRaidOmenPosition();
/*    */         
/* 24 */         if (raidOmenPosition != null) {
/* 25 */           level.getRaids().createOrExtendRaid(player, raidOmenPosition);
/* 26 */           player.clearRaidOmenPosition();
/* 27 */           return false;
/*    */         } 
/*    */       }  }
/* 30 */      return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/RaidOmenMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */