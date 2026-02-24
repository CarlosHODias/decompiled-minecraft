/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
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
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import net.minecraft.world.ticks.TickPriority;
/*     */ 
/*     */ public abstract class DiodeBlock extends HorizontalDirectionalBlock {
/*  28 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  30 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 2.0D);
/*     */   
/*     */   protected DiodeBlock(BlockBehaviour.Properties properties) {
/*  33 */     super(properties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends DiodeBlock> codec();
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  41 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  46 */     BlockPos belowPos = pos.below();
/*  47 */     return canSurviveOn(level, belowPos, level.getBlockState(belowPos));
/*     */   }
/*     */   
/*     */   protected boolean canSurviveOn(LevelReader level, BlockPos neightborPos, BlockState neighborState) {
/*  51 */     return neighborState.isFaceSturdy((BlockGetter)level, neightborPos, Direction.UP, SupportType.RIGID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  56 */     if (isLocked((LevelReader)level, pos, state)) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     boolean on = (Boolean)state.getValue((Property)POWERED);
/*  61 */     boolean shouldTurnOn = shouldTurnOn((Level)level, pos, state);
/*  62 */     if (on && !shouldTurnOn) {
/*  63 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, false), 2);
/*  64 */     } else if (!on) {
/*     */ 
/*     */       
/*  67 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, true), 2);
/*  68 */       if (!shouldTurnOn) {
/*  69 */         level.scheduleTick(pos, this, getDelay(state), TickPriority.VERY_HIGH);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  76 */     return state.getSignal(level, pos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  81 */     if (!((Boolean)state.getValue((Property)POWERED))) {
/*  82 */       return 0;
/*     */     }
/*     */     
/*  85 */     if (state.getValue((Property)FACING) == direction) {
/*  86 */       return getOutputSignal(level, pos, state);
/*     */     }
/*     */     
/*  89 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  94 */     if (state.canSurvive((LevelReader)level, pos)) {
/*  95 */       checkTickOnNeighbor(level, pos, state);
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/* 100 */     dropResources(state, (LevelAccessor)level, pos, blockEntity);
/* 101 */     level.removeBlock(pos, false);
/* 102 */     for (Direction direction : Direction.values()) {
/* 103 */       level.updateNeighborsAt(pos.relative(direction), this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkTickOnNeighbor(Level level, BlockPos pos, BlockState state) {
/* 108 */     if (isLocked((LevelReader)level, pos, state)) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     boolean on = (Boolean)state.getValue((Property)POWERED);
/* 113 */     boolean shouldTurnOn = shouldTurnOn(level, pos, state);
/* 114 */     if (on != shouldTurnOn && !level.getBlockTicks().willTickThisTick(pos, this)) {
/* 115 */       TickPriority priority = TickPriority.HIGH;
/*     */ 
/*     */       
/* 118 */       if (shouldPrioritize((BlockGetter)level, pos, state)) {
/* 119 */         priority = TickPriority.EXTREMELY_HIGH;
/* 120 */       } else if (on) {
/* 121 */         priority = TickPriority.VERY_HIGH;
/*     */       } 
/*     */       
/* 124 */       level.scheduleTick(pos, this, getDelay(state), priority);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) {
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
/* 133 */     return (getInputSignal(level, pos, state) > 0);
/*     */   }
/*     */   
/*     */   protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
/* 137 */     Direction direction = (Direction)state.getValue((Property)FACING);
/*     */     
/* 139 */     BlockPos targetPos = pos.relative(direction);
/* 140 */     int input = level.getSignal(targetPos, direction);
/* 141 */     if (input >= 15) {
/* 142 */       return input;
/*     */     }
/*     */     
/* 145 */     BlockState targetBlockState = level.getBlockState(targetPos);
/* 146 */     return Math.max(input, targetBlockState.is(Blocks.REDSTONE_WIRE) ? (Integer)targetBlockState.getValue((Property)RedStoneWireBlock.POWER) : 0);
/*     */   }
/*     */   
/*     */   protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
/* 150 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 151 */     Direction clockWise = direction.getClockWise();
/* 152 */     Direction counterClockWise = direction.getCounterClockWise();
/* 153 */     boolean sideInputDiodesOnly = sideInputDiodesOnly();
/* 154 */     return Math.max(
/* 155 */         level.getControlInputSignal(pos.relative(clockWise), clockWise, sideInputDiodesOnly), 
/* 156 */         level.getControlInputSignal(pos.relative(counterClockWise), counterClockWise, sideInputDiodesOnly));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 167 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 172 */     if (shouldTurnOn(level, pos, state)) {
/* 173 */       level.scheduleTick(pos, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 179 */     updateNeighborsInFront(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 184 */     if (!movedByPiston) {
/* 185 */       updateNeighborsInFront((Level)level, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
/* 190 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 191 */     BlockPos oppositePos = pos.relative(direction.getOpposite());
/*     */     
/* 193 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction.getOpposite(), Direction.UP);
/* 194 */     level.neighborChanged(oppositePos, this, orientation);
/* 195 */     level.updateNeighborsAtExceptFromFacing(oppositePos, this, direction, orientation);
/*     */   }
/*     */   
/*     */   protected boolean sideInputDiodesOnly() {
/* 199 */     return false;
/*     */   }
/*     */   
/*     */   protected int getOutputSignal(BlockGetter level, BlockPos pos, BlockState state) {
/* 203 */     return 15;
/*     */   }
/*     */   
/*     */   public static boolean isDiode(BlockState state) {
/* 207 */     return state.getBlock() instanceof DiodeBlock;
/*     */   }
/*     */   
/*     */   public boolean shouldPrioritize(BlockGetter level, BlockPos pos, BlockState state) {
/* 211 */     Direction direction = ((Direction)state.getValue((Property)FACING)).getOpposite();
/* 212 */     BlockState oppositeState = level.getBlockState(pos.relative(direction));
/*     */     
/* 214 */     return (isDiode(oppositeState) && oppositeState.getValue((Property)FACING) != direction);
/*     */   }
/*     */   
/*     */   protected abstract int getDelay(BlockState paramBlockState);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DiodeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */