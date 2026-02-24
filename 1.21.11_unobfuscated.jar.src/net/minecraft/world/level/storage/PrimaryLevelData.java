/*     */ package net.minecraft.world.level.storage;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ public class PrimaryLevelData implements ServerLevelData, WorldData {
/*     */   public static final String LEVEL_NAME = "LevelName";
/*     */   protected static final String PLAYER = "Player";
/*     */   protected static final String WORLD_GEN_SETTINGS = "WorldGenSettings";
/*     */   private LevelSettings settings;
/*     */   private final WorldOptions worldOptions;
/*     */   private final SpecialWorldProperty specialWorldProperty;
/*     */   private final Lifecycle worldGenSettingsLifecycle;
/*     */   private LevelData.RespawnData respawnData;
/*     */   private long gameTime;
/*     */   private long dayTime;
/*     */   private final CompoundTag loadedPlayerTag;
/*     */   private final int version;
/*     */   private int clearWeatherTime;
/*     */   private boolean raining;
/*     */   private int rainTime;
/*  43 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); private boolean thundering; private int thunderTime;
/*     */   private boolean initialized;
/*     */   private boolean difficultyLocked;
/*     */   @Deprecated
/*     */   private Optional<WorldBorder.Settings> legacyWorldBorderSettings;
/*     */   private EndDragonFight.Data endDragonFightData;
/*     */   private CompoundTag customBossEvents;
/*     */   private int wanderingTraderSpawnDelay;
/*     */   private int wanderingTraderSpawnChance;
/*     */   private UUID wanderingTraderId;
/*     */   private final Set<String> knownServerBrands;
/*     */   private boolean wasModded;
/*     */   private final Set<String> removedFeatureFlags;
/*     */   private final TimerQueue<MinecraftServer> scheduledEvents;
/*     */   
/*     */   @Deprecated
/*  59 */   public enum SpecialWorldProperty { NONE,
/*  60 */     FLAT,
/*  61 */     DEBUG; }
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
/*     */   private PrimaryLevelData(CompoundTag loadedPlayerTag, boolean wasModded, LevelData.RespawnData respawnData, long gameTime, long dayTime, int version, int clearWeatherTime, int rainTime, boolean raining, int thunderTime, boolean thundering, boolean initialized, boolean difficultyLocked, Optional<WorldBorder.Settings> legacyWorldBorderSettings, int wanderingTraderSpawnDelay, int wanderingTraderSpawnChance, UUID wanderingTraderId, Set<String> knownServerBrands, Set<String> removedFeatureFlags, TimerQueue<MinecraftServer> scheduledEvents, CompoundTag customBossEvents, EndDragonFight.Data endDragonFightData, LevelSettings settings, WorldOptions worldOptions, SpecialWorldProperty specialWorldProperty, Lifecycle worldGenSettingsLifecycle) {
/* 129 */     this.wasModded = wasModded;
/* 130 */     this.respawnData = respawnData;
/* 131 */     this.gameTime = gameTime;
/* 132 */     this.dayTime = dayTime;
/* 133 */     this.version = version;
/* 134 */     this.clearWeatherTime = clearWeatherTime;
/* 135 */     this.rainTime = rainTime;
/* 136 */     this.raining = raining;
/* 137 */     this.thunderTime = thunderTime;
/* 138 */     this.thundering = thundering;
/* 139 */     this.initialized = initialized;
/* 140 */     this.difficultyLocked = difficultyLocked;
/* 141 */     this.legacyWorldBorderSettings = legacyWorldBorderSettings;
/* 142 */     this.wanderingTraderSpawnDelay = wanderingTraderSpawnDelay;
/* 143 */     this.wanderingTraderSpawnChance = wanderingTraderSpawnChance;
/* 144 */     this.wanderingTraderId = wanderingTraderId;
/* 145 */     this.knownServerBrands = knownServerBrands;
/* 146 */     this.removedFeatureFlags = removedFeatureFlags;
/* 147 */     this.loadedPlayerTag = loadedPlayerTag;
/* 148 */     this.scheduledEvents = scheduledEvents;
/* 149 */     this.customBossEvents = customBossEvents;
/* 150 */     this.endDragonFightData = endDragonFightData;
/* 151 */     this.settings = settings;
/* 152 */     this.worldOptions = worldOptions;
/* 153 */     this.specialWorldProperty = specialWorldProperty;
/* 154 */     this.worldGenSettingsLifecycle = worldGenSettingsLifecycle;
/*     */   }
/*     */   
/*     */   public PrimaryLevelData(LevelSettings levelSettings, WorldOptions worldOptions, SpecialWorldProperty specialWorldProperty, Lifecycle lifecycle) {
/* 158 */     this(null, false, LevelData.RespawnData.DEFAULT, 0L, 0L, 19133, 0, 0, false, 0, false, false, false, 
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
/* 172 */         Optional.empty(), 0, 0, null, 
/*     */ 
/*     */ 
/*     */         
/* 176 */         com.google.common.collect.Sets.newLinkedHashSet(), new java.util.HashSet<>(), new TimerQueue(net.minecraft.world.level.timers.TimerCallbacks.SERVER_CALLBACKS), null, EndDragonFight.Data.DEFAULT, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         levelSettings.copy(), worldOptions, specialWorldProperty, lifecycle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> PrimaryLevelData parse(Dynamic<T> input, LevelSettings settings, SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle) {
/* 189 */     long gameTime = input.get("Time").asLong(0L);
/*     */ 
/*     */     
/* 192 */     Objects.requireNonNull(CompoundTag.CODEC);
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
/* 213 */     Objects.requireNonNull(LOGGER); return new PrimaryLevelData(input.get("Player").flatMap(CompoundTag.CODEC::parse).result().orElse(null), input.get("WasModded").asBoolean(false), input.get("spawn").read((Decoder)LevelData.RespawnData.CODEC).result().orElse(LevelData.RespawnData.DEFAULT), gameTime, input.get("DayTime").asLong(gameTime), LevelVersion.parse(input).levelDataVersion(), input.get("clearWeatherTime").asInt(0), input.get("rainTime").asInt(0), input.get("raining").asBoolean(false), input.get("thunderTime").asInt(0), input.get("thundering").asBoolean(false), input.get("initialized").asBoolean(true), input.get("DifficultyLocked").asBoolean(false), WorldBorder.Settings.CODEC.parse(input.get("world_border").orElseEmptyMap()).result(), input.get("WanderingTraderSpawnDelay").asInt(0), input.get("WanderingTraderSpawnChance").asInt(0), input.get("WanderingTraderId").read((Decoder)net.minecraft.core.UUIDUtil.CODEC).result().orElse(null), (Set<String>)input.get("ServerBrands").asStream().flatMap(b -> b.asString().result().stream()).collect(java.util.stream.Collectors.toCollection(com.google.common.collect.Sets::newLinkedHashSet)), (Set<String>)input.get("removed_features").asStream().flatMap(b -> b.asString().result().stream()).collect(java.util.stream.Collectors.toSet()), new TimerQueue(net.minecraft.world.level.timers.TimerCallbacks.SERVER_CALLBACKS, input.get("ScheduledEvents").asStream()), (CompoundTag)input.get("CustomBossEvents").orElseEmptyMap().getValue(), input.get("DragonFight").read((Decoder)EndDragonFight.Data.CODEC).resultOrPartial(LOGGER::error).orElse(EndDragonFight.Data.DEFAULT), settings, worldOptions, specialWorldProperty, worldGenSettingsLifecycle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag createTag(RegistryAccess registryAccess, CompoundTag playerData) {
/* 223 */     if (playerData == null) {
/* 224 */       playerData = this.loadedPlayerTag;
/*     */     }
/* 226 */     CompoundTag tag = new CompoundTag();
/* 227 */     setTagData(registryAccess, tag, playerData);
/* 228 */     return tag;
/*     */   }
/*     */   
/*     */   private void setTagData(RegistryAccess registryAccess, CompoundTag tag, CompoundTag playerTag) {
/* 232 */     tag.put("ServerBrands", (Tag)stringCollectionToTag(this.knownServerBrands));
/* 233 */     tag.putBoolean("WasModded", this.wasModded);
/*     */     
/* 235 */     if (!this.removedFeatureFlags.isEmpty()) {
/* 236 */       tag.put("removed_features", (Tag)stringCollectionToTag(this.removedFeatureFlags));
/*     */     }
/*     */     
/* 239 */     CompoundTag worldVersion = new CompoundTag();
/* 240 */     worldVersion.putString("Name", SharedConstants.getCurrentVersion().name());
/* 241 */     worldVersion.putInt("Id", SharedConstants.getCurrentVersion().dataVersion().version());
/* 242 */     worldVersion.putBoolean("Snapshot", !SharedConstants.getCurrentVersion().stable());
/* 243 */     worldVersion.putString("Series", SharedConstants.getCurrentVersion().dataVersion().series());
/* 244 */     tag.put("Version", (Tag)worldVersion);
/*     */     
/* 246 */     net.minecraft.nbt.NbtUtils.addCurrentDataVersion(tag);
/*     */     
/* 248 */     net.minecraft.resources.RegistryOps registryOps = registryAccess.createSerializationContext((com.mojang.serialization.DynamicOps)net.minecraft.nbt.NbtOps.INSTANCE);
/*     */ 
/*     */     
/* 251 */     Objects.requireNonNull(LOGGER); net.minecraft.world.level.levelgen.WorldGenSettings.encode((com.mojang.serialization.DynamicOps)registryOps, this.worldOptions, registryAccess).resultOrPartial(Util.prefix("WorldGenSettings: ", LOGGER::error))
/* 252 */       .ifPresent(s -> tag.put("WorldGenSettings", s));
/*     */     
/* 254 */     tag.putInt("GameType", this.settings.gameType().getId());
/* 255 */     tag.store("spawn", LevelData.RespawnData.CODEC, this.respawnData);
/* 256 */     tag.putLong("Time", this.gameTime);
/* 257 */     tag.putLong("DayTime", this.dayTime);
/* 258 */     tag.putLong("LastPlayed", Util.getEpochMillis());
/* 259 */     tag.putString("LevelName", this.settings.levelName());
/* 260 */     tag.putInt("version", 19133);
/* 261 */     tag.putInt("clearWeatherTime", this.clearWeatherTime);
/* 262 */     tag.putInt("rainTime", this.rainTime);
/* 263 */     tag.putBoolean("raining", this.raining);
/* 264 */     tag.putInt("thunderTime", this.thunderTime);
/* 265 */     tag.putBoolean("thundering", this.thundering);
/* 266 */     tag.putBoolean("hardcore", this.settings.hardcore());
/* 267 */     tag.putBoolean("allowCommands", this.settings.allowCommands());
/* 268 */     tag.putBoolean("initialized", this.initialized);
/* 269 */     this.legacyWorldBorderSettings.ifPresent(settings -> tag.store("world_border", WorldBorder.Settings.CODEC, settings));
/* 270 */     tag.putByte("Difficulty", (byte)this.settings.difficulty().getId());
/* 271 */     tag.putBoolean("DifficultyLocked", this.difficultyLocked);
/* 272 */     tag.store("game_rules", net.minecraft.world.level.gamerules.GameRules.codec(enabledFeatures()), this.settings.gameRules());
/*     */     
/* 274 */     tag.store("DragonFight", EndDragonFight.Data.CODEC, this.endDragonFightData);
/*     */     
/* 276 */     if (playerTag != null) {
/* 277 */       tag.put("Player", (Tag)playerTag);
/*     */     }
/*     */     
/* 280 */     tag.store(WorldDataConfiguration.MAP_CODEC, this.settings.getDataConfiguration());
/*     */     
/* 282 */     if (this.customBossEvents != null) {
/* 283 */       tag.put("CustomBossEvents", (Tag)this.customBossEvents);
/*     */     }
/*     */     
/* 286 */     tag.put("ScheduledEvents", (Tag)this.scheduledEvents.store());
/*     */     
/* 288 */     tag.putInt("WanderingTraderSpawnDelay", this.wanderingTraderSpawnDelay);
/* 289 */     tag.putInt("WanderingTraderSpawnChance", this.wanderingTraderSpawnChance);
/* 290 */     tag.storeNullable("WanderingTraderId", net.minecraft.core.UUIDUtil.CODEC, this.wanderingTraderId);
/*     */   }
/*     */   
/*     */   private static ListTag stringCollectionToTag(Set<String> values) {
/* 294 */     ListTag result = new ListTag();
/* 295 */     Objects.requireNonNull(result); values.stream().map(net.minecraft.nbt.StringTag::valueOf).forEach(result::add);
/* 296 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelData.RespawnData getRespawnData() {
/* 301 */     return this.respawnData;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getGameTime() {
/* 306 */     return this.gameTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDayTime() {
/* 311 */     return this.dayTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getLoadedPlayerTag() {
/* 316 */     return this.loadedPlayerTag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameTime(long time) {
/* 321 */     this.gameTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDayTime(long time) {
/* 326 */     this.dayTime = time;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawn(LevelData.RespawnData respawnData) {
/* 331 */     this.respawnData = respawnData;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevelName() {
/* 336 */     return this.settings.levelName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 341 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getClearWeatherTime() {
/* 346 */     return this.clearWeatherTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClearWeatherTime(int clearWeatherTime) {
/* 351 */     this.clearWeatherTime = clearWeatherTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isThundering() {
/* 356 */     return this.thundering;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThundering(boolean thundering) {
/* 361 */     this.thundering = thundering;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThunderTime() {
/* 366 */     return this.thunderTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThunderTime(int thunderTime) {
/* 371 */     this.thunderTime = thunderTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRaining() {
/* 376 */     return this.raining;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRaining(boolean raining) {
/* 381 */     this.raining = raining;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRainTime() {
/* 386 */     return this.rainTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRainTime(int rainTime) {
/* 391 */     this.rainTime = rainTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameType() {
/* 396 */     return this.settings.gameType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(GameType gameType) {
/* 401 */     this.settings = this.settings.withGameType(gameType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHardcore() {
/* 406 */     return this.settings.hardcore();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowCommands() {
/* 411 */     return this.settings.allowCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 416 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInitialized(boolean initialized) {
/* 421 */     this.initialized = initialized;
/*     */   }
/*     */ 
/*     */   
/*     */   public net.minecraft.world.level.gamerules.GameRules getGameRules() {
/* 426 */     return this.settings.gameRules();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<WorldBorder.Settings> getLegacyWorldBorderSettings() {
/* 431 */     return this.legacyWorldBorderSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLegacyWorldBorderSettings(Optional<WorldBorder.Settings> settings) {
/* 436 */     this.legacyWorldBorderSettings = settings;
/*     */   }
/*     */ 
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 441 */     return this.settings.difficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficulty(Difficulty difficulty) {
/* 446 */     this.settings = this.settings.withDifficulty(difficulty);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 451 */     return this.difficultyLocked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficultyLocked(boolean difficultyLocked) {
/* 456 */     this.difficultyLocked = difficultyLocked;
/*     */   }
/*     */ 
/*     */   
/*     */   public TimerQueue<MinecraftServer> getScheduledEvents() {
/* 461 */     return this.scheduledEvents;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category, net.minecraft.world.level.LevelHeightAccessor levelHeightAccessor) {
/* 466 */     super.fillCrashReportCategory(category, levelHeightAccessor);
/* 467 */     fillCrashReportCategory(category);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldOptions worldGenOptions() {
/* 472 */     return this.worldOptions;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlatWorld() {
/* 477 */     return (this.specialWorldProperty == SpecialWorldProperty.FLAT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugWorld() {
/* 482 */     return (this.specialWorldProperty == SpecialWorldProperty.DEBUG);
/*     */   }
/*     */ 
/*     */   
/*     */   public Lifecycle worldGenSettingsLifecycle() {
/* 487 */     return this.worldGenSettingsLifecycle;
/*     */   }
/*     */ 
/*     */   
/*     */   public EndDragonFight.Data endDragonFightData() {
/* 492 */     return this.endDragonFightData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndDragonFightData(EndDragonFight.Data data) {
/* 497 */     this.endDragonFightData = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldDataConfiguration getDataConfiguration() {
/* 502 */     return this.settings.getDataConfiguration();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataConfiguration(WorldDataConfiguration dataConfiguration) {
/* 507 */     this.settings = this.settings.withDataConfiguration(dataConfiguration);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getCustomBossEvents() {
/* 512 */     return this.customBossEvents;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomBossEvents(CompoundTag customBossEvents) {
/* 517 */     this.customBossEvents = customBossEvents;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnDelay() {
/* 522 */     return this.wanderingTraderSpawnDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnDelay(int wanderingTraderSpawnDelay) {
/* 527 */     this.wanderingTraderSpawnDelay = wanderingTraderSpawnDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnChance() {
/* 532 */     return this.wanderingTraderSpawnChance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnChance(int wanderingTraderSpawnChance) {
/* 537 */     this.wanderingTraderSpawnChance = wanderingTraderSpawnChance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UUID getWanderingTraderId() {
/* 543 */     return this.wanderingTraderId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWanderingTraderId(UUID wanderingTraderId) {
/* 548 */     this.wanderingTraderId = wanderingTraderId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setModdedInfo(String serverBrand, boolean isModded) {
/* 553 */     this.knownServerBrands.add(serverBrand);
/* 554 */     this.wasModded |= isModded;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasModded() {
/* 559 */     return this.wasModded;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getKnownServerBrands() {
/* 564 */     return (Set<String>)com.google.common.collect.ImmutableSet.copyOf(this.knownServerBrands);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getRemovedFeatureFlags() {
/* 569 */     return Set.copyOf(this.removedFeatureFlags);
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerLevelData overworldData() {
/* 574 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelSettings getLevelSettings() {
/* 579 */     return this.settings.copy();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/PrimaryLevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */