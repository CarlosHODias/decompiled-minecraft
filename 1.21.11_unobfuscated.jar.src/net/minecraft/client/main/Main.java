/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.base.Ticker;
/*     */ import com.mojang.blaze3d.TracyBootstrap;
/*     */ import com.mojang.blaze3d.platform.DisplayData;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.io.File;
/*     */ import java.net.Authenticator;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.ClientBootstrap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.client.telemetry.TelemetryProperty;
/*     */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.util.NativeModuleLister;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.profiling.jfr.Environment;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import org.apache.commons.lang3.StringEscapeUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   static {
/*  55 */     System.setProperty("java.awt.headless", "true");
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     Logger logger;
/*     */     GameConfig gameConfig;
/*  61 */     OptionParser parser = new OptionParser();
/*  62 */     parser.allowsUnrecognizedOptions();
/*     */     
/*  64 */     parser.accepts("demo");
/*  65 */     parser.accepts("disableMultiplayer");
/*  66 */     parser.accepts("disableChat");
/*  67 */     parser.accepts("fullscreen");
/*  68 */     parser.accepts("checkGlErrors");
/*  69 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("renderDebugLabels");
/*  70 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("jfrProfile");
/*  71 */     OptionSpecBuilder optionSpecBuilder3 = parser.accepts("tracy");
/*  72 */     OptionSpecBuilder optionSpecBuilder4 = parser.accepts("tracyNoImages");
/*  73 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("quickPlayPath").withRequiredArg();
/*  74 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("quickPlaySingleplayer").withOptionalArg();
/*  75 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = parser.accepts("quickPlayMultiplayer").withRequiredArg();
/*  76 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = parser.accepts("quickPlayRealms").withRequiredArg();
/*  77 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = parser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), (Object[])new File[0]);
/*  78 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec6 = parser.accepts("assetsDir").withRequiredArg().ofType(File.class);
/*  79 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec7 = parser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
/*  80 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec8 = parser.accepts("proxyHost").withRequiredArg();
/*  81 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec9 = parser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", (Object[])new String[0]).ofType(Integer.class);
/*  82 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec10 = parser.accepts("proxyUser").withRequiredArg();
/*  83 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec11 = parser.accepts("proxyPass").withRequiredArg();
/*  84 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec12 = parser.accepts("username").withRequiredArg().defaultsTo("Player" + System.currentTimeMillis() % 1000L, (Object[])new String[0]);
/*  85 */     OptionSpecBuilder optionSpecBuilder5 = parser.accepts("offlineDeveloperMode");
/*  86 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec13 = parser.accepts("uuid").withRequiredArg();
/*  87 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec14 = parser.accepts("xuid").withOptionalArg().defaultsTo("", (Object[])new String[0]);
/*  88 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec15 = parser.accepts("clientId").withOptionalArg().defaultsTo("", (Object[])new String[0]);
/*  89 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec16 = parser.accepts("accessToken").withRequiredArg().required();
/*  90 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec17 = parser.accepts("version").withRequiredArg().required();
/*  91 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec18 = parser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854, (Object[])new Integer[0]);
/*  92 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec19 = parser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480, (Object[])new Integer[0]);
/*  93 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec20 = parser.accepts("fullscreenWidth").withRequiredArg().ofType(Integer.class);
/*  94 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec21 = parser.accepts("fullscreenHeight").withRequiredArg().ofType(Integer.class);
/*  95 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec22 = parser.accepts("assetIndex").withRequiredArg();
/*  96 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec23 = parser.accepts("versionType").withRequiredArg().defaultsTo("release", (Object[])new String[0]);
/*  97 */     NonOptionArgumentSpec nonOptionArgumentSpec = parser.nonOptions();
/*     */     
/*  99 */     OptionSet optionSet = parser.parse(args);
/*     */ 
/*     */     
/* 102 */     File gameDir = parseArgument(optionSet, (OptionSpec<File>)argumentAcceptingOptionSpec5);
/* 103 */     String launchedVersion = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec17);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     String stage = "Pre-bootstrap";
/*     */ 
/*     */     
/*     */     try {
/* 113 */       if (optionSet.has((OptionSpec)optionSpecBuilder2)) {
/* 114 */         JvmProfiler.INSTANCE.start(Environment.CLIENT);
/*     */       }
/* 116 */       if (optionSet.has((OptionSpec)optionSpecBuilder3)) {
/* 117 */         TracyBootstrap.setup();
/*     */       }
/*     */       
/* 120 */       Stopwatch totalTimePreClassLoadTimer = Stopwatch.createStarted(Ticker.systemTicker());
/* 121 */       Stopwatch preWindowPreClassLoadTimer = Stopwatch.createStarted(Ticker.systemTicker());
/* 122 */       GameLoadTimesEvent.INSTANCE.beginStep(TelemetryProperty.LOAD_TIME_TOTAL_TIME_MS, totalTimePreClassLoadTimer);
/* 123 */       GameLoadTimesEvent.INSTANCE.beginStep(TelemetryProperty.LOAD_TIME_PRE_WINDOW_MS, preWindowPreClassLoadTimer);
/*     */       
/* 125 */       SharedConstants.tryDetectVersion();
/* 126 */       TracyClient.reportAppInfo("Minecraft Java Edition " + SharedConstants.getCurrentVersion().name());
/* 127 */       CompletableFuture<?> dataFixerOptimization = DataFixers.optimize(DataFixTypes.TYPES_FOR_LEVEL_LIST);
/* 128 */       CrashReport.preload();
/*     */       
/* 130 */       logger = LogUtils.getLogger();
/*     */       
/* 132 */       stage = "Bootstrap";
/* 133 */       Bootstrap.bootStrap();
/* 134 */       ClientBootstrap.bootstrap();
/* 135 */       GameLoadTimesEvent.INSTANCE.setBootstrapTime(Bootstrap.bootstrapDuration.get());
/*     */       
/* 137 */       Bootstrap.validate();
/*     */       
/* 139 */       stage = "Argument parsing";
/*     */       
/* 141 */       List<String> leftoverArgs = optionSet.valuesOf((OptionSpec)nonOptionArgumentSpec);
/* 142 */       if (!leftoverArgs.isEmpty()) {
/* 143 */         logger.info("Completely ignored arguments: {}", leftoverArgs);
/*     */       }
/*     */ 
/*     */       
/* 147 */       String hostName = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec8);
/* 148 */       Proxy proxy = Proxy.NO_PROXY;
/* 149 */       if (hostName != null) {
/*     */         try {
/* 151 */           proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostName, (Integer)parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec9)));
/* 152 */         } catch (Exception exception) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 157 */       final String proxyUser = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec10);
/* 158 */       final String proxyPass = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec11);
/* 159 */       if (!proxy.equals(Proxy.NO_PROXY) && stringHasValue(proxyUser) && stringHasValue(proxyPass)) {
/* 160 */         Authenticator.setDefault(new Authenticator()
/*     */             {
/*     */               protected PasswordAuthentication getPasswordAuthentication() {
/* 163 */                 return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
/*     */               }
/*     */             });
/*     */       }
/*     */       
/* 168 */       int width = (Integer)parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec18);
/* 169 */       int height = (Integer)parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec19);
/* 170 */       OptionalInt fullscreenWidth = ofNullable(parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec20));
/* 171 */       OptionalInt fullscreenHeight = ofNullable(parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec21));
/* 172 */       boolean isFullscreen = optionSet.has("fullscreen");
/* 173 */       boolean isDemo = optionSet.has("demo");
/* 174 */       boolean disableMultiplayer = optionSet.has("disableMultiplayer");
/* 175 */       boolean disableChat = optionSet.has("disableChat");
/* 176 */       boolean captureTracyImages = !optionSet.has((OptionSpec)optionSpecBuilder4);
/* 177 */       boolean renderDebugLabels = optionSet.has((OptionSpec)optionSpecBuilder1);
/* 178 */       String versionType = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec23);
/*     */ 
/*     */       
/* 181 */       File assetsDir = optionSet.has((OptionSpec)argumentAcceptingOptionSpec6) ? parseArgument(optionSet, (OptionSpec<File>)argumentAcceptingOptionSpec6) : new File(gameDir, "assets/");
/* 182 */       File resourcePackDir = optionSet.has((OptionSpec)argumentAcceptingOptionSpec7) ? parseArgument(optionSet, (OptionSpec<File>)argumentAcceptingOptionSpec7) : new File(gameDir, "resourcepacks/");
/* 183 */       UUID uuid = hasValidUuid((OptionSpec<String>)argumentAcceptingOptionSpec13, optionSet, logger) ? UndashedUuid.fromStringLenient((String)argumentAcceptingOptionSpec13.value(optionSet)) : UUIDUtil.createOfflinePlayerUUID((String)argumentAcceptingOptionSpec12.value(optionSet));
/* 184 */       String assetIndex = optionSet.has((OptionSpec)argumentAcceptingOptionSpec22) ? (String)argumentAcceptingOptionSpec22.value(optionSet) : null;
/* 185 */       String xuid = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec14);
/* 186 */       String clientId = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec15);
/*     */       
/* 188 */       String quickPlayLogPath = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec1);
/* 189 */       GameConfig.QuickPlayVariant quickPlayVariant = getQuickPlayVariant(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec2, (OptionSpec<String>)argumentAcceptingOptionSpec3, (OptionSpec<String>)argumentAcceptingOptionSpec4);
/*     */       
/* 191 */       User user = new User((String)argumentAcceptingOptionSpec12.value(optionSet), uuid, (String)argumentAcceptingOptionSpec16.value(optionSet), emptyStringToEmptyOptional(xuid), emptyStringToEmptyOptional(clientId));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       gameConfig = new GameConfig(new GameConfig.UserData(user, proxy), new DisplayData(width, height, fullscreenWidth, fullscreenHeight, isFullscreen), new GameConfig.FolderData(gameDir, resourcePackDir, assetsDir, assetIndex), new GameConfig.GameData(isDemo, launchedVersion, versionType, disableMultiplayer, disableChat, captureTracyImages, renderDebugLabels, optionSet.has((OptionSpec)optionSpecBuilder5)), new GameConfig.QuickPlayData(quickPlayLogPath, quickPlayVariant));
/*     */ 
/*     */       
/* 199 */       Util.startTimerHackThread();
/* 200 */       dataFixerOptimization.join();
/* 201 */     } catch (Throwable t) {
/* 202 */       CrashReport report = CrashReport.forThrowable(t, stage);
/* 203 */       CrashReportCategory initialization = report.addCategory("Initialization");
/* 204 */       NativeModuleLister.addCrashSection(initialization);
/* 205 */       Minecraft.fillReport(null, null, launchedVersion, null, report);
/* 206 */       Minecraft.crash(null, gameDir, report);
/*     */       
/*     */       return;
/*     */     } 
/* 210 */     Thread shutdownThread = new Thread("Client Shutdown Thread")
/*     */       {
/*     */         public void run() {
/* 213 */           Minecraft instance = Minecraft.getInstance();
/* 214 */           if (instance == null) {
/*     */             return;
/*     */           }
/*     */           
/* 218 */           IntegratedServer server = instance.getSingleplayerServer();
/* 219 */           if (server != null) {
/* 220 */             server.halt(true);
/*     */           }
/*     */         }
/*     */       };
/* 224 */     shutdownThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(logger));
/* 225 */     Runtime.getRuntime().addShutdownHook(shutdownThread);
/*     */     
/* 227 */     Minecraft newMinecraft = null;
/*     */     
/*     */     try {
/* 230 */       Thread.currentThread().setName("Render thread");
/* 231 */       RenderSystem.initRenderThread();
/* 232 */       newMinecraft = new Minecraft(gameConfig);
/* 233 */     } catch (SilentInitException e) {
/* 234 */       Util.shutdownExecutors();
/* 235 */       logger.warn("Failed to create window: ", e);
/*     */       return;
/* 237 */     } catch (Throwable t) {
/* 238 */       CrashReport report = CrashReport.forThrowable(t, "Initializing game");
/* 239 */       CrashReportCategory initialization = report.addCategory("Initialization");
/* 240 */       NativeModuleLister.addCrashSection(initialization);
/* 241 */       Minecraft.fillReport(newMinecraft, null, gameConfig.game.launchVersion, null, report);
/* 242 */       Minecraft.crash(newMinecraft, gameConfig.location.gameDirectory, report);
/*     */       
/*     */       return;
/*     */     } 
/* 246 */     Minecraft minecraft = newMinecraft;
/*     */     
/* 248 */     minecraft.run();
/*     */     
/*     */     try {
/* 251 */       minecraft.stop();
/*     */     } finally {
/* 253 */       minecraft.destroy();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static GameConfig.QuickPlayVariant getQuickPlayVariant(OptionSet optionSet, OptionSpec<String> quickPlaySingleplayerOption, OptionSpec<String> quickPlayMultiplayerOption, OptionSpec<String> quickPlayRealmsOption) {
/* 259 */     Objects.requireNonNull(optionSet); long enabledOptions = Stream.<OptionSpec>of(new OptionSpec[] { quickPlaySingleplayerOption, quickPlayMultiplayerOption, quickPlayRealmsOption }).filter(optionSet::has)
/* 260 */       .count();
/*     */     
/* 262 */     if (enabledOptions == 0L) {
/* 263 */       return GameConfig.QuickPlayVariant.DISABLED;
/*     */     }
/*     */     
/* 266 */     if (enabledOptions > 1L) {
/* 267 */       throw new IllegalArgumentException("Only one quick play option can be specified");
/*     */     }
/*     */     
/* 270 */     if (optionSet.has(quickPlaySingleplayerOption)) {
/* 271 */       String worldId = unescapeJavaArgument(parseArgument(optionSet, quickPlaySingleplayerOption));
/* 272 */       return new GameConfig.QuickPlaySinglePlayerData(worldId);
/*     */     } 
/*     */     
/* 275 */     if (optionSet.has(quickPlayMultiplayerOption)) {
/* 276 */       String serverAddress = unescapeJavaArgument(parseArgument(optionSet, quickPlayMultiplayerOption));
/* 277 */       return (GameConfig.QuickPlayVariant)Optionull.mapOrDefault(serverAddress, QuickPlayMultiplayerData::new, GameConfig.QuickPlayVariant.DISABLED);
/*     */     } 
/*     */     
/* 280 */     if (optionSet.has(quickPlayRealmsOption)) {
/* 281 */       String realmId = unescapeJavaArgument(parseArgument(optionSet, quickPlayRealmsOption));
/* 282 */       return (GameConfig.QuickPlayVariant)Optionull.mapOrDefault(realmId, QuickPlayRealmsData::new, GameConfig.QuickPlayVariant.DISABLED);
/*     */     } 
/*     */     
/* 285 */     return GameConfig.QuickPlayVariant.DISABLED;
/*     */   }
/*     */   
/*     */   private static String unescapeJavaArgument(String arg) {
/* 289 */     if (arg == null) {
/* 290 */       return null;
/*     */     }
/* 292 */     return StringEscapeUtils.unescapeJava(arg);
/*     */   }
/*     */   
/*     */   private static Optional<String> emptyStringToEmptyOptional(String xuid) {
/* 296 */     return xuid.isEmpty() ? Optional.<String>empty() : Optional.<String>of(xuid);
/*     */   }
/*     */   
/*     */   private static OptionalInt ofNullable(Integer value) {
/* 300 */     return (value != null) ? OptionalInt.of(value) : OptionalInt.empty();
/*     */   }
/*     */   
/*     */   private static <T> T parseArgument(OptionSet optionSet, OptionSpec<T> optionSpec) {
/*     */     try {
/* 305 */       return (T)optionSet.valueOf(optionSpec);
/* 306 */     } catch (Throwable t) {
/* 307 */       if (optionSpec instanceof ArgumentAcceptingOptionSpec) { ArgumentAcceptingOptionSpec<T> options = (ArgumentAcceptingOptionSpec<T>)optionSpec;
/* 308 */         List<T> defaultValues = options.defaultValues();
/* 309 */         if (!defaultValues.isEmpty()) {
/* 310 */           return defaultValues.get(0);
/*     */         } }
/*     */       
/* 313 */       throw t;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean stringHasValue(String string) {
/* 318 */     return (string != null && !string.isEmpty());
/*     */   }
/*     */   
/*     */   private static boolean hasValidUuid(OptionSpec<String> uuidOption, OptionSet optionSet, Logger logger) {
/* 322 */     return (optionSet.has(uuidOption) && isUuidValid(uuidOption, optionSet, logger));
/*     */   }
/*     */   
/*     */   private static boolean isUuidValid(OptionSpec<String> uuidOption, OptionSet optionSet, Logger logger) {
/*     */     try {
/* 327 */       UndashedUuid.fromStringLenient((String)uuidOption.value(optionSet));
/* 328 */     } catch (IllegalArgumentException e) {
/* 329 */       logger.warn("Invalid UUID: '{}", uuidOption.value(optionSet));
/* 330 */       return false;
/*     */     } 
/* 332 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/main/Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */