/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.ScrollWheelHandler;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundSelectBundleItemPacket;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.inventory.ClickType;
/*    */ import net.minecraft.world.inventory.Slot;
/*    */ import net.minecraft.world.item.BundleItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Vector2i;
/*    */ 
/*    */ public class BundleMouseActions
/*    */   implements ItemSlotMouseAction {
/*    */   private final Minecraft minecraft;
/*    */   private final ScrollWheelHandler scrollWheelHandler;
/*    */   
/*    */   public BundleMouseActions(Minecraft minecraft) {
/* 21 */     this.minecraft = minecraft;
/* 22 */     this.scrollWheelHandler = new ScrollWheelHandler();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Slot slot) {
/* 27 */     return slot.getItem().is(ItemTags.BUNDLES);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onMouseScrolled(double scrollX, double scrollY, int slotIndex, ItemStack itemStack) {
/* 32 */     int amountOfShownItems = BundleItem.getNumberOfItemsToShow(itemStack);
/* 33 */     if (amountOfShownItems == 0) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     Vector2i wheelXY = this.scrollWheelHandler.onMouseScroll(scrollX, scrollY);
/* 38 */     int wheel = (wheelXY.y == 0) ? -wheelXY.x : wheelXY.y;
/* 39 */     if (wheel != 0) {
/* 40 */       int selectedItem = BundleItem.getSelectedItem(itemStack);
/* 41 */       int updatedSelectedItem = ScrollWheelHandler.getNextScrollWheelSelection(wheel, selectedItem, amountOfShownItems);
/* 42 */       if (selectedItem != updatedSelectedItem) {
/* 43 */         toggleSelectedBundleItem(itemStack, slotIndex, updatedSelectedItem);
/*    */       }
/*    */     } 
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onStopHovering(Slot hoveredSlot) {
/* 51 */     unselectedBundleItem(hoveredSlot.getItem(), hoveredSlot.index);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSlotClicked(Slot slot, ClickType clickType) {
/* 56 */     if (clickType == ClickType.QUICK_MOVE || clickType == ClickType.SWAP) {
/* 57 */       unselectedBundleItem(slot.getItem(), slot.index);
/*    */     }
/*    */   }
/*    */   
/*    */   private void toggleSelectedBundleItem(ItemStack bundleItem, int slotIndex, int selectedItem) {
/* 62 */     if (this.minecraft.getConnection() != null && 
/* 63 */       selectedItem < BundleItem.getNumberOfItemsToShow(bundleItem)) {
/* 64 */       ClientPacketListener connection = this.minecraft.getConnection();
/* 65 */       BundleItem.toggleSelectedItem(bundleItem, selectedItem);
/* 66 */       connection.send((Packet)new ServerboundSelectBundleItemPacket(slotIndex, selectedItem));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void unselectedBundleItem(ItemStack bundleItem, int slotIndex) {
/* 72 */     toggleSelectedBundleItem(bundleItem, slotIndex, -1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/BundleMouseActions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */