/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ClientboundHurtAnimationPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   private final float yaw;
/*    */   
/*  9 */   public ClientboundHurtAnimationPacket(int id, float yaw) { this.id = id; this.yaw = yaw; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public float yaw() { return this.yaw; }
/* 10 */    public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ClientboundHurtAnimationPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundHurtAnimationPacket::write, ClientboundHurtAnimationPacket::new);
/*    */   
/*    */   public ClientboundHurtAnimationPacket(net.minecraft.world.entity.LivingEntity entity) {
/* 13 */     this(entity.getId(), entity.getHurtDir());
/*    */   }
/*    */   
/*    */   private ClientboundHurtAnimationPacket(FriendlyByteBuf input) {
/* 17 */     this(input.readVarInt(), input.readFloat());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 21 */     output.writeVarInt(this.id);
/* 22 */     output.writeFloat(this.yaw);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundHurtAnimationPacket> type() {
/* 27 */     return GamePacketTypes.CLIENTBOUND_HURT_ANIMATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 32 */     listener.handleHurtAnimation(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundHurtAnimationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */