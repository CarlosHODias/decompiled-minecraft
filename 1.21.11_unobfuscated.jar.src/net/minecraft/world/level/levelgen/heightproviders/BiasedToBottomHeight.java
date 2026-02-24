/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class BiasedToBottomHeight extends HeightProvider {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(()), (App)Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("inner", 1).forGetter(())).apply((Applicative)i, BiasedToBottomHeight::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<BiasedToBottomHeight> CODEC;
/*    */   
/* 19 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final int inner;
/*    */   
/*    */   private BiasedToBottomHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int inner) {
/* 26 */     this.minInclusive = minInclusive;
/* 27 */     this.maxInclusive = maxInclusive;
/* 28 */     this.inner = inner;
/*    */   }
/*    */   
/*    */   public static BiasedToBottomHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive, int offset) {
/* 32 */     return new BiasedToBottomHeight(minInclusive, maxInclusive, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 37 */     int min = this.minInclusive.resolveY(context);
/* 38 */     int max = this.maxInclusive.resolveY(context);
/* 39 */     if (max - min - this.inner + 1 <= 0) {
/* 40 */       LOGGER.warn("Empty height range: {}", this);
/* 41 */       return min;
/*    */     } 
/*    */     
/* 44 */     int limit = random.nextInt(max - min - this.inner + 1);
/* 45 */     return random.nextInt(limit + this.inner) + min;
/*    */   }
/*    */ 
/*    */   
/*    */   public HeightProviderType<?> getType() {
/* 50 */     return HeightProviderType.BIASED_TO_BOTTOM;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "biased[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + " inner: " + this.inner + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/heightproviders/BiasedToBottomHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */