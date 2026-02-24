package net.minecraft.client.gui;

import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface ItemSlotMouseAction {
  boolean matches(Slot paramSlot);
  
  boolean onMouseScrolled(double paramDouble1, double paramDouble2, int paramInt, ItemStack paramItemStack);
  
  void onStopHovering(Slot paramSlot);
  
  void onSlotClicked(Slot paramSlot, ClickType paramClickType);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/ItemSlotMouseAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */