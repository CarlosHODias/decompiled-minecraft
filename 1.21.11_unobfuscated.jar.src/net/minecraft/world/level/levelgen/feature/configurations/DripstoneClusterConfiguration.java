/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class DripstoneClusterConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(()), (App)IntProvider.codec(1, 128).fieldOf("height").forGetter(()), (App)IntProvider.codec(1, 128).fieldOf("radius").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(()), (App)IntProvider.codec(0, 128).fieldOf("dripstone_block_layer_thickness").forGetter(()), (App)FloatProvider.codec(0.0F, 2.0F).fieldOf("density").forGetter(()), (App)FloatProvider.codec(0.0F, 2.0F).fieldOf("wetness").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_dripstone_column_at_max_distance_from_center").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_dripstone_column").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, DripstoneClusterConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<DripstoneClusterConfiguration> CODEC;
/*    */ 
/*    */   
/*    */   public final int floorToCeilingSearchRange;
/*    */   
/*    */   public final IntProvider height;
/*    */   
/*    */   public final IntProvider radius;
/*    */   
/*    */   public final int maxStalagmiteStalactiteHeightDiff;
/*    */   
/*    */   public final int heightDeviation;
/*    */   
/*    */   public final IntProvider dripstoneBlockLayerThickness;
/*    */   
/*    */   public final FloatProvider density;
/*    */   
/*    */   public final FloatProvider wetness;
/*    */   
/*    */   public final float chanceOfDripstoneColumnAtMaxDistanceFromCenter;
/*    */   
/*    */   public final int maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;
/*    */   
/*    */   public final int maxDistanceFromCenterAffectingHeightBias;
/*    */ 
/*    */   
/*    */   public DripstoneClusterConfiguration(int floorToCeilingSearchRange, IntProvider height, IntProvider radius, int maxStalagmiteStalactiteHeightDiff, int heightDeviation, IntProvider dripstoneBlockLayerThickness, FloatProvider density, FloatProvider wetness, float chanceOfDripstoneColumnAtMaxDistanceFromCenter, int maxDistanceFromEdgeAffectingChanceOfDripstoneColumn, int maxDistanceFromCenterAffectingHeightBias) {
/* 42 */     this.floorToCeilingSearchRange = floorToCeilingSearchRange;
/* 43 */     this.height = height;
/* 44 */     this.radius = radius;
/* 45 */     this.maxStalagmiteStalactiteHeightDiff = maxStalagmiteStalactiteHeightDiff;
/* 46 */     this.heightDeviation = heightDeviation;
/* 47 */     this.dripstoneBlockLayerThickness = dripstoneBlockLayerThickness;
/* 48 */     this.density = density;
/* 49 */     this.wetness = wetness;
/* 50 */     this.chanceOfDripstoneColumnAtMaxDistanceFromCenter = chanceOfDripstoneColumnAtMaxDistanceFromCenter;
/* 51 */     this.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn = maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;
/* 52 */     this.maxDistanceFromCenterAffectingHeightBias = maxDistanceFromCenterAffectingHeightBias;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/DripstoneClusterConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */