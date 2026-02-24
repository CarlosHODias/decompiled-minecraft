/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StairBlock extends Block implements SimpleWaterloggedBlock {
/*     */   public static final com.mojang.serialization.MapCodec<StairBlock> CODEC;
/*     */   
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockState.CODEC.fieldOf("base_state").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, StairBlock::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.mojang.serialization.MapCodec<? extends StairBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  41 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  42 */   public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
/*  43 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  45 */   private static final VoxelShape SHAPE_OUTER = Shapes.or(
/*  46 */       Block.column(16.0D, 0.0D, 8.0D), 
/*  47 */       Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D));
/*     */   
/*  49 */   private static final VoxelShape SHAPE_STRAIGHT = Shapes.or(SHAPE_OUTER, Shapes.rotate(SHAPE_OUTER, OctahedralGroup.BLOCK_ROT_Y_90));
/*  50 */   private static final VoxelShape SHAPE_INNER = Shapes.or(SHAPE_STRAIGHT, Shapes.rotate(SHAPE_STRAIGHT, OctahedralGroup.BLOCK_ROT_Y_90));
/*     */   
/*  52 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_OUTER = Shapes.rotateHorizontal(SHAPE_OUTER);
/*  53 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_STRAIGHT = Shapes.rotateHorizontal(SHAPE_STRAIGHT);
/*  54 */   private static final Map<Direction, VoxelShape> SHAPE_BOTTOM_INNER = Shapes.rotateHorizontal(SHAPE_INNER);
/*     */   
/*  56 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_OUTER = Shapes.rotateHorizontal(SHAPE_OUTER, OctahedralGroup.INVERT_Y);
/*  57 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_STRAIGHT = Shapes.rotateHorizontal(SHAPE_STRAIGHT, OctahedralGroup.INVERT_Y);
/*  58 */   private static final Map<Direction, VoxelShape> SHAPE_TOP_INNER = Shapes.rotateHorizontal(SHAPE_INNER, OctahedralGroup.INVERT_Y);
/*     */   
/*     */   private final Block base;
/*     */   protected final BlockState baseState;
/*     */   
/*     */   protected StairBlock(BlockState baseState, BlockBehaviour.Properties properties) {
/*  64 */     super(properties);
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)SHAPE, (Comparable)StairsShape.STRAIGHT)).setValue((Property)WATERLOGGED, false));
/*  66 */     this.base = baseState.getBlock();
/*  67 */     this.baseState = baseState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  77 */     boolean isBottom = (state.getValue((Property)HALF) == Half.BOTTOM);
/*  78 */     Direction facing = (Direction)state.getValue((Property)FACING);
/*     */     
/*  80 */     switch ((StairsShape)state.getValue((Property)SHAPE)) { default: throw new MatchException(null, null);
/*  81 */       case STRAIGHT: if (isBottom);
/*  82 */       case INNER_RIGHT: case INNER_LEFT: if (isBottom);
/*  83 */       case OUTER_LEFT: case OUTER_RIGHT: if (isBottom); break; }  switch ((StairsShape)
/*  84 */       state.getValue((Property)SHAPE)) { default: throw new MatchException(null, null);case STRAIGHT: case OUTER_LEFT: case INNER_RIGHT: case INNER_LEFT: case OUTER_RIGHT: break; }  return SHAPE_TOP_OUTER.get(
/*     */ 
/*     */         
/*  87 */         facing.getClockWise());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getExplosionResistance() {
/*  93 */     return this.base.getExplosionResistance();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  98 */     Direction clickedFace = context.getClickedFace();
/*  99 */     BlockPos pos = context.getClickedPos();
/* 100 */     FluidState replacedFluidState = context.getLevel().getFluidState(pos);
/*     */     
/* 102 */     BlockState state = (BlockState)((BlockState)((BlockState)defaultBlockState()
/* 103 */       .setValue((Property)FACING, (Comparable)context.getHorizontalDirection()))
/* 104 */       .setValue((Property)HALF, (clickedFace == Direction.DOWN || (clickedFace != Direction.UP && (context.getClickLocation()).y - pos.getY() > 0.5D)) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM))
/* 105 */       .setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */     
/* 107 */     return (BlockState)state.setValue((Property)SHAPE, (Comparable)getStairsShape(state, (BlockGetter)context.getLevel(), pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 112 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 113 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 115 */     if (directionToNeighbour.getAxis().isHorizontal()) {
/* 116 */       return (BlockState)state.setValue((Property)SHAPE, (Comparable)getStairsShape(state, (BlockGetter)level, pos));
/*     */     }
/* 118 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static StairsShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 122 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 123 */     BlockState behindState = level.getBlockState(pos.relative(facing));
/* 124 */     if (isStairs(behindState) && state.getValue((Property)HALF) == behindState.getValue((Property)HALF)) {
/* 125 */       Direction behindFacing = (Direction)behindState.getValue((Property)FACING);
/* 126 */       if (behindFacing.getAxis() != ((Direction)state.getValue((Property)FACING)).getAxis() && canTakeShape(state, level, pos, behindFacing.getOpposite())) {
/* 127 */         if (behindFacing == facing.getCounterClockWise()) {
/* 128 */           return StairsShape.OUTER_LEFT;
/*     */         }
/* 130 */         return StairsShape.OUTER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     BlockState frontState = level.getBlockState(pos.relative(facing.getOpposite()));
/* 135 */     if (isStairs(frontState) && state.getValue((Property)HALF) == frontState.getValue((Property)HALF)) {
/* 136 */       Direction frontFacing = (Direction)frontState.getValue((Property)FACING);
/* 137 */       if (frontFacing.getAxis() != ((Direction)state.getValue((Property)FACING)).getAxis() && canTakeShape(state, level, pos, frontFacing)) {
/* 138 */         if (frontFacing == facing.getCounterClockWise()) {
/* 139 */           return StairsShape.INNER_LEFT;
/*     */         }
/* 141 */         return StairsShape.INNER_RIGHT;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     return StairsShape.STRAIGHT;
/*     */   }
/*     */   
/*     */   private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction neighbour) {
/* 149 */     BlockState neighborState = level.getBlockState(pos.relative(neighbour));
/* 150 */     return (!isStairs(neighborState) || neighborState.getValue((Property)FACING) != state.getValue((Property)FACING) || neighborState.getValue((Property)HALF) != state.getValue((Property)HALF));
/*     */   }
/*     */   
/*     */   public static boolean isStairs(BlockState state) {
/* 154 */     return state.getBlock() instanceof StairBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 159 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 164 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 165 */     StairsShape shape = (StairsShape)state.getValue((Property)SHAPE);
/* 166 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 168 */         if (direction.getAxis() == Direction.Axis.Z) {
/* 169 */           switch (shape) {
/*     */             case INNER_LEFT:
/* 171 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_RIGHT);
/*     */             case INNER_RIGHT:
/* 173 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_LEFT);
/*     */             case OUTER_LEFT:
/* 175 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_RIGHT);
/*     */             case OUTER_RIGHT:
/* 177 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_LEFT);
/*     */           } 
/* 179 */           return state.rotate(Rotation.CLOCKWISE_180);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 184 */         if (direction.getAxis() == Direction.Axis.X) {
/* 185 */           switch (shape) {
/*     */             case INNER_LEFT:
/* 187 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_LEFT);
/*     */             case INNER_RIGHT:
/* 189 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.INNER_RIGHT);
/*     */             case OUTER_LEFT:
/* 191 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_RIGHT);
/*     */             case OUTER_RIGHT:
/* 193 */               return (BlockState)state.rotate(Rotation.CLOCKWISE_180).setValue((Property)SHAPE, (Comparable)StairsShape.OUTER_LEFT);
/*     */             case STRAIGHT:
/* 195 */               return state.rotate(Rotation.CLOCKWISE_180);
/*     */           } 
/*     */         
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 202 */     return super.mirror(state, mirror);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
/* 207 */     builder.add(new Property[] { (Property)FACING, (Property)HALF, (Property)SHAPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 212 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 213 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 215 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 220 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/StairBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */