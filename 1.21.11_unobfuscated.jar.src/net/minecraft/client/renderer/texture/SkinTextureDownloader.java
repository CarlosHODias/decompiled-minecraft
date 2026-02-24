/*     */ package net.minecraft.client.renderer.texture;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SkinTextureDownloader {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SKIN_WIDTH = 64;
/*     */   private static final int SKIN_HEIGHT = 64;
/*     */   private static final int LEGACY_SKIN_HEIGHT = 32;
/*     */   private final Proxy proxy;
/*     */   private final TextureManager textureManager;
/*     */   private final Executor mainThreadExecutor;
/*     */   
/*     */   public SkinTextureDownloader(Proxy proxy, TextureManager textureManager, Executor mainThreadExecutor) {
/*  34 */     this.proxy = proxy;
/*  35 */     this.textureManager = textureManager;
/*  36 */     this.mainThreadExecutor = mainThreadExecutor;
/*     */   }
/*     */   
/*     */   public CompletableFuture<ClientAsset.Texture> downloadAndRegisterSkin(Identifier textureId, Path localCopy, String url, boolean processLegacySkin) {
/*  40 */     ClientAsset.DownloadedTexture texture = new ClientAsset.DownloadedTexture(textureId, url);
/*  41 */     return CompletableFuture.supplyAsync(() -> {
/*     */           NativeImage loadedSkin;
/*     */           try {
/*     */             loadedSkin = downloadSkin(localCopy, texture.url());
/*  45 */           } catch (IOException e) {
/*     */             throw new UncheckedIOException(e);
/*     */           } 
/*     */           return processLegacySkin ? processLegacySkin(loadedSkin, texture.url()) : loadedSkin;
/*  49 */         }, Util.nonCriticalIoPool().forName("downloadTexture"))
/*  50 */       .thenCompose(fixedSkin -> registerTextureInManager((ClientAsset.Texture)texture, texture));
/*     */   }
/*     */   
/*     */   private NativeImage downloadSkin(Path localCopy, String url) throws IOException {
/*  54 */     if (Files.isRegularFile(localCopy, new java.nio.file.LinkOption[0])) {
/*  55 */       LOGGER.debug("Loading HTTP texture from local cache ({})", localCopy);
/*  56 */       InputStream inputStream = Files.newInputStream(localCopy, new java.nio.file.OpenOption[0]); 
/*  57 */       try { NativeImage nativeImage = NativeImage.read(inputStream);
/*  58 */         if (inputStream != null) inputStream.close();  return nativeImage; } catch (Throwable throwable) { if (inputStream != null)
/*     */           try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */     
/*  61 */     }  HttpURLConnection connection = null;
/*  62 */     LOGGER.debug("Downloading HTTP texture from {} to {}", url, localCopy);
/*  63 */     URI uri = URI.create(url);
/*     */     try {
/*  65 */       connection = (HttpURLConnection)uri.toURL().openConnection(this.proxy);
/*  66 */       connection.setDoInput(true);
/*  67 */       connection.setDoOutput(false);
/*  68 */       connection.connect();
/*     */       
/*  70 */       int responseCode = connection.getResponseCode();
/*  71 */       if (responseCode / 100 != 2) {
/*  72 */         throw new IOException("Failed to open " + String.valueOf(uri) + ", HTTP error code: " + responseCode);
/*     */       }
/*     */ 
/*     */       
/*  76 */       byte[] imageContents = connection.getInputStream().readAllBytes();
/*     */       
/*     */       try {
/*  79 */         FileUtil.createDirectoriesSafe(localCopy.getParent());
/*  80 */         Files.write(localCopy, imageContents, new java.nio.file.OpenOption[0]);
/*  81 */       } catch (IOException e) {
/*  82 */         LOGGER.warn("Failed to cache texture {} in {}", url, localCopy);
/*     */       } 
/*     */       
/*  85 */       return NativeImage.read(imageContents);
/*     */     } finally {
/*  87 */       if (connection != null) {
/*  88 */         connection.disconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<ClientAsset.Texture> registerTextureInManager(ClientAsset.Texture textureId, NativeImage contents) {
/*  94 */     return CompletableFuture.supplyAsync(() -> { Objects.requireNonNull(textureId.texturePath()); DynamicTexture texture = new DynamicTexture(textureId.texturePath()::toString, contents); this.textureManager.register(textureId.texturePath(), texture); return textureId; }, this.mainThreadExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NativeImage processLegacySkin(NativeImage image, String url) {
/* 102 */     int height = image.getHeight();
/* 103 */     int width = image.getWidth();
/* 104 */     if (width != 64 || (height != 32 && height != 64)) {
/* 105 */       image.close();
/* 106 */       throw new IllegalStateException("Discarding incorrectly sized (" + width + "x" + height + ") skin texture from " + url);
/*     */     } 
/*     */     
/* 109 */     boolean isLegacy = (height == 32);
/* 110 */     if (isLegacy) {
/* 111 */       NativeImage newImage = new NativeImage(64, 64, true);
/* 112 */       newImage.copyFrom(image);
/* 113 */       image.close();
/* 114 */       image = newImage;
/*     */       
/* 116 */       image.fillRect(0, 32, 64, 32, 0);
/*     */ 
/*     */       
/* 119 */       image.copyRect(4, 16, 16, 32, 4, 4, true, false);
/* 120 */       image.copyRect(8, 16, 16, 32, 4, 4, true, false);
/* 121 */       image.copyRect(0, 20, 24, 32, 4, 12, true, false);
/* 122 */       image.copyRect(4, 20, 16, 32, 4, 12, true, false);
/* 123 */       image.copyRect(8, 20, 8, 32, 4, 12, true, false);
/* 124 */       image.copyRect(12, 20, 16, 32, 4, 12, true, false);
/*     */ 
/*     */       
/* 127 */       image.copyRect(44, 16, -8, 32, 4, 4, true, false);
/* 128 */       image.copyRect(48, 16, -8, 32, 4, 4, true, false);
/* 129 */       image.copyRect(40, 20, 0, 32, 4, 12, true, false);
/* 130 */       image.copyRect(44, 20, -8, 32, 4, 12, true, false);
/* 131 */       image.copyRect(48, 20, -16, 32, 4, 12, true, false);
/* 132 */       image.copyRect(52, 20, -8, 32, 4, 12, true, false);
/*     */     } 
/*     */     
/* 135 */     setNoAlpha(image, 0, 0, 32, 16);
/*     */     
/* 137 */     if (isLegacy) {
/* 138 */       doNotchTransparencyHack(image, 32, 0, 64, 32);
/*     */     }
/* 140 */     setNoAlpha(image, 0, 16, 64, 32);
/* 141 */     setNoAlpha(image, 16, 48, 48, 64);
/*     */     
/* 143 */     return image;
/*     */   }
/*     */   
/*     */   private static void doNotchTransparencyHack(NativeImage image, int x0, int y0, int x1, int y1) {
/* 147 */     for (int x = x0; x < x1; x++) {
/* 148 */       for (int y = y0; y < y1; y++) {
/* 149 */         int pix = image.getPixel(x, y);
/* 150 */         if (ARGB.alpha(pix) < 128) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     for (int i = x0; i < x1; i++) {
/* 157 */       for (int y = y0; y < y1; y++) {
/* 158 */         image.setPixel(i, y, image.getPixel(i, y) & 0xFFFFFF);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setNoAlpha(NativeImage image, int x0, int y0, int x1, int y1) {
/* 164 */     for (int x = x0; x < x1; x++) {
/* 165 */       for (int y = y0; y < y1; y++)
/* 166 */         image.setPixel(x, y, ARGB.opaque(image.getPixel(x, y))); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/SkinTextureDownloader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */