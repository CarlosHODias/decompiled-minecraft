/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class LevelSummary
/*     */   implements Comparable<LevelSummary>
/*     */ {
/*  19 */   public static final Component PLAY_WORLD = (Component)Component.translatable("selectWorld.select");
/*     */   
/*     */   private final LevelSettings settings;
/*     */   private final LevelVersion levelVersion;
/*     */   private final String levelId;
/*     */   private final boolean requiresManualConversion;
/*     */   private final boolean locked;
/*     */   private final boolean experimental;
/*     */   private final Path icon;
/*     */   private Component info;
/*     */   
/*     */   public LevelSummary(LevelSettings settings, LevelVersion levelVersion, String levelId, boolean requiresManualConversion, boolean locked, boolean experimental, Path icon) {
/*  31 */     this.settings = settings;
/*  32 */     this.levelVersion = levelVersion;
/*  33 */     this.levelId = levelId;
/*  34 */     this.locked = locked;
/*  35 */     this.experimental = experimental;
/*  36 */     this.icon = icon;
/*  37 */     this.requiresManualConversion = requiresManualConversion;
/*     */   }
/*     */   
/*     */   public String getLevelId() {
/*  41 */     return this.levelId;
/*     */   }
/*     */   
/*     */   public String getLevelName() {
/*  45 */     return StringUtils.isEmpty(this.settings.levelName()) ? this.levelId : this.settings.levelName();
/*     */   }
/*     */   
/*     */   public Path getIcon() {
/*  49 */     return this.icon;
/*     */   }
/*     */   
/*     */   public boolean requiresManualConversion() {
/*  53 */     return this.requiresManualConversion;
/*     */   }
/*     */   
/*     */   public boolean isExperimental() {
/*  57 */     return this.experimental;
/*     */   }
/*     */   
/*     */   public long getLastPlayed() {
/*  61 */     return this.levelVersion.lastPlayed();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(LevelSummary rhs) {
/*  66 */     if (getLastPlayed() < rhs.getLastPlayed()) {
/*  67 */       return 1;
/*     */     }
/*  69 */     if (getLastPlayed() > rhs.getLastPlayed()) {
/*  70 */       return -1;
/*     */     }
/*  72 */     return this.levelId.compareTo(rhs.levelId);
/*     */   }
/*     */   
/*     */   public LevelSettings getSettings() {
/*  76 */     return this.settings;
/*     */   }
/*     */   
/*     */   public GameType getGameMode() {
/*  80 */     return this.settings.gameType();
/*     */   }
/*     */   
/*     */   public boolean isHardcore() {
/*  84 */     return this.settings.hardcore();
/*     */   }
/*     */   
/*     */   public boolean hasCommands() {
/*  88 */     return this.settings.allowCommands();
/*     */   }
/*     */   
/*     */   public MutableComponent getWorldVersionName() {
/*  92 */     if (StringUtil.isNullOrEmpty(this.levelVersion.minecraftVersionName())) {
/*  93 */       return Component.translatable("selectWorld.versionUnknown");
/*     */     }
/*  95 */     return Component.literal(this.levelVersion.minecraftVersionName());
/*     */   }
/*     */   
/*     */   public LevelVersion levelVersion() {
/*  99 */     return this.levelVersion;
/*     */   }
/*     */   
/*     */   public boolean shouldBackup() {
/* 103 */     return backupStatus().shouldBackup();
/*     */   }
/*     */   
/*     */   public boolean isDowngrade() {
/* 107 */     return (backupStatus() == BackupStatus.DOWNGRADE);
/*     */   }
/*     */   
/*     */   public BackupStatus backupStatus() {
/* 111 */     WorldVersion currentVersion = SharedConstants.getCurrentVersion();
/* 112 */     int currentVersionNumber = currentVersion.dataVersion().version();
/* 113 */     int levelVersionNumber = this.levelVersion.minecraftVersion().version();
/* 114 */     if (!currentVersion.stable() && levelVersionNumber < currentVersionNumber)
/* 115 */       return BackupStatus.UPGRADE_TO_SNAPSHOT; 
/* 116 */     if (levelVersionNumber > currentVersionNumber) {
/* 117 */       return BackupStatus.DOWNGRADE;
/*     */     }
/* 119 */     return BackupStatus.NONE;
/*     */   }
/*     */   
/*     */   public boolean isLocked() {
/* 123 */     return this.locked;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 127 */     if (isLocked() || requiresManualConversion()) {
/* 128 */       return true;
/*     */     }
/*     */     
/* 131 */     return !isCompatible();
/*     */   }
/*     */   
/*     */   public boolean isCompatible() {
/* 135 */     return SharedConstants.getCurrentVersion().dataVersion().isCompatible(this.levelVersion.minecraftVersion());
/*     */   }
/*     */   
/*     */   public Component getInfo() {
/* 139 */     if (this.info == null) {
/* 140 */       this.info = createInfo();
/*     */     }
/*     */     
/* 143 */     return this.info;
/*     */   }
/*     */   
/*     */   private Component createInfo() {
/* 147 */     if (isLocked()) {
/* 148 */       return (Component)Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
/*     */     }
/* 150 */     if (requiresManualConversion()) {
/* 151 */       return (Component)Component.translatable("selectWorld.conversion").withStyle(ChatFormatting.RED);
/*     */     }
/* 153 */     if (!isCompatible()) {
/* 154 */       return (Component)Component.translatable("selectWorld.incompatible.info", new Object[] { getWorldVersionName() }).withStyle(ChatFormatting.RED);
/*     */     }
/* 156 */     MutableComponent result = isHardcore() ? 
/* 157 */       Component.empty().append((Component)Component.translatable("gameMode.hardcore").withColor(-65536)) : 
/* 158 */       Component.translatable("gameMode." + getGameMode().getName());
/*     */     
/* 160 */     if (hasCommands()) {
/* 161 */       result.append(", ").append((Component)Component.translatable("selectWorld.commands"));
/*     */     }
/*     */     
/* 164 */     if (isExperimental()) {
/* 165 */       result.append(", ").append((Component)Component.translatable("selectWorld.experimental").withStyle(ChatFormatting.YELLOW));
/*     */     }
/*     */     
/* 168 */     MutableComponent worldVersionName = getWorldVersionName();
/* 169 */     MutableComponent decoratedVersionName = Component.literal(", ").append((Component)Component.translatable("selectWorld.version")).append(CommonComponents.SPACE);
/* 170 */     if (shouldBackup()) {
/* 171 */       decoratedVersionName.append((Component)worldVersionName.withStyle(isDowngrade() ? ChatFormatting.RED : ChatFormatting.ITALIC));
/*     */     } else {
/* 173 */       decoratedVersionName.append((Component)worldVersionName);
/*     */     } 
/* 175 */     result.append((Component)decoratedVersionName);
/* 176 */     return (Component)result;
/*     */   }
/*     */   
/*     */   public Component primaryActionMessage() {
/* 180 */     return PLAY_WORLD;
/*     */   }
/*     */   
/*     */   public boolean primaryActionActive() {
/* 184 */     return !isDisabled();
/*     */   }
/*     */   
/*     */   public boolean canUpload() {
/* 188 */     return (!requiresManualConversion() && !isLocked());
/*     */   }
/*     */   
/*     */   public boolean canEdit() {
/* 192 */     return !isDisabled();
/*     */   }
/*     */   
/*     */   public boolean canRecreate() {
/* 196 */     return !isDisabled();
/*     */   }
/*     */   
/*     */   public boolean canDelete() {
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   public enum BackupStatus {
/* 204 */     NONE(false, false, ""),
/* 205 */     DOWNGRADE(true, true, "downgrade"),
/* 206 */     UPGRADE_TO_SNAPSHOT(true, false, "snapshot");
/*     */     
/*     */     private final boolean shouldBackup;
/*     */     private final boolean severe;
/*     */     private final String translationKey;
/*     */     
/*     */     BackupStatus(boolean shouldBackup, boolean severe, String translationKey) {
/* 213 */       this.shouldBackup = shouldBackup;
/* 214 */       this.severe = severe;
/* 215 */       this.translationKey = translationKey;
/*     */     }
/*     */     
/*     */     public boolean shouldBackup() {
/* 219 */       return this.shouldBackup;
/*     */     }
/*     */     
/*     */     public boolean isSevere() {
/* 223 */       return this.severe;
/*     */     }
/*     */     
/*     */     public String getTranslationKey() {
/* 227 */       return this.translationKey;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SymlinkLevelSummary extends LevelSummary {
/* 232 */     private static final Component MORE_INFO_BUTTON = (Component)Component.translatable("symlink_warning.more_info");
/* 233 */     private static final Component INFO = (Component)Component.translatable("symlink_warning.title").withColor(-65536);
/*     */     
/*     */     public SymlinkLevelSummary(String levelId, Path icon) {
/* 236 */       super(null, null, levelId, false, false, false, icon);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLevelName() {
/* 241 */       return getLevelId();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getInfo() {
/* 246 */       return INFO;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getLastPlayed() {
/* 251 */       return -1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisabled() {
/* 256 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Component primaryActionMessage() {
/* 261 */       return MORE_INFO_BUTTON;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean primaryActionActive() {
/* 266 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUpload() {
/* 271 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canEdit() {
/* 276 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canRecreate() {
/* 281 */       return false;
/*     */     } }
/*     */   
/*     */   public static class CorruptedLevelSummary extends LevelSummary {
/*     */     static {
/* 286 */       INFO = (Component)Component.translatable("recover_world.warning").withStyle(style -> style.withColor(-65536));
/* 287 */     } private static final Component RECOVER = (Component)Component.translatable("recover_world.button"); private static final Component INFO;
/*     */     private final long lastPlayed;
/*     */     
/*     */     public CorruptedLevelSummary(String levelId, Path icon, long lastPlayed) {
/* 291 */       super(null, null, levelId, false, false, false, icon);
/* 292 */       this.lastPlayed = lastPlayed;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLevelName() {
/* 297 */       return getLevelId();
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getInfo() {
/* 302 */       return INFO;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getLastPlayed() {
/* 307 */       return this.lastPlayed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisabled() {
/* 312 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Component primaryActionMessage() {
/* 317 */       return RECOVER;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean primaryActionActive() {
/* 322 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUpload() {
/* 327 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canEdit() {
/* 332 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canRecreate() {
/* 337 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/LevelSummary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */