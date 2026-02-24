/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.grower.TreeGrower;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MangrovePropaguleBlock extends SaplingBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  27 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TreeGrower.CODEC.fieldOf("tree").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, MangrovePropaguleBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<MangrovePropaguleBlock> CODEC;
/*     */   
/*     */   public com.mojang.serialization.MapCodec<MangrovePropaguleBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = BlockStateProperties.AGE_4;
/*     */   
/*     */   public static final int MAX_AGE = 4;
/*  40 */   private static final int[] SHAPE_MIN_Y = new int[] { 13, 10, 7, 3, 0 }; private static final VoxelShape[] SHAPE_PER_AGE; static {
/*  41 */     SHAPE_PER_AGE = Block.boxes(4, age -> Block.column(2.0D, SHAPE_MIN_Y[age], 16.0D));
/*     */   }
/*  43 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  44 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*     */   
/*     */   public MangrovePropaguleBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
/*  47 */     super(treeGrower, properties);
/*  48 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  49 */         .setValue((Property)STAGE, 0))
/*  50 */         .setValue((Property)AGE, 0))
/*  51 */         .setValue((Property)WATERLOGGED, false))
/*  52 */         .setValue((Property)HANGING, false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  58 */     builder.add(new Property[] { (Property)STAGE }).add(new Property[] { (Property)AGE }).add(new Property[] { (Property)WATERLOGGED }).add(new Property[] { (Property)HANGING });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  63 */     return (super.mayPlaceOn(state, level, pos) || state.is(Blocks.CLAY));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  68 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  69 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/*  70 */     return (BlockState)((BlockState)super.getStateForPlacement(context).setValue((Property)WATERLOGGED, isWaterSource)).setValue((Property)AGE, 4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  75 */     int age = (Boolean)state.getValue((Property)HANGING) ? (Integer)state.getValue((Property)AGE) : 4;
/*  76 */     return SHAPE_PER_AGE[age].move(state.getOffset(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  81 */     if (isHanging(state)) {
/*  82 */       return level.getBlockState(pos.above()).is(Blocks.MANGROVE_LEAVES);
/*     */     }
/*  84 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  89 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  90 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  92 */     if (directionToNeighbour == Direction.UP && !state.canSurvive(level, pos)) {
/*  93 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  95 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 100 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 101 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 103 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 108 */     if (!isHanging(state)) {
/*     */ 
/*     */       
/* 111 */       if (random.nextInt(7) == 0) {
/* 112 */         advanceTree(level, pos, state, random);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     if (!isFullyGrown(state)) {
/* 118 */       level.setBlock(pos, (BlockState)state.cycle((Property)AGE), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 124 */     return (!isHanging(state) || !isFullyGrown(state));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 129 */     return isHanging(state) ? (!isFullyGrown(state)) : super.isBonemealSuccess(level, random, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 134 */     if (isHanging(state) && !isFullyGrown(state)) {
/* 135 */       level.setBlock(pos, (BlockState)state.cycle((Property)AGE), 2);
/*     */     } else {
/* 137 */       super.performBonemeal(level, random, pos, state);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isHanging(BlockState state) {
/* 142 */     return (Boolean)state.getValue((Property)HANGING);
/*     */   }
/*     */   
/*     */   private static boolean isFullyGrown(BlockState state) {
/* 146 */     return ((Integer)state.getValue((Property)AGE) == 4);
/*     */   }
/*     */   
/*     */   public static BlockState createNewHangingPropagule() {
/* 150 */     return createNewHangingPropagule(0);
/*     */   }
/*     */   
/*     */   public static BlockState createNewHangingPropagule(int age) {
/* 154 */     return (BlockState)((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState()
/* 155 */       .setValue((Property)HANGING, true))
/* 156 */       .setValue((Property)AGE, age);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MangrovePropaguleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */