/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ShulkerBoxRenderer
/*     */   implements BlockEntityRenderer<ShulkerBoxBlockEntity, ShulkerBoxRenderState> {
/*     */   private final MaterialSet materials;
/*     */   private final ShulkerBoxModel model;
/*     */   
/*     */   public ShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
/*  37 */     this(context.entityModelSet(), context.materials());
/*     */   }
/*     */   
/*     */   public ShulkerBoxRenderer(SpecialModelRenderer.BakingContext context) {
/*  41 */     this(context.entityModelSet(), context.materials());
/*     */   }
/*     */   
/*     */   public ShulkerBoxRenderer(EntityModelSet context, MaterialSet materials) {
/*  45 */     this.materials = materials;
/*  46 */     this.model = new ShulkerBoxModel(context.bakeLayer(ModelLayers.SHULKER_BOX));
/*     */   }
/*     */ 
/*     */   
/*     */   public ShulkerBoxRenderState createRenderState() {
/*  51 */     return new ShulkerBoxRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(ShulkerBoxBlockEntity blockEntity, ShulkerBoxRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  56 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  57 */     state.direction = (Direction)blockEntity.getBlockState().getValueOrElse((Property)ShulkerBoxBlock.FACING, (Comparable)Direction.UP);
/*  58 */     state.color = blockEntity.getColor();
/*  59 */     state.progress = blockEntity.getProgress(partialTicks);
/*     */   }
/*     */   
/*     */   public void submit(ShulkerBoxRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*     */     Material material;
/*  64 */     DyeColor color = state.color;
/*     */     
/*  66 */     if (color == null) {
/*  67 */       material = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
/*     */     } else {
/*  69 */       material = Sheets.getShulkerBoxMaterial(color);
/*     */     } 
/*  71 */     submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.direction, state.progress, state.breakProgress, material, 0);
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Direction direction, float progress, ModelFeatureRenderer.CrumblingOverlay breakProgress, Material material, int outlineColor) {
/*  75 */     poseStack.pushPose();
/*  76 */     prepareModel(poseStack, direction, progress);
/*  77 */     Objects.requireNonNull(this.model); submitNodeCollector.submitModel(this.model, progress, poseStack, material.renderType(this.model::renderType), lightCoords, overlayCoords, -1, this.materials.get(material), outlineColor, breakProgress);
/*  78 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void prepareModel(PoseStack poseStack, Direction direction, float progress) {
/*  82 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/*  83 */     float scale = 0.9995F;
/*  84 */     poseStack.scale(0.9995F, 0.9995F, 0.9995F);
/*     */     
/*  86 */     poseStack.mulPose((Quaternionfc)direction.getRotation());
/*     */     
/*  88 */     poseStack.scale(1.0F, -1.0F, -1.0F);
/*  89 */     poseStack.translate(0.0F, -1.0F, 0.0F);
/*     */     
/*  91 */     this.model.setupAnim(progress);
/*     */   }
/*     */   
/*     */   public void getExtents(Direction direction, float progress, Consumer<Vector3fc> output) {
/*  95 */     PoseStack poseStack = new PoseStack();
/*  96 */     prepareModel(poseStack, direction, progress);
/*  97 */     this.model.root().getExtentsForGui(poseStack, output);
/*     */   }
/*     */   
/*     */   private static class ShulkerBoxModel extends Model<Float> {
/*     */     private final ModelPart lid;
/*     */     
/*     */     public ShulkerBoxModel(ModelPart root) {
/* 104 */       super(root, RenderTypes::entityCutoutNoCull);
/* 105 */       this.lid = root.getChild("lid");
/*     */     }
/*     */ 
/*     */     
/*     */     public void setupAnim(Float progress) {
/* 110 */       super.setupAnim(progress);
/* 111 */       this.lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
/* 112 */       this.lid.yRot = 270.0F * progress * 0.017453292F;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/ShulkerBoxRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */