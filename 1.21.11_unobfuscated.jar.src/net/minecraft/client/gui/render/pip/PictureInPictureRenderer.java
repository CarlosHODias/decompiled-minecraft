/*     */ package net.minecraft.client.gui.render.pip;
/*     */ 
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.gui.render.state.BlitRenderState;
/*     */ import net.minecraft.client.gui.render.state.GuiRenderState;
/*     */ import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
/*     */ import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PictureInPictureRenderer<T extends PictureInPictureRenderState>
/*     */   implements AutoCloseable
/*     */ {
/*     */   protected final MultiBufferSource.BufferSource bufferSource;
/*     */   private GpuTexture texture;
/*     */   private GpuTextureView textureView;
/*     */   private GpuTexture depthTexture;
/*     */   private GpuTextureView depthTextureView;
/*  30 */   private final CachedOrthoProjectionMatrixBuffer projectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("PIP - " + getClass().getSimpleName(), -1000.0F, 1000.0F, true);
/*     */   
/*     */   protected PictureInPictureRenderer(MultiBufferSource.BufferSource bufferSource) {
/*  33 */     this.bufferSource = bufferSource;
/*     */   }
/*     */   
/*     */   public void prepare(T renderState, GuiRenderState guiRenderState, int guiScale) {
/*  37 */     int width = (renderState.x1() - renderState.x0()) * guiScale;
/*  38 */     int height = (renderState.y1() - renderState.y0()) * guiScale;
/*     */     
/*  40 */     boolean needsAResize = (this.texture == null || this.texture.getWidth(0) != width || this.texture.getHeight(0) != height);
/*  41 */     if (!needsAResize && textureIsReadyToBlit(renderState)) {
/*  42 */       blitTexture(renderState, guiRenderState);
/*     */       return;
/*     */     } 
/*  45 */     prepareTexturesAndProjection(needsAResize, width, height);
/*     */     
/*  47 */     RenderSystem.outputColorTextureOverride = this.textureView;
/*  48 */     RenderSystem.outputDepthTextureOverride = this.depthTextureView;
/*     */     
/*  50 */     PoseStack poseStack = new PoseStack();
/*     */     
/*  52 */     poseStack.translate(width / 2.0F, getTranslateY(height, guiScale), 0.0F);
/*     */     
/*  54 */     float scale = guiScale * renderState.scale();
/*  55 */     poseStack.scale(scale, scale, -scale);
/*     */     
/*  57 */     renderToTexture(renderState, poseStack);
/*  58 */     this.bufferSource.endBatch();
/*     */     
/*  60 */     RenderSystem.outputColorTextureOverride = null;
/*  61 */     RenderSystem.outputDepthTextureOverride = null;
/*     */     
/*  63 */     blitTexture(renderState, guiRenderState);
/*     */   }
/*     */   
/*     */   protected void blitTexture(T renderState, GuiRenderState guiRenderState) {
/*  67 */     guiRenderState.submitBlitToCurrentLayer(new BlitRenderState(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, 
/*     */           
/*  69 */           TextureSetup.singleTexture(this.textureView, RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)), 
/*  70 */           renderState.pose(), 
/*  71 */           renderState.x0(), renderState.y0(), renderState.x1(), renderState.y1(), 0.0F, 1.0F, 1.0F, 0.0F, -1, 
/*     */ 
/*     */ 
/*     */           
/*  75 */           renderState.scissorArea(), null));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareTexturesAndProjection(boolean needsAResize, int width, int height) {
/*  81 */     if (this.texture != null && needsAResize) {
/*  82 */       this.texture.close();
/*  83 */       this.texture = null;
/*  84 */       this.textureView.close();
/*  85 */       this.textureView = null;
/*  86 */       this.depthTexture.close();
/*  87 */       this.depthTexture = null;
/*  88 */       this.depthTextureView.close();
/*  89 */       this.depthTextureView = null;
/*     */     } 
/*  91 */     GpuDevice device = RenderSystem.getDevice();
/*  92 */     if (this.texture == null) {
/*  93 */       this.texture = device.createTexture(() -> "UI " + getTextureLabel() + " texture", 12, TextureFormat.RGBA8, width, height, 1, 1);
/*  94 */       this.textureView = device.createTextureView(this.texture);
/*  95 */       this.depthTexture = device.createTexture(() -> "UI " + getTextureLabel() + " depth texture", 8, TextureFormat.DEPTH32, width, height, 1, 1);
/*  96 */       this.depthTextureView = device.createTextureView(this.depthTexture);
/*     */     } 
/*  98 */     device.createCommandEncoder().clearColorAndDepthTextures(this.texture, 0, this.depthTexture, 1.0D);
/*     */     
/* 100 */     RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(width, height), ProjectionType.ORTHOGRAPHIC);
/*     */   }
/*     */   
/*     */   protected boolean textureIsReadyToBlit(T renderState) {
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   protected float getTranslateY(int height, int guiScale) {
/* 108 */     return height;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 113 */     if (this.texture != null) {
/* 114 */       this.texture.close();
/*     */     }
/* 116 */     if (this.textureView != null) {
/* 117 */       this.textureView.close();
/*     */     }
/* 119 */     if (this.depthTexture != null) {
/* 120 */       this.depthTexture.close();
/*     */     }
/* 122 */     if (this.depthTextureView != null) {
/* 123 */       this.depthTextureView.close();
/*     */     }
/* 125 */     this.projectionMatrixBuffer.close();
/*     */   }
/*     */   
/*     */   public abstract Class<T> getRenderStateClass();
/*     */   
/*     */   protected abstract void renderToTexture(T paramT, PoseStack paramPoseStack);
/*     */   
/*     */   protected abstract String getTextureLabel();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/pip/PictureInPictureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */