/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ 
/*    */ public final class CommonDialogData extends Record {
/*    */   private final net.minecraft.network.chat.Component title;
/*    */   private final java.util.Optional<net.minecraft.network.chat.Component> externalTitle;
/*    */   private final boolean canCloseWithEscape;
/*    */   private final boolean pause;
/*    */   private final DialogAction afterAction;
/*    */   private final java.util.List<net.minecraft.server.dialog.body.DialogBody> body;
/*    */   private final java.util.List<Input> inputs;
/*    */   public static final com.mojang.serialization.MapCodec<CommonDialogData> MAP_CODEC;
/*    */   
/* 14 */   public CommonDialogData(net.minecraft.network.chat.Component title, java.util.Optional<net.minecraft.network.chat.Component> externalTitle, boolean canCloseWithEscape, boolean pause, DialogAction afterAction, java.util.List<net.minecraft.server.dialog.body.DialogBody> body, java.util.List<Input> inputs) { this.title = title; this.externalTitle = externalTitle; this.canCloseWithEscape = canCloseWithEscape; this.pause = pause; this.afterAction = afterAction; this.body = body; this.inputs = inputs; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/CommonDialogData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonDialogData; } public net.minecraft.network.chat.Component title() { return this.title; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/CommonDialogData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonDialogData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/CommonDialogData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/CommonDialogData;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<net.minecraft.network.chat.Component> externalTitle() { return this.externalTitle; } public boolean canCloseWithEscape() { return this.canCloseWithEscape; } public boolean pause() { return this.pause; } public DialogAction afterAction() { return this.afterAction; } public java.util.List<net.minecraft.server.dialog.body.DialogBody> body() { return this.body; } public java.util.List<Input> inputs() { return this.inputs; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 31 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("title").forGetter(CommonDialogData::title), (com.mojang.datafixers.kinds.App)net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("external_title").forGetter(CommonDialogData::externalTitle), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("can_close_with_escape", true).forGetter(CommonDialogData::canCloseWithEscape), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("pause", true).forGetter(CommonDialogData::pause), (com.mojang.datafixers.kinds.App)DialogAction.CODEC.optionalFieldOf("after_action", DialogAction.CLOSE).forGetter(CommonDialogData::afterAction), (com.mojang.datafixers.kinds.App)net.minecraft.server.dialog.body.DialogBody.COMPACT_LIST_CODEC.optionalFieldOf("body", java.util.List.of()).forGetter(CommonDialogData::body), (com.mojang.datafixers.kinds.App)Input.CODEC.listOf().optionalFieldOf("inputs", java.util.List.of()).forGetter(CommonDialogData::inputs)).apply((com.mojang.datafixers.kinds.Applicative)i, CommonDialogData::new)).validate(data -> 
/* 32 */         (data.pause && !data.afterAction.willUnpause()) ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(data));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.chat.Component computeExternalTitle() {
/* 40 */     return this.externalTitle.orElse(this.title);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/CommonDialogData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */