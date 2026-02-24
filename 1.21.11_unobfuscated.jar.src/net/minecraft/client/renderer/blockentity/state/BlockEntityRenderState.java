/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.client.renderer.LevelRenderer;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockEntityRenderState {
/* 15 */   public BlockPos blockPos = BlockPos.ZERO;
/* 16 */   public BlockState blockState = Blocks.AIR.defaultBlockState();
/* 17 */   public BlockEntityType<?> blockEntityType = BlockEntityType.TEST_BLOCK;
/*    */   public int lightCoords;
/*    */   public ModelFeatureRenderer.CrumblingOverlay breakProgress;
/*    */   
/*    */   public static void extractBase(BlockEntity blockEntity, BlockEntityRenderState state, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 22 */     state.blockPos = blockEntity.getBlockPos();
/* 23 */     state.blockState = blockEntity.getBlockState();
/* 24 */     state.blockEntityType = blockEntity.getType();
/* 25 */     state.lightCoords = (blockEntity.getLevel() != null) ? LevelRenderer.getLightColor((BlockAndTintGetter)blockEntity.getLevel(), blockEntity.getBlockPos()) : 15728880;
/* 26 */     state.breakProgress = breakProgress;
/*    */   }
/*    */   
/*    */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 30 */     category.setDetail("BlockEntityRenderState", getClass().getCanonicalName());
/* 31 */     category.setDetail("Position", this.blockPos);
/* 32 */     Objects.requireNonNull(this.blockState); category.setDetail("Block state", this.blockState::toString);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/BlockEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */