/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Queue;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.MappableRingBuffer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ 
/*     */ public class ParticleFeatureRenderer
/*     */   implements AutoCloseable
/*     */ {
/*  26 */   private final Queue<ParticleBufferCache> availableBuffers = new ArrayDeque<>();
/*  27 */   private final List<ParticleBufferCache> usedBuffers = new ArrayList<>();
/*     */   
/*     */   public void render(SubmitNodeCollection nodeCollection) {
/*  30 */     if (nodeCollection.getParticleGroupRenderers().isEmpty()) {
/*     */       return;
/*     */     }
/*  33 */     GpuDevice device = RenderSystem.getDevice();
/*  34 */     Minecraft minecraft = Minecraft.getInstance();
/*  35 */     TextureManager textureManager = minecraft.getTextureManager();
/*  36 */     RenderTarget mainTarget = minecraft.getMainRenderTarget();
/*  37 */     RenderTarget particleTarget = minecraft.levelRenderer.getParticlesTarget();
/*     */     
/*  39 */     for (SubmitNodeCollector.ParticleGroupRenderer particleGroupRenderer : (Iterable<SubmitNodeCollector.ParticleGroupRenderer>)nodeCollection.getParticleGroupRenderers()) {
/*  40 */       ParticleBufferCache buffer = this.availableBuffers.poll();
/*  41 */       if (buffer == null) {
/*  42 */         buffer = new ParticleBufferCache();
/*     */       }
/*  44 */       this.usedBuffers.add(buffer);
/*  45 */       QuadParticleRenderState.PreparedBuffers prepared = particleGroupRenderer.prepare(buffer);
/*  46 */       if (prepared == null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  52 */       RenderPass renderPass = device.createCommandEncoder().createRenderPass(() -> "Particles - Main", mainTarget.getColorTextureView(), OptionalInt.empty(), mainTarget.getDepthTextureView(), OptionalDouble.empty()); 
/*  53 */       try { prepareRenderPass(renderPass);
/*  54 */         particleGroupRenderer.render(prepared, buffer, renderPass, textureManager, false);
/*     */         
/*  56 */         if (particleTarget == null)
/*     */         {
/*  58 */           particleGroupRenderer.render(prepared, buffer, renderPass, textureManager, true);
/*     */         }
/*  60 */         if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */           try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/*  63 */        if (particleTarget != null) {
/*  64 */         renderPass = device.createCommandEncoder().createRenderPass(() -> "Particles - Transparent", particleTarget.getColorTextureView(), OptionalInt.empty(), particleTarget.getDepthTextureView(), OptionalDouble.empty()); 
/*  65 */         try { prepareRenderPass(renderPass);
/*  66 */           particleGroupRenderer.render(prepared, buffer, renderPass, textureManager, true);
/*  67 */           if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */             try { renderPass.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/*     */       
/*     */       } 
/*  73 */     }  } public void endFrame() { for (ParticleBufferCache usedBuffer : this.usedBuffers) {
/*  74 */       usedBuffer.rotate();
/*     */     }
/*  76 */     this.availableBuffers.addAll(this.usedBuffers);
/*  77 */     this.usedBuffers.clear(); }
/*     */ 
/*     */   
/*     */   private void prepareRenderPass(RenderPass renderPass) {
/*  81 */     renderPass.setUniform("Projection", RenderSystem.getProjectionMatrixBuffer());
/*  82 */     renderPass.setUniform("Fog", RenderSystem.getShaderFog());
/*  83 */     renderPass.bindTexture("Sampler2", (Minecraft.getInstance()).gameRenderer.lightTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  88 */     this.availableBuffers.forEach(ParticleBufferCache::close);
/*     */   }
/*     */   
/*     */   public static class ParticleBufferCache implements AutoCloseable {
/*     */     private MappableRingBuffer ringBuffer;
/*     */     
/*     */     public void write(ByteBuffer byteBuffer) {
/*  95 */       if (this.ringBuffer == null || this.ringBuffer.size() < byteBuffer.remaining()) {
/*  96 */         if (this.ringBuffer != null) {
/*  97 */           this.ringBuffer.close();
/*     */         }
/*  99 */         this.ringBuffer = new MappableRingBuffer(() -> "Particle Vertices", 34, byteBuffer.remaining());
/*     */       } 
/* 101 */       GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.ringBuffer.currentBuffer().slice(), false, true); 
/* 102 */       try { view.data().put(byteBuffer);
/* 103 */         if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/*     */           try { view.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/* 107 */        } public GpuBuffer get() { if (this.ringBuffer == null) {
/* 108 */         throw new IllegalStateException("Can't get buffer before it's made");
/*     */       }
/* 110 */       return this.ringBuffer.currentBuffer(); }
/*     */ 
/*     */     
/*     */     void rotate() {
/* 114 */       if (this.ringBuffer != null) {
/* 115 */         this.ringBuffer.rotate();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 121 */       if (this.ringBuffer != null)
/* 122 */         this.ringBuffer.close(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/ParticleFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */