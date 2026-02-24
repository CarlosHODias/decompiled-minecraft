/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.criterion.BlockPredicate;
/*     */ import net.minecraft.advancements.criterion.ChanneledLightningTrigger;
/*     */ import net.minecraft.advancements.criterion.DamagePredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.DataComponentMatchers;
/*     */ import net.minecraft.advancements.criterion.DistancePredicate;
/*     */ import net.minecraft.advancements.criterion.DistanceTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityEquipmentPredicate;
/*     */ import net.minecraft.advancements.criterion.EntityPredicate;
/*     */ import net.minecraft.advancements.criterion.EntitySubPredicate;
/*     */ import net.minecraft.advancements.criterion.FallAfterExplosionTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemUsedOnLocationTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledByArrowTrigger;
/*     */ import net.minecraft.advancements.criterion.KilledTrigger;
/*     */ import net.minecraft.advancements.criterion.LightningBoltPredicate;
/*     */ import net.minecraft.advancements.criterion.LightningStrikeTrigger;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.LootTableTrigger;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.advancements.criterion.PlayerHurtEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerInteractTrigger;
/*     */ import net.minecraft.advancements.criterion.PlayerPredicate;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.RecipeCraftedTrigger;
/*     */ import net.minecraft.advancements.criterion.ShotCrossbowTrigger;
/*     */ import net.minecraft.advancements.criterion.SlideDownBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.SpearMobsTrigger;
/*     */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.criterion.SummonedEntityTrigger;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.advancements.criterion.TargetBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.TradeTrigger;
/*     */ import net.minecraft.advancements.criterion.UsedTotemTrigger;
/*     */ import net.minecraft.advancements.criterion.UsingItemTrigger;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicates;
/*     */ import net.minecraft.core.component.predicates.JukeboxPlayablePredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ComparatorBlock;
/*     */ import net.minecraft.world.level.block.CopperBulbBlock;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
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
/*     */ public class VanillaAdventureAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/* 143 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DISTANCE_FROM_BOTTOM_TO_TOP = 384;
/*     */   
/*     */   private static final int Y_COORDINATE_AT_TOP = 320;
/*     */   
/*     */   private static final int Y_COORDINATE_AT_BOTTOM = -64;
/*     */   
/*     */   private static final int BEDROCK_THICKNESS = 5;
/* 152 */   private static final Map<MobCategory, Set<EntityType<?>>> EXCEPTIONS_BY_EXPECTED_CATEGORIES = Map.of(MobCategory.MONSTER, 
/* 153 */       Set.of(EntityType.GIANT, EntityType.ILLUSIONER, EntityType.WARDEN));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   private static final List<EntityType<?>> MOBS_TO_KILL = Arrays.asList((EntityType<?>[])new EntityType[] { EntityType.BLAZE, EntityType.BOGGED, EntityType.BREEZE, EntityType.CAMEL_HUSK, EntityType.CAVE_SPIDER, EntityType.CREAKING, EntityType.CREEPER, EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PARCHED, EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WITHER, EntityType.ZOGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_NAUTILUS });
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
/*     */   
/*     */   private static Criterion<LightningStrikeTrigger.TriggerInstance> fireCountAndBystander(MinMaxBounds.Ints fireCount, Optional<EntityPredicate> bystander) {
/* 206 */     return LightningStrikeTrigger.TriggerInstance.lightningStrike(
/* 207 */         Optional.of(EntityPredicate.Builder.entity()
/* 208 */           .distance(DistancePredicate.absolute(MinMaxBounds.Doubles.atMost(30.0D)))
/* 209 */           .subPredicate((EntitySubPredicate)LightningBoltPredicate.blockSetOnFire(fireCount))
/* 210 */           .build()), bystander);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Criterion<UsingItemTrigger.TriggerInstance> lookAtThroughItem(EntityPredicate.Builder lookingAt, ItemPredicate.Builder with) {
/* 216 */     return UsingItemTrigger.TriggerInstance.lookingAt(
/* 217 */         EntityPredicate.Builder.entity().subPredicate(
/* 218 */           (EntitySubPredicate)PlayerPredicate.Builder.player().setLookingAt(lookingAt)
/*     */           
/* 220 */           .build()), with);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/* 228 */     HolderLookup.RegistryLookup registryLookup1 = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/* 229 */     HolderLookup.RegistryLookup registryLookup2 = registries.lookupOrThrow(Registries.ITEM);
/* 230 */     HolderLookup.RegistryLookup registryLookup3 = registries.lookupOrThrow(Registries.BLOCK);
/*     */     
/* 232 */     AdvancementHolder root = Advancement.Builder.advancement()
/* 233 */       .display((ItemLike)Items.MAP, (Component)Component.translatable("advancements.adventure.root.title"), (Component)Component.translatable("advancements.adventure.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"), AdvancementType.TASK, false, false, false)
/* 234 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 235 */       .addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity())
/* 236 */       .addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer())
/* 237 */       .save(output, "adventure/root");
/*     */     
/* 239 */     AdvancementHolder sleepInBed = Advancement.Builder.advancement()
/* 240 */       .parent(root)
/* 241 */       .display((ItemLike)Blocks.RED_BED, (Component)Component.translatable("advancements.adventure.sleep_in_bed.title"), (Component)Component.translatable("advancements.adventure.sleep_in_bed.description"), null, AdvancementType.TASK, true, true, false)
/* 242 */       .addCriterion("slept_in_bed", PlayerTrigger.TriggerInstance.sleptInBed())
/* 243 */       .save(output, "adventure/sleep_in_bed");
/*     */     
/* 245 */     createAdventuringTime(registries, output, sleepInBed, MultiNoiseBiomeSourceParameterList.Preset.OVERWORLD);
/*     */     
/* 247 */     AdvancementHolder trade = Advancement.Builder.advancement()
/* 248 */       .parent(root)
/* 249 */       .display((ItemLike)Items.EMERALD, (Component)Component.translatable("advancements.adventure.trade.title"), (Component)Component.translatable("advancements.adventure.trade.description"), null, AdvancementType.TASK, true, true, false)
/* 250 */       .addCriterion("traded", TradeTrigger.TriggerInstance.tradedWithVillager())
/* 251 */       .save(output, "adventure/trade");
/*     */     
/* 253 */     Advancement.Builder.advancement()
/* 254 */       .parent(trade)
/* 255 */       .display((ItemLike)Items.EMERALD, (Component)Component.translatable("advancements.adventure.trade_at_world_height.title"), (Component)Component.translatable("advancements.adventure.trade_at_world_height.description"), null, AdvancementType.TASK, true, true, false)
/* 256 */       .addCriterion("trade_at_world_height", TradeTrigger.TriggerInstance.tradedWithVillager(EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(319.0D)))))
/* 257 */       .save(output, "adventure/trade_at_world_height");
/*     */     
/* 259 */     AdvancementHolder killAMob = createMonsterHunterAdvancement(root, output, (HolderGetter<EntityType<?>>)registryLookup1, validateMobsToKill(MOBS_TO_KILL, (HolderLookup<EntityType<?>>)registryLookup1));
/*     */     
/* 261 */     AdvancementHolder shootArrow = Advancement.Builder.advancement()
/* 262 */       .parent(killAMob)
/* 263 */       .display((ItemLike)Items.BOW, (Component)Component.translatable("advancements.adventure.shoot_arrow.title"), (Component)Component.translatable("advancements.adventure.shoot_arrow.description"), null, AdvancementType.TASK, true, true, false)
/* 264 */       .addCriterion("shot_arrow", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.ARROWS)))))
/* 265 */       .save(output, "adventure/shoot_arrow");
/*     */     
/* 267 */     AdvancementHolder throwTrident = Advancement.Builder.advancement()
/* 268 */       .parent(killAMob)
/* 269 */       .display((ItemLike)Items.TRIDENT, (Component)Component.translatable("advancements.adventure.throw_trident.title"), (Component)Component.translatable("advancements.adventure.throw_trident.description"), null, AdvancementType.TASK, true, true, false)
/* 270 */       .addCriterion("shot_trident", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.TRIDENT)))))
/* 271 */       .save(output, "adventure/throw_trident");
/*     */     
/* 273 */     Advancement.Builder.advancement()
/* 274 */       .parent(throwTrident)
/* 275 */       .display((ItemLike)Items.TRIDENT, (Component)Component.translatable("advancements.adventure.very_very_frightening.title"), (Component)Component.translatable("advancements.adventure.very_very_frightening.description"), null, AdvancementType.TASK, true, true, false)
/* 276 */       .addCriterion("struck_villager", ChanneledLightningTrigger.TriggerInstance.channeledLightning(new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.VILLAGER)
/* 277 */           })).save(output, "adventure/very_very_frightening");
/*     */     
/* 279 */     Advancement.Builder.advancement()
/* 280 */       .parent(trade)
/* 281 */       .display((ItemLike)Blocks.CARVED_PUMPKIN, (Component)Component.translatable("advancements.adventure.summon_iron_golem.title"), (Component)Component.translatable("advancements.adventure.summon_iron_golem.description"), null, AdvancementType.GOAL, true, true, false)
/* 282 */       .addCriterion("summoned_golem", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.IRON_GOLEM)))
/* 283 */       .save(output, "adventure/summon_iron_golem");
/*     */     
/* 285 */     Advancement.Builder.advancement()
/* 286 */       .parent(shootArrow)
/* 287 */       .display((ItemLike)Items.ARROW, (Component)Component.translatable("advancements.adventure.sniper_duel.title"), (Component)Component.translatable("advancements.adventure.sniper_duel.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 288 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 289 */       .addCriterion("killed_skeleton", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.SKELETON).distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(50.0D))), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))))
/* 290 */       .save(output, "adventure/sniper_duel");
/*     */     
/* 292 */     Advancement.Builder.advancement()
/* 293 */       .parent(killAMob)
/* 294 */       .display((ItemLike)Items.TOTEM_OF_UNDYING, (Component)Component.translatable("advancements.adventure.totem_of_undying.title"), (Component)Component.translatable("advancements.adventure.totem_of_undying.description"), null, AdvancementType.GOAL, true, true, false)
/* 295 */       .addCriterion("used_totem", UsedTotemTrigger.TriggerInstance.usedTotem((HolderGetter)registryLookup2, (ItemLike)Items.TOTEM_OF_UNDYING))
/* 296 */       .save(output, "adventure/totem_of_undying");
/*     */     
/* 298 */     Advancement.Builder.advancement()
/* 299 */       .parent(killAMob)
/* 300 */       .display((ItemLike)Items.IRON_SPEAR, (Component)Component.translatable("advancements.adventure.spear_many_mobs.title"), (Component)Component.translatable("advancements.adventure.spear_many_mobs.description"), null, AdvancementType.GOAL, true, true, false)
/* 301 */       .addCriterion("spear_many_mobs", SpearMobsTrigger.TriggerInstance.spearMobs(5))
/* 302 */       .save(output, "adventure/spear_many_mobs");
/*     */     
/* 304 */     AdvancementHolder olBetsy = Advancement.Builder.advancement()
/* 305 */       .parent(root)
/* 306 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.ol_betsy.title"), (Component)Component.translatable("advancements.adventure.ol_betsy.description"), null, AdvancementType.TASK, true, true, false)
/* 307 */       .addCriterion("shot_crossbow", ShotCrossbowTrigger.TriggerInstance.shotCrossbow((HolderGetter)registryLookup2, (ItemLike)Items.CROSSBOW))
/* 308 */       .save(output, "adventure/ol_betsy");
/*     */     
/* 310 */     Advancement.Builder.advancement()
/* 311 */       .parent(olBetsy)
/* 312 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.whos_the_pillager_now.title"), (Component)Component.translatable("advancements.adventure.whos_the_pillager_now.description"), null, AdvancementType.TASK, true, true, false)
/* 313 */       .addCriterion("kill_pillager", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PILLAGER)
/* 314 */           })).save(output, "adventure/whos_the_pillager_now");
/*     */     
/* 316 */     Advancement.Builder.advancement()
/* 317 */       .parent(olBetsy)
/* 318 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.two_birds_one_arrow.title"), (Component)Component.translatable("advancements.adventure.two_birds_one_arrow.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 319 */       .rewards(AdvancementRewards.Builder.experience(65))
/* 320 */       .addCriterion("two_birds", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, new EntityPredicate.Builder[] { EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PHANTOM), EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PHANTOM)
/* 321 */           })).save(output, "adventure/two_birds_one_arrow");
/*     */     
/* 323 */     Advancement.Builder.advancement()
/* 324 */       .parent(olBetsy)
/* 325 */       .display((ItemLike)Items.CROSSBOW, (Component)Component.translatable("advancements.adventure.arbalistic.title"), (Component)Component.translatable("advancements.adventure.arbalistic.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 326 */       .rewards(AdvancementRewards.Builder.experience(85))
/* 327 */       .addCriterion("arbalistic", KilledByArrowTrigger.TriggerInstance.crossbowKilled((HolderGetter)registryLookup2, MinMaxBounds.Ints.exactly(5)))
/* 328 */       .save(output, "adventure/arbalistic");
/*     */     
/* 330 */     HolderLookup.RegistryLookup<BannerPattern> patternLookup = registries.lookupOrThrow(Registries.BANNER_PATTERN);
/* 331 */     AdvancementHolder raidOmen = Advancement.Builder.advancement()
/* 332 */       .parent(root)
/* 333 */       .display(Raid.getOminousBannerInstance((HolderGetter)patternLookup), (Component)Component.translatable("advancements.adventure.voluntary_exile.title"), (Component)Component.translatable("advancements.adventure.voluntary_exile.description"), null, AdvancementType.TASK, true, true, true)
/* 334 */       .addCriterion("voluntary_exile", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.RAIDERS).equipment(EntityEquipmentPredicate.captainPredicate((HolderGetter)registryLookup2, (HolderGetter)patternLookup))))
/* 335 */       .save(output, "adventure/voluntary_exile");
/*     */     
/* 337 */     Advancement.Builder.advancement()
/* 338 */       .parent(raidOmen)
/* 339 */       .display(Raid.getOminousBannerInstance((HolderGetter)patternLookup), (Component)Component.translatable("advancements.adventure.hero_of_the_village.title"), (Component)Component.translatable("advancements.adventure.hero_of_the_village.description"), null, AdvancementType.CHALLENGE, true, true, true)
/* 340 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 341 */       .addCriterion("hero_of_the_village", PlayerTrigger.TriggerInstance.raidWon())
/* 342 */       .save(output, "adventure/hero_of_the_village");
/*     */     
/* 344 */     Advancement.Builder.advancement()
/* 345 */       .parent(root)
/* 346 */       .display((ItemLike)Blocks.HONEY_BLOCK.asItem(), (Component)Component.translatable("advancements.adventure.honey_block_slide.title"), (Component)Component.translatable("advancements.adventure.honey_block_slide.description"), null, AdvancementType.TASK, true, true, false)
/* 347 */       .addCriterion("honey_block_slide", SlideDownBlockTrigger.TriggerInstance.slidesDownBlock(Blocks.HONEY_BLOCK))
/* 348 */       .save(output, "adventure/honey_block_slide");
/*     */     
/* 350 */     Advancement.Builder.advancement()
/* 351 */       .parent(shootArrow)
/* 352 */       .display((ItemLike)Blocks.TARGET.asItem(), (Component)Component.translatable("advancements.adventure.bullseye.title"), (Component)Component.translatable("advancements.adventure.bullseye.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 353 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 354 */       .addCriterion("bullseye", TargetBlockTrigger.TriggerInstance.targetHit(MinMaxBounds.Ints.exactly(15), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().distance(DistancePredicate.horizontal(MinMaxBounds.Doubles.atLeast(30.0D)))))))
/* 355 */       .save(output, "adventure/bullseye");
/*     */     
/* 357 */     Advancement.Builder.advancement()
/* 358 */       .parent(sleepInBed)
/* 359 */       .display((ItemLike)Items.LEATHER_BOOTS, (Component)Component.translatable("advancements.adventure.walk_on_powder_snow_with_leather_boots.title"), (Component)Component.translatable("advancements.adventure.walk_on_powder_snow_with_leather_boots.description"), null, AdvancementType.TASK, true, true, false)
/* 360 */       .addCriterion("walk_on_powder_snow_with_leather_boots", PlayerTrigger.TriggerInstance.walkOnBlockWithEquipment((HolderGetter)registryLookup3, (HolderGetter)registryLookup2, Blocks.POWDER_SNOW, Items.LEATHER_BOOTS))
/* 361 */       .save(output, "adventure/walk_on_powder_snow_with_leather_boots");
/*     */     
/* 363 */     Advancement.Builder.advancement()
/* 364 */       .parent(root)
/* 365 */       .display((ItemLike)Items.LIGHTNING_ROD, (Component)Component.translatable("advancements.adventure.lightning_rod_with_villager_no_fire.title"), (Component)Component.translatable("advancements.adventure.lightning_rod_with_villager_no_fire.description"), null, AdvancementType.TASK, true, true, false)
/* 366 */       .addCriterion("lightning_rod_with_villager_no_fire", fireCountAndBystander(MinMaxBounds.Ints.exactly(0), Optional.of(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.VILLAGER).build())))
/* 367 */       .save(output, "adventure/lightning_rod_with_villager_no_fire");
/*     */     
/* 369 */     AdvancementHolder isItABird = Advancement.Builder.advancement()
/* 370 */       .parent(root)
/* 371 */       .display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_parrot.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_parrot.description"), null, AdvancementType.TASK, true, true, false)
/* 372 */       .addCriterion("spyglass_at_parrot", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PARROT), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS
/* 373 */             }))).save(output, "adventure/spyglass_at_parrot");
/*     */     
/* 375 */     AdvancementHolder isItABalloon = Advancement.Builder.advancement()
/* 376 */       .parent(isItABird)
/* 377 */       .display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_ghast.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_ghast.description"), null, AdvancementType.TASK, true, true, false)
/* 378 */       .addCriterion("spyglass_at_ghast", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.GHAST), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS
/* 379 */             }))).save(output, "adventure/spyglass_at_ghast");
/*     */     
/* 381 */     Advancement.Builder.advancement()
/* 382 */       .parent(sleepInBed)
/* 383 */       .display((ItemLike)Items.JUKEBOX, (Component)Component.translatable("advancements.adventure.play_jukebox_in_meadows.title"), (Component)Component.translatable("advancements.adventure.play_jukebox_in_meadows.description"), null, AdvancementType.TASK, true, true, false)
/* 384 */       .addCriterion("play_jukebox_in_meadows", 
/* 385 */         ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
/* 386 */           LocationPredicate.Builder.location()
/* 387 */           .setBiomes((HolderSet)HolderSet.direct(new Holder[] { (Holder)registries.lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.MEADOW)
/* 388 */               })).setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.JUKEBOX
/* 389 */               })), ItemPredicate.Builder.item()
/* 390 */           .withComponents(DataComponentMatchers.Builder.components().partial(DataComponentPredicates.JUKEBOX_PLAYABLE, (DataComponentPredicate)JukeboxPlayablePredicate.any()).build())))
/*     */ 
/*     */       
/* 393 */       .save(output, "adventure/play_jukebox_in_meadows");
/*     */     
/* 395 */     Advancement.Builder.advancement()
/* 396 */       .parent(isItABalloon)
/* 397 */       .display((ItemLike)Items.SPYGLASS, (Component)Component.translatable("advancements.adventure.spyglass_at_dragon.title"), (Component)Component.translatable("advancements.adventure.spyglass_at_dragon.description"), null, AdvancementType.TASK, true, true, false)
/* 398 */       .addCriterion("spyglass_at_dragon", lookAtThroughItem(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.ENDER_DRAGON), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.SPYGLASS
/* 399 */             }))).save(output, "adventure/spyglass_at_dragon");
/*     */     
/* 401 */     Advancement.Builder.advancement()
/* 402 */       .parent(root)
/* 403 */       .display((ItemLike)Items.WATER_BUCKET, (Component)Component.translatable("advancements.adventure.fall_from_world_height.title"), (Component)Component.translatable("advancements.adventure.fall_from_world_height.description"), null, AdvancementType.TASK, true, true, false)
/* 404 */       .addCriterion("fall_from_world_height", 
/* 405 */         DistanceTrigger.TriggerInstance.fallFromHeight(
/* 406 */           EntityPredicate.Builder.entity().located(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(-59.0D))), 
/* 407 */           DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(379.0D)), 
/* 408 */           LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atLeast(319.0D))))
/*     */ 
/*     */       
/* 411 */       .save(output, "adventure/fall_from_world_height");
/*     */     
/* 413 */     Advancement.Builder.advancement()
/* 414 */       .parent(killAMob)
/* 415 */       .display((ItemLike)Blocks.SCULK_CATALYST, (Component)Component.translatable("advancements.adventure.kill_mob_near_sculk_catalyst.title"), (Component)Component.translatable("advancements.adventure.kill_mob_near_sculk_catalyst.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 416 */       .addCriterion("kill_mob_near_sculk_catalyst", KilledTrigger.TriggerInstance.playerKilledEntityNearSculkCatalyst())
/* 417 */       .save(output, "adventure/kill_mob_near_sculk_catalyst");
/*     */     
/* 419 */     Advancement.Builder.advancement()
/* 420 */       .parent(root)
/* 421 */       .display((ItemLike)Blocks.SCULK_SENSOR, (Component)Component.translatable("advancements.adventure.avoid_vibration.title"), (Component)Component.translatable("advancements.adventure.avoid_vibration.description"), null, AdvancementType.TASK, true, true, false)
/* 422 */       .addCriterion("avoid_vibration", PlayerTrigger.TriggerInstance.avoidVibration())
/* 423 */       .save(output, "adventure/avoid_vibration");
/*     */     
/* 425 */     AdvancementHolder respectingTheRemnants = respectingTheRemnantsCriterions((HolderGetter<Item>)registryLookup2, Advancement.Builder.advancement())
/* 426 */       .parent(root)
/* 427 */       .display((ItemLike)Items.BRUSH, (Component)Component.translatable("advancements.adventure.salvage_sherd.title"), (Component)Component.translatable("advancements.adventure.salvage_sherd.description"), null, AdvancementType.TASK, true, true, false)
/* 428 */       .save(output, "adventure/salvage_sherd");
/*     */     
/* 430 */     Advancement.Builder.advancement()
/* 431 */       .parent(respectingTheRemnants)
/* 432 */       .display(
/* 433 */         DecoratedPotBlockEntity.createDecoratedPotItem(new PotDecorations(Optional.empty(), Optional.of(Items.HEART_POTTERY_SHERD), Optional.empty(), Optional.of(Items.EXPLORER_POTTERY_SHERD))), 
/* 434 */         (Component)Component.translatable("advancements.adventure.craft_decorated_pot_using_only_sherds.title"), 
/* 435 */         (Component)Component.translatable("advancements.adventure.craft_decorated_pot_using_only_sherds.description"), null, AdvancementType.TASK, true, true, false)
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 442 */       .addCriterion("pot_crafted_using_only_sherds", 
/* 443 */         RecipeCraftedTrigger.TriggerInstance.craftedItem(
/* 444 */           ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace("decorated_pot")), 
/* 445 */           List.of(
/* 446 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 447 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 448 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS), 
/* 449 */             ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, ItemTags.DECORATED_POT_SHERDS))))
/*     */ 
/*     */ 
/*     */       
/* 453 */       .save(output, "adventure/craft_decorated_pot_using_only_sherds");
/*     */     
/* 455 */     AdvancementHolder craftingANewLook = craftingANewLook(Advancement.Builder.advancement())
/* 456 */       .parent(root)
/* 457 */       .display(new ItemStack((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE), (Component)Component.translatable("advancements.adventure.trim_with_any_armor_pattern.title"), (Component)Component.translatable("advancements.adventure.trim_with_any_armor_pattern.description"), null, AdvancementType.TASK, true, true, false)
/* 458 */       .save(output, "adventure/trim_with_any_armor_pattern");
/*     */     
/* 460 */     smithingWithStyle(Advancement.Builder.advancement())
/* 461 */       .parent(craftingANewLook)
/* 462 */       .display(new ItemStack((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE), (Component)Component.translatable("advancements.adventure.trim_with_all_exclusive_armor_patterns.title"), (Component)Component.translatable("advancements.adventure.trim_with_all_exclusive_armor_patterns.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 463 */       .rewards(AdvancementRewards.Builder.experience(150))
/* 464 */       .save(output, "adventure/trim_with_all_exclusive_armor_patterns");
/*     */     
/* 466 */     Advancement.Builder.advancement()
/* 467 */       .parent(root)
/* 468 */       .display((ItemLike)Items.CHISELED_BOOKSHELF, (Component)Component.translatable("advancements.adventure.read_power_from_chiseled_bookshelf.title"), (Component)Component.translatable("advancements.adventure.read_power_from_chiseled_bookshelf.description"), null, AdvancementType.TASK, true, true, false)
/* 469 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 470 */       .addCriterion("chiseled_bookshelf", placedBlockReadByComparator((HolderGetter<Block>)registryLookup3, Blocks.CHISELED_BOOKSHELF))
/* 471 */       .addCriterion("comparator", placedComparatorReadingBlock((HolderGetter<Block>)registryLookup3, Blocks.CHISELED_BOOKSHELF))
/* 472 */       .save(output, "adventure/read_power_of_chiseled_bookshelf");
/*     */     
/* 474 */     Advancement.Builder.advancement()
/* 475 */       .parent(root)
/* 476 */       .display((ItemLike)Items.ARMADILLO_SCUTE, (Component)Component.translatable("advancements.adventure.brush_armadillo.title"), (Component)Component.translatable("advancements.adventure.brush_armadillo.description"), null, AdvancementType.TASK, true, true, false)
/* 477 */       .addCriterion("brush_armadillo", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
/* 478 */           ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.BRUSH
/* 479 */             }), Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.ARMADILLO)))))
/*     */       
/* 481 */       .save(output, "adventure/brush_armadillo");
/*     */     
/* 483 */     AdvancementHolder trialsEdition = Advancement.Builder.advancement()
/* 484 */       .parent(root)
/* 485 */       .display((ItemLike)Blocks.CHISELED_TUFF, (Component)Component.translatable("advancements.adventure.minecraft_trials_edition.title"), (Component)Component.translatable("advancements.adventure.minecraft_trials_edition.description"), null, AdvancementType.TASK, true, true, false)
/* 486 */       .addCriterion("minecraft_trials_edition", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.TRIAL_CHAMBERS))))
/* 487 */       .save(output, "adventure/minecraft_trials_edition");
/*     */     
/* 489 */     Advancement.Builder.advancement()
/* 490 */       .parent(trialsEdition)
/* 491 */       .display((ItemLike)Items.COPPER_BULB, (Component)Component.translatable("advancements.adventure.lighten_up.title"), (Component)Component.translatable("advancements.adventure.lighten_up.description"), null, AdvancementType.TASK, true, true, false)
/* 492 */       .addCriterion("lighten_up", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.OXIDIZED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB, Blocks.EXPOSED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)CopperBulbBlock.LIT, true))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, (ItemLike[])VanillaHusbandryAdvancements.WAX_SCRAPING_TOOLS)))
/* 493 */       .save(output, "adventure/lighten_up");
/*     */     
/* 495 */     AdvancementHolder underLockAndKey = Advancement.Builder.advancement()
/* 496 */       .parent(trialsEdition)
/* 497 */       .display((ItemLike)Items.TRIAL_KEY, (Component)Component.translatable("advancements.adventure.under_lock_and_key.title"), (Component)Component.translatable("advancements.adventure.under_lock_and_key.description"), null, AdvancementType.TASK, true, true, false)
/* 498 */       .addCriterion("under_lock_and_key", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.VAULT }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)VaultBlock.OMINOUS, false))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.TRIAL_KEY
/* 499 */             }))).save(output, "adventure/under_lock_and_key");
/*     */     
/* 501 */     Advancement.Builder.advancement()
/* 502 */       .parent(underLockAndKey)
/* 503 */       .display((ItemLike)Items.OMINOUS_TRIAL_KEY, (Component)Component.translatable("advancements.adventure.revaulting.title"), (Component)Component.translatable("advancements.adventure.revaulting.description"), null, AdvancementType.GOAL, true, true, false)
/* 504 */       .addCriterion("revaulting", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.VAULT }).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty((Property)VaultBlock.OMINOUS, true))), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.OMINOUS_TRIAL_KEY
/* 505 */             }))).save(output, "adventure/revaulting");
/*     */     
/* 507 */     Advancement.Builder.advancement()
/* 508 */       .parent(trialsEdition)
/* 509 */       .display((ItemLike)Items.WIND_CHARGE, (Component)Component.translatable("advancements.adventure.blowback.title"), (Component)Component.translatable("advancements.adventure.blowback.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 510 */       .rewards(AdvancementRewards.Builder.experience(40))
/* 511 */       .addCriterion("blowback", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.BREEZE), DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.BREEZE_WIND_CHARGE))))
/* 512 */       .save(output, "adventure/blowback");
/*     */     
/* 514 */     Advancement.Builder.advancement()
/* 515 */       .parent(root)
/* 516 */       .display((ItemLike)Items.CRAFTER, (Component)Component.translatable("advancements.adventure.crafters_crafting_crafters.title"), (Component)Component.translatable("advancements.adventure.crafters_crafting_crafters.description"), null, AdvancementType.TASK, true, true, false)
/* 517 */       .addCriterion("crafter_crafted_crafter", RecipeCraftedTrigger.TriggerInstance.crafterCraftedItem(ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace("crafter"))))
/* 518 */       .save(output, "adventure/crafters_crafting_crafters");
/*     */     
/* 520 */     Advancement.Builder.advancement()
/* 521 */       .parent(root)
/* 522 */       .display((ItemLike)Items.LODESTONE, (Component)Component.translatable("advancements.adventure.use_lodestone.title"), (Component)Component.translatable("advancements.adventure.use_lodestone.description"), null, AdvancementType.TASK, true, true, false)
/* 523 */       .addCriterion("use_lodestone", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of((HolderGetter)registryLookup3, new Block[] { Blocks.LODESTONE })), ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.COMPASS
/* 524 */             }))).save(output, "adventure/use_lodestone");
/*     */     
/* 526 */     Advancement.Builder.advancement()
/* 527 */       .parent(trialsEdition)
/* 528 */       .display((ItemLike)Items.WIND_CHARGE, (Component)Component.translatable("advancements.adventure.who_needs_rockets.title"), (Component)Component.translatable("advancements.adventure.who_needs_rockets.description"), null, AdvancementType.TASK, true, true, false)
/* 529 */       .addCriterion("who_needs_rockets", FallAfterExplosionTrigger.TriggerInstance.fallAfterExplosion(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(7.0D)), EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.WIND_CHARGE)))
/* 530 */       .save(output, "adventure/who_needs_rockets");
/*     */     
/* 532 */     Advancement.Builder.advancement()
/* 533 */       .parent(trialsEdition)
/* 534 */       .display((ItemLike)Items.MACE, (Component)Component.translatable("advancements.adventure.overoverkill.title"), (Component)Component.translatable("advancements.adventure.overoverkill.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 535 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 536 */       .addCriterion("overoverkill", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(DamagePredicate.Builder.damageInstance().dealtDamage(MinMaxBounds.Doubles.atLeast(100.0D)).type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_MACE_SMASH)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityType.PLAYER).equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of((HolderGetter)registryLookup2, new ItemLike[] { (ItemLike)Items.MACE
/* 537 */                     }))))))).save(output, "adventure/overoverkill");
/*     */     
/* 539 */     Advancement.Builder.advancement()
/* 540 */       .parent(root)
/* 541 */       .display((ItemLike)Blocks.CREAKING_HEART, (Component)Component.translatable("advancements.adventure.heart_transplanter.title"), (Component)Component.translatable("advancements.adventure.heart_transplanter.description"), null, AdvancementType.TASK, true, true, false)
/* 542 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 543 */       .addCriterion("place_creaking_heart_dormant", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.CREAKING_HEART, (Property)BlockStateProperties.CREAKING_HEART_STATE, (Comparable)CreakingHeartState.DORMANT))
/* 544 */       .addCriterion("place_creaking_heart_awake", ItemUsedOnLocationTrigger.TriggerInstance.placedBlockWithProperties(Blocks.CREAKING_HEART, (Property)BlockStateProperties.CREAKING_HEART_STATE, (Comparable)CreakingHeartState.AWAKE))
/* 545 */       .addCriterion("place_pale_oak_log", placedBlockActivatesCreakingHeart((HolderGetter<Block>)registryLookup3, BlockTags.PALE_OAK_LOGS))
/* 546 */       .save(output, "adventure/heart_transplanter");
/*     */   }
/*     */   
/*     */   public static AdvancementHolder createMonsterHunterAdvancement(AdvancementHolder parent, Consumer<AdvancementHolder> output, HolderGetter<EntityType<?>> entityTypes, List<EntityType<?>> mobsToKill) {
/* 550 */     AdvancementHolder killAMob = addMobsToKill(Advancement.Builder.advancement(), entityTypes, mobsToKill)
/* 551 */       .parent(parent)
/* 552 */       .display((ItemLike)Items.IRON_SWORD, (Component)Component.translatable("advancements.adventure.kill_a_mob.title"), (Component)Component.translatable("advancements.adventure.kill_a_mob.description"), null, AdvancementType.TASK, true, true, false)
/* 553 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 554 */       .save(output, "adventure/kill_a_mob");
/*     */     
/* 556 */     addMobsToKill(Advancement.Builder.advancement(), entityTypes, mobsToKill)
/* 557 */       .parent(killAMob)
/* 558 */       .display((ItemLike)Items.DIAMOND_SWORD, (Component)Component.translatable("advancements.adventure.kill_all_mobs.title"), (Component)Component.translatable("advancements.adventure.kill_all_mobs.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 559 */       .rewards(AdvancementRewards.Builder.experience(100))
/* 560 */       .save(output, "adventure/kill_all_mobs");
/*     */     
/* 562 */     return killAMob;
/*     */   }
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockReadByComparator(HolderGetter<Block> blocks, Block block) {
/* 566 */     LootItemCondition.Builder[] conditions = (LootItemCondition.Builder[])ComparatorBlock.FACING.getPossibleValues().stream().map(direction -> {
/*     */           StatePropertiesPredicate.Builder comparatorProperties = StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComparatorBlock.FACING, (Comparable)direction);
/*     */           
/*     */           BlockPredicate.Builder comparatorTest = BlockPredicate.Builder.block().of(blocks, new Block[] { Blocks.COMPARATOR }).setProperties(comparatorProperties);
/*     */           return LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(comparatorTest), new BlockPos(direction.getOpposite().getUnitVec3i()));
/* 571 */         }).toArray(x$0 -> new LootItemCondition.Builder[x$0]);
/*     */     
/* 573 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] {
/* 574 */           (LootItemCondition.Builder)LootItemBlockStatePropertyCondition.hasBlockStateProperties(block), 
/* 575 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(conditions)
/*     */         });
/*     */   }
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedComparatorReadingBlock(HolderGetter<Block> blocks, Block block) {
/* 580 */     LootItemCondition.Builder[] conditions = (LootItemCondition.Builder[])ComparatorBlock.FACING.getPossibleValues().stream().map(direction -> {
/*     */           StatePropertiesPredicate.Builder comparatorProperties = StatePropertiesPredicate.Builder.properties().hasProperty((Property)ComparatorBlock.FACING, (Comparable)direction);
/*     */           LootItemBlockStatePropertyCondition.Builder comparatorTest = new LootItemBlockStatePropertyCondition.Builder(Blocks.COMPARATOR).setProperties(comparatorProperties);
/*     */           LootItemCondition.Builder blockTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { block })), new BlockPos(direction.getUnitVec3i()));
/*     */           return AllOfCondition.allOf(new LootItemCondition.Builder[] { (LootItemCondition.Builder)comparatorTest, blockTest });
/* 585 */         }).toArray(x$0 -> new LootItemCondition.Builder[x$0]);
/*     */     
/* 587 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] {
/* 588 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(conditions)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockActivatesCreakingHeart(HolderGetter<Block> blocks, TagKey<Block> block) {
/* 597 */     LootItemCondition.Builder[] conditions = (LootItemCondition.Builder[])Stream.<Direction>of(Direction.values())
/* 598 */       .map(direction -> {
/*     */           StatePropertiesPredicate.Builder creakingHeartProperties = StatePropertiesPredicate.Builder.properties().hasProperty((Property)CreakingHeartBlock.AXIS, (Comparable)direction.getAxis());
/*     */           
/*     */           BlockPredicate.Builder placedPaleOakLogBlock = BlockPredicate.Builder.block().of(blocks, block).setProperties(creakingHeartProperties);
/*     */           
/*     */           Vec3i blockOffset = direction.getUnitVec3i();
/*     */           
/*     */           LootItemCondition.Builder placedPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock)), creakingHeartBlockTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, new Block[] { Blocks.CREAKING_HEART }).setProperties(creakingHeartProperties)), new BlockPos(blockOffset)), existingPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock), new BlockPos(blockOffset.multiply(2)));
/*     */           
/*     */           return AllOfCondition.allOf(new LootItemCondition.Builder[] { placedPaleOakLogTest, creakingHeartBlockTest, existingPaleOakLogTest });
/* 608 */         }).toArray(x$0 -> new LootItemCondition.Builder[x$0]);
/*     */     
/* 610 */     return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(new LootItemCondition.Builder[] {
/* 611 */           (LootItemCondition.Builder)AnyOfCondition.anyOf(conditions)
/*     */         });
/*     */   }
/*     */   
/*     */   private static Advancement.Builder smithingWithStyle(Advancement.Builder advancement) {
/* 616 */     advancement.requirements(AdvancementRequirements.Strategy.AND);
/*     */     
/* 618 */     Set<Item> required = Set.of(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE);
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
/* 629 */     VanillaRecipeProvider.smithingTrims().filter(trim -> required.contains(trim.template())).forEach(trimTemplate -> advancement.addCriterion("armor_trimmed_" + String.valueOf(trimTemplate.recipeId().identifier()), RecipeCraftedTrigger.TriggerInstance.craftedItem(trimTemplate.recipeId())));
/*     */ 
/*     */ 
/*     */     
/* 633 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder craftingANewLook(Advancement.Builder advancement) {
/* 637 */     advancement.requirements(AdvancementRequirements.Strategy.OR);
/*     */     
/* 639 */     VanillaRecipeProvider.smithingTrims().map(VanillaRecipeProvider.TrimTemplate::recipeId).forEach(recipeId -> advancement.addCriterion("armor_trimmed_" + String.valueOf(recipeId.identifier()), RecipeCraftedTrigger.TriggerInstance.craftedItem(recipeId)));
/*     */ 
/*     */ 
/*     */     
/* 643 */     return advancement;
/*     */   }
/*     */   
/*     */   private static Advancement.Builder respectingTheRemnantsCriterions(HolderGetter<Item> items, Advancement.Builder advancement) {
/* 647 */     List<Pair<String, Criterion<LootTableTrigger.TriggerInstance>>> lootCriteria = List.of(
/* 648 */         Pair.of("desert_pyramid", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY)), 
/* 649 */         Pair.of("desert_well", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY)), 
/* 650 */         Pair.of("ocean_ruin_cold", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)), 
/* 651 */         Pair.of("ocean_ruin_warm", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)), 
/* 652 */         Pair.of("trail_ruins_rare", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)), 
/* 653 */         Pair.of("trail_ruins_common", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON)));
/*     */     
/* 655 */     lootCriteria.forEach(p -> advancement.addCriterion((String)p.getFirst(), (Criterion)p.getSecond()));
/*     */     
/* 657 */     String hasSherdCriterion = "has_sherd";
/* 658 */     advancement.addCriterion("has_sherd", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(items, ItemTags.DECORATED_POT_SHERDS) }));
/*     */     
/* 660 */     advancement.requirements(new AdvancementRequirements(List.of(
/* 661 */             lootCriteria.stream().map(Pair::getFirst).toList(), 
/* 662 */             List.of("has_sherd"))));
/*     */ 
/*     */     
/* 665 */     return advancement;
/*     */   }
/*     */   
/*     */   protected static void createAdventuringTime(HolderLookup.Provider registries, Consumer<AdvancementHolder> output, AdvancementHolder sleepInBed, MultiNoiseBiomeSourceParameterList.Preset preset) {
/* 669 */     addBiomes(Advancement.Builder.advancement(), registries, preset.usedBiomes().toList())
/* 670 */       .parent(sleepInBed)
/* 671 */       .display((ItemLike)Items.DIAMOND_BOOTS, (Component)Component.translatable("advancements.adventure.adventuring_time.title"), (Component)Component.translatable("advancements.adventure.adventuring_time.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 672 */       .rewards(AdvancementRewards.Builder.experience(500))
/* 673 */       .save(output, "adventure/adventuring_time");
/*     */   }
/*     */   
/*     */   private static Advancement.Builder addMobsToKill(Advancement.Builder advancement, HolderGetter<EntityType<?>> entityTypes, List<EntityType<?>> mobsToKill) {
/* 677 */     mobsToKill.forEach(mob -> advancement.addCriterion(BuiltInRegistries.ENTITY_TYPE.getKey(mob).toString(), KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityTypes, mob))));
/* 678 */     return advancement;
/*     */   }
/*     */   
/*     */   protected static Advancement.Builder addBiomes(Advancement.Builder advancement, HolderLookup.Provider registries, List<ResourceKey<Biome>> explorableBiomes) {
/* 682 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.BIOME);
/* 683 */     for (ResourceKey<Biome> biome : explorableBiomes) {
/* 684 */       advancement.addCriterion(biome.identifier().toString(), PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome((Holder)registryLookup.getOrThrow(biome))));
/*     */     }
/* 686 */     return advancement;
/*     */   }
/*     */   
/*     */   private static List<EntityType<?>> validateMobsToKill(List<EntityType<?>> data, HolderLookup<EntityType<?>> entityTypes) {
/* 690 */     List<String> errors = new ArrayList<>();
/* 691 */     Set<? extends EntityType<?>> mobsToKill = Set.copyOf(data);
/*     */ 
/*     */     
/* 694 */     Set<MobCategory> specifiedCategories = (Set<MobCategory>)mobsToKill.stream().map(EntityType::getCategory).collect(Collectors.toSet());
/* 695 */     Sets.SetView setView1 = Sets.symmetricDifference(
/* 696 */         EXCEPTIONS_BY_EXPECTED_CATEGORIES.keySet(), specifiedCategories);
/*     */ 
/*     */     
/* 699 */     if (!setView1.isEmpty()) {
/* 700 */       errors.add("Found EntityType with MobCategory only in either expected exceptions or kill_all_mobs advancement: " + (String)
/* 701 */           setView1.stream().map(Object::toString).sorted().collect(Collectors.joining(", ")));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 706 */     Sets.SetView setView2 = Sets.intersection((Set)
/* 707 */         EXCEPTIONS_BY_EXPECTED_CATEGORIES.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()), mobsToKill);
/*     */ 
/*     */ 
/*     */     
/* 711 */     if (!setView2.isEmpty()) {
/* 712 */       errors.add("Found EntityType in both expected exceptions and kill_all_mobs advancement: " + (String)
/* 713 */           setView2.stream().map(Object::toString).sorted().collect(Collectors.joining(", ")));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 720 */     Objects.requireNonNull(mobsToKill); Map<MobCategory, Set<EntityType<?>>> doNotKillByCategory = (Map<MobCategory, Set<EntityType<?>>>)entityTypes.listElements().map(Holder.Reference::value).filter(Predicate.not(mobsToKill::contains))
/* 721 */       .collect(Collectors.groupingBy(EntityType::getCategory, Collectors.toSet()));
/*     */     
/* 723 */     EXCEPTIONS_BY_EXPECTED_CATEGORIES.forEach((exceptedCategory, exceptedTypes) -> {
/*     */           Sets.SetView setView = Sets.difference((Set)doNotKillByCategory.getOrDefault(exceptedCategory, Set.of()), exceptedTypes);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           if (!setView.isEmpty()) {
/*     */             errors.add(String.format(Locale.ROOT, "Found (new?) EntityType with MobCategory %s which are in neither expected exceptions nor kill_all_mobs advancement: %s", new Object[] { exceptedCategory, setView.stream().map(Object::toString).sorted().collect(Collectors.joining(", ")) }));
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 737 */     if (!errors.isEmpty()) {
/* 738 */       Objects.requireNonNull(LOGGER); errors.forEach(LOGGER::error);
/* 739 */       throw new IllegalStateException("Found inconsistencies with kill_all_mobs advancement");
/*     */     } 
/*     */     
/* 742 */     return data;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/advancements/packs/VanillaAdventureAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */