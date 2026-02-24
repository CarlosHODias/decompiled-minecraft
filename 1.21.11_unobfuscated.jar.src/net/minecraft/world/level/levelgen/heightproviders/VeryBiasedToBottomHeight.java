/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class VeryBiasedToBottomHeight extends HeightProvider {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("inner", 1).forGetter(())).apply((Applicative)i, VeryBiasedToBottomHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<VeryBiasedToBottomHeight> CODEC;
/*    */   
/* 20 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int inner;
/*    */   
/*    */   private VeryBiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
/* 27 */     this.minInclusive = minInclusive;
/* 28 */     this.maxInclusive = maxInclusive;
/* 29 */     this.inner = inner;
/*    */   }
/*    */   
/*    */   public static VeryBiasedToBottomHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int offset) {
/* 33 */     return new VeryBiasedToBottomHeight(minInclusive, maxInclusive, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 38 */     int min = this.minInclusive.resolveY(context);
/* 39 */     int max = this.maxInclusive.resolveY(context);
/* 40 */     if (max - min - this.inner + 1 <= 0) {
/* 41 */       LOGGER.warn("Empty height range: {}", this);
/* 42 */       return min;
/*    */     } 
/*    */     
/* 45 */     int upperInclusive = Mth.nextInt(random, min + this.inner, max);
/* 46 */     int biasedUpperInclusive = Mth.nextInt(random, min, upperInclusive - 1);
/* 47 */     return Mth.nextInt(random, min, biasedUpperInclusive - 1 + this.inner);
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 52 */     return HeightProviderType.VERY_BIASED_TO_BOTTOM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/heightproviders/VeryBiasedToBottomHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */