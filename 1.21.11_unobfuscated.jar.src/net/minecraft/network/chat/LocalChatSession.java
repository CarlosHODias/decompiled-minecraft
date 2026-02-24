/*    */ package net.minecraft.network.chat;
/*    */ public final class LocalChatSession extends Record { private final java.util.UUID sessionId; private final net.minecraft.world.entity.player.ProfileKeyPair keyPair;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LocalChatSession;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*    */   }
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LocalChatSession;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*    */   }
/*  9 */   public LocalChatSession(java.util.UUID sessionId, net.minecraft.world.entity.player.ProfileKeyPair keyPair) { this.sessionId = sessionId; this.keyPair = keyPair; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LocalChatSession;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.UUID sessionId() { return this.sessionId; } public net.minecraft.world.entity.player.ProfileKeyPair keyPair() { return this.keyPair; }
/*    */    public static LocalChatSession create(net.minecraft.world.entity.player.ProfileKeyPair keyPair) {
/* 11 */     return new LocalChatSession(java.util.UUID.randomUUID(), keyPair);
/*    */   }
/*    */   
/*    */   public SignedMessageChain.Encoder createMessageEncoder(java.util.UUID profileId) {
/* 15 */     return new SignedMessageChain(profileId, this.sessionId).encoder(net.minecraft.util.Signer.from(this.keyPair.privateKey(), "SHA256withRSA"));
/*    */   }
/*    */   
/*    */   public RemoteChatSession asRemote() {
/* 19 */     return new RemoteChatSession(this.sessionId, this.keyPair.publicKey());
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/LocalChatSession.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */