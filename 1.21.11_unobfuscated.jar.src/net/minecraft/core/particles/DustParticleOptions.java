/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustParticleOptions extends ScalableParticleOptionsBase {
/* 14 */   public static final DustParticleOptions REDSTONE = new DustParticleOptions(16711680, 1.0F); public static final int REDSTONE_PARTICLE_COLOR = 16711680; public static final MapCodec<DustParticleOptions> CODEC;
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(()), (App)SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply((Applicative)i, DustParticleOptions::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, o -> o.color, ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DustParticleOptions::new);
/*    */   }
/*    */ 
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, DustParticleOptions> STREAM_CODEC;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   public DustParticleOptions(int color, float scale) {
/* 30 */     super(scale);
/* 31 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<DustParticleOptions> getType() {
/* 36 */     return ParticleTypes.DUST;
/*    */   }
/*    */   
/*    */   public Vector3f getColor() {
/* 40 */     return net.minecraft.util.ARGB.vector3fFromRGB24(this.color);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/DustParticleOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */