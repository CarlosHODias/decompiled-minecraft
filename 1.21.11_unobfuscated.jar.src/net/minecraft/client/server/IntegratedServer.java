/*     */ package net.minecraft.client.server;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.gizmos.GizmoCollector;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.ModCheck;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.debugchart.SampleLogger;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class IntegratedServer extends MinecraftServer {
/*  50 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MIN_SIM_DISTANCE = 2;
/*     */   
/*     */   public static final int MAX_PLAYERS = 8;
/*     */   private final Minecraft minecraft;
/*     */   private boolean paused = true;
/*  57 */   private int publishedPort = -1;
/*     */   private GameType publishedGameType;
/*     */   private LanServerPinger lanPinger;
/*     */   private UUID uuid;
/*  61 */   private int previousSimulationDistance = 0;
/*  62 */   private volatile List<SimpleGizmoCollector.GizmoInstance> latestTicksGizmos = new ArrayList<>();
/*  63 */   private final SimpleGizmoCollector gizmoCollector = new SimpleGizmoCollector();
/*     */   
/*     */   public IntegratedServer(Thread serverThread, Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Services services, LevelLoadListener levelLoadListener) {
/*  66 */     super(serverThread, levelStorageAccess, packRepository, worldStem, minecraft.getProxy(), minecraft.getFixerUpper(), services, levelLoadListener);
/*     */     
/*  68 */     setSingleplayerProfile(minecraft.getGameProfile());
/*  69 */     setDemo(minecraft.isDemo());
/*  70 */     setPlayerList(new IntegratedPlayerList(this, registries(), this.playerDataStorage));
/*     */     
/*  72 */     this.minecraft = minecraft;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean initServer() {
/*  77 */     LOGGER.info("Starting integrated minecraft server version {}", SharedConstants.getCurrentVersion().name());
/*     */     
/*  79 */     setUsesAuthentication(true);
/*     */     
/*  81 */     initializeKeyPair();
/*     */     
/*  83 */     loadLevel();
/*     */     
/*  85 */     GameProfile host = getSingleplayerProfile();
/*  86 */     String levelName = getWorldData().getLevelName();
/*  87 */     setMotd((host != null) ? (host.name() + " - " + host.name()) : levelName);
/*     */     
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPaused() {
/*  94 */     return this.paused;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processPacketsAndTick(boolean sprinting) {
/*  99 */     Gizmos.TemporaryCollection ignored = Gizmos.withCollector((GizmoCollector)this.gizmoCollector); 
/* 100 */     try { super.processPacketsAndTick(sprinting);
/* 101 */       if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/* 102 */         try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (tickRateManager().runsNormally()) {
/* 103 */       this.latestTicksGizmos = this.gizmoCollector.drainGizmos();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickServer(BooleanSupplier haveTime) {
/* 109 */     boolean wasPaused = this.paused;
/*     */     
/* 111 */     this.paused = (Minecraft.getInstance().isPaused() || getPlayerList().getPlayers().isEmpty());
/*     */     
/* 113 */     ProfilerFiller profiler = Profiler.get();
/* 114 */     if (!wasPaused && this.paused) {
/* 115 */       profiler.push("autoSave");
/* 116 */       LOGGER.info("Saving and pausing game...");
/* 117 */       saveEverything(false, false, false);
/* 118 */       profiler.pop();
/*     */     } 
/*     */     
/* 121 */     if (this.paused) {
/* 122 */       tickPaused();
/*     */       
/*     */       return;
/*     */     } 
/* 126 */     if (wasPaused)
/*     */     {
/*     */       
/* 129 */       forceTimeSynchronization();
/*     */     }
/*     */     
/* 132 */     super.tickServer(haveTime);
/*     */     
/* 134 */     int serverViewDistance = Math.max(2, (Integer)this.minecraft.options.renderDistance().get());
/* 135 */     if (serverViewDistance != getPlayerList().getViewDistance()) {
/* 136 */       LOGGER.info("Changing view distance to {}, from {}", serverViewDistance, getPlayerList().getViewDistance());
/* 137 */       getPlayerList().setViewDistance(serverViewDistance);
/*     */     } 
/* 139 */     int serverSimulationDistance = Math.max(2, (Integer)this.minecraft.options.simulationDistance().get());
/* 140 */     if (serverSimulationDistance != this.previousSimulationDistance) {
/* 141 */       LOGGER.info("Changing simulation distance to {}, from {}", serverSimulationDistance, this.previousSimulationDistance);
/* 142 */       getPlayerList().setSimulationDistance(serverSimulationDistance);
/* 143 */       this.previousSimulationDistance = serverSimulationDistance;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected net.minecraft.util.debugchart.LocalSampleLogger getTickTimeLogger() {
/* 149 */     return this.minecraft.getDebugOverlay().getTickTimeLogger();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTickTimeLoggingEnabled() {
/* 154 */     return true;
/*     */   }
/*     */   
/*     */   private void tickPaused() {
/* 158 */     tickConnection();
/* 159 */     for (ServerPlayer player : (Iterable<ServerPlayer>)getPlayerList().getPlayers()) {
/* 160 */       player.awardStat(Stats.TOTAL_WORLD_TIME);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRconBroadcast() {
/* 166 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Path getServerDirectory() {
/* 176 */     return this.minecraft.gameDirectory.toPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRateLimitPacketsPerSecond() {
/* 186 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useNativeTransport() {
/* 191 */     return this.minecraft.options.useNativeTransport();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onServerCrash(CrashReport report) {
/* 196 */     this.minecraft.delayCrashRaw(report);
/*     */   }
/*     */ 
/*     */   
/*     */   public SystemReport fillServerSystemReport(SystemReport systemReport) {
/* 201 */     systemReport.setDetail("Type", "Integrated Server");
/* 202 */     systemReport.setDetail("Is Modded", () -> getModdedStatus().fullDescription());
/* 203 */     Objects.requireNonNull(this.minecraft); systemReport.setDetail("Launched Version", this.minecraft::getLaunchedVersion);
/*     */     
/* 205 */     return systemReport;
/*     */   }
/*     */ 
/*     */   
/*     */   public ModCheck getModdedStatus() {
/* 210 */     return Minecraft.checkModStatus().merge(super.getModdedStatus());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean publishServer(GameType gameMode, boolean allowCommands, int port) {
/*     */     try {
/* 216 */       this.minecraft.prepareForMultiplayer();
/* 217 */       this.minecraft.getConnection().prepareKeyPair();
/*     */       
/* 219 */       getConnection().startTcpServerListener(null, port);
/* 220 */       LOGGER.info("Started serving on {}", port);
/* 221 */       this.publishedPort = port;
/*     */       
/* 223 */       this.lanPinger = new LanServerPinger(getMotd(), "" + port);
/* 224 */       this.lanPinger.start();
/*     */       
/* 226 */       this.publishedGameType = gameMode;
/* 227 */       getPlayerList().setAllowCommandsForAllPlayers(allowCommands);
/* 228 */       LevelBasedPermissionSet levelBasedPermissionSet = getProfilePermissions(this.minecraft.player.nameAndId());
/* 229 */       this.minecraft.player.setPermissions((PermissionSet)levelBasedPermissionSet);
/* 230 */       for (ServerPlayer player : (Iterable<ServerPlayer>)getPlayerList().getPlayers()) {
/* 231 */         getCommands().sendCommands(player);
/*     */       }
/*     */       
/* 234 */       return true;
/* 235 */     } catch (IOException iOException) {
/*     */       
/* 237 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopServer() {
/* 242 */     super.stopServer();
/*     */     
/* 244 */     if (this.lanPinger != null) {
/* 245 */       this.lanPinger.interrupt();
/* 246 */       this.lanPinger = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void halt(boolean wait) {
/* 252 */     executeBlocking(() -> {
/*     */           List<ServerPlayer> players = Lists.newArrayList(getPlayerList().getPlayers());
/*     */           
/*     */           for (ServerPlayer player : players) {
/*     */             if (!player.getUUID().equals(this.uuid)) {
/*     */               getPlayerList().remove(player);
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 262 */     super.halt(wait);
/*     */     
/* 264 */     if (this.lanPinger != null) {
/* 265 */       this.lanPinger.interrupt();
/* 266 */       this.lanPinger = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublished() {
/* 272 */     return (this.publishedPort > -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 277 */     return this.publishedPort;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultGameType(GameType gameType) {
/* 282 */     super.setDefaultGameType(gameType);
/*     */     
/* 284 */     this.publishedGameType = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet operatorUserPermissions() {
/* 289 */     return LevelBasedPermissionSet.GAMEMASTER;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelBasedPermissionSet getFunctionCompilationPermissions() {
/* 294 */     return LevelBasedPermissionSet.GAMEMASTER;
/*     */   }
/*     */   
/*     */   public void setUUID(UUID uuid) {
/* 298 */     this.uuid = uuid;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleplayerOwner(NameAndId nameAndId) {
/* 303 */     return (getSingleplayerProfile() != null && nameAndId.name().equalsIgnoreCase(getSingleplayerProfile().name()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScaledTrackingDistance(int baseRange) {
/* 308 */     return (int)((Double)this.minecraft.options.entityDistanceScaling().get() * baseRange);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean forceSynchronousWrites() {
/* 313 */     return this.minecraft.options.syncWrites;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getForcedGameType() {
/* 318 */     if (isPublished() && !isHardcore()) {
/* 319 */       return (GameType)MoreObjects.firstNonNull(this.publishedGameType, this.worldData.getGameType());
/*     */     }
/* 321 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GlobalPos selectLevelLoadFocusPos() {
/* 326 */     CompoundTag loadedPlayerTag = this.worldData.getLoadedPlayerTag();
/* 327 */     if (loadedPlayerTag == null) {
/* 328 */       return super.selectLevelLoadFocusPos();
/*     */     }
/* 330 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER); 
/* 331 */     try { ValueInput input = TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)registryAccess(), loadedPlayerTag);
/* 332 */       ServerPlayer.SavedPosition loadedPosition = input.read(ServerPlayer.SavedPosition.MAP_CODEC).orElse(ServerPlayer.SavedPosition.EMPTY);
/* 333 */       if (loadedPosition.dimension().isPresent() && loadedPosition.position().isPresent())
/* 334 */       { GlobalPos globalPos = new GlobalPos(loadedPosition.dimension().get(), BlockPos.containing(loadedPosition.position().get()));
/*     */         
/* 336 */         reporter.close(); return globalPos; }  reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 337 */      return super.selectLevelLoadFocusPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveEverything(boolean silent, boolean flush, boolean force) {
/* 342 */     boolean retval = super.saveEverything(silent, flush, force);
/* 343 */     warnOnLowDiskSpace();
/* 344 */     return retval;
/*     */   }
/*     */   
/*     */   private void warnOnLowDiskSpace() {
/* 348 */     if (this.storageSource.checkForLowDiskSpace()) {
/* 349 */       this.minecraft.execute(() -> SystemToast.onLowDiskSpace(this.minecraft));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportChunkLoadFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 355 */     super.reportChunkLoadFailure(throwable, storageInfo, pos);
/* 356 */     warnOnLowDiskSpace();
/* 357 */     this.minecraft.execute(() -> SystemToast.onChunkLoadFailure(this.minecraft, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void reportChunkSaveFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 362 */     super.reportChunkSaveFailure(throwable, storageInfo, pos);
/* 363 */     warnOnLowDiskSpace();
/* 364 */     this.minecraft.execute(() -> SystemToast.onChunkSaveFailure(this.minecraft, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPlayers() {
/* 369 */     return 8;
/*     */   }
/*     */   
/*     */   public Collection<SimpleGizmoCollector.GizmoInstance> getPerTickGizmos() {
/* 373 */     return this.latestTicksGizmos;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/server/IntegratedServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */