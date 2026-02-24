/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.util.SignatureUpdater;
/*    */ 
/*    */ public final class SignedMessageLink extends Record {
/*    */   private final int index;
/*    */   private final UUID sender;
/*    */   private final UUID sessionId;
/*    */   public static final com.mojang.serialization.Codec<SignedMessageLink> CODEC;
/*    */   
/* 15 */   public SignedMessageLink(int index, UUID sender, UUID sessionId) { this.index = index; this.sender = sender; this.sessionId = sessionId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageLink;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageLink; } public int index() { return this.index; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageLink;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageLink; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageLink;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageLink;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public UUID sender() { return this.sender; } public UUID sessionId() { return this.sessionId; } static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(SignedMessageLink::index), (App)UUIDUtil.CODEC.fieldOf("sender").forGetter(SignedMessageLink::sender), (App)UUIDUtil.CODEC.fieldOf("session_id").forGetter(SignedMessageLink::sessionId)).apply((com.mojang.datafixers.kinds.Applicative)i, SignedMessageLink::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SignedMessageLink unsigned(UUID sender) {
/* 23 */     return root(sender, net.minecraft.util.Util.NIL_UUID);
/*    */   }
/*    */   
/*    */   public static SignedMessageLink root(UUID sender, UUID sessionId) {
/* 27 */     return new SignedMessageLink(0, sender, sessionId);
/*    */   }
/*    */   
/*    */   public void updateSignature(SignatureUpdater.Output output) throws java.security.SignatureException {
/* 31 */     output.update(UUIDUtil.uuidToByteArray(this.sender));
/* 32 */     output.update(UUIDUtil.uuidToByteArray(this.sessionId));
/* 33 */     output.update(com.google.common.primitives.Ints.toByteArray(this.index));
/*    */   }
/*    */   
/*    */   public boolean isDescendantOf(SignedMessageLink link) {
/* 37 */     return (this.index > link.index() && this.sender.equals(link.sender()) && this.sessionId.equals(link.sessionId()));
/*    */   }
/*    */   
/*    */   public SignedMessageLink advance() {
/* 41 */     if (this.index == Integer.MAX_VALUE) {
/* 42 */       return null;
/*    */     }
/* 44 */     return new SignedMessageLink(this.index + 1, this.sender, this.sessionId);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/SignedMessageLink.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */