/*   */ package net.minecraft.client.multiplayer;
/*   */ 
/*   */ public final class TransferState extends Record {
/*   */   private final java.util.Map<net.minecraft.resources.Identifier, byte[]> cookies;
/*   */   private final java.util.Map<java.util.UUID, PlayerInfo> seenPlayers;
/*   */   private final boolean seenInsecureChatWarning;
/*   */   
/* 8 */   public TransferState(java.util.Map<net.minecraft.resources.Identifier, byte[]> cookies, java.util.Map<java.util.UUID, PlayerInfo> seenPlayers, boolean seenInsecureChatWarning) { this.cookies = cookies; this.seenPlayers = seenPlayers; this.seenInsecureChatWarning = seenInsecureChatWarning; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/TransferState;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 8 */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/TransferState; } public java.util.Map<net.minecraft.resources.Identifier, byte[]> cookies() { return this.cookies; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/TransferState;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/TransferState; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/TransferState;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/TransferState;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Map<java.util.UUID, PlayerInfo> seenPlayers() { return this.seenPlayers; } public boolean seenInsecureChatWarning() { return this.seenInsecureChatWarning; }
/*   */ 
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/TransferState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */