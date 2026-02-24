/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.WeightedListInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ 
/*     */ public class PlacementUtils
/*     */ {
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  33 */     AquaticPlacements.bootstrap(context);
/*  34 */     CavePlacements.bootstrap(context);
/*  35 */     EndPlacements.bootstrap(context);
/*  36 */     MiscOverworldPlacements.bootstrap(context);
/*  37 */     NetherPlacements.bootstrap(context);
/*  38 */     OrePlacements.bootstrap(context);
/*  39 */     TreePlacements.bootstrap(context);
/*  40 */     VegetationPlacements.bootstrap(context);
/*  41 */     VillagePlacements.bootstrap(context);
/*     */   }
/*     */   
/*  44 */   public static final PlacementModifier HEIGHTMAP = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING);
/*  45 */   public static final PlacementModifier HEIGHTMAP_NO_LEAVES = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
/*  46 */   public static final PlacementModifier HEIGHTMAP_TOP_SOLID = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG);
/*  47 */   public static final PlacementModifier HEIGHTMAP_WORLD_SURFACE = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG);
/*  48 */   public static final PlacementModifier HEIGHTMAP_OCEAN_FLOOR = (PlacementModifier)HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR);
/*     */   
/*  50 */   public static final PlacementModifier FULL_RANGE = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top());
/*  51 */   public static final PlacementModifier RANGE_10_10 = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10));
/*  52 */   public static final PlacementModifier RANGE_8_8 = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(8));
/*  53 */   public static final PlacementModifier RANGE_4_4 = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(4), VerticalAnchor.belowTop(4));
/*  54 */   public static final PlacementModifier RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT = (PlacementModifier)HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256));
/*     */   
/*     */   public static ResourceKey<PlacedFeature> createKey(String name) {
/*  57 */     return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.withDefaultNamespace(name));
/*     */   }
/*     */   
/*     */   public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placementModifiers) {
/*  61 */     context.register(id, new PlacedFeature(feature, List.copyOf(placementModifiers)));
/*     */   }
/*     */   
/*     */   public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> feature, PlacementModifier... placementModifiers) {
/*  65 */     register(context, id, feature, List.of(placementModifiers));
/*     */   }
/*     */   
/*     */   public static PlacementModifier countExtra(int count, float chance, int extra) {
/*  69 */     float weight = 1.0F / chance;
/*  70 */     if (Math.abs(weight - (int)weight) > 1.0E-5F) {
/*  71 */       throw new IllegalStateException("Chance data cannot be represented as list weight");
/*     */     }
/*  73 */     WeightedList<IntProvider> distribution = WeightedList.builder()
/*  74 */       .add(ConstantInt.of(count), (int)weight - 1)
/*  75 */       .add(ConstantInt.of(count + extra), 1)
/*  76 */       .build();
/*  77 */     return (PlacementModifier)CountPlacement.of((IntProvider)new WeightedListInt(distribution));
/*     */   }
/*     */   
/*     */   public static PlacementFilter isEmpty() {
/*  81 */     return (PlacementFilter)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE);
/*     */   }
/*     */   
/*     */   public static BlockPredicateFilter filteredByBlockSurvival(Block block) {
/*  85 */     return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(block.defaultBlockState(), (Vec3i)BlockPos.ZERO));
/*     */   }
/*     */   
/*     */   public static Holder<PlacedFeature> inlinePlaced(Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placedFeatures) {
/*  89 */     return Holder.direct(new PlacedFeature(configuredFeature, List.of(placedFeatures)));
/*     */   }
/*     */   
/*     */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> inlinePlaced(F feature, FC config, PlacementModifier... placedFeatures) {
/*  93 */     return inlinePlaced(Holder.direct(new ConfiguredFeature((Feature)feature, (FeatureConfiguration)config)), placedFeatures);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> onlyWhenEmpty(F feature, FC config) {
/* 100 */     return filtered(feature, config, BlockPredicate.ONLY_IN_AIR_PREDICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> filtered(F feature, FC config, BlockPredicate predicate) {
/* 107 */     return inlinePlaced(feature, config, new PlacementModifier[] { (PlacementModifier)BlockPredicateFilter.forPredicate(predicate) });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/placement/PlacementUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */