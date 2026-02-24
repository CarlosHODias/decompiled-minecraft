/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SmallDripleafBlock extends DoublePlantBlock implements SimpleWaterloggedBlock, BonemealableBlock {
/*  29 */   public static final MapCodec<SmallDripleafBlock> CODEC = simpleCodec(SmallDripleafBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<SmallDripleafBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */   
/*  36 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  37 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*     */   
/*  39 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*     */   
/*     */   public SmallDripleafBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*     */     
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)).setValue((Property)WATERLOGGED, false)).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  49 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  54 */     return (state.is(BlockTags.SMALL_DRIPLEAF_PLACEABLE) || (level.getFluidState(pos.above()).isSourceOfType((Fluid)Fluids.WATER) && super.mayPlaceOn(state, level, pos)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  59 */     BlockState state = super.getStateForPlacement(context);
/*  60 */     if (state != null) {
/*  61 */       return copyWaterloggedFrom((LevelReader)context.getLevel(), context.getClickedPos(), (BlockState)state.setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite()));
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity by, net.minecraft.world.item.ItemStack itemStack) {
/*  68 */     if (!level.isClientSide()) {
/*  69 */       BlockPos abovePos = pos.above();
/*  70 */       BlockState blockState = DoublePlantBlock.copyWaterloggedFrom((LevelReader)level, abovePos, (BlockState)((BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)).setValue((Property)FACING, state.getValue((Property)FACING)));
/*  71 */       level.setBlock(abovePos, blockState, 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  77 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  78 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  80 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  85 */     if (state.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/*  86 */       return super.canSurvive(state, level, pos);
/*     */     }
/*     */     
/*  89 */     BlockPos belowPos = pos.below();
/*  90 */     BlockState belowState = level.getBlockState(belowPos);
/*  91 */     return mayPlaceOn(belowState, (BlockGetter)level, belowPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  96 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  97 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  99 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 104 */     builder.add(new Property[] { (Property)HALF, (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 119 */     if (state.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
/*     */       
/* 121 */       BlockPos above = pos.above();
/* 122 */       level.setBlock(above, level.getFluidState(above).createLegacyBlock(), 18);
/* 123 */       BigDripleafBlock.placeWithRandomHeight((LevelAccessor)level, random, pos, (Direction)state.getValue((Property)FACING));
/*     */     } else {
/* 125 */       BlockPos belowPos = pos.below();
/* 126 */       performBonemeal(level, random, belowPos, level.getBlockState(belowPos));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 132 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 137 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getMaxVerticalOffset() {
/* 142 */     return 0.1F;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SmallDripleafBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */