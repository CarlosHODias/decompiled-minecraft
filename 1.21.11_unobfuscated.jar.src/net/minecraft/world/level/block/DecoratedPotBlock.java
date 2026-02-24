/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DecoratedPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  51 */   public static final MapCodec<DecoratedPotBlock> CODEC = simpleCodec(DecoratedPotBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<DecoratedPotBlock> codec() {
/*  55 */     return CODEC;
/*     */   }
/*     */   
/*  58 */   public static final Identifier SHERDS_DYNAMIC_DROP_ID = Identifier.withDefaultNamespace("sherds");
/*     */   
/*  60 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  61 */   public static final BooleanProperty CRACKED = BlockStateProperties.CRACKED;
/*  62 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  64 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*     */   protected DecoratedPotBlock(BlockBehaviour.Properties properties) {
/*  67 */     super(properties);
/*  68 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  69 */         .setValue((Property)HORIZONTAL_FACING, (Comparable)Direction.NORTH))
/*  70 */         .setValue((Property)WATERLOGGED, false))
/*  71 */         .setValue((Property)CRACKED, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  76 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  77 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  79 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  84 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  85 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/*  86 */       .setValue((Property)HORIZONTAL_FACING, (Comparable)context.getHorizontalDirection()))
/*  87 */       .setValue((Property)WATERLOGGED, (replacedFluidState.getType() == Fluids.WATER)))
/*  88 */       .setValue((Property)CRACKED, false);
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*     */     DecoratedPotBlockEntity decoratedPot;
/*  93 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof DecoratedPotBlockEntity) { decoratedPot = (DecoratedPotBlockEntity)blockEntity; }
/*  94 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/*  97 */     if (level.isClientSide()) {
/*  98 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 101 */     ItemStack potItem = decoratedPot.getTheItem();
/* 102 */     if (!itemStack.isEmpty() && (
/* 103 */       potItem.isEmpty() || (ItemStack.isSameItemSameComponents(potItem, itemStack) && potItem.getCount() < potItem.getMaxStackSize()))) {
/*     */       float pitchBend;
/* 105 */       decoratedPot.wobble(DecoratedPotBlockEntity.WobbleStyle.POSITIVE);
/* 106 */       player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
/* 107 */       ItemStack awardedItem = itemStack.consumeAndReturn(1, (LivingEntity)player);
/*     */       
/* 109 */       if (decoratedPot.isEmpty()) {
/* 110 */         decoratedPot.setTheItem(awardedItem);
/* 111 */         pitchBend = awardedItem.getCount() / awardedItem.getMaxStackSize();
/*     */       } else {
/* 113 */         potItem.grow(1);
/* 114 */         pitchBend = potItem.getCount() / potItem.getMaxStackSize();
/*     */       } 
/*     */       
/* 117 */       level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * pitchBend);
/* 118 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 119 */         serverLevel.sendParticles((net.minecraft.core.particles.ParticleOptions)ParticleTypes.DUST_PLUME, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, 7, 0.0D, 0.0D, 0.0D, 0.0D); }
/*     */       
/* 121 */       decoratedPot.setChanged();
/* 122 */       level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_CHANGE, pos);
/*     */       
/* 124 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 127 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     DecoratedPotBlockEntity decoratedPot;
/* 132 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof DecoratedPotBlockEntity) { decoratedPot = (DecoratedPotBlockEntity)blockEntity; }
/* 133 */     else { return (InteractionResult)InteractionResult.PASS; }
/*     */ 
/*     */     
/* 136 */     level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 137 */     decoratedPot.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
/* 138 */     level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_CHANGE, pos);
/* 139 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 149 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 154 */     builder.add(new Property[] { (Property)HORIZONTAL_FACING, (Property)WATERLOGGED, (Property)CRACKED });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 159 */     return (BlockEntity)new DecoratedPotBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 164 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/* 169 */     BlockEntity maybeEntity = (BlockEntity)params.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY);
/*     */     
/* 171 */     if (maybeEntity instanceof DecoratedPotBlockEntity) { DecoratedPotBlockEntity entity = (DecoratedPotBlockEntity)maybeEntity;
/* 172 */       params.withDynamicDrop(SHERDS_DYNAMIC_DROP_ID, output -> {
/*     */             for (Item item : (Iterable<Item>)entity.getDecorations().ordered()) {
/*     */               output.accept(item.getDefaultInstance());
/*     */             }
/*     */           }); }
/*     */ 
/*     */     
/* 179 */     return super.getDrops(state, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 184 */     ItemStack destroyedWith = player.getMainHandItem();
/* 185 */     BlockState nextState = state;
/* 186 */     if (destroyedWith.is(ItemTags.BREAKS_DECORATED_POTS) && !net.minecraft.world.item.enchantment.EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING)) {
/* 187 */       nextState = (BlockState)state.setValue((Property)CRACKED, true);
/* 188 */       level.setBlock(pos, nextState, 260);
/*     */     } 
/* 190 */     return super.playerWillDestroy(level, pos, nextState, player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 195 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 196 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 198 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundType getSoundType(BlockState state) {
/* 203 */     if ((Boolean)state.getValue((Property)CRACKED)) {
/* 204 */       return SoundType.DECORATED_POT_CRACKED;
/*     */     }
/* 206 */     return SoundType.DECORATED_POT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 211 */     BlockPos pos = blockHit.getBlockPos();
/* 212 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (projectile.mayInteract(serverLevel, pos) && projectile.mayBreak(serverLevel)) {
/* 213 */         level.setBlock(pos, (BlockState)state.setValue((Property)CRACKED, true), 260);
/* 214 */         level.destroyBlock(pos, true, (Entity)projectile);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 220 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof DecoratedPotBlockEntity) { DecoratedPotBlockEntity decoratedPotBlockEntity = (DecoratedPotBlockEntity)blockEntity;
/* 221 */       PotDecorations decorations = decoratedPotBlockEntity.getDecorations();
/* 222 */       return DecoratedPotBlockEntity.createDecoratedPotItem(decorations); }
/*     */ 
/*     */     
/* 225 */     return super.getCloneItemStack(level, pos, state, includeData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 230 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 235 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 240 */     return (BlockState)state.setValue((Property)HORIZONTAL_FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)HORIZONTAL_FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 245 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)HORIZONTAL_FACING)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DecoratedPotBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */