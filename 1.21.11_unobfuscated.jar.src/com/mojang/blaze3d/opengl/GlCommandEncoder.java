/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.pipeline.BlendFunction;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.platform.DepthTestFunction;
/*     */ import com.mojang.blaze3d.platform.LogicOp;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuQuery;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL31;
/*     */ import org.lwjgl.opengl.GL32;
/*     */ import org.lwjgl.opengl.GL32C;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlCommandEncoder
/*     */   implements CommandEncoder
/*     */ {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final GlDevice device;
/*     */   
/*     */   private final int readFbo;
/*     */   private final int drawFbo;
/*     */   private RenderPipeline lastPipeline;
/*     */   private boolean inRenderPass;
/*     */   private GlProgram lastProgram;
/*     */   private GlTimerQuery activeTimerQuery;
/*     */   
/*     */   protected GlCommandEncoder(GlDevice device) {
/*  56 */     this.device = device;
/*  57 */     this.readFbo = device.directStateAccess().createFrameBufferObject();
/*  58 */     this.drawFbo = device.directStateAccess().createFrameBufferObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderPass createRenderPass(Supplier<String> label, GpuTextureView colorTexture, OptionalInt clearColor) {
/*  63 */     return createRenderPass(label, colorTexture, clearColor, null, OptionalDouble.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderPass createRenderPass(Supplier<String> label, GpuTextureView colorTexture, OptionalInt clearColor, GpuTextureView depthTexture, OptionalDouble clearDepth) {
/*  68 */     if (this.inRenderPass) {
/*  69 */       throw new IllegalStateException("Close the existing render pass before creating a new one!");
/*     */     }
/*  71 */     if (clearDepth.isPresent() && depthTexture == null) {
/*  72 */       LOGGER.warn("Depth clear value was provided but no depth texture is being used");
/*     */     }
/*  74 */     if (colorTexture.isClosed()) {
/*  75 */       throw new IllegalStateException("Color texture is closed");
/*     */     }
/*  77 */     if ((colorTexture.texture().usage() & 0x8) == 0) {
/*  78 */       throw new IllegalStateException("Color texture must have USAGE_RENDER_ATTACHMENT");
/*     */     }
/*  80 */     if (colorTexture.texture().getDepthOrLayers() > 1) {
/*  81 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported as an attachment");
/*     */     }
/*  83 */     if (depthTexture != null) {
/*  84 */       if (depthTexture.isClosed()) {
/*  85 */         throw new IllegalStateException("Depth texture is closed");
/*     */       }
/*  87 */       if ((depthTexture.texture().usage() & 0x8) == 0) {
/*  88 */         throw new IllegalStateException("Depth texture must have USAGE_RENDER_ATTACHMENT");
/*     */       }
/*  90 */       if (depthTexture.texture().getDepthOrLayers() > 1) {
/*  91 */         throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported as an attachment");
/*     */       }
/*     */     } 
/*     */     
/*  95 */     this.inRenderPass = true;
/*  96 */     this.device.debugLabels().pushDebugGroup(label);
/*  97 */     int fbo = ((GlTextureView)colorTexture).getFbo(this.device.directStateAccess(), (depthTexture == null) ? null : depthTexture.texture());
/*  98 */     GlStateManager._glBindFramebuffer(36160, fbo);
/*  99 */     int clearMask = 0;
/* 100 */     if (clearColor.isPresent()) {
/* 101 */       int argb = clearColor.getAsInt();
/* 102 */       GL11.glClearColor(ARGB.redFloat(argb), ARGB.greenFloat(argb), ARGB.blueFloat(argb), ARGB.alphaFloat(argb));
/* 103 */       clearMask |= 0x4000;
/*     */     } 
/* 105 */     if (depthTexture != null && clearDepth.isPresent()) {
/* 106 */       GL11.glClearDepth(clearDepth.getAsDouble());
/* 107 */       clearMask |= 0x100;
/*     */     } 
/* 109 */     if (clearMask != 0) {
/* 110 */       GlStateManager._disableScissorTest();
/* 111 */       GlStateManager._depthMask(true);
/* 112 */       GlStateManager._colorMask(true, true, true, true);
/* 113 */       GlStateManager._clear(clearMask);
/*     */     } 
/* 115 */     GlStateManager._viewport(0, 0, colorTexture.getWidth(0), colorTexture.getHeight(0));
/* 116 */     this.lastPipeline = null;
/* 117 */     return new GlRenderPass(this, (depthTexture != null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearColorTexture(GpuTexture colorTexture, int clearColor) {
/* 122 */     if (this.inRenderPass) {
/* 123 */       throw new IllegalStateException("Close the existing render pass before creating a new one!");
/*     */     }
/*     */     
/* 126 */     verifyColorTexture(colorTexture);
/*     */     
/* 128 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, ((GlTexture)colorTexture).id, 0, 0, 36160);
/* 129 */     GL11.glClearColor(ARGB.redFloat(clearColor), ARGB.greenFloat(clearColor), ARGB.blueFloat(clearColor), ARGB.alphaFloat(clearColor));
/* 130 */     GlStateManager._disableScissorTest();
/* 131 */     GlStateManager._colorMask(true, true, true, true);
/* 132 */     GlStateManager._clear(16384);
/* 133 */     GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, 0, 0);
/* 134 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, int clearColor, GpuTexture depthTexture, double clearDepth) {
/* 139 */     if (this.inRenderPass) {
/* 140 */       throw new IllegalStateException("Close the existing render pass before creating a new one!");
/*     */     }
/*     */     
/* 143 */     verifyColorTexture(colorTexture);
/* 144 */     verifyDepthTexture(depthTexture);
/*     */     
/* 146 */     int fbo = ((GlTexture)colorTexture).getFbo(this.device.directStateAccess(), depthTexture);
/* 147 */     GlStateManager._glBindFramebuffer(36160, fbo);
/* 148 */     GlStateManager._disableScissorTest();
/* 149 */     GL11.glClearDepth(clearDepth);
/* 150 */     GL11.glClearColor(ARGB.redFloat(clearColor), ARGB.greenFloat(clearColor), ARGB.blueFloat(clearColor), ARGB.alphaFloat(clearColor));
/* 151 */     GlStateManager._depthMask(true);
/* 152 */     GlStateManager._colorMask(true, true, true, true);
/* 153 */     GlStateManager._clear(16640);
/* 154 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearColorAndDepthTextures(GpuTexture colorTexture, int clearColor, GpuTexture depthTexture, double clearDepth, int regionX, int regionY, int regionWidth, int regionHeight) {
/* 159 */     if (this.inRenderPass) {
/* 160 */       throw new IllegalStateException("Close the existing render pass before creating a new one!");
/*     */     }
/*     */     
/* 163 */     verifyColorTexture(colorTexture);
/* 164 */     verifyDepthTexture(depthTexture);
/*     */     
/* 166 */     verifyRegion(colorTexture, regionX, regionY, regionWidth, regionHeight);
/*     */     
/* 168 */     int fbo = ((GlTexture)colorTexture).getFbo(this.device.directStateAccess(), depthTexture);
/* 169 */     GlStateManager._glBindFramebuffer(36160, fbo);
/* 170 */     GlStateManager._scissorBox(regionX, regionY, regionWidth, regionHeight);
/* 171 */     GlStateManager._enableScissorTest();
/* 172 */     GL11.glClearDepth(clearDepth);
/* 173 */     GL11.glClearColor(ARGB.redFloat(clearColor), ARGB.greenFloat(clearColor), ARGB.blueFloat(clearColor), ARGB.alphaFloat(clearColor));
/* 174 */     GlStateManager._depthMask(true);
/* 175 */     GlStateManager._colorMask(true, true, true, true);
/* 176 */     GlStateManager._clear(16640);
/* 177 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */   
/*     */   private void verifyRegion(GpuTexture colorTexture, int regionX, int regionY, int regionWidth, int regionHeight) {
/* 181 */     if (regionX < 0 || regionX >= colorTexture.getWidth(0)) {
/* 182 */       throw new IllegalArgumentException("regionX should not be outside of the texture");
/*     */     }
/* 184 */     if (regionY < 0 || regionY >= colorTexture.getHeight(0)) {
/* 185 */       throw new IllegalArgumentException("regionY should not be outside of the texture");
/*     */     }
/* 187 */     if (regionWidth <= 0) {
/* 188 */       throw new IllegalArgumentException("regionWidth should be greater than 0");
/*     */     }
/* 190 */     if (regionX + regionWidth > colorTexture.getWidth(0)) {
/* 191 */       throw new IllegalArgumentException("regionWidth + regionX should be less than the texture width");
/*     */     }
/* 193 */     if (regionHeight <= 0) {
/* 194 */       throw new IllegalArgumentException("regionHeight should be greater than 0");
/*     */     }
/* 196 */     if (regionY + regionHeight > colorTexture.getHeight(0)) {
/* 197 */       throw new IllegalArgumentException("regionWidth + regionX should be less than the texture height");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearDepthTexture(GpuTexture depthTexture, double clearDepth) {
/* 203 */     if (this.inRenderPass) {
/* 204 */       throw new IllegalStateException("Close the existing render pass before creating a new one!");
/*     */     }
/*     */     
/* 207 */     verifyDepthTexture(depthTexture);
/*     */     
/* 209 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, 0, ((GlTexture)depthTexture).id, 0, 36160);
/* 210 */     GL11.glDrawBuffer(0);
/* 211 */     GL11.glClearDepth(clearDepth);
/* 212 */     GlStateManager._depthMask(true);
/* 213 */     GlStateManager._disableScissorTest();
/* 214 */     GlStateManager._clear(256);
/* 215 */     GL11.glDrawBuffer(36064);
/* 216 */     GlStateManager._glFramebufferTexture2D(36160, 36096, 3553, 0, 0);
/* 217 */     GlStateManager._glBindFramebuffer(36160, 0);
/*     */   }
/*     */   
/*     */   private void verifyColorTexture(GpuTexture colorTexture) {
/* 221 */     if (!colorTexture.getFormat().hasColorAspect()) {
/* 222 */       throw new IllegalStateException("Trying to clear a non-color texture as color");
/*     */     }
/* 224 */     if (colorTexture.isClosed()) {
/* 225 */       throw new IllegalStateException("Color texture is closed");
/*     */     }
/* 227 */     if ((colorTexture.usage() & 0x8) == 0) {
/* 228 */       throw new IllegalStateException("Color texture must have USAGE_RENDER_ATTACHMENT");
/*     */     }
/* 230 */     if (colorTexture.getDepthOrLayers() > 1) {
/* 231 */       throw new UnsupportedOperationException("Clearing a texture with multiple layers or depths is not yet supported");
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyDepthTexture(GpuTexture depthTexture) {
/* 236 */     if (!depthTexture.getFormat().hasDepthAspect()) {
/* 237 */       throw new IllegalStateException("Trying to clear a non-depth texture as depth");
/*     */     }
/* 239 */     if (depthTexture.isClosed()) {
/* 240 */       throw new IllegalStateException("Depth texture is closed");
/*     */     }
/* 242 */     if ((depthTexture.usage() & 0x8) == 0) {
/* 243 */       throw new IllegalStateException("Depth texture must have USAGE_RENDER_ATTACHMENT");
/*     */     }
/* 245 */     if (depthTexture.getDepthOrLayers() > 1) {
/* 246 */       throw new UnsupportedOperationException("Clearing a texture with multiple layers or depths is not yet supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToBuffer(GpuBufferSlice slice, ByteBuffer data) {
/* 252 */     if (this.inRenderPass) {
/* 253 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 256 */     GlBuffer buffer = (GlBuffer)slice.buffer();
/* 257 */     if (buffer.closed) {
/* 258 */       throw new IllegalStateException("Buffer already closed");
/*     */     }
/* 260 */     if ((buffer.usage() & 0x8) == 0) {
/* 261 */       throw new IllegalStateException("Buffer needs USAGE_COPY_DST to be a destination for a copy");
/*     */     }
/*     */     
/* 264 */     int length = data.remaining();
/* 265 */     if (length > slice.length()) {
/* 266 */       throw new IllegalArgumentException("Cannot write more data than the slice allows (attempting to write " + length + " bytes into a slice of length " + slice.length() + ")");
/*     */     }
/* 268 */     if (slice.length() + slice.offset() > buffer.size()) {
/* 269 */       throw new IllegalArgumentException("Cannot write more data than this buffer can hold (attempting to write " + length + " bytes at offset " + slice.offset() + " to " + buffer.size() + " size buffer)");
/*     */     }
/*     */     
/* 272 */     this.device.directStateAccess().bufferSubData(buffer.handle, slice.offset(), data, buffer.usage());
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuBuffer.MappedView mapBuffer(GpuBuffer buffer, boolean read, boolean write) {
/* 277 */     return mapBuffer(buffer.slice(), read, write);
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuBuffer.MappedView mapBuffer(GpuBufferSlice slice, boolean read, boolean write) {
/* 282 */     if (this.inRenderPass) {
/* 283 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 286 */     GlBuffer buffer = (GlBuffer)slice.buffer();
/* 287 */     if (buffer.closed) {
/* 288 */       throw new IllegalStateException("Buffer already closed");
/*     */     }
/* 290 */     if (!read && !write) {
/* 291 */       throw new IllegalArgumentException("At least read or write must be true");
/*     */     }
/* 293 */     if (read && (buffer.usage() & 0x1) == 0) {
/* 294 */       throw new IllegalStateException("Buffer is not readable");
/*     */     }
/* 296 */     if (write && (buffer.usage() & 0x2) == 0) {
/* 297 */       throw new IllegalStateException("Buffer is not writable");
/*     */     }
/* 299 */     if (slice.offset() + slice.length() > buffer.size()) {
/* 300 */       throw new IllegalArgumentException("Cannot map more data than this buffer can hold (attempting to map " + slice.length() + " bytes at offset " + slice.offset() + " from " + buffer.size() + " size buffer)");
/*     */     }
/*     */     
/* 303 */     int flags = 0;
/* 304 */     if (read) {
/* 305 */       flags |= 0x1;
/*     */     }
/* 307 */     if (write) {
/* 308 */       flags |= 0x22;
/*     */     }
/* 310 */     return this.device.getBufferStorage().mapBuffer(this.device.directStateAccess(), buffer, slice.offset(), slice.length(), flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyToBuffer(GpuBufferSlice source, GpuBufferSlice target) {
/* 315 */     if (this.inRenderPass) {
/* 316 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 319 */     GlBuffer sourceBuffer = (GlBuffer)source.buffer();
/* 320 */     if (sourceBuffer.closed)
/* 321 */       throw new IllegalStateException("Source buffer already closed"); 
/* 322 */     if ((sourceBuffer.usage() & 0x10) == 0) {
/* 323 */       throw new IllegalStateException("Source buffer needs USAGE_COPY_SRC to be a source for a copy");
/*     */     }
/*     */     
/* 326 */     GlBuffer targetBuffer = (GlBuffer)target.buffer();
/* 327 */     if (targetBuffer.closed)
/* 328 */       throw new IllegalStateException("Target buffer already closed"); 
/* 329 */     if ((targetBuffer.usage() & 0x8) == 0) {
/* 330 */       throw new IllegalStateException("Target buffer needs USAGE_COPY_DST to be a destination for a copy");
/*     */     }
/*     */     
/* 333 */     if (source.length() != target.length()) {
/* 334 */       throw new IllegalArgumentException("Cannot copy from slice of size " + source.length() + " to slice of size " + target.length() + ", they must be equal");
/*     */     }
/*     */     
/* 337 */     if (source.offset() + source.length() > sourceBuffer.size()) {
/* 338 */       throw new IllegalArgumentException("Cannot copy more data than the source buffer holds (attempting to copy " + source.length() + " bytes at offset " + source.offset() + " from " + sourceBuffer.size() + " size buffer)");
/*     */     }
/*     */     
/* 341 */     if (target.offset() + target.length() > targetBuffer.size()) {
/* 342 */       throw new IllegalArgumentException("Cannot copy more data than the target buffer can hold (attempting to copy " + target.length() + " bytes at offset " + target.offset() + " to " + targetBuffer.size() + " size buffer)");
/*     */     }
/*     */     
/* 345 */     this.device.directStateAccess().copyBufferSubData(sourceBuffer.handle, targetBuffer.handle, source.offset(), target.offset(), source.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, NativeImage source) {
/* 350 */     int width = destination.getWidth(0);
/* 351 */     int height = destination.getHeight(0);
/* 352 */     if (source.getWidth() != width || source.getHeight() != height) {
/* 353 */       throw new IllegalArgumentException("Cannot replace texture of size " + width + "x" + height + " with image of size " + source.getWidth() + "x" + source.getHeight());
/*     */     }
/* 355 */     if (destination.isClosed()) {
/* 356 */       throw new IllegalStateException("Destination texture is closed");
/*     */     }
/* 358 */     if ((destination.usage() & 0x1) == 0) {
/* 359 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST to be a destination for a write");
/*     */     }
/*     */     
/* 362 */     writeToTexture(destination, source, 0, 0, 0, 0, width, height, 0, 0);
/*     */   }
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, NativeImage source, int mipLevel, int depthOrLayer, int destX, int destY, int width, int height, int sourceX, int sourceY) {
/*     */     int target;
/* 367 */     if (this.inRenderPass) {
/* 368 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 371 */     if (mipLevel < 0 || mipLevel >= destination.getMipLevels()) {
/* 372 */       throw new IllegalArgumentException("Invalid mipLevel " + mipLevel + ", must be >= 0 and < " + destination.getMipLevels());
/*     */     }
/* 374 */     if (sourceX + width > source.getWidth() || sourceY + height > source.getHeight()) {
/* 375 */       throw new IllegalArgumentException("Copy source (" + source.getWidth() + "x" + source.getHeight() + ") is not large enough to read a rectangle of " + width + "x" + height + " from " + sourceX + "x" + sourceY);
/*     */     }
/* 377 */     if (destX + width > destination.getWidth(mipLevel) || destY + height > destination.getHeight(mipLevel)) {
/* 378 */       throw new IllegalArgumentException("Dest texture (" + width + "x" + height + ") is not large enough to write a rectangle of " + width + "x" + height + " at " + destX + "x" + destY + " (at mip level " + mipLevel + ")");
/*     */     }
/* 380 */     if (destination.isClosed()) {
/* 381 */       throw new IllegalStateException("Destination texture is closed");
/*     */     }
/* 383 */     if ((destination.usage() & 0x1) == 0) {
/* 384 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST to be a destination for a write");
/*     */     }
/* 386 */     if (depthOrLayer >= destination.getDepthOrLayers()) {
/* 387 */       throw new UnsupportedOperationException("Depth or layer is out of range, must be >= 0 and < " + destination.getDepthOrLayers());
/*     */     }
/*     */ 
/*     */     
/* 391 */     if ((destination.usage() & 0x10) != 0) {
/* 392 */       target = GlConst.CUBEMAP_TARGETS[depthOrLayer % 6];
/* 393 */       GL11.glBindTexture(34067, ((GlTexture)destination).id);
/*     */     } else {
/* 395 */       target = 3553;
/* 396 */       GlStateManager._bindTexture(((GlTexture)destination).id);
/*     */     } 
/* 398 */     GlStateManager._pixelStore(3314, source.getWidth());
/* 399 */     GlStateManager._pixelStore(3316, sourceX);
/* 400 */     GlStateManager._pixelStore(3315, sourceY);
/* 401 */     GlStateManager._pixelStore(3317, source.format().components());
/* 402 */     GlStateManager._texSubImage2D(target, mipLevel, destX, destY, width, height, GlConst.toGl(source.format()), 5121, source.getPointer());
/*     */   }
/*     */   
/*     */   public void writeToTexture(GpuTexture destination, ByteBuffer source, NativeImage.Format format, int mipLevel, int depthOrLayer, int destX, int destY, int width, int height) {
/*     */     int target;
/* 407 */     if (this.inRenderPass) {
/* 408 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 411 */     if (mipLevel < 0 || mipLevel >= destination.getMipLevels()) {
/* 412 */       throw new IllegalArgumentException("Invalid mipLevel, must be >= 0 and < " + destination.getMipLevels());
/*     */     }
/* 414 */     if (width * height * format.components() > source.remaining()) {
/* 415 */       throw new IllegalArgumentException("Copy would overrun the source buffer (remaining length of " + source.remaining() + ", but copy is " + width + "x" + height + " of format " + String.valueOf(format) + ")");
/*     */     }
/* 417 */     if (destX + width > destination.getWidth(mipLevel) || destY + height > destination.getHeight(mipLevel)) {
/* 418 */       throw new IllegalArgumentException("Dest texture (" + destination.getWidth(mipLevel) + "x" + destination.getHeight(mipLevel) + ") is not large enough to write a rectangle of " + width + "x" + height + " at " + destX + "x" + destY);
/*     */     }
/* 420 */     if (destination.isClosed()) {
/* 421 */       throw new IllegalStateException("Destination texture is closed");
/*     */     }
/* 423 */     if ((destination.usage() & 0x1) == 0) {
/* 424 */       throw new IllegalStateException("Color texture must have USAGE_COPY_DST to be a destination for a write");
/*     */     }
/* 426 */     if (depthOrLayer >= destination.getDepthOrLayers()) {
/* 427 */       throw new UnsupportedOperationException("Depth or layer is out of range, must be >= 0 and < " + destination.getDepthOrLayers());
/*     */     }
/*     */ 
/*     */     
/* 431 */     if ((destination.usage() & 0x10) != 0) {
/* 432 */       target = GlConst.CUBEMAP_TARGETS[depthOrLayer % 6];
/* 433 */       GL11.glBindTexture(34067, ((GlTexture)destination).id);
/*     */     } else {
/* 435 */       target = 3553;
/* 436 */       GlStateManager._bindTexture(((GlTexture)destination).id);
/*     */     } 
/* 438 */     GlStateManager._pixelStore(3314, width);
/* 439 */     GlStateManager._pixelStore(3316, 0);
/* 440 */     GlStateManager._pixelStore(3315, 0);
/* 441 */     GlStateManager._pixelStore(3317, format.components());
/* 442 */     GlStateManager._texSubImage2D(target, mipLevel, destX, destY, width, height, GlConst.toGl(format), 5121, source);
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel) {
/* 447 */     if (this.inRenderPass) {
/* 448 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 451 */     copyTextureToBuffer(source, destination, offset, callback, mipLevel, 0, 0, source.getWidth(mipLevel), source.getHeight(mipLevel));
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyTextureToBuffer(GpuTexture source, GpuBuffer destination, long offset, Runnable callback, int mipLevel, int x, int y, int width, int height) {
/* 456 */     if (this.inRenderPass) {
/* 457 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 460 */     if (mipLevel < 0 || mipLevel >= source.getMipLevels()) {
/* 461 */       throw new IllegalArgumentException("Invalid mipLevel " + mipLevel + ", must be >= 0 and < " + source.getMipLevels());
/*     */     }
/* 463 */     if ((source.getWidth(mipLevel) * source.getHeight(mipLevel) * source.getFormat().pixelSize()) + offset > destination.size()) {
/* 464 */       throw new IllegalArgumentException("Buffer of size " + destination.size() + " is not large enough to hold " + width + "x" + height + " pixels (" + source.getFormat().pixelSize() + " bytes each) starting from offset " + offset);
/*     */     }
/* 466 */     if ((source.usage() & 0x2) == 0) {
/* 467 */       throw new IllegalArgumentException("Texture needs USAGE_COPY_SRC to be a source for a copy");
/*     */     }
/* 469 */     if ((destination.usage() & 0x8) == 0) {
/* 470 */       throw new IllegalArgumentException("Buffer needs USAGE_COPY_DST to be a destination for a copy");
/*     */     }
/* 472 */     if (x + width > source.getWidth(mipLevel) || y + height > source.getHeight(mipLevel)) {
/* 473 */       throw new IllegalArgumentException("Copy source texture (" + source.getWidth(mipLevel) + "x" + source.getHeight(mipLevel) + ") is not large enough to read a rectangle of " + width + "x" + height + " from " + x + "," + y);
/*     */     }
/* 475 */     if (source.isClosed()) {
/* 476 */       throw new IllegalStateException("Source texture is closed");
/*     */     }
/* 478 */     if (destination.isClosed()) {
/* 479 */       throw new IllegalStateException("Destination buffer is closed");
/*     */     }
/* 481 */     if (source.getDepthOrLayers() > 1) {
/* 482 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for copying");
/*     */     }
/*     */ 
/*     */     
/* 486 */     GlStateManager.clearGlErrors();
/* 487 */     this.device.directStateAccess().bindFrameBufferTextures(this.readFbo, ((GlTexture)source).glId(), 0, mipLevel, 36008);
/* 488 */     GlStateManager._glBindBuffer(35051, ((GlBuffer)destination).handle);
/* 489 */     GlStateManager._pixelStore(3330, width);
/* 490 */     GlStateManager._readPixels(x, y, width, height, GlConst.toGlExternalId(source.getFormat()), GlConst.toGlType(source.getFormat()), offset);
/* 491 */     RenderSystem.queueFencedTask(callback);
/* 492 */     GlStateManager._glFramebufferTexture2D(36008, 36064, 3553, 0, mipLevel);
/* 493 */     GlStateManager._glBindFramebuffer(36008, 0);
/* 494 */     GlStateManager._glBindBuffer(35051, 0);
/*     */     
/* 496 */     int error = GlStateManager._getError();
/* 497 */     if (error != 0) {
/* 498 */       throw new IllegalStateException("Couldn't perform copyTobuffer for texture " + source.getLabel() + ": GL error " + error);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyTextureToTexture(GpuTexture source, GpuTexture destination, int mipLevel, int destX, int destY, int sourceX, int sourceY, int width, int height) {
/* 504 */     if (this.inRenderPass) {
/* 505 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/*     */     
/* 508 */     if (mipLevel < 0 || mipLevel >= source.getMipLevels() || mipLevel >= destination.getMipLevels()) {
/* 509 */       throw new IllegalArgumentException("Invalid mipLevel " + mipLevel + ", must be >= 0 and < " + source.getMipLevels() + " and < " + destination.getMipLevels());
/*     */     }
/* 511 */     if (destX + width > destination.getWidth(mipLevel) || destY + height > destination.getHeight(mipLevel)) {
/* 512 */       throw new IllegalArgumentException("Dest texture (" + destination.getWidth(mipLevel) + "x" + destination.getHeight(mipLevel) + ") is not large enough to write a rectangle of " + width + "x" + height + " at " + destX + "x" + destY);
/*     */     }
/* 514 */     if (sourceX + width > source.getWidth(mipLevel) || sourceY + height > source.getHeight(mipLevel)) {
/* 515 */       throw new IllegalArgumentException("Source texture (" + source.getWidth(mipLevel) + "x" + source.getHeight(mipLevel) + ") is not large enough to read a rectangle of " + width + "x" + height + " at " + sourceX + "x" + sourceY);
/*     */     }
/* 517 */     if (source.isClosed()) {
/* 518 */       throw new IllegalStateException("Source texture is closed");
/*     */     }
/* 520 */     if (destination.isClosed()) {
/* 521 */       throw new IllegalStateException("Destination texture is closed");
/*     */     }
/* 523 */     if ((source.usage() & 0x2) == 0) {
/* 524 */       throw new IllegalArgumentException("Texture needs USAGE_COPY_SRC to be a source for a copy");
/*     */     }
/* 526 */     if ((destination.usage() & 0x1) == 0) {
/* 527 */       throw new IllegalArgumentException("Texture needs USAGE_COPY_DST to be a destination for a copy");
/*     */     }
/* 529 */     if (source.getDepthOrLayers() > 1) {
/* 530 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for copying");
/*     */     }
/* 532 */     if (destination.getDepthOrLayers() > 1) {
/* 533 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for copying");
/*     */     }
/*     */ 
/*     */     
/* 537 */     GlStateManager.clearGlErrors();
/* 538 */     GlStateManager._disableScissorTest();
/* 539 */     boolean isDepth = source.getFormat().hasDepthAspect();
/* 540 */     int sourceId = ((GlTexture)source).glId();
/* 541 */     int destId = ((GlTexture)destination).glId();
/* 542 */     this.device.directStateAccess().bindFrameBufferTextures(this.readFbo, isDepth ? 0 : sourceId, isDepth ? sourceId : 0, 0, 0);
/* 543 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, isDepth ? 0 : destId, isDepth ? destId : 0, 0, 0);
/* 544 */     this.device.directStateAccess().blitFrameBuffers(this.readFbo, this.drawFbo, sourceX, sourceY, width, height, destX, destY, width, height, isDepth ? 256 : 16384, 9728);
/*     */     
/* 546 */     int error = GlStateManager._getError();
/* 547 */     if (error != 0) {
/* 548 */       throw new IllegalStateException("Couldn't perform copyToTexture for texture " + source.getLabel() + " to " + destination.getLabel() + ": GL error " + error);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void presentTexture(GpuTextureView textureView) {
/* 554 */     if (this.inRenderPass) {
/* 555 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/* 557 */     if (!textureView.texture().getFormat().hasColorAspect()) {
/* 558 */       throw new IllegalStateException("Cannot present a non-color texture!");
/*     */     }
/* 560 */     if ((textureView.texture().usage() & 0x8) == 0) {
/* 561 */       throw new IllegalStateException("Color texture must have USAGE_RENDER_ATTACHMENT to presented to the screen");
/*     */     }
/* 563 */     if (textureView.texture().getDepthOrLayers() > 1) {
/* 564 */       throw new UnsupportedOperationException("Textures with multiple depths or layers are not yet supported for presentation");
/*     */     }
/*     */     
/* 567 */     GlStateManager._disableScissorTest();
/* 568 */     GlStateManager._viewport(0, 0, textureView.getWidth(0), textureView.getHeight(0));
/* 569 */     GlStateManager._depthMask(true);
/* 570 */     GlStateManager._colorMask(true, true, true, true);
/* 571 */     this.device.directStateAccess().bindFrameBufferTextures(this.drawFbo, ((GlTexture)textureView.texture()).glId(), 0, 0, 0);
/* 572 */     this.device.directStateAccess().blitFrameBuffers(this.drawFbo, 0, 0, 0, textureView.getWidth(0), textureView.getHeight(0), 0, 0, textureView.getWidth(0), textureView.getHeight(0), 16384, 9728);
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuFence createFence() {
/* 577 */     if (this.inRenderPass) {
/* 578 */       throw new IllegalStateException("Close the existing render pass before performing additional commands");
/*     */     }
/* 580 */     return new GlFence();
/*     */   }
/*     */   
/*     */   protected <T> void executeDrawMultiple(GlRenderPass renderPass, Collection<RenderPass.Draw<T>> draws, GpuBuffer defaultIndexBuffer, VertexFormat.IndexType defaultIndexType, Collection<String> dynamicUniforms, T uniformArgument) {
/* 584 */     if (!trySetup(renderPass, dynamicUniforms)) {
/*     */       return;
/*     */     }
/*     */     
/* 588 */     if (defaultIndexType == null) {
/* 589 */       defaultIndexType = VertexFormat.IndexType.SHORT;
/*     */     }
/*     */     
/* 592 */     for (RenderPass.Draw<T> draw : draws) {
/* 593 */       VertexFormat.IndexType indexType = (draw.indexType() == null) ? defaultIndexType : draw.indexType();
/* 594 */       renderPass.setIndexBuffer((draw.indexBuffer() == null) ? defaultIndexBuffer : draw.indexBuffer(), indexType);
/* 595 */       renderPass.setVertexBuffer(draw.slot(), draw.vertexBuffer());
/* 596 */       if (GlRenderPass.VALIDATION) {
/* 597 */         if (renderPass.indexBuffer == null) {
/* 598 */           throw new IllegalStateException("Missing index buffer");
/*     */         }
/* 600 */         if (renderPass.indexBuffer.isClosed()) {
/* 601 */           throw new IllegalStateException("Index buffer has been closed!");
/*     */         }
/* 603 */         if (renderPass.vertexBuffers[0] == null)
/*     */         {
/* 605 */           throw new IllegalStateException("Missing vertex buffer at slot 0");
/*     */         }
/* 607 */         if (renderPass.vertexBuffers[0].isClosed()) {
/* 608 */           throw new IllegalStateException("Vertex buffer at slot 0 has been closed!");
/*     */         }
/*     */       } 
/* 611 */       BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer = draw.uniformUploaderConsumer();
/* 612 */       if (uniformUploaderConsumer != null)
/* 613 */         uniformUploaderConsumer.accept(uniformArgument, (name, buffer) -> { Uniform patt1$temp = renderPass.pipeline.program().getUniform(name); if (patt1$temp instanceof Uniform.Ubo) { Uniform.Ubo $b$0 = (Uniform.Ubo)patt1$temp; try { int patt2$temp = $b$0.blockBinding(), blockBinding = patt2$temp; GL32.glBindBufferRange(35345, blockBinding, ((GlBuffer)buffer.buffer()).handle, buffer.offset(), buffer.length()); }
/* 614 */                 catch (Throwable throwable)
/*     */                 { throw new MatchException(throwable.toString(), throwable); }
/*     */                  }
/*     */             
/*     */             }); 
/* 619 */       drawFromBuffers(renderPass, 0, draw.firstIndex(), draw.indexCount(), indexType, renderPass.pipeline, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void executeDraw(GlRenderPass renderPass, int baseVertex, int firstIndex, int drawCount, VertexFormat.IndexType indexType, int instanceCount) {
/* 624 */     if (!trySetup(renderPass, Collections.emptyList())) {
/*     */       return;
/*     */     }
/*     */     
/* 628 */     if (GlRenderPass.VALIDATION) {
/* 629 */       if (indexType != null) {
/* 630 */         if (renderPass.indexBuffer == null) {
/* 631 */           throw new IllegalStateException("Missing index buffer");
/*     */         }
/* 633 */         if (renderPass.indexBuffer.isClosed()) {
/* 634 */           throw new IllegalStateException("Index buffer has been closed!");
/*     */         }
/* 636 */         if ((renderPass.indexBuffer.usage() & 0x40) == 0) {
/* 637 */           throw new IllegalStateException("Index buffer must have GpuBuffer.USAGE_INDEX!");
/*     */         }
/*     */       } 
/* 640 */       GlRenderPipeline pipeline = renderPass.pipeline;
/* 641 */       if (renderPass.vertexBuffers[0] == null && pipeline != null && !pipeline.info().getVertexFormat().getElements().isEmpty()) {
/* 642 */         throw new IllegalStateException("Vertex format contains elements but vertex buffer at slot 0 is null");
/*     */       }
/* 644 */       if (renderPass.vertexBuffers[0] != null && renderPass.vertexBuffers[0].isClosed()) {
/* 645 */         throw new IllegalStateException("Vertex buffer at slot 0 has been closed!");
/*     */       }
/* 647 */       if (renderPass.vertexBuffers[0] != null && (renderPass.vertexBuffers[0].usage() & 0x20) == 0) {
/* 648 */         throw new IllegalStateException("Vertex buffer must have GpuBuffer.USAGE_VERTEX!");
/*     */       }
/*     */     } 
/*     */     
/* 652 */     drawFromBuffers(renderPass, baseVertex, firstIndex, drawCount, indexType, renderPass.pipeline, instanceCount);
/*     */   }
/*     */   
/*     */   private void drawFromBuffers(GlRenderPass renderPass, int baseVertex, int firstIndex, int drawCount, VertexFormat.IndexType indexType, GlRenderPipeline pipeline, int instanceCount) {
/* 656 */     this.device.vertexArrayCache().bindVertexArray(pipeline.info().getVertexFormat(), (GlBuffer)renderPass.vertexBuffers[0]);
/*     */     
/* 658 */     if (indexType != null) {
/* 659 */       GlStateManager._glBindBuffer(34963, ((GlBuffer)renderPass.indexBuffer).handle);
/* 660 */       if (instanceCount > 1) {
/* 661 */         if (baseVertex > 0) {
/* 662 */           GL32.glDrawElementsInstancedBaseVertex(GlConst.toGl(pipeline.info().getVertexFormatMode()), drawCount, GlConst.toGl(indexType), firstIndex * indexType.bytes, instanceCount, baseVertex);
/*     */         } else {
/* 664 */           GL31.glDrawElementsInstanced(GlConst.toGl(pipeline.info().getVertexFormatMode()), drawCount, GlConst.toGl(indexType), firstIndex * indexType.bytes, instanceCount);
/*     */         } 
/* 666 */       } else if (baseVertex > 0) {
/* 667 */         GL32.glDrawElementsBaseVertex(GlConst.toGl(pipeline.info().getVertexFormatMode()), drawCount, GlConst.toGl(indexType), firstIndex * indexType.bytes, baseVertex);
/*     */       } else {
/* 669 */         GlStateManager._drawElements(GlConst.toGl(pipeline.info().getVertexFormatMode()), drawCount, GlConst.toGl(indexType), firstIndex * indexType.bytes);
/*     */       } 
/* 671 */     } else if (instanceCount > 1) {
/* 672 */       GL31.glDrawArraysInstanced(GlConst.toGl(pipeline.info().getVertexFormatMode()), baseVertex, drawCount, instanceCount);
/*     */     } else {
/* 674 */       GlStateManager._drawArrays(GlConst.toGl(pipeline.info().getVertexFormatMode()), baseVertex, drawCount);
/*     */     } 
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
/*     */   private boolean trySetup(GlRenderPass renderPass, Collection<String> dynamicUniforms) {
/*     */     // Byte code:
/*     */     //   0: getstatic com/mojang/blaze3d/opengl/GlRenderPass.VALIDATION : Z
/*     */     //   3: ifeq -> 555
/*     */     //   6: aload_1
/*     */     //   7: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   10: ifnonnull -> 24
/*     */     //   13: new java/lang/IllegalStateException
/*     */     //   16: dup
/*     */     //   17: ldc_w 'Can't draw without a render pipeline'
/*     */     //   20: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   23: athrow
/*     */     //   24: aload_1
/*     */     //   25: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   28: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   31: getstatic com/mojang/blaze3d/opengl/GlProgram.INVALID_PROGRAM : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   34: if_acmpne -> 48
/*     */     //   37: new java/lang/IllegalStateException
/*     */     //   40: dup
/*     */     //   41: ldc_w 'Pipeline contains invalid shader program'
/*     */     //   44: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   47: athrow
/*     */     //   48: aload_1
/*     */     //   49: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   52: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   55: invokevirtual getUniforms : ()Ljava/util/List;
/*     */     //   58: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   63: astore_3
/*     */     //   64: aload_3
/*     */     //   65: invokeinterface hasNext : ()Z
/*     */     //   70: ifeq -> 300
/*     */     //   73: aload_3
/*     */     //   74: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   79: checkcast com/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription
/*     */     //   82: astore #4
/*     */     //   84: aload_1
/*     */     //   85: getfield uniforms : Ljava/util/HashMap;
/*     */     //   88: aload #4
/*     */     //   90: invokevirtual name : ()Ljava/lang/String;
/*     */     //   93: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   96: checkcast com/mojang/blaze3d/buffers/GpuBufferSlice
/*     */     //   99: astore #5
/*     */     //   101: aload_2
/*     */     //   102: aload #4
/*     */     //   104: invokevirtual name : ()Ljava/lang/String;
/*     */     //   107: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   112: ifeq -> 118
/*     */     //   115: goto -> 64
/*     */     //   118: aload #5
/*     */     //   120: ifnonnull -> 149
/*     */     //   123: new java/lang/IllegalStateException
/*     */     //   126: dup
/*     */     //   127: aload #4
/*     */     //   129: invokevirtual name : ()Ljava/lang/String;
/*     */     //   132: aload #4
/*     */     //   134: invokevirtual type : ()Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   137: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   140: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   145: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   148: athrow
/*     */     //   149: aload #4
/*     */     //   151: invokevirtual type : ()Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   154: getstatic com/mojang/blaze3d/shaders/UniformType.UNIFORM_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   157: if_acmpne -> 222
/*     */     //   160: aload #5
/*     */     //   162: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   165: invokevirtual isClosed : ()Z
/*     */     //   168: ifeq -> 189
/*     */     //   171: new java/lang/IllegalStateException
/*     */     //   174: dup
/*     */     //   175: aload #4
/*     */     //   177: invokevirtual name : ()Ljava/lang/String;
/*     */     //   180: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   185: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   188: athrow
/*     */     //   189: aload #5
/*     */     //   191: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   194: invokevirtual usage : ()I
/*     */     //   197: sipush #128
/*     */     //   200: iand
/*     */     //   201: ifne -> 222
/*     */     //   204: new java/lang/IllegalStateException
/*     */     //   207: dup
/*     */     //   208: aload #4
/*     */     //   210: invokevirtual name : ()Ljava/lang/String;
/*     */     //   213: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   218: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   221: athrow
/*     */     //   222: aload #4
/*     */     //   224: invokevirtual type : ()Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   227: getstatic com/mojang/blaze3d/shaders/UniformType.TEXEL_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   230: if_acmpne -> 297
/*     */     //   233: aload #5
/*     */     //   235: invokevirtual offset : ()J
/*     */     //   238: lconst_0
/*     */     //   239: lcmp
/*     */     //   240: ifne -> 260
/*     */     //   243: aload #5
/*     */     //   245: invokevirtual length : ()J
/*     */     //   248: aload #5
/*     */     //   250: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   253: invokevirtual size : ()J
/*     */     //   256: lcmp
/*     */     //   257: ifeq -> 271
/*     */     //   260: new java/lang/IllegalStateException
/*     */     //   263: dup
/*     */     //   264: ldc_w 'Uniform texel buffers do not support a slice of a buffer, must be entire buffer'
/*     */     //   267: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   270: athrow
/*     */     //   271: aload #4
/*     */     //   273: invokevirtual textureFormat : ()Lcom/mojang/blaze3d/textures/TextureFormat;
/*     */     //   276: ifnonnull -> 297
/*     */     //   279: new java/lang/IllegalStateException
/*     */     //   282: dup
/*     */     //   283: aload #4
/*     */     //   285: invokevirtual name : ()Ljava/lang/String;
/*     */     //   288: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   293: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   296: athrow
/*     */     //   297: goto -> 64
/*     */     //   300: aload_1
/*     */     //   301: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   304: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   307: invokevirtual getUniforms : ()Ljava/util/Map;
/*     */     //   310: invokeinterface entrySet : ()Ljava/util/Set;
/*     */     //   315: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   320: astore_3
/*     */     //   321: aload_3
/*     */     //   322: invokeinterface hasNext : ()Z
/*     */     //   327: ifeq -> 511
/*     */     //   330: aload_3
/*     */     //   331: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   336: checkcast java/util/Map$Entry
/*     */     //   339: astore #4
/*     */     //   341: aload #4
/*     */     //   343: invokeinterface getValue : ()Ljava/lang/Object;
/*     */     //   348: instanceof com/mojang/blaze3d/opengl/Uniform$Sampler
/*     */     //   351: ifeq -> 508
/*     */     //   354: aload #4
/*     */     //   356: invokeinterface getKey : ()Ljava/lang/Object;
/*     */     //   361: checkcast java/lang/String
/*     */     //   364: astore #5
/*     */     //   366: aload_1
/*     */     //   367: getfield samplers : Ljava/util/HashMap;
/*     */     //   370: aload #5
/*     */     //   372: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   375: checkcast com/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler
/*     */     //   378: astore #6
/*     */     //   380: aload #6
/*     */     //   382: ifnonnull -> 400
/*     */     //   385: new java/lang/IllegalStateException
/*     */     //   388: dup
/*     */     //   389: aload #5
/*     */     //   391: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   396: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   399: athrow
/*     */     //   400: aload #6
/*     */     //   402: invokevirtual view : ()Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   405: astore #7
/*     */     //   407: aload #7
/*     */     //   409: invokevirtual isClosed : ()Z
/*     */     //   412: ifeq -> 438
/*     */     //   415: new java/lang/IllegalStateException
/*     */     //   418: dup
/*     */     //   419: aload #5
/*     */     //   421: aload #7
/*     */     //   423: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   426: invokevirtual getLabel : ()Ljava/lang/String;
/*     */     //   429: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   434: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   437: athrow
/*     */     //   438: aload #7
/*     */     //   440: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   443: invokevirtual usage : ()I
/*     */     //   446: iconst_4
/*     */     //   447: iand
/*     */     //   448: ifne -> 474
/*     */     //   451: new java/lang/IllegalStateException
/*     */     //   454: dup
/*     */     //   455: aload #5
/*     */     //   457: aload #7
/*     */     //   459: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   462: invokevirtual getLabel : ()Ljava/lang/String;
/*     */     //   465: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   470: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   473: athrow
/*     */     //   474: aload #6
/*     */     //   476: invokevirtual sampler : ()Lcom/mojang/blaze3d/opengl/GlSampler;
/*     */     //   479: invokevirtual isClosed : ()Z
/*     */     //   482: ifeq -> 508
/*     */     //   485: new java/lang/IllegalStateException
/*     */     //   488: dup
/*     */     //   489: aload #5
/*     */     //   491: aload #7
/*     */     //   493: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   496: invokevirtual getLabel : ()Ljava/lang/String;
/*     */     //   499: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   504: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   507: athrow
/*     */     //   508: goto -> 321
/*     */     //   511: aload_1
/*     */     //   512: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   515: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   518: invokevirtual wantsDepthTexture : ()Z
/*     */     //   521: ifeq -> 577
/*     */     //   524: aload_1
/*     */     //   525: invokevirtual hasDepthTexture : ()Z
/*     */     //   528: ifne -> 577
/*     */     //   531: getstatic com/mojang/blaze3d/opengl/GlCommandEncoder.LOGGER : Lorg/slf4j/Logger;
/*     */     //   534: ldc_w 'Render pipeline {} wants a depth texture but none was provided - this is probably a bug'
/*     */     //   537: aload_1
/*     */     //   538: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   541: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   544: invokevirtual getLocation : ()Lnet/minecraft/resources/Identifier;
/*     */     //   547: invokeinterface warn : (Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   552: goto -> 577
/*     */     //   555: aload_1
/*     */     //   556: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   559: ifnull -> 575
/*     */     //   562: aload_1
/*     */     //   563: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   566: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   569: getstatic com/mojang/blaze3d/opengl/GlProgram.INVALID_PROGRAM : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   572: if_acmpne -> 577
/*     */     //   575: iconst_0
/*     */     //   576: ireturn
/*     */     //   577: aload_1
/*     */     //   578: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   581: invokevirtual info : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   584: astore_3
/*     */     //   585: aload_1
/*     */     //   586: getfield pipeline : Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/*     */     //   589: invokevirtual program : ()Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   592: astore #4
/*     */     //   594: aload_0
/*     */     //   595: aload_3
/*     */     //   596: invokevirtual applyPipelineState : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V
/*     */     //   599: aload_0
/*     */     //   600: getfield lastProgram : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   603: aload #4
/*     */     //   605: if_acmpeq -> 612
/*     */     //   608: iconst_1
/*     */     //   609: goto -> 613
/*     */     //   612: iconst_0
/*     */     //   613: istore #5
/*     */     //   615: iload #5
/*     */     //   617: ifeq -> 634
/*     */     //   620: aload #4
/*     */     //   622: invokevirtual getProgramId : ()I
/*     */     //   625: invokestatic _glUseProgram : (I)V
/*     */     //   628: aload_0
/*     */     //   629: aload #4
/*     */     //   631: putfield lastProgram : Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   634: aload #4
/*     */     //   636: invokevirtual getUniforms : ()Ljava/util/Map;
/*     */     //   639: invokeinterface entrySet : ()Ljava/util/Set;
/*     */     //   644: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   649: astore #6
/*     */     //   651: aload #6
/*     */     //   653: invokeinterface hasNext : ()Z
/*     */     //   658: ifeq -> 1147
/*     */     //   661: aload #6
/*     */     //   663: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   668: checkcast java/util/Map$Entry
/*     */     //   671: astore #7
/*     */     //   673: aload #7
/*     */     //   675: invokeinterface getKey : ()Ljava/lang/Object;
/*     */     //   680: checkcast java/lang/String
/*     */     //   683: astore #8
/*     */     //   685: aload_1
/*     */     //   686: getfield dirtyUniforms : Ljava/util/Set;
/*     */     //   689: aload #8
/*     */     //   691: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   696: istore #9
/*     */     //   698: aload #7
/*     */     //   700: invokeinterface getValue : ()Ljava/lang/Object;
/*     */     //   705: checkcast com/mojang/blaze3d/opengl/Uniform
/*     */     //   708: dup
/*     */     //   709: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   712: pop
/*     */     //   713: astore #10
/*     */     //   715: iconst_0
/*     */     //   716: istore #11
/*     */     //   718: aload #10
/*     */     //   720: iload #11
/*     */     //   722: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   727: tableswitch default -> 752, 0 -> 762, 1 -> 834, 2 -> 963
/*     */     //   752: new java/lang/MatchException
/*     */     //   755: dup
/*     */     //   756: aconst_null
/*     */     //   757: aconst_null
/*     */     //   758: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   761: athrow
/*     */     //   762: aload #10
/*     */     //   764: checkcast com/mojang/blaze3d/opengl/Uniform$Ubo
/*     */     //   767: astore #12
/*     */     //   769: aload #12
/*     */     //   771: invokevirtual blockBinding : ()I
/*     */     //   774: istore #14
/*     */     //   776: iload #14
/*     */     //   778: istore #13
/*     */     //   780: iload #9
/*     */     //   782: ifne -> 788
/*     */     //   785: goto -> 651
/*     */     //   788: aload_1
/*     */     //   789: getfield uniforms : Ljava/util/HashMap;
/*     */     //   792: aload #8
/*     */     //   794: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   797: checkcast com/mojang/blaze3d/buffers/GpuBufferSlice
/*     */     //   800: astore #14
/*     */     //   802: ldc_w 35345
/*     */     //   805: iload #13
/*     */     //   807: aload #14
/*     */     //   809: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   812: checkcast com/mojang/blaze3d/opengl/GlBuffer
/*     */     //   815: getfield handle : I
/*     */     //   818: aload #14
/*     */     //   820: invokevirtual offset : ()J
/*     */     //   823: aload #14
/*     */     //   825: invokevirtual length : ()J
/*     */     //   828: invokestatic glBindBufferRange : (IIIJJ)V
/*     */     //   831: goto -> 1144
/*     */     //   834: aload #10
/*     */     //   836: checkcast com/mojang/blaze3d/opengl/Uniform$Utb
/*     */     //   839: astore #14
/*     */     //   841: aload #14
/*     */     //   843: invokevirtual location : ()I
/*     */     //   846: istore #19
/*     */     //   848: iload #19
/*     */     //   850: istore #15
/*     */     //   852: aload #14
/*     */     //   854: invokevirtual samplerIndex : ()I
/*     */     //   857: istore #19
/*     */     //   859: iload #19
/*     */     //   861: istore #16
/*     */     //   863: aload #14
/*     */     //   865: invokevirtual format : ()Lcom/mojang/blaze3d/textures/TextureFormat;
/*     */     //   868: astore #19
/*     */     //   870: aload #19
/*     */     //   872: astore #17
/*     */     //   874: aload #14
/*     */     //   876: invokevirtual texture : ()I
/*     */     //   879: istore #19
/*     */     //   881: iload #19
/*     */     //   883: istore #18
/*     */     //   885: iload #5
/*     */     //   887: ifne -> 895
/*     */     //   890: iload #9
/*     */     //   892: ifeq -> 902
/*     */     //   895: iload #15
/*     */     //   897: iload #16
/*     */     //   899: invokestatic _glUniform1i : (II)V
/*     */     //   902: ldc_w 33984
/*     */     //   905: iload #16
/*     */     //   907: iadd
/*     */     //   908: invokestatic _activeTexture : (I)V
/*     */     //   911: ldc_w 35882
/*     */     //   914: iload #18
/*     */     //   916: invokestatic glBindTexture : (II)V
/*     */     //   919: iload #9
/*     */     //   921: ifeq -> 1144
/*     */     //   924: aload_1
/*     */     //   925: getfield uniforms : Ljava/util/HashMap;
/*     */     //   928: aload #8
/*     */     //   930: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   933: checkcast com/mojang/blaze3d/buffers/GpuBufferSlice
/*     */     //   936: astore #19
/*     */     //   938: ldc_w 35882
/*     */     //   941: aload #17
/*     */     //   943: invokestatic toGlInternalId : (Lcom/mojang/blaze3d/textures/TextureFormat;)I
/*     */     //   946: aload #19
/*     */     //   948: invokevirtual buffer : ()Lcom/mojang/blaze3d/buffers/GpuBuffer;
/*     */     //   951: checkcast com/mojang/blaze3d/opengl/GlBuffer
/*     */     //   954: getfield handle : I
/*     */     //   957: invokestatic glTexBuffer : (III)V
/*     */     //   960: goto -> 1144
/*     */     //   963: aload #10
/*     */     //   965: checkcast com/mojang/blaze3d/opengl/Uniform$Sampler
/*     */     //   968: astore #19
/*     */     //   970: aload #19
/*     */     //   972: invokevirtual location : ()I
/*     */     //   975: istore #22
/*     */     //   977: iload #22
/*     */     //   979: istore #20
/*     */     //   981: aload #19
/*     */     //   983: invokevirtual samplerIndex : ()I
/*     */     //   986: istore #22
/*     */     //   988: iload #22
/*     */     //   990: istore #21
/*     */     //   992: aload_1
/*     */     //   993: getfield samplers : Ljava/util/HashMap;
/*     */     //   996: aload #8
/*     */     //   998: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   1001: checkcast com/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler
/*     */     //   1004: astore #22
/*     */     //   1006: aload #22
/*     */     //   1008: ifnonnull -> 1014
/*     */     //   1011: goto -> 651
/*     */     //   1014: aload #22
/*     */     //   1016: invokevirtual view : ()Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   1019: astore #23
/*     */     //   1021: iload #5
/*     */     //   1023: ifne -> 1031
/*     */     //   1026: iload #9
/*     */     //   1028: ifeq -> 1038
/*     */     //   1031: iload #20
/*     */     //   1033: iload #21
/*     */     //   1035: invokestatic _glUniform1i : (II)V
/*     */     //   1038: ldc_w 33984
/*     */     //   1041: iload #21
/*     */     //   1043: iadd
/*     */     //   1044: invokestatic _activeTexture : (I)V
/*     */     //   1047: aload #23
/*     */     //   1049: invokevirtual texture : ()Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   1052: astore #24
/*     */     //   1054: aload #24
/*     */     //   1056: invokevirtual usage : ()I
/*     */     //   1059: bipush #16
/*     */     //   1061: iand
/*     */     //   1062: ifeq -> 1084
/*     */     //   1065: ldc_w 34067
/*     */     //   1068: istore #25
/*     */     //   1070: ldc_w 34067
/*     */     //   1073: aload #24
/*     */     //   1075: getfield id : I
/*     */     //   1078: invokestatic glBindTexture : (II)V
/*     */     //   1081: goto -> 1097
/*     */     //   1084: sipush #3553
/*     */     //   1087: istore #25
/*     */     //   1089: aload #24
/*     */     //   1091: getfield id : I
/*     */     //   1094: invokestatic _bindTexture : (I)V
/*     */     //   1097: iload #21
/*     */     //   1099: aload #22
/*     */     //   1101: invokevirtual sampler : ()Lcom/mojang/blaze3d/opengl/GlSampler;
/*     */     //   1104: invokevirtual getId : ()I
/*     */     //   1107: invokestatic glBindSampler : (II)V
/*     */     //   1110: iload #25
/*     */     //   1112: ldc_w 33084
/*     */     //   1115: aload #23
/*     */     //   1117: invokevirtual baseMipLevel : ()I
/*     */     //   1120: invokestatic _texParameter : (III)V
/*     */     //   1123: iload #25
/*     */     //   1125: ldc_w 33085
/*     */     //   1128: aload #23
/*     */     //   1130: invokevirtual baseMipLevel : ()I
/*     */     //   1133: aload #23
/*     */     //   1135: invokevirtual mipLevels : ()I
/*     */     //   1138: iadd
/*     */     //   1139: iconst_1
/*     */     //   1140: isub
/*     */     //   1141: invokestatic _texParameter : (III)V
/*     */     //   1144: goto -> 651
/*     */     //   1147: aload_1
/*     */     //   1148: getfield dirtyUniforms : Ljava/util/Set;
/*     */     //   1151: invokeinterface clear : ()V
/*     */     //   1156: aload_1
/*     */     //   1157: invokevirtual isScissorEnabled : ()Z
/*     */     //   1160: ifeq -> 1188
/*     */     //   1163: invokestatic _enableScissorTest : ()V
/*     */     //   1166: aload_1
/*     */     //   1167: invokevirtual getScissorX : ()I
/*     */     //   1170: aload_1
/*     */     //   1171: invokevirtual getScissorY : ()I
/*     */     //   1174: aload_1
/*     */     //   1175: invokevirtual getScissorWidth : ()I
/*     */     //   1178: aload_1
/*     */     //   1179: invokevirtual getScissorHeight : ()I
/*     */     //   1182: invokestatic _scissorBox : (IIII)V
/*     */     //   1185: goto -> 1191
/*     */     //   1188: invokestatic _disableScissorTest : ()V
/*     */     //   1191: iconst_1
/*     */     //   1192: ireturn
/*     */     //   1193: astore #6
/*     */     //   1195: new java/lang/MatchException
/*     */     //   1198: dup
/*     */     //   1199: aload #6
/*     */     //   1201: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   1204: aload #6
/*     */     //   1206: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   1209: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #679	-> 0
/*     */     //   #681	-> 6
/*     */     //   #682	-> 13
/*     */     //   #685	-> 24
/*     */     //   #686	-> 37
/*     */     //   #689	-> 48
/*     */     //   #690	-> 84
/*     */     //   #691	-> 101
/*     */     //   #693	-> 115
/*     */     //   #695	-> 118
/*     */     //   #696	-> 123
/*     */     //   #699	-> 149
/*     */     //   #700	-> 160
/*     */     //   #701	-> 171
/*     */     //   #703	-> 189
/*     */     //   #704	-> 204
/*     */     //   #707	-> 222
/*     */     //   #708	-> 233
/*     */     //   #709	-> 260
/*     */     //   #711	-> 271
/*     */     //   #712	-> 279
/*     */     //   #715	-> 297
/*     */     //   #717	-> 300
/*     */     //   #718	-> 341
/*     */     //   #719	-> 354
/*     */     //   #720	-> 366
/*     */     //   #721	-> 380
/*     */     //   #722	-> 385
/*     */     //   #724	-> 400
/*     */     //   #725	-> 407
/*     */     //   #726	-> 415
/*     */     //   #727	-> 438
/*     */     //   #728	-> 451
/*     */     //   #730	-> 474
/*     */     //   #731	-> 485
/*     */     //   #735	-> 508
/*     */     //   #737	-> 511
/*     */     //   #738	-> 531
/*     */     //   #740	-> 555
/*     */     //   #741	-> 575
/*     */     //   #747	-> 577
/*     */     //   #748	-> 585
/*     */     //   #750	-> 594
/*     */     //   #752	-> 599
/*     */     //   #753	-> 615
/*     */     //   #754	-> 620
/*     */     //   #755	-> 628
/*     */     //   #758	-> 634
/*     */     //   #759	-> 673
/*     */     //   #760	-> 685
/*     */     //   #762	-> 698
/*     */     //   #763	-> 762
/*     */     //   #764	-> 780
/*     */     //   #765	-> 785
/*     */     //   #767	-> 788
/*     */     //   #768	-> 802
/*     */     //   #769	-> 831
/*     */     //   #770	-> 834
/*     */     //   #771	-> 885
/*     */     //   #772	-> 895
/*     */     //   #774	-> 902
/*     */     //   #775	-> 911
/*     */     //   #776	-> 919
/*     */     //   #777	-> 924
/*     */     //   #778	-> 938
/*     */     //   #779	-> 960
/*     */     //   #781	-> 963
/*     */     //   #782	-> 992
/*     */     //   #783	-> 1006
/*     */     //   #784	-> 1011
/*     */     //   #787	-> 1014
/*     */     //   #788	-> 1021
/*     */     //   #789	-> 1031
/*     */     //   #791	-> 1038
/*     */     //   #792	-> 1047
/*     */     //   #794	-> 1054
/*     */     //   #795	-> 1065
/*     */     //   #796	-> 1070
/*     */     //   #798	-> 1084
/*     */     //   #799	-> 1089
/*     */     //   #801	-> 1097
/*     */     //   #802	-> 1110
/*     */     //   #803	-> 1123
/*     */     //   #806	-> 1144
/*     */     //   #807	-> 1147
/*     */     //   #809	-> 1156
/*     */     //   #810	-> 1163
/*     */     //   #811	-> 1166
/*     */     //   #813	-> 1188
/*     */     //   #815	-> 1191
/*     */     //   #781	-> 1193
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   101	196	5	value	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*     */     //   84	213	4	uniform	Lcom/mojang/blaze3d/pipeline/RenderPipeline$UniformDescription;
/*     */     //   407	101	7	textureView	Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   366	142	5	name	Ljava/lang/String;
/*     */     //   380	128	6	viewAndSampler	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */     //   341	167	4	entry	Ljava/util/Map$Entry;
/*     */     //   802	29	14	bufferView	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*     */     //   780	54	13	blockBinding	I
/*     */     //   938	22	19	bufferView	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*     */     //   852	111	15	location	I
/*     */     //   863	100	16	samplerIndex	I
/*     */     //   874	89	17	format	Lcom/mojang/blaze3d/textures/TextureFormat;
/*     */     //   885	78	18	texture	I
/*     */     //   1070	14	25	target	I
/*     */     //   1006	138	22	viewAndSampler	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/*     */     //   1021	123	23	textureView	Lcom/mojang/blaze3d/opengl/GlTextureView;
/*     */     //   1054	90	24	texture	Lcom/mojang/blaze3d/opengl/GlTexture;
/*     */     //   1089	55	25	target	I
/*     */     //   981	163	20	location	I
/*     */     //   992	152	21	samplerIndex	I
/*     */     //   685	459	8	name	Ljava/lang/String;
/*     */     //   698	446	9	isDirty	Z
/*     */     //   673	471	7	entry	Ljava/util/Map$Entry;
/*     */     //   585	608	3	pipeline	Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   594	599	4	glProgram	Lcom/mojang/blaze3d/opengl/GlProgram;
/*     */     //   615	578	5	differentProgram	Z
/*     */     //   0	1210	0	this	Lcom/mojang/blaze3d/opengl/GlCommandEncoder;
/*     */     //   0	1210	1	renderPass	Lcom/mojang/blaze3d/opengl/GlRenderPass;
/*     */     //   0	1210	2	dynamicUniforms	Ljava/util/Collection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   341	167	4	entry	Ljava/util/Map$Entry<Ljava/lang/String;Lcom/mojang/blaze3d/opengl/Uniform;>;
/*     */     //   673	471	7	entry	Ljava/util/Map$Entry<Ljava/lang/String;Lcom/mojang/blaze3d/opengl/Uniform;>;
/*     */     //   0	1210	2	dynamicUniforms	Ljava/util/Collection<Ljava/lang/String;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   771	774	1193	java/lang/Throwable
/*     */     //   843	846	1193	java/lang/Throwable
/*     */     //   854	857	1193	java/lang/Throwable
/*     */     //   865	868	1193	java/lang/Throwable
/*     */     //   876	879	1193	java/lang/Throwable
/*     */     //   972	975	1193	java/lang/Throwable
/*     */     //   983	986	1193	java/lang/Throwable
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
/*     */   private void applyPipelineState(RenderPipeline pipeline) {
/* 820 */     if (this.lastPipeline == pipeline) {
/*     */       return;
/*     */     }
/* 823 */     this.lastPipeline = pipeline;
/*     */     
/* 825 */     if (pipeline.getDepthTestFunction() != DepthTestFunction.NO_DEPTH_TEST) {
/* 826 */       GlStateManager._enableDepthTest();
/* 827 */       GlStateManager._depthFunc(GlConst.toGl(pipeline.getDepthTestFunction()));
/*     */     } else {
/* 829 */       GlStateManager._disableDepthTest();
/*     */     } 
/*     */     
/* 832 */     if (pipeline.isCull()) {
/* 833 */       GlStateManager._enableCull();
/*     */     } else {
/* 835 */       GlStateManager._disableCull();
/*     */     } 
/*     */     
/* 838 */     if (pipeline.getBlendFunction().isPresent()) {
/* 839 */       GlStateManager._enableBlend();
/* 840 */       BlendFunction blendFunction = pipeline.getBlendFunction().get();
/* 841 */       GlStateManager._blendFuncSeparate(GlConst.toGl(blendFunction.sourceColor()), GlConst.toGl(blendFunction.destColor()), GlConst.toGl(blendFunction.sourceAlpha()), GlConst.toGl(blendFunction.destAlpha()));
/*     */     } else {
/* 843 */       GlStateManager._disableBlend();
/*     */     } 
/*     */     
/* 846 */     GlStateManager._polygonMode(1032, GlConst.toGl(pipeline.getPolygonMode()));
/* 847 */     GlStateManager._depthMask(pipeline.isWriteDepth());
/* 848 */     GlStateManager._colorMask(pipeline.isWriteColor(), pipeline.isWriteColor(), pipeline.isWriteColor(), pipeline.isWriteAlpha());
/*     */     
/* 850 */     if (pipeline.getDepthBiasConstant() != 0.0F || pipeline.getDepthBiasScaleFactor() != 0.0F) {
/* 851 */       GlStateManager._polygonOffset(pipeline.getDepthBiasScaleFactor(), pipeline.getDepthBiasConstant());
/* 852 */       GlStateManager._enablePolygonOffset();
/*     */     } else {
/* 854 */       GlStateManager._disablePolygonOffset();
/*     */     } 
/*     */     
/* 857 */     switch (pipeline.getColorLogic()) { case NONE:
/* 858 */         GlStateManager._disableColorLogicOp(); break;
/*     */       case OR_REVERSE:
/* 860 */         GlStateManager._enableColorLogicOp();
/* 861 */         GlStateManager._logicOp(5387);
/*     */         break; }
/*     */   
/*     */   }
/*     */   
/*     */   public void finishRenderPass() {
/* 867 */     this.inRenderPass = false;
/* 868 */     GlStateManager._glBindFramebuffer(36160, 0);
/* 869 */     this.device.debugLabels().popDebugGroup();
/*     */   }
/*     */   
/*     */   protected GlDevice getDevice() {
/* 873 */     return this.device;
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuQuery timerQueryBegin() {
/* 878 */     RenderSystem.assertOnRenderThread();
/* 879 */     if (this.activeTimerQuery != null) {
/* 880 */       throw new IllegalStateException("A GL_TIME_ELAPSED query is already active");
/*     */     }
/* 882 */     int queryId = GL32C.glGenQueries();
/* 883 */     GL32C.glBeginQuery(35007, queryId);
/* 884 */     this.activeTimerQuery = new GlTimerQuery(queryId);
/* 885 */     return this.activeTimerQuery;
/*     */   }
/*     */ 
/*     */   
/*     */   public void timerQueryEnd(GpuQuery query) {
/* 890 */     RenderSystem.assertOnRenderThread();
/* 891 */     if (query != this.activeTimerQuery) {
/* 892 */       throw new IllegalStateException("Mismatched or duplicate GpuQuery when ending timerQuery");
/*     */     }
/* 894 */     GL32C.glEndQuery(35007);
/* 895 */     this.activeTimerQuery = null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlCommandEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */