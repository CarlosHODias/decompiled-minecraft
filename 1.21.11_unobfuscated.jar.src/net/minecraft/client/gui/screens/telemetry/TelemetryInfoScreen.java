/*     */ package net.minecraft.client.gui.screens.telemetry;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Checkbox;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class TelemetryInfoScreen extends Screen {
/*  22 */   private static final Component TITLE = (Component)Component.translatable("telemetry_info.screen.title");
/*  23 */   private static final Component DESCRIPTION = (Component)Component.translatable("telemetry_info.screen.description").withColor(-4539718);
/*  24 */   private static final Component BUTTON_PRIVACY_STATEMENT = (Component)Component.translatable("telemetry_info.button.privacy_statement");
/*  25 */   private static final Component BUTTON_GIVE_FEEDBACK = (Component)Component.translatable("telemetry_info.button.give_feedback");
/*  26 */   private static final Component BUTTON_VIEW_DATA = (Component)Component.translatable("telemetry_info.button.show_data");
/*  27 */   private static final Component CHECKBOX_OPT_IN = (Component)Component.translatable("telemetry_info.opt_in.description").withColor(-2039584);
/*     */   
/*     */   private static final int SPACING = 8;
/*  30 */   private static final boolean EXTRA_TELEMETRY_AVAILABLE = Minecraft.getInstance().extraTelemetryAvailable();
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final Options options;
/*     */   
/*     */   private final HeaderAndFooterLayout layout;
/*     */   
/*     */   private TelemetryEventWidget telemetryEventWidget;
/*     */   
/*     */   private MultiLineTextWidget description;
/*     */   
/*     */   private Checkbox checkbox;
/*     */   private double savedScroll;
/*     */   
/*     */   public TelemetryInfoScreen(Screen lastScreen, Options options) {
/*  46 */     super(TITLE); Objects.requireNonNull((Minecraft.getInstance()).font); this.layout = new HeaderAndFooterLayout(this, 16 + 9 * 5 + 20, EXTRA_TELEMETRY_AVAILABLE ? (33 + Checkbox.getBoxSize((Minecraft.getInstance()).font)) : 33);
/*  47 */     this.lastScreen = lastScreen;
/*  48 */     this.options = options;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/*  53 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), DESCRIPTION });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  58 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  59 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  60 */     header.addChild((LayoutElement)new StringWidget(TITLE, this.font));
/*  61 */     this.description = (MultiLineTextWidget)header.addChild((LayoutElement)new MultiLineTextWidget(DESCRIPTION, this.font).setCentered(true));
/*  62 */     LinearLayout upperContentButtons = (LinearLayout)header.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  63 */     upperContentButtons.addChild((LayoutElement)Button.builder(BUTTON_PRIVACY_STATEMENT, this::openPrivacyStatementLink).build());
/*  64 */     upperContentButtons.addChild((LayoutElement)Button.builder(BUTTON_GIVE_FEEDBACK, this::openFeedbackLink).build());
/*     */     
/*  66 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.vertical().spacing(4));
/*  67 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  68 */     if (EXTRA_TELEMETRY_AVAILABLE) {
/*  69 */       this.checkbox = (Checkbox)footer.addChild((LayoutElement)Checkbox.builder(CHECKBOX_OPT_IN, this.font)
/*  70 */           .maxWidth(this.width - 40)
/*  71 */           .selected(this.options.telemetryOptInExtra())
/*  72 */           .onValueChange(this::onOptInChanged)
/*  73 */           .build());
/*     */     }
/*  75 */     LinearLayout footerButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  76 */     footerButtons.addChild((LayoutElement)Button.builder(BUTTON_VIEW_DATA, this::openDataFolder).build());
/*  77 */     footerButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).build());
/*     */     
/*  79 */     LinearLayout content = (LinearLayout)this.layout.addToContents((LayoutElement)LinearLayout.vertical().spacing(8));
/*  80 */     this.telemetryEventWidget = (TelemetryEventWidget)content.addChild((LayoutElement)new TelemetryEventWidget(0, 0, this.width - 40, this.layout.getContentHeight(), this.font));
/*  81 */     this.telemetryEventWidget.setOnScrolledListener(scroll -> this.savedScroll = scroll);
/*     */     
/*  83 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  84 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  89 */     if (this.telemetryEventWidget != null) {
/*  90 */       this.telemetryEventWidget.setScrollAmount(this.savedScroll);
/*  91 */       this.telemetryEventWidget.setWidth(this.width - 40);
/*  92 */       this.telemetryEventWidget.setHeight(this.layout.getContentHeight());
/*  93 */       this.telemetryEventWidget.updateLayout();
/*     */     } 
/*  95 */     if (this.description != null) {
/*  96 */       this.description.setMaxWidth(this.width - 16);
/*     */     }
/*  98 */     if (this.checkbox != null) {
/*  99 */       this.checkbox.adjustWidth(this.width - 40, this.font);
/*     */     }
/* 101 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 106 */     if (this.telemetryEventWidget != null) {
/* 107 */       setInitialFocus((GuiEventListener)this.telemetryEventWidget);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onOptInChanged(AbstractWidget widget, boolean value) {
/* 112 */     if (this.telemetryEventWidget != null) {
/* 113 */       this.telemetryEventWidget.onOptInChanged(value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void openPrivacyStatementLink(Button button) {
/* 118 */     ConfirmLinkScreen.confirmLinkNow(this, CommonLinks.PRIVACY_STATEMENT);
/*     */   }
/*     */   
/*     */   private void openFeedbackLink(Button button) {
/* 122 */     ConfirmLinkScreen.confirmLinkNow(this, CommonLinks.RELEASE_FEEDBACK);
/*     */   }
/*     */   
/*     */   private void openDataFolder(Button button) {
/* 126 */     Util.getPlatform().openPath(this.minecraft.getTelemetryManager().getLogDirectory());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 131 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/telemetry/TelemetryInfoScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */