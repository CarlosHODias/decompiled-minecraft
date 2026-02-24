/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.server.dialog.ActionButton;
/*    */ import net.minecraft.server.dialog.ButtonListDialog;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ public abstract class ButtonListDialogScreen<T extends ButtonListDialog> extends DialogScreen<T> {
/*    */   public static final int FOOTER_MARGIN = 5;
/*    */   
/*    */   public ButtonListDialogScreen(Screen previousScreen, T dialog, DialogConnectionAccess connectionAccess) {
/* 18 */     super(previousScreen, dialog, connectionAccess);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void populateBodyElements(LinearLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {
/* 23 */     super.populateBodyElements(layout, controlSet, dialog, connectionAccess);
/*    */     
/* 25 */     List<Button> buttons = createListActions(dialog, connectionAccess).<Button>map(d -> controlSet.createActionButton(d).build()).toList();
/* 26 */     layout.addChild(packControlsIntoColumns((List)buttons, dialog.columns()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updateHeaderAndFooter(HeaderAndFooterLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {
/* 33 */     super.updateHeaderAndFooter(layout, controlSet, dialog, connectionAccess);
/*    */     
/* 35 */     dialog.exitAction().ifPresentOrElse(exitButton -> layout.addToFooter((LayoutElement)controlSet.createActionButton(exitButton).build()), () -> layout.setFooterHeight(5));
/*    */   }
/*    */   
/*    */   protected abstract Stream<ActionButton> createListActions(T paramT, DialogConnectionAccess paramDialogConnectionAccess);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/ButtonListDialogScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */