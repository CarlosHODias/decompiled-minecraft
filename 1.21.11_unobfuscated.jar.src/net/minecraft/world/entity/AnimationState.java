/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnimationState
/*    */ {
/*    */   private static final int STOPPED = -2147483648;
/* 10 */   private int startTick = Integer.MIN_VALUE;
/*    */   
/*    */   public void start(int tickCount) {
/* 13 */     this.startTick = tickCount;
/*    */   }
/*    */   
/*    */   public void startIfStopped(int tickCount) {
/* 17 */     if (!isStarted()) {
/* 18 */       start(tickCount);
/*    */     }
/*    */   }
/*    */   
/*    */   public void animateWhen(boolean condition, int tickCount) {
/* 23 */     if (condition) {
/* 24 */       startIfStopped(tickCount);
/*    */     } else {
/* 26 */       stop();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void stop() {
/* 31 */     this.startTick = Integer.MIN_VALUE;
/*    */   }
/*    */   
/*    */   public void ifStarted(Consumer<AnimationState> timer) {
/* 35 */     if (isStarted()) {
/* 36 */       timer.accept(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public void fastForward(int ticks, float timeScale) {
/* 41 */     if (!isStarted()) {
/*    */       return;
/*    */     }
/* 44 */     this.startTick -= (int)(ticks * timeScale);
/*    */   }
/*    */   
/*    */   public long getTimeInMillis(float ageInTicks) {
/* 48 */     float timeInTicks = ageInTicks - this.startTick;
/* 49 */     return (long)(timeInTicks * 50.0F);
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 53 */     return (this.startTick != Integer.MIN_VALUE);
/*    */   }
/*    */   
/*    */   public void copyFrom(AnimationState state) {
/* 57 */     this.startTick = state.startTick;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/AnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */