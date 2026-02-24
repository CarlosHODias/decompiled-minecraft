package net.minecraft.server.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerConnection {
  ServerPlayer getPlayer();
  
  void send(Packet<?> paramPacket);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/ServerPlayerConnection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */