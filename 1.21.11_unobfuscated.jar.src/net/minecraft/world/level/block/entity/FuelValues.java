/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntSortedMap;
/*     */ import java.util.Collections;
/*     */ import java.util.SequencedSet;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class FuelValues {
/*     */   private final Object2IntSortedMap<Item> values;
/*     */   
/*     */   private FuelValues(Object2IntSortedMap<Item> values) {
/*  24 */     this.values = values;
/*     */   }
/*     */   
/*     */   public boolean isFuel(ItemStack itemStack) {
/*  28 */     return this.values.containsKey(itemStack.getItem());
/*     */   }
/*     */   
/*     */   public SequencedSet<Item> fuelItems() {
/*  32 */     return Collections.unmodifiableSequencedSet((SequencedSet<? extends Item>)this.values.keySet());
/*     */   }
/*     */   
/*     */   public int burnDuration(ItemStack itemStack) {
/*  36 */     if (itemStack.isEmpty()) {
/*  37 */       return 0;
/*     */     }
/*     */     
/*  40 */     return this.values.getInt(itemStack.getItem());
/*     */   }
/*     */   
/*     */   public static FuelValues vanillaBurnTimes(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures) {
/*  44 */     return vanillaBurnTimes(registries, enabledFeatures, 200);
/*     */   }
/*     */   
/*     */   public static FuelValues vanillaBurnTimes(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures, int baseUnit) {
/*  48 */     return new Builder(registries, enabledFeatures)
/*  49 */       .add((ItemLike)Items.LAVA_BUCKET, baseUnit * 100)
/*  50 */       .add((ItemLike)Blocks.COAL_BLOCK, baseUnit * 8 * 10)
/*  51 */       .add((ItemLike)Items.BLAZE_ROD, baseUnit * 12)
/*  52 */       .add((ItemLike)Items.COAL, baseUnit * 8)
/*  53 */       .add((ItemLike)Items.CHARCOAL, baseUnit * 8)
/*  54 */       .add(ItemTags.LOGS, baseUnit * 3 / 2)
/*  55 */       .add(ItemTags.BAMBOO_BLOCKS, baseUnit * 3 / 2)
/*  56 */       .add(ItemTags.PLANKS, baseUnit * 3 / 2)
/*  57 */       .add((ItemLike)Blocks.BAMBOO_MOSAIC, baseUnit * 3 / 2)
/*  58 */       .add(ItemTags.WOODEN_STAIRS, baseUnit * 3 / 2)
/*  59 */       .add((ItemLike)Blocks.BAMBOO_MOSAIC_STAIRS, baseUnit * 3 / 2)
/*  60 */       .add(ItemTags.WOODEN_SLABS, baseUnit * 3 / 4)
/*  61 */       .add((ItemLike)Blocks.BAMBOO_MOSAIC_SLAB, baseUnit * 3 / 4)
/*  62 */       .add(ItemTags.WOODEN_TRAPDOORS, baseUnit * 3 / 2)
/*  63 */       .add(ItemTags.WOODEN_PRESSURE_PLATES, baseUnit * 3 / 2)
/*  64 */       .add(ItemTags.WOODEN_SHELVES, baseUnit * 3 / 2)
/*  65 */       .add(ItemTags.WOODEN_FENCES, baseUnit * 3 / 2)
/*  66 */       .add(ItemTags.FENCE_GATES, baseUnit * 3 / 2)
/*  67 */       .add((ItemLike)Blocks.NOTE_BLOCK, baseUnit * 3 / 2)
/*  68 */       .add((ItemLike)Blocks.BOOKSHELF, baseUnit * 3 / 2)
/*  69 */       .add((ItemLike)Blocks.CHISELED_BOOKSHELF, baseUnit * 3 / 2)
/*  70 */       .add((ItemLike)Blocks.LECTERN, baseUnit * 3 / 2)
/*  71 */       .add((ItemLike)Blocks.JUKEBOX, baseUnit * 3 / 2)
/*  72 */       .add((ItemLike)Blocks.CHEST, baseUnit * 3 / 2)
/*  73 */       .add((ItemLike)Blocks.TRAPPED_CHEST, baseUnit * 3 / 2)
/*  74 */       .add((ItemLike)Blocks.CRAFTING_TABLE, baseUnit * 3 / 2)
/*  75 */       .add((ItemLike)Blocks.DAYLIGHT_DETECTOR, baseUnit * 3 / 2)
/*  76 */       .add(ItemTags.BANNERS, baseUnit * 3 / 2)
/*  77 */       .add((ItemLike)Items.BOW, baseUnit * 3 / 2)
/*  78 */       .add((ItemLike)Items.FISHING_ROD, baseUnit * 3 / 2)
/*  79 */       .add((ItemLike)Blocks.LADDER, baseUnit * 3 / 2)
/*  80 */       .add(ItemTags.SIGNS, baseUnit)
/*  81 */       .add(ItemTags.HANGING_SIGNS, baseUnit * 4)
/*  82 */       .add((ItemLike)Items.WOODEN_SHOVEL, baseUnit)
/*  83 */       .add((ItemLike)Items.WOODEN_SWORD, baseUnit)
/*  84 */       .add((ItemLike)Items.WOODEN_SPEAR, baseUnit)
/*  85 */       .add((ItemLike)Items.WOODEN_HOE, baseUnit)
/*  86 */       .add((ItemLike)Items.WOODEN_AXE, baseUnit)
/*  87 */       .add((ItemLike)Items.WOODEN_PICKAXE, baseUnit)
/*  88 */       .add(ItemTags.WOODEN_DOORS, baseUnit)
/*  89 */       .add(ItemTags.BOATS, baseUnit * 6)
/*  90 */       .add(ItemTags.WOOL, baseUnit / 2)
/*  91 */       .add(ItemTags.WOODEN_BUTTONS, baseUnit / 2)
/*  92 */       .add((ItemLike)Items.STICK, baseUnit / 2)
/*  93 */       .add(ItemTags.SAPLINGS, baseUnit / 2)
/*  94 */       .add((ItemLike)Items.BOWL, baseUnit / 2)
/*  95 */       .add(ItemTags.WOOL_CARPETS, 1 + baseUnit / 3)
/*  96 */       .add((ItemLike)Blocks.DRIED_KELP_BLOCK, 1 + baseUnit * 20)
/*  97 */       .add((ItemLike)Items.CROSSBOW, baseUnit * 3 / 2)
/*  98 */       .add((ItemLike)Blocks.BAMBOO, baseUnit / 4)
/*  99 */       .add((ItemLike)Blocks.DEAD_BUSH, baseUnit / 2)
/* 100 */       .add((ItemLike)Blocks.SHORT_DRY_GRASS, baseUnit / 2)
/* 101 */       .add((ItemLike)Blocks.TALL_DRY_GRASS, baseUnit / 2)
/* 102 */       .add((ItemLike)Blocks.SCAFFOLDING, baseUnit / 4)
/* 103 */       .add((ItemLike)Blocks.LOOM, baseUnit * 3 / 2)
/* 104 */       .add((ItemLike)Blocks.BARREL, baseUnit * 3 / 2)
/* 105 */       .add((ItemLike)Blocks.CARTOGRAPHY_TABLE, baseUnit * 3 / 2)
/* 106 */       .add((ItemLike)Blocks.FLETCHING_TABLE, baseUnit * 3 / 2)
/* 107 */       .add((ItemLike)Blocks.SMITHING_TABLE, baseUnit * 3 / 2)
/* 108 */       .add((ItemLike)Blocks.COMPOSTER, baseUnit * 3 / 2)
/* 109 */       .add((ItemLike)Blocks.AZALEA, baseUnit / 2)
/* 110 */       .add((ItemLike)Blocks.FLOWERING_AZALEA, baseUnit / 2)
/* 111 */       .add((ItemLike)Blocks.MANGROVE_ROOTS, baseUnit * 3 / 2)
/* 112 */       .add((ItemLike)Blocks.LEAF_LITTER, baseUnit / 2)
/*     */       
/* 114 */       .remove(ItemTags.NON_FLAMMABLE_WOOD)
/* 115 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final HolderLookup<Item> items;
/*     */     private final FeatureFlagSet enabledFeatures;
/* 123 */     private final Object2IntSortedMap<Item> values = (Object2IntSortedMap<Item>)new Object2IntLinkedOpenHashMap();
/*     */     
/*     */     public Builder(HolderLookup.Provider registries, FeatureFlagSet enabledFeatures) {
/* 126 */       this.items = (HolderLookup<Item>)registries.lookupOrThrow(Registries.ITEM);
/* 127 */       this.enabledFeatures = enabledFeatures;
/*     */     }
/*     */     
/*     */     public FuelValues build() {
/* 131 */       return new FuelValues(this.values);
/*     */     }
/*     */     
/*     */     public Builder remove(TagKey<Item> tag) {
/* 135 */       this.values.keySet().removeIf(item -> item.builtInRegistryHolder().is(tag));
/* 136 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(TagKey<Item> tag, int time) {
/* 140 */       this.items.get(tag).ifPresent(items -> {
/*     */             for (Holder<Item> item : (Iterable<Holder<Item>>)time) {
/*     */               putInternal(time, (Item)item.value());
/*     */             }
/*     */           });
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder add(ItemLike itemLike, int time) {
/* 149 */       Item item = itemLike.asItem();
/* 150 */       putInternal(time, item);
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     private void putInternal(int time, Item item) {
/* 155 */       if (item.isEnabled(this.enabledFeatures))
/* 156 */         this.values.put(item, time); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/FuelValues.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */