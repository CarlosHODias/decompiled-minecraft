/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ public final class CommonButtonData extends Record {
/*    */   private final net.minecraft.network.chat.Component label;
/*    */   private final java.util.Optional<net.minecraft.network.chat.Component> tooltip;
/*    */   private final int width;
/*    */   public static final int DEFAULT_WIDTH = 150;
/*    */   public static final com.mojang.serialization.MapCodec<CommonButtonData> MAP_CODEC;
/*    */   
/* 10 */   public CommonButtonData(net.minecraft.network.chat.Component label, java.util.Optional<net.minecraft.network.chat.Component> tooltip, int width) { this.label = label; this.tooltip = tooltip; this.width = width; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/CommonButtonData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonButtonData; } public net.minecraft.network.chat.Component label() { return this.label; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/CommonButtonData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/CommonButtonData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/CommonButtonData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/CommonButtonData;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<net.minecraft.network.chat.Component> tooltip() { return this.tooltip; } public int width() { return this.width; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("label").forGetter(CommonButtonData::label), (com.mojang.datafixers.kinds.App)net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("tooltip").forGetter(CommonButtonData::tooltip), (com.mojang.datafixers.kinds.App)Dialog.WIDTH_CODEC.optionalFieldOf("width", 150).forGetter(CommonButtonData::width)).apply((com.mojang.datafixers.kinds.Applicative)i, CommonButtonData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CommonButtonData(net.minecraft.network.chat.Component label, int width) {
/* 25 */     this(label, java.util.Optional.empty(), width);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/CommonButtonData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */