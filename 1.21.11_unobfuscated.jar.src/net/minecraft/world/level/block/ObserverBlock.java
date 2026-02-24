/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class ObserverBlock extends DirectionalBlock {
/*  22 */   public static final MapCodec<ObserverBlock> CODEC = simpleCodec(ObserverBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<ObserverBlock> codec() {
/*  26 */     return CODEC;
/*     */   }
/*     */   
/*  29 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   public ObserverBlock(BlockBehaviour.Properties properties) {
/*  32 */     super(properties);
/*     */     
/*  34 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.SOUTH)).setValue((Property)POWERED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  39 */     builder.add(new Property[] { (Property)FACING, (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  44 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  49 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  54 */     if ((Boolean)state.getValue((Property)POWERED)) {
/*  55 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, false), 2);
/*     */     } else {
/*  57 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, true), 2);
/*  58 */       level.scheduleTick(pos, this, 2);
/*     */     } 
/*  60 */     updateNeighborsInFront((Level)level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  65 */     if (state.getValue((Property)FACING) == directionToNeighbour && !((Boolean)state.getValue((Property)POWERED))) {
/*  66 */       startSignal(level, ticks, pos);
/*     */     }
/*     */     
/*  69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private void startSignal(LevelReader level, ScheduledTickAccess ticks, BlockPos pos) {
/*  73 */     if (!level.isClientSide() && !ticks.getBlockTicks().hasScheduledTick(pos, this)) {
/*  74 */       ticks.scheduleTick(pos, this, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
/*  79 */     Direction direction = (Direction)state.getValue((Property)FACING);
/*  80 */     BlockPos oppositePos = pos.relative(direction.getOpposite());
/*     */     
/*  82 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction.getOpposite(), null);
/*  83 */     level.neighborChanged(oppositePos, this, orientation);
/*  84 */     level.updateNeighborsAtExceptFromFacing(oppositePos, this, direction, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  94 */     return state.getSignal(level, pos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  99 */     if ((Boolean)state.getValue((Property)POWERED) && state.getValue((Property)FACING) == direction) {
/* 100 */       return 15;
/*     */     }
/* 102 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 107 */     if (state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     if (!level.isClientSide() && (Boolean)state.getValue((Property)POWERED) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 112 */       BlockState newState = (BlockState)state.setValue((Property)POWERED, false);
/*     */       
/* 114 */       level.setBlock(pos, newState, 18);
/* 115 */       updateNeighborsInFront(level, pos, newState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 121 */     if ((Boolean)state.getValue((Property)POWERED) && level.getBlockTicks().hasScheduledTick(pos, this))
/*     */     {
/* 123 */       updateNeighborsInFront((Level)level, pos, (BlockState)state.setValue((Property)POWERED, false));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 129 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getNearestLookingDirection().getOpposite().getOpposite());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ObserverBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */