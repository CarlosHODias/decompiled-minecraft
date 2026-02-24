package net.minecraft.network.protocol.login.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

public interface CustomQueryPayload {
  Identifier id();
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/custom/CustomQueryPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */