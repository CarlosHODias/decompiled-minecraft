/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.IdMap;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ClientboundBlockUpdatePacket implements Packet<ClientGamePacketListener> {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockUpdatePacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundBlockUpdatePacket::getPos, 
/*    */       
/* 17 */       ByteBufCodecs.idMapper((IdMap)Block.BLOCK_STATE_REGISTRY), ClientboundBlockUpdatePacket::getBlockState, ClientboundBlockUpdatePacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final BlockState blockState;
/*    */ 
/*    */   
/*    */   public ClientboundBlockUpdatePacket(BlockPos pos, BlockState state) {
/* 25 */     this.pos = pos;
/* 26 */     this.blockState = state;
/*    */   }
/*    */   
/*    */   public ClientboundBlockUpdatePacket(BlockGetter level, BlockPos pos) {
/* 30 */     this(pos, level.getBlockState(pos));
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundBlockUpdatePacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_BLOCK_UPDATE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.handleBlockUpdate(this);
/*    */   }
/*    */   
/*    */   public BlockState getBlockState() {
/* 44 */     return this.blockState;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 48 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundBlockUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */