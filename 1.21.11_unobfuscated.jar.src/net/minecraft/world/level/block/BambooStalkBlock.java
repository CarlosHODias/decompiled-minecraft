/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
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
/*     */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BambooStalkBlock extends Block implements BonemealableBlock {
/*  27 */   public static final MapCodec<BambooStalkBlock> CODEC = simpleCodec(BambooStalkBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BambooStalkBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */   
/*  34 */   private static final VoxelShape SHAPE_SMALL = Block.column(6.0D, 0.0D, 16.0D);
/*  35 */   private static final VoxelShape SHAPE_LARGE = Block.column(10.0D, 0.0D, 16.0D);
/*  36 */   private static final VoxelShape SHAPE_COLLISION = Block.column(3.0D, 0.0D, 16.0D);
/*     */   
/*  38 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
/*  39 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
/*  40 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*     */   
/*     */   public static final int MAX_HEIGHT = 16;
/*     */   public static final int STAGE_GROWING = 0;
/*     */   public static final int STAGE_DONE_GROWING = 1;
/*     */   public static final int AGE_THIN_BAMBOO = 0;
/*     */   public static final int AGE_THICK_BAMBOO = 1;
/*     */   
/*     */   public BambooStalkBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, 0)).setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)STAGE, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  55 */     builder.add(new Property[] { (Property)AGE, (Property)LEAVES, (Property)STAGE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  65 */     VoxelShape shape = (state.getValue((Property)LEAVES) == BambooLeaves.LARGE) ? SHAPE_LARGE : SHAPE_SMALL;
/*  66 */     return shape.move(state.getOffset(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  76 */     return SHAPE_COLLISION.move(state.getOffset(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  86 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  87 */     if (!fluidState.isEmpty()) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
/*  92 */     if (belowState.is(BlockTags.BAMBOO_PLANTABLE_ON)) {
/*  93 */       if (belowState.is(Blocks.BAMBOO_SAPLING))
/*  94 */         return (BlockState)defaultBlockState().setValue((Property)AGE, 0); 
/*  95 */       if (belowState.is(Blocks.BAMBOO)) {
/*  96 */         int age = ((Integer)belowState.getValue((Property)AGE) > 0) ? 1 : 0;
/*  97 */         return (BlockState)defaultBlockState().setValue((Property)AGE, age);
/*     */       } 
/*  99 */       BlockState aboveState = context.getLevel().getBlockState(context.getClickedPos().above());
/* 100 */       if (aboveState.is(Blocks.BAMBOO)) {
/* 101 */         return (BlockState)defaultBlockState().setValue((Property)AGE, aboveState.getValue((Property)AGE));
/*     */       }
/* 103 */       return Blocks.BAMBOO_SAPLING.defaultBlockState();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 113 */     if (!state.canSurvive((LevelReader)level, pos)) {
/* 114 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/* 120 */     return ((Integer)state.getValue((Property)STAGE) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 125 */     if ((Integer)state.getValue((Property)STAGE) != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 129 */     if (random.nextInt(3) == 0 && level.isEmptyBlock(pos.above()) && level.getRawBrightness(pos.above(), 0) >= 9) {
/* 130 */       int height = getHeightBelowUpToMax((BlockGetter)level, pos) + 1;
/* 131 */       if (height < 16) {
/* 132 */         growBamboo(state, (Level)level, pos, random, height);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 139 */     return level.getBlockState(pos.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 144 */     if (!state.canSurvive(level, pos)) {
/* 145 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*     */     
/* 148 */     if (directionToNeighbour == Direction.UP && 
/* 149 */       neighbourState.is(Blocks.BAMBOO) && (Integer)neighbourState.getValue((Property)AGE) > (Integer)state.getValue((Property)AGE)) {
/* 150 */       return (BlockState)state.cycle((Property)AGE);
/*     */     }
/*     */ 
/*     */     
/* 154 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 159 */     int heightAbove = getHeightAboveUpToMax((BlockGetter)level, pos);
/* 160 */     int heightBelow = getHeightBelowUpToMax((BlockGetter)level, pos);
/* 161 */     return (heightAbove + heightBelow + 1 < 16 && (Integer)level.getBlockState(pos.above(heightAbove)).getValue((Property)STAGE) != 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 166 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 171 */     int heightAbove = getHeightAboveUpToMax((BlockGetter)level, pos);
/* 172 */     int heightBelow = getHeightBelowUpToMax((BlockGetter)level, pos);
/* 173 */     int totalHeight = heightAbove + heightBelow + 1;
/*     */     
/* 175 */     int newBamboo = 1 + random.nextInt(2);
/* 176 */     for (int i = 0; i < newBamboo; i++) {
/* 177 */       BlockPos topPos = pos.above(heightAbove);
/* 178 */       BlockState topState = level.getBlockState(topPos);
/* 179 */       if (totalHeight >= 16 || (Integer)topState.getValue((Property)STAGE) == 1 || !level.isEmptyBlock(topPos.above())) {
/*     */         return;
/*     */       }
/*     */       
/* 183 */       growBamboo(topState, (Level)level, topPos, random, totalHeight);
/*     */       
/* 185 */       heightAbove++;
/* 186 */       totalHeight++;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int height) {
/* 191 */     BlockState belowState = level.getBlockState(pos.below());
/* 192 */     BlockPos twoBelowPos = pos.below(2);
/* 193 */     BlockState twoBelowState = level.getBlockState(twoBelowPos);
/*     */     
/* 195 */     BambooLeaves leaves = BambooLeaves.NONE;
/* 196 */     if (height >= 1) {
/* 197 */       if (!belowState.is(Blocks.BAMBOO) || belowState.getValue((Property)LEAVES) == BambooLeaves.NONE) {
/* 198 */         leaves = BambooLeaves.SMALL;
/* 199 */       } else if (belowState.is(Blocks.BAMBOO) && belowState.getValue((Property)LEAVES) != BambooLeaves.NONE) {
/* 200 */         leaves = BambooLeaves.LARGE;
/*     */         
/* 202 */         if (twoBelowState.is(Blocks.BAMBOO)) {
/* 203 */           level.setBlock(pos.below(), (BlockState)belowState.setValue((Property)LEAVES, (Comparable)BambooLeaves.SMALL), 3);
/* 204 */           level.setBlock(twoBelowPos, (BlockState)twoBelowState.setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE), 3);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 209 */     int age = ((Integer)state.getValue((Property)AGE) == 1 || twoBelowState.is(Blocks.BAMBOO)) ? 1 : 0;
/* 210 */     int stage = ((height >= 11 && random.nextFloat() < 0.25F) || height == 15) ? 1 : 0;
/* 211 */     level.setBlock(pos.above(), (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AGE, age)).setValue((Property)LEAVES, (Comparable)leaves)).setValue((Property)STAGE, stage), 3);
/*     */   }
/*     */   
/*     */   protected int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
/* 215 */     int height = 0;
/* 216 */     while (height < 16 && level.getBlockState(pos.above(height + 1)).is(Blocks.BAMBOO)) {
/* 217 */       height++;
/*     */     }
/* 219 */     return height;
/*     */   }
/*     */   
/*     */   protected int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
/* 223 */     int height = 0;
/* 224 */     while (height < 16 && level.getBlockState(pos.below(height + 1)).is(Blocks.BAMBOO)) {
/* 225 */       height++;
/*     */     }
/* 227 */     return height;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BambooStalkBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */