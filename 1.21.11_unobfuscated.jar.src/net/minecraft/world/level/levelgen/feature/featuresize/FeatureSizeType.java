/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class FeatureSizeType<P extends FeatureSize> {
/*  8 */   public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE = register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);
/*  9 */   public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE = register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);
/*    */   
/*    */   private static <P extends FeatureSize> FeatureSizeType<P> register(String name, MapCodec<P> codec) {
/* 12 */     return (FeatureSizeType<P>)Registry.register(BuiltInRegistries.FEATURE_SIZE_TYPE, name, new FeatureSizeType<>(codec));
/*    */   }
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private FeatureSizeType(MapCodec<P> codec) {
/* 18 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 22 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/featuresize/FeatureSizeType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */