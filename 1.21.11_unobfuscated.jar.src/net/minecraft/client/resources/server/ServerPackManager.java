/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.packs.DownloadQueue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerPackManager
/*     */ {
/*     */   private final PackDownloader downloader;
/*     */   private final PackLoadFeedback packLoadFeedback;
/*     */   private final PackReloadConfig reloadConfig;
/*     */   private final Runnable updateRequest;
/*     */   private PackPromptStatus packPromptStatus;
/*  52 */   private final List<ServerPackData> packs = new ArrayList<>();
/*     */   
/*     */   public ServerPackManager(PackDownloader downloader, PackLoadFeedback packLoadFeedback, PackReloadConfig reloadConfig, Runnable updateRequest, PackPromptStatus packPromptStatus) {
/*  55 */     this.downloader = downloader;
/*  56 */     this.packLoadFeedback = packLoadFeedback;
/*  57 */     this.reloadConfig = reloadConfig;
/*  58 */     this.updateRequest = updateRequest;
/*  59 */     this.packPromptStatus = packPromptStatus;
/*     */   }
/*     */   
/*     */   private void registerForUpdate() {
/*  63 */     this.updateRequest.run();
/*     */   }
/*     */   
/*     */   public enum PackPromptStatus {
/*  67 */     PENDING,
/*  68 */     ALLOWED,
/*  69 */     DECLINED;
/*     */   }
/*     */   
/*     */   private enum PackDownloadStatus {
/*  73 */     REQUESTED,
/*  74 */     PENDING,
/*  75 */     DONE;
/*     */   }
/*     */   
/*     */   private enum RemovalReason {
/*  79 */     DOWNLOAD_FAILED(PackLoadFeedback.FinalResult.DOWNLOAD_FAILED),
/*  80 */     ACTIVATION_FAILED(PackLoadFeedback.FinalResult.ACTIVATION_FAILED),
/*  81 */     DECLINED(PackLoadFeedback.FinalResult.DECLINED),
/*  82 */     DISCARDED(PackLoadFeedback.FinalResult.DISCARDED),
/*  83 */     SERVER_REMOVED(null),
/*  84 */     SERVER_REPLACED(null);
/*     */     
/*     */     private final PackLoadFeedback.FinalResult serverResponse;
/*     */ 
/*     */     
/*     */     RemovalReason(PackLoadFeedback.FinalResult serverResponse) {
/*  90 */       this.serverResponse = serverResponse;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum ActivationStatus {
/*  95 */     INACTIVE,
/*  96 */     PENDING,
/*  97 */     ACTIVE;
/*     */   }
/*     */   
/*     */   private static class ServerPackData {
/*     */     private final UUID id;
/*     */     private final URL url;
/*     */     private final HashCode hash;
/*     */     private Path path;
/*     */     private ServerPackManager.RemovalReason removalReason;
/* 106 */     private ServerPackManager.PackDownloadStatus downloadStatus = ServerPackManager.PackDownloadStatus.REQUESTED;
/* 107 */     private ServerPackManager.ActivationStatus activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/*     */     private boolean promptAccepted;
/*     */     
/*     */     private ServerPackData(UUID id, URL url, HashCode hash) {
/* 111 */       this.id = id;
/* 112 */       this.url = url;
/* 113 */       this.hash = hash;
/*     */     }
/*     */     
/*     */     public void setRemovalReasonIfNotSet(ServerPackManager.RemovalReason removalReason) {
/* 117 */       if (this.removalReason == null) {
/* 118 */         this.removalReason = removalReason;
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isRemoved() {
/* 123 */       return (this.removalReason != null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void markExistingPacksAsRemoved(UUID id) {
/* 129 */     for (ServerPackData pack : this.packs) {
/* 130 */       if (pack.id.equals(id)) {
/* 131 */         pack.setRemovalReasonIfNotSet(RemovalReason.SERVER_REPLACED);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pushPack(UUID id, URL url, HashCode hash) {
/* 137 */     if (this.packPromptStatus == PackPromptStatus.DECLINED) {
/* 138 */       this.packLoadFeedback.reportFinalResult(id, PackLoadFeedback.FinalResult.DECLINED);
/*     */       
/*     */       return;
/*     */     } 
/* 142 */     pushNewPack(id, new ServerPackData(id, url, hash));
/*     */   }
/*     */   public void pushLocalPack(UUID id, Path path) {
/*     */     URL url;
/* 146 */     if (this.packPromptStatus == PackPromptStatus.DECLINED) {
/* 147 */       this.packLoadFeedback.reportFinalResult(id, PackLoadFeedback.FinalResult.DECLINED);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 153 */       url = path.toUri().toURL();
/* 154 */     } catch (MalformedURLException e) {
/* 155 */       throw new IllegalStateException("Can't convert path to URL " + String.valueOf(path), e);
/*     */     } 
/*     */     
/* 158 */     ServerPackData pack = new ServerPackData(id, url, null);
/* 159 */     pack.downloadStatus = PackDownloadStatus.DONE;
/* 160 */     pack.path = path;
/* 161 */     pushNewPack(id, pack);
/*     */   }
/*     */ 
/*     */   
/*     */   private void pushNewPack(UUID id, ServerPackData pack) {
/* 166 */     markExistingPacksAsRemoved(id);
/*     */     
/* 168 */     this.packs.add(pack);
/*     */     
/* 170 */     if (this.packPromptStatus == PackPromptStatus.ALLOWED) {
/* 171 */       acceptPack(pack);
/*     */     }
/*     */     
/* 174 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   private void acceptPack(ServerPackData pack) {
/* 178 */     this.packLoadFeedback.reportUpdate(pack.id, PackLoadFeedback.Update.ACCEPTED);
/* 179 */     pack.promptAccepted = true;
/*     */   }
/*     */   
/*     */   private ServerPackData findPackInfo(UUID id) {
/* 183 */     for (ServerPackData pack : this.packs) {
/* 184 */       if (!pack.isRemoved() && pack.id.equals(id)) {
/* 185 */         return pack;
/*     */       }
/*     */     } 
/* 188 */     return null;
/*     */   }
/*     */   
/*     */   public void popPack(UUID id) {
/* 192 */     ServerPackData packInfo = findPackInfo(id);
/* 193 */     if (packInfo != null) {
/* 194 */       packInfo.setRemovalReasonIfNotSet(RemovalReason.SERVER_REMOVED);
/* 195 */       registerForUpdate();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void popAll() {
/* 200 */     for (ServerPackData pack : this.packs) {
/* 201 */       pack.setRemovalReasonIfNotSet(RemovalReason.SERVER_REMOVED);
/*     */     }
/* 203 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   public void allowServerPacks() {
/* 207 */     this.packPromptStatus = PackPromptStatus.ALLOWED;
/*     */     
/* 209 */     for (ServerPackData pack : this.packs) {
/* 210 */       if (!pack.promptAccepted && !pack.isRemoved()) {
/* 211 */         acceptPack(pack);
/*     */       }
/*     */     } 
/*     */     
/* 215 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   public void rejectServerPacks() {
/* 219 */     this.packPromptStatus = PackPromptStatus.DECLINED;
/* 220 */     for (ServerPackData pack : this.packs) {
/* 221 */       if (!pack.promptAccepted) {
/* 222 */         pack.setRemovalReasonIfNotSet(RemovalReason.DECLINED);
/*     */       }
/*     */     } 
/* 225 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   public void resetPromptStatus() {
/* 229 */     this.packPromptStatus = PackPromptStatus.PENDING;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 234 */     boolean downloadsPending = updateDownloads();
/* 235 */     if (!downloadsPending) {
/* 236 */       triggerReloadIfNeeded();
/*     */     }
/*     */     
/* 239 */     cleanupRemovedPacks();
/*     */   }
/*     */   
/*     */   private void cleanupRemovedPacks() {
/* 243 */     this.packs.removeIf(data -> {
/*     */           if (data.activationStatus != ActivationStatus.INACTIVE) {
/*     */             return false;
/*     */           }
/*     */           if (data.removalReason != null) {
/*     */             PackLoadFeedback.FinalResult response = data.removalReason.serverResponse;
/*     */             if (response != null) {
/*     */               this.packLoadFeedback.reportFinalResult(data.id, response);
/*     */             }
/*     */             return true;
/*     */           } 
/*     */           return false;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onDownload(Collection<ServerPackData> data, DownloadQueue.BatchResult result) {
/* 262 */     if (!result.failed().isEmpty())
/*     */     {
/* 264 */       for (ServerPackData pack : this.packs) {
/* 265 */         if (pack.activationStatus != ActivationStatus.ACTIVE) {
/* 266 */           if (result.failed().contains(pack.id)) {
/* 267 */             pack.setRemovalReasonIfNotSet(RemovalReason.DOWNLOAD_FAILED); continue;
/*     */           } 
/* 269 */           pack.setRemovalReasonIfNotSet(RemovalReason.DISCARDED);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 275 */     for (ServerPackData pack : data) {
/* 276 */       Path packFile = (Path)result.downloaded().get(pack.id);
/* 277 */       if (packFile != null) {
/* 278 */         pack.downloadStatus = PackDownloadStatus.DONE;
/* 279 */         pack.path = packFile;
/*     */         
/* 281 */         if (!pack.isRemoved()) {
/* 282 */           this.packLoadFeedback.reportUpdate(pack.id, PackLoadFeedback.Update.DOWNLOADED);
/*     */         }
/*     */       } 
/*     */     } 
/* 286 */     registerForUpdate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean updateDownloads() {
/* 294 */     List<ServerPackData> downloadPacks = new ArrayList<>();
/*     */     boolean downloadsInProgress = false;
/* 296 */     for (ServerPackData pack : this.packs) {
/* 297 */       if (pack.isRemoved() || !pack.promptAccepted) {
/*     */         continue;
/*     */       }
/*     */       
/* 301 */       if (pack.downloadStatus != PackDownloadStatus.DONE) {
/* 302 */         downloadsInProgress = true;
/*     */       }
/*     */       
/* 305 */       if (pack.downloadStatus == PackDownloadStatus.REQUESTED) {
/* 306 */         pack.downloadStatus = PackDownloadStatus.PENDING;
/* 307 */         downloadPacks.add(pack);
/*     */       } 
/*     */     } 
/*     */     
/* 311 */     if (!downloadPacks.isEmpty()) {
/* 312 */       Map<UUID, DownloadQueue.DownloadRequest> downloadRequests = new HashMap<>();
/* 313 */       for (ServerPackData pack : downloadPacks) {
/* 314 */         downloadRequests.put(pack.id, new DownloadQueue.DownloadRequest(pack.url, pack.hash));
/*     */       }
/* 316 */       this.downloader.download(downloadRequests, result -> onDownload(downloadPacks, downloadPacks));
/*     */     } 
/* 318 */     return downloadsInProgress;
/*     */   }
/*     */   
/*     */   private void triggerReloadIfNeeded() {
/*     */     boolean needsReload = false;
/* 323 */     final List<ServerPackData> packsToLoad = new ArrayList<>();
/* 324 */     final List<ServerPackData> packsToUnload = new ArrayList<>();
/*     */     
/* 326 */     for (ServerPackData pack : this.packs) {
/* 327 */       if (pack.activationStatus == ActivationStatus.PENDING) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 332 */       boolean shouldBeActive = (pack.promptAccepted && pack.downloadStatus == PackDownloadStatus.DONE && !pack.isRemoved());
/* 333 */       if (shouldBeActive && pack.activationStatus == ActivationStatus.INACTIVE) {
/* 334 */         packsToLoad.add(pack);
/* 335 */         needsReload = true;
/*     */       } 
/*     */       
/* 338 */       if (pack.activationStatus == ActivationStatus.ACTIVE) {
/* 339 */         if (!shouldBeActive) {
/* 340 */           needsReload = true;
/* 341 */           packsToUnload.add(pack);
/*     */           continue;
/*     */         } 
/* 344 */         packsToLoad.add(pack);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 349 */     if (needsReload) {
/* 350 */       for (ServerPackData pack : packsToLoad) {
/* 351 */         if (pack.activationStatus != ActivationStatus.ACTIVE) {
/* 352 */           pack.activationStatus = ActivationStatus.PENDING;
/*     */         }
/*     */       } 
/*     */       
/* 356 */       for (ServerPackData pack : packsToUnload) {
/* 357 */         pack.activationStatus = ActivationStatus.PENDING;
/*     */       }
/*     */       
/* 360 */       this.reloadConfig.scheduleReload(new PackReloadConfig.Callbacks()
/*     */           {
/*     */             public void onSuccess() {
/* 363 */               for (ServerPackManager.ServerPackData pack : (Iterable<ServerPackManager.ServerPackData>)packsToLoad) {
/* 364 */                 pack.activationStatus = ServerPackManager.ActivationStatus.ACTIVE;
/* 365 */                 if (pack.removalReason == null) {
/* 366 */                   ServerPackManager.this.packLoadFeedback.reportFinalResult(pack.id, PackLoadFeedback.FinalResult.APPLIED);
/*     */                 }
/*     */               } 
/* 369 */               for (ServerPackManager.ServerPackData pack : (Iterable<ServerPackManager.ServerPackData>)packsToUnload) {
/* 370 */                 pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/*     */               }
/* 372 */               ServerPackManager.this.registerForUpdate();
/*     */             }
/*     */ 
/*     */             
/*     */             public void onFailure(boolean isRecovery) {
/* 377 */               if (!isRecovery) {
/*     */                 
/* 379 */                 packsToLoad.clear();
/* 380 */                 for (ServerPackManager.ServerPackData pack : ServerPackManager.this.packs) {
/* 381 */                   switch (pack.activationStatus.ordinal()) { case 2:
/* 382 */                       packsToLoad.add(pack);
/*     */                     case 1:
/* 384 */                       pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/* 385 */                       pack.setRemovalReasonIfNotSet(ServerPackManager.RemovalReason.ACTIVATION_FAILED);
/*     */                     case 0:
/* 387 */                       pack.setRemovalReasonIfNotSet(ServerPackManager.RemovalReason.DISCARDED); }
/*     */                 
/*     */                 } 
/* 390 */                 ServerPackManager.this.registerForUpdate();
/*     */               } else {
/*     */                 
/* 393 */                 for (ServerPackManager.ServerPackData pack : ServerPackManager.this.packs) {
/* 394 */                   if (pack.activationStatus == ServerPackManager.ActivationStatus.PENDING) {
/* 395 */                     pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/*     */             public List<PackReloadConfig.IdAndPath> packsToLoad() {
/* 403 */               return packsToLoad.stream().map(pack -> new PackReloadConfig.IdAndPath(pack.id, pack.path)).toList();
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/server/ServerPackManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */