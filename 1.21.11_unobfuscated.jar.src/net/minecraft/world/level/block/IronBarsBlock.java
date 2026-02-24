/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class IronBarsBlock extends CrossCollisionBlock {
/* 21 */   public static final MapCodec<IronBarsBlock> CODEC = simpleCodec(IronBarsBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends IronBarsBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected IronBarsBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(2.0F, 16.0F, 2.0F, 16.0F, 16.0F, properties);
/* 30 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, false)).setValue((Property)EAST, false)).setValue((Property)SOUTH, false)).setValue((Property)WEST, false)).setValue((Property)WATERLOGGED, false));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 35 */     Level level = context.getLevel();
/* 36 */     BlockPos pos = context.getClickedPos();
/* 37 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*    */     
/* 39 */     BlockPos north = pos.north();
/* 40 */     BlockPos south = pos.south();
/* 41 */     BlockPos west = pos.west();
/* 42 */     BlockPos east = pos.east();
/*    */     
/* 44 */     BlockState northState = level.getBlockState(north);
/* 45 */     BlockState southState = level.getBlockState(south);
/* 46 */     BlockState westState = level.getBlockState(west);
/* 47 */     BlockState eastState = level.getBlockState(east);
/*    */     
/* 49 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 50 */       .setValue((Property)NORTH, attachsTo(northState, northState.isFaceSturdy((BlockGetter)level, north, Direction.SOUTH))))
/* 51 */       .setValue((Property)SOUTH, attachsTo(southState, southState.isFaceSturdy((BlockGetter)level, south, Direction.NORTH))))
/* 52 */       .setValue((Property)WEST, attachsTo(westState, westState.isFaceSturdy((BlockGetter)level, west, Direction.EAST))))
/* 53 */       .setValue((Property)EAST, attachsTo(eastState, eastState.isFaceSturdy((BlockGetter)level, east, Direction.WEST))))
/* 54 */       .setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 60 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 61 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*    */     }
/* 63 */     if (directionToNeighbour.getAxis().isHorizontal()) {
/* 64 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), attachsTo(neighbourState, neighbourState.isFaceSturdy((BlockGetter)level, neighbourPos, directionToNeighbour.getOpposite())));
/*    */     }
/* 66 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 71 */     return net.minecraft.world.phys.shapes.Shapes.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/* 77 */     if (neighborState.is(this) || (neighborState.is(BlockTags.BARS) && state.is(BlockTags.BARS) && neighborState.hasProperty((Property)PROPERTY_BY_DIRECTION.get(direction.getOpposite())))) {
/* 78 */       if (!direction.getAxis().isHorizontal()) {
/* 79 */         return true;
/*    */       }
/* 81 */       if ((Boolean)state.getValue((Property)PROPERTY_BY_DIRECTION.get(direction)) && (Boolean)neighborState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) {
/* 82 */         return true;
/*    */       }
/*    */     } 
/* 85 */     return super.skipRendering(state, neighborState, direction);
/*    */   }
/*    */   
/*    */   public final boolean attachsTo(BlockState state, boolean faceSolid) {
/* 89 */     return ((!isExceptionForConnection(state) && faceSolid) || state.getBlock() instanceof IronBarsBlock || state.is(BlockTags.WALLS));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 94 */     builder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/IronBarsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */