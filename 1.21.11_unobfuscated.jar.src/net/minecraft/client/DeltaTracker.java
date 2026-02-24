/*     */ package net.minecraft.client;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DeltaTracker
/*     */ {
/*  13 */   public static final DeltaTracker ZERO = new DefaultValue(0.0F);
/*  14 */   public static final DeltaTracker ONE = new DefaultValue(1.0F);
/*     */   
/*     */   float getGameTimeDeltaTicks();
/*     */   
/*     */   float getGameTimeDeltaPartialTick(boolean paramBoolean);
/*     */   
/*     */   float getRealtimeDeltaTicks();
/*     */   
/*     */   public static class Timer
/*     */     implements DeltaTracker
/*     */   {
/*     */     private float deltaTicks;
/*     */     private float deltaTickResidual;
/*     */     private float realtimeDeltaTicks;
/*     */     private float pausedDeltaTickResidual;
/*     */     private long lastMs;
/*     */     
/*     */     public Timer(float ticksPerSecond, long currentMs, FloatUnaryOperator targetMsptProvider) {
/*  32 */       this.msPerTick = 1000.0F / ticksPerSecond;
/*  33 */       this.lastUiMs = this.lastMs = currentMs;
/*  34 */       this.targetMsptProvider = targetMsptProvider;
/*     */     }
/*     */     private long lastUiMs; private final float msPerTick; private final FloatUnaryOperator targetMsptProvider; private boolean paused; private boolean frozen;
/*     */     public int advanceTime(long currentMs, boolean shouldAdvanceGameTime) {
/*  38 */       advanceRealTime(currentMs);
/*  39 */       if (shouldAdvanceGameTime) {
/*  40 */         return advanceGameTime(currentMs);
/*     */       }
/*  42 */       return 0;
/*     */     }
/*     */     
/*     */     private int advanceGameTime(long currentMs) {
/*  46 */       this.deltaTicks = (float)(currentMs - this.lastMs) / this.targetMsptProvider.apply(this.msPerTick);
/*  47 */       this.lastMs = currentMs;
/*     */       
/*  49 */       this.deltaTickResidual += this.deltaTicks;
/*  50 */       int ticks = (int)this.deltaTickResidual;
/*  51 */       this.deltaTickResidual -= ticks;
/*  52 */       return ticks;
/*     */     }
/*     */     
/*     */     private void advanceRealTime(long currentMs) {
/*  56 */       this.realtimeDeltaTicks = (float)(currentMs - this.lastUiMs) / this.msPerTick;
/*  57 */       this.lastUiMs = currentMs;
/*     */     }
/*     */     
/*     */     public void updatePauseState(boolean pauseState) {
/*  61 */       if (pauseState) {
/*  62 */         pause();
/*     */       } else {
/*  64 */         unPause();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void pause() {
/*  69 */       if (!this.paused) {
/*  70 */         this.pausedDeltaTickResidual = this.deltaTickResidual;
/*     */       }
/*  72 */       this.paused = true;
/*     */     }
/*     */     
/*     */     private void unPause() {
/*  76 */       if (this.paused) {
/*  77 */         this.deltaTickResidual = this.pausedDeltaTickResidual;
/*     */       }
/*  79 */       this.paused = false;
/*     */     }
/*     */     
/*     */     public void updateFrozenState(boolean frozen) {
/*  83 */       this.frozen = frozen;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getGameTimeDeltaTicks() {
/*  88 */       return this.deltaTicks;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getGameTimeDeltaPartialTick(boolean ignoreFrozenGame) {
/*  93 */       if (!ignoreFrozenGame && this.frozen) {
/*  94 */         return 1.0F;
/*     */       }
/*  96 */       return this.paused ? this.pausedDeltaTickResidual : this.deltaTickResidual;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getRealtimeDeltaTicks() {
/* 101 */       if (this.realtimeDeltaTicks > 7.0F)
/*     */       {
/*     */ 
/*     */         
/* 105 */         return 0.5F;
/*     */       }
/* 107 */       return this.realtimeDeltaTicks;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DefaultValue implements DeltaTracker {
/*     */     private final float value;
/*     */     
/*     */     private DefaultValue(float value) {
/* 115 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getGameTimeDeltaTicks() {
/* 120 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getGameTimeDeltaPartialTick(boolean ignored) {
/* 125 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getRealtimeDeltaTicks() {
/* 130 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/DeltaTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */