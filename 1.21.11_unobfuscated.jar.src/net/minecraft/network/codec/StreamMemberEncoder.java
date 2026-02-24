package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamMemberEncoder<O, T> {
  void encode(T paramT, O paramO);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/codec/StreamMemberEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */