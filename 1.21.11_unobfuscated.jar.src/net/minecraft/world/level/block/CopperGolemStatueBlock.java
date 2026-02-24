/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
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
/*     */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CopperGolemStatueBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  45 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperGolemStatueBlock::getWeatheringState), (App)propertiesCodec()).apply((Applicative)i, CopperGolemStatueBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CopperGolemStatueBlock> CODEC;
/*     */   
/*     */   public MapCodec<? extends CopperGolemStatueBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  56 */   public static final EnumProperty<Pose> POSE = BlockStateProperties.COPPER_GOLEM_POSE;
/*  57 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  59 */   private static final VoxelShape SHAPE = Block.column(10.0D, 0.0D, 14.0D);
/*     */   
/*     */   private final WeatheringCopper.WeatherState weatheringState;
/*     */   
/*     */   public CopperGolemStatueBlock(WeatheringCopper.WeatherState weatherState, BlockBehaviour.Properties properties) {
/*  64 */     super(properties);
/*  65 */     this.weatheringState = weatherState;
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)defaultBlockState()
/*  67 */         .setValue((Property)FACING, (Comparable)Direction.NORTH))
/*  68 */         .setValue((Property)POSE, Pose.STANDING))
/*  69 */         .setValue((Property)WATERLOGGED, false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  75 */     super.createBlockStateDefinition(builder);
/*  76 */     builder.add(new Property[] { (Property)FACING, (Property)POSE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  81 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  82 */     return (BlockState)((BlockState)defaultBlockState()
/*  83 */       .setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite()))
/*  84 */       .setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  89 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  94 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  99 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public WeatheringCopper.WeatherState getWeatheringState() {
/* 103 */     return this.weatheringState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
/* 108 */     if (itemStack.is(ItemTags.AXES)) {
/* 109 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 111 */     updatePose(level, state, pos, player);
/* 112 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   void updatePose(Level level, BlockState state, BlockPos pos, Player player) {
/* 116 */     level.playSound(null, pos, SoundEvents.COPPER_GOLEM_BECOME_STATUE, SoundSource.BLOCKS);
/* 117 */     level.setBlock(pos, (BlockState)state.setValue((Property)POSE, ((Pose)state.getValue((Property)POSE)).getNextPose()), 3);
/* 118 */     level.gameEvent((net.minecraft.world.entity.Entity)player, (net.minecraft.core.Holder)net.minecraft.world.level.gameevent.GameEvent.BLOCK_CHANGE, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 123 */     return (type == PathComputationType.WATER && state.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 128 */     return (BlockEntity)new CopperGolemStatueBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/* 133 */     return oldState.is(BlockTags.COPPER_GOLEM_STATUES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 143 */     return ((Pose)state.getValue((Property)POSE)).ordinal() + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 148 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CopperGolemStatueBlockEntity) { CopperGolemStatueBlockEntity entity = (CopperGolemStatueBlockEntity)blockEntity;
/* 149 */       return entity.getItem(asItem().getDefaultInstance(), (Pose)state.getValue((Property)POSE)); }
/*     */ 
/*     */     
/* 152 */     return super.getCloneItemStack(level, pos, state, includeData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 157 */     level.updateNeighbourForOutputSignal(pos, state.getBlock());
/*     */   }
/*     */   
/*     */   public enum Pose implements StringRepresentable {
/* 161 */     STANDING("standing"),
/* 162 */     SITTING("sitting"),
/* 163 */     RUNNING("running"),
/* 164 */     STAR("star");
/*     */     
/* 166 */     public static final java.util.function.IntFunction<Pose> BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 167 */     public static final Codec<Pose> CODEC = (Codec<Pose>)StringRepresentable.fromEnum(Pose::values);
/*     */     private final String name;
/*     */     
/*     */     Pose(String name) {
/* 171 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 176 */       return this.name;
/*     */     }
/*     */     
/*     */     public Pose getNextPose() {
/* 180 */       return BY_ID.apply(ordinal() + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 186 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 187 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 189 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 194 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 195 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 197 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CopperGolemStatueBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */