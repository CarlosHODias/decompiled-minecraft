/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ 
/*    */ public class ServerboundUseItemPacket implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundUseItemPacket> STREAM_CODEC = Packet.codec(ServerboundUseItemPacket::write, ServerboundUseItemPacket::new);
/*    */   
/*    */   private final InteractionHand hand;
/*    */   private final int sequence;
/*    */   private final float yRot;
/*    */   private final float xRot;
/*    */   
/*    */   public ServerboundUseItemPacket(InteractionHand hand, int sequence, float yRot, float xRot) {
/* 18 */     this.hand = hand;
/* 19 */     this.sequence = sequence;
/* 20 */     this.yRot = yRot;
/* 21 */     this.xRot = xRot;
/*    */   }
/*    */   
/*    */   private ServerboundUseItemPacket(FriendlyByteBuf input) {
/* 25 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/* 26 */     this.sequence = input.readVarInt();
/* 27 */     this.yRot = input.readFloat();
/* 28 */     this.xRot = input.readFloat();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 32 */     output.writeEnum((Enum)this.hand);
/* 33 */     output.writeVarInt(this.sequence);
/* 34 */     output.writeFloat(this.yRot);
/* 35 */     output.writeFloat(this.xRot);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundUseItemPacket> type() {
/* 40 */     return GamePacketTypes.SERVERBOUND_USE_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 45 */     listener.handleUseItem(this);
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 49 */     return this.hand;
/*    */   }
/*    */   
/*    */   public int getSequence() {
/* 53 */     return this.sequence;
/*    */   }
/*    */   
/*    */   public float getYRot() {
/* 57 */     return this.yRot;
/*    */   }
/*    */   
/*    */   public float getXRot() {
/* 61 */     return this.xRot;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundUseItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */