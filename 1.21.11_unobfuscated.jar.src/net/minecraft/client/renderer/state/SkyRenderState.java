/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public class SkyRenderState {
/*  7 */   public DimensionType.Skybox skybox = DimensionType.Skybox.NONE;
/*    */   public boolean shouldRenderDarkDisc;
/*    */   public float sunAngle;
/*    */   public float moonAngle;
/*    */   public float starAngle;
/*    */   public float rainBrightness;
/*    */   public float starBrightness;
/*    */   public int sunriseAndSunsetColor;
/* 15 */   public MoonPhase moonPhase = MoonPhase.FULL_MOON;
/*    */   public int skyColor;
/*    */   public float endFlashIntensity;
/*    */   public float endFlashXAngle;
/*    */   public float endFlashYAngle;
/*    */   
/*    */   public void reset() {
/* 22 */     this.skybox = DimensionType.Skybox.NONE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/SkyRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */