/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class SimpleParticleType extends ParticleType<SimpleParticleType> implements ParticleOptions {
/*  8 */   private final MapCodec<SimpleParticleType> codec = MapCodec.unit(this::getType);
/*    */   
/* 10 */   private final StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec = StreamCodec.unit(this);
/*    */   
/*    */   protected SimpleParticleType(boolean overrideLimiter) {
/* 13 */     super(overrideLimiter);
/*    */   }
/*    */ 
/*    */   
/*    */   public SimpleParticleType getType() {
/* 18 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapCodec<SimpleParticleType> codec() {
/* 23 */     return this.codec;
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec() {
/* 28 */     return this.streamCodec;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/SimpleParticleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */