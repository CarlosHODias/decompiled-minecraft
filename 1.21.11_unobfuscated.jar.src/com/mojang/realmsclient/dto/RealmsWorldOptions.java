/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ 
/*     */ public class RealmsWorldOptions
/*     */   extends ValueObject implements ReflectionBasedSerialization {
/*     */   @SerializedName("spawnProtection")
/*  13 */   public int spawnProtection = 0;
/*     */   
/*     */   @SerializedName("forceGameMode")
/*     */   public boolean forceGameMode = false;
/*     */   
/*     */   @SerializedName("difficulty")
/*  19 */   public int difficulty = 2;
/*     */   
/*     */   @SerializedName("gameMode")
/*  22 */   public int gameMode = 0;
/*     */   
/*     */   @SerializedName("slotName")
/*  25 */   private String slotName = "";
/*     */   
/*     */   @SerializedName("version")
/*  28 */   public String version = "";
/*     */   
/*     */   @SerializedName("compatibility")
/*  31 */   public RealmsServer.Compatibility compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
/*     */   
/*     */   @SerializedName("worldTemplateId")
/*  34 */   public long templateId = -1L;
/*     */   
/*     */   @SerializedName("worldTemplateImage")
/*  37 */   public String templateImage = null;
/*     */   
/*     */   @Exclude
/*     */   public boolean empty;
/*     */ 
/*     */   
/*     */   private RealmsWorldOptions() {}
/*     */ 
/*     */   
/*     */   public RealmsWorldOptions(int spawnProtection, int difficulty, int gameMode, boolean forceGameMode, String slotName, String version, RealmsServer.Compatibility compatibility) {
/*  47 */     this.spawnProtection = spawnProtection;
/*  48 */     this.difficulty = difficulty;
/*  49 */     this.gameMode = gameMode;
/*  50 */     this.forceGameMode = forceGameMode;
/*  51 */     this.slotName = slotName;
/*  52 */     this.version = version;
/*  53 */     this.compatibility = compatibility;
/*     */   }
/*     */   
/*     */   public static RealmsWorldOptions createDefaults() {
/*  57 */     return new RealmsWorldOptions();
/*     */   }
/*     */   
/*     */   public static RealmsWorldOptions createDefaultsWith(GameType gameMode, Difficulty difficulty, boolean hardcore, String version, String worldName) {
/*  61 */     RealmsWorldOptions options = createDefaults();
/*  62 */     options.difficulty = difficulty.getId();
/*  63 */     options.gameMode = gameMode.getId();
/*  64 */     options.slotName = worldName;
/*  65 */     options.version = version;
/*  66 */     return options;
/*     */   }
/*     */   
/*     */   public static RealmsWorldOptions createFromSettings(LevelSettings settings, String worldVersion) {
/*  70 */     return createDefaultsWith(settings.gameType(), settings.difficulty(), settings.hardcore(), worldVersion, settings.levelName());
/*     */   }
/*     */   
/*     */   public static RealmsWorldOptions createEmptyDefaults() {
/*  74 */     RealmsWorldOptions options = createDefaults();
/*  75 */     options.setEmpty(true);
/*  76 */     return options;
/*     */   }
/*     */   
/*     */   public void setEmpty(boolean empty) {
/*  80 */     this.empty = empty;
/*     */   }
/*     */   
/*     */   public static RealmsWorldOptions parse(GuardedSerializer gson, String json) {
/*  84 */     RealmsWorldOptions options = gson.<RealmsWorldOptions>fromJson(json, RealmsWorldOptions.class);
/*  85 */     if (options == null) {
/*  86 */       return createDefaults();
/*     */     }
/*  88 */     finalize(options);
/*  89 */     return options;
/*     */   }
/*     */   
/*     */   private static void finalize(RealmsWorldOptions options) {
/*  93 */     if (options.slotName == null) {
/*  94 */       options.slotName = "";
/*     */     }
/*  96 */     if (options.version == null) {
/*  97 */       options.version = "";
/*     */     }
/*  99 */     if (options.compatibility == null) {
/* 100 */       options.compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSlotName(int i) {
/* 105 */     if (StringUtil.isBlank(this.slotName)) {
/* 106 */       if (this.empty) {
/* 107 */         return I18n.get("mco.configure.world.slot.empty", new Object[0]);
/*     */       }
/*     */       
/* 110 */       return getDefaultSlotName(i);
/*     */     } 
/* 112 */     return this.slotName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultSlotName(int i) {
/* 117 */     return I18n.get("mco.configure.world.slot", new Object[] { i });
/*     */   }
/*     */   
/*     */   public RealmsWorldOptions copy() {
/* 121 */     return new RealmsWorldOptions(this.spawnProtection, this.difficulty, this.gameMode, this.forceGameMode, this.slotName, this.version, this.compatibility);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsWorldOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */