/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.realms.RepeatedNarrator;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsLongRunningMcoTaskScreen extends RealmsScreen {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  27 */   private static final RepeatedNarrator REPEATED_NARRATOR = new RepeatedNarrator(Duration.ofSeconds(5L));
/*     */   
/*     */   private final List<LongRunningTask> queuedTasks;
/*     */   
/*     */   private final Screen lastScreen;
/*  32 */   protected final LinearLayout layout = LinearLayout.vertical();
/*     */   
/*     */   private volatile Component title;
/*     */   private LoadingDotsWidget loadingDotsWidget;
/*     */   
/*     */   public RealmsLongRunningMcoTaskScreen(Screen lastScreen, LongRunningTask... tasks) {
/*  38 */     super(GameNarrator.NO_TITLE);
/*  39 */     this.lastScreen = lastScreen;
/*  40 */     this.queuedTasks = List.of(tasks);
/*  41 */     if (this.queuedTasks.isEmpty()) {
/*  42 */       throw new IllegalArgumentException("No tasks added");
/*     */     }
/*     */     
/*  45 */     this.title = ((LongRunningTask)this.queuedTasks.get(0)).getTitle();
/*     */     Runnable runnable = () -> {
/*     */         LongRunningTask[] arrayOfLongRunningTask = tasks;
/*     */         int i = arrayOfLongRunningTask.length, j = 0;
/*     */         while (j < i) {
/*     */           LongRunningTask task = arrayOfLongRunningTask[j];
/*     */           setTitle(task.getTitle());
/*     */           if (!task.aborted()) {
/*     */             task.run();
/*     */             if (task.aborted())
/*     */               return; 
/*     */             j++;
/*     */           } 
/*     */         } 
/*     */       };
/*  60 */     Thread thread = new Thread(runnable, "Realms-long-running-task");
/*  61 */     thread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new RealmsDefaultUncaughtExceptionHandler(LOGGER));
/*  62 */     thread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInterruptWithAnotherScreen() {
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  72 */     super.tick();
/*     */     
/*  74 */     if (this.loadingDotsWidget != null) {
/*  75 */       REPEATED_NARRATOR.narrate(this.minecraft.getNarrator(), this.loadingDotsWidget.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  81 */     if (event.key() == 256) {
/*  82 */       cancel();
/*  83 */       return true;
/*     */     } 
/*  85 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  90 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  91 */     this.layout.addChild((LayoutElement)realmsLogo());
/*  92 */     this.loadingDotsWidget = new LoadingDotsWidget(this.font, this.title);
/*  93 */     this.layout.addChild((LayoutElement)this.loadingDotsWidget, layoutSettings -> layoutSettings.paddingTop(10).paddingBottom(30));
/*  94 */     this.layout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> cancel()).build());
/*  95 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  96 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 101 */     this.layout.arrangeElements();
/* 102 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   protected void cancel() {
/* 106 */     for (LongRunningTask queuedTask : this.queuedTasks) {
/* 107 */       queuedTask.abortTask();
/*     */     }
/* 109 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public void setTitle(Component title) {
/* 113 */     if (this.loadingDotsWidget != null) {
/* 114 */       this.loadingDotsWidget.setMessage(title);
/*     */     }
/* 116 */     this.title = title;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsLongRunningMcoTaskScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */