/*    */ package net.minecraft.network.protocol.cookie;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ServerboundCookieResponsePacket extends Record implements net.minecraft.network.protocol.Packet<ServerCookiePacketListener> {
/*    */   private final Identifier key;
/*    */   private final byte[] payload;
/*    */   
/* 11 */   public ServerboundCookieResponsePacket(Identifier key, byte[] payload) { this.key = key; this.payload = payload; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket; } public Identifier key() { return this.key; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public byte[] payload() { return this.payload; }
/* 12 */    public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ServerboundCookieResponsePacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ServerboundCookieResponsePacket::write, ServerboundCookieResponsePacket::new);
/*    */   
/*    */   private ServerboundCookieResponsePacket(FriendlyByteBuf input) {
/* 15 */     this(input.readIdentifier(), (byte[])input.readNullable((net.minecraft.network.codec.StreamDecoder)ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeIdentifier(this.key);
/* 20 */     output.writeNullable(this.payload, (net.minecraft.network.codec.StreamEncoder)ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundCookieResponsePacket> type() {
/* 25 */     return CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerCookiePacketListener listener) {
/* 30 */     listener.handleCookieResponse(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */