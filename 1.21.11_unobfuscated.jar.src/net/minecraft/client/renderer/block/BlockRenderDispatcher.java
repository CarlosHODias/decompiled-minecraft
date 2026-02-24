/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.renderer.ItemBlockRenderTypes;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class BlockRenderDispatcher
/*     */   implements ResourceManagerReloadListener {
/*     */   private final BlockModelShaper blockModelShaper;
/*     */   private final MaterialSet materials;
/*     */   private final ModelBlockRenderer modelRenderer;
/*     */   private LiquidBlockRenderer liquidBlockRenderer;
/*  34 */   private final RandomSource singleThreadRandom = RandomSource.create();
/*  35 */   private final List<BlockModelPart> singleThreadPartList = new ArrayList<>();
/*     */   private final BlockColors blockColors;
/*     */   
/*     */   public BlockRenderDispatcher(BlockModelShaper blockModelShaper, MaterialSet materials, BlockColors blockColors) {
/*  39 */     this.blockModelShaper = blockModelShaper;
/*  40 */     this.materials = materials;
/*  41 */     this.blockColors = blockColors;
/*  42 */     this.modelRenderer = new ModelBlockRenderer(this.blockColors);
/*     */   }
/*     */   
/*     */   public BlockModelShaper getBlockModelShaper() {
/*  46 */     return this.blockModelShaper;
/*     */   }
/*     */   
/*     */   public void renderBreakingTexture(BlockState state, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer builder) {
/*  50 */     if (state.getRenderShape() != RenderShape.MODEL) {
/*     */       return;
/*     */     }
/*     */     
/*  54 */     BlockStateModel model = this.blockModelShaper.getBlockModel(state);
/*  55 */     this.singleThreadRandom.setSeed(state.getSeed(pos));
/*  56 */     this.singleThreadPartList.clear();
/*  57 */     model.collectParts(this.singleThreadRandom, this.singleThreadPartList);
/*     */     
/*  59 */     this.modelRenderer.tesselateBlock(level, this.singleThreadPartList, state, pos, poseStack, builder, true, OverlayTexture.NO_OVERLAY);
/*     */   }
/*     */   
/*     */   public void renderBatched(BlockState blockState, BlockPos pos, BlockAndTintGetter level, PoseStack poseStack, VertexConsumer builder, boolean cull, List<BlockModelPart> parts) {
/*     */     try {
/*  64 */       this.modelRenderer.tesselateBlock(level, parts, blockState, pos, poseStack, builder, cull, OverlayTexture.NO_OVERLAY);
/*  65 */     } catch (Throwable t) {
/*  66 */       CrashReport report = CrashReport.forThrowable(t, "Tesselating block in world");
/*  67 */       CrashReportCategory category = report.addCategory("Block being tesselated");
/*     */       
/*  69 */       CrashReportCategory.populateBlockDetails(category, (LevelHeightAccessor)level, pos, blockState);
/*     */       
/*  71 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderLiquid(BlockPos pos, BlockAndTintGetter level, VertexConsumer builder, BlockState blockState, FluidState fluidState) {
/*     */     try {
/*  77 */       ((LiquidBlockRenderer)Objects.<LiquidBlockRenderer>requireNonNull(this.liquidBlockRenderer)).tesselate(level, pos, builder, blockState, fluidState);
/*  78 */     } catch (Throwable t) {
/*  79 */       CrashReport report = CrashReport.forThrowable(t, "Tesselating liquid in world");
/*  80 */       CrashReportCategory category = report.addCategory("Block being tesselated");
/*     */       
/*  82 */       CrashReportCategory.populateBlockDetails(category, (LevelHeightAccessor)level, pos, blockState);
/*     */       
/*  84 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ModelBlockRenderer getModelRenderer() {
/*  89 */     return this.modelRenderer;
/*     */   }
/*     */   
/*     */   public BlockStateModel getBlockModel(BlockState state) {
/*  93 */     return this.blockModelShaper.getBlockModel(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int overlayCoords) {
/*  98 */     RenderShape shape = state.getRenderShape();
/*  99 */     if (shape == RenderShape.INVISIBLE) {
/*     */       return;
/*     */     }
/*     */     
/* 103 */     BlockStateModel model = getBlockModel(state);
/*     */     
/* 105 */     int col = this.blockColors.getColor(state, null, null, 0);
/* 106 */     float r = (col >> 16 & 0xFF) / 255.0F;
/* 107 */     float g = (col >> 8 & 0xFF) / 255.0F;
/* 108 */     float b = (col & 0xFF) / 255.0F;
/*     */     
/* 110 */     ModelBlockRenderer.renderModel(poseStack.last(), bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state)), model, r, g, b, lightCoords, overlayCoords);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 115 */     this.liquidBlockRenderer = new LiquidBlockRenderer(this.materials);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/BlockRenderDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */