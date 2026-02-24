/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class UniformFloat extends FloatProvider {
/*    */   public static final MapCodec<UniformFloat> CODEC;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("min_inclusive").forGetter(()), (App)Codec.FLOAT.fieldOf("max_exclusive").forGetter(())).apply((Applicative)i, UniformFloat::new)).validate(u -> (u.maxExclusive <= u.minInclusive) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final float minInclusive;
/*    */   
/*    */   private final float maxExclusive;
/*    */ 
/*    */   
/*    */   private UniformFloat(float minInclusive, float maxExclusive) {
/* 28 */     this.minInclusive = minInclusive;
/* 29 */     this.maxExclusive = maxExclusive;
/*    */   }
/*    */   
/*    */   public static UniformFloat of(float minInclusive, float maxExclusive) {
/* 33 */     if (maxExclusive <= minInclusive) {
/* 34 */       throw new IllegalArgumentException("Max must exceed min");
/*    */     }
/* 36 */     return new UniformFloat(minInclusive, maxExclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 41 */     return Mth.randomBetween(random, this.minInclusive, this.maxExclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMinValue() {
/* 46 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getMaxValue() {
/* 51 */     return this.maxExclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatProviderType<?> getType() {
/* 56 */     return FloatProviderType.UNIFORM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "[" + this.minInclusive + "-" + this.maxExclusive + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/UniformFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */