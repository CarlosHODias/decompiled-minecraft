/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class SculkChargeParticleOptions extends Record implements ParticleOptions {
/*    */   private final float roll;
/*    */   public static final com.mojang.serialization.MapCodec<SculkChargeParticleOptions> CODEC;
/*    */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, SculkChargeParticleOptions> STREAM_CODEC;
/*    */   
/* 10 */   public SculkChargeParticleOptions(float roll) { this.roll = roll; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions; } public float roll() { return this.roll; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.fieldOf("roll").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, SculkChargeParticleOptions::new));
/*    */ 
/*    */ 
/*    */     
/* 15 */     STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.FLOAT, o -> o.roll, SculkChargeParticleOptions::new); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParticleType<SculkChargeParticleOptions> getType() {
/* 22 */     return ParticleTypes.SCULK_CHARGE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/SculkChargeParticleOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */