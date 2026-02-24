/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*     */ import net.minecraft.client.model.object.banner.BannerModel;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.BannerBlock;
/*     */ import net.minecraft.world.level.block.WallBannerBlock;
/*     */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BannerRenderer
/*     */   implements BlockEntityRenderer<BannerBlockEntity, BannerRenderState> {
/*     */   private static final int MAX_PATTERNS = 16;
/*     */   private static final float SIZE = 0.6666667F;
/*     */   private final MaterialSet materials;
/*     */   private final BannerModel standingModel;
/*     */   private final BannerModel wallModel;
/*     */   private final BannerFlagModel standingFlagModel;
/*     */   private final BannerFlagModel wallFlagModel;
/*     */   
/*     */   public BannerRenderer(BlockEntityRendererProvider.Context context) {
/*  50 */     this(context.entityModelSet(), context.materials());
/*     */   }
/*     */   
/*     */   public BannerRenderer(SpecialModelRenderer.BakingContext context) {
/*  54 */     this(context.entityModelSet(), context.materials());
/*     */   }
/*     */   
/*     */   public BannerRenderer(EntityModelSet modelSet, MaterialSet materials) {
/*  58 */     this.materials = materials;
/*  59 */     this.standingModel = new BannerModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER));
/*  60 */     this.wallModel = new BannerModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER));
/*  61 */     this.standingFlagModel = new BannerFlagModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
/*  62 */     this.wallFlagModel = new BannerFlagModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER_FLAG));
/*     */   }
/*     */ 
/*     */   
/*     */   public BannerRenderState createRenderState() {
/*  67 */     return new BannerRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(BannerBlockEntity blockEntity, BannerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  72 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  73 */     state.baseColor = blockEntity.getBaseColor();
/*  74 */     state.patterns = blockEntity.getPatterns();
/*     */     
/*  76 */     BlockState blockState = blockEntity.getBlockState();
/*  77 */     if (blockState.getBlock() instanceof BannerBlock) {
/*  78 */       state.angle = -RotationSegment.convertToDegrees((Integer)blockState.getValue((Property)BannerBlock.ROTATION));
/*  79 */       state.standing = true;
/*     */     } else {
/*  81 */       state.angle = -((Direction)blockState.getValue((Property)WallBannerBlock.FACING)).toYRot();
/*  82 */       state.standing = false;
/*     */     } 
/*     */     
/*  85 */     long gameTime = (blockEntity.getLevel() != null) ? blockEntity.getLevel().getGameTime() : 0L;
/*  86 */     BlockPos blockPos = blockEntity.getBlockPos();
/*     */     
/*  88 */     state.phase = ((float)Math.floorMod((blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + gameTime, 100L) + partialTicks) / 100.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void submit(BannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*     */     BannerModel model;
/*     */     BannerFlagModel flagModel;
/*  96 */     if (state.standing) {
/*  97 */       model = this.standingModel;
/*  98 */       flagModel = this.standingFlagModel;
/*     */     } else {
/* 100 */       model = this.wallModel;
/* 101 */       flagModel = this.wallFlagModel;
/*     */     } 
/*     */     
/* 104 */     submitBanner(this.materials, poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.angle, model, flagModel, state.phase, state.baseColor, state.patterns, state.breakProgress, 0);
/*     */   }
/*     */   
/*     */   public void submitSpecial(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, DyeColor baseColor, BannerPatternLayers patterns, int outlineColor) {
/* 108 */     submitBanner(this.materials, poseStack, submitNodeCollector, lightCoords, overlayCoords, 0.0F, this.standingModel, this.standingFlagModel, 0.0F, baseColor, patterns, null, outlineColor);
/*     */   }
/*     */   
/*     */   private static void submitBanner(MaterialSet materials, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, float angle, BannerModel model, BannerFlagModel flagModel, float phase, DyeColor baseColor, BannerPatternLayers patterns, ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor) {
/* 112 */     poseStack.pushPose();
/* 113 */     poseStack.translate(0.5F, 0.0F, 0.5F);
/* 114 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(angle));
/* 115 */     poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
/* 116 */     Material material = ModelBakery.BANNER_BASE;
/* 117 */     submitNodeCollector.submitModel((Model)model, Unit.INSTANCE, poseStack, material.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, materials.get(material), outlineColor, breakProgress);
/*     */     
/* 119 */     submitPatterns(materials, poseStack, submitNodeCollector, lightCoords, overlayCoords, (Model<Float>)flagModel, phase, material, true, baseColor, patterns, false, breakProgress, outlineColor);
/*     */     
/* 121 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public static <S> void submitPatterns(MaterialSet materials, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model<S> model, S state, Material baseMaterial, boolean banner, DyeColor baseColor, BannerPatternLayers patterns, boolean hasFoil, ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor) {
/* 125 */     submitNodeCollector.submitModel(model, state, poseStack, baseMaterial.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, materials.get(baseMaterial), outlineColor, breakProgress);
/* 126 */     if (hasFoil) {
/* 127 */       submitNodeCollector.submitModel(model, state, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, materials.get(baseMaterial), 0, breakProgress);
/*     */     }
/*     */     
/* 130 */     submitPatternLayer(materials, poseStack, submitNodeCollector, lightCoords, overlayCoords, model, state, banner ? Sheets.BANNER_BASE : Sheets.SHIELD_BASE, baseColor, breakProgress);
/*     */     
/* 132 */     for (int maskIndex = 0; maskIndex < 16 && maskIndex < patterns.layers().size(); maskIndex++) {
/* 133 */       BannerPatternLayers.Layer layer = patterns.layers().get(maskIndex);
/* 134 */       Material material = banner ? Sheets.getBannerMaterial(layer.pattern()) : Sheets.getShieldMaterial(layer.pattern());
/* 135 */       submitPatternLayer(materials, poseStack, submitNodeCollector, lightCoords, overlayCoords, model, state, material, layer.color(), null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <S> void submitPatternLayer(MaterialSet materials, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model<S> model, S state, Material material, DyeColor color, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 140 */     int diffuseColor = color.getTextureDiffuseColor();
/* 141 */     submitNodeCollector.submitModel(model, state, poseStack, material.renderType(RenderTypes::entityNoOutline), lightCoords, overlayCoords, diffuseColor, materials.get(material), 0, breakProgress);
/*     */   }
/*     */   
/*     */   public void getExtents(Consumer<Vector3fc> output) {
/* 145 */     PoseStack poseStack = new PoseStack();
/* 146 */     poseStack.translate(0.5F, 0.0F, 0.5F);
/* 147 */     poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
/* 148 */     this.standingModel.root().getExtentsForGui(poseStack, output);
/* 149 */     this.standingFlagModel.setupAnim(0.0F);
/* 150 */     this.standingFlagModel.root().getExtentsForGui(poseStack, output);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BannerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */