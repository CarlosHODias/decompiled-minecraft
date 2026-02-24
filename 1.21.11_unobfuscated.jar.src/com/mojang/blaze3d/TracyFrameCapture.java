/*     */ package com.mojang.blaze3d;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ public class TracyFrameCapture
/*     */   implements AutoCloseable
/*     */ {
/*     */   private static final int MAX_WIDTH = 320;
/*     */   private static final int MAX_HEIGHT = 180;
/*     */   private static final long BYTES_PER_PIXEL = 4L;
/*     */   private int targetWidth;
/*     */   private int targetHeight;
/*     */   private int width;
/*     */   private int height;
/*     */   private GpuTexture frameBuffer;
/*     */   private GpuTextureView frameBufferView;
/*     */   private GpuBuffer pixelbuffer;
/*     */   private int lastCaptureDelay;
/*     */   private boolean capturedThisFrame;
/*  32 */   private Status status = Status.WAITING_FOR_CAPTURE;
/*     */   
/*     */   public TracyFrameCapture() {
/*  35 */     this.width = 320;
/*  36 */     this.height = 180;
/*  37 */     GpuDevice device = RenderSystem.getDevice();
/*  38 */     this.frameBuffer = device.createTexture("Tracy Frame Capture", 10, TextureFormat.RGBA8, this.width, this.height, 1, 1);
/*  39 */     this.frameBufferView = device.createTextureView(this.frameBuffer);
/*  40 */     this.pixelbuffer = device.createBuffer(() -> "Tracy Frame Capture buffer", 9, (this.width * this.height) * 4L);
/*     */   }
/*     */   
/*     */   private void resize(int width, int height) {
/*  44 */     float aspectRatio = width / height;
/*     */     
/*  46 */     if (width > 320) {
/*  47 */       width = 320;
/*  48 */       height = (int)(320.0F / aspectRatio);
/*     */     } 
/*  50 */     if (height > 180) {
/*  51 */       width = (int)(180.0F * aspectRatio);
/*  52 */       height = 180;
/*     */     } 
/*     */ 
/*     */     
/*  56 */     width = width / 4 * 4;
/*  57 */     height = height / 4 * 4;
/*     */     
/*  59 */     if (this.width != width || this.height != height) {
/*  60 */       this.width = width;
/*  61 */       this.height = height;
/*     */       
/*  63 */       GpuDevice device = RenderSystem.getDevice();
/*  64 */       this.frameBuffer.close();
/*  65 */       this.frameBuffer = device.createTexture("Tracy Frame Capture", 10, TextureFormat.RGBA8, width, height, 1, 1);
/*  66 */       this.frameBufferView.close();
/*  67 */       this.frameBufferView = device.createTextureView(this.frameBuffer);
/*  68 */       this.pixelbuffer.close();
/*  69 */       this.pixelbuffer = device.createBuffer(() -> "Tracy Frame Capture buffer", 9, (width * height) * 4L);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void capture(RenderTarget captureTarget) {
/*  78 */     if (this.status != Status.WAITING_FOR_CAPTURE || this.capturedThisFrame || captureTarget.getColorTexture() == null) {
/*     */       return;
/*     */     }
/*  81 */     this.capturedThisFrame = true;
/*     */     
/*  83 */     if (captureTarget.width != this.targetWidth || captureTarget.height != this.targetHeight) {
/*  84 */       this.targetWidth = captureTarget.width;
/*  85 */       this.targetHeight = captureTarget.height;
/*  86 */       resize(this.targetWidth, this.targetHeight);
/*     */     } 
/*     */     
/*  89 */     this.status = Status.WAITING_FOR_COPY;
/*  90 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  91 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Tracy blit", this.frameBufferView, OptionalInt.empty()); 
/*  92 */     try { renderPass.setPipeline(RenderPipelines.TRACY_BLIT);
/*  93 */       renderPass.bindTexture("InSampler", captureTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/*  94 */       renderPass.draw(0, 3);
/*  95 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  97 */      commandEncoder.copyTextureToBuffer(this.frameBuffer, this.pixelbuffer, 0L, () -> this.status = Status.WAITING_FOR_UPLOAD, 0);
/*     */     
/*  99 */     this.lastCaptureDelay = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upload() {
/* 107 */     if (this.status != Status.WAITING_FOR_UPLOAD) {
/*     */       return;
/*     */     }
/* 110 */     this.status = Status.WAITING_FOR_CAPTURE;
/*     */     
/* 112 */     GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.pixelbuffer, true, false); 
/* 113 */     try { TracyClient.frameImage(view.data(), this.width, this.height, this.lastCaptureDelay, true);
/* 114 */       if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/*     */         try { view.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 118 */      } public void endFrame() { this.lastCaptureDelay++;
/* 119 */     this.capturedThisFrame = false;
/* 120 */     TracyClient.markFrame(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 125 */     this.frameBuffer.close();
/* 126 */     this.frameBufferView.close();
/* 127 */     this.pixelbuffer.close();
/*     */   }
/*     */   
/*     */   enum Status {
/* 131 */     WAITING_FOR_CAPTURE,
/* 132 */     WAITING_FOR_COPY,
/* 133 */     WAITING_FOR_UPLOAD;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/TracyFrameCapture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */