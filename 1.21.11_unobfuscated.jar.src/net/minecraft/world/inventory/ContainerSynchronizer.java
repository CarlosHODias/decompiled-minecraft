package net.minecraft.world.inventory;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface ContainerSynchronizer {
  void sendInitialData(AbstractContainerMenu paramAbstractContainerMenu, List<ItemStack> paramList, ItemStack paramItemStack, int[] paramArrayOfint);
  
  void sendSlotChange(AbstractContainerMenu paramAbstractContainerMenu, int paramInt, ItemStack paramItemStack);
  
  void sendCarriedChange(AbstractContainerMenu paramAbstractContainerMenu, ItemStack paramItemStack);
  
  void sendDataChange(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2);
  
  RemoteSlot createSlot();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ContainerSynchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */