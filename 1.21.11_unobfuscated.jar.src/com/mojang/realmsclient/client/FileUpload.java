/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.dto.UploadInfo;
/*     */ import com.mojang.realmsclient.gui.screens.UploadResult;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.http.HttpClient;
/*     */ import java.net.http.HttpRequest;
/*     */ import java.net.http.HttpResponse;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import org.apache.commons.io.input.CountingInputStream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FileUpload implements AutoCloseable {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_RETRIES = 5;
/*     */   
/*     */   private static final String UPLOAD_PATH = "/upload";
/*     */   
/*     */   private final File file;
/*     */   private final long realmId;
/*     */   private final int slotId;
/*     */   private final UploadInfo uploadInfo;
/*     */   private final String sessionId;
/*     */   private final String username;
/*     */   private final String clientVersion;
/*     */   private final String worldVersion;
/*     */   private final UploadStatus uploadStatus;
/*     */   private final HttpClient client;
/*     */   
/*     */   public FileUpload(File file, long realmId, int slotId, UploadInfo uploadInfo, User user, String clientVersion, String worldVersion, UploadStatus uploadStatus) {
/*  45 */     this.file = file;
/*  46 */     this.realmId = realmId;
/*  47 */     this.slotId = slotId;
/*  48 */     this.uploadInfo = uploadInfo;
/*  49 */     this.sessionId = user.getSessionId();
/*  50 */     this.username = user.getName();
/*  51 */     this.clientVersion = clientVersion;
/*  52 */     this.worldVersion = worldVersion;
/*  53 */     this.uploadStatus = uploadStatus;
/*     */     
/*  55 */     this.client = HttpClient.newBuilder().executor((Executor)Util.nonCriticalIoPool()).connectTimeout(Duration.ofSeconds(15L)).build();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  60 */     this.client.close();
/*     */   }
/*     */   
/*     */   public CompletableFuture<UploadResult> startUpload() {
/*  64 */     long fileSize = this.file.length();
/*  65 */     this.uploadStatus.setTotalBytes(fileSize);
/*  66 */     return requestUpload(0, fileSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<UploadResult> requestUpload(int currentAttempt, long fileSize) {
/*  71 */     HttpRequest.BodyPublisher publisher = inputStreamPublisherWithSize(() -> {
/*     */           try {
/*     */             return new UploadCountingInputStream(new FileInputStream(this.file), this.uploadStatus);
/*  74 */           } catch (IOException e) {
/*     */             LOGGER.warn("Failed to open file {}", this.file, e);
/*     */             
/*     */             return null;
/*     */           } 
/*     */         }, fileSize);
/*  80 */     HttpRequest request = HttpRequest.newBuilder(this.uploadInfo.uploadEndpoint().resolve("/upload/" + this.realmId + "/" + this.slotId))
/*  81 */       .timeout(Duration.ofMinutes(10L))
/*  82 */       .setHeader("Cookie", uploadCookie())
/*  83 */       .setHeader("Content-Type", "application/octet-stream")
/*  84 */       .POST(publisher)
/*  85 */       .build();
/*     */     
/*  87 */     return this.client.<String>sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).thenCompose(response -> {
/*     */           long retryDelaySeconds = getRetryDelaySeconds(currentAttempt);
/*     */           if (shouldRetry(retryDelaySeconds, currentAttempt)) {
/*     */             this.uploadStatus.restart();
/*     */             try {
/*     */               Thread.sleep(Duration.ofSeconds(retryDelaySeconds));
/*  93 */             } catch (InterruptedException interruptedException) {}
/*     */             return requestUpload(currentAttempt + 1, currentAttempt);
/*     */           } 
/*     */           return CompletableFuture.completedFuture(handleResponse(currentAttempt));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static HttpRequest.BodyPublisher inputStreamPublisherWithSize(Supplier<InputStream> inputStreamSupplier, long fileSize) {
/* 102 */     return HttpRequest.BodyPublishers.fromPublisher(
/* 103 */         HttpRequest.BodyPublishers.ofInputStream(inputStreamSupplier), fileSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String uploadCookie() {
/* 109 */     return "sid=" + this.sessionId + ";token=" + this.uploadInfo.token() + ";user=" + this.username + ";version=" + this.clientVersion + ";worldVersion=" + this.worldVersion;
/*     */   }
/*     */   
/*     */   private UploadResult handleResponse(HttpResponse<String> response) {
/* 113 */     int statusCode = response.statusCode();
/* 114 */     if (statusCode == 401) {
/* 115 */       LOGGER.debug("Realms server returned 401: {}", response.headers().firstValue("WWW-Authenticate"));
/*     */     }
/* 117 */     String errorMessage = null;
/* 118 */     String body = response.body();
/* 119 */     if (body != null && !body.isBlank()) {
/*     */       try {
/* 121 */         JsonElement errorMsgElement = LenientJsonParser.parse(body).getAsJsonObject().get("errorMsg");
/* 122 */         if (errorMsgElement != null) {
/* 123 */           errorMessage = errorMsgElement.getAsString();
/*     */         }
/* 125 */       } catch (Exception e) {
/* 126 */         LOGGER.warn("Failed to parse response {}", body, e);
/*     */       } 
/*     */     }
/*     */     
/* 130 */     return new UploadResult(statusCode, errorMessage);
/*     */   }
/*     */   
/*     */   private boolean shouldRetry(long retryDelaySeconds, int currentAttempt) {
/* 134 */     return (retryDelaySeconds > 0L && currentAttempt + 1 < 5);
/*     */   }
/*     */   
/*     */   private long getRetryDelaySeconds(HttpResponse<?> response) {
/* 138 */     return response.headers().firstValueAsLong("Retry-After").orElse(0L);
/*     */   }
/*     */   
/*     */   private static class UploadCountingInputStream
/*     */     extends CountingInputStream {
/*     */     private final UploadStatus uploadStatus;
/*     */     
/*     */     private UploadCountingInputStream(InputStream proxy, UploadStatus uploadStatus) {
/* 146 */       super(proxy);
/* 147 */       this.uploadStatus = uploadStatus;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void afterRead(int n) throws IOException {
/* 152 */       super.afterRead(n);
/* 153 */       this.uploadStatus.onWrite(getByteCount());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/FileUpload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */