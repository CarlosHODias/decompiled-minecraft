/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.server.dialog.ActionButton;
/*    */ import net.minecraft.server.dialog.ButtonListDialog;
/*    */ import net.minecraft.server.dialog.MultiActionDialog;
/*    */ 
/*    */ public class MultiButtonDialogScreen
/*    */   extends ButtonListDialogScreen<MultiActionDialog> {
/*    */   public MultiButtonDialogScreen(Screen previousScreen, MultiActionDialog dialog, DialogConnectionAccess connectionAccess) {
/* 12 */     super(previousScreen, dialog, connectionAccess);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stream<ActionButton> createListActions(MultiActionDialog dialog, DialogConnectionAccess connectionAccess) {
/* 17 */     return dialog.actions().stream();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/MultiButtonDialogScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */