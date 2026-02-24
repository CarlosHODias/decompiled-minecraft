/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class ScalableParticleOptionsBase implements ParticleOptions {
/*    */   public static final float MIN_SCALE = 0.01F;
/*    */   public static final float MAX_SCALE = 4.0F;
/*    */   
/*    */   static {
/* 12 */     SCALE = Codec.FLOAT.validate(v -> (v >= 0.01F && v <= 4.0F) ? DataResult.success(v) : DataResult.error(()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected static final Codec<Float> SCALE;
/*    */   private final float scale;
/*    */   
/*    */   public ScalableParticleOptionsBase(float scale) {
/* 20 */     this.scale = Mth.clamp(scale, 0.01F, 4.0F);
/*    */   }
/*    */   
/*    */   public float getScale() {
/* 24 */     return this.scale;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/particles/ScalableParticleOptionsBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */