/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ 
/*    */ public final class ItemBlockState extends Record implements SelectItemModelProperty<String> {
/*    */   private final String property;
/*    */   
/* 14 */   public ItemBlockState(String property) { this.property = property; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/ItemBlockState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ItemBlockState; } public String property() { return this.property; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/ItemBlockState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ItemBlockState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/ItemBlockState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/ItemBlockState;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.codecs.PrimitiveCodec<String> VALUE_CODEC = Codec.STRING; public static final SelectItemModelProperty.Type<ItemBlockState, String> TYPE;
/*    */   static {
/* 17 */     TYPE = SelectItemModelProperty.Type.create(
/* 18 */         RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("block_state_property").forGetter(ItemBlockState::property)).apply((com.mojang.datafixers.kinds.Applicative)i, ItemBlockState::new)), (Codec<String>)VALUE_CODEC);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 26 */     BlockItemStateProperties blockItemStateProperties = (BlockItemStateProperties)itemStack.get(net.minecraft.core.component.DataComponents.BLOCK_STATE);
/* 27 */     if (blockItemStateProperties == null) {
/* 28 */       return null;
/*    */     }
/* 30 */     return (String)blockItemStateProperties.properties().get(this.property);
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectItemModelProperty.Type<ItemBlockState, String> type() {
/* 35 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<String> valueCodec() {
/* 40 */     return (Codec<String>)VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/ItemBlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */