/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariants;
/*     */ import net.minecraft.world.item.EitherHolder;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaGiftLoot extends Record implements net.minecraft.data.loot.LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*  34 */   public VanillaGiftLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  34 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot; } public HolderLookup.Provider registries() { return this.registries; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; } public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  37 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.CHICKEN_VARIANT);
/*     */     
/*  39 */     output.accept(BuiltInLootTables.CAT_MORNING_GIFT, 
/*  40 */         LootTable.lootTable()
/*  41 */         .withPool(LootPool.lootPool()
/*  42 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  43 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_HIDE).setWeight(10))
/*  44 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT).setWeight(10))
/*  45 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHICKEN).setWeight(10))
/*  46 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(10))
/*  47 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  48 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10))
/*  49 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PHANTOM_MEMBRANE).setWeight(2))));
/*     */ 
/*     */     
/*  52 */     output.accept(BuiltInLootTables.ARMORER_GIFT, 
/*  53 */         LootTable.lootTable()
/*  54 */         .withPool(LootPool.lootPool()
/*  55 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_HELMET))
/*  57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/*  58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_LEGGINGS))
/*  59 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_BOOTS))));
/*     */ 
/*     */     
/*  62 */     output.accept(BuiltInLootTables.BUTCHER_GIFT, 
/*  63 */         LootTable.lootTable()
/*  64 */         .withPool(LootPool.lootPool()
/*  65 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  66 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_RABBIT))
/*  67 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_CHICKEN))
/*  68 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP))
/*  69 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_BEEF))
/*  70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_MUTTON))));
/*     */ 
/*     */     
/*  73 */     output.accept(BuiltInLootTables.CARTOGRAPHER_GIFT, 
/*  74 */         LootTable.lootTable()
/*  75 */         .withPool(LootPool.lootPool()
/*  76 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/*  78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER))));
/*     */ 
/*     */     
/*  81 */     output.accept(BuiltInLootTables.CLERIC_GIFT, 
/*  82 */         LootTable.lootTable()
/*  83 */         .withPool(LootPool.lootPool()
/*  84 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  85 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE))
/*  86 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI))));
/*     */ 
/*     */     
/*  89 */     output.accept(BuiltInLootTables.FARMER_GIFT, 
/*  90 */         LootTable.lootTable()
/*  91 */         .withPool(LootPool.lootPool()
/*  92 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  93 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD))
/*  94 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_PIE))
/*  95 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKIE))));
/*     */ 
/*     */ 
/*     */     
/*  99 */     output.accept(BuiltInLootTables.FISHERMAN_GIFT, 
/* 100 */         LootTable.lootTable()
/* 101 */         .withPool(LootPool.lootPool()
/* 102 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 103 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD))
/* 104 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON))));
/*     */ 
/*     */     
/* 107 */     output.accept(BuiltInLootTables.FLETCHER_GIFT, 
/* 108 */         LootTable.lootTable()
/* 109 */         .withPool(LootPool.lootPool()
/* 110 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 111 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(26))
/* 112 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SWIFTNESS)))
/* 113 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)))
/* 114 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))
/* 115 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.HEALING)))
/* 116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.HARMING)))
/* 117 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.LEAPING)))
/* 118 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 119 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/* 120 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
/* 121 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.INVISIBILITY)))
/* 122 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.NIGHT_VISION)))
/* 123 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)))
/* 124 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))));
/*     */ 
/*     */     
/* 127 */     output.accept(BuiltInLootTables.LEATHERWORKER_GIFT, 
/* 128 */         LootTable.lootTable()
/* 129 */         .withPool(LootPool.lootPool()
/* 130 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 131 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER))));
/*     */ 
/*     */     
/* 134 */     output.accept(BuiltInLootTables.LIBRARIAN_GIFT, 
/* 135 */         LootTable.lootTable()
/* 136 */         .withPool(LootPool.lootPool()
/* 137 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 138 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK))));
/*     */ 
/*     */     
/* 141 */     output.accept(BuiltInLootTables.MASON_GIFT, 
/* 142 */         LootTable.lootTable()
/* 143 */         .withPool(LootPool.lootPool()
/* 144 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 145 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY))));
/*     */ 
/*     */     
/* 148 */     output.accept(BuiltInLootTables.SHEPHERD_GIFT, 
/* 149 */         LootTable.lootTable()
/* 150 */         .withPool(LootPool.lootPool()
/* 151 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 152 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHITE_WOOL))
/* 153 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ORANGE_WOOL))
/* 154 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGENTA_WOOL))
/* 155 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_BLUE_WOOL))
/* 156 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.YELLOW_WOOL))
/* 157 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIME_WOOL))
/* 158 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PINK_WOOL))
/* 159 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GRAY_WOOL))
/* 160 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_GRAY_WOOL))
/* 161 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CYAN_WOOL))
/* 162 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PURPLE_WOOL))
/* 163 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLUE_WOOL))
/* 164 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_WOOL))
/* 165 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GREEN_WOOL))
/* 166 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_WOOL))
/* 167 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLACK_WOOL))));
/*     */ 
/*     */     
/* 170 */     output.accept(BuiltInLootTables.TOOLSMITH_GIFT, 
/* 171 */         LootTable.lootTable()
/* 172 */         .withPool(LootPool.lootPool()
/* 173 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 174 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE))
/* 175 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 176 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_HOE))
/* 177 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SHOVEL))));
/*     */ 
/*     */     
/* 180 */     output.accept(BuiltInLootTables.WEAPONSMITH_GIFT, 
/* 181 */         LootTable.lootTable()
/* 182 */         .withPool(LootPool.lootPool()
/* 183 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 184 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 185 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE))
/* 186 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE))));
/*     */ 
/*     */     
/* 189 */     output.accept(BuiltInLootTables.UNEMPLOYED_GIFT, 
/* 190 */         LootTable.lootTable()
/* 191 */         .withPool(LootPool.lootPool()
/* 192 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 193 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS))));
/*     */ 
/*     */     
/* 196 */     output.accept(BuiltInLootTables.BABY_VILLAGER_GIFT, 
/* 197 */         LootTable.lootTable()
/* 198 */         .withPool(LootPool.lootPool()
/* 199 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 200 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POPPY))));
/*     */ 
/*     */     
/* 203 */     output.accept(BuiltInLootTables.SNIFFER_DIGGING, 
/* 204 */         LootTable.lootTable()
/* 205 */         .withPool(LootPool.lootPool()
/* 206 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 207 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TORCHFLOWER_SEEDS))
/* 208 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PITCHER_POD))));
/*     */ 
/*     */     
/* 211 */     output.accept(BuiltInLootTables.PANDA_SNEEZE, 
/* 212 */         LootTable.lootTable()
/* 213 */         .withPool(LootPool.lootPool()
/* 214 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 215 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).setWeight(1))
/* 216 */           .add((LootPoolEntryContainer.Builder)net.minecraft.world.level.storage.loot.entries.EmptyLootItem.emptyItem().setWeight(699))));
/*     */ 
/*     */     
/* 219 */     output.accept(BuiltInLootTables.CHICKEN_LAY, 
/* 220 */         LootTable.lootTable()
/* 221 */         .withPool(LootPool.lootPool()
/* 222 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 223 */           .add((LootPoolEntryContainer.Builder)net.minecraft.world.level.storage.loot.entries.AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] {
/* 224 */                 LootItem.lootTableItem((ItemLike)Items.EGG)
/* 225 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, new EitherHolder((Holder)registryLookup.getOrThrow(ChickenVariants.TEMPERATE)))).build()))), 
/* 226 */                 LootItem.lootTableItem((ItemLike)Items.BROWN_EGG)
/* 227 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, new EitherHolder((Holder)registryLookup.getOrThrow(ChickenVariants.WARM)))).build()))), 
/* 228 */                 LootItem.lootTableItem((ItemLike)Items.BLUE_EGG)
/* 229 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, new EitherHolder((Holder)registryLookup.getOrThrow(ChickenVariants.COLD)))).build())))
/*     */               }))));
/* 231 */     output.accept(BuiltInLootTables.ARMADILLO_SHED, 
/* 232 */         LootTable.lootTable()
/* 233 */         .withPool(LootPool.lootPool()
/* 234 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 235 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARMADILLO_SCUTE))));
/*     */ 
/*     */     
/* 238 */     output.accept(BuiltInLootTables.TURTLE_GROW, 
/* 239 */         LootTable.lootTable()
/* 240 */         .withPool(LootPool.lootPool()
/* 241 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 242 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TURTLE_SCUTE))));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/loot/packs/VanillaGiftLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */