/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.ClientboundPacketListener;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.ping.ClientPongPacketListener;
/*    */ 
/*    */ public interface ClientStatusPacketListener
/*    */   extends ClientboundPacketListener, ClientPongPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.STATUS;
/*    */   }
/*    */   
/*    */   void handleStatusResponse(ClientboundStatusResponsePacket paramClientboundStatusResponsePacket);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/status/ClientStatusPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */