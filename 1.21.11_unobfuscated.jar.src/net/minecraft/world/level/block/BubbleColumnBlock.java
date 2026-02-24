/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BubbleColumnBlock extends Block implements BucketPickup {
/*  36 */   public static final MapCodec<BubbleColumnBlock> CODEC = simpleCodec(BubbleColumnBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BubbleColumnBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final BooleanProperty DRAG_DOWN = BlockStateProperties.DRAG;
/*     */   private static final int CHECK_PERIOD = 5;
/*     */   
/*     */   public BubbleColumnBlock(BlockBehaviour.Properties properties) {
/*  47 */     super(properties);
/*  48 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DRAG_DOWN, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  53 */     if (isPrecise) {
/*  54 */       BlockState stateAbove = level.getBlockState(pos.above());
/*  55 */       boolean nothingAbove = (stateAbove.getCollisionShape((BlockGetter)level, pos).isEmpty() && stateAbove.getFluidState().isEmpty());
/*  56 */       if (nothingAbove) {
/*  57 */         entity.onAboveBubbleColumn((Boolean)state.getValue((Property)DRAG_DOWN), pos);
/*     */       } else {
/*  59 */         entity.onInsideBubbleColumn((Boolean)state.getValue((Property)DRAG_DOWN));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  66 */     updateColumn((LevelAccessor)level, pos, state, level.getBlockState(pos.below()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  71 */     return Fluids.WATER.getSource(false);
/*     */   }
/*     */   
/*     */   public static void updateColumn(LevelAccessor level, BlockPos origin, BlockState belowState) {
/*  75 */     updateColumn(level, origin, level.getBlockState(origin), belowState);
/*     */   }
/*     */   
/*     */   public static void updateColumn(LevelAccessor level, BlockPos origin, BlockState originState, BlockState belowState) {
/*  79 */     if (!canExistIn(originState)) {
/*     */       return;
/*     */     }
/*  82 */     BlockState columnState = getColumnState(belowState);
/*  83 */     level.setBlock(origin, columnState, 2);
/*     */     
/*  85 */     BlockPos.MutableBlockPos pos = origin.mutable().move(Direction.UP);
/*  86 */     while (canExistIn(level.getBlockState((BlockPos)pos))) {
/*  87 */       if (!level.setBlock((BlockPos)pos, columnState, 2)) {
/*     */         return;
/*     */       }
/*  90 */       pos.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean canExistIn(BlockState state) {
/*  95 */     return (state.is(Blocks.BUBBLE_COLUMN) || (state.is(Blocks.WATER) && state.getFluidState().getAmount() >= 8 && state.getFluidState().isSource()));
/*     */   }
/*     */   
/*     */   private static BlockState getColumnState(BlockState belowState) {
/*  99 */     if (belowState.is(Blocks.BUBBLE_COLUMN)) {
/* 100 */       return belowState;
/*     */     }
/* 102 */     if (belowState.is(Blocks.SOUL_SAND)) {
/* 103 */       return (BlockState)Blocks.BUBBLE_COLUMN.defaultBlockState().setValue((Property)DRAG_DOWN, false);
/*     */     }
/* 105 */     if (belowState.is(Blocks.MAGMA_BLOCK)) {
/* 106 */       return (BlockState)Blocks.BUBBLE_COLUMN.defaultBlockState().setValue((Property)DRAG_DOWN, true);
/*     */     }
/*     */     
/* 109 */     return Blocks.WATER.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 114 */     double x = pos.getX();
/* 115 */     double y = pos.getY();
/* 116 */     double z = pos.getZ();
/*     */     
/* 118 */     if ((Boolean)state.getValue((Property)DRAG_DOWN)) {
/* 119 */       level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.CURRENT_DOWN, x + 0.5D, y + 0.8D, z, 0.0D, 0.0D, 0.0D);
/* 120 */       if (random.nextInt(200) == 0) {
/* 121 */         level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } else {
/* 124 */       level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.BUBBLE_COLUMN_UP, x + 0.5D, y, z + 0.5D, 0.0D, 0.04D, 0.0D);
/* 125 */       level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.BUBBLE_COLUMN_UP, x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0.0D, 0.04D, 0.0D);
/* 126 */       if (random.nextInt(200) == 0) {
/* 127 */         level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 134 */     ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     
/* 136 */     if (!state.canSurvive(level, pos) || directionToNeighbour == Direction.DOWN || (directionToNeighbour == Direction.UP && 
/*     */       
/* 138 */       !neighbourState.is(Blocks.BUBBLE_COLUMN) && canExistIn(neighbourState)))
/*     */     {
/* 140 */       ticks.scheduleTick(pos, this, 5);
/*     */     }
/*     */     
/* 143 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 148 */     BlockState belowState = level.getBlockState(pos.below());
/*     */     
/* 150 */     return (belowState.is(Blocks.BUBBLE_COLUMN) || belowState.is(Blocks.MAGMA_BLOCK) || belowState.is(Blocks.SOUL_SAND));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 155 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState state) {
/* 160 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 165 */     builder.add(new Property[] { (Property)DRAG_DOWN });
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 170 */     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/* 171 */     return new ItemStack((net.minecraft.world.level.ItemLike)Items.WATER_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<net.minecraft.sounds.SoundEvent> getPickupSound() {
/* 176 */     return Fluids.WATER.getPickupSound();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BubbleColumnBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */