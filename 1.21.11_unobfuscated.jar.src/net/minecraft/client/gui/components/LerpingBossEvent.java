/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.BossEvent;
/*    */ 
/*    */ public class LerpingBossEvent
/*    */   extends BossEvent {
/*    */   private static final long LERP_MILLISECONDS = 100L;
/*    */   protected float targetPercent;
/*    */   protected long setTime;
/*    */   
/*    */   public LerpingBossEvent(UUID id, Component name, float progress, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
/* 16 */     super(id, name, color, overlay);
/* 17 */     this.targetPercent = progress;
/* 18 */     this.progress = progress;
/* 19 */     this.setTime = Util.getMillis();
/* 20 */     setDarkenScreen(darkenScreen);
/* 21 */     setPlayBossMusic(playMusic);
/* 22 */     setCreateWorldFog(createWorldFog);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setProgress(float progress) {
/* 27 */     this.progress = getProgress();
/* 28 */     this.targetPercent = progress;
/* 29 */     this.setTime = Util.getMillis();
/*    */   }
/*    */ 
/*    */   
/*    */   public float getProgress() {
/* 34 */     long timeSinceSet = Util.getMillis() - this.setTime;
/* 35 */     float lerpPercent = Mth.clamp((float)timeSinceSet / 100.0F, 0.0F, 1.0F);
/* 36 */     return Mth.lerp(lerpPercent, this.progress, this.targetPercent);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/LerpingBossEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */