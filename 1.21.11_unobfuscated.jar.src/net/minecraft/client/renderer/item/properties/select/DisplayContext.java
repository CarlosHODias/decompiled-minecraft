/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class DisplayContext extends Record implements SelectItemModelProperty<ItemDisplayContext> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/DisplayContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/DisplayContext;
/*    */   }
/*    */   
/* 12 */   public static final com.mojang.serialization.Codec<ItemDisplayContext> VALUE_CODEC = ItemDisplayContext.CODEC;
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/DisplayContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/DisplayContext; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/DisplayContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/DisplayContext;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public static final SelectItemModelProperty.Type<DisplayContext, ItemDisplayContext> TYPE = SelectItemModelProperty.Type.create(com.mojang.serialization.MapCodec.unit(new DisplayContext()), VALUE_CODEC);
/*    */ 
/*    */   
/*    */   public ItemDisplayContext get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 18 */     return displayContext;
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectItemModelProperty.Type<DisplayContext, ItemDisplayContext> type() {
/* 23 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.Codec<ItemDisplayContext> valueCodec() {
/* 28 */     return VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/DisplayContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */