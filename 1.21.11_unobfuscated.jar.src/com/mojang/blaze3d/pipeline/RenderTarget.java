/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ 
/*     */ public abstract class RenderTarget
/*     */ {
/*  16 */   private static int UNNAMED_RENDER_TARGETS = 0;
/*     */   
/*     */   public int width;
/*     */   
/*     */   public int height;
/*     */   protected final String label;
/*     */   public final boolean useDepth;
/*     */   protected GpuTexture colorTexture;
/*     */   protected GpuTextureView colorTextureView;
/*     */   protected GpuTexture depthTexture;
/*     */   protected GpuTextureView depthTextureView;
/*     */   
/*     */   public RenderTarget(String label, boolean useDepth) {
/*  29 */     this.label = (label == null) ? ("FBO " + UNNAMED_RENDER_TARGETS++) : label;
/*  30 */     this.useDepth = useDepth;
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/*  34 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  36 */     destroyBuffers();
/*  37 */     createBuffers(width, height);
/*     */   }
/*     */   
/*     */   public void destroyBuffers() {
/*  41 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  43 */     if (this.depthTexture != null) {
/*  44 */       this.depthTexture.close();
/*  45 */       this.depthTexture = null;
/*     */     } 
/*  47 */     if (this.depthTextureView != null) {
/*  48 */       this.depthTextureView.close();
/*  49 */       this.depthTextureView = null;
/*     */     } 
/*  51 */     if (this.colorTexture != null) {
/*  52 */       this.colorTexture.close();
/*  53 */       this.colorTexture = null;
/*     */     } 
/*  55 */     if (this.colorTextureView != null) {
/*  56 */       this.colorTextureView.close();
/*  57 */       this.colorTextureView = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void copyDepthFrom(RenderTarget source) {
/*  62 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  64 */     if (this.depthTexture == null) {
/*  65 */       throw new IllegalStateException("Trying to copy depth texture to a RenderTarget without a depth texture");
/*     */     }
/*  67 */     if (source.depthTexture == null) {
/*  68 */       throw new IllegalStateException("Trying to copy depth texture from a RenderTarget without a depth texture");
/*     */     }
/*  70 */     RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(source.depthTexture, this.depthTexture, 0, 0, 0, 0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   public void createBuffers(int width, int height) {
/*  74 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  76 */     GpuDevice device = RenderSystem.getDevice();
/*  77 */     int maxTextureSize = device.getMaxTextureSize();
/*  78 */     if (width <= 0 || width > maxTextureSize || height <= 0 || height > maxTextureSize) {
/*  79 */       throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + maxTextureSize + ")");
/*     */     }
/*     */     
/*  82 */     this.width = width;
/*  83 */     this.height = height;
/*     */     
/*  85 */     if (this.useDepth) {
/*  86 */       this.depthTexture = device.createTexture(() -> this.label + " / Depth", 15, TextureFormat.DEPTH32, width, height, 1, 1);
/*  87 */       this.depthTextureView = device.createTextureView(this.depthTexture);
/*     */     } 
/*     */     
/*  90 */     this.colorTexture = device.createTexture(() -> this.label + " / Color", 15, TextureFormat.RGBA8, width, height, 1, 1);
/*  91 */     this.colorTextureView = device.createTextureView(this.colorTexture);
/*     */   }
/*     */   
/*     */   public void blitToScreen() {
/*  95 */     if (this.colorTexture == null) {
/*  96 */       throw new IllegalStateException("Can't blit to screen, color texture doesn't exist yet");
/*     */     }
/*  98 */     RenderSystem.getDevice().createCommandEncoder().presentTexture(this.colorTextureView);
/*     */   }
/*     */   
/*     */   public void blitAndBlendToTexture(GpuTextureView output) {
/* 102 */     RenderSystem.assertOnRenderThread();
/* 103 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Blit render target", output, OptionalInt.empty()); 
/* 104 */     try { renderPass.setPipeline(RenderPipelines.ENTITY_OUTLINE_BLIT);
/* 105 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 106 */       renderPass.bindTexture("InSampler", this.colorTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
/* 107 */       renderPass.draw(0, 3);
/* 108 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 112 */      } public GpuTexture getColorTexture() { return this.colorTexture; }
/*     */ 
/*     */   
/*     */   public GpuTextureView getColorTextureView() {
/* 116 */     return this.colorTextureView;
/*     */   }
/*     */   
/*     */   public GpuTexture getDepthTexture() {
/* 120 */     return this.depthTexture;
/*     */   }
/*     */   
/*     */   public GpuTextureView getDepthTextureView() {
/* 124 */     return this.depthTextureView;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/pipeline/RenderTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */