/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ 
/*    */ public class ClientboundOpenBookPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundOpenBookPacket> STREAM_CODEC = Packet.codec(ClientboundOpenBookPacket::write, ClientboundOpenBookPacket::new);
/*    */   
/*    */   private final InteractionHand hand;
/*    */   
/*    */   public ClientboundOpenBookPacket(InteractionHand hand) {
/* 15 */     this.hand = hand;
/*    */   }
/*    */   
/*    */   private ClientboundOpenBookPacket(FriendlyByteBuf input) {
/* 19 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeEnum((Enum)this.hand);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundOpenBookPacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_OPEN_BOOK;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleOpenBook(this);
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 37 */     return this.hand;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundOpenBookPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */