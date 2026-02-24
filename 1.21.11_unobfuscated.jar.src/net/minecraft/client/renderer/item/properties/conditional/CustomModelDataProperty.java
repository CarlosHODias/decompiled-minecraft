/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomModelData;
/*    */ 
/*    */ public final class CustomModelDataProperty extends Record implements ConditionalItemModelProperty {
/*    */   private final int index;
/*    */   public static final com.mojang.serialization.MapCodec<CustomModelDataProperty> MAP_CODEC;
/*    */   
/* 14 */   public CustomModelDataProperty(int index) { this.index = index; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty; } public int index() { return this.index; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("index", 0).forGetter(CustomModelDataProperty::index)).apply((com.mojang.datafixers.kinds.Applicative)i, CustomModelDataProperty::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner, int seed, net.minecraft.world.item.ItemDisplayContext displayContext) {
/* 21 */     CustomModelData customModelData = (CustomModelData)itemStack.get(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
/* 22 */     if (customModelData != null) {
/* 23 */       return (customModelData.getBoolean(this.index) == Boolean.TRUE);
/*    */     }
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CustomModelDataProperty> type() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/conditional/CustomModelDataProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */