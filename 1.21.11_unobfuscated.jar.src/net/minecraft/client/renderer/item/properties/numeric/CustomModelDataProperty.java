/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataProperty extends Record implements RangeSelectItemModelProperty {
/*    */   private final int index;
/*    */   public static final com.mojang.serialization.MapCodec<CustomModelDataProperty> MAP_CODEC;
/*    */   
/* 13 */   public CustomModelDataProperty(int index) { this.index = index; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty; } public int index() { return this.index; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", 0).forGetter(CustomModelDataProperty::index)).apply((com.mojang.datafixers.kinds.Applicative)i, CustomModelDataProperty::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.ItemOwner owner, int seed) {
/* 20 */     CustomModelData customModelData = (CustomModelData)itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
/* 21 */     if (customModelData != null) {
/* 22 */       Float value = customModelData.getFloat(this.index);
/* 23 */       if (value != null) {
/* 24 */         return value;
/*    */       }
/*    */     } 
/* 27 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CustomModelDataProperty> type() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/CustomModelDataProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */