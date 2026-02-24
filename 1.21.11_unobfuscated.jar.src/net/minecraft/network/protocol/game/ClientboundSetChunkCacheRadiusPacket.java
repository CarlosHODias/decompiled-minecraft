/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetChunkCacheRadiusPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetChunkCacheRadiusPacket> STREAM_CODEC = Packet.codec(ClientboundSetChunkCacheRadiusPacket::write, ClientboundSetChunkCacheRadiusPacket::new);
/*    */   
/*    */   private final int radius;
/*    */   
/*    */   public ClientboundSetChunkCacheRadiusPacket(int radius) {
/* 14 */     this.radius = radius;
/*    */   }
/*    */   
/*    */   private ClientboundSetChunkCacheRadiusPacket(FriendlyByteBuf input) {
/* 18 */     this.radius = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeVarInt(this.radius);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetChunkCacheRadiusPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_RADIUS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleSetChunkCacheRadius(this);
/*    */   }
/*    */   
/*    */   public int getRadius() {
/* 36 */     return this.radius;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetChunkCacheRadiusPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */