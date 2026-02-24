/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundContainerClosePacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerClosePacket> STREAM_CODEC = Packet.codec(ServerboundContainerClosePacket::write, ServerboundContainerClosePacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   
/*    */   public ServerboundContainerClosePacket(int containerId) {
/* 14 */     this.containerId = containerId;
/*    */   }
/*    */   
/*    */   private ServerboundContainerClosePacket(FriendlyByteBuf input) {
/* 18 */     this.containerId = input.readContainerId();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeContainerId(this.containerId);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundContainerClosePacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_CONTAINER_CLOSE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleContainerClose(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 36 */     return this.containerId;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundContainerClosePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */