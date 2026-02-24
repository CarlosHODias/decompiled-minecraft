/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface ContainerSingleItem extends Container {
/*    */   ItemStack getTheItem();
/*    */   
/*    */   default ItemStack splitTheItem(int count) {
/* 12 */     return getTheItem().split(count);
/*    */   }
/*    */   
/*    */   void setTheItem(ItemStack paramItemStack);
/*    */   
/*    */   default ItemStack removeTheItem() {
/* 18 */     return splitTheItem(getMaxStackSize());
/*    */   }
/*    */ 
/*    */   
/*    */   default int getContainerSize() {
/* 23 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isEmpty() {
/* 28 */     return getTheItem().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   default void clearContent() {
/* 33 */     removeTheItem();
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItemNoUpdate(int slot) {
/* 38 */     return removeItem(slot, getMaxStackSize());
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack getItem(int slot) {
/* 43 */     return (slot == 0) ? getTheItem() : ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItem(int slot, int count) {
/* 48 */     if (slot != 0) {
/* 49 */       return ItemStack.EMPTY;
/*    */     }
/* 51 */     return splitTheItem(count);
/*    */   }
/*    */ 
/*    */   
/*    */   default void setItem(int slot, ItemStack itemStack) {
/* 56 */     if (slot == 0)
/* 57 */       setTheItem(itemStack); 
/*    */   }
/*    */   
/*    */   public static interface BlockContainerSingleItem
/*    */     extends ContainerSingleItem
/*    */   {
/*    */     BlockEntity getContainerBlockEntity();
/*    */     
/*    */     default boolean stillValid(Player player) {
/* 66 */       return Container.stillValidBlockEntity(getContainerBlockEntity(), player);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/ticks/ContainerSingleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */