/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ 
/*    */ public final class AmbientParticle extends Record {
/*    */   private final ParticleOptions particle;
/*    */   private final float probability;
/*    */   public static final com.mojang.serialization.Codec<AmbientParticle> CODEC;
/*    */   
/* 11 */   public AmbientParticle(ParticleOptions particle, float probability) { this.particle = particle; this.probability = probability; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientParticle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientParticle; } public ParticleOptions particle() { return this.particle; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientParticle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientParticle; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientParticle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientParticle;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public float probability() { return this.probability; } static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.core.particles.ParticleTypes.CODEC.fieldOf("particle").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, AmbientParticle::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canSpawn(net.minecraft.util.RandomSource random) {
/* 18 */     return (random.nextFloat() <= this.probability);
/*    */   }
/*    */   
/*    */   public static java.util.List<AmbientParticle> of(ParticleOptions particle, float probability) {
/* 22 */     return java.util.List.of(new AmbientParticle(particle, probability));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/AmbientParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */