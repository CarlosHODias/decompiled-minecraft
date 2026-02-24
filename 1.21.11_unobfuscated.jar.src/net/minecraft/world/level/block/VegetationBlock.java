/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ public abstract class VegetationBlock extends Block {
/*    */   protected VegetationBlock(BlockBehaviour.Properties properties) {
/* 16 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends VegetationBlock> codec();
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 23 */     return (state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 28 */     if (!state.canSurvive(level, pos)) {
/* 29 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 31 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 36 */     BlockPos below = pos.below();
/* 37 */     return mayPlaceOn(level.getBlockState(below), (BlockGetter)level, below);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean propagatesSkylightDown(BlockState state) {
/* 42 */     return state.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 47 */     if (type == PathComputationType.AIR && !this.hasCollision) {
/* 48 */       return true;
/*    */     }
/* 50 */     return super.isPathfindable(state, type);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/VegetationBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */