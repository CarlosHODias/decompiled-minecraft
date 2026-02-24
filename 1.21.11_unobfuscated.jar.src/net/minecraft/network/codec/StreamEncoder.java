package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamEncoder<O, T> {
  void encode(O paramO, T paramT);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/codec/StreamEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */