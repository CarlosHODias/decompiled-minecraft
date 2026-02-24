/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ 
/*     */ public class CompiledSectionMesh
/*     */   implements SectionMesh
/*     */ {
/*  19 */   public static final SectionMesh UNCOMPILED = new SectionMesh()
/*     */     {
/*     */       public boolean facesCanSeeEachother(Direction direction1, Direction direction2) {
/*  22 */         return false;
/*     */       }
/*     */     };
/*  25 */   public static final SectionMesh EMPTY = new SectionMesh()
/*     */     {
/*     */       public boolean facesCanSeeEachother(Direction direction1, Direction direction2) {
/*  28 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private final List<BlockEntity> renderableBlockEntities;
/*     */   private final VisibilitySet visibilitySet;
/*     */   private final MeshData.SortState transparencyState;
/*     */   private TranslucencyPointOfView translucencyPointOfView;
/*  36 */   private final Map<ChunkSectionLayer, SectionBuffers> buffers = new EnumMap<>(ChunkSectionLayer.class);
/*     */   
/*     */   public CompiledSectionMesh(TranslucencyPointOfView translucencyPointOfView, SectionCompiler.Results results) {
/*  39 */     this.translucencyPointOfView = translucencyPointOfView;
/*  40 */     this.visibilitySet = results.visibilitySet;
/*  41 */     this.renderableBlockEntities = results.blockEntities;
/*  42 */     this.transparencyState = results.transparencyState;
/*     */   }
/*     */   
/*     */   public void setTranslucencyPointOfView(TranslucencyPointOfView translucencyPointOfView) {
/*  46 */     this.translucencyPointOfView = translucencyPointOfView;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifferentPointOfView(TranslucencyPointOfView pointOfView) {
/*  51 */     return !pointOfView.equals(this.translucencyPointOfView);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasRenderableLayers() {
/*  56 */     return !this.buffers.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty(ChunkSectionLayer layer) {
/*  61 */     return !this.buffers.containsKey(layer);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BlockEntity> getRenderableBlockEntities() {
/*  66 */     return this.renderableBlockEntities;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean facesCanSeeEachother(Direction direction1, Direction direction2) {
/*  71 */     return this.visibilitySet.visibilityBetween(direction1, direction2);
/*     */   }
/*     */ 
/*     */   
/*     */   public SectionBuffers getBuffers(ChunkSectionLayer layer) {
/*  76 */     return this.buffers.get(layer);
/*     */   }
/*     */   
/*     */   public void uploadMeshLayer(ChunkSectionLayer layer, MeshData mesh, long sectionNode) {
/*  80 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  81 */     SectionBuffers sectionBuffers = getBuffers(layer);
/*  82 */     if (sectionBuffers != null) {
/*  83 */       if (sectionBuffers.getVertexBuffer().size() < mesh.vertexBuffer().remaining()) {
/*  84 */         sectionBuffers.getVertexBuffer().close();
/*  85 */         sectionBuffers.setVertexBuffer(RenderSystem.getDevice().createBuffer(() -> "Section vertex buffer - layer: " + layer.label() + "; cords: " + SectionPos.x(sectionNode) + ", " + SectionPos.y(sectionNode) + ", " + SectionPos.z(sectionNode), 40, 
/*     */               
/*  87 */               mesh.vertexBuffer()));
/*     */       }
/*  89 */       else if (!sectionBuffers.getVertexBuffer().isClosed()) {
/*  90 */         commandEncoder.writeToBuffer(sectionBuffers.getVertexBuffer().slice(), mesh.vertexBuffer());
/*     */       } 
/*     */ 
/*     */       
/*  94 */       ByteBuffer indexByteBuffer = mesh.indexBuffer();
/*  95 */       if (indexByteBuffer != null) {
/*  96 */         if (sectionBuffers.getIndexBuffer() == null || sectionBuffers.getIndexBuffer().size() < indexByteBuffer.remaining()) {
/*  97 */           if (sectionBuffers.getIndexBuffer() != null) {
/*  98 */             sectionBuffers.getIndexBuffer().close();
/*     */           }
/* 100 */           sectionBuffers.setIndexBuffer(RenderSystem.getDevice().createBuffer(() -> "Section index buffer - layer: " + layer.label() + "; cords: " + SectionPos.x(sectionNode) + ", " + SectionPos.y(sectionNode) + ", " + SectionPos.z(sectionNode), 72, indexByteBuffer));
/*     */ 
/*     */         
/*     */         }
/* 104 */         else if (!sectionBuffers.getIndexBuffer().isClosed()) {
/* 105 */           commandEncoder.writeToBuffer(sectionBuffers.getIndexBuffer().slice(), indexByteBuffer);
/*     */         }
/*     */       
/* 108 */       } else if (sectionBuffers.getIndexBuffer() != null) {
/* 109 */         sectionBuffers.getIndexBuffer().close();
/* 110 */         sectionBuffers.setIndexBuffer(null);
/*     */       } 
/*     */       
/* 113 */       sectionBuffers.setIndexCount(mesh.drawState().indexCount());
/* 114 */       sectionBuffers.setIndexType(mesh.drawState().indexType());
/*     */     } else {
/* 116 */       GpuBuffer vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Section vertex buffer - layer: " + layer.label() + "; cords: " + SectionPos.x(sectionNode) + ", " + SectionPos.y(sectionNode) + ", " + SectionPos.z(sectionNode), 40, 
/*     */           
/* 118 */           mesh.vertexBuffer());
/* 119 */       ByteBuffer indexByteBuffer = mesh.indexBuffer();
/* 120 */       GpuBuffer indexBuffer = (indexByteBuffer != null) ? RenderSystem.getDevice().createBuffer(() -> "Section index buffer - layer: " + layer.label() + "; cords: " + SectionPos.x(sectionNode) + ", " + SectionPos.y(sectionNode) + ", " + SectionPos.z(sectionNode), 72, indexByteBuffer) : 
/*     */         
/* 122 */         null;
/* 123 */       SectionBuffers newSectionBuffers = new SectionBuffers(vertexBuffer, indexBuffer, mesh.drawState().indexCount(), mesh.drawState().indexType());
/* 124 */       this.buffers.put(layer, newSectionBuffers);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void uploadLayerIndexBuffer(ChunkSectionLayer layer, ByteBufferBuilder.Result indexBuffer, long sectionNode) {
/* 129 */     SectionBuffers target = getBuffers(layer);
/* 130 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     if (target.getIndexBuffer() == null) {
/* 135 */       target.setIndexBuffer(RenderSystem.getDevice().createBuffer(() -> "Section index buffer - layer: " + layer.label() + "; cords: " + SectionPos.x(sectionNode) + ", " + SectionPos.y(sectionNode) + ", " + SectionPos.z(sectionNode), 72, 
/*     */             
/* 137 */             indexBuffer.byteBuffer()));
/*     */     } else {
/* 139 */       CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/* 140 */       if (!target.getIndexBuffer().isClosed()) {
/* 141 */         commandEncoder.writeToBuffer(target.getIndexBuffer().slice(), indexBuffer.byteBuffer());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasTranslucentGeometry() {
/* 148 */     return this.buffers.containsKey(ChunkSectionLayer.TRANSLUCENT);
/*     */   }
/*     */   
/*     */   public MeshData.SortState getTransparencyState() {
/* 152 */     return this.transparencyState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 157 */     this.buffers.values().forEach(SectionBuffers::close);
/* 158 */     this.buffers.clear();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/CompiledSectionMesh.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */