/*     */ package net.minecraft.client.renderer.fog.environment;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.PanoramicScreenshotParameters;
/*     */ import net.minecraft.client.renderer.fog.FogData;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AtmosphericFogEnvironment
/*     */   extends FogEnvironment
/*     */ {
/*     */   private static final int MIN_RAIN_FOG_SKY_LIGHT = 8;
/*     */   private static final float RAIN_FOG_START_OFFSET = -160.0F;
/*     */   private static final float RAIN_FOG_END_OFFSET = -256.0F;
/*     */   private float rainFogMultiplier;
/*     */   
/*     */   public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
/*  32 */     int fogColor = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.FOG_COLOR, partialTicks);
/*     */     
/*  34 */     if (renderDistance >= 4) {
/*  35 */       float sunAngle = (Float)camera.attributeProbe().getValue(EnvironmentAttributes.SUN_ANGLE, partialTicks) * 0.017453292F;
/*  36 */       float sunX = (Mth.sin(sunAngle) > 0.0F) ? -1.0F : 1.0F;
/*  37 */       PanoramicScreenshotParameters panoramicScreenshot = (Minecraft.getInstance()).gameRenderer.getPanoramicScreenshotParameters();
/*  38 */       Vector3fc forwardVector = (panoramicScreenshot != null) ? panoramicScreenshot.forwardVector() : camera.forwardVector();
/*  39 */       float lookingAtTheSunFactor = forwardVector.dot(sunX, 0.0F, 0.0F);
/*  40 */       if (lookingAtTheSunFactor > 0.0F) {
/*  41 */         int color = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, partialTicks);
/*  42 */         float alpha = ARGB.alphaFloat(color);
/*  43 */         if (alpha > 0.0F) {
/*  44 */           fogColor = ARGB.srgbLerp(lookingAtTheSunFactor * alpha, fogColor, ARGB.opaque(color));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  49 */     int skyColor = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partialTicks);
/*  50 */     skyColor = applyWeatherDarken(skyColor, level.getRainLevel(partialTicks), level.getThunderLevel(partialTicks));
/*     */     
/*  52 */     float skyFogEnd = Math.min((Float)
/*  53 */         camera.attributeProbe().getValue(EnvironmentAttributes.SKY_FOG_END_DISTANCE, partialTicks) / 16.0F, renderDistance);
/*     */ 
/*     */     
/*  56 */     float skyColorMixFactor = Mth.clampedLerp(skyFogEnd / 32.0F, 0.25F, 1.0F);
/*  57 */     skyColorMixFactor = 1.0F - (float)Math.pow(skyColorMixFactor, 0.25D);
/*     */     
/*  59 */     fogColor = ARGB.srgbLerp(skyColorMixFactor, fogColor, skyColor);
/*     */     
/*  61 */     return fogColor;
/*     */   }
/*     */   
/*     */   private static int applyWeatherDarken(int color, float rainLevel, float thunderLevel) {
/*  65 */     if (rainLevel > 0.0F) {
/*  66 */       float rainColorModifier = 1.0F - rainLevel * 0.5F;
/*  67 */       float rainBlueColorModifier = 1.0F - rainLevel * 0.4F;
/*  68 */       color = ARGB.scaleRGB(color, rainColorModifier, rainColorModifier, rainBlueColorModifier);
/*     */     } 
/*  70 */     if (thunderLevel > 0.0F) {
/*  71 */       color = ARGB.scaleRGB(color, 1.0F - thunderLevel * 0.5F);
/*     */     }
/*  73 */     return color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/*  78 */     updateRainFogState(camera, level, deltaTracker);
/*     */     
/*  80 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/*  81 */     fog.environmentalStart = (Float)camera.attributeProbe().getValue(EnvironmentAttributes.FOG_START_DISTANCE, partialTicks);
/*  82 */     fog.environmentalEnd = (Float)camera.attributeProbe().getValue(EnvironmentAttributes.FOG_END_DISTANCE, partialTicks);
/*     */     
/*  84 */     fog.environmentalStart += -160.0F * this.rainFogMultiplier;
/*     */ 
/*     */     
/*  87 */     float minRainFogEnd = Math.min(96.0F, fog.environmentalEnd);
/*  88 */     fog.environmentalEnd = Math.max(minRainFogEnd, fog.environmentalEnd + -256.0F * this.rainFogMultiplier);
/*     */     
/*  90 */     fog.skyEnd = Math.min(renderDistance, (Float)
/*     */         
/*  92 */         camera.attributeProbe().getValue(EnvironmentAttributes.SKY_FOG_END_DISTANCE, partialTicks));
/*     */     
/*  94 */     fog.cloudEnd = Math.min((
/*  95 */         (Integer)(Minecraft.getInstance()).options.cloudRange().get() * 16), (Float)
/*  96 */         camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE, partialTicks));
/*     */ 
/*     */ 
/*     */     
/* 100 */     if ((Minecraft.getInstance()).gui.getBossOverlay().shouldCreateWorldFog()) {
/* 101 */       fog.environmentalStart = Math.min(fog.environmentalStart, 10.0F);
/* 102 */       fog.environmentalEnd = Math.min(fog.environmentalEnd, 96.0F);
/* 103 */       fog.skyEnd = fog.environmentalEnd;
/* 104 */       fog.cloudEnd = fog.environmentalEnd;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateRainFogState(Camera camera, ClientLevel level, DeltaTracker deltaTracker) {
/* 109 */     BlockPos blockPos = camera.blockPosition();
/* 110 */     Biome biome = (Biome)level.getBiome(blockPos).value();
/* 111 */     float deltaTicks = deltaTracker.getGameTimeDeltaTicks();
/* 112 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 113 */     boolean rainsInBiome = biome.hasPrecipitation();
/* 114 */     float skyLightLevelMultiplier = Mth.clamp((level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(blockPos) - 8.0F) / 7.0F, 0.0F, 1.0F);
/* 115 */     float targetRainFogMultiplier = level.getRainLevel(partialTicks) * skyLightLevelMultiplier * (rainsInBiome ? 1.0F : 0.5F);
/* 116 */     this.rainFogMultiplier += (targetRainFogMultiplier - this.rainFogMultiplier) * deltaTicks * 0.2F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 121 */     return (fogType == FogType.ATMOSPHERIC);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/fog/environment/AtmosphericFogEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */