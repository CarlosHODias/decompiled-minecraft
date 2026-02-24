/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*    */ 
/*    */ public final class RemoteChatSession extends Record {
/*    */   private final UUID sessionId;
/*    */   private final ProfilePublicKey profilePublicKey;
/*    */   
/* 11 */   public RemoteChatSession(UUID sessionId, ProfilePublicKey profilePublicKey) { this.sessionId = sessionId; this.profilePublicKey = profilePublicKey; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/RemoteChatSession;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession; } public UUID sessionId() { return this.sessionId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/RemoteChatSession;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/RemoteChatSession;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/RemoteChatSession;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ProfilePublicKey profilePublicKey() { return this.profilePublicKey; }
/*    */    public SignedMessageValidator createMessageValidator(java.time.Duration gracePeriod) {
/* 13 */     return new SignedMessageValidator.KeyBased(
/* 14 */         this.profilePublicKey.createSignatureValidator(), () -> this.profilePublicKey.data().hasExpired(gracePeriod));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SignedMessageChain.Decoder createMessageDecoder(UUID profileId) {
/* 20 */     return new SignedMessageChain(profileId, this.sessionId).decoder(this.profilePublicKey);
/*    */   }
/*    */   
/*    */   public Data asData() {
/* 24 */     return new Data(this.sessionId, this.profilePublicKey.data());
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 28 */     return this.profilePublicKey.data().hasExpired();
/*    */   }
/*    */   public static final class Data extends Record { private final UUID sessionId; private final ProfilePublicKey.Data profilePublicKey;
/* 31 */     public Data(UUID sessionId, ProfilePublicKey.Data profilePublicKey) { this.sessionId = sessionId; this.profilePublicKey = profilePublicKey; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/RemoteChatSession$Data;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/RemoteChatSession$Data;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/RemoteChatSession$Data;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data;
/* 31 */       //   0	8	1	o	Ljava/lang/Object; } public UUID sessionId() { return this.sessionId; } public ProfilePublicKey.Data profilePublicKey() { return this.profilePublicKey; }
/*    */      public static Data read(FriendlyByteBuf input) {
/* 33 */       return new Data(input.readUUID(), new ProfilePublicKey.Data(input));
/*    */     }
/*    */     
/*    */     public static void write(FriendlyByteBuf output, Data data) {
/* 37 */       output.writeUUID(data.sessionId);
/* 38 */       data.profilePublicKey.write(output);
/*    */     }
/*    */     
/*    */     public RemoteChatSession validate(com.mojang.authlib.GameProfile profile, net.minecraft.util.SignatureValidator serviceSignatureValidator) throws ProfilePublicKey.ValidationException {
/* 42 */       return new RemoteChatSession(this.sessionId, ProfilePublicKey.createValidated(serviceSignatureValidator, profile.id(), this.profilePublicKey));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/RemoteChatSession.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */