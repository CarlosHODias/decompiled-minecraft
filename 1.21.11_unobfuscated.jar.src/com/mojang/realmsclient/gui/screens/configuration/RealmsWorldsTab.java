/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import com.mojang.realmsclient.gui.RealmsWorldSlotButton;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsSelectWorldTemplateScreen;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchMinigameTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchSlotTask;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class RealmsWorldsTab extends GridLayoutTab implements RealmsConfigurationTab {
/*  25 */   static final Component TITLE = (Component)Component.translatable("mco.configure.worlds.title");
/*     */   
/*     */   private final RealmsConfigureWorldScreen configurationScreen;
/*     */   private final Minecraft minecraft;
/*     */   private RealmsServer serverData;
/*     */   private final Button optionsButton;
/*     */   private final Button backupButton;
/*     */   private final Button resetWorldButton;
/*  33 */   private final List<RealmsWorldSlotButton> slotButtonList = com.google.common.collect.Lists.newArrayList();
/*     */   
/*     */   RealmsWorldsTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData) {
/*  36 */     super(TITLE);
/*  37 */     this.configurationScreen = configurationScreen;
/*  38 */     this.minecraft = minecraft;
/*  39 */     this.serverData = serverData;
/*  40 */     GridLayout.RowHelper helper = this.layout.spacing(20).createRowHelper(1);
/*     */     
/*  42 */     GridLayout.RowHelper slots = new GridLayout().spacing(16).createRowHelper(4);
/*  43 */     this.slotButtonList.clear();
/*  44 */     for (int i = 1; i < 5; i++) {
/*  45 */       this.slotButtonList.add((RealmsWorldSlotButton)slots.addChild((LayoutElement)createSlotButton(i), LayoutSettings.defaults().alignVerticallyBottom()));
/*     */     }
/*  47 */     helper.addChild((LayoutElement)slots.getGrid());
/*     */     
/*  49 */     GridLayout.RowHelper buttons = new GridLayout().spacing(8).createRowHelper(1);
/*     */     
/*  51 */     this.optionsButton = (Button)buttons.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.buttons.options"), button -> minecraft.setScreen((Screen)new RealmsSlotOptionsScreen(configurationScreen, ((RealmsSlot)serverData.slots.get(serverData.activeSlot)).copy(), serverData.worldType, serverData.activeSlot)))
/*     */         
/*  53 */         .bounds(0, 0, 150, 20).build());
/*     */     
/*  55 */     this.backupButton = (Button)buttons.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.backup"), button -> minecraft.setScreen((Screen)new RealmsBackupScreen(configurationScreen, serverData.copy(), serverData.activeSlot)))
/*     */         
/*  57 */         .bounds(0, 0, 150, 20).build());
/*     */     
/*  59 */     this.resetWorldButton = (Button)buttons.addChild((LayoutElement)Button.builder((Component)Component.empty(), button -> resetButtonPressed())
/*     */         
/*  61 */         .bounds(0, 0, 150, 20).build());
/*     */     
/*  63 */     helper.addChild((LayoutElement)buttons.getGrid(), LayoutSettings.defaults().alignHorizontallyCenter());
/*  64 */     this.backupButton.active = true;
/*  65 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   private void resetButtonPressed() {
/*  69 */     if (isMinigame()) {
/*  70 */       this.minecraft.setScreen((Screen)new RealmsSelectWorldTemplateScreen((Component)Component.translatable("mco.template.title.minigame"), this::templateSelectionCallback, RealmsServer.WorldType.MINIGAME, null));
/*     */     } else {
/*  72 */       this.minecraft.setScreen((Screen)RealmsResetWorldScreen.forResetSlot((Screen)this.configurationScreen, this.serverData.copy(), () -> this.minecraft.execute(())));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void templateSelectionCallback(WorldTemplate worldTemplate) {
/*  77 */     if (worldTemplate != null && WorldTemplate.WorldTemplateType.MINIGAME == worldTemplate.type()) {
/*  78 */       this.configurationScreen.stateChanged();
/*  79 */       RealmsConfigureWorldScreen newScreen = this.configurationScreen.getNewScreen();
/*  80 */       this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)newScreen, new LongRunningTask[] { (LongRunningTask)new SwitchMinigameTask(this.serverData.id, worldTemplate, newScreen) }));
/*     */     } else {
/*  82 */       this.minecraft.setScreen((Screen)this.configurationScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMinigame() {
/*  87 */     return this.serverData.isMinigameActive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSelected(RealmsServer serverData) {
/*  92 */     updateData(serverData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/*  97 */     this.serverData = serverData;
/*  98 */     this.optionsButton.active = (!serverData.expired && !isMinigame());
/*  99 */     this.resetWorldButton.active = !serverData.expired;
/* 100 */     if (isMinigame()) {
/* 101 */       this.resetWorldButton.setMessage((Component)Component.translatable("mco.configure.world.buttons.switchminigame"));
/*     */     } else {
/* 103 */       boolean emptySlot = (serverData.slots.containsKey(serverData.activeSlot) && ((RealmsSlot)serverData.slots.get(serverData.activeSlot)).options.empty);
/* 104 */       if (emptySlot) {
/* 105 */         this.resetWorldButton.setMessage((Component)Component.translatable("mco.configure.world.buttons.newworld"));
/*     */       } else {
/* 107 */         this.resetWorldButton.setMessage((Component)Component.translatable("mco.configure.world.buttons.resetworld"));
/*     */       } 
/*     */     } 
/* 110 */     this.backupButton.active = !isMinigame();
/* 111 */     for (RealmsWorldSlotButton realmsWorldSlotButton : this.slotButtonList) {
/* 112 */       RealmsWorldSlotButton.State state = realmsWorldSlotButton.setServerData(serverData);
/* 113 */       if (state.activeSlot) {
/* 114 */         realmsWorldSlotButton.setSize(80, 80); continue;
/*     */       } 
/* 116 */       realmsWorldSlotButton.setSize(50, 50);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private RealmsWorldSlotButton createSlotButton(int i) {
/* 122 */     return new RealmsWorldSlotButton(0, 0, 80, 80, i, this.serverData, button -> {
/*     */           RealmsWorldSlotButton.State state = ((RealmsWorldSlotButton)i).getState();
/*     */           switch (state.action) {
/*     */             case NOTHING:
/*     */               break;
/*     */             case SWITCH_SLOT:
/*     */               if (state.minigame) {
/*     */                 switchToMinigame();
/*     */                 break;
/*     */               } 
/*     */               if (state.empty) {
/*     */                 switchToEmptySlot(i, this.serverData);
/*     */                 break;
/*     */               } 
/*     */               switchToFullSlot(i, this.serverData);
/*     */               break;
/*     */             default:
/*     */               throw new IllegalStateException("Unknown action " + String.valueOf(state.action));
/*     */           } 
/*     */         });
/*     */   }
/*     */   private void switchToMinigame() {
/* 144 */     RealmsSelectWorldTemplateScreen screen = new RealmsSelectWorldTemplateScreen((Component)Component.translatable("mco.template.title.minigame"), this::templateSelectionCallback, RealmsServer.WorldType.MINIGAME, null, 
/* 145 */         List.of(
/* 146 */           Component.translatable("mco.minigame.world.info.line1").withColor(-4539718), 
/* 147 */           Component.translatable("mco.minigame.world.info.line2").withColor(-4539718)));
/*     */ 
/*     */     
/* 150 */     this.minecraft.setScreen((Screen)screen);
/*     */   }
/*     */   
/*     */   private void switchToFullSlot(int selectedSlot, RealmsServer serverData) {
/* 154 */     this.minecraft.setScreen((Screen)RealmsPopups.infoPopupScreen((Screen)this.configurationScreen, 
/* 155 */           (Component)Component.translatable("mco.configure.world.slot.switch.question.line1"), popup -> {
/*     */             RealmsConfigureWorldScreen newScreen = this.configurationScreen.getNewScreen();
/*     */             this.configurationScreen.stateChanged();
/*     */             this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)newScreen, new LongRunningTask[] { (LongRunningTask)new SwitchSlotTask(serverData.id, serverData, ()) }));
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void switchToEmptySlot(int selectedSlot, RealmsServer serverData) {
/* 166 */     this.minecraft.setScreen(
/* 167 */         (Screen)RealmsPopups.infoPopupScreen((Screen)this.configurationScreen, 
/*     */           
/* 169 */           (Component)Component.translatable("mco.configure.world.slot.switch.question.line1"), popups -> {
/*     */             this.configurationScreen.stateChanged();
/*     */             RealmsResetWorldScreen resetWorldScreen = RealmsResetWorldScreen.forEmptySlot((Screen)this.configurationScreen, selectedSlot, selectedSlot, ());
/*     */             this.minecraft.setScreen((Screen)resetWorldScreen);
/*     */           }));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsWorldsTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */