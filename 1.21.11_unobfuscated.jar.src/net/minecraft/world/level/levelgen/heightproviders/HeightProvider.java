/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public abstract class HeightProvider {
/* 11 */   private static final Codec<Either<VerticalAnchor, HeightProvider>> CONSTANT_OR_DISPATCH_CODEC = Codec.either(VerticalAnchor.CODEC, 
/*    */       
/* 13 */       BuiltInRegistries.HEIGHT_PROVIDER_TYPE.byNameCodec().dispatch(HeightProvider::getType, HeightProviderType::codec));
/*    */   static {
/* 15 */     CODEC = CONSTANT_OR_DISPATCH_CODEC.xmap(either -> (HeightProvider)either.map(ConstantHeight::of, ()), f -> (f.getType() == HeightProviderType.CONSTANT) ? Either.left(((ConstantHeight)f).getValue()) : Either.right(f));
/*    */   }
/*    */   
/*    */   public static final Codec<HeightProvider> CODEC;
/*    */   
/*    */   public abstract HeightProviderType<?> getType();
/*    */   
/*    */   public abstract int sample(RandomSource paramRandomSource, WorldGenerationContext paramWorldGenerationContext);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/heightproviders/HeightProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */