/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Layout
/*    */   extends LayoutElement
/*    */ {
/*    */   default void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
/* 12 */     visitChildren(child -> child.visitWidgets(widgetVisitor));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default void arrangeElements() {
/* 22 */     visitChildren(child -> {
/*    */           if (child instanceof Layout) {
/*    */             Layout layout = (Layout)child;
/*    */             layout.arrangeElements();
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   void visitChildren(Consumer<LayoutElement> paramConsumer);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/layouts/Layout.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */