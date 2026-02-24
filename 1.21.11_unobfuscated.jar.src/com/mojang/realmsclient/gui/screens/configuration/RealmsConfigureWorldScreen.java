/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.client.RealmsError;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.PreferredRegionsDto;
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RegionDataDto;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreferenceDto;
/*     */ import com.mojang.realmsclient.dto.ServiceQuality;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.util.RealmsUtil;
/*     */ import com.mojang.realmsclient.util.task.CloseServerTask;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.OpenServerTask;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.LoadingTab;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.components.tabs.TabNavigationBar;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.util.StringUtil;
/*     */ 
/*     */ public class RealmsConfigureWorldScreen extends RealmsScreen {
/*  49 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*  50 */   private static final Component PLAY_TEXT = (Component)Component.translatable("mco.selectServer.play");
/*     */   
/*     */   private final RealmsMainScreen lastScreen;
/*     */   
/*     */   private RealmsServer serverData;
/*     */   
/*     */   private PreferredRegionsDto regions;
/*     */   
/*  58 */   private final Map<RealmsRegion, ServiceQuality> regionServiceQuality = new LinkedHashMap<>();
/*     */   
/*     */   private final long serverId;
/*     */   
/*     */   private boolean stateChanged;
/*     */   
/*     */   private final TabManager tabManager;
/*     */   
/*     */   private Button playButton;
/*     */   
/*     */   private TabNavigationBar tabNavigationBar;
/*     */   final HeaderAndFooterLayout layout;
/*     */   
/*     */   public RealmsConfigureWorldScreen(RealmsMainScreen lastScreen, long serverId, RealmsServer serverData, PreferredRegionsDto regions) {
/*  72 */     super((Component)Component.empty()); this.tabManager = new TabManager(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0), x$0 -> rec$.removeWidget(x$0), this::onTabSelected, this::onTabDeselected); this.layout = new HeaderAndFooterLayout((Screen)this);
/*  73 */     this.lastScreen = lastScreen;
/*  74 */     this.serverId = serverId;
/*  75 */     this.serverData = serverData;
/*  76 */     this.regions = regions;
/*     */   }
/*     */   
/*     */   public RealmsConfigureWorldScreen(RealmsMainScreen lastScreen, long serverId) {
/*  80 */     this(lastScreen, serverId, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  85 */     if (this.serverData == null) {
/*  86 */       fetchServerData(this.serverId);
/*     */     }
/*     */     
/*  89 */     if (this.regions == null) {
/*  90 */       fetchRegionData();
/*     */     }
/*     */     
/*  93 */     MutableComponent mutableComponent = Component.translatable("mco.configure.world.loading");
/*  94 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 100 */       .tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new LoadingTab(getFont(), RealmsWorldsTab.TITLE, (Component)mutableComponent), (Tab)new LoadingTab(getFont(), RealmsPlayersTab.TITLE, (Component)mutableComponent), (Tab)new LoadingTab(getFont(), RealmsSubscriptionTab.TITLE, (Component)mutableComponent), (Tab)new LoadingTab(getFont(), RealmsSettingsTab.TITLE, (Component)mutableComponent) }).build();
/* 101 */     this.tabNavigationBar.setTabActiveState(3, false);
/* 102 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/*     */     
/* 104 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*     */     
/* 106 */     this.playButton = (Button)footer.addChild((LayoutElement)Button.builder(PLAY_TEXT, button -> {
/*     */             onClose();
/*     */             RealmsMainScreen.play(this.serverData, (Screen)this);
/* 109 */           }).width(150).build());
/* 110 */     this.playButton.active = false;
/*     */     
/* 112 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*     */     
/* 114 */     this.layout.visitWidgets(button -> {
/*     */           button.setTabOrderGroup(1);
/*     */           
/*     */           addRenderableWidget((GuiEventListener)button);
/*     */         });
/* 119 */     this.tabNavigationBar.selectTab(0, false);
/* 120 */     repositionElements();
/*     */     
/* 122 */     if (this.serverData != null && this.regions != null) {
/* 123 */       onRealmsDataFetched();
/*     */     }
/*     */   }
/*     */   
/*     */   private void onTabSelected(Tab tab) {
/* 128 */     if (this.serverData != null && tab instanceof RealmsConfigurationTab) { RealmsConfigurationTab configurationTab = (RealmsConfigurationTab)tab;
/* 129 */       configurationTab.onSelected(this.serverData); }
/*     */   
/*     */   }
/*     */   
/*     */   private void onTabDeselected(Tab tab) {
/* 134 */     if (this.serverData != null && tab instanceof RealmsConfigurationTab) { RealmsConfigurationTab configurationTab = (RealmsConfigurationTab)tab;
/* 135 */       configurationTab.onDeselected(this.serverData); }
/*     */   
/*     */   }
/*     */   
/*     */   public int getContentHeight() {
/* 140 */     return this.layout.getContentHeight();
/*     */   }
/*     */   
/*     */   public int getHeaderHeight() {
/* 144 */     return this.layout.getHeaderHeight();
/*     */   }
/*     */   
/*     */   public Screen getLastScreen() {
/* 148 */     return (Screen)this.lastScreen;
/*     */   }
/*     */   
/*     */   public Screen createErrorScreen(RealmsServiceException exception) {
/* 152 */     return (Screen)new RealmsGenericErrorScreen(exception, (Screen)this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   public void repositionElements() {
/* 157 */     if (this.tabNavigationBar == null) {
/*     */       return;
/*     */     }
/* 160 */     this.tabNavigationBar.setWidth(this.width);
/* 161 */     this.tabNavigationBar.arrangeElements();
/*     */     
/* 163 */     int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
/* 164 */     ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
/* 165 */     this.tabManager.setTabArea(tabArea);
/* 166 */     this.layout.setHeaderHeight(tabAreaTop);
/* 167 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private void updateButtonStates() {
/* 171 */     if (this.serverData != null && this.playButton != null) {
/* 172 */       this.playButton.active = this.serverData.shouldPlayButtonBeActive();
/* 173 */       if (!this.playButton.active && this.serverData.state == RealmsServer.State.CLOSED) {
/* 174 */         this.playButton.setTooltip(Tooltip.create(RealmsServer.WORLD_CLOSED_COMPONENT));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 181 */     super.render(graphics, xm, ym, a);
/* 182 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 187 */     if (this.tabNavigationBar.keyPressed(event)) {
/* 188 */       return true;
/*     */     }
/* 190 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphics graphics) {
/* 195 */     graphics.blit(RenderPipelines.GUI_TEXTURED, net.minecraft.client.gui.screens.worldselection.CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
/* 196 */     renderMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 201 */     if (this.serverData != null) { Tab tab = this.tabManager.getCurrentTab(); if (tab instanceof RealmsConfigurationTab) { RealmsConfigurationTab realmsConfigurationTab = (RealmsConfigurationTab)tab;
/* 202 */         realmsConfigurationTab.onDeselected(this.serverData); }
/*     */        }
/* 204 */      this.minecraft.setScreen((Screen)this.lastScreen);
/* 205 */     if (this.stateChanged) {
/* 206 */       this.lastScreen.resetScreen();
/*     */     }
/*     */   }
/*     */   
/*     */   public void fetchRegionData() {
/* 211 */     RealmsUtil.supplyAsync(RealmsClient::getPreferredRegionSelections, 
/*     */         
/* 213 */         RealmsUtil.openScreenAndLogOnFailure(this::createErrorScreen, "Couldn't get realms region data"))
/* 214 */       .thenAcceptAsync(regions -> {
/*     */           this.regions = regions;
/*     */           onRealmsDataFetched();
/*     */         }, (Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   public void fetchServerData(long realmId) {
/* 221 */     RealmsUtil.supplyAsync(client -> client.getOwnRealm(realmId), 
/*     */         
/* 223 */         RealmsUtil.openScreenAndLogOnFailure(this::createErrorScreen, "Couldn't get own world"))
/* 224 */       .thenAcceptAsync(serverData -> {
/*     */           this.serverData = serverData;
/*     */           onRealmsDataFetched();
/*     */         }, (Executor)this.minecraft);
/*     */   }
/*     */   
/*     */   private void onRealmsDataFetched() {
/* 231 */     if (this.serverData == null || this.regions == null) {
/*     */       return;
/*     */     }
/*     */     
/* 235 */     this.regionServiceQuality.clear();
/* 236 */     for (RegionDataDto region : (Iterable<RegionDataDto>)this.regions.regionData()) {
/* 237 */       if (region.region() != RealmsRegion.INVALID_REGION) {
/* 238 */         this.regionServiceQuality.put(region.region(), region.serviceQuality());
/*     */       }
/*     */     } 
/*     */     
/* 242 */     int focusedTabIndex = -1;
/* 243 */     if (this.tabNavigationBar != null) {
/* 244 */       focusedTabIndex = this.tabNavigationBar.getTabs().indexOf(this.tabManager.getCurrentTab());
/*     */     }
/*     */     
/* 247 */     if (this.tabNavigationBar != null) {
/* 248 */       removeWidget((GuiEventListener)this.tabNavigationBar);
/*     */     }
/*     */     
/* 251 */     this.tabNavigationBar = (TabNavigationBar)addRenderableWidget((GuiEventListener)TabNavigationBar.builder(this.tabManager, this.width)
/* 252 */         .addTabs(new Tab[] {
/* 253 */             (Tab)new RealmsWorldsTab(this, java.util.Objects.<Minecraft>requireNonNull(this.minecraft), this.serverData), (Tab)new RealmsPlayersTab(this, this.minecraft, this.serverData), (Tab)new RealmsSubscriptionTab(this, this.minecraft, this.serverData), (Tab)new RealmsSettingsTab(this, this.minecraft, this.serverData, this.regionServiceQuality)
/*     */ 
/*     */ 
/*     */           
/* 257 */           }).build());
/*     */     
/* 259 */     setFocused((GuiEventListener)this.tabNavigationBar);
/* 260 */     if (focusedTabIndex != -1) {
/* 261 */       this.tabNavigationBar.selectTab(focusedTabIndex, false);
/*     */     }
/*     */     
/* 264 */     this.tabNavigationBar.setTabActiveState(3, !this.serverData.expired);
/* 265 */     if (this.serverData.expired) {
/* 266 */       this.tabNavigationBar.setTabTooltip(3, Tooltip.create((Component)Component.translatable("mco.configure.world.settings.expired")));
/*     */     } else {
/* 268 */       this.tabNavigationBar.setTabTooltip(3, null);
/*     */     } 
/*     */     
/* 271 */     updateButtonStates();
/* 272 */     repositionElements();
/*     */   }
/*     */   
/*     */   public void saveSlotSettings(RealmsSlot slot) {
/* 276 */     RealmsSlot oldSlot = (RealmsSlot)this.serverData.slots.get(this.serverData.activeSlot);
/* 277 */     slot.options.templateId = oldSlot.options.templateId;
/* 278 */     slot.options.templateImage = oldSlot.options.templateImage;
/*     */     
/* 280 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 282 */       if (this.serverData.activeSlot != slot.slotId) {
/* 283 */         throw new RealmsServiceException(RealmsError.CustomError.configurationError());
/*     */       }
/*     */       
/* 286 */       client.updateSlot(this.serverData.id, slot.slotId, slot.options, slot.settings);
/* 287 */       this.serverData.slots.put(this.serverData.activeSlot, slot);
/* 288 */       if (slot.options.gameMode != oldSlot.options.gameMode || slot.isHardcore() != oldSlot.isHardcore()) {
/* 289 */         RealmsMainScreen.refreshServerList();
/*     */       }
/* 291 */       stateChanged();
/* 292 */     } catch (RealmsServiceException e) {
/* 293 */       LOGGER.error("Couldn't save slot settings", (Throwable)e);
/* 294 */       this.minecraft.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)this));
/*     */       
/*     */       return;
/*     */     } 
/* 298 */     this.minecraft.setScreen((Screen)this);
/*     */   }
/*     */   
/*     */   public void saveSettings(String name, String desc, RegionSelectionPreference preference, RealmsRegion region) {
/* 302 */     String description = StringUtil.isBlank(desc) ? "" : desc;
/* 303 */     String finalName = StringUtil.isBlank(name) ? "" : name;
/*     */     
/* 305 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 307 */       RealmsSlot realmsSlot = (RealmsSlot)this.serverData.slots.get(this.serverData.activeSlot);
/* 308 */       RealmsRegion regionSelection = (preference == RegionSelectionPreference.MANUAL) ? region : null;
/* 309 */       RegionSelectionPreferenceDto regionSelectionPreference = new RegionSelectionPreferenceDto(preference, regionSelection);
/* 310 */       client.updateConfiguration(this.serverData.id, finalName, description, regionSelectionPreference, realmsSlot.slotId, realmsSlot.options, realmsSlot.settings);
/* 311 */       this.serverData.regionSelectionPreference = regionSelectionPreference;
/* 312 */       this.serverData.name = name;
/* 313 */       this.serverData.motd = description;
/* 314 */       stateChanged();
/* 315 */     } catch (RealmsServiceException e) {
/* 316 */       LOGGER.error("Couldn't save settings", (Throwable)e);
/* 317 */       this.minecraft.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)this));
/*     */       
/*     */       return;
/*     */     } 
/* 321 */     this.minecraft.setScreen((Screen)this);
/*     */   }
/*     */   
/*     */   public void openTheWorld(boolean join) {
/* 325 */     RealmsConfigureWorldScreen screenWithKnownData = getNewScreenWithKnownData(this.serverData);
/* 326 */     this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)getNewScreen(), new LongRunningTask[] { (LongRunningTask)new OpenServerTask(this.serverData, (Screen)screenWithKnownData, join, this.minecraft) }));
/*     */   }
/*     */   
/*     */   public void closeTheWorld() {
/* 330 */     RealmsConfigureWorldScreen screenWithKnownData = getNewScreenWithKnownData(this.serverData);
/* 331 */     this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)getNewScreen(), new LongRunningTask[] { (LongRunningTask)new CloseServerTask(this.serverData, screenWithKnownData) }));
/*     */   }
/*     */   
/*     */   public void stateChanged() {
/* 335 */     this.stateChanged = true;
/* 336 */     if (this.tabNavigationBar != null) {
/* 337 */       for (Tab child : (Iterable<Tab>)this.tabNavigationBar.getTabs()) {
/* 338 */         if (child instanceof RealmsConfigurationTab) { RealmsConfigurationTab tab = (RealmsConfigurationTab)child;
/* 339 */           tab.updateData(this.serverData); }
/*     */       
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean invitePlayer(long serverId, String name) {
/* 346 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 348 */       List<PlayerInfo> players = client.invite(serverId, name);
/* 349 */       if (this.serverData != null) {
/* 350 */         this.serverData.players = players;
/*     */       } else {
/* 352 */         this.serverData = client.getOwnRealm(serverId);
/*     */       } 
/* 354 */       stateChanged();
/* 355 */     } catch (RealmsServiceException e) {
/* 356 */       LOGGER.error("Couldn't invite user", (Throwable)e);
/* 357 */       return false;
/*     */     } 
/* 359 */     return true;
/*     */   }
/*     */   
/*     */   public RealmsConfigureWorldScreen getNewScreen() {
/* 363 */     RealmsConfigureWorldScreen realmsConfigureWorldScreen = new RealmsConfigureWorldScreen(this.lastScreen, this.serverId);
/* 364 */     realmsConfigureWorldScreen.stateChanged = this.stateChanged;
/* 365 */     return realmsConfigureWorldScreen;
/*     */   }
/*     */   
/*     */   public RealmsConfigureWorldScreen getNewScreenWithKnownData(RealmsServer serverData) {
/* 369 */     RealmsConfigureWorldScreen realmsConfigureWorldScreen = new RealmsConfigureWorldScreen(this.lastScreen, this.serverId, serverData, this.regions);
/* 370 */     realmsConfigureWorldScreen.stateChanged = this.stateChanged;
/* 371 */     return realmsConfigureWorldScreen;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsConfigureWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */