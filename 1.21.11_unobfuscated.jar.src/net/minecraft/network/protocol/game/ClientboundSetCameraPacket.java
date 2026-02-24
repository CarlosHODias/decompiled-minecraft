/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ClientboundSetCameraPacket implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetCameraPacket> STREAM_CODEC = Packet.codec(ClientboundSetCameraPacket::write, ClientboundSetCameraPacket::new);
/*    */   
/*    */   private final int cameraId;
/*    */   
/*    */   public ClientboundSetCameraPacket(Entity camera) {
/* 17 */     this.cameraId = camera.getId();
/*    */   }
/*    */   
/*    */   private ClientboundSetCameraPacket(FriendlyByteBuf input) {
/* 21 */     this.cameraId = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeVarInt(this.cameraId);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundSetCameraPacket> type() {
/* 30 */     return GamePacketTypes.CLIENTBOUND_SET_CAMERA;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 35 */     listener.handleSetCamera(this);
/*    */   }
/*    */   
/*    */   public Entity getEntity(Level level) {
/* 39 */     return level.getEntity(this.cameraId);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetCameraPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */