/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class AbstractContainerWidget
/*    */   extends AbstractScrollArea
/*    */   implements ContainerEventHandler {
/*    */   private GuiEventListener focused;
/*    */   private boolean isDragging;
/*    */   
/*    */   public AbstractContainerWidget(int x, int y, int width, int height, Component message) {
/* 17 */     super(x, y, width, height, message);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isDragging() {
/* 22 */     return this.isDragging;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void setDragging(boolean dragging) {
/* 27 */     this.isDragging = dragging;
/*    */   }
/*    */ 
/*    */   
/*    */   public GuiEventListener getFocused() {
/* 32 */     return this.focused;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFocused(GuiEventListener focused) {
/* 37 */     if (this.focused != null) {
/* 38 */       this.focused.setFocused(false);
/*    */     }
/* 40 */     if (focused != null) {
/* 41 */       focused.setFocused(true);
/*    */     }
/* 43 */     this.focused = focused;
/*    */   }
/*    */ 
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 48 */     return super.nextFocusPath(navigationEvent);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 53 */     boolean scrolling = updateScrolling(event);
/* 54 */     return (super.mouseClicked(event, doubleClick) || scrolling);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mouseReleased(MouseButtonEvent event) {
/* 59 */     super.mouseReleased(event);
/* 60 */     return super.mouseReleased(event);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 65 */     super.mouseDragged(event, dx, dy);
/* 66 */     return super.mouseDragged(event, dx, dy);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFocused() {
/* 71 */     return super.isFocused();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFocused(boolean focused) {
/* 76 */     super.setFocused(focused);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/AbstractContainerWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */