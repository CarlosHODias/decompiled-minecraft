/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*    */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*    */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*    */ 
/*    */ public class GeodeConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0D, 1.0D); public static final Codec<GeodeConfiguration> CODEC;
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)GeodeBlockSettings.CODEC.fieldOf("blocks").forGetter(()), (App)GeodeLayerSettings.CODEC.fieldOf("layers").forGetter(()), (App)GeodeCrackSettings.CODEC.fieldOf("crack").forGetter(()), (App)CHANCE_RANGE.fieldOf("use_potential_placements_chance").orElse(0.35D).forGetter(()), (App)CHANCE_RANGE.fieldOf("use_alternate_layer0_chance").orElse(0.0D).forGetter(()), (App)Codec.BOOL.fieldOf("placements_require_layer0_alternate").orElse(true).forGetter(()), (App)IntProvider.codec(1, 20).fieldOf("outer_wall_distance").orElse(UniformInt.of(4, 5)).forGetter(()), (App)IntProvider.codec(1, 20).fieldOf("distribution_points").orElse(UniformInt.of(3, 4)).forGetter(()), (App)IntProvider.codec(0, 10).fieldOf("point_offset").orElse(UniformInt.of(1, 2)).forGetter(()), (App)Codec.INT.fieldOf("min_gen_offset").orElse(-16).forGetter(()), (App)Codec.INT.fieldOf("max_gen_offset").orElse(16).forGetter(()), (App)CHANCE_RANGE.fieldOf("noise_multiplier").orElse(0.05D).forGetter(()), (App)Codec.INT.fieldOf("invalid_blocks_threshold").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, GeodeConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final GeodeBlockSettings geodeBlockSettings;
/*    */ 
/*    */   
/*    */   public final GeodeLayerSettings geodeLayerSettings;
/*    */ 
/*    */   
/*    */   public final GeodeCrackSettings geodeCrackSettings;
/*    */ 
/*    */   
/*    */   public final double usePotentialPlacementsChance;
/*    */ 
/*    */   
/*    */   public final double useAlternateLayer0Chance;
/*    */ 
/*    */   
/*    */   public final boolean placementsRequireLayer0Alternate;
/*    */ 
/*    */   
/*    */   public final IntProvider outerWallDistance;
/*    */ 
/*    */   
/*    */   public final IntProvider distributionPoints;
/*    */ 
/*    */   
/*    */   public final IntProvider pointOffset;
/*    */   
/*    */   public final int minGenOffset;
/*    */   
/*    */   public final int maxGenOffset;
/*    */   
/*    */   public final double noiseMultiplier;
/*    */   
/*    */   public final int invalidBlocksThreshold;
/*    */ 
/*    */   
/*    */   public GeodeConfiguration(GeodeBlockSettings geodeBlockSettings, GeodeLayerSettings geodeLayerSettings, GeodeCrackSettings geodeCrackSettings, double usePotentialPlacementsChance, double useAlternateLayer0Chance, boolean placementsRequireLayer0Alternate, IntProvider outerWallDistance, IntProvider distributionPoints, IntProvider pointOffset, int minGenOffset, int maxGenOffset, double noiseMultiplier, int invalidBlocksThreshold) {
/* 55 */     this.geodeBlockSettings = geodeBlockSettings;
/* 56 */     this.geodeLayerSettings = geodeLayerSettings;
/* 57 */     this.geodeCrackSettings = geodeCrackSettings;
/* 58 */     this.usePotentialPlacementsChance = usePotentialPlacementsChance;
/* 59 */     this.useAlternateLayer0Chance = useAlternateLayer0Chance;
/* 60 */     this.placementsRequireLayer0Alternate = placementsRequireLayer0Alternate;
/* 61 */     this.outerWallDistance = outerWallDistance;
/* 62 */     this.distributionPoints = distributionPoints;
/* 63 */     this.pointOffset = pointOffset;
/* 64 */     this.minGenOffset = minGenOffset;
/* 65 */     this.maxGenOffset = maxGenOffset;
/* 66 */     this.noiseMultiplier = noiseMultiplier;
/* 67 */     this.invalidBlocksThreshold = invalidBlocksThreshold;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */