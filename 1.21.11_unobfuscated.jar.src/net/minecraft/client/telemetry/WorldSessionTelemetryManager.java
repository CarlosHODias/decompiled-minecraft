/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.client.telemetry.events.PerformanceMetricsEvent;
/*    */ import net.minecraft.client.telemetry.events.WorldLoadEvent;
/*    */ import net.minecraft.client.telemetry.events.WorldLoadTimesEvent;
/*    */ import net.minecraft.client.telemetry.events.WorldUnloadEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class WorldSessionTelemetryManager
/*    */ {
/* 17 */   private final UUID worldSessionId = UUID.randomUUID();
/*    */   
/*    */   private final TelemetryEventSender eventSender;
/*    */   private final WorldLoadEvent worldLoadEvent;
/*    */   private final WorldUnloadEvent worldUnloadEvent;
/*    */   private final PerformanceMetricsEvent performanceMetricsEvent;
/*    */   private final WorldLoadTimesEvent worldLoadTimesEvent;
/*    */   
/*    */   public WorldSessionTelemetryManager(TelemetryEventSender eventSender, boolean newWorld, Duration worldLoadDuration, String minigameName) {
/* 26 */     this.worldUnloadEvent = new WorldUnloadEvent();
/* 27 */     this.worldLoadEvent = new WorldLoadEvent(minigameName);
/* 28 */     this.performanceMetricsEvent = new PerformanceMetricsEvent();
/* 29 */     this.worldLoadTimesEvent = new WorldLoadTimesEvent(newWorld, worldLoadDuration);
/*    */     
/* 31 */     this.eventSender = eventSender.decorate(properties -> {
/*    */           this.worldLoadEvent.addProperties(properties);
/*    */           properties.put(TelemetryProperty.WORLD_SESSION_ID, this.worldSessionId);
/*    */         });
/*    */   }
/*    */   
/*    */   public void tick() {
/* 38 */     this.performanceMetricsEvent.tick(this.eventSender);
/*    */   }
/*    */   
/*    */   public void onPlayerInfoReceived(GameType type, boolean hardcore) {
/* 42 */     this.worldLoadEvent.setGameMode(type, hardcore);
/* 43 */     this.worldUnloadEvent.onPlayerInfoReceived();
/* 44 */     worldSessionStart();
/*    */   }
/*    */   
/*    */   public void onServerBrandReceived(String serverBrand) {
/* 48 */     this.worldLoadEvent.setServerBrand(serverBrand);
/* 49 */     worldSessionStart();
/*    */   }
/*    */   
/*    */   public void setTime(long gameTime) {
/* 53 */     this.worldUnloadEvent.setTime(gameTime);
/*    */   }
/*    */   
/*    */   public void worldSessionStart() {
/* 57 */     if (this.worldLoadEvent.send(this.eventSender)) {
/* 58 */       this.worldLoadTimesEvent.send(this.eventSender);
/* 59 */       this.performanceMetricsEvent.start();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisconnect() {
/* 65 */     this.worldLoadEvent.send(this.eventSender);
/* 66 */     this.performanceMetricsEvent.stop();
/* 67 */     this.worldUnloadEvent.send(this.eventSender);
/*    */   }
/*    */   
/*    */   public void onAdvancementDone(Level level, AdvancementHolder holder) {
/* 71 */     Identifier advancementId = holder.id();
/* 72 */     if (holder.value().sendsTelemetryEvent() && "minecraft".equals(advancementId.getNamespace())) {
/* 73 */       long gameTime = level.getGameTime();
/* 74 */       this.eventSender.send(TelemetryEventType.ADVANCEMENT_MADE, properties -> {
/*    */             properties.put(TelemetryProperty.ADVANCEMENT_ID, advancementId.toString());
/*    */             properties.put(TelemetryProperty.ADVANCEMENT_GAME_TIME, gameTime);
/*    */           });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/WorldSessionTelemetryManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */