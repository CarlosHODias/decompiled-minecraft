/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultifaceBlock
/*     */   extends Block
/*     */   implements SimpleWaterloggedBlock
/*     */ {
/*  39 */   public static final MapCodec<MultifaceBlock> CODEC = simpleCodec(MultifaceBlock::new);
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends MultifaceBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */   
/*  46 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  48 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  50 */   protected static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final boolean canRotate;
/*     */   private final boolean canMirrorX;
/*     */   private final boolean canMirrorZ;
/*     */   
/*     */   public MultifaceBlock(BlockBehaviour.Properties properties) {
/*  59 */     super(properties);
/*  60 */     registerDefaultState(getDefaultMultifaceState(this.stateDefinition));
/*  61 */     this.shapes = makeShapes();
/*     */     
/*  63 */     this.canRotate = Direction.Plane.HORIZONTAL.stream().allMatch(this::isFaceSupported);
/*  64 */     this.canMirrorX = (Direction.Plane.HORIZONTAL.stream().filter((Predicate)Direction.Axis.X).filter(this::isFaceSupported).count() % 2L == 0L);
/*  65 */     this.canMirrorZ = (Direction.Plane.HORIZONTAL.stream().filter((Predicate)Direction.Axis.Z).filter(this::isFaceSupported).count() % 2L == 0L);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  69 */     Map<Direction, VoxelShape> shapes = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  71 */     return getShapeForEachState(state -> { VoxelShape shape = Shapes.empty(); for (Direction direction : DIRECTIONS) { if (hasFace(state, direction)) shape = Shapes.or(shape, (VoxelShape)shapes.get(direction));  }  return shape.isEmpty() ? Shapes.block() : shape; }, (Property<?>[])new Property[] { (Property)WATERLOGGED });
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
/*     */   public static Set<Direction> availableFaces(BlockState state) {
/*  83 */     if (!(state.getBlock() instanceof MultifaceBlock)) {
/*  84 */       return Set.of();
/*     */     }
/*  86 */     Set<Direction> faces = EnumSet.noneOf(Direction.class);
/*  87 */     for (Direction direction : Direction.values()) {
/*  88 */       if (hasFace(state, direction)) {
/*  89 */         faces.add(direction);
/*     */       }
/*     */     } 
/*  92 */     return faces;
/*     */   }
/*     */   
/*     */   public static Set<Direction> unpack(byte data) {
/*  96 */     Set<Direction> presentDirections = EnumSet.noneOf(Direction.class);
/*  97 */     for (Direction direction : Direction.values()) {
/*  98 */       if ((data & (byte)(1 << direction.ordinal())) > 0) {
/*  99 */         presentDirections.add(direction);
/*     */       }
/*     */     } 
/* 102 */     return presentDirections;
/*     */   }
/*     */   
/*     */   public static byte pack(Collection<Direction> directions) {
/* 106 */     byte code = 0;
/* 107 */     for (Direction direction : directions) {
/* 108 */       code = (byte)(code | 1 << direction.ordinal());
/*     */     }
/* 110 */     return code;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isFaceSupported(Direction faceDirection) {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 120 */     for (Direction direction : DIRECTIONS) {
/* 121 */       if (isFaceSupported(direction)) {
/* 122 */         builder.add(new Property[] { (Property)getFaceProperty(direction) });
/*     */       }
/*     */     } 
/* 125 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 133 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 134 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 137 */     if (!hasAnyFace(state)) {
/* 138 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 141 */     if (!hasFace(state, directionToNeighbour) || canAttachTo((BlockGetter)level, directionToNeighbour, neighbourPos, neighbourState)) {
/* 142 */       return state;
/*     */     }
/* 144 */     return removeFace(state, getFaceProperty(directionToNeighbour));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 149 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 150 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 152 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 157 */     return this.shapes.apply(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*     */     boolean hasAtLeastOneFace = false;
/* 163 */     for (Direction directionToNeighbour : DIRECTIONS) {
/* 164 */       if (hasFace(state, directionToNeighbour)) {
/*     */ 
/*     */         
/* 167 */         if (!canAttachTo((BlockGetter)level, pos, directionToNeighbour)) {
/* 168 */           return false;
/*     */         }
/* 170 */         hasAtLeastOneFace = true;
/*     */       } 
/* 172 */     }  return hasAtLeastOneFace;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 177 */     return (!context.getItemInHand().is(asItem()) || hasAnyVacantFace(state));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 182 */     Level level = context.getLevel();
/* 183 */     BlockPos placePos = context.getClickedPos();
/* 184 */     BlockState oldState = level.getBlockState(placePos);
/* 185 */     return Arrays.<Direction>stream(context.getNearestLookingDirections())
/* 186 */       .map(direction -> getStateForPlacement(oldState, (BlockGetter)oldState, level, placePos))
/* 187 */       .filter(Objects::nonNull)
/* 188 */       .findFirst()
/* 189 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public boolean isValidStateForPlacement(BlockGetter level, BlockState oldState, BlockPos placementPos, Direction placementDirection) {
/* 193 */     if (!isFaceSupported(placementDirection) || (oldState.is(this) && hasFace(oldState, placementDirection))) {
/* 194 */       return false;
/*     */     }
/* 196 */     BlockPos neighbourPos = placementPos.relative(placementDirection);
/* 197 */     return canAttachTo(level, placementDirection, neighbourPos, level.getBlockState(neighbourPos));
/*     */   }
/*     */   public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) {
/*     */     BlockState newState;
/* 201 */     if (!isValidStateForPlacement(level, oldState, placementPos, placementDirection)) {
/* 202 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 206 */     if (oldState.is(this)) {
/*     */       
/* 208 */       newState = oldState;
/* 209 */     } else if (oldState.getFluidState().isSourceOfType((Fluid)Fluids.WATER)) {
/* 210 */       newState = (BlockState)defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, true);
/*     */     } else {
/* 212 */       newState = defaultBlockState();
/*     */     } 
/*     */     
/* 215 */     return (BlockState)newState.setValue((Property)getFaceProperty(placementDirection), true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 220 */     if (!this.canRotate) {
/* 221 */       return state;
/*     */     }
/*     */     
/* 224 */     Objects.requireNonNull(rotation); return mapDirections(state, rotation::rotate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 229 */     if (mirror == Mirror.FRONT_BACK && !this.canMirrorX) {
/* 230 */       return state;
/*     */     }
/* 232 */     if (mirror == Mirror.LEFT_RIGHT && !this.canMirrorZ) {
/* 233 */       return state;
/*     */     }
/*     */     
/* 236 */     Objects.requireNonNull(mirror); return mapDirections(state, mirror::mirror);
/*     */   }
/*     */   
/*     */   private BlockState mapDirections(BlockState state, Function<Direction, Direction> mapping) {
/* 240 */     BlockState newState = state;
/* 241 */     for (Direction direction : DIRECTIONS) {
/* 242 */       if (isFaceSupported(direction)) {
/* 243 */         newState = (BlockState)newState.setValue((Property)getFaceProperty(mapping.apply(direction)), state.getValue((Property)getFaceProperty(direction)));
/*     */       }
/*     */     } 
/* 246 */     return newState;
/*     */   }
/*     */   
/*     */   public static boolean hasFace(BlockState state, Direction faceDirection) {
/* 250 */     BooleanProperty property = getFaceProperty(faceDirection);
/* 251 */     return (Boolean)state.getValueOrElse((Property)property, false);
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter level, BlockPos pos, Direction directionTowardsNeighbour) {
/* 255 */     BlockPos neighbourPos = pos.relative(directionTowardsNeighbour);
/* 256 */     BlockState blockState = level.getBlockState(neighbourPos);
/* 257 */     return canAttachTo(level, directionTowardsNeighbour, neighbourPos, blockState);
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter level, Direction directionTowardsNeighbour, BlockPos neighbourPos, BlockState neighbourState) {
/* 261 */     return (Block.isFaceFull(neighbourState.getBlockSupportShape(level, neighbourPos), directionTowardsNeighbour.getOpposite()) || 
/* 262 */       Block.isFaceFull(neighbourState.getCollisionShape(level, neighbourPos), directionTowardsNeighbour.getOpposite()));
/*     */   }
/*     */   
/*     */   private static BlockState removeFace(BlockState state, BooleanProperty property) {
/* 266 */     BlockState newState = (BlockState)state.setValue((Property)property, false);
/* 267 */     if (hasAnyFace(newState)) {
/* 268 */       return newState;
/*     */     }
/*     */     
/* 271 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public static BooleanProperty getFaceProperty(Direction faceDirection) {
/* 275 */     return PROPERTY_BY_DIRECTION.get(faceDirection);
/*     */   }
/*     */   
/*     */   private static BlockState getDefaultMultifaceState(StateDefinition<Block, BlockState> stateDefinition) {
/* 279 */     BlockState state = (BlockState)((BlockState)stateDefinition.any()).setValue((Property)WATERLOGGED, false);
/* 280 */     for (BooleanProperty faceProperty : PROPERTY_BY_DIRECTION.values()) {
/* 281 */       state = (BlockState)state.trySetValue((Property)faceProperty, false);
/*     */     }
/* 283 */     return state;
/*     */   }
/*     */   
/*     */   protected static boolean hasAnyFace(BlockState state) {
/* 287 */     for (Direction direction : DIRECTIONS) {
/* 288 */       if (hasFace(state, direction)) {
/* 289 */         return true;
/*     */       }
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean hasAnyVacantFace(BlockState state) {
/* 296 */     for (Direction direction : DIRECTIONS) {
/* 297 */       if (!hasFace(state, direction)) {
/* 298 */         return true;
/*     */       }
/*     */     } 
/* 301 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MultifaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */