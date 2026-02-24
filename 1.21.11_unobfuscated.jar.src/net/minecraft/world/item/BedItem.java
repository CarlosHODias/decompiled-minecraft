/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedItem extends BlockItem {
/*    */   public BedItem(Block block, Item.Properties properties) {
/*  9 */     super(block, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
/* 14 */     return context.getLevel().setBlock(context.getClickedPos(), placementState, 26);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/BedItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */