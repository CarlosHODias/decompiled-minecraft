/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import java.util.OptionalLong;
/*     */ 
/*     */ public class TimerQuery {
/*     */   private CommandEncoder activeEncoder;
/*     */   private GpuQuery activeGpuQuery;
/*     */   
/*     */   public static TimerQuery getInstance() {
/*  10 */     return TimerQueryLazyLoader.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRecording() {
/*  17 */     return (this.activeGpuQuery != null);
/*     */   }
/*     */   
/*     */   public void beginProfile() {
/*  21 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  23 */     if (this.activeGpuQuery != null) {
/*  24 */       throw new IllegalStateException("Current profile not ended");
/*     */     }
/*     */     
/*  27 */     this.activeEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  28 */     this.activeGpuQuery = this.activeEncoder.timerQueryBegin();
/*     */   }
/*     */   
/*     */   public FrameProfile endProfile() {
/*  32 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  34 */     if (this.activeGpuQuery == null || this.activeEncoder == null) {
/*  35 */       throw new IllegalStateException("endProfile called before beginProfile");
/*     */     }
/*     */     
/*  38 */     this.activeEncoder.timerQueryEnd(this.activeGpuQuery);
/*  39 */     FrameProfile frameProfile = new FrameProfile(this.activeGpuQuery);
/*  40 */     this.activeGpuQuery = null;
/*  41 */     this.activeEncoder = null;
/*  42 */     return frameProfile;
/*     */   }
/*     */   
/*     */   public static class FrameProfile
/*     */   {
/*     */     private static final long NO_RESULT = 0L;
/*     */     private static final long CANCELLED_RESULT = -1L;
/*     */     private final GpuQuery gpuQuery;
/*  50 */     private long timerResult = 0L;
/*     */     
/*     */     private FrameProfile(GpuQuery gpuQuery) {
/*  53 */       this.gpuQuery = gpuQuery;
/*     */     }
/*     */     
/*     */     public void cancel() {
/*  57 */       RenderSystem.assertOnRenderThread();
/*     */       
/*  59 */       if (this.timerResult != 0L) {
/*     */         return;
/*     */       }
/*     */       
/*  63 */       this.timerResult = -1L;
/*  64 */       this.gpuQuery.close();
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/*  68 */       RenderSystem.assertOnRenderThread();
/*     */       
/*  70 */       if (this.timerResult != 0L) {
/*  71 */         return true;
/*     */       }
/*     */       
/*  74 */       OptionalLong value = this.gpuQuery.getValue();
/*  75 */       if (value.isPresent()) {
/*  76 */         this.timerResult = value.getAsLong();
/*  77 */         this.gpuQuery.close();
/*  78 */         return true;
/*     */       } 
/*  80 */       return false;
/*     */     }
/*     */     
/*     */     public long get() {
/*  84 */       RenderSystem.assertOnRenderThread();
/*     */       
/*  86 */       if (this.timerResult == 0L) {
/*  87 */         OptionalLong value = this.gpuQuery.getValue();
/*  88 */         if (value.isPresent()) {
/*  89 */           this.timerResult = value.getAsLong();
/*  90 */           this.gpuQuery.close();
/*     */         } 
/*     */       } 
/*  93 */       return this.timerResult;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TimerQueryLazyLoader {
/*     */     private static TimerQuery instantiate() {
/*  99 */       return new TimerQuery();
/*     */     }
/*     */     
/* 102 */     private static final TimerQuery INSTANCE = instantiate();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/TimerQuery.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */