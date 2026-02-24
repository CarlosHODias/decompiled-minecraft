/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.CubeMapTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class CubeMap
/*     */   implements AutoCloseable {
/*     */   private static final int SIDES = 6;
/*     */   private final GpuBuffer vertexBuffer;
/*     */   private final CachedPerspectiveProjectionMatrixBuffer projectionMatrixUbo;
/*     */   private final Identifier location;
/*     */   
/*     */   public CubeMap(Identifier base) {
/*  39 */     this.location = base;
/*  40 */     this.projectionMatrixUbo = new CachedPerspectiveProjectionMatrixBuffer("cubemap", 0.05F, 10.0F);
/*  41 */     this.vertexBuffer = initializeVertices();
/*     */   }
/*     */   
/*     */   public void render(Minecraft minecraft, float rotXInDegrees, float rotYInDegrees) {
/*  45 */     RenderSystem.setProjectionMatrix(this.projectionMatrixUbo.getBuffer(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), 85.0F), ProjectionType.PERSPECTIVE);
/*     */     
/*  47 */     RenderPipeline renderPipeline = RenderPipelines.PANORAMA;
/*  48 */     RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
/*  49 */     GpuTextureView colorTexture = mainRenderTarget.getColorTextureView();
/*  50 */     GpuTextureView depthTexture = mainRenderTarget.getDepthTextureView();
/*  51 */     RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/*  52 */     GpuBuffer indexBuffer = indices.getBuffer(36);
/*     */     
/*  54 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/*  55 */     modelViewStack.pushMatrix();
/*  56 */     modelViewStack.rotationX(3.1415927F);
/*  57 */     modelViewStack.rotateX(rotXInDegrees * 0.017453292F);
/*  58 */     modelViewStack.rotateY(rotYInDegrees * 0.017453292F);
/*  59 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)new Matrix4f((Matrix4fc)modelViewStack), (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)new Matrix4f());
/*  60 */     modelViewStack.popMatrix();
/*     */     
/*  62 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Cubemap", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/*  63 */     try { renderPass.setPipeline(renderPipeline);
/*  64 */       RenderSystem.bindDefaultUniforms(renderPass);
/*  65 */       renderPass.setVertexBuffer(0, this.vertexBuffer);
/*  66 */       renderPass.setIndexBuffer(indexBuffer, indices.type());
/*     */       
/*  68 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/*  69 */       AbstractTexture texture = minecraft.getTextureManager().getTexture(this.location);
/*  70 */       renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
/*  71 */       renderPass.drawIndexed(0, 0, 36, 1);
/*  72 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  76 */      } private static GpuBuffer initializeVertices() { ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 4 * 6); 
/*  77 */     try { BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
/*  78 */       bufferBuilder.addVertex(-1.0F, -1.0F, 1.0F);
/*  79 */       bufferBuilder.addVertex(-1.0F, 1.0F, 1.0F);
/*  80 */       bufferBuilder.addVertex(1.0F, 1.0F, 1.0F);
/*  81 */       bufferBuilder.addVertex(1.0F, -1.0F, 1.0F);
/*     */       
/*  83 */       bufferBuilder.addVertex(1.0F, -1.0F, 1.0F);
/*  84 */       bufferBuilder.addVertex(1.0F, 1.0F, 1.0F);
/*  85 */       bufferBuilder.addVertex(1.0F, 1.0F, -1.0F);
/*  86 */       bufferBuilder.addVertex(1.0F, -1.0F, -1.0F);
/*     */       
/*  88 */       bufferBuilder.addVertex(1.0F, -1.0F, -1.0F);
/*  89 */       bufferBuilder.addVertex(1.0F, 1.0F, -1.0F);
/*  90 */       bufferBuilder.addVertex(-1.0F, 1.0F, -1.0F);
/*  91 */       bufferBuilder.addVertex(-1.0F, -1.0F, -1.0F);
/*     */       
/*  93 */       bufferBuilder.addVertex(-1.0F, -1.0F, -1.0F);
/*  94 */       bufferBuilder.addVertex(-1.0F, 1.0F, -1.0F);
/*  95 */       bufferBuilder.addVertex(-1.0F, 1.0F, 1.0F);
/*  96 */       bufferBuilder.addVertex(-1.0F, -1.0F, 1.0F);
/*     */       
/*  98 */       bufferBuilder.addVertex(-1.0F, -1.0F, -1.0F);
/*  99 */       bufferBuilder.addVertex(-1.0F, -1.0F, 1.0F);
/* 100 */       bufferBuilder.addVertex(1.0F, -1.0F, 1.0F);
/* 101 */       bufferBuilder.addVertex(1.0F, -1.0F, -1.0F);
/*     */       
/* 103 */       bufferBuilder.addVertex(-1.0F, 1.0F, 1.0F);
/* 104 */       bufferBuilder.addVertex(-1.0F, 1.0F, -1.0F);
/* 105 */       bufferBuilder.addVertex(1.0F, 1.0F, -1.0F);
/* 106 */       bufferBuilder.addVertex(1.0F, 1.0F, 1.0F);
/*     */       
/* 108 */       MeshData meshData = bufferBuilder.buildOrThrow(); 
/* 109 */       try { GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Cube map vertex buffer", 32, meshData.vertexBuffer());
/* 110 */         if (meshData != null) meshData.close(); 
/* 111 */         if (byteBufferBuilder != null) byteBufferBuilder.close();  return gpuBuffer; } catch (Throwable throwable) { if (meshData != null)
/*     */           try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 115 */      } public void registerTextures(TextureManager textureManager) { textureManager.register(this.location, (AbstractTexture)new CubeMapTexture(this.location)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 120 */     this.vertexBuffer.close();
/* 121 */     this.projectionMatrixUbo.close();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/CubeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */