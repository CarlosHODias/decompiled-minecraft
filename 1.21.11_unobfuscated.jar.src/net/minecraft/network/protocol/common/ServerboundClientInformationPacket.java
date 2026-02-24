/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.server.level.ClientInformation;
/*    */ 
/*    */ public final class ServerboundClientInformationPacket extends Record implements net.minecraft.network.protocol.Packet<ServerCommonPacketListener> {
/*    */   private final ClientInformation information;
/*    */   
/*  9 */   public ServerboundClientInformationPacket(ClientInformation information) { this.information = information; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket; } public ClientInformation information() { return this.information; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundClientInformationPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ServerboundClientInformationPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ServerboundClientInformationPacket::write, ServerboundClientInformationPacket::new);
/*    */   
/*    */   private ServerboundClientInformationPacket(FriendlyByteBuf input) {
/* 13 */     this(new ClientInformation(input));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     this.information.write(output);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundClientInformationPacket> type() {
/* 22 */     return CommonPacketTypes.SERVERBOUND_CLIENT_INFORMATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 27 */     listener.handleClientInformation(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ServerboundClientInformationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */