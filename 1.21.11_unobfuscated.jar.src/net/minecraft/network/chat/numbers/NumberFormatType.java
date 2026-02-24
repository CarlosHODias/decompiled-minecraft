package net.minecraft.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface NumberFormatType<T extends NumberFormat> {
  MapCodec<T> mapCodec();
  
  StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/numbers/NumberFormatType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */