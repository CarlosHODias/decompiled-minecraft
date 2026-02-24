/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.PlayerFaceRenderer;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.reporting.ReportPlayerScreen;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ 
/*     */ public class PlayerEntry
/*     */   extends ContainerObjectSelectionList.Entry<PlayerEntry> {
/*  35 */   private static final Identifier DRAFT_REPORT_SPRITE = Identifier.withDefaultNamespace("icon/draft_report");
/*  36 */   private static final Duration TOOLTIP_DELAY = Duration.ofMillis(500L);
/*  37 */   private static final WidgetSprites REPORT_BUTTON_SPRITES = new WidgetSprites(
/*  38 */       Identifier.withDefaultNamespace("social_interactions/report_button"), 
/*  39 */       Identifier.withDefaultNamespace("social_interactions/report_button_disabled"), 
/*  40 */       Identifier.withDefaultNamespace("social_interactions/report_button_highlighted"));
/*     */   
/*  42 */   private static final WidgetSprites MUTE_BUTTON_SPRITES = new WidgetSprites(
/*  43 */       Identifier.withDefaultNamespace("social_interactions/mute_button"), 
/*  44 */       Identifier.withDefaultNamespace("social_interactions/mute_button_highlighted"));
/*     */   
/*  46 */   private static final WidgetSprites UNMUTE_BUTTON_SPRITES = new WidgetSprites(
/*  47 */       Identifier.withDefaultNamespace("social_interactions/unmute_button"), 
/*  48 */       Identifier.withDefaultNamespace("social_interactions/unmute_button_highlighted"));
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final List<AbstractWidget> children;
/*     */   
/*     */   private final UUID id;
/*     */   
/*     */   private final String playerName;
/*     */   
/*     */   private final Supplier<PlayerSkin> skinGetter;
/*     */   
/*     */   private boolean isRemoved;
/*     */   private boolean hasRecentMessages;
/*     */   private final boolean reportingEnabled;
/*     */   private boolean hasDraftReport;
/*     */   private final boolean chatReportable;
/*     */   private Button hideButton;
/*     */   private Button showButton;
/*     */   private Button reportButton;
/*     */   private float tooltipHoverTime;
/*  69 */   private static final Component HIDDEN = (Component)Component.translatable("gui.socialInteractions.status_hidden").withStyle(ChatFormatting.ITALIC);
/*  70 */   private static final Component BLOCKED = (Component)Component.translatable("gui.socialInteractions.status_blocked").withStyle(ChatFormatting.ITALIC);
/*  71 */   private static final Component OFFLINE = (Component)Component.translatable("gui.socialInteractions.status_offline").withStyle(ChatFormatting.ITALIC);
/*  72 */   private static final Component HIDDEN_OFFLINE = (Component)Component.translatable("gui.socialInteractions.status_hidden_offline").withStyle(ChatFormatting.ITALIC);
/*  73 */   private static final Component BLOCKED_OFFLINE = (Component)Component.translatable("gui.socialInteractions.status_blocked_offline").withStyle(ChatFormatting.ITALIC);
/*  74 */   private static final Component REPORT_DISABLED_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.report.disabled");
/*  75 */   private static final Component HIDE_TEXT_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.hide");
/*  76 */   private static final Component SHOW_TEXT_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.show");
/*  77 */   private static final Component REPORT_PLAYER_TOOLTIP = (Component)Component.translatable("gui.socialInteractions.tooltip.report");
/*     */   
/*     */   private static final int SKIN_SIZE = 24;
/*     */   private static final int PADDING = 4;
/*  81 */   public static final int SKIN_SHADE = ARGB.color(190, 0, 0, 0);
/*     */   private static final int CHAT_TOGGLE_ICON_SIZE = 20;
/*  83 */   public static final int BG_FILL = ARGB.color(255, 74, 74, 74);
/*  84 */   public static final int BG_FILL_REMOVED = ARGB.color(255, 48, 48, 48);
/*  85 */   public static final int PLAYERNAME_COLOR = ARGB.color(255, 255, 255, 255);
/*  86 */   public static final int PLAYER_STATUS_COLOR = ARGB.color(140, 255, 255, 255);
/*     */   
/*     */   public PlayerEntry(Minecraft minecraft, SocialInteractionsScreen socialInteractionsScreen, UUID id, String playerName, Supplier<PlayerSkin> skinGetter, boolean chatReportable) {
/*  89 */     this.minecraft = minecraft;
/*  90 */     this.id = id;
/*  91 */     this.playerName = playerName;
/*  92 */     this.skinGetter = skinGetter;
/*     */     
/*  94 */     ReportingContext reportingContext = minecraft.getReportingContext();
/*  95 */     this.reportingEnabled = reportingContext.sender().isEnabled();
/*  96 */     this.chatReportable = chatReportable;
/*  97 */     refreshHasDraftReport(reportingContext);
/*     */     
/*  99 */     MutableComponent mutableComponent1 = Component.translatable("gui.socialInteractions.narration.hide", new Object[] { playerName });
/* 100 */     MutableComponent mutableComponent2 = Component.translatable("gui.socialInteractions.narration.show", new Object[] { playerName });
/*     */     
/* 102 */     PlayerSocialManager socialManager = minecraft.getPlayerSocialManager();
/* 103 */     boolean chatAllowed = minecraft.getChatStatus().isChatAllowed(minecraft.isLocalServer());
/* 104 */     boolean notLocalPlayer = !minecraft.player.getUUID().equals(id);
/* 105 */     if (SharedConstants.DEBUG_SOCIAL_INTERACTIONS || (notLocalPlayer && chatAllowed && !socialManager.isBlocked(id))) {
/* 106 */       this
/*     */         
/* 108 */         .reportButton = (Button)new ImageButton(0, 0, 20, 20, REPORT_BUTTON_SPRITES, button -> reportingContext.draftReportHandled(reportingContext, minecraft, (), false), (Component)Component.translatable("gui.socialInteractions.report"))
/*     */         {
/*     */           protected MutableComponent createNarrationMessage() {
/* 111 */             return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */           }
/*     */         };
/* 114 */       this.reportButton.active = this.reportingEnabled;
/* 115 */       this.reportButton.setTooltip(createReportButtonTooltip());
/* 116 */       this.reportButton.setTooltipDelay(TOOLTIP_DELAY);
/* 117 */       this
/*     */ 
/*     */         
/* 120 */         .hideButton = (Button)new ImageButton(0, 0, 20, 20, MUTE_BUTTON_SPRITES, button -> { socialManager.hidePlayer(socialManager); onHiddenOrShown(true, (Component)Component.translatable("gui.socialInteractions.hidden_in_chat", new Object[] { id })); }, (Component)Component.translatable("gui.socialInteractions.hide"))
/*     */         {
/*     */           protected MutableComponent createNarrationMessage() {
/* 123 */             return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */           }
/*     */         };
/* 126 */       this.hideButton.setTooltip(Tooltip.create(HIDE_TEXT_TOOLTIP, (Component)mutableComponent1));
/* 127 */       this.hideButton.setTooltipDelay(TOOLTIP_DELAY);
/* 128 */       this
/*     */ 
/*     */         
/* 131 */         .showButton = (Button)new ImageButton(0, 0, 20, 20, UNMUTE_BUTTON_SPRITES, button -> { socialManager.showPlayer(socialManager); onHiddenOrShown(false, (Component)Component.translatable("gui.socialInteractions.shown_in_chat", new Object[] { id })); }, (Component)Component.translatable("gui.socialInteractions.show"))
/*     */         {
/*     */           protected MutableComponent createNarrationMessage() {
/* 134 */             return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */           }
/*     */         };
/* 137 */       this.showButton.setTooltip(Tooltip.create(SHOW_TEXT_TOOLTIP, (Component)mutableComponent2));
/* 138 */       this.showButton.setTooltipDelay(TOOLTIP_DELAY);
/*     */       
/* 140 */       this.children = new ArrayList<>();
/* 141 */       this.children.add(this.hideButton);
/* 142 */       this.children.add(this.reportButton);
/* 143 */       updateHideAndShowButton(socialManager.isHidden(this.id));
/*     */     } else {
/* 145 */       this.children = (List<AbstractWidget>)ImmutableList.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refreshHasDraftReport(ReportingContext reportingContext) {
/* 150 */     this.hasDraftReport = reportingContext.hasDraftReportFor(this.id);
/*     */   }
/*     */   
/*     */   private Tooltip createReportButtonTooltip() {
/* 154 */     if (!this.reportingEnabled) {
/* 155 */       return Tooltip.create(REPORT_DISABLED_TOOLTIP);
/*     */     }
/* 157 */     return Tooltip.create(REPORT_PLAYER_TOOLTIP, 
/*     */         
/* 159 */         (Component)Component.translatable("gui.socialInteractions.narration.report", new Object[] { this.playerName }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */     int textStartY;
/* 165 */     int skinX = getContentX() + 4;
/* 166 */     int skinY = getContentY() + (getContentHeight() - 24) / 2;
/* 167 */     int textStartX = skinX + 24 + 4;
/*     */ 
/*     */     
/* 170 */     Component status = getStatusComponent();
/* 171 */     if (status == CommonComponents.EMPTY) {
/* 172 */       graphics.fill(getContentX(), getContentY(), getContentRight(), getContentBottom(), BG_FILL);
/* 173 */       Objects.requireNonNull(this.minecraft.font); textStartY = getContentY() + (getContentHeight() - 9) / 2;
/*     */     } else {
/* 175 */       graphics.fill(getContentX(), getContentY(), getContentRight(), getContentBottom(), BG_FILL_REMOVED);
/* 176 */       Objects.requireNonNull(this.minecraft.font); Objects.requireNonNull(this.minecraft.font); textStartY = getContentY() + (getContentHeight() - 9 + 9) / 2;
/* 177 */       graphics.drawString(this.minecraft.font, status, textStartX, textStartY + 12, PLAYER_STATUS_COLOR);
/*     */     } 
/*     */     
/* 180 */     PlayerFaceRenderer.draw(graphics, this.skinGetter.get(), skinX, skinY, 24);
/*     */     
/* 182 */     graphics.drawString(this.minecraft.font, this.playerName, textStartX, textStartY, PLAYERNAME_COLOR);
/*     */     
/* 184 */     if (this.isRemoved) {
/* 185 */       graphics.fill(skinX, skinY, skinX + 24, skinY + 24, SKIN_SHADE);
/*     */     }
/*     */     
/* 188 */     if (this.hideButton != null && this.showButton != null && this.reportButton != null) {
/* 189 */       float lastHoverTime = this.tooltipHoverTime;
/*     */       
/* 191 */       this.hideButton.setX(getContentX() + getContentWidth() - this.hideButton.getWidth() - 4 - 20 - 4);
/* 192 */       this.hideButton.setY(getContentY() + (getContentHeight() - this.hideButton.getHeight()) / 2);
/* 193 */       this.hideButton.render(graphics, mouseX, mouseY, a);
/* 194 */       this.showButton.setX(getContentX() + getContentWidth() - this.showButton.getWidth() - 4 - 20 - 4);
/* 195 */       this.showButton.setY(getContentY() + (getContentHeight() - this.showButton.getHeight()) / 2);
/* 196 */       this.showButton.render(graphics, mouseX, mouseY, a);
/* 197 */       this.reportButton.setX(getContentX() + getContentWidth() - this.showButton.getWidth() - 4);
/* 198 */       this.reportButton.setY(getContentY() + (getContentHeight() - this.showButton.getHeight()) / 2);
/* 199 */       this.reportButton.render(graphics, mouseX, mouseY, a);
/*     */       
/* 201 */       if (lastHoverTime == this.tooltipHoverTime) {
/* 202 */         this.tooltipHoverTime = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 206 */     if (this.hasDraftReport && this.reportButton != null) {
/* 207 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DRAFT_REPORT_SPRITE, this.reportButton.getX() + 5, this.reportButton.getY() + 1, 15, 15);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 213 */     return (List)this.children;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 218 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public String getPlayerName() {
/* 222 */     return this.playerName;
/*     */   }
/*     */   
/*     */   public UUID getPlayerId() {
/* 226 */     return this.id;
/*     */   }
/*     */   
/*     */   public Supplier<PlayerSkin> getSkinGetter() {
/* 230 */     return this.skinGetter;
/*     */   }
/*     */   
/*     */   public void setRemoved(boolean isRemoved) {
/* 234 */     this.isRemoved = isRemoved;
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 238 */     return this.isRemoved;
/*     */   }
/*     */   
/*     */   public void setHasRecentMessages(boolean hasRecentMessages) {
/* 242 */     this.hasRecentMessages = hasRecentMessages;
/*     */   }
/*     */   
/*     */   public boolean hasRecentMessages() {
/* 246 */     return this.hasRecentMessages;
/*     */   }
/*     */   
/*     */   public boolean isChatReportable() {
/* 250 */     return this.chatReportable;
/*     */   }
/*     */   
/*     */   private void onHiddenOrShown(boolean isHidden, Component message) {
/* 254 */     updateHideAndShowButton(isHidden);
/* 255 */     this.minecraft.gui.getChat().addMessage(message);
/* 256 */     this.minecraft.getNarrator().saySystemNow(message);
/*     */   }
/*     */   
/*     */   private void updateHideAndShowButton(boolean isHidden) {
/* 260 */     this.showButton.visible = isHidden;
/* 261 */     this.hideButton.visible = !isHidden;
/* 262 */     this.children.set(0, isHidden ? this.showButton : this.hideButton);
/*     */   }
/*     */   
/*     */   private MutableComponent getEntryNarationMessage(MutableComponent buttonNarrationMessage) {
/* 266 */     Component status = getStatusComponent();
/* 267 */     if (status == CommonComponents.EMPTY) {
/* 268 */       return Component.literal(this.playerName).append(", ").append((Component)buttonNarrationMessage);
/*     */     }
/* 270 */     return Component.literal(this.playerName).append(", ").append(status).append(", ").append((Component)buttonNarrationMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   private Component getStatusComponent() {
/* 275 */     boolean isHidden = this.minecraft.getPlayerSocialManager().isHidden(this.id);
/* 276 */     boolean isBlocked = this.minecraft.getPlayerSocialManager().isBlocked(this.id);
/*     */     
/* 278 */     if (isBlocked && this.isRemoved)
/* 279 */       return BLOCKED_OFFLINE; 
/* 280 */     if (isHidden && this.isRemoved)
/* 281 */       return HIDDEN_OFFLINE; 
/* 282 */     if (isBlocked)
/* 283 */       return BLOCKED; 
/* 284 */     if (isHidden)
/* 285 */       return HIDDEN; 
/* 286 */     if (this.isRemoved) {
/* 287 */       return OFFLINE;
/*     */     }
/* 289 */     return CommonComponents.EMPTY;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/social/PlayerEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */