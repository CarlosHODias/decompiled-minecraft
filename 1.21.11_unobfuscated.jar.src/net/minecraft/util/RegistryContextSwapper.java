package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;

public interface RegistryContextSwapper {
  <T> DataResult<T> swapTo(Codec<T> paramCodec, T paramT, HolderLookup.Provider paramProvider);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/RegistryContextSwapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */