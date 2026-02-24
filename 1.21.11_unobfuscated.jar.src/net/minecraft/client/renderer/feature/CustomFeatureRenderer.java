/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ 
/*    */ public class CustomFeatureRenderer
/*    */ {
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource) {
/* 20 */     Storage storage = nodeCollection.getCustomGeometrySubmits();
/* 21 */     for (Map.Entry<RenderType, List<SubmitNodeStorage.CustomGeometrySubmit>> entry : storage.customGeometrySubmits.entrySet()) {
/* 22 */       VertexConsumer buffer = bufferSource.getBuffer(entry.getKey());
/* 23 */       for (SubmitNodeStorage.CustomGeometrySubmit customGeometrySubmit : entry.getValue())
/* 24 */         customGeometrySubmit.customGeometryRenderer().render(customGeometrySubmit.pose(), buffer); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Storage
/*    */   {
/* 30 */     private final Map<RenderType, List<SubmitNodeStorage.CustomGeometrySubmit>> customGeometrySubmits = new HashMap<>();
/* 31 */     private final Set<RenderType> customGeometrySubmitsUsage = (Set<RenderType>)new ObjectOpenHashSet();
/*    */     
/*    */     public void add(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
/* 34 */       List<SubmitNodeStorage.CustomGeometrySubmit> submits = this.customGeometrySubmits.computeIfAbsent(renderType, rt -> new ArrayList());
/* 35 */       submits.add(new SubmitNodeStorage.CustomGeometrySubmit(poseStack.last().copy(), customGeometryRenderer));
/*    */     }
/*    */     
/*    */     public void clear() {
/* 39 */       for (Map.Entry<RenderType, List<SubmitNodeStorage.CustomGeometrySubmit>> entry : this.customGeometrySubmits.entrySet()) {
/* 40 */         if (!((List)entry.getValue()).isEmpty()) {
/* 41 */           this.customGeometrySubmitsUsage.add(entry.getKey());
/* 42 */           ((List)entry.getValue()).clear();
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/*    */     public void endFrame() {
/* 48 */       this.customGeometrySubmits.keySet().removeIf(renderType -> !this.customGeometrySubmitsUsage.contains(renderType));
/* 49 */       this.customGeometrySubmitsUsage.clear();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/CustomFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */