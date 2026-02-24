package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamDecoder<I, T> {
  T decode(I paramI);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/codec/StreamDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */