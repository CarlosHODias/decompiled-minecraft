/*     */ package net.minecraft.server.notifications;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.IpBanListEntry;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.ServerOpListEntry;
/*     */ import net.minecraft.server.players.UserBanListEntry;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ 
/*     */ public class NotificationManager
/*     */   implements NotificationService
/*     */ {
/*  15 */   private final List<NotificationService> notificationServices = Lists.newArrayList();
/*     */   
/*     */   public void registerService(NotificationService notificationService) {
/*  18 */     this.notificationServices.add(notificationService);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerJoined(ServerPlayer player) {
/*  23 */     this.notificationServices.forEach(notificationService -> notificationService.playerJoined(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerLeft(ServerPlayer player) {
/*  28 */     this.notificationServices.forEach(notificationService -> notificationService.playerLeft(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverStarted() {
/*  33 */     this.notificationServices.forEach(NotificationService::serverStarted);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverShuttingDown() {
/*  38 */     this.notificationServices.forEach(NotificationService::serverShuttingDown);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveStarted() {
/*  43 */     this.notificationServices.forEach(NotificationService::serverSaveStarted);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSaveCompleted() {
/*  48 */     this.notificationServices.forEach(NotificationService::serverSaveCompleted);
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverActivityOccured() {
/*  53 */     this.notificationServices.forEach(NotificationService::serverActivityOccured);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerOped(ServerOpListEntry operator) {
/*  58 */     this.notificationServices.forEach(notificationService -> notificationService.playerOped(operator));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerDeoped(ServerOpListEntry operator) {
/*  63 */     this.notificationServices.forEach(notificationService -> notificationService.playerDeoped(operator));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerAddedToAllowlist(NameAndId player) {
/*  68 */     this.notificationServices.forEach(notificationService -> notificationService.playerAddedToAllowlist(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerRemovedFromAllowlist(NameAndId player) {
/*  73 */     this.notificationServices.forEach(notificationService -> notificationService.playerRemovedFromAllowlist(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipBanned(IpBanListEntry ban) {
/*  78 */     this.notificationServices.forEach(notificationService -> notificationService.ipBanned(ban));
/*     */   }
/*     */ 
/*     */   
/*     */   public void ipUnbanned(String ip) {
/*  83 */     this.notificationServices.forEach(notificationService -> notificationService.ipUnbanned(ip));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerBanned(UserBanListEntry ban) {
/*  88 */     this.notificationServices.forEach(notificationService -> notificationService.playerBanned(ban));
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerUnbanned(NameAndId player) {
/*  93 */     this.notificationServices.forEach(notificationService -> notificationService.playerUnbanned(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {
/*  98 */     this.notificationServices.forEach(notificationService -> notificationService.onGameRuleChanged(gameRule, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void statusHeartbeat() {
/* 103 */     this.notificationServices.forEach(NotificationService::statusHeartbeat);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/notifications/NotificationManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */