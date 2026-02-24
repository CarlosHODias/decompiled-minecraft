/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuFence;
/*    */ 
/*    */ 
/*    */ public class GlFence
/*    */   implements GpuFence
/*    */ {
/*  9 */   private long handle = GlStateManager._glFenceSync(37143, 0);
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 14 */     if (this.handle != 0L) {
/* 15 */       GlStateManager._glDeleteSync(this.handle);
/* 16 */       this.handle = 0L;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean awaitCompletion(long timeoutMs) {
/* 22 */     if (this.handle == 0L)
/*    */     {
/* 24 */       return true;
/*    */     }
/*    */     
/* 27 */     int result = GlStateManager._glClientWaitSync(this.handle, 0, timeoutMs);
/* 28 */     if (result == 37147)
/*    */     {
/* 30 */       return false;
/*    */     }
/* 32 */     if (result == 37149)
/*    */     {
/* 34 */       throw new IllegalStateException("Failed to complete GPU fence: " + GlStateManager._getError());
/*    */     }
/*    */ 
/*    */     
/* 38 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlFence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */