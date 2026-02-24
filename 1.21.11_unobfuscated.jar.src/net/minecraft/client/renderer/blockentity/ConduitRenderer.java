/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.MaterialMapper;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.CondiutRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ConduitBlockEntity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class ConduitRenderer implements BlockEntityRenderer<ConduitBlockEntity, CondiutRenderState> {
/*  33 */   public static final MaterialMapper MAPPER = new MaterialMapper(TextureAtlas.LOCATION_BLOCKS, "entity/conduit");
/*     */   
/*  35 */   public static final Material SHELL_TEXTURE = MAPPER.defaultNamespaceApply("base");
/*  36 */   public static final Material ACTIVE_SHELL_TEXTURE = MAPPER.defaultNamespaceApply("cage");
/*  37 */   public static final Material WIND_TEXTURE = MAPPER.defaultNamespaceApply("wind");
/*  38 */   public static final Material VERTICAL_WIND_TEXTURE = MAPPER.defaultNamespaceApply("wind_vertical");
/*  39 */   public static final Material OPEN_EYE_TEXTURE = MAPPER.defaultNamespaceApply("open_eye");
/*  40 */   public static final Material CLOSED_EYE_TEXTURE = MAPPER.defaultNamespaceApply("closed_eye");
/*     */   
/*     */   private final MaterialSet materials;
/*     */   
/*     */   private final ModelPart eye;
/*     */   private final ModelPart wind;
/*     */   private final ModelPart shell;
/*     */   private final ModelPart cage;
/*     */   
/*     */   public ConduitRenderer(BlockEntityRendererProvider.Context context) {
/*  50 */     this.materials = context.materials();
/*  51 */     this.eye = context.bakeLayer(ModelLayers.CONDUIT_EYE);
/*  52 */     this.wind = context.bakeLayer(ModelLayers.CONDUIT_WIND);
/*  53 */     this.shell = context.bakeLayer(ModelLayers.CONDUIT_SHELL);
/*  54 */     this.cage = context.bakeLayer(ModelLayers.CONDUIT_CAGE);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createEyeLayer() {
/*  58 */     MeshDefinition mesh = new MeshDefinition();
/*  59 */     PartDefinition root = mesh.getRoot();
/*  60 */     root.addOrReplaceChild("eye", 
/*  61 */         CubeListBuilder.create()
/*  62 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
/*     */ 
/*     */     
/*  65 */     return LayerDefinition.create(mesh, 16, 16);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createWindLayer() {
/*  69 */     MeshDefinition mesh = new MeshDefinition();
/*  70 */     PartDefinition root = mesh.getRoot();
/*  71 */     root.addOrReplaceChild("wind", 
/*  72 */         CubeListBuilder.create()
/*  73 */         .texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  76 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createShellLayer() {
/*  80 */     MeshDefinition mesh = new MeshDefinition();
/*  81 */     PartDefinition root = mesh.getRoot();
/*  82 */     root.addOrReplaceChild("shell", 
/*  83 */         CubeListBuilder.create()
/*  84 */         .texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  87 */     return LayerDefinition.create(mesh, 32, 16);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createCageLayer() {
/*  91 */     MeshDefinition mesh = new MeshDefinition();
/*  92 */     PartDefinition root = mesh.getRoot();
/*  93 */     root.addOrReplaceChild("shell", 
/*  94 */         CubeListBuilder.create()
/*  95 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/*     */ 
/*     */     
/*  98 */     return LayerDefinition.create(mesh, 32, 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public CondiutRenderState createRenderState() {
/* 103 */     return new CondiutRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(ConduitBlockEntity blockEntity, CondiutRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 108 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 109 */     state.isActive = blockEntity.isActive();
/* 110 */     state.activeRotation = blockEntity.getActiveRotation(blockEntity.isActive() ? partialTicks : 0.0F);
/* 111 */     state.animTime = blockEntity.tickCount + partialTicks;
/* 112 */     state.animationPhase = blockEntity.tickCount / 66 % 3;
/* 113 */     state.isHunting = blockEntity.isHunting();
/*     */   }
/*     */ 
/*     */   
/*     */   public void submit(CondiutRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 118 */     if (!state.isActive) {
/* 119 */       poseStack.pushPose();
/* 120 */       poseStack.translate(0.5F, 0.5F, 0.5F);
/* 121 */       poseStack.mulPose((Quaternionfc)new Quaternionf().rotationY(state.activeRotation * 0.017453292F));
/* 122 */       submitNodeCollector.submitModelPart(this.shell, poseStack, SHELL_TEXTURE.renderType(RenderTypes::entitySolid), state.lightCoords, OverlayTexture.NO_OVERLAY, this.materials.get(SHELL_TEXTURE), -1, state.breakProgress);
/* 123 */       poseStack.popPose();
/*     */       
/*     */       return;
/*     */     } 
/* 127 */     float rotation = state.activeRotation * 57.295776F;
/* 128 */     float hh = Mth.sin((state.animTime * 0.1F)) / 2.0F + 0.5F;
/* 129 */     hh = hh * hh + hh;
/*     */     
/* 131 */     poseStack.pushPose();
/* 132 */     poseStack.translate(0.5F, 0.3F + hh * 0.2F, 0.5F);
/* 133 */     Vector3f axis = new Vector3f(0.5F, 1.0F, 0.5F).normalize();
/* 134 */     poseStack.mulPose((Quaternionfc)new Quaternionf().rotationAxis(rotation * 0.017453292F, (org.joml.Vector3fc)axis));
/* 135 */     submitNodeCollector.submitModelPart(this.cage, poseStack, ACTIVE_SHELL_TEXTURE.renderType(RenderTypes::entityCutoutNoCull), state.lightCoords, OverlayTexture.NO_OVERLAY, this.materials.get(ACTIVE_SHELL_TEXTURE), -1, state.breakProgress);
/* 136 */     poseStack.popPose();
/*     */     
/* 138 */     poseStack.pushPose();
/* 139 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/*     */     
/* 141 */     if (state.animationPhase == 1) {
/* 142 */       poseStack.mulPose((Quaternionfc)new Quaternionf().rotationX(1.5707964F));
/* 143 */     } else if (state.animationPhase == 2) {
/* 144 */       poseStack.mulPose((Quaternionfc)new Quaternionf().rotationZ(1.5707964F));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Material windMaterial = (state.animationPhase == 1) ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE;
/* 150 */     RenderType windRenderType = windMaterial.renderType(RenderTypes::entityCutoutNoCull);
/* 151 */     TextureAtlasSprite windSprite = this.materials.get(windMaterial);
/* 152 */     submitNodeCollector.submitModelPart(this.wind, poseStack, windRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, windSprite);
/* 153 */     poseStack.popPose();
/* 154 */     poseStack.pushPose();
/* 155 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 156 */     poseStack.scale(0.875F, 0.875F, 0.875F);
/* 157 */     poseStack.mulPose((Quaternionfc)new Quaternionf().rotationXYZ(3.1415927F, 0.0F, 3.1415927F));
/* 158 */     submitNodeCollector.submitModelPart(this.wind, poseStack, windRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, windSprite);
/* 159 */     poseStack.popPose();
/*     */     
/* 161 */     poseStack.pushPose();
/* 162 */     poseStack.translate(0.5F, 0.3F + hh * 0.2F, 0.5F);
/* 163 */     poseStack.scale(0.5F, 0.5F, 0.5F);
/* 164 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/* 165 */     poseStack.mulPose((Quaternionfc)new Quaternionf().rotationZ(3.1415927F).rotateY(3.1415927F));
/* 166 */     float scale = 1.3333334F;
/* 167 */     poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
/* 168 */     Material eyeMaterial = state.isHunting ? OPEN_EYE_TEXTURE : CLOSED_EYE_TEXTURE;
/* 169 */     submitNodeCollector.submitModelPart(this.eye, poseStack, eyeMaterial.renderType(RenderTypes::entityCutoutNoCull), state.lightCoords, OverlayTexture.NO_OVERLAY, this.materials.get(eyeMaterial));
/* 170 */     poseStack.popPose();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/ConduitRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */