/*     */ package net.minecraft.client.gui.screens.social;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class SocialInteractionsScreen extends Screen {
/*  31 */   private static final Component TITLE = (Component)Component.translatable("gui.socialInteractions.title");
/*  32 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("social_interactions/background");
/*  33 */   private static final Identifier SEARCH_SPRITE = Identifier.withDefaultNamespace("icon/search");
/*     */   
/*  35 */   private static final Component TAB_ALL = (Component)Component.translatable("gui.socialInteractions.tab_all");
/*  36 */   private static final Component TAB_HIDDEN = (Component)Component.translatable("gui.socialInteractions.tab_hidden");
/*  37 */   private static final Component TAB_BLOCKED = (Component)Component.translatable("gui.socialInteractions.tab_blocked");
/*  38 */   private static final Component TAB_ALL_SELECTED = (Component)TAB_ALL.plainCopy().withStyle(ChatFormatting.UNDERLINE);
/*  39 */   private static final Component TAB_HIDDEN_SELECTED = (Component)TAB_HIDDEN.plainCopy().withStyle(ChatFormatting.UNDERLINE);
/*  40 */   private static final Component TAB_BLOCKED_SELECTED = (Component)TAB_BLOCKED.plainCopy().withStyle(ChatFormatting.UNDERLINE);
/*  41 */   private static final Component SEARCH_HINT = (Component)Component.translatable("gui.socialInteractions.search_hint").withStyle(EditBox.SEARCH_HINT_STYLE);
/*  42 */   private static final Component EMPTY_SEARCH = (Component)Component.translatable("gui.socialInteractions.search_empty").withStyle(ChatFormatting.GRAY);
/*  43 */   private static final Component EMPTY_HIDDEN = (Component)Component.translatable("gui.socialInteractions.empty_hidden").withStyle(ChatFormatting.GRAY);
/*  44 */   private static final Component EMPTY_BLOCKED = (Component)Component.translatable("gui.socialInteractions.empty_blocked").withStyle(ChatFormatting.GRAY);
/*  45 */   private static final Component BLOCKING_HINT = (Component)Component.translatable("gui.socialInteractions.blocking_hint");
/*     */   
/*     */   private static final int BG_BORDER_SIZE = 8;
/*     */   
/*     */   private static final int BG_WIDTH = 236;
/*     */   
/*     */   private static final int SEARCH_HEIGHT = 16;
/*     */   private static final int MARGIN_Y = 64;
/*     */   public static final int SEARCH_START = 72;
/*     */   public static final int LIST_START = 88;
/*     */   private static final int IMAGE_WIDTH = 238;
/*     */   private static final int BUTTON_HEIGHT = 20;
/*     */   private static final int ITEM_HEIGHT = 36;
/*  58 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private SocialInteractionsPlayerList socialInteractionsPlayerList;
/*     */   private EditBox searchBox;
/*  64 */   private String lastSearch = "";
/*     */   
/*  66 */   private Page page = Page.ALL;
/*     */   
/*     */   private Button allButton;
/*     */   private Button hiddenButton;
/*     */   private Button blockedButton;
/*     */   private Button blockingHintButton;
/*     */   private Component serverLabel;
/*     */   private int playerCount;
/*     */   
/*     */   public SocialInteractionsScreen() {
/*  76 */     this(null);
/*     */   }
/*     */   
/*     */   public SocialInteractionsScreen(Screen lastScreen) {
/*  80 */     super(TITLE);
/*  81 */     this.lastScreen = lastScreen;
/*  82 */     updateServerLabel(Minecraft.getInstance());
/*     */   }
/*     */   
/*     */   private int windowHeight() {
/*  86 */     return Math.max(52, this.height - 128 - 16);
/*     */   }
/*     */   
/*     */   private int listEnd() {
/*  90 */     return 80 + windowHeight() - 8;
/*     */   }
/*     */   
/*     */   private int marginX() {
/*  94 */     return (this.width - 238) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/*  99 */     if (this.serverLabel != null) {
/* 100 */       return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.serverLabel });
/*     */     }
/* 102 */     return super.getNarrationMessage();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 107 */     this.layout.addTitleHeader(TITLE, this.font);
/*     */     
/* 109 */     this.socialInteractionsPlayerList = new SocialInteractionsPlayerList(this, this.minecraft, this.width, listEnd() - 88, 88, 36);
/*     */     
/* 111 */     int buttonWidth = this.socialInteractionsPlayerList.getRowWidth() / 3;
/* 112 */     int buttonLeft = this.socialInteractionsPlayerList.getRowLeft();
/* 113 */     int buttonRight = this.socialInteractionsPlayerList.getRowRight();
/*     */     
/* 115 */     this.allButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(TAB_ALL, button -> showPage(Page.ALL)).bounds(buttonLeft, 45, buttonWidth, 20).build());
/* 116 */     this.hiddenButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(TAB_HIDDEN, button -> showPage(Page.HIDDEN)).bounds((buttonLeft + buttonRight - buttonWidth) / 2 + 1, 45, buttonWidth, 20).build());
/* 117 */     this.blockedButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(TAB_BLOCKED, button -> showPage(Page.BLOCKED)).bounds(buttonRight - buttonWidth + 1, 45, buttonWidth, 20).build());
/*     */     
/* 119 */     String oldEdit = (this.searchBox != null) ? this.searchBox.getValue() : "";
/* 120 */     this.searchBox = (EditBox)addRenderableWidget((GuiEventListener)new EditBox(this.font, marginX() + 28, 74, 200, 15, SEARCH_HINT)
/*     */         {
/*     */           protected MutableComponent createNarrationMessage() {
/* 123 */             if (!SocialInteractionsScreen.this.searchBox.getValue().isEmpty() && SocialInteractionsScreen.this.socialInteractionsPlayerList.isEmpty()) {
/* 124 */               return super.createNarrationMessage().append(", ").append(SocialInteractionsScreen.EMPTY_SEARCH);
/*     */             }
/* 126 */             return super.createNarrationMessage();
/*     */           }
/*     */         });
/* 129 */     this.searchBox.setMaxLength(16);
/* 130 */     this.searchBox.setVisible(true);
/* 131 */     this.searchBox.setTextColor(-1);
/* 132 */     this.searchBox.setValue(oldEdit);
/* 133 */     this.searchBox.setHint(SEARCH_HINT);
/* 134 */     this.searchBox.setResponder(this::checkSearchStringUpdate);
/*     */     
/* 136 */     this.blockingHintButton = (Button)addRenderableWidget((GuiEventListener)Button.builder(BLOCKING_HINT, ConfirmLinkScreen.confirmLink(this, net.minecraft.util.CommonLinks.BLOCKING_HELP))
/* 137 */         .bounds(this.width / 2 - 100, 64 + windowHeight(), 200, 20)
/* 138 */         .build());
/*     */     
/* 140 */     addWidget((GuiEventListener)this.socialInteractionsPlayerList);
/* 141 */     showPage(this.page);
/*     */     
/* 143 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*     */     
/* 145 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 146 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public void added() {
/* 151 */     if (this.socialInteractionsPlayerList != null) {
/* 152 */       this.socialInteractionsPlayerList.refreshHasDraftReport();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 158 */     this.layout.arrangeElements();
/* 159 */     this.socialInteractionsPlayerList.updateSizeAndPosition(this.width, listEnd() - 88, 88);
/* 160 */     this.searchBox.setPosition(marginX() + 28, 74);
/* 161 */     int buttonLeft = this.socialInteractionsPlayerList.getRowLeft();
/* 162 */     int buttonRight = this.socialInteractionsPlayerList.getRowRight();
/* 163 */     int buttonWidth = this.socialInteractionsPlayerList.getRowWidth() / 3;
/* 164 */     this.allButton.setPosition(buttonLeft, 45);
/* 165 */     this.hiddenButton.setPosition((buttonLeft + buttonRight - buttonWidth) / 2 + 1, 45);
/* 166 */     this.blockedButton.setPosition(buttonRight - buttonWidth + 1, 45);
/* 167 */     this.blockingHintButton.setPosition(this.width / 2 - 100, 64 + windowHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 172 */     setInitialFocus((GuiEventListener)this.searchBox);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 177 */     this.minecraft.setScreen(this.lastScreen); } private void showPage(Page page) { Collection<UUID> onlinePlayerIds;
/*     */     Set<UUID> hiddenPlayers;
/*     */     PlayerSocialManager socialManager;
/*     */     Set<UUID> blockedPlayers;
/* 181 */     this.page = page;
/* 182 */     this.allButton.setMessage(TAB_ALL);
/* 183 */     this.hiddenButton.setMessage(TAB_HIDDEN);
/* 184 */     this.blockedButton.setMessage(TAB_BLOCKED);
/*     */     boolean isEmpty = false;
/* 186 */     switch (page.ordinal()) {
/*     */       case 0:
/* 188 */         this.allButton.setMessage(TAB_ALL_SELECTED);
/* 189 */         onlinePlayerIds = this.minecraft.player.connection.getOnlinePlayerIds();
/* 190 */         this.socialInteractionsPlayerList.updatePlayerList(onlinePlayerIds, this.socialInteractionsPlayerList.scrollAmount(), true);
/*     */         break;
/*     */       case 1:
/* 193 */         this.hiddenButton.setMessage(TAB_HIDDEN_SELECTED);
/* 194 */         hiddenPlayers = this.minecraft.getPlayerSocialManager().getHiddenPlayers();
/* 195 */         isEmpty = hiddenPlayers.isEmpty();
/* 196 */         this.socialInteractionsPlayerList.updatePlayerList(hiddenPlayers, this.socialInteractionsPlayerList.scrollAmount(), false);
/*     */         break;
/*     */       case 2:
/* 199 */         this.blockedButton.setMessage(TAB_BLOCKED_SELECTED);
/* 200 */         socialManager = this.minecraft.getPlayerSocialManager();
/* 201 */         Objects.requireNonNull(socialManager); blockedPlayers = (Set<UUID>)this.minecraft.player.connection.getOnlinePlayerIds().stream().filter(socialManager::isBlocked).collect(Collectors.toSet());
/* 202 */         isEmpty = blockedPlayers.isEmpty();
/* 203 */         this.socialInteractionsPlayerList.updatePlayerList(blockedPlayers, this.socialInteractionsPlayerList.scrollAmount(), false);
/*     */         break;
/*     */     } 
/*     */     
/* 207 */     GameNarrator narrator = this.minecraft.getNarrator();
/* 208 */     if (!this.searchBox.getValue().isEmpty() && this.socialInteractionsPlayerList.isEmpty() && !this.searchBox.isFocused()) {
/* 209 */       narrator.saySystemNow(EMPTY_SEARCH);
/* 210 */     } else if (isEmpty) {
/* 211 */       if (page == Page.HIDDEN) {
/* 212 */         narrator.saySystemNow(EMPTY_HIDDEN);
/* 213 */       } else if (page == Page.BLOCKED) {
/* 214 */         narrator.saySystemNow(EMPTY_BLOCKED);
/*     */       } 
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 221 */     super.renderBackground(graphics, mouseX, mouseY, a);
/*     */     
/* 223 */     int marginX = marginX() + 3;
/* 224 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, marginX, 64, 236, windowHeight() + 16);
/* 225 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SEARCH_SPRITE, marginX + 10, 76, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 230 */     super.render(graphics, mouseX, mouseY, a);
/* 231 */     updateServerLabel(this.minecraft);
/*     */     
/* 233 */     if (this.serverLabel != null) {
/* 234 */       graphics.drawString(this.minecraft.font, this.serverLabel, marginX() + 8, 35, -1);
/*     */     }
/*     */     
/* 237 */     if (!this.socialInteractionsPlayerList.isEmpty()) {
/* 238 */       this.socialInteractionsPlayerList.render(graphics, mouseX, mouseY, a);
/* 239 */     } else if (!this.searchBox.getValue().isEmpty()) {
/* 240 */       graphics.drawCenteredString(this.minecraft.font, EMPTY_SEARCH, this.width / 2, (72 + listEnd()) / 2, -1);
/* 241 */     } else if (this.page == Page.HIDDEN) {
/* 242 */       graphics.drawCenteredString(this.minecraft.font, EMPTY_HIDDEN, this.width / 2, (72 + listEnd()) / 2, -1);
/* 243 */     } else if (this.page == Page.BLOCKED) {
/* 244 */       graphics.drawCenteredString(this.minecraft.font, EMPTY_BLOCKED, this.width / 2, (72 + listEnd()) / 2, -1);
/*     */     } 
/*     */     
/* 247 */     this.blockingHintButton.visible = (this.page == Page.BLOCKED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 252 */     if (!this.searchBox.isFocused() && this.minecraft.options.keySocialInteractions.matches(event)) {
/* 253 */       onClose();
/* 254 */       return true;
/*     */     } 
/* 256 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 261 */     return false;
/*     */   }
/*     */   
/*     */   private void checkSearchStringUpdate(String searchText) {
/* 265 */     searchText = searchText.toLowerCase(Locale.ROOT);
/* 266 */     if (!searchText.equals(this.lastSearch)) {
/* 267 */       this.socialInteractionsPlayerList.setFilter(searchText);
/* 268 */       this.lastSearch = searchText;
/* 269 */       showPage(this.page);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateServerLabel(Minecraft minecraft) {
/* 274 */     int playerCount = minecraft.getConnection().getOnlinePlayers().size();
/* 275 */     if (this.playerCount != playerCount) {
/* 276 */       String serverName = "";
/* 277 */       ServerData currentServer = minecraft.getCurrentServer();
/* 278 */       if (minecraft.isLocalServer()) {
/* 279 */         serverName = minecraft.getSingleplayerServer().getMotd();
/* 280 */       } else if (currentServer != null) {
/* 281 */         serverName = currentServer.name;
/*     */       } 
/* 283 */       if (playerCount > 1) {
/* 284 */         this.serverLabel = (Component)Component.translatable("gui.socialInteractions.server_label.multiple", new Object[] { serverName, playerCount });
/*     */       } else {
/* 286 */         this.serverLabel = (Component)Component.translatable("gui.socialInteractions.server_label.single", new Object[] { serverName, playerCount });
/*     */       } 
/* 288 */       this.playerCount = playerCount;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onAddPlayer(PlayerInfo info) {
/* 293 */     this.socialInteractionsPlayerList.addPlayer(info, this.page);
/*     */   }
/*     */   
/*     */   public void onRemovePlayer(UUID id) {
/* 297 */     this.socialInteractionsPlayerList.removePlayer(id);
/*     */   }
/*     */   
/*     */   public enum Page {
/* 301 */     ALL,
/* 302 */     HIDDEN,
/* 303 */     BLOCKED;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/social/SocialInteractionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */