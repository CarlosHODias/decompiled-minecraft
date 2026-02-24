/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class EquipmentTable extends Record {
/*    */   private final net.minecraft.resources.ResourceKey<LootTable> lootTable;
/*    */   private final Map<EquipmentSlot, Float> slotDropChances;
/*    */   public static final Codec<Map<EquipmentSlot, Float>> DROP_CHANCES_CODEC;
/*    */   public static final Codec<EquipmentTable> CODEC;
/*    */   
/* 14 */   public EquipmentTable(net.minecraft.resources.ResourceKey<LootTable> lootTable, Map<EquipmentSlot, Float> slotDropChances) { this.lootTable = lootTable; this.slotDropChances = slotDropChances; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/EquipmentTable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/world/entity/EquipmentTable; } public net.minecraft.resources.ResourceKey<LootTable> lootTable() { return this.lootTable; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/EquipmentTable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EquipmentTable; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/EquipmentTable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/EquipmentTable;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Map<EquipmentSlot, Float> slotDropChances() { return this.slotDropChances; } static {
/* 15 */     DROP_CHANCES_CODEC = Codec.either((Codec)Codec.FLOAT, (Codec)Codec.unboundedMap((Codec)EquipmentSlot.CODEC, (Codec)Codec.FLOAT)).xmap(either -> (Map)either.map(EquipmentTable::createForAllSlots, java.util.function.Function.identity()), provider -> {
/*    */           boolean dropChancesTheSame = (provider.values().stream().distinct().count() == 1L), allSlotsArePresent = provider.keySet().containsAll(EquipmentSlot.VALUES);
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 21 */           return (dropChancesTheSame && allSlotsArePresent) ? Either.left(provider.values().stream().findFirst().orElse(0.0F)) : Either.right(provider);
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(EquipmentTable::lootTable), (com.mojang.datafixers.kinds.App)DROP_CHANCES_CODEC.optionalFieldOf("slot_drop_chances", Map.of()).forGetter(EquipmentTable::slotDropChances)).apply((com.mojang.datafixers.kinds.Applicative)i, EquipmentTable::new));
/*    */   }
/*    */   public EquipmentTable(net.minecraft.resources.ResourceKey<LootTable> lootTable, float dropChance) {
/*    */     this(lootTable, createForAllSlots(dropChance));
/*    */   }
/*    */   private static Map<EquipmentSlot, Float> createForAllSlots(float dropChance) {
/* 39 */     return createForAllSlots(java.util.List.of(EquipmentSlot.values()), dropChance);
/*    */   }
/*    */   
/*    */   private static Map<EquipmentSlot, Float> createForAllSlots(java.util.List<EquipmentSlot> slots, float dropChance) {
/* 43 */     Map<EquipmentSlot, Float> values = com.google.common.collect.Maps.newHashMap();
/* 44 */     for (EquipmentSlot slot : slots) {
/* 45 */       values.put(slot, dropChance);
/*    */     }
/* 47 */     return values;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EquipmentTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */