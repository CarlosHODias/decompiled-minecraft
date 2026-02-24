/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundSetCarriedItemPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetCarriedItemPacket> STREAM_CODEC = Packet.codec(ServerboundSetCarriedItemPacket::write, ServerboundSetCarriedItemPacket::new);
/*    */   
/*    */   private final int slot;
/*    */   
/*    */   public ServerboundSetCarriedItemPacket(int slot) {
/* 14 */     this.slot = slot;
/*    */   }
/*    */   
/*    */   private ServerboundSetCarriedItemPacket(FriendlyByteBuf input) {
/* 18 */     this.slot = input.readShort();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeShort(this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundSetCarriedItemPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_SET_CARRIED_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleSetCarriedItem(this);
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 36 */     return this.slot;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSetCarriedItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */