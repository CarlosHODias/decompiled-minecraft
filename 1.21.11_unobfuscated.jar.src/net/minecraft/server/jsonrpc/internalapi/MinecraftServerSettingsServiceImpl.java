/*     */ package net.minecraft.server.jsonrpc.internalapi;
/*     */ 
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class MinecraftServerSettingsServiceImpl
/*     */   implements MinecraftServerSettingsService {
/*     */   private final DedicatedServer server;
/*     */   private final JsonRpcLogger jsonrpcLogger;
/*     */   
/*     */   public MinecraftServerSettingsServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
/*  16 */     this.server = server;
/*  17 */     this.jsonrpcLogger = jsonrpcLogger;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAutoSave() {
/*  22 */     return this.server.isAutoSave();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setAutoSave(boolean enabled, ClientInfo clientInfo) {
/*  27 */     this.jsonrpcLogger.log(clientInfo, "Update autosave from {} to {}", new Object[] { isAutoSave(), enabled });
/*  28 */     this.server.setAutoSave(enabled);
/*  29 */     return isAutoSave();
/*     */   }
/*     */ 
/*     */   
/*     */   public Difficulty getDifficulty() {
/*  34 */     return this.server.getWorldData().getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Difficulty setDifficulty(Difficulty difficulty, ClientInfo clientInfo) {
/*  39 */     this.jsonrpcLogger.log(clientInfo, "Update difficulty from '{}' to '{}'", new Object[] { getDifficulty(), difficulty });
/*  40 */     this.server.setDifficulty(difficulty);
/*  41 */     return getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnforceWhitelist() {
/*  46 */     return this.server.isEnforceWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setEnforceWhitelist(boolean enforce, ClientInfo clientInfo) {
/*  51 */     this.jsonrpcLogger.log(clientInfo, "Update enforce allowlist from {} to {}", new Object[] { isEnforceWhitelist(), enforce });
/*  52 */     this.server.setEnforceWhitelist(enforce);
/*  53 */     this.server.kickUnlistedPlayers();
/*  54 */     return isEnforceWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsingWhitelist() {
/*  59 */     return this.server.isUsingWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setUsingWhitelist(boolean use, ClientInfo clientInfo) {
/*  64 */     this.jsonrpcLogger.log(clientInfo, "Update using allowlist from {} to {}", new Object[] { isUsingWhitelist(), use });
/*  65 */     this.server.setUsingWhitelist(use);
/*  66 */     this.server.kickUnlistedPlayers();
/*  67 */     return isUsingWhitelist();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPlayers() {
/*  72 */     return this.server.getMaxPlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setMaxPlayers(int maxPlayers, ClientInfo clientInfo) {
/*  77 */     this.jsonrpcLogger.log(clientInfo, "Update max players from {} to {}", new Object[] { getMaxPlayers(), maxPlayers });
/*  78 */     this.server.setMaxPlayers(maxPlayers);
/*  79 */     return getMaxPlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPauseWhenEmptySeconds() {
/*  84 */     return this.server.pauseWhenEmptySeconds();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setPauseWhenEmptySeconds(int emptySeconds, ClientInfo clientInfo) {
/*  89 */     this.jsonrpcLogger.log(clientInfo, "Update pause when empty from {} seconds to {} seconds", new Object[] { getPauseWhenEmptySeconds(), emptySeconds });
/*  90 */     this.server.setPauseWhenEmptySeconds(emptySeconds);
/*  91 */     return getPauseWhenEmptySeconds();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPlayerIdleTimeout() {
/*  96 */     return this.server.playerIdleTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setPlayerIdleTimeout(int idleTime, ClientInfo clientInfo) {
/* 101 */     this.jsonrpcLogger.log(clientInfo, "Update player idle timeout from {} minutes to {} minutes", new Object[] { getPlayerIdleTimeout(), idleTime });
/* 102 */     this.server.setPlayerIdleTimeout(idleTime);
/* 103 */     return getPlayerIdleTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowFlight() {
/* 108 */     return this.server.allowFlight();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setAllowFlight(boolean allow, ClientInfo clientInfo) {
/* 113 */     this.jsonrpcLogger.log(clientInfo, "Update allow flight from {} to {}", new Object[] { allowFlight(), allow });
/* 114 */     this.server.setAllowFlight(allow);
/* 115 */     return allowFlight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSpawnProtectionRadius() {
/* 120 */     return this.server.spawnProtectionRadius();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setSpawnProtectionRadius(int spawnProtection, ClientInfo clientInfo) {
/* 125 */     this.jsonrpcLogger.log(clientInfo, "Update spawn protection radius from {} to {}", new Object[] { getSpawnProtectionRadius(), spawnProtection });
/* 126 */     this.server.setSpawnProtectionRadius(spawnProtection);
/* 127 */     return getSpawnProtectionRadius();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMotd() {
/* 132 */     return this.server.getMotd();
/*     */   }
/*     */ 
/*     */   
/*     */   public String setMotd(String motd, ClientInfo clientInfo) {
/* 137 */     this.jsonrpcLogger.log(clientInfo, "Update MOTD from '{}' to '{}'", new Object[] { getMotd(), motd });
/* 138 */     this.server.setMotd(motd);
/* 139 */     return getMotd();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean forceGameMode() {
/* 144 */     return this.server.forceGameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setForceGameMode(boolean force, ClientInfo clientInfo) {
/* 149 */     this.jsonrpcLogger.log(clientInfo, "Update force game mode from {} to {}", new Object[] { forceGameMode(), force });
/* 150 */     this.server.setForceGameMode(force);
/* 151 */     return forceGameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameMode() {
/* 156 */     return this.server.gameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType setGameMode(GameType gameMode, ClientInfo clientInfo) {
/* 161 */     this.jsonrpcLogger.log(clientInfo, "Update game mode from '{}' to '{}'", new Object[] { getGameMode(), gameMode });
/* 162 */     this.server.setGameMode(gameMode);
/* 163 */     return getGameMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getViewDistance() {
/* 168 */     return this.server.viewDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setViewDistance(int viewDistance, ClientInfo clientInfo) {
/* 173 */     this.jsonrpcLogger.log(clientInfo, "Update view distance from {} to {}", new Object[] { getViewDistance(), viewDistance });
/* 174 */     this.server.setViewDistance(viewDistance);
/* 175 */     return getViewDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSimulationDistance() {
/* 180 */     return this.server.simulationDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setSimulationDistance(int simulationDistance, ClientInfo clientInfo) {
/* 185 */     this.jsonrpcLogger.log(clientInfo, "Update simulation distance from {} to {}", new Object[] { getSimulationDistance(), simulationDistance });
/* 186 */     this.server.setSimulationDistance(simulationDistance);
/* 187 */     return getSimulationDistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsTransfers() {
/* 192 */     return this.server.acceptsTransfers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setAcceptsTransfers(boolean accept, ClientInfo clientInfo) {
/* 197 */     this.jsonrpcLogger.log(clientInfo, "Update accepts transfers from {} to {}", new Object[] { acceptsTransfers(), accept });
/* 198 */     this.server.setAcceptsTransfers(accept);
/* 199 */     return acceptsTransfers();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatusHeartbeatInterval() {
/* 204 */     return this.server.statusHeartbeatInterval();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setStatusHeartbeatInterval(int statusHeartbeatInterval, ClientInfo clientInfo) {
/* 209 */     this.jsonrpcLogger.log(clientInfo, "Update status heartbeat interval from {} to {}", new Object[] { getStatusHeartbeatInterval(), statusHeartbeatInterval });
/* 210 */     this.server.setStatusHeartbeatInterval(statusHeartbeatInterval);
/* 211 */     return getStatusHeartbeatInterval();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet getOperatorUserPermissions() {
/* 216 */     return this.server.operatorUserPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet setOperatorUserPermissions(LevelBasedPermissionSet permissions, ClientInfo clientInfo) {
/* 221 */     this.jsonrpcLogger.log(clientInfo, "Update operator user permission level from {} to {}", new Object[] { getOperatorUserPermissions(), permissions.level() });
/* 222 */     this.server.setOperatorUserPermissions(permissions);
/* 223 */     return getOperatorUserPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hidesOnlinePlayers() {
/* 228 */     return this.server.hidesOnlinePlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setHidesOnlinePlayers(boolean hide, ClientInfo clientInfo) {
/* 233 */     this.jsonrpcLogger.log(clientInfo, "Update hides online players from {} to {}", new Object[] { hidesOnlinePlayers(), hide });
/* 234 */     this.server.setHidesOnlinePlayers(hide);
/* 235 */     return hidesOnlinePlayers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean repliesToStatus() {
/* 240 */     return this.server.repliesToStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setRepliesToStatus(boolean enable, ClientInfo clientInfo) {
/* 245 */     this.jsonrpcLogger.log(clientInfo, "Update replies to status from {} to {}", new Object[] { repliesToStatus(), enable });
/* 246 */     this.server.setRepliesToStatus(enable);
/* 247 */     return repliesToStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityBroadcastRangePercentage() {
/* 252 */     return this.server.entityBroadcastRangePercentage();
/*     */   }
/*     */ 
/*     */   
/*     */   public int setEntityBroadcastRangePercentage(int percentage, ClientInfo clientInfo) {
/* 257 */     this.jsonrpcLogger.log(clientInfo, "Update entity broadcast range percentage from {}% to {}%", new Object[] { getEntityBroadcastRangePercentage(), percentage });
/* 258 */     this.server.setEntityBroadcastRangePercentage(percentage);
/* 259 */     return getEntityBroadcastRangePercentage();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/internalapi/MinecraftServerSettingsServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */