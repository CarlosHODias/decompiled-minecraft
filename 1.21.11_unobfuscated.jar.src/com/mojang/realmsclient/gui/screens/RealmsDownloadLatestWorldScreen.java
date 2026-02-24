/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.RateLimiter;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.Unit;
/*     */ import com.mojang.realmsclient.client.FileDownload;
/*     */ import com.mojang.realmsclient.dto.WorldDownload;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsDownloadLatestWorldScreen extends RealmsScreen {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  30 */   private static final ReentrantLock DOWNLOAD_LOCK = new ReentrantLock();
/*     */   
/*     */   private static final int BAR_WIDTH = 200;
/*     */   
/*     */   private static final int BAR_TOP = 80;
/*     */   
/*     */   private static final int BAR_BOTTOM = 95;
/*     */   private static final int BAR_BORDER = 1;
/*     */   private final Screen lastScreen;
/*     */   private final WorldDownload worldDownload;
/*     */   private final Component downloadTitle;
/*     */   private final RateLimiter narrationRateLimiter;
/*     */   private Button cancelButton;
/*     */   private final String worldName;
/*     */   private final DownloadStatus downloadStatus;
/*     */   private volatile Component errorMessage;
/*  46 */   private volatile Component status = (Component)Component.translatable("mco.download.preparing");
/*     */   
/*     */   private volatile String progress;
/*     */   
/*     */   private volatile boolean cancelled;
/*     */   private volatile boolean showDots = true;
/*     */   private volatile boolean finished;
/*     */   private volatile boolean extracting;
/*     */   private Long previousWrittenBytes;
/*     */   private Long previousTimeSnapshot;
/*     */   private long bytesPersSecond;
/*     */   private int animTick;
/*  58 */   private static final String[] DOTS = new String[] { "", ".", ". .", ". . ." };
/*     */   
/*     */   private int dotIndex;
/*     */   private boolean checked;
/*     */   private final BooleanConsumer callback;
/*     */   
/*     */   public RealmsDownloadLatestWorldScreen(Screen lastScreen, WorldDownload worldDownload, String worldName, BooleanConsumer callback) {
/*  65 */     super(GameNarrator.NO_TITLE);
/*  66 */     this.callback = callback;
/*  67 */     this.lastScreen = lastScreen;
/*  68 */     this.worldName = worldName;
/*  69 */     this.worldDownload = worldDownload;
/*  70 */     this.downloadStatus = new DownloadStatus();
/*  71 */     this.downloadTitle = (Component)Component.translatable("mco.download.title");
/*  72 */     this.narrationRateLimiter = RateLimiter.create(0.10000000149011612D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  77 */     this.cancelButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
/*  78 */         .bounds((this.width - 200) / 2, this.height - 42, 200, 20).build());
/*  79 */     checkDownloadSize();
/*     */   }
/*     */   
/*     */   private void checkDownloadSize() {
/*  83 */     if (this.finished || this.checked) {
/*     */       return;
/*     */     }
/*  86 */     this.checked = true;
/*     */     
/*  88 */     if (getContentLength(this.worldDownload.downloadLink()) >= 5368709120L) {
/*  89 */       MutableComponent mutableComponent = Component.translatable("mco.download.confirmation.oversized", new Object[] { Unit.humanReadable(5368709120L) });
/*  90 */       this.minecraft.setScreen((Screen)RealmsPopups.warningAcknowledgePopupScreen((Screen)this, (Component)mutableComponent, popupScreen -> {
/*     */               this.minecraft.setScreen((Screen)this);
/*     */ 
/*     */               
/*     */               downloadSave();
/*     */             }));
/*     */     } else {
/*  97 */       downloadSave();
/*     */     } 
/*     */   }
/*     */   
/*     */   private long getContentLength(String downloadLink) {
/* 102 */     return FileDownload.contentLength(downloadLink).orElse(0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 107 */     super.tick();
/*     */     
/* 109 */     this.animTick++;
/*     */     
/* 111 */     if (this.status != null && 
/* 112 */       this.narrationRateLimiter.tryAcquire(1)) {
/* 113 */       Component message = createProgressNarrationMessage();
/* 114 */       this.minecraft.getNarrator().saySystemNow(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Component createProgressNarrationMessage() {
/* 120 */     List<Component> elements = Lists.newArrayList();
/* 121 */     elements.add(this.downloadTitle);
/* 122 */     elements.add(this.status);
/* 123 */     if (this.progress != null) {
/* 124 */       elements.add(Component.translatable("mco.download.percent", new Object[] { this.progress }));
/* 125 */       elements.add(Component.translatable("mco.download.speed.narration", new Object[] { Unit.humanReadable(this.bytesPersSecond) }));
/*     */     } 
/* 127 */     if (this.errorMessage != null) {
/* 128 */       elements.add(this.errorMessage);
/*     */     }
/* 130 */     return CommonComponents.joinLines(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 135 */     this.cancelled = true;
/* 136 */     if (this.finished && this.callback != null && this.errorMessage == null) {
/* 137 */       this.callback.accept(true);
/*     */     }
/* 139 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 144 */     super.render(graphics, xm, ym, a);
/*     */     
/* 146 */     graphics.drawCenteredString(this.font, this.downloadTitle, this.width / 2, 20, -1);
/*     */     
/* 148 */     graphics.drawCenteredString(this.font, this.status, this.width / 2, 50, -1);
/*     */     
/* 150 */     if (this.showDots) {
/* 151 */       drawDots(graphics);
/*     */     }
/*     */     
/* 154 */     if (this.downloadStatus.bytesWritten != 0L && !this.cancelled) {
/* 155 */       drawProgressBar(graphics);
/* 156 */       drawDownloadSpeed(graphics);
/*     */     } 
/*     */     
/* 159 */     if (this.errorMessage != null) {
/* 160 */       graphics.drawCenteredString(this.font, this.errorMessage, this.width / 2, 110, -65536);
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawDots(GuiGraphics graphics) {
/* 165 */     int statusWidth = this.font.width((FormattedText)this.status);
/*     */     
/* 167 */     if (this.animTick != 0 && this.animTick % 10 == 0) {
/* 168 */       this.dotIndex++;
/*     */     }
/*     */     
/* 171 */     graphics.drawString(this.font, DOTS[this.dotIndex % DOTS.length], this.width / 2 + statusWidth / 2 + 5, 50, -1);
/*     */   }
/*     */   
/*     */   private void drawProgressBar(GuiGraphics graphics) {
/* 175 */     double percentage = Math.min(this.downloadStatus.bytesWritten / this.downloadStatus.totalBytes, 1.0D);
/* 176 */     this.progress = String.format(Locale.ROOT, "%.1f", new Object[] { percentage * 100.0D });
/*     */     
/* 178 */     int left = (this.width - 200) / 2;
/* 179 */     int right = left + (int)Math.round(200.0D * percentage);
/* 180 */     graphics.fill(left - 1, 79, right + 1, 96, -1);
/* 181 */     graphics.fill(left, 80, right, 95, -8355712);
/*     */     
/* 183 */     graphics.drawCenteredString(this.font, (Component)Component.translatable("mco.download.percent", new Object[] { this.progress }), this.width / 2, 84, -1);
/*     */   }
/*     */   
/*     */   private void drawDownloadSpeed(GuiGraphics graphics) {
/* 187 */     if (this.animTick % 20 == 0) {
/* 188 */       if (this.previousWrittenBytes != null) {
/* 189 */         long timeElapsed = Util.getMillis() - this.previousTimeSnapshot;
/* 190 */         if (timeElapsed == 0L) {
/* 191 */           timeElapsed = 1L;
/*     */         }
/* 193 */         this.bytesPersSecond = 1000L * (this.downloadStatus.bytesWritten - this.previousWrittenBytes) / timeElapsed;
/* 194 */         drawDownloadSpeed0(graphics, this.bytesPersSecond);
/*     */       } 
/* 196 */       this.previousWrittenBytes = this.downloadStatus.bytesWritten;
/* 197 */       this.previousTimeSnapshot = Util.getMillis();
/*     */     } else {
/* 199 */       drawDownloadSpeed0(graphics, this.bytesPersSecond);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawDownloadSpeed0(GuiGraphics graphics, long bytesPerSecond) {
/* 204 */     if (bytesPerSecond > 0L) {
/* 205 */       int progressLength = this.font.width(this.progress);
/* 206 */       graphics.drawString(this.font, (Component)Component.translatable("mco.download.speed", new Object[] { Unit.humanReadable(bytesPerSecond) }), this.width / 2 + progressLength / 2 + 15, 84, -1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void downloadSave() {
/* 212 */     new Thread(() -> {
/*     */           try {
/*     */             if (!DOWNLOAD_LOCK.tryLock(1L, TimeUnit.SECONDS)) {
/*     */               this.status = (Component)Component.translatable("mco.download.failed");
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/*     */             if (this.cancelled) {
/*     */               downloadCancelled();
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/*     */             this.status = (Component)Component.translatable("mco.download.downloading", new Object[] { this.worldName });
/*     */             
/*     */             FileDownload fileDownload = new FileDownload();
/*     */             
/*     */             fileDownload.download(this.worldDownload, this.worldName, this.downloadStatus, this.minecraft.getLevelSource());
/*     */             
/*     */             while (!fileDownload.isFinished()) {
/*     */               if (fileDownload.isError()) {
/*     */                 fileDownload.cancel();
/*     */                 this.errorMessage = (Component)Component.translatable("mco.download.failed");
/*     */                 this.cancelButton.setMessage(CommonComponents.GUI_DONE);
/*     */                 return;
/*     */               } 
/*     */               if (fileDownload.isExtracting()) {
/*     */                 if (!this.extracting) {
/*     */                   this.status = (Component)Component.translatable("mco.download.extracting");
/*     */                 }
/*     */                 this.extracting = true;
/*     */               } 
/*     */               if (this.cancelled) {
/*     */                 fileDownload.cancel();
/*     */                 downloadCancelled();
/*     */                 return;
/*     */               } 
/*     */               try {
/*     */                 Thread.sleep(500L);
/* 252 */               } catch (InterruptedException ignored) {
/*     */                 LOGGER.error("Failed to check Realms backup download status");
/*     */               } 
/*     */             } 
/*     */             
/*     */             this.finished = true;
/*     */             this.status = (Component)Component.translatable("mco.download.done");
/*     */             this.cancelButton.setMessage(CommonComponents.GUI_DONE);
/* 260 */           } catch (InterruptedException ignored) {
/*     */             LOGGER.error("Could not acquire upload lock");
/* 262 */           } catch (Exception e) {
/*     */             this.errorMessage = (Component)Component.translatable("mco.download.failed");
/*     */             
/*     */             LOGGER.info("Exception while downloading world", e);
/*     */           } finally {
/*     */             if (DOWNLOAD_LOCK.isHeldByCurrentThread()) {
/*     */               DOWNLOAD_LOCK.unlock();
/*     */             } else {
/*     */               return;
/*     */             } 
/*     */             this.showDots = false;
/*     */             this.finished = true;
/*     */           } 
/* 275 */         }).start();
/*     */   }
/*     */   
/*     */   private void downloadCancelled() {
/* 279 */     this.status = (Component)Component.translatable("mco.download.cancelled");
/*     */   }
/*     */   
/*     */   public static class DownloadStatus {
/*     */     public volatile long bytesWritten;
/*     */     public volatile long totalBytes;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsDownloadLatestWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */