/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.DisplayData;
/*     */ import java.io.File;
/*     */ import java.net.Proxy;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.client.resources.IndexedAssetSource;
/*     */ import net.minecraft.util.StringUtil;
/*     */ 
/*     */ 
/*     */ public class GameConfig
/*     */ {
/*     */   public final UserData user;
/*     */   public final DisplayData display;
/*     */   public final FolderData location;
/*     */   public final GameData game;
/*     */   public final QuickPlayData quickPlay;
/*     */   
/*     */   public GameConfig(UserData userData, DisplayData displayData, FolderData folderData, GameData gameData, QuickPlayData quickPlayData) {
/*  21 */     this.user = userData;
/*  22 */     this.display = displayData;
/*  23 */     this.location = folderData;
/*  24 */     this.game = gameData;
/*  25 */     this.quickPlay = quickPlayData;
/*     */   }
/*     */   
/*     */   public static class GameData {
/*     */     public final boolean demo;
/*     */     public final String launchVersion;
/*     */     public final String versionType;
/*     */     public final boolean disableMultiplayer;
/*     */     public final boolean disableChat;
/*     */     public final boolean captureTracyImages;
/*     */     public final boolean renderDebugLabels;
/*     */     public final boolean offlineDeveloperMode;
/*     */     
/*     */     public GameData(boolean demo, String launchVersion, String versionType, boolean disableMultiplayer, boolean disableChat, boolean captureTracyImages, boolean renderDebugLabels, boolean offlineDeveloperMode) {
/*  39 */       this.demo = demo;
/*  40 */       this.launchVersion = launchVersion;
/*  41 */       this.versionType = versionType;
/*  42 */       this.disableMultiplayer = disableMultiplayer;
/*  43 */       this.disableChat = disableChat;
/*  44 */       this.captureTracyImages = captureTracyImages;
/*  45 */       this.renderDebugLabels = renderDebugLabels;
/*  46 */       this.offlineDeveloperMode = offlineDeveloperMode;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UserData {
/*     */     public final User user;
/*     */     public final Proxy proxy;
/*     */     
/*     */     public UserData(User user, Proxy proxy) {
/*  55 */       this.user = user;
/*  56 */       this.proxy = proxy;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FolderData {
/*     */     public final File gameDirectory;
/*     */     public final File resourcePackDirectory;
/*     */     public final File assetDirectory;
/*     */     public final String assetIndex;
/*     */     
/*     */     public FolderData(File gameDirectory, File resourcePackDirectory, File assetDirectory, String assetIndex) {
/*  67 */       this.gameDirectory = gameDirectory;
/*  68 */       this.resourcePackDirectory = resourcePackDirectory;
/*  69 */       this.assetDirectory = assetDirectory;
/*  70 */       this.assetIndex = assetIndex;
/*     */     }
/*     */     
/*     */     public Path getExternalAssetSource() {
/*  74 */       return (this.assetIndex == null) ? this.assetDirectory.toPath() : IndexedAssetSource.createIndexFs(this.assetDirectory.toPath(), this.assetIndex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface QuickPlayVariant {
/*  79 */     public static final QuickPlayVariant DISABLED = new GameConfig.QuickPlayDisabled();
/*     */     boolean isEnabled(); }
/*     */   public static final class QuickPlaySinglePlayerData extends Record implements QuickPlayVariant { private final String worldId;
/*     */     
/*  83 */     public QuickPlaySinglePlayerData(String worldId) { this.worldId = worldId; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  83 */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData; } public String worldId() { return this.worldId; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #83	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public boolean isEnabled() {
/*  86 */       return true;
/*     */     } }
/*     */   public static final class QuickPlayMultiplayerData extends Record implements QuickPlayVariant { private final String serverAddress;
/*     */     
/*  90 */     public QuickPlayMultiplayerData(String serverAddress) { this.serverAddress = serverAddress; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*  90 */       //   0	8	1	o	Ljava/lang/Object; } public String serverAddress() { return this.serverAddress; }
/*     */     
/*     */     public boolean isEnabled() {
/*  93 */       return !StringUtil.isBlank(this.serverAddress);
/*     */     } }
/*     */   public static final class QuickPlayRealmsData extends Record implements QuickPlayVariant { private final String realmId;
/*     */     
/*  97 */     public QuickPlayRealmsData(String realmId) { this.realmId = realmId; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*  97 */       //   0	8	1	o	Ljava/lang/Object; } public String realmId() { return this.realmId; }
/*     */ 
/*     */     
/* 100 */     public boolean isEnabled() { return !StringUtil.isBlank(this.realmId); } } public static final class QuickPlayDisabled extends Record implements QuickPlayVariant { public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */     } public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayDisabled;
/*     */     } public boolean isEnabled() {
/* 107 */       return false;
/*     */     } }
/*     */   public static final class QuickPlayData extends Record { private final String logPath; private final GameConfig.QuickPlayVariant variant;
/*     */     
/* 111 */     public QuickPlayData(String logPath, GameConfig.QuickPlayVariant variant) { this.logPath = logPath; this.variant = variant; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/* 111 */       //   0	8	1	o	Ljava/lang/Object; } public String logPath() { return this.logPath; } public GameConfig.QuickPlayVariant variant() { return this.variant; } public boolean isEnabled() {
/* 112 */       return this.variant.isEnabled();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/main/GameConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */