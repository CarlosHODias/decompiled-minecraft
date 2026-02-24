/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ 
/*    */ 
/*    */ public class LevelRenderState
/*    */ {
/* 11 */   public CameraRenderState cameraRenderState = new CameraRenderState();
/* 12 */   public final List<EntityRenderState> entityRenderStates = new ArrayList<>();
/* 13 */   public final List<BlockEntityRenderState> blockEntityRenderStates = new ArrayList<>();
/*    */   public boolean haveGlowingEntities;
/*    */   public BlockOutlineRenderState blockOutlineRenderState;
/* 16 */   public final List<BlockBreakingRenderState> blockBreakingRenderStates = new ArrayList<>();
/* 17 */   public final WeatherRenderState weatherRenderState = new WeatherRenderState();
/* 18 */   public final WorldBorderRenderState worldBorderRenderState = new WorldBorderRenderState();
/* 19 */   public final SkyRenderState skyRenderState = new SkyRenderState();
/*    */   public long gameTime;
/*    */   
/*    */   public void reset() {
/* 23 */     this.entityRenderStates.clear();
/* 24 */     this.blockEntityRenderStates.clear();
/* 25 */     this.blockBreakingRenderStates.clear();
/* 26 */     this.haveGlowingEntities = false;
/* 27 */     this.blockOutlineRenderState = null;
/* 28 */     this.weatherRenderState.reset();
/* 29 */     this.worldBorderRenderState.reset();
/* 30 */     this.skyRenderState.reset();
/* 31 */     this.gameTime = 0L;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/LevelRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */