/*    */ package net.minecraft.client.telemetry.events;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraft.client.telemetry.TelemetryEventSender;
/*    */ import net.minecraft.client.telemetry.TelemetryEventType;
/*    */ import net.minecraft.client.telemetry.TelemetryProperty;
/*    */ import net.minecraft.client.telemetry.TelemetryPropertyMap;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldLoadEvent
/*    */ {
/*    */   private boolean eventSent;
/*    */   private TelemetryProperty.GameMode gameMode;
/*    */   private String serverBrand;
/*    */   private final String minigameName;
/*    */   
/*    */   public WorldLoadEvent(String minigameName) {
/* 21 */     this.minigameName = minigameName;
/*    */   }
/*    */   
/*    */   public void addProperties(TelemetryPropertyMap.Builder properties) {
/* 25 */     if (this.serverBrand != null) {
/* 26 */       properties.put(TelemetryProperty.SERVER_MODDED, !this.serverBrand.equals("vanilla"));
/*    */     }
/* 28 */     properties.put(TelemetryProperty.SERVER_TYPE, getServerType());
/*    */   }
/*    */   
/*    */   private TelemetryProperty.ServerType getServerType() {
/* 32 */     ServerData server = Minecraft.getInstance().getCurrentServer();
/* 33 */     if (server != null && server.isRealm()) {
/* 34 */       return TelemetryProperty.ServerType.REALM;
/*    */     }
/* 36 */     if (Minecraft.getInstance().hasSingleplayerServer()) {
/* 37 */       return TelemetryProperty.ServerType.LOCAL;
/*    */     }
/* 39 */     return TelemetryProperty.ServerType.OTHER;
/*    */   }
/*    */   
/*    */   public boolean send(TelemetryEventSender eventSender) {
/* 43 */     if (this.eventSent || this.gameMode == null || this.serverBrand == null) {
/* 44 */       return false;
/*    */     }
/* 46 */     this.eventSent = true;
/* 47 */     eventSender.send(TelemetryEventType.WORLD_LOADED, properties -> {
/*    */           properties.put(TelemetryProperty.GAME_MODE, this.gameMode);
/*    */           if (this.minigameName != null) {
/*    */             properties.put(TelemetryProperty.REALMS_MAP_CONTENT, this.minigameName);
/*    */           }
/*    */         });
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setGameMode(GameType type, boolean hardcore) {
/* 58 */     switch (type) { default: throw new MatchException(null, null);
/* 59 */       case SURVIVAL: if (hardcore);
/*    */       case CREATIVE: 
/*    */       case ADVENTURE: 
/* 62 */       case SPECTATOR: break; }  this.gameMode = TelemetryProperty.GameMode.SPECTATOR;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServerBrand(String serverBrand) {
/* 67 */     this.serverBrand = serverBrand;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/events/WorldLoadEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */