/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ 
/*    */ 
/*    */ public class GlTextureView
/*    */   extends GpuTextureView
/*    */ {
/*    */   private static final int EMPTY = -1;
/*    */   private boolean closed;
/* 15 */   private int firstFboId = -1;
/* 16 */   private int firstFboDepthId = -1;
/*    */   private Int2IntMap fboCache;
/*    */   
/*    */   protected GlTextureView(GlTexture texture, int baseMipLevel, int mipLevels) {
/* 20 */     super(texture, baseMipLevel, mipLevels);
/* 21 */     texture.addViews();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 26 */     return this.closed;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 31 */     if (!this.closed) {
/* 32 */       this.closed = true;
/* 33 */       texture().removeViews();
/* 34 */       if (this.firstFboId != -1) {
/* 35 */         GlStateManager._glDeleteFramebuffers(this.firstFboId);
/*    */       }
/*    */       
/* 38 */       if (this.fboCache != null) {
/* 39 */         for (IntIterator<Integer> intIterator = this.fboCache.values().iterator(); intIterator.hasNext(); ) { int fbo = (Integer)intIterator.next();
/* 40 */           GlStateManager._glDeleteFramebuffers(fbo); }
/*    */       
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getFbo(DirectStateAccess dsa, GpuTexture depth) {
/* 47 */     int depthId = (depth == null) ? 0 : ((GlTexture)depth).id;
/*    */     
/* 49 */     if (this.firstFboDepthId == depthId) {
/* 50 */       return this.firstFboId;
/*    */     }
/*    */     
/* 53 */     if (this.firstFboId == -1) {
/* 54 */       this.firstFboId = createFbo(dsa, depthId);
/* 55 */       this.firstFboDepthId = depthId;
/* 56 */       return this.firstFboId;
/*    */     } 
/*    */     
/* 59 */     if (this.fboCache == null) {
/* 60 */       this.fboCache = (Int2IntMap)new Int2IntArrayMap();
/*    */     }
/* 62 */     return this.fboCache.computeIfAbsent(depthId, _depthId -> createFbo(dsa, dsa));
/*    */   }
/*    */   
/*    */   private int createFbo(DirectStateAccess dsa, int depthid) {
/* 66 */     int fbo = dsa.createFrameBufferObject();
/* 67 */     dsa.bindFrameBufferTextures(fbo, (texture()).id, depthid, baseMipLevel(), 0);
/* 68 */     return fbo;
/*    */   }
/*    */ 
/*    */   
/*    */   public GlTexture texture() {
/* 73 */     return (GlTexture)super.texture();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlTextureView.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */