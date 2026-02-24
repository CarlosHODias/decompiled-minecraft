/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.FishingHookPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaFishingLoot extends Record implements net.minecraft.data.loot.LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*  35 */   public VanillaFishingLoot(HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaFishingLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  35 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaFishingLoot; } public HolderLookup.Provider registries() { return this.registries; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaFishingLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaFishingLoot; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaFishingLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #35	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaFishingLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object; } public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  38 */     HolderLookup.RegistryLookup<Biome> biomes = this.registries.lookupOrThrow(Registries.BIOME);
/*     */     
/*  40 */     output.accept(BuiltInLootTables.FISHING, 
/*  41 */         LootTable.lootTable()
/*  42 */         .withPool(LootPool.lootPool()
/*  43 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  44 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_JUNK).setWeight(10).setQuality(-2))
/*  45 */           .add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_TREASURE).setWeight(5).setQuality(2)
/*  46 */             .when(net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate((net.minecraft.advancements.criterion.EntitySubPredicate)FishingHookPredicate.inOpenWater(true)))))
/*  47 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH).setWeight(85).setQuality(-1))));
/*     */ 
/*     */ 
/*     */     
/*  51 */     output.accept(BuiltInLootTables.FISHING_FISH, fishingFishLootTable());
/*     */     
/*  53 */     output.accept(BuiltInLootTables.FISHING_JUNK, 
/*  54 */         LootTable.lootTable()
/*  55 */         .withPool(LootPool.lootPool()
/*  56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LILY_PAD).setWeight(17))
/*  57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(10).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.0F, 0.9F))))
/*  58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(10))
/*  59 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10))
/*  60 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(10).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WATER)))
/*  61 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(5))
/*  62 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(2).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.0F, 0.9F))))
/*  63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOWL).setWeight(10))
/*  64 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(5))
/*  65 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.INK_SAC).setWeight(1).apply((LootItemFunction.Builder)net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(10.0F))))
/*  66 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TRIPWIRE_HOOK).setWeight(10))
/*  67 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  68 */           .add((LootPoolEntryContainer.Builder)((net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO)
/*  69 */             .when(net.minecraft.world.level.storage.loot.predicates.LocationCheck.checkLocation(net.minecraft.advancements.criterion.LocationPredicate.Builder.location().setBiomes((HolderSet)HolderSet.direct(new Holder[] { (Holder)
/*  70 */                       biomes.getOrThrow(Biomes.JUNGLE), (Holder)
/*  71 */                       biomes.getOrThrow(Biomes.SPARSE_JUNGLE), (Holder)
/*  72 */                       biomes.getOrThrow(Biomes.BAMBOO_JUNGLE)
/*     */                     
/*  74 */                     }))))).setWeight(10))));
/*     */ 
/*     */ 
/*     */     
/*  78 */     output.accept(BuiltInLootTables.FISHING_TREASURE, 
/*  79 */         LootTable.lootTable()
/*  80 */         .withPool(LootPool.lootPool()
/*  81 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG))
/*  82 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE))
/*  83 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOW)
/*  84 */             .apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.0F, 0.25F)))
/*  85 */             .apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F))))
/*     */           
/*  87 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD)
/*  88 */             .apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.0F, 0.25F)))
/*  89 */             .apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F))))
/*     */           
/*  91 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK)
/*  92 */             .apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F))))
/*     */           
/*  94 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAUTILUS_SHELL))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootTable.Builder fishingFishLootTable() {
/* 100 */     return LootTable.lootTable()
/* 101 */       .withPool(LootPool.lootPool()
/* 102 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(60))
/* 103 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(25))
/* 104 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TROPICAL_FISH).setWeight(2))
/* 105 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUFFERFISH).setWeight(13)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/loot/packs/VanillaFishingLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */