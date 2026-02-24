/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkShriekerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  33 */   public static final MapCodec<SculkShriekerBlock> CODEC = simpleCodec(SculkShriekerBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<SculkShriekerBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty SHRIEKING = BlockStateProperties.SHRIEKING;
/*  41 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  42 */   public static final BooleanProperty CAN_SUMMON = BlockStateProperties.CAN_SUMMON;
/*     */   
/*  44 */   private static final VoxelShape SHAPE_COLLISION = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*  46 */   public static final double TOP_Y = SHAPE_COLLISION.max(Direction.Axis.Y);
/*     */   
/*     */   public SculkShriekerBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*     */     
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHRIEKING, false)).setValue((Property)WATERLOGGED, false)).setValue((Property)CAN_SUMMON, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  56 */     builder.add(new Property[] { (Property)SHRIEKING });
/*  57 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*  58 */     builder.add(new Property[] { (Property)CAN_SUMMON });
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/*  63 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  64 */       ServerPlayer player = SculkShriekerBlockEntity.tryGetPlayer(entity);
/*  65 */       if (player != null) {
/*  66 */         serverLevel.getBlockEntity(pos, BlockEntityType.SCULK_SHRIEKER).ifPresent(shrieker -> shrieker.tryShriek(serverLevel, player));
/*     */       } }
/*     */ 
/*     */     
/*  70 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  75 */     if ((Boolean)state.getValue((Property)SHRIEKING)) {
/*  76 */       level.setBlock(pos, (BlockState)state.setValue((Property)SHRIEKING, false), 3);
/*     */       
/*  78 */       level.getBlockEntity(pos, BlockEntityType.SCULK_SHRIEKER).ifPresent(shrieker -> shrieker.tryRespond(level));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  84 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState state) {
/*  89 */     return SHAPE_COLLISION;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  99 */     return (BlockEntity)new SculkShriekerBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 104 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 105 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 107 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 112 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, (context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 117 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 118 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 120 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 125 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 126 */     if (dropExperience) {
/* 127 */       tryDropExperience(level, pos, tool, (IntProvider)net.minecraft.util.valueproviders.ConstantInt.of(5));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 133 */     if (!level.isClientSide()) {
/* 134 */       return BaseEntityBlock.createTickerHelper(type, BlockEntityType.SCULK_SHRIEKER, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser()));
/*     */     }
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SculkShriekerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */