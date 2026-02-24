/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*    */ import net.minecraft.network.protocol.ping.ServerPingPacketListener;
/*    */ 
/*    */ public interface ServerStatusPacketListener
/*    */   extends ServerPacketListener, ServerPingPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.STATUS;
/*    */   }
/*    */   
/*    */   void handleStatusRequest(ServerboundStatusRequestPacket paramServerboundStatusRequestPacket);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/status/ServerStatusPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */