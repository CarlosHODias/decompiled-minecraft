/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.SignBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SignItem extends StandingAndWallBlockItem {
/*    */   public SignItem(Block sign, Block wallSign, Item.Properties properties) {
/* 15 */     super(sign, wallSign, Direction.DOWN, properties);
/*    */   }
/*    */   
/*    */   public SignItem(Item.Properties properties, Block sign, Block wallSign, Direction direction) {
/* 19 */     super(sign, wallSign, direction, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack itemStack, BlockState placedState) {
/* 24 */     boolean success = super.updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
/*    */     
/* 26 */     if (!level.isClientSide() && !success && player != null) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/* 27 */         Block block = level.getBlockState(pos).getBlock(); if (block instanceof SignBlock) { SignBlock sign = (SignBlock)block;
/* 28 */           sign.openTextEdit(player, signEntity, true); }
/*    */          }
/*    */        }
/* 31 */      return success;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/SignItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */