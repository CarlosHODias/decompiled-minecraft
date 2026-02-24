/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPaddleBoatPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPaddleBoatPacket> STREAM_CODEC = Packet.codec(ServerboundPaddleBoatPacket::write, ServerboundPaddleBoatPacket::new);
/*    */   
/*    */   private final boolean left;
/*    */   private final boolean right;
/*    */   
/*    */   public ServerboundPaddleBoatPacket(boolean left, boolean right) {
/* 15 */     this.left = left;
/* 16 */     this.right = right;
/*    */   }
/*    */   
/*    */   private ServerboundPaddleBoatPacket(FriendlyByteBuf input) {
/* 20 */     this.left = input.readBoolean();
/* 21 */     this.right = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeBoolean(this.left);
/* 26 */     output.writeBoolean(this.right);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 31 */     listener.handlePaddleBoat(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundPaddleBoatPacket> type() {
/* 36 */     return GamePacketTypes.SERVERBOUND_PADDLE_BOAT;
/*    */   }
/*    */   
/*    */   public boolean getLeft() {
/* 40 */     return this.left;
/*    */   }
/*    */   
/*    */   public boolean getRight() {
/* 44 */     return this.right;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundPaddleBoatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */