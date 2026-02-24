/*      */ package net.minecraft.client;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.exceptions.AuthenticationException;
/*      */ import com.mojang.authlib.minecraft.BanDetails;
/*      */ import com.mojang.authlib.minecraft.UserApiService;
/*      */ import com.mojang.authlib.yggdrasil.ProfileActionType;
/*      */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*      */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*      */ import com.mojang.blaze3d.TracyFrameCapture;
/*      */ import com.mojang.blaze3d.pipeline.MainTarget;
/*      */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*      */ import com.mojang.blaze3d.platform.ClientShutdownWatchdog;
/*      */ import com.mojang.blaze3d.platform.DisplayData;
/*      */ import com.mojang.blaze3d.platform.FramerateLimitTracker;
/*      */ import com.mojang.blaze3d.platform.GLX;
/*      */ import com.mojang.blaze3d.platform.IconSet;
/*      */ import com.mojang.blaze3d.platform.InputConstants;
/*      */ import com.mojang.blaze3d.platform.Window;
/*      */ import com.mojang.blaze3d.platform.WindowEventHandler;
/*      */ import com.mojang.blaze3d.shaders.ShaderType;
/*      */ import com.mojang.blaze3d.systems.GpuDevice;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import com.mojang.blaze3d.systems.TimerQuery;
/*      */ import com.mojang.blaze3d.vertex.Tesselator;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.jtracy.DiscontinuousFrame;
/*      */ import com.mojang.jtracy.TracyClient;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.realmsclient.RealmsMainScreen;
/*      */ import com.mojang.realmsclient.client.RealmsClient;
/*      */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.UncheckedIOException;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.net.Proxy;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionStage;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.LongSupplier;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.Optionull;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.SystemReport;
/*      */ import net.minecraft.client.color.block.BlockColors;
/*      */ import net.minecraft.client.entity.ClientMannequin;
/*      */ import net.minecraft.client.gui.Font;
/*      */ import net.minecraft.client.gui.Gui;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.LogoRenderer;
/*      */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*      */ import net.minecraft.client.gui.components.debug.DebugScreenEntryList;
/*      */ import net.minecraft.client.gui.components.debugchart.ProfilerPieChart;
/*      */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*      */ import net.minecraft.client.gui.components.toasts.Toast;
/*      */ import net.minecraft.client.gui.components.toasts.ToastManager;
/*      */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*      */ import net.minecraft.client.gui.font.FontManager;
/*      */ import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
/*      */ import net.minecraft.client.gui.screens.BanNoticeScreens;
/*      */ import net.minecraft.client.gui.screens.ChatScreen;
/*      */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*      */ import net.minecraft.client.gui.screens.DeathScreen;
/*      */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*      */ import net.minecraft.client.gui.screens.InBedChatScreen;
/*      */ import net.minecraft.client.gui.screens.LevelLoadingScreen;
/*      */ import net.minecraft.client.gui.screens.LoadingOverlay;
/*      */ import net.minecraft.client.gui.screens.OutOfMemoryScreen;
/*      */ import net.minecraft.client.gui.screens.Overlay;
/*      */ import net.minecraft.client.gui.screens.PauseScreen;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.TitleScreen;
/*      */ import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
/*      */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*      */ import net.minecraft.client.gui.screens.social.PlayerSocialManager;
/*      */ import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
/*      */ import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
/*      */ import net.minecraft.client.main.GameConfig;
/*      */ import net.minecraft.client.main.SilentInitException;
/*      */ import net.minecraft.client.model.geom.EntityModelSet;
/*      */ import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*      */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*      */ import net.minecraft.client.multiplayer.ProfileKeyPairManager;
/*      */ import net.minecraft.client.multiplayer.ServerData;
/*      */ import net.minecraft.client.multiplayer.chat.ChatListener;
/*      */ import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
/*      */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*      */ import net.minecraft.client.particle.ParticleEngine;
/*      */ import net.minecraft.client.particle.ParticleResources;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.player.LocalPlayerResolver;
/*      */ import net.minecraft.client.profiling.ClientMetricsSamplersProvider;
/*      */ import net.minecraft.client.quickplay.QuickPlay;
/*      */ import net.minecraft.client.quickplay.QuickPlayLog;
/*      */ import net.minecraft.client.renderer.GameRenderer;
/*      */ import net.minecraft.client.renderer.GpuWarnlistManager;
/*      */ import net.minecraft.client.renderer.LevelRenderer;
/*      */ import net.minecraft.client.renderer.MapRenderer;
/*      */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*      */ import net.minecraft.client.renderer.RenderBuffers;
/*      */ import net.minecraft.client.renderer.ShaderManager;
/*      */ import net.minecraft.client.renderer.VirtualScreen;
/*      */ import net.minecraft.client.renderer.block.BlockModelShaper;
/*      */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*      */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*      */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
/*      */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*      */ import net.minecraft.client.renderer.entity.EntityRenderers;
/*      */ import net.minecraft.client.renderer.entity.ItemRenderer;
/*      */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*      */ import net.minecraft.client.renderer.texture.SkinTextureDownloader;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.resources.ClientPackSource;
/*      */ import net.minecraft.client.resources.DryFoliageColorReloadListener;
/*      */ import net.minecraft.client.resources.FoliageColorReloadListener;
/*      */ import net.minecraft.client.resources.GrassColorReloadListener;
/*      */ import net.minecraft.client.resources.MapTextureManager;
/*      */ import net.minecraft.client.resources.SkinManager;
/*      */ import net.minecraft.client.resources.SplashManager;
/*      */ import net.minecraft.client.resources.WaypointStyleManager;
/*      */ import net.minecraft.client.resources.language.ClientLanguage;
/*      */ import net.minecraft.client.resources.language.I18n;
/*      */ import net.minecraft.client.resources.language.LanguageManager;
/*      */ import net.minecraft.client.resources.model.AtlasManager;
/*      */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*      */ import net.minecraft.client.resources.model.MaterialSet;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*      */ import net.minecraft.client.server.IntegratedServer;
/*      */ import net.minecraft.client.sounds.MusicManager;
/*      */ import net.minecraft.client.sounds.SoundManager;
/*      */ import net.minecraft.client.telemetry.ClientTelemetryManager;
/*      */ import net.minecraft.client.telemetry.TelemetryProperty;
/*      */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*      */ import net.minecraft.client.tutorial.Tutorial;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.gizmos.GizmoCollector;
/*      */ import net.minecraft.gizmos.Gizmos;
/*      */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.PacketProcessor;
/*      */ import net.minecraft.network.chat.ClickEvent;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.chat.contents.KeybindResolver;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*      */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.server.Bootstrap;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.Services;
/*      */ import net.minecraft.server.WorldStem;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.dialog.Dialogs;
/*      */ import net.minecraft.server.level.ChunkLevel;
/*      */ import net.minecraft.server.level.progress.LevelLoadListener;
/*      */ import net.minecraft.server.level.progress.LoggingLevelLoadListener;
/*      */ import net.minecraft.server.packs.PackResources;
/*      */ import net.minecraft.server.packs.PackType;
/*      */ import net.minecraft.server.packs.VanillaPackResources;
/*      */ import net.minecraft.server.packs.repository.FolderRepositorySource;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.server.packs.repository.PackSource;
/*      */ import net.minecraft.server.packs.repository.RepositorySource;
/*      */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*      */ import net.minecraft.server.packs.resources.ReloadInstance;
/*      */ import net.minecraft.server.packs.resources.ReloadableResourceManager;
/*      */ import net.minecraft.server.packs.resources.ResourceManager;
/*      */ import net.minecraft.server.players.ProfileResolver;
/*      */ import net.minecraft.sounds.Music;
/*      */ import net.minecraft.sounds.Musics;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.DialogTags;
/*      */ import net.minecraft.util.CommonLinks;
/*      */ import net.minecraft.util.FileUtil;
/*      */ import net.minecraft.util.FileZipper;
/*      */ import net.minecraft.util.MemoryReserve;
/*      */ import net.minecraft.util.ModCheck;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixers;
/*      */ import net.minecraft.util.profiling.ContinuousProfiler;
/*      */ import net.minecraft.util.profiling.EmptyProfileResults;
/*      */ import net.minecraft.util.profiling.InactiveProfiler;
/*      */ import net.minecraft.util.profiling.ProfileResults;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.profiling.SingleTickProfiler;
/*      */ import net.minecraft.util.profiling.Zone;
/*      */ import net.minecraft.util.profiling.metrics.profiling.ActiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.InactiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.BackgroundMusic;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.PiercingWeapon;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.validation.DirectoryValidator;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.EntityHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector3fc;
/*      */ import org.lwjgl.util.tinyfd.TinyFileDialogs;
/*      */ 
/*      */ public class Minecraft extends net.minecraft.util.thread.ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler {
/*      */   private static Minecraft instance;
/*  264 */   private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int MAX_TICKS_PER_UPDATE = 10;
/*  267 */   public static final Identifier DEFAULT_FONT = Identifier.withDefaultNamespace("default");
/*  268 */   public static final Identifier UNIFORM_FONT = Identifier.withDefaultNamespace("uniform");
/*  269 */   public static final Identifier ALT_FONT = Identifier.withDefaultNamespace("alt");
/*  270 */   private static final Identifier REGIONAL_COMPLIANCIES = Identifier.withDefaultNamespace("regional_compliancies.json");
/*  271 */   private static final CompletableFuture<Unit> RESOURCE_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
/*      */   
/*  273 */   private static final Component SOCIAL_INTERACTIONS_NOT_AVAILABLE = (Component)Component.translatable("multiplayer.socialInteractions.not_available");
/*  274 */   private static final Component SAVING_LEVEL = (Component)Component.translatable("menu.savingLevel");
/*      */ 
/*      */   
/*      */   public static final String UPDATE_DRIVERS_ADVICE = "Please make sure you have up-to-date drivers (see aka.ms/mcdriver for instructions).";
/*      */   
/*  279 */   private final long canary = Double.doubleToLongBits(Math.PI);
/*      */   
/*      */   private final Path resourcePackDirectory;
/*      */   
/*      */   private final CompletableFuture<ProfileResult> profileFuture;
/*      */   
/*      */   private final TextureManager textureManager;
/*      */   
/*      */   private final ShaderManager shaderManager;
/*      */   private final DataFixer fixerUpper;
/*      */   private final VirtualScreen virtualScreen;
/*      */   private final Window window;
/*  291 */   private final DeltaTracker.Timer deltaTracker = new DeltaTracker.Timer(20.0F, 0L, this::getTickTargetMillis);
/*      */   
/*      */   private final RenderBuffers renderBuffers;
/*      */   
/*      */   public final LevelRenderer levelRenderer;
/*      */   private final EntityRenderDispatcher entityRenderDispatcher;
/*      */   private final ItemModelResolver itemModelResolver;
/*      */   private final ItemRenderer itemRenderer;
/*      */   private final MapRenderer mapRenderer;
/*      */   public final ParticleEngine particleEngine;
/*      */   private final ParticleResources particleResources;
/*      */   private final User user;
/*      */   public final Font font;
/*      */   public final Font fontFilterFishy;
/*      */   public final GameRenderer gameRenderer;
/*      */   public final Gui gui;
/*      */   public final Options options;
/*      */   public final DebugScreenEntryList debugEntries;
/*      */   private final HotbarManager hotbarManager;
/*      */   public final MouseHandler mouseHandler;
/*      */   public final KeyboardHandler keyboardHandler;
/*  312 */   private InputType lastInputType = InputType.NONE;
/*      */   
/*      */   public final File gameDirectory;
/*      */   
/*      */   private final String launchedVersion;
/*      */   
/*      */   private final String versionType;
/*      */   
/*      */   private final Proxy proxy;
/*      */   private final boolean offlineDeveloperMode;
/*      */   private final LevelStorageSource levelSource;
/*      */   private final boolean demo;
/*      */   private final boolean allowsMultiplayer;
/*      */   private final boolean allowsChat;
/*      */   private final ReloadableResourceManager resourceManager;
/*      */   private final VanillaPackResources vanillaPackResources;
/*      */   private final DownloadedPackSource downloadedPackSource;
/*      */   private final PackRepository resourcePackRepository;
/*      */   private final LanguageManager languageManager;
/*      */   private final BlockColors blockColors;
/*      */   private final RenderTarget mainRenderTarget;
/*      */   private final TracyFrameCapture tracyFrameCapture;
/*      */   private final SoundManager soundManager;
/*      */   private final MusicManager musicManager;
/*      */   private final FontManager fontManager;
/*      */   private final SplashManager splashManager;
/*      */   private final GpuWarnlistManager gpuWarnlistManager;
/*  339 */   private final PeriodicNotificationManager regionalCompliancies = new PeriodicNotificationManager(REGIONAL_COMPLIANCIES, Minecraft::countryEqualsISO3);
/*      */   
/*      */   private final UserApiService userApiService;
/*      */   
/*      */   private final CompletableFuture<UserApiService.UserProperties> userPropertiesFuture;
/*      */   
/*      */   private final SkinManager skinManager;
/*      */   private final AtlasManager atlasManager;
/*      */   private final ModelManager modelManager;
/*      */   private final BlockRenderDispatcher blockRenderer;
/*      */   private final MapTextureManager mapTextureManager;
/*      */   private final WaypointStyleManager waypointStyles;
/*      */   private final ToastManager toastManager;
/*      */   private final Tutorial tutorial;
/*      */   private final PlayerSocialManager playerSocialManager;
/*      */   private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
/*      */   private final ClientTelemetryManager telemetryManager;
/*      */   private final ProfileKeyPairManager profileKeyPairManager;
/*      */   private final RealmsDataFetcher realmsDataFetcher;
/*      */   private final QuickPlayLog quickPlayLog;
/*      */   private final Services services;
/*      */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*      */   public net.minecraft.client.multiplayer.MultiPlayerGameMode gameMode;
/*      */   public ClientLevel level;
/*      */   public LocalPlayer player;
/*      */   private IntegratedServer singleplayerServer;
/*      */   private Connection pendingConnection;
/*      */   private boolean isLocalServer;
/*      */   private Entity cameraEntity;
/*      */   public Entity crosshairPickEntity;
/*      */   public HitResult hitResult;
/*      */   private int rightClickDelay;
/*      */   protected int missTime;
/*      */   private volatile boolean pause;
/*  373 */   private long lastNanoTime = Util.getNanos();
/*      */   
/*      */   private long lastTime;
/*      */   
/*      */   private int frames;
/*      */   
/*      */   public boolean noRender;
/*      */   
/*      */   public Screen screen;
/*      */   
/*      */   private Overlay overlay;
/*      */   
/*      */   private boolean clientLevelTeardownInProgress;
/*      */   
/*      */   private Thread gameThread;
/*      */   
/*      */   private volatile boolean running;
/*      */   
/*      */   private Supplier<CrashReport> delayedCrash;
/*      */   
/*      */   private static int fps;
/*      */   private long frameTimeNs;
/*      */   private final FramerateLimitTracker framerateLimitTracker;
/*      */   public boolean wireframe;
/*      */   public boolean smartCull = true;
/*      */   private boolean windowActive;
/*      */   private CompletableFuture<Void> pendingReload;
/*      */   private TutorialToast socialInteractionsToast;
/*      */   private int fpsPieRenderTicks;
/*      */   private final ContinuousProfiler fpsPieProfiler;
/*  403 */   private MetricsRecorder metricsRecorder = InactiveMetricsRecorder.INSTANCE;
/*  404 */   private final ResourceLoadStateTracker reloadStateTracker = new ResourceLoadStateTracker();
/*      */   
/*      */   private long savedCpuDuration;
/*      */   
/*      */   private double gpuUtilization;
/*      */   
/*      */   private TimerQuery.FrameProfile currentFrameProfile;
/*      */   
/*      */   private final GameNarrator narrator;
/*      */   
/*      */   private final ChatListener chatListener;
/*      */   
/*      */   private ReportingContext reportingContext;
/*      */   
/*      */   private final CommandHistory commandHistory;
/*      */   private final DirectoryValidator directoryValidator;
/*      */   private boolean gameLoadFinished;
/*      */   private final long clientStartTimeMs;
/*      */   private long clientTickCount;
/*      */   private final PacketProcessor packetProcessor;
/*  424 */   private final SimpleGizmoCollector perTickGizmos = new SimpleGizmoCollector();
/*  425 */   private List<SimpleGizmoCollector.GizmoInstance> drainedLatestTickGizmos = new ArrayList<>();
/*      */   
/*      */   public Minecraft(final GameConfig gameConfig) {
/*  428 */     super("Client");
/*  429 */     instance = this;
/*      */     
/*  431 */     this.clientStartTimeMs = System.currentTimeMillis();
/*      */     
/*  433 */     this.gameDirectory = gameConfig.location.gameDirectory;
/*  434 */     File assetsDirectory = gameConfig.location.assetDirectory;
/*  435 */     this.resourcePackDirectory = gameConfig.location.resourcePackDirectory.toPath();
/*  436 */     this.launchedVersion = gameConfig.game.launchVersion;
/*  437 */     this.versionType = gameConfig.game.versionType;
/*      */     
/*  439 */     Path gameDirPath = this.gameDirectory.toPath();
/*  440 */     this.directoryValidator = LevelStorageSource.parseValidator(gameDirPath.resolve("allowed_symlinks.txt"));
/*      */     
/*  442 */     ClientPackSource clientPackSource = new ClientPackSource(gameConfig.location.getExternalAssetSource(), this.directoryValidator);
/*  443 */     this.downloadedPackSource = new DownloadedPackSource(this, gameDirPath.resolve("downloads"), gameConfig.user);
/*  444 */     FolderRepositorySource folderRepositorySource = new FolderRepositorySource(this.resourcePackDirectory, PackType.CLIENT_RESOURCES, PackSource.DEFAULT, this.directoryValidator);
/*  445 */     this.resourcePackRepository = new PackRepository(new RepositorySource[] { (RepositorySource)clientPackSource, this.downloadedPackSource.createRepositorySource(), (RepositorySource)folderRepositorySource });
/*      */     
/*  447 */     this.vanillaPackResources = clientPackSource.getVanillaPack();
/*  448 */     this.proxy = gameConfig.user.proxy;
/*  449 */     this.offlineDeveloperMode = gameConfig.game.offlineDeveloperMode;
/*  450 */     YggdrasilAuthenticationService authenticationService = this.offlineDeveloperMode ? YggdrasilAuthenticationService.createOffline(this.proxy) : new YggdrasilAuthenticationService(this.proxy);
/*  451 */     this.services = Services.create(authenticationService, this.gameDirectory);
/*  452 */     this.user = gameConfig.user.user;
/*  453 */     this.profileFuture = this.offlineDeveloperMode ? CompletableFuture.<ProfileResult>completedFuture(null) : CompletableFuture.<ProfileResult>supplyAsync(() -> this.services.sessionService().fetchProfile(this.user.getProfileId(), true), (Executor)Util.nonCriticalIoPool());
/*  454 */     this.userApiService = createUserApiService(authenticationService, gameConfig);
/*  455 */     this.userPropertiesFuture = CompletableFuture.supplyAsync(() -> {
/*      */           try {
/*      */             return this.userApiService.fetchProperties();
/*  458 */           } catch (AuthenticationException e) {
/*      */             LOGGER.error("Failed to fetch user properties", (Throwable)e);
/*      */             return UserApiService.OFFLINE_PROPERTIES;
/*      */           } 
/*  462 */         }, (Executor)Util.nonCriticalIoPool());
/*      */     
/*  464 */     LOGGER.info("Setting user: {}", this.user.getName());
/*  465 */     LOGGER.debug("(Session ID is {})", this.user.getSessionId());
/*      */     
/*  467 */     this.demo = gameConfig.game.demo;
/*  468 */     this.allowsMultiplayer = !gameConfig.game.disableMultiplayer;
/*  469 */     this.allowsChat = !gameConfig.game.disableChat;
/*      */     
/*  471 */     this.singleplayerServer = null;
/*      */     
/*  473 */     KeybindResolver.setKeyResolver(KeyMapping::createNameSupplier);
/*  474 */     this.fixerUpper = DataFixers.getDataFixer();
/*  475 */     this.gameThread = Thread.currentThread();
/*      */     
/*  477 */     this.options = new Options(this, this.gameDirectory);
/*  478 */     this.debugEntries = new DebugScreenEntryList(this.gameDirectory);
/*  479 */     this.toastManager = new ToastManager(this, this.options);
/*      */     
/*  481 */     boolean lastStartWasClean = this.options.startedCleanly;
/*  482 */     this.options.startedCleanly = false;
/*  483 */     this.options.save();
/*      */     
/*  485 */     this.running = true;
/*  486 */     this.tutorial = new Tutorial(this, this.options);
/*  487 */     this.hotbarManager = new HotbarManager(gameDirPath, this.fixerUpper);
/*      */     
/*  489 */     LOGGER.info("Backend library: {}", RenderSystem.getBackendDescription());
/*      */     
/*  491 */     DisplayData displayData = gameConfig.display;
/*  492 */     if (this.options.overrideHeight > 0 && this.options.overrideWidth > 0) {
/*  493 */       displayData = gameConfig.display.withSize(this.options.overrideWidth, this.options.overrideHeight);
/*      */     }
/*  495 */     if (!lastStartWasClean) {
/*  496 */       displayData = displayData.withFullscreen(false);
/*  497 */       this.options.fullscreenVideoModeString = null;
/*  498 */       LOGGER.warn("Detected unexpected shutdown during last game startup: resetting fullscreen mode");
/*      */     } 
/*      */     
/*  501 */     Util.timeSource = RenderSystem.initBackendSystem();
/*      */     
/*  503 */     this.virtualScreen = new VirtualScreen(this);
/*  504 */     this.window = this.virtualScreen.newWindow(displayData, this.options.fullscreenVideoModeString, createTitle());
/*  505 */     setWindowActive(true);
/*      */     
/*  507 */     this.window.setWindowCloseCallback(new Runnable()
/*      */         {
/*      */           private boolean threadStarted;
/*      */           
/*      */           public void run() {
/*  512 */             if (!this.threadStarted) {
/*  513 */               this.threadStarted = true;
/*  514 */               ClientShutdownWatchdog.startShutdownWatchdog(gameConfig.location.gameDirectory, Minecraft.this.gameThread.threadId());
/*      */             } 
/*      */           }
/*      */         });
/*      */     
/*  519 */     GameLoadTimesEvent.INSTANCE.endStep(TelemetryProperty.LOAD_TIME_PRE_WINDOW_MS);
/*      */     
/*      */     try {
/*  522 */       this.window.setIcon((PackResources)this.vanillaPackResources, SharedConstants.getCurrentVersion().stable() ? IconSet.RELEASE : IconSet.SNAPSHOT);
/*  523 */     } catch (IOException e) {
/*  524 */       LOGGER.error("Couldn't set icon", e);
/*      */     } 
/*      */     
/*  527 */     this.mouseHandler = new MouseHandler(this);
/*  528 */     this.mouseHandler.setup(this.window);
/*      */     
/*  530 */     this.keyboardHandler = new KeyboardHandler(this);
/*  531 */     this.keyboardHandler.setup(this.window);
/*      */     
/*  533 */     RenderSystem.initRenderer(this.window.handle(), this.options.glDebugVerbosity, SharedConstants.DEBUG_SYNCHRONOUS_GL_LOGS, (id, type) -> getShaderManager().getShader(id, type), gameConfig.game.renderDebugLabels);
/*      */ 
/*      */ 
/*      */     
/*  537 */     this.options.applyGraphicsPreset(this.options.graphicsPreset().get());
/*      */     
/*  539 */     LOGGER.info("Using optional rendering extensions: {}", String.join(", ", RenderSystem.getDevice().getEnabledExtensions()));
/*      */     
/*  541 */     this.mainRenderTarget = (RenderTarget)new MainTarget(this.window.getWidth(), this.window.getHeight());
/*      */     
/*  543 */     this.resourceManager = new ReloadableResourceManager(PackType.CLIENT_RESOURCES);
/*      */     
/*  545 */     this.resourcePackRepository.reload();
/*  546 */     this.options.loadSelectedResourcePacks(this.resourcePackRepository);
/*      */     
/*  548 */     this.languageManager = new LanguageManager(this.options.languageCode, languageData -> {
/*      */           if (this.player != null) {
/*      */             this.player.connection.updateSearchTrees();
/*      */           }
/*      */         });
/*  553 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.languageManager);
/*      */     
/*  555 */     this.textureManager = new TextureManager((ResourceManager)this.resourceManager);
/*  556 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.textureManager);
/*      */     
/*  558 */     this.shaderManager = new ShaderManager(this.textureManager, this::triggerResourcePackRecovery);
/*  559 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.shaderManager);
/*      */     
/*  561 */     SkinTextureDownloader skinTextureDownloader = new SkinTextureDownloader(this.proxy, this.textureManager, (Executor)this);
/*  562 */     this.skinManager = new SkinManager(assetsDirectory.toPath().resolve("skins"), this.services, skinTextureDownloader, (Executor)this);
/*      */     
/*  564 */     this.levelSource = new LevelStorageSource(gameDirPath.resolve("saves"), gameDirPath.resolve("backups"), this.directoryValidator, this.fixerUpper);
/*  565 */     this.commandHistory = new CommandHistory(gameDirPath);
/*      */     
/*  567 */     this.musicManager = new MusicManager(this);
/*  568 */     this.soundManager = new SoundManager(this.options);
/*  569 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.soundManager);
/*      */     
/*  571 */     this.splashManager = new SplashManager(this.user);
/*  572 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.splashManager);
/*      */     
/*  574 */     this.atlasManager = new AtlasManager(this.textureManager, (Integer)this.options.mipmapLevels().get());
/*  575 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.atlasManager);
/*      */     
/*  577 */     LocalPlayerResolver localPlayerResolver = new LocalPlayerResolver(this, this.services.profileResolver());
/*  578 */     this.playerSkinRenderCache = new PlayerSkinRenderCache(this.textureManager, this.skinManager, (ProfileResolver)localPlayerResolver);
/*  579 */     ClientMannequin.registerOverrides(this.playerSkinRenderCache);
/*      */     
/*  581 */     this.fontManager = new FontManager(this.textureManager, this.atlasManager, this.playerSkinRenderCache);
/*  582 */     this.font = this.fontManager.createFont();
/*  583 */     this.fontFilterFishy = this.fontManager.createFontFilterFishy();
/*  584 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.fontManager);
/*  585 */     updateFontOptions();
/*      */     
/*  587 */     this.resourceManager.registerReloadListener((PreparableReloadListener)new GrassColorReloadListener());
/*  588 */     this.resourceManager.registerReloadListener((PreparableReloadListener)new FoliageColorReloadListener());
/*  589 */     this.resourceManager.registerReloadListener((PreparableReloadListener)new DryFoliageColorReloadListener());
/*      */     
/*  591 */     this.window.setErrorSection("Startup");
/*      */     
/*  593 */     RenderSystem.setupDefaultState();
/*      */     
/*  595 */     this.window.setErrorSection("Post startup");
/*      */     
/*  597 */     this.blockColors = BlockColors.createDefault();
/*      */     
/*  599 */     this.modelManager = new ModelManager(this.blockColors, this.atlasManager, this.playerSkinRenderCache);
/*  600 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.modelManager);
/*      */     
/*  602 */     EquipmentAssetManager equipmentAssets = new EquipmentAssetManager();
/*  603 */     this.resourceManager.registerReloadListener((PreparableReloadListener)equipmentAssets);
/*      */     
/*  605 */     this.itemModelResolver = new ItemModelResolver(this.modelManager);
/*  606 */     this.itemRenderer = new ItemRenderer();
/*      */     
/*  608 */     this.mapTextureManager = new MapTextureManager(this.textureManager);
/*      */     
/*  610 */     this.mapRenderer = new MapRenderer(this.atlasManager, this.mapTextureManager);
/*      */     
/*      */     try {
/*  613 */       int maxSectionBuilders = Runtime.getRuntime().availableProcessors();
/*  614 */       Tesselator.init();
/*  615 */       this.renderBuffers = new RenderBuffers(maxSectionBuilders);
/*  616 */     } catch (OutOfMemoryError e) {
/*  617 */       TinyFileDialogs.tinyfd_messageBox("Minecraft", "Oh no! The game was unable to allocate memory off-heap while trying to start. You may try to free some memory by closing other applications on your computer, check that your system meets the minimum requirements, and try again. If the problem persists, please visit: " + String.valueOf(CommonLinks.GENERAL_HELP), "ok", "error", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  624 */       throw new SilentInitException("Unable to allocate render buffers", e);
/*      */     } 
/*      */     
/*  627 */     this.playerSocialManager = new PlayerSocialManager(this, this.userApiService);
/*      */     
/*  629 */     this.blockRenderer = new BlockRenderDispatcher(this.modelManager.getBlockModelShaper(), (MaterialSet)this.atlasManager, this.blockColors);
/*  630 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.blockRenderer);
/*      */     
/*  632 */     this.entityRenderDispatcher = new EntityRenderDispatcher(this, this.textureManager, this.itemModelResolver, this.mapRenderer, this.blockRenderer, this.atlasManager, this.font, this.options, this.modelManager.entityModels(), equipmentAssets, this.playerSkinRenderCache);
/*  633 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.entityRenderDispatcher);
/*      */     
/*  635 */     this.blockEntityRenderDispatcher = new BlockEntityRenderDispatcher(this.font, this.modelManager.entityModels(), this.blockRenderer, this.itemModelResolver, this.itemRenderer, this.entityRenderDispatcher, (MaterialSet)this.atlasManager, this.playerSkinRenderCache);
/*  636 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.blockEntityRenderDispatcher);
/*      */     
/*  638 */     this.particleResources = new ParticleResources();
/*  639 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.particleResources);
/*  640 */     this.particleEngine = new ParticleEngine(this.level, this.particleResources);
/*  641 */     Objects.requireNonNull(this.particleEngine); this.particleResources.onReload(this.particleEngine::clearParticles);
/*      */     
/*  643 */     this.waypointStyles = new WaypointStyleManager();
/*  644 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.waypointStyles);
/*      */     
/*  646 */     this.gameRenderer = new GameRenderer(this, this.entityRenderDispatcher.getItemInHandRenderer(), this.renderBuffers, this.blockRenderer);
/*      */     
/*  648 */     this.levelRenderer = new LevelRenderer(this, this.entityRenderDispatcher, this.blockEntityRenderDispatcher, this.renderBuffers, this.gameRenderer.getLevelRenderState(), this.gameRenderer.getFeatureRenderDispatcher());
/*  649 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.levelRenderer);
/*  650 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.levelRenderer.getCloudRenderer());
/*      */     
/*  652 */     this.gpuWarnlistManager = new GpuWarnlistManager();
/*  653 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.gpuWarnlistManager);
/*      */     
/*  655 */     this.resourceManager.registerReloadListener((PreparableReloadListener)this.regionalCompliancies);
/*      */     
/*  657 */     this.gui = new Gui(this);
/*      */     
/*  659 */     RealmsClient realmsClient = RealmsClient.getOrCreate(this);
/*      */     
/*  661 */     this.realmsDataFetcher = new RealmsDataFetcher(realmsClient);
/*      */     
/*  663 */     RenderSystem.setErrorCallback(this::onFullscreenError);
/*      */     
/*  665 */     if (this.mainRenderTarget.width != this.window.getWidth() || this.mainRenderTarget.height != this.window.getHeight()) {
/*      */       
/*  667 */       StringBuilder message = new StringBuilder("Recovering from unsupported resolution (" + this.window.getWidth() + "x" + this.window.getHeight() + ").\nPlease make sure you have up-to-date drivers (see aka.ms/mcdriver for instructions).");
/*      */       
/*      */       try {
/*  670 */         GpuDevice device = RenderSystem.getDevice();
/*  671 */         List<String> messages = device.getLastDebugMessages();
/*  672 */         if (!messages.isEmpty()) {
/*  673 */           message.append("\n\nReported GL debug messages:\n").append(String.join("\n", (Iterable)messages));
/*      */         }
/*  675 */       } catch (Throwable throwable) {}
/*      */ 
/*      */       
/*  678 */       this.window.setWindowed(this.mainRenderTarget.width, this.mainRenderTarget.height);
/*  679 */       TinyFileDialogs.tinyfd_messageBox("Minecraft", message.toString(), "ok", "error", false);
/*  680 */     } else if ((Boolean)this.options.fullscreen().get() && !this.window.isFullscreen()) {
/*  681 */       if (lastStartWasClean) {
/*  682 */         this.window.toggleFullScreen();
/*  683 */         this.options.fullscreen().set(this.window.isFullscreen());
/*      */       } else {
/*      */         
/*  686 */         this.options.fullscreen().set(false);
/*      */       } 
/*      */     } 
/*      */     
/*  690 */     this.window.updateVsync((Boolean)this.options.enableVsync().get());
/*  691 */     this.window.updateRawMouseInput((Boolean)this.options.rawMouseInput().get());
/*  692 */     this.window.setAllowCursorChanges((Boolean)this.options.allowCursorChanges().get());
/*  693 */     this.window.setDefaultErrorCallback();
/*      */     
/*  695 */     resizeDisplay();
/*      */     
/*  697 */     this.gameRenderer.preloadUiShader(this.vanillaPackResources.asProvider());
/*      */     
/*  699 */     this.telemetryManager = new ClientTelemetryManager(this, this.userApiService, this.user);
/*  700 */     this.profileKeyPairManager = this.offlineDeveloperMode ? ProfileKeyPairManager.EMPTY_KEY_MANAGER : ProfileKeyPairManager.create(this.userApiService, this.user, gameDirPath);
/*      */     
/*  702 */     this.narrator = new GameNarrator(this);
/*  703 */     this.narrator.checkStatus((this.options.narrator().get() != NarratorStatus.OFF));
/*      */     
/*  705 */     this.chatListener = new ChatListener(this);
/*  706 */     this.chatListener.setMessageDelay((Double)this.options.chatDelay().get());
/*      */     
/*  708 */     this.reportingContext = ReportingContext.create(ReportEnvironment.local(), this.userApiService);
/*      */     
/*  710 */     TitleScreen.registerTextures(this.textureManager);
/*  711 */     LoadingOverlay.registerTextures(this.textureManager);
/*  712 */     this.gameRenderer.getPanorama().registerTextures(this.textureManager);
/*      */ 
/*      */ 
/*      */     
/*  716 */     setScreen((Screen)new GenericMessageScreen((Component)Component.translatable("gui.loadingMinecraft")));
/*      */     
/*  718 */     List<PackResources> packs = this.resourcePackRepository.openAllSelected();
/*  719 */     this.reloadStateTracker.startReload(ResourceLoadStateTracker.ReloadReason.INITIAL, packs);
/*  720 */     ReloadInstance reloadInstance = this.resourceManager.createReload(Util.backgroundExecutor().forName("resourceLoad"), (Executor)this, RESOURCE_RELOAD_INITIAL_TASK, packs);
/*  721 */     GameLoadTimesEvent.INSTANCE.beginStep(TelemetryProperty.LOAD_TIME_LOADING_OVERLAY_MS);
/*      */     
/*  723 */     GameLoadCookie loadCookie = new GameLoadCookie(realmsClient, gameConfig.quickPlay);
/*  724 */     setOverlay((Overlay)new LoadingOverlay(this, reloadInstance, maybeT -> Util.ifElse(loadCookie, (), ()), false));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  734 */     this.quickPlayLog = QuickPlayLog.of(gameConfig.quickPlay.logPath());
/*  735 */     this.framerateLimitTracker = new FramerateLimitTracker(this.options, this);
/*      */     
/*  737 */     Objects.requireNonNull(this.framerateLimitTracker); this.fpsPieProfiler = new ContinuousProfiler((LongSupplier)Util.timeSource, () -> this.fpsPieRenderTicks, this.framerateLimitTracker::isHeavilyThrottled);
/*      */     
/*  739 */     if (TracyClient.isAvailable() && gameConfig.game.captureTracyImages) {
/*  740 */       this.tracyFrameCapture = new TracyFrameCapture();
/*      */     } else {
/*  742 */       this.tracyFrameCapture = null;
/*      */     } 
/*  744 */     this.packetProcessor = new PacketProcessor(this.gameThread);
/*      */   }
/*      */   
/*      */   public boolean hasShiftDown() {
/*  748 */     Window window = getWindow();
/*  749 */     return (InputConstants.isKeyDown(window, 340) || InputConstants.isKeyDown(window, 344));
/*      */   }
/*      */   
/*      */   public boolean hasControlDown() {
/*  753 */     Window window = getWindow();
/*  754 */     return (InputConstants.isKeyDown(window, 341) || InputConstants.isKeyDown(window, 345));
/*      */   }
/*      */   
/*      */   public boolean hasAltDown() {
/*  758 */     Window window = getWindow();
/*  759 */     return (InputConstants.isKeyDown(window, 342) || InputConstants.isKeyDown(window, 346));
/*      */   }
/*      */   
/*      */   private void onResourceLoadFinished(GameLoadCookie loadCookie) {
/*  763 */     if (!this.gameLoadFinished) {
/*  764 */       this.gameLoadFinished = true;
/*  765 */       onGameLoadFinished(loadCookie);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void onGameLoadFinished(GameLoadCookie cookie) {
/*  770 */     Runnable showScreen = buildInitialScreens(cookie);
/*      */     
/*  772 */     GameLoadTimesEvent.INSTANCE.endStep(TelemetryProperty.LOAD_TIME_LOADING_OVERLAY_MS);
/*  773 */     GameLoadTimesEvent.INSTANCE.endStep(TelemetryProperty.LOAD_TIME_TOTAL_TIME_MS);
/*  774 */     GameLoadTimesEvent.INSTANCE.send(this.telemetryManager.getOutsideSessionSender());
/*      */     
/*  776 */     showScreen.run();
/*      */     
/*  778 */     this.options.startedCleanly = true;
/*  779 */     this.options.save();
/*      */   }
/*      */   
/*      */   public boolean isGameLoadFinished() {
/*  783 */     return this.gameLoadFinished;
/*      */   }
/*      */   
/*      */   private Runnable buildInitialScreens(GameLoadCookie cookie) {
/*  787 */     List<Function<Runnable, Screen>> screens = new ArrayList<>();
/*  788 */     boolean onboardingScreenAdded = addInitialScreens(screens);
/*      */     
/*      */     Runnable nextStep = () -> {
/*      */         if (cookie != null && cookie.quickPlayData.isEnabled()) {
/*      */           QuickPlay.connect(this, cookie.quickPlayData.variant(), cookie.realmsClient());
/*      */         } else {
/*      */           setScreen((Screen)new TitleScreen(true, new LogoRenderer(onboardingScreenAdded)));
/*      */         } 
/*      */       };
/*      */     
/*  798 */     for (Function<Runnable, Screen> function : (Iterable<Function<Runnable, Screen>>)Lists.reverse(screens)) {
/*  799 */       Screen screen = function.apply(nextStep);
/*  800 */       nextStep = (() -> setScreen(screen));
/*      */     } 
/*      */     
/*  803 */     return nextStep;
/*      */   }
/*      */   
/*      */   private boolean addInitialScreens(List<Function<Runnable, Screen>> screens) {
/*      */     boolean onboardingScreenAdded = false;
/*  808 */     if (this.options.onboardAccessibility || SharedConstants.DEBUG_FORCE_ONBOARDING_SCREEN) {
/*  809 */       screens.add(next -> new AccessibilityOnboardingScreen(this.options, next));
/*  810 */       onboardingScreenAdded = true;
/*      */     } 
/*      */     
/*  813 */     BanDetails multiplayerBan = multiplayerBan();
/*  814 */     if (multiplayerBan != null) {
/*  815 */       screens.add(next -> BanNoticeScreens.create((), multiplayerBan));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  823 */     ProfileResult profileResult = this.profileFuture.join();
/*  824 */     if (profileResult != null) {
/*  825 */       GameProfile profile = profileResult.profile();
/*  826 */       Set<ProfileActionType> actions = profileResult.actions();
/*  827 */       if (actions.contains(ProfileActionType.FORCED_NAME_CHANGE)) {
/*  828 */         screens.add(onClose -> BanNoticeScreens.createNameBan(profile.name(), onClose));
/*      */       }
/*  830 */       if (actions.contains(ProfileActionType.USING_BANNED_SKIN)) {
/*  831 */         screens.add(BanNoticeScreens::createSkinBan);
/*      */       }
/*      */     } 
/*  834 */     return onboardingScreenAdded;
/*      */   }
/*      */   
/*      */   private static boolean countryEqualsISO3(Object iso3Locale) {
/*      */     try {
/*  839 */       return Locale.getDefault().getISO3Country().equals(iso3Locale);
/*  840 */     } catch (MissingResourceException e) {
/*  841 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateTitle() {
/*  846 */     this.window.setTitle(createTitle());
/*      */   }
/*      */   
/*      */   private String createTitle() {
/*  850 */     StringBuilder builder = new StringBuilder("Minecraft");
/*  851 */     if (checkModStatus().shouldReportAsModified()) {
/*  852 */       builder.append("*");
/*      */     }
/*  854 */     builder.append(" ");
/*  855 */     builder.append(SharedConstants.getCurrentVersion().name());
/*      */     
/*  857 */     ClientPacketListener connection = getConnection();
/*  858 */     if (connection != null && connection.getConnection().isConnected()) {
/*  859 */       builder.append(" - ");
/*  860 */       ServerData server = getCurrentServer();
/*  861 */       if (this.singleplayerServer != null && !this.singleplayerServer.isPublished()) {
/*  862 */         builder.append(I18n.get("title.singleplayer", new Object[0]));
/*  863 */       } else if (server != null && server.isRealm()) {
/*  864 */         builder.append(I18n.get("title.multiplayer.realms", new Object[0]));
/*  865 */       } else if (this.singleplayerServer != null || (server != null && server.isLan())) {
/*  866 */         builder.append(I18n.get("title.multiplayer.lan", new Object[0]));
/*      */       } else {
/*  868 */         builder.append(I18n.get("title.multiplayer.other", new Object[0]));
/*      */       } 
/*      */     } 
/*      */     
/*  872 */     return builder.toString();
/*      */   }
/*      */   
/*      */   private UserApiService createUserApiService(YggdrasilAuthenticationService authService, GameConfig config) {
/*  876 */     if (config.game.offlineDeveloperMode) {
/*  877 */       return UserApiService.OFFLINE;
/*      */     }
/*  879 */     return authService.createUserApiService(config.user.user.getAccessToken());
/*      */   }
/*      */   
/*      */   public boolean isOfflineDeveloperMode() {
/*  883 */     return this.offlineDeveloperMode;
/*      */   }
/*      */   
/*      */   public static ModCheck checkModStatus() {
/*  887 */     return ModCheck.identify("vanilla", ClientBrandRetriever::getClientModName, "Client", Minecraft.class);
/*      */   }
/*      */   
/*      */   private void rollbackResourcePacks(Throwable t, GameLoadCookie loadCookie) {
/*  891 */     if (this.resourcePackRepository.getSelectedIds().size() > 1) {
/*  892 */       clearResourcePacksOnError(t, null, loadCookie);
/*      */     } else {
/*  894 */       Util.throwAsRuntime(t);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearResourcePacksOnError(Throwable t, Component message, GameLoadCookie loadCookie) {
/*  899 */     LOGGER.info("Caught error loading resourcepacks, removing all selected resourcepacks", t);
/*  900 */     this.reloadStateTracker.startRecovery(t);
/*  901 */     this.downloadedPackSource.onRecovery();
/*      */     
/*  903 */     this.resourcePackRepository.setSelected(java.util.Collections.emptyList());
/*      */     
/*  905 */     this.options.resourcePacks.clear();
/*  906 */     this.options.incompatibleResourcePacks.clear();
/*  907 */     this.options.save();
/*      */     
/*  909 */     reloadResourcePacks(true, loadCookie)
/*  910 */       .thenRunAsync(() -> addResourcePackLoadFailToast(message), (Executor)this);
/*      */   }
/*      */   
/*      */   private void abortResourcePackRecovery() {
/*  914 */     setOverlay(null);
/*  915 */     if (this.level != null) {
/*  916 */       this.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
/*  917 */       disconnectWithProgressScreen();
/*      */     } 
/*  919 */     setScreen((Screen)new TitleScreen());
/*  920 */     addResourcePackLoadFailToast(null);
/*      */   }
/*      */   
/*      */   private void addResourcePackLoadFailToast(Component message) {
/*  924 */     ToastManager toastManager = getToastManager();
/*  925 */     SystemToast.addOrUpdate(toastManager, SystemToast.SystemToastId.PACK_LOAD_FAILURE, (Component)Component.translatable("resourcePack.load_fail"), message);
/*      */   }
/*      */   
/*      */   public void triggerResourcePackRecovery(Exception exception) {
/*  929 */     if (!this.resourcePackRepository.isAbleToClearAnyPack()) {
/*      */ 
/*      */ 
/*      */       
/*  933 */       if (this.resourcePackRepository.getSelectedIds().size() <= 1) {
/*      */         
/*  935 */         LOGGER.error(LogUtils.FATAL_MARKER, exception.getMessage(), exception);
/*  936 */         emergencySaveAndCrash(new CrashReport(exception.getMessage(), exception));
/*      */       } else {
/*  938 */         schedule(this::abortResourcePackRecovery);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  943 */     clearResourcePacksOnError(exception, (Component)Component.translatable("resourcePack.runtime_failure"), null);
/*      */   }
/*      */   
/*      */   public void run() {
/*  947 */     this.gameThread = Thread.currentThread();
/*      */     
/*  949 */     if (Runtime.getRuntime().availableProcessors() > 4) {
/*  950 */       this.gameThread.setPriority(10);
/*      */     }
/*      */     
/*  953 */     DiscontinuousFrame tickFrame = TracyClient.createDiscontinuousFrame("Client Tick");
/*      */     try {
/*      */       boolean oomRecovery = false;
/*  956 */       while (this.running) {
/*  957 */         handleDelayedCrash();
/*      */         
/*      */         try {
/*  960 */           SingleTickProfiler tickProfiler = SingleTickProfiler.createTickProfiler("Renderer");
/*  961 */           boolean shouldCollectFrameProfile = getDebugOverlay().showProfilerChart();
/*  962 */           Profiler.Scope ignored = Profiler.use(constructProfiler(shouldCollectFrameProfile, tickProfiler)); 
/*  963 */           try { this.metricsRecorder.startTick();
/*  964 */             tickFrame.start();
/*      */             
/*  966 */             runTick(!oomRecovery);
/*      */             
/*  968 */             tickFrame.end();
/*  969 */             this.metricsRecorder.endTick();
/*  970 */             if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*  971 */               try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  finishProfilers(shouldCollectFrameProfile, tickProfiler);
/*  972 */         } catch (OutOfMemoryError e) {
/*  973 */           if (oomRecovery)
/*      */           {
/*  975 */             throw e;
/*      */           }
/*  977 */           emergencySave();
/*  978 */           setScreen((Screen)new OutOfMemoryScreen());
/*  979 */           System.gc();
/*  980 */           LOGGER.error(LogUtils.FATAL_MARKER, "Out of memory", e);
/*  981 */           oomRecovery = true;
/*      */         }
/*      */       
/*      */       } 
/*  985 */     } catch (ReportedException e) {
/*  986 */       LOGGER.error(LogUtils.FATAL_MARKER, "Reported exception thrown!", (Throwable)e);
/*  987 */       emergencySaveAndCrash(e.getReport());
/*  988 */     } catch (Throwable t) {
/*  989 */       LOGGER.error(LogUtils.FATAL_MARKER, "Unreported exception thrown!", t);
/*  990 */       emergencySaveAndCrash(new CrashReport("Unexpected error", t));
/*      */     } 
/*      */   }
/*      */   
/*      */   void updateFontOptions() {
/*  995 */     this.fontManager.updateOptions(this.options);
/*      */   }
/*      */   
/*      */   private void onFullscreenError(int error, long description) {
/*  999 */     this.options.enableVsync().set(false);
/* 1000 */     this.options.save();
/*      */   }
/*      */   
/*      */   public RenderTarget getMainRenderTarget() {
/* 1004 */     return this.mainRenderTarget;
/*      */   }
/*      */   
/*      */   public String getLaunchedVersion() {
/* 1008 */     return this.launchedVersion;
/*      */   }
/*      */   
/*      */   public String getVersionType() {
/* 1012 */     return this.versionType;
/*      */   }
/*      */   
/*      */   public void delayCrash(CrashReport crash) {
/* 1016 */     this.delayedCrash = (() -> fillReport(crash));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void delayCrashRaw(CrashReport crash) {
/* 1023 */     this.delayedCrash = (() -> crash);
/*      */   }
/*      */   
/*      */   private void handleDelayedCrash() {
/* 1027 */     if (this.delayedCrash != null)
/*      */     {
/* 1029 */       crash(this, this.gameDirectory, this.delayedCrash.get());
/*      */     }
/*      */   }
/*      */   
/*      */   public void emergencySaveAndCrash(CrashReport partialReport) {
/* 1034 */     MemoryReserve.release();
/* 1035 */     CrashReport finalReport = fillReport(partialReport);
/* 1036 */     emergencySave();
/* 1037 */     crash(this, this.gameDirectory, finalReport);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int saveReport(File gameDirectory, CrashReport crash) {
/* 1042 */     Path crashDir = gameDirectory.toPath().resolve("crash-reports");
/* 1043 */     Path crashFile = crashDir.resolve("crash-" + Util.getFilenameFormattedDateTime() + "-client.txt");
/*      */     
/* 1045 */     Bootstrap.realStdoutPrintln(crash.getFriendlyReport(ReportType.CRASH));
/*      */ 
/*      */     
/* 1048 */     if (crash.getSaveFile() != null) {
/* 1049 */       Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + String.valueOf(crash.getSaveFile().toAbsolutePath()));
/* 1050 */       return -1;
/* 1051 */     }  if (crash.saveToFile(crashFile, ReportType.CRASH)) {
/* 1052 */       Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + String.valueOf(crashFile.toAbsolutePath()));
/* 1053 */       return -1;
/*      */     } 
/* 1055 */     Bootstrap.realStdoutPrintln("#@?@# Game crashed! Crash report could not be saved. #@?@#");
/* 1056 */     return -2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void crash(Minecraft minecraft, File gameDirectory, CrashReport crash) {
/* 1061 */     int exitCode = saveReport(gameDirectory, crash);
/*      */     
/* 1063 */     if (minecraft != null)
/*      */     {
/* 1065 */       minecraft.soundManager.emergencyShutdown();
/*      */     }
/*      */     
/* 1068 */     System.exit(exitCode);
/*      */   }
/*      */   
/*      */   public boolean isEnforceUnicode() {
/* 1072 */     return (Boolean)this.options.forceUnicodeFont().get();
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> reloadResourcePacks() {
/* 1076 */     return reloadResourcePacks(false, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CompletableFuture<Void> reloadResourcePacks(boolean isRecovery, GameLoadCookie loadCookie) {
/* 1082 */     if (this.pendingReload != null) {
/* 1083 */       return this.pendingReload;
/*      */     }
/* 1085 */     CompletableFuture<Void> result = new CompletableFuture<>();
/* 1086 */     if (!isRecovery && this.overlay instanceof LoadingOverlay) {
/* 1087 */       this.pendingReload = result;
/* 1088 */       return result;
/*      */     } 
/*      */ 
/*      */     
/* 1092 */     this.resourcePackRepository.reload();
/* 1093 */     List<PackResources> packs = this.resourcePackRepository.openAllSelected();
/* 1094 */     if (!isRecovery) {
/* 1095 */       this.reloadStateTracker.startReload(ResourceLoadStateTracker.ReloadReason.MANUAL, packs);
/*      */     }
/*      */     
/* 1098 */     setOverlay((Overlay)new LoadingOverlay(this, this.resourceManager.createReload(Util.backgroundExecutor().forName("resourceLoad"), (Executor)this, RESOURCE_RELOAD_INITIAL_TASK, packs), maybeT -> Util.ifElse(result, (), ()), !isRecovery));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1116 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void selfTest() {
/*      */     boolean error = false;
/* 1123 */     BlockModelShaper blockModelShaper = getBlockRenderer().getBlockModelShaper();
/* 1124 */     BlockStateModel missingModel = blockModelShaper.getModelManager().getMissingBlockStateModel();
/*      */     
/* 1126 */     for (Block block : (Iterable<Block>)BuiltInRegistries.BLOCK) {
/* 1127 */       for (UnmodifiableIterator<BlockState> unmodifiableIterator = block.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { BlockState state = unmodifiableIterator.next();
/* 1128 */         if (state.getRenderShape() == net.minecraft.world.level.block.RenderShape.MODEL) {
/* 1129 */           BlockStateModel model = blockModelShaper.getBlockModel(state);
/* 1130 */           if (model == missingModel) {
/* 1131 */             LOGGER.debug("Missing model for: {}", state);
/* 1132 */             error = true;
/*      */           } 
/*      */         }  }
/*      */     
/*      */     } 
/*      */ 
/*      */     
/* 1139 */     TextureAtlasSprite missingIcon = missingModel.particleIcon();
/* 1140 */     for (Block block : (Iterable<Block>)BuiltInRegistries.BLOCK) {
/* 1141 */       for (UnmodifiableIterator<BlockState> unmodifiableIterator = block.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) { BlockState state = unmodifiableIterator.next();
/* 1142 */         TextureAtlasSprite particleIcon = blockModelShaper.getParticleIcon(state);
/* 1143 */         if (!state.isAir() && particleIcon == missingIcon)
/*      */         {
/* 1145 */           LOGGER.debug("Missing particle icon for: {}", state);
/*      */         } }
/*      */     
/*      */     } 
/*      */ 
/*      */     
/* 1151 */     BuiltInRegistries.ITEM.listElements().forEach(holder -> {
/*      */           Item item = (Item)holder.value();
/*      */ 
/*      */           
/*      */           String descriptionId = item.getDescriptionId(), name = Component.translatable(descriptionId).getString();
/*      */           
/*      */           if (name.toLowerCase(Locale.ROOT).equals(item.getDescriptionId())) {
/*      */             LOGGER.debug("Missing translation for: {} {} {}", new Object[] { holder.key().identifier(), descriptionId, item });
/*      */           }
/*      */         });
/*      */     
/* 1162 */     error |= net.minecraft.client.gui.screens.MenuScreens.selfTest();
/*      */     
/* 1164 */     error |= EntityRenderers.validateRegistrations();
/*      */     
/* 1166 */     if (error) {
/* 1167 */       throw new IllegalStateException("Your game data is foobar, fix the errors above!");
/*      */     }
/*      */   }
/*      */   
/*      */   public LevelStorageSource getLevelSource() {
/* 1172 */     return this.levelSource;
/*      */   }
/*      */   
/*      */   public void openChatScreen(ChatComponent.ChatMethod chatMethod) {
/* 1176 */     ChatStatus chatStatus = getChatStatus();
/* 1177 */     if (!chatStatus.isChatAllowed(isLocalServer())) {
/* 1178 */       if (this.gui.isShowingChatDisabledByPlayer()) {
/* 1179 */         this.gui.setChatDisabledByPlayerShown(false);
/* 1180 */         setScreen((Screen)new ConfirmLinkScreen(result -> { if (result) Util.getPlatform().openUri(CommonLinks.ACCOUNT_SETTINGS);  setScreen(null); }, ChatStatus.INFO_DISABLED_BY_PROFILE, CommonLinks.ACCOUNT_SETTINGS, true));
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1187 */         Component message = chatStatus.getMessage();
/* 1188 */         this.gui.setOverlayMessage(message, false);
/* 1189 */         this.narrator.saySystemNow(message);
/* 1190 */         this.gui.setChatDisabledByPlayerShown((chatStatus == ChatStatus.DISABLED_BY_PROFILE));
/*      */       } 
/*      */     } else {
/* 1193 */       this.gui.getChat().openScreen(chatMethod, ChatScreen::new);
/*      */     } 
/*      */   }
/*      */   public void setScreen(Screen screen) {
/*      */     ChatScreen chatScreen;
/* 1198 */     if (SharedConstants.IS_RUNNING_IN_IDE && Thread.currentThread() != this.gameThread) {
/* 1199 */       LOGGER.error("setScreen called from non-game thread");
/*      */     }
/*      */     
/* 1202 */     if (this.screen != null) {
/* 1203 */       this.screen.removed();
/*      */     } else {
/* 1205 */       setLastInputType(InputType.NONE);
/*      */     } 
/*      */     
/* 1208 */     if (screen == null) {
/* 1209 */       if (this.clientLevelTeardownInProgress) {
/* 1210 */         throw new IllegalStateException("Trying to return to in-game GUI during disconnection");
/*      */       }
/* 1212 */       if (this.level == null) {
/* 1213 */         TitleScreen titleScreen = new TitleScreen();
/* 1214 */       } else if (this.player.isDeadOrDying()) {
/* 1215 */         if (this.player.shouldShowDeathScreen()) {
/* 1216 */           DeathScreen deathScreen = new DeathScreen(null, this.level.getLevelData().isHardcore(), this.player);
/*      */         } else {
/* 1218 */           this.player.respawn();
/*      */         } 
/*      */       } else {
/* 1221 */         chatScreen = this.gui.getChat().restoreChatScreen();
/*      */       } 
/*      */     } 
/*      */     
/* 1225 */     this.screen = (Screen)chatScreen;
/* 1226 */     if (this.screen != null) {
/* 1227 */       this.screen.added();
/*      */     }
/*      */     
/* 1230 */     if (chatScreen != null) {
/* 1231 */       this.mouseHandler.releaseMouse();
/* 1232 */       KeyMapping.releaseAll();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1237 */       chatScreen.init(this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight());
/* 1238 */       this.noRender = false;
/*      */     } else {
/* 1240 */       if (this.level != null) {
/* 1241 */         KeyMapping.restoreToggleStatesOnScreenClosed();
/*      */       }
/* 1243 */       this.soundManager.resume();
/* 1244 */       this.mouseHandler.grabMouse();
/*      */     } 
/*      */     
/* 1247 */     updateTitle();
/*      */   }
/*      */   
/*      */   public void setOverlay(Overlay overlay) {
/* 1251 */     this.overlay = overlay;
/*      */   }
/*      */   
/*      */   public void destroy() {
/*      */     try {
/* 1256 */       LOGGER.info("Stopping!");
/*      */       try {
/* 1258 */         this.narrator.destroy();
/* 1259 */       } catch (Throwable throwable) {}
/*      */       
/*      */       try {
/* 1262 */         if (this.level != null) {
/* 1263 */           this.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
/*      */         }
/* 1265 */         disconnectWithProgressScreen();
/* 1266 */       } catch (Throwable throwable) {}
/*      */ 
/*      */ 
/*      */       
/* 1270 */       if (this.screen != null) {
/* 1271 */         this.screen.removed();
/*      */       }
/* 1273 */       close();
/*      */     } finally {
/* 1275 */       Util.timeSource = System::nanoTime;
/* 1276 */       if (this.delayedCrash == null) {
/* 1277 */         System.exit(0);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/* 1284 */     if (this.currentFrameProfile != null) {
/* 1285 */       this.currentFrameProfile.cancel();
/*      */     }
/*      */     try {
/* 1288 */       this.telemetryManager.close();
/* 1289 */       this.regionalCompliancies.close();
/* 1290 */       this.atlasManager.close();
/* 1291 */       this.fontManager.close();
/* 1292 */       this.gameRenderer.close();
/* 1293 */       this.shaderManager.close();
/* 1294 */       this.levelRenderer.close();
/* 1295 */       this.soundManager.destroy();
/* 1296 */       this.mapTextureManager.close();
/* 1297 */       this.textureManager.close();
/* 1298 */       this.resourceManager.close();
/* 1299 */       if (this.tracyFrameCapture != null) {
/* 1300 */         this.tracyFrameCapture.close();
/*      */       }
/* 1302 */       net.minecraft.client.gui.font.providers.FreeTypeUtil.destroy();
/* 1303 */       Util.shutdownExecutors();
/* 1304 */       RenderSystem.getSamplerCache().close();
/* 1305 */       RenderSystem.getDevice().close();
/* 1306 */     } catch (Throwable t) {
/* 1307 */       LOGGER.error("Shutdown failure!", t);
/* 1308 */       throw t;
/*      */     } finally {
/* 1310 */       this.virtualScreen.close();
/* 1311 */       this.window.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void runTick(boolean advanceGameTime) {
/*      */     boolean recordGpuUtilization;
/* 1317 */     this.window.setErrorSection("Pre render");
/*      */     
/* 1319 */     if (this.window.shouldClose()) {
/* 1320 */       stop();
/*      */     }
/*      */ 
/*      */     
/* 1324 */     if (this.pendingReload != null && !(this.overlay instanceof LoadingOverlay)) {
/* 1325 */       CompletableFuture<Void> future = this.pendingReload;
/* 1326 */       this.pendingReload = null;
/* 1327 */       reloadResourcePacks().thenRun(() -> future.complete(null));
/*      */     } 
/*      */     
/* 1330 */     int ticksToDo = this.deltaTracker.advanceTime(Util.getMillis(), advanceGameTime);
/*      */     
/* 1332 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/* 1334 */     if (advanceGameTime) {
/* 1335 */       Gizmos.TemporaryCollection temporaryCollection = collectPerTickGizmos(); 
/* 1336 */       try { profiler.push("scheduledPacketProcessing");
/* 1337 */         this.packetProcessor.processQueuedPackets();
/* 1338 */         profiler.popPush("scheduledExecutables");
/* 1339 */         runAllTasks();
/* 1340 */         profiler.pop();
/* 1341 */         if (temporaryCollection != null) temporaryCollection.close();  } catch (Throwable throwable) { if (temporaryCollection != null)
/*      */           try { temporaryCollection.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1343 */        profiler.push("tick");
/* 1344 */       if (ticksToDo > 0 && 
/* 1345 */         isLevelRunningNormally()) {
/* 1346 */         profiler.push("textures");
/* 1347 */         this.textureManager.tick();
/* 1348 */         profiler.pop();
/*      */       } 
/*      */       
/* 1351 */       for (int i = 0; i < Math.min(10, ticksToDo); i++) {
/* 1352 */         profiler.incrementCounter("clientTick");
/* 1353 */         Gizmos.TemporaryCollection temporaryCollection1 = collectPerTickGizmos(); 
/* 1354 */         try { tick();
/* 1355 */           if (temporaryCollection1 != null) temporaryCollection1.close();  } catch (Throwable throwable) { if (temporaryCollection1 != null)
/*      */             try { temporaryCollection1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 1357 */       }  if (ticksToDo > 0 && (this.level == null || this.level.tickRateManager().runsNormally())) {
/* 1358 */         this.drainedLatestTickGizmos = this.perTickGizmos.drainGizmos();
/*      */       }
/* 1360 */       profiler.pop();
/*      */     } 
/*      */     
/* 1363 */     this.window.setErrorSection("Render");
/*      */     
/* 1365 */     Gizmos.TemporaryCollection ignored = this.levelRenderer.collectPerFrameGizmos(); 
/* 1366 */     try { profiler.push("gpuAsync");
/* 1367 */       RenderSystem.executePendingTasks();
/*      */       
/* 1369 */       profiler.popPush("sound");
/* 1370 */       this.soundManager.updateSource(this.gameRenderer.getMainCamera());
/* 1371 */       profiler.popPush("toasts");
/* 1372 */       this.toastManager.update();
/* 1373 */       profiler.popPush("mouse");
/* 1374 */       this.mouseHandler.handleAccumulatedMovement();
/* 1375 */       profiler.popPush("render");
/*      */       
/* 1377 */       long renderStartTimer = Util.getNanos();
/* 1378 */       if (this.debugEntries.isCurrentlyEnabled(DebugScreenEntries.GPU_UTILIZATION) || this.metricsRecorder.isRecording()) {
/* 1379 */         recordGpuUtilization = ((this.currentFrameProfile == null || this.currentFrameProfile.isDone()) && !TimerQuery.getInstance().isRecording());
/* 1380 */         if (recordGpuUtilization) {
/* 1381 */           TimerQuery.getInstance().beginProfile();
/*      */         }
/*      */       } else {
/* 1384 */         recordGpuUtilization = false;
/* 1385 */         this.gpuUtilization = 0.0D;
/*      */       } 
/*      */       
/* 1388 */       RenderTarget mainRenderTarget = getMainRenderTarget();
/* 1389 */       RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(mainRenderTarget.getColorTexture(), 0, mainRenderTarget.getDepthTexture(), 1.0D);
/*      */       
/* 1391 */       profiler.push("gameRenderer");
/* 1392 */       if (!this.noRender) {
/* 1393 */         this.gameRenderer.render(this.deltaTracker, advanceGameTime);
/*      */       }
/*      */       
/* 1396 */       profiler.popPush("blit");
/*      */       
/* 1398 */       if (!this.window.isMinimized()) {
/* 1399 */         mainRenderTarget.blitToScreen();
/*      */       }
/* 1401 */       this.frameTimeNs = Util.getNanos() - renderStartTimer;
/* 1402 */       if (recordGpuUtilization) {
/* 1403 */         this.currentFrameProfile = TimerQuery.getInstance().endProfile();
/*      */       }
/*      */       
/* 1406 */       profiler.popPush("updateDisplay");
/* 1407 */       if (this.tracyFrameCapture != null) {
/* 1408 */         this.tracyFrameCapture.upload();
/* 1409 */         this.tracyFrameCapture.capture(mainRenderTarget);
/*      */       } 
/* 1411 */       this.window.updateDisplay(this.tracyFrameCapture);
/* 1412 */       int framerateLimit = this.framerateLimitTracker.getFramerateLimit();
/* 1413 */       if (framerateLimit < 260) {
/* 1414 */         RenderSystem.limitDisplayFPS(framerateLimit);
/*      */       }
/* 1416 */       profiler.pop();
/*      */       
/* 1418 */       profiler.popPush("yield");
/* 1419 */       Thread.yield();
/* 1420 */       profiler.pop();
/* 1421 */       if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*      */         try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1423 */      this.window.setErrorSection("Post render");
/* 1424 */     this.frames++;
/*      */     
/* 1426 */     boolean previouslyPaused = this.pause;
/* 1427 */     this.pause = (hasSingleplayerServer() && ((this.screen != null && this.screen.isPauseScreen()) || (this.overlay != null && this.overlay.isPauseScreen())) && !this.singleplayerServer.isPublished());
/* 1428 */     if (!previouslyPaused && this.pause) {
/* 1429 */       this.soundManager.pauseAllExcept(new SoundSource[] { SoundSource.MUSIC, SoundSource.UI });
/*      */     }
/* 1431 */     this.deltaTracker.updatePauseState(this.pause);
/* 1432 */     this.deltaTracker.updateFrozenState(!isLevelRunningNormally());
/*      */     
/* 1434 */     long currentTime = Util.getNanos();
/* 1435 */     long frameDuration = currentTime - this.lastNanoTime;
/* 1436 */     if (recordGpuUtilization) {
/* 1437 */       this.savedCpuDuration = frameDuration;
/*      */     }
/* 1439 */     getDebugOverlay().logFrameDuration(frameDuration);
/* 1440 */     this.lastNanoTime = currentTime;
/*      */     
/* 1442 */     profiler.push("fpsUpdate");
/*      */     
/* 1444 */     if (this.currentFrameProfile != null && this.currentFrameProfile.isDone()) {
/* 1445 */       this.gpuUtilization = this.currentFrameProfile.get() * 100.0D / this.savedCpuDuration;
/*      */     }
/*      */     
/* 1448 */     while (Util.getMillis() >= this.lastTime + 1000L) {
/* 1449 */       fps = this.frames;
/* 1450 */       this.lastTime += 1000L;
/* 1451 */       this.frames = 0;
/*      */     } 
/* 1453 */     profiler.pop();
/*      */   }
/*      */   private ProfilerFiller constructProfiler(boolean shouldCollectFrameProfile, SingleTickProfiler tickProfiler) {
/*      */     InactiveProfiler inactiveProfiler;
/*      */     ProfilerFiller profilerFiller;
/* 1458 */     if (!shouldCollectFrameProfile) {
/* 1459 */       this.fpsPieProfiler.disable();
/* 1460 */       if (!this.metricsRecorder.isRecording() && tickProfiler == null) {
/* 1461 */         return (ProfilerFiller)InactiveProfiler.INSTANCE;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1466 */     if (shouldCollectFrameProfile) {
/* 1467 */       if (!this.fpsPieProfiler.isEnabled()) {
/* 1468 */         this.fpsPieRenderTicks = 0;
/* 1469 */         this.fpsPieProfiler.enable();
/*      */       } 
/* 1471 */       this.fpsPieRenderTicks++;
/* 1472 */       profilerFiller = this.fpsPieProfiler.getFiller();
/*      */     } else {
/* 1474 */       inactiveProfiler = InactiveProfiler.INSTANCE;
/*      */     } 
/*      */     
/* 1477 */     if (this.metricsRecorder.isRecording()) {
/* 1478 */       profilerFiller = ProfilerFiller.combine((ProfilerFiller)inactiveProfiler, this.metricsRecorder.getProfiler());
/*      */     }
/*      */     
/* 1481 */     return SingleTickProfiler.decorateFiller(profilerFiller, tickProfiler);
/*      */   }
/*      */   
/*      */   private void finishProfilers(boolean shouldCollectFrameProfile, SingleTickProfiler tickProfiler) {
/* 1485 */     if (tickProfiler != null) {
/* 1486 */       tickProfiler.endTick();
/*      */     }
/* 1488 */     ProfilerPieChart profilerPieChart = getDebugOverlay().getProfilerPieChart();
/* 1489 */     if (shouldCollectFrameProfile) {
/* 1490 */       profilerPieChart.setPieChartResults(this.fpsPieProfiler.getResults());
/*      */     } else {
/* 1492 */       profilerPieChart.setPieChartResults(null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void resizeDisplay() {
/* 1498 */     int guiScale = this.window.calculateScale((Integer)this.options.guiScale().get(), isEnforceUnicode());
/* 1499 */     this.window.setGuiScale(guiScale);
/*      */     
/* 1501 */     if (this.screen != null) {
/* 1502 */       this.screen.resize(this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight());
/*      */     }
/*      */     
/* 1505 */     RenderTarget mainRenderTarget = getMainRenderTarget();
/* 1506 */     mainRenderTarget.resize(this.window.getWidth(), this.window.getHeight());
/* 1507 */     this.gameRenderer.resize(this.window.getWidth(), this.window.getHeight());
/* 1508 */     this.mouseHandler.setIgnoreFirstMove();
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorEntered() {
/* 1513 */     this.mouseHandler.cursorEntered();
/*      */   }
/*      */   
/*      */   public int getFps() {
/* 1517 */     return fps;
/*      */   }
/*      */   
/*      */   public long getFrameTimeNs() {
/* 1521 */     return this.frameTimeNs;
/*      */   }
/*      */   
/*      */   private void emergencySave() {
/* 1525 */     MemoryReserve.release();
/*      */     try {
/* 1527 */       if (this.isLocalServer && this.singleplayerServer != null) {
/* 1528 */         this.singleplayerServer.halt(true);
/*      */       }
/* 1530 */       disconnectWithSavingScreen();
/* 1531 */     } catch (Throwable throwable) {}
/*      */     
/* 1533 */     System.gc();
/*      */   }
/*      */   public boolean debugClientMetricsStart(Consumer<Component> debugFeedback) {
/*      */     Consumer<Path> whenClientMetricsRecordingFinished;
/* 1537 */     if (this.metricsRecorder.isRecording()) {
/* 1538 */       debugClientMetricsStop();
/* 1539 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*      */     Consumer<ProfileResults> onStopped = results -> {
/*      */         if (debugFeedback == EmptyProfileResults.EMPTY) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*      */         int ticks = debugFeedback.getTickDuration();
/*      */ 
/*      */         
/*      */         double durationInSeconds = debugFeedback.getNanoDuration() / TimeUtil.NANOSECONDS_PER_SECOND;
/*      */ 
/*      */         
/*      */         execute(());
/*      */       };
/*      */ 
/*      */     
/*      */     Consumer<Path> onFinished = profilePath -> {
/*      */         MutableComponent mutableComponent = Component.literal(debugFeedback.toString()).withStyle(ChatFormatting.UNDERLINE).withStyle(());
/*      */         
/*      */         execute(());
/*      */       };
/*      */     
/* 1565 */     SystemReport systemReport = fillSystemReport(new SystemReport(), this, this.languageManager, this.launchedVersion, this.options);
/*      */     
/*      */     Consumer<List<Path>> profileReports = logs -> {
/*      */         Path profilePath = archiveProfilingReport(systemReport, onFinished);
/*      */         systemReport.accept(profilePath);
/*      */       };
/* 1571 */     if (this.singleplayerServer == null) {
/* 1572 */       whenClientMetricsRecordingFinished = (path -> profileReports.accept(ImmutableList.of(path)));
/*      */     } else {
/* 1574 */       this.singleplayerServer.fillSystemReport(systemReport);
/*      */       
/* 1576 */       CompletableFuture<Path> clientMetricRecordingResult = new CompletableFuture<>();
/* 1577 */       CompletableFuture<Path> serverMetricRecordingResult = new CompletableFuture<>();
/* 1578 */       CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { clientMetricRecordingResult, serverMetricRecordingResult
/* 1579 */           }).thenRunAsync(() -> profileReports.accept(ImmutableList.of(clientMetricRecordingResult.join(), serverMetricRecordingResult.join())), 
/*      */           
/* 1581 */           (Executor)Util.ioPool());
/*      */ 
/*      */       
/* 1584 */       Objects.requireNonNull(serverMetricRecordingResult); this.singleplayerServer.startRecordingMetrics(ignored -> {  }, serverMetricRecordingResult::complete);
/* 1585 */       Objects.requireNonNull(clientMetricRecordingResult); whenClientMetricsRecordingFinished = clientMetricRecordingResult::complete;
/*      */     } 
/* 1587 */     this.metricsRecorder = (MetricsRecorder)ActiveMetricsRecorder.createStarted((net.minecraft.util.profiling.metrics.MetricsSamplerProvider)new ClientMetricsSamplersProvider((LongSupplier)Util.timeSource, this.levelRenderer), (LongSupplier)Util.timeSource, 
/*      */ 
/*      */         
/* 1590 */         (Executor)Util.ioPool(), new MetricsPersister("client"), results -> { this.metricsRecorder = InactiveMetricsRecorder.INSTANCE; onStopped.accept(onStopped); }, whenClientMetricsRecordingFinished);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1599 */     return true;
/*      */   }
/*      */   
/*      */   private void debugClientMetricsStop() {
/* 1603 */     this.metricsRecorder.end();
/* 1604 */     if (this.singleplayerServer != null) {
/* 1605 */       this.singleplayerServer.finishRecordingMetrics();
/*      */     }
/*      */   }
/*      */   
/*      */   private void debugClientMetricsCancel() {
/* 1610 */     this.metricsRecorder.cancel();
/* 1611 */     if (this.singleplayerServer != null) {
/* 1612 */       this.singleplayerServer.cancelRecordingMetrics();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private Path archiveProfilingReport(SystemReport systemReport, List<Path> profilingResultPaths) {
/*      */     Path archivePath;
/*      */     String levelName;
/* 1620 */     if (isLocalServer()) {
/* 1621 */       levelName = getSingleplayerServer().getWorldData().getLevelName();
/*      */     } else {
/* 1623 */       ServerData server = getCurrentServer();
/* 1624 */       levelName = (server != null) ? server.name : "unknown";
/*      */     } 
/*      */     
/*      */     try {
/* 1628 */       String profilingName = String.format(Locale.ROOT, "%s-%s-%s", new Object[] {
/*      */ 
/*      */             
/* 1631 */             Util.getFilenameFormattedDateTime(), levelName, 
/*      */             
/* 1633 */             SharedConstants.getCurrentVersion().id()
/*      */           });
/* 1635 */       String zipFileName = FileUtil.findAvailableName(MetricsPersister.PROFILING_RESULTS_DIR, profilingName, ".zip");
/* 1636 */       archivePath = MetricsPersister.PROFILING_RESULTS_DIR.resolve(zipFileName);
/* 1637 */     } catch (IOException e) {
/* 1638 */       throw new UncheckedIOException(e);
/*      */     } 
/*      */     
/* 1641 */     try { FileZipper fileZipper = new FileZipper(archivePath); 
/* 1642 */       try { fileZipper.add(Paths.get("system.txt", new String[0]), systemReport.toLineSeparatedString());
/* 1643 */         fileZipper.add(Paths.get("client", new String[0]).resolve(this.options.getFile().getName()), this.options.dumpOptionsForReport());
/* 1644 */         Objects.requireNonNull(fileZipper); profilingResultPaths.forEach(fileZipper::add);
/* 1645 */         fileZipper.close(); } catch (Throwable throwable) { try { fileZipper.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  }
/* 1646 */     finally { for (Path path : profilingResultPaths) {
/*      */         try {
/* 1648 */           FileUtils.forceDelete(path.toFile());
/* 1649 */         } catch (IOException e) {
/* 1650 */           LOGGER.warn("Failed to delete temporary profiling result {}", path, e);
/*      */         } 
/*      */       }  }
/*      */ 
/*      */     
/* 1655 */     return archivePath;
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1659 */     this.running = false;
/*      */   }
/*      */   
/*      */   public boolean isRunning() {
/* 1663 */     return this.running;
/*      */   }
/*      */   
/*      */   public void pauseGame(boolean suppressPauseMenuIfWeReallyArePausing) {
/* 1667 */     if (this.screen != null) {
/*      */       return;
/*      */     }
/*      */     
/* 1671 */     boolean canGameReallyBePaused = (hasSingleplayerServer() && !this.singleplayerServer.isPublished());
/* 1672 */     if (canGameReallyBePaused) {
/* 1673 */       setScreen((Screen)new PauseScreen(!suppressPauseMenuIfWeReallyArePausing));
/*      */     } else {
/* 1675 */       setScreen((Screen)new PauseScreen(true));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void continueAttack(boolean down) {
/* 1680 */     if (!down) {
/* 1681 */       this.missTime = 0;
/*      */     }
/* 1683 */     if (this.missTime > 0 || this.player.isUsingItem()) {
/*      */       return;
/*      */     }
/*      */     
/* 1687 */     ItemStack heldItem = this.player.getItemInHand(InteractionHand.MAIN_HAND);
/* 1688 */     if (heldItem.has(DataComponents.PIERCING_WEAPON)) {
/*      */       return;
/*      */     }
/* 1691 */     if (down && this.hitResult != null && this.hitResult.getType() == HitResult.Type.BLOCK) {
/* 1692 */       BlockHitResult blockHit = (BlockHitResult)this.hitResult;
/* 1693 */       BlockPos pos = blockHit.getBlockPos();
/*      */       
/* 1695 */       if (!this.level.getBlockState(pos).isAir()) {
/* 1696 */         Direction direction = blockHit.getDirection();
/* 1697 */         if (this.gameMode.continueDestroyBlock(pos, direction)) {
/* 1698 */           this.level.addBreakingBlockEffect(pos, direction);
/* 1699 */           this.player.swing(InteractionHand.MAIN_HAND);
/*      */         } 
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1705 */     this.gameMode.stopDestroyBlock(); } private boolean startAttack() {
/*      */     AttackRange customItemRange;
/*      */     BlockHitResult blockHit;
/*      */     BlockPos pos;
/* 1709 */     if (this.missTime > 0) {
/* 1710 */       return false;
/*      */     }
/*      */     
/* 1713 */     if (this.hitResult == null) {
/* 1714 */       LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
/* 1715 */       if (this.gameMode.hasMissTime()) {
/* 1716 */         this.missTime = 10;
/*      */       }
/* 1718 */       return false;
/*      */     } 
/*      */     
/* 1721 */     if (this.player.isHandsBusy()) {
/* 1722 */       return false;
/*      */     }
/*      */     
/* 1725 */     ItemStack heldItem = this.player.getItemInHand(InteractionHand.MAIN_HAND);
/* 1726 */     if (!heldItem.isItemEnabled(this.level.enabledFeatures())) {
/* 1727 */       return false;
/*      */     }
/*      */     
/* 1730 */     if (this.player.cannotAttackWithItem(heldItem, 0)) {
/* 1731 */       return false;
/*      */     }
/*      */     
/*      */     boolean endAttack = false;
/* 1735 */     PiercingWeapon piercingWeapon = (PiercingWeapon)heldItem.get(DataComponents.PIERCING_WEAPON);
/* 1736 */     if (piercingWeapon != null && !this.gameMode.isSpectator()) {
/* 1737 */       this.gameMode.piercingAttack(piercingWeapon);
/* 1738 */       this.player.swing(InteractionHand.MAIN_HAND);
/* 1739 */       return true;
/*      */     } 
/*      */     
/* 1742 */     switch (this.hitResult.getType()) {
/*      */       case ENTITY:
/* 1744 */         customItemRange = (AttackRange)heldItem.get(DataComponents.ATTACK_RANGE);
/* 1745 */         if (customItemRange == null || customItemRange.isInRange((LivingEntity)this.player, this.hitResult.getLocation())) {
/* 1746 */           this.gameMode.attack((Player)this.player, ((EntityHitResult)this.hitResult).getEntity());
/*      */         }
/*      */         break;
/*      */       case BLOCK:
/* 1750 */         blockHit = (BlockHitResult)this.hitResult;
/* 1751 */         pos = blockHit.getBlockPos();
/*      */         
/* 1753 */         if (!this.level.getBlockState(pos).isAir()) {
/* 1754 */           this.gameMode.startDestroyBlock(pos, blockHit.getDirection());
/* 1755 */           if (this.level.getBlockState(pos).isAir()) {
/* 1756 */             endAttack = true;
/*      */           }
/*      */           break;
/*      */         } 
/*      */       
/*      */       case MISS:
/* 1762 */         if (this.gameMode.hasMissTime()) {
/* 1763 */           this.missTime = 10;
/*      */         }
/* 1765 */         this.player.resetAttackStrengthTicker();
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 1770 */     if (!this.player.isSpectator()) {
/* 1771 */       this.player.swing(InteractionHand.MAIN_HAND);
/*      */     }
/* 1773 */     return endAttack;
/*      */   }
/*      */   
/*      */   private void startUseItem() {
/* 1777 */     if (this.gameMode.isDestroying()) {
/*      */       return;
/*      */     }
/*      */     
/* 1781 */     this.rightClickDelay = 4;
/*      */     
/* 1783 */     if (this.player.isHandsBusy()) {
/*      */       return;
/*      */     }
/*      */     
/* 1787 */     if (this.hitResult == null) {
/* 1788 */       LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
/*      */     }
/*      */     
/* 1791 */     for (InteractionHand hand : InteractionHand.values()) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1796 */       ItemStack heldItem = this.player.getItemInHand(hand);
/* 1797 */       if (!heldItem.isItemEnabled(this.level.enabledFeatures())) {
/*      */         return;
/*      */       }
/*      */       
/* 1801 */       if (this.hitResult != null) {
/* 1802 */         EntityHitResult entityHit; Entity entity; InteractionResult result; BlockHitResult blockHit; int oldCount; InteractionResult useResult; switch (this.hitResult.getType()) {
/*      */           case ENTITY:
/* 1804 */             entityHit = (EntityHitResult)this.hitResult;
/* 1805 */             entity = entityHit.getEntity();
/* 1806 */             if (!this.level.getWorldBorder().isWithinBounds(entity.blockPosition())) {
/*      */               return;
/*      */             }
/*      */             
/* 1810 */             if (!this.player.isWithinEntityInteractionRange(entity, 0.0D)) {
/*      */               break;
/*      */             }
/* 1813 */             result = this.gameMode.interactAt((Player)this.player, entity, entityHit, hand);
/* 1814 */             if (!result.consumesAction()) {
/* 1815 */               result = this.gameMode.interact((Player)this.player, entity, hand);
/*      */             }
/*      */             
/* 1818 */             if (result instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)result;
/* 1819 */               if (success.swingSource() == InteractionResult.SwingSource.CLIENT) {
/* 1820 */                 this.player.swing(hand);
/*      */               }
/*      */               return; }
/*      */             
/*      */             break;
/*      */           
/*      */           case BLOCK:
/* 1827 */             blockHit = (BlockHitResult)this.hitResult;
/*      */             
/* 1829 */             oldCount = heldItem.getCount();
/* 1830 */             useResult = this.gameMode.useItemOn(this.player, hand, blockHit);
/* 1831 */             if (useResult instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)useResult;
/* 1832 */               if (success.swingSource() == InteractionResult.SwingSource.CLIENT) {
/* 1833 */                 this.player.swing(hand);
/*      */ 
/*      */                 
/* 1836 */                 if (!heldItem.isEmpty() && (heldItem.getCount() != oldCount || this.player.hasInfiniteMaterials())) {
/* 1837 */                   this.gameRenderer.itemInHandRenderer.itemUsed(hand);
/*      */                 }
/*      */               } 
/*      */               return; }
/*      */             
/* 1842 */             if (useResult instanceof InteractionResult.Fail) {
/*      */               return;
/*      */             }
/*      */             break;
/*      */         } 
/*      */       
/*      */       } 
/* 1849 */       if (!heldItem.isEmpty()) {
/* 1850 */         InteractionResult useItemResult = this.gameMode.useItem((Player)this.player, hand);
/* 1851 */         if (useItemResult instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)useItemResult;
/* 1852 */           if (success.swingSource() == InteractionResult.SwingSource.CLIENT) {
/* 1853 */             this.player.swing(hand);
/*      */           }
/*      */           
/* 1856 */           this.gameRenderer.itemInHandRenderer.itemUsed(hand);
/*      */           return; }
/*      */       
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public MusicManager getMusicManager() {
/* 1864 */     return this.musicManager;
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1868 */     this.clientTickCount++;
/*      */     
/* 1870 */     if (this.level != null && !this.pause) {
/* 1871 */       this.level.tickRateManager().tick();
/*      */     }
/* 1873 */     if (this.rightClickDelay > 0) {
/* 1874 */       this.rightClickDelay--;
/*      */     }
/*      */     
/* 1877 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/* 1879 */     profiler.push("gui");
/* 1880 */     this.chatListener.tick();
/* 1881 */     this.gui.tick(this.pause);
/* 1882 */     profiler.pop();
/* 1883 */     this.gameRenderer.pick(1.0F);
/* 1884 */     this.tutorial.onLookAt(this.level, this.hitResult);
/*      */     
/* 1886 */     profiler.push("gameMode");
/* 1887 */     if (!this.pause && this.level != null) {
/* 1888 */       this.gameMode.tick();
/*      */     }
/* 1890 */     profiler.popPush("screen");
/*      */     
/* 1892 */     if (this.screen == null && this.player != null)
/* 1893 */     { if (this.player.isDeadOrDying() && !(this.screen instanceof DeathScreen)) {
/* 1894 */         setScreen(null);
/* 1895 */       } else if (this.player.isSleeping() && this.level != null) {
/* 1896 */         this.gui.getChat().openScreen(ChatComponent.ChatMethod.MESSAGE, InBedChatScreen::new);
/*      */       }  }
/* 1898 */     else { Screen screen = this.screen; if (screen instanceof InBedChatScreen) { InBedChatScreen inBedScreen = (InBedChatScreen)screen; if (!this.player.isSleeping())
/* 1899 */           inBedScreen.onPlayerWokeUp();  }
/*      */        }
/*      */     
/* 1902 */     if (this.screen != null) {
/* 1903 */       this.missTime = 10000;
/*      */     }
/*      */     
/* 1906 */     if (this.screen != null) {
/*      */       try {
/* 1908 */         this.screen.tick();
/* 1909 */       } catch (Throwable t) {
/* 1910 */         CrashReport report = CrashReport.forThrowable(t, "Ticking screen");
/* 1911 */         this.screen.fillCrashDetails(report);
/* 1912 */         throw new ReportedException(report);
/*      */       } 
/*      */     }
/* 1915 */     if (this.overlay != null) {
/* 1916 */       this.overlay.tick();
/*      */     }
/*      */     
/* 1919 */     if (!getDebugOverlay().showDebugScreen()) {
/* 1920 */       this.gui.clearCache();
/*      */     }
/*      */     
/* 1923 */     if (this.overlay == null && this.screen == null) {
/* 1924 */       profiler.popPush("Keybindings");
/* 1925 */       handleKeybinds();
/*      */       
/* 1927 */       if (this.missTime > 0) {
/* 1928 */         this.missTime--;
/*      */       }
/*      */     } 
/*      */     
/* 1932 */     if (this.level != null) {
/* 1933 */       if (!this.pause) {
/* 1934 */         profiler.popPush("gameRenderer");
/* 1935 */         this.gameRenderer.tick();
/* 1936 */         profiler.popPush("entities");
/* 1937 */         this.level.tickEntities();
/* 1938 */         profiler.popPush("blockEntities");
/* 1939 */         this.level.tickBlockEntities();
/*      */       } 
/* 1941 */     } else if (this.gameRenderer.currentPostEffect() != null) {
/* 1942 */       this.gameRenderer.clearPostEffect();
/*      */     } 
/*      */     
/* 1945 */     this.musicManager.tick();
/* 1946 */     this.soundManager.tick(this.pause);
/*      */ 
/*      */     
/* 1949 */     if (this.level != null) {
/* 1950 */       if (!this.pause) {
/* 1951 */         profiler.popPush("level");
/* 1952 */         if (!this.options.joinedFirstServer && isMultiplayerServer()) {
/* 1953 */           MutableComponent mutableComponent1 = Component.translatable("tutorial.socialInteractions.title");
/* 1954 */           MutableComponent mutableComponent2 = Component.translatable("tutorial.socialInteractions.description", new Object[] { Tutorial.key("socialInteractions") });
/* 1955 */           this.socialInteractionsToast = new TutorialToast(this.font, TutorialToast.Icons.SOCIAL_INTERACTIONS, (Component)mutableComponent1, (Component)mutableComponent2, true, 8000);
/*      */           
/* 1957 */           this.toastManager.addToast((Toast)this.socialInteractionsToast);
/* 1958 */           this.options.joinedFirstServer = true;
/* 1959 */           this.options.save();
/*      */         } 
/*      */         
/* 1962 */         this.tutorial.tick();
/*      */         
/*      */         try {
/* 1965 */           this.level.tick(() -> true);
/* 1966 */         } catch (Throwable t) {
/* 1967 */           CrashReport report = CrashReport.forThrowable(t, "Exception in world tick");
/* 1968 */           if (this.level == null) {
/* 1969 */             CrashReportCategory levelCategory = report.addCategory("Affected level");
/* 1970 */             levelCategory.setDetail("Problem", "Level is null!");
/*      */           } else {
/* 1972 */             this.level.fillReportDetails(report);
/*      */           } 
/* 1974 */           throw new ReportedException(report);
/*      */         } 
/*      */       } 
/* 1977 */       profiler.popPush("animateTick");
/* 1978 */       if (!this.pause && isLevelRunningNormally()) {
/* 1979 */         this.level.animateTick(this.player.getBlockX(), this.player.getBlockY(), this.player.getBlockZ());
/*      */       }
/* 1981 */       profiler.popPush("particles");
/* 1982 */       if (!this.pause && isLevelRunningNormally()) {
/* 1983 */         this.particleEngine.tick();
/*      */       }
/*      */       
/* 1986 */       ClientPacketListener connection = getConnection();
/* 1987 */       if (connection != null && !this.pause) {
/* 1988 */         connection.send((Packet)net.minecraft.network.protocol.game.ServerboundClientTickEndPacket.INSTANCE);
/*      */       }
/* 1990 */     } else if (this.pendingConnection != null) {
/* 1991 */       profiler.popPush("pendingConnection");
/* 1992 */       this.pendingConnection.tick();
/*      */     } 
/*      */     
/* 1995 */     profiler.popPush("keyboard");
/* 1996 */     this.keyboardHandler.tick();
/*      */     
/* 1998 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private boolean isLevelRunningNormally() {
/* 2002 */     return (this.level == null || this.level.tickRateManager().runsNormally());
/*      */   }
/*      */   
/*      */   private boolean isMultiplayerServer() {
/* 2006 */     return (!this.isLocalServer || (this.singleplayerServer != null && this.singleplayerServer.isPublished()));
/*      */   }
/*      */   
/*      */   private void handleKeybinds() {
/* 2010 */     while (this.options.keyTogglePerspective.consumeClick()) {
/* 2011 */       CameraType previous = this.options.getCameraType();
/* 2012 */       this.options.setCameraType(this.options.getCameraType().cycle());
/* 2013 */       if (previous.isFirstPerson() != this.options.getCameraType().isFirstPerson()) {
/* 2014 */         this.gameRenderer.checkEntityPostEffect(this.options.getCameraType().isFirstPerson() ? getCameraEntity() : null);
/*      */       }
/* 2016 */       this.levelRenderer.needsUpdate();
/*      */     } 
/*      */     
/* 2019 */     while (this.options.keySmoothCamera.consumeClick()) {
/* 2020 */       this.options.smoothCamera = !this.options.smoothCamera;
/*      */     }
/*      */     
/* 2023 */     for (int i = 0; i < 9; i++) {
/* 2024 */       boolean savePressed = this.options.keySaveHotbarActivator.isDown();
/* 2025 */       boolean loadPressed = this.options.keyLoadHotbarActivator.isDown();
/* 2026 */       if (this.options.keyHotbarSlots[i].consumeClick()) {
/* 2027 */         if (this.player.isSpectator()) {
/* 2028 */           this.gui.getSpectatorGui().onHotbarSelected(i);
/* 2029 */         } else if (this.player.hasInfiniteMaterials() && this.screen == null && (loadPressed || savePressed)) {
/* 2030 */           CreativeModeInventoryScreen.handleHotbarLoadOrSave(this, i, loadPressed, savePressed);
/*      */         } else {
/* 2032 */           this.player.getInventory().setSelectedSlot(i);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 2037 */     while (this.options.keySocialInteractions.consumeClick()) {
/* 2038 */       if (!isMultiplayerServer() && !SharedConstants.DEBUG_SOCIAL_INTERACTIONS) {
/* 2039 */         this.player.displayClientMessage(SOCIAL_INTERACTIONS_NOT_AVAILABLE, true);
/* 2040 */         this.narrator.saySystemNow(SOCIAL_INTERACTIONS_NOT_AVAILABLE); continue;
/*      */       } 
/* 2042 */       if (this.socialInteractionsToast != null) {
/* 2043 */         this.socialInteractionsToast.hide();
/* 2044 */         this.socialInteractionsToast = null;
/*      */       } 
/* 2046 */       setScreen((Screen)new SocialInteractionsScreen());
/*      */     } 
/*      */ 
/*      */     
/* 2050 */     while (this.options.keyInventory.consumeClick()) {
/* 2051 */       if (this.gameMode.isServerControlledInventory()) {
/* 2052 */         this.player.sendOpenInventory(); continue;
/*      */       } 
/* 2054 */       this.tutorial.onOpenInventory();
/* 2055 */       setScreen((Screen)new net.minecraft.client.gui.screens.inventory.InventoryScreen((Player)this.player));
/*      */     } 
/*      */     
/* 2058 */     while (this.options.keyAdvancements.consumeClick()) {
/* 2059 */       setScreen((Screen)new net.minecraft.client.gui.screens.advancements.AdvancementsScreen(this.player.connection.getAdvancements()));
/*      */     }
/* 2061 */     while (this.options.keyQuickActions.consumeClick()) {
/* 2062 */       getQuickActionsDialog().ifPresent(dialog -> this.player.connection.showDialog(dialog, this.screen));
/*      */     }
/* 2064 */     while (this.options.keySwapOffhand.consumeClick()) {
/* 2065 */       if (!this.player.isSpectator()) {
/* 2066 */         getConnection().send((Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
/*      */       }
/*      */     } 
/* 2069 */     while (this.options.keyDrop.consumeClick()) {
/* 2070 */       if (!this.player.isSpectator() && 
/* 2071 */         this.player.drop(hasControlDown())) {
/* 2072 */         this.player.swing(InteractionHand.MAIN_HAND);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2077 */     while (this.options.keyChat.consumeClick()) {
/* 2078 */       openChatScreen(ChatComponent.ChatMethod.MESSAGE);
/*      */     }
/* 2080 */     if (this.screen == null && this.overlay == null && this.options.keyCommand.consumeClick()) {
/* 2081 */       openChatScreen(ChatComponent.ChatMethod.COMMAND);
/*      */     }
/*      */     
/*      */     boolean instantAttack = false;
/* 2085 */     if (this.player.isUsingItem()) {
/* 2086 */       if (!this.options.keyUse.isDown()) {
/* 2087 */         this.gameMode.releaseUsingItem((Player)this.player);
/*      */       }
/*      */       
/* 2090 */       while (this.options.keyAttack.consumeClick());
/*      */       
/* 2092 */       while (this.options.keyUse.consumeClick());
/*      */       
/* 2094 */       while (this.options.keyPickItem.consumeClick());
/*      */     } else {
/*      */       
/* 2097 */       while (this.options.keyAttack.consumeClick()) {
/* 2098 */         instantAttack |= startAttack();
/*      */       }
/* 2100 */       while (this.options.keyUse.consumeClick()) {
/* 2101 */         startUseItem();
/*      */       }
/* 2103 */       while (this.options.keyPickItem.consumeClick()) {
/* 2104 */         pickBlock();
/*      */       }
/* 2106 */       if (this.player.isSpectator()) {
/* 2107 */         while (this.options.keySpectatorHotbar.consumeClick()) {
/* 2108 */           this.gui.getSpectatorGui().onHotbarActionKeyPressed();
/*      */         }
/*      */       }
/*      */     } 
/*      */     
/* 2113 */     if (this.options.keyUse.isDown() && this.rightClickDelay == 0 && !this.player.isUsingItem()) {
/* 2114 */       startUseItem();
/*      */     }
/*      */     
/* 2117 */     continueAttack((this.screen == null && !instantAttack && this.options.keyAttack.isDown() && this.mouseHandler.isMouseGrabbed()));
/*      */   }
/*      */   
/*      */   private Optional<Holder<Dialog>> getQuickActionsDialog() {
/* 2121 */     Registry<Dialog> dialogRegistry = this.player.connection.registryAccess().lookupOrThrow(Registries.DIALOG);
/* 2122 */     return dialogRegistry.get(DialogTags.QUICK_ACTIONS).flatMap(quickActions -> (quickActions.size() == 0) ? Optional.empty() : ((quickActions.size() == 1) ? Optional.of(quickActions.get(0)) : dialogRegistry.get(Dialogs.QUICK_ACTIONS)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClientTelemetryManager getTelemetryManager() {
/* 2136 */     return this.telemetryManager;
/*      */   }
/*      */   
/*      */   public double getGpuUtilization() {
/* 2140 */     return this.gpuUtilization;
/*      */   }
/*      */   
/*      */   public ProfileKeyPairManager getProfileKeyPairManager() {
/* 2144 */     return this.profileKeyPairManager;
/*      */   }
/*      */   
/*      */   public WorldOpenFlows createWorldOpenFlows() {
/* 2148 */     return new WorldOpenFlows(this, this.levelSource);
/*      */   }
/*      */   
/*      */   public void doWorldLoad(LevelStorageSource.LevelStorageAccess levelSourceAccess, PackRepository packRepository, WorldStem worldStem, boolean newWorld) {
/* 2152 */     disconnectWithProgressScreen();
/*      */     
/* 2154 */     Instant worldLoadStart = Instant.now();
/*      */     
/* 2156 */     LevelLoadTracker loadTracker = new LevelLoadTracker(newWorld ? 500L : 0L);
/* 2157 */     LevelLoadingScreen screen = new LevelLoadingScreen(loadTracker, LevelLoadingScreen.Reason.OTHER);
/* 2158 */     setScreen((Screen)screen);
/*      */     
/* 2160 */     int chunkStatusViewRadius = Math.max(5, 3) + ChunkLevel.RADIUS_AROUND_FULL_CHUNK + 1;
/*      */     
/*      */     try {
/* 2163 */       levelSourceAccess.saveDataTag((RegistryAccess)worldStem.registries().compositeAccess(), worldStem.worldData());
/*      */       
/* 2165 */       LevelLoadListener loadListener = LevelLoadListener.compose((LevelLoadListener)loadTracker, (LevelLoadListener)LoggingLevelLoadListener.forSingleplayer());
/* 2166 */       this.singleplayerServer = (IntegratedServer)MinecraftServer.spin(thread -> new IntegratedServer(loadListener, this, levelSourceAccess, levelSourceAccess, packRepository, this.services, worldStem));
/* 2167 */       loadTracker.setServerChunkStatusView(this.singleplayerServer.createChunkLoadStatusView(chunkStatusViewRadius));
/* 2168 */       this.isLocalServer = true;
/* 2169 */       updateReportEnvironment(ReportEnvironment.local());
/* 2170 */       this.quickPlayLog.setWorldData(QuickPlayLog.Type.SINGLEPLAYER, levelSourceAccess.getLevelId(), worldStem.worldData().getLevelName());
/* 2171 */     } catch (Throwable t) {
/* 2172 */       CrashReport report = CrashReport.forThrowable(t, "Starting integrated server");
/* 2173 */       CrashReportCategory category = report.addCategory("Starting integrated server");
/*      */       
/* 2175 */       category.setDetail("Level ID", levelSourceAccess.getLevelId());
/* 2176 */       category.setDetail("Level Name", () -> worldStem.worldData().getLevelName());
/*      */       
/* 2178 */       throw new ReportedException(report);
/*      */     } 
/*      */     
/* 2181 */     ProfilerFiller profiler = Profiler.get();
/*      */     
/* 2183 */     profiler.push("waitForServer");
/*      */     
/* 2185 */     long tickLengthNs = TimeUnit.SECONDS.toNanos(1L) / 60L;
/* 2186 */     while (!this.singleplayerServer.isReady() || this.overlay != null) {
/* 2187 */       long finishTime = Util.getNanos() + tickLengthNs;
/* 2188 */       screen.tick();
/* 2189 */       if (this.overlay != null) {
/* 2190 */         this.overlay.tick();
/*      */       }
/* 2192 */       runTick(false);
/* 2193 */       runAllTasks();
/* 2194 */       managedBlock(() -> (Util.getNanos() > finishTime));
/* 2195 */       handleDelayedCrash();
/*      */     } 
/*      */     
/* 2198 */     profiler.pop();
/* 2199 */     Duration worldLoadDuration = Duration.between(worldLoadStart, Instant.now());
/*      */     
/* 2201 */     SocketAddress socketAddress = this.singleplayerServer.getConnection().startMemoryChannel();
/* 2202 */     Connection connection = Connection.connectToLocalServer(socketAddress);
/* 2203 */     connection.initiateServerboundPlayConnection(
/* 2204 */         socketAddress.toString(), 0, (ClientLoginPacketListener)new ClientHandshakePacketListenerImpl(connection, this, null, null, newWorld, worldLoadDuration, status -> {  }, loadTracker, null));
/*      */ 
/*      */ 
/*      */     
/* 2208 */     connection.send((Packet)new ServerboundHelloPacket(getUser().getName(), getUser().getProfileId()));
/* 2209 */     this.pendingConnection = connection;
/*      */   }
/*      */   
/*      */   public void setLevel(ClientLevel level) {
/* 2213 */     this.level = level;
/* 2214 */     updateLevelInEngines(level);
/*      */   }
/*      */   
/*      */   public void disconnectFromWorld(Component message) {
/* 2218 */     boolean localServer = isLocalServer();
/* 2219 */     ServerData currentServer = getCurrentServer();
/*      */     
/* 2221 */     if (this.level != null)
/*      */     {
/* 2223 */       this.level.disconnect(message);
/*      */     }
/*      */     
/* 2226 */     if (localServer) {
/* 2227 */       disconnectWithSavingScreen();
/*      */     } else {
/* 2229 */       disconnectWithProgressScreen();
/*      */     } 
/*      */     
/* 2232 */     TitleScreen titleScreen = new TitleScreen();
/* 2233 */     if (localServer) {
/* 2234 */       setScreen((Screen)titleScreen);
/* 2235 */     } else if (currentServer != null && currentServer.isRealm()) {
/* 2236 */       setScreen((Screen)new RealmsMainScreen((Screen)titleScreen));
/*      */     } else {
/* 2238 */       setScreen((Screen)new JoinMultiplayerScreen((Screen)titleScreen));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void disconnectWithSavingScreen() {
/* 2243 */     disconnect((Screen)new GenericMessageScreen(SAVING_LEVEL), false);
/*      */   }
/*      */   
/*      */   public void disconnectWithProgressScreen() {
/* 2247 */     disconnectWithProgressScreen(true);
/*      */   }
/*      */   
/*      */   public void disconnectWithProgressScreen(boolean stopSound) {
/* 2251 */     disconnect((Screen)new net.minecraft.client.gui.screens.ProgressScreen(true), false, stopSound);
/*      */   }
/*      */   
/*      */   public void disconnect(Screen screen, boolean keepResourcePacks) {
/* 2255 */     disconnect(screen, keepResourcePacks, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void disconnect(Screen screen, boolean keepResourcePacks, boolean stopSound) {
/* 2261 */     ClientPacketListener connection = getConnection();
/* 2262 */     if (connection != null) {
/* 2263 */       dropAllTasks();
/* 2264 */       connection.close();
/* 2265 */       if (!keepResourcePacks) {
/* 2266 */         clearDownloadedResourcePacks();
/*      */       }
/*      */     } 
/* 2269 */     this.playerSocialManager.stopOnlineMode();
/*      */     
/* 2271 */     if (this.metricsRecorder.isRecording()) {
/* 2272 */       debugClientMetricsCancel();
/*      */     }
/*      */     
/* 2275 */     IntegratedServer server = this.singleplayerServer;
/* 2276 */     this.singleplayerServer = null;
/*      */     
/* 2278 */     this.gameRenderer.resetData();
/* 2279 */     this.gameMode = null;
/*      */     
/* 2281 */     this.narrator.clear();
/* 2282 */     this.clientLevelTeardownInProgress = true;
/*      */     try {
/* 2284 */       if (this.level != null) {
/* 2285 */         this.gui.onDisconnected();
/*      */       }
/*      */       
/* 2288 */       if (server != null) {
/*      */         
/* 2290 */         setScreen((Screen)new GenericMessageScreen(SAVING_LEVEL));
/* 2291 */         ProfilerFiller profiler = Profiler.get();
/* 2292 */         profiler.push("waitForServer");
/* 2293 */         while (!server.isShutdown()) {
/* 2294 */           runTick(false);
/*      */         }
/* 2296 */         profiler.pop();
/*      */       } 
/* 2298 */       setScreenAndShow(screen);
/* 2299 */       this.isLocalServer = false;
/*      */       
/* 2301 */       this.level = null;
/* 2302 */       updateLevelInEngines(null, stopSound);
/* 2303 */       this.player = null;
/*      */     } finally {
/* 2305 */       this.clientLevelTeardownInProgress = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearDownloadedResourcePacks() {
/* 2310 */     this.downloadedPackSource.cleanupAfterDisconnect();
/*      */     
/* 2312 */     runAllTasks();
/*      */   }
/*      */   
/*      */   public void clearClientLevel(Screen screen) {
/* 2316 */     ClientPacketListener connection = getConnection();
/* 2317 */     if (connection != null) {
/* 2318 */       connection.clearLevel();
/*      */     }
/*      */     
/* 2321 */     if (this.metricsRecorder.isRecording()) {
/* 2322 */       debugClientMetricsCancel();
/*      */     }
/*      */     
/* 2325 */     this.gameRenderer.resetData();
/* 2326 */     this.gameMode = null;
/*      */     
/* 2328 */     this.narrator.clear();
/* 2329 */     this.clientLevelTeardownInProgress = true;
/*      */     try {
/* 2331 */       setScreenAndShow(screen);
/*      */ 
/*      */ 
/*      */       
/* 2335 */       this.gui.onDisconnected();
/* 2336 */       this.level = null;
/* 2337 */       updateLevelInEngines(null);
/* 2338 */       this.player = null;
/*      */     } finally {
/* 2340 */       this.clientLevelTeardownInProgress = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScreenAndShow(Screen screen)
/*      */   {
/* 2350 */     Zone ignored = Profiler.get().zone("forcedTick"); 
/* 2351 */     try { setScreen(screen);
/* 2352 */       runTick(false);
/* 2353 */       if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*      */         try { ignored.close(); }
/*      */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */           throw throwable; }
/* 2357 */      } private void updateLevelInEngines(ClientLevel level) { updateLevelInEngines(level, true); }
/*      */ 
/*      */   
/*      */   private void updateLevelInEngines(ClientLevel level, boolean stopSound) {
/* 2361 */     if (stopSound) {
/* 2362 */       this.soundManager.stop();
/*      */     }
/* 2364 */     setCameraEntity(null);
/* 2365 */     this.pendingConnection = null;
/*      */     
/* 2367 */     this.levelRenderer.setLevel(level);
/* 2368 */     this.particleEngine.setLevel(level);
/* 2369 */     this.gameRenderer.setLevel(level);
/* 2370 */     updateTitle();
/*      */   }
/*      */   
/*      */   private UserApiService.UserProperties userProperties() {
/* 2374 */     return this.userPropertiesFuture.join();
/*      */   }
/*      */   
/*      */   public boolean telemetryOptInExtra() {
/* 2378 */     return (extraTelemetryAvailable() && (Boolean)this.options.telemetryOptInExtra().get());
/*      */   }
/*      */   
/*      */   public boolean extraTelemetryAvailable() {
/* 2382 */     return (allowsTelemetry() && userProperties().flag(UserApiService.UserFlag.OPTIONAL_TELEMETRY_AVAILABLE));
/*      */   }
/*      */   
/*      */   public boolean allowsTelemetry() {
/* 2386 */     if (SharedConstants.IS_RUNNING_IN_IDE && !SharedConstants.DEBUG_FORCE_TELEMETRY) {
/* 2387 */       return false;
/*      */     }
/* 2389 */     return userProperties().flag(UserApiService.UserFlag.TELEMETRY_ENABLED);
/*      */   }
/*      */   
/*      */   public boolean allowsMultiplayer() {
/* 2393 */     return (this.allowsMultiplayer && 
/* 2394 */       userProperties().flag(UserApiService.UserFlag.SERVERS_ALLOWED) && 
/* 2395 */       multiplayerBan() == null && 
/* 2396 */       !isNameBanned());
/*      */   }
/*      */   
/*      */   public boolean allowsRealms() {
/* 2400 */     return (userProperties().flag(UserApiService.UserFlag.REALMS_ALLOWED) && multiplayerBan() == null);
/*      */   }
/*      */   
/*      */   public BanDetails multiplayerBan() {
/* 2404 */     return (BanDetails)userProperties().bannedScopes().get("MULTIPLAYER");
/*      */   }
/*      */   
/*      */   public boolean isNameBanned() {
/* 2408 */     ProfileResult result = this.profileFuture.getNow(null);
/* 2409 */     return (result != null && result.actions().contains(ProfileActionType.FORCED_NAME_CHANGE));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlocked(UUID uuid) {
/* 2414 */     if (!getChatStatus().isChatAllowed(false)) {
/* 2415 */       return ((this.player == null || !uuid.equals(this.player.getUUID())) && !uuid.equals(Util.NIL_UUID));
/*      */     }
/* 2417 */     return this.playerSocialManager.shouldHideMessageFrom(uuid);
/*      */   }
/*      */   
/*      */   public ChatStatus getChatStatus() {
/* 2421 */     if (this.options.chatVisibility().get() == ChatVisiblity.HIDDEN) {
/* 2422 */       return ChatStatus.DISABLED_BY_OPTIONS;
/*      */     }
/* 2424 */     if (!this.allowsChat) {
/* 2425 */       return ChatStatus.DISABLED_BY_LAUNCHER;
/*      */     }
/* 2427 */     if (!userProperties().flag(UserApiService.UserFlag.CHAT_ALLOWED)) {
/* 2428 */       return ChatStatus.DISABLED_BY_PROFILE;
/*      */     }
/* 2430 */     return ChatStatus.ENABLED;
/*      */   }
/*      */   
/*      */   public final boolean isDemo() {
/* 2434 */     return this.demo;
/*      */   }
/*      */   
/*      */   public final boolean canSwitchGameMode() {
/* 2438 */     return (this.player != null && this.gameMode != null);
/*      */   }
/*      */   
/*      */   public ClientPacketListener getConnection() {
/* 2442 */     return (this.player == null) ? null : this.player.connection;
/*      */   }
/*      */   
/*      */   public static boolean renderNames() {
/* 2446 */     return !instance.options.hideGui;
/*      */   }
/*      */   
/*      */   public static boolean useShaderTransparency() {
/* 2450 */     return (!instance.gameRenderer.isPanoramicMode() && (Boolean)instance.options.improvedTransparency().get());
/*      */   }
/*      */   
/*      */   public static boolean useAmbientOcclusion() {
/* 2454 */     return (Boolean)instance.options.ambientOcclusion().get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pickBlock() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield hitResult : Lnet/minecraft/world/phys/HitResult;
/*      */     //   4: ifnull -> 20
/*      */     //   7: aload_0
/*      */     //   8: getfield hitResult : Lnet/minecraft/world/phys/HitResult;
/*      */     //   11: invokevirtual getType : ()Lnet/minecraft/world/phys/HitResult$Type;
/*      */     //   14: getstatic net/minecraft/world/phys/HitResult$Type.MISS : Lnet/minecraft/world/phys/HitResult$Type;
/*      */     //   17: if_acmpne -> 21
/*      */     //   20: return
/*      */     //   21: aload_0
/*      */     //   22: invokevirtual hasControlDown : ()Z
/*      */     //   25: istore_1
/*      */     //   26: aload_0
/*      */     //   27: getfield hitResult : Lnet/minecraft/world/phys/HitResult;
/*      */     //   30: dup
/*      */     //   31: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   34: pop
/*      */     //   35: astore_2
/*      */     //   36: iconst_0
/*      */     //   37: istore_3
/*      */     //   38: aload_2
/*      */     //   39: iload_3
/*      */     //   40: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*      */     //   45: lookupswitch default -> 116, 0 -> 72, 1 -> 94
/*      */     //   72: aload_2
/*      */     //   73: checkcast net/minecraft/world/phys/BlockHitResult
/*      */     //   76: astore #4
/*      */     //   78: aload_0
/*      */     //   79: getfield gameMode : Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;
/*      */     //   82: aload #4
/*      */     //   84: invokevirtual getBlockPos : ()Lnet/minecraft/core/BlockPos;
/*      */     //   87: iload_1
/*      */     //   88: invokevirtual handlePickItemFromBlock : (Lnet/minecraft/core/BlockPos;Z)V
/*      */     //   91: goto -> 116
/*      */     //   94: aload_2
/*      */     //   95: checkcast net/minecraft/world/phys/EntityHitResult
/*      */     //   98: astore #5
/*      */     //   100: aload_0
/*      */     //   101: getfield gameMode : Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;
/*      */     //   104: aload #5
/*      */     //   106: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   109: iload_1
/*      */     //   110: invokevirtual handlePickItemFromEntity : (Lnet/minecraft/world/entity/Entity;Z)V
/*      */     //   113: goto -> 116
/*      */     //   116: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2458	-> 0
/*      */     //   #2459	-> 20
/*      */     //   #2462	-> 21
/*      */     //   #2463	-> 26
/*      */     //   #2464	-> 72
/*      */     //   #2465	-> 78
/*      */     //   #2466	-> 94
/*      */     //   #2467	-> 100
/*      */     //   #2471	-> 116
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   78	16	4	blockHitResult	Lnet/minecraft/world/phys/BlockHitResult;
/*      */     //   100	16	5	entityHitResult	Lnet/minecraft/world/phys/EntityHitResult;
/*      */     //   0	117	0	this	Lnet/minecraft/client/Minecraft;
/*      */     //   26	91	1	includeData	Z
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CrashReport fillReport(CrashReport report) {
/* 2474 */     SystemReport systemReport = report.getSystemReport();
/*      */     try {
/* 2476 */       fillSystemReport(systemReport, this, this.languageManager, this.launchedVersion, this.options);
/* 2477 */       fillUptime(report.addCategory("Uptime"));
/*      */       
/* 2479 */       if (this.level != null) {
/* 2480 */         this.level.fillReportDetails(report);
/*      */       }
/*      */       
/* 2483 */       if (this.singleplayerServer != null) {
/* 2484 */         this.singleplayerServer.fillSystemReport(systemReport);
/*      */       }
/*      */       
/* 2487 */       this.reloadStateTracker.fillCrashReport(report);
/* 2488 */     } catch (Throwable t) {
/* 2489 */       LOGGER.error("Failed to collect details", t);
/*      */     } 
/*      */     
/* 2492 */     return report;
/*      */   }
/*      */   
/*      */   public static void fillReport(Minecraft minecraft, LanguageManager languageManager, String launchedVersion, Options options, CrashReport report) {
/* 2496 */     SystemReport system = report.getSystemReport();
/* 2497 */     fillSystemReport(system, minecraft, languageManager, launchedVersion, options);
/*      */   }
/*      */   
/*      */   private static String formatSeconds(double timeInSeconds) {
/* 2501 */     return String.format(Locale.ROOT, "%.3fs", new Object[] { timeInSeconds });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void fillUptime(CrashReportCategory category) {
/* 2507 */     category.setDetail("JVM uptime", () -> formatSeconds(ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0D));
/*      */     
/* 2509 */     category.setDetail("Wall uptime", () -> formatSeconds((System.currentTimeMillis() - this.clientStartTimeMs) / 1000.0D));
/*      */     
/* 2511 */     category.setDetail("High-res time", () -> formatSeconds(Util.getMillis() / 1000.0D));
/*      */     
/* 2513 */     category.setDetail("Client ticks", () -> String.format(Locale.ROOT, "%d ticks / %.3fs", new Object[] { this.clientTickCount, this.clientTickCount / 20.0D }));
/*      */   }
/*      */   
/*      */   private static SystemReport fillSystemReport(SystemReport systemReport, Minecraft minecraft, LanguageManager languageManager, String launchedVersion, Options options) {
/* 2517 */     systemReport.setDetail("Launched Version", () -> launchedVersion);
/* 2518 */     String launcherBrand = getLauncherBrand();
/* 2519 */     if (launcherBrand != null) {
/* 2520 */       systemReport.setDetail("Launcher name", launcherBrand);
/*      */     }
/* 2522 */     systemReport.setDetail("Backend library", RenderSystem::getBackendDescription);
/* 2523 */     systemReport.setDetail("Backend API", RenderSystem::getApiDescription);
/* 2524 */     systemReport.setDetail("Window size", () -> (minecraft != null) ? ("" + minecraft.window.getWidth() + "x" + minecraft.window.getWidth()) : "<not initialized>");
/* 2525 */     systemReport.setDetail("GFLW Platform", Window::getPlatform);
/*      */     
/* 2527 */     systemReport.setDetail("Render Extensions", () -> String.join(", ", RenderSystem.getDevice().getEnabledExtensions()));
/* 2528 */     systemReport.setDetail("GL debug messages", () -> {
/*      */           GpuDevice device = RenderSystem.tryGetDevice();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           return (device == null) ? "<no renderer available>" : (device.isDebuggingEnabled() ? String.join("\n", device.getLastDebugMessages()) : "<debugging unavailable>");
/*      */         });
/*      */ 
/*      */ 
/*      */     
/* 2539 */     systemReport.setDetail("Is Modded", () -> checkModStatus().fullDescription());
/* 2540 */     systemReport.setDetail("Universe", () -> (minecraft != null) ? Long.toHexString(minecraft.canary) : "404");
/*      */     
/* 2542 */     systemReport.setDetail("Type", "Client");
/* 2543 */     if (options != null) {
/* 2544 */       if (minecraft != null) {
/* 2545 */         String gpuWarnings = minecraft.getGpuWarnlistManager().getAllWarnings();
/* 2546 */         if (gpuWarnings != null) {
/* 2547 */           systemReport.setDetail("GPU Warnings", gpuWarnings);
/*      */         }
/*      */       } 
/* 2550 */       systemReport.setDetail("Transparency", (Boolean)options.improvedTransparency().get() ? "shader" : "regular");
/* 2551 */       systemReport.setDetail("Render Distance", "" + options.getEffectiveRenderDistance() + "/" + options.getEffectiveRenderDistance() + " chunks");
/*      */     } 
/* 2553 */     if (minecraft != null) {
/* 2554 */       systemReport.setDetail("Resource Packs", () -> PackRepository.displayPackList(minecraft.getResourcePackRepository().getSelectedPacks()));
/*      */     }
/*      */     
/* 2557 */     if (languageManager != null) {
/* 2558 */       systemReport.setDetail("Current Language", () -> languageManager.getSelected());
/*      */     }
/* 2560 */     systemReport.setDetail("Locale", String.valueOf(Locale.getDefault()));
/* 2561 */     systemReport.setDetail("System encoding", () -> System.getProperty("sun.jnu.encoding", "<not set>"));
/* 2562 */     systemReport.setDetail("File encoding", () -> System.getProperty("file.encoding", "<not set>"));
/* 2563 */     systemReport.setDetail("CPU", GLX::_getCpuInfo);
/* 2564 */     return systemReport;
/*      */   }
/*      */   
/*      */   public static Minecraft getInstance() {
/* 2568 */     return instance;
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> delayTextureReload() {
/* 2572 */     return submit(this::reloadResourcePacks).thenCompose(result -> result);
/*      */   }
/*      */   
/*      */   public void updateReportEnvironment(ReportEnvironment environment) {
/* 2576 */     if (!this.reportingContext.matches(environment)) {
/* 2577 */       this.reportingContext = ReportingContext.create(environment, this.userApiService);
/*      */     }
/*      */   }
/*      */   
/*      */   public ServerData getCurrentServer() {
/* 2582 */     return (ServerData)Optionull.map(getConnection(), ClientPacketListener::getServerData);
/*      */   }
/*      */   
/*      */   public boolean isLocalServer() {
/* 2586 */     return this.isLocalServer;
/*      */   }
/*      */   
/*      */   public boolean hasSingleplayerServer() {
/* 2590 */     return (this.isLocalServer && this.singleplayerServer != null);
/*      */   }
/*      */   
/*      */   public IntegratedServer getSingleplayerServer() {
/* 2594 */     return this.singleplayerServer;
/*      */   }
/*      */   
/*      */   public boolean isSingleplayer() {
/* 2598 */     IntegratedServer singleplayerServer = getSingleplayerServer();
/* 2599 */     return (singleplayerServer != null && !singleplayerServer.isPublished());
/*      */   }
/*      */   
/*      */   public boolean isLocalPlayer(UUID profileId) {
/* 2603 */     return profileId.equals(getUser().getProfileId());
/*      */   }
/*      */   
/*      */   public User getUser() {
/* 2607 */     return this.user;
/*      */   }
/*      */   
/*      */   public GameProfile getGameProfile() {
/* 2611 */     ProfileResult profileResult = this.profileFuture.join();
/* 2612 */     if (profileResult != null) {
/* 2613 */       return profileResult.profile();
/*      */     }
/* 2615 */     return new GameProfile(this.user.getProfileId(), this.user.getName());
/*      */   }
/*      */   
/*      */   public Proxy getProxy() {
/* 2619 */     return this.proxy;
/*      */   }
/*      */   
/*      */   public TextureManager getTextureManager() {
/* 2623 */     return this.textureManager;
/*      */   }
/*      */   
/*      */   public ShaderManager getShaderManager() {
/* 2627 */     return this.shaderManager;
/*      */   }
/*      */   
/*      */   public ResourceManager getResourceManager() {
/* 2631 */     return (ResourceManager)this.resourceManager;
/*      */   }
/*      */   
/*      */   public PackRepository getResourcePackRepository() {
/* 2635 */     return this.resourcePackRepository;
/*      */   }
/*      */   
/*      */   public VanillaPackResources getVanillaPackResources() {
/* 2639 */     return this.vanillaPackResources;
/*      */   }
/*      */   
/*      */   public DownloadedPackSource getDownloadedPackSource() {
/* 2643 */     return this.downloadedPackSource;
/*      */   }
/*      */   
/*      */   public Path getResourcePackDirectory() {
/* 2647 */     return this.resourcePackDirectory;
/*      */   }
/*      */   
/*      */   public LanguageManager getLanguageManager() {
/* 2651 */     return this.languageManager;
/*      */   }
/*      */   
/*      */   public boolean isPaused() {
/* 2655 */     return this.pause;
/*      */   }
/*      */   
/*      */   public GpuWarnlistManager getGpuWarnlistManager() {
/* 2659 */     return this.gpuWarnlistManager;
/*      */   }
/*      */   
/*      */   public SoundManager getSoundManager() {
/* 2663 */     return this.soundManager;
/*      */   }
/*      */   
/*      */   public Music getSituationalMusic() {
/* 2667 */     Music screenMusic = (Music)Optionull.map(this.screen, Screen::getBackgroundMusic);
/* 2668 */     if (screenMusic != null) {
/* 2669 */       return screenMusic;
/*      */     }
/* 2671 */     Camera camera = this.gameRenderer.getMainCamera();
/* 2672 */     if (this.player != null && camera != null) {
/* 2673 */       Level playerLevel = this.player.level();
/* 2674 */       if (playerLevel.dimension() == Level.END && this.gui.getBossOverlay().shouldPlayMusic()) {
/* 2675 */         return Musics.END_BOSS;
/*      */       }
/*      */       
/* 2678 */       BackgroundMusic backgroundMusic = (BackgroundMusic)camera.attributeProbe().getValue(EnvironmentAttributes.BACKGROUND_MUSIC, 1.0F);
/* 2679 */       boolean isCreative = ((this.player.getAbilities()).instabuild && (this.player.getAbilities()).mayfly);
/* 2680 */       boolean isUnderwater = this.player.isUnderWater();
/* 2681 */       return backgroundMusic.select(isCreative, isUnderwater).orElse(null);
/*      */     } 
/*      */     
/* 2684 */     return Musics.MENU;
/*      */   }
/*      */   
/*      */   public float getMusicVolume() {
/* 2688 */     if (this.screen != null && this.screen.getBackgroundMusic() != null) {
/* 2689 */       return 1.0F;
/*      */     }
/* 2691 */     Camera camera = this.gameRenderer.getMainCamera();
/* 2692 */     if (camera != null) {
/* 2693 */       return (Float)camera.attributeProbe().getValue(EnvironmentAttributes.MUSIC_VOLUME, 1.0F);
/*      */     }
/* 2695 */     return 1.0F;
/*      */   }
/*      */   
/*      */   public Services services() {
/* 2699 */     return this.services;
/*      */   }
/*      */   
/*      */   public SkinManager getSkinManager() {
/* 2703 */     return this.skinManager;
/*      */   }
/*      */   
/*      */   public Entity getCameraEntity() {
/* 2707 */     return this.cameraEntity;
/*      */   }
/*      */   
/*      */   public void setCameraEntity(Entity cameraEntity) {
/* 2711 */     this.cameraEntity = cameraEntity;
/* 2712 */     this.gameRenderer.checkEntityPostEffect(cameraEntity);
/*      */   }
/*      */   
/*      */   public boolean shouldEntityAppearGlowing(Entity entity) {
/* 2716 */     return (entity.isCurrentlyGlowing() || (this.player != null && this.player.isSpectator() && this.options.keySpectatorOutlines.isDown() && entity.getType() == EntityType.PLAYER));
/*      */   }
/*      */ 
/*      */   
/*      */   protected Thread getRunningThread() {
/* 2721 */     return this.gameThread;
/*      */   }
/*      */ 
/*      */   
/*      */   public Runnable wrapRunnable(Runnable runnable) {
/* 2726 */     return runnable;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean shouldRun(Runnable task) {
/* 2731 */     return true;
/*      */   }
/*      */   
/*      */   public BlockRenderDispatcher getBlockRenderer() {
/* 2735 */     return this.blockRenderer;
/*      */   }
/*      */   
/*      */   public EntityRenderDispatcher getEntityRenderDispatcher() {
/* 2739 */     return this.entityRenderDispatcher;
/*      */   }
/*      */   
/*      */   public BlockEntityRenderDispatcher getBlockEntityRenderDispatcher() {
/* 2743 */     return this.blockEntityRenderDispatcher;
/*      */   }
/*      */   
/*      */   public ItemRenderer getItemRenderer() {
/* 2747 */     return this.itemRenderer;
/*      */   }
/*      */   
/*      */   public MapRenderer getMapRenderer() {
/* 2751 */     return this.mapRenderer;
/*      */   }
/*      */   
/*      */   public DataFixer getFixerUpper() {
/* 2755 */     return this.fixerUpper;
/*      */   }
/*      */   
/*      */   public DeltaTracker getDeltaTracker() {
/* 2759 */     return this.deltaTracker;
/*      */   }
/*      */   
/*      */   public BlockColors getBlockColors() {
/* 2763 */     return this.blockColors;
/*      */   }
/*      */   
/*      */   public boolean showOnlyReducedInfo() {
/* 2767 */     return ((this.player != null && this.player.isReducedDebugInfo()) || (Boolean)this.options.reducedDebugInfo().get());
/*      */   }
/*      */   
/*      */   public ToastManager getToastManager() {
/* 2771 */     return this.toastManager;
/*      */   }
/*      */   
/*      */   public Tutorial getTutorial() {
/* 2775 */     return this.tutorial;
/*      */   }
/*      */   
/*      */   public boolean isWindowActive() {
/* 2779 */     return this.windowActive;
/*      */   }
/*      */   
/*      */   public HotbarManager getHotbarManager() {
/* 2783 */     return this.hotbarManager;
/*      */   }
/*      */   
/*      */   public ModelManager getModelManager() {
/* 2787 */     return this.modelManager;
/*      */   }
/*      */   
/*      */   public AtlasManager getAtlasManager() {
/* 2791 */     return this.atlasManager;
/*      */   }
/*      */   
/*      */   public MapTextureManager getMapTextureManager() {
/* 2795 */     return this.mapTextureManager;
/*      */   }
/*      */   
/*      */   public WaypointStyleManager getWaypointStyles() {
/* 2799 */     return this.waypointStyles;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWindowActive(boolean windowActive) {
/* 2804 */     this.windowActive = windowActive;
/*      */   }
/*      */   
/*      */   public Component grabPanoramixScreenshot(File folder) {
/* 2808 */     int downscaleFactor = 4;
/* 2809 */     int width = 4096;
/* 2810 */     int height = 4096;
/* 2811 */     int ow = this.window.getWidth();
/* 2812 */     int oh = this.window.getHeight();
/* 2813 */     RenderTarget target = getMainRenderTarget();
/*      */     
/* 2815 */     float xRot = this.player.getXRot();
/* 2816 */     float yRot = this.player.getYRot();
/* 2817 */     float xRotO = this.player.xRotO;
/* 2818 */     float yRotO = this.player.yRotO;
/* 2819 */     this.gameRenderer.setRenderBlockOutline(false);
/*      */     
/*      */     try {
/* 2822 */       this.gameRenderer.setPanoramicScreenshotParameters(new net.minecraft.client.renderer.PanoramicScreenshotParameters((Vector3fc)new Vector3f(
/* 2823 */               this.gameRenderer.getMainCamera().forwardVector())));
/*      */ 
/*      */       
/* 2826 */       this.window.setWidth(4096);
/* 2827 */       this.window.setHeight(4096);
/* 2828 */       target.resize(4096, 4096);
/* 2829 */       for (int i = 0; i < 6; i++) {
/* 2830 */         switch (i) {
/*      */           case 0:
/* 2832 */             this.player.setYRot(yRot);
/* 2833 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 1:
/* 2836 */             this.player.setYRot((yRot + 90.0F) % 360.0F);
/* 2837 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 2:
/* 2840 */             this.player.setYRot((yRot + 180.0F) % 360.0F);
/* 2841 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 3:
/* 2844 */             this.player.setYRot((yRot - 90.0F) % 360.0F);
/* 2845 */             this.player.setXRot(0.0F);
/*      */             break;
/*      */           case 4:
/* 2848 */             this.player.setYRot(yRot);
/* 2849 */             this.player.setXRot(-90.0F);
/*      */             break;
/*      */           
/*      */           default:
/* 2853 */             this.player.setYRot(yRot);
/* 2854 */             this.player.setXRot(90.0F);
/*      */             break;
/*      */         } 
/* 2857 */         this.player.yRotO = this.player.getYRot();
/* 2858 */         this.player.xRotO = this.player.getXRot();
/*      */         
/* 2860 */         this.gameRenderer.updateCamera(DeltaTracker.ONE);
/* 2861 */         this.gameRenderer.renderLevel(DeltaTracker.ONE);
/*      */         try {
/* 2863 */           Thread.sleep(10L);
/* 2864 */         } catch (InterruptedException interruptedException) {}
/*      */         
/* 2866 */         Screenshot.grab(folder, "panorama_" + i + ".png", target, 4, result -> {
/*      */             
/*      */             });
/* 2869 */       }  MutableComponent mutableComponent = Component.literal(folder.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(s -> s.withClickEvent((ClickEvent)new ClickEvent.OpenFile(folder.getAbsoluteFile())));
/*      */ 
/*      */       
/* 2872 */       return (Component)Component.translatable("screenshot.success", new Object[] { mutableComponent });
/* 2873 */     } catch (Exception e) {
/* 2874 */       LOGGER.error("Couldn't save image", e);
/* 2875 */       return (Component)Component.translatable("screenshot.failure", new Object[] { e.getMessage() });
/*      */     } finally {
/* 2877 */       this.player.setXRot(xRot);
/* 2878 */       this.player.setYRot(yRot);
/* 2879 */       this.player.xRotO = xRotO;
/* 2880 */       this.player.yRotO = yRotO;
/*      */       
/* 2882 */       this.gameRenderer.setRenderBlockOutline(true);
/*      */       
/* 2884 */       this.window.setWidth(ow);
/* 2885 */       this.window.setHeight(oh);
/* 2886 */       target.resize(ow, oh);
/*      */       
/* 2888 */       this.gameRenderer.setPanoramicScreenshotParameters(null);
/*      */     } 
/*      */   }
/*      */   
/*      */   public SplashManager getSplashManager() {
/* 2893 */     return this.splashManager;
/*      */   }
/*      */   
/*      */   public Overlay getOverlay() {
/* 2897 */     return this.overlay;
/*      */   }
/*      */   
/*      */   public PlayerSocialManager getPlayerSocialManager() {
/* 2901 */     return this.playerSocialManager;
/*      */   }
/*      */   
/*      */   public Window getWindow() {
/* 2905 */     return this.window;
/*      */   }
/*      */   
/*      */   public FramerateLimitTracker getFramerateLimitTracker() {
/* 2909 */     return this.framerateLimitTracker;
/*      */   }
/*      */   
/*      */   public net.minecraft.client.gui.components.DebugScreenOverlay getDebugOverlay() {
/* 2913 */     return this.gui.getDebugOverlay();
/*      */   }
/*      */   
/*      */   public RenderBuffers renderBuffers() {
/* 2917 */     return this.renderBuffers;
/*      */   }
/*      */   
/*      */   public void updateMaxMipLevel(int mipmapLevels) {
/* 2921 */     this.atlasManager.updateMaxMipLevel(mipmapLevels);
/*      */   }
/*      */   
/*      */   public EntityModelSet getEntityModels() {
/* 2925 */     return this.modelManager.entityModels().get();
/*      */   }
/*      */   
/*      */   public boolean isTextFilteringEnabled() {
/* 2929 */     return userProperties().flag(UserApiService.UserFlag.PROFANITY_FILTER_ENABLED);
/*      */   }
/*      */   
/*      */   public void prepareForMultiplayer() {
/* 2933 */     this.playerSocialManager.startOnlineMode();
/*      */     
/* 2935 */     getProfileKeyPairManager().prepareKeyPair();
/*      */   }
/*      */   
/*      */   public InputType getLastInputType() {
/* 2939 */     return this.lastInputType;
/*      */   }
/*      */   
/*      */   public void setLastInputType(InputType lastInputType) {
/* 2943 */     this.lastInputType = lastInputType;
/*      */   }
/*      */   
/*      */   public GameNarrator getNarrator() {
/* 2947 */     return this.narrator;
/*      */   }
/*      */   
/*      */   public ChatListener getChatListener() {
/* 2951 */     return this.chatListener;
/*      */   }
/*      */   
/*      */   public ReportingContext getReportingContext() {
/* 2955 */     return this.reportingContext;
/*      */   }
/*      */   
/*      */   public RealmsDataFetcher realmsDataFetcher() {
/* 2959 */     return this.realmsDataFetcher;
/*      */   }
/*      */   
/*      */   public QuickPlayLog quickPlayLog() {
/* 2963 */     return this.quickPlayLog;
/*      */   }
/*      */   
/*      */   public CommandHistory commandHistory() {
/* 2967 */     return this.commandHistory;
/*      */   }
/*      */   
/*      */   public DirectoryValidator directoryValidator() {
/* 2971 */     return this.directoryValidator;
/*      */   }
/*      */   
/*      */   public PlayerSkinRenderCache playerSkinRenderCache() {
/* 2975 */     return this.playerSkinRenderCache;
/*      */   }
/*      */   
/*      */   private float getTickTargetMillis(float defaultTickTargetMillis) {
/* 2979 */     if (this.level != null) {
/* 2980 */       TickRateManager manager = this.level.tickRateManager();
/* 2981 */       if (manager.runsNormally()) {
/* 2982 */         return Math.max(defaultTickTargetMillis, manager.millisecondsPerTick());
/*      */       }
/*      */     } 
/* 2985 */     return defaultTickTargetMillis;
/*      */   }
/*      */   
/*      */   public ItemModelResolver getItemModelResolver() {
/* 2989 */     return this.itemModelResolver;
/*      */   }
/*      */   
/*      */   public boolean canInterruptScreen() {
/* 2993 */     return ((this.screen == null || this.screen.canInterruptWithAnotherScreen()) && !this.clientLevelTeardownInProgress);
/*      */   }
/*      */   
/*      */   public enum ChatStatus {
/* 2997 */     ENABLED(CommonComponents.EMPTY)
/*      */     {
/*      */       public boolean isChatAllowed(boolean isLocalServer) {
/* 3000 */         return true;
/*      */       }
/*      */     },
/* 3003 */     DISABLED_BY_OPTIONS(Component.translatable("chat.disabled.options").withStyle(ChatFormatting.RED))
/*      */     {
/*      */       public boolean isChatAllowed(boolean isLocalServer) {
/* 3006 */         return false;
/*      */       }
/*      */     },
/* 3009 */     DISABLED_BY_LAUNCHER(Component.translatable("chat.disabled.launcher").withStyle(ChatFormatting.RED))
/*      */     {
/*      */       public boolean isChatAllowed(boolean isLocalServer) {
/* 3012 */         return isLocalServer;
/*      */       }
/*      */     },
/* 3015 */     DISABLED_BY_PROFILE(Component.translatable("chat.disabled.profile", new Object[] { Component.keybind(Minecraft.instance.options.keyChat.getName()) }).withStyle(ChatFormatting.RED))
/*      */     {
/*      */       public boolean isChatAllowed(boolean isLocalServer) {
/* 3018 */         return isLocalServer;
/*      */       }
/*      */     };
/*      */ 
/*      */     
/* 3023 */     private static final Component INFO_DISABLED_BY_PROFILE = (Component)Component.translatable("chat.disabled.profile.moreInfo");
/*      */     
/*      */     private final Component message;
/*      */     
/*      */     ChatStatus(Component message) {
/* 3028 */       this.message = message;
/*      */     }
/*      */     public abstract boolean isChatAllowed(boolean param1Boolean);
/*      */     
/* 3032 */     public Component getMessage() { return this.message; } } enum null {
/*      */     public boolean isChatAllowed(boolean isLocalServer) { return true; }
/*      */   } enum null { public boolean isChatAllowed(boolean isLocalServer) { return false; } } enum null {
/*      */     public boolean isChatAllowed(boolean isLocalServer) { return isLocalServer; }
/*      */   } enum null {
/*      */     public boolean isChatAllowed(boolean isLocalServer) { return isLocalServer; }
/* 3038 */   } private static final class GameLoadCookie extends Record { private final RealmsClient realmsClient; private GameLoadCookie(RealmsClient realmsClient, GameConfig.QuickPlayData quickPlayData) { this.realmsClient = realmsClient; this.quickPlayData = quickPlayData; } private final GameConfig.QuickPlayData quickPlayData; public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/Minecraft$GameLoadCookie;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3038	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/Minecraft$GameLoadCookie; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/Minecraft$GameLoadCookie;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3038	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/Minecraft$GameLoadCookie; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/Minecraft$GameLoadCookie;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3038	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/client/Minecraft$GameLoadCookie;
/* 3038 */       //   0	8	1	o	Ljava/lang/Object; } public RealmsClient realmsClient() { return this.realmsClient; } public GameConfig.QuickPlayData quickPlayData() { return this.quickPlayData; }
/*      */      }
/*      */   
/*      */   public static String getLauncherBrand() {
/* 3042 */     return System.getProperty("minecraft.launcher.brand");
/*      */   }
/*      */   
/*      */   public PacketProcessor packetProcessor() {
/* 3046 */     return this.packetProcessor;
/*      */   }
/*      */   
/*      */   public Gizmos.TemporaryCollection collectPerTickGizmos() {
/* 3050 */     return Gizmos.withCollector((GizmoCollector)this.perTickGizmos);
/*      */   }
/*      */   
/*      */   public Collection<SimpleGizmoCollector.GizmoInstance> getPerTickGizmos() {
/* 3054 */     return this.drainedLatestTickGizmos;
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/Minecraft.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */