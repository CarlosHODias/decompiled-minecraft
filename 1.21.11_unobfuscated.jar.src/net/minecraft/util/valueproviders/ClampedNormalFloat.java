/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedNormalFloat
/*    */   extends FloatProvider {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("mean").forGetter(()), (App)Codec.FLOAT.fieldOf("deviation").forGetter(()), (App)Codec.FLOAT.fieldOf("min").forGetter(()), (App)Codec.FLOAT.fieldOf("max").forGetter(())).apply((Applicative)i, ClampedNormalFloat::new)).validate(c -> (c.max < c.min) ? DataResult.error(()) : DataResult.success(c));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ClampedNormalFloat> CODEC;
/*    */   
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   private final float min;
/*    */   private final float max;
/*    */   
/*    */   public static ClampedNormalFloat of(float mean, float deviation, float min, float max) {
/* 29 */     return new ClampedNormalFloat(mean, deviation, min, max);
/*    */   }
/*    */   
/*    */   private ClampedNormalFloat(float mean, float deviation, float min, float max) {
/* 33 */     this.mean = mean;
/* 34 */     this.deviation = deviation;
/* 35 */     this.min = min;
/* 36 */     this.max = max;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 41 */     return sample(random, this.mean, this.deviation, this.min, this.max);
/*    */   }
/*    */   
/*    */   public static float sample(RandomSource random, float mean, float deviation, float min, float max) {
/* 45 */     return Mth.clamp(Mth.normal(random, mean, deviation), min, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMinValue() {
/* 50 */     return this.min;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMaxValue() {
/* 55 */     return this.max;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatProviderType<?> getType() {
/* 60 */     return FloatProviderType.CLAMPED_NORMAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min + "-" + this.max + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/ClampedNormalFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */