/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.GridLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GridLayoutTab implements Tab {
/*    */   private final Component title;
/* 13 */   protected final GridLayout layout = new GridLayout();
/*    */   
/*    */   public GridLayoutTab(Component title) {
/* 16 */     this.title = title;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTabTitle() {
/* 21 */     return this.title;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTabExtraNarration() {
/* 26 */     return (Component)Component.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public void visitChildren(Consumer<AbstractWidget> childrenConsumer) {
/* 31 */     this.layout.visitWidgets(childrenConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doLayout(ScreenRectangle screenRectangle) {
/* 36 */     this.layout.arrangeElements();
/* 37 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, screenRectangle, 0.5F, 0.16666667F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/tabs/GridLayoutTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */