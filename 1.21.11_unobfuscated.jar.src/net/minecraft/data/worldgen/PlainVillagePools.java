/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.VillagePlacements;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ 
/*     */ public class PlainVillagePools {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/plains/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/plains/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  20 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  21 */     Holder.Reference reference1 = placedFeatures.getOrThrow(VillagePlacements.OAK_VILLAGE);
/*  22 */     Holder.Reference reference2 = placedFeatures.getOrThrow(VillagePlacements.FLOWER_PLAIN_VILLAGE);
/*  23 */     Holder.Reference reference3 = placedFeatures.getOrThrow(VillagePlacements.PILE_HAY_VILLAGE);
/*     */     
/*  25 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  26 */     Holder.Reference reference4 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_10_PERCENT);
/*  27 */     Holder.Reference reference5 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_20_PERCENT);
/*  28 */     Holder.Reference reference6 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_70_PERCENT);
/*  29 */     Holder.Reference reference7 = processorLists.getOrThrow(ProcessorLists.ZOMBIE_PLAINS);
/*  30 */     Holder.Reference reference8 = processorLists.getOrThrow(ProcessorLists.STREET_PLAINS);
/*  31 */     Holder.Reference reference9 = processorLists.getOrThrow(ProcessorLists.FARM_PLAINS);
/*     */     
/*  33 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  34 */     Holder.Reference reference10 = pools.getOrThrow(Pools.EMPTY);
/*  35 */     Holder.Reference reference11 = pools.getOrThrow(TERMINATORS_KEY);
/*     */     
/*  37 */     context.register(START, new StructureTemplatePool((Holder)reference10, 
/*     */           
/*  39 */           (List)ImmutableList.of(
/*  40 */             Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_fountain_01", (Holder)reference5), 50), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_1", (Holder)reference5), 50), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_2"), 50), 
/*  43 */             Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_3", (Holder)reference6), 50), 
/*  44 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_fountain_01", (Holder)reference7), 1), 
/*  45 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_1", (Holder)reference7), 1), 
/*  46 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_2", (Holder)reference7), 1), 
/*  47 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_3", (Holder)reference7), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     Pools.register(context, "village/plains/streets", new StructureTemplatePool((Holder)reference11, 
/*     */           
/*  54 */           (List)ImmutableList.of(
/*  55 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/corner_01", (Holder)reference8), 2), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/corner_02", (Holder)reference8), 2), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/corner_03", (Holder)reference8), 2), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_01", (Holder)reference8), 4), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_02", (Holder)reference8), 4), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_03", (Holder)reference8), 7), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_04", (Holder)reference8), 7), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_05", (Holder)reference8), 3), 
/*  63 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_06", (Holder)reference8), 4), 
/*  64 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_01", (Holder)reference8), 2), 
/*  65 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_02", (Holder)reference8), 1), 
/*  66 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_03", (Holder)reference8), 2), (Object[])new Pair[] {
/*  67 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_04", (Holder)reference8), 2), 
/*  68 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_05", (Holder)reference8), 2), 
/*  69 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_06", (Holder)reference8), 2), 
/*  70 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/turn_01", (Holder)reference8), 3)
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  75 */     Pools.register(context, "village/plains/zombie/streets", new StructureTemplatePool((Holder)reference11, 
/*     */           
/*  77 */           (List)ImmutableList.of(
/*  78 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/corner_01", (Holder)reference8), 2), 
/*  79 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/corner_02", (Holder)reference8), 2), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/corner_03", (Holder)reference8), 2), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_01", (Holder)reference8), 4), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_02", (Holder)reference8), 4), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_03", (Holder)reference8), 7), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_04", (Holder)reference8), 7), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_05", (Holder)reference8), 3), 
/*  86 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_06", (Holder)reference8), 4), 
/*  87 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_01", (Holder)reference8), 2), 
/*  88 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_02", (Holder)reference8), 1), 
/*  89 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_03", (Holder)reference8), 2), (Object[])new Pair[] {
/*  90 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_04", (Holder)reference8), 2), 
/*  91 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_05", (Holder)reference8), 2), 
/*  92 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_06", (Holder)reference8), 2), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/turn_01", (Holder)reference8), 3)
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  98 */     Pools.register(context, "village/plains/houses", new StructureTemplatePool((Holder)reference11, 
/*     */           
/* 100 */           (List)ImmutableList.of(
/* 101 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_1", (Holder)reference4), 2), 
/* 102 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_2", (Holder)reference4), 2), 
/* 103 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_3", (Holder)reference4), 2), 
/* 104 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_4", (Holder)reference4), 2), 
/* 105 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_5", (Holder)reference4), 2), 
/* 106 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_6", (Holder)reference4), 1), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_7", (Holder)reference4), 2), 
/* 108 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_8", (Holder)reference4), 3), 
/* 109 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_medium_house_1", (Holder)reference4), 2), 
/* 110 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_medium_house_2", (Holder)reference4), 2), 
/* 111 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_big_house_1", (Holder)reference4), 2), 
/* 112 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", (Holder)reference4), 2), (Object[])new Pair[] { 
/* 113 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_2", (Holder)reference4), 2), 
/* 114 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", (Holder)reference4), 2), 
/* 115 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fletcher_house_1", (Holder)reference4), 2), 
/* 116 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_shepherds_house_1"), 2), 
/* 117 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", (Holder)reference4), 2), 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", (Holder)reference4), 2), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", (Holder)reference4), 2), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", (Holder)reference4), 1), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", (Holder)reference4), 5), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", (Holder)reference4), 1), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", (Holder)reference4), 2), 
/* 124 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", (Holder)reference4), 2), 
/* 125 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", (Holder)reference4), 2), 
/* 126 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", (Holder)reference4), 2), 
/* 127 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_1", (Holder)reference4), 2), 
/* 128 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2"), 2), 
/* 129 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", (Holder)reference9), 4), 
/* 130 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", (Holder)reference9), 4), 
/* 131 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1"), 1), 
/* 132 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2"), 1), 
/* 133 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_3"), 5), 
/* 134 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_accessory_1"), 1), 
/* 135 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_4", (Holder)reference6), 3), 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_5"), 1), 
/* 137 */               Pair.of(StructurePoolElement.empty(), 10) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Pools.register(context, "village/plains/zombie/houses", new StructureTemplatePool((Holder)reference11, 
/*     */           
/* 144 */           (List)ImmutableList.of(
/* 145 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_1", (Holder)reference7), 2), 
/* 146 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_2", (Holder)reference7), 2), 
/* 147 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_3", (Holder)reference7), 2), 
/* 148 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_4", (Holder)reference7), 2), 
/* 149 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_5", (Holder)reference7), 2), 
/* 150 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_6", (Holder)reference7), 1), 
/* 151 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_7", (Holder)reference7), 2), 
/* 152 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_8", (Holder)reference7), 2), 
/* 153 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_medium_house_1", (Holder)reference7), 2), 
/* 154 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_medium_house_2", (Holder)reference7), 2), 
/* 155 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_big_house_1", (Holder)reference7), 2), 
/* 156 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", (Holder)reference7), 2), (Object[])new Pair[] { 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_butcher_shop_2", (Holder)reference7), 2), 
/* 158 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", (Holder)reference7), 2), 
/* 159 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_fletcher_house_1", (Holder)reference7), 2), 
/* 160 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_shepherds_house_1", (Holder)reference7), 2), 
/* 161 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", (Holder)reference7), 2), 
/* 162 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", (Holder)reference7), 2), 
/* 163 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", (Holder)reference7), 2), 
/* 164 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", (Holder)reference7), 1), 
/* 165 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", (Holder)reference7), 3), 
/* 166 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", (Holder)reference7), 1), 
/* 167 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", (Holder)reference7), 2), 
/* 168 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", (Holder)reference7), 2), 
/* 169 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", (Holder)reference7), 2), 
/* 170 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", (Holder)reference7), 2), 
/* 171 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_stable_1", (Holder)reference7), 2), 
/* 172 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2", (Holder)reference7), 2), 
/* 173 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", (Holder)reference7), 4), 
/* 174 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", (Holder)reference7), 4), 
/* 175 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1", (Holder)reference7), 1), 
/* 176 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2", (Holder)reference7), 1), 
/* 177 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_animal_pen_3", (Holder)reference7), 5), 
/* 178 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_meeting_point_4", (Holder)reference7), 3), 
/* 179 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_meeting_point_5", (Holder)reference7), 1), 
/* 180 */               Pair.of(StructurePoolElement.empty(), 10) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     context.register(TERMINATORS_KEY, new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 187 */           (List)ImmutableList.of(
/* 188 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference8), 1), 
/* 189 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference8), 1), 
/* 190 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference8), 1), 
/* 191 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference8), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     Pools.register(context, "village/plains/trees", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 198 */           (List)ImmutableList.of(
/* 199 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     Pools.register(context, "village/plains/decor", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 206 */           (List)ImmutableList.of(
/* 207 */             Pair.of(StructurePoolElement.legacy("village/plains/plains_lamp_1"), 2), 
/* 208 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 1), 
/* 209 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 1), 
/* 210 */             Pair.of(StructurePoolElement.feature((Holder)reference3), 1), 
/* 211 */             Pair.of(StructurePoolElement.empty(), 2)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     Pools.register(context, "village/plains/zombie/decor", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 218 */           (List)ImmutableList.of(
/* 219 */             Pair.of(StructurePoolElement.legacy("village/plains/plains_lamp_1", (Holder)reference7), 1), 
/* 220 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 1), 
/* 221 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 1), 
/* 222 */             Pair.of(StructurePoolElement.feature((Holder)reference3), 1), 
/* 223 */             Pair.of(StructurePoolElement.empty(), 2)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     Pools.register(context, "village/plains/villagers", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 230 */           (List)ImmutableList.of(
/* 231 */             Pair.of(StructurePoolElement.legacy("village/plains/villagers/nitwit"), 1), 
/* 232 */             Pair.of(StructurePoolElement.legacy("village/plains/villagers/baby"), 1), 
/* 233 */             Pair.of(StructurePoolElement.legacy("village/plains/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     Pools.register(context, "village/plains/zombie/villagers", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 240 */           (List)ImmutableList.of(
/* 241 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/villagers/nitwit"), 1), 
/* 242 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     Pools.register(context, "village/common/animals", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 251 */           (List)ImmutableList.of(
/* 252 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cows_1"), 7), 
/* 253 */             Pair.of(StructurePoolElement.legacy("village/common/animals/pigs_1"), 7), 
/* 254 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_1"), 1), 
/* 255 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_2"), 1), 
/* 256 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_3"), 1), 
/* 257 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_4"), 1), 
/* 258 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_5"), 1), 
/* 259 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_1"), 1), 
/* 260 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_2"), 1), 
/* 261 */             Pair.of(StructurePoolElement.empty(), 5)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     Pools.register(context, "village/common/sheep", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 268 */           (List)ImmutableList.of(
/* 269 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_1"), 1), 
/* 270 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_2"), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     Pools.register(context, "village/common/cats", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 277 */           (List)ImmutableList.of(
/* 278 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_black"), 1), 
/* 279 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_british"), 1), 
/* 280 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_calico"), 1), 
/* 281 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_persian"), 1), 
/* 282 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_ragdoll"), 1), 
/* 283 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_red"), 1), 
/* 284 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_siamese"), 1), 
/* 285 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_tabby"), 1), 
/* 286 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_white"), 1), 
/* 287 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_jellie"), 1), 
/* 288 */             Pair.of(StructurePoolElement.empty(), 3)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 293 */     Pools.register(context, "village/common/butcher_animals", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 295 */           (List)ImmutableList.of(
/* 296 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cows_1"), 3), 
/* 297 */             Pair.of(StructurePoolElement.legacy("village/common/animals/pigs_1"), 3), 
/* 298 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_1"), 1), 
/* 299 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_2"), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     Pools.register(context, "village/common/iron_golem", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 306 */           (List)ImmutableList.of(
/* 307 */             Pair.of(StructurePoolElement.legacy("village/common/iron_golem"), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     Pools.register(context, "village/common/well_bottoms", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 314 */           (List)ImmutableList.of(
/* 315 */             Pair.of(StructurePoolElement.legacy("village/common/well_bottom"), 1)), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/PlainVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */