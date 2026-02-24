/*      */ package net.minecraft.gametest.framework;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import io.netty.channel.ChannelHandler;
/*      */ import io.netty.channel.embedded.EmbeddedChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.IntPredicate;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.LongStream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.PacketFlow;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.commands.FillBiomeCommand;
/*      */ import net.minecraft.server.level.ClientInformation;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.network.CommonListenerCookie;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.context.UseOnContext;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ButtonBlock;
/*      */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*      */ import net.minecraft.world.level.block.LeverBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.entity.EntityTypeTest;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class GameTestHelper {
/*      */   private final GameTestInfo testInfo;
/*      */   private boolean finalCheckAdded;
/*      */   
/*      */   public GameTestHelper(GameTestInfo testInfo) {
/*   82 */     this.testInfo = testInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public GameTestAssertException assertionException(Component description) {
/*   88 */     return new GameTestAssertException(description, this.testInfo.getTick());
/*      */   }
/*      */   
/*      */   public GameTestAssertException assertionException(String descriptionId, Object... arguments) {
/*   92 */     return assertionException((Component)Component.translatableEscape(descriptionId, arguments));
/*      */   }
/*      */   
/*      */   public GameTestAssertPosException assertionException(BlockPos pos, Component description) {
/*   96 */     return new GameTestAssertPosException(description, absolutePos(pos), pos, this.testInfo.getTick());
/*      */   }
/*      */   
/*      */   public GameTestAssertPosException assertionException(BlockPos pos, String descriptionId, Object... arguments) {
/*  100 */     return assertionException(pos, (Component)Component.translatableEscape(descriptionId, arguments));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ServerLevel getLevel() {
/*  106 */     return this.testInfo.getLevel();
/*      */   }
/*      */   
/*      */   public BlockState getBlockState(BlockPos pos) {
/*  110 */     return getLevel().getBlockState(absolutePos(pos));
/*      */   }
/*      */   
/*      */   public <T extends BlockEntity> T getBlockEntity(BlockPos pos, Class<T> type) {
/*  114 */     BlockEntity blockEntity = getLevel().getBlockEntity(absolutePos(pos));
/*  115 */     if (blockEntity == null) {
/*  116 */       throw assertionException(pos, "test.error.missing_block_entity", new Object[0]);
/*      */     }
/*  118 */     if (type.isInstance(blockEntity)) {
/*  119 */       return type.cast(blockEntity);
/*      */     }
/*  121 */     throw assertionException(pos, "test.error.wrong_block_entity", new Object[] { blockEntity.getType().builtInRegistryHolder().getRegisteredName() });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void killAllEntities() {
/*  127 */     killAllEntitiesOfClass(Entity.class);
/*      */   }
/*      */   
/*      */   public void killAllEntitiesOfClass(Class<? extends Entity> baseClass) {
/*  131 */     AABB bounds = getBounds();
/*  132 */     List<? extends Entity> entities = getLevel().getEntitiesOfClass(baseClass, bounds.inflate(1.0D), mob -> !(mob instanceof Player));
/*  133 */     entities.forEach(entity -> entity.kill(getLevel()));
/*      */   }
/*      */   
/*      */   public ItemEntity spawnItem(Item item, Vec3 pos) {
/*  137 */     ServerLevel level = getLevel();
/*  138 */     Vec3 absoluteVec = absoluteVec(pos);
/*  139 */     ItemEntity itemEntity = new ItemEntity((Level)level, absoluteVec.x, absoluteVec.y, absoluteVec.z, new ItemStack((net.minecraft.world.level.ItemLike)item, 1));
/*  140 */     itemEntity.setDeltaMovement(0.0D, 0.0D, 0.0D);
/*  141 */     level.addFreshEntity((Entity)itemEntity);
/*  142 */     return itemEntity;
/*      */   }
/*      */   
/*      */   public ItemEntity spawnItem(Item item, float x, float y, float z) {
/*  146 */     return spawnItem(item, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public ItemEntity spawnItem(Item item, BlockPos pos) {
/*  150 */     return spawnItem(item, pos.getX(), pos.getY(), pos.getZ());
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, BlockPos pos) {
/*  154 */     return spawn(entityType, Vec3.atBottomCenterOf((Vec3i)pos));
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> spawn(EntityType<E> entityType, BlockPos pos, int amount) {
/*  158 */     return spawn(entityType, Vec3.atBottomCenterOf((Vec3i)pos), amount);
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> spawn(EntityType<E> entityType, Vec3 pos, int amount) {
/*  162 */     List<E> entities = new ArrayList<>();
/*  163 */     for (int i = 0; i < amount; i++) {
/*  164 */       entities.add(spawn(entityType, pos));
/*      */     }
/*  166 */     return entities;
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, Vec3 pos) {
/*  170 */     return spawn(entityType, pos, null);
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, Vec3 pos, EntitySpawnReason spawnReason) {
/*  174 */     ServerLevel level = getLevel();
/*      */ 
/*      */     
/*  177 */     Entity entity = entityType.create((Level)level, EntitySpawnReason.STRUCTURE);
/*  178 */     if (entity == null) {
/*  179 */       throw assertionException(BlockPos.containing(pos), "test.error.spawn_failure", new Object[] { entityType.builtInRegistryHolder().getRegisteredName() });
/*      */     }
/*  181 */     if (entity instanceof Mob) { Mob mob = (Mob)entity;
/*  182 */       mob.setPersistenceRequired(); }
/*      */     
/*  184 */     Vec3 absoluteVec = absoluteVec(pos);
/*  185 */     float yRot = entity.rotate(getTestRotation());
/*  186 */     entity.snapTo(absoluteVec.x, absoluteVec.y, absoluteVec.z, yRot, entity.getXRot());
/*  187 */     entity.setYBodyRot(yRot);
/*  188 */     entity.setYHeadRot(yRot);
/*  189 */     if (spawnReason != null && entity instanceof Mob) { Mob mob = (Mob)entity;
/*  190 */       mob.finalizeSpawn((ServerLevelAccessor)getLevel(), getLevel().getCurrentDifficultyAt(mob.blockPosition()), spawnReason, null); }
/*      */     
/*  192 */     level.addFreshEntityWithPassengers(entity);
/*  193 */     return (E)entity;
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawn(EntityType<E> entityType, int x, int y, int z, EntitySpawnReason entitySpawnReason) {
/*  197 */     return (E)spawn(entityType, new Vec3(x, y, z), entitySpawnReason);
/*      */   }
/*      */   
/*      */   public void hurt(Entity entity, DamageSource source, float damage) {
/*  201 */     entity.hurtServer(getLevel(), source, damage);
/*      */   }
/*      */   
/*      */   public void kill(Entity entity) {
/*  205 */     entity.kill(getLevel());
/*      */   }
/*      */   
/*      */   public <E extends Entity> E findOneEntity(EntityType<E> entityType) {
/*  209 */     return findClosestEntity(entityType, 0, 0, 0, 2.147483647E9D);
/*      */   }
/*      */   
/*      */   public <E extends Entity> E findClosestEntity(EntityType<E> entityType, int x, int y, int z, double distance) {
/*  213 */     List<E> entities = findEntities(entityType, x, y, z, distance);
/*  214 */     if (entities.isEmpty()) {
/*  215 */       throw assertionException("test.error.expected_entity_around", new Object[] { entityType.getDescription(), x, y, z });
/*      */     }
/*  217 */     if (entities.size() > 1) {
/*  218 */       throw assertionException("test.error.too_many_entities", new Object[] { entityType.toShortString(), x, y, z, entities.size() });
/*      */     }
/*  220 */     Vec3 center = absoluteVec(new Vec3(x, y, z));
/*      */     
/*  222 */     entities.sort((e1, e2) -> {
/*      */           double d1 = e1.position().distanceTo(center), d2 = e2.position().distanceTo(center);
/*      */           
/*      */           return Double.compare(d1, d2);
/*      */         });
/*  227 */     return entities.get(0);
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> findEntities(EntityType<E> entityType, int x, int y, int z, double distance) {
/*  231 */     return findEntities(entityType, Vec3.atBottomCenterOf((Vec3i)new BlockPos(x, y, z)), distance);
/*      */   }
/*      */   
/*      */   public <E extends Entity> List<E> findEntities(EntityType<E> entityType, Vec3 pos, double distance) {
/*  235 */     ServerLevel level = getLevel();
/*  236 */     Vec3 absoluteVec = absoluteVec(pos);
/*  237 */     AABB structureBounds = this.testInfo.getStructureBounds();
/*  238 */     AABB containedBounds = new AABB(absoluteVec.add(-distance, -distance, -distance), absoluteVec.add(distance, distance, distance));
/*  239 */     return level.getEntities((EntityTypeTest)entityType, structureBounds, e -> (e.getBoundingBox().intersects(containedBounds) && e.isAlive()));
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, int x, int y, int z) {
/*  243 */     return spawn(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Entity> E spawn(EntityType<E> entityType, float x, float y, float z) {
/*  247 */     return spawn(entityType, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, BlockPos pos) {
/*  251 */     Mob mob = spawn(entityType, pos);
/*  252 */     mob.removeFreeWill();
/*  253 */     return (E)mob;
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, int x, int y, int z) {
/*  257 */     return spawnWithNoFreeWill(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, Vec3 pos) {
/*  261 */     Mob mob = spawn(entityType, pos);
/*  262 */     mob.removeFreeWill();
/*  263 */     return (E)mob;
/*      */   }
/*      */   
/*      */   public <E extends Mob> E spawnWithNoFreeWill(EntityType<E> entityType, float x, float y, float z) {
/*  267 */     return spawnWithNoFreeWill(entityType, new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public void moveTo(Mob mob, float x, float y, float z) {
/*  271 */     Vec3 absoluteVec = absoluteVec(new Vec3(x, y, z));
/*  272 */     mob.snapTo(absoluteVec.x, absoluteVec.y, absoluteVec.z, mob.getYRot(), mob.getXRot());
/*      */   }
/*      */   
/*      */   public GameTestSequence walkTo(Mob mob, BlockPos targetPos, float speedModifier) {
/*  276 */     return startSequence().thenExecuteAfter(2, () -> {
/*      */           Path path = mob.getNavigation().createPath(absolutePos(targetPos), 0);
/*      */           mob.getNavigation().moveTo(path, speedModifier);
/*      */         });
/*      */   }
/*      */   
/*      */   public void pressButton(int x, int y, int z) {
/*  283 */     pressButton(new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void pressButton(BlockPos buttonPos) {
/*  287 */     assertBlockTag(BlockTags.BUTTONS, buttonPos);
/*      */     
/*  289 */     BlockPos absolutePos = absolutePos(buttonPos);
/*  290 */     BlockState blockState = getLevel().getBlockState(absolutePos);
/*      */     
/*  292 */     ButtonBlock buttonBlock = (ButtonBlock)blockState.getBlock();
/*  293 */     buttonBlock.press(blockState, (Level)getLevel(), absolutePos, null);
/*      */   }
/*      */   
/*      */   public void useBlock(BlockPos relativePos) {
/*  297 */     useBlock(relativePos, makeMockPlayer(GameType.CREATIVE));
/*      */   }
/*      */   
/*      */   public void useBlock(BlockPos relativePos, Player player) {
/*  301 */     BlockPos absolutePos = absolutePos(relativePos);
/*  302 */     useBlock(relativePos, player, new BlockHitResult(Vec3.atCenterOf((Vec3i)absolutePos), Direction.NORTH, absolutePos, true));
/*      */   }
/*      */   
/*      */   public void useBlock(BlockPos relativePos, Player player, BlockHitResult hitResult) {
/*  306 */     BlockPos absolutePos = absolutePos(relativePos);
/*  307 */     BlockState blockState = getLevel().getBlockState(absolutePos);
/*      */     
/*  309 */     InteractionHand hand = InteractionHand.MAIN_HAND;
/*      */     
/*  311 */     InteractionResult itemInteractionResult = blockState.useItemOn(player.getItemInHand(hand), (Level)getLevel(), player, hand, hitResult);
/*  312 */     if (itemInteractionResult.consumesAction()) {
/*      */       return;
/*      */     }
/*      */     
/*  316 */     if (itemInteractionResult instanceof InteractionResult.TryEmptyHandInteraction && blockState.useWithoutItem((Level)getLevel(), player, hitResult).consumesAction()) {
/*      */       return;
/*      */     }
/*      */     
/*  320 */     UseOnContext context = new UseOnContext(player, hand, hitResult);
/*  321 */     player.getItemInHand(hand).useOn(context);
/*      */   }
/*      */   
/*      */   public LivingEntity makeAboutToDrown(LivingEntity entity) {
/*  325 */     entity.setAirSupply(0);
/*  326 */     entity.setHealth(0.25F);
/*  327 */     return entity;
/*      */   }
/*      */   
/*      */   public LivingEntity withLowHealth(LivingEntity entity) {
/*  331 */     entity.setHealth(0.25F);
/*  332 */     return entity;
/*      */   }
/*      */   
/*      */   public Player makeMockPlayer(final GameType gameType) {
/*  336 */     return new Player(this, (Level)getLevel(), new GameProfile(UUID.randomUUID(), "test-mock-player"))
/*      */       {
/*      */         public GameType gameMode() {
/*  339 */           return gameType;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isClientAuthoritative() {
/*  344 */           return false;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated(forRemoval = true)
/*      */   public ServerPlayer makeMockServerPlayerInLevel() {
/*  355 */     CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "test-mock-player"), false);
/*  356 */     ServerPlayer player = new ServerPlayer(this, getLevel().getServer(), getLevel(), cookie.gameProfile(), cookie.clientInformation())
/*      */       {
/*      */         public GameType gameMode() {
/*  359 */           return GameType.CREATIVE;
/*      */         }
/*      */       };
/*  362 */     Connection connection = new Connection(PacketFlow.SERVERBOUND);
/*      */ 
/*      */     
/*  365 */     EmbeddedChannel channel = new EmbeddedChannel(new ChannelHandler[] { (ChannelHandler)connection });
/*  366 */     getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
/*  367 */     return player;
/*      */   }
/*      */   
/*      */   public void pullLever(int x, int y, int z) {
/*  371 */     pullLever(new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void pullLever(BlockPos leverPos) {
/*  375 */     assertBlockPresent(Blocks.LEVER, leverPos);
/*      */     
/*  377 */     BlockPos absolutePos = absolutePos(leverPos);
/*  378 */     BlockState blockState = getLevel().getBlockState(absolutePos);
/*      */     
/*  380 */     LeverBlock leverBlock = (LeverBlock)blockState.getBlock();
/*  381 */     leverBlock.pull(blockState, (Level)getLevel(), absolutePos, null);
/*      */   }
/*      */   
/*      */   public void pulseRedstone(BlockPos pos, long duration) {
/*  385 */     setBlock(pos, Blocks.REDSTONE_BLOCK);
/*  386 */     runAfterDelay(duration, () -> setBlock(pos, Blocks.AIR));
/*      */   }
/*      */   
/*      */   public void destroyBlock(BlockPos pos) {
/*  390 */     getLevel().destroyBlock(absolutePos(pos), false, null);
/*      */   }
/*      */   
/*      */   public void setBlock(int x, int y, int z, Block block) {
/*  394 */     setBlock(new BlockPos(x, y, z), block);
/*      */   }
/*      */   
/*      */   public void setBlock(int x, int y, int z, BlockState state) {
/*  398 */     setBlock(new BlockPos(x, y, z), state);
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, Block block) {
/*  402 */     setBlock(blockPos, block.defaultBlockState());
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, BlockState state) {
/*  406 */     getLevel().setBlock(absolutePos(blockPos), state, 3);
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, Block block, Direction direction) {
/*  410 */     setBlock(blockPos, block.defaultBlockState(), direction);
/*      */   }
/*      */   
/*      */   public void setBlock(BlockPos blockPos, BlockState blockState, Direction direction) {
/*  414 */     BlockState state = blockState;
/*      */     
/*  416 */     if (blockState.hasProperty((Property)HorizontalDirectionalBlock.FACING)) {
/*  417 */       state = (BlockState)blockState.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction);
/*      */     }
/*      */     
/*  420 */     if (blockState.hasProperty((Property)BlockStateProperties.FACING)) {
/*  421 */       state = (BlockState)blockState.setValue((Property)BlockStateProperties.FACING, (Comparable)direction);
/*      */     }
/*      */     
/*  424 */     getLevel().setBlock(absolutePos(blockPos), state, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void assertBlockPresent(Block blockType, int x, int y, int z) {
/*  430 */     assertBlockPresent(blockType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertBlockPresent(Block blockType, BlockPos pos) {
/*  434 */     BlockState state = getBlockState(pos);
/*  435 */     assertBlock(pos, block -> state.is(blockType), block -> Component.translatable("test.error.expected_block", new Object[] { blockType.getName(), block.getName() }));
/*      */   }
/*      */   
/*      */   public void assertBlockNotPresent(Block blockType, int x, int y, int z) {
/*  439 */     assertBlockNotPresent(blockType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertBlockNotPresent(Block blockType, BlockPos pos) {
/*  443 */     assertBlock(pos, block -> !getBlockState(pos).is(pos), block -> Component.translatable("test.error.unexpected_block", new Object[] { blockType.getName() }));
/*      */   }
/*      */   
/*      */   public void assertBlockTag(TagKey<Block> tag, BlockPos pos) {
/*  447 */     assertBlockState(pos, state -> state.is(tag), state -> Component.translatable("test.error.expected_block_tag", new Object[] { Component.translationArg(tag.location()), state.getBlock().getName() }));
/*      */   }
/*      */   
/*      */   public void succeedWhenBlockPresent(Block block, int x, int y, int z) {
/*  451 */     succeedWhenBlockPresent(block, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void succeedWhenBlockPresent(Block block, BlockPos pos) {
/*  455 */     succeedWhen(() -> assertBlockPresent(block, pos));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void assertBlock(BlockPos pos, Predicate<Block> predicate, Function<Block, Component> errorMessage) {
/*  461 */     assertBlockState(pos, blockState -> predicate.test(blockState.getBlock()), state -> (Component)errorMessage.apply(state.getBlock()));
/*      */   }
/*      */   
/*      */   public <T extends Comparable<T>> void assertBlockProperty(BlockPos pos, Property<T> property, T value) {
/*  465 */     BlockState blockState = getBlockState(pos);
/*  466 */     boolean hasProperty = blockState.hasProperty(property);
/*  467 */     if (!hasProperty)
/*  468 */       throw assertionException(pos, "test.error.block_property_missing", new Object[] { property.getName(), value }); 
/*  469 */     if (!blockState.getValue(property).equals(value)) {
/*  470 */       throw assertionException(pos, "test.error.block_property_mismatch", new Object[] { property.getName(), value, blockState.getValue(property) });
/*      */     }
/*      */   }
/*      */   
/*      */   public <T extends Comparable<T>> void assertBlockProperty(BlockPos pos, Property<T> property, Predicate<T> predicate, Component errorMessage) {
/*  475 */     assertBlockState(pos, blockState -> {
/*      */           if (!blockState.hasProperty(property)) {
/*      */             return false;
/*      */           }
/*      */           Comparable comparable = blockState.getValue(property);
/*      */           return predicate.test(comparable);
/*      */         }, state -> errorMessage);
/*      */   }
/*      */   
/*      */   public void assertBlockState(BlockPos pos, BlockState expected) {
/*  485 */     BlockState blockState = getBlockState(pos);
/*  486 */     if (!blockState.equals(expected)) {
/*  487 */       throw assertionException(pos, "test.error.state_not_equal", new Object[] { expected, blockState });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertBlockState(BlockPos pos, Predicate<BlockState> predicate, Function<BlockState, Component> errorMessage) {
/*  492 */     BlockState blockState = getBlockState(pos);
/*  493 */     if (!predicate.test(blockState)) {
/*  494 */       throw assertionException(pos, (Component)errorMessage.apply(blockState));
/*      */     }
/*      */   }
/*      */   
/*      */   public <T extends BlockEntity> void assertBlockEntityData(BlockPos pos, Class<T> type, Predicate<T> predicate, Supplier<Component> errorMessage) {
/*  499 */     T blockEntity = getBlockEntity(pos, type);
/*  500 */     if (!predicate.test(blockEntity)) {
/*  501 */       throw assertionException(pos, (Component)errorMessage.get());
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertRedstoneSignal(BlockPos pos, Direction direction, IntPredicate levelPredicate, Supplier<Component> errorMessage) {
/*  506 */     BlockPos blockPos = absolutePos(pos);
/*  507 */     ServerLevel level = getLevel();
/*  508 */     BlockState blockState = level.getBlockState(blockPos);
/*  509 */     int signal = blockState.getSignal((BlockGetter)level, blockPos, direction);
/*  510 */     if (!levelPredicate.test(signal)) {
/*  511 */       throw assertionException(pos, (Component)errorMessage.get());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType) {
/*  518 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive)) {
/*  519 */       throw assertionException("test.error.expected_entity_in_test", new Object[] { entityType.getDescription() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, int x, int y, int z) {
/*  524 */     assertEntityPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, BlockPos pos) {
/*  528 */     BlockPos absolutePos = absolutePos(pos);
/*  529 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive)) {
/*  530 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, AABB relativeAABB) {
/*  535 */     AABB absoluteAABB = absoluteAABB(relativeAABB);
/*  536 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, absoluteAABB, Entity::isAlive)) {
/*  537 */       throw assertionException(BlockPos.containing(relativeAABB.getCenter()), "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, AABB relativeAABB, Component message) {
/*  542 */     AABB absoluteAABB = absoluteAABB(relativeAABB);
/*  543 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, absoluteAABB, Entity::isAlive)) {
/*  544 */       throw assertionException(BlockPos.containing(relativeAABB.getCenter()), message);
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntitiesPresent(EntityType<?> entityType, int expectedEntities) {
/*  549 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive);
/*  550 */     if (entities.size() != expectedEntities) {
/*  551 */       throw assertionException("test.error.expected_entity_count", new Object[] { expectedEntities, entityType.getDescription(), entities.size() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntitiesPresent(EntityType<?> entityType, BlockPos pos, int numOfExpectedEntities, double distance) {
/*  556 */     BlockPos absolutePos = absolutePos(pos);
/*  557 */     List<? extends Entity> entities = (List)getEntities(entityType, pos, distance);
/*  558 */     if (entities.size() != numOfExpectedEntities) {
/*  559 */       throw assertionException(pos, "test.error.expected_entity_count", new Object[] { numOfExpectedEntities, entityType.getDescription(), entities.size() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityPresent(EntityType<?> entityType, BlockPos pos, double distance) {
/*  564 */     List<? extends Entity> entities = (List)getEntities(entityType, pos, distance);
/*  565 */     if (entities.isEmpty()) {
/*  566 */       BlockPos absolutePos = absolutePos(pos);
/*  567 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(EntityType<T> entityType, BlockPos pos, double distance) {
/*  572 */     BlockPos absolutePos = absolutePos(pos);
/*  573 */     return getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos).inflate(distance), Entity::isAlive);
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(EntityType<T> entityType) {
/*  577 */     return getLevel().getEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive);
/*      */   }
/*      */   
/*      */   public void assertEntityInstancePresent(Entity entity, int x, int y, int z) {
/*  581 */     assertEntityInstancePresent(entity, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertEntityInstancePresent(Entity entity, BlockPos pos) {
/*  585 */     BlockPos absolutePos = absolutePos(pos);
/*  586 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entity.getType(), new AABB(absolutePos), Entity::isAlive);
/*  587 */     entities.stream().filter(it -> (it == entity)).findFirst().orElseThrow(() -> assertionException(pos, "test.error.expected_entity", new Object[] { entity.getType().getDescription() }));
/*      */   }
/*      */   
/*      */   public void assertItemEntityCountIs(Item itemType, BlockPos pos, double distance, int count) {
/*  591 */     BlockPos absolutePos = absolutePos(pos);
/*  592 */     List<ItemEntity> entities = getLevel().getEntities((EntityTypeTest)EntityType.ITEM, new AABB(absolutePos).inflate(distance), Entity::isAlive);
/*      */     
/*  594 */     int num = 0;
/*  595 */     for (ItemEntity entity : entities) {
/*  596 */       ItemStack itemStack = entity.getItem();
/*  597 */       if (itemStack.is(itemType)) {
/*  598 */         num += itemStack.getCount();
/*      */       }
/*      */     } 
/*      */     
/*  602 */     if (num != count) {
/*  603 */       throw assertionException(pos, "test.error.expected_items_count", new Object[] { count, itemType.getName(), num });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertItemEntityPresent(Item itemType, BlockPos pos, double distance) {
/*  608 */     BlockPos absolutePos = absolutePos(pos);
/*  609 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  610 */     if (!getLevel().hasEntities((EntityTypeTest)EntityType.ITEM, new AABB(absolutePos).inflate(distance), isSameItem)) {
/*  611 */       throw assertionException(pos, "test.error.expected_item", new Object[] { itemType.getName() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertItemEntityNotPresent(Item itemType, BlockPos pos, double distance) {
/*  616 */     BlockPos absolutePos = absolutePos(pos);
/*  617 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  618 */     if (getLevel().hasEntities((EntityTypeTest)EntityType.ITEM, new AABB(absolutePos).inflate(distance), isSameItem)) {
/*  619 */       throw assertionException(pos, "test.error.unexpected_item", new Object[] { itemType.getName() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertItemEntityPresent(Item itemType) {
/*  624 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  625 */     if (!getLevel().hasEntities((EntityTypeTest)EntityType.ITEM, getBounds(), isSameItem)) {
/*  626 */       throw assertionException("test.error.expected_item", new Object[] { itemType.getName() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertItemEntityNotPresent(Item itemType) {
/*  631 */     Predicate<ItemEntity> isSameItem = entity -> (entity.isAlive() && entity.getItem().is(itemType));
/*  632 */     if (getLevel().hasEntities((EntityTypeTest)EntityType.ITEM, getBounds(), isSameItem)) {
/*  633 */       throw assertionException("test.error.unexpected_item", new Object[] { itemType.getName() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType) {
/*  638 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entityType, getBounds(), Entity::isAlive);
/*  639 */     if (!entities.isEmpty()) {
/*  640 */       throw assertionException(((Entity)entities.getFirst()).blockPosition(), "test.error.unexpected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType, int x, int y, int z) {
/*  645 */     assertEntityNotPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType, BlockPos pos) {
/*  649 */     BlockPos absolutePos = absolutePos(pos);
/*  650 */     if (getLevel().hasEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive)) {
/*  651 */       throw assertionException(pos, "test.error.unexpected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityNotPresent(EntityType<?> entityType, AABB relativeAABB) {
/*  656 */     AABB absoluteAABB = absoluteAABB(relativeAABB);
/*  657 */     List<? extends Entity> entities = getLevel().getEntities((EntityTypeTest)entityType, absoluteAABB, Entity::isAlive);
/*  658 */     if (!entities.isEmpty()) {
/*  659 */       throw assertionException(((Entity)entities.getFirst()).blockPosition(), "test.error.unexpected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityTouching(EntityType<?> entityType, double x, double y, double z) {
/*  664 */     Vec3 vec = new Vec3(x, y, z);
/*  665 */     Vec3 absoluteVec = absoluteVec(vec);
/*      */     Predicate<? super Entity> predicate = e -> e.getBoundingBox().intersects(absoluteVec, absoluteVec);
/*  667 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, getBounds(), predicate)) {
/*  668 */       throw assertionException("test.error.expected_entity_touching", new Object[] { entityType.getDescription(), absoluteVec.x(), absoluteVec.y(), absoluteVec.z(), x, y, z });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertEntityNotTouching(EntityType<?> entityType, double x, double y, double z) {
/*  673 */     Vec3 vec = new Vec3(x, y, z);
/*  674 */     Vec3 absoluteVec = absoluteVec(vec);
/*      */     Predicate<? super Entity> predicate = e -> !e.getBoundingBox().intersects(absoluteVec, absoluteVec);
/*  676 */     if (!getLevel().hasEntities((EntityTypeTest)entityType, getBounds(), predicate)) {
/*  677 */       throw assertionException("test.error.expected_entity_not_touching", new Object[] { entityType.getDescription(), absoluteVec.x(), absoluteVec.y(), absoluteVec.z(), x, y, z });
/*      */     }
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityData(BlockPos pos, EntityType<E> entityType, Predicate<E> test) {
/*  682 */     BlockPos absolutePos = absolutePos(pos);
/*  683 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive);
/*  684 */     if (entities.isEmpty()) {
/*  685 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */     
/*  688 */     for (Entity entity : entities) {
/*  689 */       if (!test.test((E)entity)) {
/*  690 */         throw assertionException(entity.blockPosition(), "test.error.expected_entity_data_predicate", new Object[] { entity.getName() });
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityData(BlockPos pos, EntityType<E> entityType, Function<? super E, T> dataAccessor, T data) {
/*  696 */     assertEntityData(new AABB(pos), entityType, dataAccessor, data);
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityData(AABB box, EntityType<E> entityType, Function<? super E, T> dataAccessor, T data) {
/*  700 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, absoluteAABB(box), Entity::isAlive);
/*  701 */     if (entities.isEmpty()) {
/*  702 */       throw assertionException(BlockPos.containing(box.getBottomCenter()), "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */     
/*  705 */     for (Entity entity : entities) {
/*  706 */       T actual = dataAccessor.apply((E)entity);
/*      */       
/*  708 */       if (!Objects.equals(actual, data)) {
/*  709 */         throw assertionException(BlockPos.containing(box.getBottomCenter()), "test.error.expected_entity_data", new Object[] { data, actual });
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public <E extends LivingEntity> void assertEntityIsHolding(BlockPos pos, EntityType<E> entityType, Item item) {
/*  715 */     BlockPos absolutePos = absolutePos(pos);
/*      */     
/*  717 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos), Entity::isAlive);
/*  718 */     if (entities.isEmpty()) {
/*  719 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */     
/*  722 */     for (LivingEntity livingEntity : entities) {
/*  723 */       if (livingEntity.isHolding(item)) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  728 */     throw assertionException(pos, "test.error.expected_entity_holding", new Object[] { item.getName() });
/*      */   }
/*      */   
/*      */   public <E extends Entity & InventoryCarrier> void assertEntityInventoryContains(BlockPos pos, EntityType<E> entityType, Item item) {
/*  732 */     BlockPos absolutePos = absolutePos(pos);
/*      */     
/*  734 */     List<E> entities = getLevel().getEntities((EntityTypeTest)entityType, new AABB(absolutePos), rec$ -> ((Entity)rec$).isAlive());
/*  735 */     if (entities.isEmpty()) {
/*  736 */       throw assertionException(pos, "test.error.expected_entity", new Object[] { entityType.getDescription() });
/*      */     }
/*      */     
/*  739 */     for (Entity entity : entities) {
/*  740 */       if (((InventoryCarrier)entity).getInventory().hasAnyMatching(itemStack -> itemStack.is(item))) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  745 */     throw assertionException(pos, "test.error.expected_entity_having", new Object[] { item.getName() });
/*      */   }
/*      */   
/*      */   public void assertContainerEmpty(BlockPos pos) {
/*  749 */     BaseContainerBlockEntity container = getBlockEntity(pos, BaseContainerBlockEntity.class);
/*  750 */     if (!container.isEmpty()) {
/*  751 */       throw assertionException(pos, "test.error.expected_empty_container", new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertContainerContainsSingle(BlockPos pos, Item item) {
/*  756 */     BaseContainerBlockEntity container = getBlockEntity(pos, BaseContainerBlockEntity.class);
/*  757 */     if (container.countItem(item) != 1) {
/*  758 */       throw assertionException(pos, "test.error.expected_container_contents_single", new Object[] { item.getName() });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertContainerContains(BlockPos pos, Item item) {
/*  763 */     BaseContainerBlockEntity container = getBlockEntity(pos, BaseContainerBlockEntity.class);
/*  764 */     if (container.countItem(item) == 0) {
/*  765 */       throw assertionException(pos, "test.error.expected_container_contents", new Object[] { item.getName() });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void assertSameBlockStates(BoundingBox sourceBoundingBox, BlockPos targetBoundingBoxCorner) {
/*  773 */     BlockPos.betweenClosedStream(sourceBoundingBox)
/*  774 */       .forEach(sourcePos -> {
/*      */           BlockPos targetPos = targetBoundingBoxCorner.offset(targetBoundingBoxCorner.getX() - targetBoundingBoxCorner.minX(), targetBoundingBoxCorner.getY() - targetBoundingBoxCorner.minY(), targetBoundingBoxCorner.getZ() - targetBoundingBoxCorner.minZ());
/*      */           assertSameBlockState(targetBoundingBoxCorner, targetPos);
/*      */         });
/*      */   }
/*      */   
/*      */   public void assertSameBlockState(BlockPos sourcePos, BlockPos targetPos) {
/*  781 */     BlockState sourceState = getBlockState(sourcePos);
/*  782 */     BlockState targetState = getBlockState(targetPos);
/*  783 */     if (sourceState != targetState) {
/*  784 */       throw assertionException(sourcePos, "test.error.state_not_equal", new Object[] { targetState, sourceState });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertAtTickTimeContainerContains(long time, BlockPos pos, Item item) {
/*  789 */     runAtTickTime(time, () -> assertContainerContainsSingle(pos, item));
/*      */   }
/*      */   
/*      */   public void assertAtTickTimeContainerEmpty(long time, BlockPos pos) {
/*  793 */     runAtTickTime(time, () -> assertContainerEmpty(pos));
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void succeedWhenEntityData(BlockPos pos, EntityType<E> entityType, Function<E, T> dataAccessor, T data) {
/*  797 */     succeedWhen(() -> assertEntityData(pos, entityType, dataAccessor, data));
/*      */   }
/*      */   
/*      */   public <E extends Entity> void assertEntityProperty(E entity, Predicate<E> test, Component description) {
/*  801 */     if (!test.test(entity)) {
/*  802 */       throw assertionException(entity.blockPosition(), "test.error.entity_property", new Object[] { entity.getName(), description });
/*      */     }
/*      */   }
/*      */   
/*      */   public <E extends Entity, T> void assertEntityProperty(E entity, Function<E, T> test, T expected, Component description) {
/*  807 */     T actual = test.apply(entity);
/*  808 */     if (!actual.equals(expected)) {
/*  809 */       throw assertionException(entity.blockPosition(), "test.error.entity_property_details", new Object[] { entity.getName(), description, actual, expected });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertLivingEntityHasMobEffect(LivingEntity entity, Holder<MobEffect> mobEffect, int amplifier) {
/*  814 */     MobEffectInstance mobEffectInstance = entity.getEffect(mobEffect);
/*  815 */     if (mobEffectInstance == null || mobEffectInstance.getAmplifier() != amplifier) {
/*  816 */       throw assertionException("test.error.expected_entity_effect", new Object[] { entity.getName(), PotionContents.getPotionDescription(mobEffect, amplifier) });
/*      */     }
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityPresent(EntityType<?> entityType, int x, int y, int z) {
/*  821 */     succeedWhenEntityPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityPresent(EntityType<?> entityType, BlockPos pos) {
/*  825 */     succeedWhen(() -> assertEntityPresent(entityType, pos));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void succeedWhenEntityNotPresent(EntityType<?> entityType, int x, int y, int z) {
/*  831 */     succeedWhenEntityNotPresent(entityType, new BlockPos(x, y, z));
/*      */   }
/*      */   
/*      */   public void succeedWhenEntityNotPresent(EntityType<?> entityType, BlockPos pos) {
/*  835 */     succeedWhen(() -> assertEntityNotPresent(entityType, pos));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void succeed() {
/*  843 */     this.testInfo.succeed();
/*      */   }
/*      */   
/*      */   private void ensureSingleFinalCheck() {
/*  847 */     if (this.finalCheckAdded) {
/*  848 */       throw new IllegalStateException("This test already has final clause");
/*      */     }
/*  850 */     this.finalCheckAdded = true;
/*      */   }
/*      */   
/*      */   public void succeedIf(Runnable asserter) {
/*  854 */     ensureSingleFinalCheck();
/*  855 */     this.testInfo.createSequence()
/*  856 */       .thenWaitUntil(0L, asserter)
/*  857 */       .thenSucceed();
/*      */   }
/*      */   
/*      */   public void succeedWhen(Runnable asserter) {
/*  861 */     ensureSingleFinalCheck();
/*  862 */     this.testInfo.createSequence()
/*  863 */       .thenWaitUntil(asserter)
/*  864 */       .thenSucceed();
/*      */   }
/*      */   
/*      */   public void succeedOnTickWhen(int tick, Runnable asserter) {
/*  868 */     ensureSingleFinalCheck();
/*  869 */     this.testInfo.createSequence()
/*  870 */       .thenWaitUntil(tick, asserter)
/*  871 */       .thenSucceed();
/*      */   }
/*      */   
/*      */   public void runAtTickTime(long time, Runnable asserter) {
/*  875 */     this.testInfo.setRunAtTickTime(time, asserter);
/*      */   }
/*      */   
/*      */   public void runAfterDelay(long ticksToDelay, Runnable whatToRun) {
/*  879 */     runAtTickTime(this.testInfo.getTick() + ticksToDelay, whatToRun);
/*      */   }
/*      */   
/*      */   public void randomTick(BlockPos pos) {
/*  883 */     BlockPos absolutePos = absolutePos(pos);
/*  884 */     ServerLevel level = getLevel();
/*  885 */     level.getBlockState(absolutePos).randomTick(level, absolutePos, level.random);
/*      */   }
/*      */   
/*      */   public void tickBlock(BlockPos pos) {
/*  889 */     BlockPos absolutePos = absolutePos(pos);
/*  890 */     ServerLevel level = getLevel();
/*  891 */     level.getBlockState(absolutePos).tick(level, absolutePos, level.random);
/*      */   }
/*      */   
/*      */   public void tickPrecipitation(BlockPos pos) {
/*  895 */     BlockPos absolutePos = absolutePos(pos);
/*  896 */     ServerLevel level = getLevel();
/*  897 */     level.tickPrecipitation(absolutePos);
/*      */   }
/*      */   
/*      */   public void tickPrecipitation() {
/*  901 */     AABB aabb = getRelativeBounds();
/*  902 */     int maxX = (int)Math.floor(aabb.maxX);
/*  903 */     int maxZ = (int)Math.floor(aabb.maxZ);
/*  904 */     int maxY = (int)Math.floor(aabb.maxY);
/*  905 */     for (int x = (int)Math.floor(aabb.minX); x < maxX; x++) {
/*  906 */       for (int z = (int)Math.floor(aabb.minZ); z < maxZ; z++) {
/*  907 */         tickPrecipitation(new BlockPos(x, maxY, z));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getHeight(Heightmap.Types heightmap, int x, int z) {
/*  913 */     BlockPos absolutePos = absolutePos(new BlockPos(x, 0, z));
/*  914 */     return relativePos(getLevel().getHeightmapPos(heightmap, absolutePos)).getY();
/*      */   }
/*      */   
/*      */   public void fail(Component message, BlockPos pos) {
/*  918 */     throw assertionException(pos, message);
/*      */   }
/*      */   
/*      */   public void fail(Component message, Entity entity) {
/*  922 */     throw assertionException(entity.blockPosition(), message);
/*      */   }
/*      */   
/*      */   public void fail(Component message) {
/*  926 */     throw assertionException(message);
/*      */   }
/*      */   
/*      */   public void fail(String message) {
/*  930 */     throw assertionException(Component.literal(message));
/*      */   }
/*      */   
/*      */   public void failIf(Runnable asserter) {
/*  934 */     this.testInfo.createSequence()
/*  935 */       .thenWaitUntil(asserter)
/*  936 */       .thenFail(() -> assertionException("test.error.fail", new Object[0]));
/*      */   }
/*      */   
/*      */   public void failIfEver(Runnable asserter) {
/*  940 */     LongStream.range(this.testInfo.getTick(), this.testInfo.getTimeoutTicks())
/*  941 */       .forEach(i -> {
/*      */           Objects.requireNonNull(asserter);
/*      */           this.testInfo.setRunAtTickTime(asserter, asserter::run);
/*      */         }); } public GameTestSequence startSequence() {
/*  945 */     return this.testInfo.createSequence();
/*      */   }
/*      */   
/*      */   public BlockPos absolutePos(BlockPos relativePos) {
/*  949 */     BlockPos testPos = this.testInfo.getTestOrigin();
/*  950 */     BlockPos absolutePosBeforeTranform = testPos.offset((Vec3i)relativePos);
/*  951 */     return StructureTemplate.transform(absolutePosBeforeTranform, Mirror.NONE, this.testInfo.getRotation(), testPos);
/*      */   }
/*      */   
/*      */   public BlockPos relativePos(BlockPos absolutePos) {
/*  955 */     BlockPos testPos = this.testInfo.getTestOrigin();
/*  956 */     Rotation inverseRotation = this.testInfo.getRotation().getRotated(Rotation.CLOCKWISE_180);
/*  957 */     BlockPos absolutePosBeforeTransform = StructureTemplate.transform(absolutePos, Mirror.NONE, inverseRotation, testPos);
/*  958 */     return absolutePosBeforeTransform.subtract((Vec3i)testPos);
/*      */   }
/*      */   
/*      */   public AABB absoluteAABB(AABB relativeAABB) {
/*  962 */     Vec3 min = absoluteVec(relativeAABB.getMinPosition());
/*  963 */     Vec3 max = absoluteVec(relativeAABB.getMaxPosition());
/*  964 */     return new AABB(min, max);
/*      */   }
/*      */   
/*      */   public AABB relativeAABB(AABB absoluteAABB) {
/*  968 */     Vec3 min = relativeVec(absoluteAABB.getMinPosition());
/*  969 */     Vec3 max = relativeVec(absoluteAABB.getMaxPosition());
/*  970 */     return new AABB(min, max);
/*      */   }
/*      */   
/*      */   public Vec3 absoluteVec(Vec3 relativeVec) {
/*  974 */     Vec3 testPosVec = Vec3.atLowerCornerOf((Vec3i)this.testInfo.getTestOrigin());
/*  975 */     return StructureTemplate.transform(testPosVec.add(relativeVec), Mirror.NONE, this.testInfo.getRotation(), this.testInfo.getTestOrigin());
/*      */   }
/*      */   
/*      */   public Vec3 relativeVec(Vec3 absoluteVec) {
/*  979 */     Vec3 testPosVec = Vec3.atLowerCornerOf((Vec3i)this.testInfo.getTestOrigin());
/*  980 */     return StructureTemplate.transform(absoluteVec.subtract(testPosVec), Mirror.NONE, this.testInfo.getRotation(), this.testInfo.getTestOrigin());
/*      */   }
/*      */   
/*      */   public Rotation getTestRotation() {
/*  984 */     return this.testInfo.getRotation();
/*      */   }
/*      */   
/*      */   public Direction getTestDirection() {
/*  988 */     return this.testInfo.getRotation().rotate(Direction.SOUTH);
/*      */   }
/*      */   
/*      */   public Direction getAbsoluteDirection(Direction direction) {
/*  992 */     return getTestRotation().rotate(direction);
/*      */   }
/*      */   
/*      */   public void assertTrue(boolean condition, Component errorMessage) {
/*  996 */     if (!condition) {
/*  997 */       throw assertionException(errorMessage);
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertTrue(boolean condition, String errorMessage) {
/* 1002 */     assertTrue(condition, (Component)Component.literal(errorMessage));
/*      */   }
/*      */   
/*      */   public <N> void assertValueEqual(N value, N expected, String valueName) {
/* 1006 */     assertValueEqual(value, expected, (Component)Component.literal(valueName));
/*      */   }
/*      */   
/*      */   public <N> void assertValueEqual(N value, N expected, Component valueName) {
/* 1010 */     if (!value.equals(expected)) {
/* 1011 */       throw assertionException("test.error.value_not_equal", new Object[] { valueName, value, expected });
/*      */     }
/*      */   }
/*      */   
/*      */   public void assertFalse(boolean condition, Component errorMessage) {
/* 1016 */     assertTrue(!condition, errorMessage);
/*      */   }
/*      */   
/*      */   public void assertFalse(boolean condition, String errorMessage) {
/* 1020 */     assertFalse(condition, (Component)Component.literal(errorMessage));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTick() {
/* 1027 */     return this.testInfo.getTick();
/*      */   }
/*      */   
/*      */   public AABB getBounds() {
/* 1031 */     return this.testInfo.getStructureBounds();
/*      */   }
/*      */   
/*      */   public AABB getRelativeBounds() {
/* 1035 */     AABB absolute = this.testInfo.getStructureBounds();
/* 1036 */     Rotation rotation = this.testInfo.getRotation();
/* 1037 */     switch (rotation) {
/*      */       case COUNTERCLOCKWISE_90:
/*      */       case CLOCKWISE_90:
/* 1040 */         return new AABB(0.0D, 0.0D, 0.0D, absolute.getZsize(), absolute.getYsize(), absolute.getXsize());
/*      */     } 
/* 1042 */     return new AABB(0.0D, 0.0D, 0.0D, absolute.getXsize(), absolute.getYsize(), absolute.getZsize());
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEveryBlockInStructure(Consumer<BlockPos> forBlock) {
/* 1047 */     AABB aabb = getRelativeBounds().contract(1.0D, 1.0D, 1.0D);
/* 1048 */     BlockPos.MutableBlockPos.betweenClosedStream(aabb).forEach(forBlock);
/*      */   }
/*      */   
/*      */   public void onEachTick(Runnable action) {
/* 1052 */     LongStream.range(this.testInfo.getTick(), this.testInfo.getTimeoutTicks()).forEach(i -> {
/*      */           Objects.requireNonNull(action);
/*      */           this.testInfo.setRunAtTickTime(action, action::run);
/*      */         });
/*      */   }
/*      */   public void placeAt(Player player, ItemStack blockStack, BlockPos pos, Direction face) {
/* 1058 */     BlockPos absolute = absolutePos(pos.relative(face));
/* 1059 */     BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf((Vec3i)absolute), face, absolute, false);
/* 1060 */     UseOnContext context = new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult);
/* 1061 */     blockStack.useOn(context);
/*      */   }
/*      */   
/*      */   public void setBiome(ResourceKey<Biome> biome) {
/* 1065 */     AABB bounds = getBounds();
/* 1066 */     BlockPos low = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
/* 1067 */     BlockPos high = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
/* 1068 */     Either<Integer, CommandSyntaxException> result = FillBiomeCommand.fill(getLevel(), low, high, (Holder)getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(biome));
/* 1069 */     if (result.right().isPresent())
/* 1070 */       throw assertionException("test.error.set_biome", new Object[0]); 
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */