/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DerivedLevelData
/*     */   implements ServerLevelData
/*     */ {
/*     */   private final WorldData worldData;
/*     */   private final ServerLevelData wrapped;
/*     */   
/*     */   public DerivedLevelData(WorldData worldData, ServerLevelData wrapped) {
/*  26 */     this.worldData = worldData;
/*  27 */     this.wrapped = wrapped;
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelData.RespawnData getRespawnData() {
/*  32 */     return this.wrapped.getRespawnData();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getGameTime() {
/*  37 */     return this.wrapped.getGameTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDayTime() {
/*  42 */     return this.wrapped.getDayTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevelName() {
/*  47 */     return this.worldData.getLevelName();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getClearWeatherTime() {
/*  52 */     return this.wrapped.getClearWeatherTime();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClearWeatherTime(int clearWeatherTime) {}
/*     */ 
/*     */   
/*     */   public boolean isThundering() {
/*  61 */     return this.wrapped.isThundering();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThunderTime() {
/*  66 */     return this.wrapped.getThunderTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRaining() {
/*  71 */     return this.wrapped.isRaining();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRainTime() {
/*  76 */     return this.wrapped.getRainTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public GameType getGameType() {
/*  81 */     return this.worldData.getGameType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGameTime(long time) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDayTime(long time) {}
/*     */ 
/*     */   
/*     */   public void setSpawn(LevelData.RespawnData respawnData) {
/*  94 */     this.wrapped.setSpawn(respawnData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThundering(boolean thundering) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThunderTime(int thunderTime) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRaining(boolean raining) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainTime(int rainTime) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGameType(GameType gameType) {}
/*     */ 
/*     */   
/*     */   public boolean isHardcore() {
/* 119 */     return this.worldData.isHardcore();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllowCommands() {
/* 124 */     return this.worldData.isAllowCommands();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInitialized() {
/* 129 */     return this.wrapped.isInitialized();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialized(boolean initialized) {}
/*     */ 
/*     */   
/*     */   public GameRules getGameRules() {
/* 138 */     return this.worldData.getGameRules();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<WorldBorder.Settings> getLegacyWorldBorderSettings() {
/* 143 */     return this.wrapped.getLegacyWorldBorderSettings();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLegacyWorldBorderSettings(Optional<WorldBorder.Settings> settings) {}
/*     */ 
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 152 */     return this.worldData.getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 157 */     return this.worldData.isDifficultyLocked();
/*     */   }
/*     */ 
/*     */   
/*     */   public TimerQueue<MinecraftServer> getScheduledEvents() {
/* 162 */     return this.wrapped.getScheduledEvents();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnDelay() {
/* 167 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnDelay(int wanderingTraderSpawnDelay) {}
/*     */ 
/*     */   
/*     */   public int getWanderingTraderSpawnChance() {
/* 176 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnChance(int wanderingTraderSpawnChance) {}
/*     */ 
/*     */   
/*     */   public UUID getWanderingTraderId() {
/* 185 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderId(UUID wanderingTraderId) {}
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 194 */     category.setDetail("Derived", true);
/* 195 */     this.wrapped.fillCrashReportCategory(category, levelHeightAccessor);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/DerivedLevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */