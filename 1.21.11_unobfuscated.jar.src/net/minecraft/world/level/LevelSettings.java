/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class LevelSettings {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final String levelName;
/*    */   private final GameType gameType;
/*    */   private final boolean hardcore;
/*    */   private final Difficulty difficulty;
/*    */   private final boolean allowCommands;
/*    */   private final GameRules gameRules;
/*    */   private final WorldDataConfiguration dataConfiguration;
/*    */   
/*    */   public LevelSettings(String levelName, GameType gameType, boolean hardcore, Difficulty difficulty, boolean allowCommands, GameRules gameRules, WorldDataConfiguration dataConfiguration) {
/* 21 */     this.levelName = levelName;
/* 22 */     this.gameType = gameType;
/* 23 */     this.hardcore = hardcore;
/* 24 */     this.difficulty = difficulty;
/* 25 */     this.allowCommands = allowCommands;
/* 26 */     this.gameRules = gameRules;
/* 27 */     this.dataConfiguration = dataConfiguration;
/*    */   }
/*    */   
/*    */   public static LevelSettings parse(Dynamic<?> input, WorldDataConfiguration loadConfig) {
/* 31 */     GameType gameType = GameType.byId(input.get("GameType").asInt(0));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     Objects.requireNonNull(LOGGER); return new LevelSettings(input.get("LevelName").asString(""), gameType, input.get("hardcore").asBoolean(false), input.get("Difficulty").asNumber().map(n -> Difficulty.byId(n.byteValue())).result().orElse(Difficulty.NORMAL), input.get("allowCommands").asBoolean((gameType == GameType.CREATIVE)), GameRules.codec(loadConfig.enabledFeatures()).parse(input.get("game_rules").orElseEmptyMap()).resultOrPartial(LOGGER::warn).orElseThrow(), loadConfig);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String levelName() {
/* 43 */     return this.levelName;
/*    */   }
/*    */   
/*    */   public GameType gameType() {
/* 47 */     return this.gameType;
/*    */   }
/*    */   
/*    */   public boolean hardcore() {
/* 51 */     return this.hardcore;
/*    */   }
/*    */   
/*    */   public Difficulty difficulty() {
/* 55 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   public boolean allowCommands() {
/* 59 */     return this.allowCommands;
/*    */   }
/*    */   
/*    */   public GameRules gameRules() {
/* 63 */     return this.gameRules;
/*    */   }
/*    */   
/*    */   public WorldDataConfiguration getDataConfiguration() {
/* 67 */     return this.dataConfiguration;
/*    */   }
/*    */   
/*    */   public LevelSettings withGameType(GameType gameType) {
/* 71 */     return new LevelSettings(this.levelName, gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDifficulty(Difficulty difficulty) {
/* 75 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, difficulty, this.allowCommands, this.gameRules, this.dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings withDataConfiguration(WorldDataConfiguration dataConfiguration) {
/* 79 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, dataConfiguration);
/*    */   }
/*    */   
/*    */   public LevelSettings copy() {
/* 83 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules.copy(this.dataConfiguration.enabledFeatures()), this.dataConfiguration);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/LevelSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */