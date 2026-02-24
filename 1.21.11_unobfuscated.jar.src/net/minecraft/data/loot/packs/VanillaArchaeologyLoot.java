/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaArchaeologyLoot extends Record implements net.minecraft.data.loot.LootTableSubProvider {
/*     */   private final net.minecraft.core.HolderLookup.Provider registries;
/*     */   
/*  20 */   public VanillaArchaeologyLoot(net.minecraft.core.HolderLookup.Provider registries) { this.registries = registries; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  20 */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot; } public net.minecraft.core.HolderLookup.Provider registries() { return this.registries; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot; } public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #20	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaArchaeologyLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   } public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  24 */     output.accept(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY, 
/*  25 */         LootTable.lootTable()
/*  26 */         .withPool(LootPool.lootPool()
/*  27 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  28 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARMS_UP_POTTERY_SHERD).setWeight(2))
/*  29 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREWER_POTTERY_SHERD).setWeight(2))
/*  30 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BRICK))
/*  31 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  32 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK))
/*  33 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUSPICIOUS_STEW).apply((net.minecraft.world.level.storage.loot.functions.LootItemFunction.Builder)net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction.stewEffect()
/*  34 */               .withEffect(MobEffects.NIGHT_VISION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/*  35 */               .withEffect(MobEffects.JUMP_BOOST, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/*  36 */               .withEffect(MobEffects.WEAKNESS, (NumberProvider)UniformGenerator.between(6.0F, 8.0F))
/*  37 */               .withEffect(MobEffects.BLINDNESS, (NumberProvider)UniformGenerator.between(5.0F, 7.0F))
/*  38 */               .withEffect(MobEffects.POISON, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))
/*  39 */               .withEffect(MobEffects.SATURATION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     output.accept(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, 
/*  45 */         LootTable.lootTable()
/*  46 */         .withPool(LootPool.lootPool()
/*  47 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  48 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARCHER_POTTERY_SHERD))
/*  49 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MINER_POTTERY_SHERD))
/*  50 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRIZE_POTTERY_SHERD))
/*  51 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SKULL_POTTERY_SHERD))
/*  52 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND))
/*  53 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TNT))
/*  54 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER))
/*  55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))));
/*     */ 
/*     */ 
/*     */     
/*  59 */     output.accept(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 
/*  60 */         LootTable.lootTable()
/*  61 */         .withPool(LootPool.lootPool()
/*  62 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2))
/*  64 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(2))
/*  65 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_HOE).setWeight(2))
/*  66 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY).setWeight(2))
/*  67 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BRICK).setWeight(2))
/*  68 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.YELLOW_DYE).setWeight(2))
/*  69 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLUE_DYE).setWeight(2))
/*  70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_BLUE_DYE).setWeight(2))
/*  71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHITE_DYE).setWeight(2))
/*  72 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ORANGE_DYE).setWeight(2))
/*  73 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_CANDLE).setWeight(2))
/*  74 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GREEN_CANDLE).setWeight(2))
/*  75 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PURPLE_CANDLE).setWeight(2))
/*  76 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_CANDLE).setWeight(2))
/*  77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGENTA_STAINED_GLASS_PANE))
/*  78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PINK_STAINED_GLASS_PANE))
/*  79 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLUE_STAINED_GLASS_PANE))
/*  80 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_BLUE_STAINED_GLASS_PANE))
/*  81 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_STAINED_GLASS_PANE))
/*  82 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.YELLOW_STAINED_GLASS_PANE))
/*  83 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PURPLE_STAINED_GLASS_PANE))
/*  84 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_HANGING_SIGN))
/*  85 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OAK_HANGING_SIGN))
/*  86 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET))
/*  87 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL))
/*  88 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS))
/*  89 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS))
/*  90 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DEAD_BUSH))
/*  91 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOWER_POT))
/*  92 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING))
/*  93 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD))));
/*     */ 
/*     */ 
/*     */     
/*  97 */     output.accept(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, 
/*  98 */         LootTable.lootTable()
/*  99 */         .withPool(LootPool.lootPool()
/* 100 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 101 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BURN_POTTERY_SHERD))
/* 102 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DANGER_POTTERY_SHERD))
/* 103 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FRIEND_POTTERY_SHERD))
/* 104 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEART_POTTERY_SHERD))
/* 105 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEARTBREAK_POTTERY_SHERD))
/* 106 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HOWL_POTTERY_SHERD))
/* 107 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHEAF_POTTERY_SHERD))
/* 108 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 109 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 110 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 111 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE))
/* 112 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_RELIC))));
/*     */ 
/*     */ 
/*     */     
/* 116 */     output.accept(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY, 
/* 117 */         LootTable.lootTable()
/* 118 */         .withPool(LootPool.lootPool()
/* 119 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 120 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANGLER_POTTERY_SHERD))
/* 121 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHELTER_POTTERY_SHERD))
/* 122 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNORT_POTTERY_SHERD))
/* 123 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNIFFER_EGG))
/* 124 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE))
/* 125 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2))
/* 126 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(2))
/* 127 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_HOE).setWeight(2))
/* 128 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(2))
/* 129 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(2))));
/*     */ 
/*     */ 
/*     */     
/* 133 */     output.accept(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY, 
/* 134 */         LootTable.lootTable()
/* 135 */         .withPool(LootPool.lootPool()
/* 136 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 137 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLADE_POTTERY_SHERD))
/* 138 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPLORER_POTTERY_SHERD))
/* 139 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MOURNER_POTTERY_SHERD))
/* 140 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PLENTY_POTTERY_SHERD))
/* 141 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE))
/* 142 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2))
/* 143 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(2))
/* 144 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_HOE).setWeight(2))
/* 145 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(2))
/* 146 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(2))));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/loot/packs/VanillaArchaeologyLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */