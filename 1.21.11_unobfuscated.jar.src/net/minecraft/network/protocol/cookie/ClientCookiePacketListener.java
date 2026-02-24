package net.minecraft.network.protocol.cookie;

import net.minecraft.network.ClientboundPacketListener;

public interface ClientCookiePacketListener extends ClientboundPacketListener {
  void handleRequestCookie(ClientboundCookieRequestPacket paramClientboundCookieRequestPacket);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/cookie/ClientCookiePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */