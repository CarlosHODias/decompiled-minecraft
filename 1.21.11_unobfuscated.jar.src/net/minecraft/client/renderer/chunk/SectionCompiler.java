/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.ItemBlockRenderTypes;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*     */ import net.minecraft.client.renderer.block.ModelBlockRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ 
/*     */ public class SectionCompiler
/*     */ {
/*     */   private final BlockRenderDispatcher blockRenderer;
/*     */   private final BlockEntityRenderDispatcher blockEntityRenderer;
/*     */   
/*     */   public SectionCompiler(BlockRenderDispatcher blockRenderer, BlockEntityRenderDispatcher blockEntityRenderer) {
/*  37 */     this.blockRenderer = blockRenderer;
/*  38 */     this.blockEntityRenderer = blockEntityRenderer;
/*     */   }
/*     */   
/*     */   public Results compile(SectionPos sectionPos, RenderSectionRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack builders) {
/*  42 */     Results results = new Results();
/*     */     
/*  44 */     BlockPos minPos = sectionPos.origin();
/*  45 */     BlockPos maxPos = minPos.offset(15, 15, 15);
/*     */     
/*  47 */     VisGraph visGraph = new VisGraph();
/*  48 */     PoseStack poseStack = new PoseStack();
/*     */     
/*  50 */     ModelBlockRenderer.enableCaching();
/*     */     
/*  52 */     Map<ChunkSectionLayer, BufferBuilder> startedLayers = new EnumMap<>(ChunkSectionLayer.class);
/*     */     
/*  54 */     RandomSource random = RandomSource.create();
/*  55 */     ObjectArrayList objectArrayList = new ObjectArrayList();
/*     */     
/*  57 */     for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(minPos, maxPos)) {
/*  58 */       BlockState blockState = region.getBlockState(pos);
/*  59 */       if (blockState.isSolidRender()) {
/*  60 */         visGraph.setOpaque(pos);
/*     */       }
/*     */       
/*  63 */       if (blockState.hasBlockEntity()) {
/*  64 */         BlockEntity blockEntity = region.getBlockEntity(pos);
/*  65 */         if (blockEntity != null) {
/*  66 */           handleBlockEntity(results, blockEntity);
/*     */         }
/*     */       } 
/*     */       
/*  70 */       FluidState fluidState = blockState.getFluidState();
/*  71 */       if (!fluidState.isEmpty()) {
/*  72 */         ChunkSectionLayer layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
/*  73 */         BufferBuilder builder = getOrBeginLayer(startedLayers, builders, layer);
/*  74 */         this.blockRenderer.renderLiquid(pos, region, (VertexConsumer)builder, blockState, fluidState);
/*     */       } 
/*     */       
/*  77 */       if (blockState.getRenderShape() == RenderShape.MODEL) {
/*  78 */         ChunkSectionLayer layer = ItemBlockRenderTypes.getChunkRenderType(blockState);
/*  79 */         BufferBuilder builder = getOrBeginLayer(startedLayers, builders, layer);
/*     */         
/*  81 */         random.setSeed(blockState.getSeed(pos));
/*  82 */         this.blockRenderer.getBlockModel(blockState).collectParts(random, (List)objectArrayList);
/*     */         
/*  84 */         poseStack.pushPose();
/*  85 */         poseStack.translate(SectionPos.sectionRelative(pos.getX()), SectionPos.sectionRelative(pos.getY()), SectionPos.sectionRelative(pos.getZ()));
/*  86 */         this.blockRenderer.renderBatched(blockState, pos, region, poseStack, (VertexConsumer)builder, true, (List)objectArrayList);
/*  87 */         poseStack.popPose();
/*     */         
/*  89 */         objectArrayList.clear();
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     for (Map.Entry<ChunkSectionLayer, BufferBuilder> entry : startedLayers.entrySet()) {
/*  94 */       ChunkSectionLayer layer = entry.getKey();
/*  95 */       MeshData mesh = ((BufferBuilder)entry.getValue()).build();
/*  96 */       if (mesh != null) {
/*  97 */         if (layer == ChunkSectionLayer.TRANSLUCENT) {
/*  98 */           results.transparencyState = mesh.sortQuads(builders.buffer(layer), vertexSorting);
/*     */         }
/* 100 */         results.renderedLayers.put(layer, mesh);
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     ModelBlockRenderer.clearCache();
/*     */     
/* 106 */     results.visibilitySet = visGraph.resolve();
/*     */     
/* 108 */     return results;
/*     */   }
/*     */   
/*     */   private BufferBuilder getOrBeginLayer(Map<ChunkSectionLayer, BufferBuilder> startedLayers, SectionBufferBuilderPack buffers, ChunkSectionLayer renderType) {
/* 112 */     BufferBuilder builder = startedLayers.get(renderType);
/* 113 */     if (builder == null) {
/* 114 */       ByteBufferBuilder buffer = buffers.buffer(renderType);
/* 115 */       builder = new BufferBuilder(buffer, VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
/* 116 */       startedLayers.put(renderType, builder);
/*     */     } 
/* 118 */     return builder;
/*     */   }
/*     */   
/*     */   private <E extends BlockEntity> void handleBlockEntity(Results results, E blockEntity) {
/* 122 */     BlockEntityRenderer<E, ?> renderer = this.blockEntityRenderer.getRenderer((BlockEntity)blockEntity);
/* 123 */     if (renderer != null && !renderer.shouldRenderOffScreen())
/* 124 */       results.blockEntities.add((BlockEntity)blockEntity); 
/*     */   }
/*     */   
/*     */   public static final class Results
/*     */   {
/* 129 */     public final List<BlockEntity> blockEntities = new ArrayList<>();
/* 130 */     public final Map<ChunkSectionLayer, MeshData> renderedLayers = new EnumMap<>(ChunkSectionLayer.class);
/* 131 */     public VisibilitySet visibilitySet = new VisibilitySet();
/*     */     public MeshData.SortState transparencyState;
/*     */     
/*     */     public void release() {
/* 135 */       this.renderedLayers.values().forEach(MeshData::close);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/SectionCompiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */