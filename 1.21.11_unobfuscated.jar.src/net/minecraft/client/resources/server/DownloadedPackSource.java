/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.Unit;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.main.GameConfig;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.DownloadQueue;
/*     */ import net.minecraft.server.packs.FilePackResources;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.server.packs.repository.RepositorySource;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DownloadedPackSource
/*     */   implements AutoCloseable
/*     */ {
/*  50 */   private static final Component SERVER_NAME = (Component)Component.translatable("resourcePack.server.name");
/*     */   
/*  52 */   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
/*     */   
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final RepositorySource EMPTY_SOURCE = result -> {
/*     */     
/*     */     };
/*  57 */   private static final PackSelectionConfig DOWNLOADED_PACK_SELECTION = new PackSelectionConfig(true, Pack.Position.TOP, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static final PackLoadFeedback LOG_ONLY_FEEDBACK = new PackLoadFeedback()
/*     */     {
/*     */       public void reportUpdate(UUID id, PackLoadFeedback.Update update) {
/*  66 */         DownloadedPackSource.LOGGER.debug("Downloaded pack {} changed state to {}", id, update);
/*     */       }
/*     */ 
/*     */       
/*     */       public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/*  71 */         DownloadedPackSource.LOGGER.debug("Downloaded pack {} finished with state {}", id, result);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private final Minecraft minecraft;
/*  77 */   private RepositorySource packSource = EMPTY_SOURCE;
/*     */   
/*     */   private PackReloadConfig.Callbacks pendingReload;
/*     */   
/*     */   private final ServerPackManager manager;
/*     */   
/*     */   private final DownloadQueue downloadQueue;
/*  84 */   private PackSource packType = PackSource.SERVER;
/*  85 */   private PackLoadFeedback packFeedback = LOG_ONLY_FEEDBACK;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int packIdSerialNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DownloadedPackSource(Minecraft minecraft, Path packCache, GameConfig.UserData user) {
/*  96 */     this.minecraft = minecraft;
/*     */     try {
/*  98 */       this.downloadQueue = new DownloadQueue(packCache);
/*  99 */     } catch (IOException e) {
/* 100 */       throw new UncheckedIOException("Failed to open download queue in directory " + String.valueOf(packCache), e);
/*     */     } 
/*     */     
/* 103 */     Objects.requireNonNull(minecraft); Executor executor = minecraft::schedule;
/* 104 */     this
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
/* 119 */       .manager = new ServerPackManager(createDownloader(this.downloadQueue, executor, user.user, user.proxy), new PackLoadFeedback() { public void reportUpdate(UUID id, PackLoadFeedback.Update result) { DownloadedPackSource.this.packFeedback.reportUpdate(id, result); } public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) { DownloadedPackSource.this.packFeedback.reportFinalResult(id, result); } }, createReloadConfig(), createUpdateScheduler(executor), ServerPackManager.PackPromptStatus.PENDING);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpUtil.DownloadProgressListener createDownloadNotifier(final int totalCount) {
/* 125 */     return new HttpUtil.DownloadProgressListener() { private final SystemToast.SystemToastId toastId; private Component title; private Component message; private int count; private int failCount; private OptionalLong totalBytes;
/*     */         {
/* 127 */           this.toastId = new SystemToast.SystemToastId();
/* 128 */           this.title = (Component)Component.empty();
/* 129 */           this.message = null;
/*     */ 
/*     */           
/* 132 */           this.totalBytes = OptionalLong.empty();
/*     */         }
/*     */         private void updateToast() {
/* 135 */           DownloadedPackSource.this.minecraft.execute(() -> SystemToast.addOrUpdate(DownloadedPackSource.this.minecraft.getToastManager(), this.toastId, this.title, this.message));
/*     */         }
/*     */         
/*     */         private void updateProgress(long bytesSoFar) {
/* 139 */           if (this.totalBytes.isPresent()) {
/* 140 */             this.message = (Component)Component.translatable("download.pack.progress.percent", new Object[] { bytesSoFar * 100L / this.totalBytes.getAsLong() });
/*     */           } else {
/* 142 */             this.message = (Component)Component.translatable("download.pack.progress.bytes", new Object[] { Unit.humanReadable(bytesSoFar) });
/*     */           } 
/* 144 */           updateToast();
/*     */         }
/*     */ 
/*     */         
/*     */         public void requestStart() {
/* 149 */           this.count++;
/* 150 */           this.title = (Component)Component.translatable("download.pack.title", new Object[] { this.count, totalCount });
/* 151 */           updateToast();
/* 152 */           DownloadedPackSource.LOGGER.debug("Starting pack {}/{} download", this.count, totalCount);
/*     */         }
/*     */ 
/*     */         
/*     */         public void downloadStart(OptionalLong sizeBytes) {
/* 157 */           DownloadedPackSource.LOGGER.debug("File size = {} bytes", sizeBytes);
/* 158 */           this.totalBytes = sizeBytes;
/* 159 */           updateProgress(0L);
/*     */         }
/*     */ 
/*     */         
/*     */         public void downloadedBytes(long bytesSoFar) {
/* 164 */           DownloadedPackSource.LOGGER.debug("Progress for pack {}: {} bytes", this.count, bytesSoFar);
/* 165 */           updateProgress(bytesSoFar);
/*     */         }
/*     */ 
/*     */         
/*     */         public void requestFinished(boolean success) {
/* 170 */           if (!success) {
/* 171 */             DownloadedPackSource.LOGGER.info("Pack {} failed to download", this.count);
/* 172 */             this.failCount++;
/*     */           } else {
/* 174 */             DownloadedPackSource.LOGGER.debug("Download ended for pack {}", this.count);
/*     */           } 
/*     */           
/* 177 */           if (this.count == totalCount) {
/* 178 */             if (this.failCount > 0) {
/* 179 */               this.title = (Component)Component.translatable("download.pack.failed", new Object[] { this.failCount, totalCount });
/* 180 */               this.message = null;
/* 181 */               updateToast();
/*     */             } else {
/* 183 */               SystemToast.forceHide(DownloadedPackSource.this.minecraft.getToastManager(), this.toastId);
/*     */             } 
/*     */           }
/*     */         } }
/*     */       ;
/*     */   }
/*     */   
/*     */   private PackDownloader createDownloader(final DownloadQueue downloadQueue, final Executor mainThreadExecutor, final User user, final Proxy proxy) {
/* 191 */     return new PackDownloader()
/*     */       {
/*     */         private static final int MAX_PACK_SIZE_BYTES = 262144000;
/* 194 */         private static final HashFunction CACHE_HASHING_FUNCTION = Hashing.sha1();
/*     */         
/*     */         private Map<String, String> createDownloadHeaders() {
/* 197 */           WorldVersion version = SharedConstants.getCurrentVersion();
/* 198 */           return Map.of("X-Minecraft-Username", 
/* 199 */               user.getName(), "X-Minecraft-UUID", 
/* 200 */               UndashedUuid.toString(user.getProfileId()), "X-Minecraft-Version", 
/* 201 */               version.name(), "X-Minecraft-Version-ID", 
/* 202 */               version.id(), "X-Minecraft-Pack-Format", 
/* 203 */               String.valueOf(version.packVersion(PackType.CLIENT_RESOURCES)), "User-Agent", "Minecraft Java/" + 
/* 204 */               version.name());
/*     */         }
/*     */ 
/*     */         
/*     */         public void download(Map<UUID, DownloadQueue.DownloadRequest> requests, Consumer<DownloadQueue.BatchResult> output) {
/* 209 */           downloadQueue.downloadBatch(new DownloadQueue.BatchConfig(CACHE_HASHING_FUNCTION, 262144000, 
/*     */ 
/*     */ 
/*     */                 
/* 213 */                 createDownloadHeaders(), proxy, 
/*     */                 
/* 215 */                 DownloadedPackSource.this.createDownloadNotifier(requests.size())), requests)
/*     */ 
/*     */             
/* 218 */             .thenAcceptAsync(output, mainThreadExecutor);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Runnable createUpdateScheduler(final Executor mainThreadExecutor) {
/* 224 */     return new Runnable()
/*     */       {
/*     */         private boolean scheduledInMainExecutor;
/*     */         private boolean hasUpdates;
/*     */         
/*     */         public void run() {
/* 230 */           this.hasUpdates = true;
/* 231 */           if (!this.scheduledInMainExecutor) {
/* 232 */             this.scheduledInMainExecutor = true;
/* 233 */             mainThreadExecutor.execute(this::runAllUpdates);
/*     */           } 
/*     */         }
/*     */         
/*     */         private void runAllUpdates() {
/* 238 */           while (this.hasUpdates) {
/* 239 */             this.hasUpdates = false;
/* 240 */             DownloadedPackSource.this.manager.tick();
/*     */           } 
/* 242 */           this.scheduledInMainExecutor = false;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private PackReloadConfig createReloadConfig() {
/* 248 */     return this::startReload;
/*     */   }
/*     */   
/*     */   private List<Pack> loadRequestedPacks(List<PackReloadConfig.IdAndPath> packsToLoad) {
/* 252 */     List<Pack> packs = new ArrayList<>(packsToLoad.size());
/*     */     
/* 254 */     for (PackReloadConfig.IdAndPath idAndPath : (Iterable<PackReloadConfig.IdAndPath>)Lists.reverse(packsToLoad)) {
/* 255 */       String name = String.format(Locale.ROOT, "server/%08X/%s", new Object[] { this.packIdSerialNumber++, idAndPath.id() });
/* 256 */       Path path = idAndPath.path();
/*     */       
/* 258 */       PackLocationInfo packLocationInfo = new PackLocationInfo(name, SERVER_NAME, this.packType, 
/*     */ 
/*     */ 
/*     */           
/* 262 */           Optional.empty());
/*     */ 
/*     */       
/* 265 */       FilePackResources.FileResourcesSupplier fileResourcesSupplier = new FilePackResources.FileResourcesSupplier(path);
/*     */       
/* 267 */       PackFormat currentPackVersion = SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES);
/* 268 */       Pack.Metadata metadata = Pack.readPackMetadata(packLocationInfo, (Pack.ResourcesSupplier)fileResourcesSupplier, currentPackVersion, PackType.CLIENT_RESOURCES);
/* 269 */       if (metadata == null) {
/* 270 */         LOGGER.warn("Invalid pack metadata in {}, ignoring all", path);
/* 271 */         return null;
/*     */       } 
/* 273 */       packs.add(new Pack(packLocationInfo, (Pack.ResourcesSupplier)fileResourcesSupplier, metadata, DOWNLOADED_PACK_SELECTION));
/*     */     } 
/*     */     
/* 276 */     return packs;
/*     */   }
/*     */ 
/*     */   
/*     */   public RepositorySource createRepositorySource() {
/* 281 */     return output -> this.packSource.loadPacks(output);
/*     */   }
/*     */   
/*     */   private static RepositorySource configureSource(List<Pack> packs) {
/* 285 */     if (packs.isEmpty()) {
/* 286 */       return EMPTY_SOURCE;
/*     */     }
/* 288 */     Objects.requireNonNull(packs); return packs::forEach;
/*     */   }
/*     */   
/*     */   private void startReload(PackReloadConfig.Callbacks callbacks) {
/* 292 */     this.pendingReload = callbacks;
/* 293 */     List<PackReloadConfig.IdAndPath> normalPacks = callbacks.packsToLoad();
/* 294 */     List<Pack> packs = loadRequestedPacks(normalPacks);
/* 295 */     if (packs == null) {
/*     */       
/* 297 */       callbacks.onFailure(false);
/* 298 */       List<PackReloadConfig.IdAndPath> recoveryPacks = callbacks.packsToLoad();
/*     */       
/* 300 */       packs = loadRequestedPacks(recoveryPacks);
/* 301 */       if (packs == null) {
/* 302 */         LOGGER.warn("Double failure in loading server packs");
/* 303 */         packs = List.of();
/*     */       } 
/*     */     } 
/*     */     
/* 307 */     this.packSource = configureSource(packs);
/* 308 */     this.minecraft.reloadResourcePacks();
/*     */   }
/*     */   
/*     */   public void onRecovery() {
/* 312 */     if (this.pendingReload != null) {
/* 313 */       this.pendingReload.onFailure(false);
/* 314 */       List<Pack> packs = loadRequestedPacks(this.pendingReload.packsToLoad());
/* 315 */       if (packs == null) {
/* 316 */         LOGGER.warn("Double failure in loading server packs");
/* 317 */         packs = List.of();
/*     */       } 
/* 319 */       this.packSource = configureSource(packs);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onRecoveryFailure() {
/* 324 */     if (this.pendingReload != null) {
/* 325 */       this.pendingReload.onFailure(true);
/* 326 */       this.pendingReload = null;
/* 327 */       this.packSource = EMPTY_SOURCE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onReloadSuccess() {
/* 333 */     if (this.pendingReload != null) {
/* 334 */       this.pendingReload.onSuccess();
/* 335 */       this.pendingReload = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashCode tryParseSha1Hash(String hash) {
/* 341 */     if (hash != null && SHA1.matcher(hash).matches()) {
/* 342 */       return HashCode.fromString(hash.toLowerCase(Locale.ROOT));
/*     */     }
/* 344 */     return null;
/*     */   }
/*     */   
/*     */   public void pushPack(UUID id, URL url, String hash) {
/* 348 */     HashCode parsedHash = tryParseSha1Hash(hash);
/* 349 */     this.manager.pushPack(id, url, parsedHash);
/*     */   }
/*     */   
/*     */   public void pushLocalPack(UUID id, Path path) {
/* 353 */     this.manager.pushLocalPack(id, path);
/*     */   }
/*     */   
/*     */   public void popPack(UUID id) {
/* 357 */     this.manager.popPack(id);
/*     */   }
/*     */   
/*     */   public void popAll() {
/* 361 */     this.manager.popAll();
/*     */   }
/*     */   
/*     */   private static PackLoadFeedback createPackResponseSender(final Connection connection) {
/* 365 */     return new PackLoadFeedback()
/*     */       {
/*     */         public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/*     */           // Byte code:
/*     */           //   0: getstatic net/minecraft/client/resources/server/DownloadedPackSource.LOGGER : Lorg/slf4j/Logger;
/*     */           //   3: ldc 'Pack {} changed status to {}'
/*     */           //   5: aload_1
/*     */           //   6: aload_2
/*     */           //   7: invokeinterface debug : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
/*     */           //   12: getstatic net/minecraft/client/resources/server/DownloadedPackSource$8.$SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$Update : [I
/*     */           //   15: aload_2
/*     */           //   16: invokevirtual ordinal : ()I
/*     */           //   19: iaload
/*     */           //   20: lookupswitch default -> 48, 1 -> 58, 2 -> 64
/*     */           //   48: new java/lang/MatchException
/*     */           //   51: dup
/*     */           //   52: aconst_null
/*     */           //   53: aconst_null
/*     */           //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   57: athrow
/*     */           //   58: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.ACCEPTED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   61: goto -> 67
/*     */           //   64: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.DOWNLOADED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   67: astore_3
/*     */           //   68: aload_0
/*     */           //   69: getfield val$connection : Lnet/minecraft/network/Connection;
/*     */           //   72: new net/minecraft/network/protocol/common/ServerboundResourcePackPacket
/*     */           //   75: dup
/*     */           //   76: aload_1
/*     */           //   77: aload_3
/*     */           //   78: invokespecial <init> : (Ljava/util/UUID;Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;)V
/*     */           //   81: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */           //   84: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #368	-> 0
/*     */           //   #369	-> 12
/*     */           //   #370	-> 58
/*     */           //   #371	-> 64
/*     */           //   #373	-> 68
/*     */           //   #374	-> 84
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	85	0	this	Lnet/minecraft/client/resources/server/DownloadedPackSource$6;
/*     */           //   0	85	1	id	Ljava/util/UUID;
/*     */           //   0	85	2	result	Lnet/minecraft/client/resources/server/PackLoadFeedback$Update;
/*     */           //   68	17	3	response	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/*     */           // Byte code:
/*     */           //   0: getstatic net/minecraft/client/resources/server/DownloadedPackSource.LOGGER : Lorg/slf4j/Logger;
/*     */           //   3: ldc 'Pack {} changed status to {}'
/*     */           //   5: aload_1
/*     */           //   6: aload_2
/*     */           //   7: invokeinterface debug : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
/*     */           //   12: getstatic net/minecraft/client/resources/server/DownloadedPackSource$8.$SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult : [I
/*     */           //   15: aload_2
/*     */           //   16: invokevirtual ordinal : ()I
/*     */           //   19: iaload
/*     */           //   20: tableswitch default -> 56, 1 -> 66, 2 -> 72, 3 -> 78, 4 -> 84, 5 -> 90
/*     */           //   56: new java/lang/MatchException
/*     */           //   59: dup
/*     */           //   60: aconst_null
/*     */           //   61: aconst_null
/*     */           //   62: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   65: athrow
/*     */           //   66: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.SUCCESSFULLY_LOADED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   69: goto -> 93
/*     */           //   72: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.FAILED_DOWNLOAD : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   75: goto -> 93
/*     */           //   78: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.DECLINED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   81: goto -> 93
/*     */           //   84: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.DISCARDED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   87: goto -> 93
/*     */           //   90: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.FAILED_RELOAD : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   93: astore_3
/*     */           //   94: aload_0
/*     */           //   95: getfield val$connection : Lnet/minecraft/network/Connection;
/*     */           //   98: new net/minecraft/network/protocol/common/ServerboundResourcePackPacket
/*     */           //   101: dup
/*     */           //   102: aload_1
/*     */           //   103: aload_3
/*     */           //   104: invokespecial <init> : (Ljava/util/UUID;Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;)V
/*     */           //   107: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */           //   110: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #378	-> 0
/*     */           //   #379	-> 12
/*     */           //   #380	-> 66
/*     */           //   #381	-> 72
/*     */           //   #382	-> 78
/*     */           //   #383	-> 84
/*     */           //   #384	-> 90
/*     */           //   #386	-> 94
/*     */           //   #387	-> 110
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	111	0	this	Lnet/minecraft/client/resources/server/DownloadedPackSource$6;
/*     */           //   0	111	1	id	Ljava/util/UUID;
/*     */           //   0	111	2	result	Lnet/minecraft/client/resources/server/PackLoadFeedback$FinalResult;
/*     */           //   94	17	3	response	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */         }
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
/*     */   public void configureForServerControl(Connection connection, ServerPackManager.PackPromptStatus packPromptStatus) {
/* 392 */     this.packType = PackSource.SERVER;
/* 393 */     this.packFeedback = createPackResponseSender(connection);
/* 394 */     switch (packPromptStatus) { case ALLOWED:
/* 395 */         this.manager.allowServerPacks(); break;
/* 396 */       case DECLINED: this.manager.rejectServerPacks(); break;
/* 397 */       case PENDING: this.manager.resetPromptStatus();
/*     */         break; }
/*     */   
/*     */   }
/*     */   public void configureForLocalWorld() {
/* 402 */     this.packType = PackSource.WORLD;
/* 403 */     this.packFeedback = LOG_ONLY_FEEDBACK;
/* 404 */     this.manager.allowServerPacks();
/*     */   }
/*     */   
/*     */   public void allowServerPacks() {
/* 408 */     this.manager.allowServerPacks();
/*     */   }
/*     */   
/*     */   public void rejectServerPacks() {
/* 412 */     this.manager.rejectServerPacks();
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> waitForPackFeedback(final UUID packId) {
/* 416 */     final CompletableFuture<Void> result = new CompletableFuture<>();
/* 417 */     final PackLoadFeedback original = this.packFeedback;
/* 418 */     this.packFeedback = new PackLoadFeedback()
/*     */       {
/*     */         public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/* 421 */           original.reportUpdate(id, result);
/*     */         }
/*     */ 
/*     */         
/*     */         public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult status) {
/* 426 */           if (packId.equals(id)) {
/* 427 */             DownloadedPackSource.this.packFeedback = original;
/* 428 */             if (status == PackLoadFeedback.FinalResult.APPLIED) {
/* 429 */               result.complete(null);
/*     */             } else {
/* 431 */               result.completeExceptionally(new IllegalStateException("Failed to apply pack " + String.valueOf(id) + ", reason: " + String.valueOf(status)));
/*     */             } 
/*     */           } 
/* 434 */           original.reportFinalResult(id, status);
/*     */         }
/*     */       };
/* 437 */     return result;
/*     */   }
/*     */   
/*     */   public void cleanupAfterDisconnect() {
/* 441 */     this.manager.popAll();
/* 442 */     this.packFeedback = LOG_ONLY_FEEDBACK;
/* 443 */     this.manager.resetPromptStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 448 */     this.downloadQueue.close();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/server/DownloadedPackSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */