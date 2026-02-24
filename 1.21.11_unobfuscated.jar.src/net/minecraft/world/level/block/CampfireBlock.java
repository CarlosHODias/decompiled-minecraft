/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.CampfireCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.RecipePropertySet;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.CampfireBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  56 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("spawn_particles").forGetter(()), (App)Codec.intRange(0, 1000).fieldOf("fire_damage").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CampfireBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CampfireBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<CampfireBlock> codec() {
/*  64 */     return CODEC;
/*     */   }
/*     */   
/*  67 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*  68 */   public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
/*  69 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  70 */   public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*     */   
/*  72 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 7.0D);
/*     */   
/*  74 */   private static final VoxelShape SHAPE_VIRTUAL_POST = Block.column(4.0D, 0.0D, 16.0D);
/*     */   
/*     */   private static final int SMOKE_DISTANCE = 5;
/*     */   private final boolean spawnParticles;
/*     */   private final int fireDamage;
/*     */   
/*     */   public CampfireBlock(boolean spawnParticles, int fireDamage, BlockBehaviour.Properties properties) {
/*  81 */     super(properties);
/*  82 */     this.spawnParticles = spawnParticles;
/*  83 */     this.fireDamage = fireDamage;
/*  84 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, true)).setValue((Property)SIGNAL_FIRE, false)).setValue((Property)WATERLOGGED, false)).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  89 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  90 */     if (blockEntity instanceof CampfireBlockEntity) { CampfireBlockEntity campfire = (CampfireBlockEntity)blockEntity;
/*  91 */       ItemStack itemInHand = player.getItemInHand(hand);
/*  92 */       if (level.recipeAccess().propertySet(RecipePropertySet.CAMPFIRE_INPUT).test(itemInHand)) {
/*  93 */         if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (campfire.placeFood(serverLevel, (LivingEntity)player, itemInHand)) {
/*  94 */             player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
/*  95 */             return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */           }  }
/*  97 */          return (InteractionResult)InteractionResult.CONSUME;
/*     */       }  }
/*     */ 
/*     */     
/* 101 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 106 */     if ((Boolean)state.getValue((Property)LIT) && entity instanceof LivingEntity) {
/* 107 */       entity.hurt(level.damageSources().campfire(), this.fireDamage);
/*     */     }
/*     */     
/* 110 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 115 */     Level level = context.getLevel();
/* 116 */     BlockPos pos = context.getClickedPos();
/* 117 */     boolean replacedWater = (level.getFluidState(pos).getType() == Fluids.WATER);
/* 118 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 119 */       .setValue((Property)WATERLOGGED, replacedWater))
/* 120 */       .setValue((Property)SIGNAL_FIRE, isSmokeSource(level.getBlockState(pos.below()))))
/* 121 */       .setValue((Property)LIT, !replacedWater))
/* 122 */       .setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 127 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 128 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 131 */     if (directionToNeighbour == Direction.DOWN) {
/* 132 */       return (BlockState)state.setValue((Property)SIGNAL_FIRE, isSmokeSource(neighbourState));
/*     */     }
/* 134 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private boolean isSmokeSource(BlockState blockState) {
/* 138 */     return blockState.is(Blocks.HAY_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 143 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 148 */     if (!((Boolean)state.getValue((Property)LIT))) {
/*     */       return;
/*     */     }
/*     */     
/* 152 */     if (random.nextInt(10) == 0) {
/* 153 */       level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
/*     */     }
/*     */     
/* 156 */     if (this.spawnParticles && random.nextInt(5) == 0) {
/* 157 */       for (int i = 0; i < random.nextInt(1) + 1; i++) {
/* 158 */         level.addParticle((ParticleOptions)ParticleTypes.LAVA, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, (random.nextFloat() / 2.0F), 5.0E-5D, (random.nextFloat() / 2.0F));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void dowse(Entity source, LevelAccessor level, BlockPos pos, BlockState state) {
/* 164 */     if (level.isClientSide()) {
/* 165 */       for (int j = 0; j < 20; j++) {
/* 166 */         makeParticles((Level)level, pos, (Boolean)state.getValue((Property)SIGNAL_FIRE), true);
/*     */       }
/*     */     }
/* 169 */     level.gameEvent(source, (Holder)net.minecraft.world.level.gameevent.GameEvent.BLOCK_CHANGE, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 174 */     if (!((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)) && fluidState.getType() == Fluids.WATER) {
/* 175 */       boolean isLit = (Boolean)state.getValue((Property)LIT);
/* 176 */       if (isLit) {
/* 177 */         if (!level.isClientSide()) {
/* 178 */           level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */         }
/*     */         
/* 181 */         dowse(null, level, pos, state);
/*     */       } 
/*     */       
/* 184 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)WATERLOGGED, true)).setValue((Property)LIT, false), 3);
/* 185 */       level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay((LevelReader)level));
/* 186 */       return true;
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 193 */     BlockPos pos = blockHit.getBlockPos();
/* 194 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (projectile.isOnFire() && projectile.mayInteract(serverLevel, pos) && !((Boolean)state.getValue((Property)LIT)) && !((Boolean)state.getValue((Property)WATERLOGGED)))
/* 195 */         level.setBlock(pos, (BlockState)state.setValue((Property)BlockStateProperties.LIT, true), 11);  }
/*     */   
/*     */   }
/*     */   
/*     */   public static void makeParticles(Level level, BlockPos pos, boolean isSignalFire, boolean smoking) {
/* 200 */     RandomSource random = level.getRandom();
/* 201 */     SimpleParticleType smokeParticle = isSignalFire ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
/* 202 */     level.addAlwaysVisibleParticle((ParticleOptions)smokeParticle, true, 
/*     */         
/* 204 */         pos.getX() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? true : -1), 
/* 205 */         pos.getY() + random.nextDouble() + random.nextDouble(), 
/* 206 */         pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? true : -1), 0.0D, 0.07D, 0.0D);
/*     */ 
/*     */     
/* 209 */     if (smoking) {
/* 210 */       level.addParticle((ParticleOptions)ParticleTypes.SMOKE, 
/* 211 */           pos.getX() + 0.5D + random.nextDouble() / 4.0D * (random.nextBoolean() ? true : -1), 
/* 212 */           pos.getY() + 0.4D, 
/* 213 */           pos.getZ() + 0.5D + random.nextDouble() / 4.0D * (random.nextBoolean() ? true : -1), 0.0D, 0.005D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSmokeyPos(Level level, BlockPos pos) {
/* 226 */     for (int i = 1; i <= 5; i++) {
/* 227 */       BlockPos posToCheck = pos.below(i);
/* 228 */       BlockState blockState = level.getBlockState(posToCheck);
/* 229 */       if (isLitCampfire(blockState)) {
/* 230 */         return true;
/*     */       }
/*     */       
/* 233 */       boolean smokeBlocked = Shapes.joinIsNotEmpty(SHAPE_VIRTUAL_POST, blockState.getCollisionShape((BlockGetter)level, pos, CollisionContext.empty()), net.minecraft.world.phys.shapes.BooleanOp.AND);
/* 234 */       if (smokeBlocked) {
/*     */ 
/*     */         
/* 237 */         BlockState belowState = level.getBlockState(posToCheck.below());
/* 238 */         return isLitCampfire(belowState);
/*     */       } 
/*     */     } 
/* 241 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isLitCampfire(BlockState blockState) {
/* 246 */     return (blockState.hasProperty((Property)LIT) && blockState.is(BlockTags.CAMPFIRES) && (Boolean)blockState.getValue((Property)LIT));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 251 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 252 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 254 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 259 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 264 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 269 */     builder.add(new Property[] { (Property)LIT, (Property)SIGNAL_FIRE, (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 274 */     return (BlockEntity)new CampfireBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 279 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 280 */       if ((Boolean)blockState.getValue((Property)LIT)) {
/* 281 */         RecipeManager.CachedCheck<net.minecraft.world.item.crafting.SingleRecipeInput, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING);
/* 282 */         return createTickerHelper(type, BlockEntityType.CAMPFIRE, (innerLevel, pos, state, entity) -> CampfireBlockEntity.cookTick(serverLevel, pos, state, entity, quickCheck));
/*     */       } 
/* 284 */       return createTickerHelper(type, BlockEntityType.CAMPFIRE, CampfireBlockEntity::cooldownTick); }
/*     */ 
/*     */     
/* 287 */     if ((Boolean)blockState.getValue((Property)LIT)) {
/* 288 */       return createTickerHelper(type, BlockEntityType.CAMPFIRE, CampfireBlockEntity::particleTick);
/*     */     }
/*     */     
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 296 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canLight(BlockState state) {
/* 300 */     return (state.is(BlockTags.CAMPFIRES, s -> (s.hasProperty((Property)WATERLOGGED) && s.hasProperty((Property)LIT))) && !((Boolean)state.getValue((Property)WATERLOGGED)) && !((Boolean)state.getValue((Property)LIT)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CampfireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */