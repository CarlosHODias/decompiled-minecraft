/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CopperBulbBlock;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AxisAlignedLinearPosTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class ProcessorLists {
/*  38 */   private static final ResourceKey<StructureProcessorList> EMPTY = createKey("empty");
/*     */   
/*  40 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_PLAINS = createKey("zombie_plains");
/*  41 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_SAVANNA = createKey("zombie_savanna");
/*  42 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_SNOWY = createKey("zombie_snowy");
/*  43 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_TAIGA = createKey("zombie_taiga");
/*  44 */   public static final ResourceKey<StructureProcessorList> ZOMBIE_DESERT = createKey("zombie_desert");
/*  45 */   public static final ResourceKey<StructureProcessorList> MOSSIFY_10_PERCENT = createKey("mossify_10_percent");
/*  46 */   public static final ResourceKey<StructureProcessorList> MOSSIFY_20_PERCENT = createKey("mossify_20_percent");
/*  47 */   public static final ResourceKey<StructureProcessorList> MOSSIFY_70_PERCENT = createKey("mossify_70_percent");
/*  48 */   public static final ResourceKey<StructureProcessorList> STREET_PLAINS = createKey("street_plains");
/*  49 */   public static final ResourceKey<StructureProcessorList> STREET_SAVANNA = createKey("street_savanna");
/*  50 */   public static final ResourceKey<StructureProcessorList> STREET_SNOWY_OR_TAIGA = createKey("street_snowy_or_taiga");
/*  51 */   public static final ResourceKey<StructureProcessorList> FARM_PLAINS = createKey("farm_plains");
/*  52 */   public static final ResourceKey<StructureProcessorList> FARM_SAVANNA = createKey("farm_savanna");
/*  53 */   public static final ResourceKey<StructureProcessorList> FARM_SNOWY = createKey("farm_snowy");
/*  54 */   public static final ResourceKey<StructureProcessorList> FARM_TAIGA = createKey("farm_taiga");
/*  55 */   public static final ResourceKey<StructureProcessorList> FARM_DESERT = createKey("farm_desert");
/*  56 */   public static final ResourceKey<StructureProcessorList> OUTPOST_ROT = createKey("outpost_rot");
/*  57 */   public static final ResourceKey<StructureProcessorList> BOTTOM_RAMPART = createKey("bottom_rampart");
/*  58 */   public static final ResourceKey<StructureProcessorList> TREASURE_ROOMS = createKey("treasure_rooms");
/*  59 */   public static final ResourceKey<StructureProcessorList> HOUSING = createKey("housing");
/*  60 */   public static final ResourceKey<StructureProcessorList> SIDE_WALL_DEGRADATION = createKey("side_wall_degradation");
/*  61 */   public static final ResourceKey<StructureProcessorList> STABLE_DEGRADATION = createKey("stable_degradation");
/*  62 */   public static final ResourceKey<StructureProcessorList> BASTION_GENERIC_DEGRADATION = createKey("bastion_generic_degradation");
/*  63 */   public static final ResourceKey<StructureProcessorList> RAMPART_DEGRADATION = createKey("rampart_degradation");
/*  64 */   public static final ResourceKey<StructureProcessorList> ENTRANCE_REPLACEMENT = createKey("entrance_replacement");
/*  65 */   public static final ResourceKey<StructureProcessorList> BRIDGE = createKey("bridge");
/*  66 */   public static final ResourceKey<StructureProcessorList> ROOF = createKey("roof");
/*  67 */   public static final ResourceKey<StructureProcessorList> HIGH_WALL = createKey("high_wall");
/*  68 */   public static final ResourceKey<StructureProcessorList> HIGH_RAMPART = createKey("high_rampart");
/*  69 */   public static final ResourceKey<StructureProcessorList> FOSSIL_ROT = createKey("fossil_rot");
/*  70 */   public static final ResourceKey<StructureProcessorList> FOSSIL_COAL = createKey("fossil_coal");
/*  71 */   public static final ResourceKey<StructureProcessorList> FOSSIL_DIAMONDS = createKey("fossil_diamonds");
/*  72 */   public static final ResourceKey<StructureProcessorList> ANCIENT_CITY_START_DEGRADATION = createKey("ancient_city_start_degradation");
/*  73 */   public static final ResourceKey<StructureProcessorList> ANCIENT_CITY_GENERIC_DEGRADATION = createKey("ancient_city_generic_degradation");
/*  74 */   public static final ResourceKey<StructureProcessorList> ANCIENT_CITY_WALLS_DEGRADATION = createKey("ancient_city_walls_degradation");
/*  75 */   public static final ResourceKey<StructureProcessorList> TRAIL_RUINS_HOUSES_ARCHAEOLOGY = createKey("trail_ruins_houses_archaeology");
/*  76 */   public static final ResourceKey<StructureProcessorList> TRAIL_RUINS_ROADS_ARCHAEOLOGY = createKey("trail_ruins_roads_archaeology");
/*  77 */   public static final ResourceKey<StructureProcessorList> TRAIL_RUINS_TOWER_TOP_ARCHAEOLOGY = createKey("trail_ruins_tower_top_archaeology");
/*  78 */   public static final ResourceKey<StructureProcessorList> TRIAL_CHAMBERS_COPPER_BULB_DEGRADATION = createKey("trial_chambers_copper_bulb_degradation");
/*     */   
/*     */   private static ResourceKey<StructureProcessorList> createKey(String name) {
/*  81 */     return ResourceKey.create(Registries.PROCESSOR_LIST, net.minecraft.resources.Identifier.withDefaultNamespace(name));
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<StructureProcessorList> context, ResourceKey<StructureProcessorList> id, List<StructureProcessor> processors) {
/*  85 */     context.register(id, new StructureProcessorList(processors));
/*     */   }
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
/*  89 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/*     */     
/*  91 */     ProcessorRule ADD_GILDED_BLACKSTONE = new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.GILDED_BLACKSTONE.defaultBlockState());
/*  92 */     ProcessorRule REMOVE_GILDED_BLACKSTONE = new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GILDED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE.defaultBlockState());
/*     */     
/*  94 */     register(context, EMPTY, (List<StructureProcessor>)ImmutableList.of());
/*     */     
/*  96 */     register(context, ZOMBIE_PLAINS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/*  97 */                 Blocks.MOSSY_COBBLESTONE.defaultBlockState()), new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, 
/*  98 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/*  99 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 100 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 101 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MOSSY_COBBLESTONE, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 102 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHITE_TERRACOTTA, 0.07F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 103 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 104 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_PLANKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 105 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.OAK_STAIRS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 106 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_OAK_LOG, 0.02F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 107 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 108 */                 Blocks.COBWEB.defaultBlockState()), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 109 */                     Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 110 */                     Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 111 */                   Blocks.CARROTS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 112 */                   Blocks.POTATOES.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 113 */                   Blocks.BEETROOTS.defaultBlockState()) }))));
/*     */ 
/*     */     
/* 116 */     register(context, ZOMBIE_SAVANNA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 117 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 118 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 119 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_PLANKS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 120 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_STAIRS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 121 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 122 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ACACIA_WOOD, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 123 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.ORANGE_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 124 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.YELLOW_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 125 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.RED_TERRACOTTA, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 126 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 127 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 128 */                   Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 129 */                     Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 130 */                   Blocks.MELON_STEM.defaultBlockState()) }))));
/*     */ 
/*     */     
/* 133 */     register(context, ZOMBIE_SNOWY, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 134 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 135 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 136 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.LANTERN), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 137 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_PLANKS, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 138 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_SLAB, 0.4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 139 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_SPRUCE_LOG, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 140 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.STRIPPED_SPRUCE_WOOD, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 141 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 142 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 143 */                   Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 144 */                   Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 145 */                 Blocks.CARROTS.defaultBlockState()), (Object[])new ProcessorRule[] { new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 146 */                   Blocks.POTATOES.defaultBlockState()) }))));
/*     */ 
/*     */     
/* 149 */     register(context, ZOMBIE_TAIGA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 150 */                 Blocks.MOSSY_COBBLESTONE.defaultBlockState()), new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 151 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 152 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 153 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.CAMPFIRE), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)
/* 154 */                 Blocks.CAMPFIRE.defaultBlockState().setValue((Property)net.minecraft.world.level.block.CampfireBlock.LIT, false)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 155 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SPRUCE_LOG, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 156 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GLASS_PANE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 157 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 158 */                   Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, true)).setValue((Property)IronBarsBlock.SOUTH, true)), new ProcessorRule((RuleTest)new BlockStateMatchTest((BlockState)((BlockState)
/* 159 */                   Blocks.GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)((BlockState)Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState().setValue((Property)IronBarsBlock.EAST, true)).setValue((Property)IronBarsBlock.WEST, true)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 160 */                 Blocks.PUMPKIN_STEM.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 161 */                 Blocks.POTATOES.defaultBlockState()), (Object[])new ProcessorRule[0]))));
/*     */ 
/*     */     
/* 164 */     register(context, ZOMBIE_DESERT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.DOORS), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 165 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 166 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.WALL_TORCH), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 167 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 168 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CUT_SANDSTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 169 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.TERRACOTTA, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 170 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE_STAIRS, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 171 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SMOOTH_SANDSTONE_SLAB, 0.08F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 172 */                 Blocks.COBWEB.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 173 */                 Blocks.BEETROOTS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 174 */                 Blocks.MELON_STEM.defaultBlockState())))));
/*     */ 
/*     */     
/* 177 */     register(context, MOSSIFY_10_PERCENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 178 */                 Blocks.MOSSY_COBBLESTONE.defaultBlockState())))));
/*     */ 
/*     */     
/* 181 */     register(context, MOSSIFY_20_PERCENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 182 */                 Blocks.MOSSY_COBBLESTONE.defaultBlockState())))));
/*     */ 
/*     */     
/* 185 */     register(context, MOSSIFY_70_PERCENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.COBBLESTONE, 0.7F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 186 */                 Blocks.MOSSY_COBBLESTONE.defaultBlockState())))));
/*     */ 
/*     */     
/* 189 */     register(context, STREET_PLAINS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 190 */                 Blocks.OAK_PLANKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 191 */                 Blocks.GRASS_BLOCK.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 192 */                 Blocks.WATER.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 193 */                 Blocks.WATER.defaultBlockState())))));
/*     */ 
/*     */     
/* 196 */     register(context, STREET_SAVANNA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 197 */                 Blocks.ACACIA_PLANKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 198 */                 Blocks.GRASS_BLOCK.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 199 */                 Blocks.WATER.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 200 */                 Blocks.WATER.defaultBlockState())))));
/*     */ 
/*     */     
/* 203 */     register(context, STREET_SNOWY_OR_TAIGA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 204 */                 Blocks.SPRUCE_PLANKS.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT_PATH), (RuleTest)new BlockMatchTest(Blocks.ICE), 
/* 205 */                 Blocks.SPRUCE_PLANKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 206 */                 Blocks.GRASS_BLOCK.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.GRASS_BLOCK), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 207 */                 Blocks.WATER.defaultBlockState()), new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.DIRT), (RuleTest)new BlockMatchTest(Blocks.WATER), 
/* 208 */                 Blocks.WATER.defaultBlockState())))));
/*     */ 
/*     */     
/* 211 */     register(context, FARM_PLAINS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 212 */                 Blocks.CARROTS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 213 */                 Blocks.POTATOES.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 214 */                 Blocks.BEETROOTS.defaultBlockState())))));
/*     */ 
/*     */     
/* 217 */     register(context, FARM_SAVANNA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 218 */                 Blocks.MELON_STEM.defaultBlockState())))));
/*     */ 
/*     */     
/* 221 */     register(context, FARM_SNOWY, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 222 */                 Blocks.CARROTS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.8F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 223 */                 Blocks.POTATOES.defaultBlockState())))));
/*     */ 
/*     */     
/* 226 */     register(context, FARM_TAIGA, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 227 */                 Blocks.PUMPKIN_STEM.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 228 */                 Blocks.POTATOES.defaultBlockState())))));
/*     */ 
/*     */     
/* 231 */     register(context, FARM_DESERT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 232 */                 Blocks.BEETROOTS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WHEAT, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 233 */                 Blocks.MELON_STEM.defaultBlockState())))));
/*     */ 
/*     */     
/* 236 */     register(context, OUTPOST_ROT, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.05F)));
/*     */     
/* 238 */     register(context, BOTTOM_RAMPART, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MAGMA_BLOCK, 0.75F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 239 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.15F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 240 */                 Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     register(context, TREASURE_ROOMS, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.35F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 246 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 247 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     register(context, HOUSING, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 253 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 254 */                 Blocks.AIR.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     register(context, SIDE_WALL_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 260 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 261 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     register(context, STABLE_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 267 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 268 */                 Blocks.AIR.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     register(context, BASTION_GENERIC_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 274 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 275 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 276 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     register(context, RAMPART_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 282 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 283 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 284 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 285 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 286 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     register(context, ENTRANCE_REPLACEMENT, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.CHISELED_POLISHED_BLACKSTONE, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 292 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.6F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 293 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), REMOVE_GILDED_BLACKSTONE, ADD_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     register(context, BRIDGE, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 299 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.BLACKSTONE, 1.0E-4F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 300 */                 Blocks.AIR.defaultBlockState())))));
/*     */ 
/*     */     
/* 303 */     register(context, ROOF, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 304 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.15F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 305 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 306 */                 Blocks.BLACKSTONE.defaultBlockState())))));
/*     */ 
/*     */     
/* 309 */     register(context, HIGH_WALL, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.01F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 310 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 311 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.POLISHED_BLACKSTONE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 312 */                 Blocks.BLACKSTONE.defaultBlockState()), REMOVE_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */     
/* 316 */     register(context, HIGH_RAMPART, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor((List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GOLD_BLOCK, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 317 */                 Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)AlwaysTrueTest.INSTANCE, (RuleTest)AlwaysTrueTest.INSTANCE, (PosRuleTest)new AxisAlignedLinearPosTest(0.0F, 0.05F, 0, 100, net.minecraft.core.Direction.Axis.Y), 
/* 318 */                 Blocks.AIR.defaultBlockState()), REMOVE_GILDED_BLACKSTONE))));
/*     */ 
/*     */ 
/*     */     
/* 322 */     register(context, FOSSIL_ROT, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.9F), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     register(context, FOSSIL_COAL, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.1F), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     register(context, FOSSIL_DIAMONDS, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor(0.1F), new RuleProcessor(
/*     */             
/* 334 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new BlockMatchTest(Blocks.COAL_ORE), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 335 */                 Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     register(context, ANCIENT_CITY_START_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new RuleProcessor(
/* 341 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 342 */                 Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILES, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 343 */                 Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SOUL_LANTERN, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 344 */                 Blocks.AIR.defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     register(context, ANCIENT_CITY_GENERIC_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor((HolderSet)
/* 350 */             blocks.getOrThrow(BlockTags.ANCIENT_CITY_REPLACEABLE), 0.95F), new RuleProcessor(
/* 351 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 352 */                 Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILES, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 353 */                 Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SOUL_LANTERN, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 354 */                 Blocks.AIR.defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     register(context, ANCIENT_CITY_WALLS_DEGRADATION, (List<StructureProcessor>)ImmutableList.of(new BlockRotProcessor((HolderSet)
/* 360 */             blocks.getOrThrow(BlockTags.ANCIENT_CITY_REPLACEABLE), 0.95F), new RuleProcessor(
/* 361 */             (List)ImmutableList.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_BRICKS, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 362 */                 Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILES, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 363 */                 Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.DEEPSLATE_TILE_SLAB, 0.3F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 364 */                 Blocks.AIR.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.SOUL_LANTERN, 0.05F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 365 */                 Blocks.AIR.defaultBlockState()))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 370 */     register(context, TRAIL_RUINS_HOUSES_ARCHAEOLOGY, (List)List.of(new RuleProcessor(
/* 371 */             List.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 372 */                 Blocks.DIRT.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 373 */                 Blocks.COARSE_DIRT.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MUD_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 374 */                 Blocks.PACKED_MUD.defaultBlockState()))), 
/*     */           
/* 376 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 6), 
/* 377 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, 3)));
/*     */ 
/*     */     
/* 380 */     register(context, TRAIL_RUINS_ROADS_ARCHAEOLOGY, (List)List.of(new RuleProcessor(
/* 381 */             List.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.2F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 382 */                 Blocks.DIRT.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.GRAVEL, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 383 */                 Blocks.COARSE_DIRT.defaultBlockState()), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.MUD_BRICKS, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, 
/* 384 */                 Blocks.PACKED_MUD.defaultBlockState()))), 
/*     */           
/* 386 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 2)));
/*     */ 
/*     */     
/* 389 */     register(context, TRAIL_RUINS_TOWER_TOP_ARCHAEOLOGY, (List)List.of(
/* 390 */           trailsArchyLootProcessor(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON, 2)));
/*     */ 
/*     */     
/* 393 */     register(context, TRIAL_CHAMBERS_COPPER_BULB_DEGRADATION, (List)List.of(new RuleProcessor(
/* 394 */             List.of(new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WAXED_COPPER_BULB, 0.1F), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)
/* 395 */                 Blocks.WAXED_OXIDIZED_COPPER_BULB.defaultBlockState().setValue((Property)CopperBulbBlock.LIT, true)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WAXED_COPPER_BULB, 0.33333334F), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)
/* 396 */                 Blocks.WAXED_WEATHERED_COPPER_BULB.defaultBlockState().setValue((Property)CopperBulbBlock.LIT, true)), new ProcessorRule((RuleTest)new RandomBlockMatchTest(Blocks.WAXED_COPPER_BULB, 0.5F), (RuleTest)AlwaysTrueTest.INSTANCE, (BlockState)
/* 397 */                 Blocks.WAXED_EXPOSED_COPPER_BULB.defaultBlockState().setValue((Property)CopperBulbBlock.LIT, true)))), new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CappedProcessor trailsArchyLootProcessor(ResourceKey<LootTable> lootTable, int count) {
/* 404 */     return new CappedProcessor((StructureProcessor)new RuleProcessor(
/* 405 */           List.of(new ProcessorRule((RuleTest)new TagMatchTest(BlockTags.TRAIL_RUINS_REPLACEABLE), (RuleTest)AlwaysTrueTest.INSTANCE, (PosRuleTest)PosAlwaysTrueTest.INSTANCE, 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 410 */               Blocks.SUSPICIOUS_GRAVEL.defaultBlockState(), (RuleBlockEntityModifier)new AppendLoot(lootTable)))), 
/*     */ 
/*     */ 
/*     */         
/* 414 */         (IntProvider)net.minecraft.util.valueproviders.ConstantInt.of(count));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/ProcessorLists.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */