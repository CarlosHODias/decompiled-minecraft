/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkPalettedStorageFix extends com.mojang.datafixers.DataFix {
/*     */   private static final int NORTH_WEST_MASK = 128;
/*     */   private static final int WEST_MASK = 64;
/*     */   private static final int SOUTH_WEST_MASK = 32;
/*     */   private static final int SOUTH_MASK = 16;
/*     */   private static final int SOUTH_EAST_MASK = 8;
/*     */   private static final int EAST_MASK = 4;
/*     */   private static final int NORTH_EAST_MASK = 2;
/*     */   private static final int NORTH_MASK = 1;
/*     */   
/*     */   public ChunkPalettedStorageFix(Schema outputSchema, boolean changesType) {
/*  45 */     super(outputSchema, changesType);
/*     */   }
/*     */   
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final int SIZE = 4096;
/*     */   
/*     */   private static class MappingConstants
/*     */   {
/*  53 */     private static final BitSet VIRTUAL = new BitSet(256);
/*  54 */     private static final BitSet FIX = new BitSet(256);
/*  55 */     private static final Dynamic<?> PUMPKIN = ExtraDataFixUtils.blockState("minecraft:pumpkin");
/*  56 */     private static final Dynamic<?> SNOWY_PODZOL = ExtraDataFixUtils.blockState("minecraft:podzol", Map.of("snowy", "true"));
/*  57 */     private static final Dynamic<?> SNOWY_GRASS = ExtraDataFixUtils.blockState("minecraft:grass_block", Map.of("snowy", "true"));
/*  58 */     private static final Dynamic<?> SNOWY_MYCELIUM = ExtraDataFixUtils.blockState("minecraft:mycelium", Map.of("snowy", "true"));
/*  59 */     private static final Dynamic<?> UPPER_SUNFLOWER = ExtraDataFixUtils.blockState("minecraft:sunflower", Map.of("half", "upper"));
/*  60 */     private static final Dynamic<?> UPPER_LILAC = ExtraDataFixUtils.blockState("minecraft:lilac", Map.of("half", "upper"));
/*  61 */     private static final Dynamic<?> UPPER_TALL_GRASS = ExtraDataFixUtils.blockState("minecraft:tall_grass", Map.of("half", "upper"));
/*  62 */     private static final Dynamic<?> UPPER_LARGE_FERN = ExtraDataFixUtils.blockState("minecraft:large_fern", Map.of("half", "upper"));
/*  63 */     private static final Dynamic<?> UPPER_ROSE_BUSH = ExtraDataFixUtils.blockState("minecraft:rose_bush", Map.of("half", "upper"));
/*  64 */     private static final Dynamic<?> UPPER_PEONY = ExtraDataFixUtils.blockState("minecraft:peony", Map.of("half", "upper"));
/*     */     private static final Map<String, Dynamic<?>> FLOWER_POT_MAP; private static final Map<String, Dynamic<?>> SKULL_MAP; private static final Map<String, Dynamic<?>> DOOR_MAP; private static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP; private static final Int2ObjectMap<String> DYE_COLOR_MAP; private static final Map<String, Dynamic<?>> BED_BLOCK_MAP; private static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP; private static void mapSkull(Map<String, Dynamic<?>> map, int i, String name, String type) { map.put("" + i + "north", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "north"))); map.put("" + i + "east", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "east"))); map.put("" + i + "south", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "south"))); map.put("" + i + "west", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "west"))); for (int rot = 0; rot < 16; rot++)
/*  66 */         map.put("" + i + i, ExtraDataFixUtils.blockState("minecraft:" + name + "_" + type, Map.of("rotation", String.valueOf(rot))));  } static { FLOWER_POT_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */             map.put("minecraft:air0", ExtraDataFixUtils.blockState("minecraft:flower_pot"));
/*     */             
/*     */             map.put("minecraft:red_flower0", ExtraDataFixUtils.blockState("minecraft:potted_poppy"));
/*     */             map.put("minecraft:red_flower1", ExtraDataFixUtils.blockState("minecraft:potted_blue_orchid"));
/*     */             map.put("minecraft:red_flower2", ExtraDataFixUtils.blockState("minecraft:potted_allium"));
/*     */             map.put("minecraft:red_flower3", ExtraDataFixUtils.blockState("minecraft:potted_azure_bluet"));
/*     */             map.put("minecraft:red_flower4", ExtraDataFixUtils.blockState("minecraft:potted_red_tulip"));
/*     */             map.put("minecraft:red_flower5", ExtraDataFixUtils.blockState("minecraft:potted_orange_tulip"));
/*     */             map.put("minecraft:red_flower6", ExtraDataFixUtils.blockState("minecraft:potted_white_tulip"));
/*     */             map.put("minecraft:red_flower7", ExtraDataFixUtils.blockState("minecraft:potted_pink_tulip"));
/*     */             map.put("minecraft:red_flower8", ExtraDataFixUtils.blockState("minecraft:potted_oxeye_daisy"));
/*     */             map.put("minecraft:yellow_flower0", ExtraDataFixUtils.blockState("minecraft:potted_dandelion"));
/*     */             map.put("minecraft:sapling0", ExtraDataFixUtils.blockState("minecraft:potted_oak_sapling"));
/*     */             map.put("minecraft:sapling1", ExtraDataFixUtils.blockState("minecraft:potted_spruce_sapling"));
/*     */             map.put("minecraft:sapling2", ExtraDataFixUtils.blockState("minecraft:potted_birch_sapling"));
/*     */             map.put("minecraft:sapling3", ExtraDataFixUtils.blockState("minecraft:potted_jungle_sapling"));
/*     */             map.put("minecraft:sapling4", ExtraDataFixUtils.blockState("minecraft:potted_acacia_sapling"));
/*     */             map.put("minecraft:sapling5", ExtraDataFixUtils.blockState("minecraft:potted_dark_oak_sapling"));
/*     */             map.put("minecraft:red_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_red_mushroom"));
/*     */             map.put("minecraft:brown_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_brown_mushroom"));
/*     */             map.put("minecraft:deadbush0", ExtraDataFixUtils.blockState("minecraft:potted_dead_bush"));
/*     */             map.put("minecraft:tallgrass2", ExtraDataFixUtils.blockState("minecraft:potted_fern"));
/*     */             map.put("minecraft:cactus0", ExtraDataFixUtils.blockState("minecraft:potted_cactus"));
/*     */           });
/*  91 */       SKULL_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */             mapSkull(map, 0, "skeleton", "skull");
/*     */ 
/*     */             
/*     */             mapSkull(map, 1, "wither_skeleton", "skull");
/*     */ 
/*     */             
/*     */             mapSkull(map, 2, "zombie", "head");
/*     */ 
/*     */             
/*     */             mapSkull(map, 3, "player", "head");
/*     */ 
/*     */             
/*     */             mapSkull(map, 4, "creeper", "head");
/*     */ 
/*     */             
/*     */             mapSkull(map, 5, "dragon", "head");
/*     */           });
/*     */       
/* 110 */       DOOR_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */             mapDoor(map, "oak_door");
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
/*     */             mapDoor(map, "iron_door");
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
/*     */             mapDoor(map, "spruce_door");
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
/*     */             mapDoor(map, "birch_door");
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
/*     */             mapDoor(map, "jungle_door");
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
/*     */             mapDoor(map, "acacia_door");
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
/*     */             mapDoor(map, "dark_oak_door");
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       NOTE_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */             for (int i = 0; i < 26; i++) {
/*     */               map.put("true" + i, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "true", "note", String.valueOf(i))));
/*     */               
/*     */               map.put("false" + i, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "false", "note", String.valueOf(i))));
/*     */             } 
/*     */           });
/* 195 */       DYE_COLOR_MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
/*     */             map.put(0, "white");
/*     */             
/*     */             map.put(1, "orange");
/*     */             map.put(2, "magenta");
/*     */             map.put(3, "light_blue");
/*     */             map.put(4, "yellow");
/*     */             map.put(5, "lime");
/*     */             map.put(6, "pink");
/*     */             map.put(7, "gray");
/*     */             map.put(8, "light_gray");
/*     */             map.put(9, "cyan");
/*     */             map.put(10, "purple");
/*     */             map.put(11, "blue");
/*     */             map.put(12, "brown");
/*     */             map.put(13, "green");
/*     */             map.put(14, "red");
/*     */             map.put(15, "black");
/*     */           });
/* 214 */       BED_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */             ObjectIterator<Int2ObjectMap.Entry<String>> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             while (objectIterator.hasNext()) {
/*     */               Int2ObjectMap.Entry<String> entry = objectIterator.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               if (!Objects.equals(entry.getValue(), "red")) {
/*     */                 addBeds(map, entry.getIntKey(), (String)entry.getValue());
/*     */               }
/*     */             } 
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 237 */       BANNER_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */             ObjectIterator<Int2ObjectMap.Entry<String>> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             while (objectIterator.hasNext()) {
/*     */               Int2ObjectMap.Entry<String> entry = objectIterator.next();
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               if (!Objects.equals(entry.getValue(), "white")) {
/*     */                 addBanners(map, 15 - entry.getIntKey(), (String)entry.getValue());
/*     */               }
/*     */             } 
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 257 */       FIX.set(2);
/* 258 */       FIX.set(3);
/* 259 */       FIX.set(110);
/*     */       
/* 261 */       FIX.set(140);
/* 262 */       FIX.set(144);
/*     */       
/* 264 */       FIX.set(25);
/*     */       
/* 266 */       FIX.set(86);
/*     */ 
/*     */       
/* 269 */       FIX.set(26);
/* 270 */       FIX.set(176);
/* 271 */       FIX.set(177);
/*     */       
/* 273 */       FIX.set(175);
/*     */       
/* 275 */       FIX.set(64);
/* 276 */       FIX.set(71);
/* 277 */       FIX.set(193);
/* 278 */       FIX.set(194);
/* 279 */       FIX.set(195);
/* 280 */       FIX.set(196);
/* 281 */       FIX.set(197);
/*     */       
/* 283 */       VIRTUAL.set(54);
/* 284 */       VIRTUAL.set(146);
/*     */       
/* 286 */       VIRTUAL.set(25);
/*     */       
/* 288 */       VIRTUAL.set(26);
/*     */       
/* 290 */       VIRTUAL.set(51);
/*     */       
/* 292 */       VIRTUAL.set(53);
/* 293 */       VIRTUAL.set(67);
/* 294 */       VIRTUAL.set(108);
/* 295 */       VIRTUAL.set(109);
/* 296 */       VIRTUAL.set(114);
/* 297 */       VIRTUAL.set(128);
/* 298 */       VIRTUAL.set(134);
/* 299 */       VIRTUAL.set(135);
/* 300 */       VIRTUAL.set(136);
/* 301 */       VIRTUAL.set(156);
/* 302 */       VIRTUAL.set(163);
/* 303 */       VIRTUAL.set(164);
/* 304 */       VIRTUAL.set(180);
/* 305 */       VIRTUAL.set(203);
/*     */       
/* 307 */       VIRTUAL.set(55);
/*     */       
/* 309 */       VIRTUAL.set(85);
/* 310 */       VIRTUAL.set(113);
/* 311 */       VIRTUAL.set(188);
/* 312 */       VIRTUAL.set(189);
/* 313 */       VIRTUAL.set(190);
/* 314 */       VIRTUAL.set(191);
/* 315 */       VIRTUAL.set(192);
/*     */       
/* 317 */       VIRTUAL.set(93);
/* 318 */       VIRTUAL.set(94);
/*     */       
/* 320 */       VIRTUAL.set(101);
/* 321 */       VIRTUAL.set(102);
/* 322 */       VIRTUAL.set(160);
/*     */       
/* 324 */       VIRTUAL.set(106);
/*     */ 
/*     */       
/* 327 */       VIRTUAL.set(107);
/* 328 */       VIRTUAL.set(183);
/* 329 */       VIRTUAL.set(184);
/* 330 */       VIRTUAL.set(185);
/* 331 */       VIRTUAL.set(186);
/* 332 */       VIRTUAL.set(187);
/*     */       
/* 334 */       VIRTUAL.set(132);
/* 335 */       VIRTUAL.set(139);
/*     */       
/* 337 */       VIRTUAL.set(199); }
/*     */     private static void mapDoor(Map<String, Dynamic<?>> map, String type) { String id = "minecraft:" + type; map.put("minecraft:" + type + "eastlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "eastlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "eastlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "eastlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "eastlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "eastlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "eastlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "eastlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "eastupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "eastupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "eastupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "eastupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "eastupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "eastupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "eastupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "eastupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "northlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "northlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "northlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "northlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "northlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "northlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "northlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "northlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "northupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "northupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "northupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "northupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "northupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "northupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "northupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "northupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "southlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "southlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "southlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "southlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "southlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "southlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "southlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "southlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "southupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "southupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "southupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "southupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "southupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "southupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "southupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "southupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "westlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "westlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "westlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "westlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "westlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "westlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "westlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "westlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "westupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "westupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))); map.put("minecraft:" + type + "westupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))); map.put("minecraft:" + type + "westupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))); map.put("minecraft:" + type + "westupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))); map.put("minecraft:" + type + "westupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/*     */       map.put("minecraft:" + type + "westupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/* 340 */       map.put("minecraft:" + type + "westupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))); } private static final Dynamic<?> AIR = ExtraDataFixUtils.blockState("minecraft:air"); private static void addBeds(Map<String, Dynamic<?>> map, int colorId, String color) { map.put("southfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "foot"))); map.put("westfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "foot"))); map.put("northfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "foot"))); map.put("eastfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "foot"))); map.put("southfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "head"))); map.put("westfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "head"))); map.put("northfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "head"))); map.put("eastfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "head"))); map.put("southtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "true", "part", "head"))); map.put("westtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "true", "part", "head"))); map.put("northtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "true", "part", "head"))); map.put("easttruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "true", "part", "head"))); }
/*     */     private static void addBanners(Map<String, Dynamic<?>> map, int colorId, String color) { for (int i = 0; i < 16; i++)
/*     */         map.put("" + i + "_" + i, ExtraDataFixUtils.blockState("minecraft:" + color + "_banner", Map.of("rotation", String.valueOf(i))));  map.put("north_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "north")));
/*     */       map.put("south_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "south")));
/*     */       map.put("west_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "west")));
/*     */       map.put("east_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "east"))); } }
/* 346 */   public static String getName(Dynamic<?> state) { return state.get("Name").asString(""); }
/*     */ 
/*     */   
/*     */   public static String getProperty(Dynamic<?> state, String property) {
/* 350 */     return state.get("Properties").get(property).asString("");
/*     */   }
/*     */   
/*     */   public static int idFor(CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> states, Dynamic<?> state) {
/* 354 */     int id = states.getId(state);
/* 355 */     if (id == -1) {
/* 356 */       id = states.add(state);
/*     */     }
/* 358 */     return id;
/*     */   }
/*     */   
/*     */   private Dynamic<?> fix(Dynamic<?> input) {
/* 362 */     Optional<? extends Dynamic<?>> level = input.get("Level").result();
/* 363 */     if (level.isPresent() && ((Dynamic)level.get()).get("Sections").asStreamOpt().result().isPresent()) {
/* 364 */       return input.set("Level", new UpgradeChunk(level.get()).write());
/*     */     }
/* 366 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 371 */     Type<?> oldType = getInputSchema().getType(References.CHUNK);
/* 372 */     Type<?> newType = getOutputSchema().getType(References.CHUNK);
/* 373 */     return writeFixAndRead("ChunkPalettedStorageFix", oldType, newType, this::fix);
/*     */   }
/*     */   
/*     */   private static class Section {
/* 377 */     private final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> palette = CrudeIncrementalIntIdentityHashBiMap.create(32);
/*     */     
/*     */     private final List<Dynamic<?>> listTag;
/*     */     private final Dynamic<?> section;
/*     */     private final boolean hasData;
/* 382 */     private final Int2ObjectMap<IntList> toFix = (Int2ObjectMap<IntList>)new Int2ObjectLinkedOpenHashMap();
/*     */     
/* 384 */     private final IntList update = (IntList)new IntArrayList();
/*     */     public final int y;
/* 386 */     private final Set<Dynamic<?>> seen = Sets.newIdentityHashSet();
/* 387 */     private final int[] buffer = new int[4096];
/*     */     
/*     */     public Section(Dynamic<?> section) {
/* 390 */       this.listTag = Lists.newArrayList();
/* 391 */       this.section = section;
/* 392 */       this.y = section.get("Y").asInt(0);
/* 393 */       this.hasData = section.get("Blocks").result().isPresent();
/*     */     }
/*     */     
/*     */     public Dynamic<?> getBlock(int pos) {
/* 397 */       if (pos < 0 || pos > 4095) {
/* 398 */         return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */       }
/*     */       
/* 401 */       Dynamic<?> tag = (Dynamic)this.palette.byId(this.buffer[pos]);
/* 402 */       return (tag == null) ? ChunkPalettedStorageFix.MappingConstants.AIR : tag;
/*     */     }
/*     */     
/*     */     public void setBlock(int idx, Dynamic<?> blockState) {
/* 406 */       if (this.seen.add(blockState)) {
/* 407 */         this.listTag.add("%%FILTER_ME%%".equals(ChunkPalettedStorageFix.getName(blockState)) ? ChunkPalettedStorageFix.MappingConstants.AIR : blockState);
/*     */       }
/* 409 */       this.buffer[idx] = ChunkPalettedStorageFix.idFor(this.palette, blockState);
/*     */     }
/*     */     
/*     */     public int upgrade(int sides) {
/* 413 */       if (!this.hasData) {
/* 414 */         return sides;
/*     */       }
/* 416 */       ByteBuffer blocks = this.section.get("Blocks").asByteBufferOpt().result().get();
/* 417 */       ChunkPalettedStorageFix.DataLayer data = this.section.get("Data").asByteBufferOpt().map(buffer -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(buffer))).result().orElseGet(DataLayer::new);
/* 418 */       ChunkPalettedStorageFix.DataLayer addBlocks = this.section.get("Add").asByteBufferOpt().map(buffer -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(buffer))).result().orElseGet(DataLayer::new);
/*     */       
/* 420 */       this.seen.add(ChunkPalettedStorageFix.MappingConstants.AIR);
/* 421 */       ChunkPalettedStorageFix.idFor(this.palette, ChunkPalettedStorageFix.MappingConstants.AIR);
/* 422 */       this.listTag.add(ChunkPalettedStorageFix.MappingConstants.AIR);
/*     */       
/* 424 */       for (int idx = 0; idx < 4096; idx++) {
/* 425 */         int xx = idx & 0xF;
/* 426 */         int yy = idx >> 8 & 0xF;
/* 427 */         int zz = idx >> 4 & 0xF;
/* 428 */         int id = addBlocks.get(xx, yy, zz) << 12 | (blocks.get(idx) & 0xFF) << 4 | data.get(xx, yy, zz);
/*     */         
/* 430 */         if (ChunkPalettedStorageFix.MappingConstants.FIX.get(id >> 4)) {
/* 431 */           addFix(id >> 4, idx);
/*     */         }
/* 433 */         if (ChunkPalettedStorageFix.MappingConstants.VIRTUAL.get(id >> 4)) {
/*     */           
/* 435 */           int s = ChunkPalettedStorageFix.getSideMask((xx == 0), (xx == 15), (zz == 0), (zz == 15));
/* 436 */           if (s == 0) {
/*     */             
/* 438 */             this.update.add(idx);
/*     */           } else {
/* 440 */             sides |= s;
/*     */           } 
/*     */         } 
/*     */         
/* 444 */         setBlock(idx, BlockStateData.getTag(id));
/*     */       } 
/*     */       
/* 447 */       return sides;
/*     */     }
/*     */     private void addFix(int id, int position) {
/*     */       IntArrayList intArrayList;
/* 451 */       IntList list = (IntList)this.toFix.get(id);
/* 452 */       if (list == null) {
/* 453 */         intArrayList = new IntArrayList();
/* 454 */         this.toFix.put(id, intArrayList);
/*     */       } 
/* 456 */       intArrayList.add(position);
/*     */     }
/*     */     
/*     */     public Dynamic<?> write() {
/* 460 */       Dynamic<?> section = this.section;
/* 461 */       if (!this.hasData) {
/* 462 */         return section;
/*     */       }
/* 464 */       section = section.set("Palette", section.createList(this.listTag.stream()));
/*     */       
/* 466 */       int size = Math.max(4, DataFixUtils.ceillog2(this.seen.size()));
/* 467 */       PackedBitStorage storage = new PackedBitStorage(size, 4096);
/* 468 */       for (int j = 0; j < this.buffer.length; j++) {
/* 469 */         storage.set(j, this.buffer[j]);
/*     */       }
/*     */       
/* 472 */       section = section.set("BlockStates", section.createLongList(Arrays.stream(storage.getRaw())));
/*     */       
/* 474 */       section = section.remove("Blocks");
/* 475 */       section = section.remove("Data");
/* 476 */       section = section.remove("Add");
/*     */       
/* 478 */       return section;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UpgradeChunk
/*     */   {
/*     */     private int sides;
/* 485 */     private final ChunkPalettedStorageFix.Section[] sections = new ChunkPalettedStorageFix.Section[16];
/*     */     
/*     */     private final Dynamic<?> level;
/*     */     private final int x;
/*     */     private final int z;
/* 490 */     private final Int2ObjectMap<Dynamic<?>> blockEntities = (Int2ObjectMap<Dynamic<?>>)new Int2ObjectLinkedOpenHashMap(16);
/*     */     
/*     */     public UpgradeChunk(Dynamic<?> level) {
/* 493 */       this.level = level;
/* 494 */       this.x = level.get("xPos").asInt(0) << 4;
/* 495 */       this.z = level.get("zPos").asInt(0) << 4;
/*     */       
/* 497 */       level.get("TileEntities").asStreamOpt().ifSuccess(s -> s.forEach(()));
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
/* 510 */       boolean convertedFromAlphaFormat = level.get("convertedFromAlphaFormat").asBoolean(false);
/*     */       
/* 512 */       level.get("Sections").asStreamOpt().ifSuccess(s -> s.forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 520 */       for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 521 */         if (section != null)
/*     */         {
/*     */ 
/*     */           
/* 525 */           for (ObjectIterator<Int2ObjectMap.Entry<IntList>> objectIterator = section.toFix.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { IntListIterator<Integer> intListIterator; Int2ObjectMap.Entry<IntList> entry = objectIterator.next();
/* 526 */             int dy = section.y << 12;
/* 527 */             switch (entry.getIntKey()) {
/*     */               case 2:
/* 529 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 530 */                   pos |= dy;
/*     */                   
/* 532 */                   Dynamic<?> state = getBlock(pos);
/* 533 */                   if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(state))) {
/* 534 */                     String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 535 */                     if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
/* 536 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_GRASS);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 3:
/* 543 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 544 */                   pos |= dy;
/*     */                   
/* 546 */                   Dynamic<?> state = getBlock(pos);
/* 547 */                   if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(state))) {
/* 548 */                     String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 549 */                     if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
/* 550 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_PODZOL);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 110:
/* 557 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 558 */                   pos |= dy;
/*     */                   
/* 560 */                   Dynamic<?> state = getBlock(pos);
/* 561 */                   if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(state))) {
/* 562 */                     String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.UP)));
/* 563 */                     if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
/* 564 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SNOWY_MYCELIUM);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 25:
/* 571 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 572 */                   pos |= dy;
/* 573 */                   Dynamic<?> entity = removeBlockEntity(pos);
/* 574 */                   if (entity != null) {
/* 575 */                     String key = Boolean.toString(entity.get("powered").asBoolean(false)) + Boolean.toString(entity.get("powered").asBoolean(false));
/* 576 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.getOrDefault(key, ChunkPalettedStorageFix.MappingConstants.NOTE_BLOCK_MAP.get("false0")));
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 26:
/* 582 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 583 */                   pos |= dy;
/* 584 */                   Dynamic<?> entity = getBlockEntity(pos);
/* 585 */                   Dynamic<?> state = getBlock(pos);
/* 586 */                   if (entity != null) {
/* 587 */                     int color = entity.get("color").asInt(0);
/* 588 */                     if (color != 14 && color >= 0 && color < 16) {
/* 589 */                       String key = ChunkPalettedStorageFix.getProperty(state, "facing") + ChunkPalettedStorageFix.getProperty(state, "facing") + ChunkPalettedStorageFix.getProperty(state, "occupied") + ChunkPalettedStorageFix.getProperty(state, "part");
/* 590 */                       if (ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.containsKey(key)) {
/* 591 */                         setBlock(pos, ChunkPalettedStorageFix.MappingConstants.BED_BLOCK_MAP.get(key));
/*     */                       }
/*     */                     } 
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 176:
/*     */               case 177:
/* 600 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 601 */                   pos |= dy;
/* 602 */                   Dynamic<?> entity = getBlockEntity(pos);
/* 603 */                   Dynamic<?> state = getBlock(pos);
/* 604 */                   if (entity != null) {
/* 605 */                     int color = entity.get("Base").asInt(0);
/* 606 */                     if (color != 15 && color >= 0 && color < 16) {
/* 607 */                       String key = ChunkPalettedStorageFix.getProperty(state, (entry.getIntKey() == 176) ? "rotation" : "facing") + "_" + ChunkPalettedStorageFix.getProperty(state, (entry.getIntKey() == 176) ? "rotation" : "facing");
/* 608 */                       if (ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.containsKey(key)) {
/* 609 */                         setBlock(pos, ChunkPalettedStorageFix.MappingConstants.BANNER_BLOCK_MAP.get(key));
/*     */                       }
/*     */                     } 
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 86:
/* 617 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 618 */                   pos |= dy;
/*     */                   
/* 620 */                   Dynamic<?> state = getBlock(pos);
/* 621 */                   if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(state))) {
/* 622 */                     String name = ChunkPalettedStorageFix.getName(getBlock(relative(pos, ChunkPalettedStorageFix.Direction.DOWN)));
/* 623 */                     if ("minecraft:grass_block".equals(name) || "minecraft:dirt".equals(name)) {
/* 624 */                       setBlock(pos, ChunkPalettedStorageFix.MappingConstants.PUMPKIN);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 140:
/* 631 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 632 */                   pos |= dy;
/* 633 */                   Dynamic<?> entity = removeBlockEntity(pos);
/* 634 */                   if (entity != null) {
/* 635 */                     String key = entity.get("Item").asString("") + entity.get("Item").asString("");
/* 636 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.getOrDefault(key, ChunkPalettedStorageFix.MappingConstants.FLOWER_POT_MAP.get("minecraft:air0")));
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 144:
/* 642 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 643 */                   pos |= dy;
/* 644 */                   Dynamic<?> entity = getBlockEntity(pos);
/* 645 */                   if (entity != null) {
/* 646 */                     String key; String type = String.valueOf(entity.get("SkullType").asInt(0));
/* 647 */                     String facing = ChunkPalettedStorageFix.getProperty(getBlock(pos), "facing");
/*     */                     
/* 649 */                     if ("up".equals(facing) || "down".equals(facing)) {
/* 650 */                       key = type + type;
/*     */                     } else {
/* 652 */                       key = type + type;
/*     */                     } 
/*     */                     
/* 655 */                     entity.remove("SkullType");
/* 656 */                     entity.remove("facing");
/* 657 */                     entity.remove("Rot");
/*     */                     
/* 659 */                     setBlock(pos, ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.getOrDefault(key, ChunkPalettedStorageFix.MappingConstants.SKULL_MAP.get("0north")));
/*     */                   }  }
/*     */               
/*     */               
/*     */               case 64:
/*     */               case 71:
/*     */               case 193:
/*     */               case 194:
/*     */               case 195:
/*     */               case 196:
/*     */               case 197:
/* 670 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 671 */                   pos |= dy;
/*     */                   
/* 673 */                   Dynamic<?> state = getBlock(pos);
/* 674 */                   if (ChunkPalettedStorageFix.getName(state).endsWith("_door")) {
/* 675 */                     Dynamic<?> lower = getBlock(pos);
/* 676 */                     if ("lower".equals(ChunkPalettedStorageFix.getProperty(lower, "half"))) {
/* 677 */                       int abovePos = relative(pos, ChunkPalettedStorageFix.Direction.UP);
/* 678 */                       Dynamic<?> upper = getBlock(abovePos);
/* 679 */                       String name = ChunkPalettedStorageFix.getName(lower);
/* 680 */                       if (name.equals(ChunkPalettedStorageFix.getName(upper))) {
/* 681 */                         String facing = ChunkPalettedStorageFix.getProperty(lower, "facing");
/* 682 */                         String open = ChunkPalettedStorageFix.getProperty(lower, "open");
/* 683 */                         String hinge = convertedFromAlphaFormat ? "left" : ChunkPalettedStorageFix.getProperty(upper, "hinge");
/* 684 */                         String powered = convertedFromAlphaFormat ? "false" : ChunkPalettedStorageFix.getProperty(upper, "powered");
/* 685 */                         setBlock(pos, ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(name + name + "lower" + facing + hinge + open));
/* 686 */                         setBlock(abovePos, ChunkPalettedStorageFix.MappingConstants.DOOR_MAP.get(name + name + "upper" + facing + hinge + open));
/*     */                       } 
/*     */                     } 
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 175:
/* 694 */                 for (intListIterator = ((IntList)entry.getValue()).iterator(); intListIterator.hasNext(); ) { int pos = (Integer)intListIterator.next();
/* 695 */                   pos |= dy;
/*     */                   
/* 697 */                   Dynamic<?> block = getBlock(pos);
/* 698 */                   if ("upper".equals(ChunkPalettedStorageFix.getProperty(block, "half"))) {
/* 699 */                     Dynamic<?> below = getBlock(relative(pos, ChunkPalettedStorageFix.Direction.DOWN));
/* 700 */                     String variant = ChunkPalettedStorageFix.getName(below);
/* 701 */                     switch (variant) { case "minecraft:sunflower":
/* 702 */                         setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_SUNFLOWER);
/* 703 */                       case "minecraft:lilac": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_LILAC);
/* 704 */                       case "minecraft:tall_grass": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_TALL_GRASS);
/* 705 */                       case "minecraft:large_fern": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_LARGE_FERN);
/* 706 */                       case "minecraft:rose_bush": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_ROSE_BUSH);
/* 707 */                       case "minecraft:peony": setBlock(pos, ChunkPalettedStorageFix.MappingConstants.UPPER_PEONY); }
/*     */                   
/*     */                   }  }
/*     */               
/*     */             }  }
/*     */         
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private Dynamic<?> getBlockEntity(int pos) {
/* 719 */       return (Dynamic)this.blockEntities.get(pos);
/*     */     }
/*     */     
/*     */     private Dynamic<?> removeBlockEntity(int pos) {
/* 723 */       return (Dynamic)this.blockEntities.remove(pos);
/*     */     }
/*     */     public static int relative(int pos, ChunkPalettedStorageFix.Direction direction) {
/*     */       int x, y, z;
/* 727 */       switch (direction.getAxis().ordinal()) { default: throw new MatchException(null, null);
/*     */         case 0:
/* 729 */           x = (pos & 0xF) + direction.getAxisDirection().getStep();
/* 730 */           if (x < 0 || x > 15);
/*     */         
/*     */         case 1:
/* 733 */           y = (pos >> 8) + direction.getAxisDirection().getStep();
/* 734 */           if (y < 0 || y > 255);
/*     */         
/*     */         case 2:
/* 737 */           z = (pos >> 4 & 0xF) + direction.getAxisDirection().getStep();
/* 738 */           if (z < 0 || z > 15); }  return pos & 0xFFFFFF0F | z << 4;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void setBlock(int pos, Dynamic<?> block) {
/* 744 */       if (pos < 0 || pos > 65535) {
/*     */         return;
/*     */       }
/*     */       
/* 748 */       ChunkPalettedStorageFix.Section section = getSection(pos);
/*     */       
/* 750 */       if (section == null) {
/*     */         return;
/*     */       }
/*     */       
/* 754 */       section.setBlock(pos & 0xFFF, block);
/*     */     }
/*     */     
/*     */     private ChunkPalettedStorageFix.Section getSection(int pos) {
/* 758 */       int sectionY = pos >> 12;
/* 759 */       return (sectionY < this.sections.length) ? this.sections[sectionY] : null;
/*     */     }
/*     */     
/*     */     public Dynamic<?> getBlock(int pos) {
/* 763 */       if (pos < 0 || pos > 65535) {
/* 764 */         return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */       }
/*     */       
/* 767 */       ChunkPalettedStorageFix.Section section = getSection(pos);
/*     */       
/* 769 */       if (section == null) {
/* 770 */         return ChunkPalettedStorageFix.MappingConstants.AIR;
/*     */       }
/*     */       
/* 773 */       return section.getBlock(pos & 0xFFF);
/*     */     }
/*     */     
/*     */     public Dynamic<?> write() {
/* 777 */       Dynamic<?> level = this.level;
/* 778 */       if (this.blockEntities.isEmpty()) {
/* 779 */         level = level.remove("TileEntities");
/*     */       } else {
/* 781 */         level = level.set("TileEntities", level.createList(this.blockEntities.values().stream()));
/*     */       } 
/*     */       
/* 784 */       Dynamic<?> indices = level.emptyMap();
/* 785 */       List<Dynamic<?>> sections = Lists.newArrayList();
/* 786 */       for (ChunkPalettedStorageFix.Section section : this.sections) {
/* 787 */         if (section != null) {
/* 788 */           sections.add(section.write());
/* 789 */           indices = indices.set(String.valueOf(section.y), indices.createIntList(Arrays.stream(section.update.toIntArray())));
/*     */         } 
/*     */       } 
/*     */       
/* 793 */       Dynamic<?> tag = level.emptyMap();
/* 794 */       tag = tag.set("Sides", tag.createByte((byte)this.sides));
/* 795 */       tag = tag.set("Indices", indices);
/* 796 */       return level.set("UpgradeData", tag).set("Sections", tag.createList(sections.stream()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DataLayer
/*     */   {
/*     */     private static final int SIZE = 2048;
/*     */     private static final int NIBBLE_SIZE = 4;
/*     */     private final byte[] data;
/*     */     
/*     */     public DataLayer() {
/* 807 */       this.data = new byte[2048];
/*     */     }
/*     */     
/*     */     public DataLayer(byte[] data) {
/* 811 */       this.data = data;
/*     */       
/* 813 */       if (data.length != 2048) {
/* 814 */         throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + data.length);
/*     */       }
/*     */     }
/*     */     
/*     */     public int get(int x, int y, int z) {
/* 819 */       int position = getPosition(y << 8 | z << 4 | x);
/*     */       
/* 821 */       if (isFirst(y << 8 | z << 4 | x)) {
/* 822 */         return this.data[position] & 0xF;
/*     */       }
/* 824 */       return this.data[position] >> 4 & 0xF;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isFirst(int position) {
/* 829 */       return ((position & 0x1) == 0);
/*     */     }
/*     */     
/*     */     private int getPosition(int position) {
/* 833 */       return position >> 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getSideMask(boolean west, boolean east, boolean north, boolean south) {
/* 838 */     int s = 0;
/* 839 */     if (north) {
/* 840 */       if (east) {
/* 841 */         s |= 0x2;
/* 842 */       } else if (west) {
/* 843 */         s |= 0x80;
/*     */       } else {
/* 845 */         s |= 0x1;
/*     */       } 
/* 847 */     } else if (south) {
/* 848 */       if (west) {
/* 849 */         s |= 0x20;
/* 850 */       } else if (east) {
/* 851 */         s |= 0x8;
/*     */       } else {
/* 853 */         s |= 0x10;
/*     */       } 
/* 855 */     } else if (east) {
/* 856 */       s |= 0x4;
/* 857 */     } else if (west) {
/* 858 */       s |= 0x40;
/*     */     } 
/* 860 */     return s;
/*     */   }
/*     */   
/*     */   public enum Direction {
/* 864 */     DOWN(AxisDirection.NEGATIVE, Axis.Y),
/* 865 */     UP(AxisDirection.POSITIVE, Axis.Y),
/* 866 */     NORTH(AxisDirection.NEGATIVE, Axis.Z),
/* 867 */     SOUTH(AxisDirection.POSITIVE, Axis.Z),
/* 868 */     WEST(AxisDirection.NEGATIVE, Axis.X),
/* 869 */     EAST(AxisDirection.POSITIVE, Axis.X);
/*     */     
/*     */     private final Axis axis;
/*     */     
/*     */     private final AxisDirection axisDirection;
/*     */     
/*     */     Direction(AxisDirection axisDirection, Axis axis) {
/* 876 */       this.axis = axis;
/* 877 */       this.axisDirection = axisDirection;
/*     */     }
/*     */     
/*     */     public AxisDirection getAxisDirection() {
/* 881 */       return this.axisDirection;
/*     */     }
/*     */     
/*     */     public Axis getAxis() {
/* 885 */       return this.axis;
/*     */     }
/*     */     
/*     */     public enum Axis {
/* 889 */       X,
/* 890 */       Y,
/* 891 */       Z;
/*     */     }
/*     */     
/*     */     public enum AxisDirection {
/* 895 */       POSITIVE(1),
/* 896 */       NEGATIVE(-1);
/*     */       
/*     */       private final int step;
/*     */ 
/*     */       
/*     */       AxisDirection(int step) {
/* 902 */         this.step = step;
/*     */       }
/*     */       
/*     */       public int getStep() {
/* 906 */         return this.step; } } } public enum Axis { X, Y, Z; } public enum AxisDirection { POSITIVE(1), NEGATIVE(-1); private final int step; AxisDirection(int step) { this.step = step; } public int getStep() { return this.step; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/ChunkPalettedStorageFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */