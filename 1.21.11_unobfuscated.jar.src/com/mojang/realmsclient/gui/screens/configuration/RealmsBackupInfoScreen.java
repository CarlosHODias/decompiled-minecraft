/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.realmsclient.dto.Backup;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class RealmsBackupInfoScreen extends net.minecraft.realms.RealmsScreen {
/*  19 */   private static final Component TITLE = (Component)Component.translatable("mco.backup.info.title");
/*  20 */   private static final Component UNKNOWN = (Component)Component.translatable("mco.backup.unknown");
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final Backup backup;
/*  25 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private BackupInfoList backupInfoList;
/*     */   
/*     */   public RealmsBackupInfoScreen(Screen lastScreen, Backup backup) {
/*  30 */     super(TITLE);
/*  31 */     this.lastScreen = lastScreen;
/*  32 */     this.backup = backup;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  37 */     this.layout.addTitleHeader(TITLE, this.font);
/*  38 */     this.backupInfoList = (BackupInfoList)this.layout.addToContents((LayoutElement)new BackupInfoList(this.minecraft));
/*  39 */     this.layout.addToFooter((LayoutElement)Button.builder(net.minecraft.network.chat.CommonComponents.GUI_BACK, button -> onClose()).build());
/*     */     
/*  41 */     repositionElements();
/*  42 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  47 */     this.backupInfoList.updateSize(this.width, this.layout);
/*  48 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  53 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private Component checkForSpecificMetadata(String key, String value) {
/*  57 */     String k = key.toLowerCase(Locale.ROOT);
/*  58 */     if (k.contains("game") && k.contains("mode"))
/*  59 */       return gameModeMetadata(value); 
/*  60 */     if (k.contains("game") && k.contains("difficulty"))
/*  61 */       return gameDifficultyMetadata(value); 
/*  62 */     if (key.equals("world_type")) {
/*  63 */       return parseWorldType(value);
/*     */     }
/*  65 */     return (Component)Component.literal(value);
/*     */   }
/*     */   
/*     */   private Component gameDifficultyMetadata(String value) {
/*     */     try {
/*  70 */       return ((net.minecraft.world.Difficulty)RealmsSlotOptionsScreen.DIFFICULTIES.get(Integer.parseInt(value))).getDisplayName();
/*  71 */     } catch (Exception ignored) {
/*  72 */       return UNKNOWN;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component gameModeMetadata(String value) {
/*     */     try {
/*  78 */       return ((net.minecraft.world.level.GameType)RealmsSlotOptionsScreen.GAME_MODES.get(Integer.parseInt(value))).getShortDisplayName();
/*  79 */     } catch (Exception ignored) {
/*  80 */       return UNKNOWN;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component parseWorldType(String value) {
/*     */     try {
/*  86 */       return RealmsServer.WorldType.valueOf(value.toUpperCase(Locale.ROOT)).getDisplayName();
/*  87 */     } catch (Exception ignored) {
/*  88 */       return RealmsServer.WorldType.UNKNOWN.getDisplayName();
/*     */     } 
/*     */   }
/*     */   
/*     */   private class BackupInfoListEntry extends ObjectSelectionList.Entry<BackupInfoListEntry> {
/*  93 */     private static final Component TEMPLATE_NAME = (Component)Component.translatable("mco.backup.entry.templateName");
/*  94 */     private static final Component GAME_DIFFICULTY = (Component)Component.translatable("mco.backup.entry.gameDifficulty");
/*  95 */     private static final Component NAME = (Component)Component.translatable("mco.backup.entry.name");
/*  96 */     private static final Component GAME_SERVER_VERSION = (Component)Component.translatable("mco.backup.entry.gameServerVersion");
/*  97 */     private static final Component UPLOADED = (Component)Component.translatable("mco.backup.entry.uploaded");
/*  98 */     private static final Component ENABLED_PACK = (Component)Component.translatable("mco.backup.entry.enabledPack");
/*  99 */     private static final Component DESCRIPTION = (Component)Component.translatable("mco.backup.entry.description");
/* 100 */     private static final Component GAME_MODE = (Component)Component.translatable("mco.backup.entry.gameMode");
/* 101 */     private static final Component SEED = (Component)Component.translatable("mco.backup.entry.seed");
/* 102 */     private static final Component WORLD_TYPE = (Component)Component.translatable("mco.backup.entry.worldType");
/* 103 */     private static final Component UNDEFINED = (Component)Component.translatable("mco.backup.entry.undefined");
/*     */     
/*     */     private final String key;
/*     */     private final String value;
/*     */     private final Component keyComponent;
/*     */     private final Component valueComponent;
/*     */     
/*     */     public BackupInfoListEntry(String key, String value) {
/* 111 */       this.key = key;
/* 112 */       this.value = value;
/* 113 */       this.keyComponent = translateKey(key);
/* 114 */       this.valueComponent = RealmsBackupInfoScreen.this.checkForSpecificMetadata(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 119 */       graphics.drawString(RealmsBackupInfoScreen.this.font, this.keyComponent, getContentX(), getContentY(), -6250336);
/* 120 */       graphics.drawString(RealmsBackupInfoScreen.this.font, this.valueComponent, getContentX(), getContentY() + 12, -1);
/*     */     }
/*     */     
/*     */     private Component translateKey(String key) {
/* 124 */       switch (key) { case "template_name": case "game_difficulty": case "name": case "game_server_version": case "uploaded": case "enabled_packs": case "description": case "game_mode": case "seed": case "world_type": default: break; }  return 
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
/* 135 */         UNDEFINED;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 141 */       return (Component)Component.translatable("narrator.select", new Object[] { this.key + " " + this.key });
/*     */     }
/*     */   }
/*     */   
/*     */   private class BackupInfoList extends ObjectSelectionList<BackupInfoListEntry> {
/*     */     public BackupInfoList(Minecraft minecraft) {
/* 147 */       super(minecraft, RealmsBackupInfoScreen.this.width, RealmsBackupInfoScreen.this.layout.getContentHeight(), RealmsBackupInfoScreen.this.layout.getHeaderHeight(), 36);
/* 148 */       if (RealmsBackupInfoScreen.this.backup.changeList != null)
/* 149 */         RealmsBackupInfoScreen.this.backup.changeList.forEach((key, value) -> addEntry((AbstractSelectionList.Entry)new RealmsBackupInfoScreen.BackupInfoListEntry(key, value))); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsBackupInfoScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */