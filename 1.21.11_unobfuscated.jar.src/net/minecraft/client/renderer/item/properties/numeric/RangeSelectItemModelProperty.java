package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

public interface RangeSelectItemModelProperty {
  float get(ItemStack paramItemStack, ClientLevel paramClientLevel, ItemOwner paramItemOwner, int paramInt);
  
  MapCodec<? extends RangeSelectItemModelProperty> type();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/RangeSelectItemModelProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */