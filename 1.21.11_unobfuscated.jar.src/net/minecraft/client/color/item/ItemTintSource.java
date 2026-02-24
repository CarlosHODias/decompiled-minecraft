package net.minecraft.client.color.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ItemTintSource {
  int calculate(ItemStack paramItemStack, ClientLevel paramClientLevel, LivingEntity paramLivingEntity);
  
  MapCodec<? extends ItemTintSource> type();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/item/ItemTintSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */