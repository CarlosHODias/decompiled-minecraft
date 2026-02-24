/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.criterion.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.criterion.CuredZombieVillagerTrigger;
/*     */ import net.minecraft.advancements.criterion.DamagePredicate;
/*     */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.criterion.EnchantedItemTrigger;
/*     */ import net.minecraft.advancements.criterion.EntityHurtPlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.LocationPredicate;
/*     */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*     */ import net.minecraft.advancements.criterion.TagPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ 
/*     */ public class VanillaStoryAdvancements
/*     */   implements AdvancementSubProvider
/*     */ {
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/*  38 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.ITEM);
/*     */     
/*  40 */     AdvancementHolder root = Advancement.Builder.advancement()
/*  41 */       .display((ItemLike)Blocks.GRASS_BLOCK, (Component)Component.translatable("advancements.story.root.title"), (Component)Component.translatable("advancements.story.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"), AdvancementType.TASK, false, false, false)
/*  42 */       .addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.CRAFTING_TABLE
/*  43 */           })).save(output, "story/root");
/*     */     
/*  45 */     AdvancementHolder mineStone = Advancement.Builder.advancement()
/*  46 */       .parent(root)
/*  47 */       .display((ItemLike)Items.WOODEN_PICKAXE, (Component)Component.translatable("advancements.story.mine_stone.title"), (Component)Component.translatable("advancements.story.mine_stone.description"), null, AdvancementType.TASK, true, true, false)
/*  48 */       .addCriterion("get_stone", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of((HolderGetter)registryLookup, ItemTags.STONE_TOOL_MATERIALS)
/*  49 */           })).save(output, "story/mine_stone");
/*     */     
/*  51 */     AdvancementHolder upgradeTools = Advancement.Builder.advancement()
/*  52 */       .parent(mineStone)
/*  53 */       .display((ItemLike)Items.STONE_PICKAXE, (Component)Component.translatable("advancements.story.upgrade_tools.title"), (Component)Component.translatable("advancements.story.upgrade_tools.description"), null, AdvancementType.TASK, true, true, false)
/*  54 */       .addCriterion("stone_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.STONE_PICKAXE
/*  55 */           })).save(output, "story/upgrade_tools");
/*     */     
/*  57 */     AdvancementHolder smeltIron = Advancement.Builder.advancement()
/*  58 */       .parent(upgradeTools)
/*  59 */       .display((ItemLike)Items.IRON_INGOT, (Component)Component.translatable("advancements.story.smelt_iron.title"), (Component)Component.translatable("advancements.story.smelt_iron.description"), null, AdvancementType.TASK, true, true, false)
/*  60 */       .addCriterion("iron", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_INGOT
/*  61 */           })).save(output, "story/smelt_iron");
/*     */     
/*  63 */     AdvancementHolder ironTools = Advancement.Builder.advancement()
/*  64 */       .parent(smeltIron)
/*  65 */       .display((ItemLike)Items.IRON_PICKAXE, (Component)Component.translatable("advancements.story.iron_tools.title"), (Component)Component.translatable("advancements.story.iron_tools.description"), null, AdvancementType.TASK, true, true, false)
/*  66 */       .addCriterion("iron_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_PICKAXE
/*  67 */           })).save(output, "story/iron_tools");
/*     */     
/*  69 */     AdvancementHolder mineDiamond = Advancement.Builder.advancement()
/*  70 */       .parent(ironTools)
/*  71 */       .display((ItemLike)Items.DIAMOND, (Component)Component.translatable("advancements.story.mine_diamond.title"), (Component)Component.translatable("advancements.story.mine_diamond.description"), null, AdvancementType.TASK, true, true, false)
/*  72 */       .addCriterion("diamond", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND
/*  73 */           })).save(output, "story/mine_diamond");
/*     */     
/*  75 */     AdvancementHolder lavaBucket = Advancement.Builder.advancement()
/*  76 */       .parent(smeltIron)
/*  77 */       .display((ItemLike)Items.LAVA_BUCKET, (Component)Component.translatable("advancements.story.lava_bucket.title"), (Component)Component.translatable("advancements.story.lava_bucket.description"), null, AdvancementType.TASK, true, true, false)
/*  78 */       .addCriterion("lava_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.LAVA_BUCKET
/*  79 */           })).save(output, "story/lava_bucket");
/*     */     
/*  81 */     AdvancementHolder obtainArmor = Advancement.Builder.advancement()
/*  82 */       .parent(smeltIron)
/*  83 */       .display((ItemLike)Items.IRON_CHESTPLATE, (Component)Component.translatable("advancements.story.obtain_armor.title"), (Component)Component.translatable("advancements.story.obtain_armor.description"), null, AdvancementType.TASK, true, true, false)
/*  84 */       .requirements(AdvancementRequirements.Strategy.OR)
/*  85 */       .addCriterion("iron_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_HELMET
/*  86 */           })).addCriterion("iron_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_CHESTPLATE
/*  87 */           })).addCriterion("iron_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_LEGGINGS
/*  88 */           })).addCriterion("iron_boots", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_BOOTS
/*  89 */           })).save(output, "story/obtain_armor");
/*     */     
/*  91 */     Advancement.Builder.advancement()
/*  92 */       .parent(mineDiamond)
/*  93 */       .display((ItemLike)Items.ENCHANTED_BOOK, (Component)Component.translatable("advancements.story.enchant_item.title"), (Component)Component.translatable("advancements.story.enchant_item.description"), null, AdvancementType.TASK, true, true, false)
/*  94 */       .addCriterion("enchanted_item", EnchantedItemTrigger.TriggerInstance.enchantedItem())
/*  95 */       .save(output, "story/enchant_item");
/*     */     
/*  97 */     AdvancementHolder formObsidian = Advancement.Builder.advancement()
/*  98 */       .parent(lavaBucket)
/*  99 */       .display((ItemLike)Blocks.OBSIDIAN, (Component)Component.translatable("advancements.story.form_obsidian.title"), (Component)Component.translatable("advancements.story.form_obsidian.description"), null, AdvancementType.TASK, true, true, false)
/* 100 */       .addCriterion("obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.OBSIDIAN
/* 101 */           })).save(output, "story/form_obsidian");
/*     */     
/* 103 */     Advancement.Builder.advancement()
/* 104 */       .parent(obtainArmor)
/* 105 */       .display((ItemLike)Items.SHIELD, (Component)Component.translatable("advancements.story.deflect_arrow.title"), (Component)Component.translatable("advancements.story.deflect_arrow.description"), null, AdvancementType.TASK, true, true, false)
/* 106 */       .addCriterion("deflected_projectile", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))).blocked(true)))
/* 107 */       .save(output, "story/deflect_arrow");
/*     */     
/* 109 */     Advancement.Builder.advancement()
/* 110 */       .parent(mineDiamond)
/* 111 */       .display((ItemLike)Items.DIAMOND_CHESTPLATE, (Component)Component.translatable("advancements.story.shiny_gear.title"), (Component)Component.translatable("advancements.story.shiny_gear.description"), null, AdvancementType.TASK, true, true, false)
/* 112 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 113 */       .addCriterion("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_HELMET
/* 114 */           })).addCriterion("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_CHESTPLATE
/* 115 */           })).addCriterion("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_LEGGINGS
/* 116 */           })).addCriterion("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_BOOTS
/* 117 */           })).save(output, "story/shiny_gear");
/*     */     
/* 119 */     AdvancementHolder enterTheNether = Advancement.Builder.advancement()
/* 120 */       .parent(formObsidian)
/* 121 */       .display((ItemLike)Items.FLINT_AND_STEEL, (Component)Component.translatable("advancements.story.enter_the_nether.title"), (Component)Component.translatable("advancements.story.enter_the_nether.description"), null, AdvancementType.TASK, true, true, false)
/* 122 */       .addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER))
/* 123 */       .save(output, "story/enter_the_nether");
/*     */     
/* 125 */     Advancement.Builder.advancement()
/* 126 */       .parent(enterTheNether)
/* 127 */       .display((ItemLike)Items.GOLDEN_APPLE, (Component)Component.translatable("advancements.story.cure_zombie_villager.title"), (Component)Component.translatable("advancements.story.cure_zombie_villager.description"), null, AdvancementType.GOAL, true, true, false)
/* 128 */       .addCriterion("cured_zombie", CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager())
/* 129 */       .save(output, "story/cure_zombie_villager");
/*     */     
/* 131 */     AdvancementHolder followEnderEye = Advancement.Builder.advancement()
/* 132 */       .parent(enterTheNether)
/* 133 */       .display((ItemLike)Items.ENDER_EYE, (Component)Component.translatable("advancements.story.follow_ender_eye.title"), (Component)Component.translatable("advancements.story.follow_ender_eye.description"), null, AdvancementType.TASK, true, true, false)
/* 134 */       .addCriterion("in_stronghold", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.STRONGHOLD))))
/* 135 */       .save(output, "story/follow_ender_eye");
/*     */     
/* 137 */     Advancement.Builder.advancement()
/* 138 */       .parent(followEnderEye)
/* 139 */       .display((ItemLike)Blocks.END_STONE, (Component)Component.translatable("advancements.story.enter_the_end.title"), (Component)Component.translatable("advancements.story.enter_the_end.description"), null, AdvancementType.TASK, true, true, false)
/* 140 */       .addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END))
/* 141 */       .save(output, "story/enter_the_end");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/advancements/packs/VanillaStoryAdvancements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */