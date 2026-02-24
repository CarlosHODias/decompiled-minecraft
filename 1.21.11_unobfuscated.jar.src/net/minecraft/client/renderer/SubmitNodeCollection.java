/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.ModelPartFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public class SubmitNodeCollection
/*     */   implements OrderedSubmitNodeCollector {
/*  33 */   private final List<SubmitNodeStorage.ShadowSubmit> shadowSubmits = new ArrayList<>();
/*  34 */   private final List<SubmitNodeStorage.FlameSubmit> flameSubmits = new ArrayList<>();
/*  35 */   private final NameTagFeatureRenderer.Storage nameTagSubmits = new NameTagFeatureRenderer.Storage();
/*  36 */   private final List<SubmitNodeStorage.TextSubmit> textSubmits = new ArrayList<>();
/*  37 */   private final List<SubmitNodeStorage.LeashSubmit> leashSubmits = new ArrayList<>();
/*  38 */   private final List<SubmitNodeStorage.BlockSubmit> blockSubmits = new ArrayList<>();
/*  39 */   private final List<SubmitNodeStorage.MovingBlockSubmit> movingBlockSubmits = new ArrayList<>();
/*  40 */   private final List<SubmitNodeStorage.BlockModelSubmit> blockModelSubmits = new ArrayList<>();
/*  41 */   private final List<SubmitNodeStorage.ItemSubmit> itemSubmits = new ArrayList<>();
/*  42 */   private final List<SubmitNodeCollector.ParticleGroupRenderer> particleGroupRenderers = new ArrayList<>();
/*  43 */   private final ModelFeatureRenderer.Storage modelSubmits = new ModelFeatureRenderer.Storage();
/*  44 */   private final ModelPartFeatureRenderer.Storage modelPartSubmits = new ModelPartFeatureRenderer.Storage();
/*  45 */   private final CustomFeatureRenderer.Storage customGeometrySubmits = new CustomFeatureRenderer.Storage();
/*     */   private final SubmitNodeStorage submitNodeStorage;
/*     */   private boolean wasUsed = false;
/*     */   
/*     */   public SubmitNodeCollection(SubmitNodeStorage submitNodeStorage) {
/*  50 */     this.submitNodeStorage = submitNodeStorage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
/*  55 */     this.wasUsed = true;
/*  56 */     PoseStack.Pose pose = poseStack.last();
/*  57 */     this.shadowSubmits.add(new SubmitNodeStorage.ShadowSubmit(new Matrix4f((Matrix4fc)pose.pose()), radius, pieces));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitNameTag(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, double distanceToCameraSq, CameraRenderState camera) {
/*  62 */     this.wasUsed = true;
/*  63 */     this.nameTagSubmits.add(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, distanceToCameraSq, camera);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
/*  68 */     this.wasUsed = true;
/*  69 */     this.textSubmits.add(new SubmitNodeStorage.TextSubmit(new Matrix4f((Matrix4fc)poseStack.last().pose()), x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
/*  74 */     this.wasUsed = true;
/*  75 */     this.flameSubmits.add(new SubmitNodeStorage.FlameSubmit(poseStack.last().copy(), renderState, rotation));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
/*  80 */     this.wasUsed = true;
/*  81 */     this.leashSubmits.add(new SubmitNodeStorage.LeashSubmit(new Matrix4f((Matrix4fc)poseStack.last().pose()), leashState));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/*  86 */     this.wasUsed = true;
/*  87 */     SubmitNodeStorage.ModelSubmit<S> modelSubmit = new SubmitNodeStorage.ModelSubmit<>(poseStack.last().copy(), model, state, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay);
/*  88 */     this.modelSubmits.add(renderType, modelSubmit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, boolean sheeted, boolean hasFoil, int tintedColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int outlineColor) {
/*  93 */     this.wasUsed = true;
/*  94 */     this.modelPartSubmits.add(renderType, new SubmitNodeStorage.ModelPartSubmit(poseStack.last().copy(), modelPart, lightCoords, overlayCoords, sprite, sheeted, hasFoil, tintedColor, crumblingOverlay, outlineColor));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitBlock(PoseStack poseStack, BlockState state, int lightCoords, int overlayCoords, int outlineColor) {
/*  99 */     this.wasUsed = true;
/* 100 */     this.blockSubmits.add(new SubmitNodeStorage.BlockSubmit(poseStack.last().copy(), state, lightCoords, overlayCoords, outlineColor));
/* 101 */     Minecraft.getInstance().getModelManager().specialBlockModelRenderer().renderByBlock(state.getBlock(), ItemDisplayContext.NONE, poseStack, this.submitNodeStorage, lightCoords, overlayCoords, outlineColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
/* 106 */     this.wasUsed = true;
/* 107 */     this.movingBlockSubmits.add(new SubmitNodeStorage.MovingBlockSubmit(new Matrix4f((Matrix4fc)poseStack.last().pose()), movingBlockRenderState));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitBlockModel(PoseStack poseStack, RenderType renderType, BlockStateModel model, float r, float g, float b, int lightCoords, int overlayCoords, int outlineColor) {
/* 112 */     this.wasUsed = true;
/* 113 */     this.blockModelSubmits.add(new SubmitNodeStorage.BlockModelSubmit(poseStack.last().copy(), renderType, model, r, g, b, lightCoords, overlayCoords, outlineColor));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) {
/* 118 */     this.wasUsed = true;
/* 119 */     this.itemSubmits.add(new SubmitNodeStorage.ItemSubmit(poseStack.last().copy(), displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, renderType, foilType));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
/* 124 */     this.wasUsed = true;
/* 125 */     this.customGeometrySubmits.add(poseStack, renderType, customGeometryRenderer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitParticleGroup(SubmitNodeCollector.ParticleGroupRenderer particleGroupRenderer) {
/* 130 */     this.wasUsed = true;
/* 131 */     this.particleGroupRenderers.add(particleGroupRenderer);
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.ShadowSubmit> getShadowSubmits() {
/* 135 */     return this.shadowSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.FlameSubmit> getFlameSubmits() {
/* 139 */     return this.flameSubmits;
/*     */   }
/*     */   
/*     */   public NameTagFeatureRenderer.Storage getNameTagSubmits() {
/* 143 */     return this.nameTagSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.TextSubmit> getTextSubmits() {
/* 147 */     return this.textSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.LeashSubmit> getLeashSubmits() {
/* 151 */     return this.leashSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.BlockSubmit> getBlockSubmits() {
/* 155 */     return this.blockSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.MovingBlockSubmit> getMovingBlockSubmits() {
/* 159 */     return this.movingBlockSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.BlockModelSubmit> getBlockModelSubmits() {
/* 163 */     return this.blockModelSubmits;
/*     */   }
/*     */   
/*     */   public ModelPartFeatureRenderer.Storage getModelPartSubmits() {
/* 167 */     return this.modelPartSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeStorage.ItemSubmit> getItemSubmits() {
/* 171 */     return this.itemSubmits;
/*     */   }
/*     */   
/*     */   public List<SubmitNodeCollector.ParticleGroupRenderer> getParticleGroupRenderers() {
/* 175 */     return this.particleGroupRenderers;
/*     */   }
/*     */   
/*     */   public ModelFeatureRenderer.Storage getModelSubmits() {
/* 179 */     return this.modelSubmits;
/*     */   }
/*     */   
/*     */   public CustomFeatureRenderer.Storage getCustomGeometrySubmits() {
/* 183 */     return this.customGeometrySubmits;
/*     */   }
/*     */   
/*     */   public boolean wasUsed() {
/* 187 */     return this.wasUsed;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 191 */     this.shadowSubmits.clear();
/* 192 */     this.flameSubmits.clear();
/* 193 */     this.nameTagSubmits.clear();
/* 194 */     this.textSubmits.clear();
/* 195 */     this.leashSubmits.clear();
/* 196 */     this.blockSubmits.clear();
/* 197 */     this.movingBlockSubmits.clear();
/* 198 */     this.blockModelSubmits.clear();
/* 199 */     this.itemSubmits.clear();
/* 200 */     this.particleGroupRenderers.clear();
/* 201 */     this.modelSubmits.clear();
/* 202 */     this.customGeometrySubmits.clear();
/* 203 */     this.modelPartSubmits.clear();
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 207 */     this.modelSubmits.endFrame();
/* 208 */     this.modelPartSubmits.endFrame();
/* 209 */     this.customGeometrySubmits.endFrame();
/*     */     
/* 211 */     this.wasUsed = false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SubmitNodeCollection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */