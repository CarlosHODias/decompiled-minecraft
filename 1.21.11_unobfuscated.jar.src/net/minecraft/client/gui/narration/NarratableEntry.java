/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.components.TabOrderedElement;
/*    */ 
/*    */ public interface NarratableEntry
/*    */   extends NarrationSupplier, TabOrderedElement {
/*    */   NarrationPriority narrationPriority();
/*    */   
/*    */   default boolean isActive() {
/* 12 */     return true;
/*    */   }
/*    */   
/*    */   default Collection<? extends NarratableEntry> getNarratables() {
/* 16 */     return List.of(this);
/*    */   }
/*    */   
/*    */   public enum NarrationPriority
/*    */   {
/* 21 */     NONE,
/* 22 */     HOVERED,
/* 23 */     FOCUSED;
/*    */ 
/*    */     
/*    */     public boolean isTerminal() {
/* 27 */       return (this == FOCUSED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/narration/NarratableEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */