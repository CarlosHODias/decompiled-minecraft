/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ 
/*    */ public class LargeDripstoneConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30).forGetter(()), (App)net.minecraft.util.valueproviders.IntProvider.codec(1, 60).fieldOf("column_radius").forGetter(()), (App)FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(()), (App)Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(()), (App)FloatProvider.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(()), (App)FloatProvider.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(()), (App)FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(()), (App)Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(()), (App)Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, LargeDripstoneConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<LargeDripstoneConfiguration> CODEC;
/*    */ 
/*    */   
/*    */   public final int floorToCeilingSearchRange;
/*    */ 
/*    */   
/*    */   public final net.minecraft.util.valueproviders.IntProvider columnRadius;
/*    */ 
/*    */   
/*    */   public final FloatProvider heightScale;
/*    */ 
/*    */   
/*    */   public final float maxColumnRadiusToCaveHeightRatio;
/*    */ 
/*    */   
/*    */   public final FloatProvider stalactiteBluntness;
/*    */ 
/*    */   
/*    */   public final FloatProvider stalagmiteBluntness;
/*    */ 
/*    */   
/*    */   public final FloatProvider windSpeed;
/*    */ 
/*    */   
/*    */   public final int minRadiusForWind;
/*    */   
/*    */   public final float minBluntnessForWind;
/*    */ 
/*    */   
/*    */   public LargeDripstoneConfiguration(int floorToCeilingSearchRange, net.minecraft.util.valueproviders.IntProvider columnRadius, FloatProvider heightScale, float maxColumnRadiusToCaveHeightRatio, FloatProvider stalactiteBluntness, FloatProvider stalagmiteBluntness, FloatProvider windSpeed, int minRadiusForWind, float minBluntnessForWind) {
/* 44 */     this.floorToCeilingSearchRange = floorToCeilingSearchRange;
/* 45 */     this.columnRadius = columnRadius;
/* 46 */     this.heightScale = heightScale;
/* 47 */     this.maxColumnRadiusToCaveHeightRatio = maxColumnRadiusToCaveHeightRatio;
/* 48 */     this.stalactiteBluntness = stalactiteBluntness;
/* 49 */     this.stalagmiteBluntness = stalagmiteBluntness;
/* 50 */     this.windSpeed = windSpeed;
/* 51 */     this.minRadiusForWind = minRadiusForWind;
/* 52 */     this.minBluntnessForWind = minBluntnessForWind;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/LargeDripstoneConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */