/*    */ package net.minecraft.world.level.block.entity;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.TrappedChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class TrappedChestBlockEntity extends ChestBlockEntity {
/*    */   public TrappedChestBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 14 */     super(BlockEntityType.TRAPPED_CHEST, worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void signalOpenCount(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
/* 19 */     super.signalOpenCount(level, pos, blockState, previous, current);
/* 20 */     if (previous != current) {
/* 21 */       Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, ((Direction)blockState.getValue((Property)TrappedChestBlock.FACING)).getOpposite(), Direction.UP);
/* 22 */       Block block = blockState.getBlock();
/* 23 */       level.updateNeighborsAt(pos, block, orientation);
/* 24 */       level.updateNeighborsAt(pos.below(), block, orientation);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/TrappedChestBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */