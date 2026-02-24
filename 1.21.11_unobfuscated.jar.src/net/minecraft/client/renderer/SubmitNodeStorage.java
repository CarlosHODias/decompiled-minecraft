/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
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
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class SubmitNodeStorage
/*     */   implements SubmitNodeCollector
/*     */ {
/*  30 */   private final Int2ObjectAVLTreeMap<SubmitNodeCollection> submitsPerOrder = new Int2ObjectAVLTreeMap();
/*     */ 
/*     */   
/*     */   public SubmitNodeCollection order(int order) {
/*  34 */     return (SubmitNodeCollection)this.submitsPerOrder.computeIfAbsent(order, ignored -> new SubmitNodeCollection(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
/*  39 */     order(0).submitShadow(poseStack, radius, pieces);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitNameTag(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, double distanceToCameraSq, CameraRenderState camera) {
/*  44 */     order(0).submitNameTag(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, distanceToCameraSq, camera);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
/*  49 */     order(0).submitText(poseStack, x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
/*  54 */     order(0).submitFlame(poseStack, renderState, rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
/*  59 */     order(0).submitLeash(poseStack, leashState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/*  64 */     order(0).submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, boolean sheeted, boolean hasFoil, int tintedColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int outlineColor) {
/*  69 */     order(0).submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, sheeted, hasFoil, tintedColor, crumblingOverlay, outlineColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitBlock(PoseStack poseStack, BlockState state, int lightCoords, int overlayCoords, int outlineColor) {
/*  74 */     order(0).submitBlock(poseStack, state, lightCoords, overlayCoords, outlineColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState) {
/*  79 */     order(0).submitMovingBlock(poseStack, movingBlockRenderState);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitBlockModel(PoseStack poseStack, RenderType renderType, BlockStateModel model, float r, float g, float b, int lightCoords, int overlayCoords, int outlineColor) {
/*  84 */     order(0).submitBlockModel(poseStack, renderType, model, r, g, b, lightCoords, overlayCoords, outlineColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) {
/*  89 */     order(0).submitItem(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, renderType, foilType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
/*  94 */     order(0).submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitParticleGroup(SubmitNodeCollector.ParticleGroupRenderer particleGroupRenderer) {
/*  99 */     order(0).submitParticleGroup(particleGroupRenderer);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 103 */     this.submitsPerOrder.values().forEach(SubmitNodeCollection::clear);
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 107 */     this.submitsPerOrder.values().removeIf(collection -> !collection.wasUsed());
/* 108 */     this.submitsPerOrder.values().forEach(SubmitNodeCollection::endFrame);
/*     */   }
/*     */   
/*     */   public Int2ObjectAVLTreeMap<SubmitNodeCollection> getSubmitsPerOrder() {
/* 112 */     return this.submitsPerOrder;
/*     */   }
/*     */   public static final class ShadowSubmit extends Record { private final Matrix4f pose; private final float radius; private final List<EntityRenderState.ShadowPiece> pieces;
/* 115 */     public ShadowSubmit(Matrix4f pose, float radius, List<EntityRenderState.ShadowPiece> pieces) { this.pose = pose; this.radius = radius; this.pieces = pieces; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ShadowSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 115 */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ShadowSubmit; } public Matrix4f pose() { return this.pose; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ShadowSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ShadowSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ShadowSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ShadowSubmit;
/* 115 */       //   0	8	1	o	Ljava/lang/Object; } public float radius() { return this.radius; } public List<EntityRenderState.ShadowPiece> pieces() { return this.pieces; }
/*     */      }
/*     */   public static final class FlameSubmit extends Record { private final PoseStack.Pose pose; private final EntityRenderState entityRenderState; private final Quaternionf rotation;
/* 118 */     public FlameSubmit(PoseStack.Pose pose, EntityRenderState entityRenderState, Quaternionf rotation) { this.pose = pose; this.entityRenderState = entityRenderState; this.rotation = rotation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$FlameSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$FlameSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$FlameSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$FlameSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$FlameSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #118	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$FlameSubmit;
/* 118 */       //   0	8	1	o	Ljava/lang/Object; } public PoseStack.Pose pose() { return this.pose; } public EntityRenderState entityRenderState() { return this.entityRenderState; } public Quaternionf rotation() { return this.rotation; }
/*     */      }
/*     */   public static final class NameTagSubmit extends Record { private final Matrix4f pose; private final float x; private final float y; private final Component text; private final int lightCoords; private final int color; private final int backgroundColor; private final double distanceToCameraSq;
/* 121 */     public NameTagSubmit(Matrix4f pose, float x, float y, Component text, int lightCoords, int color, int backgroundColor, double distanceToCameraSq) { this.pose = pose; this.x = x; this.y = y; this.text = text; this.lightCoords = lightCoords; this.color = color; this.backgroundColor = backgroundColor; this.distanceToCameraSq = distanceToCameraSq; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$NameTagSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$NameTagSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$NameTagSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$NameTagSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$NameTagSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #121	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$NameTagSubmit;
/* 121 */       //   0	8	1	o	Ljava/lang/Object; } public Matrix4f pose() { return this.pose; } public float x() { return this.x; } public float y() { return this.y; } public Component text() { return this.text; } public int lightCoords() { return this.lightCoords; } public int color() { return this.color; } public int backgroundColor() { return this.backgroundColor; } public double distanceToCameraSq() { return this.distanceToCameraSq; }
/*     */      }
/*     */   public static final class TextSubmit extends Record { private final Matrix4f pose; private final float x; private final float y; private final FormattedCharSequence string; private final boolean dropShadow; private final Font.DisplayMode displayMode; private final int lightCoords; private final int color; private final int backgroundColor; private final int outlineColor;
/* 124 */     public TextSubmit(Matrix4f pose, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) { this.pose = pose; this.x = x; this.y = y; this.string = string; this.dropShadow = dropShadow; this.displayMode = displayMode; this.lightCoords = lightCoords; this.color = color; this.backgroundColor = backgroundColor; this.outlineColor = outlineColor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$TextSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #124	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TextSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$TextSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #124	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TextSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$TextSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #124	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TextSubmit;
/* 124 */       //   0	8	1	o	Ljava/lang/Object; } public Matrix4f pose() { return this.pose; } public float x() { return this.x; } public float y() { return this.y; } public FormattedCharSequence string() { return this.string; } public boolean dropShadow() { return this.dropShadow; } public Font.DisplayMode displayMode() { return this.displayMode; } public int lightCoords() { return this.lightCoords; } public int color() { return this.color; } public int backgroundColor() { return this.backgroundColor; } public int outlineColor() { return this.outlineColor; }
/*     */      }
/*     */   public static final class LeashSubmit extends Record { private final Matrix4f pose; private final EntityRenderState.LeashState leashState;
/* 127 */     public LeashSubmit(Matrix4f pose, EntityRenderState.LeashState leashState) { this.pose = pose; this.leashState = leashState; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$LeashSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$LeashSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$LeashSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$LeashSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$LeashSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$LeashSubmit;
/* 127 */       //   0	8	1	o	Ljava/lang/Object; } public Matrix4f pose() { return this.pose; } public EntityRenderState.LeashState leashState() { return this.leashState; }
/*     */      }
/*     */   public static final class ModelSubmit<S> extends Record { private final PoseStack.Pose pose; private final Model<? super S> model; private final S state; private final int lightCoords; private final int overlayCoords; private final int tintedColor; private final TextureAtlasSprite sprite; private final int outlineColor; private final ModelFeatureRenderer.CrumblingOverlay crumblingOverlay;
/* 130 */     public ModelSubmit(PoseStack.Pose pose, Model<? super S> model, S state, int lightCoords, int overlayCoords, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) { this.pose = pose; this.model = model; this.state = state; this.lightCoords = lightCoords; this.overlayCoords = overlayCoords; this.tintedColor = tintedColor; this.sprite = sprite; this.outlineColor = outlineColor; this.crumblingOverlay = crumblingOverlay; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit<TS;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit<TS;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 130 */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit<TS;>; } public PoseStack.Pose pose() { return this.pose; } public Model<? super S> model() { return this.model; } public S state() { return this.state; } public int lightCoords() { return this.lightCoords; } public int overlayCoords() { return this.overlayCoords; } public int tintedColor() { return this.tintedColor; } public TextureAtlasSprite sprite() { return this.sprite; } public int outlineColor() { return this.outlineColor; } public ModelFeatureRenderer.CrumblingOverlay crumblingOverlay() { return this.crumblingOverlay; }
/*     */      }
/*     */   public static final class ModelPartSubmit extends Record { private final PoseStack.Pose pose; private final ModelPart modelPart; private final int lightCoords; private final int overlayCoords; private final TextureAtlasSprite sprite; private final boolean sheeted; private final boolean hasFoil; private final int tintedColor; private final ModelFeatureRenderer.CrumblingOverlay crumblingOverlay; private final int outlineColor;
/* 133 */     public ModelPartSubmit(PoseStack.Pose pose, ModelPart modelPart, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, boolean sheeted, boolean hasFoil, int tintedColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int outlineColor) { this.pose = pose; this.modelPart = modelPart; this.lightCoords = lightCoords; this.overlayCoords = overlayCoords; this.sprite = sprite; this.sheeted = sheeted; this.hasFoil = hasFoil; this.tintedColor = tintedColor; this.crumblingOverlay = crumblingOverlay; this.outlineColor = outlineColor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelPartSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelPartSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelPartSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelPartSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelPartSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #133	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelPartSubmit;
/* 133 */       //   0	8	1	o	Ljava/lang/Object; } public PoseStack.Pose pose() { return this.pose; } public ModelPart modelPart() { return this.modelPart; } public int lightCoords() { return this.lightCoords; } public int overlayCoords() { return this.overlayCoords; } public TextureAtlasSprite sprite() { return this.sprite; } public boolean sheeted() { return this.sheeted; } public boolean hasFoil() { return this.hasFoil; } public int tintedColor() { return this.tintedColor; } public ModelFeatureRenderer.CrumblingOverlay crumblingOverlay() { return this.crumblingOverlay; } public int outlineColor() { return this.outlineColor; }
/*     */      }
/*     */   public static final class TranslucentModelSubmit<S> extends Record { private final SubmitNodeStorage.ModelSubmit<S> modelSubmit; private final RenderType renderType; private final Vector3f position;
/* 136 */     public TranslucentModelSubmit(SubmitNodeStorage.ModelSubmit<S> modelSubmit, RenderType renderType, Vector3f position) { this.modelSubmit = modelSubmit; this.renderType = renderType; this.position = position; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit<TS;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit<TS;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #136	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 136 */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$TranslucentModelSubmit<TS;>; } public SubmitNodeStorage.ModelSubmit<S> modelSubmit() { return this.modelSubmit; } public RenderType renderType() { return this.renderType; } public Vector3f position() { return this.position; }
/*     */      }
/*     */   public static final class BlockSubmit extends Record { private final PoseStack.Pose pose; private final BlockState state; private final int lightCoords; private final int overlayCoords; private final int outlineColor;
/* 139 */     public BlockSubmit(PoseStack.Pose pose, BlockState state, int lightCoords, int overlayCoords, int outlineColor) { this.pose = pose; this.state = state; this.lightCoords = lightCoords; this.overlayCoords = overlayCoords; this.outlineColor = outlineColor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockSubmit;
/* 139 */       //   0	8	1	o	Ljava/lang/Object; } public PoseStack.Pose pose() { return this.pose; } public BlockState state() { return this.state; } public int lightCoords() { return this.lightCoords; } public int overlayCoords() { return this.overlayCoords; } public int outlineColor() { return this.outlineColor; }
/*     */      }
/*     */   public static final class MovingBlockSubmit extends Record { private final Matrix4f pose; private final MovingBlockRenderState movingBlockRenderState;
/* 142 */     public MovingBlockSubmit(Matrix4f pose, MovingBlockRenderState movingBlockRenderState) { this.pose = pose; this.movingBlockRenderState = movingBlockRenderState; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$MovingBlockSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$MovingBlockSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$MovingBlockSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$MovingBlockSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$MovingBlockSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #142	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$MovingBlockSubmit;
/* 142 */       //   0	8	1	o	Ljava/lang/Object; } public Matrix4f pose() { return this.pose; } public MovingBlockRenderState movingBlockRenderState() { return this.movingBlockRenderState; }
/*     */      }
/*     */   public static final class BlockModelSubmit extends Record { private final PoseStack.Pose pose; private final RenderType renderType; private final BlockStateModel model; private final float r; private final float g; private final float b; private final int lightCoords; private final int overlayCoords; private final int outlineColor;
/* 145 */     public BlockModelSubmit(PoseStack.Pose pose, RenderType renderType, BlockStateModel model, float r, float g, float b, int lightCoords, int overlayCoords, int outlineColor) { this.pose = pose; this.renderType = renderType; this.model = model; this.r = r; this.g = g; this.b = b; this.lightCoords = lightCoords; this.overlayCoords = overlayCoords; this.outlineColor = outlineColor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockModelSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockModelSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockModelSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockModelSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockModelSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$BlockModelSubmit;
/* 145 */       //   0	8	1	o	Ljava/lang/Object; } public PoseStack.Pose pose() { return this.pose; } public RenderType renderType() { return this.renderType; } public BlockStateModel model() { return this.model; } public float r() { return this.r; } public float g() { return this.g; } public float b() { return this.b; } public int lightCoords() { return this.lightCoords; } public int overlayCoords() { return this.overlayCoords; } public int outlineColor() { return this.outlineColor; }
/*     */      }
/*     */   public static final class ItemSubmit extends Record { private final PoseStack.Pose pose; private final ItemDisplayContext displayContext; private final int lightCoords; private final int overlayCoords; private final int outlineColor; private final int[] tintLayers; private final List<BakedQuad> quads; private final RenderType renderType; private final ItemStackRenderState.FoilType foilType;
/* 148 */     public ItemSubmit(PoseStack.Pose pose, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, RenderType renderType, ItemStackRenderState.FoilType foilType) { this.pose = pose; this.displayContext = displayContext; this.lightCoords = lightCoords; this.overlayCoords = overlayCoords; this.outlineColor = outlineColor; this.tintLayers = tintLayers; this.quads = quads; this.renderType = renderType; this.foilType = foilType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$ItemSubmit;
/* 148 */       //   0	8	1	o	Ljava/lang/Object; } public PoseStack.Pose pose() { return this.pose; } public ItemDisplayContext displayContext() { return this.displayContext; } public int lightCoords() { return this.lightCoords; } public int overlayCoords() { return this.overlayCoords; } public int outlineColor() { return this.outlineColor; } public int[] tintLayers() { return this.tintLayers; } public List<BakedQuad> quads() { return this.quads; } public RenderType renderType() { return this.renderType; } public ItemStackRenderState.FoilType foilType() { return this.foilType; }
/*     */      }
/*     */   public static final class CustomGeometrySubmit extends Record { private final PoseStack.Pose pose; private final SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer;
/* 151 */     public CustomGeometrySubmit(PoseStack.Pose pose, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) { this.pose = pose; this.customGeometryRenderer = customGeometryRenderer; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SubmitNodeStorage$CustomGeometrySubmit;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$CustomGeometrySubmit; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SubmitNodeStorage$CustomGeometrySubmit;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$CustomGeometrySubmit; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SubmitNodeStorage$CustomGeometrySubmit;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #151	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/SubmitNodeStorage$CustomGeometrySubmit;
/* 151 */       //   0	8	1	o	Ljava/lang/Object; } public PoseStack.Pose pose() { return this.pose; } public SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer() { return this.customGeometryRenderer; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SubmitNodeStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */