/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundEntityTagQueryPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundEntityTagQueryPacket> STREAM_CODEC = Packet.codec(ServerboundEntityTagQueryPacket::write, ServerboundEntityTagQueryPacket::new);
/*    */   
/*    */   private final int transactionId;
/*    */   private final int entityId;
/*    */   
/*    */   public ServerboundEntityTagQueryPacket(int transactionId, int entityId) {
/* 15 */     this.transactionId = transactionId;
/* 16 */     this.entityId = entityId;
/*    */   }
/*    */   
/*    */   private ServerboundEntityTagQueryPacket(FriendlyByteBuf input) {
/* 20 */     this.transactionId = input.readVarInt();
/* 21 */     this.entityId = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeVarInt(this.transactionId);
/* 26 */     output.writeVarInt(this.entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundEntityTagQueryPacket> type() {
/* 31 */     return GamePacketTypes.SERVERBOUND_ENTITY_TAG_QUERY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 36 */     listener.handleEntityTagQuery(this);
/*    */   }
/*    */   
/*    */   public int getTransactionId() {
/* 40 */     return this.transactionId;
/*    */   }
/*    */   
/*    */   public int getEntityId() {
/* 44 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundEntityTagQueryPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */