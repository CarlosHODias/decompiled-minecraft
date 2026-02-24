/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AddRealmPopupScreen
/*     */   extends RealmsScreen
/*     */ {
/*  26 */   private static final Component POPUP_TEXT = (Component)Component.translatable("mco.selectServer.popup");
/*  27 */   private static final Component CLOSE_TEXT = (Component)Component.translatable("mco.selectServer.close");
/*     */   
/*  29 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("popup/background");
/*  30 */   private static final Identifier TRIAL_AVAILABLE_SPRITE = Identifier.withDefaultNamespace("icon/trial_available");
/*     */   
/*  32 */   private static final WidgetSprites CROSS_BUTTON_SPRITES = new WidgetSprites(
/*  33 */       Identifier.withDefaultNamespace("widget/cross_button"), 
/*  34 */       Identifier.withDefaultNamespace("widget/cross_button_highlighted"));
/*     */   
/*     */   private static final int IMAGE_WIDTH = 195;
/*     */   
/*     */   private static final int IMAGE_HEIGHT = 152;
/*     */   
/*     */   private static final int BG_BORDER_SIZE = 6;
/*     */   
/*     */   private static final int BUTTON_SPACING = 4;
/*     */   
/*     */   private static final int PADDING = 10;
/*     */   private static final int WIDTH = 320;
/*     */   private static final int HEIGHT = 172;
/*     */   private static final int TEXT_WIDTH = 100;
/*     */   private static final int BUTTON_WIDTH = 99;
/*     */   private static final int CAROUSEL_SWITCH_INTERVAL = 100;
/*  50 */   private static List<Identifier> carouselImages = List.of();
/*     */   
/*     */   private final Screen backgroundScreen;
/*     */   
/*     */   private final boolean trialAvailable;
/*     */   
/*     */   private Button createTrialButton;
/*     */   private int carouselIndex;
/*     */   private int carouselTick;
/*     */   
/*     */   public AddRealmPopupScreen(Screen backgroundScreen, boolean trialAvailable) {
/*  61 */     super(POPUP_TEXT);
/*  62 */     this.backgroundScreen = backgroundScreen;
/*  63 */     this.trialAvailable = trialAvailable;
/*     */   }
/*     */   
/*     */   public static void updateCarouselImages(ResourceManager resourceManager) {
/*  67 */     Collection<Identifier> candidates = resourceManager.listResources("textures/gui/images", s -> s.getPath().endsWith(".png")).keySet();
/*  68 */     carouselImages = candidates.stream().filter(id -> id.getNamespace().equals("realms")).toList();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  73 */     this.backgroundScreen.resize(this.width, this.height);
/*     */     
/*  75 */     if (this.trialAvailable) {
/*  76 */       this.createTrialButton = (Button)addRenderableWidget(
/*  77 */           (GuiEventListener)Button.builder((Component)Component.translatable("mco.selectServer.trial"), 
/*  78 */             ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.START_REALMS_TRIAL))
/*  79 */           .bounds(right() - 10 - 99, bottom() - 10 - 4 - 40, 99, 20).build());
/*     */     }
/*     */ 
/*     */     
/*  83 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.selectServer.buy"), 
/*  84 */           ConfirmLinkScreen.confirmLink((Screen)this, CommonLinks.BUY_REALMS))
/*  85 */         .bounds(right() - 10 - 99, bottom() - 10 - 20, 99, 20).build());
/*     */     
/*  87 */     ImageButton closeButton = (ImageButton)addRenderableWidget((GuiEventListener)new ImageButton(left() + 4, top() + 4, 14, 14, CROSS_BUTTON_SPRITES, button -> onClose(), CLOSE_TEXT));
/*  88 */     closeButton.setTooltip(Tooltip.create(CLOSE_TEXT));
/*     */     
/*  90 */     int textBoxHeight = 142 - (this.trialAvailable ? 40 : 20);
/*  91 */     FittingMultiLineTextWidget fittingMultiLineTextWidget = new FittingMultiLineTextWidget(right() - 10 - 100, top() + 10, 100, textBoxHeight, POPUP_TEXT, this.font);
/*  92 */     if (fittingMultiLineTextWidget.showingScrollBar()) {
/*  93 */       fittingMultiLineTextWidget.setWidth(94);
/*     */     }
/*  95 */     addRenderableWidget((GuiEventListener)fittingMultiLineTextWidget);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 100 */     super.tick();
/* 101 */     if (++this.carouselTick > 100) {
/* 102 */       this.carouselTick = 0;
/* 103 */       this.carouselIndex = (this.carouselIndex + 1) % carouselImages.size();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 109 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 111 */     if (this.createTrialButton != null) {
/* 112 */       renderDiamond(graphics, this.createTrialButton);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void renderDiamond(GuiGraphics graphics, Button button) {
/* 117 */     int size = 8;
/* 118 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRIAL_AVAILABLE_SPRITE, button.getX() + button.getWidth() - 8 - 4, button.getY() + button.getHeight() / 2 - 4, 8, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 123 */     this.backgroundScreen.renderBackground(graphics, -1, -1, a);
/* 124 */     graphics.nextStratum();
/* 125 */     this.backgroundScreen.render(graphics, -1, -1, a);
/* 126 */     graphics.nextStratum();
/* 127 */     renderTransparentBackground(graphics);
/* 128 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, left(), top(), 320, 172);
/* 129 */     if (!carouselImages.isEmpty()) {
/* 130 */       graphics.blit(RenderPipelines.GUI_TEXTURED, carouselImages.get(this.carouselIndex), left() + 10, top() + 10, 0.0F, 0.0F, 195, 152, 195, 152);
/*     */     }
/*     */   }
/*     */   
/*     */   private int left() {
/* 135 */     return (this.width - 320) / 2;
/*     */   }
/*     */   
/*     */   private int top() {
/* 139 */     return (this.height - 172) / 2;
/*     */   }
/*     */   
/*     */   private int right() {
/* 143 */     return left() + 320;
/*     */   }
/*     */   
/*     */   private int bottom() {
/* 147 */     return top() + 172;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 152 */     this.minecraft.setScreen(this.backgroundScreen);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/AddRealmPopupScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */