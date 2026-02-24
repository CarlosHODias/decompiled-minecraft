/*     */ package net.minecraft.client.model.geom;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ 
/*     */ public class ModelLayers
/*     */ {
/*     */   private static final String DEFAULT_LAYER = "main";
/*  14 */   private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
/*     */   
/*  16 */   public static final ModelLayerLocation ACACIA_BOAT = register("boat/acacia");
/*  17 */   public static final ModelLayerLocation ACACIA_CHEST_BOAT = register("chest_boat/acacia");
/*  18 */   public static final ModelLayerLocation ALLAY = register("allay");
/*  19 */   public static final ModelLayerLocation ARMADILLO = register("armadillo");
/*  20 */   public static final ModelLayerLocation ARMADILLO_BABY = register("armadillo_baby");
/*  21 */   public static final ModelLayerLocation ARMOR_STAND = register("armor_stand");
/*  22 */   public static final ArmorModelSet<ModelLayerLocation> ARMOR_STAND_ARMOR = registerArmorSet("armor_stand");
/*  23 */   public static final ModelLayerLocation ARMOR_STAND_SMALL = register("armor_stand_small");
/*  24 */   public static final ArmorModelSet<ModelLayerLocation> ARMOR_STAND_SMALL_ARMOR = registerArmorSet("armor_stand_small");
/*  25 */   public static final ModelLayerLocation ARROW = register("arrow");
/*  26 */   public static final ModelLayerLocation AXOLOTL = register("axolotl");
/*  27 */   public static final ModelLayerLocation AXOLOTL_BABY = register("axolotl_baby");
/*  28 */   public static final ModelLayerLocation BAMBOO_CHEST_RAFT = register("chest_boat/bamboo");
/*  29 */   public static final ModelLayerLocation BAMBOO_RAFT = register("boat/bamboo");
/*  30 */   public static final ModelLayerLocation STANDING_BANNER = register("standing_banner");
/*  31 */   public static final ModelLayerLocation STANDING_BANNER_FLAG = register("standing_banner", "flag");
/*  32 */   public static final ModelLayerLocation WALL_BANNER = register("wall_banner");
/*  33 */   public static final ModelLayerLocation WALL_BANNER_FLAG = register("wall_banner", "flag");
/*  34 */   public static final ModelLayerLocation BAT = register("bat");
/*  35 */   public static final ModelLayerLocation BED_FOOT = register("bed_foot");
/*  36 */   public static final ModelLayerLocation BED_HEAD = register("bed_head");
/*  37 */   public static final ModelLayerLocation BEE = register("bee");
/*  38 */   public static final ModelLayerLocation BEE_BABY = register("bee_baby");
/*  39 */   public static final ModelLayerLocation BEE_STINGER = register("bee_stinger");
/*  40 */   public static final ModelLayerLocation BELL = register("bell");
/*  41 */   public static final ModelLayerLocation BIRCH_BOAT = register("boat/birch");
/*  42 */   public static final ModelLayerLocation BIRCH_CHEST_BOAT = register("chest_boat/birch");
/*  43 */   public static final ModelLayerLocation BLAZE = register("blaze");
/*  44 */   public static final ModelLayerLocation BOAT_WATER_PATCH = register("boat", "water_patch");
/*  45 */   public static final ModelLayerLocation BOGGED = register("bogged");
/*  46 */   public static final ArmorModelSet<ModelLayerLocation> BOGGED_ARMOR = registerArmorSet("bogged");
/*  47 */   public static final ModelLayerLocation BOGGED_OUTER_LAYER = register("bogged", "outer");
/*  48 */   public static final ModelLayerLocation BOOK = register("book");
/*  49 */   public static final ModelLayerLocation BREEZE = register("breeze");
/*  50 */   public static final ModelLayerLocation BREEZE_WIND = register("breeze", "wind");
/*  51 */   public static final ModelLayerLocation BREEZE_EYES = register("breeze", "eyes");
/*  52 */   public static final ModelLayerLocation CAMEL = register("camel");
/*  53 */   public static final ModelLayerLocation CAMEL_BABY = register("camel_baby");
/*  54 */   public static final ModelLayerLocation CAMEL_SADDLE = register("camel", "saddle");
/*  55 */   public static final ModelLayerLocation CAMEL_BABY_SADDLE = register("camel_baby", "saddle");
/*  56 */   public static final ModelLayerLocation CAMEL_HUSK_SADDLE = register("camel_husk", "saddle");
/*  57 */   public static final ModelLayerLocation CAMEL_HUSK_BABY_SADDLE = register("camel_husk_baby", "saddle");
/*  58 */   public static final ModelLayerLocation CAT = register("cat");
/*  59 */   public static final ModelLayerLocation CAT_BABY = register("cat_baby");
/*  60 */   public static final ModelLayerLocation CAT_BABY_COLLAR = register("cat_baby", "collar");
/*  61 */   public static final ModelLayerLocation CAT_COLLAR = register("cat", "collar");
/*  62 */   public static final ModelLayerLocation CAVE_SPIDER = register("cave_spider");
/*  63 */   public static final ModelLayerLocation CHERRY_BOAT = register("boat/cherry");
/*  64 */   public static final ModelLayerLocation CHERRY_CHEST_BOAT = register("chest_boat/cherry");
/*  65 */   public static final ModelLayerLocation CHEST = register("chest");
/*  66 */   public static final ModelLayerLocation CHEST_MINECART = register("chest_minecart");
/*  67 */   public static final ModelLayerLocation CHICKEN = register("chicken");
/*  68 */   public static final ModelLayerLocation CHICKEN_BABY = register("chicken_baby");
/*  69 */   public static final ModelLayerLocation COD = register("cod");
/*  70 */   public static final ModelLayerLocation COLD_CHICKEN = register("cold_chicken");
/*  71 */   public static final ModelLayerLocation COLD_CHICKEN_BABY = register("cold_chicken_baby");
/*  72 */   public static final ModelLayerLocation COLD_COW = register("cold_cow");
/*  73 */   public static final ModelLayerLocation COLD_COW_BABY = register("cold_cow_baby");
/*  74 */   public static final ModelLayerLocation COLD_PIG = register("cold_pig");
/*  75 */   public static final ModelLayerLocation COLD_PIG_BABY = register("cold_pig_baby");
/*  76 */   public static final ModelLayerLocation COMMAND_BLOCK_MINECART = register("command_block_minecart");
/*  77 */   public static final ModelLayerLocation CONDUIT_CAGE = register("conduit", "cage");
/*  78 */   public static final ModelLayerLocation CONDUIT_EYE = register("conduit", "eye");
/*  79 */   public static final ModelLayerLocation CONDUIT_SHELL = register("conduit", "shell");
/*  80 */   public static final ModelLayerLocation CONDUIT_WIND = register("conduit", "wind");
/*  81 */   public static final ModelLayerLocation COPPER_GOLEM = register("copper_golem");
/*  82 */   public static final ModelLayerLocation COPPER_GOLEM_EYES = register("copper_golem", "eyes");
/*  83 */   public static final ModelLayerLocation COPPER_GOLEM_RUNNING = register("copper_golem_running");
/*  84 */   public static final ModelLayerLocation COPPER_GOLEM_SITTING = register("copper_golem_sitting");
/*  85 */   public static final ModelLayerLocation COPPER_GOLEM_STAR = register("copper_golem_star");
/*  86 */   public static final ModelLayerLocation ZOMBIE_NAUTILUS_CORAL = register("zombie_nautilus_coral");
/*  87 */   public static final ModelLayerLocation COW = register("cow");
/*  88 */   public static final ModelLayerLocation COW_BABY = register("cow_baby");
/*  89 */   public static final ModelLayerLocation CREAKING = register("creaking");
/*  90 */   public static final ModelLayerLocation CREAKING_EYES = register("creaking", "eyes");
/*  91 */   public static final ModelLayerLocation CREEPER = register("creeper");
/*  92 */   public static final ModelLayerLocation CREEPER_ARMOR = register("creeper", "armor");
/*  93 */   public static final ModelLayerLocation CREEPER_HEAD = register("creeper_head");
/*  94 */   public static final ModelLayerLocation DARK_OAK_BOAT = register("boat/dark_oak");
/*  95 */   public static final ModelLayerLocation DARK_OAK_CHEST_BOAT = register("chest_boat/dark_oak");
/*  96 */   public static final ModelLayerLocation DECORATED_POT_BASE = register("decorated_pot_base");
/*  97 */   public static final ModelLayerLocation DECORATED_POT_SIDES = register("decorated_pot_sides");
/*  98 */   public static final ModelLayerLocation DOLPHIN = register("dolphin");
/*  99 */   public static final ModelLayerLocation DOLPHIN_BABY = register("dolphin_baby");
/* 100 */   public static final ModelLayerLocation DONKEY = register("donkey");
/* 101 */   public static final ModelLayerLocation DONKEY_BABY = register("donkey_baby");
/* 102 */   public static final ModelLayerLocation DONKEY_SADDLE = register("donkey", "saddle");
/* 103 */   public static final ModelLayerLocation DONKEY_BABY_SADDLE = register("donkey_baby", "saddle");
/* 104 */   public static final ModelLayerLocation DOUBLE_CHEST_LEFT = register("double_chest_left");
/* 105 */   public static final ModelLayerLocation DOUBLE_CHEST_RIGHT = register("double_chest_right");
/* 106 */   public static final ModelLayerLocation DRAGON_SKULL = register("dragon_skull");
/* 107 */   public static final ModelLayerLocation DROWNED = register("drowned");
/* 108 */   public static final ModelLayerLocation DROWNED_BABY = register("drowned_baby");
/* 109 */   public static final ArmorModelSet<ModelLayerLocation> DROWNED_BABY_ARMOR = registerArmorSet("drowned_baby");
/* 110 */   public static final ModelLayerLocation DROWNED_BABY_OUTER_LAYER = register("drowned_baby", "outer");
/* 111 */   public static final ArmorModelSet<ModelLayerLocation> DROWNED_ARMOR = registerArmorSet("drowned");
/* 112 */   public static final ModelLayerLocation DROWNED_OUTER_LAYER = register("drowned", "outer");
/* 113 */   public static final ModelLayerLocation ELDER_GUARDIAN = register("elder_guardian");
/* 114 */   public static final ModelLayerLocation ELYTRA = register("elytra");
/* 115 */   public static final ModelLayerLocation ELYTRA_BABY = register("elytra_baby");
/* 116 */   public static final ModelLayerLocation ENDERMAN = register("enderman");
/* 117 */   public static final ModelLayerLocation ENDERMITE = register("endermite");
/* 118 */   public static final ModelLayerLocation ENDER_DRAGON = register("ender_dragon");
/* 119 */   public static final ModelLayerLocation END_CRYSTAL = register("end_crystal");
/* 120 */   public static final ModelLayerLocation EVOKER = register("evoker");
/* 121 */   public static final ModelLayerLocation EVOKER_FANGS = register("evoker_fangs");
/* 122 */   public static final ModelLayerLocation FOX = register("fox");
/* 123 */   public static final ModelLayerLocation FOX_BABY = register("fox_baby");
/* 124 */   public static final ModelLayerLocation FROG = register("frog");
/* 125 */   public static final ModelLayerLocation FURNACE_MINECART = register("furnace_minecart");
/* 126 */   public static final ModelLayerLocation GHAST = register("ghast");
/* 127 */   public static final ModelLayerLocation GIANT = register("giant");
/* 128 */   public static final ArmorModelSet<ModelLayerLocation> GIANT_ARMOR = registerArmorSet("giant");
/* 129 */   public static final ModelLayerLocation GLOW_SQUID = register("glow_squid");
/* 130 */   public static final ModelLayerLocation GLOW_SQUID_BABY = register("glow_squid_baby");
/* 131 */   public static final ModelLayerLocation GOAT = register("goat");
/* 132 */   public static final ModelLayerLocation GOAT_BABY = register("goat_baby");
/* 133 */   public static final ModelLayerLocation GUARDIAN = register("guardian");
/* 134 */   public static final ModelLayerLocation HAPPY_GHAST = register("happy_ghast");
/* 135 */   public static final ModelLayerLocation HAPPY_GHAST_BABY = register("happy_ghast_baby");
/* 136 */   public static final ModelLayerLocation HAPPY_GHAST_HARNESS = register("happy_ghast_harness");
/* 137 */   public static final ModelLayerLocation HAPPY_GHAST_BABY_HARNESS = register("happy_ghast_baby_harness");
/* 138 */   public static final ModelLayerLocation HAPPY_GHAST_ROPES = register("happy_ghast_ropes");
/* 139 */   public static final ModelLayerLocation HAPPY_GHAST_BABY_ROPES = register("happy_ghast_baby_ropes");
/* 140 */   public static final ModelLayerLocation HOGLIN = register("hoglin");
/* 141 */   public static final ModelLayerLocation HOGLIN_BABY = register("hoglin_baby");
/* 142 */   public static final ModelLayerLocation HOPPER_MINECART = register("hopper_minecart");
/* 143 */   public static final ModelLayerLocation HORSE = register("horse");
/* 144 */   public static final ModelLayerLocation HORSE_ARMOR = register("horse_armor");
/* 145 */   public static final ModelLayerLocation HORSE_SADDLE = register("horse", "saddle");
/* 146 */   public static final ModelLayerLocation HORSE_BABY = register("horse_baby");
/* 147 */   public static final ModelLayerLocation HORSE_BABY_ARMOR = register("horse_armor_baby");
/* 148 */   public static final ModelLayerLocation HORSE_BABY_SADDLE = register("horse_baby", "saddle");
/* 149 */   public static final ModelLayerLocation HUSK = register("husk");
/* 150 */   public static final ModelLayerLocation HUSK_BABY = register("husk_baby");
/* 151 */   public static final ArmorModelSet<ModelLayerLocation> HUSK_BABY_ARMOR = registerArmorSet("husk_baby");
/* 152 */   public static final ArmorModelSet<ModelLayerLocation> HUSK_ARMOR = registerArmorSet("husk");
/* 153 */   public static final ModelLayerLocation ILLUSIONER = register("illusioner");
/* 154 */   public static final ModelLayerLocation IRON_GOLEM = register("iron_golem");
/* 155 */   public static final ModelLayerLocation JUNGLE_BOAT = register("boat/jungle");
/* 156 */   public static final ModelLayerLocation JUNGLE_CHEST_BOAT = register("chest_boat/jungle");
/* 157 */   public static final ModelLayerLocation LEASH_KNOT = register("leash_knot");
/* 158 */   public static final ModelLayerLocation LLAMA = register("llama");
/* 159 */   public static final ModelLayerLocation LLAMA_BABY = register("llama_baby");
/* 160 */   public static final ModelLayerLocation LLAMA_BABY_DECOR = register("llama_baby", "decor");
/* 161 */   public static final ModelLayerLocation LLAMA_DECOR = register("llama", "decor");
/* 162 */   public static final ModelLayerLocation LLAMA_SPIT = register("llama_spit");
/* 163 */   public static final ModelLayerLocation MAGMA_CUBE = register("magma_cube");
/* 164 */   public static final ModelLayerLocation MANGROVE_BOAT = register("boat/mangrove");
/* 165 */   public static final ModelLayerLocation MANGROVE_CHEST_BOAT = register("chest_boat/mangrove");
/* 166 */   public static final ModelLayerLocation MINECART = register("minecart");
/* 167 */   public static final ModelLayerLocation MOOSHROOM = register("mooshroom");
/* 168 */   public static final ModelLayerLocation MOOSHROOM_BABY = register("mooshroom_baby");
/* 169 */   public static final ModelLayerLocation MULE = register("mule");
/* 170 */   public static final ModelLayerLocation MULE_BABY = register("mule_baby");
/* 171 */   public static final ModelLayerLocation MULE_SADDLE = register("mule", "saddle");
/* 172 */   public static final ModelLayerLocation MULE_BABY_SADDLE = register("mule_baby", "saddle");
/* 173 */   public static final ModelLayerLocation NAUTILUS = register("nautilus");
/* 174 */   public static final ModelLayerLocation NAUTILUS_BABY = register("nautilus_baby");
/* 175 */   public static final ModelLayerLocation NAUTILUS_SADDLE = register("nautilus", "saddle");
/* 176 */   public static final ModelLayerLocation NAUTILUS_ARMOR = register("nautilus_armor");
/* 177 */   public static final ModelLayerLocation OAK_BOAT = register("boat/oak");
/* 178 */   public static final ModelLayerLocation OAK_CHEST_BOAT = register("chest_boat/oak");
/* 179 */   public static final ModelLayerLocation OCELOT = register("ocelot");
/* 180 */   public static final ModelLayerLocation OCELOT_BABY = register("ocelot_baby");
/* 181 */   public static final ModelLayerLocation PALE_OAK_BOAT = register("boat/pale_oak");
/* 182 */   public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = register("chest_boat/pale_oak");
/* 183 */   public static final ModelLayerLocation PANDA = register("panda");
/* 184 */   public static final ModelLayerLocation PANDA_BABY = register("panda_baby");
/* 185 */   public static final ModelLayerLocation PARCHED = register("parched");
/* 186 */   public static final ArmorModelSet<ModelLayerLocation> PARCHED_ARMOR = registerArmorSet("parched");
/* 187 */   public static final ModelLayerLocation PARCHED_OUTER_LAYER = register("parched", "outer");
/* 188 */   public static final ModelLayerLocation PARROT = register("parrot");
/* 189 */   public static final ModelLayerLocation PHANTOM = register("phantom");
/* 190 */   public static final ModelLayerLocation PIG = register("pig");
/* 191 */   public static final ModelLayerLocation PIGLIN = register("piglin");
/* 192 */   public static final ModelLayerLocation PIGLIN_BABY = register("piglin_baby");
/* 193 */   public static final ArmorModelSet<ModelLayerLocation> PIGLIN_BABY_ARMOR = registerArmorSet("piglin_baby");
/* 194 */   public static final ModelLayerLocation PIGLIN_BRUTE = register("piglin_brute");
/* 195 */   public static final ArmorModelSet<ModelLayerLocation> PIGLIN_BRUTE_ARMOR = registerArmorSet("piglin_brute");
/* 196 */   public static final ModelLayerLocation PIGLIN_HEAD = register("piglin_head");
/* 197 */   public static final ArmorModelSet<ModelLayerLocation> PIGLIN_ARMOR = registerArmorSet("piglin");
/* 198 */   public static final ModelLayerLocation PIG_BABY = register("pig_baby");
/* 199 */   public static final ModelLayerLocation PIG_BABY_SADDLE = register("pig_baby", "saddle");
/* 200 */   public static final ModelLayerLocation PIG_SADDLE = register("pig", "saddle");
/* 201 */   public static final ModelLayerLocation PILLAGER = register("pillager");
/* 202 */   public static final ModelLayerLocation PLAYER = register("player");
/* 203 */   public static final ModelLayerLocation PLAYER_CAPE = register("player", "cape");
/* 204 */   public static final ModelLayerLocation PLAYER_EARS = register("player", "ears");
/* 205 */   public static final ModelLayerLocation PLAYER_HEAD = register("player_head");
/* 206 */   public static final ArmorModelSet<ModelLayerLocation> PLAYER_ARMOR = registerArmorSet("player");
/* 207 */   public static final ModelLayerLocation PLAYER_SLIM = register("player_slim");
/* 208 */   public static final ArmorModelSet<ModelLayerLocation> PLAYER_SLIM_ARMOR = registerArmorSet("player_slim");
/* 209 */   public static final ModelLayerLocation PLAYER_SPIN_ATTACK = register("spin_attack");
/* 210 */   public static final ModelLayerLocation POLAR_BEAR = register("polar_bear");
/* 211 */   public static final ModelLayerLocation POLAR_BEAR_BABY = register("polar_bear_baby");
/* 212 */   public static final ModelLayerLocation PUFFERFISH_BIG = register("pufferfish_big");
/* 213 */   public static final ModelLayerLocation PUFFERFISH_MEDIUM = register("pufferfish_medium");
/* 214 */   public static final ModelLayerLocation PUFFERFISH_SMALL = register("pufferfish_small");
/* 215 */   public static final ModelLayerLocation RABBIT = register("rabbit");
/* 216 */   public static final ModelLayerLocation RABBIT_BABY = register("rabbit_baby");
/* 217 */   public static final ModelLayerLocation RAVAGER = register("ravager");
/* 218 */   public static final ModelLayerLocation SALMON = register("salmon");
/* 219 */   public static final ModelLayerLocation SALMON_LARGE = register("salmon_large");
/* 220 */   public static final ModelLayerLocation SALMON_SMALL = register("salmon_small");
/* 221 */   public static final ModelLayerLocation SHEEP = register("sheep");
/* 222 */   public static final ModelLayerLocation SHEEP_BABY = register("sheep_baby");
/* 223 */   public static final ModelLayerLocation SHEEP_BABY_WOOL = register("sheep_baby", "wool");
/* 224 */   public static final ModelLayerLocation SHEEP_WOOL = register("sheep", "wool");
/* 225 */   public static final ModelLayerLocation SHEEP_WOOL_UNDERCOAT = register("sheep", "wool_undercoat");
/* 226 */   public static final ModelLayerLocation SHEEP_BABY_WOOL_UNDERCOAT = register("sheep_baby", "wool_undercoat");
/* 227 */   public static final ModelLayerLocation SHIELD = register("shield");
/* 228 */   public static final ModelLayerLocation SHULKER = register("shulker");
/* 229 */   public static final ModelLayerLocation SHULKER_BOX = register("shulker_box");
/* 230 */   public static final ModelLayerLocation SHULKER_BULLET = register("shulker_bullet");
/* 231 */   public static final ModelLayerLocation SILVERFISH = register("silverfish");
/* 232 */   public static final ModelLayerLocation SKELETON = register("skeleton");
/* 233 */   public static final ModelLayerLocation SKELETON_HORSE = register("skeleton_horse");
/* 234 */   public static final ModelLayerLocation SKELETON_HORSE_BABY = register("skeleton_horse_baby");
/* 235 */   public static final ModelLayerLocation SKELETON_HORSE_SADDLE = register("skeleton_horse", "saddle");
/* 236 */   public static final ModelLayerLocation SKELETON_HORSE_BABY_SADDLE = register("skeleton_horse_baby", "saddle");
/* 237 */   public static final ArmorModelSet<ModelLayerLocation> SKELETON_ARMOR = registerArmorSet("skeleton");
/* 238 */   public static final ModelLayerLocation SKELETON_SKULL = register("skeleton_skull");
/* 239 */   public static final ModelLayerLocation SLIME = register("slime");
/* 240 */   public static final ModelLayerLocation SLIME_OUTER = register("slime", "outer");
/* 241 */   public static final ModelLayerLocation SNIFFER = register("sniffer");
/* 242 */   public static final ModelLayerLocation SNIFFER_BABY = register("sniffer_baby");
/* 243 */   public static final ModelLayerLocation SNOW_GOLEM = register("snow_golem");
/* 244 */   public static final ModelLayerLocation SPAWNER_MINECART = register("spawner_minecart");
/* 245 */   public static final ModelLayerLocation SPIDER = register("spider");
/* 246 */   public static final ModelLayerLocation SPRUCE_BOAT = register("boat/spruce");
/* 247 */   public static final ModelLayerLocation SPRUCE_CHEST_BOAT = register("chest_boat/spruce");
/* 248 */   public static final ModelLayerLocation SQUID = register("squid");
/* 249 */   public static final ModelLayerLocation SQUID_BABY = register("squid_baby");
/* 250 */   public static final ModelLayerLocation STRAY = register("stray");
/* 251 */   public static final ArmorModelSet<ModelLayerLocation> STRAY_ARMOR = registerArmorSet("stray");
/* 252 */   public static final ModelLayerLocation STRAY_OUTER_LAYER = register("stray", "outer");
/* 253 */   public static final ModelLayerLocation STRIDER = register("strider");
/* 254 */   public static final ModelLayerLocation STRIDER_SADDLE = register("strider", "saddle");
/* 255 */   public static final ModelLayerLocation STRIDER_BABY = register("strider_baby");
/* 256 */   public static final ModelLayerLocation STRIDER_BABY_SADDLE = register("strider_baby", "saddle");
/* 257 */   public static final ModelLayerLocation TADPOLE = register("tadpole");
/* 258 */   public static final ModelLayerLocation TNT_MINECART = register("tnt_minecart");
/* 259 */   public static final ModelLayerLocation TRADER_LLAMA = register("trader_llama");
/* 260 */   public static final ModelLayerLocation TRADER_LLAMA_BABY = register("trader_llama_baby");
/* 261 */   public static final ModelLayerLocation TRIDENT = register("trident");
/* 262 */   public static final ModelLayerLocation TROPICAL_FISH_LARGE = register("tropical_fish_large");
/* 263 */   public static final ModelLayerLocation TROPICAL_FISH_LARGE_PATTERN = register("tropical_fish_large", "pattern");
/* 264 */   public static final ModelLayerLocation TROPICAL_FISH_SMALL = register("tropical_fish_small");
/* 265 */   public static final ModelLayerLocation TROPICAL_FISH_SMALL_PATTERN = register("tropical_fish_small", "pattern");
/* 266 */   public static final ModelLayerLocation TURTLE = register("turtle");
/* 267 */   public static final ModelLayerLocation TURTLE_BABY = register("turtle_baby");
/* 268 */   public static final ModelLayerLocation UNDEAD_HORSE_ARMOR = register("undead_horse_armor");
/* 269 */   public static final ModelLayerLocation UNDEAD_HORSE_BABY_ARMOR = register("undead_horse_baby_armor");
/* 270 */   public static final ModelLayerLocation VEX = register("vex");
/* 271 */   public static final ModelLayerLocation VILLAGER = register("villager");
/* 272 */   public static final ModelLayerLocation VILLAGER_NO_HAT = register("villager_no_hat");
/* 273 */   public static final ModelLayerLocation VILLAGER_BABY = register("villager_baby");
/* 274 */   public static final ModelLayerLocation VILLAGER_BABY_NO_HAT = register("villager_baby_no_hat");
/* 275 */   public static final ModelLayerLocation VINDICATOR = register("vindicator");
/* 276 */   public static final ModelLayerLocation WANDERING_TRADER = register("wandering_trader");
/* 277 */   public static final ModelLayerLocation WARDEN = register("warden");
/* 278 */   public static final ModelLayerLocation WARDEN_BIOLUMINESCENT = register("warden", "bioluminescent");
/* 279 */   public static final ModelLayerLocation WARDEN_PULSATING_SPOTS = register("warden", "pulsating_spots");
/* 280 */   public static final ModelLayerLocation WARDEN_TENDRILS = register("warden", "tendrils");
/* 281 */   public static final ModelLayerLocation WARDEN_HEART = register("warden", "heart");
/* 282 */   public static final ModelLayerLocation WARM_COW = register("warm_cow");
/* 283 */   public static final ModelLayerLocation WARM_COW_BABY = register("warm_cow_baby");
/* 284 */   public static final ModelLayerLocation WIND_CHARGE = register("wind_charge");
/* 285 */   public static final ModelLayerLocation WITCH = register("witch");
/* 286 */   public static final ModelLayerLocation WITHER = register("wither");
/* 287 */   public static final ModelLayerLocation WITHER_ARMOR = register("wither", "armor");
/* 288 */   public static final ModelLayerLocation WITHER_SKELETON = register("wither_skeleton");
/* 289 */   public static final ArmorModelSet<ModelLayerLocation> WITHER_SKELETON_ARMOR = registerArmorSet("wither_skeleton");
/* 290 */   public static final ModelLayerLocation WITHER_SKELETON_SKULL = register("wither_skeleton_skull");
/* 291 */   public static final ModelLayerLocation WITHER_SKULL = register("wither_skull");
/* 292 */   public static final ModelLayerLocation WOLF = register("wolf");
/* 293 */   public static final ModelLayerLocation WOLF_ARMOR = register("wolf_armor");
/* 294 */   public static final ModelLayerLocation WOLF_BABY = register("wolf_baby");
/* 295 */   public static final ModelLayerLocation WOLF_BABY_ARMOR = register("wolf_baby_armor");
/* 296 */   public static final ModelLayerLocation ZOGLIN = register("zoglin");
/* 297 */   public static final ModelLayerLocation ZOGLIN_BABY = register("zoglin_baby");
/* 298 */   public static final ModelLayerLocation ZOMBIE = register("zombie");
/* 299 */   public static final ModelLayerLocation ZOMBIE_BABY = register("zombie_baby");
/* 300 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_BABY_ARMOR = registerArmorSet("zombie_baby");
/* 301 */   public static final ModelLayerLocation ZOMBIE_HEAD = register("zombie_head");
/* 302 */   public static final ModelLayerLocation ZOMBIE_HORSE = register("zombie_horse");
/* 303 */   public static final ModelLayerLocation ZOMBIE_HORSE_BABY = register("zombie_horse_baby");
/* 304 */   public static final ModelLayerLocation ZOMBIE_HORSE_SADDLE = register("zombie_horse", "saddle");
/* 305 */   public static final ModelLayerLocation ZOMBIE_HORSE_BABY_SADDLE = register("zombie_horse_baby", "saddle");
/* 306 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_ARMOR = registerArmorSet("zombie");
/* 307 */   public static final ModelLayerLocation ZOMBIE_VILLAGER = register("zombie_villager");
/* 308 */   public static final ModelLayerLocation ZOMBIE_VILLAGER_NO_HAT = register("zombie_villager_no_hat");
/* 309 */   public static final ModelLayerLocation ZOMBIE_VILLAGER_BABY = register("zombie_villager_baby");
/* 310 */   public static final ModelLayerLocation ZOMBIE_VILLAGER_BABY_NO_HAT = register("zombie_villager_baby_no_hat");
/* 311 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_VILLAGER_BABY_ARMOR = registerArmorSet("zombie_villager_baby");
/* 312 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIE_VILLAGER_ARMOR = registerArmorSet("zombie_villager");
/* 313 */   public static final ModelLayerLocation ZOMBIFIED_PIGLIN = register("zombified_piglin");
/* 314 */   public static final ModelLayerLocation ZOMBIFIED_PIGLIN_BABY = register("zombified_piglin_baby");
/* 315 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIFIED_PIGLIN_BABY_ARMOR = registerArmorSet("zombified_piglin_baby");
/* 316 */   public static final ArmorModelSet<ModelLayerLocation> ZOMBIFIED_PIGLIN_ARMOR = registerArmorSet("zombified_piglin");
/* 317 */   public static final ModelLayerLocation ZOMBIE_NAUTILUS = register("zombie_nautilus");
/*     */   
/*     */   private static ModelLayerLocation register(String model) {
/* 320 */     return register(model, "main");
/*     */   }
/*     */   
/*     */   private static ModelLayerLocation register(String model, String layer) {
/* 324 */     ModelLayerLocation result = createLocation(model, layer);
/* 325 */     if (!ALL_MODELS.add(result)) {
/* 326 */       throw new IllegalStateException("Duplicate registration for " + String.valueOf(result));
/*     */     }
/* 328 */     return result;
/*     */   }
/*     */   
/*     */   private static ModelLayerLocation createLocation(String model, String layer) {
/* 332 */     return new ModelLayerLocation(Identifier.withDefaultNamespace(model), layer);
/*     */   }
/*     */   
/*     */   private static ArmorModelSet<ModelLayerLocation> registerArmorSet(String modelId) {
/* 336 */     return new ArmorModelSet(
/* 337 */         register(modelId, "helmet"), 
/* 338 */         register(modelId, "chestplate"), 
/* 339 */         register(modelId, "leggings"), 
/* 340 */         register(modelId, "boots"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ModelLayerLocation createStandingSignModelName(WoodType type) {
/* 345 */     return createLocation("sign/standing/" + type.name(), "main");
/*     */   }
/*     */   
/*     */   public static ModelLayerLocation createWallSignModelName(WoodType type) {
/* 349 */     return createLocation("sign/wall/" + type.name(), "main");
/*     */   }
/*     */   
/*     */   public static ModelLayerLocation createHangingSignModelName(WoodType type, HangingSignRenderer.AttachmentType attachmentType) {
/* 353 */     return createLocation("hanging_sign/" + type.name() + "/" + attachmentType.getSerializedName(), "main");
/*     */   }
/*     */   
/*     */   public static Stream<ModelLayerLocation> getKnownLocations() {
/* 357 */     return ALL_MODELS.stream();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/ModelLayers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */