/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ShulkerBoxSlot extends Slot {
/*    */   public ShulkerBoxSlot(Container container, int slot, int x, int y) {
/*  8 */     super(container, slot, x, y);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack itemStack) {
/* 13 */     return itemStack.getItem().canFitInsideContainerItems();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ShulkerBoxSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */