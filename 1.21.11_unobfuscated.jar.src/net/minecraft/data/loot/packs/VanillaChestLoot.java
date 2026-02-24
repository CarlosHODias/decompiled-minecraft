/*      */ package net.minecraft.data.loot.packs;
/*      */ 
/*      */ import java.util.function.BiConsumer;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.StructureTags;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.Potions;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.level.storage.loot.LootPool;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*      */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetOminousBottleAmplifierFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*      */ 
/*      */ public final class VanillaChestLoot extends Record implements net.minecraft.data.loot.LootTableSubProvider {
/*      */   private final HolderLookup.Provider registries;
/*      */   
/*   45 */   public VanillaChestLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #45	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*   45 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot; } public HolderLookup.Provider registries() { return this.registries; }
/*      */   public final int hashCode() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #45	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot; }
/*      */   public final boolean equals(Object o) { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #45	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot;
/*      */     //   0	8	1	o	Ljava/lang/Object; } public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*   48 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*      */     
/*   50 */     output.accept(BuiltInLootTables.ABANDONED_MINESHAFT, 
/*   51 */         LootTable.lootTable()
/*   52 */         .withPool(LootPool.lootPool()
/*   53 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*   54 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/*   55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE))
/*   56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(30))
/*   57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*   58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*   59 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5)))
/*      */         
/*   61 */         .withPool(LootPool.lootPool()
/*   62 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/*   63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*   64 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*   65 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*   66 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*   67 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*   68 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/*   69 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*   70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/*   71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*   72 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*   73 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*   75 */         .withPool(LootPool.lootPool()
/*   76 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*   77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.RAIL).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/*   78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POWERED_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   79 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DETECTOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   80 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACTIVATOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   81 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 16.0F))))));
/*      */ 
/*      */ 
/*      */     
/*   85 */     output.accept(BuiltInLootTables.BASTION_BRIDGE, bastionBridgeLootTable());
/*   86 */     output.accept(BuiltInLootTables.BASTION_HOGLIN_STABLE, bastionHoglinStableLootTable());
/*   87 */     output.accept(BuiltInLootTables.BASTION_OTHER, bastionOtherLootTable());
/*   88 */     output.accept(BuiltInLootTables.BASTION_TREASURE, bastionTreasureLootTable());
/*      */     
/*   90 */     output.accept(BuiltInLootTables.BURIED_TREASURE, 
/*   91 */         LootTable.lootTable()
/*   92 */         .withPool(LootPool.lootPool()
/*   93 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*   94 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEART_OF_THE_SEA)))
/*      */         
/*   96 */         .withPool(LootPool.lootPool()
/*   97 */           .setRolls((NumberProvider)UniformGenerator.between(5.0F, 8.0F))
/*   98 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   99 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  100 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TNT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  102 */         .withPool(LootPool.lootPool()
/*  103 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  104 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/*  105 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  106 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */         
/*  108 */         .withPool(LootPool.lootPool()
/*  109 */           .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/*  110 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/*  111 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD))
/*  112 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SPEAR)))
/*      */         
/*  114 */         .withPool(LootPool.lootPool()
/*  115 */           .setRolls((NumberProvider)ConstantValue.exactly(2.0F))
/*  116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_COD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  117 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_SALMON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*  119 */         .withPool(LootPool.lootPool()
/*  120 */           .setRolls((NumberProvider)UniformGenerator.between(0.0F, 2.0F))
/*  121 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
/*      */         
/*  123 */         .withPool(LootPool.lootPool()
/*  124 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  125 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/*  126 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  127 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  128 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  129 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  133 */     output.accept(BuiltInLootTables.ANCIENT_CITY, ancientCityLootTable());
/*      */     
/*  135 */     output.accept(BuiltInLootTables.ANCIENT_CITY_ICE_BOX, 
/*  136 */         LootTable.lootTable()
/*  137 */         .withPool(LootPool.lootPool()
/*  138 */           .setRolls((NumberProvider)UniformGenerator.between(4.0F, 10.0F))
/*      */ 
/*      */           
/*  141 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUSPICIOUS_STEW).setWeight(1).apply((LootItemFunction.Builder)SetStewEffectFunction.stewEffect()
/*  142 */               .withEffect(MobEffects.NIGHT_VISION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/*  143 */               .withEffect(MobEffects.BLINDNESS, (NumberProvider)UniformGenerator.between(5.0F, 7.0F)))
/*  144 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/*  145 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/*  146 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/*      */ 
/*      */           
/*  149 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PACKED_ICE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/*      */ 
/*      */           
/*  152 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  156 */     output.accept(BuiltInLootTables.DESERT_PYRAMID, desertPyramidLootTable());
/*  157 */     output.accept(BuiltInLootTables.END_CITY_TREASURE, endCityTreasureLootTable());
/*      */     
/*  159 */     output.accept(BuiltInLootTables.IGLOO_CHEST, 
/*  160 */         LootTable.lootTable()
/*  161 */         .withPool(LootPool.lootPool()
/*  162 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 8.0F))
/*  163 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  164 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  165 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  166 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).setWeight(2))
/*  167 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  168 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  169 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  171 */         .withPool(LootPool.lootPool()
/*  172 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  173 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))));
/*      */ 
/*      */ 
/*      */     
/*  177 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE, jungleTempleLootTable());
/*      */     
/*  179 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER, 
/*  180 */         LootTable.lootTable()
/*  181 */         .withPool(LootPool.lootPool()
/*  182 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 2.0F))
/*  183 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(30).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  187 */     output.accept(BuiltInLootTables.NETHER_BRIDGE, netherBridgeLootTable());
/*      */     
/*  189 */     output.accept(BuiltInLootTables.PILLAGER_OUTPOST, pillagerOutpostLootTable());
/*      */     
/*  191 */     output.accept(BuiltInLootTables.SHIPWRECK_MAP, shipwreckMapLootTable());
/*      */     
/*  193 */     output.accept(BuiltInLootTables.SHIPWRECK_SUPPLY, shipwreckSupplyLootTable());
/*      */     
/*  195 */     output.accept(BuiltInLootTables.SHIPWRECK_TREASURE, shipwreckTreasureLootTable());
/*      */     
/*  197 */     output.accept(BuiltInLootTables.SIMPLE_DUNGEON, 
/*  198 */         LootTable.lootTable()
/*  199 */         .withPool(LootPool.lootPool()
/*  200 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  201 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  202 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/*  203 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/*  204 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE).setWeight(2))
/*  205 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(15))
/*  206 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(15))
/*  207 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(20))
/*  208 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/*  209 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(15))
/*  210 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/*  211 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/*  212 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */         
/*  214 */         .withPool(LootPool.lootPool()
/*  215 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 4.0F))
/*  216 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  217 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  218 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(20))
/*  219 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  220 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(10))
/*  221 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  222 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  223 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  224 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  225 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*  227 */         .withPool(LootPool.lootPool()
/*  228 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*  229 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  230 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  231 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  232 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  236 */     output.accept(BuiltInLootTables.SPAWN_BONUS_CHEST, 
/*  237 */         LootTable.lootTable()
/*  238 */         .withPool(LootPool.lootPool()
/*  239 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  240 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/*  241 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_AXE).setWeight(3)))
/*      */         
/*  243 */         .withPool(LootPool.lootPool()
/*  244 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  245 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE))
/*  246 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_PICKAXE).setWeight(3)))
/*      */         
/*  248 */         .withPool(LootPool.lootPool()
/*  249 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*  250 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  251 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  252 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  254 */         .withPool(LootPool.lootPool()
/*  255 */           .setRolls((NumberProvider)ConstantValue.exactly(4.0F))
/*  256 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 12.0F))))
/*  257 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_PLANKS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 12.0F))))
/*  258 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  259 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SPRUCE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  260 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BIRCH_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  261 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.JUNGLE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  262 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACACIA_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  263 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  264 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.MANGROVE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  268 */     output.accept(BuiltInLootTables.STRONGHOLD_CORRIDOR, strongholdCorridorLootTable());
/*      */     
/*  270 */     output.accept(BuiltInLootTables.STRONGHOLD_CROSSING, 
/*  271 */         LootTable.lootTable()
/*  272 */         .withPool(LootPool.lootPool()
/*  273 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 4.0F))
/*  274 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  275 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  276 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*  277 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/*  278 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  279 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  280 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE))
/*  281 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  285 */     output.accept(BuiltInLootTables.STRONGHOLD_LIBRARY, strongholdLibraryLootTable());
/*      */     
/*  287 */     output.accept(BuiltInLootTables.UNDERWATER_RUIN_BIG, 
/*  288 */         LootTable.lootTable()
/*  289 */         .withPool(LootPool.lootPool()
/*  290 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 8.0F))
/*  291 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  292 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  293 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  294 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SPEAR).setWeight(2))
/*  295 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  297 */         .withPool(LootPool.lootPool()
/*  298 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  299 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))
/*  300 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  301 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/*  302 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET))
/*  303 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  304 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(10).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply((LootItemFunction.Builder)SetNameFunction.setName((Component)Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */         
/*  306 */         .withPool(LootPool.lootPool()
/*  307 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  308 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/*  309 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  310 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  311 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  312 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  316 */     output.accept(BuiltInLootTables.UNDERWATER_RUIN_SMALL, 
/*  317 */         LootTable.lootTable()
/*  318 */         .withPool(LootPool.lootPool()
/*  319 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 8.0F))
/*  320 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  321 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).setWeight(2))
/*  322 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SPEAR).setWeight(2))
/*  323 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(5))
/*  324 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  325 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  327 */         .withPool(LootPool.lootPool()
/*  328 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  329 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/*  330 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET))
/*  331 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  332 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(5).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply((LootItemFunction.Builder)SetNameFunction.setName((Component)Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */         
/*  334 */         .withPool(LootPool.lootPool()
/*  335 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  336 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/*  337 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  338 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  339 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  340 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  344 */     output.accept(BuiltInLootTables.VILLAGE_WEAPONSMITH, 
/*  345 */         LootTable.lootTable()
/*  346 */         .withPool(LootPool.lootPool()
/*  347 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  348 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  349 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  350 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  351 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  352 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  353 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*  354 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(5))
/*  355 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SPEAR).setWeight(5))
/*  356 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_SPEAR).setWeight(7))
/*  357 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(5))
/*  358 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(5))
/*  359 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(5))
/*  360 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(5))
/*  361 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/*  362 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/*  363 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/*  364 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/*  365 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/*  366 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/*  367 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR)))
/*      */         
/*  369 */         .withPool(LootPool.lootPool()
/*  370 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  371 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  372 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  375 */     output.accept(BuiltInLootTables.VILLAGE_TOOLSMITH, 
/*  376 */         LootTable.lootTable()
/*  377 */         .withPool(LootPool.lootPool()
/*  378 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  379 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  380 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  381 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  382 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  383 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*  384 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  385 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  386 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SHOVEL).setWeight(5))));
/*      */ 
/*      */ 
/*      */     
/*  390 */     output.accept(BuiltInLootTables.VILLAGE_CARTOGRAPHER, 
/*  391 */         LootTable.lootTable()
/*  392 */         .withPool(LootPool.lootPool()
/*  393 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  394 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  395 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  396 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).setWeight(5))
/*  397 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  398 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*  399 */         .withPool(LootPool.lootPool()
/*  400 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  401 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  402 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  405 */     output.accept(BuiltInLootTables.VILLAGE_MASON, 
/*  406 */         LootTable.lootTable()
/*  407 */         .withPool(LootPool.lootPool()
/*  408 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  409 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY_BALL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  410 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOWER_POT).setWeight(1))
/*  411 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.STONE).setWeight(2))
/*  412 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.STONE_BRICKS).setWeight(2))
/*  413 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  414 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.YELLOW_DYE).setWeight(1))
/*  415 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SMOOTH_STONE).setWeight(1))
/*  416 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  420 */     output.accept(BuiltInLootTables.VILLAGE_ARMORER, 
/*  421 */         LootTable.lootTable()
/*  422 */         .withPool(LootPool.lootPool()
/*  423 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  424 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  425 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  426 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(1))
/*  427 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  431 */     output.accept(BuiltInLootTables.VILLAGE_SHEPHERD, 
/*  432 */         LootTable.lootTable()
/*  433 */         .withPool(LootPool.lootPool()
/*  434 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  435 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WHITE_WOOL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  436 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BLACK_WOOL).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  437 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GRAY_WOOL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  438 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BROWN_WOOL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  439 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LIGHT_GRAY_WOOL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  440 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  441 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHEARS).setWeight(1))
/*  442 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  446 */     output.accept(BuiltInLootTables.VILLAGE_BUTCHER, 
/*  447 */         LootTable.lootTable()
/*  448 */         .withPool(LootPool.lootPool()
/*  449 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  450 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  451 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  452 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  453 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  454 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUTTON).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  455 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  459 */     output.accept(BuiltInLootTables.VILLAGE_FLETCHER, 
/*  460 */         LootTable.lootTable()
/*  461 */         .withPool(LootPool.lootPool()
/*  462 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  463 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  464 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  465 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  466 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EGG).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  467 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  468 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  472 */     output.accept(BuiltInLootTables.VILLAGE_FISHER, 
/*  473 */         LootTable.lootTable()
/*  474 */         .withPool(LootPool.lootPool()
/*  475 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  476 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  477 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  478 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  479 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  480 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BARREL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  481 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  482 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  486 */     output.accept(BuiltInLootTables.VILLAGE_TANNERY, 
/*  487 */         LootTable.lootTable()
/*  488 */         .withPool(LootPool.lootPool()
/*  489 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  490 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  491 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE).setWeight(2))
/*  492 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(2))
/*  493 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_HELMET).setWeight(2))
/*  494 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  495 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_LEGGINGS).setWeight(2))
/*  496 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(1))
/*  497 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F)))))
/*      */         
/*  499 */         .withPool(LootPool.lootPool()
/*  500 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  501 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  502 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  505 */     output.accept(BuiltInLootTables.VILLAGE_TEMPLE, 
/*  506 */         LootTable.lootTable()
/*  507 */         .withPool(LootPool.lootPool()
/*  508 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  509 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  510 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  511 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  512 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  513 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  514 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  518 */     output.accept(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, 
/*  519 */         LootTable.lootTable()
/*  520 */         .withPool(LootPool.lootPool()
/*  521 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  522 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  523 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DANDELION).setWeight(2))
/*  524 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POPPY).setWeight(1))
/*  525 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  526 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  527 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  528 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1))
/*  529 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(1))
/*  530 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  531 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  533 */         .withPool(LootPool.lootPool()
/*  534 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  535 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  536 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  539 */     output.accept(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, 
/*  540 */         LootTable.lootTable()
/*  541 */         .withPool(LootPool.lootPool()
/*  542 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  543 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  544 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FERN).setWeight(2))
/*  545 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LARGE_FERN).setWeight(2))
/*  546 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  547 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SWEET_BERRIES).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  548 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  549 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  550 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_PIE).setWeight(1))
/*  551 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  552 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SPRUCE_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  553 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_SIGN).setWeight(1))
/*  554 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_LOG).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */         
/*  556 */         .withPool(LootPool.lootPool()
/*  557 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  558 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  559 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  562 */     output.accept(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, 
/*  563 */         LootTable.lootTable()
/*  564 */         .withPool(LootPool.lootPool()
/*  565 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  566 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  567 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHORT_GRASS).setWeight(5))
/*  568 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TALL_GRASS).setWeight(5))
/*  569 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  570 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  571 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  572 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACACIA_SAPLING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  573 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(1))
/*  574 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  575 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(1)))
/*      */         
/*  577 */         .withPool(LootPool.lootPool()
/*  578 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  579 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  580 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  583 */     output.accept(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, 
/*  584 */         LootTable.lootTable()
/*  585 */         .withPool(LootPool.lootPool()
/*  586 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  587 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BLUE_ICE).setWeight(1))
/*  588 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SNOW_BLOCK).setWeight(4))
/*  589 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  590 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  591 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  592 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SOUP).setWeight(1))
/*  593 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FURNACE).setWeight(1))
/*  594 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  595 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  596 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F)))))
/*      */         
/*  598 */         .withPool(LootPool.lootPool()
/*  599 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  600 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  601 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  604 */     output.accept(BuiltInLootTables.VILLAGE_DESERT_HOUSE, 
/*  605 */         LootTable.lootTable()
/*  606 */         .withPool(LootPool.lootPool()
/*  607 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  608 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY_BALL).setWeight(1))
/*  609 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GREEN_DYE).setWeight(1))
/*  610 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CACTUS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  611 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  612 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  613 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1))
/*  614 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DEAD_BUSH).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  615 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F)))))
/*      */         
/*  617 */         .withPool(LootPool.lootPool()
/*  618 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  619 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  620 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*      */ 
/*      */     
/*  623 */     output.accept(BuiltInLootTables.WOODLAND_MANSION, woodlandMansionLootTable());
/*      */     
/*  625 */     output.accept(BuiltInLootTables.RUINED_PORTAL, 
/*  626 */         LootTable.lootTable()
/*  627 */         .withPool(LootPool.lootPool()
/*  628 */           .setRolls((NumberProvider)UniformGenerator.between(4.0F, 8.0F))
/*  629 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OBSIDIAN).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  630 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  631 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(9.0F, 18.0F))))
/*  632 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT_AND_STEEL).setWeight(40))
/*  633 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).setWeight(40))
/*      */           
/*  635 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/*  636 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 24.0F))))
/*  637 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  638 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  639 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HOE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  640 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SHOVEL).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  641 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_PICKAXE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  642 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  643 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  644 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  645 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*      */           
/*  647 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLISTERING_MELON_SLICE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))))
/*  648 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(5))
/*  649 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_WEIGHTED_PRESSURE_PLATE).setWeight(5))
/*  650 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))))
/*  651 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLOCK).setWeight(5))
/*  652 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/*      */           
/*  654 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BELL).setWeight(1))
/*  655 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
/*  656 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_BLOCK).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  658 */         .withPool(LootPool.lootPool()
/*  659 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  660 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(1))
/*  661 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LODESTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  666 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_DISPENSER, 
/*  667 */         LootTable.lootTable()
/*  668 */         .withPool(LootPool.lootPool()
/*  669 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  670 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  675 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_WATER_DISPENSER, 
/*  676 */         LootTable.lootTable()
/*  677 */         .withPool(LootPool.lootPool()
/*  678 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  679 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  684 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CHAMBER_DISPENSER, 
/*  685 */         LootTable.lootTable()
/*  686 */         .withPool(LootPool.lootPool()
/*  687 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  688 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(4))
/*  689 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(4))
/*  690 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(6))
/*  691 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EGG).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(2))
/*  692 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(6))
/*  693 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  694 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  695 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  696 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  697 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  698 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  699 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.HEALING)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  705 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_POT, 
/*  706 */         LootTable.lootTable()
/*  707 */         .withPool(LootPool.lootPool()
/*  708 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  709 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).setWeight(125))
/*  710 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).setWeight(100))
/*  711 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(100))
/*  712 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIAL_KEY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(10))
/*  713 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CREATOR_MUSIC_BOX).setWeight(5))
/*  714 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(5))
/*  715 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(5))
/*  716 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(1))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  721 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY, 
/*  722 */         LootTable.lootTable()
/*  723 */         .withPool(LootPool.lootPool()
/*  724 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 5.0F))
/*  725 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 14.0F))).setWeight(2))
/*  726 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)).setWeight(1))
/*  727 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)).setWeight(1))
/*  728 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))).setWeight(2))
/*  729 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 10.0F))).setWeight(2))
/*  730 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ACACIA_PLANKS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(1))
/*  731 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MOSS_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  732 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  733 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TUFF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 10.0F))).setWeight(1))
/*  734 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TORCH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(1))
/*  735 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/*  736 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))
/*  737 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  738 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MILK_BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  744 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE, 
/*  745 */         LootTable.lootTable()
/*  746 */         .withPool(LootPool.lootPool()
/*  747 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/*  748 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIAL_KEY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(1))
/*  749 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(5))
/*  750 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(10))
/*  751 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HONEYCOMB).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).setWeight(10))
/*  752 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 10.0F))).setWeight(10))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  758 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION, 
/*  759 */         LootTable.lootTable()
/*  760 */         .withPool(LootPool.lootPool()
/*  761 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  762 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(1))
/*  763 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).setWeight(5))
/*  764 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.5F))).setWeight(5))
/*  765 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.5F))).setWeight(5))
/*  766 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(10))
/*  767 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CAKE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))).setWeight(20))
/*  768 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.AMETHYST_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 20.0F))).setWeight(20))
/*  769 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(20))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  775 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION_BARREL, 
/*  776 */         LootTable.lootTable()
/*  777 */         .withPool(LootPool.lootPool()
/*  778 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  779 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.4F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1))
/*  780 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(1))
/*  781 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).setWeight(1))
/*  782 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(1))
/*  783 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(1))
/*  784 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(4))
/*  785 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(4))
/*  786 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAMBOO_PLANKS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 15.0F))).setWeight(10))
/*  787 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 10.0F))).setWeight(10))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  794 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR, 
/*  795 */         LootTable.lootTable()
/*  796 */         .withPool(LootPool.lootPool()
/*  797 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  798 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.4F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1))
/*  799 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HONEYCOMB).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).setWeight(1))
/*  800 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  801 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  802 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(2))
/*  803 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAMBOO_HANGING_SIGN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))).setWeight(2))
/*  804 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAMBOO_PLANKS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(2))
/*  805 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCAFFOLDING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 10.0F))).setWeight(2))
/*  806 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TORCH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(2))
/*  807 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TUFF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 20.0F))).setWeight(3))));
/*      */ 
/*      */ 
/*      */     
/*  811 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE, 
/*  812 */         LootTable.lootTable()
/*  813 */         .withPool(LootPool.lootPool()
/*  814 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  815 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  816 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHIELD).setWeight(3).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.5F, 1.0F))))
/*  817 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOW).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 15.0F))))
/*  818 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 20.0F))))
/*  819 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 10.0F))))
/*  820 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 10.0F))))
/*  821 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  822 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder()
/*  823 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] {
/*  824 */                     (Holder)enchantments.getOrThrow(Enchantments.SHARPNESS), (Holder)
/*  825 */                     enchantments.getOrThrow(Enchantments.BANE_OF_ARTHROPODS), (Holder)
/*  826 */                     enchantments.getOrThrow(Enchantments.EFFICIENCY), (Holder)
/*  827 */                     enchantments.getOrThrow(Enchantments.FORTUNE), (Holder)
/*  828 */                     enchantments.getOrThrow(Enchantments.SILK_TOUCH), (Holder)
/*  829 */                     enchantments.getOrThrow(Enchantments.FEATHER_FALLING)
/*      */ 
/*      */                   
/*  832 */                   })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder()
/*  833 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] {
/*  834 */                     (Holder)enchantments.getOrThrow(Enchantments.RIPTIDE), (Holder)
/*  835 */                     enchantments.getOrThrow(Enchantments.LOYALTY), (Holder)
/*  836 */                     enchantments.getOrThrow(Enchantments.CHANNELING), (Holder)
/*  837 */                     enchantments.getOrThrow(Enchantments.IMPALING), (Holder)
/*  838 */                     enchantments.getOrThrow(Enchantments.MENDING)
/*      */                   
/*  840 */                   })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(1).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 15.0F))))
/*  841 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).setWeight(1).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 15.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  845 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON, 
/*  846 */         LootTable.lootTable()
/*  847 */         .withPool(LootPool.lootPool()
/*  848 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  849 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/*  850 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))
/*  851 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  852 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  853 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  854 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HONEY_BOTTLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  855 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_BOTTLE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetOminousBottleAmplifierFunction.setAmplifier((NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/*  856 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))))
/*  857 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  861 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE, 
/*  862 */         LootTable.lootTable()
/*  863 */         .withPool(LootPool.lootPool()
/*  864 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  865 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(4))
/*  866 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(3))
/*  867 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUSTER_BANNER_PATTERN).setWeight(2))
/*  868 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_PRECIPICE).setWeight(2))
/*  869 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIDENT).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  873 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 
/*  874 */         LootTable.lootTable()
/*  875 */         .withPool(LootPool.lootPool()
/*  876 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  877 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE).setWeight(8))
/*  878 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON).setWeight(2)))
/*      */         
/*  880 */         .withPool(LootPool.lootPool()
/*  881 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  882 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON)))
/*      */         
/*  884 */         .withPool(LootPool.lootPool()
/*  885 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  886 */           .when(LootItemRandomChanceCondition.randomChance(0.25F))
/*  887 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE))));
/*      */ 
/*      */ 
/*      */     
/*  891 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE, 
/*  892 */         LootTable.lootTable()
/*  893 */         .withPool(LootPool.lootPool()
/*  894 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  895 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD_BLOCK).setWeight(5))
/*  896 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BLOCK).setWeight(4))
/*  897 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(4).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 20.0F))))
/*  898 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(3))
/*  899 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))))
/*  900 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))))
/*  901 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder()
/*  902 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] {
/*  903 */                     (Holder)enchantments.getOrThrow(Enchantments.KNOCKBACK), (Holder)
/*  904 */                     enchantments.getOrThrow(Enchantments.PUNCH), (Holder)
/*  905 */                     enchantments.getOrThrow(Enchantments.SMITE), (Holder)
/*  906 */                     enchantments.getOrThrow(Enchantments.LOOTING), (Holder)
/*  907 */                     enchantments.getOrThrow(Enchantments.MULTISHOT)
/*      */                   
/*  909 */                   })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder()
/*  910 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] {
/*  911 */                     (Holder)enchantments.getOrThrow(Enchantments.BREACH), (Holder)
/*  912 */                     enchantments.getOrThrow(Enchantments.DENSITY)
/*      */                   
/*  914 */                   })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)new SetEnchantmentsFunction.Builder()
/*  915 */               .withEnchantment((Holder)enchantments.getOrThrow(Enchantments.WIND_BURST), (NumberProvider)ConstantValue.exactly(1.0F))))
/*  916 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BLOCK).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  920 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON, 
/*  921 */         LootTable.lootTable()
/*  922 */         .withPool(LootPool.lootPool()
/*  923 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  924 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 10.0F))))
/*  925 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 12.0F))))
/*  926 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_SLOWNESS)))
/*  927 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F))))
/*  928 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_BOTTLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetOminousBottleAmplifierFunction.setAmplifier((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))));
/*      */ 
/*      */ 
/*      */     
/*  932 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE, 
/*  933 */         LootTable.lootTable()
/*  934 */         .withPool(LootPool.lootPool()
/*  935 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  936 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(3))
/*  937 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(3))
/*  938 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOW_BANNER_PATTERN).setWeight(2))
/*  939 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CREATOR).setWeight(1))
/*  940 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEAVY_CORE).setWeight(1))));
/*      */ 
/*      */ 
/*      */     
/*  944 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS, 
/*  945 */         LootTable.lootTable()
/*  946 */         .withPool(LootPool.lootPool()
/*  947 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  948 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE).setWeight(8))
/*  949 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON).setWeight(2)))
/*      */         
/*  951 */         .withPool(LootPool.lootPool()
/*  952 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  953 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON)))
/*      */         
/*  955 */         .withPool(LootPool.lootPool()
/*  956 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  957 */           .when(LootItemRandomChanceCondition.randomChance(0.75F))
/*  958 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE))));
/*      */ 
/*      */ 
/*      */     
/*  962 */     spawnerLootTables(output);
/*      */   }
/*      */   
/*      */   public void spawnerLootTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  966 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*      */ 
/*      */ 
/*      */     
/*  970 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY, 
/*  971 */         LootTable.lootTable()
/*  972 */         .withPool(LootPool.lootPool()
/*  973 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  974 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIAL_KEY))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  981 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES, 
/*  982 */         LootTable.lootTable()
/*  983 */         .withPool(LootPool.lootPool()
/*  984 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  985 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_CHICKEN).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  986 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  987 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  988 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/*  989 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SWIFTNESS)))));
/*      */ 
/*      */ 
/*      */     
/*  993 */     output.accept(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 
/*  994 */         LootTable.lootTable()
/*  995 */         .withPool(LootPool.lootPool()
/*  996 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  997 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_TRIAL_KEY))));
/*      */ 
/*      */ 
/*      */     
/* 1001 */     output.accept(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 
/* 1002 */         LootTable.lootTable()
/* 1003 */         .withPool(LootPool.lootPool()
/* 1004 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1005 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_BEEF).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1006 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1007 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1008 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 1009 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))));
/*      */ 
/*      */ 
/*      */     
/* 1013 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS, 
/* 1014 */         LootTable.lootTable()
/* 1015 */         .withPool(LootPool.lootPool()
/* 1016 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1017 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WIND_CHARGED)))
/* 1018 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.OOZING)))
/* 1019 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAVING)))
/* 1020 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.INFESTED)))
/* 1021 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))
/* 1022 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SWIFTNESS)))
/* 1023 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOW_FALLING))))
/*      */         
/* 1025 */         .withPool(LootPool.lootPool()
/* 1026 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1027 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1028 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))
/* 1029 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_SLOWNESS)))
/* 1030 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1031 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public LootTable.Builder shipwreckSupplyLootTable() {
/* 1037 */     return LootTable.lootTable()
/* 1038 */       .withPool(LootPool.lootPool()
/* 1039 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 10.0F))
/* 1040 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 12.0F))))
/* 1041 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1042 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MOSS_BLOCK).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1043 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POISONOUS_POTATO).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1044 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/* 1045 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 21.0F))))
/* 1046 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUSPICIOUS_STEW).setWeight(10).apply((LootItemFunction.Builder)SetStewEffectFunction.stewEffect()
/* 1047 */             .withEffect(MobEffects.NIGHT_VISION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/* 1048 */             .withEffect(MobEffects.JUMP_BOOST, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/* 1049 */             .withEffect(MobEffects.WEAKNESS, (NumberProvider)UniformGenerator.between(6.0F, 8.0F))
/* 1050 */             .withEffect(MobEffects.BLINDNESS, (NumberProvider)UniformGenerator.between(5.0F, 7.0F))
/* 1051 */             .withEffect(MobEffects.POISON, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))
/* 1052 */             .withEffect(MobEffects.SATURATION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))))
/*      */         
/* 1054 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1055 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 24.0F))))
/* 1056 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.PUMPKIN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1057 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1058 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1059 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TNT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1060 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1061 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1062 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1063 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1065 */       .withPool(LootPool.lootPool()
/* 1066 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1067 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5))
/* 1068 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*      */       
/* 1070 */       .withPool(LootPool.lootPool()
/* 1071 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1072 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/* 1073 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1074 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1075 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1076 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder shipwreckMapLootTable() {
/* 1081 */     return LootTable.lootTable()
/* 1082 */       .withPool(LootPool.lootPool()
/* 1083 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1084 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply((LootItemFunction.Builder)SetNameFunction.setName((Component)Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */       
/* 1086 */       .withPool(LootPool.lootPool()
/* 1087 */         .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/* 1088 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS))
/* 1089 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/* 1090 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLOCK))
/* 1091 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1092 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1093 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */       
/* 1095 */       .withPool(LootPool.lootPool()
/* 1096 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1097 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5))
/* 1098 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*      */       
/* 1100 */       .withPool(LootPool.lootPool()
/* 1101 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1102 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/* 1103 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1104 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1105 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1106 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionHoglinStableLootTable() {
/* 1111 */     return LootTable.lootTable()
/* 1112 */       .withPool(LootPool.lootPool()
/* 1113 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1114 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(15).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1115 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(12).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.95F))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1116 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1117 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1118 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))))
/* 1119 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1120 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1121 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 17.0F))))
/* 1122 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1124 */       .withPool(LootPool.lootPool()
/* 1125 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 4.0F))
/* 1126 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1127 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1128 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GLOWSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/* 1129 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1130 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SOUL_SAND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1131 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_NYLIUM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1132 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1133 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1134 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 17.0F))))
/* 1135 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/* 1136 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1137 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1138 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_FUNGUS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1139 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_ROOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F)))))
/*      */       
/* 1141 */       .withPool(LootPool.lootPool()
/* 1142 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1143 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1144 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1146 */       .withPool(LootPool.lootPool()
/* 1147 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1148 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1149 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionBridgeLootTable() {
/* 1154 */     return LootTable.lootTable()
/* 1155 */       .withPool(LootPool.lootPool()
/* 1156 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1157 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LODESTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1159 */       .withPool(LootPool.lootPool()
/* 1160 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 2.0F))
/* 1161 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.5F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1162 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(10.0F, 28.0F))))
/* 1163 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 12.0F))))
/* 1164 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/* 1165 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1166 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/* 1167 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/* 1168 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1169 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1170 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1171 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1172 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1173 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1175 */       .withPool(LootPool.lootPool()
/* 1176 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 1177 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1178 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1179 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 17.0F))))
/* 1180 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1181 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F)))))
/*      */       
/* 1183 */       .withPool(LootPool.lootPool()
/* 1184 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1185 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1186 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1188 */       .withPool(LootPool.lootPool()
/* 1189 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1190 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1191 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder endCityTreasureLootTable() {
/* 1196 */     return LootTable.lootTable()
/* 1197 */       .withPool(LootPool.lootPool()
/* 1198 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 6.0F))
/* 1199 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1200 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/* 1201 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1202 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1203 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1204 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/* 1205 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 1206 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 1207 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 1208 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 1209 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1210 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SPEAR).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1211 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1212 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1213 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1214 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1215 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1216 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1217 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1218 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1219 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1220 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1221 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1222 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1223 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SHOVEL).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F)))))
/*      */       
/* 1225 */       .withPool(LootPool.lootPool()
/* 1226 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1227 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(14))
/* 1228 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder netherBridgeLootTable() {
/* 1233 */     return LootTable.lootTable()
/* 1234 */       .withPool(LootPool.lootPool()
/* 1235 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 1236 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1237 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1238 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1239 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).setWeight(5))
/* 1240 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).setWeight(5))
/* 1241 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT_AND_STEEL).setWeight(5))
/* 1242 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHER_WART).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 1243 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(10))
/* 1244 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(8))
/* 1245 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(5))
/* 1246 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(5))
/* 1247 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(3))
/* 1248 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */       
/* 1250 */       .withPool(LootPool.lootPool()
/* 1251 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1252 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(14))
/* 1253 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionTreasureLootTable() {
/* 1258 */     return LootTable.lootTable()
/* 1259 */       .withPool(LootPool.lootPool()
/* 1260 */         .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/* 1261 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1262 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ANCIENT_DEBRIS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1263 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1264 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ANCIENT_DEBRIS).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))))
/* 1265 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1266 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SPEAR).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1267 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1268 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1269 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1270 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1271 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(6))
/* 1272 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SPEAR).setWeight(6))
/* 1273 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 1274 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(5))
/* 1275 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(5))
/* 1276 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(5))
/* 1277 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1278 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1280 */       .withPool(LootPool.lootPool()
/* 1281 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 4.0F))
/* 1282 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(12.0F, 25.0F))))
/* 1283 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1284 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1285 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 9.0F))))
/* 1286 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 9.0F))))
/* 1287 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))
/* 1288 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.QUARTZ).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 23.0F))))
/* 1289 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 15.0F))))
/* 1290 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F)))))
/*      */       
/* 1292 */       .withPool(LootPool.lootPool()
/* 1293 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1294 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1295 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1297 */       .withPool(LootPool.lootPool()
/* 1298 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1299 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder bastionOtherLootTable() {
/* 1304 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 1305 */     return LootTable.lootTable()
/* 1306 */       .withPool(LootPool.lootPool()
/* 1307 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1308 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1309 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1310 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1311 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1312 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1313 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(10.0F, 22.0F))))
/* 1314 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PIGLIN_BANNER_PATTERN).setWeight(9).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1315 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_PIGSTEP).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1316 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 17.0F))))
/* 1317 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(9).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1318 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder().withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SOUL_SPEED)))))
/*      */       
/* 1320 */       .withPool(LootPool.lootPool()
/* 1321 */         .setRolls((NumberProvider)ConstantValue.exactly(2.0F))
/* 1322 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(2).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.9F))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1323 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_BLOCK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1324 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder().withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/* 1325 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1326 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1327 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1328 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1329 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1330 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1331 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1332 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1333 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1334 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1335 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */       
/* 1337 */       .withPool(LootPool.lootPool()
/* 1338 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 4.0F))
/* 1339 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1340 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_CHAIN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 10.0F))))
/* 1341 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1342 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BONE_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/* 1343 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1344 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1345 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1346 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1347 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 17.0F))))
/* 1348 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1350 */       .withPool(LootPool.lootPool()
/* 1351 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1352 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1353 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1355 */       .withPool(LootPool.lootPool()
/* 1356 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1357 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1358 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder woodlandMansionLootTable() {
/* 1363 */     return LootTable.lootTable()
/* 1364 */       .withPool(LootPool.lootPool()
/* 1365 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 1366 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD).setWeight(20))
/* 1367 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/* 1368 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 1369 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(15))
/* 1370 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(15))
/* 1371 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(20))
/* 1372 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_CHESTPLATE).setWeight(10))
/* 1373 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HOE).setWeight(15))
/* 1374 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 1375 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1377 */       .withPool(LootPool.lootPool()
/* 1378 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 4.0F))
/* 1379 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1380 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1381 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(20))
/* 1382 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1383 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(10))
/* 1384 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1385 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1386 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1387 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1388 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1389 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RESIN_CLUMP).setWeight(50).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */       
/* 1391 */       .withPool(LootPool.lootPool()
/* 1392 */         .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/* 1393 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1394 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1395 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1396 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F)))))
/*      */       
/* 1398 */       .withPool(LootPool.lootPool()
/* 1399 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1400 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(1))
/* 1401 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder strongholdLibraryLootTable() {
/* 1406 */     return LootTable.lootTable()
/* 1407 */       .withPool(LootPool.lootPool()
/* 1408 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 10.0F))
/* 1409 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1410 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1411 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/* 1412 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS))
/* 1413 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*      */       
/* 1415 */       .withPool(LootPool.lootPool()
/* 1416 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1417 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder strongholdCorridorLootTable() {
/* 1422 */     return LootTable.lootTable()
/* 1423 */       .withPool(LootPool.lootPool()
/* 1424 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 1425 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).setWeight(10))
/* 1426 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1427 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1428 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1429 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/* 1430 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1431 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1432 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/* 1433 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(5))
/* 1434 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(5))
/* 1435 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(5))
/* 1436 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(5))
/* 1437 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(5))
/* 1438 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))
/* 1439 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1440 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 1441 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 1442 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 1443 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 1444 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE))
/* 1445 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*      */       
/* 1447 */       .withPool(LootPool.lootPool()
/* 1448 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1449 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1450 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder ancientCityLootTable() {
/* 1455 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 1456 */     return LootTable.lootTable()
/* 1457 */       .withPool(LootPool.lootPool()
/* 1458 */         .setRolls((NumberProvider)UniformGenerator.between(5.0F, 10.0F))
/*      */ 
/*      */         
/* 1461 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1462 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE).setWeight(1))
/*      */ 
/*      */         
/* 1465 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1466 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_CATALYST).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1467 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(2))
/* 1468 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HOE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(30.0F, 50.0F))))
/* 1469 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1470 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1471 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1472 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(2))
/* 1473 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(2))
/* 1474 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(30.0F, 50.0F))))
/*      */ 
/*      */         
/* 1477 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(3).apply((LootItemFunction.Builder)new EnchantRandomlyFunction.Builder().withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SWIFT_SNEAK))))
/* 1478 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 10.0F))))
/* 1479 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_SENSOR).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1480 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CANDLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1481 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.AMETHYST_SHARD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 1482 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1483 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 1484 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/*      */         
/* 1486 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ECHO_SHARD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1487 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DISC_FRAGMENT_5).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*      */ 
/*      */         
/* 1490 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)))
/* 1491 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1492 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 10.0F))))
/* 1493 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 1494 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SOUL_TORCH).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/*      */ 
/*      */         
/* 1497 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 15.0F)))))
/*      */       
/* 1499 */       .withPool(LootPool.lootPool()
/* 1500 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1501 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(75))
/* 1502 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(4))
/* 1503 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder jungleTempleLootTable() {
/* 1508 */     return LootTable.lootTable()
/* 1509 */       .withPool(LootPool.lootPool()
/* 1510 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 6.0F))
/* 1511 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1512 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1513 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1514 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1515 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1516 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1517 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 1518 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1519 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 1520 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 1521 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 1522 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 1523 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*      */       
/* 1525 */       .withPool(LootPool.lootPool()
/* 1526 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1527 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))
/* 1528 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder shipwreckTreasureLootTable() {
/* 1533 */     return LootTable.lootTable()
/* 1534 */       .withPool(LootPool.lootPool()
/* 1535 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 6.0F))
/* 1536 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(90).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1537 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1538 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1539 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5))
/* 1540 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(5)))
/*      */       
/* 1542 */       .withPool(LootPool.lootPool()
/* 1543 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 5.0F))
/* 1544 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(50).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1545 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1546 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F)))))
/*      */       
/* 1548 */       .withPool(LootPool.lootPool()
/* 1549 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1550 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5))
/* 1551 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*      */       
/* 1553 */       .withPool(LootPool.lootPool()
/* 1554 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1555 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/* 1556 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1557 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1558 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1559 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder pillagerOutpostLootTable() {
/* 1564 */     return LootTable.lootTable()
/* 1565 */       .withPool(LootPool.lootPool()
/* 1566 */         .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/* 1567 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW)))
/*      */       
/* 1569 */       .withPool(LootPool.lootPool()
/* 1570 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 1571 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))
/* 1572 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1573 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F)))))
/*      */       
/* 1575 */       .withPool(LootPool.lootPool()
/* 1576 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 1577 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */       
/* 1579 */       .withPool(LootPool.lootPool()
/* 1580 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 1581 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(7))
/* 1582 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1583 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1584 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIPWIRE_HOOK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1585 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1586 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1588 */       .withPool(LootPool.lootPool()
/* 1589 */         .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/* 1590 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOAT_HORN)).apply((LootItemFunction.Builder)net.minecraft.world.level.storage.loot.functions.SetInstrumentFunction.setInstrumentOptions(net.minecraft.tags.InstrumentTags.REGULAR_GOAT_HORNS)))
/*      */       
/* 1592 */       .withPool(LootPool.lootPool()
/* 1593 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1594 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(3))
/* 1595 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ 
/*      */   
/*      */   public LootTable.Builder desertPyramidLootTable() {
/* 1600 */     return LootTable.lootTable()
/* 1601 */       .withPool(LootPool.lootPool()
/* 1602 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 1603 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1604 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1605 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1606 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1607 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1608 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1609 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 1610 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1611 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(15))
/* 1612 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/* 1613 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 1614 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 1615 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(20).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1616 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/* 1617 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 1618 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(15)))
/*      */       
/* 1620 */       .withPool(LootPool.lootPool()
/* 1621 */         .setRolls((NumberProvider)ConstantValue.exactly(4.0F))
/* 1622 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1623 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1624 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1625 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1626 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SAND).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F)))))
/*      */       
/* 1628 */       .withPool(LootPool.lootPool()
/* 1629 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1630 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(6))
/* 1631 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/loot/packs/VanillaChestLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */