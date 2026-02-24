/*     */ package net.minecraft.client.renderer;
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
/*     */ import java.util.ArrayList;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.state.WorldBorderRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class WorldBorderRenderer {
/*  35 */   public static final Identifier FORCEFIELD_LOCATION = Identifier.withDefaultNamespace("textures/misc/forcefield.png");
/*     */   
/*     */   private boolean needsRebuild = true;
/*     */   
/*     */   private double lastMinX;
/*     */   
/*     */   private double lastMinZ;
/*     */   private double lastBorderMinX;
/*     */   private double lastBorderMaxX;
/*     */   private double lastBorderMinZ;
/*     */   private double lastBorderMaxZ;
/*  46 */   private final GpuBuffer worldBorderBuffer = RenderSystem.getDevice().createBuffer(() -> "World border vertex buffer", 40, 16L * DefaultVertexFormat.POSITION_TEX.getVertexSize());
/*     */   private final RenderSystem.AutoStorageIndexBuffer indices;
/*     */   
/*     */   public WorldBorderRenderer() {
/*  50 */     this.indices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
/*     */   }
/*     */   
/*     */   private void rebuildWorldBorderBuffer(WorldBorderRenderState state, double renderDistance, double cameraZ, double cameraX, float halfHeightY, float v1, float v0) {
/*  54 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION_TEX.getVertexSize() * 4 * 4); 
/*  55 */     try { double borderMinX = state.minX;
/*  56 */       double borderMaxX = state.maxX;
/*  57 */       double borderMinZ = state.minZ;
/*  58 */       double borderMaxZ = state.maxZ;
/*     */       
/*  60 */       double minZ = Math.max(Mth.floor(cameraZ - renderDistance), borderMinZ);
/*  61 */       double maxZ = Math.min(Mth.ceil(cameraZ + renderDistance), borderMaxZ);
/*  62 */       float u0z = (Mth.floor(minZ) & 0x1) * 0.5F;
/*  63 */       float u1z = (float)(maxZ - minZ) / 2.0F;
/*     */       
/*  65 */       double minX = Math.max(Mth.floor(cameraX - renderDistance), borderMinX);
/*  66 */       double maxX = Math.min(Mth.ceil(cameraX + renderDistance), borderMaxX);
/*  67 */       float u0x = (Mth.floor(minX) & 0x1) * 0.5F;
/*  68 */       float u1x = (float)(maxX - minX) / 2.0F;
/*     */       
/*  70 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
/*     */       
/*  72 */       bufferBuilder.addVertex(0.0F, -halfHeightY, (float)(borderMaxZ - minZ)).setUv(u0x, v1);
/*  73 */       bufferBuilder.addVertex((float)(maxX - minX), -halfHeightY, (float)(borderMaxZ - minZ)).setUv(u1x + u0x, v1);
/*  74 */       bufferBuilder.addVertex((float)(maxX - minX), halfHeightY, (float)(borderMaxZ - minZ)).setUv(u1x + u0x, v0);
/*  75 */       bufferBuilder.addVertex(0.0F, halfHeightY, (float)(borderMaxZ - minZ)).setUv(u0x, v0);
/*     */       
/*  77 */       bufferBuilder.addVertex(0.0F, -halfHeightY, 0.0F).setUv(u0z, v1);
/*  78 */       bufferBuilder.addVertex(0.0F, -halfHeightY, (float)(maxZ - minZ)).setUv(u1z + u0z, v1);
/*  79 */       bufferBuilder.addVertex(0.0F, halfHeightY, (float)(maxZ - minZ)).setUv(u1z + u0z, v0);
/*  80 */       bufferBuilder.addVertex(0.0F, halfHeightY, 0.0F).setUv(u0z, v0);
/*     */       
/*  82 */       bufferBuilder.addVertex((float)(maxX - minX), -halfHeightY, 0.0F).setUv(u0x, v1);
/*  83 */       bufferBuilder.addVertex(0.0F, -halfHeightY, 0.0F).setUv(u1x + u0x, v1);
/*  84 */       bufferBuilder.addVertex(0.0F, halfHeightY, 0.0F).setUv(u1x + u0x, v0);
/*  85 */       bufferBuilder.addVertex((float)(maxX - minX), halfHeightY, 0.0F).setUv(u0x, v0);
/*     */       
/*  87 */       bufferBuilder.addVertex((float)(borderMaxX - minX), -halfHeightY, (float)(maxZ - minZ)).setUv(u0z, v1);
/*  88 */       bufferBuilder.addVertex((float)(borderMaxX - minX), -halfHeightY, 0.0F).setUv(u1z + u0z, v1);
/*  89 */       bufferBuilder.addVertex((float)(borderMaxX - minX), halfHeightY, 0.0F).setUv(u1z + u0z, v0);
/*  90 */       bufferBuilder.addVertex((float)(borderMaxX - minX), halfHeightY, (float)(maxZ - minZ)).setUv(u0z, v0);
/*     */       
/*  92 */       MeshData meshData = bufferBuilder.buildOrThrow(); 
/*  93 */       try { RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.worldBorderBuffer.slice(), meshData.vertexBuffer());
/*  94 */         if (meshData != null) meshData.close();  } catch (Throwable throwable) { if (meshData != null)
/*     */           try { meshData.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  96 */        this.lastBorderMinX = borderMinX;
/*  97 */       this.lastBorderMaxX = borderMaxX;
/*  98 */       this.lastBorderMinZ = borderMinZ;
/*  99 */       this.lastBorderMaxZ = borderMaxZ;
/* 100 */       this.lastMinX = minX;
/* 101 */       this.lastMinZ = minZ;
/* 102 */       this.needsRebuild = false;
/* 103 */       if (byteBufferBuilder != null) byteBufferBuilder.close();  } catch (Throwable throwable) { if (byteBufferBuilder != null)
/*     */         try { byteBufferBuilder.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 107 */      } public void extract(WorldBorder border, float deltaPartialTick, Vec3 cameraPos, double renderDistance, WorldBorderRenderState state) { state.minX = border.getMinX(deltaPartialTick);
/* 108 */     state.maxX = border.getMaxX(deltaPartialTick);
/* 109 */     state.minZ = border.getMinZ(deltaPartialTick);
/* 110 */     state.maxZ = border.getMaxZ(deltaPartialTick);
/* 111 */     if ((cameraPos.x < state.maxX - renderDistance && cameraPos.x > state.minX + renderDistance && cameraPos.z < state.maxZ - renderDistance && cameraPos.z > state.minZ + renderDistance) || cameraPos.x < state.minX - renderDistance || cameraPos.x > state.maxX + renderDistance || cameraPos.z < state.minZ - renderDistance || cameraPos.z > state.maxZ + renderDistance) {
/*     */       
/* 113 */       state.alpha = 0.0D;
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     state.alpha = 1.0D - border.getDistanceToBorder(cameraPos.x, cameraPos.z) / renderDistance;
/* 118 */     state.alpha = Math.pow(state.alpha, 4.0D);
/* 119 */     state.alpha = Mth.clamp(state.alpha, 0.0D, 1.0D);
/* 120 */     state.tint = border.getStatus().getColor(); }
/*     */ 
/*     */   
/*     */   public void render(WorldBorderRenderState state, Vec3 cameraPos, double renderDistance, double depthFar) { GpuTextureView colorTexture, depthTexture;
/* 124 */     if (state.alpha <= 0.0D) {
/*     */       return;
/*     */     }
/* 127 */     double cameraX = cameraPos.x;
/* 128 */     double cameraZ = cameraPos.z;
/* 129 */     float halfHeightY = (float)depthFar;
/*     */     
/* 131 */     float red = ARGB.red(state.tint) / 255.0F;
/* 132 */     float green = ARGB.green(state.tint) / 255.0F;
/* 133 */     float blue = ARGB.blue(state.tint) / 255.0F;
/*     */     
/* 135 */     float offset = (float)(Util.getMillis() % 3000L) / 3000.0F;
/*     */     
/* 137 */     float v0 = (float)-Mth.frac(cameraPos.y * 0.5D);
/* 138 */     float v1 = v0 + halfHeightY;
/*     */     
/* 140 */     if (shouldRebuildWorldBorderBuffer(state)) {
/* 141 */       rebuildWorldBorderBuffer(state, renderDistance, cameraZ, cameraX, halfHeightY, v1, v0);
/*     */     }
/*     */     
/* 144 */     TextureManager textureManager = Minecraft.getInstance().getTextureManager();
/* 145 */     AbstractTexture abstractTexture = textureManager.getTexture(FORCEFIELD_LOCATION);
/*     */     
/* 147 */     RenderPipeline renderPipeline = RenderPipelines.WORLD_BORDER;
/*     */     
/* 149 */     RenderTarget mainRenderTarget = Minecraft.getInstance().getMainRenderTarget();
/* 150 */     RenderTarget weatherTarget = (Minecraft.getInstance()).levelRenderer.getWeatherTarget();
/*     */ 
/*     */     
/* 153 */     if (weatherTarget != null) {
/* 154 */       colorTexture = weatherTarget.getColorTextureView();
/* 155 */       depthTexture = weatherTarget.getDepthTextureView();
/*     */     } else {
/* 157 */       colorTexture = mainRenderTarget.getColorTextureView();
/* 158 */       depthTexture = mainRenderTarget.getDepthTextureView();
/*     */     } 
/*     */     
/* 161 */     GpuBuffer indexBuffer = this.indices.getBuffer(6);
/* 162 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((Matrix4fc)RenderSystem.getModelViewMatrix(), (Vector4fc)new Vector4f(red, green, blue, (float)state.alpha), (Vector3fc)new Vector3f((float)(this.lastMinX - cameraX), (float)-cameraPos.y, (float)(this.lastMinZ - cameraZ)), (Matrix4fc)new Matrix4f().translation(offset, offset, 0.0F));
/* 163 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "World border", colorTexture, OptionalInt.empty(), depthTexture, OptionalDouble.empty()); 
/* 164 */     try { renderPass.setPipeline(renderPipeline);
/* 165 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 166 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 167 */       renderPass.setIndexBuffer(indexBuffer, this.indices.type());
/* 168 */       renderPass.bindTexture("Sampler0", abstractTexture.getTextureView(), abstractTexture.getSampler());
/* 169 */       renderPass.setVertexBuffer(0, this.worldBorderBuffer);
/*     */       
/* 171 */       ArrayList<RenderPass.Draw<WorldBorderRenderer>> draws = new ArrayList<>();
/* 172 */       for (WorldBorderRenderState.DistancePerDirection distancePerDirection : (Iterable<WorldBorderRenderState.DistancePerDirection>)state.closestBorder(cameraX, cameraZ)) {
/* 173 */         if (distancePerDirection.distance() < renderDistance) {
/* 174 */           int sideIndex = distancePerDirection.direction().get2DDataValue();
/* 175 */           draws.add(new RenderPass.Draw(0, this.worldBorderBuffer, indexBuffer, this.indices.type(), 6 * sideIndex, 6));
/*     */         } 
/*     */       } 
/*     */       
/* 179 */       renderPass.drawMultipleIndexed(draws, null, null, java.util.Collections.emptyList(), this);
/* 180 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */         try { renderPass.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 184 */      } public void invalidate() { this.needsRebuild = true; }
/*     */ 
/*     */   
/*     */   private boolean shouldRebuildWorldBorderBuffer(WorldBorderRenderState state) {
/* 188 */     return (this.needsRebuild || state.minX != this.lastBorderMinX || state.minZ != this.lastBorderMinZ || state.maxX != this.lastBorderMaxX || state.maxZ != this.lastBorderMaxZ);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/WorldBorderRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */