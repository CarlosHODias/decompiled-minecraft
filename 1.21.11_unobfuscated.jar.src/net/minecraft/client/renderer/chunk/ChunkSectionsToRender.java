/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import com.mojang.blaze3d.systems.RenderPass;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.vertex.VertexFormat;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public final class ChunkSectionsToRender extends Record {
/*    */   private final GpuTextureView textureView;
/*    */   private final EnumMap<ChunkSectionLayer, List<RenderPass.Draw<GpuBufferSlice[]>>> drawsPerLayer;
/*    */   private final int maxIndicesRequired;
/*    */   private final GpuBufferSlice[] chunkSectionInfos;
/*    */   
/* 21 */   public ChunkSectionsToRender(GpuTextureView textureView, EnumMap<ChunkSectionLayer, List<RenderPass.Draw<GpuBufferSlice[]>>> drawsPerLayer, int maxIndicesRequired, GpuBufferSlice[] chunkSectionInfos) { this.textureView = textureView; this.drawsPerLayer = drawsPerLayer; this.maxIndicesRequired = maxIndicesRequired; this.chunkSectionInfos = chunkSectionInfos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 21 */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender; } public GpuTextureView textureView() { return this.textureView; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public EnumMap<ChunkSectionLayer, List<RenderPass.Draw<GpuBufferSlice[]>>> drawsPerLayer() { return this.drawsPerLayer; } public int maxIndicesRequired() { return this.maxIndicesRequired; } public GpuBufferSlice[] chunkSectionInfos() { return this.chunkSectionInfos; }
/*    */   
/*    */   public void renderGroup(ChunkSectionLayerGroup group, GpuSampler sampler) {
/* 24 */     RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/* 25 */     GpuBuffer defaultIndexBuffer = (this.maxIndicesRequired == 0) ? null : autoIndices.getBuffer(this.maxIndicesRequired);
/* 26 */     VertexFormat.IndexType defaultIndexType = (this.maxIndicesRequired == 0) ? null : autoIndices.type();
/* 27 */     ChunkSectionLayer[] layers = group.layers();
/* 28 */     Minecraft minecraft = Minecraft.getInstance();
/* 29 */     boolean wireframe = (net.minecraft.SharedConstants.DEBUG_HOTKEYS && minecraft.wireframe);
/*    */     
/* 31 */     RenderTarget renderTarget = group.outputTarget();
/* 32 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Section layers for " + group.label(), renderTarget.getColorTextureView(), java.util.OptionalInt.empty(), renderTarget.getDepthTextureView(), java.util.OptionalDouble.empty()); try {
/* 33 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 34 */       renderPass.bindTexture("Sampler2", minecraft.gameRenderer.lightTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(com.mojang.blaze3d.textures.FilterMode.LINEAR));
/*    */       
/* 36 */       for (ChunkSectionLayer layer : layers) {
/* 37 */         List<RenderPass.Draw<GpuBufferSlice[]>> draws = this.drawsPerLayer.get(layer);
/* 38 */         if (!draws.isEmpty()) {
/* 39 */           if (layer == ChunkSectionLayer.TRANSLUCENT) {
/* 40 */             draws = draws.reversed();
/*    */           }
/* 42 */           renderPass.setPipeline(wireframe ? net.minecraft.client.renderer.RenderPipelines.WIREFRAME : layer.pipeline());
/* 43 */           renderPass.bindTexture("Sampler0", this.textureView, sampler);
/* 44 */           renderPass.drawMultipleIndexed(draws, defaultIndexBuffer, defaultIndexType, List.of("ChunkSection"), this.chunkSectionInfos);
/*    */         } 
/*    */       } 
/* 47 */       if (renderPass != null) renderPass.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (renderPass != null)
/*    */         try {
/*    */           renderPass.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/ChunkSectionsToRender.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */