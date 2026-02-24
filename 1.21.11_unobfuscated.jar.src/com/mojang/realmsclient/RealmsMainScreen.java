/*      */ package com.mojang.realmsclient;
/*      */ import com.google.common.util.concurrent.RateLimiter;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.realmsclient.client.Ping;
/*      */ import com.mojang.realmsclient.client.RealmsClient;
/*      */ import com.mojang.realmsclient.dto.PingResult;
/*      */ import com.mojang.realmsclient.dto.RealmsNews;
/*      */ import com.mojang.realmsclient.dto.RealmsNotification;
/*      */ import com.mojang.realmsclient.dto.RealmsServer;
/*      */ import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
/*      */ import com.mojang.realmsclient.dto.RegionPingResult;
/*      */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*      */ import com.mojang.realmsclient.gui.RealmsDataFetcher;
/*      */ import com.mojang.realmsclient.gui.RealmsServerList;
/*      */ import com.mojang.realmsclient.gui.screens.AddRealmPopupScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
/*      */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*      */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*      */ import com.mojang.realmsclient.gui.task.DataFetcher;
/*      */ import com.mojang.realmsclient.util.RealmsPersistence;
/*      */ import com.mojang.realmsclient.util.RealmsUtil;
/*      */ import com.mojang.realmsclient.util.task.GetServerDetailsTask;
/*      */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.Font;
/*      */ import net.minecraft.client.gui.GuiGraphics;
/*      */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*      */ import net.minecraft.client.gui.components.AbstractWidget;
/*      */ import net.minecraft.client.gui.components.Button;
/*      */ import net.minecraft.client.gui.components.CycleButton;
/*      */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*      */ import net.minecraft.client.gui.components.ImageButton;
/*      */ import net.minecraft.client.gui.components.ImageWidget;
/*      */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*      */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*      */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*      */ import net.minecraft.client.gui.components.PopupScreen;
/*      */ import net.minecraft.client.gui.components.SpriteIconButton;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.components.WidgetSprites;
/*      */ import net.minecraft.client.gui.components.WidgetTooltipHolder;
/*      */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*      */ import net.minecraft.client.gui.layouts.FrameLayout;
/*      */ import net.minecraft.client.gui.layouts.GridLayout;
/*      */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*      */ import net.minecraft.client.gui.layouts.Layout;
/*      */ import net.minecraft.client.gui.layouts.LayoutElement;
/*      */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*      */ import net.minecraft.client.gui.layouts.LinearLayout;
/*      */ import net.minecraft.client.gui.layouts.SpacerElement;
/*      */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*      */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientActivePlayersTooltip;
/*      */ import net.minecraft.client.input.KeyEvent;
/*      */ import net.minecraft.client.input.MouseButtonEvent;
/*      */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.FormattedText;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.realms.RealmsScreen;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.item.component.ResolvableProfile;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class RealmsMainScreen extends RealmsScreen {
/*   91 */   private static final Identifier INFO_SPRITE = Identifier.withDefaultNamespace("icon/info");
/*   92 */   private static final Identifier NEW_REALM_SPRITE = Identifier.withDefaultNamespace("icon/new_realm");
/*   93 */   private static final Identifier EXPIRED_SPRITE = Identifier.withDefaultNamespace("realm_status/expired");
/*   94 */   private static final Identifier EXPIRES_SOON_SPRITE = Identifier.withDefaultNamespace("realm_status/expires_soon");
/*   95 */   private static final Identifier OPEN_SPRITE = Identifier.withDefaultNamespace("realm_status/open");
/*   96 */   private static final Identifier CLOSED_SPRITE = Identifier.withDefaultNamespace("realm_status/closed");
/*   97 */   private static final Identifier INVITE_SPRITE = Identifier.withDefaultNamespace("icon/invite");
/*   98 */   private static final Identifier NEWS_SPRITE = Identifier.withDefaultNamespace("icon/news");
/*   99 */   public static final Identifier HARDCORE_MODE_SPRITE = Identifier.withDefaultNamespace("hud/heart/hardcore_full");
/*      */   
/*  101 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  103 */   private static final Identifier NO_REALMS_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/no_realms.png");
/*      */   
/*  105 */   private static final Component TITLE = (Component)Component.translatable("menu.online");
/*  106 */   private static final Component LOADING_TEXT = (Component)Component.translatable("mco.selectServer.loading");
/*  107 */   private static final Component SERVER_UNITIALIZED_TEXT = (Component)Component.translatable("mco.selectServer.uninitialized");
/*  108 */   private static final Component SUBSCRIPTION_EXPIRED_TEXT = (Component)Component.translatable("mco.selectServer.expiredList");
/*  109 */   private static final Component SUBSCRIPTION_RENEW_TEXT = (Component)Component.translatable("mco.selectServer.expiredRenew");
/*  110 */   private static final Component TRIAL_EXPIRED_TEXT = (Component)Component.translatable("mco.selectServer.expiredTrial");
/*  111 */   private static final Component PLAY_TEXT = (Component)Component.translatable("mco.selectServer.play");
/*  112 */   private static final Component LEAVE_SERVER_TEXT = (Component)Component.translatable("mco.selectServer.leave");
/*  113 */   private static final Component CONFIGURE_SERVER_TEXT = (Component)Component.translatable("mco.selectServer.configure");
/*  114 */   private static final Component SERVER_EXPIRED_TOOLTIP = (Component)Component.translatable("mco.selectServer.expired");
/*  115 */   private static final Component SERVER_EXPIRES_SOON_TOOLTIP = (Component)Component.translatable("mco.selectServer.expires.soon");
/*  116 */   private static final Component SERVER_EXPIRES_IN_DAY_TOOLTIP = (Component)Component.translatable("mco.selectServer.expires.day");
/*  117 */   private static final Component SERVER_OPEN_TOOLTIP = (Component)Component.translatable("mco.selectServer.open");
/*  118 */   private static final Component SERVER_CLOSED_TOOLTIP = (Component)Component.translatable("mco.selectServer.closed");
/*  119 */   private static final Component UNITIALIZED_WORLD_NARRATION = (Component)Component.translatable("gui.narrate.button", new Object[] { SERVER_UNITIALIZED_TEXT });
/*  120 */   private static final Component NO_REALMS_TEXT = (Component)Component.translatable("mco.selectServer.noRealms");
/*  121 */   private static final Component NO_PENDING_INVITES = (Component)Component.translatable("mco.invites.nopending");
/*  122 */   private static final Component PENDING_INVITES = (Component)Component.translatable("mco.invites.pending");
/*  123 */   private static final Component INCOMPATIBLE_POPUP_TITLE = (Component)Component.translatable("mco.compatibility.incompatible.popup.title");
/*  124 */   private static final Component INCOMPATIBLE_RELEASE_TYPE_POPUP_MESSAGE = (Component)Component.translatable("mco.compatibility.incompatible.releaseType.popup.message");
/*      */   
/*      */   private static final int BUTTON_WIDTH = 100;
/*      */   
/*      */   private static final int BUTTON_COLUMNS = 3;
/*      */   private static final int BUTTON_SPACING = 4;
/*      */   private static final int CONTENT_WIDTH = 308;
/*      */   private static final int LOGO_PADDING = 5;
/*      */   private static final int HEADER_HEIGHT = 44;
/*      */   private static final int FOOTER_PADDING = 11;
/*      */   private static final int NEW_REALM_SPRITE_WIDTH = 40;
/*      */   private static final int NEW_REALM_SPRITE_HEIGHT = 20;
/*  136 */   private static final boolean SNAPSHOT = !SharedConstants.getCurrentVersion().stable();
/*  137 */   private static boolean snapshotToggle = SNAPSHOT;
/*      */   
/*  139 */   private final CompletableFuture<RealmsAvailability.Result> availability = RealmsAvailability.get();
/*      */   
/*      */   private DataFetcher.Subscription dataSubscription;
/*      */   
/*  143 */   private final Set<UUID> handledSeenNotifications = new HashSet<>();
/*      */   
/*      */   private static boolean regionsPinged;
/*      */   
/*      */   private final RateLimiter inviteNarrationLimiter;
/*      */   
/*      */   private final Screen lastScreen;
/*      */   
/*      */   private Button playButton;
/*      */   
/*      */   private Button backButton;
/*      */   private Button renewButton;
/*      */   private Button configureButton;
/*      */   private Button leaveButton;
/*      */   private RealmSelectionList realmSelectionList;
/*      */   private RealmsServerList serverList;
/*  159 */   private List<RealmsServer> availableSnapshotServers = List.of();
/*  160 */   private RealmsServerPlayerLists onlinePlayersPerRealm = new RealmsServerPlayerLists(Map.of());
/*      */   
/*      */   private volatile boolean trialsAvailable;
/*      */   
/*      */   private volatile String newsLink;
/*  165 */   private final List<RealmsNotification> notifications = new ArrayList<>();
/*      */   
/*      */   private Button addRealmButton;
/*      */   
/*      */   private NotificationButton pendingInvitesButton;
/*      */   
/*      */   private NotificationButton newsButton;
/*      */   private LayoutState activeLayoutState;
/*      */   private HeaderAndFooterLayout layout;
/*      */   
/*      */   public RealmsMainScreen(Screen lastScreen) {
/*  176 */     super(TITLE);
/*  177 */     this.lastScreen = lastScreen;
/*  178 */     this.inviteNarrationLimiter = RateLimiter.create(0.01666666753590107D);
/*      */   }
/*      */ 
/*      */   
/*      */   public void init() {
/*  183 */     this.serverList = new RealmsServerList(this.minecraft);
/*      */     
/*  185 */     this.realmSelectionList = new RealmSelectionList();
/*      */     
/*  187 */     MutableComponent mutableComponent1 = Component.translatable("mco.invites.title");
/*  188 */     this.pendingInvitesButton = new NotificationButton((Component)mutableComponent1, INVITE_SPRITE, b -> this.minecraft.setScreen((Screen)new RealmsPendingInvitesScreen((Screen)this, invitesTitle)), null);
/*      */     
/*  190 */     MutableComponent mutableComponent2 = Component.translatable("mco.news");
/*  191 */     this.newsButton = new NotificationButton((Component)mutableComponent2, NEWS_SPRITE, b -> { String newsLink = this.newsLink; if (newsLink == null) return;  ConfirmLinkScreen.confirmLinkNow((Screen)this, newsLink); if (this.newsButton.notificationCount() != 0) { RealmsPersistence.RealmsPersistenceData data = RealmsPersistence.readFile(); data.hasUnreadNews = false; RealmsPersistence.writeFile(data); this.newsButton.setNotificationCount(0); }  }, (Component)mutableComponent2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  207 */     this.playButton = Button.builder(PLAY_TEXT, button -> play(getSelectedServer(), (Screen)this)).width(100).build();
/*  208 */     this.configureButton = Button.builder(CONFIGURE_SERVER_TEXT, button -> configureClicked(getSelectedServer())).width(100).build();
/*  209 */     this.renewButton = Button.builder(SUBSCRIPTION_RENEW_TEXT, button -> onRenew(getSelectedServer())).width(100).build();
/*      */     
/*  211 */     this.leaveButton = Button.builder(LEAVE_SERVER_TEXT, button -> leaveClicked(getSelectedServer())).width(100).build();
/*  212 */     this.addRealmButton = Button.builder((Component)Component.translatable("mco.selectServer.purchase"), button -> openTrialAvailablePopup()).size(100, 20).build();
/*  213 */     this.backButton = Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(100).build();
/*      */     
/*  215 */     if (RealmsClient.ENVIRONMENT == RealmsClient.Environment.STAGE) {
/*  216 */       addRenderableWidget((GuiEventListener)CycleButton.booleanBuilder((Component)Component.literal("Snapshot"), (Component)Component.literal("Release"), snapshotToggle).create(5, 5, 100, 20, (Component)Component.literal("Realm"), (button, value) -> {
/*      */               snapshotToggle = value;
/*      */               
/*      */               this.availableSnapshotServers = List.of();
/*      */               debugRefreshDataFetchers();
/*      */             }));
/*      */     }
/*  223 */     updateLayout(LayoutState.LOADING);
/*  224 */     updateButtonStates();
/*      */     
/*  226 */     this.availability.thenAcceptAsync(result -> { Screen errorScreen = result.createErrorScreen(this.lastScreen); if (errorScreen == null) { this.dataSubscription = initDataFetcher(this.minecraft.realmsDataFetcher()); } else { this.minecraft.setScreen(errorScreen); }  }, this.screenExecutor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSnapshot() {
/*  237 */     return (SNAPSHOT && snapshotToggle);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void repositionElements() {
/*  242 */     if (this.layout != null) {
/*  243 */       this.realmSelectionList.updateSize(this.width, this.layout);
/*  244 */       this.layout.arrangeElements();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onClose() {
/*  250 */     this.minecraft.setScreen(this.lastScreen);
/*      */   }
/*      */   
/*      */   private void updateLayout() {
/*  254 */     if (this.serverList.isEmpty() && this.availableSnapshotServers.isEmpty() && this.notifications.isEmpty()) {
/*  255 */       updateLayout(LayoutState.NO_REALMS);
/*      */     } else {
/*  257 */       updateLayout(LayoutState.LIST);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateLayout(LayoutState state) {
/*  262 */     if (this.activeLayoutState == state) {
/*      */       return;
/*      */     }
/*  265 */     if (this.layout != null) {
/*  266 */       this.layout.visitWidgets(x$0 -> rec$.removeWidget(x$0));
/*      */     }
/*  268 */     this.layout = createLayout(state);
/*  269 */     this.activeLayoutState = state;
/*  270 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  271 */     repositionElements();
/*      */   }
/*      */   
/*      */   private HeaderAndFooterLayout createLayout(LayoutState state) {
/*  275 */     HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*  276 */     layout.setHeaderHeight(44);
/*  277 */     layout.addToHeader((LayoutElement)createHeader());
/*      */     
/*  279 */     Layout footer = createFooter(state);
/*  280 */     footer.arrangeElements();
/*  281 */     layout.setFooterHeight(footer.getHeight() + 22);
/*  282 */     layout.addToFooter((LayoutElement)footer);
/*      */     
/*  284 */     switch (state.ordinal()) { case 0:
/*  285 */         layout.addToContents((LayoutElement)new LoadingDotsWidget(this.font, LOADING_TEXT)); break;
/*  286 */       case 1: layout.addToContents((LayoutElement)createNoRealmsContent()); break;
/*  287 */       case 2: layout.addToContents((LayoutElement)this.realmSelectionList);
/*      */         break; }
/*      */     
/*  290 */     return layout;
/*      */   }
/*      */   
/*      */   private Layout createHeader() {
/*  294 */     int sideCellWidth = 90;
/*      */     
/*  296 */     LinearLayout buttons = LinearLayout.horizontal().spacing(4);
/*  297 */     buttons.defaultCellSetting().alignVerticallyMiddle();
/*  298 */     buttons.addChild((LayoutElement)this.pendingInvitesButton);
/*  299 */     buttons.addChild((LayoutElement)this.newsButton);
/*      */     
/*  301 */     LinearLayout header = LinearLayout.horizontal();
/*  302 */     header.defaultCellSetting().alignVerticallyMiddle();
/*      */     
/*  304 */     header.addChild((LayoutElement)SpacerElement.width(90));
/*  305 */     header.addChild((LayoutElement)realmsLogo(), LayoutSettings::alignHorizontallyCenter);
/*  306 */     ((FrameLayout)header.addChild((LayoutElement)new FrameLayout(90, 44))).addChild((LayoutElement)buttons, LayoutSettings::alignHorizontallyRight);
/*      */     
/*  308 */     return (Layout)header;
/*      */   }
/*      */   
/*      */   private Layout createFooter(LayoutState state) {
/*  312 */     GridLayout footer = new GridLayout().spacing(4);
/*  313 */     GridLayout.RowHelper helper = footer.createRowHelper(3);
/*      */     
/*  315 */     if (state == LayoutState.LIST) {
/*  316 */       helper.addChild((LayoutElement)this.playButton);
/*  317 */       helper.addChild((LayoutElement)this.configureButton);
/*  318 */       helper.addChild((LayoutElement)this.renewButton);
/*  319 */       helper.addChild((LayoutElement)this.leaveButton);
/*      */     } 
/*  321 */     helper.addChild((LayoutElement)this.addRealmButton);
/*  322 */     helper.addChild((LayoutElement)this.backButton);
/*      */     
/*  324 */     return (Layout)footer;
/*      */   }
/*      */   
/*      */   private LinearLayout createNoRealmsContent() {
/*  328 */     LinearLayout content = LinearLayout.vertical().spacing(8);
/*  329 */     content.defaultCellSetting().alignHorizontallyCenter();
/*      */     
/*  331 */     content.addChild((LayoutElement)ImageWidget.texture(130, 64, NO_REALMS_LOCATION, 130, 64));
/*      */     
/*  333 */     content.addChild((LayoutElement)FocusableTextWidget.builder(NO_REALMS_TEXT, this.font).maxWidth(308).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build());
/*      */     
/*  335 */     return content;
/*      */   }
/*      */   
/*      */   private void updateButtonStates() {
/*  339 */     RealmsServer server = getSelectedServer();
/*  340 */     boolean serverSelected = (server != null);
/*  341 */     this.addRealmButton.active = (this.activeLayoutState != LayoutState.LOADING);
/*  342 */     this.playButton.active = (serverSelected && server.shouldPlayButtonBeActive());
/*  343 */     if (!this.playButton.active && serverSelected && server.state == RealmsServer.State.CLOSED) {
/*  344 */       this.playButton.setTooltip(Tooltip.create(RealmsServer.WORLD_CLOSED_COMPONENT));
/*      */     }
/*  346 */     this.renewButton.active = (serverSelected && shouldRenewButtonBeActive(server));
/*  347 */     this.leaveButton.active = (serverSelected && shouldLeaveButtonBeActive(server));
/*  348 */     this.configureButton.active = (serverSelected && shouldConfigureButtonBeActive(server));
/*      */   }
/*      */   
/*      */   private boolean shouldRenewButtonBeActive(RealmsServer server) {
/*  352 */     return (server.expired && isSelfOwnedServer(server));
/*      */   }
/*      */   
/*      */   private boolean shouldConfigureButtonBeActive(RealmsServer server) {
/*  356 */     return (isSelfOwnedServer(server) && server.state != RealmsServer.State.UNINITIALIZED);
/*      */   }
/*      */   
/*      */   private boolean shouldLeaveButtonBeActive(RealmsServer server) {
/*  360 */     return !isSelfOwnedServer(server);
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  365 */     super.tick();
/*      */     
/*  367 */     if (this.dataSubscription != null) {
/*  368 */       this.dataSubscription.tick();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void refreshPendingInvites() {
/*  373 */     (Minecraft.getInstance().realmsDataFetcher()).pendingInvitesTask.reset();
/*      */   }
/*      */   
/*      */   public static void refreshServerList() {
/*  377 */     (Minecraft.getInstance().realmsDataFetcher()).serverListUpdateTask.reset();
/*      */   }
/*      */   
/*      */   private void debugRefreshDataFetchers() {
/*  381 */     for (DataFetcher.Task<?> task : (Iterable<DataFetcher.Task<?>>)this.minecraft.realmsDataFetcher().getTasks()) {
/*  382 */       task.reset();
/*      */     }
/*      */   }
/*      */   
/*      */   private DataFetcher.Subscription initDataFetcher(RealmsDataFetcher dataSource) {
/*  387 */     DataFetcher.Subscription result = dataSource.dataFetcher.createSubscription();
/*      */     
/*  389 */     result.subscribe(dataSource.serverListUpdateTask, updatedServers -> {
/*      */           this.serverList.updateServersList(updatedServers.serverList());
/*      */           
/*      */           this.availableSnapshotServers = updatedServers.availableSnapshotServers();
/*      */           
/*      */           refreshListAndLayout();
/*      */           
/*      */           boolean ownsNonExpiredRealmServer = false;
/*      */           for (RealmsServer retrievedServer : (Iterable<RealmsServer>)this.serverList) {
/*      */             if (isSelfOwnedNonExpiredServer(retrievedServer)) {
/*      */               ownsNonExpiredRealmServer = true;
/*      */             }
/*      */           } 
/*      */           if (!regionsPinged && ownsNonExpiredRealmServer) {
/*      */             regionsPinged = true;
/*      */             pingRegions();
/*      */           } 
/*      */         });
/*  407 */     callRealmsClient(RealmsClient::getNotifications, retrievedNotifications -> {
/*      */           this.notifications.clear();
/*      */           
/*      */           this.notifications.addAll(retrievedNotifications);
/*      */           
/*      */           for (RealmsNotification notification : (Iterable<RealmsNotification>)retrievedNotifications) {
/*      */             if (notification instanceof RealmsNotification.InfoPopup) {
/*      */               RealmsNotification.InfoPopup popup = (RealmsNotification.InfoPopup)notification;
/*      */               
/*      */               PopupScreen popupScreen = popup.buildScreen((Screen)this, this::dismissNotification);
/*      */               if (popupScreen != null) {
/*      */                 this.minecraft.setScreen((Screen)popupScreen);
/*      */                 markNotificationsAsSeen(List.of(notification));
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           if (!this.notifications.isEmpty() && this.activeLayoutState != LayoutState.LOADING) {
/*      */             refreshListAndLayout();
/*      */           }
/*      */         });
/*  428 */     result.subscribe(dataSource.pendingInvitesTask, numberOfPendingInvites -> {
/*      */           this.pendingInvitesButton.setNotificationCount(numberOfPendingInvites);
/*      */           
/*      */           this.pendingInvitesButton.setTooltip((numberOfPendingInvites == 0) ? Tooltip.create(NO_PENDING_INVITES) : Tooltip.create(PENDING_INVITES));
/*      */           if (numberOfPendingInvites > 0 && this.inviteNarrationLimiter.tryAcquire(1)) {
/*      */             this.minecraft.getNarrator().saySystemNow((Component)Component.translatable("mco.configure.world.invite.narration", new Object[] { numberOfPendingInvites }));
/*      */           }
/*      */         });
/*  436 */     result.subscribe(dataSource.trialAvailabilityTask, newStatus -> this.trialsAvailable = newStatus);
/*      */     
/*  438 */     result.subscribe(dataSource.onlinePlayersTask, playerList -> this.onlinePlayersPerRealm = playerList);
/*      */     
/*  440 */     result.subscribe(dataSource.newsTask, news -> {
/*      */           dataSource.newsManager.updateUnreadNews(dataSource);
/*      */           
/*      */           this.newsLink = dataSource.newsManager.newsLink();
/*      */           this.newsButton.setNotificationCount(dataSource.newsManager.hasUnreadNews() ? Integer.MAX_VALUE : 0);
/*      */         });
/*  446 */     return result;
/*      */   }
/*      */   
/*      */   private void markNotificationsAsSeen(Collection<RealmsNotification> notifications) {
/*  450 */     List<UUID> seenNotifications = new ArrayList<>(notifications.size());
/*  451 */     for (RealmsNotification notification : notifications) {
/*  452 */       if (!notification.seen() && !this.handledSeenNotifications.contains(notification.uuid())) {
/*  453 */         seenNotifications.add(notification.uuid());
/*      */       }
/*      */     } 
/*      */     
/*  457 */     if (!seenNotifications.isEmpty()) {
/*  458 */       callRealmsClient(realmsClient -> {
/*      */             realmsClient.notificationsSeen(seenNotifications);
/*      */             return null;
/*      */           }, ignored -> this.handledSeenNotifications.addAll(seenNotifications));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> void callRealmsClient(RealmsCall<T> supplier, Consumer<T> callback) {
/*  470 */     Minecraft minecraft = Minecraft.getInstance();
/*  471 */     CompletableFuture.<T>supplyAsync(() -> {
/*      */           try {
/*      */             return supplier.request(RealmsClient.getOrCreate(minecraft));
/*  474 */           } catch (RealmsServiceException e) {
/*      */             throw new RuntimeException(e);
/*      */           } 
/*  477 */         }).thenAcceptAsync(callback, (Executor)minecraft).exceptionally(e -> {
/*      */           LOGGER.error("Failed to execute call to Realms Service", e);
/*      */           return null;
/*      */         });
/*      */   }
/*      */   
/*      */   private void refreshListAndLayout() {
/*  484 */     this.realmSelectionList.refreshEntries(this);
/*  485 */     updateLayout();
/*  486 */     updateButtonStates();
/*      */   }
/*      */   
/*      */   private void pingRegions() {
/*  490 */     new Thread(() -> {
/*      */           List<RegionPingResult> regionPingResultList = Ping.pingAllRegions();
/*      */ 
/*      */           
/*      */           RealmsClient client = RealmsClient.getOrCreate();
/*      */           
/*      */           PingResult pingResult = new PingResult(regionPingResultList, getOwnedNonExpiredRealmIds());
/*      */           
/*      */           try {
/*      */             client.sendPingResults(pingResult);
/*  500 */           } catch (Throwable t) {
/*      */             LOGGER.warn("Could not send ping result to Realms: ", t);
/*      */           } 
/*  503 */         }).start();
/*      */   }
/*      */   
/*      */   private List<Long> getOwnedNonExpiredRealmIds() {
/*  507 */     List<Long> ids = com.google.common.collect.Lists.newArrayList();
/*      */     
/*  509 */     for (RealmsServer server : (Iterable<RealmsServer>)this.serverList) {
/*  510 */       if (isSelfOwnedNonExpiredServer(server)) {
/*  511 */         ids.add(server.id);
/*      */       }
/*      */     } 
/*      */     
/*  515 */     return ids;
/*      */   }
/*      */   
/*      */   private void onRenew(RealmsServer server) {
/*  519 */     if (server != null) {
/*  520 */       String extensionUrl = net.minecraft.util.CommonLinks.extendRealms(server.remoteSubscriptionId, this.minecraft.getUser().getProfileId(), server.expiredTrial);
/*  521 */       this.minecraft.setScreen((Screen)new ConfirmLinkScreen(result -> { if (extensionUrl) { Util.getPlatform().openUri(extensionUrl); } else { this.minecraft.setScreen((Screen)this); }  }, extensionUrl, true));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void configureClicked(RealmsServer selectedServer) {
/*  532 */     if (selectedServer != null && this.minecraft.isLocalPlayer(selectedServer.ownerUUID)) {
/*  533 */       this.minecraft.setScreen((Screen)new RealmsConfigureWorldScreen(this, selectedServer.id));
/*      */     }
/*      */   }
/*      */   
/*      */   private void leaveClicked(RealmsServer selectedServer) {
/*  538 */     if (selectedServer != null && !this.minecraft.isLocalPlayer(selectedServer.ownerUUID)) {
/*  539 */       MutableComponent mutableComponent = Component.translatable("mco.configure.world.leave.question.line1");
/*  540 */       this.minecraft.setScreen((Screen)RealmsPopups.infoPopupScreen((Screen)this, (Component)mutableComponent, popup -> leaveServer(selectedServer)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private RealmsServer getSelectedServer() {
/*  547 */     AbstractSelectionList.Entry entry = this.realmSelectionList.getSelected(); if (entry instanceof ServerEntry) { ServerEntry serverEntry = (ServerEntry)entry;
/*  548 */       return serverEntry.getServer(); }
/*      */     
/*  550 */     return null;
/*      */   }
/*      */   
/*      */   private void leaveServer(final RealmsServer server) {
/*  554 */     new Thread("Realms-leave-server")
/*      */       {
/*      */         public void run() {
/*      */           try {
/*  558 */             RealmsClient client = RealmsClient.getOrCreate();
/*  559 */             client.uninviteMyselfFrom(server.id);
/*  560 */             RealmsMainScreen.this.minecraft.execute(RealmsMainScreen::refreshServerList);
/*  561 */           } catch (RealmsServiceException e) {
/*  562 */             RealmsMainScreen.LOGGER.error("Couldn't configure world", (Throwable)e);
/*  563 */             RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen((Screen)new RealmsGenericErrorScreen(e, (Screen)RealmsMainScreen.this)));
/*      */           } 
/*      */         }
/*  566 */       }.start();
/*      */     
/*  568 */     this.minecraft.setScreen((Screen)this);
/*      */   }
/*      */   
/*      */   private void dismissNotification(UUID uuid) {
/*  572 */     callRealmsClient(realmsClient -> {
/*      */           realmsClient.notificationsDismiss(List.of(uuid));
/*      */           return null;
/*      */         }, ignored -> {
/*      */           this.notifications.removeIf(());
/*      */           refreshListAndLayout();
/*      */         });
/*      */   }
/*      */   
/*      */   public void resetScreen() {
/*  582 */     this.realmSelectionList.setSelected((Entry)null);
/*  583 */     refreshServerList();
/*      */   }
/*      */ 
/*      */   
/*      */   public Component getNarrationMessage() {
/*  588 */     switch (this.activeLayoutState.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return 
/*      */ 
/*      */       
/*  591 */       super.getNarrationMessage();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/*  597 */     super.render(graphics, xm, ym, a);
/*      */     
/*  599 */     if (isSnapshot()) {
/*  600 */       graphics.drawString(this.font, "Minecraft " + SharedConstants.getCurrentVersion().name(), 2, this.height - 10, -1);
/*      */     }
/*      */     
/*  603 */     if (this.trialsAvailable && this.addRealmButton.active) {
/*  604 */       AddRealmPopupScreen.renderDiamond(graphics, this.addRealmButton);
/*      */     }
/*      */     
/*  607 */     switch (RealmsClient.ENVIRONMENT) { case STAGE:
/*  608 */         renderEnvironment(graphics, "STAGE!", -256); break;
/*  609 */       case LOCAL: renderEnvironment(graphics, "LOCAL!", -8388737);
/*      */         break; }
/*      */   
/*      */   }
/*      */   private void openTrialAvailablePopup() {
/*  614 */     this.minecraft.setScreen((Screen)new AddRealmPopupScreen((Screen)this, this.trialsAvailable));
/*      */   }
/*      */   
/*      */   public static void play(RealmsServer server, Screen cancelScreen) {
/*  618 */     play(server, cancelScreen, false);
/*      */   }
/*      */   
/*      */   public static void play(RealmsServer server, Screen cancelScreen, boolean skipCompatibility) {
/*  622 */     if (server != null) {
/*  623 */       if (!isSnapshot() || skipCompatibility || server.isMinigameActive()) {
/*  624 */         Minecraft.getInstance().setScreen((Screen)new RealmsLongRunningMcoTaskScreen(cancelScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(cancelScreen, server) }));
/*      */         return;
/*      */       } 
/*  627 */       switch (server.compatibility) {
/*      */         case COMPATIBLE:
/*  629 */           Minecraft.getInstance().setScreen((Screen)new RealmsLongRunningMcoTaskScreen(cancelScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(cancelScreen, server) })); break;
/*  630 */         case UNVERIFIABLE: confirmToPlay(server, cancelScreen, 
/*  631 */               (Component)Component.translatable("mco.compatibility.unverifiable.title").withColor(-171), 
/*  632 */               (Component)Component.translatable("mco.compatibility.unverifiable.message"), CommonComponents.GUI_CONTINUE);
/*      */           break;
/*      */         case NEEDS_DOWNGRADE:
/*  635 */           confirmToPlay(server, cancelScreen, 
/*  636 */               (Component)Component.translatable("selectWorld.backupQuestion.downgrade").withColor(-2142128), 
/*  637 */               (Component)Component.translatable("mco.compatibility.downgrade.description", new Object[] {
/*  638 */                   Component.literal(server.activeVersion).withColor(-171), 
/*  639 */                   Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171)
/*  640 */                 }), (Component)Component.translatable("mco.compatibility.downgrade")); break;
/*      */         case NEEDS_UPGRADE:
/*  642 */           upgradeRealmAndPlay(server, cancelScreen); break;
/*      */         case INCOMPATIBLE:
/*  644 */           Minecraft.getInstance().setScreen((Screen)new PopupScreen.Builder(cancelScreen, INCOMPATIBLE_POPUP_TITLE)
/*  645 */               .setMessage((Component)Component.translatable("mco.compatibility.incompatible.series.popup.message", new Object[] {
/*  646 */                     Component.literal(server.activeVersion).withColor(-171), 
/*  647 */                     Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171)
/*  648 */                   })).addButton(CommonComponents.GUI_BACK, PopupScreen::onClose)
/*  649 */               .build());
/*      */           break;
/*      */         case RELEASE_TYPE_INCOMPATIBLE:
/*  652 */           Minecraft.getInstance().setScreen((Screen)new PopupScreen.Builder(cancelScreen, INCOMPATIBLE_POPUP_TITLE)
/*  653 */               .setMessage(INCOMPATIBLE_RELEASE_TYPE_POPUP_MESSAGE)
/*  654 */               .addButton(CommonComponents.GUI_BACK, PopupScreen::onClose)
/*  655 */               .build());
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void confirmToPlay(RealmsServer server, Screen lastScreen, Component title, Component message, Component confirmButton) {
/*  662 */     Minecraft.getInstance().setScreen((Screen)new PopupScreen.Builder(lastScreen, title)
/*      */         
/*  664 */         .setMessage(message)
/*  665 */         .addButton(confirmButton, popupScreen -> {
/*      */             Minecraft.getInstance().setScreen((Screen)new RealmsLongRunningMcoTaskScreen(lastScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(lastScreen, server) }));
/*      */             
/*      */             refreshServerList();
/*  669 */           }).addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/*  670 */         .build());
/*      */   }
/*      */ 
/*      */   
/*      */   private static void upgradeRealmAndPlay(RealmsServer server, Screen cancelScreen) {
/*  675 */     MutableComponent mutableComponent1 = Component.translatable("mco.compatibility.upgrade.title").withColor(-171);
/*  676 */     MutableComponent mutableComponent2 = Component.translatable("mco.compatibility.upgrade");
/*  677 */     MutableComponent mutableComponent3 = Component.literal(server.activeVersion).withColor(-171);
/*  678 */     MutableComponent mutableComponent4 = Component.literal(SharedConstants.getCurrentVersion().name()).withColor(-171);
/*  679 */     MutableComponent mutableComponent5 = isSelfOwnedServer(server) ? 
/*  680 */       Component.translatable("mco.compatibility.upgrade.description", new Object[] { mutableComponent3, mutableComponent4
/*  681 */         }) : Component.translatable("mco.compatibility.upgrade.friend.description", new Object[] { mutableComponent3, mutableComponent4 });
/*  682 */     confirmToPlay(server, cancelScreen, (Component)mutableComponent1, (Component)mutableComponent5, (Component)mutableComponent2);
/*      */   }
/*      */   
/*      */   private class RealmSelectionList extends ObjectSelectionList<Entry> {
/*      */     public RealmSelectionList() {
/*  687 */       super(Minecraft.getInstance(), RealmsMainScreen.this.width, RealmsMainScreen.this.height, 0, 36);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setSelected(RealmsMainScreen.Entry selected) {
/*  692 */       super.setSelected((AbstractSelectionList.Entry)selected);
/*  693 */       RealmsMainScreen.this.updateButtonStates();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getRowWidth() {
/*  698 */       return 300;
/*      */     }
/*      */     
/*      */     private void refreshEntries(RealmsMainScreen realmsMainScreen) {
/*  702 */       RealmsMainScreen.Entry previouslySelected = (RealmsMainScreen.Entry)getSelected();
/*  703 */       clearEntries();
/*      */       
/*  705 */       for (RealmsNotification notification : RealmsMainScreen.this.notifications) {
/*  706 */         if (notification instanceof RealmsNotification.VisitUrl) { RealmsNotification.VisitUrl visitUrl = (RealmsNotification.VisitUrl)notification;
/*  707 */           addEntriesForNotification(visitUrl, realmsMainScreen, previouslySelected);
/*  708 */           RealmsMainScreen.this.markNotificationsAsSeen(List.of(notification));
/*      */           
/*      */           break; }
/*      */       
/*      */       } 
/*      */       
/*  714 */       refreshServerEntries(previouslySelected);
/*      */     }
/*      */     
/*      */     private void addEntriesForNotification(RealmsNotification.VisitUrl visitUrl, RealmsMainScreen realmsMainScreen, RealmsMainScreen.Entry previouslySelected) {
/*  718 */       Component message = visitUrl.getMessage();
/*  719 */       int messageHeight = RealmsMainScreen.this.font.wordWrapHeight((FormattedText)message, RealmsMainScreen.NotificationMessageEntry.textWidth(getRowWidth()));
/*  720 */       RealmsMainScreen.NotificationMessageEntry entry = new RealmsMainScreen.NotificationMessageEntry(realmsMainScreen, messageHeight, message, visitUrl);
/*  721 */       addEntry((AbstractSelectionList.Entry)entry, 38 + messageHeight);
/*  722 */       if (previouslySelected instanceof RealmsMainScreen.NotificationMessageEntry) { RealmsMainScreen.NotificationMessageEntry notificationMessageEntry = (RealmsMainScreen.NotificationMessageEntry)previouslySelected; if (notificationMessageEntry.getText().equals(message))
/*  723 */           setSelected(entry);  }
/*      */     
/*      */     }
/*      */     
/*      */     private void refreshServerEntries(RealmsMainScreen.Entry previouslySelected) {
/*  728 */       for (RealmsServer eligibleForSnapshotServer : RealmsMainScreen.this.availableSnapshotServers) {
/*  729 */         addEntry((AbstractSelectionList.Entry)new RealmsMainScreen.AvailableSnapshotEntry(eligibleForSnapshotServer));
/*      */       }
/*      */       
/*  732 */       for (RealmsServer server : (Iterable<RealmsServer>)RealmsMainScreen.this.serverList) {
/*      */         RealmsMainScreen.Entry entry;
/*  734 */         if (RealmsMainScreen.isSnapshot() && !server.isSnapshotRealm()) {
/*  735 */           if (server.state == RealmsServer.State.UNINITIALIZED) {
/*      */             continue;
/*      */           }
/*  738 */           entry = new RealmsMainScreen.ParentEntry(RealmsMainScreen.this, server);
/*      */         } else {
/*  740 */           entry = new RealmsMainScreen.ServerEntry(server);
/*      */         } 
/*  742 */         addEntry((AbstractSelectionList.Entry)entry);
/*  743 */         if (previouslySelected instanceof RealmsMainScreen.ServerEntry) { RealmsMainScreen.ServerEntry serverEntry = (RealmsMainScreen.ServerEntry)previouslySelected; if (serverEntry.serverData.id == server.id)
/*  744 */             setSelected(entry);  }
/*      */       
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static Component getVersionComponent(String version, boolean isCompatible) {
/*  751 */     return getVersionComponent(version, isCompatible ? -8355712 : -2142128);
/*      */   }
/*      */   
/*      */   public static Component getVersionComponent(String version, int color) {
/*  755 */     if (org.apache.commons.lang3.StringUtils.isBlank(version)) {
/*  756 */       return CommonComponents.EMPTY;
/*      */     }
/*  758 */     return (Component)Component.literal(version).withColor(color);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Component getGameModeComponent(int gameMode, boolean hardcore) {
/*  763 */     if (hardcore) {
/*  764 */       return (Component)Component.translatable("gameMode.hardcore").withColor(-65536);
/*      */     }
/*  766 */     return GameType.byId(gameMode).getLongDisplayName();
/*      */   }
/*      */   
/*      */   private abstract class Entry extends ObjectSelectionList.Entry<Entry> {
/*      */     protected static final int STATUS_LIGHT_WIDTH = 10;
/*      */     private static final int STATUS_LIGHT_HEIGHT = 28;
/*      */     protected static final int PADDING_X = 7;
/*      */     protected static final int PADDING_Y = 2;
/*      */     
/*      */     protected void renderStatusLights(RealmsServer serverData, GuiGraphics graphics, int rowRight, int rowTop, int mouseX, int mouseY) {
/*  776 */       int x = rowRight - 10 - 7;
/*  777 */       int y = rowTop + 2;
/*  778 */       if (serverData.expired) {
/*  779 */         drawRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRED_SPRITE, () -> RealmsMainScreen.SERVER_EXPIRED_TOOLTIP);
/*  780 */       } else if (serverData.state == RealmsServer.State.CLOSED) {
/*  781 */         drawRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.CLOSED_SPRITE, () -> RealmsMainScreen.SERVER_CLOSED_TOOLTIP);
/*  782 */       } else if (RealmsMainScreen.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
/*  783 */         drawRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRES_SOON_SPRITE, () -> (serverData.daysLeft <= 0) ? RealmsMainScreen.SERVER_EXPIRES_SOON_TOOLTIP : ((serverData.daysLeft == 1) ? RealmsMainScreen.SERVER_EXPIRES_IN_DAY_TOOLTIP : Component.translatable("mco.selectServer.expires.days", new Object[] { serverData.daysLeft })));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  792 */       else if (serverData.state == RealmsServer.State.OPEN) {
/*  793 */         drawRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.OPEN_SPRITE, () -> RealmsMainScreen.SERVER_OPEN_TOOLTIP);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void drawRealmStatus(GuiGraphics graphics, int x, int y, int xm, int ym, Identifier sprite, Supplier<Component> tooltip) {
/*  798 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 10, 28);
/*  799 */       if (RealmsMainScreen.this.realmSelectionList.isMouseOver(xm, ym) && xm >= x && xm <= x + 10 && ym >= y && ym <= y + 28) {
/*  800 */         graphics.setTooltipForNextFrame(tooltip.get(), xm, ym);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void renderFirstLine(GuiGraphics graphics, int rowTop, int rowLeft, int rowWidth, int serverNameColor, RealmsServer serverData) {
/*  805 */       int textX = textX(rowLeft);
/*  806 */       int firstLineY = firstLineY(rowTop);
/*      */       
/*  808 */       Component versionComponent = RealmsMainScreen.getVersionComponent(serverData.activeVersion, serverData.isCompatible());
/*  809 */       int versionTextX = versionTextX(rowLeft, rowWidth, versionComponent);
/*  810 */       renderClampedString(graphics, serverData.getName(), textX, firstLineY, versionTextX, serverNameColor);
/*  811 */       if (versionComponent != CommonComponents.EMPTY && !serverData.isMinigameActive()) {
/*  812 */         graphics.drawString(RealmsMainScreen.this.font, versionComponent, versionTextX, firstLineY, -8355712);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void renderSecondLine(GuiGraphics graphics, int rowTop, int rowLeft, int rowWidth, RealmsServer serverData) {
/*  817 */       int textX = textX(rowLeft);
/*  818 */       int firstLineY = firstLineY(rowTop);
/*  819 */       int secondLineY = secondLineY(firstLineY);
/*      */       
/*  821 */       String minigameName = serverData.getMinigameName();
/*  822 */       boolean minigameActive = serverData.isMinigameActive();
/*  823 */       if (minigameActive && minigameName != null) {
/*  824 */         MutableComponent mutableComponent = Component.literal(minigameName).withStyle(net.minecraft.ChatFormatting.GRAY);
/*  825 */         graphics.drawString(RealmsMainScreen.this.font, (Component)Component.translatable("mco.selectServer.minigameName", new Object[] { mutableComponent }).withColor(-171), textX, secondLineY, -1);
/*      */       } else {
/*  827 */         int maxX = renderGameMode(serverData, graphics, rowLeft, rowWidth, firstLineY);
/*  828 */         renderClampedString(graphics, serverData.getDescription(), textX, secondLineY(firstLineY), maxX, -8355712);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void renderThirdLine(GuiGraphics graphics, int rowTop, int rowLeft, RealmsServer server) {
/*  833 */       int textX = textX(rowLeft);
/*  834 */       int firstLineY = firstLineY(rowTop);
/*  835 */       int thirdLineY = thirdLineY(firstLineY);
/*      */       
/*  837 */       if (!RealmsMainScreen.isSelfOwnedServer(server)) {
/*  838 */         graphics.drawString(RealmsMainScreen.this.font, server.owner, textX, thirdLineY(firstLineY), -8355712);
/*  839 */       } else if (server.expired) {
/*  840 */         Component expirationText = server.expiredTrial ? RealmsMainScreen.TRIAL_EXPIRED_TEXT : RealmsMainScreen.SUBSCRIPTION_EXPIRED_TEXT;
/*  841 */         graphics.drawString(RealmsMainScreen.this.font, expirationText, textX, thirdLineY, -2142128);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void renderClampedString(GuiGraphics graphics, String string, int x, int y, int maxX, int color) {
/*  846 */       if (string == null) {
/*      */         return;
/*      */       }
/*  849 */       int availableSpace = maxX - x;
/*  850 */       if (RealmsMainScreen.this.font.width(string) > availableSpace) {
/*  851 */         String clampedName = RealmsMainScreen.this.font.plainSubstrByWidth(string, availableSpace - RealmsMainScreen.this.font.width("... "));
/*  852 */         graphics.drawString(RealmsMainScreen.this.font, clampedName + "...", x, y, color);
/*      */       } else {
/*  854 */         graphics.drawString(RealmsMainScreen.this.font, string, x, y, color);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected int versionTextX(int rowLeft, int rowWidth, Component versionComponent) {
/*  859 */       return rowLeft + rowWidth - RealmsMainScreen.this.font.width((FormattedText)versionComponent) - 20;
/*      */     }
/*      */     
/*      */     protected int gameModeTextX(int rowLeft, int rowWidth, Component versionComponent) {
/*  863 */       return rowLeft + rowWidth - RealmsMainScreen.this.font.width((FormattedText)versionComponent) - 20;
/*      */     }
/*      */     
/*      */     protected int renderGameMode(RealmsServer server, GuiGraphics graphics, int rowLeft, int rowWidth, int firstLineY) {
/*  867 */       boolean hardcore = server.isHardcore;
/*  868 */       int gameMode = server.gameMode;
/*  869 */       int x = rowLeft;
/*  870 */       if (GameType.isValidId(gameMode)) {
/*  871 */         Component gameModeComponent = RealmsMainScreen.getGameModeComponent(gameMode, hardcore);
/*  872 */         x = gameModeTextX(rowLeft, rowWidth, gameModeComponent);
/*  873 */         graphics.drawString(RealmsMainScreen.this.font, gameModeComponent, x, secondLineY(firstLineY), -8355712);
/*      */       } 
/*  875 */       if (hardcore) {
/*  876 */         x -= 10;
/*  877 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.HARDCORE_MODE_SPRITE, x, secondLineY(firstLineY), 8, 8);
/*      */       } 
/*  879 */       return x;
/*      */     }
/*      */     
/*      */     protected int firstLineY(int rowTop) {
/*  883 */       return rowTop + 1;
/*      */     }
/*      */     
/*      */     protected int lineHeight() {
/*  887 */       Objects.requireNonNull(RealmsMainScreen.this.font); return 2 + 9;
/*      */     }
/*      */     
/*      */     protected int textX(int rowLeft) {
/*  891 */       return rowLeft + 36 + 2;
/*      */     }
/*      */     
/*      */     protected int secondLineY(int firstLineY) {
/*  895 */       return firstLineY + lineHeight();
/*      */     }
/*      */     
/*      */     protected int thirdLineY(int firstLineY) {
/*  899 */       return firstLineY + lineHeight() * 2;
/*      */     }
/*      */   }
/*      */   
/*      */   private class NotificationMessageEntry
/*      */     extends Entry {
/*      */     private static final int SIDE_MARGINS = 40;
/*      */     public static final int PADDING = 7;
/*      */     public static final int HEIGHT_WITHOUT_TEXT = 38;
/*      */     private final Component text;
/*  909 */     private final List<AbstractWidget> children = new ArrayList<>();
/*      */     private final RealmsMainScreen.CrossButton dismissButton;
/*      */     private final MultiLineTextWidget textWidget;
/*      */     private final GridLayout gridLayout;
/*      */     private final FrameLayout textFrame;
/*      */     private final Button button;
/*  915 */     private int lastEntryWidth = -1;
/*      */     
/*      */     public NotificationMessageEntry(RealmsMainScreen realmsMainScreen, int messageHeight, Component text, RealmsNotification.VisitUrl notification) {
/*  918 */       this.text = text;
/*  919 */       this.gridLayout = new GridLayout();
/*  920 */       this.gridLayout.addChild((LayoutElement)ImageWidget.sprite(20, 20, RealmsMainScreen.INFO_SPRITE), 0, 0, this.gridLayout.newCellSettings().padding(7, 7, 0, 0));
/*  921 */       this.gridLayout.addChild((LayoutElement)SpacerElement.width(40), 0, 0);
/*  922 */       this.textFrame = (FrameLayout)this.gridLayout.addChild((LayoutElement)new FrameLayout(0, messageHeight), 0, 1, this.gridLayout.newCellSettings().paddingTop(7));
/*  923 */       this.textWidget = (MultiLineTextWidget)this.textFrame.addChild((LayoutElement)new MultiLineTextWidget(text, RealmsMainScreen.this.font).setCentered(true), this.textFrame.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyTop());
/*  924 */       this.gridLayout.addChild((LayoutElement)SpacerElement.width(40), 0, 2);
/*  925 */       if (notification.dismissable()) {
/*  926 */         this.dismissButton = (RealmsMainScreen.CrossButton)this.gridLayout.addChild((LayoutElement)new RealmsMainScreen.CrossButton(b -> RealmsMainScreen.this.dismissNotification(notification.uuid()), (Component)Component.translatable("mco.notification.dismiss")), 0, 2, this.gridLayout.newCellSettings().alignHorizontallyRight().padding(0, 7, 7, 0));
/*      */       } else {
/*  928 */         this.dismissButton = null;
/*      */       } 
/*  930 */       this.button = (Button)this.gridLayout.addChild((LayoutElement)notification.buildOpenLinkButton((Screen)realmsMainScreen), 1, 1, this.gridLayout.newCellSettings().alignHorizontallyCenter().padding(4));
/*  931 */       this.button.setOverrideRenderHighlightedSprite(() -> rec$.isFocused());
/*  932 */       Objects.requireNonNull(this.children); this.gridLayout.visitWidgets(this.children::add);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean keyPressed(KeyEvent event) {
/*  937 */       if (this.button.keyPressed(event)) {
/*  938 */         return true;
/*      */       }
/*  940 */       if (this.dismissButton != null && this.dismissButton.keyPressed(event)) {
/*  941 */         return true;
/*      */       }
/*  943 */       return super.keyPressed(event);
/*      */     }
/*      */     
/*      */     private void updateEntryWidth() {
/*  947 */       int entryWidth = getWidth();
/*  948 */       if (this.lastEntryWidth != entryWidth) {
/*  949 */         refreshLayout(entryWidth);
/*  950 */         this.lastEntryWidth = entryWidth;
/*      */       } 
/*      */     }
/*      */     
/*      */     private void refreshLayout(int entryWidth) {
/*  955 */       int width = textWidth(entryWidth);
/*  956 */       this.textFrame.setMinWidth(width);
/*  957 */       this.textWidget.setMaxWidth(width);
/*  958 */       this.gridLayout.arrangeElements();
/*      */     }
/*      */     
/*      */     public static int textWidth(int rowWidth) {
/*  962 */       return rowWidth - 80;
/*      */     }
/*      */ 
/*      */     
/*      */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*  967 */       this.gridLayout.setPosition(getContentX(), getContentY());
/*  968 */       updateEntryWidth();
/*  969 */       this.children.forEach(child -> child.render(graphics, mouseX, mouseY, a));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*  974 */       if (this.dismissButton != null && this.dismissButton.mouseClicked(event, doubleClick)) {
/*  975 */         return true;
/*      */       }
/*  977 */       if (this.button.mouseClicked(event, doubleClick)) {
/*  978 */         return true;
/*      */       }
/*      */       
/*  981 */       return super.mouseClicked(event, doubleClick);
/*      */     }
/*      */     
/*      */     public Component getText() {
/*  985 */       return this.text;
/*      */     }
/*      */ 
/*      */     
/*      */     public Component getNarration() {
/*  990 */       return getText();
/*      */     }
/*      */   }
/*      */   
/*      */   private class AvailableSnapshotEntry extends Entry {
/*  995 */     private static final Component START_SNAPSHOT_REALM = (Component)Component.translatable("mco.snapshot.start");
/*      */     
/*      */     private static final int TEXT_PADDING = 5;
/*  998 */     private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */     private final RealmsServer parent;
/*      */     
/*      */     public AvailableSnapshotEntry(RealmsServer parent) {
/* 1002 */       this.parent = parent;
/* 1003 */       this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.tooltip")));
/*      */     }
/*      */ 
/*      */     
/*      */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1008 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, getContentX() - 5, getContentYMiddle() - 10, 40, 20);
/*      */       
/* 1010 */       Objects.requireNonNull(RealmsMainScreen.this.font); int textYPos = getContentYMiddle() - 9 / 2;
/* 1011 */       graphics.drawString(RealmsMainScreen.this.font, START_SNAPSHOT_REALM, getContentX() + 40 - 2, textYPos - 5, -8388737);
/* 1012 */       graphics.drawString(RealmsMainScreen.this.font, (Component)Component.translatable("mco.snapshot.description", new Object[] { Objects.requireNonNullElse(this.parent.name, "unknown server") }), getContentX() + 40 - 2, textYPos + 5, -8355712);
/* 1013 */       this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 1018 */       addSnapshotRealm();
/* 1019 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean keyPressed(KeyEvent event) {
/* 1024 */       if (event.isSelection()) {
/* 1025 */         addSnapshotRealm();
/* 1026 */         return false;
/*      */       } 
/* 1028 */       return super.keyPressed(event);
/*      */     }
/*      */     
/*      */     private void addSnapshotRealm() {
/* 1032 */       RealmsMainScreen.this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1033 */       RealmsMainScreen.this.minecraft.setScreen((Screen)new PopupScreen.Builder((Screen)RealmsMainScreen.this, (Component)Component.translatable("mco.snapshot.createSnapshotPopup.title"))
/* 1034 */           .setMessage((Component)Component.translatable("mco.snapshot.createSnapshotPopup.text"))
/* 1035 */           .addButton((Component)Component.translatable("mco.selectServer.create"), popup -> RealmsMainScreen.this.minecraft.setScreen((Screen)new RealmsCreateRealmScreen(RealmsMainScreen.this, this.parent, true)))
/*      */           
/* 1037 */           .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/* 1038 */           .build());
/*      */     }
/*      */ 
/*      */     
/*      */     public Component getNarration() {
/* 1043 */       return (Component)Component.translatable("gui.narrate.button", new Object[] { CommonComponents.joinForNarration(new Component[] { START_SNAPSHOT_REALM, 
/* 1044 */                 (Component)Component.translatable("mco.snapshot.description", new Object[] { Objects.requireNonNullElse(this.parent.name, "unknown server") }) }) });
/*      */     }
/*      */   }
/*      */   
/*      */   private class ParentEntry
/*      */     extends Entry {
/*      */     private final RealmsServer server;
/* 1051 */     private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */     public ParentEntry(RealmsMainScreen this$0, RealmsServer server) {
/* 1053 */       super(this$0);
/* 1054 */       this.server = server;
/* 1055 */       if (!server.expired) {
/* 1056 */         this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.parent.tooltip")));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1062 */       renderStatusLights(this.server, graphics, getContentRight(), getContentY(), mouseX, mouseY);
/* 1063 */       RealmsUtil.renderPlayerFace(graphics, getContentX(), getContentY(), 32, this.server.ownerUUID);
/* 1064 */       renderFirstLine(graphics, getContentY(), getContentX(), getContentWidth(), -8355712, this.server);
/* 1065 */       renderSecondLine(graphics, getContentY(), getContentX(), getContentWidth(), this.server);
/* 1066 */       renderThirdLine(graphics, getContentY(), getContentX(), this.server);
/* 1067 */       this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */     }
/*      */ 
/*      */     
/*      */     public Component getNarration() {
/* 1072 */       return (Component)Component.literal(Objects.<String>requireNonNullElse(this.server.name, "unknown server"));
/*      */     }
/*      */   }
/*      */   
/*      */   private class ServerEntry extends Entry {
/* 1077 */     private static final Component ONLINE_PLAYERS_TOOLTIP_HEADER = (Component)Component.translatable("mco.onlinePlayers");
/*      */     
/*      */     private static final int PLAYERS_ONLINE_SPRITE_SIZE = 9;
/*      */     
/*      */     private static final int PLAYERS_ONLINE_SPRITE_SEPARATION = 3;
/*      */     private static final int SKIN_HEAD_LARGE_WIDTH = 36;
/*      */     private final RealmsServer serverData;
/* 1084 */     private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();
/*      */     
/*      */     public ServerEntry(RealmsServer serverData) {
/* 1087 */       this.serverData = serverData;
/* 1088 */       boolean selfOwnedServer = RealmsMainScreen.isSelfOwnedServer(serverData);
/* 1089 */       if (RealmsMainScreen.isSnapshot() && selfOwnedServer && serverData.isSnapshotRealm()) {
/* 1090 */         this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.paired", new Object[] { serverData.parentWorldName })));
/* 1091 */       } else if (!selfOwnedServer && serverData.needsDowngrade()) {
/* 1092 */         this.tooltip.set(Tooltip.create((Component)Component.translatable("mco.snapshot.friendsRealm.downgrade", new Object[] { serverData.activeVersion })));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 1098 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1099 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, getContentX() - 5, getContentYMiddle() - 10, 40, 20);
/*      */         
/* 1101 */         Objects.requireNonNull(RealmsMainScreen.this.font); int textYPos = getContentYMiddle() - 9 / 2;
/* 1102 */         graphics.drawString(RealmsMainScreen.this.font, RealmsMainScreen.SERVER_UNITIALIZED_TEXT, getContentX() + 40 - 2, textYPos, -8388737);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1107 */       RealmsUtil.renderPlayerFace(graphics, getContentX(), getContentY(), 32, this.serverData.ownerUUID);
/*      */       
/* 1109 */       renderFirstLine(graphics, getContentY(), getContentX(), getContentWidth(), -1, this.serverData);
/* 1110 */       renderSecondLine(graphics, getContentY(), getContentX(), getContentWidth(), this.serverData);
/* 1111 */       renderThirdLine(graphics, getContentY(), getContentX(), this.serverData);
/*      */       
/* 1113 */       renderStatusLights(this.serverData, graphics, getContentRight(), getContentY(), mouseX, mouseY);
/* 1114 */       boolean hasRenderedTooltip = renderOnlinePlayers(graphics, getContentY(), getContentX(), getContentWidth(), getContentHeight(), mouseX, mouseY, a);
/* 1115 */       if (!hasRenderedTooltip) {
/* 1116 */         this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, hovered, isFocused(), new ScreenRectangle(getContentX(), getContentY(), getContentWidth(), getContentHeight()));
/*      */       }
/*      */     }
/*      */     
/*      */     private boolean renderOnlinePlayers(GuiGraphics graphics, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, float a) {
/* 1121 */       List<ResolvableProfile> profileResults = RealmsMainScreen.this.onlinePlayersPerRealm.getProfileResultsFor(this.serverData.id);
/* 1122 */       int playerCount = profileResults.size();
/* 1123 */       if (playerCount > 0) {
/* 1124 */         List<PlayerSkinRenderCache.RenderInfo> tooltipEntries; int playersOnlineXEnd = rowLeft + rowWidth - 21;
/* 1125 */         int playersOnlineY = rowTop + rowHeight - 9 - 2;
/* 1126 */         int playerOnlineWidth = 9 * playerCount + 3 * (playerCount - 1);
/* 1127 */         int playersOnlineXStart = playersOnlineXEnd - playerOnlineWidth;
/*      */         
/* 1129 */         if (mouseX >= playersOnlineXStart && mouseX <= playersOnlineXEnd && mouseY >= playersOnlineY && mouseY <= playersOnlineY + 9) {
/* 1130 */           tooltipEntries = new ArrayList<>(playerCount);
/*      */         } else {
/* 1132 */           tooltipEntries = null;
/*      */         } 
/*      */         
/* 1135 */         PlayerSkinRenderCache skinCache = RealmsMainScreen.this.minecraft.playerSkinRenderCache();
/* 1136 */         for (int i = 0; i < profileResults.size(); i++) {
/* 1137 */           ResolvableProfile profile = profileResults.get(i);
/* 1138 */           PlayerSkinRenderCache.RenderInfo profileRenderInfo = skinCache.getOrDefault(profile);
/* 1139 */           int xPos = playersOnlineXStart + 12 * i;
/* 1140 */           net.minecraft.client.gui.components.PlayerFaceRenderer.draw(graphics, profileRenderInfo.playerSkin(), xPos, playersOnlineY, 9);
/*      */           
/* 1142 */           if (tooltipEntries != null) {
/* 1143 */             tooltipEntries.add(profileRenderInfo);
/*      */           }
/*      */         } 
/*      */         
/* 1147 */         if (tooltipEntries != null) {
/* 1148 */           graphics.setTooltipForNextFrame(RealmsMainScreen.this.font, List.of(ONLINE_PLAYERS_TOOLTIP_HEADER), java.util.Optional.of(new ClientActivePlayersTooltip.ActivePlayersTooltip(tooltipEntries)), mouseX, mouseY);
/* 1149 */           return true;
/*      */         } 
/*      */       } 
/* 1152 */       return false;
/*      */     }
/*      */     
/*      */     private void playRealm() {
/* 1156 */       RealmsMainScreen.this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1157 */       RealmsMainScreen.play(this.serverData, (Screen)RealmsMainScreen.this);
/*      */     }
/*      */     
/*      */     private void createUnitializedRealm() {
/* 1161 */       RealmsMainScreen.this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 1162 */       RealmsCreateRealmScreen createScreen = new RealmsCreateRealmScreen(RealmsMainScreen.this, this.serverData, this.serverData.isSnapshotRealm());
/* 1163 */       RealmsMainScreen.this.minecraft.setScreen((Screen)createScreen);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 1168 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1169 */         createUnitializedRealm();
/* 1170 */       } else if (this.serverData.shouldPlayButtonBeActive() && 
/* 1171 */         doubleClick && isFocused()) {
/* 1172 */         playRealm();
/*      */       } 
/*      */       
/* 1175 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean keyPressed(KeyEvent event) {
/* 1180 */       if (event.isSelection()) {
/* 1181 */         if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1182 */           createUnitializedRealm();
/* 1183 */           return true;
/* 1184 */         }  if (this.serverData.shouldPlayButtonBeActive()) {
/* 1185 */           playRealm();
/* 1186 */           return true;
/*      */         } 
/*      */       } 
/* 1189 */       return super.keyPressed(event);
/*      */     }
/*      */ 
/*      */     
/*      */     public Component getNarration() {
/* 1194 */       if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
/* 1195 */         return RealmsMainScreen.UNITIALIZED_WORLD_NARRATION;
/*      */       }
/* 1197 */       return (Component)Component.translatable("narrator.select", new Object[] { Objects.requireNonNullElse(this.serverData.name, "unknown server") });
/*      */     }
/*      */ 
/*      */     
/*      */     public RealmsServer getServer() {
/* 1202 */       return this.serverData;
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean isSelfOwnedServer(RealmsServer serverData) {
/* 1207 */     return Minecraft.getInstance().isLocalPlayer(serverData.ownerUUID);
/*      */   }
/*      */   
/*      */   private boolean isSelfOwnedNonExpiredServer(RealmsServer serverData) {
/* 1211 */     return (isSelfOwnedServer(serverData) && !serverData.expired);
/*      */   }
/*      */   
/*      */   private void renderEnvironment(GuiGraphics graphics, String text, int color) {
/* 1215 */     graphics.pose().pushMatrix();
/* 1216 */     graphics.pose().translate((this.width / 2 - 25), 20.0F);
/* 1217 */     graphics.pose().rotate(-0.34906584F);
/* 1218 */     graphics.pose().scale(1.5F, 1.5F);
/*      */     
/* 1220 */     graphics.drawString(this.font, text, 0, 0, color);
/*      */     
/* 1222 */     graphics.pose().popMatrix();
/*      */   }
/*      */   
/*      */   private static class NotificationButton extends SpriteIconButton.CenteredIcon {
/* 1226 */     private static final Identifier[] NOTIFICATION_ICONS = new Identifier[] {
/* 1227 */         Identifier.withDefaultNamespace("notification/1"), 
/* 1228 */         Identifier.withDefaultNamespace("notification/2"), 
/* 1229 */         Identifier.withDefaultNamespace("notification/3"), 
/* 1230 */         Identifier.withDefaultNamespace("notification/4"), 
/* 1231 */         Identifier.withDefaultNamespace("notification/5"), 
/* 1232 */         Identifier.withDefaultNamespace("notification/more")
/*      */       };
/*      */     
/*      */     private static final int UNKNOWN_COUNT = 2147483647;
/*      */     
/*      */     private static final int SIZE = 20;
/*      */     
/*      */     private static final int SPRITE_SIZE = 14;
/*      */     private int notificationCount;
/*      */     
/*      */     public NotificationButton(Component title, Identifier texture, Button.OnPress onPress, Component tooltip) {
/* 1243 */       super(20, 20, title, 14, 14, new WidgetSprites(texture), onPress, tooltip, null);
/*      */     }
/*      */     
/*      */     private int notificationCount() {
/* 1247 */       return this.notificationCount;
/*      */     }
/*      */     
/*      */     public void setNotificationCount(int notificationCount) {
/* 1251 */       this.notificationCount = notificationCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 1256 */       super.renderContents(graphics, mouseX, mouseY, a);
/* 1257 */       if (this.active && this.notificationCount != 0) {
/* 1258 */         drawNotificationCounter(graphics);
/*      */       }
/*      */     }
/*      */     
/*      */     private void drawNotificationCounter(GuiGraphics graphics) {
/* 1263 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATION_ICONS[Math.min(this.notificationCount, 6) - 1], getX() + getWidth() - 5, getY() - 3, 8, 8);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class CrossButton extends ImageButton {
/* 1268 */     private static final WidgetSprites SPRITES = new WidgetSprites(
/* 1269 */         Identifier.withDefaultNamespace("widget/cross_button"), 
/* 1270 */         Identifier.withDefaultNamespace("widget/cross_button_highlighted"));
/*      */ 
/*      */     
/*      */     protected CrossButton(Button.OnPress onPress, Component tooltip) {
/* 1274 */       super(0, 0, 14, 14, SPRITES, onPress);
/* 1275 */       setTooltip(Tooltip.create(tooltip));
/*      */     }
/*      */   }
/*      */   
/*      */   private enum LayoutState {
/* 1280 */     LOADING,
/* 1281 */     NO_REALMS,
/* 1282 */     LIST;
/*      */   }
/*      */   
/*      */   private static interface RealmsCall<T> {
/*      */     T request(RealmsClient param1RealmsClient) throws RealmsServiceException;
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/RealmsMainScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */