/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RotatedBlockProvider extends BlockStateProvider {
/*    */   public static final com.mojang.serialization.MapCodec<RotatedBlockProvider> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState).xmap(RotatedBlockProvider::new, p -> p.block);
/*    */   }
/*    */   private final Block block;
/*    */   
/*    */   public RotatedBlockProvider(Block block) {
/* 19 */     this.block = block;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 24 */     return BlockStateProviderType.ROTATED_BLOCK_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, net.minecraft.core.BlockPos pos) {
/* 29 */     Direction.Axis randomAxis = Direction.Axis.getRandom(random);
/* 30 */     return (BlockState)this.block.defaultBlockState().trySetValue((net.minecraft.world.level.block.state.properties.Property)RotatedPillarBlock.AXIS, (Comparable)randomAxis);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/stateproviders/RotatedBlockProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */