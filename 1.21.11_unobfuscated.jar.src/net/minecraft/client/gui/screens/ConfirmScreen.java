/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class ConfirmScreen
/*     */   extends Screen {
/*     */   private final Component message;
/*  17 */   protected LinearLayout layout = LinearLayout.vertical().spacing(8);
/*     */   protected Component yesButtonComponent;
/*     */   protected Component noButtonComponent;
/*     */   protected Button yesButton;
/*     */   protected Button noButton;
/*     */   private int delayTicker;
/*     */   protected final BooleanConsumer callback;
/*     */   
/*     */   public ConfirmScreen(BooleanConsumer callback, Component title, Component message) {
/*  26 */     this(callback, title, message, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
/*     */   }
/*     */   
/*     */   public ConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesButtonComponent, Component noButtonComponent) {
/*  30 */     super(title);
/*  31 */     this.callback = callback;
/*  32 */     this.message = message;
/*  33 */     this.yesButtonComponent = yesButtonComponent;
/*  34 */     this.noButtonComponent = noButtonComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/*  39 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.message });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  44 */     super.init();
/*  45 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*     */     
/*  47 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*     */     
/*  49 */     this.layout.addChild((LayoutElement)new MultiLineTextWidget(this.message, this.font).setMaxWidth(this.width - 50).setMaxRows(15).setCentered(true));
/*     */     
/*  51 */     addAdditionalText();
/*  52 */     LinearLayout buttonLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/*  53 */     buttonLayout.defaultCellSetting().paddingTop(16);
/*  54 */     addButtons(buttonLayout);
/*     */     
/*  56 */     this.layout.visitWidgets(this::addRenderableWidget);
/*  57 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  62 */     this.layout.arrangeElements();
/*  63 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalText() {}
/*     */ 
/*     */   
/*     */   protected void addButtons(LinearLayout buttonLayout) {
/*  71 */     this.yesButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.yesButtonComponent, button -> this.callback.accept(true)).build());
/*  72 */     this.noButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.noButtonComponent, button -> this.callback.accept(false)).build());
/*     */   }
/*     */   
/*     */   public void setDelay(int delay) {
/*  76 */     this.delayTicker = delay;
/*     */     
/*  78 */     this.yesButton.active = false;
/*  79 */     this.noButton.active = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  84 */     super.tick();
/*     */     
/*  86 */     if (--this.delayTicker == 0) {
/*  87 */       this.yesButton.active = true;
/*  88 */       this.noButton.active = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  99 */     if (this.delayTicker <= 0 && event.key() == 256) {
/* 100 */       this.callback.accept(false);
/* 101 */       return true;
/*     */     } 
/* 103 */     return super.keyPressed(event);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ConfirmScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */