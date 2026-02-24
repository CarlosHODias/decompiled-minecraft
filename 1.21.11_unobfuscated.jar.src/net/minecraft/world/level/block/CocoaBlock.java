/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
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
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CocoaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
/*  29 */   public static final MapCodec<CocoaBlock> CODEC = simpleCodec(CocoaBlock::new);
/*     */   public static final int MAX_AGE = 2;
/*     */   
/*     */   public MapCodec<CocoaBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  37 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
/*     */   private static final List<Map<Direction, VoxelShape>> SHAPES;
/*     */   
/*     */   static {
/*  41 */     SHAPES = IntStream.rangeClosed(0, 2).<Map<Direction, VoxelShape>>mapToObj(i -> Shapes.rotateHorizontal(Block.column((4 + i * 2), (7 - i * 2), 12.0D).move(0.0D, 0.0D, (i - 5) / 16.0D).optimize())).toList();
/*     */   }
/*     */   public CocoaBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)AGE, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  50 */     return ((Integer)state.getValue((Property)AGE) < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  55 */     if (level.random.nextInt(5) == 0) {
/*  56 */       int age = (Integer)state.getValue((Property)AGE);
/*  57 */       if (age < 2) {
/*  58 */         level.setBlock(pos, (BlockState)state.setValue((Property)AGE, age + 1), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  65 */     BlockState relativeState = level.getBlockState(pos.relative((Direction)state.getValue((Property)FACING)));
/*  66 */     return relativeState.is(BlockTags.JUNGLE_LOGS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  71 */     return (VoxelShape)((Map)SHAPES.get((Integer)state.getValue((Property)AGE))).get(state.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  76 */     BlockState state = defaultBlockState();
/*     */     
/*  78 */     Level level = context.getLevel();
/*  79 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  81 */     for (Direction direction : context.getNearestLookingDirections()) {
/*  82 */       if (direction.getAxis().isHorizontal()) {
/*  83 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)direction);
/*  84 */         if (state.canSurvive((LevelReader)level, pos)) {
/*  85 */           return state;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  95 */     if (directionToNeighbour == state.getValue((Property)FACING) && !state.canSurvive(level, pos)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 104 */     return ((Integer)state.getValue((Property)AGE) < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 114 */     level.setBlock(pos, (BlockState)state.setValue((Property)AGE, (Integer)state.getValue((Property)AGE) + 1), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 119 */     builder.add(new Property[] { (Property)FACING, (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 124 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CocoaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */