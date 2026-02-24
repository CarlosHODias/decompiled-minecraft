/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*    */ import net.minecraft.server.notifications.NotificationManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftApi
/*    */ {
/*    */   private final NotificationManager notificationManager;
/*    */   private final MinecraftAllowListService allowListService;
/*    */   private final MinecraftBanListService banListService;
/*    */   private final MinecraftPlayerListService minecraftPlayerListService;
/*    */   private final MinecraftGameRuleService gameRuleService;
/*    */   private final MinecraftOperatorListService minecraftOperatorListService;
/*    */   private final MinecraftServerSettingsService minecraftServerSettingsService;
/*    */   private final MinecraftServerStateService minecraftServerStateService;
/*    */   private final MinecraftExecutorService executorService;
/*    */   
/*    */   public MinecraftApi(NotificationManager notificationManager, MinecraftAllowListService allowListService, MinecraftBanListService banListService, MinecraftPlayerListService minecraftPlayerListService, MinecraftGameRuleService gameRuleService, MinecraftOperatorListService minecraftOperatorListService, MinecraftServerSettingsService minecraftServerSettingsService, MinecraftServerStateService minecraftServerStateService, MinecraftExecutorService executorService) {
/* 32 */     this.notificationManager = notificationManager;
/* 33 */     this.allowListService = allowListService;
/* 34 */     this.banListService = banListService;
/* 35 */     this.minecraftPlayerListService = minecraftPlayerListService;
/* 36 */     this.gameRuleService = gameRuleService;
/* 37 */     this.minecraftOperatorListService = minecraftOperatorListService;
/* 38 */     this.minecraftServerSettingsService = minecraftServerSettingsService;
/* 39 */     this.minecraftServerStateService = minecraftServerStateService;
/* 40 */     this.executorService = executorService;
/*    */   }
/*    */   
/*    */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
/* 44 */     return this.executorService.submit(supplier);
/*    */   }
/*    */   
/*    */   public CompletableFuture<Void> submit(Runnable runnable) {
/* 48 */     return this.executorService.submit(runnable);
/*    */   }
/*    */   
/*    */   public MinecraftAllowListService allowListService() {
/* 52 */     return this.allowListService;
/*    */   }
/*    */   
/*    */   public MinecraftBanListService banListService() {
/* 56 */     return this.banListService;
/*    */   }
/*    */   
/*    */   public MinecraftPlayerListService playerListService() {
/* 60 */     return this.minecraftPlayerListService;
/*    */   }
/*    */   
/*    */   public MinecraftGameRuleService gameRuleService() {
/* 64 */     return this.gameRuleService;
/*    */   }
/*    */   
/*    */   public MinecraftOperatorListService operatorListService() {
/* 68 */     return this.minecraftOperatorListService;
/*    */   }
/*    */   
/*    */   public MinecraftServerSettingsService serverSettingsService() {
/* 72 */     return this.minecraftServerSettingsService;
/*    */   }
/*    */   
/*    */   public MinecraftServerStateService serverStateService() {
/* 76 */     return this.minecraftServerStateService;
/*    */   }
/*    */   
/*    */   public NotificationManager notificationManager() {
/* 80 */     return this.notificationManager;
/*    */   }
/*    */   
/*    */   public static MinecraftApi of(DedicatedServer server) {
/* 84 */     JsonRpcLogger jsonrpcLogger = new JsonRpcLogger();
/* 85 */     MinecraftAllowListServiceImpl allowListService = new MinecraftAllowListServiceImpl(server, jsonrpcLogger);
/* 86 */     MinecraftBanListServiceImpl banListService = new MinecraftBanListServiceImpl((MinecraftServer)server, jsonrpcLogger);
/* 87 */     MinecraftPlayerListServiceImpl playerListService = new MinecraftPlayerListServiceImpl(server, jsonrpcLogger);
/* 88 */     MinecraftGameRuleServiceImpl gameRuleService = new MinecraftGameRuleServiceImpl(server, jsonrpcLogger);
/* 89 */     MinecraftOperatorListServiceImpl operatorListService = new MinecraftOperatorListServiceImpl((MinecraftServer)server, jsonrpcLogger);
/* 90 */     MinecraftServerSettingsServiceImpl serverSettingsService = new MinecraftServerSettingsServiceImpl(server, jsonrpcLogger);
/* 91 */     MinecraftServerStateServiceImpl serverStateService = new MinecraftServerStateServiceImpl(server, jsonrpcLogger);
/* 92 */     MinecraftExecutorService executorService = new MinecraftExecutorServiceImpl(server);
/* 93 */     return new MinecraftApi(
/* 94 */         server.notificationManager(), allowListService, banListService, playerListService, gameRuleService, operatorListService, serverSettingsService, serverStateService, executorService);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/internalapi/MinecraftApi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */