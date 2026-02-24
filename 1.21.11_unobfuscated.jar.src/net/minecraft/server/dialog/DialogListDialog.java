/*    */ package net.minecraft.server.dialog;
/*    */ public final class DialogListDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   private final net.minecraft.core.HolderSet<Dialog> dialogs;
/*    */   private final java.util.Optional<ActionButton> exitAction;
/*    */   private final int columns;
/*    */   private final int buttonWidth;
/*    */   public static final com.mojang.serialization.MapCodec<DialogListDialog> MAP_CODEC;
/*    */   
/* 10 */   public DialogListDialog(CommonDialogData common, net.minecraft.core.HolderSet<Dialog> dialogs, java.util.Optional<ActionButton> exitAction, int columns, int buttonWidth) { this.common = common; this.dialogs = dialogs; this.exitAction = exitAction; this.columns = columns; this.buttonWidth = buttonWidth; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/DialogListDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/DialogListDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/DialogListDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/DialogListDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/DialogListDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/DialogListDialog;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.HolderSet<Dialog> dialogs() { return this.dialogs; } public java.util.Optional<ActionButton> exitAction() { return this.exitAction; } public int columns() { return this.columns; } public int buttonWidth() { return this.buttonWidth; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)CommonDialogData.MAP_CODEC.forGetter(DialogListDialog::common), (com.mojang.datafixers.kinds.App)Dialog.LIST_CODEC.fieldOf("dialogs").forGetter(DialogListDialog::dialogs), (com.mojang.datafixers.kinds.App)ActionButton.CODEC.optionalFieldOf("exit_action").forGetter(DialogListDialog::exitAction), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("columns", 2).forGetter(DialogListDialog::columns), (com.mojang.datafixers.kinds.App)WIDTH_CODEC.optionalFieldOf("button_width", 150).forGetter(DialogListDialog::buttonWidth)).apply((com.mojang.datafixers.kinds.Applicative)i, DialogListDialog::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<DialogListDialog> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/DialogListDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */