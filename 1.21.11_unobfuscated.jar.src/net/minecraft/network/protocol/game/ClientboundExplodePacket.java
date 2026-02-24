/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundExplodePacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final Vec3 center;
/*    */   private final float radius;
/*    */   private final int blockCount;
/*    */   private final java.util.Optional<Vec3> playerKnockback;
/*    */   private final ParticleOptions explosionParticle;
/*    */   private final net.minecraft.core.Holder<SoundEvent> explosionSound;
/*    */   private final WeightedList<ExplosionParticleInfo> blockParticles;
/*    */   
/* 18 */   public ClientboundExplodePacket(Vec3 center, float radius, int blockCount, java.util.Optional<Vec3> playerKnockback, ParticleOptions explosionParticle, net.minecraft.core.Holder<SoundEvent> explosionSound, WeightedList<ExplosionParticleInfo> blockParticles) { this.center = center; this.radius = radius; this.blockCount = blockCount; this.playerKnockback = playerKnockback; this.explosionParticle = explosionParticle; this.explosionSound = explosionSound; this.blockParticles = blockParticles; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket; } public Vec3 center() { return this.center; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public float radius() { return this.radius; } public int blockCount() { return this.blockCount; } public java.util.Optional<Vec3> playerKnockback() { return this.playerKnockback; } public ParticleOptions explosionParticle() { return this.explosionParticle; } public net.minecraft.core.Holder<SoundEvent> explosionSound() { return this.explosionSound; } public WeightedList<ExplosionParticleInfo> blockParticles() { return this.blockParticles; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ClientboundExplodePacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Vec3.STREAM_CODEC, ClientboundExplodePacket::center, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ClientboundExplodePacket::radius, net.minecraft.network.codec.ByteBufCodecs.INT, ClientboundExplodePacket::blockCount, 
/*    */ 
/*    */ 
/*    */       
/* 31 */       Vec3.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional), ClientboundExplodePacket::playerKnockback, net.minecraft.core.particles.ParticleTypes.STREAM_CODEC, ClientboundExplodePacket::explosionParticle, SoundEvent.STREAM_CODEC, ClientboundExplodePacket::explosionSound, 
/*    */ 
/*    */       
/* 34 */       WeightedList.streamCodec(ExplosionParticleInfo.STREAM_CODEC), ClientboundExplodePacket::blockParticles, ClientboundExplodePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundExplodePacket> type() {
/* 40 */     return GamePacketTypes.CLIENTBOUND_EXPLODE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 45 */     listener.handleExplosion(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundExplodePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */