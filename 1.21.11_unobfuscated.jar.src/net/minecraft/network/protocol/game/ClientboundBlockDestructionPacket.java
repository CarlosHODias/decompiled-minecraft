/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundBlockDestructionPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundBlockDestructionPacket> STREAM_CODEC = Packet.codec(ClientboundBlockDestructionPacket::write, ClientboundBlockDestructionPacket::new);
/*    */   
/*    */   private final int id;
/*    */   private final BlockPos pos;
/*    */   private final int progress;
/*    */   
/*    */   public ClientboundBlockDestructionPacket(int id, BlockPos pos, int progress) {
/* 17 */     this.id = id;
/* 18 */     this.pos = pos;
/* 19 */     this.progress = progress;
/*    */   }
/*    */   
/*    */   private ClientboundBlockDestructionPacket(FriendlyByteBuf input) {
/* 23 */     this.id = input.readVarInt();
/* 24 */     this.pos = input.readBlockPos();
/* 25 */     this.progress = input.readUnsignedByte();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeVarInt(this.id);
/* 30 */     output.writeBlockPos(this.pos);
/* 31 */     output.writeByte(this.progress);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundBlockDestructionPacket> type() {
/* 36 */     return GamePacketTypes.CLIENTBOUND_BLOCK_DESTRUCTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 41 */     listener.handleBlockDestruction(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 45 */     return this.id;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 49 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int getProgress() {
/* 53 */     return this.progress;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundBlockDestructionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */