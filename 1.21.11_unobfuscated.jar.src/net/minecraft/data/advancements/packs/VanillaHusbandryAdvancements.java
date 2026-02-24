/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.criterion.BeeNestDestroyedTrigger;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.BredAnimalsTrigger;
/*     */ import net.minecraft.advancements.criterion.ConsumeItemTrigger;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.EffectsChangedTrigger;
/*     */ import net.minecraft.advancements.criterion.EnchantmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.FilledBucketTrigger;
/*     */ import net.minecraft.advancements.criterion.FishingRodHookedTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.PickedUpItemTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.StartRidingTrigger;
/*     */ import net.minecraft.advancements.criterion.TameAnimalTrigger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.EnchantmentsPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VanillaHusbandryAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/*  76 */   public static final List<EntityType<?>> BREEDABLE_ANIMALS = List.of((EntityType<?>[])new EntityType[] { EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.SHEEP, EntityType.COW, EntityType.MOOSHROOM, EntityType.PIG, EntityType.CHICKEN, EntityType.WOLF, EntityType.OCELOT, EntityType.RABBIT, EntityType.LLAMA, EntityType.CAT, EntityType.PANDA, EntityType.FOX, EntityType.BEE, EntityType.HOGLIN, EntityType.STRIDER, EntityType.GOAT, EntityType.AXOLOTL, EntityType.CAMEL, EntityType.ARMADILLO, EntityType.NAUTILUS });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static final List<EntityType<?>> INDIRECTLY_BREEDABLE_ANIMALS = List.of(EntityType.TURTLE, EntityType.FROG, EntityType.SNIFFER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private static final Item[] FISH = new Item[] { Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.SALMON };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private static final Item[] FISH_BUCKETS = new Item[] { Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   private static final Item[] EDIBLE_ITEMS = new Item[] { Items.APPLE, Items.MUSHROOM_STEW, Items.BREAD, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKIE, Items.MELON_SLICE, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.ROTTEN_FLESH, Items.SPIDER_EYE, Items.CARROT, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.GOLDEN_CARROT, Items.PUMPKIN_PIE, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, Items.MUTTON, Items.COOKED_MUTTON, Items.CHORUS_FRUIT, Items.BEETROOT, Items.BEETROOT_SOUP, Items.DRIED_KELP, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.HONEY_BOTTLE, Items.GLOW_BERRIES };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public static final Item[] WAX_SCRAPING_TOOLS = new Item[] { Items.WOODEN_AXE, Items.GOLDEN_AXE, Items.STONE_AXE, Items.COPPER_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Comparator<Holder.Reference<?>> HOLDER_KEY_COMPARATOR;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 176 */     HOLDER_KEY_COMPARATOR = Comparator.comparing(e -> e.key().identifier());
/*     */   }
/*     */   
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/* 180 */     HolderLookup.RegistryLookup registryLookup1 = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/* 181 */     HolderLookup.RegistryLookup registryLookup2 = registries.lookupOrThrow(Registries.ITEM);
/* 182 */     HolderLookup.RegistryLookup registryLookup3 = registries.lookupOrThrow(Registries.BLOCK);
/* 183 */     HolderLookup.RegistryLookup registryLookup4 = registries.lookupOrThrow(Registries.FROG_VARIANT);
/* 184 */     HolderLookup.RegistryLookup registryLookup5 = registries.lookupOrThrow(Registries.CAT_VARIANT);
/* 185 */     HolderLookup.RegistryLookup registryLookup6 = registries.lookupOrThrow(Registries.WOLF_VARIANT);
/*     */     
/* 187 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 188 */     AdvancementHolder root = Advancement.Builder.advancement()
/* 189 */       .display((ItemLike)Blocks.HAY_BLOCK, (Component)Component.translatable("advancements.husbandry.root.title"), (Component)Component.translatable("advancements.husbandry.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/husbandry"), AdvancementType.TASK, false, false, false)
/* 190 */       .addCriterion("consumed_item", ConsumeItemTrigger.TriggerInstance.usedItem())
/* 191 */       .save(output, "husbandry/root");
/*     */     
/* 193 */     AdvancementHolder plantSeed = Advancement.Builder.advancement()
/* 194 */       .parent(root)
/* 195 */       .display((ItemLike)Items.WHEAT, (Component)Component.translatable("advancements.husbandry.plant_seed.title"), (Component)Component.translatable("advancements.husbandry.plant_seed.description"), null, AdvancementType.TASK, true, true, false)
/* 196 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 197 */       .addCriterion("wheat", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.WHEAT))
/* 198 */       .addCriterion("pumpkin_stem", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM))
/* 199 */       .addCriterion("melon_stem", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM))
/* 200 */       .addCriterion("beetroots", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS))
/* 201 */       .addCriterion("nether_wart", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART))
/* 202 */       .addCriterion("torchflower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.TORCHFLOWER_CROP))
/* 203 */       .addCriterion("pitcher_pod", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PITCHER_CROP))
/* 204 */       .save(output, "husbandry/plant_seed");
/*     */     
/* 206 */     AdvancementHolder breedAnAnimalAdvancement = Advancement.Builder.advancement()
/* 207 */       .parent(root)
/* 208 */       .display((ItemLike)Items.WHEAT, (Component)Component.translatable("advancements.husbandry.breed_an_animal.title"), (Component)Component.translatable("advancements.husbandry.breed_an_animal.description"), null, AdvancementType.TASK, true, true, false)
/* 209 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 210 */       .addCriterion("bred", BredAnimalsTrigger.TriggerInstance.bredAnimals())
/* 211 */       .save(output, "husbandry/breed_an_animal");
/*     */     
/* 213 */     createBreedAllAnimalsAdvancement(breedAnAnimalAdvancement, output, (HolderGetter<EntityType<?>>)registryLookup1, BREEDABLE_ANIMALS.stream(), INDIRECTLY_BREEDABLE_ANIMALS.stream());
/*     */     
/* 215 */     addFood(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2)
/* 216 */       .parent(plantSeed)
/* 217 */       .display((ItemLike)Items.APPLE, (Component)Component.translatable("advancements.husbandry.balanced_diet.title"), (Component)Component.translatable("advancements.husbandry.balanced_diet.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 218 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 219 */       .save(output, "husbandry/balanced_diet");
/*     */     
/* 221 */     Advancement.Builder.advancement()
/* 222 */       .parent(plantSeed)
/* 223 */       .display((ItemLike)Items.NETHERITE_HOE, (Component)Component.translatable("advancements.husbandry.netherite_hoe.title"), (Component)Component.translatable("advancements.husbandry.netherite_hoe.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 224 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 225 */       .addCriterion("netherite_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.NETHERITE_HOE
/* 226 */           })).save(output, "husbandry/obtain_netherite_hoe");
/*     */     
/* 228 */     AdvancementHolder tameAnAnimal = Advancement.Builder.advancement()
/* 229 */       .parent(root)
/* 230 */       .display((ItemLike)Items.LEAD, (Component)Component.translatable("advancements.husbandry.tame_an_animal.title"), (Component)Component.translatable("advancements.husbandry.tame_an_animal.description"), null, AdvancementType.TASK, true, true, false)
/* 231 */       .addCriterion("tamed_animal", TameAnimalTrigger.TriggerInstance.tamedAnimal())
/* 232 */       .save(output, "husbandry/tame_an_animal");
/*     */     
/* 234 */     AdvancementHolder fishyBusiness = addFish(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2)
/* 235 */       .parent(root)
/* 236 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 237 */       .display((ItemLike)Items.FISHING_ROD, (Component)Component.translatable("advancements.husbandry.fishy_business.title"), (Component)Component.translatable("advancements.husbandry.fishy_business.description"), null, AdvancementType.TASK, true, true, false)
/* 238 */       .save(output, "husbandry/fishy_business");
/*     */     
/* 240 */     AdvancementHolder tacticalFishing = addFishBuckets(Advancement.Builder.advancement(), (HolderGetter<Item>)registryLookup2)
/* 241 */       .parent(fishyBusiness)
/* 242 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 243 */       .display((ItemLike)Items.PUFFERFISH_BUCKET, (Component)Component.translatable("advancements.husbandry.tactical_fishing.title"), (Component)Component.translatable("advancements.husbandry.tactical_fishing.description"), null, AdvancementType.TASK, true, true, false)
/* 244 */       .save(output, "husbandry/tactical_fishing");
/*     */     
/* 246 */     AdvancementHolder theCutestPredetor = Advancement.Builder.advancement()
/* 247 */       .parent(tacticalFishing)
/* 248 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 249 */       .addCriterion(BuiltInRegistries.ITEM.getKey(Items.AXOLOTL_BUCKET).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.AXOLOTL_BUCKET
/* 250 */             }))).display((ItemLike)Items.AXOLOTL_BUCKET, (Component)Component.translatable("advancements.husbandry.axolotl_in_a_bucket.title"), (Component)Component.translatable("advancements.husbandry.axolotl_in_a_bucket.description"), null, AdvancementType.TASK, true, true, false)
/* 251 */       .save(output, "husbandry/axolotl_in_a_bucket");
/*     */     
/* 253 */     Advancement.Builder.advancement()
/* 254 */       .parent(theCutestPredetor)
/* 255 */       .addCriterion("kill_axolotl_target", EffectsChangedTrigger.TriggerInstance.gotEffectsFrom(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.AXOLOTL)))
/* 256 */       .display((ItemLike)Items.TROPICAL_FISH_BUCKET, (Component)Component.translatable("advancements.husbandry.kill_axolotl_target.title"), (Component)Component.translatable("advancements.husbandry.kill_axolotl_target.description"), null, AdvancementType.TASK, true, true, false)
/* 257 */       .save(output, "husbandry/kill_axolotl_target");
/*     */     
/* 259 */     addCatVariants(Advancement.Builder.advancement(), (HolderLookup<CatVariant>)registryLookup5)
/* 260 */       .parent(tameAnAnimal)
/* 261 */       .display((ItemLike)Items.COD, (Component)Component.translatable("advancements.husbandry.complete_catalogue.title"), (Component)Component.translatable("advancements.husbandry.complete_catalogue.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 262 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 263 */       .save(output, "husbandry/complete_catalogue");
/*     */     
/* 265 */     addTamedWolfVariants(Advancement.Builder.advancement(), (HolderLookup<WolfVariant>)registryLookup6)
/* 266 */       .parent(tameAnAnimal)
/* 267 */       .display((ItemLike)Items.BONE, (Component)Component.translatable("advancements.husbandry.whole_pack.title"), (Component)Component.translatable("advancements.husbandry.whole_pack.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 268 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 269 */       .save(output, "husbandry/whole_pack");
/*     */     
/* 271 */     AdvancementHolder safelyHarvestHoney = Advancement.Builder.advancement()
/* 272 */       .parent(root)
/* 273 */       .addCriterion("safely_harvest_honey", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, BlockTags.BEEHIVES)).setSmokey(true), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.GLASS_BOTTLE
/* 274 */             }))).display((ItemLike)Items.HONEY_BOTTLE, (Component)Component.translatable("advancements.husbandry.safely_harvest_honey.title"), (Component)Component.translatable("advancements.husbandry.safely_harvest_honey.description"), null, AdvancementType.TASK, true, true, false)
/* 275 */       .save(output, "husbandry/safely_harvest_honey");
/*     */     
/* 277 */     AdvancementHolder waxOn = Advancement.Builder.advancement()
/* 278 */       .parent(safelyHarvestHoney)
/* 279 */       .display((ItemLike)Items.HONEYCOMB, (Component)Component.translatable("advancements.husbandry.wax_on.title"), (Component)Component.translatable("advancements.husbandry.wax_on.description"), null, AdvancementType.TASK, true, true, false)
/* 280 */       .addCriterion("wax_on", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, ((BiMap)HoneycombItem.WAXABLES.get()).keySet())), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.HONEYCOMB
/* 281 */             }))).save(output, "husbandry/wax_on");
/*     */     
/* 283 */     Advancement.Builder.advancement()
/* 284 */       .parent(waxOn)
/* 285 */       .display((ItemLike)Items.STONE_AXE, (Component)Component.translatable("advancements.husbandry.wax_off.title"), (Component)Component.translatable("advancements.husbandry.wax_off.description"), null, AdvancementType.TASK, true, true, false)
/* 286 */       .addCriterion("wax_off", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, ((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).keySet())), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, (ItemLike[])WAX_SCRAPING_TOOLS)))
/* 287 */       .save(output, "husbandry/wax_off");
/*     */     
/* 289 */     AdvancementHolder tadpoleInABucket = Advancement.Builder.advancement()
/* 290 */       .parent(root)
/* 291 */       .addCriterion(BuiltInRegistries.ITEM.getKey(Items.TADPOLE_BUCKET).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.TADPOLE_BUCKET
/* 292 */             }))).display((ItemLike)Items.TADPOLE_BUCKET, (Component)Component.translatable("advancements.husbandry.tadpole_in_a_bucket.title"), (Component)Component.translatable("advancements.husbandry.tadpole_in_a_bucket.description"), null, AdvancementType.TASK, true, true, false)
/* 293 */       .save(output, "husbandry/tadpole_in_a_bucket");
/*     */     
/* 295 */     AdvancementHolder allFrogsOnALeash = addLeashedFrogVariants((HolderGetter<EntityType<?>>)registryLookup1, (HolderGetter<Item>)registryLookup2, (HolderLookup<FrogVariant>)registryLookup4, Advancement.Builder.advancement())
/* 296 */       .parent(tadpoleInABucket)
/* 297 */       .display((ItemLike)Items.LEAD, (Component)Component.translatable("advancements.husbandry.leash_all_frog_variants.title"), (Component)Component.translatable("advancements.husbandry.leash_all_frog_variants.description"), null, AdvancementType.TASK, true, true, false)
/* 298 */       .save(output, "husbandry/leash_all_frog_variants");
/*     */     
/* 300 */     Advancement.Builder.advancement()
/* 301 */       .parent(allFrogsOnALeash)
/* 302 */       .display((ItemLike)Items.VERDANT_FROGLIGHT, (Component)Component.translatable("advancements.husbandry.froglights.title"), (Component)Component.translatable("advancements.husbandry.froglights.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 303 */       .addCriterion("froglights", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.OCHRE_FROGLIGHT, (ItemLike)Items.PEARLESCENT_FROGLIGHT, (ItemLike)Items.VERDANT_FROGLIGHT
/* 304 */           })).save(output, "husbandry/froglights");
/*     */     
/* 306 */     Advancement.Builder.advancement()
/* 307 */       .parent(root)
/* 308 */       .addCriterion("silk_touch_nest", 
/* 309 */         BeeNestDestroyedTrigger.TriggerInstance.destroyedBeeNest(Blocks.BEE_NEST, 
/*     */           
/* 311 */           ItemPredicate.Builder.item()
/* 312 */           .withComponents(DataComponentMatchers.Builder.components()
/* 313 */             .partial(DataComponentPredicates.ENCHANTMENTS, (DataComponentPredicate)EnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate((Holder)enchantments.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))).build()), 
/*     */           
/* 315 */           MinMaxBounds.Ints.exactly(3)))
/*     */ 
/*     */       
/* 318 */       .display((ItemLike)Blocks.BEE_NEST, (Component)Component.translatable("advancements.husbandry.silk_touch_nest.title"), (Component)Component.translatable("advancements.husbandry.silk_touch_nest.description"), null, AdvancementType.TASK, true, true, false)
/* 319 */       .save(output, "husbandry/silk_touch_nest");
/*     */     
/* 321 */     Advancement.Builder.advancement()
/* 322 */       .parent(root)
/* 323 */       .display((ItemLike)Items.OAK_BOAT, (Component)Component.translatable("advancements.husbandry.ride_a_boat_with_a_goat.title"), (Component)Component.translatable("advancements.husbandry.ride_a_boat_with_a_goat.description"), null, AdvancementType.TASK, true, true, false)
/* 324 */       .addCriterion("ride_a_boat_with_a_goat", 
/* 325 */         StartRidingTrigger.TriggerInstance.playerStartsRiding(
/* 326 */           EntityPredicate.Builder.entity().vehicle(
/* 327 */             EntityPredicate.Builder.entity()
/* 328 */             .of((HolderGetter)registryLookup1, EntityTypeTags.BOAT)
/* 329 */             .passenger(
/* 330 */               EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.GOAT)))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 335 */       .save(output, "husbandry/ride_a_boat_with_a_goat");
/*     */     
/* 337 */     Advancement.Builder.advancement()
/* 338 */       .parent(root)
/* 339 */       .display((ItemLike)Items.GLOW_INK_SAC, (Component)Component.translatable("advancements.husbandry.make_a_sign_glow.title"), (Component)Component.translatable("advancements.husbandry.make_a_sign_glow.description"), null, AdvancementType.TASK, true, true, false)
/* 340 */       .addCriterion("make_a_sign_glow", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, BlockTags.ALL_SIGNS)), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.GLOW_INK_SAC
/* 341 */             }))).save(output, "husbandry/make_a_sign_glow");
/*     */     
/* 343 */     AdvancementHolder itemDeliveredToPlayer = Advancement.Builder.advancement()
/* 344 */       .parent(root)
/* 345 */       .display((ItemLike)Items.COOKIE, (Component)Component.translatable("advancements.husbandry.allay_deliver_item_to_player.title"), (Component)Component.translatable("advancements.husbandry.allay_deliver_item_to_player.description"), null, AdvancementType.TASK, true, true, true)
/* 346 */       .addCriterion("allay_deliver_item_to_player", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.ALLAY)))))
/* 347 */       .save(output, "husbandry/allay_deliver_item_to_player");
/*     */     
/* 349 */     Advancement.Builder.advancement()
/* 350 */       .parent(itemDeliveredToPlayer)
/* 351 */       .display((ItemLike)Items.NOTE_BLOCK, (Component)Component.translatable("advancements.husbandry.allay_deliver_cake_to_note_block.title"), (Component)Component.translatable("advancements.husbandry.allay_deliver_cake_to_note_block.description"), null, AdvancementType.TASK, true, true, true)
/* 352 */       .addCriterion("allay_deliver_cake_to_note_block", ItemUsedOnLocationTrigger.TriggerInstance.allayDropItemOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.NOTE_BLOCK })), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.CAKE
/* 353 */             }))).save(output, "husbandry/allay_deliver_cake_to_note_block");
/*     */     
/* 355 */     AdvancementHolder obtainSnifferEgg = Advancement.Builder.advancement()
/* 356 */       .parent(root)
/* 357 */       .display((ItemLike)Items.SNIFFER_EGG, (Component)Component.translatable("advancements.husbandry.obtain_sniffer_egg.title"), (Component)Component.translatable("advancements.husbandry.obtain_sniffer_egg.description"), null, AdvancementType.TASK, true, true, true)
/* 358 */       .addCriterion("obtain_sniffer_egg", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.SNIFFER_EGG
/* 359 */           })).save(output, "husbandry/obtain_sniffer_egg");
/*     */     
/* 361 */     AdvancementHolder feedSnifflet = Advancement.Builder.advancement()
/* 362 */       .parent(obtainSnifferEgg)
/* 363 */       .display((ItemLike)Items.TORCHFLOWER_SEEDS, (Component)Component.translatable("advancements.husbandry.feed_snifflet.title"), (Component)Component.translatable("advancements.husbandry.feed_snifflet.description"), null, AdvancementType.TASK, true, true, true)
/* 364 */       .addCriterion("feed_snifflet", 
/* 365 */         PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 366 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.SNIFFER_FOOD), 
/* 367 */           Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.SNIFFER).flags(EntityFlagsPredicate.Builder.flags().setIsBaby(true))))))
/*     */ 
/*     */       
/* 370 */       .save(output, "husbandry/feed_snifflet");
/*     */     
/* 372 */     Advancement.Builder.advancement()
/* 373 */       .parent(feedSnifflet)
/* 374 */       .display((ItemLike)Items.PITCHER_POD, (Component)Component.translatable("advancements.husbandry.plant_any_sniffer_seed.title"), (Component)Component.translatable("advancements.husbandry.plant_any_sniffer_seed.description"), null, AdvancementType.TASK, true, true, true)
/* 375 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 376 */       .addCriterion("torchflower", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.TORCHFLOWER_CROP))
/* 377 */       .addCriterion("pitcher_pod", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(Blocks.PITCHER_CROP))
/* 378 */       .save(output, "husbandry/plant_any_sniffer_seed");
/*     */     
/* 380 */     Advancement.Builder.advancement()
/* 381 */       .parent(tameAnAnimal)
/* 382 */       .display((ItemLike)Items.SHEARS, (Component)Component.translatable("advancements.husbandry.remove_wolf_armor.title"), (Component)Component.translatable("advancements.husbandry.remove_wolf_armor.description"), null, AdvancementType.TASK, true, true, false)
/* 383 */       .addCriterion("remove_wolf_armor", PlayerInteractTrigger.TriggerInstance.equipmentSheared(
/* 384 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WOLF_ARMOR
/* 385 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WOLF)))))
/* 386 */       .save(output, "husbandry/remove_wolf_armor");
/*     */     
/* 388 */     Advancement.Builder.advancement()
/* 389 */       .parent(tameAnAnimal)
/* 390 */       .display((ItemLike)Items.WOLF_ARMOR, (Component)Component.translatable("advancements.husbandry.repair_wolf_armor.title"), (Component)Component.translatable("advancements.husbandry.repair_wolf_armor.description"), null, AdvancementType.TASK, true, true, false)
/* 391 */       .addCriterion("repair_wolf_armor", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 392 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.ARMADILLO_SCUTE
/* 393 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WOLF).equipment(
/* 394 */                 EntityEquipmentPredicate.Builder.equipment().body(
/* 395 */                   ItemPredicate.Builder.item()
/* 396 */                   .of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.WOLF_ARMOR
/* 397 */                     }).withComponents(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.DAMAGE, 0)).build())))))))
/*     */ 
/*     */ 
/*     */       
/* 401 */       .save(output, "husbandry/repair_wolf_armor");
/*     */     
/* 403 */     Advancement.Builder.advancement()
/* 404 */       .parent(root)
/* 405 */       .display((ItemLike)Items.DRIED_GHAST, (Component)Component.translatable("advancements.husbandry.place_dried_ghast_in_water.title"), (Component)Component.translatable("advancements.husbandry.place_dried_ghast_in_water.description"), null, AdvancementType.TASK, true, true, false)
/* 406 */       .addCriterion("place_dried_ghast_in_water", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.DRIED_GHAST, (Property)BlockStateProperties.WATERLOGGED, true))
/* 407 */       .save(output, "husbandry/place_dried_ghast_in_water");
/*     */   }
/*     */   
/*     */   public static AdvancementHolder createBreedAllAnimalsAdvancement(AdvancementHolder parent, Consumer<AdvancementHolder> output, HolderGetter<EntityType<?>> entityTypes, Stream<EntityType<?>> breedable, Stream<EntityType<?>> indirectlyBreedable) {
/* 411 */     return addBreedable(Advancement.Builder.advancement(), breedable, entityTypes, indirectlyBreedable)
/* 412 */       .parent(parent)
/* 413 */       .display((ItemLike)Items.GOLDEN_CARROT, (Component)Component.translatable("advancements.husbandry.breed_all_animals.title"), (Component)Component.translatable("advancements.husbandry.breed_all_animals.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 414 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 415 */       .save(output, "husbandry/bred_all_animals");
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addLeashedFrogVariants(HolderGetter<EntityType<?>> entityTypes, HolderGetter<Item> items, HolderLookup<FrogVariant> frogVariants, Advancement.Builder advancement) {
/* 419 */     sortedVariants(frogVariants).forEach(frogVariant -> advancement.addCriterion(frogVariant.key().identifier().toString(), PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)Items.LEAD }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityType.FROG).components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.FROG_VARIANT, frogVariant)).build()))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 432 */     return advancement;
/*     */   }
/*     */   
/*     */   private static <T> Stream<Holder.Reference<T>> sortedVariants(HolderLookup<T> variants) {
/* 436 */     return (Stream)variants.listElements().sorted(HOLDER_KEY_COMPARATOR);
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFood(Advancement.Builder advancement, HolderGetter<Item> items) {
/* 440 */     for (Item food : EDIBLE_ITEMS) {
/* 441 */       advancement.addCriterion(BuiltInRegistries.ITEM.getKey(food).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(items, (ItemLike)food));
/*     */     }
/* 443 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addBreedable(Advancement.Builder advancement, Stream<EntityType<?>> breedable, HolderGetter<EntityType<?>> entityTypes, Stream<EntityType<?>> indirectlyBreedable) {
/* 447 */     breedable.forEach(animal -> advancement.addCriterion(EntityType.getKey(animal).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityTypes, animal))));
/*     */ 
/*     */     
/* 450 */     indirectlyBreedable.forEach(animal -> advancement.addCriterion(EntityType.getKey(animal).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(Optional.of(EntityPredicate.Builder.entity().of(entityTypes, animal).build()), Optional.of(EntityPredicate.Builder.entity().of(entityTypes, animal).build()), Optional.empty())));
/*     */ 
/*     */     
/* 453 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFishBuckets(Advancement.Builder advancement, HolderGetter<Item> items) {
/* 457 */     for (Item bucket : FISH_BUCKETS) {
/* 458 */       advancement.addCriterion(BuiltInRegistries.ITEM.getKey(bucket).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)bucket })));
/*     */     } 
/* 460 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addFish(Advancement.Builder advancement, HolderGetter<Item> items) {
/* 464 */     for (Item fish : FISH) {
/* 465 */       advancement.addCriterion(BuiltInRegistries.ITEM.getKey(fish).getPath(), FishingRodHookedTrigger.TriggerInstance.fishedItem(Optional.empty(), Optional.empty(), Optional.of(ItemPredicate.Builder.item().of(items, new ItemLike[] { (ItemLike)fish }).build())));
/*     */     } 
/* 467 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addCatVariants(Advancement.Builder advancement, HolderLookup<CatVariant> catVariants) {
/* 471 */     sortedVariants(catVariants).forEach(v -> advancement.addCriterion(v.key().identifier().toString(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.CAT_VARIANT, v)).build()))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addTamedWolfVariants(Advancement.Builder advancement, HolderLookup<WolfVariant> wolfVariants) {
/* 484 */     sortedVariants(wolfVariants).forEach(v -> advancement.addCriterion(v.key().identifier().toString(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().components(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.expect(DataComponents.WOLF_VARIANT, v)).build()))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 493 */     return advancement;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/advancements/packs/VanillaHusbandryAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */