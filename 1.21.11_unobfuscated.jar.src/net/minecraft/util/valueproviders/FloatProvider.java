/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public abstract class FloatProvider implements SampledFloat {
/*  9 */   private static final Codec<Either<Float, FloatProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either((Codec)Codec.FLOAT, 
/*    */       
/* 11 */       BuiltInRegistries.FLOAT_PROVIDER_TYPE.byNameCodec().dispatch(FloatProvider::getType, FloatProviderType::codec));
/*    */   static {
/* 13 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (FloatProvider)either.map(ConstantFloat::of, ()), f -> (f.getType() == FloatProviderType.CONSTANT) ? Either.left(((ConstantFloat)f).getValue()) : Either.right(f));
/*    */   }
/*    */   
/*    */   public static final Codec<FloatProvider> CODEC;
/*    */   
/*    */   public static Codec<FloatProvider> codec(float minValue, float maxValue) {
/* 19 */     return CODEC.validate(value -> (value.getMinValue() < minValue) ? DataResult.error(()) : ((value.getMaxValue() > maxValue) ? DataResult.error(()) : DataResult.success(value)));
/*    */   }
/*    */   
/*    */   public abstract float getMinValue();
/*    */   
/*    */   public abstract float getMaxValue();
/*    */   
/*    */   public abstract FloatProviderType<?> getType();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/valueproviders/FloatProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */