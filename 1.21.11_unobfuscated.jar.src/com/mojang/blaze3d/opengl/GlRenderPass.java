/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.ScissorState;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlRenderPass
/*     */   implements RenderPass
/*     */ {
/*     */   protected static final int MAX_VERTEX_BUFFERS = 1;
/*  25 */   public static final boolean VALIDATION = SharedConstants.IS_RUNNING_IN_IDE;
/*     */   
/*     */   private final GlCommandEncoder encoder;
/*     */   
/*     */   private final boolean hasDepthTexture;
/*     */   private boolean closed;
/*     */   protected GlRenderPipeline pipeline;
/*  32 */   protected final GpuBuffer[] vertexBuffers = new GpuBuffer[1];
/*     */   protected GpuBuffer indexBuffer;
/*  34 */   protected VertexFormat.IndexType indexType = VertexFormat.IndexType.INT;
/*     */   
/*  36 */   private final ScissorState scissorState = new ScissorState();
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected final HashMap<String, GpuBufferSlice> uniforms = new HashMap<>();
/*  41 */   protected final HashMap<String, TextureViewAndSampler> samplers = new HashMap<>();
/*  42 */   protected final Set<String> dirtyUniforms = new HashSet<>();
/*     */   protected int pushedDebugGroups;
/*     */   
/*     */   public GlRenderPass(GlCommandEncoder encoder, boolean hasDepthTexture) {
/*  46 */     this.encoder = encoder;
/*  47 */     this.hasDepthTexture = hasDepthTexture;
/*     */   }
/*     */   
/*     */   public boolean hasDepthTexture() {
/*  51 */     return this.hasDepthTexture;
/*     */   }
/*     */ 
/*     */   
/*     */   public void pushDebugGroup(Supplier<String> label) {
/*  56 */     if (this.closed) {
/*  57 */       throw new IllegalStateException("Can't use a closed render pass");
/*     */     }
/*  59 */     this.pushedDebugGroups++;
/*  60 */     this.encoder.getDevice().debugLabels().pushDebugGroup(label);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popDebugGroup() {
/*  65 */     if (this.closed) {
/*  66 */       throw new IllegalStateException("Can't use a closed render pass");
/*     */     }
/*  68 */     if (this.pushedDebugGroups == 0) {
/*  69 */       throw new IllegalStateException("Can't pop more debug groups than was pushed!");
/*     */     }
/*  71 */     this.pushedDebugGroups--;
/*  72 */     this.encoder.getDevice().debugLabels().popDebugGroup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPipeline(RenderPipeline pipeline) {
/*  77 */     if (this.pipeline == null || this.pipeline.info() != pipeline) {
/*  78 */       this.dirtyUniforms.addAll(this.uniforms.keySet());
/*  79 */       this.dirtyUniforms.addAll(this.samplers.keySet());
/*     */     } 
/*  81 */     this.pipeline = this.encoder.getDevice().getOrCompilePipeline(pipeline);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bindTexture(String name, GpuTextureView textureView, GpuSampler sampler) {
/*  86 */     if (sampler == null) {
/*  87 */       this.samplers.remove(name);
/*     */     } else {
/*  89 */       this.samplers.put(name, new TextureViewAndSampler((GlTextureView)textureView, (GlSampler)sampler));
/*     */     } 
/*  91 */     this.dirtyUniforms.add(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUniform(String name, GpuBuffer value) {
/*  96 */     this.uniforms.put(name, value.slice());
/*  97 */     this.dirtyUniforms.add(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUniform(String name, GpuBufferSlice value) {
/* 102 */     int alignment = this.encoder.getDevice().getUniformOffsetAlignment();
/* 103 */     if (value.offset() % alignment > 0L) {
/* 104 */       throw new IllegalArgumentException("Uniform buffer offset must be aligned to " + alignment);
/*     */     }
/* 106 */     this.uniforms.put(name, value);
/* 107 */     this.dirtyUniforms.add(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enableScissor(int x, int y, int width, int height) {
/* 112 */     this.scissorState.enable(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void disableScissor() {
/* 117 */     this.scissorState.disable();
/*     */   }
/*     */   
/*     */   public boolean isScissorEnabled() {
/* 121 */     return this.scissorState.enabled();
/*     */   }
/*     */   
/*     */   public int getScissorX() {
/* 125 */     return this.scissorState.x();
/*     */   }
/*     */   
/*     */   public int getScissorY() {
/* 129 */     return this.scissorState.y();
/*     */   }
/*     */   
/*     */   public int getScissorWidth() {
/* 133 */     return this.scissorState.width();
/*     */   }
/*     */   
/*     */   public int getScissorHeight() {
/* 137 */     return this.scissorState.height();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVertexBuffer(int slot, GpuBuffer vertexBuffer) {
/* 142 */     if (slot < 0 || slot >= 1) {
/* 143 */       throw new IllegalArgumentException("Vertex buffer slot is out of range: " + slot);
/*     */     }
/* 145 */     this.vertexBuffers[slot] = vertexBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIndexBuffer(GpuBuffer indexBuffer, VertexFormat.IndexType indexType) {
/* 150 */     this.indexBuffer = indexBuffer;
/* 151 */     this.indexType = indexType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawIndexed(int baseVertex, int firstIndex, int indexCount, int instanceCount) {
/* 156 */     if (this.closed) {
/* 157 */       throw new IllegalStateException("Can't use a closed render pass");
/*     */     }
/* 159 */     this.encoder.executeDraw(this, baseVertex, firstIndex, indexCount, this.indexType, instanceCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void drawMultipleIndexed(Collection<RenderPass.Draw<T>> draws, GpuBuffer defaultIndexBuffer, VertexFormat.IndexType defaultIndexType, Collection<String> dynamicUniforms, T uniformArgument) {
/* 164 */     if (this.closed) {
/* 165 */       throw new IllegalStateException("Can't use a closed render pass");
/*     */     }
/* 167 */     this.encoder.executeDrawMultiple(this, draws, defaultIndexBuffer, defaultIndexType, dynamicUniforms, uniformArgument);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int firstVertex, int vertexCount) {
/* 172 */     if (this.closed) {
/* 173 */       throw new IllegalStateException("Can't use a closed render pass");
/*     */     }
/* 175 */     this.encoder.executeDraw(this, firstVertex, 0, vertexCount, null, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 180 */     if (!this.closed) {
/* 181 */       if (this.pushedDebugGroups > 0) {
/* 182 */         throw new IllegalStateException("Render pass had debug groups left open!");
/*     */       }
/* 184 */       this.closed = true;
/* 185 */       this.encoder.finishRenderPass();
/*     */     } 
/*     */   }
/*     */   protected static final class TextureViewAndSampler extends Record { private final GlTextureView view; private final GlSampler sampler;
/* 189 */     protected TextureViewAndSampler(GlTextureView view, GlSampler sampler) { this.view = view; this.sampler = sampler; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 189 */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler; } public GlTextureView view() { return this.view; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPass$TextureViewAndSampler;
/* 189 */       //   0	8	1	o	Ljava/lang/Object; } public GlSampler sampler() { return this.sampler; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlRenderPass.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */