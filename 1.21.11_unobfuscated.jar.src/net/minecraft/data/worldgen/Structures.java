/*     */ package net.minecraft.data.worldgen;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
/*     */ import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.BuriedTreasureStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.EndCityStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.IglooStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.JungleTempleStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.NetherFossilStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.ShipwreckStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.SwampHutStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ 
/*     */ public class Structures {
/*     */   public static void bootstrap(BootstrapContext<Structure> context) {
/*  50 */     HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
/*  51 */     HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);
/*     */     
/*  53 */     context.register(BuiltinStructures.PILLAGER_OUTPOST, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/*  54 */             biomes.getOrThrow(BiomeTags.HAS_PILLAGER_OUTPOST))
/*  55 */           .spawnOverrides(Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, 
/*  56 */                 WeightedList.of(new MobSpawnSettings.SpawnerData(EntityType.PILLAGER, 1, 1)))))
/*     */ 
/*     */ 
/*     */           
/*  60 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/*  61 */           .build(), (Holder)
/*  62 */           templates.getOrThrow(PillagerOutpostPools.START), 7, 
/*     */           
/*  64 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     context.register(BuiltinStructures.MINESHAFT, new MineshaftStructure(new Structure.StructureSettings.Builder((HolderSet)
/*  70 */             biomes.getOrThrow(BiomeTags.HAS_MINESHAFT))
/*  71 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
/*  72 */           .build(), MineshaftStructure.Type.NORMAL));
/*     */ 
/*     */ 
/*     */     
/*  76 */     context.register(BuiltinStructures.MINESHAFT_MESA, new MineshaftStructure(new Structure.StructureSettings.Builder((HolderSet)
/*  77 */             biomes.getOrThrow(BiomeTags.HAS_MINESHAFT_MESA))
/*  78 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
/*  79 */           .build(), MineshaftStructure.Type.MESA));
/*     */ 
/*     */ 
/*     */     
/*  83 */     context.register(BuiltinStructures.WOODLAND_MANSION, new WoodlandMansionStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_WOODLAND_MANSION))));
/*  84 */     context.register(BuiltinStructures.JUNGLE_TEMPLE, new JungleTempleStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_JUNGLE_TEMPLE))));
/*  85 */     context.register(BuiltinStructures.DESERT_PYRAMID, new DesertPyramidStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_DESERT_PYRAMID))));
/*  86 */     context.register(BuiltinStructures.IGLOO, new IglooStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_IGLOO))));
/*  87 */     context.register(BuiltinStructures.SHIPWRECK, new ShipwreckStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_SHIPWRECK)), false));
/*  88 */     context.register(BuiltinStructures.SHIPWRECK_BEACHED, new ShipwreckStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_SHIPWRECK_BEACHED)), true));
/*     */     
/*  90 */     context.register(BuiltinStructures.SWAMP_HUT, new SwampHutStructure(new Structure.StructureSettings.Builder((HolderSet)
/*  91 */             biomes.getOrThrow(BiomeTags.HAS_SWAMP_HUT))
/*  92 */           .spawnOverrides(Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, 
/*  93 */                 WeightedList.of(new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1))), MobCategory.CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, 
/*     */ 
/*     */                 
/*  96 */                 WeightedList.of(new MobSpawnSettings.SpawnerData(EntityType.CAT, 1, 1)))))
/*     */ 
/*     */ 
/*     */           
/* 100 */           .build()));
/*     */ 
/*     */     
/* 103 */     context.register(BuiltinStructures.STRONGHOLD, new StrongholdStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 104 */             biomes.getOrThrow(BiomeTags.HAS_STRONGHOLD))
/* 105 */           .terrainAdapation(TerrainAdjustment.BURY)
/* 106 */           .build()));
/*     */ 
/*     */     
/* 109 */     context.register(BuiltinStructures.OCEAN_MONUMENT, new OceanMonumentStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 110 */             biomes.getOrThrow(BiomeTags.HAS_OCEAN_MONUMENT))
/* 111 */           .spawnOverrides(Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, 
/* 112 */                 WeightedList.of(new MobSpawnSettings.SpawnerData(EntityType.GUARDIAN, 2, 4))), MobCategory.UNDERGROUND_WATER_CREATURE, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST), MobCategory.AXOLOTLS, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, MobSpawnSettings.EMPTY_MOB_LIST)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 118 */           .build()));
/*     */ 
/*     */     
/* 121 */     context.register(BuiltinStructures.OCEAN_RUIN_COLD, new OceanRuinStructure(new Structure.StructureSettings((HolderSet)
/* 122 */             biomes.getOrThrow(BiomeTags.HAS_OCEAN_RUIN_COLD)), OceanRuinStructure.Type.COLD, 0.3F, 0.9F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     context.register(BuiltinStructures.OCEAN_RUIN_WARM, new OceanRuinStructure(new Structure.StructureSettings((HolderSet)
/* 129 */             biomes.getOrThrow(BiomeTags.HAS_OCEAN_RUIN_WARM)), OceanRuinStructure.Type.WARM, 0.3F, 0.9F));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     context.register(BuiltinStructures.FORTRESS, new NetherFortressStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 136 */             biomes.getOrThrow(BiomeTags.HAS_NETHER_FORTRESS))
/* 137 */           .spawnOverrides(Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, NetherFortressStructure.FORTRESS_ENEMIES)))
/*     */ 
/*     */           
/* 140 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
/* 141 */           .build()));
/*     */ 
/*     */     
/* 144 */     context.register(BuiltinStructures.NETHER_FOSSIL, new NetherFossilStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 145 */             biomes.getOrThrow(BiomeTags.HAS_NETHER_FOSSIL))
/* 146 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
/* 147 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/* 148 */           .build(), 
/* 149 */           (HeightProvider)UniformHeight.of(VerticalAnchor.absolute(32), VerticalAnchor.belowTop(2))));
/*     */ 
/*     */     
/* 152 */     context.register(BuiltinStructures.END_CITY, new EndCityStructure(new Structure.StructureSettings((HolderSet)biomes.getOrThrow(BiomeTags.HAS_END_CITY))));
/*     */     
/* 154 */     context.register(BuiltinStructures.BURIED_TREASURE, new BuriedTreasureStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 155 */             biomes.getOrThrow(BiomeTags.HAS_BURIED_TREASURE))
/* 156 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
/* 157 */           .build()));
/*     */ 
/*     */     
/* 160 */     context.register(BuiltinStructures.BASTION_REMNANT, new JigsawStructure(new Structure.StructureSettings((HolderSet)
/* 161 */             biomes.getOrThrow(BiomeTags.HAS_BASTION_REMNANT)), (Holder)
/* 162 */           templates.getOrThrow(BastionPieces.START), 6, 
/*     */           
/* 164 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(33)), false));
/*     */ 
/*     */ 
/*     */     
/* 168 */     context.register(BuiltinStructures.VILLAGE_PLAINS, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 169 */             biomes.getOrThrow(BiomeTags.HAS_VILLAGE_PLAINS))
/* 170 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/* 171 */           .build(), (Holder)
/* 172 */           templates.getOrThrow(PlainVillagePools.START), 6, 
/*     */           
/* 174 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     context.register(BuiltinStructures.VILLAGE_DESERT, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 180 */             biomes.getOrThrow(BiomeTags.HAS_VILLAGE_DESERT))
/* 181 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/* 182 */           .build(), (Holder)
/* 183 */           templates.getOrThrow(DesertVillagePools.START), 6, 
/*     */           
/* 185 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     context.register(BuiltinStructures.VILLAGE_SAVANNA, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 191 */             biomes.getOrThrow(BiomeTags.HAS_VILLAGE_SAVANNA))
/* 192 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/* 193 */           .build(), (Holder)
/* 194 */           templates.getOrThrow(SavannaVillagePools.START), 6, 
/*     */           
/* 196 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     context.register(BuiltinStructures.VILLAGE_SNOWY, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 202 */             biomes.getOrThrow(BiomeTags.HAS_VILLAGE_SNOWY))
/* 203 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/* 204 */           .build(), (Holder)
/* 205 */           templates.getOrThrow(SnowyVillagePools.START), 6, 
/*     */           
/* 207 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     context.register(BuiltinStructures.VILLAGE_TAIGA, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 213 */             biomes.getOrThrow(BiomeTags.HAS_VILLAGE_TAIGA))
/* 214 */           .terrainAdapation(TerrainAdjustment.BEARD_THIN)
/* 215 */           .build(), (Holder)
/* 216 */           templates.getOrThrow(TaigaVillagePools.START), 6, 
/*     */           
/* 218 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(0)), true, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     context.register(BuiltinStructures.RUINED_PORTAL_STANDARD, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 224 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_STANDARD)), 
/* 225 */           List.of(new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.UNDERGROUND, 1.0F, 0.2F, false, false, true, false, 0.5F), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE, 0.5F, 0.2F, false, false, true, false, 0.5F))));
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
/* 249 */     context.register(BuiltinStructures.RUINED_PORTAL_DESERT, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 250 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_DESERT)), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.PARTLY_BURIED, 0.0F, 0.0F, false, false, false, false, 1.0F)));
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
/* 263 */     context.register(BuiltinStructures.RUINED_PORTAL_JUNGLE, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 264 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_JUNGLE)), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE, 0.5F, 0.8F, true, true, false, false, 1.0F)));
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
/* 277 */     context.register(BuiltinStructures.RUINED_PORTAL_SWAMP, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 278 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_SWAMP)), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR, 0.0F, 0.5F, false, true, false, false, 1.0F)));
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
/* 291 */     context.register(BuiltinStructures.RUINED_PORTAL_MOUNTAIN, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 292 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_MOUNTAIN)), 
/* 293 */           List.of(new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.IN_MOUNTAIN, 1.0F, 0.2F, false, false, true, false, 0.5F), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE, 0.5F, 0.2F, false, false, true, false, 0.5F))));
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
/* 317 */     context.register(BuiltinStructures.RUINED_PORTAL_OCEAN, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 318 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_OCEAN)), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR, 0.0F, 0.8F, false, false, true, false, 1.0F)));
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
/* 331 */     context.register(BuiltinStructures.RUINED_PORTAL_NETHER, new RuinedPortalStructure(new Structure.StructureSettings((HolderSet)
/* 332 */             biomes.getOrThrow(BiomeTags.HAS_RUINED_PORTAL_NETHER)), new RuinedPortalStructure.Setup(RuinedPortalPiece.VerticalPlacement.IN_NETHER, 0.5F, 0.0F, false, false, false, true, 1.0F)));
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
/* 345 */     context.register(BuiltinStructures.ANCIENT_CITY, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 346 */             biomes.getOrThrow(BiomeTags.HAS_ANCIENT_CITY))
/* 347 */           .spawnOverrides((Map)Arrays.<MobCategory>stream(MobCategory.values()).collect(Collectors.toMap(c -> c, c -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.of()))))
/*     */ 
/*     */           
/* 350 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
/* 351 */           .terrainAdapation(TerrainAdjustment.BEARD_BOX)
/* 352 */           .build(), (Holder)
/* 353 */           templates.getOrThrow(AncientCityStructurePieces.START), 
/* 354 */           Optional.of(net.minecraft.resources.Identifier.withDefaultNamespace("city_anchor")), 7, 
/*     */           
/* 356 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(-27)), false, 
/*     */           
/* 358 */           Optional.empty(), new JigsawStructure.MaxDistance(116), 
/*     */           
/* 360 */           List.of(), JigsawStructure.DEFAULT_DIMENSION_PADDING, JigsawStructure.DEFAULT_LIQUID_SETTINGS));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 365 */     context.register(BuiltinStructures.TRAIL_RUINS, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 366 */             biomes.getOrThrow(BiomeTags.HAS_TRAIL_RUINS))
/* 367 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
/* 368 */           .terrainAdapation(TerrainAdjustment.BURY)
/* 369 */           .build(), (Holder)
/* 370 */           templates.getOrThrow(TrailRuinsStructurePools.START), 7, 
/*     */           
/* 372 */           (HeightProvider)ConstantHeight.of(VerticalAnchor.absolute(-15)), false, Heightmap.Types.WORLD_SURFACE_WG));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     context.register(BuiltinStructures.TRIAL_CHAMBERS, new JigsawStructure(new Structure.StructureSettings.Builder((HolderSet)
/* 378 */             biomes.getOrThrow(BiomeTags.HAS_TRIAL_CHAMBERS))
/* 379 */           .generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
/* 380 */           .terrainAdapation(TerrainAdjustment.ENCAPSULATE)
/* 381 */           .spawnOverrides((Map)Arrays.<MobCategory>stream(MobCategory.values()).collect(Collectors.toMap(c -> c, c -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, WeightedList.of()))))
/*     */ 
/*     */           
/* 384 */           .build(), (Holder)
/* 385 */           templates.getOrThrow(TrialChambersStructurePools.START), 
/* 386 */           Optional.empty(), 20, 
/*     */           
/* 388 */           (HeightProvider)UniformHeight.of(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(-20)), false, 
/*     */           
/* 390 */           Optional.empty(), new JigsawStructure.MaxDistance(116), TrialChambersStructurePools.ALIAS_BINDINGS, new DimensionPadding(10), LiquidSettings.IGNORE_WATERLOGGING));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/worldgen/Structures.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */