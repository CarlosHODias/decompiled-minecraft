/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientboundSetEntityMotionPacket implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetEntityMotionPacket> STREAM_CODEC = Packet.codec(ClientboundSetEntityMotionPacket::write, ClientboundSetEntityMotionPacket::new);
/*    */   
/*    */   private final int id;
/*    */   private final Vec3 movement;
/*    */   
/*    */   public ClientboundSetEntityMotionPacket(Entity entity) {
/* 17 */     this(entity.getId(), entity.getDeltaMovement());
/*    */   }
/*    */   
/*    */   public ClientboundSetEntityMotionPacket(int id, Vec3 movement) {
/* 21 */     this.id = id;
/* 22 */     this.movement = movement;
/*    */   }
/*    */   
/*    */   private ClientboundSetEntityMotionPacket(FriendlyByteBuf input) {
/* 26 */     this.id = input.readVarInt();
/* 27 */     this.movement = input.readLpVec3();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 31 */     output.writeVarInt(this.id);
/* 32 */     output.writeLpVec3(this.movement);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetEntityMotionPacket> type() {
/* 37 */     return GamePacketTypes.CLIENTBOUND_SET_ENTITY_MOTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 42 */     listener.handleSetEntityMotion(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 46 */     return this.id;
/*    */   }
/*    */   
/*    */   public Vec3 getMovement() {
/* 50 */     return this.movement;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */