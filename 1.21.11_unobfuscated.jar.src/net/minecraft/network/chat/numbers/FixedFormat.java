/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ public final class FixedFormat extends Record implements NumberFormat {
/*    */   private final net.minecraft.network.chat.Component value;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/numbers/FixedFormat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat;
/*    */   }
/*    */   
/* 10 */   public FixedFormat(net.minecraft.network.chat.Component value) { this.value = value; } public net.minecraft.network.chat.Component value() { return this.value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/numbers/FixedFormat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat; }
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/numbers/FixedFormat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 13 */   } public static final NumberFormatType<FixedFormat> TYPE = new NumberFormatType<FixedFormat>() {
/* 14 */       private static final com.mojang.serialization.MapCodec<FixedFormat> CODEC = net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 19 */       private static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.chat.ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       public com.mojang.serialization.MapCodec<FixedFormat> mapCodec() {
/* 26 */         return CODEC;
/*    */       }
/*    */ 
/*    */       
/*    */       public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FixedFormat> streamCodec() {
/* 31 */         return STREAM_CODEC;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public net.minecraft.network.chat.MutableComponent format(int value) {
/* 37 */     return this.value.copy();
/*    */   }
/*    */ 
/*    */   
/*    */   public NumberFormatType<FixedFormat> type() {
/* 42 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/numbers/FixedFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */