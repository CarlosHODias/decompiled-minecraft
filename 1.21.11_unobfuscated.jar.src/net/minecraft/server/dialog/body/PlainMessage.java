/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ public final class PlainMessage extends Record implements DialogBody {
/*    */   private final net.minecraft.network.chat.Component contents;
/*    */   private final int width;
/*    */   public static final int DEFAULT_WIDTH = 200;
/*    */   public static final com.mojang.serialization.MapCodec<PlainMessage> MAP_CODEC;
/*    */   public static final com.mojang.serialization.Codec<PlainMessage> CODEC;
/*    */   
/* 10 */   public PlainMessage(net.minecraft.network.chat.Component contents, int width) { this.contents = contents; this.width = width; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/body/PlainMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/PlainMessage; } public net.minecraft.network.chat.Component contents() { return this.contents; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/body/PlainMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/PlainMessage; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/body/PlainMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/body/PlainMessage;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("contents").forGetter(PlainMessage::contents), (com.mojang.datafixers.kinds.App)net.minecraft.server.dialog.Dialog.WIDTH_CODEC.optionalFieldOf("width", 200).forGetter(PlainMessage::width)).apply((com.mojang.datafixers.kinds.Applicative)i, PlainMessage::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     CODEC = com.mojang.serialization.Codec.withAlternative(
/* 22 */         MAP_CODEC.codec(), net.minecraft.network.chat.ComponentSerialization.CODEC, contents -> new PlainMessage(contents, 200));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<PlainMessage> mapCodec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/body/PlainMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */