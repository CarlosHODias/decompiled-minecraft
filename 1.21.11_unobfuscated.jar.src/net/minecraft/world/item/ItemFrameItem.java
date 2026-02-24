/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ItemFrameItem extends HangingEntityItem {
/*    */   public ItemFrameItem(EntityType<? extends HangingEntity> entityType, Item.Properties properties) {
/* 11 */     super(entityType, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) {
/* 16 */     return (!player.level().isOutsideBuildHeight(blockPos) && player.mayUseItemAt(blockPos, direction, itemStack));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/ItemFrameItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */