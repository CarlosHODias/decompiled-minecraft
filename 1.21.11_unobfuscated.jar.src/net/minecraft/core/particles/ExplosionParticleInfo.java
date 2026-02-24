/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ 
/*    */ public final class ExplosionParticleInfo extends Record {
/*    */   private final ParticleOptions particle;
/*    */   private final float scaling;
/*    */   private final float speed;
/*    */   public static final com.mojang.serialization.MapCodec<ExplosionParticleInfo> CODEC;
/*    */   
/* 10 */   public ExplosionParticleInfo(ParticleOptions particle, float scaling, float speed) { this.particle = particle; this.scaling = scaling; this.speed = speed; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/ExplosionParticleInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo; } public ParticleOptions particle() { return this.particle; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/ExplosionParticleInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/ExplosionParticleInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public float scaling() { return this.scaling; } public float speed() { return this.speed; } static {
/* 11 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)ParticleTypes.CODEC.fieldOf("particle").forGetter(ExplosionParticleInfo::particle), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.optionalFieldOf("scaling", 1.0F).forGetter(ExplosionParticleInfo::scaling), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.optionalFieldOf("speed", 1.0F).forGetter(ExplosionParticleInfo::speed)).apply((com.mojang.datafixers.kinds.Applicative)i, ExplosionParticleInfo::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ExplosionParticleInfo> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(ParticleTypes.STREAM_CODEC, ExplosionParticleInfo::particle, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ExplosionParticleInfo::scaling, net.minecraft.network.codec.ByteBufCodecs.FLOAT, ExplosionParticleInfo::speed, ExplosionParticleInfo::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/ExplosionParticleInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */