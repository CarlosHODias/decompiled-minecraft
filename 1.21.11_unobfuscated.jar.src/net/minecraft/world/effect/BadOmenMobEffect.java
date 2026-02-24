/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ class BadOmenMobEffect
/*    */   extends MobEffect {
/*    */   protected BadOmenMobEffect(MobEffectCategory category, int color) {
/* 12 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 22 */     if (mob instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)mob; if (!player.isSpectator() && 
/* 23 */         level.getDifficulty() != Difficulty.PEACEFUL && level.isVillage(player.blockPosition())) {
/* 24 */         Raid raid = level.getRaidAt(player.blockPosition());
/*    */         
/* 26 */         if (raid == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
/* 27 */           player.addEffect(new MobEffectInstance(MobEffects.RAID_OMEN, 600, amplification));
/* 28 */           player.setRaidOmenPosition(player.blockPosition());
/* 29 */           return false;
/*    */         } 
/*    */       }  }
/*    */     
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/BadOmenMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */