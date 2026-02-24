/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import org.slf4j.Logger;
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
/*     */ public class V99
/*     */   extends Schema
/*     */ {
/*  75 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public V99(int versionKey, Schema parent) {
/*  78 */     super(versionKey, parent);
/*     */   }
/*     */   private static final Map<String, String> ITEM_TO_BLOCKENTITY;
/*     */   protected static void registerThrowableProjectile(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  82 */     schema.register(map, name, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void registerMinecart(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  89 */     schema.register(map, name, () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  95 */     schema.register(map, name, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 102 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/*     */     
/* 104 */     schema.register(map, "Item", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 107 */     schema.registerSimple(map, "XPOrb");
/* 108 */     registerThrowableProjectile(schema, map, "ThrownEgg");
/* 109 */     schema.registerSimple(map, "LeashKnot");
/* 110 */     schema.registerSimple(map, "Painting");
/* 111 */     schema.register(map, "Arrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/*     */ 
/*     */     
/* 114 */     schema.register(map, "TippedArrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/*     */ 
/*     */     
/* 117 */     schema.register(map, "SpectralArrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/*     */ 
/*     */     
/* 120 */     registerThrowableProjectile(schema, map, "Snowball");
/* 121 */     registerThrowableProjectile(schema, map, "Fireball");
/* 122 */     registerThrowableProjectile(schema, map, "SmallFireball");
/* 123 */     registerThrowableProjectile(schema, map, "ThrownEnderpearl");
/* 124 */     schema.registerSimple(map, "EyeOfEnderSignal");
/* 125 */     schema.register(map, "ThrownPotion", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema), "Potion", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 129 */     registerThrowableProjectile(schema, map, "ThrownExpBottle");
/* 130 */     schema.register(map, "ItemFrame", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 133 */     registerThrowableProjectile(schema, map, "WitherSkull");
/* 134 */     schema.registerSimple(map, "PrimedTnt");
/* 135 */     schema.register(map, "FallingSand", name -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 139 */     schema.register(map, "FireworksRocketEntity", name -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 142 */     schema.registerSimple(map, "Boat");
/*     */ 
/*     */     
/* 145 */     schema.register(map, "Minecart", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */ 
/*     */ 
/*     */     
/* 149 */     registerMinecart(schema, map, "MinecartRideable");
/* 150 */     schema.register(map, "MinecartChest", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */ 
/*     */ 
/*     */     
/* 154 */     registerMinecart(schema, map, "MinecartFurnace");
/* 155 */     registerMinecart(schema, map, "MinecartTNT");
/* 156 */     schema.register(map, "MinecartSpawner", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 160 */     schema.register(map, "MinecartHopper", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */ 
/*     */ 
/*     */     
/* 164 */     schema.register(map, "MinecartCommandBlock", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "LastOutput", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     schema.registerSimple(map, "ArmorStand");
/* 173 */     schema.registerSimple(map, "Creeper");
/* 174 */     schema.registerSimple(map, "Skeleton");
/* 175 */     schema.registerSimple(map, "Spider");
/* 176 */     schema.registerSimple(map, "Giant");
/* 177 */     schema.registerSimple(map, "Zombie");
/* 178 */     schema.registerSimple(map, "Slime");
/* 179 */     schema.registerSimple(map, "Ghast");
/* 180 */     schema.registerSimple(map, "PigZombie");
/* 181 */     schema.register(map, "Enderman", name -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema)));
/*     */ 
/*     */     
/* 184 */     schema.registerSimple(map, "CaveSpider");
/* 185 */     schema.registerSimple(map, "Silverfish");
/* 186 */     schema.registerSimple(map, "Blaze");
/* 187 */     schema.registerSimple(map, "LavaSlime");
/* 188 */     schema.registerSimple(map, "EnderDragon");
/* 189 */     schema.registerSimple(map, "WitherBoss");
/* 190 */     schema.registerSimple(map, "Bat");
/* 191 */     schema.registerSimple(map, "Witch");
/* 192 */     schema.registerSimple(map, "Endermite");
/* 193 */     schema.registerSimple(map, "Guardian");
/* 194 */     schema.registerSimple(map, "Pig");
/* 195 */     schema.registerSimple(map, "Sheep");
/* 196 */     schema.registerSimple(map, "Cow");
/* 197 */     schema.registerSimple(map, "Chicken");
/* 198 */     schema.registerSimple(map, "Squid");
/* 199 */     schema.registerSimple(map, "Wolf");
/* 200 */     schema.registerSimple(map, "MushroomCow");
/* 201 */     schema.registerSimple(map, "SnowMan");
/* 202 */     schema.registerSimple(map, "Ozelot");
/* 203 */     schema.registerSimple(map, "VillagerGolem");
/* 204 */     schema.register(map, "EntityHorse", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     schema.registerSimple(map, "Rabbit");
/* 210 */     schema.register(map, "Villager", name -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     schema.registerSimple(map, "EnderCrystal");
/*     */ 
/*     */ 
/*     */     
/* 220 */     schema.register(map, "AreaEffectCloud", name -> DSL.optionalFields("Particle", References.PARTICLE.in(schema)));
/*     */ 
/*     */     
/* 223 */     schema.registerSimple(map, "ShulkerBullet");
/* 224 */     schema.registerSimple(map, "DragonFireball");
/* 225 */     schema.registerSimple(map, "Shulker");
/*     */     
/* 227 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 232 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/*     */     
/* 234 */     registerInventory(schema, map, "Furnace");
/* 235 */     registerInventory(schema, map, "Chest");
/* 236 */     schema.registerSimple(map, "EnderChest");
/* 237 */     schema.register(map, "RecordPlayer", name -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */     
/* 240 */     registerInventory(schema, map, "Trap");
/* 241 */     registerInventory(schema, map, "Dropper");
/* 242 */     schema.register(map, "Sign", () -> sign(schema));
/* 243 */     schema.register(map, "MobSpawner", name -> References.UNTAGGED_SPAWNER.in(schema));
/* 244 */     schema.registerSimple(map, "Music");
/* 245 */     schema.registerSimple(map, "Piston");
/* 246 */     registerInventory(schema, map, "Cauldron");
/* 247 */     schema.registerSimple(map, "EnchantTable");
/* 248 */     schema.registerSimple(map, "Airportal");
/* 249 */     schema.register(map, "Control", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */     
/* 252 */     schema.registerSimple(map, "Beacon");
/* 253 */     schema.register(map, "Skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */     
/* 256 */     schema.registerSimple(map, "DLDetector");
/* 257 */     registerInventory(schema, map, "Hopper");
/* 258 */     schema.registerSimple(map, "Comparator");
/* 259 */     schema.register(map, "FlowerPot", name -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema))));
/*     */ 
/*     */     
/* 262 */     schema.register(map, "Banner", () -> DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     schema.registerSimple(map, "Structure");
/* 269 */     schema.registerSimple(map, "EndGateway");
/* 270 */     return map;
/*     */   }
/*     */   
/*     */   public static TypeTemplate sign(Schema schema) {
/* 274 */     return DSL.optionalFields(new Pair[] {
/* 275 */           Pair.of("Text1", References.TEXT_COMPONENT.in(schema)), 
/* 276 */           Pair.of("Text2", References.TEXT_COMPONENT.in(schema)), 
/* 277 */           Pair.of("Text3", References.TEXT_COMPONENT.in(schema)), 
/* 278 */           Pair.of("Text4", References.TEXT_COMPONENT.in(schema)), 
/* 279 */           Pair.of("FilteredText1", References.TEXT_COMPONENT.in(schema)), 
/* 280 */           Pair.of("FilteredText2", References.TEXT_COMPONENT.in(schema)), 
/* 281 */           Pair.of("FilteredText3", References.TEXT_COMPONENT.in(schema)), 
/* 282 */           Pair.of("FilteredText4", References.TEXT_COMPONENT.in(schema))
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 288 */     schema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))), References.LIGHTWEIGHT_LEVEL.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 294 */     schema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
/* 295 */     schema.registerType(false, References.PLAYER, () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema))));
/*     */ 
/*     */ 
/*     */     
/* 299 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 306 */     schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(schema), (TypeTemplate)DSL.taggedChoiceLazy("id", DSL.string(), blockEntityTypes)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(schema), References.ENTITY.in(schema)));
/*     */ 
/*     */ 
/*     */     
/* 316 */     schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 317 */     schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(schema), DSL.optionalFields("CustomName", DSL.constType(DSL.string()), (TypeTemplate)DSL.taggedChoiceLazy("id", DSL.string(), entityTypes))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema)), "tag", itemStackTag(schema)), ADD_NAMES, Hook.HookFunction.IDENTITY));
/*     */ 
/*     */ 
/*     */     
/* 328 */     schema.registerType(false, References.OPTIONS, DSL::remainder);
/* 329 */     schema.registerType(false, References.BLOCK_NAME, () -> DSL.or(DSL.constType(DSL.intType()), DSL.constType(NamespacedSchema.namespacedString())));
/* 330 */     schema.registerType(false, References.ITEM_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 331 */     schema.registerType(false, References.STATS, DSL::remainder);
/* 332 */     schema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
/* 333 */     schema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
/* 334 */     schema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     schema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
/* 342 */     schema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
/* 343 */     schema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
/* 344 */     schema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(schema))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 353 */     schema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
/* 354 */     schema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     schema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
/* 360 */     schema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
/* 361 */     schema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/* 362 */     schema.registerType(false, References.OBJECTIVE, DSL::remainder);
/* 363 */     schema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(schema), "MemberNameSuffix", References.TEXT_COMPONENT.in(schema), "DisplayName", References.TEXT_COMPONENT.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     schema.registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder);
/* 370 */     schema.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 371 */     schema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
/* 372 */     schema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema))));
/*     */ 
/*     */     
/* 375 */     schema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
/* 376 */     schema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 381 */     schema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
/* 382 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
/* 383 */     schema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     schema.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 389 */     schema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
/* 390 */     schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.optional(DSL.field("Equipment", DSL.list(References.ITEM_STACK.in(schema)))));
/*     */   }
/*     */   
/*     */   public static TypeTemplate itemStackTag(Schema schema) {
/* 394 */     return DSL.optionalFields(new Pair[] {
/* 395 */           Pair.of("EntityTag", References.ENTITY_TREE.in(schema)), 
/* 396 */           Pair.of("BlockEntityTag", References.BLOCK_ENTITY.in(schema)), 
/* 397 */           Pair.of("CanDestroy", DSL.list(References.BLOCK_NAME.in(schema))), 
/* 398 */           Pair.of("CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema))), 
/* 399 */           Pair.of("Items", DSL.list(References.ITEM_STACK.in(schema))), 
/* 400 */           Pair.of("ChargedProjectiles", DSL.list(References.ITEM_STACK.in(schema))), 
/*     */ 
/*     */           
/* 403 */           Pair.of("pages", DSL.list(References.TEXT_COMPONENT.in(schema))), 
/* 404 */           Pair.of("filtered_pages", DSL.compoundList(References.TEXT_COMPONENT.in(schema))), 
/* 405 */           Pair.of("display", DSL.optionalFields("Name", 
/* 406 */               References.TEXT_COMPONENT.in(schema), "Lore", 
/* 407 */               DSL.list(References.TEXT_COMPONENT.in(schema))))
/*     */         });
/*     */   }
/*     */   
/*     */   static {
/* 412 */     ITEM_TO_BLOCKENTITY = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           map.put("minecraft:furnace", "Furnace");
/*     */           map.put("minecraft:lit_furnace", "Furnace");
/*     */           map.put("minecraft:chest", "Chest");
/*     */           map.put("minecraft:trapped_chest", "Chest");
/*     */           map.put("minecraft:ender_chest", "EnderChest");
/*     */           map.put("minecraft:jukebox", "RecordPlayer");
/*     */           map.put("minecraft:dispenser", "Trap");
/*     */           map.put("minecraft:dropper", "Dropper");
/*     */           map.put("minecraft:sign", "Sign");
/*     */           map.put("minecraft:mob_spawner", "MobSpawner");
/*     */           map.put("minecraft:noteblock", "Music");
/*     */           map.put("minecraft:brewing_stand", "Cauldron");
/*     */           map.put("minecraft:enhanting_table", "EnchantTable");
/*     */           map.put("minecraft:command_block", "CommandBlock");
/*     */           map.put("minecraft:beacon", "Beacon");
/*     */           map.put("minecraft:skull", "Skull");
/*     */           map.put("minecraft:daylight_detector", "DLDetector");
/*     */           map.put("minecraft:hopper", "Hopper");
/*     */           map.put("minecraft:banner", "Banner");
/*     */           map.put("minecraft:flower_pot", "FlowerPot");
/*     */           map.put("minecraft:repeating_command_block", "CommandBlock");
/*     */           map.put("minecraft:chain_command_block", "CommandBlock");
/*     */           map.put("minecraft:standing_sign", "Sign");
/*     */           map.put("minecraft:wall_sign", "Sign");
/*     */           map.put("minecraft:piston_head", "Piston");
/*     */           map.put("minecraft:daylight_detector_inverted", "DLDetector");
/*     */           map.put("minecraft:unpowered_comparator", "Comparator");
/*     */           map.put("minecraft:powered_comparator", "Comparator");
/*     */           map.put("minecraft:wall_banner", "Banner");
/*     */           map.put("minecraft:standing_banner", "Banner");
/*     */           map.put("minecraft:structure_block", "Structure");
/*     */           map.put("minecraft:end_portal", "Airportal");
/*     */           map.put("minecraft:end_gateway", "EndGateway");
/*     */           map.put("minecraft:shield", "Banner");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 454 */   public static final Map<String, String> ITEM_TO_ENTITY = Map.of("minecraft:armor_stand", "ArmorStand", "minecraft:painting", "Painting");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 459 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction()
/*     */     {
/*     */       public <T> T apply(DynamicOps<T> ops, T value) {
/* 462 */         return V99.addNames(new Dynamic(ops, value), V99.ITEM_TO_BLOCKENTITY, V99.ITEM_TO_ENTITY);
/*     */       }
/*     */     };
/*     */   
/*     */   protected static <T> T addNames(Dynamic<T> input, Map<String, String> itemToBlockEntityMap, Map<String, String> itemToEntityMap) {
/* 467 */     return (T)input.update("tag", itemStackTag -> itemStackTag.update("BlockEntityTag", ()).update("EntityTag", ()))
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
/* 490 */       .getValue();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/schemas/V99.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */