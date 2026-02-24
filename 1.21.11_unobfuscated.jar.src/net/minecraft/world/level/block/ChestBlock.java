/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LidBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
/*     */   public static final MapCodec<ChestBlock> CODEC;
/*     */   
/*     */   static {
/*  64 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)i, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<? extends ChestBlock> codec() {
/*  72 */     return CODEC;
/*     */   }
/*     */   
/*  75 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  76 */   public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
/*  77 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   public static final int EVENT_SET_OPEN_COUNT = 1;
/*  80 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 14.0D);
/*  81 */   private static final Map<Direction, VoxelShape> HALF_SHAPES = Shapes.rotateHorizontal(Block.boxZ(14.0D, 0.0D, 14.0D, 0.0D, 15.0D));
/*     */   
/*     */   private final SoundEvent openSound;
/*     */   private final SoundEvent closeSound;
/*     */   
/*     */   protected ChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/*  87 */     super(properties, blockEntityType);
/*  88 */     this.openSound = openSound;
/*  89 */     this.closeSound = closeSound;
/*  90 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)ChestType.SINGLE)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
/*  94 */     ChestType type = (ChestType)state.getValue((Property)TYPE);
/*  95 */     if (type == ChestType.SINGLE) {
/*  96 */       return DoubleBlockCombiner.BlockType.SINGLE;
/*     */     }
/*  98 */     if (type == ChestType.RIGHT) {
/*  99 */       return DoubleBlockCombiner.BlockType.FIRST;
/*     */     }
/* 101 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 106 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 107 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 109 */     if (chestCanConnectTo(neighbourState) && directionToNeighbour.getAxis().isHorizontal()) {
/* 110 */       ChestType neighbourType = (ChestType)neighbourState.getValue((Property)TYPE);
/* 111 */       if (state.getValue((Property)TYPE) == ChestType.SINGLE && neighbourType != ChestType.SINGLE && 
/* 112 */         state.getValue((Property)FACING) == neighbourState.getValue((Property)FACING) && getConnectedDirection(neighbourState) == directionToNeighbour.getOpposite()) {
/* 113 */         return (BlockState)state.setValue((Property)TYPE, (Comparable)neighbourType.getOpposite());
/*     */       }
/*     */     }
/* 116 */     else if (getConnectedDirection(state) == directionToNeighbour) {
/* 117 */       return (BlockState)state.setValue((Property)TYPE, (Comparable)ChestType.SINGLE);
/*     */     } 
/* 119 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public boolean chestCanConnectTo(BlockState blockState) {
/* 123 */     return blockState.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 128 */     switch ((ChestType)state.getValue((Property)TYPE)) { default: throw new MatchException(null, null);case SINGLE: case LEFT: case RIGHT: break; }  return 
/*     */       
/* 130 */       HALF_SHAPES.get(getConnectedDirection(state));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState state) {
/* 135 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 136 */     return (state.getValue((Property)TYPE) == ChestType.LEFT) ? facing.getClockWise() : facing.getCounterClockWise();
/*     */   }
/*     */   
/*     */   public static BlockPos getConnectedBlockPos(BlockPos pos, BlockState state) {
/* 140 */     Direction connectedDirection = getConnectedDirection(state);
/* 141 */     return pos.relative(connectedDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 146 */     ChestType type = ChestType.SINGLE;
/* 147 */     Direction facingDirection = context.getHorizontalDirection().getOpposite();
/* 148 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 150 */     boolean secondaryUse = context.isSecondaryUseActive();
/* 151 */     Direction clickedFace = context.getClickedFace();
/*     */     
/* 153 */     if (clickedFace.getAxis().isHorizontal() && secondaryUse) {
/* 154 */       Direction neighbourFacing = candidatePartnerFacing(context.getLevel(), context.getClickedPos(), clickedFace.getOpposite());
/* 155 */       if (neighbourFacing != null && neighbourFacing.getAxis() != clickedFace.getAxis()) {
/* 156 */         facingDirection = neighbourFacing;
/* 157 */         type = (facingDirection.getCounterClockWise() == clickedFace.getOpposite()) ? ChestType.RIGHT : ChestType.LEFT;
/*     */       } 
/*     */     } 
/* 160 */     if (type == ChestType.SINGLE && !secondaryUse) {
/* 161 */       type = getChestType(context.getLevel(), context.getClickedPos(), facingDirection);
/*     */     }
/*     */     
/* 164 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)facingDirection)).setValue((Property)TYPE, (Comparable)type)).setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */   }
/*     */   
/*     */   protected ChestType getChestType(Level level, BlockPos pos, Direction facingDirection) {
/* 168 */     if (facingDirection == candidatePartnerFacing(level, pos, facingDirection.getClockWise()))
/* 169 */       return ChestType.LEFT; 
/* 170 */     if (facingDirection == candidatePartnerFacing(level, pos, facingDirection.getCounterClockWise())) {
/* 171 */       return ChestType.RIGHT;
/*     */     }
/* 173 */     return ChestType.SINGLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 178 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 179 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 181 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   private Direction candidatePartnerFacing(Level level, BlockPos pos, Direction neighbourDirection) {
/* 185 */     BlockState state = level.getBlockState(pos.relative(neighbourDirection));
/* 186 */     return (chestCanConnectTo(state) && state.getValue((Property)TYPE) == ChestType.SINGLE) ? (Direction)state.getValue((Property)FACING) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 191 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/* 196 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 197 */       MenuProvider menuProvider = getMenuProvider(state, level, pos);
/* 198 */       if (menuProvider != null) {
/* 199 */         player.openMenu(menuProvider);
/* 200 */         player.awardStat(getOpenChestStat());
/* 201 */         net.minecraft.world.entity.monster.piglin.PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */       }  }
/*     */     
/* 204 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected Stat<Identifier> getOpenChestStat() {
/* 208 */     return Stats.CUSTOM.get(Stats.OPEN_CHEST);
/*     */   }
/*     */   
/*     */   public BlockEntityType<? extends ChestBlockEntity> blockEntityType() {
/* 212 */     return this.blockEntityType.get();
/*     */   }
/*     */   
/* 215 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>()
/*     */     {
/*     */       public Optional<Container> acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 218 */         return (Optional)Optional.of(new CompoundContainer((Container)first, (Container)second));
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Container> acceptSingle(ChestBlockEntity single) {
/* 223 */         return (Optional)Optional.of(single);
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Container> acceptNone() {
/* 228 */         return Optional.empty();
/*     */       }
/*     */     };
/*     */   
/*     */   public static Container getContainer(ChestBlock block, BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
/* 233 */     return ((Optional<Container>)block.combine(state, level, pos, ignoreBeingBlocked).<Optional<Container>>apply(CHEST_COMBINER)).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
/*     */     BiPredicate<LevelAccessor, BlockPos> predicate;
/* 239 */     if (ignoreBeingBlocked) {
/* 240 */       predicate = ((levelAccessor, blockPos) -> false);
/*     */     } else {
/* 242 */       predicate = ChestBlock::isChestBlockedAt;
/*     */     } 
/* 244 */     return DoubleBlockCombiner.combineWithNeigbour(this.blockEntityType.get(), ChestBlock::getBlockType, ChestBlock::getConnectedDirection, (Property<Direction>)FACING, state, (LevelAccessor)level, pos, predicate);
/*     */   }
/*     */   
/* 247 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>()
/*     */     {
/*     */       public Optional<MenuProvider> acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
/* 250 */         final CompoundContainer container = new CompoundContainer((Container)first, (Container)second);
/* 251 */         return Optional.of(new MenuProvider(this)
/*     */             {
/*     */               public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 254 */                 if (first.canOpen(player) && second.canOpen(player)) {
/* 255 */                   first.unpackLootTable(inventory.player);
/* 256 */                   second.unpackLootTable(inventory.player);
/*     */                   
/* 258 */                   return (AbstractContainerMenu)ChestMenu.sixRows(containerId, inventory, container);
/*     */                 } 
/* 260 */                 Direction connectedDirection = ChestBlock.getConnectedDirection(first.getBlockState());
/* 261 */                 Vec3 firstCenter = first.getBlockPos().getCenter();
/* 262 */                 Vec3 centerBetweenChests = firstCenter.add(connectedDirection.getStepX() / 2.0D, 0.0D, connectedDirection.getStepZ() / 2.0D);
/* 263 */                 BaseContainerBlockEntity.sendChestLockedNotifications(centerBetweenChests, player, getDisplayName());
/*     */                 
/* 265 */                 return null;
/*     */               }
/*     */ 
/*     */               
/*     */               public Component getDisplayName() {
/* 270 */                 if (first.hasCustomName()) {
/* 271 */                   return first.getDisplayName();
/*     */                 }
/* 273 */                 if (second.hasCustomName()) {
/* 274 */                   return second.getDisplayName();
/*     */                 }
/* 276 */                 return (Component)Component.translatable("container.chestDouble");
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<MenuProvider> acceptSingle(ChestBlockEntity single) {
/* 283 */         return (Optional)Optional.of(single);
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<MenuProvider> acceptNone() {
/* 288 */         return Optional.empty();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 294 */     return ((Optional<MenuProvider>)combine(state, level, pos, false).<Optional<MenuProvider>>apply(MENU_PROVIDER_COMBINER)).orElse(null);
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity entity) {
/* 298 */     return new DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction>()
/*     */       {
/*     */         public Float2FloatFunction acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 301 */           return partialTickTime -> Math.max(first.getOpenNess(partialTickTime), second.getOpenNess(partialTickTime));
/*     */         }
/*     */ 
/*     */         
/*     */         public Float2FloatFunction acceptSingle(ChestBlockEntity single) {
/* 306 */           Objects.requireNonNull(single); return single::getOpenNess;
/*     */         }
/*     */ 
/*     */         
/*     */         public Float2FloatFunction acceptNone() {
/* 311 */           Objects.requireNonNull(entity); return entity::getOpenNess;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 318 */     return (BlockEntity)new ChestBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 323 */     return level.isClientSide() ? createTickerHelper(type, (BlockEntityType)blockEntityType(), ChestBlockEntity::lidAnimateTick) : null;
/*     */   }
/*     */   
/*     */   public static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) {
/* 327 */     return (isBlockedChestByBlock((BlockGetter)level, pos) || isCatSittingOnChest(level, pos));
/*     */   }
/*     */   
/*     */   private static boolean isBlockedChestByBlock(BlockGetter level, BlockPos pos) {
/* 331 */     BlockPos above = pos.above();
/* 332 */     return level.getBlockState(above).isRedstoneConductor(level, above);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isCatSittingOnChest(LevelAccessor level, BlockPos pos) {
/* 337 */     List<Cat> cats = level.getEntitiesOfClass(Cat.class, new net.minecraft.world.phys.AABB(pos.getX(), (pos.getY() + 1), pos.getZ(), (pos.getX() + 1), (pos.getY() + 2), (pos.getZ() + 1)));
/* 338 */     if (!cats.isEmpty()) {
/* 339 */       for (Cat cat : cats) {
/* 340 */         if (cat.isInSittingPose()) {
/* 341 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 350 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 355 */     return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, state, level, pos, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 360 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 365 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 370 */     builder.add(new Property[] { (Property)FACING, (Property)TYPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 375 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 380 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */     
/* 382 */     if (blockEntity instanceof ChestBlockEntity) {
/* 383 */       ((ChestBlockEntity)blockEntity).recheckOpen();
/*     */     }
/*     */   }
/*     */   
/*     */   public SoundEvent getOpenChestSound() {
/* 388 */     return this.openSound;
/*     */   }
/*     */   
/*     */   public SoundEvent getCloseChestSound() {
/* 392 */     return this.closeSound;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */