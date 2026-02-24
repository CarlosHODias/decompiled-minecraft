/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.ConduitBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class ConduitBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/* 29 */   public static final MapCodec<ConduitBlock> CODEC = simpleCodec(ConduitBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<ConduitBlock> codec() {
/* 33 */     return CODEC;
/*    */   }
/*    */   
/* 36 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 38 */   private static final VoxelShape SHAPE = Block.cube(6.0D);
/*    */   
/*    */   public ConduitBlock(BlockBehaviour.Properties properties) {
/* 41 */     super(properties);
/* 42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, true));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 47 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 52 */     return (BlockEntity)new ConduitBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 57 */     return createTickerHelper(type, BlockEntityType.CONDUIT, level.isClientSide() ? ConduitBlockEntity::clientTick : ConduitBlockEntity::serverTick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected FluidState getFluidState(BlockState state) {
/* 62 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 63 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 66 */     return super.getFluidState(state);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 71 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 72 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/*    */     
/* 75 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 80 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 85 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 86 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ConduitBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */