/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ class SaturationMobEffect
/*    */   extends InstantenousMobEffect {
/*    */   protected SaturationMobEffect(MobEffectCategory category, int color) {
/* 10 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 15 */     if (mob instanceof Player) { Player player = (Player)mob;
/* 16 */       player.getFoodData().eat(amplification + 1, 1.0F); }
/*    */     
/* 18 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/SaturationMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */