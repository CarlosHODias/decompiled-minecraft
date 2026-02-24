/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ChorusPlantBlock extends PipeBlock {
/*  17 */   public static final MapCodec<ChorusPlantBlock> CODEC = simpleCodec(ChorusPlantBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<ChorusPlantBlock> codec() {
/*  21 */     return CODEC;
/*     */   }
/*     */   
/*     */   protected ChorusPlantBlock(BlockBehaviour.Properties properties) {
/*  25 */     super(10.0F, properties);
/*     */     
/*  27 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, false)).setValue((Property)EAST, false)).setValue((Property)SOUTH, false)).setValue((Property)WEST, false)).setValue((Property)UP, false)).setValue((Property)DOWN, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  32 */     return getStateWithConnections((BlockGetter)context.getLevel(), context.getClickedPos(), defaultBlockState());
/*     */   }
/*     */   
/*     */   public static BlockState getStateWithConnections(BlockGetter level, BlockPos pos, BlockState defaultState) {
/*  36 */     BlockState down = level.getBlockState(pos.below());
/*  37 */     BlockState up = level.getBlockState(pos.above());
/*  38 */     BlockState north = level.getBlockState(pos.north());
/*  39 */     BlockState east = level.getBlockState(pos.east());
/*  40 */     BlockState south = level.getBlockState(pos.south());
/*  41 */     BlockState west = level.getBlockState(pos.west());
/*     */     
/*  43 */     Block block = defaultState.getBlock();
/*  44 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)
/*  45 */       defaultState.trySetValue((Property)DOWN, (down.is(block) || down.is(Blocks.CHORUS_FLOWER) || down.is(Blocks.END_STONE))))
/*  46 */       .trySetValue((Property)UP, (up.is(block) || up.is(Blocks.CHORUS_FLOWER))))
/*  47 */       .trySetValue((Property)NORTH, (north.is(block) || north.is(Blocks.CHORUS_FLOWER))))
/*  48 */       .trySetValue((Property)EAST, (east.is(block) || east.is(Blocks.CHORUS_FLOWER))))
/*  49 */       .trySetValue((Property)SOUTH, (south.is(block) || south.is(Blocks.CHORUS_FLOWER))))
/*  50 */       .trySetValue((Property)WEST, (west.is(block) || west.is(Blocks.CHORUS_FLOWER)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  56 */     if (!state.canSurvive(level, pos)) {
/*  57 */       ticks.scheduleTick(pos, this, 1);
/*  58 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */     } 
/*     */     
/*  61 */     boolean connect = (neighbourState.is(this) || neighbourState.is(Blocks.CHORUS_FLOWER) || (directionToNeighbour == Direction.DOWN && neighbourState.is(Blocks.END_STONE)));
/*     */     
/*  63 */     return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), connect);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  68 */     if (!state.canSurvive((LevelReader)level, pos)) {
/*  69 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  78 */     BlockState belowState = level.getBlockState(pos.below());
/*  79 */     boolean blockAboveOrBelow = (!level.getBlockState(pos.above()).isAir() && !belowState.isAir());
/*     */     
/*  81 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/*  82 */       BlockPos neighborPos = pos.relative(direction);
/*  83 */       BlockState neighborState = level.getBlockState(neighborPos);
/*  84 */       if (neighborState.is(this)) {
/*  85 */         if (blockAboveOrBelow) {
/*  86 */           return false;
/*     */         }
/*  88 */         BlockState below = level.getBlockState(neighborPos.below());
/*  89 */         if (below.is(this) || below.is(Blocks.END_STONE)) {
/*  90 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  94 */     return (belowState.is(this) || belowState.is(Blocks.END_STONE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  99 */     builder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP, (Property)DOWN });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 104 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ChorusPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */