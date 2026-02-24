/*      */ package net.minecraft.server;
/*      */ import com.google.common.base.Splitter;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.jtracy.DiscontinuousFrame;
/*      */ import com.mojang.jtracy.TracyClient;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.management.ThreadInfo;
/*      */ import java.lang.management.ThreadMXBean;
/*      */ import java.net.Proxy;
/*      */ import java.nio.file.FileStore;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.security.KeyPair;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionStage;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.LongSupplier;
/*      */ import java.util.stream.Collectors;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.SystemReport;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.LayeredRegistryAccess;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
/*      */ import net.minecraft.gametest.framework.GameTestTicker;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.PacketProcessor;
/*      */ import net.minecraft.network.chat.ChatDecorator;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketType;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*      */ import net.minecraft.network.protocol.status.ServerStatus;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.bossevents.CustomBossEvents;
/*      */ import net.minecraft.server.level.ChunkLoadCounter;
/*      */ import net.minecraft.server.level.ChunkMap;
/*      */ import net.minecraft.server.level.DemoMode;
/*      */ import net.minecraft.server.level.PlayerSpawnFinder;
/*      */ import net.minecraft.server.level.ServerChunkCache;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.level.ServerPlayerGameMode;
/*      */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*      */ import net.minecraft.server.level.progress.LevelLoadListener;
/*      */ import net.minecraft.server.network.ServerConnectionListener;
/*      */ import net.minecraft.server.network.TextFilter;
/*      */ import net.minecraft.server.notifications.NotificationManager;
/*      */ import net.minecraft.server.notifications.ServerActivityMonitor;
/*      */ import net.minecraft.server.packs.PackType;
/*      */ import net.minecraft.server.packs.repository.Pack;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.server.packs.repository.PackSource;
/*      */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*      */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*      */ import net.minecraft.server.packs.resources.ResourceManager;
/*      */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.players.NameAndId;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.server.players.ServerOpListEntry;
/*      */ import net.minecraft.server.players.UserWhiteList;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.tags.TagLoader;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.CryptException;
/*      */ import net.minecraft.util.FileUtil;
/*      */ import net.minecraft.util.ModCheck;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.NativeModuleLister;
/*      */ import net.minecraft.util.PngInfo;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*      */ import net.minecraft.util.debugchart.SampleLogger;
/*      */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*      */ import net.minecraft.util.profiling.EmptyProfileResults;
/*      */ import net.minecraft.util.profiling.ProfileResults;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.profiling.ResultField;
/*      */ import net.minecraft.util.profiling.SingleTickProfiler;
/*      */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*      */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*      */ import net.minecraft.util.profiling.metrics.profiling.ActiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.InactiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.ServerMetricsSamplersProvider;
/*      */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*      */ import net.minecraft.util.thread.ReentrantBlockableEventLoop;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.RandomSequences;
/*      */ import net.minecraft.world.Stopwatches;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.ai.village.VillageSiege;
/*      */ import net.minecraft.world.entity.npc.CatSpawner;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.DataPackConfig;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.LevelSettings;
/*      */ import net.minecraft.world.level.TicketStorage;
/*      */ import net.minecraft.world.level.WorldDataConfiguration;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
/*      */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*      */ import net.minecraft.world.level.dimension.LevelStem;
/*      */ import net.minecraft.world.level.gamerules.GameRule;
/*      */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.PatrolSpawner;
/*      */ import net.minecraft.world.level.levelgen.PhantomSpawner;
/*      */ import net.minecraft.world.level.levelgen.WorldOptions;
/*      */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.saveddata.SavedData;
/*      */ import net.minecraft.world.level.storage.CommandStorage;
/*      */ import net.minecraft.world.level.storage.DerivedLevelData;
/*      */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.LevelResource;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*      */ import net.minecraft.world.level.storage.ServerLevelData;
/*      */ import net.minecraft.world.level.storage.WorldData;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.ScoreboardSaveData;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public abstract class MinecraftServer extends ReentrantBlockableEventLoop<TickTask> implements CommandSource, ServerInfo, ChunkIOErrorReporter {
/*  191 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   public static final String VANILLA_BRAND = "vanilla";
/*      */   private static final float AVERAGE_TICK_TIME_SMOOTHING = 0.8F;
/*      */   private static final int TICK_STATS_SPAN = 100;
/*  195 */   private static final long OVERLOADED_THRESHOLD_NANOS = 20L * TimeUtil.NANOSECONDS_PER_SECOND / 20L;
/*      */   private static final int OVERLOADED_TICKS_THRESHOLD = 20;
/*  197 */   private static final long OVERLOADED_WARNING_INTERVAL_NANOS = 10L * TimeUtil.NANOSECONDS_PER_SECOND;
/*      */   private static final int OVERLOADED_TICKS_WARNING_INTERVAL = 100;
/*  199 */   private static final long STATUS_EXPIRE_TIME_NANOS = 5L * TimeUtil.NANOSECONDS_PER_SECOND;
/*  200 */   private static final long PREPARE_LEVELS_DEFAULT_DELAY_NANOS = 10L * TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*      */   
/*      */   private static final int MAX_STATUS_PLAYER_SAMPLE = 12;
/*      */   
/*      */   public static final int SPAWN_POSITION_SEARCH_RADIUS = 5;
/*      */   
/*      */   private static final int SERVER_ACTIVITY_MONITOR_SECONDS_BETWEEN_NOTIFICATIONS = 30;
/*      */   private static final int AUTOSAVE_INTERVAL = 6000;
/*      */   private static final int MIMINUM_AUTOSAVE_TICKS = 100;
/*      */   private static final int MAX_TICK_LATENCY = 3;
/*      */   public static final int ABSOLUTE_MAX_WORLD_SIZE = 29999984;
/*  211 */   public static final LevelSettings DEMO_SETTINGS = new LevelSettings("Demo World", GameType.SURVIVAL, false, Difficulty.NORMAL, false, new GameRules(FeatureFlags.DEFAULT_FLAGS), WorldDataConfiguration.DEFAULT);
/*  212 */   public static final NameAndId ANONYMOUS_PLAYER_PROFILE = new NameAndId(Util.NIL_UUID, "Anonymous Player");
/*      */   
/*      */   protected final LevelStorageSource.LevelStorageAccess storageSource;
/*      */   protected final PlayerDataStorage playerDataStorage;
/*  216 */   private final List<Runnable> tickables = Lists.newArrayList();
/*  217 */   private MetricsRecorder metricsRecorder = InactiveMetricsRecorder.INSTANCE; private Consumer<ProfileResults> onMetricsRecordingStopped = results -> stopRecordingMetrics();
/*      */   private Consumer<Path> onMetricsRecordingFinished = ignored -> {
/*      */     
/*      */     };
/*      */   private boolean willStartRecordingMetrics;
/*      */   private TimeProfiler debugCommandProfiler;
/*      */   private boolean debugCommandProfilerDelayStart;
/*      */   private final ServerConnectionListener connection;
/*      */   private final LevelLoadListener levelLoadListener;
/*      */   private ServerStatus status;
/*      */   private ServerStatus.Favicon statusIcon;
/*  228 */   private final RandomSource random = RandomSource.create();
/*      */   
/*      */   private final DataFixer fixerUpper;
/*      */   private String localIp;
/*  232 */   private int port = -1;
/*      */   private final LayeredRegistryAccess<RegistryLayer> registries;
/*  234 */   private final Map<ResourceKey<Level>, ServerLevel> levels = Maps.newLinkedHashMap();
/*      */   private PlayerList playerList;
/*      */   private volatile boolean running = true;
/*      */   private boolean stopped;
/*      */   private int tickCount;
/*  239 */   private int ticksUntilAutosave = 6000;
/*      */   
/*      */   protected final Proxy proxy;
/*      */   
/*      */   private boolean onlineMode;
/*      */   private boolean preventProxyConnections;
/*      */   private String motd;
/*      */   private int playerIdleTimeout;
/*  247 */   private final long[] tickTimesNanos = new long[100];
/*  248 */   private long aggregatedTickTimesNanos = 0L;
/*      */   
/*      */   private KeyPair keyPair;
/*      */   private GameProfile singleplayerProfile;
/*      */   private boolean isDemo;
/*      */   private volatile boolean isReady;
/*      */   private long lastOverloadWarningNanos;
/*      */   protected final Services services;
/*      */   private final NotificationManager notificationManager;
/*      */   private final ServerActivityMonitor serverActivityMonitor;
/*      */   private long lastServerStatus;
/*      */   private final Thread serverThread;
/*  260 */   private long lastTickNanos = Util.getNanos();
/*  261 */   private long taskExecutionStartNanos = Util.getNanos();
/*      */   private long idleTimeNanos;
/*  263 */   private long nextTickTimeNanos = Util.getNanos();
/*      */   private boolean waitingForNextTick = false;
/*      */   private long delayedTasksMaxNextTickTimeNanos;
/*      */   private boolean mayHaveDelayedTasks;
/*      */   private final PackRepository packRepository;
/*  268 */   private final ServerScoreboard scoreboard = new ServerScoreboard(this);
/*      */   private Stopwatches stopwatches;
/*      */   private CommandStorage commandStorage;
/*  271 */   private final CustomBossEvents customBossEvents = new CustomBossEvents();
/*      */   
/*      */   private final ServerFunctionManager functionManager;
/*      */   private boolean enforceWhitelist;
/*      */   private boolean usingWhitelist;
/*      */   private float smoothedTickTimeMillis;
/*      */   private final Executor executor;
/*      */   private String serverId;
/*      */   private ReloadableResources resources;
/*      */   private final StructureTemplateManager structureTemplateManager;
/*      */   private final ServerTickRateManager tickRateManager;
/*  282 */   private final ServerDebugSubscribers debugSubscribers = new ServerDebugSubscribers(this);
/*      */   
/*      */   protected final WorldData worldData;
/*  285 */   private LevelData.RespawnData effectiveRespawnData = LevelData.RespawnData.DEFAULT;
/*      */   
/*      */   private final PotionBrewing potionBrewing;
/*      */   private FuelValues fuelValues;
/*      */   private int emptyTicks;
/*      */   private volatile boolean isSaving;
/*  291 */   private static final AtomicReference<RuntimeException> fatalException = new AtomicReference<>();
/*  292 */   private final SuppressedExceptionCollector suppressedExceptions = new SuppressedExceptionCollector();
/*      */   
/*      */   private final DiscontinuousFrame tickFrame;
/*      */   private final PacketProcessor packetProcessor;
/*      */   
/*      */   public static <S extends MinecraftServer> S spin(Function<Thread, S> factory) {
/*  298 */     AtomicReference<S> serverReference = new AtomicReference<>();
/*      */     
/*  300 */     Thread thread = new Thread(() -> ((MinecraftServer)serverReference.get()).runServer(), "Server thread");
/*  301 */     thread.setUncaughtExceptionHandler((t, e) -> LOGGER.error("Uncaught exception in server thread", e));
/*      */     
/*  303 */     if (Runtime.getRuntime().availableProcessors() > 4) {
/*  304 */       thread.setPriority(8);
/*      */     }
/*      */     
/*  307 */     MinecraftServer minecraftServer = (MinecraftServer)factory.apply(thread);
/*  308 */     serverReference.set((S)minecraftServer);
/*  309 */     thread.start();
/*  310 */     return (S)minecraftServer;
/*      */   }
/*      */   
/*      */   public MinecraftServer(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, LevelLoadListener levelLoadListener) {
/*  314 */     super("Server");
/*  315 */     this.registries = worldStem.registries();
/*  316 */     this.worldData = worldStem.worldData();
/*      */ 
/*      */     
/*  319 */     if (!this.registries.compositeAccess().lookupOrThrow(Registries.LEVEL_STEM).containsKey(LevelStem.OVERWORLD)) {
/*  320 */       throw new IllegalStateException("Missing Overworld dimension data");
/*      */     }
/*  322 */     this.proxy = proxy;
/*  323 */     this.packRepository = packRepository;
/*  324 */     this.resources = new ReloadableResources(worldStem.resourceManager(), worldStem.dataPackResources());
/*  325 */     this.services = services;
/*  326 */     this.connection = new ServerConnectionListener(this);
/*  327 */     this.tickRateManager = new ServerTickRateManager(this);
/*  328 */     this.levelLoadListener = levelLoadListener;
/*  329 */     this.storageSource = storageSource;
/*  330 */     this.playerDataStorage = storageSource.createPlayerStorage();
/*  331 */     this.fixerUpper = fixerUpper;
/*  332 */     this.functionManager = new ServerFunctionManager(this, this.resources.managers.getFunctionLibrary());
/*  333 */     HolderLookup.RegistryLookup registryLookup = this.registries.compositeAccess().lookupOrThrow(Registries.BLOCK).filterFeatures(this.worldData.enabledFeatures());
/*  334 */     this.structureTemplateManager = new StructureTemplateManager((ResourceManager)worldStem.resourceManager(), storageSource, fixerUpper, (HolderGetter)registryLookup);
/*  335 */     this.serverThread = serverThread;
/*  336 */     this.executor = (Executor)Util.backgroundExecutor();
/*  337 */     this.potionBrewing = PotionBrewing.bootstrap(this.worldData.enabledFeatures());
/*  338 */     this.resources.managers.getRecipeManager().finalizeRecipeLoading(this.worldData.enabledFeatures());
/*  339 */     this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)this.registries.compositeAccess(), this.worldData.enabledFeatures());
/*  340 */     this.tickFrame = TracyClient.createDiscontinuousFrame("Server Tick");
/*  341 */     this.notificationManager = new NotificationManager();
/*  342 */     this.serverActivityMonitor = new ServerActivityMonitor(this.notificationManager, 30);
/*  343 */     this.packetProcessor = new PacketProcessor(serverThread);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ChunkLoadStatusView createChunkLoadStatusView(final int radius) {
/*  349 */     return new ChunkLoadStatusView()
/*      */       {
/*      */         private ChunkMap chunkMap;
/*      */         private int centerChunkX;
/*      */         private int centerChunkZ;
/*      */         
/*      */         public void moveTo(ResourceKey<Level> dimension, ChunkPos centerChunk) {
/*  356 */           ServerLevel level = MinecraftServer.this.getLevel(dimension);
/*  357 */           this.chunkMap = (level != null) ? (level.getChunkSource()).chunkMap : null;
/*  358 */           this.centerChunkX = centerChunk.x;
/*  359 */           this.centerChunkZ = centerChunk.z;
/*      */         }
/*      */ 
/*      */         
/*      */         public ChunkStatus get(int x, int z) {
/*  364 */           if (this.chunkMap == null) {
/*  365 */             return null;
/*      */           }
/*  367 */           return this.chunkMap.getLatestStatus(ChunkPos.asLong(x + this.centerChunkX - radius, z + this.centerChunkZ - radius));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public int radius() {
/*  375 */           return radius;
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   protected void loadLevel() {
/*  381 */     boolean startedWorldLoadProfiling = (!JvmProfiler.INSTANCE.isRunning() && SharedConstants.DEBUG_JFR_PROFILING_ENABLE_LEVEL_LOADING && 
/*      */       
/*  383 */       JvmProfiler.INSTANCE.start(net.minecraft.util.profiling.jfr.Environment.from(this)));
/*      */     
/*  385 */     ProfiledDuration profiledDuration = JvmProfiler.INSTANCE.onWorldLoadedStarted();
/*      */     
/*  387 */     this.worldData.setModdedInfo(getServerModName(), getModdedStatus().shouldReportAsModified());
/*      */     
/*  389 */     createLevels();
/*      */     
/*  391 */     forceDifficulty();
/*  392 */     prepareLevels();
/*  393 */     if (profiledDuration != null) {
/*  394 */       profiledDuration.finish(true);
/*      */     }
/*      */     
/*  397 */     if (startedWorldLoadProfiling) {
/*      */       try {
/*  399 */         JvmProfiler.INSTANCE.stop();
/*  400 */       } catch (Throwable t) {
/*  401 */         LOGGER.warn("Failed to stop JFR profiling", t);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void forceDifficulty() {}
/*      */   
/*      */   protected void createLevels() {
/*  410 */     ServerLevelData levelData = this.worldData.overworldData();
/*  411 */     boolean isDebug = this.worldData.isDebugWorld();
/*  412 */     Registry<LevelStem> dimensions = this.registries.compositeAccess().lookupOrThrow(Registries.LEVEL_STEM);
/*  413 */     WorldOptions worldOptions = this.worldData.worldGenOptions();
/*  414 */     long seed = worldOptions.seed();
/*  415 */     long biomeZoomSeed = BiomeManager.obfuscateSeed(seed);
/*  416 */     ImmutableList immutableList = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner(levelData));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  423 */     LevelStem overworldData = (LevelStem)dimensions.getValue(LevelStem.OVERWORLD);
/*  424 */     ServerLevel overworld = new ServerLevel(this, this.executor, this.storageSource, levelData, Level.OVERWORLD, overworldData, isDebug, biomeZoomSeed, (List)immutableList, true, null);
/*  425 */     this.levels.put(Level.OVERWORLD, overworld);
/*  426 */     DimensionDataStorage overworldDataStorage = overworld.getDataStorage();
/*  427 */     this.scoreboard.load(((ScoreboardSaveData)overworldDataStorage.computeIfAbsent(ScoreboardSaveData.TYPE)).getData());
/*  428 */     this.commandStorage = new CommandStorage(overworldDataStorage);
/*      */     
/*  430 */     this.stopwatches = (Stopwatches)overworldDataStorage.computeIfAbsent(Stopwatches.TYPE);
/*      */     
/*  432 */     if (!levelData.isInitialized()) {
/*      */       try {
/*  434 */         setInitialSpawn(overworld, levelData, worldOptions.generateBonusChest(), isDebug, this.levelLoadListener);
/*  435 */         levelData.setInitialized(true);
/*  436 */         if (isDebug) {
/*  437 */           setupDebugLevel(this.worldData);
/*      */         }
/*  439 */       } catch (Throwable t) {
/*  440 */         CrashReport report = CrashReport.forThrowable(t, "Exception initializing level");
/*      */         try {
/*  442 */           overworld.fillReportDetails(report);
/*  443 */         } catch (Throwable throwable) {}
/*      */ 
/*      */         
/*  446 */         throw new ReportedException(report);
/*      */       } 
/*  448 */       levelData.setInitialized(true);
/*      */     } 
/*      */     
/*  451 */     GlobalPos focusPos = selectLevelLoadFocusPos();
/*  452 */     this.levelLoadListener.updateFocus(focusPos.dimension(), new ChunkPos(focusPos.pos()));
/*      */     
/*  454 */     if (this.worldData.getCustomBossEvents() != null) {
/*  455 */       getCustomBossEvents().load(this.worldData.getCustomBossEvents(), (HolderLookup.Provider)registryAccess());
/*      */     }
/*      */     
/*  458 */     RandomSequences randomSequences = overworld.getRandomSequences();
/*      */     boolean hasLegacyWorldBorder = false;
/*  460 */     for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : (Iterable<Map.Entry<ResourceKey<LevelStem>, LevelStem>>)dimensions.entrySet()) {
/*  461 */       ServerLevel level; ResourceKey<LevelStem> name = entry.getKey();
/*      */       
/*  463 */       if (name != LevelStem.OVERWORLD) {
/*  464 */         ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, name.identifier());
/*  465 */         DerivedLevelData derivedLevelData = new DerivedLevelData(this.worldData, levelData);
/*  466 */         level = new ServerLevel(this, this.executor, this.storageSource, (ServerLevelData)derivedLevelData, dimension, entry.getValue(), isDebug, biomeZoomSeed, (List)ImmutableList.of(), false, randomSequences);
/*  467 */         this.levels.put(dimension, level);
/*      */       } else {
/*  469 */         level = overworld;
/*      */       } 
/*      */ 
/*      */       
/*  473 */       Optional<WorldBorder.Settings> savedWorldBorderSettings = levelData.getLegacyWorldBorderSettings();
/*  474 */       if (savedWorldBorderSettings.isPresent()) {
/*  475 */         WorldBorder.Settings legacySettings = savedWorldBorderSettings.get();
/*  476 */         DimensionDataStorage storage = level.getDataStorage();
/*  477 */         if (storage.get(WorldBorder.TYPE) == null) {
/*  478 */           double coordinateScale = level.dimensionType().coordinateScale();
/*  479 */           WorldBorder.Settings scaleAdjustedSettings = new WorldBorder.Settings(
/*  480 */               legacySettings.centerX() / coordinateScale, 
/*  481 */               legacySettings.centerZ() / coordinateScale, 
/*  482 */               legacySettings.damagePerBlock(), 
/*  483 */               legacySettings.safeZone(), 
/*  484 */               legacySettings.warningBlocks(), 
/*  485 */               legacySettings.warningTime(), 
/*  486 */               legacySettings.size(), 
/*  487 */               legacySettings.lerpTime(), 
/*  488 */               legacySettings.lerpTarget());
/*      */           
/*  490 */           WorldBorder newWorldBorder = new WorldBorder(scaleAdjustedSettings);
/*  491 */           newWorldBorder.applyInitialSettings(level.getGameTime());
/*  492 */           storage.set(WorldBorder.TYPE, (SavedData)newWorldBorder);
/*      */         } 
/*  494 */         hasLegacyWorldBorder = true;
/*      */       } 
/*  496 */       level.getWorldBorder().setAbsoluteMaxSize(getAbsoluteMaxWorldSize());
/*  497 */       getPlayerList().addWorldborderListener(level);
/*      */     } 
/*  499 */     if (hasLegacyWorldBorder) {
/*  500 */       levelData.setLegacyWorldBorderSettings(Optional.empty());
/*      */     }
/*      */   }
/*      */   
/*      */   private static void setInitialSpawn(ServerLevel level, ServerLevelData levelData, boolean spawnBonusChest, boolean isDebug, LevelLoadListener levelLoadListener) {
/*  505 */     if (SharedConstants.DEBUG_ONLY_GENERATE_HALF_THE_WORLD && SharedConstants.DEBUG_WORLD_RECREATE) {
/*  506 */       levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), new BlockPos(0, 64, -100), 0.0F, 0.0F));
/*      */       
/*      */       return;
/*      */     } 
/*  510 */     if (isDebug) {
/*  511 */       levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), BlockPos.ZERO.above(80), 0.0F, 0.0F));
/*      */       
/*      */       return;
/*      */     } 
/*  515 */     ServerChunkCache chunkSource = level.getChunkSource();
/*  516 */     ChunkPos spawnChunk = new ChunkPos(chunkSource.randomState().sampler().findSpawnPosition());
/*      */     
/*  518 */     levelLoadListener.start(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN, 0);
/*  519 */     levelLoadListener.updateFocus(level.dimension(), spawnChunk);
/*      */     
/*  521 */     int height = chunkSource.getGenerator().getSpawnHeight((LevelHeightAccessor)level);
/*      */     
/*  523 */     if (height < level.getMinY()) {
/*  524 */       BlockPos worldPosition = spawnChunk.getWorldPosition();
/*  525 */       height = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldPosition.getX() + 8, worldPosition.getZ() + 8);
/*      */     } 
/*  527 */     levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), spawnChunk.getWorldPosition().offset(8, height, 8), 0.0F, 0.0F));
/*      */     
/*  529 */     int xChunkOffset = 0;
/*  530 */     int zChunkOffset = 0;
/*  531 */     int dXChunk = 0;
/*  532 */     int dZChunk = -1;
/*  533 */     for (int i = 0; i < Mth.square(11); i++) {
/*  534 */       if (xChunkOffset >= -5 && xChunkOffset <= 5 && zChunkOffset >= -5 && zChunkOffset <= 5) {
/*  535 */         BlockPos testedPos = PlayerSpawnFinder.getSpawnPosInChunk(level, new ChunkPos(spawnChunk.x + xChunkOffset, spawnChunk.z + zChunkOffset));
/*  536 */         if (testedPos != null) {
/*  537 */           levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), testedPos, 0.0F, 0.0F));
/*      */           break;
/*      */         } 
/*      */       } 
/*  541 */       if (xChunkOffset == zChunkOffset || (xChunkOffset < 0 && xChunkOffset == -zChunkOffset) || (xChunkOffset > 0 && xChunkOffset == 1 - zChunkOffset)) {
/*  542 */         int olddx = dXChunk;
/*  543 */         dXChunk = -dZChunk;
/*  544 */         dZChunk = olddx;
/*      */       } 
/*  546 */       xChunkOffset += dXChunk;
/*  547 */       zChunkOffset += dZChunk;
/*      */     } 
/*      */     
/*  550 */     if (spawnBonusChest)
/*      */     {
/*      */       
/*  553 */       level.registryAccess()
/*  554 */         .lookup(Registries.CONFIGURED_FEATURE)
/*  555 */         .flatMap(registry -> registry.get(MiscOverworldFeatures.BONUS_CHEST))
/*  556 */         .ifPresent(feature -> ((ConfiguredFeature)feature.value()).place((WorldGenLevel)level, chunkSource.getGenerator(), level.random, levelData.getRespawnData().pos()));
/*      */     }
/*      */     
/*  559 */     levelLoadListener.finish(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN);
/*      */   }
/*      */   
/*      */   private void setupDebugLevel(WorldData worldData) {
/*  563 */     worldData.setDifficulty(Difficulty.PEACEFUL);
/*  564 */     worldData.setDifficultyLocked(true);
/*      */     
/*  566 */     ServerLevelData levelData = worldData.overworldData();
/*  567 */     levelData.setRaining(false);
/*  568 */     levelData.setThundering(false);
/*  569 */     levelData.setClearWeatherTime(1000000000);
/*  570 */     levelData.setDayTime(6000L);
/*  571 */     levelData.setGameType(GameType.SPECTATOR);
/*      */   }
/*      */   
/*      */   private void prepareLevels() {
/*  575 */     ChunkLoadCounter chunkLoadCounter = new ChunkLoadCounter();
/*      */     
/*  577 */     for (ServerLevel level : this.levels.values()) {
/*  578 */       chunkLoadCounter.track(level, () -> {
/*      */             TicketStorage savedTickets = (TicketStorage)level.getDataStorage().get(TicketStorage.TYPE);
/*      */             
/*      */             if (savedTickets != null) {
/*      */               savedTickets.activateAllDeactivatedTickets();
/*      */             }
/*      */           });
/*      */     } 
/*  586 */     this.levelLoadListener.start(LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS, chunkLoadCounter.totalChunks());
/*      */     
/*      */     do {
/*  589 */       this.levelLoadListener.update(LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS, 
/*      */           
/*  591 */           chunkLoadCounter.readyChunks(), 
/*  592 */           chunkLoadCounter.totalChunks());
/*      */ 
/*      */       
/*  595 */       this.nextTickTimeNanos = Util.getNanos() + PREPARE_LEVELS_DEFAULT_DELAY_NANOS;
/*  596 */       waitUntilNextTick();
/*  597 */     } while (chunkLoadCounter.pendingChunks() > 0);
/*      */     
/*  599 */     this.levelLoadListener.finish(LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS);
/*      */     
/*  601 */     updateMobSpawningFlags();
/*  602 */     updateEffectiveRespawnData();
/*      */   }
/*      */   
/*      */   protected GlobalPos selectLevelLoadFocusPos() {
/*  606 */     return this.worldData.overworldData().getRespawnData().globalPos();
/*      */   }
/*      */   
/*      */   public GameType getDefaultGameType() {
/*  610 */     return this.worldData.getGameType();
/*      */   }
/*      */   
/*      */   public boolean isHardcore() {
/*  614 */     return this.worldData.isHardcore();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean saveAllChunks(boolean silent, boolean flush, boolean force) {
/*  624 */     this.scoreboard.storeToSaveDataIfDirty((ScoreboardSaveData)overworld().getDataStorage().computeIfAbsent(ScoreboardSaveData.TYPE));
/*      */     
/*      */     boolean result = false;
/*  627 */     for (ServerLevel level : getAllLevels()) {
/*  628 */       if (!silent) {
/*  629 */         LOGGER.info("Saving chunks for level '{}'/{}", level, level.dimension().identifier());
/*      */       }
/*  631 */       level.save(null, flush, (SharedConstants.DEBUG_DONT_SAVE_WORLD || (level.noSave && !force)));
/*  632 */       result = true;
/*      */     } 
/*  634 */     this.worldData.setCustomBossEvents(getCustomBossEvents().save((HolderLookup.Provider)registryAccess()));
/*  635 */     this.storageSource.saveDataTag((RegistryAccess)registryAccess(), this.worldData, getPlayerList().getSingleplayerData());
/*      */     
/*  637 */     if (flush) {
/*      */       
/*  639 */       for (ServerLevel level : getAllLevels()) {
/*  640 */         LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", (level.getChunkSource()).chunkMap.getStorageName());
/*      */       }
/*  642 */       LOGGER.info("ThreadedAnvilChunkStorage: All dimensions are saved");
/*      */     } 
/*      */     
/*  645 */     return result;
/*      */   }
/*      */   
/*      */   public boolean saveEverything(boolean silent, boolean flush, boolean force) {
/*      */     try {
/*  650 */       this.isSaving = true;
/*  651 */       getPlayerList().saveAll();
/*  652 */       return saveAllChunks(silent, flush, force);
/*      */     } finally {
/*  654 */       this.isSaving = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/*  660 */     stopServer();
/*      */   }
/*      */   
/*      */   protected void stopServer() {
/*  664 */     this.packetProcessor.close();
/*      */     
/*  666 */     if (this.metricsRecorder.isRecording()) {
/*  667 */       cancelRecordingMetrics();
/*      */     }
/*      */     
/*  670 */     LOGGER.info("Stopping server");
/*  671 */     getConnection().stop();
/*  672 */     this.isSaving = true;
/*  673 */     if (this.playerList != null) {
/*  674 */       LOGGER.info("Saving players");
/*  675 */       this.playerList.saveAll();
/*  676 */       this.playerList.removeAll();
/*      */     } 
/*  678 */     LOGGER.info("Saving worlds");
/*  679 */     for (ServerLevel level : getAllLevels()) {
/*  680 */       if (level != null) {
/*  681 */         level.noSave = false;
/*      */       }
/*      */     } 
/*      */     
/*  685 */     while (this.levels.values().stream().anyMatch(l -> (l.getChunkSource()).chunkMap.hasWork())) {
/*  686 */       this.nextTickTimeNanos = Util.getNanos() + TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*  687 */       for (ServerLevel level : getAllLevels()) {
/*  688 */         level.getChunkSource().deactivateTicketsOnClosing();
/*  689 */         level.getChunkSource().tick(() -> true, false);
/*      */       } 
/*  691 */       waitUntilNextTick();
/*      */     } 
/*      */     
/*  694 */     saveAllChunks(false, true, false);
/*  695 */     for (ServerLevel level : getAllLevels()) {
/*  696 */       if (level != null) {
/*      */         try {
/*  698 */           level.close();
/*  699 */         } catch (IOException e) {
/*  700 */           LOGGER.error("Exception closing the level", e);
/*      */         } 
/*      */       }
/*      */     } 
/*  704 */     this.isSaving = false;
/*  705 */     this.resources.close();
/*      */     try {
/*  707 */       this.storageSource.close();
/*  708 */     } catch (IOException e) {
/*  709 */       LOGGER.error("Failed to unlock level {}", this.storageSource.getLevelId(), e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String getLocalIp() {
/*  714 */     return this.localIp;
/*      */   }
/*      */   
/*      */   public void setLocalIp(String ip) {
/*  718 */     this.localIp = ip;
/*      */   }
/*      */   
/*      */   public boolean isRunning() {
/*  722 */     return this.running;
/*      */   }
/*      */   
/*      */   public void halt(boolean wait) {
/*  726 */     this.running = false;
/*  727 */     if (wait) {
/*      */       try {
/*  729 */         this.serverThread.join();
/*  730 */       } catch (InterruptedException e) {
/*  731 */         LOGGER.error("Error while shutting down", e);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected void runServer() {
/*      */     try {
/*  738 */       if (initServer()) {
/*  739 */         this.nextTickTimeNanos = Util.getNanos();
/*      */         
/*  741 */         this.statusIcon = loadStatusIcon().orElse(null);
/*  742 */         this.status = buildServerStatus();
/*      */         
/*  744 */         while (this.running) {
/*      */           long thisTickNanos;
/*  746 */           if (!isPaused() && this.tickRateManager.isSprinting() && this.tickRateManager.checkShouldSprintThisTick()) {
/*  747 */             thisTickNanos = 0L;
/*      */             
/*  749 */             this.nextTickTimeNanos = Util.getNanos();
/*  750 */             this.lastOverloadWarningNanos = this.nextTickTimeNanos;
/*      */           } else {
/*  752 */             thisTickNanos = this.tickRateManager.nanosecondsPerTick();
/*  753 */             long behindTimeNanos = Util.getNanos() - this.nextTickTimeNanos;
/*  754 */             if (behindTimeNanos > OVERLOADED_THRESHOLD_NANOS + 20L * thisTickNanos && this.nextTickTimeNanos - this.lastOverloadWarningNanos >= OVERLOADED_WARNING_INTERVAL_NANOS + 100L * thisTickNanos) {
/*  755 */               long ticks = behindTimeNanos / thisTickNanos;
/*  756 */               LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", behindTimeNanos / TimeUtil.NANOSECONDS_PER_MILLISECOND, ticks);
/*  757 */               this.nextTickTimeNanos += ticks * thisTickNanos;
/*  758 */               this.lastOverloadWarningNanos = this.nextTickTimeNanos;
/*      */             } 
/*      */           } 
/*  761 */           boolean sprinting = (thisTickNanos == 0L);
/*      */           
/*  763 */           if (this.debugCommandProfilerDelayStart) {
/*  764 */             this.debugCommandProfilerDelayStart = false;
/*  765 */             this.debugCommandProfiler = new TimeProfiler(Util.getNanos(), this.tickCount);
/*      */           } 
/*      */           
/*  768 */           this.nextTickTimeNanos += thisTickNanos; 
/*  769 */           try { Profiler.Scope ignored = Profiler.use(createProfiler()); 
/*  770 */             try { processPacketsAndTick(sprinting);
/*  771 */               ProfilerFiller profiler = Profiler.get();
/*  772 */               profiler.push("nextTickWait");
/*  773 */               this.mayHaveDelayedTasks = true;
/*  774 */               this.delayedTasksMaxNextTickTimeNanos = Math.max(Util.getNanos() + thisTickNanos, this.nextTickTimeNanos);
/*  775 */               startMeasuringTaskExecutionTime();
/*  776 */               waitUntilNextTick();
/*  777 */               finishMeasuringTaskExecutionTime();
/*  778 */               if (sprinting) {
/*  779 */                 this.tickRateManager.endTickWork();
/*      */               }
/*  781 */               profiler.pop();
/*  782 */               logFullTickTime();
/*  783 */               if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*  784 */                 try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } finally { endMetricsRecordingTick(); }
/*      */ 
/*      */           
/*  787 */           this.isReady = true;
/*      */           
/*  789 */           JvmProfiler.INSTANCE.onServerTick(this.smoothedTickTimeMillis);
/*      */         } 
/*      */       } else {
/*  792 */         throw new IllegalStateException("Failed to initialize server");
/*      */       } 
/*  794 */     } catch (Throwable t) {
/*  795 */       LOGGER.error("Encountered an unexpected exception", t);
/*  796 */       CrashReport report = constructOrExtractCrashReport(t);
/*  797 */       fillSystemReport(report.getSystemReport());
/*      */       
/*  799 */       Path file = getServerDirectory().resolve("crash-reports").resolve("crash-" + Util.getFilenameFormattedDateTime() + "-server.txt");
/*      */       
/*  801 */       if (report.saveToFile(file, ReportType.CRASH)) {
/*  802 */         LOGGER.error("This crash report has been saved to: {}", file.toAbsolutePath());
/*      */       } else {
/*  804 */         LOGGER.error("We were unable to save this crash report to disk.");
/*      */       } 
/*      */       
/*  807 */       onServerCrash(report);
/*      */     } finally {
/*      */       try {
/*  810 */         this.stopped = true;
/*  811 */         stopServer();
/*  812 */       } catch (Throwable t) {
/*  813 */         LOGGER.error("Exception stopping the server", t);
/*      */       } finally {
/*  815 */         onServerExit();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void logFullTickTime() {
/*  821 */     long currentTime = Util.getNanos();
/*  822 */     if (isTickTimeLoggingEnabled()) {
/*  823 */       getTickTimeLogger().logSample(currentTime - this.lastTickNanos);
/*      */     }
/*  825 */     this.lastTickNanos = currentTime;
/*      */   }
/*      */   
/*      */   private void startMeasuringTaskExecutionTime() {
/*  829 */     if (isTickTimeLoggingEnabled()) {
/*  830 */       this.taskExecutionStartNanos = Util.getNanos();
/*  831 */       this.idleTimeNanos = 0L;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void finishMeasuringTaskExecutionTime() {
/*  836 */     if (isTickTimeLoggingEnabled()) {
/*  837 */       SampleLogger tickTimelogger = getTickTimeLogger();
/*  838 */       tickTimelogger.logPartialSample(Util.getNanos() - this.taskExecutionStartNanos - this.idleTimeNanos, TpsDebugDimensions.SCHEDULED_TASKS.ordinal());
/*  839 */       tickTimelogger.logPartialSample(this.idleTimeNanos, TpsDebugDimensions.IDLE.ordinal());
/*      */     } 
/*      */   }
/*      */   private static CrashReport constructOrExtractCrashReport(Throwable t) {
/*      */     CrashReport report;
/*  844 */     ReportedException firstReported = null;
/*  845 */     Throwable cause = t;
/*  846 */     while (cause != null) {
/*  847 */       if (cause instanceof ReportedException) { ReportedException reportedException = (ReportedException)cause;
/*  848 */         firstReported = reportedException; }
/*      */       
/*  850 */       cause = cause.getCause();
/*      */     } 
/*      */ 
/*      */     
/*  854 */     if (firstReported != null) {
/*  855 */       report = firstReported.getReport();
/*  856 */       if (firstReported != t) {
/*  857 */         report.addCategory("Wrapped in").setDetailError("Wrapping exception", t);
/*      */       }
/*      */     } else {
/*  860 */       report = new CrashReport("Exception in server tick loop", t);
/*      */     } 
/*  862 */     return report;
/*      */   }
/*      */   
/*      */   private boolean haveTime() {
/*  866 */     if (!runningTask()) { if (Util.getNanos() < (this.mayHaveDelayedTasks ? this.delayedTasksMaxNextTickTimeNanos : this.nextTickTimeNanos)); return false; }
/*      */   
/*      */   }
/*      */   public static boolean throwIfFatalException() {
/*  870 */     RuntimeException e = fatalException.get();
/*  871 */     if (e != null) {
/*  872 */       throw e;
/*      */     }
/*  874 */     return true;
/*      */   }
/*      */   
/*      */   public static void setFatalException(RuntimeException exception) {
/*  878 */     fatalException.compareAndSet(null, exception);
/*      */   }
/*      */ 
/*      */   
/*      */   public void managedBlock(BooleanSupplier condition) {
/*  883 */     super.managedBlock(() -> (throwIfFatalException() && condition.getAsBoolean()));
/*      */   }
/*      */   
/*      */   public NotificationManager notificationManager() {
/*  887 */     return this.notificationManager;
/*      */   }
/*      */   
/*      */   protected void waitUntilNextTick() {
/*  891 */     runAllTasks();
/*  892 */     this.waitingForNextTick = true;
/*      */     try {
/*  894 */       managedBlock(() -> !haveTime());
/*      */     } finally {
/*  896 */       this.waitingForNextTick = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void waitForTasks() {
/*  902 */     boolean shouldLogTime = isTickTimeLoggingEnabled();
/*  903 */     long waitStart = shouldLogTime ? Util.getNanos() : 0L;
/*  904 */     long waitNanos = this.waitingForNextTick ? (this.nextTickTimeNanos - Util.getNanos()) : 100000L;
/*  905 */     LockSupport.parkNanos("waiting for tasks", waitNanos);
/*  906 */     if (shouldLogTime) {
/*  907 */       this.idleTimeNanos += Util.getNanos() - waitStart;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public TickTask wrapRunnable(Runnable runnable) {
/*  913 */     return new TickTask(this.tickCount, runnable);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean shouldRun(TickTask task) {
/*  918 */     return (task.getTick() + 3 < this.tickCount || haveTime());
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean pollTask() {
/*  923 */     boolean mayHaveMoreTasks = pollTaskInternal();
/*  924 */     this.mayHaveDelayedTasks = mayHaveMoreTasks;
/*  925 */     return mayHaveMoreTasks;
/*      */   }
/*      */   
/*      */   private boolean pollTaskInternal() {
/*  929 */     if (super.pollTask()) {
/*  930 */       return true;
/*      */     }
/*      */     
/*  933 */     if (this.tickRateManager.isSprinting() || shouldRunAllTasks() || haveTime()) {
/*  934 */       for (ServerLevel level : getAllLevels()) {
/*  935 */         if (level.getChunkSource().pollTask()) {
/*  936 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  941 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doRunTask(TickTask task) {
/*  946 */     Profiler.get().incrementCounter("runTask");
/*  947 */     super.doRunTask(task);
/*      */   }
/*      */   
/*      */   private Optional<ServerStatus.Favicon> loadStatusIcon() {
/*  951 */     Optional<Path> iconPath = Optional.<Path>of(getFile("server-icon.png")).filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0]))
/*  952 */       .or(() -> this.storageSource.getIconFile().filter(()));
/*      */     
/*  954 */     return iconPath.flatMap(path -> {
/*      */           try {
/*      */             byte[] contents = Files.readAllBytes(path);
/*      */             PngInfo pngInfo = PngInfo.fromBytes(contents);
/*      */             if (pngInfo.width() != 64 || pngInfo.height() != 64) {
/*      */               throw new IllegalArgumentException("Invalid world icon size [" + pngInfo.width() + ", " + pngInfo.height() + "], but expected [64, 64]");
/*      */             }
/*      */             return Optional.of(new ServerStatus.Favicon(contents));
/*  962 */           } catch (Exception e) {
/*      */             LOGGER.error("Couldn't load server icon", e);
/*      */             return Optional.empty();
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public Optional<Path> getWorldScreenshotFile() {
/*  971 */     return this.storageSource.getIconFile();
/*      */   }
/*      */   
/*      */   public Path getServerDirectory() {
/*  975 */     return Path.of("", new String[0]);
/*      */   }
/*      */   
/*      */   public ServerActivityMonitor getServerActivityMonitor() {
/*  979 */     return this.serverActivityMonitor;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onServerCrash(CrashReport report) {}
/*      */ 
/*      */   
/*      */   protected void onServerExit() {}
/*      */   
/*      */   public boolean isPaused() {
/*  989 */     return false;
/*      */   }
/*      */   
/*      */   protected void tickServer(BooleanSupplier haveTime) {
/*  993 */     long nano = Util.getNanos();
/*  994 */     int emptyTickThreshold = pauseWhenEmptySeconds() * 20;
/*  995 */     if (emptyTickThreshold > 0) {
/*  996 */       if (this.playerList.getPlayerCount() == 0 && !this.tickRateManager.isSprinting()) {
/*  997 */         this.emptyTicks++;
/*      */       } else {
/*  999 */         this.emptyTicks = 0;
/*      */       } 
/* 1001 */       if (this.emptyTicks >= emptyTickThreshold) {
/* 1002 */         if (this.emptyTicks == emptyTickThreshold) {
/* 1003 */           LOGGER.info("Server empty for {} seconds, pausing", pauseWhenEmptySeconds());
/* 1004 */           autoSave();
/*      */         } 
/* 1006 */         tickConnection();
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1011 */     this.tickCount++;
/*      */     
/* 1013 */     this.tickRateManager.tick();
/*      */     
/* 1015 */     tickChildren(haveTime);
/*      */     
/* 1017 */     if (nano - this.lastServerStatus >= STATUS_EXPIRE_TIME_NANOS) {
/* 1018 */       this.lastServerStatus = nano;
/* 1019 */       this.status = buildServerStatus();
/*      */     } 
/*      */     
/* 1022 */     this.ticksUntilAutosave--;
/* 1023 */     if (this.ticksUntilAutosave <= 0) {
/* 1024 */       autoSave();
/*      */     }
/*      */     
/* 1027 */     ProfilerFiller profiler = Profiler.get();
/* 1028 */     profiler.push("tallying");
/* 1029 */     long tickTime = Util.getNanos() - nano;
/* 1030 */     int tickIndex = this.tickCount % 100;
/* 1031 */     this.aggregatedTickTimesNanos -= this.tickTimesNanos[tickIndex];
/* 1032 */     this.aggregatedTickTimesNanos += tickTime;
/* 1033 */     this.tickTimesNanos[tickIndex] = tickTime;
/* 1034 */     this.smoothedTickTimeMillis = this.smoothedTickTimeMillis * 0.8F + (float)tickTime / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND * 0.19999999F;
/*      */     
/* 1036 */     logTickMethodTime(nano);
/*      */     
/* 1038 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected void processPacketsAndTick(boolean sprinting) {
/* 1042 */     ProfilerFiller profiler = Profiler.get();
/* 1043 */     profiler.push("tick");
/* 1044 */     this.tickFrame.start();
/* 1045 */     profiler.push("scheduledPacketProcessing");
/* 1046 */     this.packetProcessor.processQueuedPackets();
/* 1047 */     profiler.pop();
/* 1048 */     tickServer(sprinting ? (() -> false) : this::haveTime);
/* 1049 */     this.tickFrame.end();
/* 1050 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private void autoSave() {
/* 1054 */     this.ticksUntilAutosave = computeNextAutosaveInterval();
/* 1055 */     LOGGER.debug("Autosave started");
/* 1056 */     ProfilerFiller profiler = Profiler.get();
/* 1057 */     profiler.push("save");
/* 1058 */     saveEverything(true, false, false);
/* 1059 */     profiler.pop();
/* 1060 */     LOGGER.debug("Autosave finished");
/*      */   }
/*      */   
/*      */   private void logTickMethodTime(long startTime) {
/* 1064 */     if (isTickTimeLoggingEnabled()) {
/* 1065 */       getTickTimeLogger().logPartialSample(Util.getNanos() - startTime, TpsDebugDimensions.TICK_SERVER_METHOD.ordinal());
/*      */     }
/*      */   }
/*      */   
/*      */   private int computeNextAutosaveInterval() {
/*      */     float ticksPerSecond;
/* 1071 */     if (this.tickRateManager.isSprinting()) {
/* 1072 */       long estimatedTickTimeNanos = getAverageTickTimeNanos() + 1L;
/* 1073 */       ticksPerSecond = (float)TimeUtil.NANOSECONDS_PER_SECOND / (float)estimatedTickTimeNanos;
/*      */     } else {
/* 1075 */       ticksPerSecond = this.tickRateManager.tickrate();
/*      */     } 
/* 1077 */     int intendedIntervalInSeconds = 300;
/*      */     
/* 1079 */     return Math.max(100, (int)(ticksPerSecond * 300.0F));
/*      */   }
/*      */   
/*      */   public void onTickRateChanged() {
/* 1083 */     int newAutosaveInterval = computeNextAutosaveInterval();
/* 1084 */     if (newAutosaveInterval < this.ticksUntilAutosave) {
/* 1085 */       this.ticksUntilAutosave = newAutosaveInterval;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ServerStatus buildServerStatus() {
/* 1094 */     ServerStatus.Players players = buildPlayerStatus();
/* 1095 */     return new ServerStatus(
/* 1096 */         Component.nullToEmpty(getMotd()), 
/* 1097 */         Optional.of(players), 
/* 1098 */         Optional.of(ServerStatus.Version.current()), 
/* 1099 */         Optional.ofNullable(this.statusIcon), 
/* 1100 */         enforceSecureProfile());
/*      */   }
/*      */ 
/*      */   
/*      */   private ServerStatus.Players buildPlayerStatus() {
/* 1105 */     List<ServerPlayer> players = this.playerList.getPlayers();
/* 1106 */     int maxPlayers = getMaxPlayers();
/* 1107 */     if (hidesOnlinePlayers()) {
/* 1108 */       return new ServerStatus.Players(maxPlayers, players.size(), List.of());
/*      */     }
/*      */     
/* 1111 */     int sampleSize = Math.min(players.size(), 12);
/*      */     
/* 1113 */     ObjectArrayList<NameAndId> sample = new ObjectArrayList(sampleSize);
/* 1114 */     int offset = Mth.nextInt(this.random, 0, players.size() - sampleSize);
/* 1115 */     for (int i = 0; i < sampleSize; i++) {
/* 1116 */       ServerPlayer player = players.get(offset + i);
/* 1117 */       sample.add(player.allowsListing() ? player.nameAndId() : ANONYMOUS_PLAYER_PROFILE);
/*      */     } 
/*      */     
/* 1120 */     Util.shuffle((List)sample, this.random);
/*      */     
/* 1122 */     return new ServerStatus.Players(maxPlayers, players.size(), (List)sample);
/*      */   }
/*      */   
/*      */   protected void tickChildren(BooleanSupplier haveTime) {
/* 1126 */     ProfilerFiller profiler = Profiler.get();
/* 1127 */     getPlayerList().getPlayers().forEach(player -> player.connection.suspendFlushing());
/* 1128 */     profiler.push("commandFunctions");
/* 1129 */     getFunctions().tick();
/*      */     
/* 1131 */     profiler.popPush("levels");
/* 1132 */     updateEffectiveRespawnData();
/* 1133 */     for (ServerLevel level : getAllLevels()) {
/* 1134 */       profiler.push(() -> String.valueOf(level) + " " + String.valueOf(level));
/*      */       
/* 1136 */       if (this.tickCount % 20 == 0) {
/* 1137 */         profiler.push("timeSync");
/* 1138 */         synchronizeTime(level);
/* 1139 */         profiler.pop();
/*      */       } 
/*      */       
/* 1142 */       profiler.push("tick");
/*      */       try {
/* 1144 */         level.tick(haveTime);
/* 1145 */       } catch (Throwable t) {
/* 1146 */         CrashReport report = CrashReport.forThrowable(t, "Exception ticking world");
/* 1147 */         level.fillReportDetails(report);
/* 1148 */         throw new ReportedException(report);
/*      */       } 
/* 1150 */       profiler.pop();
/* 1151 */       profiler.pop();
/*      */     } 
/*      */     
/* 1154 */     profiler.popPush("connection");
/* 1155 */     tickConnection();
/* 1156 */     profiler.popPush("players");
/* 1157 */     this.playerList.tick();
/* 1158 */     profiler.popPush("debugSubscribers");
/* 1159 */     this.debugSubscribers.tick();
/*      */     
/* 1161 */     if (this.tickRateManager.runsNormally()) {
/* 1162 */       profiler.popPush("gameTests");
/* 1163 */       GameTestTicker.SINGLETON.tick();
/*      */     } 
/* 1165 */     profiler.popPush("server gui refresh");
/* 1166 */     for (Runnable tickable : this.tickables) {
/* 1167 */       tickable.run();
/*      */     }
/* 1169 */     profiler.popPush("send chunks");
/* 1170 */     for (ServerPlayer player : (Iterable<ServerPlayer>)this.playerList.getPlayers()) {
/* 1171 */       player.connection.chunkSender.sendNextChunks(player);
/* 1172 */       player.connection.resumeFlushing();
/*      */     } 
/* 1174 */     profiler.pop();
/* 1175 */     this.serverActivityMonitor.tick();
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateEffectiveRespawnData() {
/* 1180 */     LevelData.RespawnData respawnData = this.worldData.overworldData().getRespawnData();
/* 1181 */     ServerLevel respawnLevel = findRespawnDimension();
/* 1182 */     this.effectiveRespawnData = respawnLevel.getWorldBorderAdjustedRespawnData(respawnData);
/*      */   }
/*      */   
/*      */   protected void tickConnection() {
/* 1186 */     getConnection().tick();
/*      */   }
/*      */   
/*      */   private void synchronizeTime(ServerLevel level) {
/* 1190 */     this.playerList.broadcastAll((Packet)new ClientboundSetTimePacket(level.getGameTime(), level.getDayTime(), (Boolean)level.getGameRules().get(GameRules.ADVANCE_TIME)), level.dimension());
/*      */   }
/*      */   
/*      */   public void forceTimeSynchronization() {
/* 1194 */     ProfilerFiller profiler = Profiler.get();
/* 1195 */     profiler.push("timeSync");
/* 1196 */     for (ServerLevel level : getAllLevels()) {
/* 1197 */       synchronizeTime(level);
/*      */     }
/* 1199 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public void addTickable(Runnable tickable) {
/* 1203 */     this.tickables.add(tickable);
/*      */   }
/*      */   
/*      */   protected void setId(String serverId) {
/* 1207 */     this.serverId = serverId;
/*      */   }
/*      */   
/*      */   public boolean isShutdown() {
/* 1211 */     return !this.serverThread.isAlive();
/*      */   }
/*      */   
/*      */   public Path getFile(String name) {
/* 1215 */     return getServerDirectory().resolve(name);
/*      */   }
/*      */   
/*      */   public final ServerLevel overworld() {
/* 1219 */     return this.levels.get(Level.OVERWORLD);
/*      */   }
/*      */   
/*      */   public ServerLevel getLevel(ResourceKey<Level> dimension) {
/* 1223 */     return this.levels.get(dimension);
/*      */   }
/*      */   
/*      */   public Set<ResourceKey<Level>> levelKeys() {
/* 1227 */     return this.levels.keySet();
/*      */   }
/*      */   
/*      */   public Iterable<ServerLevel> getAllLevels() {
/* 1231 */     return this.levels.values();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getServerVersion() {
/* 1236 */     return SharedConstants.getCurrentVersion().name();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPlayerCount() {
/* 1241 */     return this.playerList.getPlayerCount();
/*      */   }
/*      */   
/*      */   public String[] getPlayerNames() {
/* 1245 */     return this.playerList.getPlayerNamesArray();
/*      */   }
/*      */   
/*      */   public String getServerModName() {
/* 1249 */     return "vanilla";
/*      */   }
/*      */   
/*      */   public SystemReport fillSystemReport(SystemReport systemReport) {
/* 1253 */     systemReport.setDetail("Server Running", () -> Boolean.toString(this.running));
/*      */     
/* 1255 */     if (this.playerList != null) {
/* 1256 */       systemReport.setDetail("Player Count", () -> "" + this.playerList.getPlayerCount() + " / " + this.playerList.getPlayerCount() + "; " + this.playerList.getMaxPlayers());
/*      */     }
/*      */     
/* 1259 */     systemReport.setDetail("Active Data Packs", () -> PackRepository.displayPackList(this.packRepository.getSelectedPacks()));
/* 1260 */     systemReport.setDetail("Available Data Packs", () -> PackRepository.displayPackList(this.packRepository.getAvailablePacks()));
/*      */     
/* 1262 */     systemReport.setDetail("Enabled Feature Flags", () -> (String)FeatureFlags.REGISTRY.toNames(this.worldData.enabledFeatures()).stream().map(Identifier::toString).collect(Collectors.joining(", ")));
/*      */ 
/*      */     
/* 1265 */     systemReport.setDetail("World Generation", () -> this.worldData.worldGenSettingsLifecycle().toString());
/* 1266 */     systemReport.setDetail("World Seed", () -> String.valueOf(this.worldData.worldGenOptions().seed()));
/*      */     
/* 1268 */     Objects.requireNonNull(this.suppressedExceptions); systemReport.setDetail("Suppressed Exceptions", this.suppressedExceptions::dump);
/*      */     
/* 1270 */     if (this.serverId != null) {
/* 1271 */       systemReport.setDetail("Server Id", () -> this.serverId);
/*      */     }
/*      */     
/* 1274 */     return fillServerSystemReport(systemReport);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ModCheck getModdedStatus() {
/* 1280 */     return ModCheck.identify("vanilla", this::getServerModName, "Server", MinecraftServer.class);
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendSystemMessage(Component message) {
/* 1285 */     LOGGER.info(message.getString());
/*      */   }
/*      */   
/*      */   public KeyPair getKeyPair() {
/* 1289 */     return Objects.<KeyPair>requireNonNull(this.keyPair);
/*      */   }
/*      */   
/*      */   public int getPort() {
/* 1293 */     return this.port;
/*      */   }
/*      */   
/*      */   public void setPort(int port) {
/* 1297 */     this.port = port;
/*      */   }
/*      */   
/*      */   public GameProfile getSingleplayerProfile() {
/* 1301 */     return this.singleplayerProfile;
/*      */   }
/*      */   
/*      */   public void setSingleplayerProfile(GameProfile singleplayerProfile) {
/* 1305 */     this.singleplayerProfile = singleplayerProfile;
/*      */   }
/*      */   
/*      */   public boolean isSingleplayer() {
/* 1309 */     return (this.singleplayerProfile != null);
/*      */   }
/*      */   
/*      */   protected void initializeKeyPair() {
/* 1313 */     LOGGER.info("Generating keypair");
/*      */     try {
/* 1315 */       this.keyPair = Crypt.generateKeyPair();
/* 1316 */     } catch (CryptException e) {
/* 1317 */       throw new IllegalStateException("Failed to generate key pair", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setDifficulty(Difficulty difficulty, boolean ignoreLock) {
/* 1322 */     if (!ignoreLock && this.worldData.isDifficultyLocked()) {
/*      */       return;
/*      */     }
/*      */     
/* 1326 */     this.worldData.setDifficulty(this.worldData.isHardcore() ? Difficulty.HARD : difficulty);
/*      */     
/* 1328 */     updateMobSpawningFlags();
/* 1329 */     getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
/*      */   }
/*      */   
/*      */   public int getScaledTrackingDistance(int baseRange) {
/* 1333 */     return baseRange;
/*      */   }
/*      */   
/*      */   public void updateMobSpawningFlags() {
/* 1337 */     for (ServerLevel level : getAllLevels()) {
/* 1338 */       level.setSpawnSettings(level.isSpawningMonsters());
/*      */     }
/*      */   }
/*      */   
/*      */   public void setDifficultyLocked(boolean locked) {
/* 1343 */     this.worldData.setDifficultyLocked(locked);
/* 1344 */     getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
/*      */   }
/*      */   
/*      */   private void sendDifficultyUpdate(ServerPlayer player) {
/* 1348 */     LevelData levelData = player.level().getLevelData();
/* 1349 */     player.connection.send((Packet)new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
/*      */   }
/*      */   
/*      */   public boolean isDemo() {
/* 1353 */     return this.isDemo;
/*      */   }
/*      */   
/*      */   public void setDemo(boolean demo) {
/* 1357 */     this.isDemo = demo;
/*      */   }
/*      */   
/*      */   public Map<String, String> getCodeOfConducts() {
/* 1361 */     return Map.of();
/*      */   }
/*      */   
/*      */   public Optional<ServerResourcePackInfo> getServerResourcePack() {
/* 1365 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public boolean isResourcePackRequired() {
/* 1369 */     return getServerResourcePack().filter(ServerResourcePackInfo::isRequired).isPresent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesAuthentication() {
/* 1377 */     return this.onlineMode;
/*      */   }
/*      */   
/*      */   public void setUsesAuthentication(boolean onlineMode) {
/* 1381 */     this.onlineMode = onlineMode;
/*      */   }
/*      */   
/*      */   public boolean getPreventProxyConnections() {
/* 1385 */     return this.preventProxyConnections;
/*      */   }
/*      */   
/*      */   public void setPreventProxyConnections(boolean preventProxyConnections) {
/* 1389 */     this.preventProxyConnections = preventProxyConnections;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean allowFlight() {
/* 1395 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMotd() {
/* 1400 */     return this.motd;
/*      */   }
/*      */   
/*      */   public void setMotd(String motd) {
/* 1404 */     this.motd = motd;
/*      */   }
/*      */   
/*      */   public boolean isStopped() {
/* 1408 */     return this.stopped;
/*      */   }
/*      */   
/*      */   public PlayerList getPlayerList() {
/* 1412 */     return this.playerList;
/*      */   }
/*      */   
/*      */   public void setPlayerList(PlayerList players) {
/* 1416 */     this.playerList = players;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultGameType(GameType gameType) {
/* 1422 */     this.worldData.setGameType(gameType);
/*      */   }
/*      */   
/*      */   public int enforceGameTypeForPlayers(GameType gameType) {
/* 1426 */     if (gameType == null) {
/* 1427 */       return 0;
/*      */     }
/* 1429 */     int count = 0;
/* 1430 */     for (ServerPlayer player : (Iterable<ServerPlayer>)getPlayerList().getPlayers()) {
/* 1431 */       if (player.setGameMode(gameType)) {
/* 1432 */         count++;
/*      */       }
/*      */     } 
/* 1435 */     return count;
/*      */   }
/*      */   
/*      */   public ServerConnectionListener getConnection() {
/* 1439 */     return this.connection;
/*      */   }
/*      */   
/*      */   public boolean isReady() {
/* 1443 */     return this.isReady;
/*      */   }
/*      */   
/*      */   public boolean publishServer(GameType gameMode, boolean allowCommands, int port) {
/* 1447 */     return false;
/*      */   }
/*      */   
/*      */   public int getTickCount() {
/* 1451 */     return this.tickCount;
/*      */   }
/*      */   
/*      */   public boolean isUnderSpawnProtection(ServerLevel level, BlockPos pos, Player player) {
/* 1455 */     return false;
/*      */   }
/*      */   
/*      */   public boolean repliesToStatus() {
/* 1459 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hidesOnlinePlayers() {
/* 1463 */     return false;
/*      */   }
/*      */   
/*      */   public Proxy getProxy() {
/* 1467 */     return this.proxy;
/*      */   }
/*      */   
/*      */   public int playerIdleTimeout() {
/* 1471 */     return this.playerIdleTimeout;
/*      */   }
/*      */   
/*      */   public void setPlayerIdleTimeout(int playerIdleTimeout) {
/* 1475 */     this.playerIdleTimeout = playerIdleTimeout;
/*      */   }
/*      */   
/*      */   public Services services() {
/* 1479 */     return this.services;
/*      */   }
/*      */   
/*      */   public ServerStatus getStatus() {
/* 1483 */     return this.status;
/*      */   }
/*      */   
/*      */   public void invalidateStatus() {
/* 1487 */     this.lastServerStatus = 0L;
/*      */   }
/*      */   
/*      */   public int getAbsoluteMaxWorldSize() {
/* 1491 */     return 29999984;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean scheduleExecutables() {
/* 1496 */     return (super.scheduleExecutables() && !isStopped());
/*      */   }
/*      */ 
/*      */   
/*      */   public void executeIfPossible(Runnable command) {
/* 1501 */     if (isStopped()) {
/* 1502 */       throw new java.util.concurrent.RejectedExecutionException("Server already shutting down");
/*      */     }
/* 1504 */     super.executeIfPossible(command);
/*      */   }
/*      */ 
/*      */   
/*      */   public Thread getRunningThread() {
/* 1509 */     return this.serverThread;
/*      */   }
/*      */   
/*      */   public int getCompressionThreshold() {
/* 1513 */     return 256;
/*      */   }
/*      */   
/*      */   public boolean enforceSecureProfile() {
/* 1517 */     return false;
/*      */   }
/*      */   
/*      */   public long getNextTickTime() {
/* 1521 */     return this.nextTickTimeNanos;
/*      */   }
/*      */   
/*      */   public DataFixer getFixerUpper() {
/* 1525 */     return this.fixerUpper;
/*      */   }
/*      */   
/*      */   public ServerAdvancementManager getAdvancements() {
/* 1529 */     return this.resources.managers.getAdvancements();
/*      */   }
/*      */   
/*      */   public ServerFunctionManager getFunctions() {
/* 1533 */     return this.functionManager;
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> reloadResources(Collection<String> packsToEnable) {
/* 1537 */     CompletableFuture<Void> result = CompletableFuture.supplyAsync(() -> { Objects.requireNonNull(this.packRepository); return packsToEnable.stream().map(this.packRepository::getPack).filter(Objects::nonNull).map(Pack::open).collect(ImmutableList.toImmutableList()); }, (Executor)this)
/*      */ 
/*      */       
/* 1540 */       .thenCompose(packsToLoad -> {
/*      */           MultiPackResourceManager multiPackResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, (List)packsToLoad);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           List<Registry.PendingTags<?>> postponedTags = TagLoader.loadTagsForExistingRegistries((ResourceManager)multiPackResourceManager, (RegistryAccess)this.registries.compositeAccess());
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           return ReloadableServerResources.loadResources((ResourceManager)multiPackResourceManager, this.registries, postponedTags, this.worldData.enabledFeatures(), isDedicatedServer() ? Commands.CommandSelection.DEDICATED : Commands.CommandSelection.INTEGRATED, getFunctionCompilationPermissions(), this.executor, (Executor)this).whenComplete(()).thenApply(());
/* 1553 */         }).thenAcceptAsync(newResources -> {
/*      */           this.resources.close();
/*      */           
/*      */           this.resources = packsToEnable;
/*      */           
/*      */           this.packRepository.setSelected(packsToEnable);
/*      */           
/*      */           WorldDataConfiguration newConfig = new WorldDataConfiguration(getSelectedPacks(this.packRepository, true), this.worldData.enabledFeatures());
/*      */           
/*      */           this.worldData.setDataConfiguration(newConfig);
/*      */           
/*      */           this.resources.managers.updateStaticRegistryTags();
/*      */           this.resources.managers.getRecipeManager().finalizeRecipeLoading(this.worldData.enabledFeatures());
/*      */           getPlayerList().saveAll();
/*      */           getPlayerList().reloadResources();
/*      */           this.functionManager.replaceLibrary(this.resources.managers.getFunctionLibrary());
/*      */           this.structureTemplateManager.onResourceManagerReload((ResourceManager)this.resources.resourceManager);
/*      */           this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)this.registries.compositeAccess(), this.worldData.enabledFeatures());
/*      */         }, (Executor)this);
/* 1572 */     if (isSameThread()) {
/* 1573 */       Objects.requireNonNull(result); managedBlock(result::isDone);
/*      */     } 
/* 1575 */     return result;
/*      */   }
/*      */   
/*      */   public static WorldDataConfiguration configurePackRepository(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean initMode, boolean safeMode) {
/* 1579 */     DataPackConfig dataPackConfig = initialDataConfig.dataPacks();
/*      */     
/* 1581 */     FeatureFlagSet forcedFeatures = initMode ? FeatureFlagSet.of() : initialDataConfig.enabledFeatures();
/* 1582 */     FeatureFlagSet allowedFeatures = initMode ? FeatureFlags.REGISTRY.allFlags() : initialDataConfig.enabledFeatures();
/*      */     
/* 1584 */     packRepository.reload();
/*      */     
/* 1586 */     if (safeMode) {
/* 1587 */       return configureRepositoryWithSelection(packRepository, List.of("vanilla"), forcedFeatures, false);
/*      */     }
/*      */     
/* 1590 */     Set<String> selected = Sets.newLinkedHashSet();
/*      */     
/* 1592 */     for (String id : (Iterable<String>)dataPackConfig.getEnabled()) {
/* 1593 */       if (packRepository.isAvailable(id)) {
/* 1594 */         selected.add(id); continue;
/*      */       } 
/* 1596 */       LOGGER.warn("Missing data pack {}", id);
/*      */     } 
/*      */ 
/*      */     
/* 1600 */     for (Pack pack : (Iterable<Pack>)packRepository.getAvailablePacks()) {
/* 1601 */       String packId = pack.getId();
/* 1602 */       if (dataPackConfig.getDisabled().contains(packId)) {
/*      */         continue;
/*      */       }
/* 1605 */       FeatureFlagSet packFeatures = pack.getRequestedFeatures();
/* 1606 */       boolean isSelected = selected.contains(packId);
/* 1607 */       if (!isSelected && pack.getPackSource().shouldAddAutomatically()) {
/* 1608 */         if (packFeatures.isSubsetOf(allowedFeatures)) {
/* 1609 */           LOGGER.info("Found new data pack {}, loading it automatically", packId);
/* 1610 */           selected.add(packId);
/*      */         } else {
/* 1612 */           LOGGER.info("Found new data pack {}, but can't load it due to missing features {}", packId, FeatureFlags.printMissingFlags(allowedFeatures, packFeatures));
/*      */         } 
/*      */       }
/* 1615 */       if (isSelected && !packFeatures.isSubsetOf(allowedFeatures)) {
/* 1616 */         LOGGER.warn("Pack {} requires features {} that are not enabled for this world, disabling pack.", packId, FeatureFlags.printMissingFlags(allowedFeatures, packFeatures));
/* 1617 */         selected.remove(packId);
/*      */       } 
/*      */     } 
/*      */     
/* 1621 */     if (selected.isEmpty()) {
/* 1622 */       LOGGER.info("No datapacks selected, forcing vanilla");
/* 1623 */       selected.add("vanilla");
/*      */     } 
/*      */     
/* 1626 */     return configureRepositoryWithSelection(packRepository, selected, forcedFeatures, true);
/*      */   }
/*      */   
/*      */   private static WorldDataConfiguration configureRepositoryWithSelection(PackRepository packRepository, Collection<String> selected, FeatureFlagSet forcedFeatures, boolean disableInactive) {
/* 1630 */     packRepository.setSelected(selected);
/* 1631 */     enableForcedFeaturePacks(packRepository, forcedFeatures);
/*      */ 
/*      */     
/* 1634 */     DataPackConfig packConfig = getSelectedPacks(packRepository, disableInactive);
/* 1635 */     FeatureFlagSet packRequestedFeatures = packRepository.getRequestedFeatureFlags().join(forcedFeatures);
/*      */     
/* 1637 */     return new WorldDataConfiguration(packConfig, packRequestedFeatures);
/*      */   }
/*      */   
/*      */   private static void enableForcedFeaturePacks(PackRepository packRepository, FeatureFlagSet forcedFeatures) {
/* 1641 */     FeatureFlagSet providedFeatures = packRepository.getRequestedFeatureFlags();
/* 1642 */     FeatureFlagSet missingFeatures = forcedFeatures.subtract(providedFeatures);
/* 1643 */     if (missingFeatures.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1647 */     ObjectArraySet<String> objectArraySet = new ObjectArraySet(packRepository.getSelectedIds());
/*      */     
/* 1649 */     for (Pack pack : (Iterable<Pack>)packRepository.getAvailablePacks()) {
/* 1650 */       if (missingFeatures.isEmpty()) {
/*      */         break;
/*      */       }
/*      */       
/* 1654 */       if (pack.getPackSource() != PackSource.FEATURE) {
/*      */         continue;
/*      */       }
/* 1657 */       String packId = pack.getId();
/* 1658 */       FeatureFlagSet packFeatures = pack.getRequestedFeatures();
/* 1659 */       if (!packFeatures.isEmpty() && packFeatures.intersects(missingFeatures) && packFeatures.isSubsetOf(forcedFeatures)) {
/* 1660 */         if (!objectArraySet.add(packId)) {
/* 1661 */           throw new IllegalStateException("Tried to force '" + packId + "', but it was already enabled");
/*      */         }
/* 1663 */         LOGGER.info("Found feature pack ('{}') for requested feature, forcing to enabled", packId);
/* 1664 */         missingFeatures = missingFeatures.subtract(packFeatures);
/*      */       } 
/*      */     } 
/*      */     
/* 1668 */     packRepository.setSelected((Collection)objectArraySet);
/*      */   }
/*      */   
/*      */   private static DataPackConfig getSelectedPacks(PackRepository packRepository, boolean disableInactive) {
/* 1672 */     Collection<String> selected = packRepository.getSelectedIds();
/* 1673 */     ImmutableList immutableList = ImmutableList.copyOf(selected);
/* 1674 */     List<String> disabled = disableInactive ? packRepository.getAvailableIds().stream().filter(id -> !selected.contains(id)).toList() : List.<String>of();
/* 1675 */     return new DataPackConfig((List)immutableList, disabled);
/*      */   }
/*      */   
/*      */   public void kickUnlistedPlayers() {
/* 1679 */     if (!isEnforceWhitelist() || !isUsingWhitelist()) {
/*      */       return;
/*      */     }
/*      */     
/* 1683 */     PlayerList playerList = getPlayerList();
/* 1684 */     UserWhiteList whiteList = playerList.getWhiteList();
/*      */     
/* 1686 */     List<ServerPlayer> players = Lists.newArrayList(playerList.getPlayers());
/* 1687 */     for (ServerPlayer player : players) {
/* 1688 */       if (!whiteList.isWhiteListed(player.nameAndId())) {
/* 1689 */         player.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.not_whitelisted"));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public PackRepository getPackRepository() {
/* 1695 */     return this.packRepository;
/*      */   }
/*      */   
/*      */   public Commands getCommands() {
/* 1699 */     return this.resources.managers.getCommands();
/*      */   }
/*      */   
/*      */   public CommandSourceStack createCommandSourceStack() {
/* 1703 */     ServerLevel level = findRespawnDimension();
/* 1704 */     return new CommandSourceStack(this, Vec3.atLowerCornerOf((Vec3i)getRespawnData().pos()), Vec2.ZERO, level, (PermissionSet)LevelBasedPermissionSet.OWNER, "Server", (Component)Component.literal("Server"), this, null);
/*      */   }
/*      */   
/*      */   public ServerLevel findRespawnDimension() {
/* 1708 */     LevelData.RespawnData respawnData = getWorldData().overworldData().getRespawnData();
/* 1709 */     ResourceKey<Level> respawnDimension = respawnData.dimension();
/* 1710 */     ServerLevel respawnLevel = getLevel(respawnDimension);
/* 1711 */     return (respawnLevel != null) ? respawnLevel : overworld();
/*      */   }
/*      */   
/*      */   public void setRespawnData(LevelData.RespawnData respawnData) {
/* 1715 */     ServerLevelData levelData = this.worldData.overworldData();
/* 1716 */     LevelData.RespawnData oldRespawnData = levelData.getRespawnData();
/* 1717 */     if (!oldRespawnData.equals(respawnData)) {
/* 1718 */       levelData.setSpawn(respawnData);
/* 1719 */       getPlayerList().broadcastAll((Packet)new net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket(respawnData));
/* 1720 */       updateEffectiveRespawnData();
/*      */     } 
/*      */   }
/*      */   
/*      */   public LevelData.RespawnData getRespawnData() {
/* 1725 */     return this.effectiveRespawnData;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean acceptsSuccess() {
/* 1730 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean acceptsFailure() {
/* 1735 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RecipeManager getRecipeManager() {
/* 1742 */     return this.resources.managers.getRecipeManager();
/*      */   }
/*      */   
/*      */   public ServerScoreboard getScoreboard() {
/* 1746 */     return this.scoreboard;
/*      */   }
/*      */   
/*      */   public CommandStorage getCommandStorage() {
/* 1750 */     if (this.commandStorage == null)
/*      */     {
/* 1752 */       throw new NullPointerException("Called before server init");
/*      */     }
/* 1754 */     return this.commandStorage;
/*      */   }
/*      */   
/*      */   public Stopwatches getStopwatches() {
/* 1758 */     if (this.stopwatches == null)
/*      */     {
/* 1760 */       throw new NullPointerException("Called before server init");
/*      */     }
/* 1762 */     return this.stopwatches;
/*      */   }
/*      */   
/*      */   public CustomBossEvents getCustomBossEvents() {
/* 1766 */     return this.customBossEvents;
/*      */   }
/*      */   
/*      */   public boolean isEnforceWhitelist() {
/* 1770 */     return this.enforceWhitelist;
/*      */   }
/*      */   
/*      */   public void setEnforceWhitelist(boolean enforceWhitelist) {
/* 1774 */     this.enforceWhitelist = enforceWhitelist;
/*      */   }
/*      */   
/*      */   public boolean isUsingWhitelist() {
/* 1778 */     return this.usingWhitelist;
/*      */   }
/*      */   
/*      */   public void setUsingWhitelist(boolean usingWhitelist) {
/* 1782 */     this.usingWhitelist = usingWhitelist;
/*      */   }
/*      */   
/*      */   public float getCurrentSmoothedTickTime() {
/* 1786 */     return this.smoothedTickTimeMillis;
/*      */   }
/*      */   
/*      */   public ServerTickRateManager tickRateManager() {
/* 1790 */     return this.tickRateManager;
/*      */   }
/*      */   
/*      */   public long getAverageTickTimeNanos() {
/* 1794 */     return this.aggregatedTickTimesNanos / Math.min(100, Math.max(this.tickCount, 1));
/*      */   }
/*      */   
/*      */   public long[] getTickTimesNanos() {
/* 1798 */     return this.tickTimesNanos;
/*      */   }
/*      */   
/*      */   public LevelBasedPermissionSet getProfilePermissions(NameAndId nameAndId) {
/* 1802 */     if (getPlayerList().isOp(nameAndId)) {
/* 1803 */       ServerOpListEntry opListEntry = (ServerOpListEntry)getPlayerList().getOps().get(nameAndId);
/* 1804 */       if (opListEntry != null) {
/* 1805 */         return opListEntry.permissions();
/*      */       }
/* 1807 */       if (isSingleplayerOwner(nameAndId)) {
/* 1808 */         return LevelBasedPermissionSet.OWNER;
/*      */       }
/* 1810 */       if (isSingleplayer()) {
/* 1811 */         return getPlayerList().isAllowCommandsForAllPlayers() ? LevelBasedPermissionSet.OWNER : LevelBasedPermissionSet.ALL;
/*      */       }
/* 1813 */       return operatorUserPermissions();
/*      */     } 
/* 1815 */     return LevelBasedPermissionSet.ALL;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void dumpServerProperties(Path path) throws IOException {}
/*      */ 
/*      */   
/*      */   private void saveDebugReport(Path output) {
/* 1824 */     Path levelsDir = output.resolve("levels");
/*      */     
/*      */     try {
/* 1827 */       for (Map.Entry<ResourceKey<Level>, ServerLevel> level : this.levels.entrySet()) {
/* 1828 */         Identifier levelId = ((ResourceKey)level.getKey()).identifier();
/* 1829 */         Path levelPath = levelsDir.resolve(levelId.getNamespace()).resolve(levelId.getPath());
/* 1830 */         Files.createDirectories(levelPath, (FileAttribute<?>[])new FileAttribute[0]);
/* 1831 */         ((ServerLevel)level.getValue()).saveDebugReport(levelPath);
/*      */       } 
/*      */       
/* 1834 */       dumpGameRules(output.resolve("gamerules.txt"));
/* 1835 */       dumpClasspath(output.resolve("classpath.txt"));
/* 1836 */       dumpMiscStats(output.resolve("stats.txt"));
/* 1837 */       dumpThreads(output.resolve("threads.txt"));
/* 1838 */       dumpServerProperties(output.resolve("server.properties.txt"));
/* 1839 */       dumpNativeModules(output.resolve("modules.txt"));
/* 1840 */     } catch (IOException e) {
/* 1841 */       LOGGER.warn("Failed to save debug report", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpMiscStats(Path path) throws IOException {
/* 1846 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]); 
/* 1847 */     try { output.write(String.format(Locale.ROOT, "pending_tasks: %d\n", new Object[] { getPendingTasksCount() }));
/* 1848 */       output.write(String.format(Locale.ROOT, "average_tick_time: %f\n", new Object[] { getCurrentSmoothedTickTime() }));
/* 1849 */       output.write(String.format(Locale.ROOT, "tick_times: %s\n", new Object[] { Arrays.toString(this.tickTimesNanos) }));
/* 1850 */       output.write(String.format(Locale.ROOT, "queue: %s\n", new Object[] { Util.backgroundExecutor() }));
/* 1851 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/* 1855 */      } private void dumpGameRules(Path path) throws IOException { Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]); 
/* 1856 */     try { final List<String> entries = Lists.newArrayList();
/* 1857 */       final GameRules gameRules = this.worldData.getGameRules();
/* 1858 */       gameRules.visitGameRuleTypes(new GameRuleTypeVisitor(this)
/*      */           {
/*      */             public <T> void visit(GameRule<T> gameRule) {
/* 1861 */               entries.add(String.format(Locale.ROOT, "%s=%s\n", new Object[] { gameRule.getIdentifier(), gameRules.getAsString(gameRule) }));
/*      */             }
/*      */           });
/* 1864 */       for (String entry : entries) {
/* 1865 */         output.write(entry);
/*      */       }
/* 1867 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/* 1871 */      } private void dumpClasspath(Path path) throws IOException { Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]); 
/* 1872 */     try { String classpath = System.getProperty("java.class.path");
/* 1873 */       String separator = File.pathSeparator;
/* 1874 */       for (String s : (Iterable<String>)Splitter.on(separator).split(classpath)) {
/* 1875 */         output.write(s);
/* 1876 */         output.write("\n");
/*      */       } 
/* 1878 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/* 1882 */      } private void dumpThreads(Path path) throws IOException { ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
/* 1883 */     ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
/* 1884 */     Arrays.sort(threadInfos, Comparator.comparing(ThreadInfo::getThreadName));
/*      */     
/* 1886 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]); 
/* 1887 */     try { for (ThreadInfo threadInfo : threadInfos) {
/* 1888 */         output.write(threadInfo.toString());
/* 1889 */         output.write(10);
/*      */       } 
/* 1891 */       if (output != null) output.close();  } catch (Throwable throwable) { if (output != null)
/*      */         try { output.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/* 1895 */      } private void dumpNativeModules(Path path) throws IOException { Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]); 
/*      */     try { List<NativeModuleLister.NativeModuleInfo> modules;
/*      */       
/* 1898 */       try { modules = Lists.newArrayList(NativeModuleLister.listModules()); }
/* 1899 */       catch (Throwable t)
/* 1900 */       { LOGGER.warn("Failed to list native modules", t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1908 */         if (output != null) output.close();  return; }  modules.sort(Comparator.comparing(module -> module.name)); for (NativeModuleLister.NativeModuleInfo module : modules) { output.write(module.toString()); output.write(10); }  if (output != null) output.close();  } catch (Throwable modules) { if (output != null)
/*      */         try { output.close(); }
/*      */         catch (Throwable throwable) { modules.addSuppressed(throwable); }
/*      */           throw modules; }
/* 1912 */      } private ProfilerFiller createProfiler() { if (this.willStartRecordingMetrics) {
/* 1913 */       this.metricsRecorder = (MetricsRecorder)ActiveMetricsRecorder.createStarted((net.minecraft.util.profiling.metrics.MetricsSamplerProvider)new ServerMetricsSamplersProvider((LongSupplier)Util.timeSource, 
/* 1914 */             isDedicatedServer()), (LongSupplier)Util.timeSource, 
/*      */           
/* 1916 */           (Executor)Util.ioPool(), new MetricsPersister("server"), this.onMetricsRecordingStopped, reportPath -> {
/*      */             executeBlocking(());
/*      */ 
/*      */ 
/*      */             
/*      */             this.onMetricsRecordingFinished.accept(reportPath);
/*      */           });
/*      */ 
/*      */ 
/*      */       
/* 1926 */       this.willStartRecordingMetrics = false;
/*      */     } 
/*      */     
/* 1929 */     this.metricsRecorder.startTick();
/*      */     
/* 1931 */     return SingleTickProfiler.decorateFiller(this.metricsRecorder.getProfiler(), SingleTickProfiler.createTickProfiler("Server")); }
/*      */ 
/*      */   
/*      */   protected void endMetricsRecordingTick() {
/* 1935 */     this.metricsRecorder.endTick();
/*      */   }
/*      */   
/*      */   public boolean isRecordingMetrics() {
/* 1939 */     return this.metricsRecorder.isRecording();
/*      */   }
/*      */   
/*      */   public void startRecordingMetrics(Consumer<ProfileResults> onStopped, Consumer<Path> onFinished) {
/* 1943 */     this.onMetricsRecordingStopped = (report -> {
/*      */         stopRecordingMetrics();
/*      */         onStopped.accept(onStopped);
/*      */       });
/* 1947 */     this.onMetricsRecordingFinished = onFinished;
/* 1948 */     this.willStartRecordingMetrics = true;
/*      */   }
/*      */   
/*      */   public void stopRecordingMetrics() {
/* 1952 */     this.metricsRecorder = InactiveMetricsRecorder.INSTANCE;
/*      */   }
/*      */   
/*      */   public void finishRecordingMetrics() {
/* 1956 */     this.metricsRecorder.end();
/*      */   }
/*      */   
/*      */   public void cancelRecordingMetrics() {
/* 1960 */     this.metricsRecorder.cancel();
/*      */   }
/*      */   
/*      */   public Path getWorldPath(LevelResource resource) {
/* 1964 */     return this.storageSource.getLevelPath(resource);
/*      */   }
/*      */   
/*      */   public boolean forceSynchronousWrites() {
/* 1968 */     return true;
/*      */   }
/*      */   
/*      */   public StructureTemplateManager getStructureManager() {
/* 1972 */     return this.structureTemplateManager;
/*      */   }
/*      */   
/*      */   public WorldData getWorldData() {
/* 1976 */     return this.worldData;
/*      */   }
/*      */   
/*      */   public RegistryAccess.Frozen registryAccess() {
/* 1980 */     return this.registries.compositeAccess();
/*      */   }
/*      */   
/*      */   public LayeredRegistryAccess<RegistryLayer> registries() {
/* 1984 */     return this.registries;
/*      */   }
/*      */   
/*      */   public ReloadableServerRegistries.Holder reloadableRegistries() {
/* 1988 */     return this.resources.managers.fullRegistries();
/*      */   }
/*      */   
/*      */   public TextFilter createTextFilterForPlayer(ServerPlayer player) {
/* 1992 */     return TextFilter.DUMMY;
/*      */   }
/*      */   
/*      */   public ServerPlayerGameMode createGameModeForPlayer(ServerPlayer player) {
/* 1996 */     return isDemo() ? (ServerPlayerGameMode)new DemoMode(player) : new ServerPlayerGameMode(player);
/*      */   }
/*      */   
/*      */   public GameType getForcedGameType() {
/* 2000 */     return null;
/*      */   }
/*      */   
/*      */   public ResourceManager getResourceManager() {
/* 2004 */     return (ResourceManager)this.resources.resourceManager;
/*      */   }
/*      */   
/*      */   public boolean isCurrentlySaving() {
/* 2008 */     return this.isSaving;
/*      */   }
/*      */   
/*      */   public boolean isTimeProfilerRunning() {
/* 2012 */     return (this.debugCommandProfilerDelayStart || this.debugCommandProfiler != null);
/*      */   }
/*      */   
/*      */   public void startTimeProfiler() {
/* 2016 */     this.debugCommandProfilerDelayStart = true;
/*      */   }
/*      */   
/*      */   public ProfileResults stopTimeProfiler() {
/* 2020 */     if (this.debugCommandProfiler == null) {
/* 2021 */       return (ProfileResults)EmptyProfileResults.EMPTY;
/*      */     }
/* 2023 */     ProfileResults results = this.debugCommandProfiler.stop(Util.getNanos(), this.tickCount);
/* 2024 */     this.debugCommandProfiler = null;
/* 2025 */     return results;
/*      */   }
/*      */   
/*      */   public int getMaxChainedNeighborUpdates() {
/* 2029 */     return 1000000;
/*      */   }
/*      */   
/*      */   public void logChatMessage(Component message, ChatType.Bound chatType, String tag) {
/* 2033 */     String decoratedMessage = chatType.decorate(message).getString();
/* 2034 */     if (tag != null) {
/* 2035 */       LOGGER.info("[{}] {}", tag, decoratedMessage);
/*      */     } else {
/* 2037 */       LOGGER.info("{}", decoratedMessage);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ChatDecorator getChatDecorator() {
/* 2043 */     return ChatDecorator.PLAIN;
/*      */   }
/*      */   
/*      */   public boolean logIPs() {
/* 2047 */     return true;
/*      */   }
/*      */   
/*      */   public void handleCustomClickAction(Identifier id, Optional<Tag> payload) {
/* 2051 */     LOGGER.debug("Received custom click action {} with payload {}", id, payload.orElse(null));
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelLoadListener getLevelLoadListener() {
/* 2056 */     return this.levelLoadListener;
/*      */   }
/*      */   
/*      */   public boolean setAutoSave(boolean enable) {
/*      */     boolean success = false;
/* 2061 */     for (ServerLevel level : getAllLevels()) {
/* 2062 */       if (level != null && level.noSave == enable) {
/* 2063 */         level.noSave = !enable;
/* 2064 */         success = true;
/*      */       } 
/*      */     } 
/* 2067 */     return success;
/*      */   }
/*      */   
/*      */   public boolean isAutoSave() {
/* 2071 */     for (ServerLevel level : getAllLevels()) {
/* 2072 */       if (level != null && !level.noSave) {
/* 2073 */         return true;
/*      */       }
/*      */     } 
/* 2076 */     return false;
/*      */   }
/*      */   
/*      */   public <T> void onGameRuleChanged(GameRule<T> rule, T value) {
/* 2080 */     notificationManager().onGameRuleChanged(rule, value);
/* 2081 */     if (rule == GameRules.REDUCED_DEBUG_INFO) {
/* 2082 */       byte event = (Boolean)value ? 22 : 23;
/* 2083 */       for (ServerPlayer player : (Iterable<ServerPlayer>)getPlayerList().getPlayers()) {
/* 2084 */         player.connection.send((Packet)new ClientboundEntityEventPacket((Entity)player, event));
/*      */       }
/* 2086 */     } else if (rule == GameRules.LIMITED_CRAFTING || rule == GameRules.IMMEDIATE_RESPAWN) {
/* 2087 */       ClientboundGameEventPacket.Type eventType = (rule == GameRules.LIMITED_CRAFTING) ? ClientboundGameEventPacket.LIMITED_CRAFTING : ClientboundGameEventPacket.IMMEDIATE_RESPAWN;
/* 2088 */       ClientboundGameEventPacket packet = new ClientboundGameEventPacket(eventType, (Boolean)value ? 1.0F : 0.0F);
/* 2089 */       getPlayerList().getPlayers().forEach(player -> player.connection.send((Packet)packet));
/* 2090 */     } else if (rule == GameRules.LOCATOR_BAR) {
/* 2091 */       getAllLevels().forEach(level -> {
/*      */             ServerWaypointManager waypointManager = level.getWaypointManager(); if ((Boolean)value) {
/*      */               Objects.requireNonNull(waypointManager);
/*      */               level.players().forEach(waypointManager::updatePlayer);
/*      */             } else {
/*      */               waypointManager.breakAllConnections();
/*      */             } 
/*      */           });
/* 2099 */     } else if (rule == GameRules.SPAWN_MONSTERS) {
/* 2100 */       updateMobSpawningFlags();
/*      */     } 
/*      */   }
/*      */   public static final class ServerResourcePackInfo extends Record { private final UUID id; private final String url; private final String hash; private final boolean isRequired; private final Component prompt;
/* 2104 */     public ServerResourcePackInfo(UUID id, String url, String hash, boolean isRequired, Component prompt) { this.id = id; this.url = url; this.hash = hash; this.isRequired = isRequired; this.prompt = prompt; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2104	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2104	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2104	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;
/* 2104 */       //   0	8	1	o	Ljava/lang/Object; } public UUID id() { return this.id; } public String url() { return this.url; } public String hash() { return this.hash; } public boolean isRequired() { return this.isRequired; } public Component prompt() { return this.prompt; }
/*      */      } private static final class ReloadableResources extends Record implements AutoCloseable { private final CloseableResourceManager resourceManager; private final ReloadableServerResources managers;
/* 2106 */     private ReloadableResources(CloseableResourceManager resourceManager, ReloadableServerResources managers) { this.resourceManager = resourceManager; this.managers = managers; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2106	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2106	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2106	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/* 2106 */       //   0	8	1	o	Ljava/lang/Object; } public CloseableResourceManager resourceManager() { return this.resourceManager; } public ReloadableServerResources managers() { return this.managers; }
/*      */     
/*      */     public void close() {
/* 2109 */       this.resourceManager.close();
/*      */     } }
/*      */ 
/*      */   
/*      */   private static class TimeProfiler {
/*      */     private final long startNanos;
/*      */     private final int startTick;
/*      */     
/*      */     private TimeProfiler(long startNanos, int startTick) {
/* 2118 */       this.startNanos = startNanos;
/* 2119 */       this.startTick = startTick;
/*      */     }
/*      */     
/*      */     private ProfileResults stop(final long stopNanos, final int stopTick) {
/* 2123 */       return new ProfileResults()
/*      */         {
/*      */           public List<ResultField> getTimes(String path) {
/* 2126 */             return Collections.emptyList();
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean saveResults(Path file) {
/* 2131 */             return false;
/*      */           }
/*      */ 
/*      */           
/*      */           public long getStartTimeNano() {
/* 2136 */             return MinecraftServer.TimeProfiler.this.startNanos;
/*      */           }
/*      */ 
/*      */           
/*      */           public int getStartTimeTicks() {
/* 2141 */             return MinecraftServer.TimeProfiler.this.startTick;
/*      */           }
/*      */ 
/*      */           
/*      */           public long getEndTimeNano() {
/* 2146 */             return stopNanos;
/*      */           }
/*      */ 
/*      */           
/*      */           public int getEndTimeTicks() {
/* 2151 */             return stopTick;
/*      */           }
/*      */           
/*      */           public String getProfilerResults()
/*      */           {
/* 2156 */             return ""; } }; } } class null implements ProfileResults { public List<ResultField> getTimes(String path) { return Collections.emptyList(); } public String getProfilerResults() { return ""; }
/*      */     public boolean saveResults(Path file) { return false; }
/*      */     public long getStartTimeNano() { return MinecraftServer.TimeProfiler.this.startNanos; }
/*      */     public int getStartTimeTicks() { return MinecraftServer.TimeProfiler.this.startTick; } public long getEndTimeNano() { return stopNanos; } public int getEndTimeTicks() {
/*      */       return stopTick;
/*      */     } }
/*      */    public boolean acceptsTransfers() {
/* 2163 */     return false;
/*      */   }
/*      */   
/*      */   private void storeChunkIoError(CrashReport report, ChunkPos pos, RegionStorageInfo storageInfo) {
/* 2167 */     Util.ioPool().execute(() -> {
/*      */           try {
/*      */             Path debugDir = getFile("debug");
/*      */             FileUtil.createDirectoriesSafe(debugDir);
/*      */             String sanitizedLevelName = FileUtil.sanitizeName(storageInfo.level());
/*      */             Path reportFile = debugDir.resolve("chunk-" + sanitizedLevelName + "-" + Util.getFilenameFormattedDateTime() + "-server.txt");
/*      */             FileStore fileStore = Files.getFileStore(debugDir);
/*      */             long remainingSpace = fileStore.getUsableSpace();
/*      */             if (remainingSpace < 8192L) {
/*      */               LOGGER.warn("Not storing chunk IO report due to low space on drive {}", fileStore.name());
/*      */               return;
/*      */             } 
/*      */             CrashReportCategory category = report.addCategory("Chunk Info");
/*      */             Objects.requireNonNull(storageInfo);
/*      */             category.setDetail("Level", storageInfo::level);
/*      */             category.setDetail("Dimension", ());
/*      */             Objects.requireNonNull(storageInfo);
/*      */             category.setDetail("Storage", storageInfo::type);
/*      */             Objects.requireNonNull(pos);
/*      */             category.setDetail("Position", pos::toString);
/*      */             report.saveToFile(reportFile, ReportType.CHUNK_IO_ERROR);
/*      */             LOGGER.info("Saved details to {}", report.getSaveFile());
/* 2189 */           } catch (Exception e) {
/*      */             LOGGER.warn("Failed to store chunk IO exception", e);
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void reportChunkLoadFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 2197 */     LOGGER.error("Failed to load chunk {},{}", new Object[] { pos.x, pos.z, throwable });
/* 2198 */     this.suppressedExceptions.addEntry("chunk/load", throwable);
/* 2199 */     storeChunkIoError(CrashReport.forThrowable(throwable, "Chunk load failure"), pos, storageInfo);
/*      */   }
/*      */ 
/*      */   
/*      */   public void reportChunkSaveFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 2204 */     LOGGER.error("Failed to save chunk {},{}", new Object[] { pos.x, pos.z, throwable });
/* 2205 */     this.suppressedExceptions.addEntry("chunk/save", throwable);
/* 2206 */     storeChunkIoError(CrashReport.forThrowable(throwable, "Chunk save failure"), pos, storageInfo);
/*      */   }
/*      */   
/*      */   public void reportPacketHandlingException(Throwable throwable, PacketType<?> packetType) {
/* 2210 */     this.suppressedExceptions.addEntry("packet/" + String.valueOf(packetType), throwable);
/*      */   }
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 2214 */     return this.potionBrewing;
/*      */   }
/*      */   
/*      */   public FuelValues fuelValues() {
/* 2218 */     return this.fuelValues;
/*      */   }
/*      */   
/*      */   public ServerLinks serverLinks() {
/* 2222 */     return ServerLinks.EMPTY;
/*      */   }
/*      */   
/*      */   protected int pauseWhenEmptySeconds() {
/* 2226 */     return 0;
/*      */   }
/*      */   
/*      */   public PacketProcessor packetProcessor() {
/* 2230 */     return this.packetProcessor;
/*      */   }
/*      */   
/*      */   public ServerDebugSubscribers debugSubscribers() {
/* 2234 */     return this.debugSubscribers;
/*      */   }
/*      */   
/*      */   protected abstract boolean initServer() throws IOException;
/*      */   
/*      */   public abstract LevelBasedPermissionSet operatorUserPermissions();
/*      */   
/*      */   public abstract PermissionSet getFunctionCompilationPermissions();
/*      */   
/*      */   public abstract boolean shouldRconBroadcast();
/*      */   
/*      */   protected abstract SampleLogger getTickTimeLogger();
/*      */   
/*      */   public abstract boolean isTickTimeLoggingEnabled();
/*      */   
/*      */   public abstract SystemReport fillServerSystemReport(SystemReport paramSystemReport);
/*      */   
/*      */   public abstract boolean isDedicatedServer();
/*      */   
/*      */   public abstract int getRateLimitPacketsPerSecond();
/*      */   
/*      */   public abstract boolean useNativeTransport();
/*      */   
/*      */   public abstract boolean isPublished();
/*      */   
/*      */   public abstract boolean shouldInformAdmins();
/*      */   
/*      */   public abstract boolean isSingleplayerOwner(NameAndId paramNameAndId);
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/MinecraftServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */