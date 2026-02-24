/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer.Usage;
/*    */ import com.mojang.jtracy.MemoryPool;
/*    */ import com.mojang.jtracy.TracyClient;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class GlBuffer
/*    */   extends GpuBuffer {
/* 12 */   protected static final MemoryPool MEMORY_POOl = TracyClient.createMemoryPool("GPU Buffers");
/*    */   
/*    */   protected boolean closed;
/*    */   protected final Supplier<String> label;
/*    */   private final DirectStateAccess dsa;
/*    */   protected final int handle;
/*    */   protected ByteBuffer persistentBuffer;
/*    */   
/*    */   protected GlBuffer(Supplier<String> label, DirectStateAccess dsa, @GpuBuffer.Usage int usage, long size, int handle, ByteBuffer persistentBuffer) {
/* 21 */     super(usage, size);
/* 22 */     this.label = label;
/* 23 */     this.dsa = dsa;
/* 24 */     this.handle = handle;
/* 25 */     this.persistentBuffer = persistentBuffer;
/* 26 */     int clampedSize = (int)Math.min(size, 2147483647L);
/* 27 */     MEMORY_POOl.malloc(handle, clampedSize);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 32 */     return this.closed;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 37 */     if (this.closed) {
/*    */       return;
/*    */     }
/* 40 */     this.closed = true;
/* 41 */     if (this.persistentBuffer != null) {
/* 42 */       this.dsa.unmapBuffer(this.handle, usage());
/* 43 */       this.persistentBuffer = null;
/*    */     } 
/* 45 */     GlStateManager._glDeleteBuffers(this.handle);
/* 46 */     MEMORY_POOl.free(this.handle);
/*    */   }
/*    */   
/*    */   public static class GlMappedView implements GpuBuffer.MappedView {
/*    */     private final Runnable unmap;
/*    */     private final GlBuffer buffer;
/*    */     private final ByteBuffer data;
/*    */     private boolean closed;
/*    */     
/*    */     protected GlMappedView(Runnable unmap, GlBuffer buffer, ByteBuffer data) {
/* 56 */       this.unmap = unmap;
/* 57 */       this.buffer = buffer;
/* 58 */       this.data = data;
/*    */     }
/*    */ 
/*    */     
/*    */     public ByteBuffer data() {
/* 63 */       return this.data;
/*    */     }
/*    */ 
/*    */     
/*    */     public void close() {
/* 68 */       if (this.closed) {
/*    */         return;
/*    */       }
/* 71 */       this.closed = true;
/* 72 */       this.unmap.run();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlBuffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */