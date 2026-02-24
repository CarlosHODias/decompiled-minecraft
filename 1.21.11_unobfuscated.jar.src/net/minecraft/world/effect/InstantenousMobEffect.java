/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ public class InstantenousMobEffect extends MobEffect {
/*    */   public InstantenousMobEffect(MobEffectCategory category, int color) {
/*  5 */     super(category, color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInstantenous() {
/* 10 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
/* 15 */     return (remainingDuration >= 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/InstantenousMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */