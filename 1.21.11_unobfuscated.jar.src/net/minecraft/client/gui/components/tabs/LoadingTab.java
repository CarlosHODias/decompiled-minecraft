/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class LoadingTab implements Tab {
/*    */   private final Component title;
/* 16 */   protected final LinearLayout layout = LinearLayout.vertical(); private final Component loadingTitle;
/*    */   
/*    */   public LoadingTab(Font font, Component title, Component loadingTitle) {
/* 19 */     this.title = title;
/* 20 */     this.loadingTitle = loadingTitle;
/*    */     
/* 22 */     LoadingDotsWidget loadingDotsWidget = new LoadingDotsWidget(font, loadingTitle);
/* 23 */     this.layout.defaultCellSetting()
/* 24 */       .alignVerticallyMiddle()
/* 25 */       .alignHorizontallyCenter();
/* 26 */     this.layout.addChild((LayoutElement)loadingDotsWidget, layoutSettings -> layoutSettings.paddingBottom(30));
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTabTitle() {
/* 31 */     return this.title;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTabExtraNarration() {
/* 36 */     return this.loadingTitle;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
/* 41 */     this.layout.visitWidgets(childrenConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doLayout(ScreenRectangle screenRectangle) {
/* 46 */     this.layout.arrangeElements();
/* 47 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, screenRectangle, 0.5F, 0.5F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/tabs/LoadingTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */