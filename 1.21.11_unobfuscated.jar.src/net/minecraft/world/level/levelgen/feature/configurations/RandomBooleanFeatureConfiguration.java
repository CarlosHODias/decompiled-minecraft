/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomBooleanFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature_true").forGetter(()), (App)PlacedFeature.CODEC.fieldOf("feature_false").forGetter(())).apply((Applicative)i, RandomBooleanFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<RandomBooleanFeatureConfiguration> CODEC;
/*    */   public final Holder<PlacedFeature> featureTrue;
/*    */   public final Holder<PlacedFeature> featureFalse;
/*    */   
/*    */   public RandomBooleanFeatureConfiguration(Holder<PlacedFeature> featureTrue, Holder<PlacedFeature> featureFalse) {
/* 21 */     this.featureTrue = featureTrue;
/* 22 */     this.featureFalse = featureFalse;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> getFeatures() {
/* 27 */     return Stream.concat(((PlacedFeature)this.featureTrue.value()).getFeatures(), ((PlacedFeature)this.featureFalse.value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/RandomBooleanFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */