/*    */ package net.minecraft.server.dialog.input;
/*    */ public final class BooleanInput extends Record implements InputControl {
/*    */   private final net.minecraft.network.chat.Component label;
/*    */   private final boolean initial;
/*    */   private final String onTrue;
/*    */   private final String onFalse;
/*    */   public static final com.mojang.serialization.MapCodec<BooleanInput> MAP_CODEC;
/*    */   
/*  9 */   public BooleanInput(net.minecraft.network.chat.Component label, boolean initial, String onTrue, String onFalse) { this.label = label; this.initial = initial; this.onTrue = onTrue; this.onFalse = onFalse; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/BooleanInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/BooleanInput; } public net.minecraft.network.chat.Component label() { return this.label; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/BooleanInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/BooleanInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/BooleanInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/BooleanInput;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean initial() { return this.initial; } public String onTrue() { return this.onTrue; } public String onFalse() { return this.onFalse; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("label").forGetter(BooleanInput::label), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("initial", false).forGetter(BooleanInput::initial), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.optionalFieldOf("on_true", "true").forGetter(BooleanInput::onTrue), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.optionalFieldOf("on_false", "false").forGetter(BooleanInput::onFalse)).apply((com.mojang.datafixers.kinds.Applicative)i, BooleanInput::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<BooleanInput> mapCodec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/input/BooleanInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */