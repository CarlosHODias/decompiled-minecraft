/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetChunkCacheCenterPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetChunkCacheCenterPacket> STREAM_CODEC = Packet.codec(ClientboundSetChunkCacheCenterPacket::write, ClientboundSetChunkCacheCenterPacket::new);
/*    */   
/*    */   private final int x;
/*    */   private final int z;
/*    */   
/*    */   public ClientboundSetChunkCacheCenterPacket(int x, int z) {
/* 15 */     this.x = x;
/* 16 */     this.z = z;
/*    */   }
/*    */   
/*    */   private ClientboundSetChunkCacheCenterPacket(FriendlyByteBuf input) {
/* 20 */     this.x = input.readVarInt();
/* 21 */     this.z = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeVarInt(this.x);
/* 26 */     output.writeVarInt(this.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetChunkCacheCenterPacket> type() {
/* 31 */     return GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_CENTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 36 */     listener.handleSetChunkCacheCenter(this);
/*    */   }
/*    */   
/*    */   public int getX() {
/* 40 */     return this.x;
/*    */   }
/*    */   
/*    */   public int getZ() {
/* 44 */     return this.z;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetChunkCacheCenterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */