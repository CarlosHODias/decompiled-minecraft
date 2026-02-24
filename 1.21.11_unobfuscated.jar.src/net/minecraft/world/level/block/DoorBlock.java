/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DoorBlock extends Block {
/*     */   static {
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(DoorBlock::type), (App)propertiesCodec()).apply((Applicative)i, DoorBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<DoorBlock> CODEC;
/*     */   
/*     */   public MapCodec<? extends DoorBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  55 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*  56 */   public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
/*  57 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  58 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  60 */   private static final java.util.Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected DoorBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/*  65 */     super(properties.sound(type.soundType()));
/*  66 */     this.type = type;
/*  67 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, false)).setValue((Property)HINGE, (Comparable)DoorHingeSide.LEFT)).setValue((Property)POWERED, false)).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER));
/*     */   }
/*     */   
/*     */   public BlockSetType type() {
/*  71 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  76 */     Direction direction = (Direction)state.getValue((Property)FACING);
/*  77 */     Direction doorDirection = (Boolean)state.getValue((Property)OPEN) ? ((state.getValue((Property)HINGE) == DoorHingeSide.RIGHT) ? direction.getCounterClockWise() : direction.getClockWise()) : direction;
/*     */     
/*  79 */     return SHAPES.get(doorDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  84 */     DoubleBlockHalf half = (DoubleBlockHalf)state.getValue((Property)HALF);
/*  85 */     if (directionToNeighbour.getAxis() == Direction.Axis.Y && ((half == DoubleBlockHalf.LOWER) ? true : false) == ((directionToNeighbour == Direction.UP) ? true : false)) {
/*     */       
/*  87 */       if (neighbourState.getBlock() instanceof DoorBlock && neighbourState.getValue((Property)HALF) != half)
/*     */       {
/*  89 */         return (BlockState)neighbourState.setValue((Property)HALF, (Comparable)half);
/*     */       }
/*  91 */       return Blocks.AIR.defaultBlockState();
/*     */     } 
/*     */ 
/*     */     
/*  95 */     if (half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 104 */     if (explosion.canTriggerBlocks() && state.getValue((Property)HALF) == DoubleBlockHalf.LOWER && this.type.canOpenByWindCharge() && !((Boolean)state.getValue((Property)POWERED))) {
/* 105 */       setOpen(null, (Level)level, state, pos, !isOpen(state));
/*     */     }
/* 107 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 112 */     if (!level.isClientSide() && (player.preventsBlockDrops() || !player.hasCorrectToolForDrops(state))) {
/* 113 */       DoublePlantBlock.preventDropFromBottomPart(level, pos, state, player);
/*     */     }
/*     */     
/* 116 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 121 */     switch (type) { default: throw new MatchException(null, null);case LAND: case AIR: case WATER: break; }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 129 */     BlockPos pos = context.getClickedPos();
/* 130 */     Level level = context.getLevel();
/* 131 */     if (pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context)) {
/* 132 */       boolean powered = (level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above()));
/*     */       
/* 134 */       return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection())).setValue((Property)HINGE, (Comparable)getHinge(context))).setValue((Property)POWERED, powered)).setValue((Property)OPEN, powered)).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity by, ItemStack itemStack) {
/* 142 */     level.setBlock(pos.above(), (BlockState)state.setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
/*     */   }
/*     */   
/*     */   private DoorHingeSide getHinge(BlockPlaceContext context) {
/* 146 */     Level level = context.getLevel();
/* 147 */     BlockPos pos = context.getClickedPos();
/* 148 */     Direction placeDirection = context.getHorizontalDirection();
/* 149 */     BlockPos abovePos = pos.above();
/*     */     
/* 151 */     Direction leftDirection = placeDirection.getCounterClockWise();
/* 152 */     BlockPos leftPos = pos.relative(leftDirection);
/* 153 */     BlockState leftState = level.getBlockState(leftPos);
/* 154 */     BlockPos leftAbovePos = abovePos.relative(leftDirection);
/* 155 */     BlockState leftAboveState = level.getBlockState(leftAbovePos);
/*     */     
/* 157 */     Direction rightDirection = placeDirection.getClockWise();
/* 158 */     BlockPos rightPos = pos.relative(rightDirection);
/* 159 */     BlockState rightState = level.getBlockState(rightPos);
/* 160 */     BlockPos rightAbovePos = abovePos.relative(rightDirection);
/* 161 */     BlockState rightAboveState = level.getBlockState(rightAbovePos);
/*     */     
/* 163 */     int solidBlockBalance = (leftState.isCollisionShapeFullBlock((BlockGetter)level, leftPos) ? -1 : 0) + (
/* 164 */       leftAboveState.isCollisionShapeFullBlock((BlockGetter)level, leftAbovePos) ? -1 : 0) + (
/* 165 */       rightState.isCollisionShapeFullBlock((BlockGetter)level, rightPos) ? 1 : 0) + (
/* 166 */       rightAboveState.isCollisionShapeFullBlock((BlockGetter)level, rightAbovePos) ? 1 : 0);
/*     */     
/* 168 */     boolean doorLeft = (leftState.getBlock() instanceof DoorBlock && leftState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/* 169 */     boolean doorRight = (rightState.getBlock() instanceof DoorBlock && rightState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*     */     
/* 171 */     if ((doorLeft && !doorRight) || solidBlockBalance > 0) {
/* 172 */       return DoorHingeSide.RIGHT;
/*     */     }
/* 174 */     if ((doorRight && !doorLeft) || solidBlockBalance < 0) {
/* 175 */       return DoorHingeSide.LEFT;
/*     */     }
/*     */     
/* 178 */     int stepX = placeDirection.getStepX();
/* 179 */     int stepZ = placeDirection.getStepZ();
/*     */     
/* 181 */     Vec3 clickLocation = context.getClickLocation();
/* 182 */     double clickX = clickLocation.x - pos.getX();
/* 183 */     double clickZ = clickLocation.z - pos.getZ();
/*     */     
/* 185 */     return ((stepX < 0 && clickZ < 0.5D) || (stepX > 0 && clickZ > 0.5D) || (stepZ < 0 && clickX > 0.5D) || (stepZ > 0 && clickX < 0.5D)) ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/* 190 */     if (!this.type.canOpenByHand()) {
/* 191 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 194 */     state = (BlockState)state.cycle((Property)OPEN);
/* 195 */     level.setBlock(pos, state, 10);
/* 196 */     playSound((Entity)player, level, pos, (Boolean)state.getValue((Property)OPEN));
/* 197 */     level.gameEvent((Entity)player, isOpen(state) ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, pos);
/* 198 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen(BlockState state) {
/* 206 */     return (Boolean)state.getValue((Property)OPEN);
/*     */   }
/*     */   
/*     */   public void setOpen(Entity sourceEntity, Level level, BlockState state, BlockPos pos, boolean shouldOpen) {
/* 210 */     if (!state.is(this) || (Boolean)state.getValue((Property)OPEN) == shouldOpen) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     level.setBlock(pos, (BlockState)state.setValue((Property)OPEN, shouldOpen), 10);
/* 215 */     playSound(sourceEntity, level, pos, shouldOpen);
/* 216 */     level.gameEvent(sourceEntity, shouldOpen ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: aload_3
/*     */     //   2: invokevirtual hasNeighborSignal : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   5: ifne -> 41
/*     */     //   8: aload_2
/*     */     //   9: aload_3
/*     */     //   10: aload_1
/*     */     //   11: getstatic net/minecraft/world/level/block/DoorBlock.HALF : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   14: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   17: getstatic net/minecraft/world/level/block/state/properties/DoubleBlockHalf.LOWER : Lnet/minecraft/world/level/block/state/properties/DoubleBlockHalf;
/*     */     //   20: if_acmpne -> 29
/*     */     //   23: getstatic net/minecraft/core/Direction.UP : Lnet/minecraft/core/Direction;
/*     */     //   26: goto -> 32
/*     */     //   29: getstatic net/minecraft/core/Direction.DOWN : Lnet/minecraft/core/Direction;
/*     */     //   32: invokevirtual relative : (Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;
/*     */     //   35: invokevirtual hasNeighborSignal : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   38: ifeq -> 45
/*     */     //   41: iconst_1
/*     */     //   42: goto -> 46
/*     */     //   45: iconst_0
/*     */     //   46: istore #7
/*     */     //   48: aload_0
/*     */     //   49: invokevirtual defaultBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   52: aload #4
/*     */     //   54: invokevirtual is : (Lnet/minecraft/world/level/block/Block;)Z
/*     */     //   57: ifne -> 161
/*     */     //   60: iload #7
/*     */     //   62: aload_1
/*     */     //   63: getstatic net/minecraft/world/level/block/DoorBlock.POWERED : Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
/*     */     //   66: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   69: checkcast java/lang/Boolean
/*     */     //   72: invokevirtual booleanValue : ()Z
/*     */     //   75: if_icmpeq -> 161
/*     */     //   78: iload #7
/*     */     //   80: aload_1
/*     */     //   81: getstatic net/minecraft/world/level/block/DoorBlock.OPEN : Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
/*     */     //   84: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   87: checkcast java/lang/Boolean
/*     */     //   90: invokevirtual booleanValue : ()Z
/*     */     //   93: if_icmpeq -> 125
/*     */     //   96: aload_0
/*     */     //   97: aconst_null
/*     */     //   98: aload_2
/*     */     //   99: aload_3
/*     */     //   100: iload #7
/*     */     //   102: invokevirtual playSound : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)V
/*     */     //   105: aload_2
/*     */     //   106: aconst_null
/*     */     //   107: iload #7
/*     */     //   109: ifeq -> 118
/*     */     //   112: getstatic net/minecraft/world/level/gameevent/GameEvent.BLOCK_OPEN : Lnet/minecraft/core/Holder$Reference;
/*     */     //   115: goto -> 121
/*     */     //   118: getstatic net/minecraft/world/level/gameevent/GameEvent.BLOCK_CLOSE : Lnet/minecraft/core/Holder$Reference;
/*     */     //   121: aload_3
/*     */     //   122: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   125: aload_2
/*     */     //   126: aload_3
/*     */     //   127: aload_1
/*     */     //   128: getstatic net/minecraft/world/level/block/DoorBlock.POWERED : Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
/*     */     //   131: iload #7
/*     */     //   133: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */     //   136: invokevirtual setValue : (Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
/*     */     //   139: checkcast net/minecraft/world/level/block/state/BlockState
/*     */     //   142: getstatic net/minecraft/world/level/block/DoorBlock.OPEN : Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
/*     */     //   145: iload #7
/*     */     //   147: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */     //   150: invokevirtual setValue : (Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
/*     */     //   153: checkcast net/minecraft/world/level/block/state/BlockState
/*     */     //   156: iconst_2
/*     */     //   157: invokevirtual setBlock : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
/*     */     //   160: pop
/*     */     //   161: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #221	-> 0
/*     */     //   #222	-> 48
/*     */     //   #223	-> 78
/*     */     //   #224	-> 96
/*     */     //   #225	-> 105
/*     */     //   #227	-> 125
/*     */     //   #229	-> 161
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	162	0	this	Lnet/minecraft/world/level/block/DoorBlock;
/*     */     //   0	162	1	state	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   0	162	2	level	Lnet/minecraft/world/level/Level;
/*     */     //   0	162	3	pos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	162	4	block	Lnet/minecraft/world/level/block/Block;
/*     */     //   0	162	5	orientation	Lnet/minecraft/world/level/redstone/Orientation;
/*     */     //   0	162	6	movedByPiston	Z
/*     */     //   48	114	7	signal	Z
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 234 */     BlockPos below = pos.below();
/* 235 */     BlockState belowState = level.getBlockState(below);
/* 236 */     if (state.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
/* 237 */       return belowState.isFaceSturdy((BlockGetter)level, below, Direction.UP);
/*     */     }
/* 239 */     return belowState.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void playSound(Entity entity, Level level, BlockPos pos, boolean open) {
/* 244 */     level.playSound(entity, pos, open ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 249 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 254 */     if (mirror == Mirror.NONE) {
/* 255 */       return state;
/*     */     }
/* 257 */     return (BlockState)state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING))).cycle((Property)HINGE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getSeed(BlockState state, BlockPos pos) {
/* 262 */     return Mth.getSeed(pos.getX(), pos.below((state.getValue((Property)HALF) == DoubleBlockHalf.LOWER) ? 0 : 1).getY(), pos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 267 */     builder.add(new Property[] { (Property)HALF, (Property)FACING, (Property)OPEN, (Property)HINGE, (Property)POWERED });
/*     */   }
/*     */   
/*     */   public static boolean isWoodenDoor(Level level, BlockPos pos) {
/* 271 */     return isWoodenDoor(level.getBlockState(pos));
/*     */   }
/*     */   
/*     */   public static boolean isWoodenDoor(BlockState state) {
/* 275 */     Block block = state.getBlock(); if (block instanceof DoorBlock) { DoorBlock door = (DoorBlock)block; if (door.type().canOpenByHand()); }  return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */