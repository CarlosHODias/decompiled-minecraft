/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*    */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*    */ import com.mojang.blaze3d.vertex.MeshData;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.SequencedMap;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ 
/*    */ 
/*    */ public interface MultiBufferSource
/*    */ {
/*    */   static BufferSource immediate(ByteBufferBuilder buffer) {
/* 18 */     return immediateWithBuffers((SequencedMap<RenderType, ByteBufferBuilder>)Object2ObjectSortedMaps.emptyMap(), buffer);
/*    */   }
/*    */   
/*    */   static BufferSource immediateWithBuffers(SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers, ByteBufferBuilder sharedBuffer) {
/* 22 */     return new BufferSource(sharedBuffer, fixedBuffers);
/*    */   }
/*    */   
/*    */   VertexConsumer getBuffer(RenderType paramRenderType);
/*    */   
/*    */   public static class BufferSource
/*    */     implements MultiBufferSource {
/*    */     protected final ByteBufferBuilder sharedBuffer;
/*    */     protected final SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers;
/* 31 */     protected final Map<RenderType, BufferBuilder> startedBuilders = new HashMap<>();
/*    */     protected RenderType lastSharedType;
/*    */     
/*    */     protected BufferSource(ByteBufferBuilder sharedBuffer, SequencedMap<RenderType, ByteBufferBuilder> fixedBuffers) {
/* 35 */       this.sharedBuffer = sharedBuffer;
/* 36 */       this.fixedBuffers = fixedBuffers;
/*    */     }
/*    */ 
/*    */     
/*    */     public VertexConsumer getBuffer(RenderType renderType) {
/* 41 */       BufferBuilder builder = this.startedBuilders.get(renderType);
/* 42 */       if (builder != null && !renderType.canConsolidateConsecutiveGeometry()) {
/* 43 */         endBatch(renderType, builder);
/* 44 */         builder = null;
/*    */       } 
/*    */       
/* 47 */       if (builder != null) {
/* 48 */         return (VertexConsumer)builder;
/*    */       }
/*    */       
/* 51 */       ByteBufferBuilder fixedBuffer = this.fixedBuffers.get(renderType);
/* 52 */       if (fixedBuffer != null) {
/* 53 */         builder = new BufferBuilder(fixedBuffer, renderType.mode(), renderType.format());
/*    */       } else {
/* 55 */         if (this.lastSharedType != null) {
/* 56 */           endBatch(this.lastSharedType);
/*    */         }
/* 58 */         builder = new BufferBuilder(this.sharedBuffer, renderType.mode(), renderType.format());
/* 59 */         this.lastSharedType = renderType;
/*    */       } 
/*    */       
/* 62 */       this.startedBuilders.put(renderType, builder);
/*    */       
/* 64 */       return (VertexConsumer)builder;
/*    */     }
/*    */     
/*    */     public void endLastBatch() {
/* 68 */       if (this.lastSharedType != null) {
/* 69 */         endBatch(this.lastSharedType);
/* 70 */         this.lastSharedType = null;
/*    */       } 
/*    */     }
/*    */     
/*    */     public void endBatch() {
/* 75 */       endLastBatch();
/*    */       
/* 77 */       for (RenderType renderType : this.fixedBuffers.keySet()) {
/* 78 */         endBatch(renderType);
/*    */       }
/*    */     }
/*    */     
/*    */     public void endBatch(RenderType type) {
/* 83 */       BufferBuilder builder = this.startedBuilders.remove(type);
/* 84 */       if (builder != null) {
/* 85 */         endBatch(type, builder);
/*    */       }
/*    */     }
/*    */     
/*    */     private void endBatch(RenderType type, BufferBuilder builder) {
/* 90 */       MeshData mesh = builder.build();
/* 91 */       if (mesh != null) {
/* 92 */         if (type.sortOnUpload()) {
/* 93 */           ByteBufferBuilder buffer = this.fixedBuffers.getOrDefault(type, this.sharedBuffer);
/* 94 */           mesh.sortQuads(buffer, RenderSystem.getProjectionType().vertexSorting());
/*    */         } 
/* 96 */         type.draw(mesh);
/*    */       } 
/* 98 */       if (type.equals(this.lastSharedType))
/* 99 */         this.lastSharedType = null; 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/MultiBufferSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */