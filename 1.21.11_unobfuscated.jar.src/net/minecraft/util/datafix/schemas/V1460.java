/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
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
/*     */ public class V1460
/*     */   extends NamespacedSchema
/*     */ {
/*     */   public V1460(int versionKey, Schema parent) {
/*  75 */     super(versionKey, parent);
/*     */   }
/*     */   
/*     */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  79 */     schema.registerSimple(map, name);
/*     */   }
/*     */   
/*     */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  83 */     schema.register(map, name, () -> V1458.nameableInventory(schema));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/*  88 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/*     */     
/*  90 */     schema.register(map, "minecraft:area_effect_cloud", name -> DSL.optionalFields("Particle", References.PARTICLE.in(schema)));
/*     */ 
/*     */     
/*  93 */     registerMob(schema, map, "minecraft:armor_stand");
/*  94 */     schema.register(map, "minecraft:arrow", name -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/*  97 */     registerMob(schema, map, "minecraft:bat");
/*  98 */     registerMob(schema, map, "minecraft:blaze");
/*  99 */     schema.registerSimple(map, "minecraft:boat");
/* 100 */     registerMob(schema, map, "minecraft:cave_spider");
/* 101 */     schema.register(map, "minecraft:chest_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */ 
/*     */ 
/*     */     
/* 105 */     registerMob(schema, map, "minecraft:chicken");
/* 106 */     schema.register(map, "minecraft:commandblock_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "LastOutput", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 110 */     registerMob(schema, map, "minecraft:cow");
/* 111 */     registerMob(schema, map, "minecraft:creeper");
/* 112 */     schema.register(map, "minecraft:donkey", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 116 */     schema.registerSimple(map, "minecraft:dragon_fireball");
/* 117 */     schema.registerSimple(map, "minecraft:egg");
/* 118 */     registerMob(schema, map, "minecraft:elder_guardian");
/* 119 */     schema.registerSimple(map, "minecraft:ender_crystal");
/* 120 */     registerMob(schema, map, "minecraft:ender_dragon");
/* 121 */     schema.register(map, "minecraft:enderman", name -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/* 124 */     registerMob(schema, map, "minecraft:endermite");
/* 125 */     schema.registerSimple(map, "minecraft:ender_pearl");
/* 126 */     schema.registerSimple(map, "minecraft:evocation_fangs");
/* 127 */     registerMob(schema, map, "minecraft:evocation_illager");
/* 128 */     schema.registerSimple(map, "minecraft:eye_of_ender_signal");
/* 129 */     schema.register(map, "minecraft:falling_block", name -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 133 */     schema.registerSimple(map, "minecraft:fireball");
/* 134 */     schema.register(map, "minecraft:fireworks_rocket", name -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 137 */     schema.register(map, "minecraft:furnace_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/* 140 */     registerMob(schema, map, "minecraft:ghast");
/* 141 */     registerMob(schema, map, "minecraft:giant");
/* 142 */     registerMob(schema, map, "minecraft:guardian");
/* 143 */     schema.register(map, "minecraft:hopper_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */ 
/*     */ 
/*     */     
/* 147 */     schema.register(map, "minecraft:horse", name -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 151 */     registerMob(schema, map, "minecraft:husk");
/* 152 */     registerMob(schema, map, "minecraft:illusion_illager");
/* 153 */     schema.register(map, "minecraft:item", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 156 */     schema.register(map, "minecraft:item_frame", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 159 */     schema.registerSimple(map, "minecraft:leash_knot");
/* 160 */     schema.register(map, "minecraft:llama", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     schema.registerSimple(map, "minecraft:llama_spit");
/* 166 */     registerMob(schema, map, "minecraft:magma_cube");
/* 167 */     schema.register(map, "minecraft:minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/* 170 */     registerMob(schema, map, "minecraft:mooshroom");
/* 171 */     schema.register(map, "minecraft:mule", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 175 */     registerMob(schema, map, "minecraft:ocelot");
/* 176 */     schema.registerSimple(map, "minecraft:painting");
/* 177 */     registerMob(schema, map, "minecraft:parrot");
/* 178 */     registerMob(schema, map, "minecraft:pig");
/* 179 */     registerMob(schema, map, "minecraft:polar_bear");
/* 180 */     schema.register(map, "minecraft:potion", name -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 183 */     registerMob(schema, map, "minecraft:rabbit");
/* 184 */     registerMob(schema, map, "minecraft:sheep");
/* 185 */     registerMob(schema, map, "minecraft:shulker");
/* 186 */     schema.registerSimple(map, "minecraft:shulker_bullet");
/* 187 */     registerMob(schema, map, "minecraft:silverfish");
/* 188 */     registerMob(schema, map, "minecraft:skeleton");
/* 189 */     schema.register(map, "minecraft:skeleton_horse", name -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 192 */     registerMob(schema, map, "minecraft:slime");
/* 193 */     schema.registerSimple(map, "minecraft:small_fireball");
/* 194 */     schema.registerSimple(map, "minecraft:snowball");
/* 195 */     registerMob(schema, map, "minecraft:snowman");
/* 196 */     schema.register(map, "minecraft:spawner_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 200 */     schema.register(map, "minecraft:spectral_arrow", name -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/* 203 */     registerMob(schema, map, "minecraft:spider");
/* 204 */     registerMob(schema, map, "minecraft:squid");
/* 205 */     registerMob(schema, map, "minecraft:stray");
/* 206 */     schema.registerSimple(map, "minecraft:tnt");
/* 207 */     schema.register(map, "minecraft:tnt_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/* 210 */     registerMob(schema, map, "minecraft:vex");
/* 211 */     schema.register(map, "minecraft:villager", name -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     registerMob(schema, map, "minecraft:villager_golem");
/* 218 */     registerMob(schema, map, "minecraft:vindication_illager");
/* 219 */     registerMob(schema, map, "minecraft:witch");
/* 220 */     registerMob(schema, map, "minecraft:wither");
/* 221 */     registerMob(schema, map, "minecraft:wither_skeleton");
/* 222 */     schema.registerSimple(map, "minecraft:wither_skull");
/* 223 */     registerMob(schema, map, "minecraft:wolf");
/* 224 */     schema.registerSimple(map, "minecraft:xp_bottle");
/* 225 */     schema.registerSimple(map, "minecraft:xp_orb");
/* 226 */     registerMob(schema, map, "minecraft:zombie");
/* 227 */     schema.register(map, "minecraft:zombie_horse", name -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 230 */     registerMob(schema, map, "minecraft:zombie_pigman");
/* 231 */     schema.register(map, "minecraft:zombie_villager", name -> DSL.optionalFields("Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 242 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/*     */     
/* 244 */     registerInventory(schema, map, "minecraft:furnace");
/* 245 */     registerInventory(schema, map, "minecraft:chest");
/* 246 */     registerInventory(schema, map, "minecraft:trapped_chest");
/* 247 */     schema.registerSimple(map, "minecraft:ender_chest");
/* 248 */     schema.register(map, "minecraft:jukebox", name -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 251 */     registerInventory(schema, map, "minecraft:dispenser");
/* 252 */     registerInventory(schema, map, "minecraft:dropper");
/* 253 */     schema.register(map, "minecraft:sign", () -> V99.sign(schema));
/* 254 */     schema.register(map, "minecraft:mob_spawner", name -> References.UNTAGGED_SPAWNER.in(schema));
/* 255 */     schema.register(map, "minecraft:piston", name -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(schema)));
/*     */ 
/*     */     
/* 258 */     registerInventory(schema, map, "minecraft:brewing_stand");
/* 259 */     schema.register(map, "minecraft:enchanting_table", () -> V1458.nameable(schema));
/* 260 */     schema.registerSimple(map, "minecraft:end_portal");
/* 261 */     schema.register(map, "minecraft:beacon", () -> V1458.nameable(schema));
/* 262 */     schema.register(map, "minecraft:skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */     
/* 265 */     schema.registerSimple(map, "minecraft:daylight_detector");
/* 266 */     registerInventory(schema, map, "minecraft:hopper");
/* 267 */     schema.registerSimple(map, "minecraft:comparator");
/* 268 */     schema.register(map, "minecraft:banner", () -> V1458.nameable(schema));
/* 269 */     schema.registerSimple(map, "minecraft:structure_block");
/* 270 */     schema.registerSimple(map, "minecraft:end_gateway");
/* 271 */     schema.register(map, "minecraft:command_block", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */     
/* 274 */     registerInventory(schema, map, "minecraft:shulker_box");
/* 275 */     schema.registerSimple(map, "minecraft:bed");
/*     */     
/* 277 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 282 */     schema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))), References.LIGHTWEIGHT_LEVEL.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     schema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
/* 289 */     schema.registerType(false, References.RECIPE, () -> DSL.constType(namespacedString()));
/* 290 */     schema.registerType(false, References.PLAYER, () -> DSL.optionalFields(new Pair[] { Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema))), Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(schema))), Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(schema))), Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(schema))), Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(schema)), Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(schema)), Pair.of("recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema)))) }));
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
/* 306 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(schema)))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(schema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), blockEntityTypes)));
/*     */ 
/*     */ 
/*     */     
/* 320 */     schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(schema)), References.ENTITY.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 324 */     schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(schema), DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(schema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), entityTypes))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", V99.itemStackTag(schema)), V705.ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */ 
/*     */ 
/*     */     
/* 335 */     schema.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema))));
/* 336 */     schema.registerType(false, References.OPTIONS, DSL::remainder);
/* 337 */     schema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     schema.registerType(false, References.BLOCK_NAME, () -> DSL.constType(namespacedString()));
/* 343 */     schema.registerType(false, References.ITEM_NAME, () -> DSL.constType(namespacedString()));
/* 344 */     schema.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 345 */     schema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
/*     */     
/*     */     Supplier<TypeTemplate> itemStats = () -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType()));
/*     */     
/* 349 */     schema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(new Pair[] { Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", itemStats.get()), Pair.of("minecraft:used", itemStats.get()), Pair.of("minecraft:broken", itemStats.get()), Pair.of("minecraft:picked_up", itemStats.get()), Pair.of("minecraft:dropped", itemStats.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))) })));
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
/* 362 */     schema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
/* 363 */     schema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
/* 364 */     schema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     schema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
/* 372 */     schema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
/* 373 */     schema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
/* 374 */     schema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(schema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     schema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
/* 384 */     schema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 389 */     schema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
/* 390 */     schema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
/* 391 */     schema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/*     */     
/* 393 */     Map<String, Supplier<TypeTemplate>> criterionTypes = V1451_6.createCriterionTypes(schema);
/* 394 */     schema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", (TypeTemplate)DSL.taggedChoiceLazy("type", DSL.string(), criterionTypes), "DisplayName", References.TEXT_COMPONENT.in(schema)), V1451_6.UNPACK_OBJECTIVE_ID, V1451_6.REPACK_OBJECTIVE_ID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 402 */     schema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(schema), "MemberNameSuffix", References.TEXT_COMPONENT.in(schema), "DisplayName", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 407 */     schema.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(schema))), "SpawnData", References.ENTITY_TREE.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     schema.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string())))));
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
/* 427 */     schema.registerType(false, References.BIOME, () -> DSL.constType(namespacedString()));
/* 428 */     schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(namespacedString()));
/* 429 */     schema.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 430 */     schema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
/* 431 */     schema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema))));
/*     */ 
/*     */     
/* 434 */     schema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
/* 435 */     schema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 440 */     schema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
/* 441 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
/* 442 */     schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.and(DSL.optional(DSL.field("ArmorItems", DSL.list(References.ITEM_STACK.in(schema)))), new TypeTemplate[] { DSL.optional(DSL.field("HandItems", DSL.list(References.ITEM_STACK.in(schema)))), DSL.optional(DSL.field("body_armor_item", References.ITEM_STACK.in(schema))), DSL.optional(DSL.field("saddle", References.ITEM_STACK.in(schema))) }));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V1460.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */