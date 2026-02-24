/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundMoveVehiclePacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final Vec3 position;
/*    */   private final float yRot;
/*    */   private final float xRot;
/*    */   
/* 11 */   public ClientboundMoveVehiclePacket(Vec3 position, float yRot, float xRot) { this.position = position; this.yRot = yRot; this.xRot = xRot; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket; } public Vec3 position() { return this.position; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; }
/* 12 */    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientboundMoveVehiclePacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Vec3.STREAM_CODEC, ClientboundMoveVehiclePacket::position, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ClientboundMoveVehiclePacket::yRot, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ClientboundMoveVehiclePacket::xRot, ClientboundMoveVehiclePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClientboundMoveVehiclePacket fromEntity(Entity entity) {
/* 20 */     return new ClientboundMoveVehiclePacket(entity.position(), entity.getYRot(), entity.getXRot());
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundMoveVehiclePacket> type() {
/* 25 */     return GamePacketTypes.CLIENTBOUND_MOVE_VEHICLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 30 */     listener.handleMoveVehicle(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundMoveVehiclePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */