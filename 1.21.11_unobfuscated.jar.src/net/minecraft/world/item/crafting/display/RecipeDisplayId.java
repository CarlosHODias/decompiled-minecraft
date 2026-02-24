/*   */ package net.minecraft.world.item.crafting.display;
/*   */ 
/*   */ 
/*   */ public final class RecipeDisplayId extends Record {
/*   */   private final int index;
/*   */   
/* 7 */   public RecipeDisplayId(int index) { this.index = index; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 7 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayId; } public int index() { return this.index; }
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayId; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/RecipeDisplayId;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, RecipeDisplayId> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, RecipeDisplayId::index, RecipeDisplayId::new);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/RecipeDisplayId.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */