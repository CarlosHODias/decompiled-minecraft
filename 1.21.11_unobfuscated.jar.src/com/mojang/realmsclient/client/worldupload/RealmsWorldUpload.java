/*     */ package com.mojang.realmsclient.client.worldupload;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.FileUpload;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.UploadInfo;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.exception.RetryCallException;
/*     */ import com.mojang.realmsclient.gui.screens.UploadResult;
/*     */ import com.mojang.realmsclient.util.UploadTokenCache;
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class RealmsWorldUpload
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int UPLOAD_RETRIES = 20;
/*     */   
/*  29 */   private final RealmsClient client = RealmsClient.getOrCreate();
/*     */   
/*     */   private final Path worldFolder;
/*     */   private final RealmsSlot realmsSlot;
/*     */   private final User user;
/*     */   private final long realmId;
/*     */   private final RealmsWorldUploadStatusTracker statusCallback;
/*     */   private volatile boolean cancelled;
/*     */   private volatile CompletableFuture<?> uploadTask;
/*     */   
/*     */   public RealmsWorldUpload(Path worldFolder, RealmsSlot realmsSlot, User user, long realmId, RealmsWorldUploadStatusTracker statusCallback) {
/*  40 */     this.worldFolder = worldFolder;
/*  41 */     this.realmsSlot = realmsSlot;
/*  42 */     this.user = user;
/*  43 */     this.realmId = realmId;
/*  44 */     this.statusCallback = statusCallback;
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> packAndUpload() {
/*  48 */     return CompletableFuture.runAsync(() -> {
/*     */           File archive = null; try {
/*     */             UploadInfo uploadInfo = requestUploadInfoWithRetries();
/*     */             archive = RealmsUploadWorldPacker.pack(this.worldFolder, ());
/*     */             this.statusCallback.setUploading();
/*     */             FileUpload fileUpload = new FileUpload(archive, this.realmId, this.realmsSlot.slotId, uploadInfo, this.user, SharedConstants.getCurrentVersion().name(), this.realmsSlot.options.version, this.statusCallback.getUploadStatus());
/*     */             try {
/*     */               UploadResult join;
/*     */               CompletableFuture<UploadResult> uploadTask = fileUpload.startUpload();
/*     */               this.uploadTask = uploadTask;
/*     */               if (this.cancelled) {
/*     */                 uploadTask.cancel(true);
/*     */                 fileUpload.close();
/*     */                 return;
/*     */               } 
/*     */               
/*     */               try { join = uploadTask.join(); }
/*  65 */               catch (CompletionException e) { throw e.getCause(); }
/*     */                String errorMessage = join.getSimplifiedErrorMessage(); if (errorMessage != null)
/*     */                 throw new RealmsUploadFailedException(errorMessage);  UploadTokenCache.invalidate(this.realmId); this.client.updateSlot(this.realmId, this.realmsSlot.slotId, this.realmsSlot.options, this.realmsSlot.settings); fileUpload.close();
/*     */             } catch (Throwable throwable) {
/*     */               try {
/*     */                 fileUpload.close();
/*     */               } catch (Throwable join) {
/*     */                 throwable.addSuppressed((Throwable)join);
/*     */               }  throw throwable;
/*     */             } 
/*  75 */           } catch (RealmsServiceException e) {
/*     */             throw new RealmsUploadFailedException(e.realmsError.errorMessage());
/*  77 */           } catch (InterruptedException|java.util.concurrent.CancellationException e) {
/*     */             throw new RealmsUploadCanceledException();
/*  79 */           } catch (RealmsUploadException e) {
/*     */             throw e;
/*  81 */           } catch (Throwable e) {
/*     */             if (e instanceof Error) {
/*     */               Error error = (Error)e; throw error;
/*     */             } 
/*     */             throw new RealmsUploadFailedException(e.getMessage());
/*     */           } finally {
/*     */             if (archive != null) {
/*     */               LOGGER.debug("Deleting file {}", archive.getAbsolutePath());
/*     */               archive.delete();
/*     */             } 
/*     */           } 
/*  92 */         }, (Executor)Util.backgroundExecutor());
/*     */   }
/*     */   
/*     */   public void cancel() {
/*  96 */     this.cancelled = true;
/*  97 */     CompletableFuture<?> uploadTask = this.uploadTask;
/*  98 */     if (uploadTask != null) {
/*  99 */       uploadTask.cancel(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private UploadInfo requestUploadInfoWithRetries() throws RealmsServiceException, InterruptedException {
/* 104 */     for (int i = 0; i < 20; i++) {
/*     */       try {
/* 106 */         UploadInfo uploadInfo = this.client.requestUploadInfo(this.realmId);
/* 107 */         if (this.cancelled) {
/* 108 */           throw new RealmsUploadCanceledException();
/*     */         }
/* 110 */         if (uploadInfo != null) {
/* 111 */           if (!uploadInfo.worldClosed()) {
/* 112 */             throw new RealmsUploadWorldNotClosedException();
/*     */           }
/* 114 */           return uploadInfo;
/*     */         } 
/* 116 */       } catch (RetryCallException e) {
/* 117 */         Thread.sleep(e.delaySeconds * 1000L);
/*     */       } 
/*     */     } 
/* 120 */     throw new RealmsUploadWorldNotClosedException();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/worldupload/RealmsWorldUpload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */