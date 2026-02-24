/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ 
/*    */ 
/*    */ public abstract class FogEnvironment
/*    */ {
/*    */   public abstract void setupFog(FogData paramFogData, Camera paramCamera, ClientLevel paramClientLevel, float paramFloat, DeltaTracker paramDeltaTracker);
/*    */   
/*    */   public boolean providesColor() {
/* 17 */     return true;
/*    */   }
/*    */   
/*    */   public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
/* 21 */     return -1;
/*    */   }
/*    */   
/*    */   public boolean modifiesDarkness() {
/* 25 */     return false;
/*    */   }
/*    */   
/*    */   public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
/* 29 */     return darkness;
/*    */   }
/*    */   
/*    */   public abstract boolean isApplicable(FogType paramFogType, Entity paramEntity);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/environment/FogEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */