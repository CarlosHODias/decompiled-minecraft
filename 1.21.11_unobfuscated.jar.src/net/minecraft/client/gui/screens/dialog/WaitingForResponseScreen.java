/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class WaitingForResponseScreen extends Screen {
/* 13 */   private static final Component TITLE = (Component)Component.translatable("gui.waitingForResponse.title");
/*    */   
/* 15 */   private static final Component[] BUTTON_LABELS = new Component[] {
/* 16 */       (Component)Component.empty(), 
/* 17 */       (Component)Component.translatable("gui.waitingForResponse.button.inactive", new Object[] { 4
/* 18 */         }), (Component)Component.translatable("gui.waitingForResponse.button.inactive", new Object[] { 3
/* 19 */         }), (Component)Component.translatable("gui.waitingForResponse.button.inactive", new Object[] { 2
/* 20 */         }), (Component)Component.translatable("gui.waitingForResponse.button.inactive", new Object[] { 1 }), CommonComponents.GUI_BACK
/*    */     };
/*    */   
/*    */   private static final int BUTTON_VISIBLE_AFTER = 1;
/*    */   
/*    */   private static final int BUTTON_ACTIVE_AFTER = 5;
/*    */   
/*    */   private final Screen previousScreen;
/*    */   
/*    */   private final HeaderAndFooterLayout layout;
/*    */   
/*    */   private final Button closeButton;
/*    */   private int ticks;
/*    */   
/*    */   public WaitingForResponseScreen(Screen nextScreen) {
/* 35 */     super(TITLE);
/* 36 */     this.previousScreen = nextScreen;
/* 37 */     this.layout = new HeaderAndFooterLayout(this, 33, 0);
/* 38 */     this.closeButton = Button.builder(CommonComponents.GUI_BACK, button -> onClose()).width(200).build();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 43 */     super.init();
/*    */     
/* 45 */     this.layout.addTitleHeader(TITLE, this.font);
/*    */     
/* 47 */     this.layout.addToContents((LayoutElement)this.closeButton);
/* 48 */     this.closeButton.visible = false;
/* 49 */     this.closeButton.active = false;
/*    */     
/* 51 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 52 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 57 */     this.layout.arrangeElements();
/* 58 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 63 */     super.tick();
/*    */     
/* 65 */     if (!this.closeButton.active) {
/* 66 */       int secondsVisible = this.ticks++ / 20;
/* 67 */       this.closeButton.visible = (secondsVisible >= 1);
/* 68 */       this.closeButton.setMessage(BUTTON_LABELS[secondsVisible]);
/* 69 */       if (secondsVisible == 5) {
/* 70 */         this.closeButton.active = true;
/* 71 */         triggerImmediateNarration(true);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPauseScreen() {
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 84 */     return this.closeButton.active;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 89 */     this.minecraft.setScreen(this.previousScreen);
/*    */   }
/*    */   
/*    */   public Screen previousScreen() {
/* 93 */     return this.previousScreen;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/WaitingForResponseScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */