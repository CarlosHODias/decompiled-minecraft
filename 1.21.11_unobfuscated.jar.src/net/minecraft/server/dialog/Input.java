/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ public final class Input extends Record {
/*    */   private final String key;
/*    */   private final net.minecraft.server.dialog.input.InputControl control;
/*    */   public static final com.mojang.serialization.Codec<Input> CODEC;
/*    */   
/*  8 */   public Input(String key, net.minecraft.server.dialog.input.InputControl control) { this.key = key; this.control = control; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/Input;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/server/dialog/Input; } public String key() { return this.key; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/Input;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/Input; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/Input;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/Input;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.server.dialog.input.InputControl control() { return this.control; }
/*    */ 
/*    */   
/*    */   static {
/* 12 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.server.dialog.action.ParsedTemplate.VARIABLE_CODEC.fieldOf("key").forGetter(Input::key), (com.mojang.datafixers.kinds.App)net.minecraft.server.dialog.input.InputControl.MAP_CODEC.forGetter(Input::control)).apply((com.mojang.datafixers.kinds.Applicative)i, Input::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/Input.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */