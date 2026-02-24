/*     */ package net.minecraft.world.level.dimension.end;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ContiguousSet;
/*     */ import com.google.common.collect.DiscreteDomain;
/*     */ import com.google.common.collect.Range;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.data.worldgen.features.EndFeatures;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockPredicate;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ 
/*     */ public class EndDragonFight {
/*     */   public static final class Data extends Record {
/*     */     private final boolean needsStateScanning;
/*     */     private final boolean dragonKilled;
/*     */     private final boolean previouslyKilled;
/*     */     private final boolean isRespawning;
/*     */     private final Optional<UUID> dragonUUID;
/*     */     private final Optional<BlockPos> exitPortalLocation;
/*     */     private final Optional<List<Integer>> gateways;
/*     */     public static final Codec<Data> CODEC;
/*     */     
/*  68 */     public Data(boolean needsStateScanning, boolean dragonKilled, boolean previouslyKilled, boolean isRespawning, Optional<UUID> dragonUUID, Optional<BlockPos> exitPortalLocation, Optional<List<Integer>> gateways) { this.needsStateScanning = needsStateScanning; this.dragonKilled = dragonKilled; this.previouslyKilled = previouslyKilled; this.isRespawning = isRespawning; this.dragonUUID = dragonUUID; this.exitPortalLocation = exitPortalLocation; this.gateways = gateways; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  68 */       //   0	7	0	this	Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data; } public boolean needsStateScanning() { return this.needsStateScanning; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;
/*  68 */       //   0	8	1	o	Ljava/lang/Object; } public boolean dragonKilled() { return this.dragonKilled; } public boolean previouslyKilled() { return this.previouslyKilled; } public boolean isRespawning() { return this.isRespawning; } public Optional<UUID> dragonUUID() { return this.dragonUUID; } public Optional<BlockPos> exitPortalLocation() { return this.exitPortalLocation; } public Optional<List<Integer>> gateways() { return this.gateways; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  77 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("NeedsStateScanning").orElse(true).forGetter(Data::needsStateScanning), (App)Codec.BOOL.fieldOf("DragonKilled").orElse(false).forGetter(Data::dragonKilled), (App)Codec.BOOL.fieldOf("PreviouslyKilled").orElse(false).forGetter(Data::previouslyKilled), (App)Codec.BOOL.lenientOptionalFieldOf("IsRespawning", false).forGetter(Data::isRespawning), (App)UUIDUtil.CODEC.lenientOptionalFieldOf("Dragon").forGetter(Data::dragonUUID), (App)BlockPos.CODEC.lenientOptionalFieldOf("ExitPortalLocation").forGetter(Data::exitPortalLocation), (App)Codec.list((Codec)Codec.INT).lenientOptionalFieldOf("Gateways").forGetter(Data::gateways)).apply((Applicative)i, Data::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     public static final Data DEFAULT = new Data(true, false, false, false, Optional.empty(), Optional.empty(), Optional.empty());
/*     */   }
/*     */   
/*  91 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_TICKS_BEFORE_DRAGON_RESPAWN = 1200;
/*     */   private static final int TIME_BETWEEN_CRYSTAL_SCANS = 100;
/*     */   public static final int TIME_BETWEEN_PLAYER_SCANS = 20;
/*     */   private static final int ARENA_SIZE_CHUNKS = 8;
/*     */   public static final int ARENA_TICKET_LEVEL = 9;
/*     */   private static final int GATEWAY_COUNT = 20;
/*     */   private static final int GATEWAY_DISTANCE = 96;
/*     */   public static final int DRAGON_SPAWN_Y = 128;
/*     */   private final Predicate<Entity> validPlayer;
/* 102 */   private final ServerBossEvent dragonEvent = (ServerBossEvent)new ServerBossEvent((Component)Component.translatable("entity.minecraft.ender_dragon"), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS).setPlayBossMusic(true).setCreateWorldFog(true);
/*     */   private final ServerLevel level;
/*     */   private final BlockPos origin;
/* 105 */   private final ObjectArrayList<Integer> gateways = new ObjectArrayList();
/*     */   private final BlockPattern exitPortalPattern;
/*     */   private int ticksSinceDragonSeen;
/*     */   private int crystalsAlive;
/*     */   private int ticksSinceCrystalsScanned;
/* 110 */   private int ticksSinceLastPlayerScan = 21;
/*     */   
/*     */   private boolean dragonKilled;
/*     */   private boolean previouslyKilled;
/*     */   private boolean skipArenaLoadedCheck = false;
/*     */   private UUID dragonUUID;
/*     */   private boolean needsStateScanning = true;
/*     */   private BlockPos portalLocation;
/*     */   private DragonRespawnAnimation respawnStage;
/*     */   private int respawnTime;
/*     */   private List<EndCrystal> respawnCrystals;
/*     */   
/*     */   public EndDragonFight(ServerLevel level, long seed, Data dragonFightData) {
/* 123 */     this(level, seed, dragonFightData, BlockPos.ZERO);
/*     */   }
/*     */   
/*     */   public EndDragonFight(ServerLevel level, long seed, Data dragonFightData, BlockPos origin) {
/* 127 */     this.level = level;
/* 128 */     this.origin = origin;
/* 129 */     this.validPlayer = EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.withinDistance(origin.getX(), (128 + origin.getY()), origin.getZ(), 192.0D));
/* 130 */     this.needsStateScanning = dragonFightData.needsStateScanning;
/* 131 */     this.dragonUUID = dragonFightData.dragonUUID.orElse(null);
/* 132 */     this.dragonKilled = dragonFightData.dragonKilled;
/* 133 */     this.previouslyKilled = dragonFightData.previouslyKilled;
/* 134 */     if (dragonFightData.isRespawning) {
/* 135 */       this.respawnStage = DragonRespawnAnimation.START;
/*     */     }
/* 137 */     this.portalLocation = dragonFightData.exitPortalLocation.orElse(null);
/* 138 */     this.gateways.addAll(dragonFightData.gateways.orElseGet(() -> {
/*     */             ObjectArrayList<Integer> gateways = new ObjectArrayList((Collection)ContiguousSet.create(Range.closedOpen(0, 20), DiscreteDomain.integers()));
/*     */             
/*     */             Util.shuffle((List)gateways, RandomSource.create(seed));
/*     */             return gateways;
/*     */           }));
/* 144 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       .exitPortalPattern = net.minecraft.world.level.block.state.pattern.BlockPatternBuilder.start().aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       " }).aisle(new String[] { "  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  " }).aisle(new String[] { "       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       " }).where('#', BlockInWorld.hasState((Predicate)BlockPredicate.forBlock(Blocks.BEDROCK))).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   public void skipArenaLoadedCheck() {
/* 203 */     this.skipArenaLoadedCheck = true;
/*     */   }
/*     */   
/*     */   public Data saveData() {
/* 207 */     return new Data(this.needsStateScanning, this.dragonKilled, this.previouslyKilled, false, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 212 */         Optional.ofNullable(this.dragonUUID), 
/* 213 */         Optional.ofNullable(this.portalLocation), 
/* 214 */         (Optional)Optional.of(this.gateways));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 219 */     this.dragonEvent.setVisible(!this.dragonKilled);
/*     */     
/* 221 */     if (++this.ticksSinceLastPlayerScan >= 20) {
/* 222 */       updatePlayers();
/* 223 */       this.ticksSinceLastPlayerScan = 0;
/*     */     } 
/*     */     
/* 226 */     if (!this.dragonEvent.getPlayers().isEmpty()) {
/* 227 */       this.level.getChunkSource().addTicketWithRadius(TicketType.DRAGON, new ChunkPos(0, 0), 9);
/*     */       
/* 229 */       boolean arenaLoaded = isArenaLoaded();
/*     */       
/* 231 */       if (this.needsStateScanning && arenaLoaded) {
/* 232 */         scanState();
/* 233 */         this.needsStateScanning = false;
/*     */       } 
/*     */       
/* 236 */       if (this.respawnStage != null) {
/* 237 */         if (this.respawnCrystals == null && arenaLoaded) {
/* 238 */           this.respawnStage = null;
/* 239 */           tryRespawn();
/*     */         } 
/* 241 */         this.respawnStage.tick(this.level, this, this.respawnCrystals, this.respawnTime++, this.portalLocation);
/*     */       } 
/*     */       
/* 244 */       if (!this.dragonKilled) {
/* 245 */         if ((this.dragonUUID == null || ++this.ticksSinceDragonSeen >= 1200) && arenaLoaded) {
/* 246 */           findOrCreateDragon();
/* 247 */           this.ticksSinceDragonSeen = 0;
/*     */         } 
/*     */         
/* 250 */         if (++this.ticksSinceCrystalsScanned >= 100 && arenaLoaded) {
/* 251 */           updateCrystalCount();
/* 252 */           this.ticksSinceCrystalsScanned = 0;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 256 */       this.level.getChunkSource().removeTicketWithRadius(TicketType.DRAGON, new ChunkPos(0, 0), 9);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scanState() {
/* 261 */     LOGGER.info("Scanning for legacy world dragon fight...");
/* 262 */     boolean activePortalExists = hasActiveExitPortal();
/* 263 */     if (activePortalExists) {
/* 264 */       LOGGER.info("Found that the dragon has been killed in this world already.");
/* 265 */       this.previouslyKilled = true;
/*     */     } else {
/* 267 */       LOGGER.info("Found that the dragon has not yet been killed in this world.");
/* 268 */       this.previouslyKilled = false;
/* 269 */       if (findExitPortal() == null) {
/* 270 */         spawnExitPortal(false);
/*     */       }
/*     */     } 
/*     */     
/* 274 */     List<? extends EnderDragon> entities = this.level.getDragons();
/* 275 */     if (entities.isEmpty()) {
/* 276 */       this.dragonKilled = true;
/*     */     } else {
/* 278 */       EnderDragon dragon = entities.get(0);
/* 279 */       this.dragonUUID = dragon.getUUID();
/* 280 */       LOGGER.info("Found that there's a dragon still alive ({})", dragon);
/* 281 */       this.dragonKilled = false;
/*     */       
/* 283 */       if (!activePortalExists) {
/* 284 */         LOGGER.info("But we didn't have a portal, let's remove it.");
/* 285 */         dragon.discard();
/* 286 */         this.dragonUUID = null;
/*     */       } 
/*     */     } 
/*     */     
/* 290 */     if (!this.previouslyKilled && this.dragonKilled)
/*     */     {
/* 292 */       this.dragonKilled = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private void findOrCreateDragon() {
/* 297 */     List<? extends EnderDragon> entities = this.level.getDragons();
/* 298 */     if (entities.isEmpty()) {
/* 299 */       LOGGER.debug("Haven't seen the dragon, respawning it");
/* 300 */       createNewDragon();
/*     */     } else {
/* 302 */       LOGGER.debug("Haven't seen our dragon, but found another one to use.");
/* 303 */       this.dragonUUID = ((EnderDragon)entities.get(0)).getUUID();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setRespawnStage(DragonRespawnAnimation stage) {
/* 308 */     if (this.respawnStage == null) {
/* 309 */       throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
/*     */     }
/*     */     
/* 312 */     this.respawnTime = 0;
/* 313 */     if (stage == DragonRespawnAnimation.END) {
/* 314 */       this.respawnStage = null;
/* 315 */       this.dragonKilled = false;
/* 316 */       EnderDragon dragon = createNewDragon();
/*     */       
/* 318 */       if (dragon != null) {
/* 319 */         for (ServerPlayer player : (Iterable<ServerPlayer>)this.dragonEvent.getPlayers()) {
/* 320 */           net.minecraft.advancements.CriteriaTriggers.SUMMONED_ENTITY.trigger(player, (Entity)dragon);
/*     */         }
/*     */       }
/*     */     } else {
/* 324 */       this.respawnStage = stage;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasActiveExitPortal() {
/* 329 */     for (int x = -8; x <= 8; x++) {
/* 330 */       for (int z = -8; z <= 8; z++) {
/* 331 */         LevelChunk chunk = this.level.getChunk(x, z);
/* 332 */         for (BlockEntity blockEntity : (Iterable<BlockEntity>)chunk.getBlockEntities().values()) {
/* 333 */           if (blockEntity instanceof net.minecraft.world.level.block.entity.TheEndPortalBlockEntity) {
/* 334 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 340 */     return false;
/*     */   }
/*     */   
/*     */   private BlockPattern.BlockPatternMatch findExitPortal() {
/* 344 */     ChunkPos chunkOrigin = new ChunkPos(this.origin);
/* 345 */     for (int x = -8 + chunkOrigin.x; x <= 8 + chunkOrigin.x; x++) {
/* 346 */       for (int z = -8 + chunkOrigin.z; z <= 8 + chunkOrigin.z; z++) {
/* 347 */         LevelChunk chunk = this.level.getChunk(x, z);
/* 348 */         for (BlockEntity blockEntity : (Iterable<BlockEntity>)chunk.getBlockEntities().values()) {
/* 349 */           if (blockEntity instanceof net.minecraft.world.level.block.entity.TheEndPortalBlockEntity) {
/* 350 */             BlockPattern.BlockPatternMatch match = this.exitPortalPattern.find((LevelReader)this.level, blockEntity.getBlockPos());
/* 351 */             if (match != null) {
/* 352 */               BlockPos posInWorld = match.getBlock(3, 3, 3).getPos();
/* 353 */               if (this.portalLocation == null) {
/* 354 */                 this.portalLocation = posInWorld;
/*     */               }
/* 356 */               return match;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 363 */     BlockPos endPodiumLocation = EndPodiumFeature.getLocation(this.origin);
/* 364 */     int maxY = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, endPodiumLocation).getY();
/*     */     
/* 366 */     for (int y = maxY; y >= this.level.getMinY(); y--) {
/* 367 */       BlockPattern.BlockPatternMatch match = this.exitPortalPattern.find((LevelReader)this.level, new BlockPos(endPodiumLocation.getX(), y, endPodiumLocation.getZ()));
/* 368 */       if (match != null) {
/* 369 */         if (this.portalLocation == null) {
/* 370 */           this.portalLocation = match.getBlock(3, 3, 3).getPos();
/*     */         }
/* 372 */         return match;
/*     */       } 
/*     */     } 
/*     */     
/* 376 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isArenaLoaded() {
/* 380 */     if (this.skipArenaLoadedCheck) {
/* 381 */       return true;
/*     */     }
/* 383 */     ChunkPos chunkOrigin = new ChunkPos(this.origin);
/* 384 */     for (int x = -8 + chunkOrigin.x; x <= 8 + chunkOrigin.x; x++) {
/* 385 */       for (int z = 8 + chunkOrigin.z; z <= 8 + chunkOrigin.z; z++) {
/* 386 */         ChunkAccess chunk = this.level.getChunk(x, z, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
/* 387 */         if (!(chunk instanceof LevelChunk)) {
/* 388 */           return false;
/*     */         }
/* 390 */         FullChunkStatus status = ((LevelChunk)chunk).getFullStatus();
/* 391 */         if (!status.isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
/* 392 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 396 */     return true;
/*     */   }
/*     */   
/*     */   private void updatePlayers() {
/* 400 */     Set<ServerPlayer> newPlayers = Sets.newHashSet();
/* 401 */     for (ServerPlayer player : (Iterable<ServerPlayer>)this.level.getPlayers(this.validPlayer)) {
/* 402 */       this.dragonEvent.addPlayer(player);
/* 403 */       newPlayers.add(player);
/*     */     } 
/* 405 */     Set<ServerPlayer> toRemove = Sets.newHashSet(this.dragonEvent.getPlayers());
/* 406 */     toRemove.removeAll(newPlayers);
/* 407 */     for (ServerPlayer player : toRemove) {
/* 408 */       this.dragonEvent.removePlayer(player);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateCrystalCount() {
/* 413 */     this.ticksSinceCrystalsScanned = 0;
/* 414 */     this.crystalsAlive = 0;
/*     */     
/* 416 */     for (SpikeFeature.EndSpike spike : (Iterable<SpikeFeature.EndSpike>)SpikeFeature.getSpikesForLevel((WorldGenLevel)this.level)) {
/* 417 */       this.crystalsAlive += this.level.getEntitiesOfClass(EndCrystal.class, spike.getTopBoundingBox()).size();
/*     */     }
/*     */     
/* 420 */     LOGGER.debug("Found {} end crystals still alive", this.crystalsAlive);
/*     */   }
/*     */   
/*     */   public void setDragonKilled(EnderDragon dragon) {
/* 424 */     if (dragon.getUUID().equals(this.dragonUUID)) {
/* 425 */       this.dragonEvent.setProgress(0.0F);
/* 426 */       this.dragonEvent.setVisible(false);
/* 427 */       spawnExitPortal(true);
/* 428 */       spawnNewGateway();
/*     */       
/* 430 */       if (!this.previouslyKilled) {
/* 431 */         this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.origin)), Blocks.DRAGON_EGG.defaultBlockState());
/*     */       }
/*     */       
/* 434 */       this.previouslyKilled = true;
/* 435 */       this.dragonKilled = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   public void removeAllGateways() {
/* 445 */     this.gateways.clear();
/*     */   }
/*     */   
/*     */   private void spawnNewGateway() {
/* 449 */     if (this.gateways.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 453 */     int gateway = (Integer)this.gateways.remove(this.gateways.size() - 1);
/* 454 */     int x = Mth.floor(96.0D * Math.cos(2.0D * (-3.141592653589793D + 0.15707963267948966D * gateway)));
/* 455 */     int z = Mth.floor(96.0D * Math.sin(2.0D * (-3.141592653589793D + 0.15707963267948966D * gateway)));
/* 456 */     spawnNewGateway(new BlockPos(x, 75, z));
/*     */   }
/*     */   
/*     */   private void spawnNewGateway(BlockPos pos) {
/* 460 */     this.level.levelEvent(3000, pos, 0);
/* 461 */     this.level.registryAccess().lookup(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE)
/* 462 */       .flatMap(registry -> registry.get(EndFeatures.END_GATEWAY_DELAYED))
/* 463 */       .ifPresent(endGateway -> ((ConfiguredFeature)pos.value()).place((WorldGenLevel)this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnExitPortal(boolean activated) {
/* 468 */     EndPodiumFeature feature = new EndPodiumFeature(activated);
/*     */     
/* 470 */     if (this.portalLocation == null) {
/* 471 */       this.portalLocation = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.origin)).below();
/* 472 */       while (this.level.getBlockState(this.portalLocation).is(Blocks.BEDROCK) && this.portalLocation.getY() > 63) {
/* 473 */         this.portalLocation = this.portalLocation.below();
/*     */       }
/* 475 */       this.portalLocation = this.portalLocation.atY(Math.max(this.level.getMinY() + 1, this.portalLocation.getY()));
/*     */     } 
/*     */     
/* 478 */     if (feature.place((FeatureConfiguration)FeatureConfiguration.NONE, (WorldGenLevel)this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), this.portalLocation)) {
/*     */ 
/*     */       
/* 481 */       int chunkRadius = Mth.positiveCeilDiv(4, 16);
/* 482 */       (this.level.getChunkSource()).chunkMap.waitForLightBeforeSending(new ChunkPos(this.portalLocation), chunkRadius);
/*     */     } 
/*     */   }
/*     */   
/*     */   private EnderDragon createNewDragon() {
/* 487 */     this.level.getChunkAt(new BlockPos(this.origin.getX(), 128 + this.origin.getY(), this.origin.getZ()));
/* 488 */     EnderDragon dragon = (EnderDragon)net.minecraft.world.entity.EntityType.ENDER_DRAGON.create((net.minecraft.world.level.Level)this.level, net.minecraft.world.entity.EntitySpawnReason.EVENT);
/* 489 */     if (dragon != null) {
/* 490 */       dragon.setDragonFight(this);
/* 491 */       dragon.setFightOrigin(this.origin);
/* 492 */       dragon.getPhaseManager().setPhase(net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase.HOLDING_PATTERN);
/* 493 */       dragon.snapTo(this.origin.getX(), (128 + this.origin.getY()), this.origin.getZ(), this.level.random.nextFloat() * 360.0F, 0.0F);
/* 494 */       this.level.addFreshEntity((Entity)dragon);
/* 495 */       this.dragonUUID = dragon.getUUID();
/*     */     } 
/* 497 */     return dragon;
/*     */   }
/*     */   
/*     */   public void updateDragon(EnderDragon dragon) {
/* 501 */     if (dragon.getUUID().equals(this.dragonUUID)) {
/* 502 */       this.dragonEvent.setProgress(dragon.getHealth() / dragon.getMaxHealth());
/* 503 */       this.ticksSinceDragonSeen = 0;
/* 504 */       if (dragon.hasCustomName()) {
/* 505 */         this.dragonEvent.setName(dragon.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getCrystalsAlive() {
/* 511 */     return this.crystalsAlive;
/*     */   }
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal crystal, DamageSource source) {
/* 515 */     if (this.respawnStage != null && this.respawnCrystals.contains(crystal)) {
/* 516 */       LOGGER.debug("Aborting respawn sequence");
/* 517 */       this.respawnStage = null;
/* 518 */       this.respawnTime = 0;
/* 519 */       resetSpikeCrystals();
/* 520 */       spawnExitPortal(true);
/*     */     } else {
/* 522 */       updateCrystalCount();
/* 523 */       Entity dragon = this.level.getEntity(this.dragonUUID);
/* 524 */       if (dragon instanceof EnderDragon) { EnderDragon actuallyDragon = (EnderDragon)dragon;
/* 525 */         actuallyDragon.onCrystalDestroyed(this.level, crystal, crystal.blockPosition(), source); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasPreviouslyKilledDragon() {
/* 531 */     return this.previouslyKilled;
/*     */   }
/*     */   
/*     */   public void tryRespawn() {
/* 535 */     if (this.dragonKilled && this.respawnStage == null) {
/* 536 */       BlockPos location = this.portalLocation;
/* 537 */       if (location == null) {
/* 538 */         LOGGER.debug("Tried to respawn, but need to find the portal first.");
/* 539 */         BlockPattern.BlockPatternMatch match = findExitPortal();
/* 540 */         if (match == null) {
/* 541 */           LOGGER.debug("Couldn't find a portal, so we made one.");
/* 542 */           spawnExitPortal(true);
/*     */         } else {
/* 544 */           LOGGER.debug("Found the exit portal & saved its location for next time.");
/*     */         } 
/* 546 */         location = this.portalLocation;
/*     */       } 
/*     */       
/* 549 */       List<EndCrystal> crystals = com.google.common.collect.Lists.newArrayList();
/* 550 */       BlockPos center = location.above(1);
/* 551 */       for (Direction direction : (Iterable<Direction>)Direction.Plane.HORIZONTAL) {
/* 552 */         List<EndCrystal> found = this.level.getEntitiesOfClass(EndCrystal.class, new net.minecraft.world.phys.AABB(center.relative(direction, 2)));
/* 553 */         if (found.isEmpty()) {
/*     */           return;
/*     */         }
/* 556 */         crystals.addAll(found);
/*     */       } 
/*     */       
/* 559 */       LOGGER.debug("Found all crystals, respawning dragon.");
/* 560 */       respawnDragon(crystals);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void respawnDragon(List<EndCrystal> crystals) {
/* 565 */     if (this.dragonKilled && this.respawnStage == null) {
/* 566 */       BlockPattern.BlockPatternMatch portal = findExitPortal();
/* 567 */       while (portal != null) {
/* 568 */         for (int x = 0; x < this.exitPortalPattern.getWidth(); x++) {
/* 569 */           for (int y = 0; y < this.exitPortalPattern.getHeight(); y++) {
/* 570 */             for (int z = 0; z < this.exitPortalPattern.getDepth(); z++) {
/* 571 */               BlockInWorld block = portal.getBlock(x, y, z);
/* 572 */               if (block.getState().is(Blocks.BEDROCK) || block.getState().is(Blocks.END_PORTAL)) {
/* 573 */                 this.level.setBlockAndUpdate(block.getPos(), Blocks.END_STONE.defaultBlockState());
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 578 */         portal = findExitPortal();
/*     */       } 
/*     */       
/* 581 */       this.respawnStage = DragonRespawnAnimation.START;
/* 582 */       this.respawnTime = 0;
/* 583 */       spawnExitPortal(false);
/* 584 */       this.respawnCrystals = crystals;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetSpikeCrystals() {
/* 589 */     for (SpikeFeature.EndSpike spike : (Iterable<SpikeFeature.EndSpike>)SpikeFeature.getSpikesForLevel((WorldGenLevel)this.level)) {
/* 590 */       List<EndCrystal> spikeCrystals = this.level.getEntitiesOfClass(EndCrystal.class, spike.getTopBoundingBox());
/* 591 */       for (EndCrystal crystal : spikeCrystals) {
/* 592 */         crystal.setInvulnerable(false);
/* 593 */         crystal.setBeamTarget(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public UUID getDragonUUID() {
/* 599 */     return this.dragonUUID;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/dimension/end/EndDragonFight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */