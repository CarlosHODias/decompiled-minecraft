/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.IntUnaryOperator;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextureUtil
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MIN_MIPMAP_LEVEL = 0;
/*     */   
/*     */   private static final int DEFAULT_IMAGE_BUFFER_SIZE = 8192;
/*     */   
/*  33 */   private static final int[][] DIRECTIONS = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
/*     */ 
/*     */   
/*     */   public static ByteBuffer readResource(InputStream inputStream) throws IOException {
/*  37 */     ReadableByteChannel channel = Channels.newChannel(inputStream);
/*  38 */     if (channel instanceof SeekableByteChannel) { SeekableByteChannel seekableChannel = (SeekableByteChannel)channel;
/*  39 */       return readResource(channel, (int)seekableChannel.size() + 1); }
/*     */     
/*  41 */     return readResource(channel, 8192);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer readResource(ReadableByteChannel channel, int expectedSize) throws IOException {
/*  46 */     ByteBuffer buffer = MemoryUtil.memAlloc(expectedSize);
/*     */     try {
/*  48 */       while (channel.read(buffer) != -1) {
/*  49 */         if (!buffer.hasRemaining()) {
/*  50 */           buffer = MemoryUtil.memRealloc(buffer, buffer.capacity() * 2);
/*     */         }
/*     */       } 
/*  53 */       buffer.flip();
/*  54 */       return buffer;
/*  55 */     } catch (IOException e) {
/*  56 */       MemoryUtil.memFree(buffer);
/*  57 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeAsPNG(Path dir, String prefix, GpuTexture texture, int maxMipLevel, IntUnaryOperator pixelModifier) {
/*  62 */     RenderSystem.assertOnRenderThread();
/*  63 */     long bufferLength = 0L;
/*  64 */     for (int i = 0; i <= maxMipLevel; i++) {
/*  65 */       bufferLength += texture.getFormat().pixelSize() * texture.getWidth(i) * texture.getHeight(i);
/*     */     }
/*  67 */     if (bufferLength > 2147483647L) {
/*  68 */       throw new IllegalArgumentException("Exporting textures larger than 2GB is not supported");
/*     */     }
/*  70 */     GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Texture output buffer", 9, bufferLength);
/*  71 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder(); Runnable onCopyComplete = () -> { GpuBuffer.MappedView read = commandEncoder.mapBuffer(buffer, true, false); try { int offset = 0; for (int i = 0; i <= maxMipLevel; i++) { int mipWidth = texture.getWidth(i), mipHeight = texture.getHeight(i); 
/*     */             try { NativeImage image = new NativeImage(mipWidth, mipHeight, false); 
/*     */               try { for (int y = 0; y < mipHeight; y++) {
/*     */                   for (int x = 0; x < mipWidth; x++) {
/*     */                     int argb = read.data().getInt(offset + (x + y * mipWidth) * texture.getFormat().pixelSize()); image.setPixelABGR(x, y, pixelModifier.applyAsInt(argb));
/*     */                   } 
/*     */                 }  Path target = dir.resolve(prefix + "_" + prefix + ".png"); image.writeToFile(target); LOGGER.debug("Exported png to: {}", target.toAbsolutePath()); image.close(); }
/*  78 */               catch (Throwable throwable) { try { image.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 throw throwable; }
/*     */                }
/*  88 */             catch (IOException e) { LOGGER.debug("Unable to write: ", e); }
/*     */              offset += texture.getFormat().pixelSize() * mipWidth * mipHeight; }
/*     */            if (read != null)
/*     */             read.close();  }
/*     */         catch (Throwable throwable) { if (read != null)
/*     */             try { read.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/*     */          buffer.close();
/*  97 */       }; AtomicInteger completedCopies = new AtomicInteger();
/*  98 */     int offset = 0;
/*  99 */     for (int j = 0; j <= maxMipLevel; j++) {
/* 100 */       commandEncoder.copyTextureToBuffer(texture, buffer, offset, () -> { if (completedCopies.getAndIncrement() == maxMipLevel) onCopyComplete.run();  }, j);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 105 */       offset += texture.getFormat().pixelSize() * texture.getWidth(j) * texture.getHeight(j);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Path getDebugTexturePath(Path root) {
/* 111 */     return root.resolve("screenshots").resolve("debug");
/*     */   }
/*     */   
/*     */   public static Path getDebugTexturePath() {
/* 115 */     return getDebugTexturePath(Path.of(".", new String[0]));
/*     */   }
/*     */   
/*     */   public static void solidify(NativeImage image) {
/* 119 */     int width = image.getWidth();
/* 120 */     int height = image.getHeight();
/*     */ 
/*     */     
/* 123 */     int[] nearestColor = new int[width * height];
/* 124 */     int[] distances = new int[width * height];
/* 125 */     Arrays.fill(distances, Integer.MAX_VALUE);
/*     */ 
/*     */     
/* 128 */     IntArrayFIFOQueue queue = new IntArrayFIFOQueue();
/*     */ 
/*     */     
/* 131 */     for (int x = 0; x < width; x++) {
/* 132 */       for (int y = 0; y < height; y++) {
/* 133 */         int color = image.getPixel(x, y);
/* 134 */         if (ARGB.alpha(color) != 0) {
/* 135 */           int packedCoordinates = pack(x, y, width);
/* 136 */           distances[packedCoordinates] = 0;
/* 137 */           nearestColor[packedCoordinates] = color;
/* 138 */           queue.enqueue(packedCoordinates);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 144 */     while (!queue.isEmpty()) {
/* 145 */       int packedCoordinates = queue.dequeueInt();
/* 146 */       int j = x(packedCoordinates, width);
/* 147 */       int y = y(packedCoordinates, width);
/* 148 */       for (int[] direction : DIRECTIONS) {
/* 149 */         int neighborX = j + direction[0];
/* 150 */         int neighborY = y + direction[1];
/* 151 */         int packedNeighborCoordinates = pack(neighborX, neighborY, width);
/* 152 */         if (neighborX >= 0 && neighborY >= 0 && neighborX < width && neighborY < height && 
/* 153 */           distances[packedNeighborCoordinates] > distances[packedCoordinates] + 1) {
/* 154 */           distances[packedNeighborCoordinates] = distances[packedCoordinates] + 1;
/* 155 */           nearestColor[packedNeighborCoordinates] = nearestColor[packedCoordinates];
/* 156 */           queue.enqueue(packedNeighborCoordinates);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 163 */     for (int i = 0; i < width; i++) {
/* 164 */       for (int y = 0; y < height; y++) {
/* 165 */         int color = image.getPixel(i, y);
/* 166 */         if (ARGB.alpha(color) == 0) {
/*     */           
/* 168 */           image.setPixel(i, y, ARGB.color(0, nearestColor[pack(i, y, width)]));
/*     */         } else {
/* 170 */           image.setPixel(i, y, color);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void fillEmptyAreasWithDarkColor(NativeImage image) {
/* 177 */     int width = image.getWidth();
/* 178 */     int height = image.getHeight();
/*     */     
/* 180 */     int darkestColor = -1;
/* 181 */     int minBrightness = Integer.MAX_VALUE;
/*     */     
/* 183 */     for (int x = 0; x < width; x++) {
/* 184 */       for (int y = 0; y < height; y++) {
/* 185 */         int color = image.getPixel(x, y);
/* 186 */         int alpha = ARGB.alpha(color);
/* 187 */         if (alpha != 0) {
/* 188 */           int red = ARGB.red(color);
/* 189 */           int green = ARGB.green(color);
/* 190 */           int blue = ARGB.blue(color);
/* 191 */           int brightness = red + green + blue;
/* 192 */           if (brightness < minBrightness) {
/* 193 */             minBrightness = brightness;
/* 194 */             darkestColor = color;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     int darkRed = 3 * ARGB.red(darkestColor) / 4;
/* 201 */     int darkGreen = 3 * ARGB.green(darkestColor) / 4;
/* 202 */     int darkBlue = 3 * ARGB.blue(darkestColor) / 4;
/* 203 */     int darkenedColor = ARGB.color(0, darkRed, darkGreen, darkBlue);
/*     */     
/* 205 */     for (int i = 0; i < width; i++) {
/* 206 */       for (int y = 0; y < height; y++) {
/* 207 */         int color = image.getPixel(i, y);
/* 208 */         if (ARGB.alpha(color) == 0) {
/* 209 */           image.setPixel(i, y, darkenedColor);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int pack(int x, int y, int width) {
/* 216 */     return x + y * width;
/*     */   }
/*     */   
/*     */   private static int x(int packed, int width) {
/* 220 */     return packed % width;
/*     */   }
/*     */   
/*     */   private static int y(int packed, int width) {
/* 224 */     return packed / width;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/TextureUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */