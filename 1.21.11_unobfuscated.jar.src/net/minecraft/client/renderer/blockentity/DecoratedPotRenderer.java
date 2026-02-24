/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class DecoratedPotRenderer
/*     */   implements BlockEntityRenderer<DecoratedPotBlockEntity, DecoratedPotRenderState>
/*     */ {
/*     */   private final MaterialSet materials;
/*     */   private static final String NECK = "neck";
/*     */   private static final String FRONT = "front";
/*     */   private static final String BACK = "back";
/*     */   private static final String LEFT = "left";
/*     */   private static final String RIGHT = "right";
/*     */   private static final String TOP = "top";
/*     */   private static final String BOTTOM = "bottom";
/*     */   private final ModelPart neck;
/*     */   private final ModelPart frontSide;
/*     */   private final ModelPart backSide;
/*     */   private final ModelPart leftSide;
/*     */   private final ModelPart rightSide;
/*     */   private final ModelPart top;
/*     */   private final ModelPart bottom;
/*     */   private static final float WOBBLE_AMPLITUDE = 0.125F;
/*     */   
/*     */   public DecoratedPotRenderer(BlockEntityRendererProvider.Context context) {
/*  62 */     this(context.entityModelSet(), context.materials());
/*     */   }
/*     */   
/*     */   public DecoratedPotRenderer(SpecialModelRenderer.BakingContext context) {
/*  66 */     this(context.entityModelSet(), context.materials());
/*     */   }
/*     */   
/*     */   public DecoratedPotRenderer(EntityModelSet entityModelSet, MaterialSet materials) {
/*  70 */     this.materials = materials;
/*  71 */     ModelPart baseRoot = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_BASE);
/*  72 */     this.neck = baseRoot.getChild("neck");
/*  73 */     this.top = baseRoot.getChild("top");
/*  74 */     this.bottom = baseRoot.getChild("bottom");
/*     */     
/*  76 */     ModelPart sidesRoot = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
/*  77 */     this.frontSide = sidesRoot.getChild("front");
/*  78 */     this.backSide = sidesRoot.getChild("back");
/*  79 */     this.leftSide = sidesRoot.getChild("left");
/*  80 */     this.rightSide = sidesRoot.getChild("right");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBaseLayer() {
/*  84 */     MeshDefinition mesh = new MeshDefinition();
/*  85 */     PartDefinition root = mesh.getRoot();
/*     */     
/*  87 */     CubeDeformation inflate = new CubeDeformation(0.2F);
/*  88 */     CubeDeformation deflate = new CubeDeformation(-0.1F);
/*     */     
/*  90 */     root.addOrReplaceChild("neck", 
/*  91 */         CubeListBuilder.create()
/*  92 */         .texOffs(0, 0).addBox(4.0F, 17.0F, 4.0F, 8.0F, 3.0F, 8.0F, deflate)
/*  93 */         .texOffs(0, 5).addBox(5.0F, 20.0F, 5.0F, 6.0F, 1.0F, 6.0F, inflate), 
/*  94 */         PartPose.offsetAndRotation(0.0F, 37.0F, 16.0F, 3.1415927F, 0.0F, 0.0F));
/*     */ 
/*     */     
/*  97 */     CubeListBuilder topBottomPlane = CubeListBuilder.create().texOffs(-14, 13).addBox(0.0F, 0.0F, 0.0F, 14.0F, 0.0F, 14.0F);
/*  98 */     root.addOrReplaceChild("top", topBottomPlane, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, 0.0F, 0.0F));
/*  99 */     root.addOrReplaceChild("bottom", topBottomPlane, PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
/*     */     
/* 101 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createSidesLayer() {
/* 105 */     MeshDefinition mesh = new MeshDefinition();
/* 106 */     PartDefinition root = mesh.getRoot();
/*     */     
/* 108 */     CubeListBuilder sidePlane = CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH));
/*     */     
/* 110 */     root.addOrReplaceChild("back", sidePlane, PartPose.offsetAndRotation(15.0F, 16.0F, 1.0F, 0.0F, 0.0F, 3.1415927F));
/* 111 */     root.addOrReplaceChild("left", sidePlane, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, -1.5707964F, 3.1415927F));
/* 112 */     root.addOrReplaceChild("right", sidePlane, PartPose.offsetAndRotation(15.0F, 16.0F, 15.0F, 0.0F, 1.5707964F, 3.1415927F));
/* 113 */     root.addOrReplaceChild("front", sidePlane, PartPose.offsetAndRotation(1.0F, 16.0F, 15.0F, 3.1415927F, 0.0F, 0.0F));
/*     */     
/* 115 */     return LayerDefinition.create(mesh, 16, 16);
/*     */   }
/*     */   
/*     */   private static Material getSideMaterial(Optional<Item> item) {
/* 119 */     if (item.isPresent()) {
/* 120 */       Material result = Sheets.getDecoratedPotMaterial(DecoratedPotPatterns.getPatternFromItem(item.get()));
/* 121 */       if (result != null) {
/* 122 */         return result;
/*     */       }
/*     */     } 
/* 125 */     return Sheets.DECORATED_POT_SIDE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecoratedPotRenderState createRenderState() {
/* 132 */     return new DecoratedPotRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(DecoratedPotBlockEntity blockEntity, DecoratedPotRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 137 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 138 */     state.decorations = blockEntity.getDecorations();
/* 139 */     state.direction = blockEntity.getDirection();
/*     */     
/* 141 */     DecoratedPotBlockEntity.WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
/* 142 */     if (wobbleStyle != null && blockEntity.getLevel() != null) {
/* 143 */       state.wobbleProgress = ((float)(blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / wobbleStyle.duration;
/*     */     } else {
/* 145 */       state.wobbleProgress = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(DecoratedPotRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 151 */     poseStack.pushPose();
/*     */     
/* 153 */     Direction entityDirection = state.direction;
/*     */     
/* 155 */     poseStack.translate(0.5D, 0.0D, 0.5D);
/* 156 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - entityDirection.toYRot()));
/* 157 */     poseStack.translate(-0.5D, 0.0D, -0.5D);
/*     */     
/* 159 */     if (state.wobbleProgress >= 0.0F && state.wobbleProgress <= 1.0F)
/*     */     {
/*     */       
/* 162 */       if (state.wobbleStyle == DecoratedPotBlockEntity.WobbleStyle.POSITIVE) {
/*     */ 
/*     */         
/* 165 */         float amplitude = 0.015625F;
/* 166 */         float deltaTime = state.wobbleProgress * 6.2831855F;
/*     */         
/* 168 */         float tiltX = -1.5F * (Mth.cos(deltaTime) + 0.5F) * Mth.sin((deltaTime / 2.0F));
/* 169 */         poseStack.rotateAround((Quaternionfc)Axis.XP.rotation(tiltX * 0.015625F), 0.5F, 0.0F, 0.5F);
/*     */         
/* 171 */         float tiltZ = Mth.sin(deltaTime);
/* 172 */         poseStack.rotateAround((Quaternionfc)Axis.ZP.rotation(tiltZ * 0.015625F), 0.5F, 0.0F, 0.5F);
/*     */       }
/*     */       else {
/*     */         
/* 176 */         float turnAngle = Mth.sin((-state.wobbleProgress * 3.0F * 3.1415927F)) * 0.125F;
/* 177 */         float linearDecayFactor = 1.0F - state.wobbleProgress;
/* 178 */         poseStack.rotateAround((Quaternionfc)Axis.YP.rotation(turnAngle * linearDecayFactor), 0.5F, 0.0F, 0.5F);
/*     */       } 
/*     */     }
/*     */     
/* 182 */     submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.decorations, 0);
/* 183 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, PotDecorations decorations, int outlineColor) {
/* 187 */     RenderType renderType = Sheets.DECORATED_POT_BASE.renderType(RenderTypes::entitySolid);
/* 188 */     TextureAtlasSprite sprite = this.materials.get(Sheets.DECORATED_POT_BASE);
/*     */     
/* 190 */     submitNodeCollector.submitModelPart(this.neck, poseStack, renderType, lightCoords, overlayCoords, sprite, false, false, -1, null, outlineColor);
/* 191 */     submitNodeCollector.submitModelPart(this.top, poseStack, renderType, lightCoords, overlayCoords, sprite, false, false, -1, null, outlineColor);
/* 192 */     submitNodeCollector.submitModelPart(this.bottom, poseStack, renderType, lightCoords, overlayCoords, sprite, false, false, -1, null, outlineColor);
/*     */     
/* 194 */     Material frontMaterial = getSideMaterial(decorations.front());
/* 195 */     submitNodeCollector.submitModelPart(this.frontSide, poseStack, frontMaterial.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.materials.get(frontMaterial), false, false, -1, null, outlineColor);
/* 196 */     Material backMaterial = getSideMaterial(decorations.back());
/* 197 */     submitNodeCollector.submitModelPart(this.backSide, poseStack, backMaterial.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.materials.get(backMaterial), false, false, -1, null, outlineColor);
/* 198 */     Material leftMaterial = getSideMaterial(decorations.left());
/* 199 */     submitNodeCollector.submitModelPart(this.leftSide, poseStack, leftMaterial.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.materials.get(leftMaterial), false, false, -1, null, outlineColor);
/* 200 */     Material rightMaterial = getSideMaterial(decorations.right());
/* 201 */     submitNodeCollector.submitModelPart(this.rightSide, poseStack, rightMaterial.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.materials.get(rightMaterial), false, false, -1, null, outlineColor);
/*     */   }
/*     */   
/*     */   public void getExtents(Consumer<Vector3fc> output) {
/* 205 */     PoseStack poseStack = new PoseStack();
/* 206 */     this.neck.getExtentsForGui(poseStack, output);
/* 207 */     this.top.getExtentsForGui(poseStack, output);
/* 208 */     this.bottom.getExtentsForGui(poseStack, output);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/DecoratedPotRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */