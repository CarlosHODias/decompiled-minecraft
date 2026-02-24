/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundDamageEventPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   private final net.minecraft.core.Holder<DamageType> sourceType;
/*    */   private final int sourceCauseId;
/*    */   private final int sourceDirectId;
/*    */   private final java.util.Optional<Vec3> sourcePosition;
/*    */   
/* 17 */   public ClientboundDamageEventPacket(int entityId, net.minecraft.core.Holder<DamageType> sourceType, int sourceCauseId, int sourceDirectId, java.util.Optional<Vec3> sourcePosition) { this.entityId = entityId; this.sourceType = sourceType; this.sourceCauseId = sourceCauseId; this.sourceDirectId = sourceDirectId; this.sourcePosition = sourcePosition; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket; } public int entityId() { return this.entityId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.Holder<DamageType> sourceType() { return this.sourceType; } public int sourceCauseId() { return this.sourceCauseId; } public int sourceDirectId() { return this.sourceDirectId; } public java.util.Optional<Vec3> sourcePosition() { return this.sourcePosition; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, ClientboundDamageEventPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundDamageEventPacket::write, ClientboundDamageEventPacket::new);
/*    */   
/*    */   public ClientboundDamageEventPacket(Entity entity, DamageSource source) {
/* 27 */     this(
/* 28 */         entity.getId(), 
/* 29 */         source.typeHolder(), 
/* 30 */         (source.getEntity() != null) ? source.getEntity().getId() : -1, 
/* 31 */         (source.getDirectEntity() != null) ? source.getDirectEntity().getId() : -1, 
/* 32 */         java.util.Optional.ofNullable(source.sourcePositionRaw()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static void writeOptionalEntityId(FriendlyByteBuf output, int id) {
/* 38 */     output.writeVarInt(id + 1);
/*    */   }
/*    */   
/*    */   private static int readOptionalEntityId(FriendlyByteBuf input) {
/* 42 */     return input.readVarInt() - 1;
/*    */   }
/*    */   
/*    */   private ClientboundDamageEventPacket(RegistryFriendlyByteBuf input) {
/* 46 */     this(
/* 47 */         input.readVarInt(), (net.minecraft.core.Holder<DamageType>)
/* 48 */         DamageType.STREAM_CODEC.decode(input), 
/* 49 */         readOptionalEntityId((FriendlyByteBuf)input), 
/* 50 */         readOptionalEntityId((FriendlyByteBuf)input), 
/* 51 */         input.readOptional(i -> new Vec3(i.readDouble(), i.readDouble(), i.readDouble())));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 56 */     output.writeVarInt(this.entityId);
/* 57 */     DamageType.STREAM_CODEC.encode(output, this.sourceType);
/* 58 */     writeOptionalEntityId((FriendlyByteBuf)output, this.sourceCauseId);
/* 59 */     writeOptionalEntityId((FriendlyByteBuf)output, this.sourceDirectId);
/* 60 */     output.writeOptional(this.sourcePosition, (o, pos) -> {
/*    */           o.writeDouble(pos.x());
/*    */           o.writeDouble(pos.y());
/*    */           o.writeDouble(pos.z());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundDamageEventPacket> type() {
/* 69 */     return GamePacketTypes.CLIENTBOUND_DAMAGE_EVENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 74 */     listener.handleDamageEvent(this);
/*    */   }
/*    */   
/*    */   public DamageSource getSource(net.minecraft.world.level.Level level) {
/* 78 */     if (this.sourcePosition.isPresent()) {
/* 79 */       return new DamageSource(this.sourceType, this.sourcePosition.get());
/*    */     }
/* 81 */     Entity cause = level.getEntity(this.sourceCauseId);
/* 82 */     Entity direct = level.getEntity(this.sourceDirectId);
/* 83 */     return new DamageSource(this.sourceType, direct, cause);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundDamageEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */