/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundPingPacket implements Packet<ClientCommonPacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPingPacket> STREAM_CODEC = Packet.codec(ClientboundPingPacket::write, ClientboundPingPacket::new);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   public ClientboundPingPacket(int id) {
/* 15 */     this.id = id;
/*    */   }
/*    */   
/*    */   private ClientboundPingPacket(FriendlyByteBuf input) {
/* 19 */     this.id = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeInt(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundPingPacket> type() {
/* 28 */     return CommonPacketTypes.CLIENTBOUND_PING;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 33 */     listener.handlePing(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 37 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ClientboundPingPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */