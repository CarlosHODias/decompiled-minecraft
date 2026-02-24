/*    */ package net.minecraft.client.telemetry.events;
/*    */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*    */ import it.unimi.dsi.fastutil.longs.LongList;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.telemetry.TelemetryEventSender;
/*    */ import net.minecraft.client.telemetry.TelemetryEventType;
/*    */ import net.minecraft.client.telemetry.TelemetryProperty;
/*    */ import net.minecraft.client.telemetry.TelemetryPropertyMap;
/*    */ 
/*    */ public final class PerformanceMetricsEvent extends AggregatedTelemetryEvent {
/* 11 */   private static final long DEDICATED_MEMORY_KB = toKilobytes(Runtime.getRuntime().maxMemory());
/* 12 */   private final LongList fpsSamples = (LongList)new LongArrayList();
/* 13 */   private final LongList frameTimeSamples = (LongList)new LongArrayList();
/* 14 */   private final LongList usedMemorySamples = (LongList)new LongArrayList();
/*    */ 
/*    */   
/*    */   public void tick(TelemetryEventSender eventSender) {
/* 18 */     if (Minecraft.getInstance().telemetryOptInExtra()) {
/* 19 */       super.tick(eventSender);
/*    */     }
/*    */   }
/*    */   
/*    */   private void resetValues() {
/* 24 */     this.fpsSamples.clear();
/* 25 */     this.frameTimeSamples.clear();
/* 26 */     this.usedMemorySamples.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void takeSample() {
/* 31 */     this.fpsSamples.add(Minecraft.getInstance().getFps());
/* 32 */     takeUsedMemorySample();
/* 33 */     this.frameTimeSamples.add(Minecraft.getInstance().getFrameTimeNs());
/*    */   }
/*    */   
/*    */   private void takeUsedMemorySample() {
/* 37 */     long totalMemory = Runtime.getRuntime().totalMemory();
/* 38 */     long freeMemory = Runtime.getRuntime().freeMemory();
/* 39 */     long usedMemorySample = totalMemory - freeMemory;
/* 40 */     this.usedMemorySamples.add(toKilobytes(usedMemorySample));
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendEvent(TelemetryEventSender eventSender) {
/* 45 */     eventSender.send(TelemetryEventType.PERFORMANCE_METRICS, properties -> {
/*    */           properties.put(TelemetryProperty.FRAME_RATE_SAMPLES, new LongArrayList(this.fpsSamples));
/*    */           properties.put(TelemetryProperty.RENDER_TIME_SAMPLES, new LongArrayList(this.frameTimeSamples));
/*    */           properties.put(TelemetryProperty.USED_MEMORY_SAMPLES, new LongArrayList(this.usedMemorySamples));
/*    */           properties.put(TelemetryProperty.NUMBER_OF_SAMPLES, getSampleCount());
/*    */           properties.put(TelemetryProperty.RENDER_DISTANCE, (Minecraft.getInstance()).options.getEffectiveRenderDistance());
/*    */           properties.put(TelemetryProperty.DEDICATED_MEMORY_KB, (int)DEDICATED_MEMORY_KB);
/*    */         });
/* 53 */     resetValues();
/*    */   }
/*    */   
/*    */   private static long toKilobytes(long bytes) {
/* 57 */     return bytes / 1000L;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/events/PerformanceMetricsEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */