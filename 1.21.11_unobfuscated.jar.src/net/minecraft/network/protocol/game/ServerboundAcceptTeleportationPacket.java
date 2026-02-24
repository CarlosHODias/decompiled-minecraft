/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundAcceptTeleportationPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundAcceptTeleportationPacket> STREAM_CODEC = Packet.codec(ServerboundAcceptTeleportationPacket::write, ServerboundAcceptTeleportationPacket::new);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   public ServerboundAcceptTeleportationPacket(int id) {
/* 14 */     this.id = id;
/*    */   }
/*    */   
/*    */   private ServerboundAcceptTeleportationPacket(FriendlyByteBuf input) {
/* 18 */     this.id = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeVarInt(this.id);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundAcceptTeleportationPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_ACCEPT_TELEPORTATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleAcceptTeleportPacket(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 36 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundAcceptTeleportationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */