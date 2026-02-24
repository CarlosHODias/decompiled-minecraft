/*    */ package net.minecraft.network.protocol.ping;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPingRequestPacket implements Packet<ServerPingPacketListener> {
/*  9 */   public static final StreamCodec<ByteBuf, ServerboundPingRequestPacket> STREAM_CODEC = Packet.codec(ServerboundPingRequestPacket::write, ServerboundPingRequestPacket::new);
/*    */   
/*    */   private final long time;
/*    */   
/*    */   public ServerboundPingRequestPacket(long time) {
/* 14 */     this.time = time;
/*    */   }
/*    */   
/*    */   private ServerboundPingRequestPacket(ByteBuf input) {
/* 18 */     this.time = input.readLong();
/*    */   }
/*    */   
/*    */   private void write(ByteBuf output) {
/* 22 */     output.writeLong(this.time);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundPingRequestPacket> type() {
/* 27 */     return PingPacketTypes.SERVERBOUND_PING_REQUEST;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerPingPacketListener listener) {
/* 32 */     listener.handlePingRequest(this);
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 36 */     return this.time;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/ping/ServerboundPingRequestPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */