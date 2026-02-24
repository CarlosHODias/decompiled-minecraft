/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class BiasedToBottomInt extends IntProvider {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)i, BiasedToBottomInt::new)).validate(u -> (u.maxInclusive < u.minInclusive) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<BiasedToBottomInt> CODEC;
/*    */   
/*    */   private final int minInclusive;
/*    */   
/*    */   private final int maxInclusive;
/*    */   
/*    */   private BiasedToBottomInt(int minInclusive, int maxInclusive) {
/* 24 */     this.minInclusive = minInclusive;
/* 25 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   
/*    */   public static BiasedToBottomInt of(int minInclusive, int maxInclusive) {
/* 29 */     return new BiasedToBottomInt(minInclusive, maxInclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random) {
/* 34 */     return this.minInclusive + random.nextInt(random.nextInt(this.maxInclusive - this.minInclusive + 1) + 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 39 */     return this.minInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 44 */     return this.maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 49 */     return IntProviderType.BIASED_TO_BOTTOM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/BiasedToBottomInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */