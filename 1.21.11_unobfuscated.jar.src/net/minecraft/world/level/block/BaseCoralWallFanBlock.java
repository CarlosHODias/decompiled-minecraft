/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BaseCoralWallFanBlock extends BaseCoralFanBlock {
/*  23 */   public static final MapCodec<BaseCoralWallFanBlock> CODEC = simpleCodec(BaseCoralWallFanBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends BaseCoralWallFanBlock> codec() {
/*  27 */     return CODEC;
/*     */   }
/*     */   
/*  30 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  32 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 8.0D, 5.0D, 16.0D));
/*     */   
/*     */   protected BaseCoralWallFanBlock(BlockBehaviour.Properties properties) {
/*  35 */     super(properties);
/*  36 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  41 */     return SHAPES.get(state.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  46 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  51 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  56 */     builder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  61 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  62 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  65 */     if (directionToNeighbour.getOpposite() == state.getValue((Property)FACING) && !state.canSurvive(level, pos)) {
/*  66 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  69 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  74 */     Direction facing = (Direction)state.getValue((Property)FACING);
/*  75 */     BlockPos relativePos = pos.relative(facing.getOpposite());
/*  76 */     BlockState relativeState = level.getBlockState(relativePos);
/*     */     
/*  78 */     return relativeState.isFaceSturdy((BlockGetter)level, relativePos, facing);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  83 */     BlockState state = super.getStateForPlacement(context);
/*     */     
/*  85 */     Level level = context.getLevel();
/*  86 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  88 */     Direction[] directions = context.getNearestLookingDirections();
/*  89 */     for (Direction direction : directions) {
/*  90 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  94 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)direction.getOpposite());
/*  95 */         if (state.canSurvive((LevelReader)level, pos)) {
/*  96 */           return state;
/*     */         }
/*     */       } 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BaseCoralWallFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */