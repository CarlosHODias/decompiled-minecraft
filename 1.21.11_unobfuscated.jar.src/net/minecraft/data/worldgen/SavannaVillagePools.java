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
/*     */ public class SavannaVillagePools {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/savanna/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/savanna/terminators");
/*  18 */   private static final ResourceKey<StructureTemplatePool> ZOMBIE_TERMINATORS_KEY = Pools.createKey("village/savanna/zombie/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  21 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  22 */     Holder.Reference reference1 = placedFeatures.getOrThrow(VillagePlacements.ACACIA_VILLAGE);
/*  23 */     Holder.Reference reference2 = placedFeatures.getOrThrow(VillagePlacements.PILE_HAY_VILLAGE);
/*  24 */     Holder.Reference reference3 = placedFeatures.getOrThrow(VillagePlacements.PILE_MELON_VILLAGE);
/*     */     
/*  26 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  27 */     Holder.Reference reference4 = processorLists.getOrThrow(ProcessorLists.ZOMBIE_SAVANNA);
/*  28 */     Holder.Reference reference5 = processorLists.getOrThrow(ProcessorLists.STREET_SAVANNA);
/*  29 */     Holder.Reference reference6 = processorLists.getOrThrow(ProcessorLists.FARM_SAVANNA);
/*     */     
/*  31 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  32 */     Holder.Reference reference7 = pools.getOrThrow(Pools.EMPTY);
/*  33 */     Holder.Reference reference8 = pools.getOrThrow(TERMINATORS_KEY);
/*  34 */     Holder.Reference reference9 = pools.getOrThrow(ZOMBIE_TERMINATORS_KEY);
/*     */     
/*  36 */     context.register(START, new StructureTemplatePool((Holder)reference7, 
/*     */           
/*  38 */           (List)ImmutableList.of(
/*  39 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_1"), 100), 
/*  40 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_2"), 50), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_3"), 150), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/savanna/town_centers/savanna_meeting_point_4"), 150), 
/*  43 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_1", (Holder)reference4), 2), 
/*  44 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_2", (Holder)reference4), 1), 
/*  45 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_3", (Holder)reference4), 3), 
/*  46 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/town_centers/savanna_meeting_point_4", (Holder)reference4), 3)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     Pools.register(context, "village/savanna/streets", new StructureTemplatePool((Holder)reference8, 
/*     */           
/*  53 */           (List)ImmutableList.of(
/*  54 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/corner_01", (Holder)reference5), 2), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/corner_03", (Holder)reference5), 2), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_02", (Holder)reference5), 4), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_04", (Holder)reference5), 7), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_05", (Holder)reference5), 3), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_06", (Holder)reference5), 4), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_08", (Holder)reference5), 4), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_09", (Holder)reference5), 4), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_10", (Holder)reference5), 4), 
/*  63 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/straight_11", (Holder)reference5), 4), 
/*  64 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_02", (Holder)reference5), 1), 
/*  65 */             Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_03", (Holder)reference5), 2), (Object[])new Pair[] {
/*  66 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_04", (Holder)reference5), 2), 
/*  67 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_05", (Holder)reference5), 2), 
/*  68 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_06", (Holder)reference5), 2), 
/*  69 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/crossroad_07", (Holder)reference5), 2), 
/*  70 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/split_01", (Holder)reference5), 2), 
/*  71 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/split_02", (Holder)reference5), 2), 
/*  72 */               Pair.of(StructurePoolElement.legacy("village/savanna/streets/turn_01", (Holder)reference5), 3)
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  77 */     Pools.register(context, "village/savanna/zombie/streets", new StructureTemplatePool((Holder)reference9, 
/*     */           
/*  79 */           (List)ImmutableList.of(
/*  80 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/corner_01", (Holder)reference5), 2), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/corner_03", (Holder)reference5), 2), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_02", (Holder)reference5), 4), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_04", (Holder)reference5), 7), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_05", (Holder)reference5), 3), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_06", (Holder)reference5), 4), 
/*  86 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_08", (Holder)reference5), 4), 
/*  87 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_09", (Holder)reference5), 4), 
/*  88 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_10", (Holder)reference5), 4), 
/*  89 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/straight_11", (Holder)reference5), 4), 
/*  90 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_02", (Holder)reference5), 1), 
/*  91 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_03", (Holder)reference5), 2), (Object[])new Pair[] {
/*  92 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_04", (Holder)reference5), 2), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_05", (Holder)reference5), 2), 
/*  94 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_06", (Holder)reference5), 2), 
/*  95 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/crossroad_07", (Holder)reference5), 2), 
/*  96 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/split_01", (Holder)reference5), 2), 
/*  97 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/split_02", (Holder)reference5), 2), 
/*  98 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/streets/turn_01", (Holder)reference5), 3)
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/* 103 */     Pools.register(context, "village/savanna/houses", new StructureTemplatePool((Holder)reference8, 
/*     */           
/* 105 */           (List)ImmutableList.of(
/* 106 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_1"), 2), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_2"), 2), 
/* 108 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_3"), 2), 
/* 109 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_4"), 2), 
/* 110 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_5"), 2), 
/* 111 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_6"), 2), 
/* 112 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_7"), 2), 
/* 113 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_house_8"), 2), 
/* 114 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_medium_house_1"), 2), 
/* 115 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_medium_house_2"), 2), 
/* 116 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_1"), 2), 
/* 117 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_2"), 2), (Object[])new Pair[] { 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tool_smith_1"), 2), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fletcher_house_1"), 2), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_shepherd_1"), 7), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_armorer_1"), 1), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fisher_cottage_1"), 3), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tannery_1"), 2), 
/* 124 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_cartographer_1"), 2), 
/* 125 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_library_1"), 2), 
/* 126 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_mason_1"), 2), 
/* 127 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_1"), 2), 
/* 128 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_2"), 2), 
/* 129 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_1"), 2), 
/* 130 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_2"), 3), 
/* 131 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_large_farm_1", (Holder)reference6), 4), 
/* 132 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_large_farm_2", (Holder)reference6), 6), 
/* 133 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_farm", (Holder)reference6), 4), 
/* 134 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_1"), 2), 
/* 135 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_2"), 2), 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_3"), 2), 
/* 137 */               Pair.of(StructurePoolElement.empty(), 5) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Pools.register(context, "village/savanna/zombie/houses", new StructureTemplatePool((Holder)reference9, 
/*     */           
/* 144 */           (List)ImmutableList.of(
/* 145 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_1", (Holder)reference4), 2), 
/* 146 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_2", (Holder)reference4), 2), 
/* 147 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_3", (Holder)reference4), 2), 
/* 148 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_4", (Holder)reference4), 2), 
/* 149 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_5", (Holder)reference4), 2), 
/* 150 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_6", (Holder)reference4), 2), 
/* 151 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_7", (Holder)reference4), 2), 
/* 152 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_small_house_8", (Holder)reference4), 2), 
/* 153 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_medium_house_1", (Holder)reference4), 2), 
/* 154 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_medium_house_2", (Holder)reference4), 2), 
/* 155 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_1", (Holder)reference4), 2), 
/* 156 */             Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_butchers_shop_2", (Holder)reference4), 2), (Object[])new Pair[] { 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tool_smith_1", (Holder)reference4), 2), 
/* 158 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fletcher_house_1", (Holder)reference4), 2), 
/* 159 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_shepherd_1", (Holder)reference4), 2), 
/* 160 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_armorer_1", (Holder)reference4), 1), 
/* 161 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_fisher_cottage_1", (Holder)reference4), 2), 
/* 162 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_tannery_1", (Holder)reference4), 2), 
/* 163 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_cartographer_1", (Holder)reference4), 2), 
/* 164 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_library_1", (Holder)reference4), 2), 
/* 165 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_mason_1", (Holder)reference4), 2), 
/* 166 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_1", (Holder)reference4), 2), 
/* 167 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_weaponsmith_2", (Holder)reference4), 2), 
/* 168 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_1", (Holder)reference4), 1), 
/* 169 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_temple_2", (Holder)reference4), 3), 
/* 170 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_large_farm_1", (Holder)reference4), 4), 
/* 171 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_large_farm_2", (Holder)reference4), 4), 
/* 172 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_small_farm", (Holder)reference4), 4), 
/* 173 */               Pair.of(StructurePoolElement.legacy("village/savanna/houses/savanna_animal_pen_1", (Holder)reference4), 2), 
/* 174 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_animal_pen_2", (Holder)reference4), 2), 
/* 175 */               Pair.of(StructurePoolElement.legacy("village/savanna/zombie/houses/savanna_animal_pen_3", (Holder)reference4), 2), 
/* 176 */               Pair.of(StructurePoolElement.empty(), 5) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     context.register(TERMINATORS_KEY, new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 183 */           (List)ImmutableList.of(
/* 184 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference5), 1), 
/* 185 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference5), 1), 
/* 186 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference5), 1), 
/* 187 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference5), 1), 
/* 188 */             Pair.of(StructurePoolElement.legacy("village/savanna/terminators/terminator_05", (Holder)reference5), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     context.register(ZOMBIE_TERMINATORS_KEY, new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 195 */           (List)ImmutableList.of(
/* 196 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference5), 1), 
/* 197 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference5), 1), 
/* 198 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference5), 1), 
/* 199 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference5), 1), 
/* 200 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/terminators/terminator_05", (Holder)reference5), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     Pools.register(context, "village/savanna/trees", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 207 */           (List)ImmutableList.of(
/* 208 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Pools.register(context, "village/savanna/decor", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 215 */           (List)ImmutableList.of(
/* 216 */             Pair.of(StructurePoolElement.legacy("village/savanna/savanna_lamp_post_01"), 4), 
/* 217 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 4), 
/* 218 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 4), 
/* 219 */             Pair.of(StructurePoolElement.feature((Holder)reference3), 1), 
/* 220 */             Pair.of(StructurePoolElement.empty(), 4)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     Pools.register(context, "village/savanna/zombie/decor", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 227 */           (List)ImmutableList.of(
/* 228 */             Pair.of(StructurePoolElement.legacy("village/savanna/savanna_lamp_post_01", (Holder)reference4), 4), 
/* 229 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 4), 
/* 230 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 4), 
/* 231 */             Pair.of(StructurePoolElement.feature((Holder)reference3), 1), 
/* 232 */             Pair.of(StructurePoolElement.empty(), 4)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     Pools.register(context, "village/savanna/villagers", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 239 */           (List)ImmutableList.of(
/* 240 */             Pair.of(StructurePoolElement.legacy("village/savanna/villagers/nitwit"), 1), 
/* 241 */             Pair.of(StructurePoolElement.legacy("village/savanna/villagers/baby"), 1), 
/* 242 */             Pair.of(StructurePoolElement.legacy("village/savanna/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     Pools.register(context, "village/savanna/zombie/villagers", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 249 */           (List)ImmutableList.of(
/* 250 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/villagers/nitwit"), 1), 
/* 251 */             Pair.of(StructurePoolElement.legacy("village/savanna/zombie/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/SavannaVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */