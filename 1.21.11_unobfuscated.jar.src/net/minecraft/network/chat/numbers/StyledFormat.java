/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ 
/*    */ public final class StyledFormat extends Record implements NumberFormat {
/*    */   private final net.minecraft.network.chat.Style style;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/numbers/StyledFormat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat;
/*    */   }
/*    */   
/* 11 */   public StyledFormat(net.minecraft.network.chat.Style style) { this.style = style; } public net.minecraft.network.chat.Style style() { return this.style; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/numbers/StyledFormat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat; }
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/numbers/StyledFormat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 14 */   } public static final NumberFormatType<StyledFormat> TYPE = new NumberFormatType<StyledFormat>() {
/* 15 */       private static final com.mojang.serialization.MapCodec<StyledFormat> CODEC = net.minecraft.network.chat.Style.Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
/*    */       
/* 17 */       private static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.chat.Style.Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       public com.mojang.serialization.MapCodec<StyledFormat> mapCodec() {
/* 24 */         return CODEC;
/*    */       }
/*    */ 
/*    */       
/*    */       public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, StyledFormat> streamCodec() {
/* 29 */         return STREAM_CODEC;
/*    */       }
/*    */     };
/*    */   
/* 33 */   public static final StyledFormat NO_STYLE = new StyledFormat(net.minecraft.network.chat.Style.EMPTY);
/* 34 */   public static final StyledFormat SIDEBAR_DEFAULT = new StyledFormat(net.minecraft.network.chat.Style.EMPTY.withColor(net.minecraft.ChatFormatting.RED));
/* 35 */   public static final StyledFormat PLAYER_LIST_DEFAULT = new StyledFormat(net.minecraft.network.chat.Style.EMPTY.withColor(net.minecraft.ChatFormatting.YELLOW));
/*    */ 
/*    */   
/*    */   public net.minecraft.network.chat.MutableComponent format(int value) {
/* 39 */     return net.minecraft.network.chat.Component.literal(Integer.toString(value)).withStyle(this.style);
/*    */   }
/*    */ 
/*    */   
/*    */   public NumberFormatType<StyledFormat> type() {
/* 44 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/numbers/StyledFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */