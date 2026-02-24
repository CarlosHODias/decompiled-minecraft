/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class BlindnessFogEnvironment extends MobEffectFogEnvironment {
/*    */   public Holder<MobEffect> getMobEffect() {
/* 17 */     return MobEffects.BLINDNESS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/* 22 */     Entity entity = camera.entity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 23 */       MobEffectInstance effect = livingEntity.getEffect(getMobEffect());
/* 24 */       if (effect != null) {
/* 25 */         float distance = effect.isInfiniteDuration() ? 5.0F : Mth.lerp(Math.min(1.0F, effect.getDuration() / 20.0F), renderDistance, 5.0F);
/* 26 */         fog.environmentalStart = distance * 0.25F;
/* 27 */         fog.environmentalEnd = distance;
/* 28 */         fog.skyEnd = distance * 0.8F;
/* 29 */         fog.cloudEnd = distance * 0.8F;
/*    */       }  }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
/* 36 */     MobEffectInstance instance = entity.getEffect(getMobEffect());
/* 37 */     if (instance != null) {
/* 38 */       if (instance.endsWithin(19)) {
/* 39 */         darkness = Math.max(instance.getDuration() / 20.0F, darkness);
/*    */       } else {
/* 41 */         darkness = 1.0F;
/*    */       } 
/*    */     }
/* 44 */     return darkness;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/environment/BlindnessFogEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */