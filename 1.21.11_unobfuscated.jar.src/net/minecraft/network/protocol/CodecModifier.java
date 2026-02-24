package net.minecraft.network.protocol;

import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
public interface CodecModifier<B, V, C> {
  StreamCodec<? super B, V> apply(StreamCodec<? super B, V> paramStreamCodec, C paramC);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/CodecModifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */