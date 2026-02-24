/*     */ package net.minecraft.client.gui.screens;
/*     */ import com.mojang.text2speech.Narrator;
/*     */ import net.minecraft.client.NarratorStatus;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CommonButtons;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.LogoRenderer;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class AccessibilityOnboardingScreen extends Screen {
/*  25 */   private static final Component TITLE = (Component)Component.translatable("accessibility.onboarding.screen.title");
/*  26 */   private static final Component ONBOARDING_NARRATOR_MESSAGE = (Component)Component.translatable("accessibility.onboarding.screen.narrator");
/*     */   
/*     */   private static final int PADDING = 4;
/*     */   
/*     */   private static final int TITLE_PADDING = 16;
/*     */   
/*     */   private static final float FADE_OUT_TIME = 1000.0F;
/*     */   
/*     */   private static final int TEXT_WIDGET_WIDTH = 374;
/*     */   
/*     */   private final LogoRenderer logoRenderer;
/*     */   private final Options options;
/*     */   private final boolean narratorAvailable;
/*     */   private boolean hasNarrated;
/*     */   private float timer;
/*     */   private final Runnable onClose;
/*  42 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, initTitleYPos(), 33);
/*     */   
/*     */   private float fadeInStart;
/*     */   private boolean fadingIn = true;
/*     */   private float fadeOutStart;
/*     */   
/*     */   public AccessibilityOnboardingScreen(Options options, Runnable onClose) {
/*  49 */     super(TITLE);
/*  50 */     this.options = options;
/*  51 */     this.onClose = onClose;
/*  52 */     this.logoRenderer = new LogoRenderer(true);
/*  53 */     this.narratorAvailable = net.minecraft.client.Minecraft.getInstance().getNarrator().isActive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  58 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical());
/*  59 */     content.defaultCellSetting().alignHorizontallyCenter().padding(4);
/*     */     
/*  61 */     content.addChild((LayoutElement)FocusableTextWidget.builder(this.title, this.font).maxWidth(374).build(), w -> w.padding(8));
/*     */     
/*  63 */     AbstractWidget abstractWidget = this.options.narrator().createButton(this.options); if (abstractWidget instanceof CycleButton) { CycleButton<NarratorStatus> cycleButton = (CycleButton)abstractWidget;
/*  64 */       this.narratorButton = cycleButton;
/*  65 */       this.narratorButton.active = this.narratorAvailable;
/*  66 */       content.addChild((LayoutElement)this.narratorButton); }
/*     */     
/*  68 */     content.addChild((LayoutElement)CommonButtons.accessibility(150, button -> closeAndSetScreen((Screen)new AccessibilityOptionsScreen(this, this.minecraft.options)), false));
/*  69 */     content.addChild((LayoutElement)CommonButtons.language(150, button -> closeAndSetScreen((Screen)new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), false));
/*     */     
/*  71 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_CONTINUE, button -> onClose()).build());
/*     */     
/*  73 */     this.layout.visitWidgets(this::addRenderableWidget);
/*  74 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  79 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  84 */     if (this.narratorAvailable && this.narratorButton != null) {
/*  85 */       setInitialFocus((GuiEventListener)this.narratorButton);
/*     */     } else {
/*  87 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int initTitleYPos() {
/*  92 */     return 90;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  97 */     if (this.fadeOutStart == 0.0F) {
/*  98 */       this.fadeOutStart = (float)Util.getMillis();
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeAndSetScreen(Screen screen) {
/* 103 */     close(false, () -> this.minecraft.setScreen(screen));
/*     */   }
/*     */   
/*     */   private void close(boolean onboardingFinished, Runnable runnable) {
/* 107 */     if (onboardingFinished) {
/* 108 */       this.options.onboardingAccessibilityFinished();
/*     */     }
/* 110 */     Narrator.getNarrator().clear();
/* 111 */     runnable.run();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 116 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 118 */     handleInitialNarrationDelay();
/*     */     
/* 120 */     if (this.fadeInStart == 0.0F && this.fadingIn) {
/* 121 */       this.fadeInStart = (float)Util.getMillis();
/*     */     }
/*     */     
/* 124 */     if (this.fadeInStart > 0.0F) {
/* 125 */       float fade = ((float)Util.getMillis() - this.fadeInStart) / 2000.0F;
/* 126 */       float widgetAlpha = 1.0F;
/* 127 */       if (fade >= 1.0F) {
/* 128 */         this.fadingIn = false;
/* 129 */         this.fadeInStart = 0.0F;
/*     */       } else {
/* 131 */         fade = Mth.clamp(fade, 0.0F, 1.0F);
/* 132 */         widgetAlpha = Mth.clampedMap(fade, 0.5F, 1.0F, 0.0F, 1.0F);
/*     */       } 
/* 134 */       fadeWidgets(widgetAlpha);
/*     */     } 
/*     */     
/* 137 */     if (this.fadeOutStart > 0.0F) {
/* 138 */       float fade = 1.0F - ((float)Util.getMillis() - this.fadeOutStart) / 1000.0F;
/* 139 */       float widgetAlpha = 0.0F;
/* 140 */       if (fade <= 0.0F) {
/* 141 */         this.fadeOutStart = 0.0F;
/* 142 */         close(true, this.onClose);
/*     */       } else {
/* 144 */         fade = Mth.clamp(fade, 0.0F, 1.0F);
/* 145 */         widgetAlpha = Mth.clampedMap(fade, 0.5F, 1.0F, 0.0F, 1.0F);
/*     */       } 
/* 147 */       fadeWidgets(widgetAlpha);
/*     */     } 
/*     */     
/* 150 */     this.logoRenderer.renderLogo(graphics, this.width, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean panoramaShouldSpin() {
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   private void handleInitialNarrationDelay() {
/* 159 */     if (!this.hasNarrated && this.narratorAvailable)
/* 160 */       if (this.timer < 40.0F) {
/* 161 */         this.timer++;
/* 162 */       } else if (this.minecraft.isWindowActive()) {
/*     */         
/* 164 */         Narrator.getNarrator().say(ONBOARDING_NARRATOR_MESSAGE.getString(), true, this.minecraft.options.getFinalSoundSourceVolume(net.minecraft.sounds.SoundSource.VOICE));
/* 165 */         this.hasNarrated = true;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/AccessibilityOnboardingScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */