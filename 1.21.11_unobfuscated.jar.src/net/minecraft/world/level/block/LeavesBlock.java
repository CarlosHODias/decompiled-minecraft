/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ParticleUtils;
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
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class LeavesBlock extends Block implements SimpleWaterloggedBlock {
/*     */   public static final int DECAY_DISTANCE = 7;
/*  35 */   public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
/*  36 */   public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
/*  37 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   protected final float leafParticleChance;
/*     */   private static final int TICK_DELAY = 1;
/*     */   private static boolean cutoutLeaves = true;
/*     */   
/*     */   public abstract MapCodec<? extends LeavesBlock> codec();
/*     */   
/*     */   public LeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*  46 */     this.leafParticleChance = leafParticleChance;
/*  47 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DISTANCE, 7)).setValue((Property)PERSISTENT, false)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/*  52 */     if (!cutoutLeaves && neighborState.getBlock() instanceof LeavesBlock) {
/*  53 */       return true;
/*     */     }
/*  55 */     return super.skipRendering(state, neighborState, direction);
/*     */   }
/*     */   
/*     */   public static void setCutoutLeaves(boolean cutoutLeaves) {
/*  59 */     LeavesBlock.cutoutLeaves = cutoutLeaves;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  64 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  69 */     return ((Integer)state.getValue((Property)DISTANCE) == 7 && !((Boolean)state.getValue((Property)PERSISTENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  74 */     if (decaying(state)) {
/*  75 */       dropResources(state, (Level)level, pos);
/*  76 */       level.removeBlock(pos, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean decaying(BlockState state) {
/*  81 */     return (!((Boolean)state.getValue((Property)PERSISTENT)) && (Integer)state.getValue((Property)DISTANCE) == 7);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  86 */     level.setBlock(pos, updateDistance(state, (LevelAccessor)level, pos), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLightBlock(BlockState state) {
/*  91 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  96 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  97 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  99 */     int distanceFromNeighbor = getDistanceAt(neighbourState) + 1;
/* 100 */     if (distanceFromNeighbor != 1 || (Integer)state.getValue((Property)DISTANCE) != distanceFromNeighbor) {
/* 101 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/* 103 */     return state;
/*     */   }
/*     */   
/*     */   private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
/* 107 */     int newDistance = 7;
/* 108 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 109 */     for (Direction direction : Direction.values()) {
/* 110 */       neighborPos.setWithOffset((Vec3i)pos, direction);
/* 111 */       newDistance = Math.min(newDistance, getDistanceAt(level.getBlockState((BlockPos)neighborPos)) + 1);
/* 112 */       if (newDistance == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/* 116 */     return (BlockState)state.setValue((Property)DISTANCE, newDistance);
/*     */   }
/*     */   
/*     */   private static int getDistanceAt(BlockState state) {
/* 120 */     return getOptionalDistanceAt(state).orElse(7);
/*     */   }
/*     */   
/*     */   public static OptionalInt getOptionalDistanceAt(BlockState state) {
/* 124 */     if (state.is(BlockTags.LOGS)) {
/* 125 */       return OptionalInt.of(0);
/*     */     }
/* 127 */     if (state.hasProperty((Property)DISTANCE)) {
/* 128 */       return OptionalInt.of((Integer)state.getValue((Property)DISTANCE));
/*     */     }
/* 130 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 135 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 136 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 138 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 143 */     super.animateTick(state, level, pos, random);
/* 144 */     BlockPos below = pos.below();
/* 145 */     BlockState belowState = level.getBlockState(below);
/*     */     
/* 147 */     makeDrippingWaterParticles(level, pos, random, belowState, below);
/* 148 */     makeFallingLeavesParticles(level, pos, random, belowState, below);
/*     */   }
/*     */   
/*     */   private static void makeDrippingWaterParticles(Level level, BlockPos pos, RandomSource random, BlockState belowState, BlockPos below) {
/* 152 */     if (!level.isRainingAt(pos.above())) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     if (random.nextInt(15) != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     if (belowState.canOcclude() && belowState.isFaceSturdy((BlockGetter)level, below, Direction.UP)) {
/*     */       return;
/*     */     }
/*     */     
/* 164 */     ParticleUtils.spawnParticleBelow(level, pos, random, (ParticleOptions)ParticleTypes.DRIPPING_WATER);
/*     */   }
/*     */   
/*     */   private void makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random, BlockState belowState, BlockPos below) {
/* 168 */     if (random.nextFloat() >= this.leafParticleChance) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     if (isFaceFull(belowState.getCollisionShape((BlockGetter)level, below), Direction.UP)) {
/*     */       return;
/*     */     }
/*     */     
/* 176 */     spawnFallingLeavesParticle(level, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void spawnFallingLeavesParticle(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource);
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 183 */     builder.add(new Property[] { (Property)DISTANCE, (Property)PERSISTENT, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 188 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 189 */     BlockState state = (BlockState)((BlockState)defaultBlockState().setValue((Property)PERSISTENT, true)).setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/* 190 */     return updateDistance(state, (LevelAccessor)context.getLevel(), context.getClickedPos());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */