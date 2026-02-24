/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ 
/*    */ public final class DiscardedPayload extends Record implements CustomPacketPayload {
/*    */   private final net.minecraft.resources.Identifier id;
/*    */   
/*  7 */   public DiscardedPayload(net.minecraft.resources.Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload; } public net.minecraft.resources.Identifier id() { return this.id; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public static <T extends net.minecraft.network.FriendlyByteBuf> net.minecraft.network.codec.StreamCodec<T, DiscardedPayload> codec(net.minecraft.resources.Identifier id, int maxPayloadSize) { return (net.minecraft.network.codec.StreamCodec)CustomPacketPayload.codec((payload, buf) -> {
/*    */         
/*    */         }, buf -> {
/*    */           int length = buf.readableBytes();
/*    */           if (length < 0 || length > maxPayloadSize) {
/*    */             throw new IllegalArgumentException("Payload may not be larger than " + maxPayloadSize + " bytes");
/*    */           }
/*    */           buf.skipBytes(length);
/*    */           return new DiscardedPayload(id);
/*    */         }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomPacketPayload.Type<DiscardedPayload> type() {
/* 24 */     return new CustomPacketPayload.Type<>(this.id);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/custom/DiscardedPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */