/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ public final class MultiActionDialog extends Record implements ButtonListDialog {
/*    */   private final CommonDialogData common;
/*    */   private final java.util.List<ActionButton> actions;
/*    */   private final java.util.Optional<ActionButton> exitAction;
/*    */   private final int columns;
/*    */   public static final com.mojang.serialization.MapCodec<MultiActionDialog> MAP_CODEC;
/*    */   
/* 10 */   public MultiActionDialog(CommonDialogData common, java.util.List<ActionButton> actions, java.util.Optional<ActionButton> exitAction, int columns) { this.common = common; this.actions = actions; this.exitAction = exitAction; this.columns = columns; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/MultiActionDialog;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/MultiActionDialog; } public CommonDialogData common() { return this.common; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/MultiActionDialog;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/MultiActionDialog; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/MultiActionDialog;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/MultiActionDialog;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.List<ActionButton> actions() { return this.actions; } public java.util.Optional<ActionButton> exitAction() { return this.exitAction; } public int columns() { return this.columns; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)CommonDialogData.MAP_CODEC.forGetter(MultiActionDialog::common), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.nonEmptyList(ActionButton.CODEC.listOf()).fieldOf("actions").forGetter(MultiActionDialog::actions), (com.mojang.datafixers.kinds.App)ActionButton.CODEC.optionalFieldOf("exit_action").forGetter(MultiActionDialog::exitAction), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.optionalFieldOf("columns", 2).forGetter(MultiActionDialog::columns)).apply((com.mojang.datafixers.kinds.Applicative)i, MultiActionDialog::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<MultiActionDialog> codec() {
/* 26 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/MultiActionDialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */