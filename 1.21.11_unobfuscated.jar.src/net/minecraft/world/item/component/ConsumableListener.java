package net.minecraft.world.item.component;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ConsumableListener {
  void onConsume(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack, Consumable paramConsumable);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/ConsumableListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */