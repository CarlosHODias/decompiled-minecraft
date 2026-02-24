/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundOpenSignEditorPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundOpenSignEditorPacket> STREAM_CODEC = Packet.codec(ClientboundOpenSignEditorPacket::write, ClientboundOpenSignEditorPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final boolean isFrontText;
/*    */   
/*    */   public ClientboundOpenSignEditorPacket(BlockPos pos, boolean isFrontText) {
/* 16 */     this.pos = pos;
/* 17 */     this.isFrontText = isFrontText;
/*    */   }
/*    */   
/*    */   private ClientboundOpenSignEditorPacket(FriendlyByteBuf input) {
/* 21 */     this.pos = input.readBlockPos();
/* 22 */     this.isFrontText = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeBlockPos(this.pos);
/* 27 */     output.writeBoolean(this.isFrontText);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundOpenSignEditorPacket> type() {
/* 32 */     return GamePacketTypes.CLIENTBOUND_OPEN_SIGN_EDITOR;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 37 */     listener.handleOpenSignEditor(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 41 */     return this.pos;
/*    */   }
/*    */   
/*    */   public boolean isFrontText() {
/* 45 */     return this.isFrontText;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundOpenSignEditorPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */