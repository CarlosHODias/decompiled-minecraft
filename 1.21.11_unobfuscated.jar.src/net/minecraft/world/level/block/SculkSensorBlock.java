/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkSensorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  45 */   public static final MapCodec<SculkSensorBlock> CODEC = simpleCodec(SculkSensorBlock::new); public static final int ACTIVE_TICKS = 30;
/*     */   public static final int COOLDOWN_TICKS = 10;
/*     */   
/*     */   public MapCodec<? extends SculkSensorBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;
/*  56 */   public static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = BlockStateProperties.POWER;
/*  57 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  59 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 8.0D); private static final float[] RESONANCE_PITCH_BEND;
/*     */   static {
/*  61 */     RESONANCE_PITCH_BEND = (float[])Util.make(new float[16], arr -> {
/*     */           int[] toneMap = { 
/*     */               0, 0, 2, 4, 6, 7, 9, 10, 12, 14, 
/*     */               15, 18, 19, 21, 22, 24 };
/*     */           for (int i = 0; i < 16; i++) {
/*     */             arr[i] = NoteBlock.getPitchFromNote(toneMap[i]);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SculkSensorBlock(BlockBehaviour.Properties properties) {
/*  75 */     super(properties);
/*  76 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PHASE, (Comparable)SculkSensorPhase.INACTIVE)).setValue((Property)POWER, 0)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  81 */     BlockPos pos = context.getClickedPos();
/*  82 */     FluidState replacedFluidState = context.getLevel().getFluidState(pos);
/*     */     
/*  84 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  89 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  90 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  92 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  97 */     if (getPhase(state) != SculkSensorPhase.ACTIVE) {
/*  98 */       if (getPhase(state) == SculkSensorPhase.COOLDOWN) {
/*  99 */         level.setBlock(pos, (BlockState)state.setValue((Property)PHASE, (Comparable)SculkSensorPhase.INACTIVE), 3);
/* 100 */         if (!((Boolean)state.getValue((Property)WATERLOGGED))) {
/* 101 */           level.playSound(null, pos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 108 */     deactivate((Level)level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 113 */     if (!level.isClientSide() && canActivate(onState) && entity.getType() != EntityType.WARDEN) {
/* 114 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 115 */       if (blockEntity instanceof SculkSensorBlockEntity) { SculkSensorBlockEntity sculkSensor = (SculkSensorBlockEntity)blockEntity; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 116 */           if (sculkSensor.getVibrationUser().canReceiveVibration(serverLevel, pos, (Holder)GameEvent.STEP, GameEvent.Context.of(onState)))
/* 117 */             sculkSensor.getListener().forceScheduleVibration(serverLevel, (Holder)GameEvent.STEP, GameEvent.Context.of(entity), entity.position());  }
/*     */          }
/*     */     
/*     */     } 
/* 121 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 126 */     if (level.isClientSide() || state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     if ((Integer)state.getValue((Property)POWER) > 0 && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 131 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWER, 0), 18);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 137 */     if (getPhase(state) == SculkSensorPhase.ACTIVE) {
/* 138 */       updateNeighbours((Level)level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 144 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 145 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 147 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static void updateNeighbours(Level level, BlockPos pos, BlockState state) {
/* 151 */     Block block = state.getBlock();
/* 152 */     level.updateNeighborsAt(pos, block);
/* 153 */     level.updateNeighborsAt(pos.below(), block);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 158 */     return (BlockEntity)new SculkSensorBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 163 */     if (!level.isClientSide()) {
/* 164 */       return createTickerHelper(type, BlockEntityType.SCULK_SENSOR, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser()));
/*     */     }
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 172 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 182 */     return (Integer)state.getValue((Property)POWER);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 187 */     if (direction == Direction.UP) {
/* 188 */       return state.getSignal(level, pos, direction);
/*     */     }
/*     */     
/* 191 */     return 0;
/*     */   }
/*     */   
/*     */   public static SculkSensorPhase getPhase(BlockState state) {
/* 195 */     return (SculkSensorPhase)state.getValue((Property)PHASE);
/*     */   }
/*     */   
/*     */   public static boolean canActivate(BlockState state) {
/* 199 */     return (getPhase(state) == SculkSensorPhase.INACTIVE);
/*     */   }
/*     */   
/*     */   public static void deactivate(Level level, BlockPos pos, BlockState state) {
/* 203 */     level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)PHASE, (Comparable)SculkSensorPhase.COOLDOWN)).setValue((Property)POWER, 0), 3);
/* 204 */     level.scheduleTick(pos, state.getBlock(), 10);
/* 205 */     updateNeighbours(level, pos, state);
/*     */   }
/*     */   
/*     */   @com.google.common.annotations.VisibleForTesting
/*     */   public int getActiveTicks() {
/* 210 */     return 30;
/*     */   }
/*     */   
/*     */   public void activate(Entity sourceEntity, Level level, BlockPos pos, BlockState state, int calculatedPower, int vibrationFrequency) {
/* 214 */     level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)PHASE, (Comparable)SculkSensorPhase.ACTIVE)).setValue((Property)POWER, calculatedPower), 3);
/*     */     
/* 216 */     level.scheduleTick(pos, state.getBlock(), getActiveTicks());
/* 217 */     updateNeighbours(level, pos, state);
/* 218 */     tryResonateVibration(sourceEntity, level, pos, vibrationFrequency);
/* 219 */     level.gameEvent(sourceEntity, (Holder)GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
/* 220 */     if (!((Boolean)state.getValue((Property)WATERLOGGED))) {
/* 221 */       level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void tryResonateVibration(Entity sourceEntity, Level level, BlockPos pos, int vibrationFrequency) {
/* 226 */     for (Direction direction : Direction.values()) {
/* 227 */       BlockPos relativePos = pos.relative(direction);
/* 228 */       BlockState blockState = level.getBlockState(relativePos);
/* 229 */       if (blockState.is(BlockTags.VIBRATION_RESONATORS)) {
/* 230 */         level.gameEvent(VibrationSystem.getResonanceEventByFrequency(vibrationFrequency), relativePos, GameEvent.Context.of(sourceEntity, blockState));
/* 231 */         float pitch = RESONANCE_PITCH_BEND[vibrationFrequency];
/* 232 */         level.playSound(null, relativePos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, pitch);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 239 */     if (getPhase(state) != SculkSensorPhase.ACTIVE) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     Direction dir = Direction.getRandom(random);
/*     */     
/* 245 */     if (dir == Direction.UP || dir == Direction.DOWN) {
/*     */       return;
/*     */     }
/*     */     
/* 249 */     double x = pos.getX() + 0.5D + ((dir.getStepX() == 0) ? (0.5D - random.nextDouble()) : (dir.getStepX() * 0.6D));
/* 250 */     double y = pos.getY() + 0.25D;
/* 251 */     double z = pos.getZ() + 0.5D + ((dir.getStepZ() == 0) ? (0.5D - random.nextDouble()) : (dir.getStepZ() * 0.6D));
/* 252 */     double ya = random.nextFloat() * 0.04D;
/* 253 */     level.addParticle((ParticleOptions)net.minecraft.core.particles.DustColorTransitionOptions.SCULK_TO_REDSTONE, x, y, z, 0.0D, ya, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 258 */     builder.add(new Property[] { (Property)PHASE, (Property)POWER, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 263 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 268 */     BlockEntity entity = level.getBlockEntity(pos);
/*     */     
/* 270 */     if (entity instanceof SculkSensorBlockEntity) { SculkSensorBlockEntity sculk = (SculkSensorBlockEntity)entity;
/* 271 */       return (getPhase(state) == SculkSensorPhase.ACTIVE) ? sculk.getLastVibrationFrequency() : 0; }
/*     */ 
/*     */     
/* 274 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 279 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 289 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 290 */     if (dropExperience)
/* 291 */       tryDropExperience(level, pos, tool, (IntProvider)ConstantInt.of(5)); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SculkSensorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */