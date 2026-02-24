/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*    */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*    */ 
/*    */ public final class TrimMaterialProperty extends Record implements SelectItemModelProperty<ResourceKey<TrimMaterial>> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/TrimMaterialProperty;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/TrimMaterialProperty;
/*    */   }
/*    */   
/* 17 */   public static final Codec<ResourceKey<TrimMaterial>> VALUE_CODEC = ResourceKey.codec(Registries.TRIM_MATERIAL);
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/TrimMaterialProperty;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/TrimMaterialProperty; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/TrimMaterialProperty;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/TrimMaterialProperty;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public static final SelectItemModelProperty.Type<TrimMaterialProperty, ResourceKey<TrimMaterial>> TYPE = SelectItemModelProperty.Type.create(com.mojang.serialization.MapCodec.unit(new TrimMaterialProperty()), VALUE_CODEC);
/*    */ 
/*    */   
/*    */   public ResourceKey<TrimMaterial> get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 23 */     ArmorTrim trim = (ArmorTrim)itemStack.get(net.minecraft.core.component.DataComponents.TRIM);
/* 24 */     if (trim == null) {
/* 25 */       return null;
/*    */     }
/* 27 */     return trim.material().unwrapKey().orElse(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectItemModelProperty.Type<TrimMaterialProperty, ResourceKey<TrimMaterial>> type() {
/* 32 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<ResourceKey<TrimMaterial>> valueCodec() {
/* 37 */     return VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/TrimMaterialProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */