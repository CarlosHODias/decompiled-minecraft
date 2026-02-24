/*    */ package net.minecraft.client.telemetry.events;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import net.minecraft.client.telemetry.TelemetryEventSender;
/*    */ 
/*    */ 
/*    */ public abstract class AggregatedTelemetryEvent
/*    */ {
/*    */   private static final int SAMPLE_INTERVAL_MS = 60000;
/*    */   private static final int SAMPLES_PER_EVENT = 10;
/*    */   private int sampleCount;
/*    */   private boolean ticking = false;
/*    */   private Instant lastSampleTime;
/*    */   
/*    */   public void start() {
/* 17 */     this.ticking = true;
/* 18 */     this.lastSampleTime = Instant.now();
/* 19 */     this.sampleCount = 0;
/*    */   }
/*    */   
/*    */   public void tick(TelemetryEventSender eventSender) {
/* 23 */     if (shouldTakeSample()) {
/* 24 */       takeSample();
/* 25 */       this.sampleCount++;
/* 26 */       this.lastSampleTime = Instant.now();
/*    */     } 
/* 28 */     if (shouldSentEvent()) {
/* 29 */       sendEvent(eventSender);
/* 30 */       this.sampleCount = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean shouldTakeSample() {
/* 35 */     return (this.ticking && this.lastSampleTime != null && Duration.between(this.lastSampleTime, Instant.now()).toMillis() > 60000L);
/*    */   }
/*    */   
/*    */   public boolean shouldSentEvent() {
/* 39 */     return (this.sampleCount >= 10);
/*    */   }
/*    */   
/*    */   public void stop() {
/* 43 */     this.ticking = false;
/*    */   }
/*    */   
/*    */   protected int getSampleCount() {
/* 47 */     return this.sampleCount;
/*    */   }
/*    */   
/*    */   public abstract void takeSample();
/*    */   
/*    */   public abstract void sendEvent(TelemetryEventSender paramTelemetryEventSender);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/events/AggregatedTelemetryEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */