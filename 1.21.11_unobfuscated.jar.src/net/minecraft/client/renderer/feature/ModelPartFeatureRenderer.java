/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.OutlineBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.entity.ItemRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.resources.model.ModelBakery;
/*    */ 
/*    */ 
/*    */ public class ModelPartFeatureRenderer
/*    */ {
/* 23 */   private final PoseStack poseStack = new PoseStack();
/*    */   
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, MultiBufferSource.BufferSource crumblingBufferSource) {
/* 26 */     Storage storage = nodeCollection.getModelPartSubmits();
/* 27 */     for (Map.Entry<RenderType, List<SubmitNodeStorage.ModelPartSubmit>> entry : storage.modelPartSubmits.entrySet()) {
/* 28 */       RenderType renderType = entry.getKey();
/* 29 */       List<SubmitNodeStorage.ModelPartSubmit> modelPartSubmits = entry.getValue();
/* 30 */       VertexConsumer buffer = bufferSource.getBuffer(renderType);
/* 31 */       for (SubmitNodeStorage.ModelPartSubmit modelPartSubmit : modelPartSubmits) {
/*    */         VertexConsumer actualBuffer;
/* 33 */         if (modelPartSubmit.sprite() != null) {
/* 34 */           if (modelPartSubmit.hasFoil()) {
/* 35 */             actualBuffer = modelPartSubmit.sprite().wrap(ItemRenderer.getFoilBuffer((MultiBufferSource)bufferSource, renderType, modelPartSubmit.sheeted(), true));
/*    */           } else {
/* 37 */             actualBuffer = modelPartSubmit.sprite().wrap(buffer);
/*    */           } 
/* 39 */         } else if (modelPartSubmit.hasFoil()) {
/* 40 */           actualBuffer = ItemRenderer.getFoilBuffer((MultiBufferSource)bufferSource, renderType, modelPartSubmit.sheeted(), true);
/*    */         } else {
/* 42 */           actualBuffer = buffer;
/*    */         } 
/* 44 */         this.poseStack.last().set(modelPartSubmit.pose());
/* 45 */         modelPartSubmit.modelPart().render(this.poseStack, actualBuffer, modelPartSubmit.lightCoords(), modelPartSubmit.overlayCoords(), modelPartSubmit.tintedColor());
/*    */         
/* 47 */         if (modelPartSubmit.outlineColor() != 0 && (renderType.outline().isPresent() || renderType.isOutline())) {
/* 48 */           outlineBufferSource.setColor(modelPartSubmit.outlineColor());
/* 49 */           VertexConsumer outlineBuffer = outlineBufferSource.getBuffer(renderType);
/* 50 */           modelPartSubmit.modelPart().render(this.poseStack, (modelPartSubmit.sprite() == null) ? outlineBuffer : modelPartSubmit.sprite().wrap(outlineBuffer), modelPartSubmit.lightCoords(), modelPartSubmit.overlayCoords(), modelPartSubmit.tintedColor());
/*    */         } 
/*    */         
/* 53 */         if (modelPartSubmit.crumblingOverlay() != null) {
/* 54 */           SheetedDecalTextureGenerator sheetedDecalTextureGenerator = new SheetedDecalTextureGenerator(crumblingBufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(modelPartSubmit.crumblingOverlay().progress())), modelPartSubmit.crumblingOverlay().cameraPose(), 1.0F);
/* 55 */           modelPartSubmit.modelPart().render(this.poseStack, (VertexConsumer)sheetedDecalTextureGenerator, modelPartSubmit.lightCoords(), modelPartSubmit.overlayCoords(), modelPartSubmit.tintedColor());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Storage {
/* 62 */     private final Map<RenderType, List<SubmitNodeStorage.ModelPartSubmit>> modelPartSubmits = new HashMap<>();
/* 63 */     private final Set<RenderType> modelPartSubmitsUsage = (Set<RenderType>)new ObjectOpenHashSet();
/*    */     
/*    */     public void add(RenderType renderType, SubmitNodeStorage.ModelPartSubmit submit) {
/* 66 */       ((List<SubmitNodeStorage.ModelPartSubmit>)this.modelPartSubmits.computeIfAbsent(renderType, ignored -> new ArrayList())).add(submit);
/*    */     }
/*    */     
/*    */     public void clear() {
/* 70 */       for (Map.Entry<RenderType, List<SubmitNodeStorage.ModelPartSubmit>> entry : this.modelPartSubmits.entrySet()) {
/* 71 */         if (!((List)entry.getValue()).isEmpty()) {
/* 72 */           this.modelPartSubmitsUsage.add(entry.getKey());
/* 73 */           ((List)entry.getValue()).clear();
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/*    */     public void endFrame() {
/* 79 */       this.modelPartSubmits.keySet().removeIf(renderType -> !this.modelPartSubmitsUsage.contains(renderType));
/* 80 */       this.modelPartSubmitsUsage.clear();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/ModelPartFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */