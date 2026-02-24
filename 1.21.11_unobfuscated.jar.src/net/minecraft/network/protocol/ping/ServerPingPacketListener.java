package net.minecraft.network.protocol.ping;

import net.minecraft.network.PacketListener;

public interface ServerPingPacketListener extends PacketListener {
  void handlePingRequest(ServerboundPingRequestPacket paramServerboundPingRequestPacket);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/ping/ServerPingPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */