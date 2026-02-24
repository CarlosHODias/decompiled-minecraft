/*     */ package net.minecraft.client.renderer.feature;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.renderer.MultiBufferSource;
/*     */ import net.minecraft.client.renderer.OutlineBufferSource;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*     */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ public class ModelFeatureRenderer
/*     */ {
/*  27 */   private final PoseStack poseStack = new PoseStack();
/*     */   
/*     */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource) {
/*  30 */     Storage storage = nodeCollection.getModelSubmits();
/*  31 */     renderBatch(bufferSource, outlineBufferSource, storage.opaqueModelSubmits, crumblingBufferSource);
/*  32 */     storage.translucentModelSubmits.sort(
/*  33 */         Comparator.comparingDouble(submit -> -submit.position().lengthSquared()));
/*     */     
/*  35 */     renderTranslucents(bufferSource, outlineBufferSource, storage.translucentModelSubmits, crumblingBufferSource);
/*     */   }
/*     */   
/*     */   private void renderTranslucents(MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, List<SubmitNodeStorage.TranslucentModelSubmit<?>> submits, MultiBufferSource.BufferSource crumblingBufferSource) {
/*  39 */     for (SubmitNodeStorage.TranslucentModelSubmit<?> submit : submits) {
/*  40 */       renderModel(submit.modelSubmit(), submit.renderType(), bufferSource.getBuffer(submit.renderType()), outlineBufferSource, crumblingBufferSource);
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderBatch(MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, Map<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> map, MultiBufferSource.BufferSource crumblingBufferSource) {
/*     */     Iterable<Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>>> entries;
/*  46 */     if (SharedConstants.DEBUG_SHUFFLE_MODELS) {
/*  47 */       List<Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>>> shuffledCopy = new ArrayList<>(map.entrySet());
/*  48 */       Collections.shuffle(shuffledCopy);
/*  49 */       entries = shuffledCopy;
/*     */     } else {
/*  51 */       entries = map.entrySet();
/*     */     } 
/*  53 */     for (Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> entry : entries) {
/*  54 */       VertexConsumer buffer = bufferSource.getBuffer(entry.getKey());
/*  55 */       for (SubmitNodeStorage.ModelSubmit<?> submit : entry.getValue()) {
/*  56 */         renderModel(submit, entry.getKey(), buffer, outlineBufferSource, crumblingBufferSource);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private <S> void renderModel(SubmitNodeStorage.ModelSubmit<S> submit, RenderType renderType, VertexConsumer buffer, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource) {
/*  62 */     this.poseStack.pushPose();
/*  63 */     this.poseStack.last().set(submit.pose());
/*     */     
/*  65 */     Model<? super S> model = submit.model();
/*  66 */     VertexConsumer wrappedBuffer = (submit.sprite() == null) ? buffer : submit.sprite().wrap(buffer);
/*     */     
/*  68 */     model.setupAnim(submit.state());
/*  69 */     model.renderToBuffer(this.poseStack, wrappedBuffer, submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
/*     */     
/*  71 */     if (submit.outlineColor() != 0 && (renderType.outline().isPresent() || renderType.isOutline())) {
/*  72 */       outlineBufferSource.setColor(submit.outlineColor());
/*  73 */       VertexConsumer outlineBuffer = outlineBufferSource.getBuffer(renderType);
/*  74 */       model.renderToBuffer(this.poseStack, (submit.sprite() == null) ? outlineBuffer : submit.sprite().wrap(outlineBuffer), submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
/*     */     } 
/*     */     
/*  77 */     if (submit.crumblingOverlay() != null && renderType.affectsCrumbling()) {
/*  78 */       SheetedDecalTextureGenerator sheetedDecalTextureGenerator = new SheetedDecalTextureGenerator(crumblingBufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(submit.crumblingOverlay().progress())), submit.crumblingOverlay().cameraPose(), 1.0F);
/*  79 */       model.renderToBuffer(this.poseStack, (submit.sprite() == null) ? (VertexConsumer)sheetedDecalTextureGenerator : submit.sprite().wrap((VertexConsumer)sheetedDecalTextureGenerator), submit.lightCoords(), submit.overlayCoords(), submit.tintedColor());
/*     */     } 
/*     */     
/*  82 */     this.poseStack.popPose();
/*     */   }
/*     */   public static final class CrumblingOverlay extends Record { private final int progress; private final PoseStack.Pose cameraPose;
/*  85 */     public CrumblingOverlay(int progress, PoseStack.Pose cameraPose) { this.progress = progress; this.cameraPose = cameraPose; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  85 */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay; } public int progress() { return this.progress; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  85 */       //   0	7	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay; } public PoseStack.Pose cameraPose() { return this.cameraPose; }
/*     */      public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*  88 */     } } public static class Storage { private final Map<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> opaqueModelSubmits = new HashMap<>();
/*  89 */     private final List<SubmitNodeStorage.TranslucentModelSubmit<?>> translucentModelSubmits = new ArrayList<>();
/*  90 */     private final Set<RenderType> usedModelSubmitBuckets = (Set<RenderType>)new ObjectOpenHashSet();
/*     */     
/*     */     public void add(RenderType renderType, SubmitNodeStorage.ModelSubmit<?> modelSubmit) {
/*  93 */       if (renderType.pipeline().getBlendFunction().isEmpty()) {
/*     */         
/*  95 */         ((List<SubmitNodeStorage.ModelSubmit<?>>)
/*  96 */           this.opaqueModelSubmits.computeIfAbsent(renderType, ignored -> new ArrayList()))
/*  97 */           .add(modelSubmit);
/*     */       } else {
/*  99 */         Vector3f position = modelSubmit.pose().pose().transformPosition(new Vector3f());
/* 100 */         this.translucentModelSubmits.add(new SubmitNodeStorage.TranslucentModelSubmit(modelSubmit, renderType, position));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void clear() {
/* 105 */       this.translucentModelSubmits.clear();
/* 106 */       for (Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> bucketEntry : this.opaqueModelSubmits.entrySet()) {
/* 107 */         List<SubmitNodeStorage.ModelSubmit<?>> bucket = bucketEntry.getValue();
/* 108 */         if (!bucket.isEmpty()) {
/* 109 */           this.usedModelSubmitBuckets.add(bucketEntry.getKey());
/* 110 */           bucket.clear();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void endFrame() {
/* 116 */       this.opaqueModelSubmits.keySet().removeIf(renderType -> !this.usedModelSubmitBuckets.contains(renderType));
/* 117 */       this.usedModelSubmitBuckets.clear();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/ModelFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */