/*    */ package net.minecraft.client.gui.screens.multiplayer;
/*    */ 
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Checkbox;
/*    */ import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class WarningScreen
/*    */   extends Screen
/*    */ {
/*    */   private static final int MESSAGE_PADDING = 100;
/*    */   private final Component message;
/*    */   private final Component check;
/*    */   private final Component narration;
/*    */   protected Checkbox stopShowing;
/*    */   private FittingMultiLineTextWidget messageWidget;
/*    */   private final FrameLayout layout;
/*    */   
/*    */   protected WarningScreen(Component title, Component message, Component narration) {
/* 28 */     this(title, message, null, narration);
/*    */   }
/*    */   
/*    */   protected WarningScreen(Component title, Component message, Component check, Component narration) {
/* 32 */     super(title);
/* 33 */     this.message = message;
/* 34 */     this.check = check;
/* 35 */     this.narration = narration;
/* 36 */     this.layout = new FrameLayout(0, 0, this.width, this.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void init() {
/* 43 */     LinearLayout content = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.vertical().spacing(8));
/* 44 */     content.defaultCellSetting().alignHorizontallyCenter();
/* 45 */     content.addChild((LayoutElement)new StringWidget(getTitle(), this.font));
/* 46 */     this.messageWidget = (FittingMultiLineTextWidget)content.addChild((LayoutElement)new FittingMultiLineTextWidget(0, 0, this.width - 100, this.height - 100, this.message, this.font), s -> s.padding(12));
/*    */     
/* 48 */     LinearLayout footer = (LinearLayout)content.addChild((LayoutElement)LinearLayout.vertical().spacing(8));
/* 49 */     footer.defaultCellSetting().alignHorizontallyCenter();
/* 50 */     if (this.check != null) {
/* 51 */       this.stopShowing = (Checkbox)footer.addChild((LayoutElement)Checkbox.builder(this.check, this.font).build());
/*    */     }
/* 53 */     footer.addChild((LayoutElement)addFooterButtons());
/*    */     
/* 55 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 56 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 61 */     if (this.messageWidget != null) {
/* 62 */       this.messageWidget.setWidth(this.width - 100);
/* 63 */       this.messageWidget.setHeight(this.height - 100);
/* 64 */       this.messageWidget.minimizeHeight();
/*    */     } 
/* 66 */     this.layout.arrangeElements();
/* 67 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getNarrationMessage() {
/* 72 */     return this.narration;
/*    */   }
/*    */   
/*    */   protected abstract Layout addFooterButtons();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/multiplayer/WarningScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */