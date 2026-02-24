/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class IntProvider {
/* 10 */   private static final Codec<Either<Integer, IntProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.INT, 
/*    */       
/* 12 */       BuiltInRegistries.INT_PROVIDER_TYPE.byNameCodec().dispatch(IntProvider::getType, IntProviderType::codec));
/*    */   static {
/* 14 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (IntProvider)either.map(ConstantInt::of, ()), f -> (f.getType() == IntProviderType.CONSTANT) ? Either.left(((ConstantInt)f).getValue()) : Either.right(f));
/*    */   }
/*    */   
/*    */   public static final Codec<IntProvider> CODEC;
/*    */   
/*    */   public static Codec<IntProvider> codec(int minValue, int maxValue) {
/* 20 */     return validateCodec(minValue, maxValue, CODEC);
/*    */   }
/*    */   
/*    */   public static <T extends IntProvider> Codec<T> validateCodec(int minValue, int maxValue, Codec<T> codec) {
/* 24 */     return codec.validate(value -> validate(minValue, maxValue, value));
/*    */   }
/*    */   
/*    */   private static <T extends IntProvider> DataResult<T> validate(int minValue, int maxValue, T value) {
/* 28 */     if (value.getMinValue() < minValue) {
/* 29 */       return DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]");
/*    */     }
/* 31 */     if (value.getMaxValue() > maxValue) {
/* 32 */       return DataResult.error(() -> "Value provider too high: " + maxValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]");
/*    */     }
/* 34 */     return DataResult.success(value);
/*    */   }
/*    */   
/* 37 */   public static final Codec<IntProvider> NON_NEGATIVE_CODEC = codec(0, Integer.MAX_VALUE);
/* 38 */   public static final Codec<IntProvider> POSITIVE_CODEC = codec(1, Integer.MAX_VALUE);
/*    */   
/*    */   public abstract int sample(RandomSource paramRandomSource);
/*    */   
/*    */   public abstract int getMinValue();
/*    */   
/*    */   public abstract int getMaxValue();
/*    */   
/*    */   public abstract IntProviderType<?> getType();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/IntProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */