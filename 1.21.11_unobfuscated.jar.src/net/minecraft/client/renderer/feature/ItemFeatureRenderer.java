/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.OutlineBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.entity.ItemRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ 
/*    */ public class ItemFeatureRenderer
/*    */ {
/* 13 */   private final PoseStack poseStack = new PoseStack();
/*    */   
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource) {
/* 16 */     for (SubmitNodeStorage.ItemSubmit submit : (Iterable<SubmitNodeStorage.ItemSubmit>)nodeCollection.getItemSubmits()) {
/* 17 */       this.poseStack.pushPose();
/* 18 */       this.poseStack.last().set(submit.pose());
/* 19 */       ItemRenderer.renderItem(submit.displayContext(), this.poseStack, (MultiBufferSource)bufferSource, submit.lightCoords(), submit.overlayCoords(), submit.tintLayers(), submit.quads(), submit.renderType(), submit.foilType());
/* 20 */       if (submit.outlineColor() != 0) {
/* 21 */         outlineBufferSource.setColor(submit.outlineColor());
/* 22 */         ItemRenderer.renderItem(submit.displayContext(), this.poseStack, (MultiBufferSource)outlineBufferSource, submit.lightCoords(), submit.overlayCoords(), submit.tintLayers(), submit.quads(), submit.renderType(), ItemStackRenderState.FoilType.NONE);
/*    */       } 
/* 24 */       this.poseStack.popPose();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/ItemFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */