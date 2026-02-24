/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ParticleUtils;
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
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ 
/*     */ public class LightningRodBlock extends RodBlock implements SimpleWaterloggedBlock {
/*  28 */   public static final MapCodec<LightningRodBlock> CODEC = simpleCodec(LightningRodBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends LightningRodBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */   
/*  35 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  36 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   private static final int ACTIVATION_TICKS = 8;
/*     */   public static final int RANGE = 128;
/*     */   private static final int SPARK_CYCLE = 200;
/*     */   
/*     */   public LightningRodBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP)).setValue((Property)WATERLOGGED, false)).setValue((Property)POWERED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  48 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  49 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/*  50 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getClickedFace())).setValue((Property)WATERLOGGED, isWaterSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  55 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  56 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  58 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  63 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  64 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  66 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  71 */     return (Boolean)state.getValue((Property)POWERED) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  76 */     if ((Boolean)state.getValue((Property)POWERED) && state.getValue((Property)FACING) == direction) {
/*  77 */       return 15;
/*     */     }
/*  79 */     return 0;
/*     */   }
/*     */   
/*     */   public void onLightningStrike(BlockState state, Level level, BlockPos pos) {
/*  83 */     level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, true), 3);
/*  84 */     updateNeighbours(state, level, pos);
/*  85 */     level.scheduleTick(pos, this, 8);
/*     */     
/*  87 */     level.levelEvent(3002, pos, ((Direction)state.getValue((Property)FACING)).getAxis().ordinal());
/*     */   }
/*     */   
/*     */   private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
/*  91 */     Direction front = ((Direction)state.getValue((Property)FACING)).getOpposite();
/*  92 */     level.updateNeighborsAt(pos.relative(front), this, ExperimentalRedstoneUtils.initialOrientation(level, front, null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  97 */     level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, false), 3);
/*  98 */     updateNeighbours(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 103 */     if (!level.isThundering() || 
/* 104 */       level.random.nextInt(200) > level.getGameTime() % 200L || 
/* 105 */       pos.getY() != level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 1) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     ParticleUtils.spawnParticlesAlongAxis(((Direction)state.getValue((Property)FACING)).getAxis(), level, pos, 0.125D, (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, net.minecraft.util.valueproviders.UniformInt.of(1, 2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 114 */     if ((Boolean)state.getValue((Property)POWERED)) {
/* 115 */       updateNeighbours(state, (Level)level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 121 */     if (state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     if ((Boolean)state.getValue((Property)POWERED) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 126 */       level.scheduleTick(pos, this, 8);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 132 */     builder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 137 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LightningRodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */