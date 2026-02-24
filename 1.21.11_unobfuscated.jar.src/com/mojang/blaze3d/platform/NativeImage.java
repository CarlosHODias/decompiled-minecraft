/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.jtracy.MemoryPool;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntUnaryOperator;
/*     */ import net.minecraft.client.gui.font.providers.FreeTypeUtil;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.PngInfo;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.lwjgl.stb.STBIWriteCallback;
/*     */ import org.lwjgl.stb.STBImage;
/*     */ import org.lwjgl.stb.STBImageResize;
/*     */ import org.lwjgl.stb.STBImageWrite;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.freetype.FT_Bitmap;
/*     */ import org.lwjgl.util.freetype.FT_Face;
/*     */ import org.lwjgl.util.freetype.FT_GlyphSlot;
/*     */ import org.lwjgl.util.freetype.FreeType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeImage
/*     */   implements AutoCloseable
/*     */ {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  45 */   private static final MemoryPool MEMORY_POOL = TracyClient.createMemoryPool("NativeImage");
/*     */   
/*  47 */   private static final Set<StandardOpenOption> OPEN_OPTIONS = EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
/*     */   
/*     */   private final Format format;
/*     */   
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final boolean useStbFree;
/*     */   private long pixels;
/*     */   private final long size;
/*     */   
/*     */   public NativeImage(int width, int height, boolean zero) {
/*  58 */     this(Format.RGBA, width, height, zero);
/*     */   }
/*     */   
/*     */   public NativeImage(Format format, int width, int height, boolean zero) {
/*  62 */     if (width <= 0 || height <= 0) {
/*  63 */       throw new IllegalArgumentException("Invalid texture size: " + width + "x" + height);
/*     */     }
/*  65 */     this.format = format;
/*  66 */     this.width = width;
/*  67 */     this.height = height;
/*  68 */     this.size = width * height * format.components();
/*  69 */     this.useStbFree = false;
/*  70 */     if (zero) {
/*  71 */       this.pixels = MemoryUtil.nmemCalloc(1L, this.size);
/*     */     } else {
/*  73 */       this.pixels = MemoryUtil.nmemAlloc(this.size);
/*     */     } 
/*  75 */     MEMORY_POOL.malloc(this.pixels, (int)this.size);
/*  76 */     if (this.pixels == 0L) {
/*  77 */       throw new IllegalStateException("Unable to allocate texture of size " + width + "x" + height + " (" + format.components() + " channels)");
/*     */     }
/*     */   }
/*     */   
/*     */   public NativeImage(Format format, int width, int height, boolean useStbFree, long pixels) {
/*  82 */     if (width <= 0 || height <= 0) {
/*  83 */       throw new IllegalArgumentException("Invalid texture size: " + width + "x" + height);
/*     */     }
/*  85 */     this.format = format;
/*  86 */     this.width = width;
/*  87 */     this.height = height;
/*  88 */     this.useStbFree = useStbFree;
/*  89 */     this.pixels = pixels;
/*  90 */     this.size = width * height * format.components();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return "NativeImage[" + String.valueOf(this.format) + " " + this.width + "x" + this.height + "@" + this.pixels + (this.useStbFree ? "S" : "N") + "]";
/*     */   }
/*     */   
/*     */   private boolean isOutsideBounds(int x, int y) {
/*  99 */     return (x < 0 || x >= this.width || y < 0 || y >= this.height);
/*     */   }
/*     */   
/*     */   public static NativeImage read(InputStream inputStream) throws IOException {
/* 103 */     return read(Format.RGBA, inputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NativeImage read(Format format, InputStream inputStream) throws IOException {
/* 108 */     ByteBuffer file = null;
/*     */     try {
/* 110 */       file = TextureUtil.readResource(inputStream);
/* 111 */       return read(format, file);
/*     */     } finally {
/* 113 */       MemoryUtil.memFree(file);
/* 114 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NativeImage read(ByteBuffer bytes) throws IOException {
/* 119 */     return read(Format.RGBA, bytes);
/*     */   }
/*     */   
/*     */   public static NativeImage read(byte[] bytes) throws IOException {
/* 123 */     MemoryStack memoryStack = MemoryStack.stackGet();
/*     */     
/* 125 */     int bytesAvailable = memoryStack.getPointer();
/*     */     
/* 127 */     if (bytesAvailable < bytes.length) {
/* 128 */       ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
/*     */       try {
/* 130 */         return putAndRead(buffer, bytes);
/*     */       } finally {
/* 132 */         MemoryUtil.memFree(buffer);
/*     */       } 
/*     */     } 
/* 135 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 136 */     try { ByteBuffer buffer = stack.malloc(bytes.length);
/* 137 */       NativeImage nativeImage = putAndRead(buffer, bytes);
/* 138 */       if (stack != null) stack.close();  return nativeImage; }
/*     */     catch (Throwable throwable) { if (stack != null)
/*     */         try { stack.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 143 */      } private static NativeImage putAndRead(ByteBuffer nativeBuffer, byte[] bytes) throws IOException { nativeBuffer.put(bytes);
/* 144 */     nativeBuffer.rewind();
/* 145 */     return read(nativeBuffer); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static NativeImage read(Format format, ByteBuffer bytes) throws IOException {
/* 150 */     if (format != null && !format.supportedByStb()) {
/* 151 */       throw new UnsupportedOperationException("Don't know how to read format " + String.valueOf(format));
/*     */     }
/* 153 */     if (MemoryUtil.memAddress(bytes) == 0L) {
/* 154 */       throw new IllegalArgumentException("Invalid buffer");
/*     */     }
/* 156 */     PngInfo.validateHeader(bytes);
/* 157 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 158 */     try { IntBuffer w = stack.mallocInt(1);
/* 159 */       IntBuffer h = stack.mallocInt(1);
/* 160 */       IntBuffer comp = stack.mallocInt(1);
/*     */       
/* 162 */       ByteBuffer pixels = STBImage.stbi_load_from_memory(bytes, w, h, comp, (format == null) ? 0 : format.components);
/* 163 */       if (pixels == null) {
/* 164 */         throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
/*     */       }
/* 166 */       long address = MemoryUtil.memAddress(pixels);
/* 167 */       MEMORY_POOL.malloc(address, pixels.limit());
/* 168 */       NativeImage nativeImage = new NativeImage((format == null) ? Format.getStbFormat(comp.get(0)) : format, w.get(0), h.get(0), true, address);
/* 169 */       if (stack != null) stack.close();  return nativeImage; } catch (Throwable throwable) { if (stack != null)
/*     */         try { stack.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 173 */      } private void checkAllocated() { if (this.pixels == 0L) {
/* 174 */       throw new IllegalStateException("Image is not allocated.");
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 182 */     if (this.pixels != 0L) {
/* 183 */       if (this.useStbFree) {
/* 184 */         STBImage.nstbi_image_free(this.pixels);
/*     */       } else {
/* 186 */         MemoryUtil.nmemFree(this.pixels);
/*     */       } 
/* 188 */       MEMORY_POOL.free(this.pixels);
/*     */     } 
/* 190 */     this.pixels = 0L;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 194 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 198 */     return this.height;
/*     */   }
/*     */   
/*     */   public Format format() {
/* 202 */     return this.format;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getPixelABGR(int x, int y) {
/* 211 */     if (this.format != Format.RGBA)
/* 212 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixelRGBA only works on RGBA images; have %s", new Object[] { this.format })); 
/* 213 */     if (isOutsideBounds(x, y)) {
/* 214 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", new Object[] { x, y, this.width, this.height }));
/*     */     }
/* 216 */     checkAllocated();
/* 217 */     long offset = (x + y * this.width) * 4L;
/* 218 */     return MemoryUtil.memGetInt(this.pixels + offset);
/*     */   }
/*     */   
/*     */   public int getPixel(int x, int y) {
/* 222 */     return ARGB.fromABGR(getPixelABGR(x, y));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPixelABGR(int x, int y, int pixel) {
/* 231 */     if (this.format != Format.RGBA)
/* 232 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "setPixelRGBA only works on RGBA images; have %s", new Object[] { this.format })); 
/* 233 */     if (isOutsideBounds(x, y)) {
/* 234 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", new Object[] { x, y, this.width, this.height }));
/*     */     }
/* 236 */     checkAllocated();
/* 237 */     long offset = (x + y * this.width) * 4L;
/* 238 */     MemoryUtil.memPutInt(this.pixels + offset, pixel);
/*     */   }
/*     */   
/*     */   public void setPixel(int x, int y, int pixel) {
/* 242 */     setPixelABGR(x, y, ARGB.toABGR(pixel));
/*     */   }
/*     */   
/*     */   public NativeImage mappedCopy(IntUnaryOperator function) {
/* 246 */     if (this.format != Format.RGBA) {
/* 247 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "function application only works on RGBA images; have %s", new Object[] { this.format }));
/*     */     }
/* 249 */     checkAllocated();
/*     */     
/* 251 */     NativeImage result = new NativeImage(this.width, this.height, false);
/* 252 */     int pixelCount = this.width * this.height;
/* 253 */     IntBuffer sourceBuffer = MemoryUtil.memIntBuffer(this.pixels, pixelCount);
/* 254 */     IntBuffer targetBuffer = MemoryUtil.memIntBuffer(result.pixels, pixelCount);
/* 255 */     for (int i = 0; i < pixelCount; i++) {
/* 256 */       int pixel = ARGB.fromABGR(sourceBuffer.get(i));
/* 257 */       int modified = function.applyAsInt(pixel);
/* 258 */       targetBuffer.put(i, ARGB.toABGR(modified));
/*     */     } 
/* 260 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getPixelsABGR() {
/* 266 */     if (this.format != Format.RGBA) {
/* 267 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixels only works on RGBA images; have %s", new Object[] { this.format }));
/*     */     }
/* 269 */     checkAllocated();
/* 270 */     int[] result = new int[this.width * this.height];
/* 271 */     MemoryUtil.memIntBuffer(this.pixels, this.width * this.height).get(result);
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   public int[] getPixels() {
/* 276 */     int[] result = getPixelsABGR();
/* 277 */     for (int i = 0; i < result.length; i++) {
/* 278 */       result[i] = ARGB.fromABGR(result[i]);
/*     */     }
/* 280 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getLuminanceOrAlpha(int x, int y) {
/* 285 */     if (!this.format.hasLuminanceOrAlpha())
/* 286 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "no luminance or alpha in %s", new Object[] { this.format })); 
/* 287 */     if (isOutsideBounds(x, y)) {
/* 288 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", new Object[] { x, y, this.width, this.height }));
/*     */     }
/*     */     
/* 291 */     int offset = (x + y * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
/* 292 */     return MemoryUtil.memGetByte(this.pixels + offset);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int[] makePixelArray() {
/* 298 */     if (this.format != Format.RGBA) {
/* 299 */       throw new UnsupportedOperationException("can only call makePixelArray for RGBA images.");
/*     */     }
/* 301 */     checkAllocated();
/* 302 */     int[] pixels = new int[getWidth() * getHeight()];
/* 303 */     for (int y = 0; y < getHeight(); y++) {
/* 304 */       for (int x = 0; x < getWidth(); x++) {
/* 305 */         pixels[x + y * getWidth()] = getPixel(x, y);
/*     */       }
/*     */     } 
/* 308 */     return pixels;
/*     */   }
/*     */   
/*     */   public void writeToFile(File file) throws IOException {
/* 312 */     writeToFile(file.toPath());
/*     */   }
/*     */   
/*     */   public boolean copyFromFont(FT_Face face, int index) {
/* 316 */     if (this.format.components() != 1) {
/* 317 */       throw new IllegalArgumentException("Can only write fonts into 1-component images.");
/*     */     }
/*     */     
/* 320 */     if (FreeTypeUtil.checkError(FreeType.FT_Load_Glyph(face, index, 4), "Loading glyph")) {
/* 321 */       return false;
/*     */     }
/*     */     
/* 324 */     FT_GlyphSlot glyph = Objects.<FT_GlyphSlot>requireNonNull(face.glyph(), "Glyph not initialized");
/* 325 */     FT_Bitmap bitmap = glyph.bitmap();
/* 326 */     if (bitmap.pixel_mode() != 2) {
/* 327 */       throw new IllegalStateException("Rendered glyph was not 8-bit grayscale");
/*     */     }
/*     */     
/* 330 */     if (bitmap.width() != getWidth() || bitmap.rows() != getHeight()) {
/* 331 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Glyph bitmap of size %sx%s does not match image of size: %sx%s", new Object[] { bitmap.width(), bitmap.rows(), getWidth(), getHeight() }));
/*     */     }
/*     */     
/* 334 */     int bufferSize = bitmap.width() * bitmap.rows();
/* 335 */     ByteBuffer buffer = Objects.<ByteBuffer>requireNonNull(bitmap.buffer(bufferSize), "Glyph has no bitmap");
/* 336 */     MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), this.pixels, bufferSize);
/*     */     
/* 338 */     return true;
/*     */   }
/*     */   
/*     */   private static class WriteCallback extends STBIWriteCallback {
/*     */     private final WritableByteChannel output;
/*     */     private IOException exception;
/*     */     
/*     */     private WriteCallback(WritableByteChannel output) {
/* 346 */       this.output = output;
/*     */     }
/*     */ 
/*     */     
/*     */     public void invoke(long context, long data, int size) {
/* 351 */       ByteBuffer dataBuf = getData(data, size);
/*     */       try {
/* 353 */         this.output.write(dataBuf);
/* 354 */       } catch (IOException e) {
/* 355 */         this.exception = e;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void throwIfException() throws IOException {
/* 360 */       if (this.exception != null) {
/* 361 */         throw this.exception;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToFile(Path file) throws IOException {
/* 368 */     if (!this.format.supportedByStb()) {
/* 369 */       throw new UnsupportedOperationException("Don't know how to write format " + String.valueOf(this.format));
/*     */     }
/* 371 */     checkAllocated();
/* 372 */     WritableByteChannel out = Files.newByteChannel(file, (Set)OPEN_OPTIONS, (FileAttribute<?>[])new FileAttribute[0]); 
/* 373 */     try { if (!writeToChannel(out)) {
/* 374 */         throw new IOException("Could not write image to the PNG file \"" + String.valueOf(file.toAbsolutePath()) + "\": " + STBImage.stbi_failure_reason());
/*     */       }
/* 376 */       if (out != null) out.close();  } catch (Throwable throwable) { if (out != null)
/*     */         try { out.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 380 */      } private boolean writeToChannel(WritableByteChannel output) throws IOException { WriteCallback writer = new WriteCallback(output);
/*     */     try {
/* 382 */       int height = Math.min(getHeight(), Integer.MAX_VALUE / getWidth() / this.format.components());
/* 383 */       if (height < getHeight()) {
/* 384 */         LOGGER.warn("Dropping image height from {} to {} to fit the size into 32-bit signed int", getHeight(), height);
/*     */       }
/* 386 */       if (STBImageWrite.nstbi_write_png_to_func(writer.address(), 0L, getWidth(), height, this.format.components(), this.pixels, 0) == 0) {
/* 387 */         return false;
/*     */       }
/*     */       
/* 390 */       writer.throwIfException();
/* 391 */       return true;
/*     */     } finally {
/* 393 */       writer.free();
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyFrom(NativeImage from) {
/* 402 */     if (from.format() != this.format) {
/* 403 */       throw new UnsupportedOperationException("Image formats don't match.");
/*     */     }
/* 405 */     int components = this.format.components();
/* 406 */     checkAllocated();
/* 407 */     from.checkAllocated();
/* 408 */     if (this.width == from.width) {
/* 409 */       MemoryUtil.memCopy(from.pixels, this.pixels, Math.min(this.size, from.size));
/*     */     } else {
/* 411 */       int minWidth = Math.min(getWidth(), from.getWidth());
/* 412 */       int minHeight = Math.min(getHeight(), from.getHeight());
/* 413 */       for (int y = 0; y < minHeight; y++) {
/* 414 */         int fromOffset = y * from.getWidth() * components;
/* 415 */         int toOffset = y * getWidth() * components;
/* 416 */         MemoryUtil.memCopy(from.pixels + fromOffset, this.pixels + toOffset, minWidth);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillRect(int xs, int ys, int width, int height, int pixel) {
/* 423 */     for (int y = ys; y < ys + height; y++) {
/* 424 */       for (int x = xs; x < xs + width; x++) {
/* 425 */         setPixel(x, y, pixel);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyRect(int startX, int startY, int offsetX, int offsetY, int sizeX, int sizeY, boolean swapX, boolean swapY) {
/* 432 */     copyRect(this, startX, startY, startX + offsetX, startY + offsetY, sizeX, sizeY, swapX, swapY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyRect(NativeImage target, int sourceX, int sourceY, int targetX, int targetY, int sizeX, int sizeY, boolean swapX, boolean swapY) {
/* 440 */     for (int y = 0; y < sizeY; y++) {
/* 441 */       for (int x = 0; x < sizeX; x++) {
/* 442 */         int dx = swapX ? (sizeX - 1 - x) : x;
/* 443 */         int dy = swapY ? (sizeY - 1 - y) : y;
/* 444 */         int source = getPixelABGR(sourceX + x, sourceY + y);
/* 445 */         target.setPixelABGR(targetX + dx, targetY + dy, source);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resizeSubRectTo(int sourceX, int sourceY, int sizeX, int sizeY, NativeImage to) {
/* 453 */     checkAllocated();
/* 454 */     if (to.format() != this.format) {
/* 455 */       throw new UnsupportedOperationException("resizeSubRectTo only works for images of the same format.");
/*     */     }
/* 457 */     int components = this.format.components();
/* 458 */     STBImageResize.nstbir_resize_uint8(this.pixels + ((sourceX + sourceY * 
/* 459 */         getWidth()) * components), sizeX, sizeY, getWidth() * components, to.pixels, 
/* 460 */         to.getWidth(), to.getHeight(), 0, components);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void untrack() {
/* 466 */     DebugMemoryUntracker.untrack(this.pixels);
/*     */   }
/*     */   
/*     */   public long getPointer() {
/* 470 */     return this.pixels;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Format
/*     */   {
/* 477 */     RGBA(4, true, true, true, false, true, 0, 8, 16, 255, 24, true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 483 */     RGB(3, true, true, true, false, false, 0, 8, 16, 255, 255, true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     LUMINANCE_ALPHA(2, false, false, false, true, true, 255, 255, 255, 0, 8, true),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 495 */     LUMINANCE(1, false, false, false, true, false, 0, 0, 0, 0, 255, true);
/*     */     
/*     */     private final int components;
/*     */     
/*     */     private final boolean hasRed;
/*     */     
/*     */     private final boolean hasGreen;
/*     */     
/*     */     private final boolean hasBlue;
/*     */     
/*     */     private final boolean hasLuminance;
/*     */     
/*     */     private final boolean hasAlpha;
/*     */     
/*     */     private final int redOffset;
/*     */     
/*     */     private final int greenOffset;
/*     */     
/*     */     private final int blueOffset;
/*     */     
/*     */     private final int luminanceOffset;
/*     */     private final int alphaOffset;
/*     */     private final boolean supportedByStb;
/*     */     
/*     */     Format(int components, boolean hasRed, boolean hasGreen, boolean hasBlue, boolean hasLuminance, boolean hasAlpha, int redOffset, int greenOffset, int blueOffset, int luminanceOffset, int alphaOffset, boolean supportedByStb) {
/* 520 */       this.components = components;
/* 521 */       this.hasRed = hasRed;
/* 522 */       this.hasGreen = hasGreen;
/* 523 */       this.hasBlue = hasBlue;
/* 524 */       this.hasLuminance = hasLuminance;
/* 525 */       this.hasAlpha = hasAlpha;
/* 526 */       this.redOffset = redOffset;
/* 527 */       this.greenOffset = greenOffset;
/* 528 */       this.blueOffset = blueOffset;
/* 529 */       this.luminanceOffset = luminanceOffset;
/* 530 */       this.alphaOffset = alphaOffset;
/* 531 */       this.supportedByStb = supportedByStb;
/*     */     }
/*     */     
/*     */     public int components() {
/* 535 */       return this.components;
/*     */     }
/*     */     
/*     */     public boolean hasRed() {
/* 539 */       return this.hasRed;
/*     */     }
/*     */     
/*     */     public boolean hasGreen() {
/* 543 */       return this.hasGreen;
/*     */     }
/*     */     
/*     */     public boolean hasBlue() {
/* 547 */       return this.hasBlue;
/*     */     }
/*     */     
/*     */     public boolean hasLuminance() {
/* 551 */       return this.hasLuminance;
/*     */     }
/*     */     
/*     */     public boolean hasAlpha() {
/* 555 */       return this.hasAlpha;
/*     */     }
/*     */     
/*     */     public int redOffset() {
/* 559 */       return this.redOffset;
/*     */     }
/*     */     
/*     */     public int greenOffset() {
/* 563 */       return this.greenOffset;
/*     */     }
/*     */     
/*     */     public int blueOffset() {
/* 567 */       return this.blueOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOffset() {
/* 571 */       return this.luminanceOffset;
/*     */     }
/*     */     
/*     */     public int alphaOffset() {
/* 575 */       return this.alphaOffset;
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrRed() {
/* 579 */       return (this.hasLuminance || this.hasRed);
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrGreen() {
/* 583 */       return (this.hasLuminance || this.hasGreen);
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrBlue() {
/* 587 */       return (this.hasLuminance || this.hasBlue);
/*     */     }
/*     */     
/*     */     public boolean hasLuminanceOrAlpha() {
/* 591 */       return (this.hasLuminance || this.hasAlpha);
/*     */     }
/*     */     
/*     */     public int luminanceOrRedOffset() {
/* 595 */       return this.hasLuminance ? this.luminanceOffset : this.redOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOrGreenOffset() {
/* 599 */       return this.hasLuminance ? this.luminanceOffset : this.greenOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOrBlueOffset() {
/* 603 */       return this.hasLuminance ? this.luminanceOffset : this.blueOffset;
/*     */     }
/*     */     
/*     */     public int luminanceOrAlphaOffset() {
/* 607 */       return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
/*     */     }
/*     */     
/*     */     public boolean supportedByStb() {
/* 611 */       return this.supportedByStb;
/*     */     }
/*     */     
/*     */     private static Format getStbFormat(int i) {
/* 615 */       switch (i) {
/*     */         case 1:
/* 617 */           return LUMINANCE;
/*     */         case 2:
/* 619 */           return LUMINANCE_ALPHA;
/*     */         case 3:
/* 621 */           return RGB;
/*     */       } 
/*     */       
/* 624 */       return RGBA;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/NativeImage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */