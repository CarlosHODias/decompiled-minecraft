/*    */ package net.minecraft.world.item.equipment;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ 
/*    */ public final class ArmorMaterial extends Record {
/*    */   private final int durability;
/*    */   private final java.util.Map<ArmorType, Integer> defense;
/*    */   private final int enchantmentValue;
/*    */   private final net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> equipSound;
/*    */   private final float toughness;
/*    */   private final float knockbackResistance;
/*    */   private final net.minecraft.tags.TagKey<net.minecraft.world.item.Item> repairIngredient;
/*    */   private final net.minecraft.resources.ResourceKey<EquipmentAsset> assetId;
/*    */   
/* 17 */   public ArmorMaterial(int durability, java.util.Map<ArmorType, Integer> defense, int enchantmentValue, net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> equipSound, float toughness, float knockbackResistance, net.minecraft.tags.TagKey<net.minecraft.world.item.Item> repairIngredient, net.minecraft.resources.ResourceKey<EquipmentAsset> assetId) { this.durability = durability; this.defense = defense; this.enchantmentValue = enchantmentValue; this.equipSound = equipSound; this.toughness = toughness; this.knockbackResistance = knockbackResistance; this.repairIngredient = repairIngredient; this.assetId = assetId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/ArmorMaterial;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/ArmorMaterial; } public int durability() { return this.durability; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/ArmorMaterial;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/ArmorMaterial; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/ArmorMaterial;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/ArmorMaterial;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Map<ArmorType, Integer> defense() { return this.defense; } public int enchantmentValue() { return this.enchantmentValue; } public net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> equipSound() { return this.equipSound; } public float toughness() { return this.toughness; } public float knockbackResistance() { return this.knockbackResistance; } public net.minecraft.tags.TagKey<net.minecraft.world.item.Item> repairIngredient() { return this.repairIngredient; } public net.minecraft.resources.ResourceKey<EquipmentAsset> assetId() { return this.assetId; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemAttributeModifiers createAttributes(ArmorType type) {
/* 28 */     int defense = (Integer)this.defense.getOrDefault(type, 0);
/*    */     
/* 30 */     ItemAttributeModifiers.Builder modifiers = ItemAttributeModifiers.builder();
/*    */     
/* 32 */     EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(type.getSlot());
/*    */     
/* 34 */     net.minecraft.resources.Identifier modifierId = net.minecraft.resources.Identifier.withDefaultNamespace("armor." + type.getName());
/* 35 */     modifiers.add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, new AttributeModifier(modifierId, defense, AttributeModifier.Operation.ADD_VALUE), slotGroup);
/* 36 */     modifiers.add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR_TOUGHNESS, new AttributeModifier(modifierId, this.toughness, AttributeModifier.Operation.ADD_VALUE), slotGroup);
/*    */     
/* 38 */     if (this.knockbackResistance > 0.0F) {
/* 39 */       modifiers.add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(modifierId, this.knockbackResistance, AttributeModifier.Operation.ADD_VALUE), slotGroup);
/*    */     }
/*    */     
/* 42 */     return modifiers.build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/equipment/ArmorMaterial.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */