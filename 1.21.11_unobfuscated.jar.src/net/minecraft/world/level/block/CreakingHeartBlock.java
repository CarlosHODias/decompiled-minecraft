/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.ServerExplosion;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CreakingHeartBlock extends BaseEntityBlock {
/*  38 */   public static final MapCodec<CreakingHeartBlock> CODEC = simpleCodec(CreakingHeartBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<CreakingHeartBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*  46 */   public static final EnumProperty<CreakingHeartState> STATE = BlockStateProperties.CREAKING_HEART_STATE;
/*  47 */   public static final BooleanProperty NATURAL = BlockStateProperties.NATURAL;
/*     */   
/*     */   protected CreakingHeartBlock(BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)Direction.Axis.Y)).setValue((Property)STATE, (Comparable)CreakingHeartState.UPROOTED)).setValue((Property)NATURAL, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  56 */     return (BlockEntity)new CreakingHeartBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  61 */     if (level.isClientSide()) {
/*  62 */       return null;
/*     */     }
/*  64 */     if (blockState.getValue((Property)STATE) != CreakingHeartState.UPROOTED) {
/*  65 */       return createTickerHelper(type, BlockEntityType.CREAKING_HEART, CreakingHeartBlockEntity::serverTick);
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  72 */     if (!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos))) {
/*     */       return;
/*     */     }
/*  75 */     if (state.getValue((Property)STATE) == CreakingHeartState.UPROOTED) {
/*     */       return;
/*     */     }
/*  78 */     if (random.nextInt(16) == 0 && isSurroundedByLogs((LevelAccessor)level, pos)) {
/*  79 */       level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CREAKING_HEART_IDLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  85 */     ticks.scheduleTick(pos, this, 1);
/*  86 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  91 */     BlockState newState = updateState(state, (Level)level, pos);
/*  92 */     if (newState != state) {
/*  93 */       level.setBlock(pos, newState, 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private static BlockState updateState(BlockState state, Level level, BlockPos pos) {
/*  98 */     boolean hasLogs = hasRequiredLogs(state, (LevelReader)level, pos);
/*  99 */     boolean disabled = (state.getValue((Property)STATE) == CreakingHeartState.UPROOTED);
/* 100 */     if (hasLogs && disabled) {
/* 101 */       return (BlockState)state.setValue((Property)STATE, (Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos) ? (Comparable)CreakingHeartState.AWAKE : (Comparable)CreakingHeartState.DORMANT);
/*     */     }
/* 103 */     return state;
/*     */   }
/*     */   
/*     */   public static boolean hasRequiredLogs(BlockState state, LevelReader level, BlockPos pos) {
/* 107 */     Direction.Axis axis = (Direction.Axis)state.getValue((Property)AXIS);
/* 108 */     for (Direction dir : axis.getDirections()) {
/* 109 */       BlockState neigbour = level.getBlockState(pos.relative(dir));
/* 110 */       if (!neigbour.is(BlockTags.PALE_OAK_LOGS) || neigbour.getValue((Property)AXIS) != axis) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isSurroundedByLogs(LevelAccessor level, BlockPos pos) {
/* 118 */     for (Direction dir : Direction.values()) {
/* 119 */       BlockPos neighbourPos = pos.relative(dir);
/* 120 */       BlockState neighbourState = level.getBlockState(neighbourPos);
/* 121 */       if (!neighbourState.is(BlockTags.PALE_OAK_LOGS)) {
/* 122 */         return false;
/*     */       }
/*     */     } 
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 130 */     return updateState((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)context.getClickedFace().getAxis()), context.getLevel(), context.getClickedPos());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 135 */     return RotatedPillarBlock.rotatePillar(state, rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 140 */     builder.add(new Property[] { (Property)AXIS, (Property)STATE, (Property)NATURAL });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 145 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 150 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CreakingHeartBlockEntity) { CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity; if (explosion instanceof ServerExplosion) { ServerExplosion serverExplosion = (ServerExplosion)explosion; if (explosion.getBlockInteraction().shouldAffectBlocklikeEntities()) {
/* 151 */           creakingHeartBlockEntity.removeProtector(serverExplosion.getDamageSource());
/* 152 */           LivingEntity livingEntity = explosion.getIndirectSourceEntity(); if (livingEntity instanceof Player) { Player player = (Player)livingEntity; if (explosion.getBlockInteraction().shouldAffectBlocklikeEntities())
/* 153 */               tryAwardExperience(player, state, (Level)level, pos);  } 
/*     */         }  }
/*     */        }
/* 156 */      super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 161 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CreakingHeartBlockEntity) { CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/* 162 */       creakingHeartBlockEntity.removeProtector(player.damageSources().playerAttack(player));
/* 163 */       tryAwardExperience(player, state, level, pos); }
/*     */     
/* 165 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   private void tryAwardExperience(Player player, BlockState state, Level level, BlockPos pos) {
/* 169 */     if (!player.preventsBlockDrops() && !player.isSpectator() && (Boolean)state.getValue((Property)NATURAL) && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 170 */       popExperience(serverLevel, pos, level.random.nextIntBetweenInclusive(20, 24)); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*     */     CreakingHeartBlockEntity creakingHeartBlockEntity;
/* 181 */     if (state.getValue((Property)STATE) == CreakingHeartState.UPROOTED) {
/* 182 */       return 0;
/*     */     }
/* 184 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CreakingHeartBlockEntity) { creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity; }
/* 185 */     else { return 0; }
/*     */     
/* 187 */     return creakingHeartBlockEntity.getAnalogOutputSignal();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CreakingHeartBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */