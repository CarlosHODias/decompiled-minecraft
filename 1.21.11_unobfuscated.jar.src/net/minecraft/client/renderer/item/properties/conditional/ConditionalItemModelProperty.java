package net.minecraft.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;

public interface ConditionalItemModelProperty extends ItemModelPropertyTest {
  MapCodec<? extends ConditionalItemModelProperty> type();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/conditional/ConditionalItemModelProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */