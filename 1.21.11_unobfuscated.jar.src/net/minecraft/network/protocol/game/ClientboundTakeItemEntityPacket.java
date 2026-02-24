/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundTakeItemEntityPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTakeItemEntityPacket> STREAM_CODEC = Packet.codec(ClientboundTakeItemEntityPacket::write, ClientboundTakeItemEntityPacket::new);
/*    */   
/*    */   private final int itemId;
/*    */   private final int playerId;
/*    */   private final int amount;
/*    */   
/*    */   public ClientboundTakeItemEntityPacket(int itemId, int playerId, int amount) {
/* 16 */     this.itemId = itemId;
/* 17 */     this.playerId = playerId;
/* 18 */     this.amount = amount;
/*    */   }
/*    */   
/*    */   private ClientboundTakeItemEntityPacket(FriendlyByteBuf input) {
/* 22 */     this.itemId = input.readVarInt();
/* 23 */     this.playerId = input.readVarInt();
/* 24 */     this.amount = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeVarInt(this.itemId);
/* 29 */     output.writeVarInt(this.playerId);
/* 30 */     output.writeVarInt(this.amount);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundTakeItemEntityPacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_TAKE_ITEM_ENTITY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.handleTakeItemEntity(this);
/*    */   }
/*    */   
/*    */   public int getItemId() {
/* 44 */     return this.itemId;
/*    */   }
/*    */   
/*    */   public int getPlayerId() {
/* 48 */     return this.playerId;
/*    */   }
/*    */   
/*    */   public int getAmount() {
/* 52 */     return this.amount;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundTakeItemEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */