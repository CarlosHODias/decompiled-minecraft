package net.minecraft.world;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public interface WorldlyContainer extends Container {
  int[] getSlotsForFace(Direction paramDirection);
  
  boolean canPlaceItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
  
  boolean canTakeItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/WorldlyContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */