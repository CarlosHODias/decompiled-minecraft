/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
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
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MossyCarpetBlock extends Block implements BonemealableBlock {
/*  34 */   public static final MapCodec<MossyCarpetBlock> CODEC = simpleCodec(MossyCarpetBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<MossyCarpetBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  41 */   public static final BooleanProperty BASE = BlockStateProperties.BOTTOM;
/*  42 */   public static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
/*  43 */   public static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
/*  44 */   public static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
/*  45 */   public static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
/*     */   
/*  47 */   public static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = (Map<Direction, EnumProperty<WallSide>>)ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */ 
/*     */ 
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MossyCarpetBlock(BlockBehaviour.Properties properties) {
/*  57 */     super(properties);
/*  58 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BASE, true)).setValue((Property)NORTH, (Comparable)WallSide.NONE)).setValue((Property)EAST, (Comparable)WallSide.NONE)).setValue((Property)SOUTH, (Comparable)WallSide.NONE)).setValue((Property)WEST, (Comparable)WallSide.NONE));
/*     */     
/*  60 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   public Function<BlockState, VoxelShape> makeShapes() {
/*  64 */     Map<Direction, VoxelShape> low = Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 10.0D, 0.0D, 1.0D));
/*  65 */     Map<Direction, VoxelShape> tall = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  67 */     return getShapeForEachState(state -> {
/*     */           VoxelShape shape = (Boolean)state.getValue((Property)BASE) ? (VoxelShape)tall.get(Direction.DOWN) : Shapes.empty();
/*     */           for (Map.Entry<Direction, EnumProperty<WallSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*     */             switch ((WallSide)state.getValue((Property)entry.getValue())) {
/*     */               case LOW:
/*     */                 shape = Shapes.or(shape, (VoxelShape)low.get(entry.getKey()));
/*     */               case TALL:
/*     */                 shape = Shapes.or(shape, (VoxelShape)tall.get(entry.getKey()));
/*     */             } 
/*     */           } 
/*     */           return shape.isEmpty() ? Shapes.block() : shape;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  83 */     return this.shapes.apply(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  88 */     return (Boolean)state.getValue((Property)BASE) ? this.shapes.apply(defaultBlockState()) : Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  98 */     BlockState belowState = level.getBlockState(pos.below());
/*     */     
/* 100 */     if ((Boolean)state.getValue((Property)BASE)) {
/* 101 */       return !belowState.isAir();
/*     */     }
/*     */     
/* 104 */     return (belowState.is(this) && (Boolean)belowState.getValue((Property)BASE));
/*     */   }
/*     */   
/*     */   private static boolean hasFaces(BlockState blockState) {
/* 108 */     if ((Boolean)blockState.getValue((Property)BASE)) {
/* 109 */       return true;
/*     */     }
/* 111 */     for (EnumProperty<WallSide> property : PROPERTY_BY_DIRECTION.values()) {
/* 112 */       if (blockState.getValue((Property)property) != WallSide.NONE) {
/* 113 */         return true;
/*     */       }
/*     */     } 
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
/* 120 */     if (direction == Direction.UP) {
/* 121 */       return false;
/*     */     }
/* 123 */     return MultifaceBlock.canAttachTo(level, pos, direction);
/*     */   }
/*     */   
/*     */   private static BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos, boolean createSides) {
/* 127 */     BlockState aboveState = null;
/* 128 */     BlockState belowState = null;
/*     */ 
/*     */     
/* 131 */     createSides |= (Boolean)state.getValue((Property)BASE);
/*     */     
/* 133 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 134 */       EnumProperty<WallSide> property = getPropertyForFace(direction);
/* 135 */       WallSide side = canSupportAtFace(level, pos, direction) ? (createSides ? WallSide.LOW : (WallSide)state.getValue((Property)property)) : WallSide.NONE;
/* 136 */       if (side == WallSide.LOW) {
/* 137 */         if (aboveState == null) {
/* 138 */           aboveState = level.getBlockState(pos.above());
/*     */         }
/* 140 */         if (aboveState.is(Blocks.PALE_MOSS_CARPET) && aboveState.getValue((Property)property) != WallSide.NONE && !((Boolean)aboveState.getValue((Property)BASE))) {
/* 141 */           side = WallSide.TALL;
/*     */         }
/* 143 */         if (!((Boolean)state.getValue((Property)BASE))) {
/* 144 */           if (belowState == null) {
/* 145 */             belowState = level.getBlockState(pos.below());
/*     */           }
/* 147 */           if (belowState.is(Blocks.PALE_MOSS_CARPET) && belowState.getValue((Property)property) == WallSide.NONE) {
/* 148 */             side = WallSide.NONE;
/*     */           }
/*     */         } 
/*     */       } 
/* 152 */       state = (BlockState)state.setValue((Property)property, (Comparable)side);
/*     */     } 
/* 154 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 159 */     return getUpdatedState(defaultBlockState(), (BlockGetter)context.getLevel(), context.getClickedPos(), true);
/*     */   }
/*     */   
/*     */   public static void placeAt(LevelAccessor level, BlockPos pos, RandomSource random, @Block.UpdateFlags int updateType) {
/* 163 */     BlockState simpleCarpetLayer = Blocks.PALE_MOSS_CARPET.defaultBlockState();
/* 164 */     BlockState adjustedCarpetLayer = getUpdatedState(simpleCarpetLayer, (BlockGetter)level, pos, true);
/* 165 */     level.setBlock(pos, adjustedCarpetLayer, updateType);
/* 166 */     Objects.requireNonNull(random); BlockState state = createTopperWithSideChance((BlockGetter)level, pos, random::nextBoolean);
/* 167 */     if (!state.isAir()) {
/* 168 */       level.setBlock(pos.above(), state, updateType);
/* 169 */       BlockState updateBottomCarpet = getUpdatedState(adjustedCarpetLayer, (BlockGetter)level, pos, true);
/* 170 */       level.setBlock(pos, updateBottomCarpet, updateType);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 177 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     RandomSource random = level.getRandom();
/* 182 */     Objects.requireNonNull(random); BlockState topper = createTopperWithSideChance((BlockGetter)level, pos, random::nextBoolean);
/* 183 */     if (!topper.isAir()) {
/* 184 */       level.setBlock(pos.above(), topper, 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private static BlockState createTopperWithSideChance(BlockGetter level, BlockPos pos, BooleanSupplier sideSurvivalTest) {
/* 189 */     BlockPos above = pos.above();
/* 190 */     BlockState abovePreviousState = level.getBlockState(above);
/* 191 */     boolean isMossyCarpetAbove = abovePreviousState.is(Blocks.PALE_MOSS_CARPET);
/* 192 */     if ((isMossyCarpetAbove && (Boolean)abovePreviousState.getValue((Property)BASE)) || (!isMossyCarpetAbove && !abovePreviousState.canBeReplaced())) {
/* 193 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 195 */     BlockState noCarpetBaseState = (BlockState)Blocks.PALE_MOSS_CARPET.defaultBlockState().setValue((Property)BASE, false);
/* 196 */     BlockState aboveState = getUpdatedState(noCarpetBaseState, level, pos.above(), true);
/* 197 */     for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 198 */       EnumProperty<WallSide> property = getPropertyForFace(direction);
/* 199 */       if (aboveState.getValue((Property)property) != WallSide.NONE && !sideSurvivalTest.getAsBoolean()) {
/* 200 */         aboveState = (BlockState)aboveState.setValue((Property)property, (Comparable)WallSide.NONE);
/*     */       }
/*     */     } 
/* 203 */     if (hasFaces(aboveState) && aboveState != abovePreviousState) {
/* 204 */       return aboveState;
/*     */     }
/* 206 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 211 */     if (!state.canSurvive(level, pos)) {
/* 212 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 215 */     BlockState blockState = getUpdatedState(state, (BlockGetter)level, pos, false);
/*     */     
/* 217 */     if (!hasFaces(blockState)) {
/* 218 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 221 */     return blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 226 */     builder.add(new Property[] { (Property)BASE, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 231 */     switch (rotation) { case CLOCKWISE_180: case COUNTERCLOCKWISE_90: case CLOCKWISE_90: default: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 238 */       state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 244 */     switch (mirror) { case LEFT_RIGHT: case FRONT_BACK: default: break; }  return 
/*     */ 
/*     */       
/* 247 */       super.mirror(state, mirror);
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumProperty<WallSide> getPropertyForFace(Direction direction) {
/* 252 */     return PROPERTY_BY_DIRECTION.get(direction);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 257 */     return ((Boolean)state.getValue((Property)BASE) && !createTopperWithSideChance((BlockGetter)level, pos, () -> true).isAir());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 262 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 267 */     BlockState topper = createTopperWithSideChance((BlockGetter)level, pos, () -> true);
/* 268 */     if (!topper.isAir())
/* 269 */       level.setBlock(pos.above(), topper, 3); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MossyCarpetBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */