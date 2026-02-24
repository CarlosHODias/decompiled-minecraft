/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.ItemBlockRenderTypes;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.OutlineBufferSource;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollection;
/*    */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*    */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*    */ import net.minecraft.client.renderer.block.ModelBlockRenderer;
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class BlockFeatureRenderer {
/* 21 */   private final PoseStack poseStack = new PoseStack();
/*    */   
/*    */   public void render(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, BlockRenderDispatcher blockRenderDispatcher, OutlineBufferSource outlineBufferSource) {
/* 24 */     for (SubmitNodeStorage.MovingBlockSubmit submit : (Iterable<SubmitNodeStorage.MovingBlockSubmit>)nodeCollection.getMovingBlockSubmits()) {
/* 25 */       MovingBlockRenderState movingBlockRenderState = submit.movingBlockRenderState();
/* 26 */       BlockState blockState = movingBlockRenderState.blockState;
/* 27 */       List<BlockModelPart> parts = blockRenderDispatcher.getBlockModel(blockState).collectParts(RandomSource.create(blockState.getSeed(movingBlockRenderState.randomSeedPos)));
/*    */       
/* 29 */       PoseStack poseStack = new PoseStack();
/* 30 */       poseStack.mulPose((Matrix4fc)submit.pose());
/* 31 */       blockRenderDispatcher.getModelRenderer().tesselateBlock((BlockAndTintGetter)movingBlockRenderState, parts, blockState, movingBlockRenderState.blockPos, poseStack, bufferSource.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(blockState)), false, OverlayTexture.NO_OVERLAY);
/*    */     } 
/* 33 */     for (SubmitNodeStorage.BlockSubmit submit : (Iterable<SubmitNodeStorage.BlockSubmit>)nodeCollection.getBlockSubmits()) {
/* 34 */       this.poseStack.pushPose();
/* 35 */       this.poseStack.last().set(submit.pose());
/* 36 */       blockRenderDispatcher.renderSingleBlock(submit.state(), this.poseStack, (MultiBufferSource)bufferSource, submit.lightCoords(), submit.overlayCoords());
/* 37 */       if (submit.outlineColor() != 0) {
/* 38 */         outlineBufferSource.setColor(submit.outlineColor());
/* 39 */         blockRenderDispatcher.renderSingleBlock(submit.state(), this.poseStack, (MultiBufferSource)outlineBufferSource, submit.lightCoords(), submit.overlayCoords());
/*    */       } 
/* 41 */       this.poseStack.popPose();
/*    */     } 
/* 43 */     for (SubmitNodeStorage.BlockModelSubmit submit : (Iterable<SubmitNodeStorage.BlockModelSubmit>)nodeCollection.getBlockModelSubmits()) {
/* 44 */       ModelBlockRenderer.renderModel(submit.pose(), bufferSource.getBuffer(submit.renderType()), submit.model(), submit.r(), submit.g(), submit.b(), submit.lightCoords(), submit.overlayCoords());
/* 45 */       if (submit.outlineColor() != 0) {
/* 46 */         outlineBufferSource.setColor(submit.outlineColor());
/* 47 */         ModelBlockRenderer.renderModel(submit.pose(), outlineBufferSource.getBuffer(submit.renderType()), submit.model(), submit.r(), submit.g(), submit.b(), submit.lightCoords(), submit.overlayCoords());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/feature/BlockFeatureRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */