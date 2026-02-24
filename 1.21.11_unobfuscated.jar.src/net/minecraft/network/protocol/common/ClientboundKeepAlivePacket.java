/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundKeepAlivePacket implements Packet<ClientCommonPacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundKeepAlivePacket> STREAM_CODEC = Packet.codec(ClientboundKeepAlivePacket::write, ClientboundKeepAlivePacket::new);
/*    */   
/*    */   private final long id;
/*    */   
/*    */   public ClientboundKeepAlivePacket(long id) {
/* 14 */     this.id = id;
/*    */   }
/*    */   
/*    */   private ClientboundKeepAlivePacket(FriendlyByteBuf input) {
/* 18 */     this.id = input.readLong();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeLong(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundKeepAlivePacket> type() {
/* 27 */     return CommonPacketTypes.CLIENTBOUND_KEEP_ALIVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 32 */     listener.handleKeepAlive(this);
/*    */   }
/*    */   
/*    */   public long getId() {
/* 36 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ClientboundKeepAlivePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */