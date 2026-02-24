/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.entity.PositionMoveRotation;
/*    */ import net.minecraft.world.entity.Relative;
/*    */ 
/*    */ public final class ClientboundTeleportEntityPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   private final PositionMoveRotation change;
/*    */   private final Set<Relative> relatives;
/*    */   private final boolean onGround;
/*    */   
/* 13 */   public ClientboundTeleportEntityPacket(int id, PositionMoveRotation change, Set<Relative> relatives, boolean onGround) { this.id = id; this.change = change; this.relatives = relatives; this.onGround = onGround; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public PositionMoveRotation change() { return this.change; } public Set<Relative> relatives() { return this.relatives; } public boolean onGround() { return this.onGround; }
/* 14 */    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientboundTeleportEntityPacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ClientboundTeleportEntityPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundTeleportEntityPacket::change, Relative.SET_STREAM_CODEC, ClientboundTeleportEntityPacket::relatives, net.minecraft.network.codec.ByteBufCodecs.BOOL, ClientboundTeleportEntityPacket::onGround, ClientboundTeleportEntityPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClientboundTeleportEntityPacket teleport(int id, PositionMoveRotation values, Set<Relative> relatives, boolean onGround) {
/* 23 */     return new ClientboundTeleportEntityPacket(id, values, relatives, onGround);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundTeleportEntityPacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_TELEPORT_ENTITY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleTeleportEntity(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundTeleportEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */