package net.minecraft.client.renderer.item.properties.conditional;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ItemModelPropertyTest {
  boolean get(ItemStack paramItemStack, ClientLevel paramClientLevel, LivingEntity paramLivingEntity, int paramInt, ItemDisplayContext paramItemDisplayContext);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/conditional/ItemModelPropertyTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */