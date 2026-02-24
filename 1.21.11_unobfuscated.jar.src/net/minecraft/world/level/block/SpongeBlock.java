/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class SpongeBlock extends Block {
/* 17 */   public static final MapCodec<SpongeBlock> CODEC = simpleCodec(SpongeBlock::new); public static final int MAX_DEPTH = 6;
/*    */   public static final int MAX_COUNT = 64;
/*    */   
/*    */   public MapCodec<SpongeBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   private static final Direction[] ALL_DIRECTIONS = Direction.values();
/*    */   
/*    */   protected SpongeBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 35 */     if (oldState.is(state.getBlock())) {
/*    */       return;
/*    */     }
/* 38 */     tryAbsorbWater(level, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 43 */     tryAbsorbWater(level, pos);
/* 44 */     super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
/*    */   }
/*    */   
/*    */   protected void tryAbsorbWater(Level level, BlockPos pos) {
/* 48 */     if (removeWaterBreadthFirstSearch(level, pos)) {
/*    */       
/* 50 */       level.setBlock(pos, Blocks.WET_SPONGE.defaultBlockState(), 2);
/* 51 */       level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SPONGE_ABSORB, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean removeWaterBreadthFirstSearch(Level level, BlockPos startPos) {
/* 58 */     return (BlockPos.breadthFirstTraversal(startPos, 6, 65, (pos, consumer) -> { for (Direction direction : ALL_DIRECTIONS) consumer.accept(pos.relative(direction));  }, pos -> { if (pos.equals(startPos)) return BlockPos.TraversalNodeStatus.ACCEPT;  BlockState state = level.getBlockState(pos); FluidState fluidState = level.getFluidState(pos); if (!fluidState.is(FluidTags.WATER)) return BlockPos.TraversalNodeStatus.SKIP;  Block patt0$temp = state.getBlock(); if (patt0$temp instanceof BucketPickup) { BucketPickup bucketPickup = (BucketPickup)patt0$temp; if (!bucketPickup.pickupBlock(null, (LevelAccessor)level, pos, state).isEmpty()) return BlockPos.TraversalNodeStatus.ACCEPT;  }  if (state.getBlock() instanceof LiquidBlock) { level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3); } else if (state.is(Blocks.KELP) || state.is(Blocks.KELP_PLANT) || state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS)) { BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null; dropResources(state, (LevelAccessor)level, pos, blockEntity); level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3); } else { return BlockPos.TraversalNodeStatus.SKIP; }  return BlockPos.TraversalNodeStatus.ACCEPT; }) > 1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SpongeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */