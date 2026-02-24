/*      */ package net.minecraft.client.multiplayer;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.Camera;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.color.block.BlockTintCache;
/*      */ import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
/*      */ import net.minecraft.client.particle.FireworkParticles;
/*      */ import net.minecraft.client.particle.Particle;
/*      */ import net.minecraft.client.particle.TerrainParticle;
/*      */ import net.minecraft.client.player.AbstractClientPlayer;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.renderer.BiomeColors;
/*      */ import net.minecraft.client.renderer.EndFlashState;
/*      */ import net.minecraft.client.renderer.LevelEventHandler;
/*      */ import net.minecraft.client.renderer.LevelRenderer;
/*      */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
/*      */ import net.minecraft.client.resources.sounds.DirectionalSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Cursor3D;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ParticleStatus;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.Zone;
/*      */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*      */ import net.minecraft.util.random.WeightedList;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.AmbientParticle;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.component.FireworkExplosion;
/*      */ import net.minecraft.world.item.crafting.RecipeAccess;
/*      */ import net.minecraft.world.level.BlockAndTintGetter;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ColorResolver;
/*      */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.Biomes;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.ChunkSource;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.entity.EntityTickList;
/*      */ import net.minecraft.world.level.entity.LevelCallback;
/*      */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*      */ import net.minecraft.world.level.entity.TransientEntitySectionManager;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.WritableLevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.ticks.BlackholeTickAccess;
/*      */ import net.minecraft.world.ticks.LevelTickAccess;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ClientLevel extends Level implements CacheSlot.Cleaner<ClientLevel> {
/*  121 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  123 */   public static final Component DEFAULT_QUIT_MESSAGE = (Component)Component.translatable("multiplayer.status.quitting");
/*      */   
/*      */   private static final double FLUID_PARTICLE_SPAWN_OFFSET = 0.05D;
/*      */   private static final int NORMAL_LIGHT_UPDATES_PER_FRAME = 10;
/*      */   private static final int LIGHT_UPDATE_QUEUE_SIZE_THRESHOLD = 1000;
/*  128 */   private final EntityTickList tickingEntities = new EntityTickList();
/*  129 */   private final TransientEntitySectionManager<Entity> entityStorage = new TransientEntitySectionManager(Entity.class, new EntityCallbacks());
/*      */   
/*      */   private final ClientPacketListener connection;
/*      */   
/*      */   private final LevelRenderer levelRenderer;
/*      */   private final LevelEventHandler levelEventHandler;
/*      */   private final ClientLevelData clientLevelData;
/*      */   private final TickRateManager tickRateManager;
/*      */   private final EndFlashState endFlashState;
/*  138 */   private final Minecraft minecraft = Minecraft.getInstance();
/*  139 */   private final List<AbstractClientPlayer> players = Lists.newArrayList();
/*  140 */   private final List<EnderDragonPart> dragonParts = Lists.newArrayList();
/*  141 */   private final Map<MapId, MapItemSavedData> mapData = Maps.newHashMap();
/*      */   
/*      */   private int skyFlashTime;
/*      */   
/*      */   private final Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;
/*      */   
/*      */   private final ClientChunkCache chunkSource;
/*      */   
/*      */   private final Deque<Runnable> lightUpdateQueue;
/*      */   
/*      */   private int serverSimulationDistance;
/*      */   
/*      */   private final BlockStatePredictionHandler blockStatePredictionHandler;
/*      */   private final Set<BlockEntity> globallyRenderedBlockEntities;
/*      */   private final ClientExplosionTracker explosionTracker;
/*      */   private final WorldBorder worldBorder;
/*      */   private final EnvironmentAttributeSystem environmentAttributes;
/*      */   private final int seaLevel;
/*      */   private boolean tickDayTime;
/*      */   
/*      */   public void handleBlockChangedAck(int sequence) {
/*  162 */     if (SharedConstants.DEBUG_BLOCK_BREAK) {
/*  163 */       LOGGER.debug("ACK {}", sequence);
/*      */     }
/*      */     
/*  166 */     this.blockStatePredictionHandler.endPredictionsUpTo(sequence, this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onBlockEntityAdded(BlockEntity blockEntity) {
/*  171 */     BlockEntityRenderer<BlockEntity, ?> renderer = this.minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
/*  172 */     if (renderer != null && renderer.shouldRenderOffScreen()) {
/*  173 */       this.globallyRenderedBlockEntities.add(blockEntity);
/*      */     }
/*      */   }
/*      */   
/*      */   public Set<BlockEntity> getGloballyRenderedBlockEntities() {
/*  178 */     return this.globallyRenderedBlockEntities;
/*      */   }
/*      */   
/*      */   public void setServerVerifiedBlockState(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlag) {
/*  182 */     if (!this.blockStatePredictionHandler.updateKnownServerState(pos, blockState)) {
/*  183 */       super.setBlock(pos, blockState, updateFlag, 512);
/*      */     }
/*      */   }
/*      */   
/*      */   public void syncBlockState(BlockPos pos, BlockState state, Vec3 playerPos) {
/*  188 */     BlockState oldState = getBlockState(pos);
/*  189 */     if (oldState != state) {
/*  190 */       setBlock(pos, state, 19);
/*      */ 
/*      */       
/*  193 */       LocalPlayer localPlayer = this.minecraft.player;
/*  194 */       if (this == localPlayer.level() && localPlayer.isColliding(pos, state)) {
/*  195 */         localPlayer.absSnapTo(playerPos.x, playerPos.y, playerPos.z);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   BlockStatePredictionHandler getBlockStatePredictionHandler() {
/*  201 */     return this.blockStatePredictionHandler;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
/*  206 */     if (this.blockStatePredictionHandler.isPredicting()) {
/*  207 */       BlockState oldState = getBlockState(pos);
/*  208 */       boolean success = super.setBlock(pos, blockState, updateFlags, updateLimit);
/*  209 */       if (success) {
/*  210 */         this.blockStatePredictionHandler.retainKnownServerState(pos, oldState, this.minecraft.player);
/*      */       }
/*  212 */       return success;
/*      */     } 
/*  214 */     return super.setBlock(pos, blockState, updateFlags, updateLimit);
/*      */   }
/*      */   
/*  217 */   private static final Set<Item> MARKER_PARTICLE_ITEMS = Set.of(Items.BARRIER, Items.LIGHT);
/*      */   
/*      */   public ClientLevel(ClientPacketListener connection, ClientLevelData levelData, ResourceKey<Level> dimension, Holder<DimensionType> dimensionType, int serverChunkRadius, int serverSimulationDistance, LevelRenderer levelRenderer, boolean isDebug, long biomeZoomSeed, int seaLevel) {
/*  220 */     super(levelData, dimension, (RegistryAccess)connection.registryAccess(), dimensionType, true, isDebug, biomeZoomSeed, 1000000); this.tintCaches = (Object2ObjectArrayMap<ColorResolver, BlockTintCache>)Util.make(new Object2ObjectArrayMap(3), cache -> { cache.put(BiomeColors.GRASS_COLOR_RESOLVER, new BlockTintCache(())); cache.put(BiomeColors.FOLIAGE_COLOR_RESOLVER, new BlockTintCache(())); cache.put(BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER, new BlockTintCache(())); cache.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache(()));
/*  221 */         }); this.lightUpdateQueue = com.google.common.collect.Queues.newArrayDeque(); this.blockStatePredictionHandler = new BlockStatePredictionHandler(); this.globallyRenderedBlockEntities = (Set<BlockEntity>)new ReferenceOpenHashSet(); this.explosionTracker = new ClientExplosionTracker(); this.worldBorder = new WorldBorder(); this.connection = connection;
/*  222 */     this.chunkSource = new ClientChunkCache(this, serverChunkRadius);
/*  223 */     this.tickRateManager = new TickRateManager();
/*  224 */     this.clientLevelData = levelData;
/*  225 */     this.levelRenderer = levelRenderer;
/*  226 */     this.seaLevel = seaLevel;
/*  227 */     this.levelEventHandler = new LevelEventHandler(this.minecraft, this);
/*  228 */     this.endFlashState = ((DimensionType)dimensionType.value()).hasEndFlashes() ? new EndFlashState() : null;
/*  229 */     setRespawnData(LevelData.RespawnData.of(dimension, new BlockPos(8, 64, 8), 0.0F, 0.0F));
/*      */     
/*  231 */     this.serverSimulationDistance = serverSimulationDistance;
/*      */     
/*  233 */     this.environmentAttributes = addEnvironmentAttributeLayers(EnvironmentAttributeSystem.builder()).build();
/*      */     
/*  235 */     updateSkyBrightness();
/*  236 */     if (canHaveWeather()) {
/*  237 */       prepareWeather();
/*      */     }
/*      */   }
/*      */   
/*      */   private EnvironmentAttributeSystem.Builder addEnvironmentAttributeLayers(EnvironmentAttributeSystem.Builder environmentAttributes) {
/*  242 */     environmentAttributes.addDefaultLayers(this);
/*      */     
/*  244 */     int flashColor = ARGB.color(204, 204, 255);
/*  245 */     environmentAttributes.addTimeBasedLayer(EnvironmentAttributes.SKY_COLOR, (skyColor, cacheTickId) -> (getSkyFlashTime() > 0) ? ARGB.srgbLerp(0.22F, flashColor, flashColor) : flashColor);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  251 */     environmentAttributes.addTimeBasedLayer(EnvironmentAttributes.SKY_LIGHT_FACTOR, (skyFactor, cacheTickId) -> (getSkyFlashTime() > 0) ? 1.0F : skyFactor);
/*      */ 
/*      */ 
/*      */     
/*  255 */     return environmentAttributes;
/*      */   }
/*      */   
/*      */   public void queueLightUpdate(Runnable update) {
/*  259 */     this.lightUpdateQueue.add(update);
/*      */   }
/*      */   
/*      */   public void pollLightUpdates() {
/*  263 */     int size = this.lightUpdateQueue.size();
/*  264 */     int lightUpdatesPerFrame = (size < 1000) ? Math.max(10, size / 10) : size;
/*  265 */     for (int i = 0; i < lightUpdatesPerFrame; ) {
/*  266 */       Runnable update = this.lightUpdateQueue.poll();
/*  267 */       if (update != null) {
/*  268 */         update.run();
/*      */         i++;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public EndFlashState endFlashState() {
/*  276 */     return this.endFlashState;
/*      */   }
/*      */   
/*      */   public void tick(BooleanSupplier haveTime) {
/*  280 */     updateSkyBrightness();
/*  281 */     if (tickRateManager().runsNormally()) {
/*  282 */       getWorldBorder().tick();
/*  283 */       tickTime();
/*      */     } 
/*      */     
/*  286 */     if (this.skyFlashTime > 0) {
/*  287 */       setSkyFlashTime(this.skyFlashTime - 1);
/*      */     }
/*      */     
/*  290 */     if (this.endFlashState != null) {
/*  291 */       this.endFlashState.tick(getGameTime());
/*  292 */       if (this.endFlashState.flashStartedThisTick() && !(this.minecraft.screen instanceof net.minecraft.client.gui.screens.WinScreen)) {
/*  293 */         this.minecraft.getSoundManager().playDelayed((SoundInstance)new DirectionalSoundInstance(SoundEvents.WEATHER_END_FLASH, SoundSource.WEATHER, this.random, this.minecraft.gameRenderer.getMainCamera(), this.endFlashState.getXAngle(), this.endFlashState.getYAngle()), 30);
/*      */       }
/*      */     } 
/*      */     
/*  297 */     this.explosionTracker.tick(this);
/*      */     
/*  299 */     Zone ignored = Profiler.get().zone("blocks"); 
/*  300 */     try { this.chunkSource.tick(haveTime, true);
/*  301 */       if (ignored != null) ignored.close();  } catch (Throwable throwable) { if (ignored != null)
/*      */         try { ignored.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*  303 */      JvmProfiler.INSTANCE.onClientTick(this.minecraft.getFps());
/*  304 */     environmentAttributes().invalidateTickCache();
/*      */   }
/*      */   
/*      */   private void tickTime() {
/*  308 */     this.clientLevelData.setGameTime(this.clientLevelData.getGameTime() + 1L);
/*  309 */     if (this.tickDayTime) {
/*  310 */       this.clientLevelData.setDayTime(this.clientLevelData.getDayTime() + 1L);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setTimeFromServer(long gameTime, long dayTime, boolean tickDayTime) {
/*  315 */     this.clientLevelData.setGameTime(gameTime);
/*  316 */     this.clientLevelData.setDayTime(dayTime);
/*  317 */     this.tickDayTime = tickDayTime;
/*      */   }
/*      */   
/*      */   public Iterable<Entity> entitiesForRendering() {
/*  321 */     return getEntities().getAll();
/*      */   }
/*      */   
/*      */   public void tickEntities() {
/*  325 */     this.tickingEntities.forEach(entity -> {
/*      */           if (entity.isRemoved() || entity.isPassenger() || this.tickRateManager.isEntityFrozen(entity)) {
/*      */             return;
/*      */           }
/*      */           guardEntityTick(this::tickNonPassenger, entity);
/*      */         });
/*      */   }
/*      */   
/*      */   public boolean isTickingEntity(Entity entity) {
/*  334 */     return this.tickingEntities.contains(entity);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldTickDeath(Entity entity) {
/*  339 */     return (entity.chunkPosition().getChessboardDistance(this.minecraft.player.chunkPosition()) <= this.serverSimulationDistance);
/*      */   }
/*      */   
/*      */   public void tickNonPassenger(Entity entity) {
/*  343 */     entity.setOldPosAndRot();
/*      */     
/*  345 */     entity.tickCount++;
/*  346 */     Profiler.get().push(() -> BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
/*  347 */     entity.tick();
/*  348 */     Profiler.get().pop();
/*      */     
/*  350 */     for (Entity passenger : (Iterable<Entity>)entity.getPassengers()) {
/*  351 */       tickPassenger(entity, passenger);
/*      */     }
/*      */   }
/*      */   
/*      */   private void tickPassenger(Entity vehicle, Entity entity) {
/*  356 */     if (entity.isRemoved() || entity.getVehicle() != vehicle) {
/*  357 */       entity.stopRiding();
/*      */       
/*      */       return;
/*      */     } 
/*  361 */     if (!(entity instanceof net.minecraft.world.entity.player.Player) && !this.tickingEntities.contains(entity)) {
/*      */       return;
/*      */     }
/*      */     
/*  365 */     entity.setOldPosAndRot();
/*      */     
/*  367 */     entity.tickCount++;
/*  368 */     entity.rideTick();
/*      */     
/*  370 */     for (Entity passenger : (Iterable<Entity>)entity.getPassengers()) {
/*  371 */       tickPassenger(entity, passenger);
/*      */     }
/*      */   }
/*      */   
/*      */   public void unload(LevelChunk levelChunk) {
/*  376 */     levelChunk.clearAllBlockEntities();
/*  377 */     this.chunkSource.getLightEngine().setLightEnabled(levelChunk.getPos(), false);
/*  378 */     this.entityStorage.stopTicking(levelChunk.getPos());
/*      */   }
/*      */   
/*      */   public void onChunkLoaded(ChunkPos pos) {
/*  382 */     this.tintCaches.forEach((resolver, cache) -> cache.invalidateForChunk(pos.x, pos.z));
/*  383 */     this.entityStorage.startTicking(pos);
/*      */   }
/*      */   
/*      */   public void onSectionBecomingNonEmpty(long sectionNode) {
/*  387 */     this.levelRenderer.onSectionBecomingNonEmpty(sectionNode);
/*      */   }
/*      */   
/*      */   public void clearTintCaches() {
/*  391 */     this.tintCaches.forEach((resolver, cache) -> cache.invalidateAll());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasChunk(int chunkX, int chunkZ) {
/*  396 */     return true;
/*      */   }
/*      */   
/*      */   public int getEntityCount() {
/*  400 */     return this.entityStorage.count();
/*      */   }
/*      */   
/*      */   public void addEntity(Entity entity) {
/*  404 */     removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
/*  405 */     this.entityStorage.addEntity((net.minecraft.world.level.entity.EntityAccess)entity);
/*      */   }
/*      */   
/*      */   public void removeEntity(int id, Entity.RemovalReason reason) {
/*  409 */     Entity entity = (Entity)getEntities().get(id);
/*  410 */     if (entity != null) {
/*  411 */       entity.setRemoved(reason);
/*  412 */       entity.onClientRemoval();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Entity> getPushableEntities(Entity pusher, AABB boundingBox) {
/*  419 */     LocalPlayer player = this.minecraft.player;
/*  420 */     if (player != null && player != pusher && 
/*      */       
/*  422 */       player.getBoundingBox().intersects(boundingBox) && 
/*  423 */       EntitySelector.pushableBy(pusher).test(player))
/*      */     {
/*  425 */       return (List)List.of(player);
/*      */     }
/*  427 */     return List.of();
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getEntity(int id) {
/*  432 */     return (Entity)getEntities().get(id);
/*      */   }
/*      */   
/*      */   public void disconnect(Component message) {
/*  436 */     this.connection.getConnection().disconnect(message);
/*      */   }
/*      */   
/*      */   public void animateTick(int xt, int yt, int zt) {
/*  440 */     int r = 32;
/*  441 */     RandomSource animateRandom = RandomSource.create();
/*      */     
/*  443 */     Block markerParticleTarget = getMarkerParticleTarget();
/*  444 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*  445 */     for (int i = 0; i < 667; i++) {
/*  446 */       doAnimateTick(xt, yt, zt, 16, animateRandom, markerParticleTarget, pos);
/*  447 */       doAnimateTick(xt, yt, zt, 32, animateRandom, markerParticleTarget, pos);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Block getMarkerParticleTarget() {
/*  452 */     if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
/*  453 */       ItemStack carriedItemStack = this.minecraft.player.getMainHandItem();
/*  454 */       Item carriedItem = carriedItemStack.getItem();
/*  455 */       if (MARKER_PARTICLE_ITEMS.contains(carriedItem) && carriedItem instanceof BlockItem) { BlockItem blockItem = (BlockItem)carriedItem;
/*  456 */         return blockItem.getBlock(); }
/*      */     
/*      */     } 
/*      */     
/*  460 */     return null;
/*      */   }
/*      */   
/*      */   public void doAnimateTick(int xt, int yt, int zt, int r, RandomSource animateRandom, Block markerParticleTarget, BlockPos.MutableBlockPos pos) {
/*  464 */     int x = xt + this.random.nextInt(r) - this.random.nextInt(r);
/*  465 */     int y = yt + this.random.nextInt(r) - this.random.nextInt(r);
/*  466 */     int z = zt + this.random.nextInt(r) - this.random.nextInt(r);
/*      */     
/*  468 */     pos.set(x, y, z);
/*  469 */     BlockState state = getBlockState((BlockPos)pos);
/*  470 */     state.getBlock().animateTick(state, this, (BlockPos)pos, animateRandom);
/*      */     
/*  472 */     FluidState fluidState = getFluidState((BlockPos)pos);
/*      */     
/*  474 */     if (!fluidState.isEmpty()) {
/*  475 */       fluidState.animateTick(this, (BlockPos)pos, animateRandom);
/*      */       
/*  477 */       ParticleOptions dripParticle = fluidState.getDripParticle();
/*  478 */       if (dripParticle != null && this.random.nextInt(10) == 0) {
/*  479 */         boolean hasWatertightBottom = state.isFaceSturdy((BlockGetter)this, (BlockPos)pos, Direction.DOWN);
/*  480 */         BlockPos below = pos.below();
/*  481 */         trySpawnDripParticles(below, getBlockState(below), dripParticle, hasWatertightBottom);
/*      */       } 
/*      */     } 
/*      */     
/*  485 */     if (markerParticleTarget == state.getBlock()) {
/*  486 */       addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK_MARKER, state), x + 0.5D, y + 0.5D, z + 0.5D, 0.0D, 0.0D, 0.0D);
/*      */     }
/*      */     
/*  489 */     if (!state.isCollisionShapeFullBlock((BlockGetter)this, (BlockPos)pos)) {
/*  490 */       for (AmbientParticle particle : (Iterable<AmbientParticle>)environmentAttributes().getValue(EnvironmentAttributes.AMBIENT_PARTICLES, (BlockPos)pos)) {
/*  491 */         if (particle.canSpawn(this.random)) {
/*  492 */           addParticle(particle.particle(), pos.getX() + this.random.nextDouble(), pos.getY() + this.random.nextDouble(), pos.getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void trySpawnDripParticles(BlockPos pos, BlockState state, ParticleOptions dripParticle, boolean isTopSolid) {
/*  499 */     if (!state.getFluidState().isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  503 */     VoxelShape collisionShape = state.getCollisionShape((BlockGetter)this, pos);
/*  504 */     double topSideHeight = collisionShape.max(Direction.Axis.Y);
/*  505 */     if (topSideHeight < 1.0D) {
/*  506 */       if (isTopSolid) {
/*  507 */         spawnFluidParticle(pos.getX(), (pos.getX() + 1), pos.getZ(), (pos.getZ() + 1), (pos.getY() + 1) - 0.05D, dripParticle);
/*      */       }
/*  509 */     } else if (!state.is(BlockTags.IMPERMEABLE)) {
/*  510 */       double bottomSideHeight = collisionShape.min(Direction.Axis.Y);
/*  511 */       if (bottomSideHeight > 0.0D) {
/*  512 */         spawnParticle(pos, dripParticle, collisionShape, pos.getY() + bottomSideHeight - 0.05D);
/*      */       } else {
/*  514 */         BlockPos below = pos.below();
/*  515 */         BlockState belowState = getBlockState(below);
/*  516 */         VoxelShape belowShape = belowState.getCollisionShape((BlockGetter)this, below);
/*  517 */         double belowTopSideHeight = belowShape.max(Direction.Axis.Y);
/*  518 */         if (belowTopSideHeight < 1.0D && belowState.getFluidState().isEmpty()) {
/*  519 */           spawnParticle(pos, dripParticle, collisionShape, pos.getY() - 0.05D);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void spawnParticle(BlockPos pos, ParticleOptions dripParticle, VoxelShape dripShape, double height) {
/*  526 */     spawnFluidParticle(pos.getX() + dripShape.min(Direction.Axis.X), 
/*  527 */         pos.getX() + dripShape.max(Direction.Axis.X), 
/*  528 */         pos.getZ() + dripShape.min(Direction.Axis.Z), 
/*  529 */         pos.getZ() + dripShape.max(Direction.Axis.Z), height, dripParticle);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void spawnFluidParticle(double x1, double x2, double z1, double z2, double y, ParticleOptions dripParticle) {
/*  535 */     addParticle(dripParticle, Mth.lerp(this.random.nextDouble(), x1, x2), y, Mth.lerp(this.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public CrashReportCategory fillReportDetails(CrashReport report) {
/*  540 */     CrashReportCategory category = super.fillReportDetails(report);
/*      */     
/*  542 */     category.setDetail("Server brand", () -> this.minecraft.player.connection.serverBrand());
/*  543 */     category.setDetail("Server type", () -> (this.minecraft.getSingleplayerServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server");
/*  544 */     category.setDetail("Tracked entity count", () -> String.valueOf(getEntityCount()));
/*      */     
/*  546 */     return category;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSeededSound(Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/*  551 */     if (except == this.minecraft.player) {
/*  552 */       playSound(x, y, z, (SoundEvent)sound.value(), source, volume, pitch, false, seed);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSeededSound(Entity except, Entity sourceEntity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/*  558 */     if (except == this.minecraft.player) {
/*  559 */       this.minecraft.getSoundManager().play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)sound.value(), source, volume, pitch, sourceEntity, seed));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void playLocalSound(Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {
/*  565 */     this.minecraft.getSoundManager().play((SoundInstance)new EntityBoundSoundInstance(sound, source, volume, pitch, sourceEntity, this.random.nextLong()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void playPlayerSound(SoundEvent sound, SoundSource source, float volume, float pitch) {
/*  570 */     if (this.minecraft.player != null) {
/*  571 */       this.minecraft.getSoundManager().play((SoundInstance)new EntityBoundSoundInstance(sound, source, volume, pitch, (Entity)this.minecraft.player, this.random.nextLong()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
/*  577 */     playSound(x, y, z, sound, source, volume, pitch, distanceDelay, this.random.nextLong());
/*      */   }
/*      */   
/*      */   private void playSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay, long seed) {
/*  581 */     double distanceToSqr = this.minecraft.gameRenderer.getMainCamera().position().distanceToSqr(x, y, z);
/*  582 */     SimpleSoundInstance instance = new SimpleSoundInstance(sound, source, volume, pitch, RandomSource.create(seed), x, y, z);
/*      */     
/*  584 */     if (distanceDelay && distanceToSqr > 100.0D) {
/*      */       
/*  586 */       double delayInSeconds = Math.sqrt(distanceToSqr) / 40.0D;
/*  587 */       this.minecraft.getSoundManager().playDelayed((SoundInstance)instance, (int)(delayInSeconds * 20.0D));
/*      */     } else {
/*  589 */       this.minecraft.getSoundManager().play((SoundInstance)instance);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void createFireworks(double x, double y, double z, double xd, double yd, double zd, List<FireworkExplosion> explosions) {
/*  595 */     if (explosions.isEmpty()) {
/*      */       
/*  597 */       for (int i = 0; i < this.random.nextInt(3) + 2; i++) {
/*  598 */         addParticle((ParticleOptions)ParticleTypes.POOF, x, y, z, this.random.nextGaussian() * 0.05D, 0.005D, this.random.nextGaussian() * 0.05D);
/*      */       }
/*      */     } else {
/*  601 */       this.minecraft.particleEngine.add((Particle)new FireworkParticles.Starter(this, x, y, z, xd, yd, zd, this.minecraft.particleEngine, explosions));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendPacketToServer(Packet<?> packet) {
/*  607 */     this.connection.send(packet);
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldBorder getWorldBorder() {
/*  612 */     return this.worldBorder;
/*      */   }
/*      */ 
/*      */   
/*      */   public RecipeAccess recipeAccess() {
/*  617 */     return this.connection.recipes();
/*      */   }
/*      */ 
/*      */   
/*      */   public TickRateManager tickRateManager() {
/*  622 */     return this.tickRateManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public EnvironmentAttributeSystem environmentAttributes() {
/*  627 */     return this.environmentAttributes;
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelTickAccess<Block> getBlockTicks() {
/*  632 */     return BlackholeTickAccess.emptyLevelList();
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelTickAccess<net.minecraft.world.level.material.Fluid> getFluidTicks() {
/*  637 */     return BlackholeTickAccess.emptyLevelList();
/*      */   }
/*      */ 
/*      */   
/*      */   public ClientChunkCache getChunkSource() {
/*  642 */     return this.chunkSource;
/*      */   }
/*      */ 
/*      */   
/*      */   public MapItemSavedData getMapData(MapId id) {
/*  647 */     return this.mapData.get(id);
/*      */   }
/*      */   
/*      */   public void overrideMapData(MapId id, MapItemSavedData data) {
/*  651 */     this.mapData.put(id, data);
/*      */   }
/*      */ 
/*      */   
/*      */   public Scoreboard getScoreboard() {
/*  656 */     return this.connection.scoreboard();
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, @Block.UpdateFlags int updateFlags) {
/*  661 */     this.levelRenderer.blockChanged((BlockGetter)this, pos, old, current, updateFlags);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlocksDirty(BlockPos pos, BlockState oldState, BlockState newState) {
/*  666 */     this.levelRenderer.setBlockDirty(pos, oldState, newState);
/*      */   }
/*      */   
/*      */   public void setSectionDirtyWithNeighbors(int chunkX, int chunkY, int chunkZ) {
/*  670 */     this.levelRenderer.setSectionDirtyWithNeighbors(chunkX, chunkY, chunkZ);
/*      */   }
/*      */   
/*      */   public void setSectionRangeDirty(int minSectionX, int minSectionY, int minSectionZ, int maxSectionX, int maxSectionY, int maxSectionZ) {
/*  674 */     this.levelRenderer.setSectionRangeDirty(minSectionX, minSectionY, minSectionZ, maxSectionX, maxSectionY, maxSectionZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyBlockProgress(int id, BlockPos blockPos, int progress) {
/*  679 */     this.levelRenderer.destroyBlockProgress(id, blockPos, progress);
/*      */   }
/*      */ 
/*      */   
/*      */   public void globalLevelEvent(int type, BlockPos pos, int data) {
/*  684 */     this.levelEventHandler.globalLevelEvent(type, pos, data);
/*      */   }
/*      */ 
/*      */   
/*      */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {
/*      */     try {
/*  690 */       this.levelEventHandler.levelEvent(type, pos, data);
/*  691 */     } catch (Throwable t) {
/*  692 */       CrashReport report = CrashReport.forThrowable(t, "Playing level event");
/*  693 */       CrashReportCategory category = report.addCategory("Level event being played");
/*      */       
/*  695 */       category.setDetail("Block coordinates", CrashReportCategory.formatLocation((LevelHeightAccessor)this, pos));
/*  696 */       category.setDetail("Event source", source);
/*  697 */       category.setDetail("Event type", type);
/*  698 */       category.setDetail("Event data", data);
/*      */       
/*  700 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
/*  706 */     doAddParticle(particle, particle.getType().getOverrideLimiter(), false, x, y, z, xd, yd, zd);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, double xd, double yd, double zd) {
/*  711 */     doAddParticle(particle, (particle.getType().getOverrideLimiter() || overrideLimiter), alwaysShow, x, y, z, xd, yd, zd);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAlwaysVisibleParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
/*  716 */     doAddParticle(particle, false, true, x, y, z, xd, yd, zd);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAlwaysVisibleParticle(ParticleOptions particle, boolean overrideLimiter, double x, double y, double z, double xd, double yd, double zd) {
/*  721 */     doAddParticle(particle, (particle.getType().getOverrideLimiter() || overrideLimiter), true, x, y, z, xd, yd, zd);
/*      */   }
/*      */   
/*      */   private void doAddParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShowParticles, double x, double y, double z, double xd, double yd, double zd) {
/*      */     try {
/*  726 */       Camera camera = this.minecraft.gameRenderer.getMainCamera();
/*  727 */       ParticleStatus particleLevel = calculateParticleLevel(alwaysShowParticles);
/*      */       
/*  729 */       if (overrideLimiter) {
/*  730 */         this.minecraft.particleEngine.createParticle(particle, x, y, z, xd, yd, zd);
/*      */         
/*      */         return;
/*      */       } 
/*  734 */       if (camera.position().distanceToSqr(x, y, z) > 1024.0D) {
/*      */         return;
/*      */       }
/*      */       
/*  738 */       if (particleLevel == ParticleStatus.MINIMAL) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  743 */       this.minecraft.particleEngine.createParticle(particle, x, y, z, xd, yd, zd);
/*  744 */     } catch (Throwable t) {
/*  745 */       CrashReport report = CrashReport.forThrowable(t, "Exception while adding particle");
/*  746 */       CrashReportCategory category = report.addCategory("Particle being added");
/*      */       
/*  748 */       category.setDetail("ID", BuiltInRegistries.PARTICLE_TYPE.getKey(particle.getType()));
/*  749 */       category.setDetail("Parameters", () -> ParticleTypes.CODEC.encodeStart((DynamicOps)registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE), particle).toString());
/*  750 */       category.setDetail("Position", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, x, y, z));
/*      */       
/*  752 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ParticleStatus calculateParticleLevel(boolean alwaysShowParticles) {
/*  757 */     ParticleStatus particleLevel = (ParticleStatus)this.minecraft.options.particles().get();
/*      */     
/*  759 */     if (alwaysShowParticles && particleLevel == ParticleStatus.MINIMAL)
/*      */     {
/*  761 */       if (this.random.nextInt(10) == 0) {
/*  762 */         particleLevel = ParticleStatus.DECREASED;
/*      */       }
/*      */     }
/*      */     
/*  766 */     if (particleLevel == ParticleStatus.DECREASED)
/*      */     {
/*  768 */       if (this.random.nextInt(3) == 0) {
/*  769 */         particleLevel = ParticleStatus.MINIMAL;
/*      */       }
/*      */     }
/*      */     
/*  773 */     return particleLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<AbstractClientPlayer> players() {
/*  778 */     return this.players;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<EnderDragonPart> dragonParts() {
/*  783 */     return this.dragonParts;
/*      */   }
/*      */ 
/*      */   
/*      */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
/*  788 */     return (Holder<Biome>)registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
/*      */   }
/*      */   
/*      */   private int getSkyFlashTime() {
/*  792 */     return (Boolean)this.minecraft.options.hideLightningFlash().get() ? 0 : this.skyFlashTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSkyFlashTime(int skyFlashTime) {
/*  797 */     this.skyFlashTime = skyFlashTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getShade(Direction direction, boolean shade) {
/*  802 */     DimensionType.CardinalLightType type = dimensionType().cardinalLightType();
/*      */     
/*  804 */     if (!shade) {
/*  805 */       return (type == DimensionType.CardinalLightType.NETHER) ? 0.9F : 1.0F;
/*      */     }
/*      */     
/*  808 */     switch (direction) { default: throw new MatchException(null, null);
/*  809 */       case DOWN: if (type == DimensionType.CardinalLightType.NETHER);
/*  810 */       case UP: if (type == DimensionType.CardinalLightType.NETHER);
/*      */       case NORTH: case SOUTH: 
/*  812 */       case WEST: case EAST: break; }  return 0.6F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBlockTint(BlockPos pos, ColorResolver resolver) {
/*  818 */     BlockTintCache cache = (BlockTintCache)this.tintCaches.get(resolver);
/*  819 */     return cache.getColor(pos);
/*      */   }
/*      */   
/*      */   public int calculateBlockTint(BlockPos pos, ColorResolver colorResolver) {
/*  823 */     int dist = (Integer)(Minecraft.getInstance()).options.biomeBlendRadius().get();
/*  824 */     if (dist == 0) {
/*  825 */       return colorResolver.getColor((Biome)getBiome(pos).value(), pos.getX(), pos.getZ());
/*      */     }
/*      */     
/*  828 */     int count = (dist * 2 + 1) * (dist * 2 + 1);
/*  829 */     int totalRed = 0;
/*  830 */     int totalGreen = 0;
/*  831 */     int totalBlue = 0;
/*      */     
/*  833 */     Cursor3D cursor = new Cursor3D(pos.getX() - dist, pos.getY(), pos.getZ() - dist, pos.getX() + dist, pos.getY(), pos.getZ() + dist);
/*  834 */     BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();
/*  835 */     while (cursor.advance()) {
/*  836 */       nextPos.set(cursor.nextX(), cursor.nextY(), cursor.nextZ());
/*  837 */       int color = colorResolver.getColor((Biome)getBiome((BlockPos)nextPos).value(), nextPos.getX(), nextPos.getZ());
/*      */       
/*  839 */       totalRed += (color & 0xFF0000) >> 16;
/*  840 */       totalGreen += (color & 0xFF00) >> 8;
/*  841 */       totalBlue += color & 0xFF;
/*      */     } 
/*      */     
/*  844 */     return (totalRed / count & 0xFF) << 16 | (totalGreen / count & 0xFF) << 8 | totalBlue / count & 0xFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRespawnData(LevelData.RespawnData respawnData) {
/*  849 */     this.levelData.setSpawn(getWorldBorderAdjustedRespawnData(respawnData));
/*      */   }
/*      */ 
/*      */   
/*      */   public LevelData.RespawnData getRespawnData() {
/*  854 */     return this.levelData.getRespawnData();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  859 */     return "ClientLevel";
/*      */   }
/*      */ 
/*      */   
/*      */   public ClientLevelData getLevelData() {
/*  864 */     return this.clientLevelData;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 pos, GameEvent.Context context) {}
/*      */ 
/*      */   
/*      */   protected Map<MapId, MapItemSavedData> getAllMapData() {
/*  873 */     return (Map<MapId, MapItemSavedData>)ImmutableMap.copyOf(this.mapData);
/*      */   }
/*      */   
/*      */   protected void addMapData(Map<MapId, MapItemSavedData> mapData) {
/*  877 */     this.mapData.putAll(mapData);
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ClientLevelData
/*      */     implements WritableLevelData
/*      */   {
/*      */     private final boolean hardcore;
/*      */     private final boolean isFlat;
/*      */     private LevelData.RespawnData respawnData;
/*      */     private long gameTime;
/*      */     private long dayTime;
/*      */     private boolean raining;
/*      */     private Difficulty difficulty;
/*      */     private boolean difficultyLocked;
/*      */     
/*      */     public ClientLevelData(Difficulty difficulty, boolean hardcore, boolean isFlat) {
/*  894 */       this.difficulty = difficulty;
/*  895 */       this.hardcore = hardcore;
/*  896 */       this.isFlat = isFlat;
/*      */     }
/*      */ 
/*      */     
/*      */     public LevelData.RespawnData getRespawnData() {
/*  901 */       return this.respawnData;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getGameTime() {
/*  906 */       return this.gameTime;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getDayTime() {
/*  911 */       return this.dayTime;
/*      */     }
/*      */     
/*      */     public void setGameTime(long time) {
/*  915 */       this.gameTime = time;
/*      */     }
/*      */     
/*      */     public void setDayTime(long time) {
/*  919 */       this.dayTime = time;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setSpawn(LevelData.RespawnData respawnData) {
/*  924 */       this.respawnData = respawnData;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isThundering() {
/*  929 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRaining() {
/*  934 */       return this.raining;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setRaining(boolean raining) {
/*  939 */       this.raining = raining;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isHardcore() {
/*  944 */       return this.hardcore;
/*      */     }
/*      */ 
/*      */     
/*      */     public Difficulty getDifficulty() {
/*  949 */       return this.difficulty;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isDifficultyLocked() {
/*  954 */       return this.difficultyLocked;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/*  959 */       super.fillCrashReportCategory(category, levelHeightAccessor);
/*      */     }
/*      */     
/*      */     public void setDifficulty(Difficulty difficulty) {
/*  963 */       this.difficulty = difficulty;
/*      */     }
/*      */     
/*      */     public void setDifficultyLocked(boolean locked) {
/*  967 */       this.difficultyLocked = locked;
/*      */     }
/*      */     
/*      */     public double getHorizonHeight(LevelHeightAccessor level) {
/*  971 */       if (this.isFlat) {
/*  972 */         return level.getMinY();
/*      */       }
/*      */       
/*  975 */       return 63.0D;
/*      */     }
/*      */     
/*      */     public float voidDarknessOnsetRange() {
/*  979 */       if (this.isFlat) {
/*  980 */         return 1.0F;
/*      */       }
/*  982 */       return 32.0F;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected LevelEntityGetter<Entity> getEntities() {
/*  988 */     return this.entityStorage.getEntityGetter();
/*      */   }
/*      */ 
/*      */   
/*      */   private final class EntityCallbacks
/*      */     implements LevelCallback<Entity>
/*      */   {
/*      */     public void onCreated(Entity entity) {}
/*      */ 
/*      */     
/*      */     public void onDestroyed(Entity entity) {}
/*      */ 
/*      */     
/*      */     public void onTickingStart(Entity entity) {
/* 1002 */       ClientLevel.this.tickingEntities.add(entity);
/*      */     }
/*      */ 
/*      */     
/*      */     public void onTickingEnd(Entity entity) {
/* 1007 */       ClientLevel.this.tickingEntities.remove(entity);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onTrackingStart(Entity entity) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: dup
/*      */       //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */       //   5: pop
/*      */       //   6: astore_2
/*      */       //   7: iconst_0
/*      */       //   8: istore_3
/*      */       //   9: aload_2
/*      */       //   10: iload_3
/*      */       //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*      */       //   16: lookupswitch default -> 98, 0 -> 44, 1 -> 68
/*      */       //   44: aload_2
/*      */       //   45: checkcast net/minecraft/client/player/AbstractClientPlayer
/*      */       //   48: astore #4
/*      */       //   50: aload_0
/*      */       //   51: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   54: getfield players : Ljava/util/List;
/*      */       //   57: aload #4
/*      */       //   59: invokeinterface add : (Ljava/lang/Object;)Z
/*      */       //   64: pop
/*      */       //   65: goto -> 98
/*      */       //   68: aload_2
/*      */       //   69: checkcast net/minecraft/world/entity/boss/enderdragon/EnderDragon
/*      */       //   72: astore #5
/*      */       //   74: aload_0
/*      */       //   75: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   78: getfield dragonParts : Ljava/util/List;
/*      */       //   81: aload #5
/*      */       //   83: invokevirtual getSubEntities : ()[Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*      */       //   86: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
/*      */       //   89: invokeinterface addAll : (Ljava/util/Collection;)Z
/*      */       //   94: pop
/*      */       //   95: goto -> 98
/*      */       //   98: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1012	-> 0
/*      */       //   #1013	-> 44
/*      */       //   #1014	-> 50
/*      */       //   #1015	-> 65
/*      */       //   #1016	-> 68
/*      */       //   #1017	-> 74
/*      */       //   #1018	-> 95
/*      */       //   #1021	-> 98
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   50	18	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*      */       //   74	24	5	dragon	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*      */       //   0	99	0	this	Lnet/minecraft/client/multiplayer/ClientLevel$EntityCallbacks;
/*      */       //   0	99	1	entity	Lnet/minecraft/world/entity/Entity;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onTrackingEnd(Entity entity) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: invokevirtual unRide : ()V
/*      */       //   4: aload_1
/*      */       //   5: dup
/*      */       //   6: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */       //   9: pop
/*      */       //   10: astore_2
/*      */       //   11: iconst_0
/*      */       //   12: istore_3
/*      */       //   13: aload_2
/*      */       //   14: iload_3
/*      */       //   15: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*      */       //   20: lookupswitch default -> 102, 0 -> 48, 1 -> 72
/*      */       //   48: aload_2
/*      */       //   49: checkcast net/minecraft/client/player/AbstractClientPlayer
/*      */       //   52: astore #4
/*      */       //   54: aload_0
/*      */       //   55: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   58: getfield players : Ljava/util/List;
/*      */       //   61: aload #4
/*      */       //   63: invokeinterface remove : (Ljava/lang/Object;)Z
/*      */       //   68: pop
/*      */       //   69: goto -> 102
/*      */       //   72: aload_2
/*      */       //   73: checkcast net/minecraft/world/entity/boss/enderdragon/EnderDragon
/*      */       //   76: astore #5
/*      */       //   78: aload_0
/*      */       //   79: getfield this$0 : Lnet/minecraft/client/multiplayer/ClientLevel;
/*      */       //   82: getfield dragonParts : Ljava/util/List;
/*      */       //   85: aload #5
/*      */       //   87: invokevirtual getSubEntities : ()[Lnet/minecraft/world/entity/boss/enderdragon/EnderDragonPart;
/*      */       //   90: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
/*      */       //   93: invokeinterface removeAll : (Ljava/util/Collection;)Z
/*      */       //   98: pop
/*      */       //   99: goto -> 102
/*      */       //   102: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1025	-> 0
/*      */       //   #1026	-> 4
/*      */       //   #1027	-> 48
/*      */       //   #1028	-> 54
/*      */       //   #1029	-> 69
/*      */       //   #1030	-> 72
/*      */       //   #1031	-> 78
/*      */       //   #1032	-> 99
/*      */       //   #1035	-> 102
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   54	18	4	player	Lnet/minecraft/client/player/AbstractClientPlayer;
/*      */       //   78	24	5	dragon	Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;
/*      */       //   0	103	0	this	Lnet/minecraft/client/multiplayer/ClientLevel$EntityCallbacks;
/*      */       //   0	103	1	entity	Lnet/minecraft/world/entity/Entity;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onSectionChange(Entity entity) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String gatherChunkSourceStats() {
/* 1044 */     return "Chunks[C] W: " + this.chunkSource.gatherStats() + " E: " + this.entityStorage.gatherStats();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addDestroyBlockEffect(BlockPos pos, BlockState blockState) {
/* 1049 */     if (blockState.isAir() || !blockState.shouldSpawnTerrainParticles()) {
/*      */       return;
/*      */     }
/*      */     
/* 1053 */     VoxelShape shape = blockState.getShape((BlockGetter)this, pos);
/*      */     
/* 1055 */     double density = 0.25D;
/*      */ 
/*      */     
/* 1058 */     shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
/*      */           double widthX = Math.min(1.0D, x2 - pos), widthY = Math.min(1.0D, y2 - y1), widthZ = Math.min(1.0D, z2 - z1);
/*      */           int countX = Math.max(2, Mth.ceil(widthX / 0.25D)), countY = Math.max(2, Mth.ceil(widthY / 0.25D)), countZ = Math.max(2, Mth.ceil(widthZ / 0.25D));
/*      */           for (int xx = 0; xx < countX; xx++) {
/*      */             for (int yy = 0; yy < countY; yy++) {
/*      */               for (int zz = 0; zz < countZ; zz++) {
/*      */                 double relX = (xx + 0.5D) / countX, relY = (yy + 0.5D) / countY, relZ = (zz + 0.5D) / countZ, x = relX * widthX + pos, y = relY * widthY + y1, z = relZ * widthZ + z1;
/*      */                 this.minecraft.particleEngine.add((Particle)new TerrainParticle(this, pos.getX() + x, pos.getY() + y, pos.getZ() + z, relX - 0.5D, relY - 0.5D, relZ - 0.5D, pos, pos));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         });
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBreakingBlockEffect(BlockPos pos, Direction direction) {
/* 1086 */     BlockState blockState = getBlockState(pos);
/* 1087 */     if (blockState.getRenderShape() == net.minecraft.world.level.block.RenderShape.INVISIBLE || !blockState.shouldSpawnTerrainParticles()) {
/*      */       return;
/*      */     }
/*      */     
/* 1091 */     int x = pos.getX();
/* 1092 */     int y = pos.getY();
/* 1093 */     int z = pos.getZ();
/*      */     
/* 1095 */     float r = 0.1F;
/*      */     
/* 1097 */     AABB shape = blockState.getShape((BlockGetter)this, pos).bounds();
/* 1098 */     double xp = x + this.random.nextDouble() * (shape.maxX - shape.minX - 0.20000000298023224D) + 0.10000000149011612D + shape.minX;
/* 1099 */     double yp = y + this.random.nextDouble() * (shape.maxY - shape.minY - 0.20000000298023224D) + 0.10000000149011612D + shape.minY;
/* 1100 */     double zp = z + this.random.nextDouble() * (shape.maxZ - shape.minZ - 0.20000000298023224D) + 0.10000000149011612D + shape.minZ;
/*      */ 
/*      */     
/* 1103 */     if (direction == Direction.DOWN) {
/* 1104 */       yp = y + shape.minY - 0.10000000149011612D;
/*      */     }
/* 1106 */     if (direction == Direction.UP) {
/* 1107 */       yp = y + shape.maxY + 0.10000000149011612D;
/*      */     }
/* 1109 */     if (direction == Direction.NORTH) {
/* 1110 */       zp = z + shape.minZ - 0.10000000149011612D;
/*      */     }
/* 1112 */     if (direction == Direction.SOUTH) {
/* 1113 */       zp = z + shape.maxZ + 0.10000000149011612D;
/*      */     }
/* 1115 */     if (direction == Direction.WEST) {
/* 1116 */       xp = x + shape.minX - 0.10000000149011612D;
/*      */     }
/* 1118 */     if (direction == Direction.EAST) {
/* 1119 */       xp = x + shape.maxX + 0.10000000149011612D;
/*      */     }
/*      */     
/* 1122 */     this.minecraft.particleEngine.add(new TerrainParticle(this, xp, yp, zp, 0.0D, 0.0D, 0.0D, blockState, pos).setPower(0.2F).scale(0.6F));
/*      */   }
/*      */   
/*      */   public void setServerSimulationDistance(int serverSimulationDistance) {
/* 1126 */     this.serverSimulationDistance = serverSimulationDistance;
/*      */   }
/*      */   
/*      */   public int getServerSimulationDistance() {
/* 1130 */     return this.serverSimulationDistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public FeatureFlagSet enabledFeatures() {
/* 1135 */     return this.connection.enabledFeatures();
/*      */   }
/*      */ 
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 1140 */     return this.connection.potionBrewing();
/*      */   }
/*      */ 
/*      */   
/*      */   public FuelValues fuelValues() {
/* 1145 */     return this.connection.fuelValues();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, Level.ExplosionInteraction interactionType, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, WeightedList<ExplosionParticleInfo> secondaryParticles, Holder<SoundEvent> explosionSound) {}
/*      */ 
/*      */   
/*      */   public int getSeaLevel() {
/* 1154 */     return this.seaLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getClientLeafTintColor(BlockPos pos) {
/* 1159 */     return Minecraft.getInstance().getBlockColors().getColor(getBlockState(pos), (BlockAndTintGetter)this, pos, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerForCleaning(CacheSlot<ClientLevel, ?> slot) {
/* 1164 */     this.connection.registerForCleaning(slot);
/*      */   }
/*      */   
/*      */   public void trackExplosionEffects(Vec3 center, float radius, int blockCount, WeightedList<ExplosionParticleInfo> blockParticles) {
/* 1168 */     this.explosionTracker.track(center, radius, blockCount, blockParticles);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */