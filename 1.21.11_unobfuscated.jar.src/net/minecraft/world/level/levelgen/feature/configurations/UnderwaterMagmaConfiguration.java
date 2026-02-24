/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public class UnderwaterMagmaConfiguration implements FeatureConfiguration {
/*    */   static {
/*  7 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(0, 512).fieldOf("floor_search_range").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("placement_radius_around_floor").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("placement_probability_per_valid_position").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, UnderwaterMagmaConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<UnderwaterMagmaConfiguration> CODEC;
/*    */   
/*    */   public final int floorSearchRange;
/*    */   public final int placementRadiusAroundFloor;
/*    */   public final float placementProbabilityPerValidPosition;
/*    */   
/*    */   public UnderwaterMagmaConfiguration(int floorSearchRange, int placementRadiusAroundFloor, float placementProbabilityPerValidPosition) {
/* 18 */     this.floorSearchRange = floorSearchRange;
/* 19 */     this.placementRadiusAroundFloor = placementRadiusAroundFloor;
/* 20 */     this.placementProbabilityPerValidPosition = placementProbabilityPerValidPosition;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/UnderwaterMagmaConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */