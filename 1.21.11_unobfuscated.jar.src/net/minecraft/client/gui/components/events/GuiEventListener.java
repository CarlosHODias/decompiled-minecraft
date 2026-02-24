/*    */ package net.minecraft.client.gui.components.events;
/*    */ 
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.components.TabOrderedElement;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.input.CharacterEvent;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GuiEventListener
/*    */   extends TabOrderedElement
/*    */ {
/*    */   default void mouseMoved(double x, double y) {}
/*    */   
/*    */   default boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 23 */     return false;
/*    */   }
/*    */   
/*    */   default boolean mouseReleased(MouseButtonEvent event) {
/* 27 */     return false;
/*    */   }
/*    */   
/*    */   default boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   default boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   default boolean keyPressed(KeyEvent event) {
/* 39 */     return false;
/*    */   }
/*    */   
/*    */   default boolean keyReleased(KeyEvent event) {
/* 43 */     return false;
/*    */   }
/*    */   
/*    */   default boolean charTyped(CharacterEvent event) {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 57 */     return null;
/*    */   }
/*    */   
/*    */   default boolean isMouseOver(double mouseX, double mouseY) {
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   void setFocused(boolean paramBoolean);
/*    */   
/*    */   boolean isFocused();
/*    */   
/*    */   default boolean shouldTakeFocusAfterInteraction() {
/* 69 */     return true;
/*    */   }
/*    */   
/*    */   default ComponentPath getCurrentFocusPath() {
/* 73 */     if (isFocused()) {
/* 74 */       return ComponentPath.leaf(this);
/*    */     }
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   default ScreenRectangle getRectangle() {
/* 80 */     return ScreenRectangle.empty();
/*    */   }
/*    */   
/*    */   default ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
/* 84 */     return getRectangle().getBorder(opposite);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/events/GuiEventListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */