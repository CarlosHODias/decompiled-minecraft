/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ContainerComponentManipulator<T>
/*    */ {
/*    */   default void setContents(ItemStack itemStack, T defaultValue, Stream<ItemStack> newContents) {
/* 20 */     T currentValue = (T)itemStack.getOrDefault(type(), defaultValue);
/* 21 */     T newValue = setContents(currentValue, newContents);
/* 22 */     itemStack.set(type(), newValue);
/*    */   }
/*    */   
/*    */   default void setContents(ItemStack itemStack, Stream<ItemStack> newContents) {
/* 26 */     setContents(itemStack, empty(), newContents);
/*    */   }
/*    */   
/*    */   default void modifyItems(ItemStack itemStack, UnaryOperator<ItemStack> modifier) {
/* 30 */     T contents = (T)itemStack.get(type());
/* 31 */     if (contents != null) {
/*    */       UnaryOperator<ItemStack> nonEmptyModifier = currentItemStack -> {
/*    */           if (currentItemStack.isEmpty()) {
/*    */             return currentItemStack;
/*    */           }
/*    */           
/*    */           ItemStack newItemStack = modifier.apply(currentItemStack);
/*    */           newItemStack.limitSize(newItemStack.getMaxStackSize());
/*    */           return newItemStack;
/*    */         };
/* 41 */       setContents(itemStack, getContents(contents).map(nonEmptyModifier));
/*    */     } 
/*    */   }
/*    */   
/*    */   default SlotCollection getSlots(ItemStack itemStack) {
/* 46 */     return () -> {
/*    */         T contents = (T)itemStack.get(type());
/*    */         return (contents != null) ? getContents(contents).filter(()) : Stream.empty();
/*    */       };
/*    */   }
/*    */   
/*    */   DataComponentType<T> type();
/*    */   
/*    */   T empty();
/*    */   
/*    */   T setContents(T paramT, Stream<ItemStack> paramStream);
/*    */   
/*    */   Stream<ItemStack> getContents(T paramT);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/ContainerComponentManipulator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */