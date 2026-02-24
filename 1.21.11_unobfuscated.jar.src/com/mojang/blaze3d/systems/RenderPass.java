/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.vertex.VertexFormat;
/*    */ import java.util.Collection;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public interface RenderPass
/*    */   extends AutoCloseable
/*    */ {
/*    */   void pushDebugGroup(Supplier<String> paramSupplier);
/*    */   
/*    */   void popDebugGroup();
/*    */   
/*    */   void setPipeline(RenderPipeline paramRenderPipeline);
/*    */   
/*    */   void bindTexture(String paramString, GpuTextureView paramGpuTextureView, GpuSampler paramGpuSampler);
/*    */   
/*    */   void setUniform(String paramString, GpuBuffer paramGpuBuffer);
/*    */   
/*    */   void setUniform(String paramString, GpuBufferSlice paramGpuBufferSlice);
/*    */   
/*    */   void enableScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   void disableScissor();
/*    */   
/*    */   void setVertexBuffer(int paramInt, GpuBuffer paramGpuBuffer);
/*    */   
/*    */   void setIndexBuffer(GpuBuffer paramGpuBuffer, VertexFormat.IndexType paramIndexType);
/*    */   
/*    */   void drawIndexed(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   <T> void drawMultipleIndexed(Collection<Draw<T>> paramCollection, GpuBuffer paramGpuBuffer, VertexFormat.IndexType paramIndexType, Collection<String> paramCollection1, T paramT);
/*    */   
/*    */   void draw(int paramInt1, int paramInt2);
/*    */   
/*    */   void close();
/*    */   
/*    */   public static interface UniformUploader
/*    */   {
/*    */     void upload(String param1String, GpuBufferSlice param1GpuBufferSlice);
/*    */   }
/*    */   
/*    */   public static final class Draw<T>
/*    */     extends Record
/*    */   {
/*    */     private final int slot;
/*    */     private final GpuBuffer vertexBuffer;
/*    */     private final GpuBuffer indexBuffer;
/*    */     private final VertexFormat.IndexType indexType;
/*    */     private final int firstIndex;
/*    */     private final int indexCount;
/*    */     private final BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer;
/*    */     
/*    */     public BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer() {
/* 61 */       return this.uniformUploaderConsumer; } public int indexCount() { return this.indexCount; } public int firstIndex() { return this.firstIndex; } public VertexFormat.IndexType indexType() { return this.indexType; } public GpuBuffer indexBuffer() { return this.indexBuffer; } public GpuBuffer vertexBuffer() { return this.vertexBuffer; } public int slot() { return this.slot; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 61 */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>; } public Draw(int slot, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, VertexFormat.IndexType indexType, int firstIndex, int indexCount, BiConsumer<T, RenderPass.UniformUploader> uniformUploaderConsumer) { this.slot = slot; this.vertexBuffer = vertexBuffer; this.indexBuffer = indexBuffer; this.indexType = indexType; this.firstIndex = firstIndex; this.indexCount = indexCount; this.uniformUploaderConsumer = uniformUploaderConsumer; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>; }
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPass$Draw;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 63 */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$Draw<TT;>; } public Draw(int slot, GpuBuffer vertexBuffer, GpuBuffer indexBuffer, VertexFormat.IndexType indexType, int firstIndex, int indexCount) { this(slot, vertexBuffer, indexBuffer, indexType, firstIndex, indexCount, null); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/RenderPass.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */