/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BellBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BellBlock extends BaseEntityBlock {
/*  45 */   public static final MapCodec<BellBlock> CODEC = simpleCodec(BellBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BellBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */   
/*  52 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  53 */   public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
/*  54 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  56 */   private static final VoxelShape BELL_SHAPE = Shapes.or(
/*  57 */       Block.column(6.0D, 6.0D, 13.0D), 
/*  58 */       Block.column(8.0D, 4.0D, 6.0D));
/*     */ 
/*     */   
/*  61 */   private static final VoxelShape SHAPE_CEILING = Shapes.or(BELL_SHAPE, Block.column(2.0D, 13.0D, 16.0D));
/*  62 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_FLOOR = Shapes.rotateHorizontalAxis(Block.cube(16.0D, 16.0D, 8.0D));
/*  63 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_DOUBLE_WALL = Shapes.rotateHorizontalAxis(Shapes.or(BELL_SHAPE, Block.column(2.0D, 16.0D, 13.0D, 15.0D)));
/*  64 */   private static final Map<Direction, VoxelShape> SHAPE_SINGLE_WALL = Shapes.rotateHorizontal(Shapes.or(BELL_SHAPE, Block.boxZ(2.0D, 13.0D, 15.0D, 0.0D, 13.0D)));
/*     */   
/*     */   public static final int EVENT_BELL_RING = 1;
/*     */   
/*     */   public BellBlock(BlockBehaviour.Properties properties) {
/*  69 */     super(properties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)ATTACHMENT, (Comparable)BellAttachType.FLOOR)).setValue((Property)POWERED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  75 */     boolean signal = level.hasNeighborSignal(pos);
/*     */     
/*  77 */     if (signal != (Boolean)state.getValue((Property)POWERED)) {
/*  78 */       if (signal) {
/*  79 */         attemptToRing(level, pos, null);
/*     */       }
/*  81 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, signal), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
/*  87 */     Entity owner = projectile.getOwner();
/*  88 */     Player player = (Player)owner, playerOwner = (owner instanceof Player) ? player : null;
/*  89 */     onHit(level, state, hitResult, playerOwner, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  94 */     return onHit(level, state, hitResult, player, true) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public boolean onHit(Level level, BlockState state, BlockHitResult hitResult, Player player, boolean requireHitFromCorrectSide) {
/*  98 */     Direction direction = hitResult.getDirection();
/*  99 */     BlockPos blockPos = hitResult.getBlockPos();
/* 100 */     boolean properHit = (!requireHitFromCorrectSide || isProperHit(state, direction, (hitResult.getLocation()).y - blockPos.getY()));
/* 101 */     if (properHit) {
/* 102 */       boolean didRing = attemptToRing((Entity)player, level, blockPos, direction);
/* 103 */       if (didRing && player != null) {
/* 104 */         player.awardStat(Stats.BELL_RING);
/*     */       }
/* 106 */       return true;
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isProperHit(BlockState state, Direction clickedDirection, double clickY) {
/* 112 */     if (clickedDirection.getAxis() == Direction.Axis.Y || clickY > 0.8123999834060669D) {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 117 */     BellAttachType attachType = (BellAttachType)state.getValue((Property)ATTACHMENT);
/*     */     
/* 119 */     switch (attachType) {
/*     */       case FLOOR:
/* 121 */         return (facing.getAxis() == clickedDirection.getAxis());
/*     */       case SINGLE_WALL:
/*     */       case DOUBLE_WALL:
/* 124 */         return (facing.getAxis() != clickedDirection.getAxis());
/*     */       case CEILING:
/* 126 */         return true;
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attemptToRing(Level level, BlockPos pos, Direction direction) {
/* 133 */     return attemptToRing(null, level, pos, direction);
/*     */   }
/*     */   
/*     */   public boolean attemptToRing(Entity ringingEntity, Level level, BlockPos pos, Direction direction) {
/* 137 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 138 */     if (!level.isClientSide() && blockEntity instanceof BellBlockEntity) {
/* 139 */       if (direction == null) {
/* 140 */         direction = (Direction)level.getBlockState(pos).getValue((Property)FACING);
/*     */       }
/* 142 */       ((BellBlockEntity)blockEntity).onHit(direction);
/* 143 */       level.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
/* 144 */       level.gameEvent(ringingEntity, (Holder)net.minecraft.world.level.gameevent.GameEvent.BLOCK_CHANGE, pos);
/* 145 */       return true;
/*     */     } 
/* 147 */     return false;
/*     */   }
/*     */   
/*     */   private VoxelShape getVoxelShape(BlockState state) {
/* 151 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 152 */     switch ((BellAttachType)state.getValue((Property)ATTACHMENT)) { default: throw new MatchException(null, null);case FLOOR: case CEILING: case SINGLE_WALL: case DOUBLE_WALL: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 156 */       SHAPE_DOUBLE_WALL.get(facing.getAxis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 162 */     return getVoxelShape(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 167 */     return getVoxelShape(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 173 */     Direction clickedFace = context.getClickedFace();
/* 174 */     BlockPos pos = context.getClickedPos();
/* 175 */     Level level = context.getLevel();
/* 176 */     Direction.Axis axis = clickedFace.getAxis();
/*     */     
/* 178 */     if (axis == Direction.Axis.Y) {
/* 179 */       BlockState state = (BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHMENT, (clickedFace == Direction.DOWN) ? (Comparable)BellAttachType.CEILING : (Comparable)BellAttachType.FLOOR)).setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/*     */       
/* 181 */       if (state.canSurvive((LevelReader)context.getLevel(), pos)) {
/* 182 */         return state;
/*     */       }
/*     */     } else {
/*     */       
/* 186 */       boolean doubleAttached = ((axis == Direction.Axis.X && level.getBlockState(pos.west()).isFaceSturdy((BlockGetter)level, pos.west(), Direction.EAST) && level.getBlockState(pos.east()).isFaceSturdy((BlockGetter)level, pos.east(), Direction.WEST)) || (axis == Direction.Axis.Z && 
/* 187 */         level.getBlockState(pos.north()).isFaceSturdy((BlockGetter)level, pos.north(), Direction.SOUTH) && level.getBlockState(pos.south()).isFaceSturdy((BlockGetter)level, pos.south(), Direction.NORTH)));
/*     */       
/* 189 */       BlockState state = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)clickedFace.getOpposite())).setValue((Property)ATTACHMENT, doubleAttached ? (Comparable)BellAttachType.DOUBLE_WALL : (Comparable)BellAttachType.SINGLE_WALL);
/*     */       
/* 191 */       if (state.canSurvive((LevelReader)context.getLevel(), context.getClickedPos())) {
/* 192 */         return state;
/*     */       }
/* 194 */       boolean canAttachBelow = level.getBlockState(pos.below()).isFaceSturdy((BlockGetter)level, pos.below(), Direction.UP);
/*     */       
/* 196 */       state = (BlockState)state.setValue((Property)ATTACHMENT, canAttachBelow ? (Comparable)BellAttachType.FLOOR : (Comparable)BellAttachType.CEILING);
/*     */       
/* 198 */       if (state.canSurvive((LevelReader)context.getLevel(), context.getClickedPos())) {
/* 199 */         return state;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 209 */     if (explosion.canTriggerBlocks()) {
/* 210 */       attemptToRing((Level)level, pos, null);
/*     */     }
/* 212 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 217 */     BellAttachType attachment = (BellAttachType)state.getValue((Property)ATTACHMENT);
/*     */     
/* 219 */     Direction connectedDirection = getConnectedDirection(state).getOpposite();
/* 220 */     if (connectedDirection == directionToNeighbour && !state.canSurvive(level, pos) && attachment != BellAttachType.DOUBLE_WALL) {
/* 221 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 224 */     if (directionToNeighbour.getAxis() == ((Direction)state.getValue((Property)FACING)).getAxis()) {
/* 225 */       if (attachment == BellAttachType.DOUBLE_WALL && !neighbourState.isFaceSturdy((BlockGetter)level, neighbourPos, directionToNeighbour))
/* 226 */         return (BlockState)((BlockState)state.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.SINGLE_WALL)).setValue((Property)FACING, (Comparable)directionToNeighbour.getOpposite()); 
/* 227 */       if (attachment == BellAttachType.SINGLE_WALL && connectedDirection.getOpposite() == directionToNeighbour && neighbourState.isFaceSturdy((BlockGetter)level, neighbourPos, (Direction)state.getValue((Property)FACING))) {
/* 228 */         return (BlockState)state.setValue((Property)ATTACHMENT, (Comparable)BellAttachType.DOUBLE_WALL);
/*     */       }
/*     */     } 
/*     */     
/* 232 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 237 */     Direction connectionDir = getConnectedDirection(state).getOpposite();
/*     */     
/* 239 */     if (connectionDir == Direction.UP) {
/* 240 */       return Block.canSupportCenter(level, pos.above(), Direction.DOWN);
/*     */     }
/* 242 */     return FaceAttachedHorizontalDirectionalBlock.canAttach(level, pos, connectionDir);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Direction getConnectedDirection(BlockState state) {
/* 247 */     switch ((BellAttachType)state.getValue((Property)ATTACHMENT)) {
/*     */       case CEILING:
/* 249 */         return Direction.DOWN;
/*     */       case FLOOR:
/* 251 */         return Direction.UP;
/*     */     } 
/* 253 */     return ((Direction)state.getValue((Property)FACING)).getOpposite();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 259 */     builder.add(new Property[] { (Property)FACING, (Property)ATTACHMENT, (Property)POWERED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 264 */     return (BlockEntity)new BellBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 269 */     return createTickerHelper(type, BlockEntityType.BELL, level.isClientSide() ? BellBlockEntity::clientTick : BellBlockEntity::serverTick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 279 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 284 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BellBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */