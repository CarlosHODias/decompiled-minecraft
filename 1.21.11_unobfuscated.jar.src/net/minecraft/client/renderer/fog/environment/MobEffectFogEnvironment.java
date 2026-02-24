/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ 
/*    */ public abstract class MobEffectFogEnvironment
/*    */   extends FogEnvironment
/*    */ {
/*    */   public abstract Holder<MobEffect> getMobEffect();
/*    */   
/*    */   public boolean providesColor() {
/* 15 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean modifiesDarkness() {
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 25 */     if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity; if (livingEntity.hasEffect(getMobEffect())); }  return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/environment/MobEffectFogEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */