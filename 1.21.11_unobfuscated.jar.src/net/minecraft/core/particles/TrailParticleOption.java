/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TrailParticleOption extends Record implements ParticleOptions {
/*    */   private final Vec3 target;
/*    */   private final int color;
/*    */   private final int duration;
/*    */   public static final com.mojang.serialization.MapCodec<TrailParticleOption> CODEC;
/*    */   
/* 11 */   public TrailParticleOption(Vec3 target, int color, int duration) { this.target = target; this.color = color; this.duration = duration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/TrailParticleOption;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/core/particles/TrailParticleOption; } public Vec3 target() { return this.target; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/TrailParticleOption;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/TrailParticleOption; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/TrailParticleOption;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/TrailParticleOption;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public int color() { return this.color; } public int duration() { return this.duration; } static {
/* 12 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Vec3.CODEC.fieldOf("target").forGetter(TrailParticleOption::target), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(TrailParticleOption::color), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(TrailParticleOption::duration)).apply((com.mojang.datafixers.kinds.Applicative)i, TrailParticleOption::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, TrailParticleOption> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Vec3.STREAM_CODEC, TrailParticleOption::target, net.minecraft.network.codec.ByteBufCodecs.INT, TrailParticleOption::color, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, TrailParticleOption::duration, TrailParticleOption::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParticleType<TrailParticleOption> getType() {
/* 27 */     return ParticleTypes.TRAIL;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/TrailParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */