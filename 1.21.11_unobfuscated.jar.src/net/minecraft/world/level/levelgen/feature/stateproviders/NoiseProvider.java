/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class NoiseProvider extends NoiseBasedStateProvider {
/*    */   public static final MapCodec<NoiseProvider> CODEC;
/*    */   protected final List<BlockState> states;
/*    */   
/*    */   protected static <P extends NoiseProvider> Products.P4<RecordCodecBuilder.Mu<P>, Long, NormalNoise.NoiseParameters, Float, List<BlockState>> noiseProviderCodec(RecordCodecBuilder.Instance<P> instance) {
/* 22 */     return noiseCodec((RecordCodecBuilder.Instance)instance).and(
/* 23 */         (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("states").forGetter(p -> p.states));
/*    */   }
/*    */   
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.mapCodec(i -> noiseProviderCodec(i).apply((Applicative)i, NoiseProvider::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public NoiseProvider(long seed, NormalNoise.NoiseParameters parameters, float scale, List<BlockState> states) {
/* 32 */     super(seed, parameters, scale);
/* 33 */     this.states = states;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 38 */     return BlockStateProviderType.NOISE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, BlockPos pos) {
/* 43 */     return getRandomState(this.states, pos, this.scale);
/*    */   }
/*    */   
/*    */   protected BlockState getRandomState(List<BlockState> states, BlockPos pos, double scale) {
/* 47 */     double noiseValue = getNoiseValue(pos, scale);
/* 48 */     return getRandomState(states, noiseValue);
/*    */   }
/*    */   
/*    */   protected BlockState getRandomState(List<BlockState> states, double noiseValue) {
/* 52 */     double placementValue = Mth.clamp((1.0D + noiseValue) / 2.0D, 0.0D, 0.9999D);
/* 53 */     return states.get((int)(placementValue * states.size()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/stateproviders/NoiseProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */