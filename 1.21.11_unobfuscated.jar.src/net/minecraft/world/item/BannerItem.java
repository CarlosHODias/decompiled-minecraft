/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class BannerItem extends StandingAndWallBlockItem {
/*    */   public BannerItem(Block block, Block wallBlock, Item.Properties properties) {
/* 10 */     super(block, wallBlock, Direction.DOWN, properties);
/*    */     
/* 12 */     Validate.isInstanceOf(AbstractBannerBlock.class, block);
/* 13 */     Validate.isInstanceOf(AbstractBannerBlock.class, wallBlock);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 17 */     return ((AbstractBannerBlock)getBlock()).getColor();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/BannerItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */