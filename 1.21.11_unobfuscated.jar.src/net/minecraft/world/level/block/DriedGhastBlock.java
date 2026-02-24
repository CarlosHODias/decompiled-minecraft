/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DriedGhastBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*  39 */   public static final MapCodec<DriedGhastBlock> CODEC = simpleCodec(DriedGhastBlock::new);
/*     */   public static final int MAX_HYDRATION_LEVEL = 3;
/*     */   
/*     */   public MapCodec<DriedGhastBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  47 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty HYDRATION_LEVEL = BlockStateProperties.DRIED_GHAST_HYDRATION_LEVELS;
/*  48 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   public static final int HYDRATION_TICK_DELAY = 5000;
/*  51 */   private static final VoxelShape SHAPE = Block.column(10.0D, 10.0D, 0.0D, 10.0D);
/*     */   
/*     */   public DriedGhastBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)HYDRATION_LEVEL, 0)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  60 */     builder.add(new Property[] { (Property)FACING, (Property)HYDRATION_LEVEL, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  65 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  66 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  74 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public int getHydrationLevel(BlockState state) {
/*  78 */     return (Integer)state.getValue((Property)HYDRATION_LEVEL);
/*     */   }
/*     */   
/*     */   private boolean isReadyToSpawn(BlockState state) {
/*  82 */     return (getHydrationLevel(state) == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos position, RandomSource random) {
/*  87 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  88 */       tickWaterlogged(state, level, position, random);
/*     */       return;
/*     */     } 
/*  91 */     int hydrationLevel = getHydrationLevel(state);
/*  92 */     if (hydrationLevel > 0) {
/*  93 */       level.setBlock(position, (BlockState)state.setValue((Property)HYDRATION_LEVEL, hydrationLevel - 1), 2);
/*  94 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, position, GameEvent.Context.of(state));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickWaterlogged(BlockState state, ServerLevel level, BlockPos position, RandomSource random) {
/*  99 */     if (!isReadyToSpawn(state)) {
/* 100 */       level.playSound(null, position, SoundEvents.DRIED_GHAST_TRANSITION, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 101 */       level.setBlock(position, (BlockState)state.setValue((Property)HYDRATION_LEVEL, getHydrationLevel(state) + 1), 2);
/* 102 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, position, GameEvent.Context.of(state));
/*     */     } else {
/* 104 */       spawnGhastling(level, position, state);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnGhastling(ServerLevel level, BlockPos position, BlockState state) {
/* 109 */     level.removeBlock(position, false);
/*     */     
/* 111 */     HappyGhast ghastling = (HappyGhast)EntityType.HAPPY_GHAST.create((Level)level, net.minecraft.world.entity.EntitySpawnReason.BREEDING);
/* 112 */     if (ghastling != null) {
/* 113 */       Vec3 spawnAt = position.getBottomCenter();
/* 114 */       ghastling.setBaby(true);
/* 115 */       float blockRotation = Direction.getYRot((Direction)state.getValue((Property)FACING));
/* 116 */       ghastling.setYHeadRot(blockRotation);
/* 117 */       ghastling.snapTo(spawnAt.x(), spawnAt.y(), spawnAt.z(), blockRotation, 0.0F);
/* 118 */       level.addFreshEntity((Entity)ghastling);
/* 119 */       level.playSound(null, (Entity)ghastling, SoundEvents.GHASTLING_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 126 */     double x = pos.getX() + 0.5D;
/* 127 */     double y = pos.getY() + 0.5D;
/* 128 */     double z = pos.getZ() + 0.5D;
/*     */     
/* 130 */     if (!((Boolean)state.getValue((Property)WATERLOGGED))) {
/* 131 */       if (random.nextInt(40) == 0 && level.getBlockState(pos.below()).is(BlockTags.TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS)) {
/* 132 */         level.playLocalSound(x, y, z, SoundEvents.DRIED_GHAST_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */       }
/* 134 */       if (random.nextInt(6) == 0) {
/* 135 */         level.addParticle((ParticleOptions)ParticleTypes.WHITE_SMOKE, x, y, z, 0.0D, 0.02D, 0.0D);
/*     */       }
/*     */     } else {
/* 138 */       if (random.nextInt(40) == 0) {
/* 139 */         level.playLocalSound(x, y, z, SoundEvents.DRIED_GHAST_AMBIENT_WATER, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */       }
/* 141 */       if (random.nextInt(6) == 0) {
/* 142 */         level.addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, x + ((random.nextFloat() * 2.0F - 1.0F) / 3.0F), y + 0.4D, z + ((random.nextFloat() * 2.0F - 1.0F) / 3.0F), 0.0D, random.nextFloat(), 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 149 */     if (((Boolean)state.getValue((Property)WATERLOGGED) || (Integer)state.getValue((Property)HYDRATION_LEVEL) > 0) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 150 */       level.scheduleTick(pos, this, 5000);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 156 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 157 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 158 */     return (BlockState)((BlockState)super.getStateForPlacement(context).setValue((Property)WATERLOGGED, isWaterSource)).setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 163 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 164 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 166 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 171 */     if ((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED) || fluidState.getType() != Fluids.WATER) {
/* 172 */       return false;
/*     */     }
/* 174 */     if (!level.isClientSide()) {
/* 175 */       level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, true), 3);
/* 176 */       level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay((LevelReader)level));
/* 177 */       level.playSound(null, pos, SoundEvents.DRIED_GHAST_PLACE_IN_WATER, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 185 */     super.setPlacedBy(level, pos, state, by, itemStack);
/* 186 */     level.playSound(null, pos, (Boolean)state.getValue((Property)WATERLOGGED) ? SoundEvents.DRIED_GHAST_PLACE_IN_WATER : SoundEvents.DRIED_GHAST_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 191 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DriedGhastBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */