/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.placement.CavePlacements;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ 
/*     */ public class AncientCityStructurePools {
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  16 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  17 */     Holder.Reference reference1 = placedFeatures.getOrThrow(CavePlacements.SCULK_PATCH_ANCIENT_CITY);
/*     */     
/*  19 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  20 */     Holder.Reference reference2 = processorLists.getOrThrow(ProcessorLists.ANCIENT_CITY_GENERIC_DEGRADATION);
/*  21 */     Holder.Reference reference3 = processorLists.getOrThrow(ProcessorLists.ANCIENT_CITY_WALLS_DEGRADATION);
/*     */     
/*  23 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  24 */     Holder.Reference reference4 = pools.getOrThrow(Pools.EMPTY);
/*     */     
/*  26 */     Pools.register(context, "ancient_city/structures", new StructureTemplatePool((Holder)reference4, 
/*     */           
/*  28 */           (List)ImmutableList.of(
/*  29 */             Pair.of(StructurePoolElement.empty(), 7), 
/*  30 */             Pair.of(StructurePoolElement.single("ancient_city/structures/barracks", (Holder)reference2), 4), 
/*  31 */             Pair.of(StructurePoolElement.single("ancient_city/structures/chamber_1", (Holder)reference2), 4), 
/*  32 */             Pair.of(StructurePoolElement.single("ancient_city/structures/chamber_2", (Holder)reference2), 4), 
/*  33 */             Pair.of(StructurePoolElement.single("ancient_city/structures/chamber_3", (Holder)reference2), 4), 
/*  34 */             Pair.of(StructurePoolElement.single("ancient_city/structures/sauna_1", (Holder)reference2), 4), 
/*  35 */             Pair.of(StructurePoolElement.single("ancient_city/structures/small_statue", (Holder)reference2), 4), 
/*  36 */             Pair.of(StructurePoolElement.single("ancient_city/structures/large_ruin_1", (Holder)reference2), 1), 
/*  37 */             Pair.of(StructurePoolElement.single("ancient_city/structures/tall_ruin_1", (Holder)reference2), 1), 
/*  38 */             Pair.of(StructurePoolElement.single("ancient_city/structures/tall_ruin_2", (Holder)reference2), 1), 
/*  39 */             Pair.of(StructurePoolElement.single("ancient_city/structures/tall_ruin_3", (Holder)reference2), 2), 
/*  40 */             Pair.of(StructurePoolElement.single("ancient_city/structures/tall_ruin_4", (Holder)reference2), 2), (Object[])new Pair[] {
/*  41 */               Pair.of(StructurePoolElement.list((List)ImmutableList.of(
/*  42 */                     StructurePoolElement.single("ancient_city/structures/camp_1", (Holder)reference2), 
/*  43 */                     StructurePoolElement.single("ancient_city/structures/camp_2", (Holder)reference2), 
/*  44 */                     StructurePoolElement.single("ancient_city/structures/camp_3", (Holder)reference2))), 1), 
/*     */               
/*  46 */               Pair.of(StructurePoolElement.single("ancient_city/structures/medium_ruin_1", (Holder)reference2), 1), 
/*  47 */               Pair.of(StructurePoolElement.single("ancient_city/structures/medium_ruin_2", (Holder)reference2), 1), 
/*  48 */               Pair.of(StructurePoolElement.single("ancient_city/structures/small_ruin_1", (Holder)reference2), 1), 
/*  49 */               Pair.of(StructurePoolElement.single("ancient_city/structures/small_ruin_2", (Holder)reference2), 1), 
/*  50 */               Pair.of(StructurePoolElement.single("ancient_city/structures/large_pillar_1", (Holder)reference2), 1), 
/*  51 */               Pair.of(StructurePoolElement.single("ancient_city/structures/medium_pillar_1", (Holder)reference2), 1), 
/*  52 */               Pair.of(StructurePoolElement.list((List)ImmutableList.of(
/*  53 */                     StructurePoolElement.single("ancient_city/structures/ice_box_1"))), 1)
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Pools.register(context, "ancient_city/sculk", new StructureTemplatePool((Holder)reference4, 
/*     */           
/*  61 */           (List)ImmutableList.of(
/*  62 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 6), 
/*  63 */             Pair.of(StructurePoolElement.empty(), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     Pools.register(context, "ancient_city/walls", new StructureTemplatePool((Holder)reference4, 
/*     */           
/*  70 */           (List)ImmutableList.of(
/*  71 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_corner_wall_1", (Holder)reference3), 1), 
/*  72 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_intersection_wall_1", (Holder)reference3), 1), 
/*  73 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_lshape_wall_1", (Holder)reference3), 1), 
/*  74 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_1", (Holder)reference3), 1), 
/*  75 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_2", (Holder)reference3), 1), 
/*  76 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_1", (Holder)reference3), 1), 
/*  77 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_2", (Holder)reference3), 1), 
/*  78 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_3", (Holder)reference3), 1), 
/*  79 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_4", (Holder)reference3), 4), 
/*  80 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_passage_1", (Holder)reference3), 3), 
/*  81 */             Pair.of(StructurePoolElement.single("ancient_city/walls/ruined_corner_wall_1", (Holder)reference3), 1), 
/*  82 */             Pair.of(StructurePoolElement.single("ancient_city/walls/ruined_corner_wall_2", (Holder)reference3), 1), (Object[])new Pair[] {
/*  83 */               Pair.of(StructurePoolElement.single("ancient_city/walls/ruined_horizontal_wall_stairs_1", (Holder)reference3), 2), 
/*  84 */               Pair.of(StructurePoolElement.single("ancient_city/walls/ruined_horizontal_wall_stairs_2", (Holder)reference3), 2), 
/*  85 */               Pair.of(StructurePoolElement.single("ancient_city/walls/ruined_horizontal_wall_stairs_3", (Holder)reference3), 3), 
/*  86 */               Pair.of(StructurePoolElement.single("ancient_city/walls/ruined_horizontal_wall_stairs_4", (Holder)reference3), 3)
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/*  91 */     Pools.register(context, "ancient_city/walls/no_corners", new StructureTemplatePool((Holder)reference4, 
/*     */           
/*  93 */           (List)ImmutableList.of(
/*  94 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_1", (Holder)reference3), 1), 
/*  95 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_2", (Holder)reference3), 1), 
/*  96 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_1", (Holder)reference3), 1), 
/*  97 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_2", (Holder)reference3), 1), 
/*  98 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_3", (Holder)reference3), 1), 
/*  99 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_4", (Holder)reference3), 1), 
/* 100 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_stairs_5", (Holder)reference3), 1), 
/* 101 */             Pair.of(StructurePoolElement.single("ancient_city/walls/intact_horizontal_wall_bridge", (Holder)reference3), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     Pools.register(context, "ancient_city/city_center/walls", new StructureTemplatePool((Holder)reference4, 
/*     */           
/* 108 */           (List)ImmutableList.of(
/* 109 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/bottom_1", (Holder)reference2), 1), 
/* 110 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/bottom_2", (Holder)reference2), 1), 
/* 111 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/bottom_left_corner", (Holder)reference2), 1), 
/* 112 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/bottom_right_corner_1", (Holder)reference2), 1), 
/* 113 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/bottom_right_corner_2", (Holder)reference2), 1), 
/* 114 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/left", (Holder)reference2), 1), 
/* 115 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/right", (Holder)reference2), 1), 
/* 116 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/top", (Holder)reference2), 1), 
/* 117 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/top_right_corner", (Holder)reference2), 1), 
/* 118 */             Pair.of(StructurePoolElement.single("ancient_city/city_center/walls/top_left_corner", (Holder)reference2), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     Pools.register(context, "ancient_city/city/entrance", new StructureTemplatePool((Holder)reference4, 
/*     */           
/* 125 */           (List)ImmutableList.of(
/* 126 */             Pair.of(StructurePoolElement.single("ancient_city/city/entrance/entrance_connector", (Holder)reference2), 1), 
/* 127 */             Pair.of(StructurePoolElement.single("ancient_city/city/entrance/entrance_path_1", (Holder)reference2), 1), 
/* 128 */             Pair.of(StructurePoolElement.single("ancient_city/city/entrance/entrance_path_2", (Holder)reference2), 1), 
/* 129 */             Pair.of(StructurePoolElement.single("ancient_city/city/entrance/entrance_path_3", (Holder)reference2), 1), 
/* 130 */             Pair.of(StructurePoolElement.single("ancient_city/city/entrance/entrance_path_4", (Holder)reference2), 1), 
/* 131 */             Pair.of(StructurePoolElement.single("ancient_city/city/entrance/entrance_path_5", (Holder)reference2), 1)), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/AncientCityStructurePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */