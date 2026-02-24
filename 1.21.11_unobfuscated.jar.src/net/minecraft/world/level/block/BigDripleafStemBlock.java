/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
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
/*     */ public class BigDripleafStemBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, BonemealableBlock {
/*  30 */   public static final MapCodec<BigDripleafStemBlock> CODEC = simpleCodec(BigDripleafStemBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BigDripleafStemBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  39 */   private static final java.util.Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.column(6.0D, 0.0D, 16.0D).move(0.0D, 0.0D, 0.25D).optimize());
/*     */   
/*     */   protected BigDripleafStemBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, false)).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  48 */     return SHAPES.get(state.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  53 */     builder.add(new Property[] { (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  58 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  59 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/*  62 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  67 */     BlockPos belowPos = pos.below();
/*  68 */     BlockState belowState = level.getBlockState(belowPos);
/*  69 */     BlockState aboveState = level.getBlockState(pos.above());
/*  70 */     return ((belowState.is(this) || belowState.is(BlockTags.BIG_DRIPLEAF_PLACEABLE)) && (
/*  71 */       aboveState.is(this) || aboveState.is(Blocks.BIG_DRIPLEAF)));
/*     */   }
/*     */   
/*     */   protected static boolean place(LevelAccessor level, BlockPos pos, FluidState fluidState, Direction facing) {
/*  75 */     BlockState newState = (BlockState)((BlockState)Blocks.BIG_DRIPLEAF_STEM.defaultBlockState()
/*  76 */       .setValue((Property)WATERLOGGED, fluidState.isSourceOfType((Fluid)Fluids.WATER)))
/*  77 */       .setValue((Property)FACING, (Comparable)facing);
/*  78 */     return level.setBlock(pos, newState, 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  83 */     if ((directionToNeighbour == Direction.DOWN || directionToNeighbour == Direction.UP) && !state.canSurvive(level, pos)) {
/*  84 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*  86 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  87 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  89 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  94 */     if (!state.canSurvive((LevelReader)level, pos)) {
/*  95 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 101 */     Optional<BlockPos> headPos = BlockUtil.getTopConnectedBlock((BlockGetter)level, pos, state.getBlock(), Direction.UP, Blocks.BIG_DRIPLEAF);
/* 102 */     if (headPos.isEmpty()) {
/* 103 */       return false;
/*     */     }
/* 105 */     BlockPos abovePos = ((BlockPos)headPos.get()).above();
/* 106 */     BlockState aboveState = level.getBlockState(abovePos);
/* 107 */     return BigDripleafBlock.canPlaceAt((net.minecraft.world.level.LevelHeightAccessor)level, abovePos, aboveState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 117 */     Optional<BlockPos> forwardPos = BlockUtil.getTopConnectedBlock((BlockGetter)level, pos, state.getBlock(), Direction.UP, Blocks.BIG_DRIPLEAF);
/* 118 */     if (forwardPos.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     BlockPos headPos = forwardPos.get();
/* 123 */     BlockPos placeHeadPos = headPos.above();
/* 124 */     Direction facing = (Direction)state.getValue((Property)FACING);
/*     */     
/* 126 */     place((LevelAccessor)level, headPos, level.getFluidState(headPos), facing);
/* 127 */     BigDripleafBlock.place((LevelAccessor)level, placeHeadPos, level.getFluidState(placeHeadPos), facing);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 132 */     return new ItemStack(Blocks.BIG_DRIPLEAF);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BigDripleafStemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */