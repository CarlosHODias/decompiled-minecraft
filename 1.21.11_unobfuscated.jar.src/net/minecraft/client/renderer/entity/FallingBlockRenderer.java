/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FallingBlockRenderer extends EntityRenderer<FallingBlockEntity, FallingBlockRenderState> {
/*    */   public FallingBlockRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context);
/* 16 */     this.shadowRadius = 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRender(FallingBlockEntity entity, Frustum culler, double camX, double camY, double camZ) {
/* 21 */     if (!super.shouldRender(entity, culler, camX, camY, camZ)) {
/* 22 */       return false;
/*    */     }
/* 24 */     return (entity.getBlockState() != entity.level().getBlockState(entity.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(FallingBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 29 */     BlockState blockState = state.movingBlockRenderState.blockState;
/* 30 */     if (blockState.getRenderShape() != net.minecraft.world.level.block.RenderShape.MODEL) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     poseStack.pushPose();
/* 35 */     poseStack.translate(-0.5D, 0.0D, -0.5D);
/* 36 */     submitNodeCollector.submitMovingBlock(poseStack, state.movingBlockRenderState);
/* 37 */     poseStack.popPose();
/*    */     
/* 39 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public FallingBlockRenderState createRenderState() {
/* 44 */     return new FallingBlockRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(FallingBlockEntity entity, FallingBlockRenderState state, float partialTicks) {
/* 49 */     super.extractRenderState(entity, state, partialTicks);
/*    */ 
/*    */     
/* 52 */     BlockPos pos = BlockPos.containing(entity.getX(), (entity.getBoundingBox()).maxY, entity.getZ());
/* 53 */     state.movingBlockRenderState.randomSeedPos = entity.getStartPos();
/* 54 */     state.movingBlockRenderState.blockPos = pos;
/* 55 */     state.movingBlockRenderState.blockState = entity.getBlockState();
/* 56 */     state.movingBlockRenderState.biome = entity.level().getBiome(pos);
/* 57 */     state.movingBlockRenderState.level = (net.minecraft.world.level.BlockAndTintGetter)entity.level();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/FallingBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */