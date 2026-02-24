/*     */ package net.minecraft.client.renderer.state;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.particle.SingleQuadParticle;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.feature.ParticleFeatureRenderer;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class QuadParticleRenderState implements ParticleGroupRenderState, SubmitNodeCollector.ParticleGroupRenderer {
/*     */   private static final int INITIAL_PARTICLE_CAPACITY = 1024;
/*     */   private static final int FLOATS_PER_PARTICLE = 12;
/*     */   private static final int INTS_PER_PARTICLE = 2;
/*  32 */   private final Map<SingleQuadParticle.Layer, Storage> particles = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int particleCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(SingleQuadParticle.Layer layer, float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightColor) {
/*  52 */     ((Storage)this.particles.computeIfAbsent(layer, ignored -> new Storage())).add(x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightColor);
/*  53 */     this.particleCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  58 */     this.particles.values().forEach(Storage::clear);
/*  59 */     this.particleCount = 0;
/*     */   }
/*     */   
/*     */   public PreparedBuffers prepare(ParticleFeatureRenderer.ParticleBufferCache cachedBuffer)
/*     */   {
/*  64 */     int vertexCount = this.particleCount * 4;
/*  65 */     ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(vertexCount * DefaultVertexFormat.PARTICLE.getVertexSize()); 
/*  66 */     try { BufferBuilder bufferBuilder = new BufferBuilder(builder, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
/*  67 */       Map<SingleQuadParticle.Layer, PreparedLayer> preparedLayers = new HashMap<>();
/*  68 */       int offset = 0;
/*  69 */       for (Iterator<Map.Entry<SingleQuadParticle.Layer, Storage>> iterator = this.particles.entrySet().iterator(); iterator.hasNext(); ) { Map.Entry<SingleQuadParticle.Layer, Storage> entry = iterator.next();
/*  70 */         ((Storage)entry.getValue()).forEachParticle((x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightColor) -> renderRotatedQuad((VertexConsumer)bufferBuilder, bufferBuilder, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightColor));
/*     */ 
/*     */         
/*  73 */         if (((Storage)entry.getValue()).count() > 0) {
/*  74 */           preparedLayers.put(entry.getKey(), new PreparedLayer(offset, ((Storage)entry.getValue()).count() * 6));
/*     */         }
/*  76 */         offset += ((Storage)entry.getValue()).count() * 4; }
/*     */       
/*  78 */       MeshData mesh = bufferBuilder.build();
/*  79 */       if (mesh != null)
/*  80 */       { cachedBuffer.write(mesh.vertexBuffer());
/*     */         
/*  82 */         RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS).getBuffer(mesh.drawState().indexCount());
/*  83 */         GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (org.joml.Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*  84 */         PreparedBuffers preparedBuffers1 = new PreparedBuffers(mesh.drawState().indexCount(), dynamicTransforms, preparedLayers);
/*     */ 
/*     */ 
/*     */         
/*  88 */         if (builder != null) builder.close();  return preparedBuffers1; }  PreparedBuffers preparedBuffers = null; if (builder != null) builder.close();  return preparedBuffers; }
/*     */     catch (Throwable throwable) { if (builder != null)
/*     */         try { builder.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  93 */      } public void render(PreparedBuffers preparedBuffers, ParticleFeatureRenderer.ParticleBufferCache bufferCache, RenderPass renderPass, TextureManager textureManager, boolean isTransparency) { RenderSystem.AutoStorageIndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/*  94 */     renderPass.setVertexBuffer(0, bufferCache.get());
/*  95 */     renderPass.setIndexBuffer(indexBuffer.getBuffer(preparedBuffers.indexCount), indexBuffer.type());
/*     */     
/*  97 */     renderPass.setUniform("DynamicTransforms", preparedBuffers.dynamicTransforms);
/*     */     
/*  99 */     for (Map.Entry<SingleQuadParticle.Layer, PreparedLayer> entry : preparedBuffers.layers.entrySet()) {
/* 100 */       if (isTransparency != ((SingleQuadParticle.Layer)entry.getKey()).translucent()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 105 */       renderPass.setPipeline(((SingleQuadParticle.Layer)entry.getKey()).pipeline());
/* 106 */       AbstractTexture texture = textureManager.getTexture(((SingleQuadParticle.Layer)entry.getKey()).textureAtlasLocation());
/* 107 */       renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
/*     */       
/* 109 */       renderPass.drawIndexed(((PreparedLayer)entry.getValue()).vertexOffset, 0, ((PreparedLayer)entry.getValue()).indexCount, 1);
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderRotatedQuad(VertexConsumer builder, float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightColor) {
/* 128 */     Quaternionf rotation = new Quaternionf(xRot, yRot, zRot, wRot);
/*     */     
/* 130 */     renderVertex(builder, rotation, x, y, z, 1.0F, -1.0F, scale, u1, v1, color, lightColor);
/* 131 */     renderVertex(builder, rotation, x, y, z, 1.0F, 1.0F, scale, u1, v0, color, lightColor);
/* 132 */     renderVertex(builder, rotation, x, y, z, -1.0F, 1.0F, scale, u0, v0, color, lightColor);
/* 133 */     renderVertex(builder, rotation, x, y, z, -1.0F, -1.0F, scale, u0, v1, color, lightColor);
/*     */   }
/*     */   
/*     */   private void renderVertex(VertexConsumer builder, Quaternionf rotation, float x, float y, float z, float nx, float ny, float scale, float u, float v, int color, int lightColor) {
/* 137 */     Vector3f scratch = new Vector3f(nx, ny, 0.0F).rotate((Quaternionfc)rotation).mul(scale).add(x, y, z);
/* 138 */     builder.addVertex(scratch.x(), scratch.y(), scratch.z()).setUv(u, v).setColor(color).setLight(lightColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 143 */     if (this.particleCount > 0) {
/* 144 */       submitNodeCollector.submitParticleGroup(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class PreparedBuffers
/*     */     extends Record
/*     */   {
/*     */     private final int indexCount;
/*     */ 
/*     */ 
/*     */     
/*     */     private final GpuBufferSlice dynamicTransforms;
/*     */ 
/*     */     
/*     */     private final Map<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layers;
/*     */ 
/*     */ 
/*     */     
/*     */     public PreparedBuffers(int indexCount, GpuBufferSlice dynamicTransforms, Map<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layers) {
/* 166 */       this.indexCount = indexCount; this.dynamicTransforms = dynamicTransforms; this.layers = layers; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedBuffers;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedBuffers; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedBuffers;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedBuffers; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedBuffers;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedBuffers;
/* 166 */       //   0	8	1	o	Ljava/lang/Object; } public int indexCount() { return this.indexCount; } public GpuBufferSlice dynamicTransforms() { return this.dynamicTransforms; } public Map<SingleQuadParticle.Layer, QuadParticleRenderState.PreparedLayer> layers() { return this.layers; }
/*     */      }
/*     */   public static final class PreparedLayer extends Record { private final int vertexOffset; private final int indexCount;
/* 169 */     public PreparedLayer(int vertexOffset, int indexCount) { this.vertexOffset = vertexOffset; this.indexCount = indexCount; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedLayer;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 169 */       //   0	7	0	this	Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedLayer; } public int vertexOffset() { return this.vertexOffset; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedLayer;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedLayer; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedLayer;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/state/QuadParticleRenderState$PreparedLayer;
/* 169 */       //   0	8	1	o	Ljava/lang/Object; } public int indexCount() { return this.indexCount; }
/*     */      } @FunctionalInterface
/*     */   public static interface ParticleConsumer { void consume(float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, float param1Float8, float param1Float9, float param1Float10, float param1Float11, float param1Float12, int param1Int1, int param1Int2); }
/* 172 */   private static class Storage { private int capacity = 1024;
/* 173 */     private float[] floatValues = new float[12288];
/* 174 */     private int[] intValues = new int[2048];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int currentParticleIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(float x, float y, float z, float xRot, float yRot, float zRot, float wRot, float scale, float u0, float u1, float v0, float v1, int color, int lightColor) {
/* 192 */       if (this.currentParticleIndex >= this.capacity) {
/* 193 */         grow();
/*     */       }
/* 195 */       int index = this.currentParticleIndex * 12;
/* 196 */       this.floatValues[index++] = x;
/* 197 */       this.floatValues[index++] = y;
/* 198 */       this.floatValues[index++] = z;
/* 199 */       this.floatValues[index++] = xRot;
/* 200 */       this.floatValues[index++] = yRot;
/* 201 */       this.floatValues[index++] = zRot;
/* 202 */       this.floatValues[index++] = wRot;
/* 203 */       this.floatValues[index++] = scale;
/* 204 */       this.floatValues[index++] = u0;
/* 205 */       this.floatValues[index++] = u1;
/* 206 */       this.floatValues[index++] = v0;
/* 207 */       this.floatValues[index] = v1;
/* 208 */       index = this.currentParticleIndex * 2;
/* 209 */       this.intValues[index++] = color;
/* 210 */       this.intValues[index] = lightColor;
/* 211 */       this.currentParticleIndex++;
/*     */     }
/*     */     
/*     */     public void forEachParticle(QuadParticleRenderState.ParticleConsumer consumer) {
/* 215 */       for (int particleIndex = 0; particleIndex < this.currentParticleIndex; particleIndex++) {
/* 216 */         int floatIndex = particleIndex * 12;
/* 217 */         int intIndex = particleIndex * 2;
/* 218 */         consumer.consume(this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex++], this.floatValues[floatIndex], this.intValues[intIndex++], this.intValues[intIndex]);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 238 */       this.currentParticleIndex = 0;
/*     */     }
/*     */     
/*     */     private void grow() {
/* 242 */       this.capacity *= 2;
/* 243 */       this.floatValues = Arrays.copyOf(this.floatValues, this.capacity * 12);
/* 244 */       this.intValues = Arrays.copyOf(this.intValues, this.capacity * 2);
/*     */     }
/*     */     
/*     */     public int count() {
/* 248 */       return this.currentParticleIndex;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/QuadParticleRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */