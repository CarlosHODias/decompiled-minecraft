package net.minecraft.network.protocol.cookie;

import net.minecraft.network.protocol.game.ServerPacketListener;

public interface ServerCookiePacketListener extends ServerPacketListener {
  void handleCookieResponse(ServerboundCookieResponsePacket paramServerboundCookieResponsePacket);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/cookie/ServerCookiePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */