/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.authlib.minecraft.TelemetrySession;
/*     */ import com.mojang.authlib.minecraft.UserApiService;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class ClientTelemetryManager implements AutoCloseable {
/*  24 */   private static final AtomicInteger THREAD_COUNT = new AtomicInteger(1); static {
/*  25 */     EXECUTOR = Executors.newSingleThreadExecutor(r -> {
/*     */           Thread result = new Thread(r);
/*     */           result.setName("Telemetry-Sender-#" + THREAD_COUNT.getAndIncrement());
/*     */           return result;
/*     */         });
/*     */   }
/*     */   private static final Executor EXECUTOR;
/*     */   private final Minecraft minecraft;
/*     */   private final UserApiService userApiService;
/*     */   private final TelemetryPropertyMap deviceSessionProperties;
/*     */   private final Path logDirectory;
/*     */   private final CompletableFuture<Optional<TelemetryLogManager>> logManager;
/*  37 */   private final Supplier<TelemetryEventSender> outsideSessionSender = (Supplier<TelemetryEventSender>)Suppliers.memoize(this::createEventSender);
/*     */   
/*     */   public ClientTelemetryManager(Minecraft minecraft, UserApiService userApiService, User user) {
/*  40 */     this.minecraft = minecraft;
/*  41 */     this.userApiService = userApiService;
/*     */     
/*  43 */     TelemetryPropertyMap.Builder properties = TelemetryPropertyMap.builder();
/*  44 */     user.getXuid().ifPresent(id -> properties.put(TelemetryProperty.USER_ID, id));
/*  45 */     user.getClientId().ifPresent(id -> properties.put(TelemetryProperty.CLIENT_ID, id));
/*  46 */     properties.put(TelemetryProperty.MINECRAFT_SESSION_ID, UUID.randomUUID());
/*  47 */     properties.put(TelemetryProperty.GAME_VERSION, SharedConstants.getCurrentVersion().id());
/*  48 */     properties.put(TelemetryProperty.OPERATING_SYSTEM, Util.getPlatform().telemetryName());
/*  49 */     properties.put(TelemetryProperty.PLATFORM, System.getProperty("os.name"));
/*  50 */     properties.put(TelemetryProperty.CLIENT_MODDED, Minecraft.checkModStatus().shouldReportAsModified());
/*  51 */     properties.putIfNotNull(TelemetryProperty.LAUNCHER_NAME, Minecraft.getLauncherBrand());
/*  52 */     this.deviceSessionProperties = properties.build();
/*     */     
/*  54 */     this.logDirectory = minecraft.gameDirectory.toPath().resolve("logs/telemetry");
/*  55 */     this.logManager = TelemetryLogManager.open(this.logDirectory);
/*     */   }
/*     */   
/*     */   public WorldSessionTelemetryManager createWorldSessionManager(boolean newWorld, Duration worldLoadDuration, String minigameName) {
/*  59 */     return new WorldSessionTelemetryManager(createEventSender(), newWorld, worldLoadDuration, minigameName);
/*     */   }
/*     */   
/*     */   public TelemetryEventSender getOutsideSessionSender() {
/*  63 */     return this.outsideSessionSender.get();
/*     */   }
/*     */   
/*     */   private TelemetryEventSender createEventSender() {
/*  67 */     if (!this.minecraft.allowsTelemetry()) {
/*  68 */       return TelemetryEventSender.DISABLED;
/*     */     }
/*     */     
/*  71 */     TelemetrySession telemetrySession = this.userApiService.newTelemetrySession(EXECUTOR);
/*  72 */     if (!telemetrySession.isEnabled()) {
/*  73 */       return TelemetryEventSender.DISABLED;
/*     */     }
/*     */     
/*  76 */     CompletableFuture<Optional<TelemetryEventLogger>> loggerFuture = this.logManager.thenCompose(manager -> (CompletionStage)manager.map(TelemetryLogManager::openLogger).orElseGet(()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     return (type, buildFunction) -> {
/*     */         if (loggerFuture.isOptIn() && !Minecraft.getInstance().telemetryOptInExtra()) {
/*     */           return;
/*     */         }
/*     */         TelemetryPropertyMap.Builder properties = TelemetryPropertyMap.builder();
/*     */         properties.putAll(this.deviceSessionProperties);
/*     */         properties.put(TelemetryProperty.EVENT_TIMESTAMP_UTC, Instant.now());
/*     */         properties.put(TelemetryProperty.OPT_IN, loggerFuture.isOptIn());
/*     */         buildFunction.accept(properties);
/*     */         TelemetryEventInstance event = new TelemetryEventInstance(loggerFuture, properties.build());
/*     */         loggerFuture.thenAccept(());
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getLogDirectory() {
/* 109 */     return this.logDirectory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 114 */     this.logManager.thenAccept(manager -> manager.ifPresent(TelemetryLogManager::close));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/ClientTelemetryManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */