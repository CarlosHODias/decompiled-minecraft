/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public class PowerParticleOption implements ParticleOptions {
/*    */   private final ParticleType<PowerParticleOption> type;
/*    */   
/*    */   public static MapCodec<PowerParticleOption> codec(ParticleType<PowerParticleOption> type) {
/* 11 */     return Codec.FLOAT.xmap(power -> new PowerParticleOption(type, power), o -> o.power).optionalFieldOf("power", create(type, 1.0F));
/*    */   }
/*    */   private final float power;
/*    */   public static net.minecraft.network.codec.StreamCodec<? super ByteBuf, PowerParticleOption> streamCodec(ParticleType<PowerParticleOption> type) {
/* 15 */     return ByteBufCodecs.FLOAT.map(color -> new PowerParticleOption(type, color), o -> o.power);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private PowerParticleOption(ParticleType<PowerParticleOption> type, float power) {
/* 22 */     this.type = type;
/* 23 */     this.power = power;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<PowerParticleOption> getType() {
/* 28 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getPower() {
/* 32 */     return this.power;
/*    */   }
/*    */   
/*    */   public static PowerParticleOption create(ParticleType<PowerParticleOption> type, float power) {
/* 36 */     return new PowerParticleOption(type, power);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/PowerParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */