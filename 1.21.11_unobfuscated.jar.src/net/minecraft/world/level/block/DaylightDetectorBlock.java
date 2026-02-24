/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DaylightDetectorBlock extends BaseEntityBlock {
/*  31 */   public static final MapCodec<DaylightDetectorBlock> CODEC = simpleCodec(DaylightDetectorBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<DaylightDetectorBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*  39 */   public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
/*     */   
/*  41 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 6.0D);
/*     */   
/*     */   public DaylightDetectorBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*     */     
/*  46 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, 0)).setValue((Property)INVERTED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  51 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  61 */     return (Integer)state.getValue((Property)POWER);
/*     */   }
/*     */   
/*     */   private static void updateSignalStrength(BlockState state, Level level, BlockPos pos) {
/*  65 */     int target = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
/*  66 */     float sunAngle = (Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, pos) * 0.017453292F;
/*     */     
/*  68 */     boolean isInverted = (Boolean)state.getValue((Property)INVERTED);
/*  69 */     if (isInverted) {
/*  70 */       target = 15 - target;
/*  71 */     } else if (target > 0) {
/*     */       
/*  73 */       float offset = (sunAngle < 3.1415927F) ? 0.0F : 6.2831855F;
/*  74 */       sunAngle += (offset - sunAngle) * 0.2F;
/*     */       
/*  76 */       target = Math.round(target * Mth.cos(sunAngle));
/*     */     } 
/*  78 */     target = Mth.clamp(target, 0, 15);
/*     */     
/*  80 */     if ((Integer)state.getValue((Property)POWER) != target) {
/*  81 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWER, target), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  87 */     if (!player.mayBuild()) {
/*  88 */       return super.useWithoutItem(state, level, pos, player, hitResult);
/*     */     }
/*  90 */     if (!level.isClientSide()) {
/*  91 */       BlockState newState = (BlockState)state.cycle((Property)INVERTED);
/*  92 */       level.setBlock(pos, newState, 2);
/*  93 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, newState));
/*  94 */       updateSignalStrength(newState, level, pos);
/*     */     } 
/*  96 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 106 */     return (BlockEntity)new DaylightDetectorBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 111 */     if (!level.isClientSide() && level.dimensionType().hasSkyLight()) {
/* 112 */       return createTickerHelper(type, BlockEntityType.DAYLIGHT_DETECTOR, DaylightDetectorBlock::tickEntity);
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   private static void tickEntity(Level level, BlockPos blockPos, BlockState blockState, DaylightDetectorBlockEntity blockEntity) {
/* 118 */     if (level.getGameTime() % 20L == 0L) {
/* 119 */       updateSignalStrength(blockState, level, blockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 125 */     builder.add(new Property[] { (Property)POWER, (Property)INVERTED });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DaylightDetectorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */