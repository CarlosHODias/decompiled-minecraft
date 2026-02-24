/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTexture.Usage;
/*    */ import com.mojang.blaze3d.textures.TextureFormat;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ 
/*    */ public class GlTexture
/*    */   extends GpuTexture
/*    */ {
/*    */   private static final int EMPTY = -1;
/*    */   protected final int id;
/* 15 */   private int firstFboId = -1;
/* 16 */   private int firstFboDepthId = -1;
/*    */   
/*    */   private Int2IntMap fboCache;
/*    */   protected boolean closed;
/*    */   private int views;
/*    */   
/*    */   protected GlTexture(@GpuTexture.Usage int usage, String label, TextureFormat format, int width, int height, int depthOrLayers, int mipLevels, int id) {
/* 23 */     super(usage, label, format, width, height, depthOrLayers, mipLevels);
/* 24 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 29 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 32 */     this.closed = true;
/* 33 */     if (this.views == 0) {
/* 34 */       destroyImmediately();
/*    */     }
/*    */   }
/*    */   
/*    */   private void destroyImmediately() {
/* 39 */     GlStateManager._deleteTexture(this.id);
/*    */     
/* 41 */     if (this.firstFboId != -1) {
/* 42 */       GlStateManager._glDeleteFramebuffers(this.firstFboId);
/*    */     }
/*    */     
/* 45 */     if (this.fboCache != null) {
/* 46 */       for (IntIterator<Integer> intIterator = this.fboCache.values().iterator(); intIterator.hasNext(); ) { int fbo = (Integer)intIterator.next();
/* 47 */         GlStateManager._glDeleteFramebuffers(fbo); }
/*    */     
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 54 */     return this.closed;
/*    */   }
/*    */   
/*    */   public int getFbo(DirectStateAccess dsa, GpuTexture depth) {
/* 58 */     int depthId = (depth == null) ? 0 : ((GlTexture)depth).id;
/*    */     
/* 60 */     if (this.firstFboDepthId == depthId) {
/* 61 */       return this.firstFboId;
/*    */     }
/*    */     
/* 64 */     if (this.firstFboId == -1) {
/* 65 */       this.firstFboId = createFbo(dsa, depthId);
/* 66 */       this.firstFboDepthId = depthId;
/* 67 */       return this.firstFboId;
/*    */     } 
/*    */     
/* 70 */     if (this.fboCache == null) {
/* 71 */       this.fboCache = (Int2IntMap)new Int2IntArrayMap();
/*    */     }
/* 73 */     return this.fboCache.computeIfAbsent(depthId, _depthId -> createFbo(dsa, dsa));
/*    */   }
/*    */   
/*    */   private int createFbo(DirectStateAccess dsa, int depthid) {
/* 77 */     int fbo = dsa.createFrameBufferObject();
/* 78 */     dsa.bindFrameBufferTextures(fbo, this.id, depthid, 0, 0);
/* 79 */     return fbo;
/*    */   }
/*    */   
/*    */   public int glId() {
/* 83 */     return this.id;
/*    */   }
/*    */   
/*    */   public void addViews() {
/* 87 */     this.views++;
/*    */   }
/*    */   
/*    */   public void removeViews() {
/* 91 */     this.views--;
/*    */     
/* 93 */     if (this.closed && this.views == 0)
/* 94 */       destroyImmediately(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */