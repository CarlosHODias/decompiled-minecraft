/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomFeatureConfiguration implements FeatureConfiguration {
/*    */   public static final com.mojang.serialization.Codec<SimpleRandomFeatureConfiguration> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = ExtraCodecs.nonEmptyHolderSet(PlacedFeature.LIST_CODEC).fieldOf("features").xmap(SimpleRandomFeatureConfiguration::new, c -> c.features).codec();
/*    */   }
/*    */   public final HolderSet<PlacedFeature> features;
/*    */   
/*    */   public SimpleRandomFeatureConfiguration(HolderSet<PlacedFeature> features) {
/* 18 */     this.features = features;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> getFeatures() {
/* 23 */     return this.features.stream().flatMap(f -> ((PlacedFeature)f.value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */