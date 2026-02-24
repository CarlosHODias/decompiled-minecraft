/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPattern;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ 
/*     */ public class Sheets
/*     */ {
/*  29 */   public static final Identifier SHULKER_SHEET = Identifier.withDefaultNamespace("textures/atlas/shulker_boxes.png");
/*  30 */   public static final Identifier BED_SHEET = Identifier.withDefaultNamespace("textures/atlas/beds.png");
/*  31 */   public static final Identifier BANNER_SHEET = Identifier.withDefaultNamespace("textures/atlas/banner_patterns.png");
/*  32 */   public static final Identifier SHIELD_SHEET = Identifier.withDefaultNamespace("textures/atlas/shield_patterns.png");
/*  33 */   public static final Identifier SIGN_SHEET = Identifier.withDefaultNamespace("textures/atlas/signs.png");
/*  34 */   public static final Identifier CHEST_SHEET = Identifier.withDefaultNamespace("textures/atlas/chest.png");
/*  35 */   public static final Identifier ARMOR_TRIMS_SHEET = Identifier.withDefaultNamespace("textures/atlas/armor_trims.png");
/*  36 */   public static final Identifier DECORATED_POT_SHEET = Identifier.withDefaultNamespace("textures/atlas/decorated_pot.png");
/*  37 */   public static final Identifier GUI_SHEET = Identifier.withDefaultNamespace("textures/atlas/gui.png");
/*  38 */   public static final Identifier MAP_DECORATIONS_SHEET = Identifier.withDefaultNamespace("textures/atlas/map_decorations.png");
/*  39 */   public static final Identifier PAINTINGS_SHEET = Identifier.withDefaultNamespace("textures/atlas/paintings.png");
/*  40 */   public static final Identifier CELESTIAL_SHEET = Identifier.withDefaultNamespace("textures/atlas/celestials.png");
/*     */   
/*  42 */   private static final RenderType SHULKER_BOX_SHEET_TYPE = RenderTypes.entityCutoutNoCull(SHULKER_SHEET);
/*  43 */   private static final RenderType BED_SHEET_TYPE = RenderTypes.entitySolid(BED_SHEET);
/*  44 */   private static final RenderType BANNER_SHEET_TYPE = RenderTypes.entityNoOutline(BANNER_SHEET);
/*  45 */   private static final RenderType SHIELD_SHEET_TYPE = RenderTypes.entityNoOutline(SHIELD_SHEET);
/*  46 */   private static final RenderType SIGN_SHEET_TYPE = RenderTypes.entityCutoutNoCull(SIGN_SHEET);
/*  47 */   private static final RenderType CHEST_SHEET_TYPE = RenderTypes.entityCutout(CHEST_SHEET);
/*  48 */   private static final RenderType ARMOR_TRIMS_SHEET_TYPE = RenderTypes.armorCutoutNoCull(ARMOR_TRIMS_SHEET);
/*  49 */   private static final RenderType ARMOR_TRIMS_DECAL_SHEET_TYPE = RenderTypes.createArmorDecalCutoutNoCull(ARMOR_TRIMS_SHEET);
/*     */   
/*  51 */   private static final RenderType SOLID_BLOCK_SHEET = RenderTypes.entitySolid(TextureAtlas.LOCATION_BLOCKS);
/*  52 */   private static final RenderType CUTOUT_BLOCK_SHEET = RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS);
/*  53 */   private static final RenderType TRANSLUCENT_BLOCK_ITEM_SHEET = RenderTypes.itemEntityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);
/*  54 */   private static final RenderType TRANSLUCENT_ITEM_SHEET = RenderTypes.itemEntityTranslucentCull(TextureAtlas.LOCATION_ITEMS);
/*     */   
/*  56 */   public static final MaterialMapper ITEMS_MAPPER = new MaterialMapper(TextureAtlas.LOCATION_ITEMS, "item");
/*  57 */   public static final MaterialMapper BLOCKS_MAPPER = new MaterialMapper(TextureAtlas.LOCATION_BLOCKS, "block");
/*  58 */   public static final MaterialMapper BLOCK_ENTITIES_MAPPER = new MaterialMapper(TextureAtlas.LOCATION_BLOCKS, "entity");
/*     */   
/*  60 */   public static final MaterialMapper BANNER_MAPPER = new MaterialMapper(BANNER_SHEET, "entity/banner");
/*  61 */   public static final MaterialMapper SHIELD_MAPPER = new MaterialMapper(SHIELD_SHEET, "entity/shield");
/*  62 */   public static final MaterialMapper CHEST_MAPPER = new MaterialMapper(CHEST_SHEET, "entity/chest");
/*  63 */   public static final MaterialMapper DECORATED_POT_MAPPER = new MaterialMapper(DECORATED_POT_SHEET, "entity/decorated_pot");
/*  64 */   public static final MaterialMapper BED_MAPPER = new MaterialMapper(BED_SHEET, "entity/bed");
/*  65 */   public static final MaterialMapper SHULKER_MAPPER = new MaterialMapper(SHULKER_SHEET, "entity/shulker");
/*  66 */   public static final MaterialMapper SIGN_MAPPER = new MaterialMapper(SIGN_SHEET, "entity/signs");
/*  67 */   public static final MaterialMapper HANGING_SIGN_MAPPER = new MaterialMapper(SIGN_SHEET, "entity/signs/hanging");
/*     */   
/*  69 */   public static final Material DEFAULT_SHULKER_TEXTURE_LOCATION = SHULKER_MAPPER.defaultNamespaceApply("shulker");
/*  70 */   public static final List<Material> SHULKER_TEXTURE_LOCATION = (List<Material>)Arrays.<DyeColor>stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(Sheets::createShulkerMaterial).collect(ImmutableList.toImmutableList());
/*     */   
/*  72 */   public static final Map<WoodType, Material> SIGN_MATERIALS = (Map<WoodType, Material>)WoodType.values().collect(Collectors.toMap(Function.identity(), Sheets::createSignMaterial));
/*     */   
/*  74 */   public static final Map<WoodType, Material> HANGING_SIGN_MATERIALS = (Map<WoodType, Material>)WoodType.values().collect(Collectors.toMap(Function.identity(), Sheets::createHangingSignMaterial));
/*     */   
/*  76 */   public static final Material BANNER_BASE = BANNER_MAPPER.defaultNamespaceApply("base");
/*  77 */   public static final Material SHIELD_BASE = SHIELD_MAPPER.defaultNamespaceApply("base");
/*     */   
/*  79 */   private static final Map<Identifier, Material> BANNER_MATERIALS = new HashMap<>();
/*  80 */   private static final Map<Identifier, Material> SHIELD_MATERIALS = new HashMap<>(); public static final Map<ResourceKey<DecoratedPotPattern>, Material> DECORATED_POT_MATERIALS;
/*     */   static {
/*  82 */     DECORATED_POT_MATERIALS = (Map<ResourceKey<DecoratedPotPattern>, Material>)BuiltInRegistries.DECORATED_POT_PATTERN.listElements().collect(Collectors.toMap(Holder.Reference::key, holder -> DECORATED_POT_MAPPER.apply(((DecoratedPotPattern)holder.value()).assetId())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final Material DECORATED_POT_BASE = DECORATED_POT_MAPPER.defaultNamespaceApply("decorated_pot_base");
/*  88 */   public static final Material DECORATED_POT_SIDE = DECORATED_POT_MAPPER.defaultNamespaceApply("decorated_pot_side"); private static final Material[] BED_TEXTURES;
/*     */   static {
/*  90 */     BED_TEXTURES = (Material[])Arrays.<DyeColor>stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(Sheets::createBedMaterial).toArray(x$0 -> new Material[x$0]);
/*     */   }
/*  92 */   public static final Material CHEST_TRAP_LOCATION = CHEST_MAPPER.defaultNamespaceApply("trapped");
/*     */   
/*  94 */   public static final Material CHEST_TRAP_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("trapped_left");
/*  95 */   public static final Material CHEST_TRAP_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("trapped_right");
/*  96 */   public static final Material CHEST_XMAS_LOCATION = CHEST_MAPPER.defaultNamespaceApply("christmas");
/*  97 */   public static final Material CHEST_XMAS_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("christmas_left");
/*  98 */   public static final Material CHEST_XMAS_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("christmas_right");
/*  99 */   public static final Material CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("normal");
/* 100 */   public static final Material CHEST_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("normal_left");
/* 101 */   public static final Material CHEST_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("normal_right");
/* 102 */   public static final Material ENDER_CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("ender");
/* 103 */   public static final Material COPPER_CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("copper");
/* 104 */   public static final Material COPPER_CHEST_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("copper_left");
/* 105 */   public static final Material COPPER_CHEST_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("copper_right");
/* 106 */   public static final Material EXPOSED_COPPER_CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("copper_exposed");
/* 107 */   public static final Material EXPOSED_COPPER_CHEST_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("copper_exposed_left");
/* 108 */   public static final Material EXPOSED_COPPER_CHEST_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("copper_exposed_right");
/* 109 */   public static final Material WEATHERED_COPPER_CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("copper_weathered");
/* 110 */   public static final Material WEATHERED_COPPER_CHEST_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("copper_weathered_left");
/* 111 */   public static final Material WEATHERED_COPPER_CHEST_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("copper_weathered_right");
/* 112 */   public static final Material OXIDIZED_COPPER_CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("copper_oxidized");
/* 113 */   public static final Material OXIDIZED_COPPER_CHEST_LOCATION_LEFT = CHEST_MAPPER.defaultNamespaceApply("copper_oxidized_left");
/* 114 */   public static final Material OXIDIZED_COPPER_CHEST_LOCATION_RIGHT = CHEST_MAPPER.defaultNamespaceApply("copper_oxidized_right");
/*     */   
/*     */   public static RenderType bannerSheet() {
/* 117 */     return BANNER_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType shieldSheet() {
/* 121 */     return SHIELD_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType bedSheet() {
/* 125 */     return BED_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType shulkerBoxSheet() {
/* 129 */     return SHULKER_BOX_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType signSheet() {
/* 133 */     return SIGN_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType hangingSignSheet() {
/* 137 */     return SIGN_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType chestSheet() {
/* 141 */     return CHEST_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType armorTrimsSheet(boolean decal) {
/* 145 */     return decal ? ARMOR_TRIMS_DECAL_SHEET_TYPE : ARMOR_TRIMS_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType solidBlockSheet() {
/* 149 */     return SOLID_BLOCK_SHEET;
/*     */   }
/*     */   
/*     */   public static RenderType cutoutBlockSheet() {
/* 153 */     return CUTOUT_BLOCK_SHEET;
/*     */   }
/*     */   
/*     */   public static RenderType translucentItemSheet() {
/* 157 */     return TRANSLUCENT_ITEM_SHEET;
/*     */   }
/*     */   
/*     */   public static RenderType translucentBlockItemSheet() {
/* 161 */     return TRANSLUCENT_BLOCK_ITEM_SHEET;
/*     */   }
/*     */   
/*     */   public static Material getBedMaterial(DyeColor color) {
/* 165 */     return BED_TEXTURES[color.getId()];
/*     */   }
/*     */   
/*     */   public static Identifier colorToResourceMaterial(DyeColor color) {
/* 169 */     return Identifier.withDefaultNamespace(color.getName());
/*     */   }
/*     */   
/*     */   public static Material createBedMaterial(DyeColor color) {
/* 173 */     return BED_MAPPER.apply(colorToResourceMaterial(color));
/*     */   }
/*     */   
/*     */   public static Material getShulkerBoxMaterial(DyeColor color) {
/* 177 */     return SHULKER_TEXTURE_LOCATION.get(color.getId());
/*     */   }
/*     */   
/*     */   public static Identifier colorToShulkerMaterial(DyeColor color) {
/* 181 */     return Identifier.withDefaultNamespace("shulker_" + color.getName());
/*     */   }
/*     */   
/*     */   public static Material createShulkerMaterial(DyeColor color) {
/* 185 */     return SHULKER_MAPPER.apply(colorToShulkerMaterial(color));
/*     */   }
/*     */   
/*     */   private static Material createSignMaterial(WoodType type) {
/* 189 */     return SIGN_MAPPER.defaultNamespaceApply(type.name());
/*     */   }
/*     */   
/*     */   private static Material createHangingSignMaterial(WoodType type) {
/* 193 */     return HANGING_SIGN_MAPPER.defaultNamespaceApply(type.name());
/*     */   }
/*     */   
/*     */   public static Material getSignMaterial(WoodType type) {
/* 197 */     return SIGN_MATERIALS.get(type);
/*     */   }
/*     */   
/*     */   public static Material getHangingSignMaterial(WoodType type) {
/* 201 */     return HANGING_SIGN_MATERIALS.get(type);
/*     */   }
/*     */   
/*     */   public static Material getBannerMaterial(Holder<BannerPattern> pattern) {
/* 205 */     Objects.requireNonNull(BANNER_MAPPER); return BANNER_MATERIALS.computeIfAbsent(((BannerPattern)pattern.value()).assetId(), BANNER_MAPPER::apply);
/*     */   }
/*     */   
/*     */   public static Material getShieldMaterial(Holder<BannerPattern> pattern) {
/* 209 */     Objects.requireNonNull(SHIELD_MAPPER); return SHIELD_MATERIALS.computeIfAbsent(((BannerPattern)pattern.value()).assetId(), SHIELD_MAPPER::apply);
/*     */   }
/*     */   
/*     */   public static Material getDecoratedPotMaterial(ResourceKey<DecoratedPotPattern> pattern) {
/* 213 */     if (pattern == null) {
/* 214 */       return null;
/*     */     }
/* 216 */     return DECORATED_POT_MATERIALS.get(pattern);
/*     */   }
/*     */   
/*     */   public static Material chooseMaterial(ChestRenderState.ChestMaterialType materialType, ChestType type) {
/* 220 */     switch (materialType) { default: throw new MatchException(null, null);case ENDER_CHEST: case CHRISTMAS: case TRAPPED: case COPPER_UNAFFECTED: case COPPER_EXPOSED: case COPPER_WEATHERED: case COPPER_OXIDIZED: case REGULAR: break; }  return 
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
/* 234 */       chooseMaterial(type, CHEST_LOCATION, CHEST_LOCATION_LEFT, CHEST_LOCATION_RIGHT);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Material chooseMaterial(ChestType type, Material single, Material left, Material right) {
/* 239 */     switch (type) {
/*     */       case LEFT:
/* 241 */         return left;
/*     */       case RIGHT:
/* 243 */         return right;
/*     */     } 
/*     */     
/* 246 */     return single;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/Sheets.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */