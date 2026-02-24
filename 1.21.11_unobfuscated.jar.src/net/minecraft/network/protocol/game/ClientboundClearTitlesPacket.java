/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundClearTitlesPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundClearTitlesPacket> STREAM_CODEC = Packet.codec(ClientboundClearTitlesPacket::write, ClientboundClearTitlesPacket::new);
/*    */   
/*    */   private final boolean resetTimes;
/*    */   
/*    */   public ClientboundClearTitlesPacket(boolean resetTimes) {
/* 14 */     this.resetTimes = resetTimes;
/*    */   }
/*    */   
/*    */   private ClientboundClearTitlesPacket(FriendlyByteBuf input) {
/* 18 */     this.resetTimes = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeBoolean(this.resetTimes);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundClearTitlesPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_CLEAR_TITLES;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleTitlesClear(this);
/*    */   }
/*    */   
/*    */   public boolean shouldResetTimes() {
/* 36 */     return this.resetTimes;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundClearTitlesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */