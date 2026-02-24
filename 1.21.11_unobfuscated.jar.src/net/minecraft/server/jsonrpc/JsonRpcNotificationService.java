/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.BanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*     */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class JsonRpcNotificationService
/*     */   implements NotificationService
/*     */ {
/*     */   private final ManagementServer managementServer;
/*     */   private final MinecraftApi minecraftApi;
/*     */   
/*     */   public JsonRpcNotificationService(MinecraftApi minecraftApi, ManagementServer managementServer) {
/*  26 */     this.minecraftApi = minecraftApi;
/*  27 */     this.managementServer = managementServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerJoined(ServerPlayer player) {
/*  32 */     broadcastNotification(OutgoingRpcMethods.PLAYER_JOINED, PlayerDto.from(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerLeft(ServerPlayer player) {
/*  37 */     broadcastNotification(OutgoingRpcMethods.PLAYER_LEFT, PlayerDto.from(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverStarted() {
/*  42 */     broadcastNotification(OutgoingRpcMethods.SERVER_STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverShuttingDown() {
/*  47 */     broadcastNotification(OutgoingRpcMethods.SERVER_SHUTTING_DOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveStarted() {
/*  52 */     broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveCompleted() {
/*  57 */     broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_COMPLETED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverActivityOccured() {
/*  62 */     broadcastNotification(OutgoingRpcMethods.SERVER_ACTIVITY_OCCURRED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerOped(ServerOpListEntry operator) {
/*  67 */     broadcastNotification(OutgoingRpcMethods.PLAYER_OPED, OperatorService.OperatorDto.from(operator));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDeoped(ServerOpListEntry operator) {
/*  72 */     broadcastNotification(OutgoingRpcMethods.PLAYER_DEOPED, OperatorService.OperatorDto.from(operator));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerAddedToAllowlist(NameAndId player) {
/*  77 */     broadcastNotification(OutgoingRpcMethods.PLAYER_ADDED_TO_ALLOWLIST, PlayerDto.from(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerRemovedFromAllowlist(NameAndId player) {
/*  82 */     broadcastNotification(OutgoingRpcMethods.PLAYER_REMOVED_FROM_ALLOWLIST, PlayerDto.from(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipBanned(IpBanListEntry ban) {
/*  87 */     broadcastNotification(OutgoingRpcMethods.IP_BANNED, IpBanlistService.IpBanDto.from(ban));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipUnbanned(String ip) {
/*  92 */     broadcastNotification(OutgoingRpcMethods.IP_UNBANNED, ip);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerBanned(UserBanListEntry ban) {
/*  97 */     broadcastNotification(OutgoingRpcMethods.PLAYER_BANNED, BanlistService.UserBanDto.from(ban));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerUnbanned(NameAndId player) {
/* 102 */     broadcastNotification(OutgoingRpcMethods.PLAYER_UNBANNED, PlayerDto.from(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {
/* 107 */     broadcastNotification(OutgoingRpcMethods.GAMERULE_CHANGED, GameRulesService.getTypedRule(this.minecraftApi, gameRule, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void statusHeartbeat() {
/* 112 */     broadcastNotification(OutgoingRpcMethods.STATUS_HEARTBEAT, ServerStateService.status(this.minecraftApi));
/*     */   }
/*     */   
/*     */   private void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Void, ?>> method) {
/* 116 */     this.managementServer.forEachConnection(connection -> connection.sendNotification(method));
/*     */   }
/*     */   
/*     */   private <Params> void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Params, ?>> method, Params params) {
/* 120 */     this.managementServer.forEachConnection(connection -> connection.sendNotification(method, params));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/JsonRpcNotificationService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */