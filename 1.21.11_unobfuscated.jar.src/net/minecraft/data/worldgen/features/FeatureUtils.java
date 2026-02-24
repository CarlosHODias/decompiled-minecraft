/*    */ package net.minecraft.data.worldgen.features;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ 
/*    */ public class FeatureUtils
/*    */ {
/*    */   public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
/* 24 */     AquaticFeatures.bootstrap(context);
/* 25 */     CaveFeatures.bootstrap(context);
/* 26 */     EndFeatures.bootstrap(context);
/* 27 */     MiscOverworldFeatures.bootstrap(context);
/* 28 */     NetherFeatures.bootstrap(context);
/* 29 */     OreFeatures.bootstrap(context);
/* 30 */     PileFeatures.bootstrap(context);
/* 31 */     TreeFeatures.bootstrap(context);
/* 32 */     VegetationFeatures.bootstrap(context);
/*    */   }
/*    */   
/*    */   private static BlockPredicate simplePatchPredicate(List<Block> allowedOn) {
/*    */     BlockPredicate predicate;
/* 37 */     if (!allowedOn.isEmpty()) {
/* 38 */       predicate = BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), allowedOn));
/*    */     } else {
/* 40 */       predicate = BlockPredicate.ONLY_IN_AIR_PREDICATE;
/*    */     } 
/* 42 */     return predicate;
/*    */   }
/*    */   
/*    */   public static RandomPatchConfiguration simpleRandomPatchConfiguration(int tries, Holder<PlacedFeature> feature) {
/* 46 */     return new RandomPatchConfiguration(tries, 7, 3, feature);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, List<Block> allowedOn, int tries) {
/* 55 */     return simpleRandomPatchConfiguration(tries, PlacementUtils.filtered((Feature)feature, (FeatureConfiguration)config, simplePatchPredicate(allowedOn)));
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, List<Block> allowedOn) {
/* 59 */     return simplePatchConfiguration(feature, config, allowedOn, 96);
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config) {
/* 63 */     return simplePatchConfiguration(feature, config, List.of(), 96);
/*    */   }
/*    */   
/*    */   public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
/* 67 */     return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public static void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<NoneFeatureConfiguration> feature) {
/* 71 */     register(context, id, feature, FeatureConfiguration.NONE);
/*    */   }
/*    */   
/*    */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, F feature, FC config) {
/* 75 */     context.register(id, new ConfiguredFeature((Feature)feature, (FeatureConfiguration)config));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/features/FeatureUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */