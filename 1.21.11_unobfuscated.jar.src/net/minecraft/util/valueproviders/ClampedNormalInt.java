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
/*    */ public class ClampedNormalInt
/*    */   extends IntProvider {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("mean").forGetter(()), (App)Codec.FLOAT.fieldOf("deviation").forGetter(()), (App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)i, ClampedNormalInt::new)).validate(c -> (c.maxInclusive < c.minInclusive) ? DataResult.error(()) : DataResult.success(c));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ClampedNormalInt> CODEC;
/*    */   
/*    */   private final float mean;
/*    */   
/*    */   private final float deviation;
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static ClampedNormalInt of(float mean, float deviation, int min_inclusive, int max_inclusive) {
/* 29 */     return new ClampedNormalInt(mean, deviation, min_inclusive, max_inclusive);
/*    */   }
/*    */   
/*    */   private ClampedNormalInt(float mean, float deviation, int minInclusive, int maxInclusive) {
/* 33 */     this.mean = mean;
/* 34 */     this.deviation = deviation;
/* 35 */     this.minInclusive = minInclusive;
/* 36 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random) {
/* 41 */     return sample(random, this.mean, this.deviation, this.minInclusive, this.maxInclusive);
/*    */   }
/*    */   
/*    */   public static int sample(RandomSource random, float mean, float deviation, float min_inclusive, float max_inclusive) {
/* 45 */     return (int)Mth.clamp(Mth.normal(random, mean, deviation), min_inclusive, max_inclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 50 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 55 */     return this.maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 60 */     return IntProviderType.CLAMPED_NORMAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/ClampedNormalInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */