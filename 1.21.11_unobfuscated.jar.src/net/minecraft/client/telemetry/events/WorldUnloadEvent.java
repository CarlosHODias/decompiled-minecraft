/*    */ package net.minecraft.client.telemetry.events;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.telemetry.TelemetryEventSender;
/*    */ import net.minecraft.client.telemetry.TelemetryEventType;
/*    */ import net.minecraft.client.telemetry.TelemetryProperty;
/*    */ import net.minecraft.client.telemetry.TelemetryPropertyMap;
/*    */ 
/*    */ public class WorldUnloadEvent
/*    */ {
/*    */   private static final int NOT_TRACKING_TIME = -1;
/* 14 */   private Optional<Instant> worldLoadedTime = Optional.empty();
/*    */   private long totalTicks;
/*    */   private long lastGameTime;
/*    */   
/*    */   public void onPlayerInfoReceived() {
/* 19 */     this.lastGameTime = -1L;
/* 20 */     if (this.worldLoadedTime.isEmpty()) {
/* 21 */       this.worldLoadedTime = Optional.of(Instant.now());
/*    */     }
/*    */   }
/*    */   
/*    */   public void setTime(long gameTime) {
/* 26 */     if (this.lastGameTime != -1L) {
/* 27 */       this.totalTicks += Math.max(0L, gameTime - this.lastGameTime);
/*    */     }
/* 29 */     this.lastGameTime = gameTime;
/*    */   }
/*    */   
/*    */   private int getTimeInSecondsSinceLoad(Instant loadedTime) {
/* 33 */     Duration timeBetween = Duration.between(loadedTime, Instant.now());
/* 34 */     return (int)timeBetween.toSeconds();
/*    */   }
/*    */   
/*    */   public void send(TelemetryEventSender eventSender) {
/* 38 */     this.worldLoadedTime.ifPresent(loadedTime -> eventSender.send(TelemetryEventType.WORLD_UNLOADED, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/events/WorldUnloadEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */