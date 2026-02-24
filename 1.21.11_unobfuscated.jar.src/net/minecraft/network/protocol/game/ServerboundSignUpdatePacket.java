/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundSignUpdatePacket implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSignUpdatePacket> STREAM_CODEC = Packet.codec(ServerboundSignUpdatePacket::write, ServerboundSignUpdatePacket::new);
/*    */   
/*    */   private static final int MAX_STRING_LENGTH = 384;
/*    */   private final BlockPos pos;
/*    */   private final String[] lines;
/*    */   private final boolean isFrontText;
/*    */   
/*    */   public ServerboundSignUpdatePacket(BlockPos pos, boolean isFrontText, String line0, String line1, String line2, String line3) {
/* 18 */     this.pos = pos;
/* 19 */     this.isFrontText = isFrontText;
/* 20 */     this.lines = new String[] { line0, line1, line2, line3 };
/*    */   }
/*    */   
/*    */   private ServerboundSignUpdatePacket(FriendlyByteBuf input) {
/* 24 */     this.pos = input.readBlockPos();
/* 25 */     this.isFrontText = input.readBoolean();
/* 26 */     this.lines = new String[4];
/* 27 */     for (int i = 0; i < 4; i++) {
/* 28 */       this.lines[i] = input.readUtf(384);
/*    */     }
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeBlockPos(this.pos);
/* 34 */     output.writeBoolean(this.isFrontText);
/* 35 */     for (int i = 0; i < 4; i++) {
/* 36 */       output.writeUtf(this.lines[i]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundSignUpdatePacket> type() {
/* 42 */     return GamePacketTypes.SERVERBOUND_SIGN_UPDATE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 47 */     listener.handleSignUpdate(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 51 */     return this.pos;
/*    */   }
/*    */   
/*    */   public boolean isFrontText() {
/* 55 */     return this.isFrontText;
/*    */   }
/*    */   
/*    */   public String[] getLines() {
/* 59 */     return this.lines;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSignUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */