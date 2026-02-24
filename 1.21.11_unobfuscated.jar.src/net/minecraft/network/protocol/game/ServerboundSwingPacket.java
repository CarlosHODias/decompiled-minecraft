/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ 
/*    */ public class ServerboundSwingPacket implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSwingPacket> STREAM_CODEC = Packet.codec(ServerboundSwingPacket::write, ServerboundSwingPacket::new);
/*    */   
/*    */   private final InteractionHand hand;
/*    */   
/*    */   public ServerboundSwingPacket(InteractionHand hand) {
/* 15 */     this.hand = hand;
/*    */   }
/*    */   
/*    */   private ServerboundSwingPacket(FriendlyByteBuf input) {
/* 19 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeEnum((Enum)this.hand);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundSwingPacket> type() {
/* 28 */     return GamePacketTypes.SERVERBOUND_SWING;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 33 */     listener.handleAnimate(this);
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 37 */     return this.hand;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSwingPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */