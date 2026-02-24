/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderWarningDelayPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderWarningDelayPacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderWarningDelayPacket::write, ClientboundSetBorderWarningDelayPacket::new);
/*    */   
/*    */   private final int warningDelay;
/*    */   
/*    */   public ClientboundSetBorderWarningDelayPacket(WorldBorder border) {
/* 15 */     this.warningDelay = border.getWarningTime();
/*    */   }
/*    */   
/*    */   private ClientboundSetBorderWarningDelayPacket(FriendlyByteBuf input) {
/* 19 */     this.warningDelay = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeVarInt(this.warningDelay);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetBorderWarningDelayPacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DELAY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleSetBorderWarningDelay(this);
/*    */   }
/*    */   
/*    */   public int getWarningDelay() {
/* 37 */     return this.warningDelay;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetBorderWarningDelayPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */