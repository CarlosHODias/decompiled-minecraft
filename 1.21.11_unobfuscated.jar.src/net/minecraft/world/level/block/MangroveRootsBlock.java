/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class MangroveRootsBlock extends Block implements SimpleWaterloggedBlock {
/* 19 */   public static final MapCodec<MangroveRootsBlock> CODEC = simpleCodec(MangroveRootsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<MangroveRootsBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/*    */   protected MangroveRootsBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, false));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/* 36 */     return (neighborState.is(Blocks.MANGROVE_ROOTS) && direction.getAxis() == Direction.Axis.Y);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 41 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 42 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 43 */     return (BlockState)super.getStateForPlacement(context).setValue((Property)WATERLOGGED, isWaterSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 48 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 49 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 52 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 57 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 58 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 61 */     return super.getFluidState(state);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 66 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MangroveRootsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */