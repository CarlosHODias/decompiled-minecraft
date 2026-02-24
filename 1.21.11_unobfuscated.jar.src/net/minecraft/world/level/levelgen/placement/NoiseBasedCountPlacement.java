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
/*    */ public class NoiseBasedCountPlacement
/*    */   extends RepeatingPlacement {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("noise_to_count_ratio").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_factor").forGetter(()), (App)Codec.DOUBLE.fieldOf("noise_offset").orElse(0.0D).forGetter(())).apply((Applicative)i, NoiseBasedCountPlacement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<NoiseBasedCountPlacement> CODEC;
/*    */   
/*    */   private final int noiseToCountRatio;
/*    */   
/*    */   private final double noiseFactor;
/*    */   
/*    */   private final double noiseOffset;
/*    */   
/*    */   private NoiseBasedCountPlacement(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
/* 29 */     this.noiseToCountRatio = noiseToCountRatio;
/* 30 */     this.noiseFactor = noiseFactor;
/* 31 */     this.noiseOffset = noiseOffset;
/*    */   }
/*    */   
/*    */   public static NoiseBasedCountPlacement of(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
/* 35 */     return new NoiseBasedCountPlacement(noiseToCountRatio, noiseFactor, noiseOffset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 40 */     double flowerNoise = Biome.BIOME_INFO_NOISE.getValue(origin.getX() / this.noiseFactor, origin.getZ() / this.noiseFactor, false);
/* 41 */     return (int)Math.ceil((flowerNoise + this.noiseOffset) * this.noiseToCountRatio);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 46 */     return PlacementModifierType.NOISE_BASED_COUNT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/NoiseBasedCountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */