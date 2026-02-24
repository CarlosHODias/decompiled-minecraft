/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class InstantNeighborUpdater
/*    */   implements NeighborUpdater {
/*    */   public InstantNeighborUpdater(Level level) {
/* 14 */     this.level = level;
/*    */   }
/*    */   private final Level level;
/*    */   
/*    */   public void shapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @Block.UpdateFlags int updateFlags, int updateLimit) {
/* 19 */     NeighborUpdater.executeShapeUpdate((LevelAccessor)this.level, direction, pos, neighborPos, neighborState, updateFlags, updateLimit - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) {
/* 24 */     BlockState state = this.level.getBlockState(pos);
/* 25 */     neighborChanged(state, pos, changedBlock, orientation, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {
/* 30 */     NeighborUpdater.executeUpdate(this.level, state, pos, changedBlock, orientation, movedByPiston);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/redstone/InstantNeighborUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */