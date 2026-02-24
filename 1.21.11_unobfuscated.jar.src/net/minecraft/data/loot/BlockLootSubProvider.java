/*     */ package net.minecraft.data.loot;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CandleBlock;
/*     */ import net.minecraft.world.level.block.CaveVines;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.DoublePlantBlock;
/*     */ import net.minecraft.world.level.block.FlowerPotBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.SegmentableBlock;
/*     */ import net.minecraft.world.level.block.SlabBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.storage.loot.IntRange;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
/*     */ import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LimitCount;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public abstract class BlockLootSubProvider implements LootTableSubProvider {
/*     */   protected final HolderLookup.Provider registries;
/*     */   protected final Set<Item> explosionResistant;
/*     */   
/*     */   protected LootItemCondition.Builder hasSilkTouch() {
/*  85 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().withComponents(
/*  86 */           DataComponentMatchers.Builder.components().partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)net.minecraft.core.component.predicates.EnchantmentsPredicate.enchantments(List.of(new net.minecraft.advancements.criterion.EnchantmentPredicate((Holder)this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()));
/*     */   }
/*     */   protected final FeatureFlagSet enabledFeatures; protected final Map<ResourceKey<LootTable>, LootTable.Builder> map;
/*     */   
/*     */   protected LootItemCondition.Builder doesNotHaveSilkTouch() {
/*  91 */     return hasSilkTouch().invert();
/*     */   }
/*     */   
/*     */   protected LootItemCondition.Builder hasShears() {
/*  95 */     return MatchTool.toolMatches(ItemPredicate.Builder.item().of((HolderGetter)this.registries.lookupOrThrow(Registries.ITEM), new ItemLike[] { (ItemLike)Items.SHEARS }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LootItemCondition.Builder hasShearsOrSilkTouch() {
/* 101 */     return (LootItemCondition.Builder)hasShears().or(hasSilkTouch());
/*     */   }
/*     */   
/*     */   private LootItemCondition.Builder doesNotHaveShearsOrSilkTouch() {
/* 105 */     return hasShearsOrSilkTouch().invert();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };
/* 114 */   private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };
/*     */   
/*     */   protected BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider registries) {
/* 117 */     this(explosionResistant, enabledFeatures, new HashMap<>(), registries);
/*     */   }
/*     */   
/*     */   protected BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, Map<ResourceKey<LootTable>, LootTable.Builder> map, HolderLookup.Provider registries) {
/* 121 */     this.explosionResistant = explosionResistant;
/* 122 */     this.enabledFeatures = enabledFeatures;
/* 123 */     this.map = map;
/* 124 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   protected <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike type, FunctionUserBuilder<T> builder) {
/* 128 */     if (!this.explosionResistant.contains(type.asItem())) {
/* 129 */       return (T)builder.apply((LootItemFunction.Builder)ApplyExplosionDecay.explosionDecay());
/*     */     }
/*     */     
/* 132 */     return (T)builder.unwrap();
/*     */   }
/*     */   
/*     */   protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike type, ConditionUserBuilder<T> builder) {
/* 136 */     if (!this.explosionResistant.contains(type.asItem())) {
/* 137 */       return (T)builder.when(ExplosionCondition.survivesExplosion());
/*     */     }
/*     */     
/* 140 */     return (T)builder.unwrap();
/*     */   }
/*     */   
/*     */   public LootTable.Builder createSingleItemTable(ItemLike drop) {
/* 144 */     return LootTable.lootTable()
/* 145 */       .withPool(applyExplosionCondition(drop, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 146 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 147 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static LootTable.Builder createSelfDropDispatchTable(Block original, LootItemCondition.Builder condition, LootPoolEntryContainer.Builder<?> entry) {
/* 152 */     return LootTable.lootTable()
/* 153 */       .withPool(LootPool.lootPool()
/* 154 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 155 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 156 */           .when(condition))
/* 157 */           .otherwise(entry)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSilkTouchDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
/* 163 */     return createSelfDropDispatchTable(original, hasSilkTouch(), entry);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
/* 167 */     return createSelfDropDispatchTable(original, hasShears(), entry);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSilkTouchOrShearsDispatchTable(Block original, LootPoolEntryContainer.Builder<?> entry) {
/* 171 */     return createSelfDropDispatchTable(original, hasShearsOrSilkTouch(), entry);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop) {
/* 175 */     return createSilkTouchDispatchTable(original, applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop)));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createSingleItemTable(ItemLike drop, NumberProvider count) {
/* 179 */     return LootTable.lootTable()
/* 180 */       .withPool(LootPool.lootPool()
/* 181 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 182 */         .add(applyExplosionDecay(drop, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(count)))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSingleItemTableWithSilkTouch(Block original, ItemLike drop, NumberProvider count) {
/* 187 */     return createSilkTouchDispatchTable(original, applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop).apply((LootItemFunction.Builder)SetItemCountFunction.setCount(count))));
/*     */   }
/*     */   
/*     */   private LootTable.Builder createSilkTouchOnlyTable(ItemLike drop) {
/* 191 */     return LootTable.lootTable()
/* 192 */       .withPool(LootPool.lootPool()
/* 193 */         .when(hasSilkTouch())
/* 194 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 195 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)));
/*     */   }
/*     */ 
/*     */   
/*     */   private LootTable.Builder createPotFlowerItemTable(ItemLike flower) {
/* 200 */     return LootTable.lootTable()
/* 201 */       .withPool(applyExplosionCondition((ItemLike)Blocks.FLOWER_POT, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 202 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 203 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.FLOWER_POT))))
/*     */       
/* 205 */       .withPool(applyExplosionCondition(flower, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 206 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 207 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(flower))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createSlabItemTable(Block slab) {
/* 212 */     return LootTable.lootTable()
/* 213 */       .withPool(LootPool.lootPool()
/* 214 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 215 */         .add(applyExplosionDecay((ItemLike)slab, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)slab)
/* 216 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)).when(
/* 217 */                 (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T extends Comparable<T> & net.minecraft.util.StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block drop, Property<T> property, T value) {
/* 224 */     return LootTable.lootTable()
/* 225 */       .withPool(applyExplosionCondition((ItemLike)drop, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 226 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 227 */           .add(LootItem.lootTableItem((ItemLike)drop)
/* 228 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(drop).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, (Comparable)value))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createNameableBlockEntityTable(Block drop) {
/* 234 */     return LootTable.lootTable()
/* 235 */       .withPool(applyExplosionCondition((ItemLike)drop, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 236 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 237 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)drop)
/* 238 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 239 */               .include(DataComponents.CUSTOM_NAME)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShulkerBoxDrop(Block shulkerBox) {
/* 245 */     return LootTable.lootTable()
/* 246 */       .withPool(applyExplosionCondition((ItemLike)shulkerBox, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 247 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 248 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)shulkerBox)
/* 249 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 250 */               .include(DataComponents.CUSTOM_NAME)
/* 251 */               .include(DataComponents.CONTAINER)
/* 252 */               .include(DataComponents.LOCK)
/* 253 */               .include(DataComponents.CONTAINER_LOOT)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCopperOreDrops(Block block) {
/* 260 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 261 */     return createSilkTouchDispatchTable(block, 
/* 262 */         applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.RAW_COPPER)
/* 263 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F)))
/* 264 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createLapisOreDrops(Block block) {
/* 270 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 271 */     return createSilkTouchDispatchTable(block, 
/* 272 */         applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI)
/* 273 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F)))
/* 274 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createRedstoneOreDrops(Block block) {
/* 280 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 281 */     return createSilkTouchDispatchTable(block, 
/* 282 */         applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.REDSTONE)
/* 283 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 5.0F)))
/* 284 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBannerDrop(Block original) {
/* 290 */     return LootTable.lootTable()
/* 291 */       .withPool(applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 292 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 293 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 294 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 295 */               .include(DataComponents.CUSTOM_NAME)
/* 296 */               .include(DataComponents.ITEM_NAME)
/* 297 */               .include(DataComponents.TOOLTIP_DISPLAY)
/* 298 */               .include(DataComponents.BANNER_PATTERNS)
/* 299 */               .include(DataComponents.RARITY)))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBeeNestDrop(Block original) {
/* 306 */     return LootTable.lootTable()
/* 307 */       .withPool(LootPool.lootPool()
/* 308 */         .when(hasSilkTouch())
/* 309 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 310 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 311 */           .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 312 */             .include(DataComponents.BEES))
/*     */           
/* 314 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(original).copy((Property)BeehiveBlock.HONEY_LEVEL))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createBeeHiveDrop(Block original) {
/* 320 */     return LootTable.lootTable()
/* 321 */       .withPool(LootPool.lootPool()
/* 322 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 323 */         .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)original)
/* 324 */           .when(hasSilkTouch()))
/* 325 */           .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 326 */             .include(DataComponents.BEES))
/*     */           
/* 328 */           .apply((LootItemFunction.Builder)CopyBlockState.copyState(original).copy((Property)BeehiveBlock.HONEY_LEVEL))
/* 329 */           .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)original))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCaveVinesDrop(Block original) {
/* 335 */     return LootTable.lootTable()
/* 336 */       .withPool(LootPool.lootPool()
/* 337 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES))
/* 338 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(original).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CaveVines.BERRIES, true))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCopperGolemStatueBlock(Block block) {
/* 343 */     return LootTable.lootTable()
/* 344 */       .withPool(applyExplosionCondition((ItemLike)block, (ConditionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 345 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 346 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)block)
/* 347 */             .apply((LootItemFunction.Builder)CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 348 */               .include(DataComponents.CUSTOM_NAME))
/* 349 */             .apply((LootItemFunction.Builder)CopyBlockState.copyState(block).copy((Property)CopperGolemStatueBlock.POSE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createOreDrop(Block original, Item drop) {
/* 355 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 356 */     return createSilkTouchDispatchTable(original, 
/* 357 */         applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)drop)
/* 358 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addOreBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMushroomBlockDrop(Block original, ItemLike drop) {
/* 364 */     return createSilkTouchDispatchTable(original, applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem(drop)
/* 365 */           .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-6.0F, 2.0F)))
/* 366 */           .apply((LootItemFunction.Builder)LimitCount.limitCount(IntRange.lowerBound(0)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createGrassDrops(Block original) {
/* 372 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 373 */     return createShearsDispatchTable(original, applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)
/* 374 */           .when(LootItemRandomChanceCondition.randomChance(0.125F)))
/* 375 */           .apply((LootItemFunction.Builder)ApplyBonusCount.addUniformBonusCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), 2))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createStemDrops(Block block, Item drop) {
/* 381 */     return LootTable.lootTable()
/* 382 */       .withPool(applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 383 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 384 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)drop)
/* 385 */             .apply(StemBlock.AGE.getPossibleValues(), age -> SetItemCountFunction.setCount((NumberProvider)BinomialDistributionGenerator.binomial(3, (age + 1) / 15.0F)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)StemBlock.AGE, age)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createAttachedStemDrops(Block block, Item drop) {
/* 391 */     return LootTable.lootTable()
/* 392 */       .withPool(applyExplosionDecay((ItemLike)block, (FunctionUserBuilder<LootPool.Builder>)LootPool.lootPool()
/* 393 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 394 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)drop)
/* 395 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShearsOnlyDrop(ItemLike drop) {
/* 401 */     return LootTable.lootTable()
/* 402 */       .withPool(LootPool.lootPool()
/* 403 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 404 */         .when(hasShears())
/* 405 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike drop) {
/* 410 */     return LootTable.lootTable()
/* 411 */       .withPool(LootPool.lootPool()
/* 412 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 413 */         .when(hasShearsOrSilkTouch())
/* 414 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem(drop)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block block, LootItemCondition.Builder condition) {
/* 419 */     return LootTable.lootTable()
/* 420 */       .withPool(LootPool.lootPool()
/* 421 */         .add(applyExplosionDecay((ItemLike)block, 
/* 422 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)block)
/* 423 */             .when(condition))
/* 424 */             .apply((Object[])Direction.values(), dir -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F), true).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MultifaceBlock.getFaceProperty(dir), true)))))
/* 425 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMultifaceBlockDrops(Block block) {
/* 431 */     return LootTable.lootTable()
/* 432 */       .withPool(LootPool.lootPool()
/* 433 */         .add(applyExplosionDecay((ItemLike)block, 
/* 434 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)block)
/* 435 */             .apply((Object[])Direction.values(), dir -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F), true).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)MultifaceBlock.getFaceProperty(dir), true)))))
/* 436 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(-1.0F), true)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMossyCarpetBlockDrops(Block block) {
/* 442 */     return LootTable.lootTable()
/* 443 */       .withPool(LootPool.lootPool()
/* 444 */         .add(applyExplosionDecay((ItemLike)block, 
/* 445 */             (FunctionUserBuilder<LootPoolEntryContainer.Builder>)LootItem.lootTableItem((ItemLike)block)
/* 446 */             .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)net.minecraft.world.level.block.MossyCarpetBlock.BASE, true))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createLeavesDrops(Block original, Block sapling, float... saplingChances) {
/* 452 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 453 */     return createSilkTouchOrShearsDispatchTable(original, ((LootPoolSingletonContainer.Builder)
/* 454 */         applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)sapling)))
/* 455 */         .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), saplingChances)))
/*     */       
/* 457 */       .withPool(LootPool.lootPool()
/* 458 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 459 */         .when(doesNotHaveShearsOrSilkTouch())
/* 460 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/* 461 */           .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createOakLeavesDrops(Block original, Block sapling, float... saplingChances) {
/* 467 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 468 */     return 
/* 469 */       createLeavesDrops(original, sapling, saplingChances)
/* 470 */       .withPool(LootPool.lootPool()
/* 471 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 472 */         .when(doesNotHaveShearsOrSilkTouch())
/* 473 */         .add(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)original, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.APPLE)))
/* 474 */           .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F }))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createMangroveLeavesDrops(Block block) {
/* 480 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 481 */     return createSilkTouchOrShearsDispatchTable(block, ((LootPoolSingletonContainer.Builder)
/* 482 */         applyExplosionDecay((ItemLike)Blocks.MANGROVE_LEAVES, (FunctionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/* 483 */         .when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), NORMAL_LEAVES_STICK_CHANCES)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCropDrops(Block original, Item cropDrop, Item seedDrop, LootItemCondition.Builder isMaxAge) {
/* 488 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 489 */     return applyExplosionDecay((ItemLike)original, (FunctionUserBuilder<LootTable.Builder>)LootTable.lootTable()
/* 490 */         .withPool(LootPool.lootPool()
/* 491 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)cropDrop)
/* 492 */             .when(isMaxAge))
/* 493 */             .otherwise((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)seedDrop))))
/*     */ 
/*     */         
/* 496 */         .withPool(LootPool.lootPool()
/* 497 */           .when(isMaxAge)
/* 498 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)seedDrop).apply((LootItemFunction.Builder)ApplyBonusCount.addBonusBinomialDistributionCount((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createDoublePlantShearsDrop(Block block) {
/* 504 */     return LootTable.lootTable().withPool(LootPool.lootPool()
/* 505 */         .when(hasShears())
/* 506 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)block).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoublePlantWithSeedDrops(Block block, Block drop) {
/* 510 */     HolderLookup.RegistryLookup<Block> blocks = this.registries.lookupOrThrow(Registries.BLOCK);
/*     */     
/* 512 */     AlternativesEntry.Builder builder = ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)drop).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))
/* 513 */       .when(hasShears()))
/* 514 */       .otherwise(((LootPoolSingletonContainer.Builder)applyExplosionCondition((ItemLike)block, (ConditionUserBuilder<LootPoolSingletonContainer.Builder>)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS)))
/* 515 */         .when(LootItemRandomChanceCondition.randomChance(0.125F)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 520 */     return LootTable.lootTable()
/* 521 */       .withPool(
/* 522 */         LootPool.lootPool()
/* 523 */         .add((LootPoolEntryContainer.Builder)builder)
/* 524 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER)))
/* 525 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)blocks, new Block[] { block }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER))), new BlockPos(0, 1, 0))))
/*     */       
/* 527 */       .withPool(
/* 528 */         LootPool.lootPool()
/* 529 */         .add((LootPoolEntryContainer.Builder)builder)
/* 530 */         .when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.UPPER)))
/* 531 */         .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)blocks, new Block[] { block }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)DoublePlantBlock.HALF, (Comparable)DoubleBlockHalf.LOWER))), new BlockPos(0, -1, 0))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected LootTable.Builder createCandleDrops(Block block) {
/* 536 */     return LootTable.lootTable()
/* 537 */       .withPool(LootPool.lootPool()
/* 538 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 539 */         .add(applyExplosionDecay((ItemLike)block, LootItem.lootTableItem((ItemLike)block)
/* 540 */             .apply(List.of(2, 3, 4), count -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(count)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CandleBlock.CANDLES, count)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootTable.Builder createSegmentedBlockDrops(Block block) {
/* 546 */     if (block instanceof SegmentableBlock) { SegmentableBlock segmentableBlock = (SegmentableBlock)block;
/* 547 */       return LootTable.lootTable()
/* 548 */         .withPool(LootPool.lootPool()
/* 549 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 550 */           .add(applyExplosionDecay((ItemLike)block, LootItem.lootTableItem((ItemLike)block)
/* 551 */               .apply(IntStream.rangeClosed(1, 4).boxed().toList(), count -> SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(count)).when((LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)segmentableBlock.getSegmentAmountProperty(), count))))))); }
/*     */ 
/*     */ 
/*     */     
/* 555 */     return noDrop();
/*     */   }
/*     */   
/*     */   protected static LootTable.Builder createCandleCakeDrops(Block candle) {
/* 559 */     return LootTable.lootTable()
/* 560 */       .withPool(LootPool.lootPool()
/* 561 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 562 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)candle)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static LootTable.Builder noDrop() {
/* 567 */     return LootTable.lootTable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 574 */     generate();
/*     */     
/* 576 */     Set<ResourceKey<LootTable>> seen = new HashSet<>();
/* 577 */     for (Iterator<Block> iterator = BuiltInRegistries.BLOCK.iterator(); iterator.hasNext(); ) { Block block = iterator.next();
/* 578 */       if (!block.isEnabled(this.enabledFeatures)) {
/*     */         continue;
/*     */       }
/* 581 */       block.getLootTable().ifPresent(lootTable -> {
/*     */             if (seen.add(seen)) {
/*     */               LootTable.Builder builder = this.map.remove(seen);
/*     */               
/*     */               if (builder == null) {
/*     */                 throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", new Object[] { seen.identifier(), BuiltInRegistries.BLOCK.getKey(seen) }));
/*     */               }
/*     */               block.accept(seen, builder);
/*     */             } 
/*     */           }); }
/*     */     
/* 592 */     if (!this.map.isEmpty()) {
/* 593 */       throw new IllegalStateException("Created block loot tables for non-blocks: " + String.valueOf(this.map.keySet()));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addNetherVinesDropTable(Block vineBlock, Block plantBlock) {
/* 598 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 599 */     LootTable.Builder builder = createSilkTouchOrShearsDispatchTable(vineBlock, 
/* 600 */         LootItem.lootTableItem((ItemLike)vineBlock).when(BonusLevelTableCondition.bonusLevelFlatChance((Holder)enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.33F, 0.55F, 0.77F, 1.0F })));
/* 601 */     add(vineBlock, builder);
/* 602 */     add(plantBlock, builder);
/*     */   }
/*     */   
/*     */   protected LootTable.Builder createDoorTable(Block block) {
/* 606 */     return createSinglePropConditionTable(block, (Property<DoubleBlockHalf>)DoorBlock.HALF, DoubleBlockHalf.LOWER);
/*     */   }
/*     */   
/*     */   protected void dropPottedContents(Block potted) {
/* 610 */     add(potted, block -> createPotFlowerItemTable((ItemLike)((FlowerPotBlock)block).getPotted()));
/*     */   }
/*     */   
/*     */   protected void otherWhenSilkTouch(Block block, Block other) {
/* 614 */     add(block, createSilkTouchOnlyTable((ItemLike)other));
/*     */   }
/*     */   
/*     */   protected void dropOther(Block block, ItemLike drop) {
/* 618 */     add(block, createSingleItemTable(drop));
/*     */   }
/*     */   
/*     */   protected void dropWhenSilkTouch(Block block) {
/* 622 */     otherWhenSilkTouch(block, block);
/*     */   }
/*     */   
/*     */   protected void dropSelf(Block block) {
/* 626 */     dropOther(block, (ItemLike)block);
/*     */   }
/*     */   
/*     */   protected void add(Block block, Function<Block, LootTable.Builder> builder) {
/* 630 */     add(block, builder.apply(block));
/*     */   }
/*     */   
/*     */   protected void add(Block block, LootTable.Builder builder) {
/* 634 */     this.map.put((ResourceKey<LootTable>)block.getLootTable().orElseThrow(() -> new IllegalStateException("Block " + String.valueOf(block) + " does not have loot table")), builder);
/*     */   }
/*     */   
/*     */   protected abstract void generate();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/loot/BlockLootSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */