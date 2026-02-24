/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ 
/*    */ public final class ConfirmationDialog extends Record implements SimpleDialog {
/*    */   private final CommonDialogData common;
/*    */   private final ActionButton yesButton;
/*    */   private final ActionButton noButton;
/*    */   public static final com.mojang.serialization.MapCodec<ConfirmationDialog> MAP_CODEC;
/*    */   
/* 10 */   public ConfirmationDialog(CommonDialogData common, ActionButton yesButton, ActionButton noButton) { this.common = common; this.yesButton = yesButton; this.noButton = noButton; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/ConfirmationDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/ConfirmationDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/ConfirmationDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/ConfirmationDialog;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ActionButton yesButton() { return this.yesButton; } public ActionButton noButton() { return this.noButton; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)CommonDialogData.MAP_CODEC.forGetter(ConfirmationDialog::common), (com.mojang.datafixers.kinds.App)ActionButton.CODEC.fieldOf("yes").forGetter(ConfirmationDialog::yesButton), (com.mojang.datafixers.kinds.App)ActionButton.CODEC.fieldOf("no").forGetter(ConfirmationDialog::noButton)).apply((com.mojang.datafixers.kinds.Applicative)i, ConfirmationDialog::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ConfirmationDialog> codec() {
/* 23 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Optional<net.minecraft.server.dialog.action.Action> onCancel() {
/* 28 */     return this.noButton.action();
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.List<ActionButton> mainActions() {
/* 33 */     return java.util.List.of(this.yesButton, this.noButton);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/ConfirmationDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */