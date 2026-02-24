/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface ListBackedContainer
/*    */   extends Container {
/*    */   NonNullList<ItemStack> getItems();
/*    */   
/*    */   default int count() {
/* 15 */     return (int)getItems().stream().filter(Predicate.not(ItemStack::isEmpty)).count();
/*    */   }
/*    */   
/*    */   default int getContainerSize() {
/* 19 */     return getItems().size();
/*    */   }
/*    */   
/*    */   default void clearContent() {
/* 23 */     getItems().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isEmpty() {
/* 28 */     return getItems().stream().allMatch(ItemStack::isEmpty);
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack getItem(int slot) {
/* 33 */     return (ItemStack)getItems().get(slot);
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItem(int slot, int count) {
/* 38 */     ItemStack result = ContainerHelper.removeItem((List)getItems(), slot, count);
/* 39 */     if (!result.isEmpty()) {
/* 40 */       setChanged();
/*    */     }
/* 42 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack removeItemNoUpdate(int slot) {
/* 47 */     return ContainerHelper.removeItem((List)getItems(), slot, getMaxStackSize());
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 52 */     return (acceptsItemType(itemStack) && (
/* 53 */       getItem(slot).isEmpty() || getItem(slot).getCount() < getMaxStackSize(itemStack)));
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean acceptsItemType(ItemStack itemStack) {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   default void setItem(int slot, ItemStack itemStack) {
/* 63 */     setItemNoUpdate(slot, itemStack);
/* 64 */     setChanged();
/*    */   }
/*    */   
/*    */   default void setItemNoUpdate(int slot, ItemStack itemStack) {
/* 68 */     getItems().set(slot, itemStack);
/* 69 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/ListBackedContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */