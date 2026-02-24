/*   */ package net.minecraft.network.protocol.login;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.cookie.ServerCookiePacketListener;
/*   */ 
/*   */ public interface ServerLoginPacketListener
/*   */   extends ServerCookiePacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.LOGIN;
/*   */   }
/*   */   
/*   */   void handleHello(ServerboundHelloPacket paramServerboundHelloPacket);
/*   */   
/*   */   void handleKey(ServerboundKeyPacket paramServerboundKeyPacket);
/*   */   
/*   */   void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket paramServerboundCustomQueryAnswerPacket);
/*   */   
/*   */   void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket paramServerboundLoginAcknowledgedPacket);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ServerLoginPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */