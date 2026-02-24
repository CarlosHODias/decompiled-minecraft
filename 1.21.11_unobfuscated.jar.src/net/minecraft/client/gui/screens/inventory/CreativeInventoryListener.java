/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerListener;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CreativeInventoryListener implements ContainerListener {
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public CreativeInventoryListener(Minecraft minecraft) {
/* 12 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/* 17 */     this.minecraft.gameMode.handleCreativeModeItemAdd(itemStack, slotIndex);
/*    */   }
/*    */   
/*    */   public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/CreativeInventoryListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */