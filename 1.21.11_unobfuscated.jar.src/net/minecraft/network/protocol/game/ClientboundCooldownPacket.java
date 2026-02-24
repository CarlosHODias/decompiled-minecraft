/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundCooldownPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final Identifier cooldownGroup;
/*    */   private final int duration;
/*    */   
/* 10 */   public ClientboundCooldownPacket(Identifier cooldownGroup, int duration) { this.cooldownGroup = cooldownGroup; this.duration = duration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket; } public Identifier cooldownGroup() { return this.cooldownGroup; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int duration() { return this.duration; }
/* 11 */    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ClientboundCooldownPacket> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, ClientboundCooldownPacket::cooldownGroup, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ClientboundCooldownPacket::duration, ClientboundCooldownPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundCooldownPacket> type() {
/* 19 */     return GamePacketTypes.CLIENTBOUND_COOLDOWN;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 24 */     listener.handleItemCooldown(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundCooldownPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */