/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.Backup;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import com.mojang.realmsclient.util.task.DownloadTask;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.RestoreTask;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsBackupScreen extends RealmsScreen {
/*  40 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  42 */   private static final Component TITLE = (Component)Component.translatable("mco.configure.world.backup");
/*  43 */   private static final Component RESTORE_TOOLTIP = (Component)Component.translatable("mco.backup.button.restore");
/*  44 */   private static final Component HAS_CHANGES_TOOLTIP = (Component)Component.translatable("mco.backup.changes.tooltip");
/*  45 */   private static final Component NO_BACKUPS_LABEL = (Component)Component.translatable("mco.backup.nobackups");
/*  46 */   private static final Component DOWNLOAD_LATEST = (Component)Component.translatable("mco.backup.button.download");
/*     */   
/*     */   private static final String UPLOADED_KEY = "uploaded";
/*     */   
/*     */   private static final int PADDING = 8;
/*  51 */   public static final DateTimeFormatter SHORT_DATE_FORMAT = Util.localizedDateFormatter(FormatStyle.SHORT);
/*     */   
/*     */   private final RealmsConfigureWorldScreen lastScreen;
/*     */   
/*  55 */   private List<Backup> backups = java.util.Collections.emptyList();
/*     */   
/*     */   private BackupObjectSelectionList backupList;
/*     */   
/*  59 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private final int slotId;
/*     */   
/*     */   private Button downloadButton;
/*     */   
/*     */   private final RealmsServer serverData;
/*     */   
/*     */   private boolean noBackups = false;
/*     */   
/*     */   public RealmsBackupScreen(RealmsConfigureWorldScreen lastScreen, RealmsServer serverData, int slotId) {
/*  70 */     super(TITLE);
/*  71 */     this.lastScreen = lastScreen;
/*  72 */     this.serverData = serverData;
/*  73 */     this.slotId = slotId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  78 */     this.layout.addTitleHeader(TITLE, this.font);
/*  79 */     this.backupList = (BackupObjectSelectionList)this.layout.addToContents((LayoutElement)new BackupObjectSelectionList(this));
/*     */     
/*  81 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  82 */     this.downloadButton = (Button)footer.addChild((LayoutElement)Button.builder(DOWNLOAD_LATEST, button -> downloadClicked()).build());
/*  83 */     this.downloadButton.active = false;
/*  84 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*     */     
/*  86 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  87 */     repositionElements();
/*     */     
/*  89 */     fetchRealmsBackups();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  94 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  96 */     if (this.noBackups && this.backupList != null) {
/*     */       
/*  98 */       Objects.requireNonNull(this.font); graphics.drawString(this.font, NO_BACKUPS_LABEL, this.width / 2 - this.font.width((FormattedText)NO_BACKUPS_LABEL) / 2, this.backupList.getY() + this.backupList.getHeight() / 2 - 9 / 2, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 104 */     this.layout.arrangeElements();
/* 105 */     if (this.backupList != null) {
/* 106 */       this.backupList.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */   
/*     */   private void fetchRealmsBackups() {
/* 111 */     new Thread("Realms-fetch-backups")
/*     */       {
/*     */         public void run() {
/* 114 */           RealmsClient client = RealmsClient.getOrCreate();
/*     */           try {
/* 116 */             List<Backup> backups = client.backupsFor(RealmsBackupScreen.this.serverData.id).backups();
/* 117 */             RealmsBackupScreen.this.minecraft.execute(() -> {
/*     */                   RealmsBackupScreen.this.backups = backups;
/*     */                   RealmsBackupScreen.this.noBackups = RealmsBackupScreen.this.backups.isEmpty();
/*     */                   if (!RealmsBackupScreen.this.noBackups && RealmsBackupScreen.this.downloadButton != null) {
/*     */                     RealmsBackupScreen.this.downloadButton.active = true;
/*     */                   }
/*     */                   if (RealmsBackupScreen.this.backupList != null) {
/*     */                     RealmsBackupScreen.this.backupList.replaceEntries(RealmsBackupScreen.this.backups.stream().map(()).toList());
/*     */                   }
/*     */                 });
/* 127 */           } catch (RealmsServiceException e) {
/* 128 */             RealmsBackupScreen.LOGGER.error("Couldn't request backups", (Throwable)e);
/*     */           } 
/*     */         }
/* 131 */       }.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 136 */     this.minecraft.setScreen((Screen)this.lastScreen);
/*     */   }
/*     */   
/*     */   private void downloadClicked() {
/* 140 */     this.minecraft.setScreen(
/* 141 */         (Screen)RealmsPopups.infoPopupScreen((Screen)this, (Component)Component.translatable("mco.configure.world.restore.download.question.line1"), popup -> this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)this.lastScreen.getNewScreen(), new LongRunningTask[] { (LongRunningTask)new DownloadTask(this.serverData.id, this.slotId, (String)Objects.<String>requireNonNullElse(this.serverData.name, "") + " (" + (String)Objects.<String>requireNonNullElse(this.serverData.name, "") + ")", (Screen)this) }))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BackupObjectSelectionList
/*     */     extends ContainerObjectSelectionList<Entry>
/*     */   {
/*     */     private static final int ITEM_HEIGHT = 36;
/*     */ 
/*     */ 
/*     */     
/*     */     public BackupObjectSelectionList(RealmsBackupScreen this$0) {
/* 155 */       super(Minecraft.getInstance(), this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 36);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 160 */       return 300;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class Entry
/*     */     extends ContainerObjectSelectionList.Entry<Entry>
/*     */   {
/*     */     private static final int Y_PADDING = 2;
/*     */     private final Backup backup;
/*     */     private Button restoreButton;
/*     */     private Button changesButton;
/* 172 */     private final List<AbstractWidget> children = new ArrayList<>();
/*     */     
/*     */     public Entry(Backup backup) {
/* 175 */       this.backup = backup;
/*     */       
/* 177 */       populateChangeList(backup);
/*     */       
/* 179 */       if (!backup.changeList.isEmpty()) {
/* 180 */         this
/*     */ 
/*     */           
/* 183 */           .changesButton = Button.builder(RealmsBackupScreen.HAS_CHANGES_TOOLTIP, button -> RealmsBackupScreen.this.minecraft.setScreen((Screen)new RealmsBackupInfoScreen((Screen)RealmsBackupScreen.this, this.backup))).width(8 + RealmsBackupScreen.this.font.width((FormattedText)RealmsBackupScreen.HAS_CHANGES_TOOLTIP)).createNarration(this::narrationForBackupEntry).build();
/* 184 */         this.children.add(this.changesButton);
/*     */       } 
/*     */       
/* 187 */       if (!RealmsBackupScreen.this.serverData.expired) {
/* 188 */         this
/*     */ 
/*     */           
/* 191 */           .restoreButton = Button.builder(RealmsBackupScreen.RESTORE_TOOLTIP, button -> restoreClicked()).width(8 + RealmsBackupScreen.this.font.width((FormattedText)RealmsBackupScreen.HAS_CHANGES_TOOLTIP)).createNarration(this::narrationForBackupEntry).build();
/* 192 */         this.children.add(this.restoreButton);
/*     */       } 
/*     */     }
/*     */     
/*     */     private MutableComponent narrationForBackupEntry(Supplier<MutableComponent> defaultNarrationSupplier) {
/* 197 */       return CommonComponents.joinForNarration(new Component[] {
/* 198 */             (Component)Component.translatable("mco.backup.narration", new Object[] { RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate()) }), (Component)
/* 199 */             defaultNarrationSupplier.get() });
/*     */     }
/*     */     
/*     */     private void populateChangeList(Backup backup) {
/* 203 */       int index = RealmsBackupScreen.this.backups.indexOf(backup);
/* 204 */       if (index == RealmsBackupScreen.this.backups.size() - 1) {
/*     */         return;
/*     */       }
/* 207 */       Backup olderBackup = RealmsBackupScreen.this.backups.get(index + 1);
/* 208 */       for (String key : (Iterable<String>)backup.metadata.keySet()) {
/* 209 */         if (!key.contains("uploaded") && olderBackup.metadata.containsKey(key)) {
/* 210 */           if (!((String)backup.metadata.get(key)).equals(olderBackup.metadata.get(key)))
/* 211 */             addToChangeList(key); 
/*     */           continue;
/*     */         } 
/* 214 */         addToChangeList(key);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void addToChangeList(String key) {
/* 220 */       if (key.contains("uploaded")) {
/* 221 */         String uploadedTime = RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate());
/* 222 */         this.backup.changeList.put(key, uploadedTime);
/* 223 */         this.backup.uploadedVersion = true;
/*     */       } else {
/* 225 */         this.backup.changeList.put(key, (String)this.backup.metadata.get(key));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void restoreClicked() {
/* 230 */       Component age = RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModified);
/* 231 */       String lastModifiedDate = RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate());
/* 232 */       MutableComponent mutableComponent = Component.translatable("mco.configure.world.restore.question.line1", new Object[] { lastModifiedDate, age });
/* 233 */       RealmsBackupScreen.this.minecraft.setScreen((Screen)RealmsPopups.warningPopupScreen((Screen)RealmsBackupScreen.this, (Component)mutableComponent, popup -> {
/*     */               RealmsConfigureWorldScreen newScreen = RealmsBackupScreen.this.lastScreen.getNewScreen();
/*     */               RealmsBackupScreen.this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)newScreen, new LongRunningTask[] { (LongRunningTask)new RestoreTask(this.backup, RealmsBackupScreen.this.serverData.id, newScreen) }));
/*     */             }));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 243 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
/* 248 */       return (List)this.children;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 253 */       int middle = getContentYMiddle();
/* 254 */       Objects.requireNonNull(RealmsBackupScreen.this.font); int firstLineYPos = middle - 9 - 2;
/* 255 */       int secondLineYPos = middle + 2;
/* 256 */       int color = this.backup.uploadedVersion ? -8388737 : -1;
/* 257 */       graphics.drawString(RealmsBackupScreen.this.font, (Component)Component.translatable("mco.backup.entry", new Object[] { RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModified) }), getContentX(), firstLineYPos, color);
/* 258 */       graphics.drawString(RealmsBackupScreen.this.font, RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate()), getContentX(), secondLineYPos, -11776948);
/* 259 */       int iconXOffet = 0;
/* 260 */       int iconYPos = getContentYMiddle() - 10;
/* 261 */       if (this.restoreButton != null) {
/* 262 */         iconXOffet += this.restoreButton.getWidth() + 8;
/* 263 */         this.restoreButton.setX(getContentRight() - iconXOffet);
/* 264 */         this.restoreButton.setY(iconYPos);
/* 265 */         this.restoreButton.render(graphics, mouseX, mouseY, a);
/*     */       } 
/* 267 */       if (this.changesButton != null) {
/* 268 */         iconXOffet += this.changesButton.getWidth() + 8;
/* 269 */         this.changesButton.setX(getContentRight() - iconXOffet);
/* 270 */         this.changesButton.setY(iconYPos);
/* 271 */         this.changesButton.render(graphics, mouseX, mouseY, a);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsBackupScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */