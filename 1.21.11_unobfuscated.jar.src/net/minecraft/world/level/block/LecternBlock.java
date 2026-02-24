/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.LecternBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LecternBlock extends BaseEntityBlock {
/*  46 */   public static final MapCodec<LecternBlock> CODEC = simpleCodec(LecternBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<LecternBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  53 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  54 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  55 */   public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;
/*     */   
/*  57 */   private static final VoxelShape SHAPE_COLLISION = Shapes.or(
/*  58 */       Block.column(16.0D, 0.0D, 2.0D), 
/*  59 */       Block.column(8.0D, 2.0D, 14.0D));
/*     */ 
/*     */   
/*  62 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Shapes.or(
/*  63 */         Block.boxZ(16.0D, 10.0D, 14.0D, 1.0D, 5.333333D), new VoxelShape[] {
/*  64 */           Block.boxZ(16.0D, 12.0D, 16.0D, 5.333333D, 9.666667D), 
/*  65 */           Block.boxZ(16.0D, 14.0D, 18.0D, 9.666667D, 14.0D), SHAPE_COLLISION
/*     */         }));
/*     */   
/*     */   private static final int PAGE_CHANGE_IMPULSE_TICKS = 2;
/*     */ 
/*     */   
/*     */   protected LecternBlock(BlockBehaviour.Properties properties) {
/*  72 */     super(properties);
/*  73 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, false)).setValue((Property)HAS_BOOK, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState state) {
/*  78 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  88 */     Level level = context.getLevel();
/*  89 */     ItemStack itemStack = context.getItemInHand();
/*  90 */     Player player = context.getPlayer();
/*     */     
/*     */     boolean hasBook = false;
/*  93 */     if (!level.isClientSide() && player != null && player.canUseGameMasterBlocks()) {
/*  94 */       TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData<BlockEntityType<?>>)itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/*  95 */       if (blockEntityData != null && blockEntityData.contains("Book")) {
/*  96 */         hasBook = true;
/*     */       }
/*     */     } 
/*  99 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)HAS_BOOK, hasBook);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 104 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 109 */     return SHAPES.get(state.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 114 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 119 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 124 */     builder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)HAS_BOOK });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 129 */     return (BlockEntity)new LecternBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public static boolean tryPlaceBook(LivingEntity sourceEntity, Level level, BlockPos pos, BlockState state, ItemStack item) {
/* 133 */     if (!((Boolean)state.getValue((Property)HAS_BOOK))) {
/* 134 */       if (!level.isClientSide()) {
/* 135 */         placeBook(sourceEntity, level, pos, state, item);
/*     */       }
/* 137 */       return true;
/*     */     } 
/*     */     
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   private static void placeBook(LivingEntity sourceEntity, Level level, BlockPos pos, BlockState state, ItemStack book) {
/* 144 */     BlockEntity entity = level.getBlockEntity(pos);
/* 145 */     if (entity instanceof LecternBlockEntity) { LecternBlockEntity lectern = (LecternBlockEntity)entity;
/* 146 */       lectern.setBook(book.consumeAndReturn(1, sourceEntity));
/* 147 */       resetBookState((Entity)sourceEntity, level, pos, state, true);
/* 148 */       level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F); }
/*     */   
/*     */   }
/*     */   
/*     */   public static void resetBookState(Entity sourceEntity, Level level, BlockPos pos, BlockState state, boolean hasBook) {
/* 153 */     BlockState newState = (BlockState)((BlockState)state.setValue((Property)POWERED, false)).setValue((Property)HAS_BOOK, hasBook);
/* 154 */     level.setBlock(pos, newState, 3);
/* 155 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
/* 156 */     updateBelow(level, pos, state);
/*     */   }
/*     */   
/*     */   public static void signalPageChange(Level level, BlockPos pos, BlockState state) {
/* 160 */     changePowered(level, pos, state, true);
/* 161 */     level.scheduleTick(pos, state.getBlock(), 2);
/* 162 */     level.levelEvent(1043, pos, 0);
/*     */   }
/*     */   
/*     */   private static void changePowered(Level level, BlockPos pos, BlockState state, boolean isPowered) {
/* 166 */     level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, isPowered), 3);
/* 167 */     updateBelow(level, pos, state);
/*     */   }
/*     */   
/*     */   private static void updateBelow(Level level, BlockPos pos, BlockState state) {
/* 171 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, ((Direction)state.getValue((Property)FACING)).getOpposite(), Direction.UP);
/* 172 */     level.updateNeighborsAt(pos.below(), state.getBlock(), orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 177 */     changePowered((Level)level, pos, state, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 182 */     if ((Boolean)state.getValue((Property)POWERED)) {
/* 183 */       updateBelow((Level)level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 194 */     return (Boolean)state.getValue((Property)POWERED) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 199 */     return (direction == Direction.UP && (Boolean)state.getValue((Property)POWERED)) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 209 */     if ((Boolean)state.getValue((Property)HAS_BOOK)) {
/* 210 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 211 */       if (blockEntity instanceof LecternBlockEntity) {
/* 212 */         return ((LecternBlockEntity)blockEntity).getRedstoneSignal();
/*     */       }
/*     */     } 
/*     */     
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 221 */     if ((Boolean)state.getValue((Property)HAS_BOOK)) {
/* 222 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/* 225 */     if (itemStack.is(ItemTags.LECTERN_BOOKS)) {
/* 226 */       return tryPlaceBook((LivingEntity)player, level, pos, state, itemStack) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 229 */     if (itemStack.isEmpty() && hand == InteractionHand.MAIN_HAND)
/*     */     {
/*     */       
/* 232 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 235 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 240 */     if ((Boolean)state.getValue((Property)HAS_BOOK)) {
/* 241 */       if (!level.isClientSide()) {
/* 242 */         openScreen(level, pos, player);
/*     */       }
/* 244 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/* 248 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/* 253 */     if (!((Boolean)state.getValue((Property)HAS_BOOK))) {
/* 254 */       return null;
/*     */     }
/*     */     
/* 257 */     return super.getMenuProvider(state, level, pos);
/*     */   }
/*     */   
/*     */   private void openScreen(Level level, BlockPos pos, Player player) {
/* 261 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 262 */     if (blockEntity instanceof LecternBlockEntity) {
/* 263 */       player.openMenu((MenuProvider)blockEntity);
/* 264 */       player.awardStat(Stats.INTERACT_WITH_LECTERN);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 270 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LecternBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */