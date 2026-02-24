/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DoubleHighBlockItem extends BlockItem {
/*    */   public DoubleHighBlockItem(Block block, Item.Properties properties) {
/* 12 */     super(block, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
/* 17 */     Level level = context.getLevel();
/* 18 */     BlockPos above = context.getClickedPos().above();
/* 19 */     BlockState aboveState = level.isWaterAt(above) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
/* 20 */     level.setBlock(above, aboveState, 27);
/* 21 */     return super.placeBlock(context, placementState);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/DoubleHighBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */