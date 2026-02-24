/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class ServerboundUseItemOnPacket implements Packet<ServerGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ServerboundUseItemOnPacket> STREAM_CODEC = Packet.codec(ServerboundUseItemOnPacket::write, ServerboundUseItemOnPacket::new);
/*    */   
/*    */   private final BlockHitResult blockHit;
/*    */   private final InteractionHand hand;
/*    */   private final int sequence;
/*    */   
/*    */   public ServerboundUseItemOnPacket(InteractionHand hand, BlockHitResult blockHit, int sequence) {
/* 18 */     this.hand = hand;
/* 19 */     this.blockHit = blockHit;
/* 20 */     this.sequence = sequence;
/*    */   }
/*    */   
/*    */   private ServerboundUseItemOnPacket(FriendlyByteBuf input) {
/* 24 */     this.hand = (InteractionHand)input.readEnum(InteractionHand.class);
/* 25 */     this.blockHit = input.readBlockHitResult();
/* 26 */     this.sequence = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 30 */     output.writeEnum((Enum)this.hand);
/* 31 */     output.writeBlockHitResult(this.blockHit);
/* 32 */     output.writeVarInt(this.sequence);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundUseItemOnPacket> type() {
/* 37 */     return GamePacketTypes.SERVERBOUND_USE_ITEM_ON;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 42 */     listener.handleUseItemOn(this);
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 46 */     return this.hand;
/*    */   }
/*    */   
/*    */   public BlockHitResult getHitResult() {
/* 50 */     return this.blockHit;
/*    */   }
/*    */   
/*    */   public int getSequence() {
/* 54 */     return this.sequence;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundUseItemOnPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */