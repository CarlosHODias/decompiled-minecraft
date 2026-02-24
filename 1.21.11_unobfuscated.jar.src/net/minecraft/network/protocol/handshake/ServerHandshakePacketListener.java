/*   */ package net.minecraft.network.protocol.handshake;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*   */ 
/*   */ public interface ServerHandshakePacketListener
/*   */   extends ServerPacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.HANDSHAKING;
/*   */   }
/*   */   
/*   */   void handleIntention(ClientIntentionPacket paramClientIntentionPacket);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/handshake/ServerHandshakePacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */