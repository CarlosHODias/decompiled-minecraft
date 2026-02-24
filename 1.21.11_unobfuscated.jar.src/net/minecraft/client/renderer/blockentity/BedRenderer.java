/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BedRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.DoubleBlockCombiner;
/*     */ import net.minecraft.world.level.block.entity.BedBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BedRenderer implements BlockEntityRenderer<BedBlockEntity, BedRenderState> {
/*     */   private final MaterialSet materials;
/*     */   private final Model.Simple headModel;
/*     */   private final Model.Simple footModel;
/*     */   
/*     */   public BedRenderer(BlockEntityRendererProvider.Context context) {
/*  48 */     this(context.materials(), context.entityModelSet());
/*     */   }
/*     */   
/*     */   public BedRenderer(SpecialModelRenderer.BakingContext context) {
/*  52 */     this(context.materials(), context.entityModelSet());
/*     */   }
/*     */   
/*     */   public BedRenderer(MaterialSet materials, EntityModelSet entityModelSet) {
/*  56 */     this.materials = materials;
/*  57 */     this.headModel = new Model.Simple(entityModelSet.bakeLayer(ModelLayers.BED_HEAD), RenderTypes::entitySolid);
/*  58 */     this.footModel = new Model.Simple(entityModelSet.bakeLayer(ModelLayers.BED_FOOT), RenderTypes::entitySolid);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createHeadLayer() {
/*  62 */     MeshDefinition mesh = new MeshDefinition();
/*  63 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  65 */     root.addOrReplaceChild("main", 
/*  66 */         CubeListBuilder.create()
/*  67 */         .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     root.addOrReplaceChild("left_leg", 
/*  73 */         CubeListBuilder.create()
/*  74 */         .texOffs(50, 6).addBox(0.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F), 
/*  75 */         PartPose.rotation(1.5707964F, 0.0F, 1.5707964F));
/*     */     
/*  77 */     root.addOrReplaceChild("right_leg", 
/*  78 */         CubeListBuilder.create()
/*  79 */         .texOffs(50, 18).addBox(-16.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F), 
/*  80 */         PartPose.rotation(1.5707964F, 0.0F, 3.1415927F));
/*     */ 
/*     */     
/*  83 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createFootLayer() {
/*  87 */     MeshDefinition mesh = new MeshDefinition();
/*  88 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  90 */     root.addOrReplaceChild("main", 
/*  91 */         CubeListBuilder.create()
/*  92 */         .texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/*  96 */     root.addOrReplaceChild("left_leg", 
/*  97 */         CubeListBuilder.create()
/*  98 */         .texOffs(50, 0).addBox(0.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F), 
/*  99 */         PartPose.rotation(1.5707964F, 0.0F, 0.0F));
/*     */     
/* 101 */     root.addOrReplaceChild("right_leg", 
/* 102 */         CubeListBuilder.create()
/* 103 */         .texOffs(50, 12).addBox(-16.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F), 
/* 104 */         PartPose.rotation(1.5707964F, 0.0F, 4.712389F));
/*     */ 
/*     */     
/* 107 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   public BedRenderState createRenderState() {
/* 112 */     return new BedRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(BedBlockEntity blockEntity, BedRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 117 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 118 */     state.color = blockEntity.getColor();
/* 119 */     state.facing = (Direction)blockEntity.getBlockState().getValue((Property)BedBlock.FACING);
/* 120 */     state.isHead = (blockEntity.getBlockState().getValue((Property)BedBlock.PART) == BedPart.HEAD);
/*     */     
/* 122 */     if (blockEntity.getLevel() != null) {
/* 123 */       DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> combineResult = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, (Property)ChestBlock.FACING, blockEntity.getBlockState(), (LevelAccessor)blockEntity.getLevel(), blockEntity.getBlockPos(), (levelAccessor, blockPos) -> false);
/* 124 */       state.lightCoords = ((Int2IntFunction)combineResult.apply(new BrightnessCombiner())).get(state.lightCoords);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(BedRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 130 */     Material material = Sheets.getBedMaterial(state.color);
/* 131 */     submitPiece(poseStack, submitNodeCollector, state.isHead ? this.headModel : this.footModel, state.facing, material, state.lightCoords, OverlayTexture.NO_OVERLAY, false, state.breakProgress, 0);
/*     */   }
/*     */   
/*     */   public void submitSpecial(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Material material, int outlineColor) {
/* 135 */     submitPiece(poseStack, submitNodeCollector, this.headModel, Direction.SOUTH, material, lightCoords, overlayCoords, false, null, outlineColor);
/* 136 */     submitPiece(poseStack, submitNodeCollector, this.footModel, Direction.SOUTH, material, lightCoords, overlayCoords, true, null, outlineColor);
/*     */   }
/*     */   
/*     */   private void submitPiece(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Model.Simple model, Direction direction, Material material, int lightCoords, int overlayCoords, boolean translateZ, ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor) {
/* 140 */     poseStack.pushPose();
/* 141 */     preparePose(poseStack, translateZ, direction);
/* 142 */     submitNodeCollector.submitModel((Model)model, Unit.INSTANCE, poseStack, material.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, this.materials.get(material), outlineColor, breakProgress);
/* 143 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private static void preparePose(PoseStack poseStack, boolean translateZ, Direction direction) {
/* 147 */     poseStack.translate(0.0F, 0.5625F, translateZ ? -1.0F : 0.0F);
/* 148 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0F));
/* 149 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 150 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F + direction.toYRot()));
/* 151 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/*     */   }
/*     */   
/*     */   public void getExtents(Consumer<Vector3fc> output) {
/* 155 */     PoseStack poseStack = new PoseStack();
/* 156 */     preparePose(poseStack, false, Direction.SOUTH);
/* 157 */     this.headModel.root().getExtentsForGui(poseStack, output);
/* 158 */     poseStack.setIdentity();
/* 159 */     preparePose(poseStack, true, Direction.SOUTH);
/* 160 */     this.footModel.root().getExtentsForGui(poseStack, output);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BedRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */