/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.dto.WorldDownload;
/*     */ import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.http.HttpClient;
/*     */ import java.net.http.HttpRequest;
/*     */ import java.net.http.HttpResponse;
/*     */ import java.time.Duration;
/*     */ import java.util.Locale;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.output.CountingOutputStream;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileDownload
/*     */ {
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private volatile boolean cancelled;
/*     */   
/*     */   private volatile boolean finished;
/*     */   
/*     */   private volatile boolean error;
/*     */   private volatile boolean extracting;
/*     */   private volatile File tempFile;
/*     */   private volatile File resourcePackPath;
/*     */   private volatile CompletableFuture<?> pendingRequest;
/*     */   private Thread currentThread;
/*     */   
/*     */   private <T> T joinCancellableRequest(CompletableFuture<T> pendingRequest) throws Throwable {
/*  62 */     this.pendingRequest = pendingRequest;
/*  63 */     if (this.cancelled) {
/*  64 */       pendingRequest.cancel(true);
/*  65 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/*  69 */       return pendingRequest.join();
/*  70 */     } catch (CompletionException e) {
/*  71 */       throw e.getCause();
/*     */     }
/*  73 */     catch (CancellationException e) {
/*  74 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static HttpClient createClient() {
/*  79 */     return HttpClient.newBuilder().executor((Executor)Util.nonCriticalIoPool()).connectTimeout(Duration.ofMinutes(2L)).build();
/*     */   }
/*     */   
/*     */   private static HttpRequest.Builder createRequest(String downloadLink) {
/*  83 */     return HttpRequest.newBuilder(URI.create(downloadLink)).timeout(Duration.ofMinutes(2L));
/*     */   }
/*     */   @CheckReturnValue
/*     */   public static OptionalLong contentLength(String downloadLink) {
/*     */     
/*  88 */     try { HttpClient client = createClient(); 
/*  89 */       try { HttpResponse<Void> response = client.send(
/*  90 */             createRequest(downloadLink).HEAD().build(), 
/*  91 */             HttpResponse.BodyHandlers.discarding());
/*     */         
/*  93 */         OptionalLong optionalLong = response.headers().firstValueAsLong("Content-Length");
/*  94 */         if (client != null) client.close();  return optionalLong; } catch (Throwable throwable) { if (client != null) try { client.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/*  95 */     { LOGGER.error("Unable to get content length for download");
/*  96 */       return OptionalLong.empty(); }
/*     */   
/*     */   }
/*     */   
/*     */   public void download(WorldDownload worldDownload, String worldName, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, LevelStorageSource levelStorageSource) {
/* 101 */     if (this.currentThread != null) {
/*     */       return;
/*     */     }
/*     */     
/* 105 */     this.currentThread = new Thread(() -> {
/*     */           HttpClient client = createClient(); try {
/*     */             try {
/*     */               this.tempFile = File.createTempFile("backup", ".tar.gz");
/*     */               download(downloadStatus, client, worldDownload.downloadLink(), this.tempFile);
/*     */               finishWorldDownload(worldName.trim(), this.tempFile, levelStorageSource, downloadStatus);
/* 111 */             } catch (Exception e) {
/*     */               LOGGER.error("Caught exception while downloading world", e);
/*     */               this.error = true;
/*     */             } finally {
/*     */               this.pendingRequest = null;
/*     */               if (this.tempFile != null) {
/*     */                 this.tempFile.delete();
/*     */               }
/*     */               this.tempFile = null;
/*     */             } 
/*     */             if (this.error) {
/*     */               if (client != null)
/*     */                 client.close(); 
/*     */               return;
/*     */             } 
/*     */             String resourcePackLink = worldDownload.resourcePackUrl();
/*     */             if (!resourcePackLink.isEmpty() && !worldDownload.resourcePackHash().isEmpty())
/*     */               try {
/*     */                 this.tempFile = File.createTempFile("resources", ".tar.gz");
/*     */                 download(downloadStatus, client, resourcePackLink, this.tempFile);
/*     */                 finishResourcePackDownload(downloadStatus, this.tempFile, worldDownload);
/* 132 */               } catch (Exception e) {
/*     */                 LOGGER.error("Caught exception while downloading resource pack", e); this.error = true;
/*     */               } finally {
/*     */                 this.pendingRequest = null; if (this.tempFile != null)
/*     */                   this.tempFile.delete();  this.tempFile = null;
/*     */               }   this.finished = true; if (client != null)
/*     */               client.close(); 
/*     */           } catch (Throwable throwable) {
/*     */             if (client != null)
/*     */               try {
/*     */                 client.close();
/*     */               } catch (Throwable throwable1) {
/*     */                 throwable.addSuppressed(throwable1);
/*     */               }   throw throwable;
/*     */           } 
/* 147 */         }); this.currentThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new RealmsDefaultUncaughtExceptionHandler(LOGGER));
/* 148 */     this.currentThread.start();
/*     */   }
/*     */   
/*     */   private void download(RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, HttpClient client, String url, File target) throws IOException { HttpResponse<InputStream> response;
/* 152 */     HttpRequest request = createRequest(url).GET().build();
/*     */ 
/*     */     
/*     */     try {
/* 156 */       response = joinCancellableRequest(client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));
/* 157 */     } catch (Error e) {
/* 158 */       throw e;
/* 159 */     } catch (Throwable e) {
/* 160 */       LOGGER.error("Failed to download {}", url, e);
/* 161 */       this.error = true;
/*     */       return;
/*     */     } 
/* 164 */     if (response == null || this.cancelled) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     if (response.statusCode() != 200) {
/* 169 */       this.error = true;
/*     */       
/*     */       return;
/*     */     } 
/* 173 */     downloadStatus.totalBytes = response.headers().firstValueAsLong("Content-Length").orElse(0L);
/*     */ 
/*     */     
/* 176 */     InputStream is = response.body(); 
/* 177 */     try { OutputStream os = new FileOutputStream(target);
/*     */       
/* 179 */       try { is.transferTo((OutputStream)new DownloadCountingOutputStream(os, downloadStatus));
/* 180 */         os.close(); } catch (Throwable throwable) { try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/*     */         try { is.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 184 */      } public void cancel() { if (this.tempFile != null) {
/* 185 */       this.tempFile.delete();
/* 186 */       this.tempFile = null;
/*     */     } 
/*     */     
/* 189 */     this.cancelled = true;
/* 190 */     CompletableFuture<?> pendingRequest = this.pendingRequest;
/* 191 */     if (pendingRequest != null) {
/* 192 */       pendingRequest.cancel(true);
/*     */     } }
/*     */ 
/*     */   
/*     */   public boolean isFinished() {
/* 197 */     return this.finished;
/*     */   }
/*     */   
/*     */   public boolean isError() {
/* 201 */     return this.error;
/*     */   }
/*     */   
/*     */   public boolean isExtracting() {
/* 205 */     return this.extracting;
/*     */   }
/*     */ 
/*     */   
/* 209 */   private static final String[] INVALID_FILE_NAMES = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
/*     */ 
/*     */ 
/*     */   
/*     */   public static String findAvailableFolderName(String folder) {
/* 214 */     folder = folder.replaceAll("[\\./\"]", "_");
/*     */     
/* 216 */     for (String invalidName : INVALID_FILE_NAMES) {
/* 217 */       if (folder.equalsIgnoreCase(invalidName)) {
/* 218 */         folder = "_" + folder + "_";
/*     */       }
/*     */     } 
/*     */     
/* 222 */     return folder;
/*     */   }
/*     */   private void untarGzipArchive(String name, File file, LevelStorageSource levelStorageSource) throws IOException {
/*     */     String finalName;
/* 226 */     Pattern namePattern = Pattern.compile(".*-([0-9]+)$");
/*     */ 
/*     */     
/* 229 */     int number = 1;
/*     */     
/* 231 */     for (char replacer : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
/* 232 */       name = name.replace(replacer, '_');
/*     */     }
/*     */     
/* 235 */     if (StringUtils.isEmpty(name)) {
/* 236 */       name = "Realm";
/*     */     }
/*     */     
/* 239 */     name = findAvailableFolderName(name);
/*     */     
/*     */     try {
/* 242 */       for (LevelStorageSource.LevelDirectory level : (Iterable<LevelStorageSource.LevelDirectory>)levelStorageSource.findLevelCandidates()) {
/* 243 */         String levelId = level.directoryName();
/* 244 */         if (levelId.toLowerCase(Locale.ROOT).startsWith(name.toLowerCase(Locale.ROOT))) {
/* 245 */           Matcher matcher = namePattern.matcher(levelId);
/* 246 */           if (matcher.matches()) {
/* 247 */             int parsedNumber = Integer.parseInt(matcher.group(1));
/* 248 */             if (parsedNumber > number)
/* 249 */               number = parsedNumber; 
/*     */             continue;
/*     */           } 
/* 252 */           number++;
/*     */         }
/*     */       
/*     */       } 
/* 256 */     } catch (Exception e) {
/* 257 */       LOGGER.error("Error getting level list", e);
/* 258 */       this.error = true;
/*     */       
/*     */       return;
/*     */     } 
/* 262 */     if (!levelStorageSource.isNewLevelIdAcceptable(name) || number > 1) {
/* 263 */       finalName = name + name;
/*     */       
/* 265 */       if (!levelStorageSource.isNewLevelIdAcceptable(finalName)) {
/*     */         boolean foundName = false;
/*     */         
/* 268 */         while (!foundName) {
/* 269 */           number++;
/* 270 */           finalName = name + name;
/*     */           
/* 272 */           if (levelStorageSource.isNewLevelIdAcceptable(finalName)) {
/* 273 */             foundName = true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/* 278 */       finalName = name;
/*     */     } 
/*     */     
/* 281 */     TarArchiveInputStream tarIn = null;
/* 282 */     File saves = new File((Minecraft.getInstance()).gameDirectory.getAbsolutePath(), "saves");
/*     */     try {
/* 284 */       saves.mkdir();
/*     */       
/* 286 */       tarIn = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file))));
/*     */       
/* 288 */       TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
/* 289 */       while (tarEntry != null) {
/* 290 */         File destPath = new File(saves, tarEntry.getName().replace("world", finalName));
/*     */         
/* 292 */         if (tarEntry.isDirectory()) {
/* 293 */           destPath.mkdirs();
/*     */         } else {
/* 295 */           destPath.createNewFile();
/*     */           
/* 297 */           FileOutputStream output = new FileOutputStream(destPath); 
/* 298 */           try { IOUtils.copy((InputStream)tarIn, output);
/* 299 */             output.close(); } catch (Throwable throwable) { try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */              throw throwable; }
/*     */         
/* 302 */         }  tarEntry = tarIn.getNextTarEntry();
/*     */       } 
/* 304 */     } catch (Exception e) {
/* 305 */       LOGGER.error("Error extracting world", e);
/* 306 */       this.error = true;
/*     */     } finally {
/* 308 */       if (tarIn != null) {
/* 309 */         tarIn.close();
/*     */       }
/*     */       
/* 312 */       if (file != null) {
/* 313 */         file.delete();
/*     */       }
/*     */       
/* 316 */       try { LevelStorageSource.LevelStorageAccess access = levelStorageSource.validateAndCreateAccess(finalName); 
/* 317 */         try { access.renameAndDropPlayer(finalName);
/* 318 */           if (access != null) access.close();  } catch (Throwable throwable) { if (access != null) try { access.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e)
/* 319 */       { LOGGER.error("Failed to modify unpacked realms level {}", finalName, e); }
/* 320 */       catch (ContentValidationException e)
/* 321 */       { LOGGER.warn("Failed to download file", (Throwable)e); }
/*     */ 
/*     */       
/* 324 */       this.resourcePackPath = new File(saves, finalName + finalName + "resources.zip");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void finishWorldDownload(String worldName, File tempFile, LevelStorageSource levelStorageSource, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
/* 329 */     if (downloadStatus.bytesWritten >= downloadStatus.totalBytes && !this.cancelled && !this.error) {
/*     */       try {
/* 331 */         this.extracting = true;
/* 332 */         untarGzipArchive(worldName, tempFile, levelStorageSource);
/* 333 */       } catch (IOException e) {
/* 334 */         LOGGER.error("Error extracting archive", e);
/* 335 */         this.error = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void finishResourcePackDownload(RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, File tempFile, WorldDownload worldDownload) {
/* 341 */     if (downloadStatus.bytesWritten >= downloadStatus.totalBytes && !this.cancelled)
/*     */       
/*     */       try {
/* 344 */         String actualHash = Hashing.sha1().hashBytes(Files.toByteArray(tempFile)).toString();
/*     */         
/* 346 */         if (actualHash.equals(worldDownload.resourcePackHash())) {
/* 347 */           FileUtils.copyFile(tempFile, this.resourcePackPath);
/* 348 */           this.finished = true;
/*     */         } else {
/* 350 */           LOGGER.error("Resourcepack had wrong hash (expected {}, found {}). Deleting it.", worldDownload.resourcePackHash(), actualHash);
/* 351 */           FileUtils.deleteQuietly(tempFile);
/* 352 */           this.error = true;
/*     */         } 
/* 354 */       } catch (IOException e) {
/* 355 */         LOGGER.error("Error copying resourcepack file: {}", e.getMessage());
/* 356 */         this.error = true;
/*     */       }  
/*     */   }
/*     */   
/*     */   private static class DownloadCountingOutputStream
/*     */     extends CountingOutputStream {
/*     */     private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
/*     */     
/*     */     public DownloadCountingOutputStream(OutputStream out, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
/* 365 */       super(out);
/* 366 */       this.downloadStatus = downloadStatus;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void afterWrite(int n) throws IOException {
/* 371 */       super.afterWrite(n);
/* 372 */       this.downloadStatus.bytesWritten = getByteCount();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/FileDownload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */