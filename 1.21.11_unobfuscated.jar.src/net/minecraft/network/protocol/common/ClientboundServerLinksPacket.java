/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ 
/*    */ public final class ClientboundServerLinksPacket extends Record implements net.minecraft.network.protocol.Packet<ClientCommonPacketListener> {
/*    */   private final java.util.List<net.minecraft.server.ServerLinks.UntrustedEntry> links;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;
/*    */   }
/*    */   
/* 11 */   public ClientboundServerLinksPacket(java.util.List<net.minecraft.server.ServerLinks.UntrustedEntry> links) { this.links = links; } public java.util.List<net.minecraft.server.ServerLinks.UntrustedEntry> links() { return this.links; } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;
/*    */   } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundServerLinksPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 15 */   } public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, ClientboundServerLinksPacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.server.ServerLinks.UNTRUSTED_LINKS_STREAM_CODEC, ClientboundServerLinksPacket::links, ClientboundServerLinksPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundServerLinksPacket> type() {
/* 22 */     return CommonPacketTypes.CLIENTBOUND_SERVER_LINKS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 27 */     listener.handleServerLinks(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ClientboundServerLinksPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */