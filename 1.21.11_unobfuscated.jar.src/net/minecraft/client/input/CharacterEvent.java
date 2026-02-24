/*    */ package net.minecraft.client.input;
/*    */ public final class CharacterEvent extends Record { private final int codepoint; @InputWithModifiers.Modifiers
/*    */   private final int modifiers; public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/CharacterEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/CharacterEvent;
/*    */   } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/CharacterEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/CharacterEvent;
/*  7 */   } public CharacterEvent(int codepoint, @InputWithModifiers.Modifiers int modifiers) { this.codepoint = codepoint; this.modifiers = modifiers; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/CharacterEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/CharacterEvent;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public int codepoint() { return this.codepoint; } @InputWithModifiers.Modifiers public int modifiers() { return this.modifiers; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String codepointAsString() {
/* 12 */     return Character.toString(this.codepoint);
/*    */   }
/*    */   
/*    */   public boolean isAllowedChatCharacter() {
/* 16 */     return net.minecraft.util.StringUtil.isAllowedChatCharacter(this.codepoint);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/input/CharacterEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */