/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.util.task.RealmCreationTask;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.StringUtil;
/*     */ 
/*     */ public class RealmsCreateRealmScreen extends net.minecraft.realms.RealmsScreen {
/*  24 */   private static final Component CREATE_REALM_TEXT = (Component)Component.translatable("mco.selectServer.create");
/*  25 */   private static final Component NAME_LABEL = (Component)Component.translatable("mco.configure.world.name");
/*  26 */   private static final Component DESCRIPTION_LABEL = (Component)Component.translatable("mco.configure.world.description");
/*     */   
/*     */   private static final int BUTTON_SPACING = 10;
/*     */   
/*     */   private static final int CONTENT_WIDTH = 210;
/*     */   
/*     */   private final RealmsMainScreen lastScreen;
/*  33 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private EditBox nameBox;
/*     */   private EditBox descriptionBox;
/*     */   private final Runnable createWorldRunnable;
/*     */   
/*     */   public RealmsCreateRealmScreen(RealmsMainScreen lastScreen, RealmsServer server, boolean isSnapshot) {
/*  40 */     super(CREATE_REALM_TEXT);
/*  41 */     this.lastScreen = lastScreen;
/*  42 */     this.createWorldRunnable = (() -> createWorld(server, isSnapshot));
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  47 */     this.layout.addTitleHeader(this.title, this.font);
/*     */     
/*  49 */     LinearLayout content = ((LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical())).spacing(10);
/*     */     
/*  51 */     Button createButton = Button.builder(CommonComponents.GUI_CONTINUE, button -> this.createWorldRunnable.run()).build();
/*  52 */     createButton.active = false;
/*     */     
/*  54 */     this.nameBox = new EditBox(this.font, 210, 20, NAME_LABEL);
/*  55 */     this.nameBox.setResponder(value -> createButton.active = !StringUtil.isBlank(value));
/*  56 */     this.descriptionBox = new EditBox(this.font, 210, 20, DESCRIPTION_LABEL);
/*     */     
/*  58 */     content.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.nameBox, NAME_LABEL));
/*  59 */     content.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)this.descriptionBox, DESCRIPTION_LABEL));
/*     */     
/*  61 */     LinearLayout bottomButtons = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(10));
/*  62 */     bottomButtons.addChild((LayoutElement)createButton);
/*  63 */     bottomButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*     */     
/*  65 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  66 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  71 */     setInitialFocus((GuiEventListener)this.nameBox);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  76 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private void createWorld(RealmsServer server, boolean initializeSnapshotRealm) {
/*  80 */     if (!server.isSnapshotRealm() && initializeSnapshotRealm) {
/*  81 */       AtomicBoolean canceled = new AtomicBoolean();
/*  82 */       this.minecraft.setScreen((Screen)new net.minecraft.client.gui.screens.AlertScreen(() -> {
/*     */               canceled.set(true);
/*     */               this.lastScreen.resetScreen();
/*     */               this.minecraft.setScreen((Screen)this.lastScreen);
/*  86 */             }, (Component)Component.translatable("mco.upload.preparing"), (Component)Component.empty()));
/*  87 */       java.util.concurrent.CompletableFuture.supplyAsync(() -> createSnapshotRealm(server), (Executor)net.minecraft.util.Util.backgroundExecutor())
/*  88 */         .thenAcceptAsync(snapshotServer -> {
/*     */             
/*     */             if (!canceled.get()) {
/*     */               showResetWorldScreen(canceled);
/*     */             }
/*  93 */           }, (Executor)this.minecraft).exceptionallyAsync(ex -> {
/*     */             MutableComponent mutableComponent; this.lastScreen.resetScreen(); Throwable patt0$temp = ex.getCause();
/*     */             if (patt0$temp instanceof RealmsServiceException) {
/*     */               RealmsServiceException realmsServiceException = (RealmsServiceException)patt0$temp;
/*     */               Component errorMessage = realmsServiceException.realmsError.errorMessage();
/*     */             } else {
/*     */               mutableComponent = Component.translatable("mco.errorMessage.initialize.failed");
/*     */             } 
/*     */             this.minecraft.setScreen((Screen)new RealmsGenericErrorScreen((Component)mutableComponent, (Screen)this.lastScreen));
/*     */             return null;
/*     */           }, (Executor)this.minecraft);
/*     */     } else {
/* 105 */       showResetWorldScreen(server);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static RealmsServer createSnapshotRealm(RealmsServer server) {
/* 110 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 112 */       return client.createSnapshotRealm(server.id);
/* 113 */     } catch (RealmsServiceException e) {
/* 114 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void showResetWorldScreen(RealmsServer server) {
/* 119 */     RealmCreationTask realmCreationTask = new RealmCreationTask(server.id, this.nameBox.getValue(), this.descriptionBox.getValue());
/* 120 */     RealmsResetWorldScreen resetWorldScreen = RealmsResetWorldScreen.forNewRealm((Screen)this, server, realmCreationTask, () -> this.minecraft.execute(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     this.minecraft.setScreen((Screen)resetWorldScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 132 */     this.minecraft.setScreen((Screen)this.lastScreen);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsCreateRealmScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */