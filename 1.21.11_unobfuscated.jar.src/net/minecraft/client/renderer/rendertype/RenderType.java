/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.systems.ScissorState;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Consumer;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ 
/*     */ public class RenderType
/*     */ {
/*     */   private static final int MEGABYTE = 1048576;
/*     */   public static final int BIG_BUFFER_SIZE = 4194304;
/*     */   public static final int SMALL_BUFFER_SIZE = 786432;
/*     */   public static final int TRANSIENT_BUFFER_SIZE = 1536;
/*     */   private final RenderSetup state;
/*     */   private final Optional<RenderType> outline;
/*     */   protected final String name;
/*     */   
/*     */   private RenderType(String name, RenderSetup state) {
/*  37 */     this.name = name;
/*  38 */     this.state = state;
/*  39 */     this.outline = (state.outlineProperty == RenderSetup.OutlineProperty.AFFECTS_OUTLINE) ? state.textures.values().stream().findFirst().map(texture -> (RenderType)RenderTypes.OUTLINE.apply(texture.location(), state.pipeline.isCull())) : Optional.<RenderType>empty();
/*     */   }
/*     */   
/*     */   static RenderType create(String name, RenderSetup state) {
/*  43 */     return new RenderType(name, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  48 */     return "RenderType[" + this.name + ":" + String.valueOf(this.state) + "]";
/*     */   }
/*     */   
/*     */   public void draw(MeshData mesh) {
/*  52 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/*  53 */     Consumer<Matrix4fStack> modelViewModifier = this.state.layeringTransform.getModifier();
/*  54 */     if (modelViewModifier != null) {
/*  55 */       modelViewStack.pushMatrix();
/*  56 */       modelViewModifier.accept(modelViewStack);
/*     */     } 
/*  58 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), (Vector3fc)new Vector3f(), (Matrix4fc)this.state.textureTransform.getMatrix());
/*     */ 
/*     */     
/*  61 */     Map<String, RenderSetup.TextureAndSampler> textures = this.state.getTextures();
/*     */     
/*  63 */     MeshData meshData = mesh; 
/*  64 */     try { GpuBuffer indices; VertexFormat.IndexType indexType; GpuBuffer vertices = this.state.pipeline.getVertexFormat().uploadImmediateVertexBuffer(mesh.vertexBuffer());
/*     */ 
/*     */ 
/*     */       
/*  68 */       if (mesh.indexBuffer() == null) {
/*  69 */         RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(mesh.drawState().mode());
/*  70 */         indices = autoIndices.getBuffer(mesh.drawState().indexCount());
/*  71 */         indexType = autoIndices.type();
/*     */       } else {
/*  73 */         indices = this.state.pipeline.getVertexFormat().uploadImmediateIndexBuffer(mesh.indexBuffer());
/*  74 */         indexType = mesh.drawState().indexType();
/*     */       } 
/*     */       
/*  77 */       RenderTarget renderTarget = this.state.outputTarget.getRenderTarget();
/*  78 */       GpuTextureView colorTexture = (RenderSystem.outputColorTextureOverride != null) ? RenderSystem.outputColorTextureOverride : renderTarget.getColorTextureView();
/*  79 */       GpuTextureView depthTexture = renderTarget.useDepth ? ((RenderSystem.outputDepthTextureOverride != null) ? RenderSystem.outputDepthTextureOverride : renderTarget.getDepthTextureView()) : null;
/*  80 */       RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Immediate draw for " + this.name, colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/*  81 */       try { renderPass.setPipeline(this.state.pipeline);
/*  82 */         ScissorState scissorState = RenderSystem.getScissorStateForRenderTypeDraws();
/*  83 */         if (scissorState.enabled()) {
/*  84 */           renderPass.enableScissor(scissorState.x(), scissorState.y(), scissorState.width(), scissorState.height());
/*     */         }
/*  86 */         RenderSystem.bindDefaultUniforms(renderPass);
/*  87 */         renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/*  88 */         renderPass.setVertexBuffer(0, vertices);
/*     */         
/*  90 */         for (Map.Entry<String, RenderSetup.TextureAndSampler> entry : textures.entrySet()) {
/*  91 */           renderPass.bindTexture(entry.getKey(), ((RenderSetup.TextureAndSampler)entry.getValue()).textureView(), ((RenderSetup.TextureAndSampler)entry.getValue()).sampler());
/*     */         }
/*     */         
/*  94 */         renderPass.setIndexBuffer(indices, indexType);
/*  95 */         renderPass.drawIndexed(0, 0, mesh.drawState().indexCount(), 1);
/*  96 */         if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*  97 */           try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (meshData != null) meshData.close();  } catch (Throwable throwable) { if (meshData != null)
/*     */         try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  99 */      if (modelViewModifier != null) {
/* 100 */       modelViewStack.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public int bufferSize() {
/* 105 */     return this.state.bufferSize;
/*     */   }
/*     */   
/*     */   public VertexFormat format() {
/* 109 */     return this.state.pipeline.getVertexFormat();
/*     */   }
/*     */   
/*     */   public VertexFormat.Mode mode() {
/* 113 */     return this.state.pipeline.getVertexFormatMode();
/*     */   }
/*     */   
/*     */   public Optional<RenderType> outline() {
/* 117 */     return this.outline;
/*     */   }
/*     */   
/*     */   public boolean isOutline() {
/* 121 */     return (this.state.outlineProperty == RenderSetup.OutlineProperty.IS_OUTLINE);
/*     */   }
/*     */   
/*     */   public RenderPipeline pipeline() {
/* 125 */     return this.state.pipeline;
/*     */   }
/*     */   
/*     */   public boolean affectsCrumbling() {
/* 129 */     return this.state.affectsCrumbling;
/*     */   }
/*     */   
/*     */   public boolean canConsolidateConsecutiveGeometry() {
/* 133 */     return !(mode()).connectedPrimitives;
/*     */   }
/*     */   
/*     */   public boolean sortOnUpload() {
/* 137 */     return this.state.sortOnUpload;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/rendertype/RenderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */