/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.GpuQuery;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.util.OptionalLong;
/*    */ import org.lwjgl.opengl.ARBTimerQuery;
/*    */ import org.lwjgl.opengl.GL32C;
/*    */ 
/*    */ public class GlTimerQuery
/*    */   implements GpuQuery {
/*    */   private final int queryId;
/*    */   private boolean closed;
/* 13 */   private OptionalLong result = OptionalLong.empty();
/*    */   
/*    */   GlTimerQuery(int queryId) {
/* 16 */     this.queryId = queryId;
/*    */   }
/*    */ 
/*    */   
/*    */   public OptionalLong getValue() {
/* 21 */     RenderSystem.assertOnRenderThread();
/* 22 */     if (this.closed) {
/* 23 */       throw new IllegalStateException("GlTimerQuery is closed");
/*    */     }
/* 25 */     if (this.result.isPresent()) {
/* 26 */       return this.result;
/*    */     }
/* 28 */     if (GL32C.glGetQueryObjecti(this.queryId, 34919) == 1) {
/* 29 */       this.result = OptionalLong.of(ARBTimerQuery.glGetQueryObjecti64(this.queryId, 34918));
/* 30 */       return this.result;
/*    */     } 
/* 32 */     return OptionalLong.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 37 */     RenderSystem.assertOnRenderThread();
/* 38 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 41 */     this.closed = true;
/* 42 */     GL32C.glDeleteQueries(this.queryId);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlTimerQuery.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */