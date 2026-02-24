/*      */ package net.minecraft.world.entity.npc.villager;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.stream.Collectors;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.component.DataComponentExactPredicate;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.EnchantmentTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.StructureTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.DyeItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.MapItem;
/*      */ import net.minecraft.world.item.alchemy.Potion;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.alchemy.Potions;
/*      */ import net.minecraft.world.item.component.DyedItemColor;
/*      */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*      */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*      */ import net.minecraft.world.item.enchantment.providers.TradeRebalanceEnchantmentProviders;
/*      */ import net.minecraft.world.item.trading.ItemCost;
/*      */ import net.minecraft.world.item.trading.MerchantOffer;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.levelgen.structure.Structure;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationType;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import org.apache.commons.lang3.tuple.Pair;
/*      */ 
/*      */ public class VillagerTrades {
/*      */   private static final int DEFAULT_SUPPLY = 12;
/*      */   private static final int COMMON_ITEMS_SUPPLY = 16;
/*      */   private static final int UNCOMMON_ITEMS_SUPPLY = 3;
/*      */   private static final int XP_LEVEL_1_SELL = 1;
/*      */   private static final int XP_LEVEL_1_BUY = 2;
/*      */   private static final int XP_LEVEL_2_SELL = 5;
/*      */   private static final int XP_LEVEL_2_BUY = 10;
/*      */   private static final int XP_LEVEL_3_SELL = 10;
/*      */   private static final int XP_LEVEL_3_BUY = 20;
/*      */   private static final int XP_LEVEL_4_SELL = 15;
/*      */   private static final int XP_LEVEL_4_BUY = 30;
/*      */   private static final int XP_LEVEL_5_TRADE = 30;
/*      */   private static final float LOW_TIER_PRICE_MULTIPLIER = 0.05F;
/*      */   private static final float HIGH_TIER_PRICE_MULTIPLIER = 0.2F;
/*      */   public static final Map<ResourceKey<VillagerProfession>, Int2ObjectMap<ItemListing[]>> TRADES;
/*      */   
/*      */   static {
/*   81 */     TRADES = (Map<ResourceKey<VillagerProfession>, Int2ObjectMap<ItemListing[]>>)Util.make(Maps.newHashMap(), map -> {
/*      */           map.put(VillagerProfession.FARMER, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.WHEAT, 20, 16, 2), new EmeraldForItems((ItemLike)Items.POTATO, 26, 16, 2), new EmeraldForItems((ItemLike)Items.CARROT, 22, 16, 2), new EmeraldForItems((ItemLike)Items.BEETROOT, 15, 16, 2), new ItemsForEmeralds(Items.BREAD, 1, 6, 16, 1) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.PUMPKIN, 6, 12, 10), new ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5), new ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5) }, 3, new ItemListing[] { new ItemsForEmeralds(Items.COOKIE, 3, 18, 10), new EmeraldForItems((ItemLike)Blocks.MELON, 4, 12, 20) }, 4, new ItemListing[] { new ItemsForEmeralds(Blocks.CAKE, 1, 1, 12, 15), new SuspiciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15), new SuspiciousStewForEmerald(MobEffects.JUMP_BOOST, 160, 15), new SuspiciousStewForEmerald(MobEffects.WEAKNESS, 140, 15), new SuspiciousStewForEmerald(MobEffects.BLINDNESS, 120, 15), new SuspiciousStewForEmerald(MobEffects.POISON, 280, 15), new SuspiciousStewForEmerald(MobEffects.SATURATION, 7, 15) }, 5, new ItemListing[] { new ItemsForEmeralds(Items.GOLDEN_CARROT, 3, 3, 30), new ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30) })));
/*      */           map.put(VillagerProfession.FISHERMAN, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.STRING, 20, 16, 2), new EmeraldForItems((ItemLike)Items.COAL, 10, 16, 2), new ItemsAndEmeraldsToItems((ItemLike)Items.COD, 6, 1, Items.COOKED_COD, 6, 16, 1, 0.05F), new ItemsForEmeralds(Items.COD_BUCKET, 3, 1, 16, 1) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COD, 15, 16, 10), new ItemsAndEmeraldsToItems((ItemLike)Items.SALMON, 6, 1, Items.COOKED_SALMON, 6, 16, 5, 0.05F), new ItemsForEmeralds(Items.CAMPFIRE, 2, 1, 5) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.SALMON, 13, 16, 20), new EnchantedItemForEmeralds(Items.FISHING_ROD, 3, 3, 10, 0.2F) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.TROPICAL_FISH, 6, 12, 30) }, 5, new ItemListing[] { new EmeraldForItems((ItemLike)Items.PUFFERFISH, 4, 12, 30), new EmeraldsForVillagerTypeItem(1, 12, 30, (Map<ResourceKey<VillagerType>, Item>)ImmutableMap.builder().put(VillagerType.PLAINS, Items.OAK_BOAT).put(VillagerType.TAIGA, Items.SPRUCE_BOAT).put(VillagerType.SNOW, Items.SPRUCE_BOAT).put(VillagerType.DESERT, Items.JUNGLE_BOAT).put(VillagerType.JUNGLE, Items.JUNGLE_BOAT).put(VillagerType.SAVANNA, Items.ACACIA_BOAT).put(VillagerType.SWAMP, Items.DARK_OAK_BOAT).build()) })));
/*      */           map.put(VillagerProfession.SHEPHERD, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.WHITE_WOOL, 18, 16, 2), new EmeraldForItems((ItemLike)Blocks.BROWN_WOOL, 18, 16, 2), new EmeraldForItems((ItemLike)Blocks.BLACK_WOOL, 18, 16, 2), new EmeraldForItems((ItemLike)Blocks.GRAY_WOOL, 18, 16, 2), new ItemsForEmeralds(Items.SHEARS, 2, 1, 1) }, 2, new ItemListing[] { 
/*      */                     new EmeraldForItems((ItemLike)Items.WHITE_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.GRAY_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.BLACK_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.LIGHT_BLUE_DYE, 12, 16, 10), new EmeraldForItems((ItemLike)Items.LIME_DYE, 12, 16, 10), new ItemsForEmeralds(Blocks.WHITE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.ORANGE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.MAGENTA_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.YELLOW_WOOL, 1, 1, 16, 5), 
/*      */                     new ItemsForEmeralds(Blocks.LIME_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.PINK_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.CYAN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.PURPLE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BROWN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.GREEN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.RED_WOOL, 1, 1, 16, 5), 
/*      */                     new ItemsForEmeralds(Blocks.BLACK_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.WHITE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.ORANGE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.MAGENTA_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.YELLOW_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIME_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.PINK_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.GRAY_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 5), 
/*      */                     new ItemsForEmeralds(Blocks.CYAN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.PURPLE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BROWN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.GREEN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.RED_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BLACK_CARPET, 1, 4, 16, 5) }, 3, new ItemListing[] { 
/*      */                     new EmeraldForItems((ItemLike)Items.YELLOW_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.LIGHT_GRAY_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.ORANGE_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.RED_DYE, 12, 16, 20), new EmeraldForItems((ItemLike)Items.PINK_DYE, 12, 16, 20), new ItemsForEmeralds(Blocks.WHITE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.YELLOW_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.RED_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BLACK_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BLUE_BED, 3, 1, 12, 10), 
/*      */                     new ItemsForEmeralds(Blocks.BROWN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.CYAN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.GRAY_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.GREEN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIME_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.MAGENTA_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.ORANGE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.PINK_BED, 3, 1, 12, 10), 
/*      */                     new ItemsForEmeralds(Blocks.PURPLE_BED, 3, 1, 12, 10) }, 4, new ItemListing[] { 
/*      */                     new EmeraldForItems((ItemLike)Items.BROWN_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.PURPLE_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.BLUE_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.GREEN_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.MAGENTA_DYE, 12, 16, 30), new EmeraldForItems((ItemLike)Items.CYAN_DYE, 12, 16, 30), new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 12, 15), 
/*      */                     new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 12, 15), 
/*      */                     new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 15) }, 5, new ItemListing[] { new ItemsForEmeralds(Items.PAINTING, 2, 3, 30) })));
/*      */           map.put(VillagerProfession.FLETCHER, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.STICK, 32, 16, 2), new ItemsForEmeralds(Items.ARROW, 1, 16, 1), new ItemsAndEmeraldsToItems((ItemLike)Blocks.GRAVEL, 10, 1, Items.FLINT, 10, 12, 1, 0.05F) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 26, 12, 10), new ItemsForEmeralds(Items.BOW, 2, 1, 5) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.STRING, 14, 16, 20), new ItemsForEmeralds(Items.CROSSBOW, 3, 1, 10) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.FEATHER, 24, 16, 30), new EnchantedItemForEmeralds(Items.BOW, 2, 3, 15) }, 5, new ItemListing[] { new EmeraldForItems((ItemLike)Items.TRIPWIRE_HOOK, 8, 12, 30), new EnchantedItemForEmeralds(Items.CROSSBOW, 3, 3, 15), new TippedArrowForItemsAndEmeralds(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 12, 30) })));
/*      */           map.put(VillagerProfession.LIBRARIAN, toIntMap(ImmutableMap.builder().put(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.PAPER, 24, 16, 2), new EnchantBookForEmeralds(1, EnchantmentTags.TRADEABLE), new ItemsForEmeralds(Blocks.BOOKSHELF, 9, 1, 12, 1) }).put(2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.BOOK, 4, 12, 10), new EnchantBookForEmeralds(5, EnchantmentTags.TRADEABLE), new ItemsForEmeralds(Items.LANTERN, 1, 1, 5) }).put(3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.INK_SAC, 5, 12, 20), new EnchantBookForEmeralds(10, EnchantmentTags.TRADEABLE), new ItemsForEmeralds(Items.GLASS, 1, 4, 10) }).put(4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.WRITABLE_BOOK, 2, 12, 30), new EnchantBookForEmeralds(15, EnchantmentTags.TRADEABLE), new ItemsForEmeralds(Items.CLOCK, 5, 1, 15), new ItemsForEmeralds(Items.COMPASS, 4, 1, 15) }).put(5, new ItemListing[] { new ItemsForEmeralds(Items.NAME_TAG, 20, 1, 30) }).build()));
/*      */           map.put(VillagerProfession.CARTOGRAPHER, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.PAPER, 24, 12, 2), new ItemsForEmeralds(Items.MAP, 7, 1, 12, 1, 0.05F) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.GLASS_PANE, 11, 12, 10), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_TAIGA_VILLAGE_MAPS, "filled_map.village_taiga", MapDecorationTypes.TAIGA_VILLAGE, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SWAMP, VillagerType.SNOW, VillagerType.PLAINS }), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_SWAMP_EXPLORER_MAPS, "filled_map.explorer_swamp", MapDecorationTypes.SWAMP_HUT, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.TAIGA, VillagerType.SNOW, VillagerType.JUNGLE }), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_SNOWY_VILLAGE_MAPS, "filled_map.village_snowy", MapDecorationTypes.SNOWY_VILLAGE, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.TAIGA, VillagerType.SWAMP }), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_SAVANNA_VILLAGE_MAPS, "filled_map.village_savanna", MapDecorationTypes.SAVANNA_VILLAGE, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.PLAINS, VillagerType.JUNGLE, VillagerType.DESERT }), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_PLAINS_VILLAGE_MAPS, "filled_map.village_plains", MapDecorationTypes.PLAINS_VILLAGE, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.TAIGA, VillagerType.SNOW, VillagerType.SAVANNA, VillagerType.DESERT }), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_JUNGLE_EXPLORER_MAPS, "filled_map.explorer_jungle", MapDecorationTypes.JUNGLE_TEMPLE, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SWAMP, VillagerType.SAVANNA, VillagerType.DESERT }), TypeSpecificTrade.oneTradeInBiomes(new TreasureMapForEmeralds(8, StructureTags.ON_DESERT_VILLAGE_MAPS, "filled_map.village_desert", MapDecorationTypes.DESERT_VILLAGE, 12, 5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SAVANNA, VillagerType.JUNGLE }) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COMPASS, 1, 12, 20), new TreasureMapForEmeralds(13, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.OCEAN_MONUMENT, 12, 10), new TreasureMapForEmeralds(12, StructureTags.ON_TRIAL_CHAMBERS_MAPS, "filled_map.trial_chambers", MapDecorationTypes.TRIAL_CHAMBERS, 12, 10) }, 4, new ItemListing[] { 
/*      */                     new ItemsForEmeralds(Items.ITEM_FRAME, 7, 1, 12, 15, 0.05F), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.BLUE_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SNOW, VillagerType.TAIGA }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.WHITE_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SNOW, VillagerType.PLAINS }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.RED_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SNOW, VillagerType.SAVANNA }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.GREEN_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.DESERT, VillagerType.SAVANNA, VillagerType.JUNGLE }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.LIME_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.DESERT, VillagerType.TAIGA }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.PURPLE_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.TAIGA, VillagerType.SWAMP }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CYAN_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.DESERT, VillagerType.SNOW }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.YELLOW_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.PLAINS, VillagerType.JUNGLE }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.ORANGE_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SAVANNA, VillagerType.DESERT }), 
/*      */                     TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.BROWN_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.PLAINS, VillagerType.JUNGLE }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.MAGENTA_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SAVANNA }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SNOW, VillagerType.SWAMP }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.PINK_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.TAIGA, VillagerType.PLAINS }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.GRAY_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.DESERT }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.BLACK_BANNER, 2, 1, 12, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SWAMP }) }, 5, new ItemListing[] { new ItemsForEmeralds(Items.GLOBE_BANNER_PATTERN, 8, 1, 12, 30, 0.05F), new TreasureMapForEmeralds(14, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecorationTypes.WOODLAND_MANSION, 12, 30) })));
/*      */           map.put(VillagerProfession.CLERIC, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.ROTTEN_FLESH, 32, 16, 2), new ItemsForEmeralds(Items.REDSTONE, 1, 2, 1) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.GOLD_INGOT, 3, 12, 10), new ItemsForEmeralds(Items.LAPIS_LAZULI, 1, 1, 5) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.RABBIT_FOOT, 2, 12, 20), new ItemsForEmeralds(Blocks.GLOWSTONE, 4, 1, 12, 10) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.TURTLE_SCUTE, 4, 12, 30), new EmeraldForItems((ItemLike)Items.GLASS_BOTTLE, 9, 12, 30), new ItemsForEmeralds(Items.ENDER_PEARL, 5, 1, 15) }, 5, new ItemListing[] { new EmeraldForItems((ItemLike)Items.NETHER_WART, 22, 12, 30), new ItemsForEmeralds(Items.EXPERIENCE_BOTTLE, 3, 1, 30) })));
/*      */           map.put(VillagerProfession.ARMORER, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_HELMET), 5, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack((ItemLike)Items.BELL), 36, 1, 12, 5, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.LAVA_BUCKET, 1, 12, 20), new EmeraldForItems((ItemLike)Items.DIAMOND, 1, 12, 20), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.SHIELD), 5, 1, 12, 10, 0.2F) }, 4, new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2F), new EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2F) }, 5, new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 8, 3, 30, 0.2F), new EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2F) })));
/*      */           map.put(VillagerProfession.WEAPONSMITH, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack((ItemLike)Items.IRON_AXE), 3, 1, 12, 1, 0.2F), new EnchantedItemForEmeralds(Items.IRON_SWORD, 2, 3, 1) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack((ItemLike)Items.BELL), 36, 1, 12, 5, 0.2F) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 24, 12, 20) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F) }, 5, new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_SWORD, 8, 3, 30, 0.2F) })));
/*      */           map.put(VillagerProfession.TOOLSMITH, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_AXE), 1, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.STONE_HOE), 1, 1, 12, 1, 0.2F) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack((ItemLike)Items.BELL), 36, 1, 12, 5, 0.2F) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 30, 12, 20), new EnchantedItemForEmeralds(Items.IRON_AXE, 1, 3, 10, 0.2F), new EnchantedItemForEmeralds(Items.IRON_SHOVEL, 2, 3, 10, 0.2F), new EnchantedItemForEmeralds(Items.IRON_PICKAXE, 3, 3, 10, 0.2F), new ItemsForEmeralds(new ItemStack((ItemLike)Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F), new EnchantedItemForEmeralds(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2F) }, 5, new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2F) })));
/*      */           map.put(VillagerProfession.BUTCHER, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.CHICKEN, 14, 16, 2), new EmeraldForItems((ItemLike)Items.PORKCHOP, 7, 16, 2), new EmeraldForItems((ItemLike)Items.RABBIT, 4, 16, 2), new ItemsForEmeralds(Items.RABBIT_STEW, 1, 1, 1) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 16, 2), new ItemsForEmeralds(Items.COOKED_PORKCHOP, 1, 5, 16, 5), new ItemsForEmeralds(Items.COOKED_CHICKEN, 1, 8, 16, 5) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.MUTTON, 7, 16, 20), new EmeraldForItems((ItemLike)Items.BEEF, 10, 16, 20) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.DRIED_KELP_BLOCK, 10, 12, 30) }, 5, new ItemListing[] { new EmeraldForItems((ItemLike)Items.SWEET_BERRIES, 10, 12, 30) })));
/*      */           map.put(VillagerProfession.LEATHERWORKER, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.LEATHER, 6, 16, 2), new DyedArmorForEmeralds(Items.LEATHER_LEGGINGS, 3), new DyedArmorForEmeralds(Items.LEATHER_CHESTPLATE, 7) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.FLINT, 26, 12, 10), new DyedArmorForEmeralds(Items.LEATHER_HELMET, 5, 12, 5), new DyedArmorForEmeralds(Items.LEATHER_BOOTS, 4, 12, 5) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.RABBIT_HIDE, 9, 12, 20), new DyedArmorForEmeralds(Items.LEATHER_CHESTPLATE, 7) }, 4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.TURTLE_SCUTE, 4, 12, 30), new DyedArmorForEmeralds(Items.LEATHER_HORSE_ARMOR, 6, 12, 15) }, 5, new ItemListing[] { new ItemsForEmeralds(new ItemStack((ItemLike)Items.SADDLE), 6, 1, 12, 30, 0.2F), new DyedArmorForEmeralds(Items.LEATHER_HELMET, 5, 12, 30) })));
/*      */           map.put(VillagerProfession.MASON, toIntMap(ImmutableMap.of(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.CLAY_BALL, 10, 16, 2), new ItemsForEmeralds(Items.BRICK, 1, 10, 16, 1) }, 2, new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.STONE, 20, 16, 10), new ItemsForEmeralds(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5) }, 3, new ItemListing[] { new EmeraldForItems((ItemLike)Blocks.GRANITE, 16, 16, 20), new EmeraldForItems((ItemLike)Blocks.ANDESITE, 16, 16, 20), new EmeraldForItems((ItemLike)Blocks.DIORITE, 16, 16, 20), new ItemsForEmeralds(Blocks.DRIPSTONE_BLOCK, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_GRANITE, 1, 4, 16, 10) }, 4, new ItemListing[] { 
/*      */                     new EmeraldForItems((ItemLike)Items.QUARTZ, 12, 12, 30), new ItemsForEmeralds(Blocks.ORANGE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.WHITE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.RED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PINK_TERRACOTTA, 1, 1, 12, 15), 
/*      */                     new ItemsForEmeralds(Blocks.MAGENTA_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIME_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GREEN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.CYAN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PURPLE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.YELLOW_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BROWN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), 
/*      */                     new ItemsForEmeralds(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 12, 15), 
/*      */                     new ItemsForEmeralds(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 12, 15) }, 5, new ItemListing[] { new ItemsForEmeralds(Blocks.QUARTZ_PILLAR, 1, 1, 12, 30), new ItemsForEmeralds(Blocks.QUARTZ_BLOCK, 1, 1, 12, 30) })));
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  544 */   public static final List<Pair<ItemListing[], Integer>> WANDERING_TRADER_TRADES = (List<Pair<ItemListing[], Integer>>)ImmutableList.builder()
/*  545 */     .add(Pair.of(new ItemListing[] {
/*  546 */           new EmeraldForItems(potionCost(Potions.WATER), 2, 1, 1), new EmeraldForItems((ItemLike)Items.WATER_BUCKET, 1, 2, 1, 2), new EmeraldForItems((ItemLike)Items.MILK_BUCKET, 1, 2, 1, 2), new EmeraldForItems((ItemLike)Items.FERMENTED_SPIDER_EYE, 1, 2, 1, 3), new EmeraldForItems((ItemLike)Items.BAKED_POTATO, 4, 2, 1), new EmeraldForItems((ItemLike)Items.HAY_BLOCK, 1, 2, 1) }, 2))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  553 */     .add(Pair.of(new ItemListing[] {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.PACKED_ICE, 1, 1, 6, 1), new ItemsForEmeralds(Items.BLUE_ICE, 6, 1, 6, 1), new ItemsForEmeralds(Items.GUNPOWDER, 1, 4, 2, 1), new ItemsForEmeralds(Items.PODZOL, 3, 3, 6, 1), new ItemsForEmeralds(Blocks.ACACIA_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.BIRCH_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.DARK_OAK_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.JUNGLE_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.OAK_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.SPRUCE_LOG, 1, 8, 4, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  568 */           new ItemsForEmeralds(Blocks.CHERRY_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.MANGROVE_LOG, 1, 8, 4, 1), new ItemsForEmeralds(Blocks.PALE_OAK_LOG, 1, 8, 4, 1), new EnchantedItemForEmeralds(Items.IRON_PICKAXE, 1, 1, 1, 0.2F), new ItemsForEmeralds(potion(Potions.LONG_INVISIBILITY), 5, 1, 1, 1) }, 2))
/*      */     
/*  570 */     .add(Pair.of(new ItemListing[] {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.TROPICAL_FISH_BUCKET, 3, 1, 4, 1), new ItemsForEmeralds(Items.PUFFERFISH_BUCKET, 3, 1, 4, 1), new ItemsForEmeralds(Items.SEA_PICKLE, 2, 1, 5, 1), new ItemsForEmeralds(Items.SLIME_BALL, 4, 1, 5, 1), new ItemsForEmeralds(Items.GLOWSTONE, 2, 1, 5, 1), new ItemsForEmeralds(Items.NAUTILUS_SHELL, 5, 1, 5, 1), new ItemsForEmeralds(Items.FERN, 1, 1, 12, 1), new ItemsForEmeralds(Items.SUGAR_CANE, 1, 1, 8, 1), new ItemsForEmeralds(Items.PUMPKIN, 1, 1, 4, 1), new ItemsForEmeralds(Items.KELP, 3, 1, 12, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.CACTUS, 3, 1, 8, 1), new ItemsForEmeralds(Items.DANDELION, 1, 1, 12, 1), new ItemsForEmeralds(Items.POPPY, 1, 1, 12, 1), new ItemsForEmeralds(Items.BLUE_ORCHID, 1, 1, 8, 1), new ItemsForEmeralds(Items.ALLIUM, 1, 1, 12, 1), new ItemsForEmeralds(Items.AZURE_BLUET, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.PINK_TULIP, 1, 1, 12, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.OXEYE_DAISY, 1, 1, 12, 1), new ItemsForEmeralds(Items.CORNFLOWER, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1), new ItemsForEmeralds(Items.OPEN_EYEBLOSSOM, 1, 1, 7, 1), new ItemsForEmeralds(Items.WHEAT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.BEETROOT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.PUMPKIN_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.MELON_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.ACACIA_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.BIRCH_SAPLING, 5, 1, 8, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.DARK_OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.JUNGLE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.SPRUCE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.CHERRY_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.PALE_OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.MANGROVE_PROPAGULE, 5, 1, 8, 1), new ItemsForEmeralds(Items.RED_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.WHITE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLUE_DYE, 1, 3, 12, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.PINK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLACK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GREEN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.MAGENTA_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.YELLOW_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PURPLE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIME_DYE, 1, 3, 12, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.ORANGE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BROWN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.CYAN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.VINE, 1, 3, 4, 1), new ItemsForEmeralds(Items.PALE_HANGING_MOSS, 1, 3, 4, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 3, 4, 1), new ItemsForEmeralds(Items.RED_MUSHROOM, 1, 3, 4, 1), new ItemsForEmeralds(Items.LILY_PAD, 1, 5, 2, 1), new ItemsForEmeralds(Items.SMALL_DRIPLEAF, 1, 2, 5, 1), new ItemsForEmeralds(Items.SAND, 1, 8, 8, 1), new ItemsForEmeralds(Items.RED_SAND, 1, 4, 6, 1), new ItemsForEmeralds(Items.POINTED_DRIPSTONE, 1, 2, 5, 1), new ItemsForEmeralds(Items.ROOTED_DIRT, 1, 2, 5, 1), new ItemsForEmeralds(Items.MOSS_BLOCK, 1, 2, 5, 1), new ItemsForEmeralds(Items.PALE_MOSS_BLOCK, 1, 2, 5, 1),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           new ItemsForEmeralds(Items.WILDFLOWERS, 1, 1, 12, 1), new ItemsForEmeralds(Items.DRY_TALL_GRASS, 1, 1, 12, 1), new ItemsForEmeralds(Items.FIREFLY_BUSH, 3, 1, 12, 1)
/*  645 */         }, 5)).build();
/*      */   
/*      */   private static ItemListing commonBooks(int villagerXP) {
/*  648 */     return new TypeSpecificTrade(
/*  649 */         (Map<ResourceKey<VillagerType>, ItemListing>)ImmutableMap.builder()
/*  650 */         .put(VillagerType.DESERT, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_DESERT_COMMON))
/*  651 */         .put(VillagerType.JUNGLE, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_JUNGLE_COMMON))
/*  652 */         .put(VillagerType.PLAINS, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_PLAINS_COMMON))
/*  653 */         .put(VillagerType.SAVANNA, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_SAVANNA_COMMON))
/*  654 */         .put(VillagerType.SNOW, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_SNOW_COMMON))
/*  655 */         .put(VillagerType.SWAMP, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_SWAMP_COMMON))
/*  656 */         .put(VillagerType.TAIGA, new EnchantBookForEmeralds(villagerXP, EnchantmentTags.TRADES_TAIGA_COMMON))
/*  657 */         .build());
/*      */   }
/*      */   
/*      */   private static ItemListing specialBooks() {
/*  661 */     return new TypeSpecificTrade(
/*  662 */         (Map<ResourceKey<VillagerType>, ItemListing>)ImmutableMap.builder()
/*  663 */         .put(VillagerType.DESERT, new EnchantBookForEmeralds(30, 3, 3, EnchantmentTags.TRADES_DESERT_SPECIAL))
/*  664 */         .put(VillagerType.JUNGLE, new EnchantBookForEmeralds(30, 2, 2, EnchantmentTags.TRADES_JUNGLE_SPECIAL))
/*  665 */         .put(VillagerType.PLAINS, new EnchantBookForEmeralds(30, 3, 3, EnchantmentTags.TRADES_PLAINS_SPECIAL))
/*  666 */         .put(VillagerType.SAVANNA, new EnchantBookForEmeralds(30, 3, 3, EnchantmentTags.TRADES_SAVANNA_SPECIAL))
/*  667 */         .put(VillagerType.SNOW, new EnchantBookForEmeralds(30, EnchantmentTags.TRADES_SNOW_SPECIAL))
/*  668 */         .put(VillagerType.SWAMP, new EnchantBookForEmeralds(30, EnchantmentTags.TRADES_SWAMP_SPECIAL))
/*  669 */         .put(VillagerType.TAIGA, new EnchantBookForEmeralds(30, 2, 2, EnchantmentTags.TRADES_TAIGA_SPECIAL))
/*  670 */         .build());
/*      */   }
/*      */ 
/*      */   
/*  674 */   public static final Map<ResourceKey<VillagerProfession>, Int2ObjectMap<ItemListing[]>> EXPERIMENTAL_TRADES = Map.of(VillagerProfession.LIBRARIAN, 
/*  675 */       toIntMap(ImmutableMap.builder()
/*  676 */         .put(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.PAPER, 24, 16, 2), 
/*      */             
/*  678 */             commonBooks(1), new ItemsForEmeralds(Blocks.BOOKSHELF, 9, 1, 12, 1)
/*      */ 
/*      */           
/*  681 */           }).put(2, new ItemListing[] { new EmeraldForItems((ItemLike)Items.BOOK, 4, 12, 10), 
/*      */             
/*  683 */             commonBooks(5), new ItemsForEmeralds(Items.LANTERN, 1, 1, 5)
/*      */ 
/*      */           
/*  686 */           }).put(3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.INK_SAC, 5, 12, 20), 
/*      */             
/*  688 */             commonBooks(10), new ItemsForEmeralds(Items.GLASS, 1, 4, 10)
/*      */ 
/*      */           
/*  691 */           }).put(4, new ItemListing[] { new EmeraldForItems((ItemLike)Items.WRITABLE_BOOK, 2, 12, 30), new ItemsForEmeralds(Items.CLOCK, 5, 1, 15), new ItemsForEmeralds(Items.COMPASS, 4, 1, 15)
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  696 */           }).put(5, new ItemListing[] {
/*  697 */             specialBooks(), new ItemsForEmeralds(Items.NAME_TAG, 20, 1, 30)
/*      */ 
/*      */           
/*  700 */           }).build()), VillagerProfession.ARMORER, 
/*  701 */       toIntMap(ImmutableMap.builder()
/*  702 */         .put(1, new ItemListing[] { new EmeraldForItems((ItemLike)Items.COAL, 15, 12, 2), new EmeraldForItems((ItemLike)Items.IRON_INGOT, 5, 12, 2)
/*      */ 
/*      */ 
/*      */           
/*  706 */           }).put(2, new ItemListing[] {
/*  707 */             TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_BOOTS, 4, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA
/*      */               
/*  711 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_BOOTS, 4, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE, VillagerType.SWAMP
/*      */               
/*  715 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_HELMET, 5, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA
/*      */               
/*  719 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_HELMET, 5, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE, VillagerType.SWAMP
/*      */               
/*  723 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_LEGGINGS, 7, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA
/*      */               
/*  727 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_LEGGINGS, 7, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE, VillagerType.SWAMP
/*      */               
/*  731 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_CHESTPLATE, 9, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.TAIGA
/*      */               
/*  735 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_CHESTPLATE, 9, 1, 12, 5, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE, VillagerType.SWAMP
/*      */               })
/*      */           
/*  740 */           }).put(3, new ItemListing[] { new EmeraldForItems((ItemLike)Items.LAVA_BUCKET, 1, 12, 20), new ItemsForEmeralds(Items.SHIELD, 5, 1, 12, 10, 0.05F), new ItemsForEmeralds(Items.BELL, 36, 1, 12, 10, 0.2F)
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  745 */           }).put(4, new ItemListing[] { 
/*  746 */             TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_BOOTS, 8, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_DESERT_ARMORER_BOOTS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT
/*      */               
/*  750 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_HELMET, 9, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_DESERT_ARMORER_HELMET_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT
/*      */               
/*  754 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_LEGGINGS, 11, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_DESERT_ARMORER_LEGGINGS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT
/*      */               
/*  758 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_CHESTPLATE, 13, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_DESERT_ARMORER_CHESTPLATE_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT
/*      */               
/*  762 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_BOOTS, 8, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_PLAINS_ARMORER_BOOTS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.PLAINS
/*      */               
/*  766 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_HELMET, 9, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_PLAINS_ARMORER_HELMET_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.PLAINS
/*      */               
/*  770 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_LEGGINGS, 11, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_PLAINS_ARMORER_LEGGINGS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.PLAINS
/*      */               
/*  774 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_CHESTPLATE, 13, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_PLAINS_ARMORER_CHESTPLATE_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.PLAINS
/*      */               
/*  778 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_BOOTS, 2, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SAVANNA_ARMORER_BOOTS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SAVANNA
/*      */               
/*  782 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_HELMET, 3, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SAVANNA_ARMORER_HELMET_4), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SAVANNA
/*      */               
/*      */               }),
/*      */             
/*  786 */             TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_LEGGINGS, 5, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SAVANNA_ARMORER_LEGGINGS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SAVANNA
/*      */               
/*  790 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_CHESTPLATE, 7, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SAVANNA_ARMORER_CHESTPLATE_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SAVANNA
/*      */               
/*  794 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_BOOTS, 8, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SNOW_ARMORER_BOOTS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SNOW
/*      */               
/*  798 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.IRON_HELMET, 9, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SNOW_ARMORER_HELMET_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SNOW
/*      */               
/*  802 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_BOOTS, 8, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_JUNGLE_ARMORER_BOOTS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE
/*      */               
/*  806 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_HELMET, 9, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_JUNGLE_ARMORER_HELMET_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE
/*      */               
/*  810 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_LEGGINGS, 11, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_JUNGLE_ARMORER_LEGGINGS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE
/*      */               
/*  814 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_CHESTPLATE, 13, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_JUNGLE_ARMORER_CHESTPLATE_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE
/*      */               
/*  818 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_BOOTS, 8, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SWAMP_ARMORER_BOOTS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SWAMP
/*      */               
/*  822 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_HELMET, 9, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SWAMP_ARMORER_HELMET_4), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.SWAMP
/*      */               
/*      */               }),
/*      */             
/*  826 */             TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_LEGGINGS, 11, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SWAMP_ARMORER_LEGGINGS_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SWAMP
/*      */               
/*  830 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_CHESTPLATE, 13, 1, 3, 15, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SWAMP_ARMORER_CHESTPLATE_4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SWAMP
/*      */               
/*  834 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND_BOOTS, 1, 4, Items.DIAMOND_LEGGINGS, 1, 3, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*  838 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND_LEGGINGS, 1, 4, Items.DIAMOND_CHESTPLATE, 1, 3, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*  842 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND_HELMET, 1, 4, Items.DIAMOND_BOOTS, 1, 3, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*  846 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND_CHESTPLATE, 1, 2, Items.DIAMOND_HELMET, 1, 3, 15, 0.05F), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*      */               })
/*  851 */           }).put(5, new ItemListing[] { 
/*  852 */             TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 4, 16, (ItemLike)Items.DIAMOND_CHESTPLATE, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_DESERT_ARMORER_CHESTPLATE_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT
/*      */               
/*  856 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 3, 16, (ItemLike)Items.DIAMOND_LEGGINGS, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_DESERT_ARMORER_LEGGINGS_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT
/*      */               
/*  860 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 3, 16, (ItemLike)Items.DIAMOND_LEGGINGS, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_PLAINS_ARMORER_LEGGINGS_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.PLAINS
/*      */               
/*  864 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 2, 12, (ItemLike)Items.DIAMOND_BOOTS, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_PLAINS_ARMORER_BOOTS_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.PLAINS
/*      */               
/*  868 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 2, 6, (ItemLike)Items.DIAMOND_HELMET, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SAVANNA_ARMORER_HELMET_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SAVANNA
/*      */               
/*  872 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 3, 8, (ItemLike)Items.DIAMOND_CHESTPLATE, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SAVANNA_ARMORER_CHESTPLATE_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SAVANNA
/*      */               
/*  876 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 2, 12, (ItemLike)Items.DIAMOND_BOOTS, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SNOW_ARMORER_BOOTS_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SNOW
/*      */               
/*  880 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 3, 12, (ItemLike)Items.DIAMOND_HELMET, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SNOW_ARMORER_HELMET_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SNOW
/*      */               
/*  884 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_HELMET, 9, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_JUNGLE_ARMORER_HELMET_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.JUNGLE
/*      */               
/*  888 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_BOOTS, 8, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_JUNGLE_ARMORER_BOOTS_5), (ResourceKey<VillagerType>[])new ResourceKey[] { VillagerType.JUNGLE
/*      */               
/*      */               }),
/*      */             
/*  892 */             TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_HELMET, 9, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SWAMP_ARMORER_HELMET_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SWAMP
/*      */               
/*  896 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsForEmeralds(Items.CHAINMAIL_BOOTS, 8, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_SWAMP_ARMORER_BOOTS_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.SWAMP
/*      */               
/*  900 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 4, 18, (ItemLike)Items.DIAMOND_CHESTPLATE, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_TAIGA_ARMORER_CHESTPLATE_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*  904 */               }), TypeSpecificTrade.oneTradeInBiomes(new ItemsAndEmeraldsToItems((ItemLike)Items.DIAMOND, 3, 18, (ItemLike)Items.DIAMOND_LEGGINGS, 1, 3, 30, 0.05F, TradeRebalanceEnchantmentProviders.TRADES_TAIGA_ARMORER_LEGGINGS_5), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*  908 */               }), TypeSpecificTrade.oneTradeInBiomes(new EmeraldForItems((ItemLike)Items.DIAMOND_BLOCK, 1, 12, 30, 42), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.TAIGA
/*      */               
/*  912 */               }), TypeSpecificTrade.oneTradeInBiomes(new EmeraldForItems((ItemLike)Items.IRON_BLOCK, 1, 12, 30, 4), (ResourceKey<VillagerType>[])new ResourceKey[] {
/*      */                 
/*      */                 VillagerType.DESERT, VillagerType.JUNGLE, VillagerType.PLAINS, VillagerType.SAVANNA, VillagerType.SNOW, VillagerType.SWAMP
/*      */               
/*      */               })
/*  917 */           }).build()));
/*      */ 
/*      */   
/*      */   private static Int2ObjectMap<ItemListing[]> toIntMap(ImmutableMap<Integer, ItemListing[]> source) {
/*  921 */     return (Int2ObjectMap<ItemListing[]>)new Int2ObjectOpenHashMap((Map)source);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class EmeraldForItems
/*      */     implements ItemListing
/*      */   {
/*      */     private final ItemCost itemStack;
/*      */     
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     private final int emeraldAmount;
/*      */     private final float priceMultiplier;
/*      */     
/*      */     public EmeraldForItems(ItemLike item, int itemAmount, int maxUses, int villagerXp) {
/*  936 */       this(item, itemAmount, maxUses, villagerXp, 1);
/*      */     }
/*      */     
/*      */     public EmeraldForItems(ItemLike item, int itemAmount, int maxUses, int villagerXp, int emeraldAmount) {
/*  940 */       this(new ItemCost((ItemLike)item.asItem(), itemAmount), maxUses, villagerXp, emeraldAmount);
/*      */     }
/*      */     
/*      */     public EmeraldForItems(ItemCost itemStack, int maxUses, int villagerXp, int emeraldAmount) {
/*  944 */       this.itemStack = itemStack;
/*  945 */       this.maxUses = maxUses;
/*  946 */       this.villagerXp = villagerXp;
/*  947 */       this.emeraldAmount = emeraldAmount;
/*  948 */       this.priceMultiplier = 0.05F;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/*  953 */       return new MerchantOffer(this.itemStack, new ItemStack((ItemLike)Items.EMERALD, this.emeraldAmount), this.maxUses, this.villagerXp, this.priceMultiplier);
/*      */     } }
/*      */   private static final class TypeSpecificTrade extends Record implements ItemListing { private final Map<ResourceKey<VillagerType>, VillagerTrades.ItemListing> trades;
/*      */     
/*  957 */     private TypeSpecificTrade(Map<ResourceKey<VillagerType>, VillagerTrades.ItemListing> trades) { this.trades = trades; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/npc/villager/VillagerTrades$TypeSpecificTrade;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #957	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*  957 */       //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerTrades$TypeSpecificTrade; } public Map<ResourceKey<VillagerType>, VillagerTrades.ItemListing> trades() { return this.trades; }
/*      */     public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/npc/villager/VillagerTrades$TypeSpecificTrade;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #957	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerTrades$TypeSpecificTrade; }
/*      */     public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/npc/villager/VillagerTrades$TypeSpecificTrade;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #957	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerTrades$TypeSpecificTrade;
/*      */       //   0	8	1	o	Ljava/lang/Object; } @SafeVarargs
/*      */     public static TypeSpecificTrade oneTradeInBiomes(VillagerTrades.ItemListing trade, ResourceKey<VillagerType>... villageTypes) {
/*  961 */       return new TypeSpecificTrade((Map<ResourceKey<VillagerType>, VillagerTrades.ItemListing>)Arrays.<ResourceKey<VillagerType>>stream(villageTypes).collect(Collectors.toMap(villageType -> villageType, villageType -> trade)));
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/*  966 */       if (merchant instanceof VillagerDataHolder) { VillagerDataHolder holder = (VillagerDataHolder)merchant;
/*  967 */         ResourceKey<VillagerType> typeKey = holder.getVillagerData().type().unwrapKey().orElse(null);
/*  968 */         if (typeKey == null) {
/*  969 */           return null;
/*      */         }
/*  971 */         VillagerTrades.ItemListing itemListing = this.trades.get(typeKey);
/*  972 */         if (itemListing == null) {
/*  973 */           return null;
/*      */         }
/*  975 */         return itemListing.getOffer(serverLevel, merchant, random); }
/*      */       
/*  977 */       return null;
/*      */     } }
/*      */ 
/*      */   
/*      */   private static class EmeraldsForVillagerTypeItem implements ItemListing {
/*      */     private final Map<ResourceKey<VillagerType>, Item> trades;
/*      */     private final int cost;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     
/*      */     public EmeraldsForVillagerTypeItem(int cost, int maxUses, int villagerXp, Map<ResourceKey<VillagerType>, Item> trades) {
/*  988 */       BuiltInRegistries.VILLAGER_TYPE.registryKeySet().stream().filter(t -> !trades.containsKey(t)).findAny().ifPresent(t -> {
/*      */             throw new IllegalStateException("Missing trade for villager type: " + String.valueOf(t));
/*      */           });
/*  991 */       this.trades = trades;
/*      */       
/*  993 */       this.cost = cost;
/*  994 */       this.maxUses = maxUses;
/*  995 */       this.villagerXp = villagerXp;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1000 */       if (merchant instanceof VillagerDataHolder) { VillagerDataHolder holder = (VillagerDataHolder)merchant;
/* 1001 */         ResourceKey<VillagerType> type = holder.getVillagerData().type().unwrapKey().orElse(null);
/* 1002 */         if (type == null) {
/* 1003 */           return null;
/*      */         }
/* 1005 */         ItemCost cost = new ItemCost((ItemLike)this.trades.get(type), this.cost);
/* 1006 */         return new MerchantOffer(cost, new ItemStack((ItemLike)Items.EMERALD), this.maxUses, this.villagerXp, 0.05F); }
/*      */       
/* 1008 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ItemsForEmeralds implements ItemListing {
/*      */     private final ItemStack itemStack;
/*      */     private final int emeraldCost;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     private final float priceMultiplier;
/*      */     private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;
/*      */     
/*      */     public ItemsForEmeralds(Block block, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
/* 1021 */       this(new ItemStack((ItemLike)block), emeraldCost, numberOfItems, maxUses, villagerXp);
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int villagerXp) {
/* 1025 */       this(new ItemStack((ItemLike)item), emeraldCost, numberOfItems, 12, villagerXp);
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
/* 1029 */       this(new ItemStack((ItemLike)item), emeraldCost, numberOfItems, maxUses, villagerXp);
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
/* 1033 */       this(itemStack, emeraldCost, numberOfItems, maxUses, villagerXp, 0.05F);
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
/* 1037 */       this(new ItemStack((ItemLike)item), emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier);
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(Item item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider) {
/* 1041 */       this(new ItemStack((ItemLike)item), emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier) {
/* 1045 */       this(itemStack, emeraldCost, numberOfItems, maxUses, villagerXp, priceMultiplier, Optional.empty());
/*      */     }
/*      */     
/*      */     public ItemsForEmeralds(ItemStack itemStack, int emeraldCost, int numberOfItems, int maxUses, int villagerXp, float priceMultiplier, Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider) {
/* 1049 */       this.itemStack = itemStack;
/* 1050 */       this.emeraldCost = emeraldCost;
/* 1051 */       this.itemStack.setCount(numberOfItems);
/* 1052 */       this.maxUses = maxUses;
/* 1053 */       this.villagerXp = villagerXp;
/* 1054 */       this.priceMultiplier = priceMultiplier;
/* 1055 */       this.enchantmentProvider = enchantmentProvider;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1060 */       ItemStack item = this.itemStack.copy();
/* 1061 */       this.enchantmentProvider.ifPresent(provider -> EnchantmentHelper.enchantItemFromProvider(item, serverLevel.registryAccess(), provider, serverLevel.getCurrentDifficultyAt(merchant.blockPosition()), random));
/* 1062 */       return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, this.emeraldCost), item, this.maxUses, this.villagerXp, this.priceMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SuspiciousStewForEmerald implements ItemListing {
/*      */     private final SuspiciousStewEffects effects;
/*      */     private final int xp;
/*      */     private final float priceMultiplier;
/*      */     
/*      */     public SuspiciousStewForEmerald(Holder<MobEffect> effect, int duration, int xp) {
/* 1072 */       this(new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(effect, duration))), xp, 0.05F);
/*      */     }
/*      */     
/*      */     public SuspiciousStewForEmerald(SuspiciousStewEffects effects, int xp, float priceMultiplier) {
/* 1076 */       this.effects = effects;
/* 1077 */       this.xp = xp;
/* 1078 */       this.priceMultiplier = priceMultiplier;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1083 */       ItemStack stew = new ItemStack((ItemLike)Items.SUSPICIOUS_STEW, 1);
/* 1084 */       stew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, this.effects);
/* 1085 */       return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD), stew, 12, this.xp, this.priceMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */   private static ItemCost potionCost(Holder<Potion> potion) {
/* 1090 */     return new ItemCost((ItemLike)Items.POTION)
/* 1091 */       .withComponents(b -> b.expect(DataComponents.POTION_CONTENTS, new PotionContents(potion)));
/*      */   }
/*      */   
/*      */   private static ItemStack potion(Holder<Potion> potion) {
/* 1095 */     return PotionContents.createItemStack(Items.POTION, potion);
/*      */   }
/*      */   
/*      */   private static class EnchantedItemForEmeralds implements ItemListing {
/*      */     private final ItemStack itemStack;
/*      */     private final int baseEmeraldCost;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     private final float priceMultiplier;
/*      */     
/*      */     public EnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int villagerXp) {
/* 1106 */       this(item, baseEmeraldCost, maxUses, villagerXp, 0.05F);
/*      */     }
/*      */     
/*      */     public EnchantedItemForEmeralds(Item item, int baseEmeraldCost, int maxUses, int villagerXp, float priceMultiplier) {
/* 1110 */       this.itemStack = new ItemStack((ItemLike)item);
/* 1111 */       this.baseEmeraldCost = baseEmeraldCost;
/* 1112 */       this.maxUses = maxUses;
/* 1113 */       this.villagerXp = villagerXp;
/* 1114 */       this.priceMultiplier = priceMultiplier;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1119 */       int enchantmentCost = 5 + random.nextInt(15);
/* 1120 */       RegistryAccess registryAccess = serverLevel.registryAccess();
/* 1121 */       Optional<HolderSet.Named<Enchantment>> tag = registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.ON_TRADED_EQUIPMENT);
/* 1122 */       ItemStack resultItemStack = EnchantmentHelper.enchantItem(random, new ItemStack((ItemLike)this.itemStack.getItem()), enchantmentCost, registryAccess, tag);
/* 1123 */       int totalCost = Math.min(this.baseEmeraldCost + enchantmentCost, 64);
/* 1124 */       ItemCost emeraldCost = new ItemCost((ItemLike)Items.EMERALD, totalCost);
/*      */       
/* 1126 */       return new MerchantOffer(emeraldCost, resultItemStack, this.maxUses, this.villagerXp, this.priceMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TippedArrowForItemsAndEmeralds implements ItemListing {
/*      */     private final ItemStack toItem;
/*      */     private final int toCount;
/*      */     private final int emeraldCost;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     private final Item fromItem;
/*      */     private final int fromCount;
/*      */     private final float priceMultiplier;
/*      */     
/*      */     public TippedArrowForItemsAndEmeralds(Item fromItem, int fromCount, Item toItem, int toCount, int emeraldCost, int maxUses, int villagerXp) {
/* 1141 */       this.toItem = new ItemStack((ItemLike)toItem);
/* 1142 */       this.emeraldCost = emeraldCost;
/* 1143 */       this.maxUses = maxUses;
/* 1144 */       this.villagerXp = villagerXp;
/* 1145 */       this.fromItem = fromItem;
/* 1146 */       this.fromCount = fromCount;
/* 1147 */       this.toCount = toCount;
/* 1148 */       this.priceMultiplier = 0.05F;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1153 */       ItemCost emeralds = new ItemCost((ItemLike)Items.EMERALD, this.emeraldCost);
/*      */       
/* 1155 */       List<Holder<Potion>> potions = (List<Holder<Potion>>)BuiltInRegistries.POTION.listElements().filter(potion -> 
/* 1156 */           (!((Potion)potion.value()).getEffects().isEmpty() && serverLevel.potionBrewing().isBrewablePotion((Holder)potion)))
/*      */         
/* 1158 */         .collect(Collectors.toList());
/* 1159 */       Holder<Potion> potion = (Holder<Potion>)Util.getRandom(potions, random);
/* 1160 */       ItemStack resultItemStack = new ItemStack((ItemLike)this.toItem.getItem(), this.toCount);
/* 1161 */       resultItemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
/*      */       
/* 1163 */       return new MerchantOffer(emeralds, Optional.of(new ItemCost((ItemLike)this.fromItem, this.fromCount)), resultItemStack, this.maxUses, this.villagerXp, this.priceMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class DyedArmorForEmeralds implements ItemListing {
/*      */     private final Item item;
/*      */     private final int value;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     
/*      */     public DyedArmorForEmeralds(Item item, int value) {
/* 1174 */       this(item, value, 12, 1);
/*      */     }
/*      */     
/*      */     public DyedArmorForEmeralds(Item item, int value, int maxUses, int villagerXp) {
/* 1178 */       this.item = item;
/* 1179 */       this.value = value;
/* 1180 */       this.maxUses = maxUses;
/* 1181 */       this.villagerXp = villagerXp;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1186 */       ItemCost emeraldCost = new ItemCost((ItemLike)Items.EMERALD, this.value);
/* 1187 */       ItemStack resultItemStack = new ItemStack((ItemLike)this.item);
/*      */       
/* 1189 */       if (resultItemStack.is(ItemTags.DYEABLE)) {
/* 1190 */         List<DyeItem> dyes = Lists.newArrayList();
/* 1191 */         dyes.add(getRandomDye(random));
/*      */         
/* 1193 */         if (random.nextFloat() > 0.7F) {
/* 1194 */           dyes.add(getRandomDye(random));
/*      */         }
/*      */         
/* 1197 */         if (random.nextFloat() > 0.8F) {
/* 1198 */           dyes.add(getRandomDye(random));
/*      */         }
/*      */         
/* 1201 */         resultItemStack = DyedItemColor.applyDyes(resultItemStack, dyes);
/*      */       } 
/*      */       
/* 1204 */       return new MerchantOffer(emeraldCost, resultItemStack, this.maxUses, this.villagerXp, 0.2F);
/*      */     }
/*      */     
/*      */     private static DyeItem getRandomDye(RandomSource random) {
/* 1208 */       return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
/*      */     }
/*      */   }
/*      */   
/*      */   private static class EnchantBookForEmeralds
/*      */     implements ItemListing {
/*      */     private final int villagerXp;
/*      */     private final TagKey<Enchantment> tradeableEnchantments;
/*      */     private final int minLevel;
/*      */     private final int maxLevel;
/*      */     
/*      */     public EnchantBookForEmeralds(int villagerXp, TagKey<Enchantment> enchantments) {
/* 1220 */       this(villagerXp, 0, Integer.MAX_VALUE, enchantments);
/*      */     }
/*      */ 
/*      */     
/*      */     public EnchantBookForEmeralds(int villagerXp, int minLevel, int maxLevel, TagKey<Enchantment> enchantments) {
/* 1225 */       this.minLevel = minLevel;
/* 1226 */       this.maxLevel = maxLevel;
/* 1227 */       this.villagerXp = villagerXp;
/* 1228 */       this.tradeableEnchantments = enchantments;
/*      */     }
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/*      */       int cost;
/*      */       ItemStack book;
/* 1233 */       Optional<Holder<Enchantment>> selected = serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getRandomElementOf(this.tradeableEnchantments, random);
/*      */ 
/*      */       
/* 1236 */       if (!selected.isEmpty()) {
/* 1237 */         Holder<Enchantment> holder = selected.get();
/* 1238 */         Enchantment enchantment = (Enchantment)holder.value();
/* 1239 */         int min = Math.max(enchantment.getMinLevel(), this.minLevel);
/* 1240 */         int max = Math.min(enchantment.getMaxLevel(), this.maxLevel);
/* 1241 */         int level = Mth.nextInt(random, min, max);
/* 1242 */         book = EnchantmentHelper.createBook(new EnchantmentInstance(holder, level));
/* 1243 */         cost = 2 + random.nextInt(5 + level * 10) + 3 * level;
/* 1244 */         if (holder.is(EnchantmentTags.DOUBLE_TRADE_PRICE)) {
/* 1245 */           cost *= 2;
/*      */         }
/* 1247 */         if (cost > 64) {
/* 1248 */           cost = 64;
/*      */         }
/*      */       } else {
/* 1251 */         cost = 1;
/* 1252 */         book = new ItemStack((ItemLike)Items.BOOK);
/*      */       } 
/* 1254 */       return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, cost), Optional.of(new ItemCost((ItemLike)Items.BOOK)), book, 12, this.villagerXp, 0.2F);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FailureItemListing
/*      */     implements ItemListing {
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1261 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TreasureMapForEmeralds implements ItemListing {
/*      */     private final int emeraldCost;
/*      */     private final TagKey<Structure> destination;
/*      */     private final String displayName;
/*      */     private final Holder<MapDecorationType> destinationType;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     
/*      */     public TreasureMapForEmeralds(int value, TagKey<Structure> destination, String displayName, Holder<MapDecorationType> destinationType, int maxUses, int villagerXp) {
/* 1274 */       this.emeraldCost = value;
/* 1275 */       this.destination = destination;
/* 1276 */       this.displayName = displayName;
/* 1277 */       this.destinationType = destinationType;
/* 1278 */       this.maxUses = maxUses;
/* 1279 */       this.villagerXp = villagerXp;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1285 */       BlockPos nearest = serverLevel.findNearestMapStructure(this.destination, merchant.blockPosition(), 100, true);
/* 1286 */       if (nearest != null) {
/* 1287 */         ItemStack map = MapItem.create(serverLevel, nearest.getX(), nearest.getZ(), (byte)2, true, true);
/* 1288 */         MapItem.renderBiomePreviewMap(serverLevel, map);
/* 1289 */         MapItemSavedData.addTargetDecoration(map, nearest, "+", this.destinationType);
/* 1290 */         map.set(DataComponents.ITEM_NAME, Component.translatable(this.displayName));
/* 1291 */         return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, this.emeraldCost), Optional.of(new ItemCost((ItemLike)Items.COMPASS)), map, this.maxUses, this.villagerXp, 0.2F);
/*      */       } 
/* 1293 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ItemsAndEmeraldsToItems implements ItemListing {
/*      */     private final ItemCost fromItem;
/*      */     private final int emeraldCost;
/*      */     private final ItemStack toItem;
/*      */     private final int maxUses;
/*      */     private final int villagerXp;
/*      */     private final float priceMultiplier;
/*      */     private final Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider;
/*      */     
/*      */     public ItemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, Item toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
/* 1307 */       this(fromItem, fromCount, emeraldCost, new ItemStack((ItemLike)toItem), toCount, maxUses, villagerXp, priceMultiplier);
/*      */     }
/*      */     
/*      */     private ItemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, ItemStack toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
/* 1311 */       this(new ItemCost(fromItem, fromCount), emeraldCost, toItem.copyWithCount(toCount), maxUses, villagerXp, priceMultiplier, Optional.empty());
/*      */     }
/*      */     
/*      */     private ItemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, ItemLike toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier, ResourceKey<EnchantmentProvider> enchantmentProvider) {
/* 1315 */       this(new ItemCost(fromItem, fromCount), emeraldCost, new ItemStack(toItem, toCount), maxUses, villagerXp, priceMultiplier, Optional.of(enchantmentProvider));
/*      */     }
/*      */     
/*      */     public ItemsAndEmeraldsToItems(ItemCost fromItem, int emeraldCost, ItemStack toItem, int maxUses, int villagerXp, float priceMultiplier, Optional<ResourceKey<EnchantmentProvider>> enchantmentProvider) {
/* 1319 */       this.fromItem = fromItem;
/* 1320 */       this.emeraldCost = emeraldCost;
/* 1321 */       this.toItem = toItem;
/* 1322 */       this.maxUses = maxUses;
/* 1323 */       this.villagerXp = villagerXp;
/* 1324 */       this.priceMultiplier = priceMultiplier;
/* 1325 */       this.enchantmentProvider = enchantmentProvider;
/*      */     }
/*      */ 
/*      */     
/*      */     public MerchantOffer getOffer(ServerLevel serverLevel, Entity merchant, RandomSource random) {
/* 1330 */       ItemStack item = this.toItem.copy();
/* 1331 */       this.enchantmentProvider.ifPresent(provider -> EnchantmentHelper.enchantItemFromProvider(item, serverLevel.registryAccess(), provider, serverLevel.getCurrentDifficultyAt(merchant.blockPosition()), random));
/* 1332 */       return new MerchantOffer(new ItemCost((ItemLike)Items.EMERALD, this.emeraldCost), Optional.of(this.fromItem), item, 0, this.maxUses, this.villagerXp, this.priceMultiplier);
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface ItemListing {
/*      */     MerchantOffer getOffer(ServerLevel param1ServerLevel, Entity param1Entity, RandomSource param1RandomSource);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/npc/villager/VillagerTrades.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */