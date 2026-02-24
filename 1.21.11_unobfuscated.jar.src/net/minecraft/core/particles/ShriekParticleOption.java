/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ShriekParticleOption implements ParticleOptions {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("delay").forGetter(())).apply((Applicative)i, ShriekParticleOption::new));
/*    */ 
/*    */ 
/*    */     
/* 15 */     STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, o -> o.delay, ShriekParticleOption::new);
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<ShriekParticleOption> CODEC;
/*    */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ShriekParticleOption> STREAM_CODEC;
/*    */   private final int delay;
/*    */   
/*    */   public ShriekParticleOption(int delay) {
/* 23 */     this.delay = delay;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<ShriekParticleOption> getType() {
/* 28 */     return ParticleTypes.SHRIEK;
/*    */   }
/*    */   
/*    */   public int getDelay() {
/* 32 */     return this.delay;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/ShriekParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */