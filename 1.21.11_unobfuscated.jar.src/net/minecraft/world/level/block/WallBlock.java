/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WallSide;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallBlock extends Block implements SimpleWaterloggedBlock {
/*  33 */   public static final MapCodec<WallBlock> CODEC = simpleCodec(WallBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<WallBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/*  41 */   public static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
/*  42 */   public static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
/*  43 */   public static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
/*  44 */   public static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
/*  45 */   public static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = (Map<Direction, EnumProperty<WallSide>>)com.google.common.collect.ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> collisionShapes;
/*  56 */   private static final VoxelShape TEST_SHAPE_POST = Block.column(2.0D, 0.0D, 16.0D);
/*  57 */   private static final Map<Direction, VoxelShape> TEST_SHAPES_WALL = Shapes.rotateHorizontal(Block.boxZ(2.0D, 16.0D, 0.0D, 9.0D));
/*     */   
/*     */   public WallBlock(BlockBehaviour.Properties properties) {
/*  60 */     super(properties);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)UP, true)).setValue((Property)NORTH, (Comparable)WallSide.NONE)).setValue((Property)EAST, (Comparable)WallSide.NONE)).setValue((Property)SOUTH, (Comparable)WallSide.NONE)).setValue((Property)WEST, (Comparable)WallSide.NONE)).setValue((Property)WATERLOGGED, false));
/*     */     
/*  63 */     this.shapes = makeShapes(16.0F, 14.0F);
/*  64 */     this.collisionShapes = makeShapes(24.0F, 24.0F);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes(float postHeight, float wallTop) {
/*  68 */     VoxelShape post = Block.column(8.0D, 0.0D, postHeight);
/*  69 */     int width = 6;
/*  70 */     Map<Direction, VoxelShape> low = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, wallTop, 0.0D, 11.0D));
/*  71 */     Map<Direction, VoxelShape> tall = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, postHeight, 0.0D, 11.0D));
/*     */     
/*  73 */     return getShapeForEachState(state -> { VoxelShape shape = (Boolean)state.getValue((Property)UP) ? post : Shapes.empty(); for (Map.Entry<Direction, EnumProperty<WallSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) { switch ((WallSide)state.getValue((Property)entry.getValue())) { default: throw new MatchException(null, null);case NONE: case LOW: case TALL: break; }  shape = Shapes.or(shape, (VoxelShape)tall.get(entry.getKey())); }  return shape; }, (Property<?>[])new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  89 */     return this.shapes.apply(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  94 */     return this.collisionShapes.apply(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   private boolean connectsTo(BlockState state, boolean faceSolid, Direction direction) {
/* 103 */     Block block = state.getBlock();
/*     */     
/* 105 */     boolean connectedFenceGate = (block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction));
/* 106 */     return (state.is(BlockTags.WALLS) || (!isExceptionForConnection(state) && faceSolid) || block instanceof IronBarsBlock || connectedFenceGate);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 111 */     Level level = context.getLevel();
/* 112 */     BlockPos pos = context.getClickedPos();
/* 113 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 115 */     BlockPos northPos = pos.north();
/* 116 */     BlockPos eastPos = pos.east();
/* 117 */     BlockPos southPos = pos.south();
/* 118 */     BlockPos westPos = pos.west();
/* 119 */     BlockPos topPos = pos.above();
/*     */     
/* 121 */     BlockState northState = level.getBlockState(northPos);
/* 122 */     BlockState eastState = level.getBlockState(eastPos);
/* 123 */     BlockState southState = level.getBlockState(southPos);
/* 124 */     BlockState westState = level.getBlockState(westPos);
/* 125 */     BlockState topState = level.getBlockState(topPos);
/*     */     
/* 127 */     boolean north = connectsTo(northState, northState.isFaceSturdy((BlockGetter)level, northPos, Direction.SOUTH), Direction.SOUTH);
/* 128 */     boolean east = connectsTo(eastState, eastState.isFaceSturdy((BlockGetter)level, eastPos, Direction.WEST), Direction.WEST);
/* 129 */     boolean south = connectsTo(southState, southState.isFaceSturdy((BlockGetter)level, southPos, Direction.NORTH), Direction.NORTH);
/* 130 */     boolean west = connectsTo(westState, westState.isFaceSturdy((BlockGetter)level, westPos, Direction.EAST), Direction.EAST);
/*     */     
/* 132 */     BlockState state = (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/* 133 */     return updateShape((LevelReader)level, state, topPos, topState, north, east, south, west);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 138 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 139 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 142 */     if (directionToNeighbour == Direction.DOWN) {
/* 143 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */     }
/*     */     
/* 146 */     if (directionToNeighbour == Direction.UP) {
/* 147 */       return topUpdate(level, state, neighbourPos, neighbourState);
/*     */     }
/*     */     
/* 150 */     return sideUpdate(level, pos, state, neighbourPos, neighbourState, directionToNeighbour);
/*     */   }
/*     */   
/*     */   private static boolean isConnected(BlockState state, Property<WallSide> northWall) {
/* 154 */     return (state.getValue(northWall) != WallSide.NONE);
/*     */   }
/*     */   
/*     */   private static boolean isCovered(VoxelShape aboveShape, VoxelShape testShape) {
/* 158 */     return !Shapes.joinIsNotEmpty(testShape, aboveShape, BooleanOp.ONLY_FIRST);
/*     */   }
/*     */   
/*     */   private BlockState topUpdate(LevelReader level, BlockState state, BlockPos topPos, BlockState topNeighbour) {
/* 162 */     boolean north = isConnected(state, (Property<WallSide>)NORTH);
/* 163 */     boolean east = isConnected(state, (Property<WallSide>)EAST);
/* 164 */     boolean south = isConnected(state, (Property<WallSide>)SOUTH);
/* 165 */     boolean west = isConnected(state, (Property<WallSide>)WEST);
/*     */     
/* 167 */     return updateShape(level, state, topPos, topNeighbour, north, east, south, west);
/*     */   }
/*     */   
/*     */   private BlockState sideUpdate(LevelReader level, BlockPos pos, BlockState state, BlockPos neighbourPos, BlockState neighbour, Direction direction) {
/* 171 */     Direction opposite = direction.getOpposite();
/* 172 */     boolean isNorthConnected = (direction == Direction.NORTH) ? connectsTo(neighbour, neighbour.isFaceSturdy((BlockGetter)level, neighbourPos, opposite), opposite) : isConnected(state, (Property<WallSide>)NORTH);
/* 173 */     boolean isEastConnected = (direction == Direction.EAST) ? connectsTo(neighbour, neighbour.isFaceSturdy((BlockGetter)level, neighbourPos, opposite), opposite) : isConnected(state, (Property<WallSide>)EAST);
/* 174 */     boolean isSouthConnected = (direction == Direction.SOUTH) ? connectsTo(neighbour, neighbour.isFaceSturdy((BlockGetter)level, neighbourPos, opposite), opposite) : isConnected(state, (Property<WallSide>)SOUTH);
/* 175 */     boolean isWestConnected = (direction == Direction.WEST) ? connectsTo(neighbour, neighbour.isFaceSturdy((BlockGetter)level, neighbourPos, opposite), opposite) : isConnected(state, (Property<WallSide>)WEST);
/*     */     
/* 177 */     BlockPos above = pos.above();
/* 178 */     BlockState aboveState = level.getBlockState(above);
/* 179 */     return updateShape(level, state, above, aboveState, isNorthConnected, isEastConnected, isSouthConnected, isWestConnected);
/*     */   }
/*     */   
/*     */   private BlockState updateShape(LevelReader level, BlockState state, BlockPos topPos, BlockState topNeighbour, boolean north, boolean east, boolean south, boolean west) {
/* 183 */     VoxelShape aboveShape = topNeighbour.getCollisionShape((BlockGetter)level, topPos).getFaceShape(Direction.DOWN);
/* 184 */     BlockState sidesUpdatedState = updateSides(state, north, east, south, west, aboveShape);
/*     */     
/* 186 */     return (BlockState)sidesUpdatedState.setValue((Property)UP, shouldRaisePost(sidesUpdatedState, topNeighbour, aboveShape));
/*     */   }
/*     */   
/*     */   private boolean shouldRaisePost(BlockState state, BlockState topNeighbour, VoxelShape aboveShape) {
/* 190 */     boolean topNeighbourHasPost = (topNeighbour.getBlock() instanceof WallBlock && (Boolean)topNeighbour.getValue((Property)UP));
/* 191 */     if (topNeighbourHasPost) {
/* 192 */       return true;
/*     */     }
/*     */     
/* 195 */     WallSide northWall = (WallSide)state.getValue((Property)NORTH);
/* 196 */     WallSide southWall = (WallSide)state.getValue((Property)SOUTH);
/* 197 */     WallSide eastWall = (WallSide)state.getValue((Property)EAST);
/* 198 */     WallSide westWall = (WallSide)state.getValue((Property)WEST);
/*     */     
/* 200 */     boolean southNone = (southWall == WallSide.NONE);
/* 201 */     boolean westNone = (westWall == WallSide.NONE);
/* 202 */     boolean eastNone = (eastWall == WallSide.NONE);
/* 203 */     boolean northNone = (northWall == WallSide.NONE);
/*     */     
/* 205 */     boolean hasCorner = ((northNone && southNone && westNone && eastNone) || northNone != southNone || westNone != eastNone);
/*     */ 
/*     */     
/* 208 */     if (hasCorner) {
/* 209 */       return true;
/*     */     }
/*     */     
/* 212 */     boolean hasHighWall = ((northWall == WallSide.TALL && southWall == WallSide.TALL) || (eastWall == WallSide.TALL && westWall == WallSide.TALL));
/*     */     
/* 214 */     if (hasHighWall) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     return (topNeighbour.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(aboveShape, TEST_SHAPE_POST));
/*     */   }
/*     */   
/*     */   private BlockState updateSides(BlockState state, boolean northConnection, boolean eastConnection, boolean southConnection, boolean westConnection, VoxelShape aboveShape) {
/* 222 */     return (BlockState)((BlockState)((BlockState)((BlockState)
/* 223 */       state.setValue((Property)NORTH, (Comparable)makeWallState(northConnection, aboveShape, TEST_SHAPES_WALL.get(Direction.NORTH))))
/* 224 */       .setValue((Property)EAST, (Comparable)makeWallState(eastConnection, aboveShape, TEST_SHAPES_WALL.get(Direction.EAST))))
/* 225 */       .setValue((Property)SOUTH, (Comparable)makeWallState(southConnection, aboveShape, TEST_SHAPES_WALL.get(Direction.SOUTH))))
/* 226 */       .setValue((Property)WEST, (Comparable)makeWallState(westConnection, aboveShape, TEST_SHAPES_WALL.get(Direction.WEST)));
/*     */   }
/*     */   
/*     */   private WallSide makeWallState(boolean connectsToSide, VoxelShape aboveShape, VoxelShape testShape) {
/* 230 */     if (connectsToSide) {
/* 231 */       if (isCovered(aboveShape, testShape)) {
/* 232 */         return WallSide.TALL;
/*     */       }
/* 234 */       return WallSide.LOW;
/*     */     } 
/*     */     
/* 237 */     return WallSide.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 243 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 244 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 246 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/* 251 */     return !((Boolean)state.getValue((Property)WATERLOGGED));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 256 */     builder.add(new Property[] { (Property)UP, (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 261 */     switch (rotation) {
/*     */       case CLOCKWISE_180:
/* 263 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)NORTH, state.getValue((Property)SOUTH))).setValue((Property)EAST, state.getValue((Property)WEST))).setValue((Property)SOUTH, state.getValue((Property)NORTH))).setValue((Property)WEST, state.getValue((Property)EAST));
/*     */       case COUNTERCLOCKWISE_90:
/* 265 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)NORTH, state.getValue((Property)EAST))).setValue((Property)EAST, state.getValue((Property)SOUTH))).setValue((Property)SOUTH, state.getValue((Property)WEST))).setValue((Property)WEST, state.getValue((Property)NORTH));
/*     */       case CLOCKWISE_90:
/* 267 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue((Property)NORTH, state.getValue((Property)WEST))).setValue((Property)EAST, state.getValue((Property)NORTH))).setValue((Property)SOUTH, state.getValue((Property)EAST))).setValue((Property)WEST, state.getValue((Property)SOUTH));
/*     */     } 
/* 269 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 275 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 277 */         return (BlockState)((BlockState)state.setValue((Property)NORTH, state.getValue((Property)SOUTH))).setValue((Property)SOUTH, state.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 279 */         return (BlockState)((BlockState)state.setValue((Property)EAST, state.getValue((Property)WEST))).setValue((Property)WEST, state.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 283 */     return super.mirror(state, mirror);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WallBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */