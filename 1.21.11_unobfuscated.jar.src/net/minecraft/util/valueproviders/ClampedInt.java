/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ClampedInt extends IntProvider {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProvider.CODEC.fieldOf("source").forGetter(()), (App)Codec.INT.fieldOf("min_inclusive").forGetter(()), (App)Codec.INT.fieldOf("max_inclusive").forGetter(())).apply((Applicative)i, ClampedInt::new)).validate(u -> (u.maxInclusive < u.minInclusive) ? DataResult.error(()) : DataResult.success(u));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ClampedInt> CODEC;
/*    */   
/*    */   private final IntProvider source;
/*    */   
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   public static ClampedInt of(IntProvider source, int minInclusive, int maxInclusive) {
/* 27 */     return new ClampedInt(source, minInclusive, maxInclusive);
/*    */   }
/*    */   
/*    */   public ClampedInt(IntProvider source, int minInclusive, int maxInclusive) {
/* 31 */     this.source = source;
/* 32 */     this.minInclusive = minInclusive;
/* 33 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random) {
/* 38 */     return Mth.clamp(this.source.sample(random), this.minInclusive, this.maxInclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinValue() {
/* 43 */     return Math.max(this.minInclusive, this.source.getMinValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxValue() {
/* 48 */     return Math.min(this.maxInclusive, this.source.getMaxValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public IntProviderType<?> getType() {
/* 53 */     return IntProviderType.CLAMPED;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/ClampedInt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */