/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CeilingHangingSignBlock extends SignBlock {
/*     */   static {
/*  44 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)i, CeilingHangingSignBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CeilingHangingSignBlock> CODEC;
/*     */   
/*     */   public MapCodec<CeilingHangingSignBlock> codec() {
/*  51 */     return CODEC;
/*     */   }
/*     */   
/*  54 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*  55 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*  57 */   private static final VoxelShape SHAPE_DEFAULT = Block.column(10.0D, 0.0D, 16.0D); private static final Map<Integer, VoxelShape> SHAPES; static {
/*  58 */     SHAPES = (Map<Integer, VoxelShape>)Shapes.rotateHorizontal(Block.column(14.0D, 2.0D, 0.0D, 10.0D)).entrySet().stream().collect(Collectors.toMap(e -> RotationSegment.convertToSegment((Direction)e.getKey()), Map.Entry::getValue));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CeilingHangingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  64 */     super(type, properties.sound(type.hangingSignSoundType()));
/*  65 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, 0)).setValue((Property)ATTACHED, false)).setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  70 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof SignBlockEntity) { SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/*  71 */       if (shouldTryToChainAnotherHangingSign(player, hitResult, signEntity, itemStack)) {
/*  72 */         return (InteractionResult)InteractionResult.PASS;
/*     */       } }
/*     */     
/*  75 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(Player player, BlockHitResult hitResult, SignBlockEntity signEntity, ItemStack itemStack) {
/*  79 */     return (!signEntity.canExecuteClickCommands(signEntity.isFacingFrontText(player), player) && 
/*  80 */       itemStack.getItem() instanceof net.minecraft.world.item.HangingSignItem && hitResult.getDirection().equals(Direction.DOWN));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  85 */     return level.getBlockState(pos.above()).isFaceSturdy((BlockGetter)level, pos.above(), Direction.DOWN, SupportType.CENTER);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  90 */     Level level = context.getLevel();
/*  91 */     FluidState replacedFluidState = level.getFluidState(context.getClickedPos());
/*  92 */     BlockPos above = context.getClickedPos().above();
/*  93 */     BlockState stateAbove = level.getBlockState(above);
/*  94 */     boolean isBelowHangingSign = stateAbove.is(BlockTags.ALL_HANGING_SIGNS);
/*  95 */     Direction direction = Direction.fromYRot(context.getRotation());
/*  96 */     boolean attachedToMiddle = (!Block.isFaceFull(stateAbove.getCollisionShape((BlockGetter)level, above), Direction.DOWN) || context.isSecondaryUseActive());
/*     */     
/*  98 */     if (isBelowHangingSign && !context.isSecondaryUseActive()) {
/*  99 */       if (stateAbove.hasProperty((Property)WallHangingSignBlock.FACING)) {
/* 100 */         Direction aboveDirection = (Direction)stateAbove.getValue((Property)WallHangingSignBlock.FACING);
/* 101 */         if (aboveDirection.getAxis().test(direction)) {
/* 102 */           attachedToMiddle = false;
/*     */         }
/* 104 */       } else if (stateAbove.hasProperty((Property)ROTATION)) {
/* 105 */         Optional<Direction> aboveDirection = RotationSegment.convertToDirection((Integer)stateAbove.getValue((Property)ROTATION));
/* 106 */         if (aboveDirection.isPresent() && ((Direction)aboveDirection.get()).getAxis().test(direction)) {
/* 107 */           attachedToMiddle = false;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 112 */     int rotationSegment = !attachedToMiddle ? RotationSegment.convertToSegment(direction.getOpposite()) : RotationSegment.convertToSegment(context.getRotation() + 180.0F);
/* 113 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHED, attachedToMiddle)).setValue((Property)ROTATION, rotationSegment)).setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 118 */     return SHAPES.getOrDefault(state.getValue((Property)ROTATION), SHAPE_DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 123 */     return getShape(state, level, pos, CollisionContext.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 128 */     if (directionToNeighbour == Direction.UP && !canSurvive(state, level, pos)) {
/* 129 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 131 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getYRotationDegrees(BlockState state) {
/* 136 */     return RotationSegment.convertToDegrees((Integer)state.getValue((Property)ROTATION));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 141 */     return (BlockState)state.setValue((Property)ROTATION, rotation.rotate((Integer)state.getValue((Property)ROTATION), 16));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 146 */     return (BlockState)state.setValue((Property)ROTATION, mirror.mirror((Integer)state.getValue((Property)ROTATION), 16));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 151 */     builder.add(new Property[] { (Property)ROTATION, (Property)ATTACHED, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 156 */     return (BlockEntity)new net.minecraft.world.level.block.entity.HangingSignBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 161 */     return createTickerHelper(type, BlockEntityType.HANGING_SIGN, SignBlockEntity::tick);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CeilingHangingSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */