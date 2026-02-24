/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ 
/*     */ public class DoublePlantBlock extends VegetationBlock {
/*  26 */   public static final MapCodec<DoublePlantBlock> CODEC = simpleCodec(DoublePlantBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends DoublePlantBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*     */   
/*     */   public DoublePlantBlock(BlockBehaviour.Properties properties) {
/*  36 */     super(properties);
/*     */     
/*  38 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  43 */     DoubleBlockHalf half = (DoubleBlockHalf)state.getValue((Property)HALF);
/*  44 */     if (directionToNeighbour.getAxis() == Direction.Axis.Y && ((half == DoubleBlockHalf.LOWER) ? true : false) == ((directionToNeighbour == Direction.UP) ? true : false) && (
/*  45 */       !neighbourState.is(this) || neighbourState.getValue((Property)HALF) == half)) {
/*  46 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */ 
/*     */     
/*  50 */     if (half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/*  51 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  59 */     BlockPos pos = context.getClickedPos();
/*  60 */     Level level = context.getLevel();
/*  61 */     if (pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context)) {
/*  62 */       return super.getStateForPlacement(context);
/*     */     }
/*     */     
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity by, ItemStack itemStack) {
/*  70 */     BlockPos abovePos = pos.above();
/*  71 */     level.setBlock(abovePos, copyWaterloggedFrom((LevelReader)level, abovePos, (BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  77 */     if (state.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/*  78 */       BlockState belowState = level.getBlockState(pos.below());
/*  79 */       return (belowState.is(this) && belowState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/*  82 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */   
/*     */   public static void placeAt(LevelAccessor level, BlockState state, BlockPos lowerPos, @Block.UpdateFlags int updateType) {
/*  86 */     BlockPos upperPos = lowerPos.above();
/*     */     
/*  88 */     level.setBlock(lowerPos, copyWaterloggedFrom((LevelReader)level, lowerPos, (BlockState)state.setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)), updateType);
/*  89 */     level.setBlock(upperPos, copyWaterloggedFrom((LevelReader)level, upperPos, (BlockState)state.setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)), updateType);
/*     */   }
/*     */   
/*     */   public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
/*  93 */     if (state.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/*  94 */       return (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, level.isWaterAt(pos));
/*     */     }
/*  96 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 101 */     if (!level.isClientSide()) {
/* 102 */       if (player.preventsBlockDrops()) {
/* 103 */         preventDropFromBottomPart(level, pos, state, player);
/*     */       } else {
/*     */         
/* 106 */         dropResources(state, level, pos, null, (Entity)player, player.getMainHandItem());
/*     */       } 
/*     */     }
/*     */     
/* 110 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) {
/* 116 */     super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, destroyedWith);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
/* 121 */     DoubleBlockHalf part = (DoubleBlockHalf)state.getValue((Property)HALF);
/* 122 */     if (part == DoubleBlockHalf.UPPER) {
/* 123 */       BlockPos bottomPos = pos.below();
/* 124 */       BlockState bottomState = level.getBlockState(bottomPos);
/* 125 */       if (bottomState.is(state.getBlock()) && bottomState.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
/*     */         
/* 127 */         BlockState blockState = bottomState.getFluidState().is((Fluid)net.minecraft.world.level.material.Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
/* 128 */         level.setBlock(bottomPos, blockState, 35);
/* 129 */         level.levelEvent((Entity)player, 2001, bottomPos, Block.getId(bottomState));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 136 */     builder.add(new Property[] { (Property)HALF });
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getSeed(BlockState state, BlockPos pos) {
/* 141 */     return Mth.getSeed(pos.getX(), pos.below((state.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? 0 : 1).getY(), pos.getZ());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DoublePlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */