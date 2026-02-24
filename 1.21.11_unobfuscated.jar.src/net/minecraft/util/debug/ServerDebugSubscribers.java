/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class ServerDebugSubscribers
/*    */ {
/*    */   private final MinecraftServer server;
/* 17 */   private final Map<DebugSubscription<?>, List<ServerPlayer>> enabledSubscriptions = new HashMap<>();
/*    */   
/*    */   public ServerDebugSubscribers(MinecraftServer server) {
/* 20 */     this.server = server;
/*    */   }
/*    */   
/*    */   private List<ServerPlayer> getSubscribersFor(DebugSubscription<?> subscription) {
/* 24 */     return this.enabledSubscriptions.getOrDefault(subscription, List.of());
/*    */   }
/*    */   
/*    */   public void tick() {
/* 28 */     this.enabledSubscriptions.values().forEach(List::clear);
/* 29 */     for (ServerPlayer player : (Iterable<ServerPlayer>)this.server.getPlayerList().getPlayers()) {
/* 30 */       for (DebugSubscription<?> subscription : (Iterable<DebugSubscription<?>>)player.debugSubscriptions()) {
/* 31 */         ((List<ServerPlayer>)this.enabledSubscriptions.computeIfAbsent(subscription, s -> new ArrayList())).add(player);
/*    */       }
/*    */     } 
/* 34 */     this.enabledSubscriptions.values().removeIf(List::isEmpty);
/*    */   }
/*    */   
/*    */   public void broadcastToAll(DebugSubscription<?> subscription, Packet<?> packet) {
/* 38 */     for (ServerPlayer player : getSubscribersFor(subscription)) {
/* 39 */       player.connection.send(packet);
/*    */     }
/*    */   }
/*    */   
/*    */   public Set<DebugSubscription<?>> enabledSubscriptions() {
/* 44 */     return Set.copyOf(this.enabledSubscriptions.keySet());
/*    */   }
/*    */   
/*    */   public boolean hasAnySubscriberFor(DebugSubscription<?> subscription) {
/* 48 */     return !getSubscribersFor(subscription).isEmpty();
/*    */   }
/*    */   
/*    */   public boolean hasRequiredPermissions(ServerPlayer player) {
/* 52 */     NameAndId nameAndId = player.nameAndId();
/* 53 */     if (SharedConstants.IS_RUNNING_IN_IDE && this.server.isSingleplayerOwner(nameAndId)) {
/* 54 */       return true;
/*    */     }
/* 56 */     return this.server.getPlayerList().isOp(nameAndId);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/ServerDebugSubscribers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */