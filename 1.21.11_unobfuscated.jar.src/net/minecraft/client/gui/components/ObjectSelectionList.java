/*    */ package net.minecraft.client.gui.components;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.narration.NarrationSupplier;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class ObjectSelectionList<E extends ObjectSelectionList.Entry<E>> extends AbstractSelectionList<E> {
/* 14 */   private static final Component USAGE_NARRATION = (Component)Component.translatable("narration.selection.usage");
/*    */   
/*    */   public ObjectSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
/* 17 */     super(minecraft, width, height, y, itemHeight);
/*    */   }
/*    */ 
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 22 */     if (getItemCount() == 0)
/*    */     {
/* 24 */       return null;
/*    */     }
/*    */     
/* 27 */     if (isFocused() && navigationEvent instanceof FocusNavigationEvent.ArrowNavigation) { FocusNavigationEvent.ArrowNavigation arrowNavigation = (FocusNavigationEvent.ArrowNavigation)navigationEvent;
/* 28 */       Entry entry = (Entry)nextEntry(arrowNavigation.direction());
/* 29 */       if (entry != null) {
/* 30 */         return ComponentPath.path(this, ComponentPath.leaf(entry));
/*    */       }
/* 32 */       setFocused(null);
/* 33 */       setSelected(null);
/* 34 */       return null; }
/*    */ 
/*    */ 
/*    */     
/* 38 */     if (!isFocused()) {
/* 39 */       Entry entry = (Entry)getSelected();
/* 40 */       if (entry == null) {
/* 41 */         entry = (Entry)nextEntry(navigationEvent.getVerticalDirectionForInitialFocus());
/*    */       }
/* 43 */       if (entry == null) {
/* 44 */         return null;
/*    */       }
/* 46 */       return ComponentPath.path(this, ComponentPath.leaf(entry));
/*    */     } 
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 53 */     Entry entry = (Entry)getHovered();
/* 54 */     if (entry != null) {
/* 55 */       narrateListElementPosition(output.nest(), (E)entry);
/* 56 */       entry.updateNarration(output);
/*    */     } else {
/* 58 */       Entry entry1 = (Entry)getSelected();
/* 59 */       if (entry1 != null) {
/* 60 */         narrateListElementPosition(output.nest(), (E)entry1);
/* 61 */         entry1.updateNarration(output);
/*    */       } 
/*    */     } 
/* 64 */     if (isFocused()) {
/* 65 */       output.add(NarratedElementType.USAGE, USAGE_NARRATION);
/*    */     }
/*    */   }
/*    */   
/*    */   public static abstract class Entry<E extends Entry<E>>
/*    */     extends AbstractSelectionList.Entry<E>
/*    */     implements NarrationSupplier
/*    */   {
/*    */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 74 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public void updateNarration(NarrationElementOutput output) {
/* 79 */       output.add(NarratedElementType.TITLE, getNarration());
/*    */     }
/*    */     
/*    */     public abstract Component getNarration();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/ObjectSelectionList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */