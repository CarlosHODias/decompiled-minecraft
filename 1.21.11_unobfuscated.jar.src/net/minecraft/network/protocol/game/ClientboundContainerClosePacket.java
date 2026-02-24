/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundContainerClosePacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundContainerClosePacket> STREAM_CODEC = Packet.codec(ClientboundContainerClosePacket::write, ClientboundContainerClosePacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   
/*    */   public ClientboundContainerClosePacket(int containerId) {
/* 14 */     this.containerId = containerId;
/*    */   }
/*    */   
/*    */   private ClientboundContainerClosePacket(FriendlyByteBuf input) {
/* 18 */     this.containerId = input.readContainerId();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeContainerId(this.containerId);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundContainerClosePacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_CONTAINER_CLOSE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleContainerClose(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 36 */     return this.containerId;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundContainerClosePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */