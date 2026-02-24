/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class ServerboundMoveVehiclePacket extends Record implements net.minecraft.network.protocol.Packet<ServerGamePacketListener> {
/*    */   private final net.minecraft.world.phys.Vec3 position;
/*    */   private final float yRot;
/*    */   private final float xRot;
/*    */   private final boolean onGround;
/*    */   
/* 11 */   public ServerboundMoveVehiclePacket(net.minecraft.world.phys.Vec3 position, float yRot, float xRot, boolean onGround) { this.position = position; this.yRot = yRot; this.xRot = xRot; this.onGround = onGround; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket; } public net.minecraft.world.phys.Vec3 position() { return this.position; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; } public boolean onGround() { return this.onGround; }
/* 12 */    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ServerboundMoveVehiclePacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.world.phys.Vec3.STREAM_CODEC, ServerboundMoveVehiclePacket::position, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ServerboundMoveVehiclePacket::yRot, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ServerboundMoveVehiclePacket::xRot, net.minecraft.network.codec.ByteBufCodecs.BOOL, ServerboundMoveVehiclePacket::onGround, ServerboundMoveVehiclePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ServerboundMoveVehiclePacket fromEntity(Entity entity) {
/* 21 */     if (entity.isInterpolating()) {
/* 22 */       return new ServerboundMoveVehiclePacket(entity.getInterpolation().position(), entity.getInterpolation().yRot(), entity.getInterpolation().xRot(), entity.onGround());
/*    */     }
/* 24 */     return new ServerboundMoveVehiclePacket(entity.position(), entity.getYRot(), entity.getXRot(), entity.onGround());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundMoveVehiclePacket> type() {
/* 30 */     return GamePacketTypes.SERVERBOUND_MOVE_VEHICLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 35 */     listener.handleMoveVehicle(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundMoveVehiclePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */