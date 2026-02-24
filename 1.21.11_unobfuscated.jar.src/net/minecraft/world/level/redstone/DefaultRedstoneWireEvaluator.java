/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DefaultRedstoneWireEvaluator
/*    */   extends RedstoneWireEvaluator {
/*    */   public DefaultRedstoneWireEvaluator(RedStoneWireBlock wireBlock) {
/* 16 */     super(wireBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updatePowerStrength(Level level, BlockPos pos, BlockState state, Orientation orientation, boolean skipShapeUpdates) {
/* 21 */     int targetStrength = calculateTargetStrength(level, pos);
/*    */     
/* 23 */     if ((Integer)state.getValue((Property)RedStoneWireBlock.POWER) != targetStrength) {
/* 24 */       if (level.getBlockState(pos) == state) {
/* 25 */         level.setBlock(pos, (BlockState)state.setValue((Property)RedStoneWireBlock.POWER, targetStrength), 2);
/*    */       }
/*    */ 
/*    */       
/* 29 */       Set<BlockPos> toUpdate = Sets.newHashSet();
/* 30 */       toUpdate.add(pos);
/* 31 */       for (Direction direction : Direction.values()) {
/* 32 */         toUpdate.add(pos.relative(direction));
/*    */       }
/* 34 */       for (BlockPos blockPos : toUpdate) {
/* 35 */         level.updateNeighborsAt(blockPos, (Block)this.wireBlock);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private int calculateTargetStrength(Level level, BlockPos pos) {
/* 41 */     int blockSignal = getBlockSignal(level, pos);
/* 42 */     if (blockSignal == 15) {
/* 43 */       return blockSignal;
/*    */     }
/*    */     
/* 46 */     return Math.max(blockSignal, getIncomingWireSignal(level, pos));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/redstone/DefaultRedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */