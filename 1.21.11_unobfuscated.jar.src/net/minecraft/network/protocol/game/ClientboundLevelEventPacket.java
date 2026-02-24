/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundLevelEventPacket implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundLevelEventPacket> STREAM_CODEC = Packet.codec(ClientboundLevelEventPacket::write, ClientboundLevelEventPacket::new);
/*    */   
/*    */   private final int type;
/*    */   private final BlockPos pos;
/*    */   private final int data;
/*    */   private final boolean globalEvent;
/*    */   
/*    */   public ClientboundLevelEventPacket(int type, BlockPos pos, int data, boolean globalEvent) {
/* 19 */     this.type = type;
/* 20 */     this.pos = pos.immutable();
/* 21 */     this.data = data;
/* 22 */     this.globalEvent = globalEvent;
/*    */   }
/*    */   
/*    */   private ClientboundLevelEventPacket(FriendlyByteBuf input) {
/* 26 */     this.type = input.readInt();
/* 27 */     this.pos = input.readBlockPos();
/* 28 */     this.data = input.readInt();
/* 29 */     this.globalEvent = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeInt(this.type);
/* 34 */     output.writeBlockPos(this.pos);
/* 35 */     output.writeInt(this.data);
/* 36 */     output.writeBoolean(this.globalEvent);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundLevelEventPacket> type() {
/* 41 */     return GamePacketTypes.CLIENTBOUND_LEVEL_EVENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 46 */     listener.handleLevelEvent(this);
/*    */   }
/*    */   
/*    */   public boolean isGlobalEvent() {
/* 50 */     return this.globalEvent;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 54 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getData() {
/* 58 */     return this.data;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 62 */     return this.pos;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundLevelEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */