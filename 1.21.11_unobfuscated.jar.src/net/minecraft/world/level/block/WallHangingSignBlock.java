/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallHangingSignBlock extends SignBlock {
/*     */   static {
/*  40 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)i, WallHangingSignBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<WallHangingSignBlock> CODEC;
/*     */   
/*     */   public MapCodec<WallHangingSignBlock> codec() {
/*  47 */     return CODEC;
/*     */   }
/*     */   
/*  50 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  52 */   private static final Map<Direction.Axis, VoxelShape> SHAPES_PLANK = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 14.0D, 16.0D));
/*  53 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or(
/*  54 */         SHAPES_PLANK.get(Direction.Axis.Z), 
/*  55 */         Block.column(14.0D, 2.0D, 0.0D, 10.0D)));
/*     */ 
/*     */   
/*     */   public WallHangingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  59 */     super(type, properties.sound(type.hangingSignSoundType()));
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  65 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/*  66 */       if (shouldTryToChainAnotherHangingSign(state, player, hitResult, signEntity, itemStack)) {
/*  67 */         return (InteractionResult)InteractionResult.PASS;
/*     */       } }
/*     */     
/*  70 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(BlockState state, Player player, BlockHitResult hitResult, SignBlockEntity signEntity, ItemStack itemStack) {
/*  74 */     return (!signEntity.canExecuteClickCommands(signEntity.isFacingFrontText(player), player) && 
/*  75 */       itemStack.getItem() instanceof net.minecraft.world.item.HangingSignItem && !isHittingEditableSide(hitResult, state));
/*     */   }
/*     */   
/*     */   private boolean isHittingEditableSide(BlockHitResult hitResult, BlockState state) {
/*  79 */     return (hitResult.getDirection().getAxis() == ((Direction)state.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  84 */     return SHAPES.get(((Direction)state.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  89 */     return getShape(state, level, pos, CollisionContext.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  94 */     return SHAPES_PLANK.get(((Direction)state.getValue((Property)FACING)).getAxis());
/*     */   }
/*     */   
/*     */   public boolean canPlace(BlockState state, LevelReader level, BlockPos pos) {
/*  98 */     Direction clockwise = ((Direction)state.getValue((Property)FACING)).getClockWise();
/*  99 */     Direction counterClockwise = ((Direction)state.getValue((Property)FACING)).getCounterClockWise();
/*     */     
/* 101 */     return (canAttachTo(level, state, pos.relative(clockwise), counterClockwise) || canAttachTo(level, state, pos.relative(counterClockwise), clockwise));
/*     */   }
/*     */   
/*     */   public boolean canAttachTo(LevelReader level, BlockState state, BlockPos attachPos, Direction attachFace) {
/* 105 */     BlockState attachState = level.getBlockState(attachPos);
/*     */ 
/*     */     
/* 108 */     if (attachState.is(BlockTags.WALL_HANGING_SIGNS)) {
/* 109 */       return ((Direction)attachState.getValue((Property)FACING)).getAxis().test((Direction)state.getValue((Property)FACING));
/*     */     }
/*     */     
/* 112 */     return attachState.isFaceSturdy((BlockGetter)level, attachPos, attachFace, SupportType.FULL);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 117 */     BlockState state = defaultBlockState();
/* 118 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 120 */     Level level = context.getLevel();
/* 121 */     BlockPos pos = context.getClickedPos();
/*     */     
/* 123 */     for (Direction direction : context.getNearestLookingDirections()) {
/* 124 */       if (direction.getAxis().isHorizontal() && !direction.getAxis().test(context.getClickedFace())) {
/*     */ 
/*     */ 
/*     */         
/* 128 */         Direction facing = direction.getOpposite();
/* 129 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)facing);
/* 130 */         if (state.canSurvive((LevelReader)level, pos) && canPlace(state, (LevelReader)level, pos)) {
/* 131 */           return (BlockState)state.setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */         }
/*     */       } 
/*     */     } 
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 140 */     if (directionToNeighbour.getAxis() == ((Direction)state.getValue((Property)FACING)).getClockWise().getAxis() && !state.canSurvive(level, pos)) {
/* 141 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 143 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYRotationDegrees(BlockState state) {
/* 148 */     return ((Direction)state.getValue((Property)FACING)).toYRot();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 153 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 158 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 163 */     builder.add(new Property[] { (Property)FACING, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 168 */     return (BlockEntity)new net.minecraft.world.level.block.entity.HangingSignBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 178 */     return createTickerHelper(type, BlockEntityType.HANGING_SIGN, SignBlockEntity::tick);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WallHangingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */