/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Screenshot {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public static final String SCREENSHOT_DIR = "screenshots";
/*     */   
/*     */   public static void grab(File workDir, RenderTarget target, Consumer<Component> callback) {
/*  26 */     grab(workDir, null, target, 1, callback);
/*     */   }
/*     */   
/*     */   public static void grab(File workDir, String forceName, RenderTarget target, int downscaleFactor, Consumer<Component> callback) {
/*  30 */     takeScreenshot(target, downscaleFactor, image -> {
/*     */           File file, picDir = new File(workDir, "screenshots");
/*     */           picDir.mkdir();
/*     */           if (forceName == null) {
/*     */             file = getFile(picDir);
/*     */           } else {
/*     */             file = new File(picDir, forceName);
/*     */           } 
/*     */           Util.ioPool().execute(());
/*     */         });
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
/*     */   public static void takeScreenshot(RenderTarget target, Consumer<NativeImage> callback) {
/*  56 */     takeScreenshot(target, 1, callback);
/*     */   }
/*     */   
/*     */   public static void takeScreenshot(RenderTarget target, int downscaleFactor, Consumer<NativeImage> callback) {
/*  60 */     int width = target.width;
/*  61 */     int height = target.height;
/*     */     
/*  63 */     GpuTexture sourceTexture = target.getColorTexture();
/*  64 */     if (sourceTexture == null) {
/*  65 */       throw new IllegalStateException("Tried to capture screenshot of an incomplete framebuffer");
/*     */     }
/*  67 */     if (width % downscaleFactor != 0 || height % downscaleFactor != 0) {
/*  68 */       throw new IllegalArgumentException("Image size is not divisible by downscale factor");
/*     */     }
/*  70 */     GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Screenshot buffer", 9, width * height * sourceTexture.getFormat().pixelSize());
/*  71 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  72 */     RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(sourceTexture, buffer, 0L, () -> { GpuBuffer.MappedView read = commandEncoder.mapBuffer(buffer, true, false); try { int outputHeight = height / downscaleFactor, outputWidth = width / downscaleFactor; NativeImage image = new NativeImage(outputWidth, outputHeight, false); for (int y = 0; y < outputHeight; y++) { for (int x = 0; x < outputWidth; x++) { if (downscaleFactor == 1) { int argb = read.data().getInt((x + y * width) * sourceTexture.getFormat().pixelSize()); image.setPixelABGR(x, height - y - 1, argb | 0xFF000000); } else { int red = 0, green = 0, blue = 0; for (int i = 0; i < downscaleFactor; i++) { for (int j = 0; j < downscaleFactor; j++) { int argb = read.data().getInt((x * downscaleFactor + i + (y * downscaleFactor + j) * width) * sourceTexture.getFormat().pixelSize()); red += ARGB.red(argb); green += ARGB.green(argb); blue += ARGB.blue(argb); }  }  int sampleCount = downscaleFactor * downscaleFactor; image.setPixelABGR(x, outputHeight - y - 1, ARGB.color(255, red / sampleCount, green / sampleCount, blue / sampleCount)); }  }  }  callback.accept(image); if (read != null)
/*  73 */               read.close();  } catch (Throwable throwable) { if (read != null) try { read.close(); } catch (Throwable throwable1)
/*     */               { throwable.addSuppressed(throwable1); }
/*     */             
/*     */             
/*     */             throw throwable; }
/*     */           
/*     */           buffer.close();
/*     */         }, 0);
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
/*     */   private static File getFile(File picDir) {
/* 110 */     String name = Util.getFilenameFormattedDateTime();
/*     */     
/* 112 */     for (int count = 1;; count++) {
/* 113 */       File file = new File(picDir, name + name + ".png");
/* 114 */       if (!file.exists())
/* 115 */         return file; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/Screenshot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */