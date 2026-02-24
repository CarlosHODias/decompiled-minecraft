/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class NoiseThresholdCountPlacement
/*    */   extends RepeatingPlacement {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.DOUBLE.fieldOf("noise_level").forGetter(()), (App)Codec.INT.fieldOf("below_noise").forGetter(()), (App)Codec.INT.fieldOf("above_noise").forGetter(())).apply((Applicative)i, NoiseThresholdCountPlacement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<NoiseThresholdCountPlacement> CODEC;
/*    */   
/*    */   private final double noiseLevel;
/*    */   
/*    */   private final int belowNoise;
/*    */   private final int aboveNoise;
/*    */   
/*    */   private NoiseThresholdCountPlacement(double noiseLevel, int belowNoise, int aboveNoise) {
/* 28 */     this.noiseLevel = noiseLevel;
/* 29 */     this.belowNoise = belowNoise;
/* 30 */     this.aboveNoise = aboveNoise;
/*    */   }
/*    */   
/*    */   public static NoiseThresholdCountPlacement of(double noiseLevel, int belowNoise, int aboveNoise) {
/* 34 */     return new NoiseThresholdCountPlacement(noiseLevel, belowNoise, aboveNoise);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 40 */     double flowerNoise = Biome.BIOME_INFO_NOISE.getValue(origin.getX() / 200.0D, origin.getZ() / 200.0D, false);
/* 41 */     return (flowerNoise < this.noiseLevel) ? this.belowNoise : this.aboveNoise;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 46 */     return PlacementModifierType.NOISE_THRESHOLD_COUNT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/NoiseThresholdCountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */