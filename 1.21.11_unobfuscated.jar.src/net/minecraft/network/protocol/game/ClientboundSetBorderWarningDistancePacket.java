/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderWarningDistancePacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderWarningDistancePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderWarningDistancePacket::write, ClientboundSetBorderWarningDistancePacket::new);
/*    */   
/*    */   private final int warningBlocks;
/*    */   
/*    */   public ClientboundSetBorderWarningDistancePacket(WorldBorder border) {
/* 15 */     this.warningBlocks = border.getWarningBlocks();
/*    */   }
/*    */   
/*    */   private ClientboundSetBorderWarningDistancePacket(FriendlyByteBuf input) {
/* 19 */     this.warningBlocks = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeVarInt(this.warningBlocks);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetBorderWarningDistancePacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DISTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleSetBorderWarningDistance(this);
/*    */   }
/*    */   
/*    */   public int getWarningBlocks() {
/* 37 */     return this.warningBlocks;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetBorderWarningDistancePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */