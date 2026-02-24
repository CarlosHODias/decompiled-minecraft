/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ 
/*    */ 
/*    */ public interface ButtonListDialog
/*    */   extends Dialog
/*    */ {
/*    */   MapCodec<? extends ButtonListDialog> codec();
/*    */   
/*    */   int columns();
/*    */   
/*    */   Optional<ActionButton> exitAction();
/*    */   
/*    */   default Optional<Action> onCancel() {
/* 18 */     return exitAction().flatMap(ActionButton::action);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/ButtonListDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */