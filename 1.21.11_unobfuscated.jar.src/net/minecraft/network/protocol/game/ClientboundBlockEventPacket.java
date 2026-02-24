/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ClientboundBlockEventPacket implements Packet<ClientGamePacketListener> {
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockEventPacket> STREAM_CODEC = Packet.codec(ClientboundBlockEventPacket::write, ClientboundBlockEventPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final int b0;
/*    */   private final int b1;
/*    */   private final Block block;
/*    */   
/*    */   public ClientboundBlockEventPacket(BlockPos pos, Block block, int b0, int b1) {
/* 21 */     this.pos = pos;
/* 22 */     this.block = block;
/* 23 */     this.b0 = b0;
/* 24 */     this.b1 = b1;
/*    */   }
/*    */   
/*    */   private ClientboundBlockEventPacket(RegistryFriendlyByteBuf input) {
/* 28 */     this.pos = input.readBlockPos();
/* 29 */     this.b0 = input.readUnsignedByte();
/* 30 */     this.b1 = input.readUnsignedByte();
/*    */     
/* 32 */     this.block = (Block)ByteBufCodecs.registry(Registries.BLOCK).decode(input);
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 36 */     output.writeBlockPos(this.pos);
/* 37 */     output.writeByte(this.b0);
/* 38 */     output.writeByte(this.b1);
/* 39 */     ByteBufCodecs.registry(Registries.BLOCK).encode(output, this.block);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundBlockEventPacket> type() {
/* 44 */     return GamePacketTypes.CLIENTBOUND_BLOCK_EVENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 49 */     listener.handleBlockEvent(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 53 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int getB0() {
/* 57 */     return this.b0;
/*    */   }
/*    */   
/*    */   public int getB1() {
/* 61 */     return this.b1;
/*    */   }
/*    */   
/*    */   public Block getBlock() {
/* 65 */     return this.block;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundBlockEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */