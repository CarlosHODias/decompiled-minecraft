/*     */ package net.minecraft.client.data;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.ItemModelGenerators;
/*     */ import net.minecraft.client.renderer.MaterialMapper;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.blockentity.BellRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.ConduitRenderer;
/*     */ import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSources;
/*     */ import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
/*     */ import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
/*     */ import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
/*     */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ 
/*     */ public class AtlasProvider
/*     */   implements DataProvider {
/*  36 */   private static final Identifier TRIM_PALETTE_KEY = Identifier.withDefaultNamespace("trims/color_palettes/trim_palette");
/*     */   
/*     */   static {
/*  39 */     TRIM_PALETTE_VALUES = extractAllMaterialAssets().collect(Collectors.toMap(MaterialAssetGroup.AssetInfo::suffix, asset -> Identifier.withDefaultNamespace("trims/color_palettes/" + asset.suffix())));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Map<String, Identifier> TRIM_PALETTE_VALUES;
/*  44 */   private static final List<ResourceKey<TrimPattern>> VANILLA_PATTERNS = List.of((ResourceKey<TrimPattern>[])new ResourceKey[] { TrimPatterns.SENTRY, TrimPatterns.DUNE, TrimPatterns.COAST, TrimPatterns.WILD, TrimPatterns.WARD, TrimPatterns.EYE, TrimPatterns.VEX, TrimPatterns.TIDE, TrimPatterns.SNOUT, TrimPatterns.RIB, TrimPatterns.SPIRE, TrimPatterns.WAYFINDER, TrimPatterns.SHAPER, TrimPatterns.SILENCE, TrimPatterns.RAISER, TrimPatterns.HOST, TrimPatterns.FLOW, TrimPatterns.BOLT });
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
/*  65 */   private static final List<EquipmentClientInfo.LayerType> HUMANOID_LAYERS = List.of(EquipmentClientInfo.LayerType.HUMANOID, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS);
/*     */ 
/*     */   
/*     */   private final PackOutput.PathProvider pathProvider;
/*     */ 
/*     */ 
/*     */   
/*     */   public AtlasProvider(PackOutput output) {
/*  73 */     this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
/*     */   }
/*     */   
/*     */   private static List<Identifier> patternTextures() {
/*  77 */     List<Identifier> result = new ArrayList<>(VANILLA_PATTERNS.size() * HUMANOID_LAYERS.size());
/*  78 */     for (ResourceKey<TrimPattern> vanillaPattern : VANILLA_PATTERNS) {
/*  79 */       Identifier assetId = TrimPatterns.defaultAssetId(vanillaPattern);
/*  80 */       for (EquipmentClientInfo.LayerType humanoidLayer : HUMANOID_LAYERS) {
/*  81 */         result.add(assetId.withPath(patternPath -> humanoidLayer.trimAssetPrefix() + "/" + humanoidLayer.trimAssetPrefix()));
/*     */       }
/*     */     } 
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static SpriteSource forMaterial(Material material) {
/*  89 */     return (SpriteSource)new SingleFile(material.texture());
/*     */   }
/*     */ 
/*     */   
/*     */   private static SpriteSource forMapper(MaterialMapper mapper) {
/*  94 */     return (SpriteSource)new DirectoryLister(mapper.prefix(), mapper.prefix() + "/");
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> simpleMapper(MaterialMapper mapper) {
/*  98 */     return List.of(
/*  99 */         forMapper(mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<SpriteSource> noPrefixMapper(String directory) {
/* 104 */     return (List)List.of(new DirectoryLister(directory, ""));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Stream<MaterialAssetGroup.AssetInfo> extractAllMaterialAssets() {
/* 110 */     return 
/* 111 */       ItemModelGenerators.TRIM_MATERIAL_MODELS.stream()
/* 112 */       .map(ItemModelGenerators.TrimMaterialData::assets)
/* 113 */       .flatMap(asset -> Stream.concat(Stream.of(asset.base()), asset.overrides().values().stream()))
/* 114 */       .sorted(Comparator.comparing(MaterialAssetGroup.AssetInfo::suffix));
/*     */   }
/*     */   
/*     */   private static List<SpriteSource> armorTrims() {
/* 118 */     return (List)List.of(new PalettedPermutations(
/*     */           
/* 120 */           patternTextures(), TRIM_PALETTE_KEY, TRIM_PALETTE_VALUES));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<SpriteSource> blocksList() {
/* 128 */     return List.of(
/* 129 */         forMapper(Sheets.BLOCKS_MAPPER), 
/* 130 */         forMapper(ConduitRenderer.MAPPER), 
/* 131 */         forMaterial(BellRenderer.BELL_TEXTURE), 
/* 132 */         forMaterial(EnchantTableRenderer.BOOK_TEXTURE));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<SpriteSource> itemsList() {
/* 137 */     return (List)List.of(
/* 138 */         forMapper(Sheets.ITEMS_MAPPER), new PalettedPermutations(
/*     */ 
/*     */           
/* 141 */           List.of(ItemModelGenerators.TRIM_PREFIX_HELMET, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, ItemModelGenerators.TRIM_PREFIX_BOOTS), TRIM_PALETTE_KEY, TRIM_PALETTE_VALUES));
/*     */   }
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
/*     */   private static List<SpriteSource> bannerPatterns() {
/* 154 */     return List.of(
/* 155 */         forMaterial(ModelBakery.BANNER_BASE), 
/* 156 */         forMapper(Sheets.BANNER_MAPPER));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<SpriteSource> shieldPatterns() {
/* 161 */     return List.of(
/* 162 */         forMaterial(ModelBakery.SHIELD_BASE), 
/* 163 */         forMaterial(ModelBakery.NO_PATTERN_SHIELD), 
/* 164 */         forMapper(Sheets.SHIELD_MAPPER));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<SpriteSource> guiSprites() {
/* 169 */     return (List)List.of(new DirectoryLister("gui/sprites", ""), new DirectoryLister("mob_effect", "mob_effect/"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/* 177 */     return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { 
/* 178 */           storeAtlas(cache, AtlasIds.ARMOR_TRIMS, armorTrims()), 
/* 179 */           storeAtlas(cache, AtlasIds.BANNER_PATTERNS, bannerPatterns()), 
/* 180 */           storeAtlas(cache, AtlasIds.BEDS, simpleMapper(Sheets.BED_MAPPER)), 
/* 181 */           storeAtlas(cache, AtlasIds.BLOCKS, blocksList()), 
/* 182 */           storeAtlas(cache, AtlasIds.ITEMS, itemsList()), 
/* 183 */           storeAtlas(cache, AtlasIds.CHESTS, simpleMapper(Sheets.CHEST_MAPPER)), 
/* 184 */           storeAtlas(cache, AtlasIds.DECORATED_POT, simpleMapper(Sheets.DECORATED_POT_MAPPER)), 
/* 185 */           storeAtlas(cache, AtlasIds.GUI, guiSprites()), 
/* 186 */           storeAtlas(cache, AtlasIds.MAP_DECORATIONS, noPrefixMapper("map/decorations")), 
/* 187 */           storeAtlas(cache, AtlasIds.PAINTINGS, noPrefixMapper("painting")), 
/* 188 */           storeAtlas(cache, AtlasIds.PARTICLES, noPrefixMapper("particle")), 
/* 189 */           storeAtlas(cache, AtlasIds.SHIELD_PATTERNS, shieldPatterns()), 
/* 190 */           storeAtlas(cache, AtlasIds.SHULKER_BOXES, simpleMapper(Sheets.SHULKER_MAPPER)), 
/* 191 */           storeAtlas(cache, AtlasIds.SIGNS, simpleMapper(Sheets.SIGN_MAPPER)), 
/* 192 */           storeAtlas(cache, AtlasIds.CELESTIALS, noPrefixMapper("environment/celestial")) });
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<?> storeAtlas(CachedOutput cache, Identifier atlasId, List<SpriteSource> contents) {
/* 197 */     return DataProvider.saveStable(cache, SpriteSources.FILE_CODEC, contents, this.pathProvider.json(atlasId));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 202 */     return "Atlas Definitions";
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/AtlasProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */