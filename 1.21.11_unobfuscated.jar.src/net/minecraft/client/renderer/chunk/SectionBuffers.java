/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.vertex.VertexFormat;
/*    */ 
/*    */ public final class SectionBuffers
/*    */   implements AutoCloseable {
/*    */   private GpuBuffer vertexBuffer;
/*    */   private GpuBuffer indexBuffer;
/*    */   private int indexCount;
/*    */   private VertexFormat.IndexType indexType;
/*    */   
/*    */   public SectionBuffers(GpuBuffer vertexBuffer, GpuBuffer indexBuffer, int indexCount, VertexFormat.IndexType indexType) {
/* 14 */     this.vertexBuffer = vertexBuffer;
/* 15 */     this.indexBuffer = indexBuffer;
/* 16 */     this.indexCount = indexCount;
/* 17 */     this.indexType = indexType;
/*    */   }
/*    */   
/*    */   public GpuBuffer getVertexBuffer() {
/* 21 */     return this.vertexBuffer;
/*    */   }
/*    */   
/*    */   public GpuBuffer getIndexBuffer() {
/* 25 */     return this.indexBuffer;
/*    */   }
/*    */   
/*    */   public void setIndexBuffer(GpuBuffer indexBuffer) {
/* 29 */     this.indexBuffer = indexBuffer;
/*    */   }
/*    */   
/*    */   public int getIndexCount() {
/* 33 */     return this.indexCount;
/*    */   }
/*    */   
/*    */   public VertexFormat.IndexType getIndexType() {
/* 37 */     return this.indexType;
/*    */   }
/*    */   
/*    */   public void setIndexType(VertexFormat.IndexType indexType) {
/* 41 */     this.indexType = indexType;
/*    */   }
/*    */   
/*    */   public void setIndexCount(int indexCount) {
/* 45 */     this.indexCount = indexCount;
/*    */   }
/*    */   
/*    */   public void setVertexBuffer(GpuBuffer vertexBuffer) {
/* 49 */     this.vertexBuffer = vertexBuffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     this.vertexBuffer.close();
/* 55 */     if (this.indexBuffer != null)
/* 56 */       this.indexBuffer.close(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/SectionBuffers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */