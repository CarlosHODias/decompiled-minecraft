/*    */ package net.minecraft.network.protocol.login;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundLoginCompressionPacket implements Packet<ClientLoginPacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundLoginCompressionPacket> STREAM_CODEC = Packet.codec(ClientboundLoginCompressionPacket::write, ClientboundLoginCompressionPacket::new);
/*    */   
/*    */   private final int compressionThreshold;
/*    */   
/*    */   public ClientboundLoginCompressionPacket(int compressionThreshold) {
/* 14 */     this.compressionThreshold = compressionThreshold;
/*    */   }
/*    */   
/*    */   private ClientboundLoginCompressionPacket(FriendlyByteBuf input) {
/* 18 */     this.compressionThreshold = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeVarInt(this.compressionThreshold);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundLoginCompressionPacket> type() {
/* 27 */     return LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener listener) {
/* 32 */     listener.handleCompression(this);
/*    */   }
/*    */   
/*    */   public int getCompressionThreshold() {
/* 36 */     return this.compressionThreshold;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ClientboundLoginCompressionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */