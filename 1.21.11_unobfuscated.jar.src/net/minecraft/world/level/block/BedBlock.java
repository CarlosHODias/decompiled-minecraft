/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.BedRule;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BedBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BedBlock extends HorizontalDirectionalBlock implements EntityBlock {
/*     */   static {
/*  52 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(BedBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, BedBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<BedBlock> CODEC;
/*     */   
/*     */   public MapCodec<BedBlock> codec() {
/*  59 */     return CODEC;
/*     */   }
/*     */   
/*  62 */   public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
/*  63 */   public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED; private static final Map<Direction, VoxelShape> SHAPES; private final DyeColor color;
/*     */   static {
/*  65 */     SHAPES = (Map<Direction, VoxelShape>)Util.make(() -> {
/*     */           VoxelShape northWestLeg = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D), northEastLeg = Shapes.rotate(northWestLeg, OctahedralGroup.BLOCK_ROT_Y_90);
/*     */           return Shapes.rotateHorizontal(Shapes.or(Block.column(16.0D, 3.0D, 9.0D), new VoxelShape[] { northWestLeg, northEastLeg }));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BedBlock(DyeColor color, BlockBehaviour.Properties properties) {
/*  79 */     super(properties);
/*  80 */     this.color = color;
/*  81 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PART, (Comparable)BedPart.FOOT)).setValue((Property)OCCUPIED, false));
/*     */   }
/*     */   
/*     */   public static Direction getBedOrientation(BlockGetter level, BlockPos pos) {
/*  85 */     BlockState blockState = level.getBlockState(pos);
/*  86 */     return (blockState.getBlock() instanceof BedBlock) ? (Direction)blockState.getValue((Property)FACING) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/*  91 */     if (level.isClientSide()) {
/*  92 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     }
/*     */     
/*  95 */     if (state.getValue((Property)PART) != BedPart.HEAD) {
/*     */       
/*  97 */       pos = pos.relative((Direction)state.getValue((Property)FACING));
/*  98 */       state = level.getBlockState(pos);
/*  99 */       if (!state.is(this)) {
/* 100 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       }
/*     */     } 
/*     */     
/* 104 */     BedRule bedRule = (BedRule)level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos);
/* 105 */     if (bedRule.explodes()) {
/* 106 */       bedRule.errorMessage().ifPresent(message -> player.displayClientMessage(message, true));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 111 */       level.removeBlock(pos, false);
/*     */ 
/*     */       
/* 114 */       BlockPos blockPos = pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite());
/* 115 */       if (level.getBlockState(blockPos).is(this)) {
/* 116 */         level.removeBlock(blockPos, false);
/*     */       }
/*     */       
/* 119 */       Vec3 boomPos = pos.getCenter();
/* 120 */       level.explode(null, level.damageSources().badRespawnPointExplosion(boomPos), null, boomPos, 5.0F, true, Level.ExplosionInteraction.BLOCK);
/* 121 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 124 */     if ((Boolean)state.getValue((Property)OCCUPIED)) {
/* 125 */       if (!kickVillagerOutOfBed(level, pos)) {
/* 126 */         player.displayClientMessage((Component)Component.translatable("block.minecraft.bed.occupied"), true);
/*     */       }
/* 128 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */     
/* 131 */     player.startSleepInBed(pos)
/* 132 */       .ifLeft(problem -> {
/*     */           if (problem.message() != null) {
/*     */             player.displayClientMessage(problem.message(), true);
/*     */           }
/*     */         });
/* 137 */     return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
/* 144 */     List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new net.minecraft.world.phys.AABB(pos), LivingEntity::isSleeping);
/* 145 */     if (villagers.isEmpty()) {
/* 146 */       return false;
/*     */     }
/* 148 */     ((Villager)villagers.get(0)).stopSleeping();
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 154 */     super.fallOn(level, state, pos, entity, fallDistance * 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
/* 159 */     if (entity.isSuppressingBounce()) {
/* 160 */       super.updateEntityMovementAfterFallOn(level, entity);
/*     */     } else {
/* 162 */       bounceUp(entity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void bounceUp(Entity entity) {
/* 167 */     Vec3 movement = entity.getDeltaMovement();
/* 168 */     if (movement.y < 0.0D) {
/*     */       
/* 170 */       double factor = (entity instanceof LivingEntity) ? 1.0D : 0.8D;
/* 171 */       entity.setDeltaMovement(movement.x, -movement.y * 0.6600000262260437D * factor, movement.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 181 */     if (directionToNeighbour == getNeighbourDirection((BedPart)state.getValue((Property)PART), (Direction)state.getValue((Property)FACING))) {
/* 182 */       if (neighbourState.is(this) && neighbourState.getValue((Property)PART) != state.getValue((Property)PART)) {
/* 183 */         return (BlockState)state.setValue((Property)OCCUPIED, neighbourState.getValue((Property)OCCUPIED));
/*     */       }
/* 185 */       return Blocks.AIR.defaultBlockState();
/*     */     } 
/*     */ 
/*     */     
/* 189 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static Direction getNeighbourDirection(BedPart part, Direction facing) {
/* 193 */     return (part == BedPart.FOOT) ? facing : facing.getOpposite();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 198 */     if (!level.isClientSide() && player.preventsBlockDrops()) {
/* 199 */       BedPart part = (BedPart)state.getValue((Property)PART);
/* 200 */       if (part == BedPart.FOOT) {
/* 201 */         BlockPos headPos = pos.relative(getNeighbourDirection(part, (Direction)state.getValue((Property)FACING)));
/* 202 */         BlockState headState = level.getBlockState(headPos);
/* 203 */         if (headState.is(this) && headState.getValue((Property)PART) == BedPart.HEAD) {
/*     */           
/* 205 */           level.setBlock(headPos, Blocks.AIR.defaultBlockState(), 35);
/* 206 */           level.levelEvent((Entity)player, 2001, headPos, Block.getId(headState));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 216 */     Direction facing = context.getHorizontalDirection();
/*     */     
/* 218 */     BlockPos pos = context.getClickedPos();
/* 219 */     BlockPos relative = pos.relative(facing);
/* 220 */     Level level = context.getLevel();
/* 221 */     if (level.getBlockState(relative).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(relative)) {
/* 222 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)facing);
/*     */     }
/*     */     
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 230 */     return SHAPES.get(getConnectedDirection(state).getOpposite());
/*     */   }
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState state) {
/* 234 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 235 */     return (state.getValue((Property)PART) == BedPart.HEAD) ? facing.getOpposite() : facing;
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
/* 239 */     BedPart part = (BedPart)state.getValue((Property)PART);
/* 240 */     if (part == BedPart.HEAD) {
/* 241 */       return DoubleBlockCombiner.BlockType.FIRST;
/*     */     }
/* 243 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */   
/*     */   private static boolean isBunkBed(BlockGetter level, BlockPos pos) {
/* 247 */     return level.getBlockState(pos.below()).getBlock() instanceof BedBlock;
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> findStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, Direction forward, float yaw) {
/* 251 */     Direction right = forward.getClockWise();
/* 252 */     Direction side = right.isFacingAngle(yaw) ? right.getOpposite() : right;
/*     */     
/* 254 */     if (isBunkBed((BlockGetter)level, pos)) {
/* 255 */       return findBunkBedStandUpPosition(type, level, pos, forward, side);
/*     */     }
/*     */     
/* 258 */     int[][] offsets = bedStandUpOffsets(forward, side);
/*     */     
/* 260 */     Optional<Vec3> safePosition = findStandUpPositionAtOffset(type, level, pos, offsets, true);
/* 261 */     if (safePosition.isPresent()) {
/* 262 */       return safePosition;
/*     */     }
/* 264 */     return findStandUpPositionAtOffset(type, level, pos, offsets, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, Direction forward, Direction side) {
/* 268 */     int[][] offsets = bedSurroundStandUpOffsets(forward, side);
/*     */     
/* 270 */     Optional<Vec3> safePosition = findStandUpPositionAtOffset(type, level, pos, offsets, true);
/* 271 */     if (safePosition.isPresent()) {
/* 272 */       return safePosition;
/*     */     }
/*     */     
/* 275 */     BlockPos below = pos.below();
/*     */     
/* 277 */     Optional<Vec3> belowSafePosition = findStandUpPositionAtOffset(type, level, below, offsets, true);
/* 278 */     if (belowSafePosition.isPresent()) {
/* 279 */       return belowSafePosition;
/*     */     }
/*     */     
/* 282 */     int[][] aboveOffsets = bedAboveStandUpOffsets(forward);
/*     */     
/* 284 */     Optional<Vec3> aboveSafePosition = findStandUpPositionAtOffset(type, level, pos, aboveOffsets, true);
/* 285 */     if (aboveSafePosition.isPresent()) {
/* 286 */       return aboveSafePosition;
/*     */     }
/*     */     
/* 289 */     Optional<Vec3> unsafePosition = findStandUpPositionAtOffset(type, level, pos, offsets, false);
/* 290 */     if (unsafePosition.isPresent()) {
/* 291 */       return unsafePosition;
/*     */     }
/*     */     
/* 294 */     Optional<Vec3> belowUnsafePosition = findStandUpPositionAtOffset(type, level, below, offsets, false);
/* 295 */     if (belowUnsafePosition.isPresent()) {
/* 296 */       return belowUnsafePosition;
/*     */     }
/*     */     
/* 299 */     return findStandUpPositionAtOffset(type, level, pos, aboveOffsets, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> type, CollisionGetter level, BlockPos pos, int[][] offsets, boolean checkDangerous) {
/* 303 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 304 */     for (int[] offset : offsets) {
/* 305 */       blockPos.set(pos.getX() + offset[0], pos.getY(), pos.getZ() + offset[1]);
/*     */       
/* 307 */       Vec3 position = net.minecraft.world.entity.vehicle.DismountHelper.findSafeDismountLocation(type, level, (BlockPos)blockPos, checkDangerous);
/* 308 */       if (position != null) {
/* 309 */         return Optional.of(position);
/*     */       }
/*     */     } 
/* 312 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 317 */     builder.add(new Property[] { (Property)FACING, (Property)PART, (Property)OCCUPIED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 322 */     return (BlockEntity)new BedBlockEntity(worldPosition, blockState, this.color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 327 */     super.setPlacedBy(level, pos, state, by, itemStack);
/*     */ 
/*     */     
/* 330 */     if (!level.isClientSide()) {
/* 331 */       BlockPos otherPos = pos.relative((Direction)state.getValue((Property)FACING));
/* 332 */       level.setBlock(otherPos, (BlockState)state.setValue((Property)PART, (Comparable)BedPart.HEAD), 3);
/*     */       
/* 334 */       level.updateNeighborsAt(pos, Blocks.AIR);
/* 335 */       state.updateNeighbourShapes((LevelAccessor)level, pos, 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 340 */     return this.color;
/*     */   }
/*     */ 
/*     */   
/*     */   protected long getSeed(BlockState state, BlockPos pos) {
/* 345 */     BlockPos sourcePos = pos.relative((Direction)state.getValue((Property)FACING), (state.getValue((Property)PART) == BedPart.HEAD) ? 0 : 1);
/* 346 */     return Mth.getSeed(sourcePos.getX(), pos.getY(), sourcePos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedStandUpOffsets(Direction forward, Direction side) {
/* 358 */     return (int[][])org.apache.commons.lang3.ArrayUtils.addAll((Object[])bedSurroundStandUpOffsets(forward, side), (Object[])bedAboveStandUpOffsets(forward));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[][] bedSurroundStandUpOffsets(Direction forward, Direction side) {
/* 366 */     return new int[][] {
/* 367 */         { side.getStepX(), side.getStepZ()
/* 368 */         }, { side.getStepX() - forward.getStepX(), side.getStepZ() - forward.getStepZ()
/* 369 */         }, { side.getStepX() - forward.getStepX() * 2, side.getStepZ() - forward.getStepZ() * 2
/* 370 */         }, { -forward.getStepX() * 2, -forward.getStepZ() * 2
/* 371 */         }, { -side.getStepX() - forward.getStepX() * 2, -side.getStepZ() - forward.getStepZ() * 2
/* 372 */         }, { -side.getStepX() - forward.getStepX(), -side.getStepZ() - forward.getStepZ()
/* 373 */         }, { -side.getStepX(), -side.getStepZ()
/* 374 */         }, { -side.getStepX() + forward.getStepX(), -side.getStepZ() + forward.getStepZ()
/* 375 */         }, { forward.getStepX(), forward.getStepZ()
/* 376 */         }, { side.getStepX() + forward.getStepX(), side.getStepZ() + forward.getStepZ() }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[][] bedAboveStandUpOffsets(Direction forward) {
/* 382 */     return new int[][] { { 0, 0
/*     */         },
/* 384 */         { -forward.getStepX(), -forward.getStepZ() } };
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */