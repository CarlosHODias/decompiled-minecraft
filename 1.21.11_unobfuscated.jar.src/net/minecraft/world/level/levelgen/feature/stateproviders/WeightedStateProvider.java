/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeightedStateProvider extends BlockStateProvider {
/*    */   public static final com.mojang.serialization.MapCodec<WeightedStateProvider> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = WeightedList.nonEmptyCodec(BlockState.CODEC).comapFlatMap(WeightedStateProvider::create, p -> p.weightedList).fieldOf("entries");
/*    */   }
/*    */   private final WeightedList<BlockState> weightedList;
/*    */   private static DataResult<WeightedStateProvider> create(WeightedList<BlockState> weightedList) {
/* 15 */     if (weightedList.isEmpty()) {
/* 16 */       return DataResult.error(() -> "WeightedStateProvider with no states");
/*    */     }
/* 18 */     return DataResult.success(new WeightedStateProvider(weightedList));
/*    */   }
/*    */   
/*    */   public WeightedStateProvider(WeightedList<BlockState> weightedList) {
/* 22 */     this.weightedList = weightedList;
/*    */   }
/*    */   
/*    */   public WeightedStateProvider(WeightedList.Builder<BlockState> weightedList) {
/* 26 */     this(weightedList.build());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 31 */     return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, net.minecraft.core.BlockPos pos) {
/* 36 */     return (BlockState)this.weightedList.getRandomOrThrow(random);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */