/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*    */ import net.minecraft.advancements.criterion.EntityPredicate;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*    */ 
/*    */ public final class VanillaShearingLoot extends Record implements net.minecraft.data.loot.LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/* 29 */   public VanillaShearingLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaShearingLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 29 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaShearingLoot; } public HolderLookup.Provider registries() { return this.registries; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaShearingLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaShearingLoot; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaShearingLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaShearingLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object; } public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 32 */     output.accept(BuiltInLootTables.BOGGED_SHEAR, LootTable.lootTable()
/* 33 */         .withPool(LootPool.lootPool()
/* 34 */           .setRolls((NumberProvider)ConstantValue.exactly(2.0F))
/* 35 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_MUSHROOM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 36 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_MUSHROOM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*    */ 
/*    */ 
/*    */     
/* 40 */     LootData.WOOL_ITEM_BY_DYE.forEach((dye, wool) -> output.accept((ResourceKey)BuiltInLootTables.SHEAR_SHEEP_BY_DYE.get(dye), LootTable.lootTable().withPool(LootPool.lootPool().setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F)).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(wool)))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     output.accept(BuiltInLootTables.SHEAR_SHEEP, LootTable.lootTable()
/* 50 */         .withPool(
/* 51 */           net.minecraft.data.loot.EntityLootSubProvider.createSheepDispatchPool(BuiltInLootTables.SHEAR_SHEEP_BY_DYE)));
/*    */ 
/*    */ 
/*    */     
/* 55 */     output.accept(BuiltInLootTables.SHEAR_MOOSHROOM, LootTable.lootTable()
/* 56 */         .withPool(LootPool.lootPool()
/* 57 */           .add((LootPoolEntryContainer.Builder)net.minecraft.world.level.storage.loot.entries.AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] {
/* 58 */                 net.minecraft.world.level.storage.loot.entries.NestedLootTable.lootTableReference(BuiltInLootTables.SHEAR_RED_MOOSHROOM)
/* 59 */                 .when(net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.RED)).build()))), 
/* 60 */                 net.minecraft.world.level.storage.loot.entries.NestedLootTable.lootTableReference(BuiltInLootTables.SHEAR_BROWN_MOOSHROOM)
/* 61 */                 .when(net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.BROWN)).build())))
/*    */               }))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     output.accept(BuiltInLootTables.SHEAR_RED_MOOSHROOM, LootTable.lootTable()
/* 68 */         .withPool(LootPool.lootPool()
/* 69 */           .setRolls((NumberProvider)ConstantValue.exactly(5.0F))
/* 70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_MUSHROOM))));
/*    */ 
/*    */ 
/*    */     
/* 74 */     output.accept(BuiltInLootTables.SHEAR_BROWN_MOOSHROOM, LootTable.lootTable()
/* 75 */         .withPool(LootPool.lootPool()
/* 76 */           .setRolls((NumberProvider)ConstantValue.exactly(5.0F))
/* 77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_MUSHROOM))));
/*    */ 
/*    */ 
/*    */     
/* 81 */     output.accept(BuiltInLootTables.SHEAR_SNOW_GOLEM, LootTable.lootTable()
/* 82 */         .withPool(LootPool.lootPool()
/* 83 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 84 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARVED_PUMPKIN))));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/loot/packs/VanillaShearingLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */