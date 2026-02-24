/*     */ package net.minecraft.server.jsonrpc.methods;
/*     */ 
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class ServerSettingsService
/*     */ {
/*     */   public static boolean autosave(MinecraftApi minecraftApi) {
/*  12 */     return minecraftApi.serverSettingsService().isAutoSave();
/*     */   }
/*     */   
/*     */   public static boolean setAutosave(MinecraftApi minecraftApi, boolean enabled, ClientInfo clientInfo) {
/*  16 */     return minecraftApi.serverSettingsService().setAutoSave(enabled, clientInfo);
/*     */   }
/*     */   
/*     */   public static Difficulty difficulty(MinecraftApi minecraftApi) {
/*  20 */     return minecraftApi.serverSettingsService().getDifficulty();
/*     */   }
/*     */   
/*     */   public static Difficulty setDifficulty(MinecraftApi minecraftApi, Difficulty difficulty, ClientInfo clientInfo) {
/*  24 */     return minecraftApi.serverSettingsService().setDifficulty(difficulty, clientInfo);
/*     */   }
/*     */   
/*     */   public static boolean enforceAllowlist(MinecraftApi minecraftApi) {
/*  28 */     return minecraftApi.serverSettingsService().isEnforceWhitelist();
/*     */   }
/*     */   
/*     */   public static boolean setEnforceAllowlist(MinecraftApi minecraftApi, boolean enforce, ClientInfo clientInfo) {
/*  32 */     return minecraftApi.serverSettingsService().setEnforceWhitelist(enforce, clientInfo);
/*     */   }
/*     */   
/*     */   public static boolean usingAllowlist(MinecraftApi minecraftApi) {
/*  36 */     return minecraftApi.serverSettingsService().isUsingWhitelist();
/*     */   }
/*     */   
/*     */   public static boolean setUsingAllowlist(MinecraftApi minecraftApi, boolean use, ClientInfo clientInfo) {
/*  40 */     return minecraftApi.serverSettingsService().setUsingWhitelist(use, clientInfo);
/*     */   }
/*     */   
/*     */   public static int maxPlayers(MinecraftApi minecraftApi) {
/*  44 */     return minecraftApi.serverSettingsService().getMaxPlayers();
/*     */   }
/*     */   
/*     */   public static int setMaxPlayers(MinecraftApi minecraftApi, int maxPlayers, ClientInfo clientInfo) {
/*  48 */     return minecraftApi.serverSettingsService().setMaxPlayers(maxPlayers, clientInfo);
/*     */   }
/*     */   
/*     */   public static int pauseWhenEmpty(MinecraftApi minecraftApi) {
/*  52 */     return minecraftApi.serverSettingsService().getPauseWhenEmptySeconds();
/*     */   }
/*     */   
/*     */   public static int setPauseWhenEmpty(MinecraftApi minecraftApi, int emptySeconds, ClientInfo clientInfo) {
/*  56 */     return minecraftApi.serverSettingsService().setPauseWhenEmptySeconds(emptySeconds, clientInfo);
/*     */   }
/*     */   
/*     */   public static int playerIdleTimeout(MinecraftApi minecraftApi) {
/*  60 */     return minecraftApi.serverSettingsService().getPlayerIdleTimeout();
/*     */   }
/*     */   
/*     */   public static int setPlayerIdleTimeout(MinecraftApi minecraftApi, int idleTime, ClientInfo clientInfo) {
/*  64 */     return minecraftApi.serverSettingsService().setPlayerIdleTimeout(idleTime, clientInfo);
/*     */   }
/*     */   
/*     */   public static boolean allowFlight(MinecraftApi minecraftApi) {
/*  68 */     return minecraftApi.serverSettingsService().allowFlight();
/*     */   }
/*     */   
/*     */   public static boolean setAllowFlight(MinecraftApi minecraftApi, boolean allow, ClientInfo clientInfo) {
/*  72 */     return minecraftApi.serverSettingsService().setAllowFlight(allow, clientInfo);
/*     */   }
/*     */   
/*     */   public static int spawnProtection(MinecraftApi minecraftApi) {
/*  76 */     return minecraftApi.serverSettingsService().getSpawnProtectionRadius();
/*     */   }
/*     */   
/*     */   public static int setSpawnProtection(MinecraftApi minecraftApi, int spawnProtection, ClientInfo clientInfo) {
/*  80 */     return minecraftApi.serverSettingsService().setSpawnProtectionRadius(spawnProtection, clientInfo);
/*     */   }
/*     */   
/*     */   public static String motd(MinecraftApi minecraftApi) {
/*  84 */     return minecraftApi.serverSettingsService().getMotd();
/*     */   }
/*     */   
/*     */   public static String setMotd(MinecraftApi minecraftApi, String motd, ClientInfo clientInfo) {
/*  88 */     return minecraftApi.serverSettingsService().setMotd(motd, clientInfo);
/*     */   }
/*     */   
/*     */   public static boolean forceGameMode(MinecraftApi minecraftApi) {
/*  92 */     return minecraftApi.serverSettingsService().forceGameMode();
/*     */   }
/*     */   
/*     */   public static boolean setForceGameMode(MinecraftApi minecraftApi, boolean force, ClientInfo clientInfo) {
/*  96 */     return minecraftApi.serverSettingsService().setForceGameMode(force, clientInfo);
/*     */   }
/*     */   
/*     */   public static GameType gameMode(MinecraftApi minecraftApi) {
/* 100 */     return minecraftApi.serverSettingsService().getGameMode();
/*     */   }
/*     */   
/*     */   public static GameType setGameMode(MinecraftApi minecraftApi, GameType gameMode, ClientInfo clientInfo) {
/* 104 */     return minecraftApi.serverSettingsService().setGameMode(gameMode, clientInfo);
/*     */   }
/*     */   
/*     */   public static int viewDistance(MinecraftApi minecraftApi) {
/* 108 */     return minecraftApi.serverSettingsService().getViewDistance();
/*     */   }
/*     */   
/*     */   public static int setViewDistance(MinecraftApi minecraftApi, int viewDistance, ClientInfo clientInfo) {
/* 112 */     return minecraftApi.serverSettingsService().setViewDistance(viewDistance, clientInfo);
/*     */   }
/*     */   
/*     */   public static int simulationDistance(MinecraftApi minecraftApi) {
/* 116 */     return minecraftApi.serverSettingsService().getSimulationDistance();
/*     */   }
/*     */   
/*     */   public static int setSimulationDistance(MinecraftApi minecraftApi, int simulationDistance, ClientInfo clientInfo) {
/* 120 */     return minecraftApi.serverSettingsService().setSimulationDistance(simulationDistance, clientInfo);
/*     */   }
/*     */   
/*     */   public static boolean acceptTransfers(MinecraftApi minecraftApi) {
/* 124 */     return minecraftApi.serverSettingsService().acceptsTransfers();
/*     */   }
/*     */   
/*     */   public static boolean setAcceptTransfers(MinecraftApi minecraftApi, boolean accept, ClientInfo clientInfo) {
/* 128 */     return minecraftApi.serverSettingsService().setAcceptsTransfers(accept, clientInfo);
/*     */   }
/*     */   
/*     */   public static int statusHeartbeatInterval(MinecraftApi minecraftApi) {
/* 132 */     return minecraftApi.serverSettingsService().getStatusHeartbeatInterval();
/*     */   }
/*     */   
/*     */   public static int setStatusHeartbeatInterval(MinecraftApi minecraftApi, int statusHeartbeatInterval, ClientInfo clientInfo) {
/* 136 */     return minecraftApi.serverSettingsService().setStatusHeartbeatInterval(statusHeartbeatInterval, clientInfo);
/*     */   }
/*     */   
/*     */   public static PermissionLevel operatorUserPermissionLevel(MinecraftApi minecraftApi) {
/* 140 */     return minecraftApi.serverSettingsService().getOperatorUserPermissions().level();
/*     */   }
/*     */   
/*     */   public static PermissionLevel setOperatorUserPermissionLevel(MinecraftApi minecraftApi, PermissionLevel level, ClientInfo clientInfo) {
/* 144 */     return minecraftApi.serverSettingsService().setOperatorUserPermissions(LevelBasedPermissionSet.forLevel(level), clientInfo).level();
/*     */   }
/*     */   
/*     */   public static boolean hidesOnlinePlayers(MinecraftApi minecraftApi) {
/* 148 */     return minecraftApi.serverSettingsService().hidesOnlinePlayers();
/*     */   }
/*     */   
/*     */   public static boolean setHidesOnlinePlayers(MinecraftApi minecraftApi, boolean hide, ClientInfo clientInfo) {
/* 152 */     return minecraftApi.serverSettingsService().setHidesOnlinePlayers(hide, clientInfo);
/*     */   }
/*     */   
/*     */   public static boolean repliesToStatus(MinecraftApi minecraftApi) {
/* 156 */     return minecraftApi.serverSettingsService().repliesToStatus();
/*     */   }
/*     */   
/*     */   public static boolean setRepliesToStatus(MinecraftApi minecraftApi, boolean enable, ClientInfo clientInfo) {
/* 160 */     return minecraftApi.serverSettingsService().setRepliesToStatus(enable, clientInfo);
/*     */   }
/*     */   
/*     */   public static int entityBroadcastRangePercentage(MinecraftApi minecraftApi) {
/* 164 */     return minecraftApi.serverSettingsService().getEntityBroadcastRangePercentage();
/*     */   }
/*     */   
/*     */   public static int setEntityBroadcastRangePercentage(MinecraftApi minecraftApi, int percentage, ClientInfo clientInfo) {
/* 168 */     return minecraftApi.serverSettingsService().setEntityBroadcastRangePercentage(percentage, clientInfo);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/methods/ServerSettingsService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */