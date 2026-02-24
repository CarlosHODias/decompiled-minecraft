/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class TickRateManager
/*    */ {
/*    */   public static final float MIN_TICKRATE = 1.0F;
/* 10 */   protected float tickrate = 20.0F;
/* 11 */   protected long nanosecondsPerTick = TimeUtil.NANOSECONDS_PER_SECOND / 20L;
/* 12 */   protected int frozenTicksToRun = 0;
/*    */   protected boolean runGameElements = true;
/*    */   protected boolean isFrozen = false;
/*    */   
/*    */   public void setTickRate(float rate) {
/* 17 */     this.tickrate = Math.max(rate, 1.0F);
/* 18 */     this.nanosecondsPerTick = (long)(TimeUtil.NANOSECONDS_PER_SECOND / this.tickrate);
/*    */   }
/*    */   
/*    */   public float tickrate() {
/* 22 */     return this.tickrate;
/*    */   }
/*    */   
/*    */   public float millisecondsPerTick() {
/* 26 */     return (float)this.nanosecondsPerTick / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*    */   }
/*    */   
/*    */   public long nanosecondsPerTick() {
/* 30 */     return this.nanosecondsPerTick;
/*    */   }
/*    */   
/*    */   public boolean runsNormally() {
/* 34 */     return this.runGameElements;
/*    */   }
/*    */   
/*    */   public boolean isSteppingForward() {
/* 38 */     return (this.frozenTicksToRun > 0);
/*    */   }
/*    */   
/*    */   public void setFrozenTicksToRun(int timeout) {
/* 42 */     this.frozenTicksToRun = timeout;
/*    */   }
/*    */   
/*    */   public int frozenTicksToRun() {
/* 46 */     return this.frozenTicksToRun;
/*    */   }
/*    */   
/*    */   public void setFrozen(boolean state) {
/* 50 */     this.isFrozen = state;
/*    */   }
/*    */   
/*    */   public boolean isFrozen() {
/* 54 */     return this.isFrozen;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 58 */     this.runGameElements = (!this.isFrozen || this.frozenTicksToRun > 0);
/* 59 */     if (this.frozenTicksToRun > 0) {
/* 60 */       this.frozenTicksToRun--;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isEntityFrozen(Entity entity) {
/* 65 */     return (!runsNormally() && !(entity instanceof net.minecraft.world.entity.player.Player) && entity.countPlayerPassengers() <= 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/TickRateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */