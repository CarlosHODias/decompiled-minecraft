/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.GpuOutOfMemoryException;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ public class MainTarget
/*     */   extends RenderTarget
/*     */ {
/*     */   public static final int DEFAULT_WIDTH = 854;
/*     */   public static final int DEFAULT_HEIGHT = 480;
/*  17 */   private static final Dimension DEFAULT_DIMENSIONS = new Dimension(854, 480);
/*     */   
/*     */   public MainTarget(int desiredWidth, int desiredHeight) {
/*  20 */     super("Main", true);
/*     */     
/*  22 */     createFrameBuffer(desiredWidth, desiredHeight);
/*     */   }
/*     */   
/*     */   private void createFrameBuffer(int desiredWidth, int desiredHeight) {
/*  26 */     Dimension allocatedDimensions = allocateAttachments(desiredWidth, desiredHeight);
/*  27 */     if (this.colorTexture == null || this.depthTexture == null) {
/*  28 */       throw new IllegalStateException("Missing color and/or depth textures");
/*     */     }
/*     */     
/*  31 */     this.width = allocatedDimensions.width;
/*  32 */     this.height = allocatedDimensions.height;
/*     */   }
/*     */   
/*     */   private Dimension allocateAttachments(int width, int height) {
/*  36 */     RenderSystem.assertOnRenderThread();
/*     */     
/*  38 */     for (Dimension dimension : Dimension.listWithFallback(width, height)) {
/*  39 */       if (this.colorTexture != null) {
/*  40 */         this.colorTexture.close();
/*  41 */         this.colorTexture = null;
/*     */       } 
/*  43 */       if (this.colorTextureView != null) {
/*  44 */         this.colorTextureView.close();
/*  45 */         this.colorTextureView = null;
/*     */       } 
/*  47 */       if (this.depthTexture != null) {
/*  48 */         this.depthTexture.close();
/*  49 */         this.depthTexture = null;
/*     */       } 
/*  51 */       if (this.depthTextureView != null) {
/*  52 */         this.depthTextureView.close();
/*  53 */         this.depthTextureView = null;
/*     */       } 
/*     */       
/*  56 */       this.colorTexture = allocateColorAttachment(dimension);
/*  57 */       this.depthTexture = allocateDepthAttachment(dimension);
/*     */       
/*  59 */       if (this.colorTexture != null && this.depthTexture != null) {
/*  60 */         this.colorTextureView = RenderSystem.getDevice().createTextureView(this.colorTexture);
/*  61 */         this.depthTextureView = RenderSystem.getDevice().createTextureView(this.depthTexture);
/*  62 */         return dimension;
/*     */       } 
/*     */     } 
/*     */     
/*  66 */     throw new RuntimeException("Unrecoverable GL_OUT_OF_MEMORY (" + ((this.colorTexture == null) ? "missing color" : "have color") + ", " + ((this.depthTexture == null) ? "missing depth" : "have depth") + ")");
/*     */   }
/*     */   
/*     */   private GpuTexture allocateColorAttachment(Dimension dimension) {
/*     */     try {
/*  71 */       return RenderSystem.getDevice().createTexture(() -> this.label + " / Color", 15, TextureFormat.RGBA8, dimension.width, dimension.height, 1, 1);
/*  72 */     } catch (GpuOutOfMemoryException ignored) {
/*  73 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private GpuTexture allocateDepthAttachment(Dimension dimension) {
/*     */     try {
/*  79 */       return RenderSystem.getDevice().createTexture(() -> this.label + " / Depth", 15, TextureFormat.DEPTH32, dimension.width, dimension.height, 1, 1);
/*  80 */     } catch (GpuOutOfMemoryException ignored) {
/*  81 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class Dimension {
/*     */     public final int width;
/*     */     public final int height;
/*     */     
/*     */     private Dimension(int width, int height) {
/*  90 */       this.width = width;
/*  91 */       this.height = height;
/*     */     }
/*     */     
/*     */     private static List<Dimension> listWithFallback(int width, int height) {
/*  95 */       RenderSystem.assertOnRenderThread();
/*     */       
/*  97 */       int maxTextureSize = RenderSystem.getDevice().getMaxTextureSize();
/*  98 */       if (width <= 0 || width > maxTextureSize || height <= 0 || height > maxTextureSize) {
/*  99 */         return (List<Dimension>)ImmutableList.of(MainTarget.DEFAULT_DIMENSIONS);
/*     */       }
/* 101 */       return (List<Dimension>)ImmutableList.of(new Dimension(width, height), MainTarget.DEFAULT_DIMENSIONS);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 110 */       if (this == other) {
/* 111 */         return true;
/*     */       }
/* 113 */       if (other == null || getClass() != other.getClass()) {
/* 114 */         return false;
/*     */       }
/* 116 */       Dimension that = (Dimension)other;
/* 117 */       return (this.width == that.width && this.height == that.height);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 122 */       return Objects.hash(new Object[] { this.width, this.height });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 127 */       return "" + this.width + "x" + this.width;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/pipeline/MainTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */