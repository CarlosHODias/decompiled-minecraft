/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundSelectTradePacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSelectTradePacket> STREAM_CODEC = Packet.codec(ServerboundSelectTradePacket::write, ServerboundSelectTradePacket::new);
/*    */   
/*    */   private final int item;
/*    */   
/*    */   public ServerboundSelectTradePacket(int item) {
/* 14 */     this.item = item;
/*    */   }
/*    */   
/*    */   private ServerboundSelectTradePacket(FriendlyByteBuf input) {
/* 18 */     this.item = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeVarInt(this.item);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundSelectTradePacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_SELECT_TRADE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleSelectTrade(this);
/*    */   }
/*    */   
/*    */   public int getItem() {
/* 36 */     return this.item;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSelectTradePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */