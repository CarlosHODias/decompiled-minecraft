/*    */ package net.minecraft.client.gui.components.events;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractContainerEventHandler
/*    */   implements ContainerEventHandler
/*    */ {
/*    */   private GuiEventListener focused;
/*    */   private boolean isDragging;
/*    */   
/*    */   public final boolean isDragging() {
/* 18 */     return this.isDragging;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void setDragging(boolean dragging) {
/* 23 */     this.isDragging = dragging;
/*    */   }
/*    */ 
/*    */   
/*    */   public GuiEventListener getFocused() {
/* 28 */     return this.focused;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFocused(GuiEventListener focused) {
/* 33 */     if (this.focused == focused) {
/*    */       return;
/*    */     }
/* 36 */     if (this.focused != null) {
/* 37 */       this.focused.setFocused(false);
/*    */     }
/* 39 */     if (focused != null) {
/* 40 */       focused.setFocused(true);
/*    */     }
/* 42 */     this.focused = focused;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/events/AbstractContainerEventHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */