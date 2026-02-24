/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TelemetryEventSender
/*    */ {
/*    */   default TelemetryEventSender decorate(Consumer<TelemetryPropertyMap.Builder> decorator) {
/* 10 */     return (type, buildFunction) -> send(decorator, ());
/*    */   }
/*    */   
/*    */   public static final TelemetryEventSender DISABLED = (type, buildFunction) -> {
/*    */     
/*    */     };
/*    */   
/*    */   void send(TelemetryEventType paramTelemetryEventType, Consumer<TelemetryPropertyMap.Builder> paramConsumer);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/TelemetryEventSender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */