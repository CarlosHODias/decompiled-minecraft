/*    */ package net.minecraft.server.dialog.body;
/*    */ public final class ItemBody extends Record implements DialogBody {
/*    */   private final net.minecraft.world.item.ItemStack item;
/*    */   private final java.util.Optional<PlainMessage> description;
/*    */   private final boolean showDecorations;
/*    */   private final boolean showTooltip;
/*    */   private final int width;
/*    */   private final int height;
/*    */   public static final com.mojang.serialization.MapCodec<ItemBody> MAP_CODEC;
/*    */   
/* 11 */   public ItemBody(net.minecraft.world.item.ItemStack item, java.util.Optional<PlainMessage> description, boolean showDecorations, boolean showTooltip, int width, int height) { this.item = item; this.description = description; this.showDecorations = showDecorations; this.showTooltip = showTooltip; this.width = width; this.height = height; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/body/ItemBody;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/ItemBody; } public net.minecraft.world.item.ItemStack item() { return this.item; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/body/ItemBody;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/ItemBody; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/body/ItemBody;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/body/ItemBody;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<PlainMessage> description() { return this.description; } public boolean showDecorations() { return this.showDecorations; } public boolean showTooltip() { return this.showTooltip; } public int width() { return this.width; } public int height() { return this.height; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.item.ItemStack.STRICT_CODEC.fieldOf("item").forGetter(ItemBody::item), (com.mojang.datafixers.kinds.App)PlainMessage.CODEC.optionalFieldOf("description").forGetter(ItemBody::description), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("show_decorations", true).forGetter(ItemBody::showDecorations), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("show_tooltip", true).forGetter(ItemBody::showTooltip), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.intRange(1, 256).optionalFieldOf("width", 16).forGetter(ItemBody::width), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.intRange(1, 256).optionalFieldOf("height", 16).forGetter(ItemBody::height)).apply((com.mojang.datafixers.kinds.Applicative)i, ItemBody::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ItemBody> mapCodec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/body/ItemBody.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */