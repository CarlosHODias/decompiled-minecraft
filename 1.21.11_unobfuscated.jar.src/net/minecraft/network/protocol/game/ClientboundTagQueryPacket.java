/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundTagQueryPacket implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTagQueryPacket> STREAM_CODEC = Packet.codec(ClientboundTagQueryPacket::write, ClientboundTagQueryPacket::new);
/*    */   
/*    */   private final int transactionId;
/*    */   
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public ClientboundTagQueryPacket(int transactionId, CompoundTag tag) {
/* 18 */     this.transactionId = transactionId;
/* 19 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   private ClientboundTagQueryPacket(FriendlyByteBuf input) {
/* 23 */     this.transactionId = input.readVarInt();
/* 24 */     this.tag = input.readNbt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeVarInt(this.transactionId);
/* 29 */     output.writeNbt((Tag)this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundTagQueryPacket> type() {
/* 34 */     return GamePacketTypes.CLIENTBOUND_TAG_QUERY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 39 */     listener.handleTagQueryPacket(this);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 43 */     return this.transactionId;
/*    */   }
/*    */   
/*    */   public CompoundTag getTag() {
/* 47 */     return this.tag;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSkippable() {
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundTagQueryPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */