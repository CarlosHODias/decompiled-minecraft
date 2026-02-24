/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ 
/*    */ public class PowderedSnowFogEnvironment
/*    */   extends FogEnvironment
/*    */ {
/*    */   private static final int COLOR = -6308916;
/*    */   
/*    */   public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
/* 16 */     return -6308916;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/* 21 */     if (camera.entity().isSpectator()) {
/* 22 */       fog.environmentalStart = -8.0F;
/* 23 */       fog.environmentalEnd = renderDistance * 0.5F;
/*    */     } else {
/* 25 */       fog.environmentalStart = 0.0F;
/* 26 */       fog.environmentalEnd = 2.0F;
/*    */     } 
/* 28 */     fog.skyEnd = fog.environmentalEnd;
/* 29 */     fog.cloudEnd = fog.environmentalEnd;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 34 */     return (fogType == FogType.POWDER_SNOW);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/environment/PowderedSnowFogEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */