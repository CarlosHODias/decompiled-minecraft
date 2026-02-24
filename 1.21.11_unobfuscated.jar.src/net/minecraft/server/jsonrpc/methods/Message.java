/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ 
/*    */ public final class Message extends Record {
/*    */   private final java.util.Optional<String> literal;
/*    */   private final java.util.Optional<String> translatable;
/*    */   private final java.util.Optional<java.util.List<String>> translatableParams;
/*    */   public static final com.mojang.serialization.Codec<Message> CODEC;
/*    */   
/* 10 */   public Message(java.util.Optional<String> literal, java.util.Optional<String> translatable, java.util.Optional<java.util.List<String>> translatableParams) { this.literal = literal; this.translatable = translatable; this.translatableParams = translatableParams; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/Message;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/Message; } public java.util.Optional<String> literal() { return this.literal; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/Message;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/Message; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/Message;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/Message;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<String> translatable() { return this.translatable; } public java.util.Optional<java.util.List<String>> translatableParams() { return this.translatableParams; } static {
/* 11 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.optionalFieldOf("literal").forGetter(Message::literal), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.optionalFieldOf("translatable").forGetter(Message::translatable), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.listOf().lenientOptionalFieldOf("translatableParams").forGetter(Message::translatableParams)).apply((com.mojang.datafixers.kinds.Applicative)i, Message::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public java.util.Optional<net.minecraft.network.chat.Component> asComponent() {
/* 18 */     if (this.translatable.isPresent()) {
/* 19 */       String translationKey = this.translatable.get();
/* 20 */       if (this.translatableParams.isPresent()) {
/* 21 */         java.util.List<String> translationArgs = this.translatableParams.get();
/*    */         
/* 23 */         return (java.util.Optional)java.util.Optional.of(net.minecraft.network.chat.Component.translatable(translationKey, translationArgs.toArray()));
/*    */       } 
/* 25 */       return (java.util.Optional)java.util.Optional.of(net.minecraft.network.chat.Component.translatable(translationKey));
/*    */     } 
/*    */     
/* 28 */     return this.literal.map(net.minecraft.network.chat.Component::literal);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/methods/Message.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */