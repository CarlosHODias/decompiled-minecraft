/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataProperty extends Record implements SelectItemModelProperty<String> {
/*    */   private final int index;
/*    */   
/* 15 */   public CustomModelDataProperty(int index) { this.index = index; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty; } public int index() { return this.index; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/CustomModelDataProperty;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.codecs.PrimitiveCodec<String> VALUE_CODEC = Codec.STRING; public static final SelectItemModelProperty.Type<CustomModelDataProperty, String> TYPE;
/*    */   static {
/* 18 */     TYPE = SelectItemModelProperty.Type.create(
/* 19 */         RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", 0).forGetter(CustomModelDataProperty::index)).apply((com.mojang.datafixers.kinds.Applicative)i, CustomModelDataProperty::new)), (Codec<String>)VALUE_CODEC);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 27 */     CustomModelData customModelData = (CustomModelData)itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
/* 28 */     if (customModelData != null) {
/* 29 */       return customModelData.getString(this.index);
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectItemModelProperty.Type<CustomModelDataProperty, String> type() {
/* 36 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<String> valueCodec() {
/* 41 */     return (Codec<String>)VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/CustomModelDataProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */