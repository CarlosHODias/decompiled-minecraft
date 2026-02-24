/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ 
/*    */ public final class DropChances extends Record {
/*    */   private final java.util.Map<EquipmentSlot, Float> byEquipment;
/*    */   public static final float DEFAULT_EQUIPMENT_DROP_CHANCE = 0.085F;
/*    */   public static final float PRESERVE_ITEM_DROP_CHANCE_THRESHOLD = 1.0F;
/*    */   public static final int PRESERVE_ITEM_DROP_CHANCE = 2;
/*    */   
/* 10 */   public DropChances(java.util.Map<EquipmentSlot, Float> byEquipment) { this.byEquipment = byEquipment; } public java.util.Map<EquipmentSlot, Float> byEquipment() { return this.byEquipment; }
/*    */    public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/DropChances;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */   } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/DropChances;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */   } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/DropChances;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 17 */   } public static final DropChances DEFAULT = new DropChances(net.minecraft.util.Util.makeEnumMap(EquipmentSlot.class, slot -> 0.085F));
/*    */   
/* 19 */   public static final com.mojang.serialization.Codec<DropChances> CODEC = com.mojang.serialization.Codec.unboundedMap((com.mojang.serialization.Codec)EquipmentSlot.CODEC, net.minecraft.util.ExtraCodecs.NON_NEGATIVE_FLOAT)
/* 20 */     .xmap(DropChances::toEnumMap, DropChances::filterDefaultValues)
/* 21 */     .xmap(DropChances::new, DropChances::byEquipment);
/*    */   
/*    */   private static java.util.Map<EquipmentSlot, Float> filterDefaultValues(java.util.Map<EquipmentSlot, Float> map) {
/* 24 */     java.util.Map<EquipmentSlot, Float> filteredMap = new java.util.HashMap<>(map);
/* 25 */     filteredMap.values().removeIf(chance -> (chance == 0.085F));
/* 26 */     return filteredMap;
/*    */   }
/*    */   
/*    */   private static java.util.Map<EquipmentSlot, Float> toEnumMap(java.util.Map<EquipmentSlot, Float> map) {
/* 30 */     return net.minecraft.util.Util.makeEnumMap(EquipmentSlot.class, slot -> (Float)map.getOrDefault(slot, 0.085F));
/*    */   }
/*    */   
/*    */   public DropChances withGuaranteedDrop(EquipmentSlot slot) {
/* 34 */     return withEquipmentChance(slot, 2.0F);
/*    */   }
/*    */   
/*    */   public DropChances withEquipmentChance(EquipmentSlot slot, float chance) {
/* 38 */     if (chance < 0.0F) {
/* 39 */       throw new IllegalArgumentException("Tried to set invalid equipment chance " + chance + " for " + String.valueOf(slot));
/*    */     }
/* 41 */     if (byEquipment(slot) == chance) {
/* 42 */       return this;
/*    */     }
/* 44 */     return new DropChances(
/* 45 */         net.minecraft.util.Util.makeEnumMap(EquipmentSlot.class, newSlot -> (slot == slot) ? slot : byEquipment(slot)));
/*    */   }
/*    */ 
/*    */   
/*    */   public float byEquipment(EquipmentSlot slot) {
/* 50 */     return (Float)this.byEquipment.getOrDefault(slot, 0.085F);
/*    */   }
/*    */   
/*    */   public boolean isPreserved(EquipmentSlot slot) {
/* 54 */     return (byEquipment(slot) > 1.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/DropChances.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */