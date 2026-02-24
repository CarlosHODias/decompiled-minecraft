/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleStateProvider extends BlockStateProvider {
/*    */   public static final com.mojang.serialization.MapCodec<SimpleStateProvider> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, p -> p.state);
/*    */   }
/*    */   private final BlockState state;
/*    */   
/*    */   protected SimpleStateProvider(BlockState state) {
/* 14 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 19 */     return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(net.minecraft.util.RandomSource random, net.minecraft.core.BlockPos pos) {
/* 24 */     return this.state;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */