/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public class WeightedListInt extends IntProvider {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeightedList.nonEmptyCodec(IntProvider.CODEC).fieldOf("distribution").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, WeightedListInt::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<WeightedListInt> CODEC;
/*    */   private final WeightedList<IntProvider> distribution;
/*    */   private final int minValue;
/*    */   private final int maxValue;
/*    */   
/*    */   public WeightedListInt(WeightedList<IntProvider> distribution) {
/* 19 */     this.distribution = distribution;
/* 20 */     int min = Integer.MAX_VALUE;
/* 21 */     int max = Integer.MIN_VALUE;
/* 22 */     for (Weighted<IntProvider> value : (Iterable<Weighted<IntProvider>>)distribution.unwrap()) {
/* 23 */       int entryMin = ((IntProvider)value.value()).getMinValue();
/* 24 */       int entryMax = ((IntProvider)value.value()).getMaxValue();
/* 25 */       min = Math.min(min, entryMin);
/* 26 */       max = Math.max(max, entryMax);
/*    */     } 
/* 28 */     this.minValue = min;
/* 29 */     this.maxValue = max;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random) {
/* 34 */     return ((IntProvider)this.distribution.getRandomOrThrow(random)).sample(random);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 39 */     return this.minValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 44 */     return this.maxValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 49 */     return IntProviderType.WEIGHTED_LIST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/WeightedListInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */