/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundResourcePackPopPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final Optional<UUID> id;
/*    */   
/* 12 */   public ClientboundResourcePackPopPacket(Optional<UUID> id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket; } public Optional<UUID> id() { return this.id; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 15 */   } public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ClientboundResourcePackPopPacket> STREAM_CODEC = Packet.codec(ClientboundResourcePackPopPacket::write, ClientboundResourcePackPopPacket::new);
/*    */   
/*    */   private ClientboundResourcePackPopPacket(FriendlyByteBuf input) {
/* 18 */     this(
/* 19 */         input.readOptional((StreamDecoder)net.minecraft.core.UUIDUtil.STREAM_CODEC));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeOptional(this.id, (net.minecraft.network.codec.StreamEncoder)net.minecraft.core.UUIDUtil.STREAM_CODEC);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundResourcePackPopPacket> type() {
/* 29 */     return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 34 */     listener.handleResourcePackPop(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ClientboundResourcePackPopPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */