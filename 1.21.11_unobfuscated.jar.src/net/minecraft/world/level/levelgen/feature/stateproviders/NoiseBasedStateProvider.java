/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public abstract class NoiseBasedStateProvider extends BlockStateProvider {
/*    */   protected static <P extends NoiseBasedStateProvider> Products.P3<RecordCodecBuilder.Mu<P>, Long, NormalNoise.NoiseParameters, Float> noiseCodec(RecordCodecBuilder.Instance<P> instance) {
/* 14 */     return instance.group((App)
/* 15 */         Codec.LONG.fieldOf("seed").forGetter(p -> p.seed), (App)
/* 16 */         NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("noise").forGetter(p -> p.parameters), (App)
/* 17 */         ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(p -> p.scale));
/*    */   }
/*    */ 
/*    */   
/*    */   protected final long seed;
/*    */   protected final NormalNoise.NoiseParameters parameters;
/*    */   protected final float scale;
/*    */   protected final NormalNoise noise;
/*    */   
/*    */   protected NoiseBasedStateProvider(long seed, NormalNoise.NoiseParameters parameters, float scale) {
/* 27 */     this.seed = seed;
/* 28 */     this.parameters = parameters;
/* 29 */     this.scale = scale;
/* 30 */     this.noise = NormalNoise.create((RandomSource)new net.minecraft.world.level.levelgen.WorldgenRandom((RandomSource)new LegacyRandomSource(seed)), parameters);
/*    */   }
/*    */   
/*    */   protected double getNoiseValue(BlockPos pos, double scale) {
/* 34 */     return this.noise.getValue(pos.getX() * scale, pos.getY() * scale, pos.getZ() * scale);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/stateproviders/NoiseBasedStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */