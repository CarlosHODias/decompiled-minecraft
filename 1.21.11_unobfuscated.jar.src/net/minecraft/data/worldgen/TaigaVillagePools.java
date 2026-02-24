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
/*     */ public class TaigaVillagePools {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/taiga/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/taiga/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  20 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  21 */     Holder.Reference reference1 = placedFeatures.getOrThrow(VillagePlacements.SPRUCE_VILLAGE);
/*  22 */     Holder.Reference reference2 = placedFeatures.getOrThrow(VillagePlacements.PINE_VILLAGE);
/*  23 */     Holder.Reference reference3 = placedFeatures.getOrThrow(VillagePlacements.PILE_PUMPKIN_VILLAGE);
/*  24 */     Holder.Reference reference4 = placedFeatures.getOrThrow(VillagePlacements.PATCH_TAIGA_GRASS_VILLAGE);
/*  25 */     Holder.Reference reference5 = placedFeatures.getOrThrow(VillagePlacements.PATCH_BERRY_BUSH_VILLAGE);
/*     */     
/*  27 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  28 */     Holder.Reference reference6 = processorLists.getOrThrow(ProcessorLists.MOSSIFY_10_PERCENT);
/*  29 */     Holder.Reference reference7 = processorLists.getOrThrow(ProcessorLists.ZOMBIE_TAIGA);
/*  30 */     Holder.Reference reference8 = processorLists.getOrThrow(ProcessorLists.STREET_SNOWY_OR_TAIGA);
/*  31 */     Holder.Reference reference9 = processorLists.getOrThrow(ProcessorLists.FARM_TAIGA);
/*     */     
/*  33 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  34 */     Holder.Reference reference10 = pools.getOrThrow(Pools.EMPTY);
/*  35 */     Holder.Reference reference11 = pools.getOrThrow(TERMINATORS_KEY);
/*     */     
/*  37 */     context.register(START, new StructureTemplatePool((Holder)reference10, 
/*     */           
/*  39 */           (List)ImmutableList.of(
/*  40 */             Pair.of(StructurePoolElement.legacy("village/taiga/town_centers/taiga_meeting_point_1", (Holder)reference6), 49), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/taiga/town_centers/taiga_meeting_point_2", (Holder)reference6), 49), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_1", (Holder)reference7), 1), 
/*  43 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_2", (Holder)reference7), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     Pools.register(context, "village/taiga/streets", new StructureTemplatePool((Holder)reference11, 
/*     */           
/*  50 */           (List)ImmutableList.of(
/*  51 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_01", (Holder)reference8), 2), 
/*  52 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_02", (Holder)reference8), 2), 
/*  53 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_03", (Holder)reference8), 2), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_01", (Holder)reference8), 4), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_02", (Holder)reference8), 4), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_03", (Holder)reference8), 4), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_04", (Holder)reference8), 7), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_05", (Holder)reference8), 7), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_06", (Holder)reference8), 4), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_01", (Holder)reference8), 1), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_02", (Holder)reference8), 1), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_03", (Holder)reference8), 2), (Object[])new Pair[] {
/*  63 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_04", (Holder)reference8), 2), 
/*  64 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_05", (Holder)reference8), 2), 
/*  65 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_06", (Holder)reference8), 2), 
/*  66 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/turn_01", (Holder)reference8), 3)
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  71 */     Pools.register(context, "village/taiga/zombie/streets", new StructureTemplatePool((Holder)reference11, 
/*     */           
/*  73 */           (List)ImmutableList.of(
/*  74 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_01", (Holder)reference8), 2), 
/*  75 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_02", (Holder)reference8), 2), 
/*  76 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_03", (Holder)reference8), 2), 
/*  77 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_01", (Holder)reference8), 4), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_02", (Holder)reference8), 4), 
/*  79 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_03", (Holder)reference8), 4), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_04", (Holder)reference8), 7), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_05", (Holder)reference8), 7), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_06", (Holder)reference8), 4), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_01", (Holder)reference8), 1), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_02", (Holder)reference8), 1), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_03", (Holder)reference8), 2), (Object[])new Pair[] {
/*  86 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_04", (Holder)reference8), 2), 
/*  87 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_05", (Holder)reference8), 2), 
/*  88 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_06", (Holder)reference8), 2), 
/*  89 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/turn_01", (Holder)reference8), 3)
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  94 */     Pools.register(context, "village/taiga/houses", new StructureTemplatePool((Holder)reference11, 
/*     */           
/*  96 */           (List)ImmutableList.of(
/*  97 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_1", (Holder)reference6), 4), 
/*  98 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_2", (Holder)reference6), 4), 
/*  99 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_3", (Holder)reference6), 4), 
/* 100 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_4", (Holder)reference6), 4), 
/* 101 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_5", (Holder)reference6), 4), 
/* 102 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_1", (Holder)reference6), 2), 
/* 103 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_2", (Holder)reference6), 2), 
/* 104 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_3", (Holder)reference6), 2), 
/* 105 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_4", (Holder)reference6), 2), 
/* 106 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_butcher_shop_1", (Holder)reference6), 2), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tool_smith_1", (Holder)reference6), 2), 
/* 108 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fletcher_house_1", (Holder)reference6), 2), (Object[])new Pair[] { 
/* 109 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_shepherds_house_1", (Holder)reference6), 2), 
/* 110 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_house_1", (Holder)reference6), 1), 
/* 111 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_2", (Holder)reference6), 1), 
/* 112 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fisher_cottage_1", (Holder)reference6), 3), 
/* 113 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tannery_1", (Holder)reference6), 2), 
/* 114 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_cartographer_house_1", (Holder)reference6), 2), 
/* 115 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_library_1", (Holder)reference6), 2), 
/* 116 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_masons_house_1", (Holder)reference6), 2), 
/* 117 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_1", (Holder)reference6), 2), 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_2", (Holder)reference6), 2), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_temple_1", (Holder)reference6), 2), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_1", (Holder)reference9), 6), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_2", (Holder)reference9), 6), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_farm_1", (Holder)reference6), 1), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_animal_pen_1", (Holder)reference6), 2), 
/* 124 */               Pair.of(StructurePoolElement.empty(), 6) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     Pools.register(context, "village/taiga/zombie/houses", new StructureTemplatePool((Holder)reference11, 
/*     */           
/* 131 */           (List)ImmutableList.of(
/* 132 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_1", (Holder)reference7), 4), 
/* 133 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_2", (Holder)reference7), 4), 
/* 134 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_3", (Holder)reference7), 4), 
/* 135 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_4", (Holder)reference7), 4), 
/* 136 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_5", (Holder)reference7), 4), 
/* 137 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_1", (Holder)reference7), 2), 
/* 138 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_2", (Holder)reference7), 2), 
/* 139 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_3", (Holder)reference7), 2), 
/* 140 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_4", (Holder)reference7), 2), 
/* 141 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_butcher_shop_1", (Holder)reference7), 2), 
/* 142 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_tool_smith_1", (Holder)reference7), 2), 
/* 143 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fletcher_house_1", (Holder)reference7), 2), (Object[])new Pair[] { 
/* 144 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_shepherds_house_1", (Holder)reference7), 2), 
/* 145 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_house_1", (Holder)reference7), 1), 
/* 146 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_fisher_cottage_1", (Holder)reference7), 2), 
/* 147 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tannery_1", (Holder)reference7), 2), 
/* 148 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_cartographer_house_1", (Holder)reference7), 2), 
/* 149 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_library_1", (Holder)reference7), 2), 
/* 150 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_masons_house_1", (Holder)reference7), 2), 
/* 151 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_1", (Holder)reference7), 2), 
/* 152 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_weaponsmith_2", (Holder)reference7), 2), 
/* 153 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_temple_1", (Holder)reference7), 2), 
/* 154 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_1", (Holder)reference7), 6), 
/* 155 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_large_farm_2", (Holder)reference7), 6), 
/* 156 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_farm_1", (Holder)reference7), 1), 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_animal_pen_1", (Holder)reference7), 2), 
/* 158 */               Pair.of(StructurePoolElement.empty(), 6) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     context.register(TERMINATORS_KEY, new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 165 */           (List)ImmutableList.of(
/* 166 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", (Holder)reference8), 1), 
/* 167 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", (Holder)reference8), 1), 
/* 168 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", (Holder)reference8), 1), 
/* 169 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", (Holder)reference8), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     Pools.register(context, "village/taiga/decor", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 176 */           (List)ImmutableList.of(
/* 177 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_lamp_post_1"), 10), 
/* 178 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_1"), 4), 
/* 179 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_2"), 1), 
/* 180 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_3"), 1), 
/* 181 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_4"), 1), 
/* 182 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_5"), 2), 
/* 183 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_6"), 1), 
/* 184 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 4), 
/* 185 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 4), 
/* 186 */             Pair.of(StructurePoolElement.feature((Holder)reference3), 2), 
/* 187 */             Pair.of(StructurePoolElement.feature((Holder)reference4), 4), 
/* 188 */             Pair.of(StructurePoolElement.feature((Holder)reference5), 1), (Object[])new Pair[] {
/* 189 */               Pair.of(StructurePoolElement.empty(), 4)
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/* 194 */     Pools.register(context, "village/taiga/zombie/decor", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 196 */           (List)ImmutableList.of(
/* 197 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_1"), 4), 
/* 198 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_2"), 1), 
/* 199 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_3"), 1), 
/* 200 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_4"), 1), 
/* 201 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 4), 
/* 202 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 4), 
/* 203 */             Pair.of(StructurePoolElement.feature((Holder)reference3), 2), 
/* 204 */             Pair.of(StructurePoolElement.feature((Holder)reference4), 4), 
/* 205 */             Pair.of(StructurePoolElement.feature((Holder)reference5), 1), 
/* 206 */             Pair.of(StructurePoolElement.empty(), 4)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     Pools.register(context, "village/taiga/villagers", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 213 */           (List)ImmutableList.of(
/* 214 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/nitwit"), 1), 
/* 215 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/baby"), 1), 
/* 216 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     Pools.register(context, "village/taiga/zombie/villagers", new StructureTemplatePool((Holder)reference10, 
/*     */           
/* 223 */           (List)ImmutableList.of(
/* 224 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/villagers/nitwit"), 1), 
/* 225 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/TaigaVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */