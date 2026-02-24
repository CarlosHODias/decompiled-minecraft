/*     */ package net.minecraft.server.waypoints;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.Table;
/*     */ import com.google.common.collect.Tables;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.waypoints.Waypoint;
/*     */ import net.minecraft.world.waypoints.WaypointManager;
/*     */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*     */ 
/*     */ public class ServerWaypointManager implements WaypointManager<WaypointTransmitter> {
/*  18 */   private final Set<WaypointTransmitter> waypoints = new HashSet<>();
/*  19 */   private final Set<ServerPlayer> players = new HashSet<>();
/*  20 */   private final Table<ServerPlayer, WaypointTransmitter, WaypointTransmitter.Connection> connections = (Table<ServerPlayer, WaypointTransmitter, WaypointTransmitter.Connection>)HashBasedTable.create();
/*     */ 
/*     */   
/*     */   public void trackWaypoint(WaypointTransmitter waypoint) {
/*  24 */     this.waypoints.add(waypoint);
/*  25 */     for (ServerPlayer player : this.players) {
/*  26 */       createConnection(player, waypoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWaypoint(WaypointTransmitter waypoint) {
/*  32 */     if (!this.waypoints.contains(waypoint)) {
/*     */       return;
/*     */     }
/*     */     
/*  36 */     Map<ServerPlayer, WaypointTransmitter.Connection> playerConnection = Tables.transpose(this.connections).row(waypoint);
/*  37 */     Sets.SetView<ServerPlayer> potentialPlayers = Sets.difference(this.players, playerConnection.keySet());
/*     */     
/*  39 */     for (UnmodifiableIterator<Map.Entry<ServerPlayer, WaypointTransmitter.Connection>> unmodifiableIterator2 = ImmutableSet.copyOf(playerConnection.entrySet()).iterator(); unmodifiableIterator2.hasNext(); ) { Map.Entry<ServerPlayer, WaypointTransmitter.Connection> waypointConnection = unmodifiableIterator2.next();
/*  40 */       updateConnection(waypointConnection.getKey(), waypoint, waypointConnection.getValue()); }
/*     */ 
/*     */     
/*  43 */     for (UnmodifiableIterator<Map.Entry<ServerPlayer, WaypointTransmitter.Connection>> unmodifiableIterator1 = potentialPlayers.iterator(); unmodifiableIterator1.hasNext(); ) { ServerPlayer player = (ServerPlayer)unmodifiableIterator1.next();
/*  44 */       createConnection(player, waypoint); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void untrackWaypoint(WaypointTransmitter waypoint) {
/*  50 */     this.connections.column(waypoint).forEach((player, connection) -> connection.disconnect());
/*     */ 
/*     */     
/*  53 */     Tables.transpose(this.connections).row(waypoint).clear();
/*  54 */     this.waypoints.remove(waypoint);
/*     */   }
/*     */   
/*     */   public void addPlayer(ServerPlayer player) {
/*  58 */     this.players.add(player);
/*  59 */     for (WaypointTransmitter waypoint : this.waypoints) {
/*  60 */       createConnection(player, waypoint);
/*     */     }
/*     */     
/*  63 */     if (player.isTransmittingWaypoint()) {
/*  64 */       trackWaypoint((WaypointTransmitter)player);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updatePlayer(ServerPlayer player) {
/*  69 */     Map<WaypointTransmitter, WaypointTransmitter.Connection> waypointConnections = this.connections.row(player);
/*  70 */     Sets.SetView<WaypointTransmitter> potentialWaypoints = Sets.difference(this.waypoints, waypointConnections.keySet());
/*     */     
/*  72 */     for (UnmodifiableIterator<Map.Entry<WaypointTransmitter, WaypointTransmitter.Connection>> unmodifiableIterator2 = ImmutableSet.copyOf(waypointConnections.entrySet()).iterator(); unmodifiableIterator2.hasNext(); ) { Map.Entry<WaypointTransmitter, WaypointTransmitter.Connection> waypointConnection = unmodifiableIterator2.next();
/*  73 */       updateConnection(player, waypointConnection.getKey(), waypointConnection.getValue()); }
/*     */ 
/*     */     
/*  76 */     for (UnmodifiableIterator<Map.Entry<WaypointTransmitter, WaypointTransmitter.Connection>> unmodifiableIterator1 = potentialWaypoints.iterator(); unmodifiableIterator1.hasNext(); ) { WaypointTransmitter waypoint = (WaypointTransmitter)unmodifiableIterator1.next();
/*  77 */       createConnection(player, waypoint); }
/*     */   
/*     */   }
/*     */   
/*     */   public void removePlayer(ServerPlayer player) {
/*  82 */     this.connections.row(player).values().removeIf(connection -> {
/*     */           connection.disconnect();
/*     */           return true;
/*     */         });
/*  86 */     untrackWaypoint((WaypointTransmitter)player);
/*  87 */     this.players.remove(player);
/*     */   }
/*     */   
/*     */   public void breakAllConnections() {
/*  91 */     this.connections.values().forEach(WaypointTransmitter.Connection::disconnect);
/*  92 */     this.connections.clear();
/*     */   }
/*     */   
/*     */   public void remakeConnections(WaypointTransmitter waypoint) {
/*  96 */     for (ServerPlayer player : this.players) {
/*  97 */       createConnection(player, waypoint);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<WaypointTransmitter> transmitters() {
/* 102 */     return this.waypoints;
/*     */   }
/*     */   
/*     */   private static boolean isLocatorBarEnabledFor(ServerPlayer player) {
/* 106 */     return (Boolean)player.level().getGameRules().get(GameRules.LOCATOR_BAR);
/*     */   }
/*     */   
/*     */   private void createConnection(ServerPlayer player, WaypointTransmitter waypoint) {
/* 110 */     if (player == waypoint) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     if (!isLocatorBarEnabledFor(player)) {
/*     */       return;
/*     */     }
/*     */     
/* 118 */     waypoint.makeWaypointConnectionWith(player).ifPresentOrElse(connection -> {
/*     */           this.connections.put(player, player, player);
/*     */           player.connect();
/*     */         }, () -> {
/*     */           WaypointTransmitter.Connection connection = (WaypointTransmitter.Connection)this.connections.remove(player, waypoint);
/*     */           if (connection != null) {
/*     */             connection.disconnect();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void updateConnection(ServerPlayer player, WaypointTransmitter waypoint, WaypointTransmitter.Connection connection) {
/* 130 */     if (player == waypoint) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     if (!isLocatorBarEnabledFor(player)) {
/*     */       return;
/*     */     }
/*     */     
/* 138 */     if (!connection.isBroken()) {
/* 139 */       connection.update();
/*     */       
/*     */       return;
/*     */     } 
/* 143 */     waypoint.makeWaypointConnectionWith(player).ifPresentOrElse(newConnection -> {
/*     */           player.connect();
/*     */           this.connections.put(player, player, player);
/*     */         }, () -> {
/*     */           connection.disconnect();
/*     */           this.connections.remove(player, waypoint);
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/waypoints/ServerWaypointManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */