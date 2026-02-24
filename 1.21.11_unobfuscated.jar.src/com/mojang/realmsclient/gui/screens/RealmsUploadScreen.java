/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.RateLimiter;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.Unit;
/*     */ import com.mojang.realmsclient.client.UploadStatus;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsUploadException;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsWorldUpload;
/*     */ import com.mojang.realmsclient.client.worldupload.RealmsWorldUploadStatusTracker;
/*     */ import com.mojang.realmsclient.dto.RealmsSetting;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchSlotTask;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.TitleScreen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ 
/*     */ public class RealmsUploadScreen extends RealmsScreen implements RealmsWorldUploadStatusTracker {
/*     */   private static final int BAR_WIDTH = 200;
/*     */   private static final int BAR_TOP = 80;
/*     */   private static final int BAR_BOTTOM = 95;
/*     */   private static final int BAR_BORDER = 1;
/*  46 */   private static final String[] DOTS = new String[] { "", ".", ". .", ". . ." };
/*  47 */   private static final Component VERIFYING_TEXT = (Component)Component.translatable("mco.upload.verifying");
/*     */   
/*     */   private final RealmsResetWorldScreen lastScreen;
/*     */   
/*     */   private final LevelSummary selectedLevel;
/*     */   private final RealmCreationTask realmCreationTask;
/*     */   private final long realmId;
/*     */   private final int slotId;
/*  55 */   final AtomicReference<RealmsWorldUpload> currentUpload = new AtomicReference<>();
/*     */   
/*     */   private final UploadStatus uploadStatus;
/*     */   private final RateLimiter narrationRateLimiter;
/*     */   private volatile Component[] errorMessage;
/*  60 */   private volatile Component status = (Component)Component.translatable("mco.upload.preparing");
/*     */   
/*     */   private volatile String progress;
/*     */   
/*     */   private volatile boolean cancelled;
/*     */   
/*     */   private volatile boolean uploadFinished;
/*     */   
/*     */   private volatile boolean showDots = true;
/*     */   private volatile boolean uploadStarted;
/*     */   private Button backButton;
/*     */   private Button cancelButton;
/*     */   private int tickCount;
/*  73 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   public RealmsUploadScreen(RealmCreationTask realmCreationTask, long realmId, int slotId, RealmsResetWorldScreen lastScreen, LevelSummary selectedLevel) {
/*  76 */     super(GameNarrator.NO_TITLE);
/*  77 */     this.realmCreationTask = realmCreationTask;
/*  78 */     this.realmId = realmId;
/*  79 */     this.slotId = slotId;
/*  80 */     this.lastScreen = lastScreen;
/*  81 */     this.selectedLevel = selectedLevel;
/*  82 */     this.uploadStatus = new UploadStatus();
/*  83 */     this.narrationRateLimiter = RateLimiter.create(0.10000000149011612D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  88 */     this.backButton = (Button)this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onBack()).build());
/*  89 */     this.backButton.visible = false;
/*  90 */     this.cancelButton = (Button)this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).build());
/*     */     
/*  92 */     if (!this.uploadStarted) {
/*  93 */       if (this.lastScreen.slot == -1) {
/*  94 */         this.uploadStarted = true;
/*  95 */         upload();
/*     */       } else {
/*  97 */         List<LongRunningTask> tasks = new ArrayList<>();
/*  98 */         if (this.realmCreationTask != null) {
/*  99 */           tasks.add(this.realmCreationTask);
/*     */         }
/* 101 */         tasks.add(new SwitchSlotTask(this.realmId, this.lastScreen.slot, () -> {
/*     */                 if (!this.uploadStarted) {
/*     */                   this.uploadStarted = true;
/*     */ 
/*     */                   
/*     */                   this.minecraft.execute(());
/*     */                 } 
/*     */               }));
/*     */         
/* 110 */         this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)this.lastScreen, tasks.<LongRunningTask>toArray(new LongRunningTask[0])));
/*     */       } 
/*     */     }
/*     */     
/* 114 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 115 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 120 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private void onBack() {
/* 124 */     this.minecraft.setScreen((Screen)new RealmsConfigureWorldScreen(new RealmsMainScreen((Screen)new TitleScreen()), this.realmId));
/*     */   }
/*     */   
/*     */   private void onCancel() {
/* 128 */     this.cancelled = true;
/* 129 */     RealmsWorldUpload realmsWorldUpload = this.currentUpload.get();
/* 130 */     if (realmsWorldUpload != null) {
/* 131 */       realmsWorldUpload.cancel();
/*     */     } else {
/* 133 */       this.minecraft.setScreen((Screen)this.lastScreen);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 139 */     if (event.key() == 256) {
/* 140 */       if (this.showDots) {
/* 141 */         onCancel();
/*     */       } else {
/* 143 */         onBack();
/*     */       } 
/* 145 */       return true;
/*     */     } 
/* 147 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 152 */     super.render(graphics, xm, ym, a);
/*     */     
/* 154 */     if (!this.uploadFinished && this.uploadStatus.uploadStarted() && this.uploadStatus.uploadCompleted() && this.cancelButton != null) {
/* 155 */       this.status = VERIFYING_TEXT;
/* 156 */       this.cancelButton.active = false;
/*     */     } 
/*     */     
/* 159 */     graphics.drawCenteredString(this.font, this.status, this.width / 2, 50, -1);
/*     */     
/* 161 */     if (this.showDots) {
/* 162 */       graphics.drawString(this.font, DOTS[this.tickCount / 10 % DOTS.length], this.width / 2 + this.font.width((FormattedText)this.status) / 2 + 5, 50, -1);
/*     */     }
/*     */     
/* 165 */     if (this.uploadStatus.uploadStarted() && !this.cancelled) {
/* 166 */       drawProgressBar(graphics);
/* 167 */       drawUploadSpeed(graphics);
/*     */     } 
/*     */     
/* 170 */     Component[] errorMessages = this.errorMessage;
/* 171 */     if (errorMessages != null) {
/* 172 */       for (int i = 0; i < errorMessages.length; i++) {
/* 173 */         graphics.drawCenteredString(this.font, errorMessages[i], this.width / 2, 110 + 12 * i, -65536);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawProgressBar(GuiGraphics graphics) {
/* 179 */     double percentage = this.uploadStatus.getPercentage();
/* 180 */     this.progress = String.format(Locale.ROOT, "%.1f", new Object[] { percentage * 100.0D });
/*     */     
/* 182 */     int left = (this.width - 200) / 2;
/* 183 */     int right = left + (int)Math.round(200.0D * percentage);
/* 184 */     graphics.fill(left - 1, 79, right + 1, 96, -1);
/* 185 */     graphics.fill(left, 80, right, 95, -8355712);
/* 186 */     graphics.drawCenteredString(this.font, (Component)Component.translatable("mco.upload.percent", new Object[] { this.progress }), this.width / 2, 84, -1);
/*     */   }
/*     */   
/*     */   private void drawUploadSpeed(GuiGraphics graphics) {
/* 190 */     drawUploadSpeed0(graphics, this.uploadStatus.getBytesPerSecond());
/*     */   }
/*     */   
/*     */   private void drawUploadSpeed0(GuiGraphics graphics, long bytesPerSecond) {
/* 194 */     String uploadProgress = this.progress;
/* 195 */     if (bytesPerSecond > 0L && uploadProgress != null) {
/* 196 */       int progressLength = this.font.width(uploadProgress);
/* 197 */       String stringPresentation = "(" + Unit.humanReadable(bytesPerSecond) + "/s)";
/* 198 */       graphics.drawString(this.font, stringPresentation, this.width / 2 + progressLength / 2 + 15, 84, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 204 */     super.tick();
/*     */     
/* 206 */     this.tickCount++;
/*     */     
/* 208 */     this.uploadStatus.refreshBytesPerSecond();
/*     */     
/* 210 */     if (this.narrationRateLimiter.tryAcquire(1)) {
/* 211 */       Component message = createProgressNarrationMessage();
/* 212 */       this.minecraft.getNarrator().saySystemNow(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component createProgressNarrationMessage() {
/* 217 */     List<Component> elements = Lists.newArrayList();
/* 218 */     elements.add(this.status);
/* 219 */     if (this.progress != null) {
/* 220 */       elements.add(Component.translatable("mco.upload.percent", new Object[] { this.progress }));
/*     */     }
/* 222 */     Component[] errorMessages = this.errorMessage;
/* 223 */     if (errorMessages != null) {
/* 224 */       elements.addAll(Arrays.asList(errorMessages));
/*     */     }
/* 226 */     return CommonComponents.joinLines(elements);
/*     */   }
/*     */   
/*     */   private void upload() {
/* 230 */     Path worldFolder = this.minecraft.gameDirectory.toPath().resolve("saves").resolve(this.selectedLevel.getLevelId());
/* 231 */     RealmsWorldOptions worldOptions = RealmsWorldOptions.createFromSettings(this.selectedLevel.getSettings(), this.selectedLevel.levelVersion().minecraftVersionName());
/* 232 */     RealmsSlot realmsSlot = new RealmsSlot(this.slotId, worldOptions, List.of(RealmsSetting.hardcoreSetting(this.selectedLevel.getSettings().hardcore())));
/* 233 */     RealmsWorldUpload newUpload = new RealmsWorldUpload(worldFolder, realmsSlot, this.minecraft.getUser(), this.realmId, this);
/*     */     
/* 235 */     if (!this.currentUpload.compareAndSet(null, newUpload)) {
/* 236 */       throw new IllegalStateException("Tried to start uploading but was already uploading");
/*     */     }
/* 238 */     newUpload.packAndUpload().handleAsync((result, exception) -> { if (exception != null) { if (exception instanceof CompletionException) { CompletionException e = (CompletionException)exception; exception = e.getCause(); }  if (exception instanceof RealmsUploadException) { RealmsUploadException e = (RealmsUploadException)exception; if (e.getStatusMessage() != null) this.status = e.getStatusMessage();  setErrorMessage(e.getErrorMessages()); } else { this.status = (Component)Component.translatable("mco.upload.failed", new Object[] { exception.getMessage() }); }  } else { this.status = (Component)Component.translatable("mco.upload.done"); if (this.backButton != null) this.backButton.setMessage(CommonComponents.GUI_DONE);  }  this.uploadFinished = true; this.showDots = false; if (this.backButton != null) this.backButton.visible = true;  if (this.cancelButton != null) this.cancelButton.visible = false;  this.currentUpload.set(null); return null; }, (Executor)this.minecraft);
/*     */   }
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
/*     */   private void setErrorMessage(Component... messages) {
/* 273 */     this.errorMessage = messages;
/*     */   }
/*     */ 
/*     */   
/*     */   public UploadStatus getUploadStatus() {
/* 278 */     return this.uploadStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUploading() {
/* 283 */     this.status = (Component)Component.translatable("mco.upload.uploading", new Object[] { this.selectedLevel.getLevelName() });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsUploadScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */