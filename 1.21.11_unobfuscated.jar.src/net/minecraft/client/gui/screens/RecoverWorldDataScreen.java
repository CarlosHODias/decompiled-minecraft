/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.io.IOException;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecoverWorldDataScreen
/*     */   extends Screen
/*     */ {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SCREEN_SIDE_MARGIN = 25;
/*     */   
/*  36 */   private static final Component TITLE = (Component)Component.translatable("recover_world.title").withStyle(ChatFormatting.BOLD);
/*  37 */   private static final Component BUGTRACKER_BUTTON = (Component)Component.translatable("recover_world.bug_tracker");
/*  38 */   private static final Component RESTORE_BUTTON = (Component)Component.translatable("recover_world.restore");
/*  39 */   private static final Component NO_FALLBACK_TOOLTIP = (Component)Component.translatable("recover_world.no_fallback");
/*  40 */   private static final Component DONE_TITLE = (Component)Component.translatable("recover_world.done.title");
/*  41 */   private static final Component DONE_SUCCESS = (Component)Component.translatable("recover_world.done.success");
/*  42 */   private static final Component DONE_FAILED = (Component)Component.translatable("recover_world.done.failed");
/*  43 */   private static final Component NO_ISSUES = (Component)Component.translatable("recover_world.issue.none").withStyle(ChatFormatting.GREEN);
/*  44 */   private static final Component MISSING_FILE = (Component)Component.translatable("recover_world.issue.missing_file").withStyle(ChatFormatting.RED);
/*     */   
/*     */   private final BooleanConsumer callback;
/*  47 */   private final LinearLayout layout = LinearLayout.vertical().spacing(8);
/*     */   private final Component message;
/*     */   private final MultiLineTextWidget messageWidget;
/*     */   private final MultiLineTextWidget issuesWidget;
/*     */   private final LevelStorageSource.LevelStorageAccess storageAccess;
/*     */   
/*     */   public RecoverWorldDataScreen(Minecraft minecraft, BooleanConsumer callback, LevelStorageSource.LevelStorageAccess storageAccess) {
/*  54 */     super(TITLE);
/*  55 */     this.callback = callback;
/*  56 */     this.message = (Component)Component.translatable("recover_world.message", new Object[] { Component.literal(storageAccess.getLevelId()).withStyle(ChatFormatting.GRAY) });
/*  57 */     this.messageWidget = new MultiLineTextWidget(this.message, minecraft.font);
/*  58 */     this.storageAccess = storageAccess;
/*  59 */     Exception levelDatIssues = collectIssue(storageAccess, false);
/*  60 */     Exception levelDatOldIssues = collectIssue(storageAccess, true);
/*  61 */     MutableComponent mutableComponent = Component.empty()
/*  62 */       .append(buildInfo(storageAccess, false, levelDatIssues))
/*  63 */       .append("\n")
/*  64 */       .append(buildInfo(storageAccess, true, levelDatOldIssues));
/*  65 */     this.issuesWidget = new MultiLineTextWidget((Component)mutableComponent, minecraft.font);
/*  66 */     boolean canRecover = (levelDatIssues != null && levelDatOldIssues == null);
/*     */     
/*  68 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*     */     
/*  70 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, minecraft.font));
/*  71 */     this.layout.addChild((LayoutElement)this.messageWidget.setCentered(true));
/*  72 */     this.layout.addChild((LayoutElement)this.issuesWidget);
/*     */     
/*  74 */     LinearLayout buttonGrid = LinearLayout.horizontal().spacing(5);
/*  75 */     buttonGrid.addChild(
/*  76 */         (LayoutElement)Button.builder(BUGTRACKER_BUTTON, 
/*     */           
/*  78 */           ConfirmLinkScreen.confirmLink(this, CommonLinks.SNAPSHOT_BUGS_FEEDBACK))
/*     */         
/*  80 */         .size(120, 20)
/*  81 */         .build());
/*     */     
/*  83 */     ((Button)buttonGrid.addChild(
/*  84 */         (LayoutElement)Button.builder(RESTORE_BUTTON, button -> attemptRestore(minecraft))
/*     */ 
/*     */ 
/*     */         
/*  88 */         .size(120, 20)
/*  89 */         .tooltip(canRecover ? null : Tooltip.create(NO_FALLBACK_TOOLTIP))
/*  90 */         .build())).active = canRecover;
/*     */     
/*  92 */     this.layout.addChild((LayoutElement)buttonGrid);
/*  93 */     this.layout.addChild(
/*  94 */         (LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose())
/*     */ 
/*     */ 
/*     */         
/*  98 */         .size(120, 20)
/*  99 */         .build());
/*     */ 
/*     */     
/* 102 */     this.layout.visitWidgets(this::addRenderableWidget);
/*     */   }
/*     */ 
/*     */   
/*     */   private void attemptRestore(Minecraft minecraft) {
/* 107 */     Exception current = collectIssue(this.storageAccess, false);
/* 108 */     Exception old = collectIssue(this.storageAccess, true);
/* 109 */     if (current == null || old != null) {
/* 110 */       LOGGER.error("Failed to recover world, files not as expected. level.dat: {}, level.dat_old: {}", (current != null) ? current.getMessage() : "no issues", (old != null) ? old.getMessage() : "no issues");
/* 111 */       minecraft.setScreen(new AlertScreen(() -> this.callback.accept(false), DONE_TITLE, DONE_FAILED));
/*     */       return;
/*     */     } 
/* 114 */     minecraft.setScreenAndShow(new GenericMessageScreen((Component)Component.translatable("recover_world.restoring")));
/* 115 */     EditWorldScreen.makeBackupAndShowToast(this.storageAccess);
/* 116 */     if (this.storageAccess.restoreLevelDataFromOld()) {
/* 117 */       minecraft.setScreen(new ConfirmScreen(this.callback, DONE_TITLE, DONE_SUCCESS, CommonComponents.GUI_CONTINUE, CommonComponents.GUI_BACK));
/*     */     } else {
/* 119 */       minecraft.setScreen(new AlertScreen(() -> this.callback.accept(false), DONE_TITLE, DONE_FAILED));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component buildInfo(LevelStorageSource.LevelStorageAccess access, boolean fallback, Exception exception) {
/* 124 */     if (fallback && exception instanceof java.io.FileNotFoundException) {
/* 125 */       return (Component)Component.empty();
/*     */     }
/* 127 */     MutableComponent component = Component.empty();
/* 128 */     Instant timeStamp = access.getFileModificationTime(fallback);
/* 129 */     MutableComponent time = (timeStamp != null) ? Component.literal(WorldSelectionList.DATE_FORMAT.format(ZonedDateTime.ofInstant(timeStamp, ZoneId.systemDefault()))) : Component.translatable("recover_world.state_entry.unknown");
/* 130 */     component.append((Component)Component.translatable("recover_world.state_entry", new Object[] { time.withStyle(ChatFormatting.GRAY) }));
/* 131 */     if (exception == null) {
/* 132 */       component.append(NO_ISSUES);
/* 133 */     } else if (exception instanceof java.io.FileNotFoundException) {
/* 134 */       component.append(MISSING_FILE);
/* 135 */     } else if (exception instanceof net.minecraft.nbt.ReportedNbtException) {
/* 136 */       component.append((Component)Component.literal(exception.getCause().toString()).withStyle(ChatFormatting.RED));
/*     */     } else {
/* 138 */       component.append((Component)Component.literal(exception.toString()).withStyle(ChatFormatting.RED));
/*     */     } 
/* 140 */     return (Component)component;
/*     */   }
/*     */   
/*     */   private Exception collectIssue(LevelStorageSource.LevelStorageAccess access, boolean useFallback) {
/*     */     try {
/* 145 */       if (!useFallback) {
/* 146 */         access.getSummary(access.getDataTag());
/*     */       } else {
/* 148 */         access.getSummary(access.getDataTagFallback());
/*     */       } 
/* 150 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 151 */       return e;
/*     */     } 
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 158 */     super.init();
/* 159 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 164 */     this.issuesWidget.setMaxWidth(this.width - 50);
/* 165 */     this.messageWidget.setMaxWidth(this.width - 50);
/* 166 */     this.layout.arrangeElements();
/* 167 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 172 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.message });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 177 */     this.callback.accept(false);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/RecoverWorldDataScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */