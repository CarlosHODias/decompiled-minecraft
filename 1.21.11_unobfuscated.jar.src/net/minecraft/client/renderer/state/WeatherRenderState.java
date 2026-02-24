/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.WeatherEffectRenderer;
/*    */ 
/*    */ public class WeatherRenderState
/*    */ {
/*  9 */   public final List<WeatherEffectRenderer.ColumnInstance> rainColumns = new ArrayList<>();
/* 10 */   public final List<WeatherEffectRenderer.ColumnInstance> snowColumns = new ArrayList<>();
/*    */   public float intensity;
/*    */   public int radius;
/*    */   
/*    */   public void reset() {
/* 15 */     this.rainColumns.clear();
/* 16 */     this.snowColumns.clear();
/* 17 */     this.intensity = 0.0F;
/* 18 */     this.radius = 0;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/WeatherRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */