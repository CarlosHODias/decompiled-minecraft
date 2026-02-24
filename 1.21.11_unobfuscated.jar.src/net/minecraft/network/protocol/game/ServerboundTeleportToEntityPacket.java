/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ServerboundTeleportToEntityPacket implements Packet<ServerGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ServerboundTeleportToEntityPacket> STREAM_CODEC = Packet.codec(ServerboundTeleportToEntityPacket::write, ServerboundTeleportToEntityPacket::new);
/*    */   
/*    */   private final UUID uuid;
/*    */   
/*    */   public ServerboundTeleportToEntityPacket(UUID uuid) {
/* 19 */     this.uuid = uuid;
/*    */   }
/*    */   
/*    */   private ServerboundTeleportToEntityPacket(FriendlyByteBuf input) {
/* 23 */     this.uuid = input.readUUID();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 27 */     output.writeUUID(this.uuid);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ServerboundTeleportToEntityPacket> type() {
/* 32 */     return GamePacketTypes.SERVERBOUND_TELEPORT_TO_ENTITY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 37 */     listener.handleTeleportToEntityPacket(this);
/*    */   }
/*    */   
/*    */   public Entity getEntity(ServerLevel level) {
/* 41 */     return level.getEntity(this.uuid);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundTeleportToEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */