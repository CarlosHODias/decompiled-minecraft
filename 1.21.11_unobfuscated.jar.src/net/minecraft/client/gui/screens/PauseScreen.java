/*     */ package net.minecraft.client.gui.screens;
/*     */ import java.net.URI;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.MusicToastDisplayState;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.toasts.NowPlayingToast;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.achievement.StatsScreen;
/*     */ import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsScreen;
/*     */ import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.dialog.Dialogs;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.DialogTags;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ public class PauseScreen extends Screen {
/*  40 */   private static final Identifier DRAFT_REPORT_SPRITE = Identifier.withDefaultNamespace("icon/draft_report");
/*     */   
/*     */   private static final int COLUMNS = 2;
/*     */   
/*     */   private static final int MENU_PADDING_TOP = 50;
/*     */   private static final int BUTTON_PADDING = 4;
/*     */   private static final int BUTTON_WIDTH_FULL = 204;
/*     */   private static final int BUTTON_WIDTH_HALF = 98;
/*  48 */   private static final Component RETURN_TO_GAME = (Component)Component.translatable("menu.returnToGame");
/*  49 */   private static final Component ADVANCEMENTS = (Component)Component.translatable("gui.advancements");
/*  50 */   private static final Component STATS = (Component)Component.translatable("gui.stats");
/*  51 */   private static final Component SEND_FEEDBACK = (Component)Component.translatable("menu.sendFeedback");
/*  52 */   private static final Component REPORT_BUGS = (Component)Component.translatable("menu.reportBugs");
/*  53 */   private static final Component FEEDBACK_SUBSCREEN = (Component)Component.translatable("menu.feedback");
/*  54 */   private static final Component OPTIONS = (Component)Component.translatable("menu.options");
/*  55 */   private static final Component SHARE_TO_LAN = (Component)Component.translatable("menu.shareToLan");
/*  56 */   private static final Component PLAYER_REPORTING = (Component)Component.translatable("menu.playerReporting");
/*  57 */   private static final Component GAME = (Component)Component.translatable("menu.game");
/*  58 */   private static final Component PAUSED = (Component)Component.translatable("menu.paused");
/*     */   
/*  60 */   private static final Tooltip CUSTOM_OPTIONS_TOOLTIP = Tooltip.create((Component)Component.translatable("menu.custom_options.tooltip"));
/*     */   
/*     */   private final boolean showPauseMenu;
/*     */   
/*     */   private Button disconnectButton;
/*     */   
/*     */   public PauseScreen(boolean showPauseMenu) {
/*  67 */     super(showPauseMenu ? GAME : PAUSED);
/*  68 */     this.showPauseMenu = showPauseMenu;
/*     */   }
/*     */   
/*     */   public boolean showsPauseMenu() {
/*  72 */     return this.showPauseMenu;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  77 */     if (this.showPauseMenu) {
/*  78 */       createPauseMenu();
/*     */     }
/*     */     
/*  81 */     int textWidth = this.font.width((FormattedText)this.title);
/*  82 */     java.util.Objects.requireNonNull(this.font); addRenderableWidget(new StringWidget(this.width / 2 - textWidth / 2, this.showPauseMenu ? 40 : 10, textWidth, 9, this.title, this.font));
/*     */   }
/*     */   
/*     */   private void createPauseMenu() {
/*  86 */     GridLayout gridLayout = new GridLayout();
/*  87 */     gridLayout.defaultCellSetting().padding(4, 4, 4, 0);
/*     */     
/*  89 */     GridLayout.RowHelper helper = gridLayout.createRowHelper(2);
/*     */     
/*  91 */     helper.addChild(
/*  92 */         (LayoutElement)Button.builder(RETURN_TO_GAME, button -> {
/*     */             this.minecraft.setScreen(null);
/*     */             this.minecraft.mouseHandler.grabMouse();
/*  95 */           }).width(204).build(), 2, 
/*  96 */         gridLayout.newCellSettings().paddingTop(50));
/*     */ 
/*     */     
/*  99 */     helper.addChild((LayoutElement)openScreenButton(ADVANCEMENTS, () -> new AdvancementsScreen(this.minecraft.player.connection.getAdvancements(), this)));
/* 100 */     helper.addChild((LayoutElement)openScreenButton(STATS, () -> new StatsScreen(this, this.minecraft.player.getStats())));
/*     */     
/* 102 */     Optional<? extends Holder<Dialog>> additions = getCustomAdditions();
/* 103 */     if (additions.isEmpty()) {
/* 104 */       addFeedbackButtons(this, helper);
/*     */     } else {
/* 106 */       addFeedbackSubscreenAndCustomDialogButtons(this.minecraft, additions.get(), helper);
/*     */     } 
/*     */     
/* 109 */     helper.addChild((LayoutElement)openScreenButton(OPTIONS, () -> new OptionsScreen(this, this.minecraft.options)));
/*     */     
/* 111 */     if (this.minecraft.hasSingleplayerServer() && !this.minecraft.getSingleplayerServer().isPublished()) {
/* 112 */       helper.addChild((LayoutElement)openScreenButton(SHARE_TO_LAN, () -> new ShareToLanScreen(this)));
/*     */     } else {
/* 114 */       helper.addChild((LayoutElement)openScreenButton(PLAYER_REPORTING, () -> new SocialInteractionsScreen(this)));
/*     */     } 
/*     */     
/* 117 */     this.disconnectButton = (Button)helper.addChild(
/* 118 */         (LayoutElement)Button.builder(CommonComponents.disconnectButtonLabel(this.minecraft.isLocalServer()), button -> {
/*     */             button.active = false;
/*     */             
/*     */             this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, (), true);
/* 122 */           }).width(204).build(), 2);
/*     */ 
/*     */ 
/*     */     
/* 126 */     gridLayout.arrangeElements();
/* 127 */     FrameLayout.alignInRectangle((LayoutElement)gridLayout, 0, 0, this.width, this.height, 0.5F, 0.25F);
/*     */     
/* 129 */     gridLayout.visitWidgets(this::addRenderableWidget);
/*     */   }
/*     */   
/*     */   private Optional<? extends Holder<Dialog>> getCustomAdditions() {
/* 133 */     Registry<Dialog> dialogRegistry = this.minecraft.player.connection.registryAccess().lookupOrThrow(Registries.DIALOG);
/* 134 */     Optional<? extends HolderSet<Dialog>> maybeCustomAdditions = dialogRegistry.get(DialogTags.PAUSE_SCREEN_ADDITIONS);
/*     */     
/* 136 */     if (maybeCustomAdditions.isPresent()) {
/* 137 */       HolderSet<Dialog> customAdditions = maybeCustomAdditions.get();
/* 138 */       if (customAdditions.size() > 0) {
/* 139 */         if (customAdditions.size() == 1)
/*     */         {
/* 141 */           return Optional.of(customAdditions.get(0));
/*     */         }
/*     */         
/* 144 */         return dialogRegistry.get(Dialogs.CUSTOM_OPTIONS);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 150 */     ServerLinks serverLinks = this.minecraft.player.connection.serverLinks();
/* 151 */     if (!serverLinks.isEmpty()) {
/* 152 */       return dialogRegistry.get(Dialogs.SERVER_LINKS);
/*     */     }
/*     */     
/* 155 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void addFeedbackButtons(Screen screen, GridLayout.RowHelper helper) {
/* 159 */     helper.addChild((LayoutElement)openLinkButton(screen, SEND_FEEDBACK, SharedConstants.getCurrentVersion().stable() ? CommonLinks.RELEASE_FEEDBACK : CommonLinks.SNAPSHOT_FEEDBACK));
/* 160 */     ((Button)helper.addChild((LayoutElement)openLinkButton(screen, REPORT_BUGS, CommonLinks.SNAPSHOT_BUGS_FEEDBACK))).active = !SharedConstants.getCurrentVersion().dataVersion().isSideSeries();
/*     */   }
/*     */   
/*     */   private void addFeedbackSubscreenAndCustomDialogButtons(Minecraft minecraft, Holder<Dialog> dialog, GridLayout.RowHelper helper) {
/* 164 */     helper.addChild((LayoutElement)openScreenButton(FEEDBACK_SUBSCREEN, () -> new FeedbackSubScreen(this)));
/* 165 */     helper.addChild(
/* 166 */         (LayoutElement)Button.builder(((Dialog)dialog.value()).common().computeExternalTitle(), button -> minecraft.player.connection.showDialog(minecraft, this))
/* 167 */         .width(98)
/* 168 */         .tooltip(CUSTOM_OPTIONS_TOOLTIP)
/* 169 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 175 */     if (rendersNowPlayingToast()) {
/* 176 */       NowPlayingToast.tickMusicNotes();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 182 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 184 */     if (rendersNowPlayingToast()) {
/* 185 */       NowPlayingToast.renderToast(graphics, this.font);
/*     */     }
/*     */     
/* 188 */     if (this.showPauseMenu && this.minecraft.getReportingContext().hasDraftReport() && this.disconnectButton != null) {
/* 189 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DRAFT_REPORT_SPRITE, this.disconnectButton.getX() + this.disconnectButton.getWidth() - 17, this.disconnectButton.getY() + 3, 15, 15);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 195 */     if (this.showPauseMenu) {
/* 196 */       super.renderBackground(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean rendersNowPlayingToast() {
/* 201 */     Options options = this.minecraft.options;
/* 202 */     return (((MusicToastDisplayState)options.musicToast().get()).renderInPauseScreen() && 
/* 203 */       options.getFinalSoundSourceVolume(SoundSource.MUSIC) > 0.0F && this.showPauseMenu);
/*     */   }
/*     */ 
/*     */   
/*     */   private Button openScreenButton(Component message, Supplier<Screen> newScreen) {
/* 208 */     return Button.builder(message, button -> this.minecraft.setScreen(newScreen.get())).width(98).build();
/*     */   }
/*     */   
/*     */   private static Button openLinkButton(Screen screen, Component message, URI link) {
/* 212 */     return Button.builder(message, ConfirmLinkScreen.confirmLink(screen, link)).width(98).build();
/*     */   }
/*     */   
/*     */   private static class FeedbackSubScreen extends Screen {
/* 216 */     private static final Component TITLE = (Component)Component.translatable("menu.feedback.title"); public final Screen parent;
/*     */     private final HeaderAndFooterLayout layout;
/*     */     
/*     */     protected FeedbackSubScreen(Screen parent) {
/* 220 */       super(TITLE);
/*     */ 
/*     */ 
/*     */       
/* 224 */       this.layout = new HeaderAndFooterLayout(this);
/*     */       this.parent = parent;
/*     */     }
/*     */     protected void init() {
/* 228 */       this.layout.addTitleHeader(TITLE, this.font);
/*     */       
/* 230 */       GridLayout buttonContainer = (GridLayout)this.layout.addToContents((LayoutElement)new GridLayout());
/* 231 */       buttonContainer.defaultCellSetting().padding(4, 4, 4, 0);
/* 232 */       GridLayout.RowHelper helper = buttonContainer.createRowHelper(2);
/* 233 */       PauseScreen.addFeedbackButtons(this, helper);
/*     */       
/* 235 */       this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(200).build());
/*     */       
/* 237 */       this.layout.visitWidgets(this::addRenderableWidget);
/* 238 */       repositionElements();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void repositionElements() {
/* 243 */       this.layout.arrangeElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onClose() {
/* 248 */       this.minecraft.setScreen(this.parent);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/PauseScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */