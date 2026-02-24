/*    */ package net.minecraft.client.telemetry.events;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import com.google.common.base.Ticker;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.OptionalLong;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.telemetry.TelemetryEventSender;
/*    */ import net.minecraft.client.telemetry.TelemetryEventType;
/*    */ import net.minecraft.client.telemetry.TelemetryProperty;
/*    */ import net.minecraft.client.telemetry.TelemetryPropertyMap;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class GameLoadTimesEvent {
/* 19 */   public static final GameLoadTimesEvent INSTANCE = new GameLoadTimesEvent(Ticker.systemTicker());
/*    */   
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Ticker timeSource;
/* 24 */   private final Map<TelemetryProperty<Measurement>, Stopwatch> measurements = new HashMap<>();
/* 25 */   private OptionalLong bootstrapTime = OptionalLong.empty();
/*    */   
/*    */   protected GameLoadTimesEvent(Ticker timeSource) {
/* 28 */     this.timeSource = timeSource;
/*    */   }
/*    */   
/*    */   public synchronized void beginStep(TelemetryProperty<Measurement> property) {
/* 32 */     beginStep(property, p -> Stopwatch.createStarted(this.timeSource));
/*    */   }
/*    */   
/*    */   public synchronized void beginStep(TelemetryProperty<Measurement> property, Stopwatch measurement) {
/* 36 */     beginStep(property, p -> measurement);
/*    */   }
/*    */   
/*    */   private synchronized void beginStep(TelemetryProperty<Measurement> property, Function<TelemetryProperty<Measurement>, Stopwatch> measurement) {
/* 40 */     this.measurements.computeIfAbsent(property, measurement);
/*    */   }
/*    */   
/*    */   public synchronized void endStep(TelemetryProperty<Measurement> property) {
/* 44 */     Stopwatch stepMeasurement = this.measurements.get(property);
/* 45 */     if (stepMeasurement == null) {
/* 46 */       LOGGER.warn("Attempted to end step for {} before starting it", property.id());
/*    */       return;
/*    */     } 
/* 49 */     if (stepMeasurement.isRunning()) {
/* 50 */       stepMeasurement.stop();
/*    */     }
/*    */   }
/*    */   
/*    */   public void send(TelemetryEventSender eventSender) {
/* 55 */     eventSender.send(TelemetryEventType.GAME_LOAD_TIMES, properties -> {
/*    */           synchronized (this) {
/*    */             this.measurements.forEach(());
/*    */             this.bootstrapTime.ifPresent(());
/*    */             this.measurements.clear();
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setBootstrapTime(long duration) {
/* 72 */     this.bootstrapTime = OptionalLong.of(duration);
/*    */   }
/*    */   public static final class Measurement extends Record { private final int millis; public static final Codec<Measurement> CODEC;
/* 75 */     public Measurement(int millis) { this.millis = millis; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 75 */       //   0	7	0	this	Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement; } public int millis() { return this.millis; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #75	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/telemetry/events/GameLoadTimesEvent$Measurement;
/* 76 */       //   0	8	1	o	Ljava/lang/Object; } static { CODEC = Codec.INT.xmap(Measurement::new, o -> o.millis); }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/events/GameLoadTimesEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */