/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ public final class ServerboundCustomQueryAnswerPacket extends Record implements net.minecraft.network.protocol.Packet<ServerLoginPacketListener> {
/*    */   private final int transactionId;
/*    */   private final net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload payload;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/*    */   }
/*    */   
/* 15 */   public ServerboundCustomQueryAnswerPacket(int transactionId, net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload payload) { this.transactionId = transactionId; this.payload = payload; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public int transactionId() { return this.transactionId; } public net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload payload() { return this.payload; }
/* 16 */    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerboundCustomQueryAnswerPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ServerboundCustomQueryAnswerPacket::write, ServerboundCustomQueryAnswerPacket::read);
/*    */   
/*    */   private static final int MAX_PAYLOAD_SIZE = 1048576;
/*    */   
/*    */   private static ServerboundCustomQueryAnswerPacket read(net.minecraft.network.FriendlyByteBuf input) {
/* 21 */     int transactionId = input.readVarInt();
/* 22 */     return new ServerboundCustomQueryAnswerPacket(transactionId, 
/*    */         
/* 24 */         readPayload(transactionId, input));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload readPayload(int transactionId, net.minecraft.network.FriendlyByteBuf input) {
/* 34 */     return readUnknownPayload(input);
/*    */   }
/*    */   
/*    */   private static net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload readUnknownPayload(net.minecraft.network.FriendlyByteBuf input) {
/* 38 */     int length = input.readableBytes();
/* 39 */     if (length < 0 || length > 1048576) {
/* 40 */       throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
/*    */     }
/* 42 */     input.skipBytes(length);
/* 43 */     return (net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload)net.minecraft.network.protocol.login.custom.DiscardedQueryAnswerPayload.INSTANCE;
/*    */   }
/*    */   
/*    */   private void write(net.minecraft.network.FriendlyByteBuf output) {
/* 47 */     output.writeVarInt(this.transactionId);
/* 48 */     output.writeNullable(this.payload, (buf, data) -> data.write(buf));
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundCustomQueryAnswerPacket> type() {
/* 53 */     return LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerLoginPacketListener listener) {
/* 58 */     listener.handleCustomQueryPacket(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */