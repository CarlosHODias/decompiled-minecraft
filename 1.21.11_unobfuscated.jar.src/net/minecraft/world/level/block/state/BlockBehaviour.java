/*      */ package net.minecraft.world.level.block.state;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.resources.DependantName;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.flag.FeatureElement;
/*      */ import net.minecraft.world.flag.FeatureFlag;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.context.BlockPlaceContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.EmptyBlockGetter;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ScheduledTickAccess;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.EntityBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.SupportType;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.material.MapColor;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ 
/*      */ public abstract class BlockBehaviour implements FeatureElement {
/*   92 */   protected static final Direction[] UPDATE_SHAPE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP };
/*      */   
/*      */   protected final boolean hasCollision;
/*      */   
/*      */   protected final float explosionResistance;
/*      */   protected final boolean isRandomlyTicking;
/*      */   protected final SoundType soundType;
/*      */   protected final float friction;
/*      */   protected final float speedFactor;
/*      */   protected final float jumpFactor;
/*      */   protected final boolean dynamicShape;
/*      */   protected final FeatureFlagSet requiredFeatures;
/*      */   protected final Properties properties;
/*      */   protected final Optional<ResourceKey<LootTable>> drops;
/*      */   protected final String descriptionId;
/*      */   
/*      */   public BlockBehaviour(Properties properties) {
/*  109 */     this.hasCollision = properties.hasCollision;
/*  110 */     this.drops = properties.effectiveDrops();
/*  111 */     this.descriptionId = properties.effectiveDescriptionId();
/*  112 */     this.explosionResistance = properties.explosionResistance;
/*  113 */     this.isRandomlyTicking = properties.isRandomlyTicking;
/*  114 */     this.soundType = properties.soundType;
/*  115 */     this.friction = properties.friction;
/*  116 */     this.speedFactor = properties.speedFactor;
/*  117 */     this.jumpFactor = properties.jumpFactor;
/*  118 */     this.dynamicShape = properties.dynamicShape;
/*  119 */     this.requiredFeatures = properties.requiredFeatures;
/*  120 */     this.properties = properties;
/*      */   }
/*      */   
/*      */   public Properties properties() {
/*  124 */     return this.properties;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static <B extends Block> RecordCodecBuilder<B, Properties> propertiesCodec() {
/*  136 */     return Properties.CODEC.fieldOf("properties").forGetter(BlockBehaviour::properties);
/*      */   }
/*      */   
/*      */   public static <B extends Block> MapCodec<B> simpleCodec(Function<Properties, B> constructor) {
/*  140 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)propertiesCodec()).apply((Applicative)i, constructor));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, @Block.UpdateFlags int updateFlags, int updateLimit) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  150 */     switch (type) {
/*      */       case LAND:
/*  152 */         return !state.isCollisionShapeFullBlock((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */       case WATER:
/*  154 */         return state.getFluidState().is(FluidTags.WATER);
/*      */       case AIR:
/*  156 */         return !state.isCollisionShapeFullBlock((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */     } 
/*  158 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  172 */     return state;
/*      */   }
/*      */   
/*      */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/*  176 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/*  196 */     if (state.isAir() || explosion.getBlockInteraction() == Explosion.BlockInteraction.TRIGGER_BLOCK) {
/*      */       return;
/*      */     }
/*      */     
/*  200 */     Block block = state.getBlock();
/*  201 */     boolean doDropExperienceHack = explosion.getIndirectSourceEntity() instanceof Player;
/*      */     
/*  203 */     if (block.dropFromExplosion(explosion)) {
/*  204 */       BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*  205 */       LootParams.Builder params = new LootParams.Builder(level)
/*  206 */         .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos))
/*  207 */         .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
/*  208 */         .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
/*  209 */         .withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getDirectSourceEntity());
/*      */       
/*  211 */       if (explosion.getBlockInteraction() == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
/*  212 */         params.withParameter(LootContextParams.EXPLOSION_RADIUS, explosion.radius());
/*      */       }
/*      */       
/*  215 */       state.spawnAfterBreak(level, pos, ItemStack.EMPTY, doDropExperienceHack);
/*  216 */       state.getDrops(params).forEach(stack -> onHit.accept(stack, pos));
/*      */     } 
/*      */     
/*  219 */     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/*  220 */     block.wasExploded(level, pos, explosion);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  228 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  235 */     return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*      */   }
/*      */   
/*      */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/*  239 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected RenderShape getRenderShape(BlockState state) {
/*  245 */     return RenderShape.MODEL;
/*      */   }
/*      */   
/*      */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  249 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean isSignalSource(BlockState state) {
/*  253 */     return false;
/*      */   }
/*      */   
/*      */   protected FluidState getFluidState(BlockState state) {
/*  257 */     return Fluids.EMPTY.defaultFluidState();
/*      */   }
/*      */   
/*      */   protected boolean hasAnalogOutputSignal(BlockState state) {
/*  261 */     return false;
/*      */   }
/*      */   
/*      */   protected float getMaxHorizontalOffset() {
/*  265 */     return 0.25F;
/*      */   }
/*      */   
/*      */   protected float getMaxVerticalOffset() {
/*  269 */     return 0.2F;
/*      */   }
/*      */ 
/*      */   
/*      */   public FeatureFlagSet requiredFeatures() {
/*  274 */     return this.requiredFeatures;
/*      */   }
/*      */   
/*      */   protected boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/*  278 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  284 */     return state;
/*      */   }
/*      */   
/*      */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  288 */     return state;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  300 */     return (state.canBeReplaced() && (context.getItemInHand().isEmpty() || !context.getItemInHand().is(asItem())));
/*      */   }
/*      */   
/*      */   protected boolean canBeReplaced(BlockState state, Fluid fluid) {
/*  304 */     return (state.canBeReplaced() || !state.isSolid());
/*      */   }
/*      */   
/*      */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/*  308 */     if (this.drops.isEmpty()) {
/*  309 */       return Collections.emptyList();
/*      */     }
/*  311 */     LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
/*  312 */     ServerLevel level = lootParams.getLevel();
/*  313 */     LootTable table = level.getServer().reloadableRegistries().getLootTable(this.drops.get());
/*  314 */     return (List<ItemStack>)table.getRandomItems(lootParams);
/*      */   }
/*      */   
/*      */   protected long getSeed(BlockState state, BlockPos pos) {
/*  318 */     return Mth.getSeed((Vec3i)pos);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected VoxelShape getOcclusionShape(BlockState state) {
/*  324 */     return state.getShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
/*      */   }
/*      */   
/*      */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  328 */     return getCollisionShape(state, level, pos, CollisionContext.empty());
/*      */   }
/*      */   
/*      */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  332 */     return Shapes.empty();
/*      */   }
/*      */   
/*      */   protected int getLightBlock(BlockState state) {
/*  336 */     if (state.isSolidRender()) {
/*  337 */       return 15;
/*      */     }
/*  339 */     return state.propagatesSkylightDown() ? 0 : 1;
/*      */   }
/*      */   
/*      */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/*  343 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  347 */     return true;
/*      */   }
/*      */   
/*      */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/*  351 */     return state.isCollisionShapeFullBlock(level, pos) ? 0.2F : 1.0F;
/*      */   }
/*      */   
/*      */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*  355 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  361 */     return Shapes.block();
/*      */   }
/*      */   
/*      */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  365 */     return this.hasCollision ? state.getShape(level, pos) : Shapes.empty();
/*      */   }
/*      */   
/*      */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
/*  369 */     return Shapes.block();
/*      */   }
/*      */   
/*      */   protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
/*  373 */     return Block.isShapeFullBlock(state.getCollisionShape(level, pos));
/*      */   }
/*      */   
/*      */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  377 */     return getCollisionShape(state, level, pos, context);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {}
/*      */ 
/*      */   
/*      */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {}
/*      */   
/*      */   protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
/*  387 */     float destroySpeed = state.getDestroySpeed(level, pos);
/*  388 */     if (destroySpeed == -1.0F) {
/*  389 */       return 0.0F;
/*      */     }
/*  391 */     int modifier = player.hasCorrectToolForDrops(state) ? 30 : 100;
/*  392 */     return player.getDestroySpeed(state) / destroySpeed / modifier;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {}
/*      */ 
/*      */   
/*      */   protected void attack(BlockState state, Level level, BlockPos pos, Player player) {}
/*      */   
/*      */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  402 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  413 */     return 0;
/*      */   }
/*      */   
/*      */   public final Optional<ResourceKey<LootTable>> getLootTable() {
/*  417 */     return this.drops;
/*      */   }
/*      */   
/*      */   public final String getDescriptionId() {
/*  421 */     return this.descriptionId;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {}
/*      */   
/*      */   protected boolean propagatesSkylightDown(BlockState state) {
/*  428 */     return (!Block.isShapeFullBlock(state.getShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) && state.getFluidState().isEmpty());
/*      */   }
/*      */   
/*      */   protected boolean isRandomlyTicking(BlockState state) {
/*  432 */     return this.isRandomlyTicking;
/*      */   }
/*      */   
/*      */   protected SoundType getSoundType(BlockState state) {
/*  436 */     return this.soundType;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/*  441 */     return new ItemStack((ItemLike)asItem());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum OffsetType
/*      */   {
/*  449 */     NONE,
/*  450 */     XZ,
/*  451 */     XYZ;
/*      */   }
/*      */   
/*      */   public MapColor defaultMapColor() {
/*  455 */     return this.properties.mapColor.apply(asBlock().defaultBlockState());
/*      */   }
/*      */   
/*      */   public float defaultDestroyTime() {
/*  459 */     return this.properties.destroyTime;
/*      */   }
/*      */   protected abstract MapCodec<? extends Block> codec();
/*      */   public abstract Item asItem();
/*      */   protected abstract Block asBlock();
/*  464 */   public static class Properties { public static final Codec<Properties> CODEC = MapCodec.unitCodec(() -> of());
/*      */     
/*      */     private Function<BlockState, MapColor> mapColor = state -> MapColor.NONE;
/*      */     
/*      */     private boolean hasCollision = true;
/*  469 */     private SoundType soundType = SoundType.STONE;
/*      */     private ToIntFunction<BlockState> lightEmission = state -> 0;
/*      */     private float explosionResistance;
/*      */     private float destroyTime;
/*      */     private boolean requiresCorrectToolForDrops;
/*      */     private boolean isRandomlyTicking;
/*  475 */     private float friction = 0.6F;
/*  476 */     private float speedFactor = 1.0F; private ResourceKey<Block> id; private DependantName<Block, Optional<ResourceKey<LootTable>>> drops; private DependantName<Block, String> descriptionId; private boolean canOcclude; private boolean isAir; private boolean ignitedByLava;
/*  477 */     private float jumpFactor = 1.0F; @Deprecated
/*      */     private boolean liquid; @Deprecated
/*      */     private boolean forceSolidOff; private boolean forceSolidOn; private PushReaction pushReaction; private boolean spawnTerrainParticles;
/*      */     
/*      */     private Properties() {
/*  482 */       this.drops = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("blocks/"))));
/*  483 */       this.descriptionId = (id -> Util.makeDescriptionId("block", id.identifier()));
/*  484 */       this.canOcclude = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  492 */       this.pushReaction = PushReaction.NORMAL;
/*  493 */       this.spawnTerrainParticles = true;
/*  494 */       this.instrument = NoteBlockInstrument.HARP;
/*      */ 
/*      */       
/*  497 */       this.isValidSpawn = ((state, level, pos, entityType) -> 
/*  498 */         (state.isFaceSturdy(level, pos, Direction.UP) && state.getLightEmission() < 14));
/*      */       
/*  500 */       this.isRedstoneConductor = ((state, level, pos) -> state.isCollisionShapeFullBlock(level, pos));
/*      */ 
/*      */       
/*  503 */       this.isSuffocating = ((state, level, pos) -> 
/*  504 */         (state.blocksMotion() && state.isCollisionShapeFullBlock(level, pos)));
/*      */       
/*  506 */       this.isViewBlocking = this.isSuffocating;
/*  507 */       this.hasPostProcess = ((state, level, pos) -> false);
/*  508 */       this.emissiveRendering = ((state, level, pos) -> false);
/*      */ 
/*      */       
/*  511 */       this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*      */     }
/*      */     private NoteBlockInstrument instrument; private boolean replaceable; private BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn; private BlockBehaviour.StatePredicate isRedstoneConductor; private BlockBehaviour.StatePredicate isSuffocating; private BlockBehaviour.StatePredicate isViewBlocking; private BlockBehaviour.StatePredicate hasPostProcess; private BlockBehaviour.StatePredicate emissiveRendering;
/*      */     private boolean dynamicShape;
/*      */     private FeatureFlagSet requiredFeatures;
/*      */     private BlockBehaviour.OffsetFunction offsetFunction;
/*      */     
/*      */     public static Properties of() {
/*  519 */       return new Properties();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Properties ofFullCopy(BlockBehaviour block) {
/*  531 */       Properties copyTo = ofLegacyCopy(block);
/*  532 */       Properties copyFrom = block.properties;
/*      */       
/*  534 */       copyTo.jumpFactor = copyFrom.jumpFactor;
/*  535 */       copyTo.isRedstoneConductor = copyFrom.isRedstoneConductor;
/*  536 */       copyTo.isValidSpawn = copyFrom.isValidSpawn;
/*  537 */       copyTo.hasPostProcess = copyFrom.hasPostProcess;
/*  538 */       copyTo.isSuffocating = copyFrom.isSuffocating;
/*  539 */       copyTo.isViewBlocking = copyFrom.isViewBlocking;
/*  540 */       copyTo.drops = copyFrom.drops;
/*  541 */       copyTo.descriptionId = copyFrom.descriptionId;
/*      */       
/*  543 */       return copyTo;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public static Properties ofLegacyCopy(BlockBehaviour block) {
/*  552 */       Properties copyTo = new Properties();
/*  553 */       Properties copyFrom = block.properties;
/*      */       
/*  555 */       copyTo.destroyTime = copyFrom.destroyTime;
/*  556 */       copyTo.explosionResistance = copyFrom.explosionResistance;
/*  557 */       copyTo.hasCollision = copyFrom.hasCollision;
/*  558 */       copyTo.isRandomlyTicking = copyFrom.isRandomlyTicking;
/*  559 */       copyTo.lightEmission = copyFrom.lightEmission;
/*  560 */       copyTo.mapColor = copyFrom.mapColor;
/*  561 */       copyTo.soundType = copyFrom.soundType;
/*  562 */       copyTo.friction = copyFrom.friction;
/*  563 */       copyTo.speedFactor = copyFrom.speedFactor;
/*  564 */       copyTo.dynamicShape = copyFrom.dynamicShape;
/*  565 */       copyTo.canOcclude = copyFrom.canOcclude;
/*  566 */       copyTo.isAir = copyFrom.isAir;
/*  567 */       copyTo.ignitedByLava = copyFrom.ignitedByLava;
/*  568 */       copyTo.liquid = copyFrom.liquid;
/*  569 */       copyTo.forceSolidOff = copyFrom.forceSolidOff;
/*  570 */       copyTo.forceSolidOn = copyFrom.forceSolidOn;
/*  571 */       copyTo.pushReaction = copyFrom.pushReaction;
/*  572 */       copyTo.requiresCorrectToolForDrops = copyFrom.requiresCorrectToolForDrops;
/*  573 */       copyTo.offsetFunction = copyFrom.offsetFunction;
/*  574 */       copyTo.spawnTerrainParticles = copyFrom.spawnTerrainParticles;
/*  575 */       copyTo.requiredFeatures = copyFrom.requiredFeatures;
/*  576 */       copyTo.emissiveRendering = copyFrom.emissiveRendering;
/*  577 */       copyTo.instrument = copyFrom.instrument;
/*  578 */       copyTo.replaceable = copyFrom.replaceable;
/*      */       
/*  580 */       return copyTo;
/*      */     }
/*      */     
/*      */     public Properties mapColor(DyeColor dyeColor) {
/*  584 */       this.mapColor = (state -> dyeColor.getMapColor());
/*  585 */       return this;
/*      */     }
/*      */     
/*      */     public Properties mapColor(MapColor mapColor) {
/*  589 */       this.mapColor = (state -> mapColor);
/*  590 */       return this;
/*      */     }
/*      */     
/*      */     public Properties mapColor(Function<BlockState, MapColor> mapColor) {
/*  594 */       this.mapColor = mapColor;
/*  595 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noCollision() {
/*  599 */       this.hasCollision = false;
/*  600 */       this.canOcclude = false;
/*  601 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noOcclusion() {
/*  605 */       this.canOcclude = false;
/*  606 */       return this;
/*      */     }
/*      */     
/*      */     public Properties friction(float friction) {
/*  610 */       this.friction = friction;
/*  611 */       return this;
/*      */     }
/*      */     
/*      */     public Properties speedFactor(float speedFactor) {
/*  615 */       this.speedFactor = speedFactor;
/*  616 */       return this;
/*      */     }
/*      */     
/*      */     public Properties jumpFactor(float jumpFactor) {
/*  620 */       this.jumpFactor = jumpFactor;
/*  621 */       return this;
/*      */     }
/*      */     
/*      */     public Properties sound(SoundType soundType) {
/*  625 */       this.soundType = soundType;
/*  626 */       return this;
/*      */     }
/*      */     
/*      */     public Properties lightLevel(ToIntFunction<BlockState> lightEmission) {
/*  630 */       this.lightEmission = lightEmission;
/*  631 */       return this;
/*      */     }
/*      */     
/*      */     public Properties strength(float destroyTime, float explosionResistance) {
/*  635 */       return destroyTime(destroyTime).explosionResistance(explosionResistance);
/*      */     }
/*      */     
/*      */     public Properties instabreak() {
/*  639 */       return strength(0.0F);
/*      */     }
/*      */     
/*      */     public Properties strength(float destroyTime) {
/*  643 */       strength(destroyTime, destroyTime);
/*  644 */       return this;
/*      */     }
/*      */     
/*      */     public Properties randomTicks() {
/*  648 */       this.isRandomlyTicking = true;
/*  649 */       return this;
/*      */     }
/*      */     
/*      */     public Properties dynamicShape() {
/*  653 */       this.dynamicShape = true;
/*  654 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Properties noLootTable() {
/*  662 */       this.drops = DependantName.fixed(Optional.empty());
/*  663 */       return this;
/*      */     }
/*      */     
/*      */     public Properties overrideLootTable(Optional<ResourceKey<LootTable>> table) {
/*  667 */       this.drops = DependantName.fixed(table);
/*  668 */       return this;
/*      */     }
/*      */     
/*      */     protected Optional<ResourceKey<LootTable>> effectiveDrops() {
/*  672 */       return (Optional<ResourceKey<LootTable>>)this.drops.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*      */     }
/*      */     
/*      */     public Properties ignitedByLava() {
/*  676 */       this.ignitedByLava = true;
/*  677 */       return this;
/*      */     }
/*      */     
/*      */     public Properties liquid() {
/*  681 */       this.liquid = true;
/*  682 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Properties forceSolidOn() {
/*  689 */       this.forceSolidOn = true;
/*  690 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Properties forceSolidOff() {
/*  699 */       this.forceSolidOff = true;
/*  700 */       return this;
/*      */     }
/*      */     
/*      */     public Properties pushReaction(PushReaction pushReaction) {
/*  704 */       this.pushReaction = pushReaction;
/*  705 */       return this;
/*      */     }
/*      */     
/*      */     public Properties air() {
/*  709 */       this.isAir = true;
/*  710 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) {
/*  714 */       this.isValidSpawn = isValidSpawn;
/*  715 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isRedstoneConductor(BlockBehaviour.StatePredicate isRedstoneConductor) {
/*  719 */       this.isRedstoneConductor = isRedstoneConductor;
/*  720 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isSuffocating(BlockBehaviour.StatePredicate isSuffocating) {
/*  724 */       this.isSuffocating = isSuffocating;
/*  725 */       return this;
/*      */     }
/*      */     
/*      */     public Properties isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking) {
/*  729 */       this.isViewBlocking = isViewBlocking;
/*  730 */       return this;
/*      */     }
/*      */     
/*      */     public Properties hasPostProcess(BlockBehaviour.StatePredicate hasPostProcess) {
/*  734 */       this.hasPostProcess = hasPostProcess;
/*  735 */       return this;
/*      */     }
/*      */     
/*      */     public Properties emissiveRendering(BlockBehaviour.StatePredicate emissiveRendering) {
/*  739 */       this.emissiveRendering = emissiveRendering;
/*  740 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiresCorrectToolForDrops() {
/*  744 */       this.requiresCorrectToolForDrops = true;
/*  745 */       return this;
/*      */     }
/*      */     
/*      */     public Properties destroyTime(float destroyTime) {
/*  749 */       this.destroyTime = destroyTime;
/*  750 */       return this;
/*      */     }
/*      */     
/*      */     public Properties explosionResistance(float explosionResistance) {
/*  754 */       this.explosionResistance = Math.max(0.0F, explosionResistance);
/*  755 */       return this;
/*      */     }
/*      */     
/*      */     public Properties offsetType(BlockBehaviour.OffsetType offsetType) {
/*  759 */       switch (offsetType.ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  771 */         .offsetFunction = ((state, pos) -> {
/*      */           Block block = state.getBlock();
/*      */           
/*      */           long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
/*      */           
/*      */           float maxHorizontalOffset = block.getMaxHorizontalOffset();
/*      */           
/*      */           double x = Mth.clamp((((float)(seed & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset), z = Mth.clamp((((float)(seed >> 8L & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*      */           return new Vec3(x, 0.0D, z);
/*      */         });
/*  781 */       return this;
/*      */     }
/*      */     
/*      */     public Properties noTerrainParticles() {
/*  785 */       this.spawnTerrainParticles = false;
/*  786 */       return this;
/*      */     }
/*      */     
/*      */     public Properties requiredFeatures(FeatureFlag... flags) {
/*  790 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/*  791 */       return this;
/*      */     }
/*      */     
/*      */     public Properties instrument(NoteBlockInstrument instrument) {
/*  795 */       this.instrument = instrument;
/*  796 */       return this;
/*      */     }
/*      */     
/*      */     public Properties replaceable() {
/*  800 */       this.replaceable = true;
/*  801 */       return this;
/*      */     }
/*      */     
/*      */     public Properties setId(ResourceKey<Block> id) {
/*  805 */       this.id = id;
/*  806 */       return this;
/*      */     }
/*      */     
/*      */     public Properties overrideDescription(String descriptionId) {
/*  810 */       this.descriptionId = DependantName.fixed(descriptionId);
/*  811 */       return this;
/*      */     }
/*      */     
/*      */     protected String effectiveDescriptionId() {
/*  815 */       return (String)this.descriptionId.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*      */     } }
/*      */ 
/*      */   
/*      */   public static abstract class BlockStateBase extends StateHolder<Block, BlockState> {
/*  820 */     private static final Direction[] DIRECTIONS = Direction.values(); private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES; private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES; private final int lightEmission; private final boolean useShapeForLightOcclusion; private final boolean isAir; static {
/*  821 */       EMPTY_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill((Object[])s, Shapes.empty()));
/*  822 */       FULL_BLOCK_OCCLUSION_SHAPES = (VoxelShape[])Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill((Object[])s, Shapes.block()));
/*      */     }
/*      */ 
/*      */     
/*      */     private final boolean ignitedByLava;
/*      */     
/*      */     @Deprecated
/*      */     private final boolean liquid;
/*      */     
/*      */     @Deprecated
/*      */     private boolean legacySolid;
/*      */     private final PushReaction pushReaction;
/*      */     private final MapColor mapColor;
/*      */     private final float destroySpeed;
/*      */     private final boolean requiresCorrectToolForDrops;
/*      */     private final boolean canOcclude;
/*      */     private final BlockBehaviour.StatePredicate isRedstoneConductor;
/*      */     private final BlockBehaviour.StatePredicate isSuffocating;
/*      */     private final BlockBehaviour.StatePredicate isViewBlocking;
/*      */     private final BlockBehaviour.StatePredicate hasPostProcess;
/*      */     private final BlockBehaviour.StatePredicate emissiveRendering;
/*      */     private final BlockBehaviour.OffsetFunction offsetFunction;
/*      */     private final boolean spawnTerrainParticles;
/*      */     private final NoteBlockInstrument instrument;
/*      */     private final boolean replaceable;
/*      */     private Cache cache;
/*  848 */     private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
/*      */     private boolean isRandomlyTicking;
/*      */     private boolean solidRender;
/*      */     private VoxelShape occlusionShape;
/*      */     private VoxelShape[] occlusionShapesByFace;
/*      */     private boolean propagatesSkylightDown;
/*      */     private int lightBlock;
/*      */     
/*      */     protected BlockStateBase(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
/*  857 */       super(owner, values, propertiesCodec);
/*  858 */       BlockBehaviour.Properties properties = owner.properties;
/*      */       
/*  860 */       this.lightEmission = properties.lightEmission.applyAsInt(asState());
/*  861 */       this.useShapeForLightOcclusion = owner.useShapeForLightOcclusion(asState());
/*  862 */       this.isAir = properties.isAir;
/*  863 */       this.ignitedByLava = properties.ignitedByLava;
/*  864 */       this.liquid = properties.liquid;
/*  865 */       this.pushReaction = properties.pushReaction;
/*  866 */       this.mapColor = properties.mapColor.apply(asState());
/*  867 */       this.destroySpeed = properties.destroyTime;
/*  868 */       this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
/*  869 */       this.canOcclude = properties.canOcclude;
/*  870 */       this.isRedstoneConductor = properties.isRedstoneConductor;
/*  871 */       this.isSuffocating = properties.isSuffocating;
/*  872 */       this.isViewBlocking = properties.isViewBlocking;
/*  873 */       this.hasPostProcess = properties.hasPostProcess;
/*  874 */       this.emissiveRendering = properties.emissiveRendering;
/*  875 */       this.offsetFunction = properties.offsetFunction;
/*  876 */       this.spawnTerrainParticles = properties.spawnTerrainParticles;
/*  877 */       this.instrument = properties.instrument;
/*  878 */       this.replaceable = properties.replaceable;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean calculateSolid() {
/*  887 */       if (this.owner.properties.forceSolidOn) {
/*  888 */         return true;
/*      */       }
/*  890 */       if (this.owner.properties.forceSolidOff) {
/*  891 */         return false;
/*      */       }
/*  893 */       if (this.cache == null) {
/*  894 */         return false;
/*      */       }
/*  896 */       VoxelShape shape = this.cache.collisionShape;
/*  897 */       if (shape.isEmpty()) {
/*  898 */         return false;
/*      */       }
/*  900 */       AABB bounds = shape.bounds();
/*  901 */       if (bounds.getSize() >= 0.7291666666666666D) {
/*  902 */         return true;
/*      */       }
/*  904 */       if (bounds.getYsize() >= 1.0D) {
/*  905 */         return true;
/*      */       }
/*  907 */       return false;
/*      */     }
/*      */     
/*      */     public void initCache() {
/*  911 */       this.fluidState = this.owner.getFluidState(asState());
/*  912 */       this.isRandomlyTicking = this.owner.isRandomlyTicking(asState());
/*  913 */       if (!getBlock().hasDynamicShape()) {
/*  914 */         this.cache = new Cache(asState());
/*      */       }
/*  916 */       this.legacySolid = calculateSolid();
/*      */       
/*  918 */       this.occlusionShape = this.canOcclude ? this.owner.getOcclusionShape(asState()) : Shapes.empty();
/*  919 */       this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
/*      */       
/*  921 */       if (this.occlusionShape.isEmpty()) {
/*  922 */         this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
/*  923 */       } else if (this.solidRender) {
/*  924 */         this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
/*      */       } else {
/*  926 */         this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];
/*  927 */         for (Direction direction : DIRECTIONS) {
/*  928 */           this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction);
/*      */         }
/*      */       } 
/*      */       
/*  932 */       this.propagatesSkylightDown = this.owner.propagatesSkylightDown(asState());
/*  933 */       this.lightBlock = this.owner.getLightBlock(asState());
/*      */     }
/*      */     
/*      */     public Block getBlock() {
/*  937 */       return this.owner;
/*      */     }
/*      */     
/*      */     public Holder<Block> getBlockHolder() {
/*  941 */       return (Holder<Block>)this.owner.builtInRegistryHolder();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public boolean blocksMotion() {
/*  957 */       Block block = getBlock();
/*  958 */       return (block != Blocks.COBWEB && block != Blocks.BAMBOO_SAPLING && isSolid());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public boolean isSolid() {
/*  976 */       return this.legacySolid;
/*      */     }
/*      */     
/*      */     public boolean isValidSpawn(BlockGetter level, BlockPos pos, EntityType<?> type) {
/*  980 */       return (getBlock()).properties.isValidSpawn.test(asState(), level, pos, type);
/*      */     }
/*      */     
/*      */     public boolean propagatesSkylightDown() {
/*  984 */       return this.propagatesSkylightDown;
/*      */     }
/*      */     
/*      */     public int getLightBlock() {
/*  988 */       return this.lightBlock;
/*      */     }
/*      */     
/*      */     public VoxelShape getFaceOcclusionShape(Direction direction) {
/*  992 */       return this.occlusionShapesByFace[direction.ordinal()];
/*      */     }
/*      */     
/*      */     public VoxelShape getOcclusionShape() {
/*  996 */       return this.occlusionShape;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasLargeCollisionShape() {
/* 1001 */       return (this.cache == null || this.cache.largeCollisionShape);
/*      */     }
/*      */     
/*      */     public boolean useShapeForLightOcclusion() {
/* 1005 */       return this.useShapeForLightOcclusion;
/*      */     }
/*      */     
/*      */     public int getLightEmission() {
/* 1009 */       return this.lightEmission;
/*      */     }
/*      */     
/*      */     public boolean isAir() {
/* 1013 */       return this.isAir;
/*      */     }
/*      */     
/*      */     public boolean ignitedByLava() {
/* 1017 */       return this.ignitedByLava;
/*      */     }
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public boolean liquid() {
/* 1023 */       return this.liquid;
/*      */     }
/*      */     
/*      */     public MapColor getMapColor(BlockGetter level, BlockPos pos) {
/* 1027 */       return this.mapColor;
/*      */     }
/*      */     
/*      */     public BlockState rotate(Rotation rotation) {
/* 1031 */       return getBlock().rotate(asState(), rotation);
/*      */     }
/*      */     
/*      */     public BlockState mirror(Mirror mirror) {
/* 1035 */       return getBlock().mirror(asState(), mirror);
/*      */     }
/*      */     
/*      */     public RenderShape getRenderShape() {
/* 1039 */       return getBlock().getRenderShape(asState());
/*      */     }
/*      */     
/*      */     public boolean emissiveRendering(BlockGetter level, BlockPos pos) {
/* 1043 */       return this.emissiveRendering.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public float getShadeBrightness(BlockGetter level, BlockPos pos) {
/* 1047 */       return getBlock().getShadeBrightness(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean isRedstoneConductor(BlockGetter level, BlockPos pos) {
/* 1051 */       return this.isRedstoneConductor.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean isSignalSource() {
/* 1055 */       return getBlock().isSignalSource(asState());
/*      */     }
/*      */     
/*      */     public int getSignal(BlockGetter level, BlockPos pos, Direction direction) {
/* 1059 */       return getBlock().getSignal(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public boolean hasAnalogOutputSignal() {
/* 1063 */       return getBlock().hasAnalogOutputSignal(asState());
/*      */     }
/*      */     
/*      */     public int getAnalogOutputSignal(Level level, BlockPos pos, Direction direction) {
/* 1067 */       return getBlock().getAnalogOutputSignal(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public float getDestroySpeed(BlockGetter level, BlockPos pos) {
/* 1071 */       return this.destroySpeed;
/*      */     }
/*      */     
/*      */     public float getDestroyProgress(Player player, BlockGetter level, BlockPos pos) {
/* 1075 */       return getBlock().getDestroyProgress(asState(), player, level, pos);
/*      */     }
/*      */     
/*      */     public int getDirectSignal(BlockGetter level, BlockPos pos, Direction direction) {
/* 1079 */       return getBlock().getDirectSignal(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public PushReaction getPistonPushReaction() {
/* 1083 */       return this.pushReaction;
/*      */     }
/*      */     
/*      */     public boolean isSolidRender() {
/* 1087 */       return this.solidRender;
/*      */     }
/*      */     
/*      */     public boolean canOcclude() {
/* 1091 */       return this.canOcclude;
/*      */     }
/*      */     
/*      */     public boolean skipRendering(BlockState neighborState, Direction direction) {
/* 1095 */       return getBlock().skipRendering(asState(), neighborState, direction);
/*      */     }
/*      */     
/*      */     public VoxelShape getShape(BlockGetter level, BlockPos pos) {
/* 1099 */       return getShape(level, pos, CollisionContext.empty());
/*      */     }
/*      */     
/*      */     public VoxelShape getShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1103 */       return getBlock().getShape(asState(), level, pos, context);
/*      */     }
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/* 1107 */       if (this.cache != null) {
/* 1108 */         return this.cache.collisionShape;
/*      */       }
/* 1110 */       return getCollisionShape(level, pos, CollisionContext.empty());
/*      */     }
/*      */     
/*      */     public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1114 */       return getBlock().getCollisionShape(asState(), level, pos, context);
/*      */     }
/*      */     
/*      */     public VoxelShape getEntityInsideCollisionShape(BlockGetter level, BlockPos pos, Entity entity) {
/* 1118 */       return getBlock().getEntityInsideCollisionShape(asState(), level, pos, entity);
/*      */     }
/*      */     
/*      */     public VoxelShape getBlockSupportShape(BlockGetter level, BlockPos pos) {
/* 1122 */       return getBlock().getBlockSupportShape(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public VoxelShape getVisualShape(BlockGetter level, BlockPos pos, CollisionContext context) {
/* 1126 */       return getBlock().getVisualShape(asState(), level, pos, context);
/*      */     }
/*      */     
/*      */     public VoxelShape getInteractionShape(BlockGetter level, BlockPos pos) {
/* 1130 */       return getBlock().getInteractionShape(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public final boolean entityCanStandOn(BlockGetter level, BlockPos pos, Entity entity) {
/* 1134 */       return entityCanStandOnFace(level, pos, entity, Direction.UP);
/*      */     }
/*      */     
/*      */     public final boolean entityCanStandOnFace(BlockGetter level, BlockPos pos, Entity entity, Direction faceDirection) {
/* 1138 */       return Block.isFaceFull(getCollisionShape(level, pos, CollisionContext.of(entity)), faceDirection);
/*      */     }
/*      */     
/*      */     public Vec3 getOffset(BlockPos pos) {
/* 1142 */       BlockBehaviour.OffsetFunction function = this.offsetFunction;
/* 1143 */       if (function != null) {
/* 1144 */         return function.evaluate(asState(), pos);
/*      */       }
/* 1146 */       return Vec3.ZERO;
/*      */     }
/*      */     
/*      */     public boolean hasOffsetFunction() {
/* 1150 */       return (this.offsetFunction != null);
/*      */     }
/*      */     
/*      */     public boolean triggerEvent(Level level, BlockPos pos, int b0, int b1) {
/* 1154 */       return getBlock().triggerEvent(asState(), level, pos, b0, b1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void handleNeighborChanged(Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 1162 */       getBlock().neighborChanged(asState(), level, pos, block, orientation, movedByPiston);
/*      */     }
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, @Block.UpdateFlags int updateFlags) {
/* 1166 */       updateNeighbourShapes(level, pos, updateFlags, 512);
/*      */     }
/*      */     
/*      */     public final void updateNeighbourShapes(LevelAccessor level, BlockPos pos, @Block.UpdateFlags int updateFlags, int updateLimit) {
/* 1170 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 1171 */       for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
/* 1172 */         blockPos.setWithOffset((Vec3i)pos, direction);
/* 1173 */         level.neighborShapeChanged(direction.getOpposite(), (BlockPos)blockPos, pos, asState(), updateFlags, updateLimit);
/*      */       } 
/*      */     }
/*      */     
/*      */     public final void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, @Block.UpdateFlags int updateFlags) {
/* 1178 */       updateIndirectNeighbourShapes(level, pos, updateFlags, 512);
/*      */     }
/*      */     
/*      */     public void updateIndirectNeighbourShapes(LevelAccessor level, BlockPos pos, @Block.UpdateFlags int updateFlags, int updateLimit) {
/* 1182 */       getBlock().updateIndirectNeighbourShapes(asState(), level, pos, updateFlags, updateLimit);
/*      */     }
/*      */     
/*      */     public void onPlace(Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 1186 */       getBlock().onPlace(asState(), level, pos, oldState, movedByPiston);
/*      */     }
/*      */     
/*      */     public void affectNeighborsAfterRemoval(ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 1190 */       getBlock().affectNeighborsAfterRemoval(asState(), level, pos, movedByPiston);
/*      */     }
/*      */     
/*      */     public void onExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 1194 */       getBlock().onExplosionHit(asState(), level, pos, explosion, onHit);
/*      */     }
/*      */     
/*      */     public void tick(ServerLevel level, BlockPos pos, RandomSource random) {
/* 1198 */       getBlock().tick(asState(), level, pos, random);
/*      */     }
/*      */     
/*      */     public void randomTick(ServerLevel level, BlockPos pos, RandomSource random) {
/* 1202 */       getBlock().randomTick(asState(), level, pos, random);
/*      */     }
/*      */     
/*      */     public void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 1206 */       getBlock().entityInside(asState(), level, pos, entity, effectApplier, isPrecise);
/*      */     }
/*      */     
/*      */     public void spawnAfterBreak(ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 1210 */       getBlock().spawnAfterBreak(asState(), level, pos, tool, dropExperience);
/*      */     }
/*      */     
/*      */     public List<ItemStack> getDrops(LootParams.Builder params) {
/* 1214 */       return getBlock().getDrops(asState(), params);
/*      */     }
/*      */     
/*      */     public InteractionResult useItemOn(ItemStack itemStack, Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 1218 */       return getBlock().useItemOn(itemStack, asState(), level, hitResult.getBlockPos(), player, hand, hitResult);
/*      */     }
/*      */     
/*      */     public InteractionResult useWithoutItem(Level level, Player player, BlockHitResult hitResult) {
/* 1222 */       return getBlock().useWithoutItem(asState(), level, hitResult.getBlockPos(), player, hitResult);
/*      */     }
/*      */     
/*      */     public void attack(Level level, BlockPos pos, Player player) {
/* 1226 */       getBlock().attack(asState(), level, pos, player);
/*      */     }
/*      */     
/*      */     public boolean isSuffocating(BlockGetter level, BlockPos pos) {
/* 1230 */       return this.isSuffocating.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean isViewBlocking(BlockGetter level, BlockPos pos) {
/* 1234 */       return this.isViewBlocking.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public BlockState updateShape(LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 1238 */       return getBlock().updateShape(asState(), level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*      */     }
/*      */     
/*      */     public boolean isPathfindable(PathComputationType type) {
/* 1242 */       return getBlock().isPathfindable(asState(), type);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced(BlockPlaceContext context) {
/* 1246 */       return getBlock().canBeReplaced(asState(), context);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced(Fluid fluid) {
/* 1250 */       return getBlock().canBeReplaced(asState(), fluid);
/*      */     }
/*      */     
/*      */     public boolean canBeReplaced() {
/* 1254 */       return this.replaceable;
/*      */     }
/*      */     
/*      */     public boolean canSurvive(LevelReader level, BlockPos pos) {
/* 1258 */       return getBlock().canSurvive(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean hasPostProcess(BlockGetter level, BlockPos pos) {
/* 1262 */       return this.hasPostProcess.test(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public MenuProvider getMenuProvider(Level level, BlockPos pos) {
/* 1266 */       return getBlock().getMenuProvider(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public boolean is(TagKey<Block> tag) {
/* 1270 */       return getBlock().builtInRegistryHolder().is(tag);
/*      */     }
/*      */     
/*      */     public boolean is(TagKey<Block> tag, Predicate<BlockStateBase> predicate) {
/* 1274 */       return (is(tag) && predicate.test(this));
/*      */     }
/*      */     
/*      */     public boolean is(HolderSet<Block> set) {
/* 1278 */       return set.contains((Holder)getBlock().builtInRegistryHolder());
/*      */     }
/*      */     
/*      */     public boolean is(Holder<Block> holder) {
/* 1282 */       return is((Block)holder.value());
/*      */     }
/*      */     
/*      */     public Stream<TagKey<Block>> getTags() {
/* 1286 */       return getBlock().builtInRegistryHolder().tags();
/*      */     }
/*      */     
/*      */     public boolean hasBlockEntity() {
/* 1290 */       return getBlock() instanceof EntityBlock;
/*      */     }
/*      */     
/*      */     public boolean shouldChangedStateKeepBlockEntity(BlockState oldState) {
/* 1294 */       return getBlock().shouldChangedStateKeepBlockEntity(oldState);
/*      */     }
/*      */     
/*      */     public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockEntityType<T> type) {
/* 1298 */       if (getBlock() instanceof EntityBlock) {
/* 1299 */         return ((EntityBlock)getBlock()).getTicker(level, asState(), type);
/*      */       }
/* 1301 */       return null;
/*      */     }
/*      */     
/*      */     public boolean is(Block block) {
/* 1305 */       return (getBlock() == block);
/*      */     }
/*      */     
/*      */     public boolean is(ResourceKey<Block> block) {
/* 1309 */       return getBlock().builtInRegistryHolder().is(block);
/*      */     }
/*      */     
/*      */     public FluidState getFluidState() {
/* 1313 */       return this.fluidState;
/*      */     }
/*      */     
/*      */     public boolean isRandomlyTicking() {
/* 1317 */       return this.isRandomlyTicking;
/*      */     }
/*      */     
/*      */     public long getSeed(BlockPos pos) {
/* 1321 */       return getBlock().getSeed(asState(), pos);
/*      */     }
/*      */     
/*      */     public SoundType getSoundType() {
/* 1325 */       return getBlock().getSoundType(asState());
/*      */     }
/*      */     
/*      */     public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile entity) {
/* 1329 */       getBlock().onProjectileHit(level, state, blockHit, entity);
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction) {
/* 1333 */       return isFaceSturdy(level, pos, direction, SupportType.FULL);
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(BlockGetter level, BlockPos pos, Direction direction, SupportType supportType) {
/* 1337 */       if (this.cache != null) {
/* 1338 */         return this.cache.isFaceSturdy(direction, supportType);
/*      */       }
/* 1340 */       return supportType.isSupporting(asState(), level, pos, direction);
/*      */     }
/*      */     
/*      */     public boolean isCollisionShapeFullBlock(BlockGetter level, BlockPos pos) {
/* 1344 */       if (this.cache != null) {
/* 1345 */         return this.cache.isCollisionShapeFullBlock;
/*      */       }
/* 1347 */       return getBlock().isCollisionShapeFullBlock(asState(), level, pos);
/*      */     }
/*      */     
/*      */     public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, boolean includeData) {
/* 1351 */       return getBlock().getCloneItemStack(level, pos, asState(), includeData);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean requiresCorrectToolForDrops() {
/* 1357 */       return this.requiresCorrectToolForDrops;
/*      */     }
/*      */     
/*      */     public boolean shouldSpawnTerrainParticles() {
/* 1361 */       return this.spawnTerrainParticles;
/*      */     }
/*      */     
/*      */     public NoteBlockInstrument instrument() {
/* 1365 */       return this.instrument;
/*      */     }
/*      */     protected abstract BlockState asState();
/*      */     
/* 1369 */     private static final class Cache { private static final Direction[] DIRECTIONS = Direction.values();
/* 1370 */       private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length;
/*      */       protected final VoxelShape collisionShape;
/*      */       protected final boolean largeCollisionShape;
/*      */       private final boolean[] faceSturdy;
/*      */       protected final boolean isCollisionShapeFullBlock;
/*      */       
/*      */       private Cache(BlockState state) {
/* 1377 */         Block block = state.getBlock();
/*      */         
/* 1379 */         this.collisionShape = block.getCollisionShape(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/* 1380 */         if (!this.collisionShape.isEmpty() && state.hasOffsetFunction()) {
/* 1381 */           throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) }));
/*      */         }
/* 1383 */         this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/* 1384 */         this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/* 1385 */         for (Direction direction : DIRECTIONS) {
/* 1386 */           for (SupportType type : SupportType.values()) {
/* 1387 */             this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction);
/*      */           }
/*      */         } 
/* 1390 */         this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */       }
/*      */       
/*      */       public boolean isFaceSturdy(Direction direction, SupportType supportType) {
/* 1394 */         return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
/*      */       }
/*      */       
/*      */       private static int getFaceSupportIndex(Direction direction, SupportType supportType) {
/* 1398 */         return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal(); } } } private static final class Cache { private static final Direction[] DIRECTIONS = Direction.values(); private static final int SUPPORT_TYPE_COUNT = (SupportType.values()).length; private static int getFaceSupportIndex(Direction direction, SupportType supportType) { return direction.ordinal() * SUPPORT_TYPE_COUNT + supportType.ordinal(); }
/*      */ 
/*      */     
/*      */     protected final VoxelShape collisionShape;
/*      */     protected final boolean largeCollisionShape;
/*      */     private final boolean[] faceSturdy;
/*      */     protected final boolean isCollisionShapeFullBlock;
/*      */     
/*      */     private Cache(BlockState state) {
/*      */       Block block = state.getBlock();
/*      */       this.collisionShape = block.getCollisionShape(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
/*      */       if (!this.collisionShape.isEmpty() && state.hasOffsetFunction())
/*      */         throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", new Object[] { BuiltInRegistries.BLOCK.getKey(block) })); 
/*      */       this.largeCollisionShape = Arrays.<Direction.Axis>stream(Direction.Axis.values()).anyMatch(axis -> (this.collisionShape.min(axis) < 0.0D || this.collisionShape.max(axis) > 1.0D));
/*      */       this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
/*      */       for (Direction direction : DIRECTIONS) {
/*      */         for (SupportType type : SupportType.values())
/*      */           this.faceSturdy[getFaceSupportIndex(direction, type)] = type.isSupporting(state, (BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO, direction); 
/*      */       } 
/*      */       this.isCollisionShapeFullBlock = Block.isShapeFullBlock(state.getCollisionShape((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
/*      */     }
/*      */     
/*      */     public boolean isFaceSturdy(Direction direction, SupportType supportType) {
/*      */       return this.faceSturdy[getFaceSupportIndex(direction, supportType)];
/*      */     } }
/*      */ 
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface StateArgumentPredicate<A> {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, A param1A);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface OffsetFunction {
/*      */     Vec3 evaluate(BlockState param1BlockState, BlockPos param1BlockPos);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface StatePredicate {
/*      */     boolean test(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/BlockBehaviour.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */