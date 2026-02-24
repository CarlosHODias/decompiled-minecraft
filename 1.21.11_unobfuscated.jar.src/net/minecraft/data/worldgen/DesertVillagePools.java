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
/*     */ public class DesertVillagePools {
/*  16 */   public static final ResourceKey<StructureTemplatePool> START = Pools.createKey("village/desert/town_centers");
/*  17 */   private static final ResourceKey<StructureTemplatePool> TERMINATORS_KEY = Pools.createKey("village/desert/terminators");
/*  18 */   private static final ResourceKey<StructureTemplatePool> ZOMBIE_TERMINATORS_KEY = Pools.createKey("village/desert/zombie/terminators");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
/*  21 */     HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
/*  22 */     Holder.Reference reference1 = placedFeatures.getOrThrow(VillagePlacements.PATCH_CACTUS_VILLAGE);
/*  23 */     Holder.Reference reference2 = placedFeatures.getOrThrow(VillagePlacements.PILE_HAY_VILLAGE);
/*     */     
/*  25 */     HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);
/*  26 */     Holder.Reference reference3 = processorLists.getOrThrow(ProcessorLists.ZOMBIE_DESERT);
/*  27 */     Holder.Reference reference4 = processorLists.getOrThrow(ProcessorLists.FARM_DESERT);
/*     */     
/*  29 */     HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
/*  30 */     Holder.Reference reference5 = pools.getOrThrow(Pools.EMPTY);
/*  31 */     Holder.Reference reference6 = pools.getOrThrow(TERMINATORS_KEY);
/*  32 */     Holder.Reference reference7 = pools.getOrThrow(ZOMBIE_TERMINATORS_KEY);
/*     */     
/*  34 */     context.register(START, new StructureTemplatePool((Holder)reference5, 
/*     */           
/*  36 */           (List)ImmutableList.of(
/*  37 */             Pair.of(StructurePoolElement.legacy("village/desert/town_centers/desert_meeting_point_1"), 98), 
/*  38 */             Pair.of(StructurePoolElement.legacy("village/desert/town_centers/desert_meeting_point_2"), 98), 
/*  39 */             Pair.of(StructurePoolElement.legacy("village/desert/town_centers/desert_meeting_point_3"), 49), 
/*  40 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_1", (Holder)reference3), 2), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_2", (Holder)reference3), 2), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_3", (Holder)reference3), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     Pools.register(context, "village/desert/streets", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  49 */           (List)ImmutableList.of(
/*  50 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/corner_01"), 3), 
/*  51 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/corner_02"), 3), 
/*  52 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/straight_01"), 4), 
/*  53 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/straight_02"), 4), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/straight_03"), 3), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/crossroad_01"), 3), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/crossroad_02"), 3), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/crossroad_03"), 3), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/square_01"), 3), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/square_02"), 3), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/turn_01"), 3)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     Pools.register(context, "village/desert/zombie/streets", new StructureTemplatePool((Holder)reference7, 
/*     */           
/*  67 */           (List)ImmutableList.of(
/*  68 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/corner_01"), 3), 
/*  69 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/corner_02"), 3), 
/*  70 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/straight_01"), 4), 
/*  71 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/straight_02"), 4), 
/*  72 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/straight_03"), 3), 
/*  73 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/crossroad_01"), 3), 
/*  74 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/crossroad_02"), 3), 
/*  75 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/crossroad_03"), 3), 
/*  76 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/square_01"), 3), 
/*  77 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/square_02"), 3), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/turn_01"), 3)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     Pools.register(context, "village/desert/houses", new StructureTemplatePool((Holder)reference6, 
/*     */           
/*  85 */           (List)ImmutableList.of(
/*  86 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_1"), 2), 
/*  87 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_2"), 2), 
/*  88 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_3"), 2), 
/*  89 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_4"), 2), 
/*  90 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_5"), 2), 
/*  91 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_6"), 1), 
/*  92 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_7"), 2), 
/*  93 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_8"), 2), 
/*  94 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_medium_house_1"), 2), 
/*  95 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_medium_house_2"), 2), 
/*  96 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_butcher_shop_1"), 2), 
/*  97 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tool_smith_1"), 2), (Object[])new Pair[] { 
/*  98 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fletcher_house_1"), 2), 
/*  99 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_shepherd_house_1"), 2), 
/* 100 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_armorer_1"), 1), 
/* 101 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fisher_1"), 2), 
/* 102 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tannery_1"), 2), 
/* 103 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_cartographer_house_1"), 2), 
/* 104 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_library_1"), 2), 
/* 105 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_mason_1"), 2), 
/* 106 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_weaponsmith_1"), 2), 
/* 107 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_1"), 2), 
/* 108 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_2"), 2), 
/* 109 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_large_farm_1", (Holder)reference4), 11), 
/* 110 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_1", (Holder)reference4), 4), 
/* 111 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_2", (Holder)reference4), 4), 
/* 112 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_1"), 2), 
/* 113 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_2"), 2), 
/* 114 */               Pair.of(StructurePoolElement.empty(), 5) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     Pools.register(context, "village/desert/zombie/houses", new StructureTemplatePool((Holder)reference7, 
/*     */           
/* 121 */           (List)ImmutableList.of(
/* 122 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_1", (Holder)reference3), 2), 
/* 123 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_2", (Holder)reference3), 2), 
/* 124 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_3", (Holder)reference3), 2), 
/* 125 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_4", (Holder)reference3), 2), 
/* 126 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_5", (Holder)reference3), 2), 
/* 127 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_6", (Holder)reference3), 1), 
/* 128 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_7", (Holder)reference3), 2), 
/* 129 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_8", (Holder)reference3), 2), 
/* 130 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_medium_house_1", (Holder)reference3), 2), 
/* 131 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_medium_house_2", (Holder)reference3), 2), 
/* 132 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_butcher_shop_1", (Holder)reference3), 2), 
/* 133 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tool_smith_1", (Holder)reference3), 2), (Object[])new Pair[] { 
/* 134 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fletcher_house_1", (Holder)reference3), 2), 
/* 135 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_shepherd_house_1", (Holder)reference3), 2), 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_armorer_1", (Holder)reference3), 1), 
/* 137 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fisher_1", (Holder)reference3), 2), 
/* 138 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tannery_1", (Holder)reference3), 2), 
/* 139 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_cartographer_house_1", (Holder)reference3), 2), 
/* 140 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_library_1", (Holder)reference3), 2), 
/* 141 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_mason_1", (Holder)reference3), 2), 
/* 142 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_weaponsmith_1", (Holder)reference3), 2), 
/* 143 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_1", (Holder)reference3), 2), 
/* 144 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_2", (Holder)reference3), 2), 
/* 145 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_large_farm_1", (Holder)reference3), 7), 
/* 146 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_1", (Holder)reference3), 4), 
/* 147 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_2", (Holder)reference3), 4), 
/* 148 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_1", (Holder)reference3), 2), 
/* 149 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_2", (Holder)reference3), 2), 
/* 150 */               Pair.of(StructurePoolElement.empty(), 5) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     context.register(TERMINATORS_KEY, new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 157 */           (List)ImmutableList.of(
/* 158 */             Pair.of(StructurePoolElement.legacy("village/desert/terminators/terminator_01"), 1), 
/* 159 */             Pair.of(StructurePoolElement.legacy("village/desert/terminators/terminator_02"), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     context.register(ZOMBIE_TERMINATORS_KEY, new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 166 */           (List)ImmutableList.of(
/* 167 */             Pair.of(StructurePoolElement.legacy("village/desert/terminators/terminator_01"), 1), 
/* 168 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/terminators/terminator_02"), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     Pools.register(context, "village/desert/decor", new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 175 */           (List)ImmutableList.of(
/* 176 */             Pair.of(StructurePoolElement.legacy("village/desert/desert_lamp_1"), 10), 
/* 177 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 4), 
/* 178 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 4), 
/* 179 */             Pair.of(StructurePoolElement.empty(), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     Pools.register(context, "village/desert/zombie/decor", new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 186 */           (List)ImmutableList.of(
/* 187 */             Pair.of(StructurePoolElement.legacy("village/desert/desert_lamp_1", (Holder)reference3), 10), 
/* 188 */             Pair.of(StructurePoolElement.feature((Holder)reference1), 4), 
/* 189 */             Pair.of(StructurePoolElement.feature((Holder)reference2), 4), 
/* 190 */             Pair.of(StructurePoolElement.empty(), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     Pools.register(context, "village/desert/villagers", new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 197 */           (List)ImmutableList.of(
/* 198 */             Pair.of(StructurePoolElement.legacy("village/desert/villagers/nitwit"), 1), 
/* 199 */             Pair.of(StructurePoolElement.legacy("village/desert/villagers/baby"), 1), 
/* 200 */             Pair.of(StructurePoolElement.legacy("village/desert/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     Pools.register(context, "village/desert/camel", new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 207 */           (List)ImmutableList.of(
/* 208 */             Pair.of(StructurePoolElement.legacy("village/desert/camel_spawn"), 1)), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Pools.register(context, "village/desert/zombie/villagers", new StructureTemplatePool((Holder)reference5, 
/*     */           
/* 215 */           (List)ImmutableList.of(
/* 216 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/villagers/nitwit"), 1), 
/* 217 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/villagers/unemployed"), 10)), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/DesertVillagePools.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */