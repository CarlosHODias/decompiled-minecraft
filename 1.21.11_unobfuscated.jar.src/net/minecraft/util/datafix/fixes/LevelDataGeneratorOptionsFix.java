/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LevelDataGeneratorOptionsFix extends DataFix {
/*     */   static {
/*  29 */     MAP = (Map<String, String>)Util.make(Maps.newHashMap(), map -> {
/*     */           map.put("0", "minecraft:ocean");
/*     */           map.put("1", "minecraft:plains");
/*     */           map.put("2", "minecraft:desert");
/*     */           map.put("3", "minecraft:mountains");
/*     */           map.put("4", "minecraft:forest");
/*     */           map.put("5", "minecraft:taiga");
/*     */           map.put("6", "minecraft:swamp");
/*     */           map.put("7", "minecraft:river");
/*     */           map.put("8", "minecraft:nether");
/*     */           map.put("9", "minecraft:the_end");
/*     */           map.put("10", "minecraft:frozen_ocean");
/*     */           map.put("11", "minecraft:frozen_river");
/*     */           map.put("12", "minecraft:snowy_tundra");
/*     */           map.put("13", "minecraft:snowy_mountains");
/*     */           map.put("14", "minecraft:mushroom_fields");
/*     */           map.put("15", "minecraft:mushroom_field_shore");
/*     */           map.put("16", "minecraft:beach");
/*     */           map.put("17", "minecraft:desert_hills");
/*     */           map.put("18", "minecraft:wooded_hills");
/*     */           map.put("19", "minecraft:taiga_hills");
/*     */           map.put("20", "minecraft:mountain_edge");
/*     */           map.put("21", "minecraft:jungle");
/*     */           map.put("22", "minecraft:jungle_hills");
/*     */           map.put("23", "minecraft:jungle_edge");
/*     */           map.put("24", "minecraft:deep_ocean");
/*     */           map.put("25", "minecraft:stone_shore");
/*     */           map.put("26", "minecraft:snowy_beach");
/*     */           map.put("27", "minecraft:birch_forest");
/*     */           map.put("28", "minecraft:birch_forest_hills");
/*     */           map.put("29", "minecraft:dark_forest");
/*     */           map.put("30", "minecraft:snowy_taiga");
/*     */           map.put("31", "minecraft:snowy_taiga_hills");
/*     */           map.put("32", "minecraft:giant_tree_taiga");
/*     */           map.put("33", "minecraft:giant_tree_taiga_hills");
/*     */           map.put("34", "minecraft:wooded_mountains");
/*     */           map.put("35", "minecraft:savanna");
/*     */           map.put("36", "minecraft:savanna_plateau");
/*     */           map.put("37", "minecraft:badlands");
/*     */           map.put("38", "minecraft:wooded_badlands_plateau");
/*     */           map.put("39", "minecraft:badlands_plateau");
/*     */           map.put("40", "minecraft:small_end_islands");
/*     */           map.put("41", "minecraft:end_midlands");
/*     */           map.put("42", "minecraft:end_highlands");
/*     */           map.put("43", "minecraft:end_barrens");
/*     */           map.put("44", "minecraft:warm_ocean");
/*     */           map.put("45", "minecraft:lukewarm_ocean");
/*     */           map.put("46", "minecraft:cold_ocean");
/*     */           map.put("47", "minecraft:deep_warm_ocean");
/*     */           map.put("48", "minecraft:deep_lukewarm_ocean");
/*     */           map.put("49", "minecraft:deep_cold_ocean");
/*     */           map.put("50", "minecraft:deep_frozen_ocean");
/*     */           map.put("127", "minecraft:the_void");
/*     */           map.put("129", "minecraft:sunflower_plains");
/*     */           map.put("130", "minecraft:desert_lakes");
/*     */           map.put("131", "minecraft:gravelly_mountains");
/*     */           map.put("132", "minecraft:flower_forest");
/*     */           map.put("133", "minecraft:taiga_mountains");
/*     */           map.put("134", "minecraft:swamp_hills");
/*     */           map.put("140", "minecraft:ice_spikes");
/*     */           map.put("149", "minecraft:modified_jungle");
/*     */           map.put("151", "minecraft:modified_jungle_edge");
/*     */           map.put("155", "minecraft:tall_birch_forest");
/*     */           map.put("156", "minecraft:tall_birch_hills");
/*     */           map.put("157", "minecraft:dark_forest_hills");
/*     */           map.put("158", "minecraft:snowy_taiga_mountains");
/*     */           map.put("160", "minecraft:giant_spruce_taiga");
/*     */           map.put("161", "minecraft:giant_spruce_taiga_hills");
/*     */           map.put("162", "minecraft:modified_gravelly_mountains");
/*     */           map.put("163", "minecraft:shattered_savanna");
/*     */           map.put("164", "minecraft:shattered_savanna_plateau");
/*     */           map.put("165", "minecraft:eroded_badlands");
/*     */           map.put("166", "minecraft:modified_wooded_badlands_plateau");
/*     */           map.put("167", "minecraft:modified_badlands_plateau");
/*     */         });
/*     */   }
/*     */   
/*     */   static final Map<String, String> MAP;
/*     */   public static final String GENERATOR_OPTIONS = "generatorOptions";
/*     */   
/*     */   public LevelDataGeneratorOptionsFix(Schema outputSchema, boolean changesType) {
/* 110 */     super(outputSchema, changesType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 115 */     Type<?> resultType = getOutputSchema().getType(References.LEVEL);
/* 116 */     return fixTypeEverywhereTyped("LevelDataGeneratorOptionsFix", getInputSchema().getType(References.LEVEL), resultType, input -> Util.writeAndReadTypedOrThrow(input, resultType, ()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> convert(String flatOptionString, DynamicOps<T> ops) {
/*     */     List<Pair<Integer, String>> layerList;
/* 134 */     Iterator<String> parts = Splitter.on(';').split(flatOptionString).iterator();
/*     */ 
/*     */     
/* 137 */     String biome = "minecraft:plains";
/* 138 */     Map<String, Map<String, String>> structuresOptions = Maps.newHashMap();
/*     */     
/* 140 */     if (!flatOptionString.isEmpty() && parts.hasNext()) {
/* 141 */       layerList = getLayersInfoFromString(parts.next());
/*     */       
/* 143 */       if (!layerList.isEmpty()) {
/* 144 */         if (parts.hasNext()) {
/* 145 */           biome = MAP.getOrDefault(parts.next(), "minecraft:plains");
/*     */         }
/*     */         
/* 148 */         if (parts.hasNext()) {
/* 149 */           String[] structures1 = ((String)parts.next()).toLowerCase(Locale.ROOT).split(",");
/*     */           
/* 151 */           for (String structure : structures1) {
/* 152 */             String[] separated = structure.split("\\(", 2);
/*     */             
/* 154 */             if (!separated[0].isEmpty()) {
/* 155 */               structuresOptions.put(separated[0], Maps.newHashMap());
/*     */               
/* 157 */               if (separated.length > 1 && separated[1].endsWith(")") && separated[1].length() > 1) {
/* 158 */                 String[] options = separated[1].substring(0, separated[1].length() - 1).split(" ");
/*     */                 
/* 160 */                 for (String part : options) {
/* 161 */                   String[] split = part.split("=", 2);
/* 162 */                   if (split.length == 2) {
/* 163 */                     ((Map<String, String>)structuresOptions.get(separated[0])).put(split[0], split[1]);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } else {
/* 170 */           structuresOptions.put("village", Maps.newHashMap());
/*     */         } 
/*     */       } 
/*     */     } else {
/* 174 */       layerList = Lists.newArrayList();
/* 175 */       layerList.add(Pair.of(1, "minecraft:bedrock"));
/* 176 */       layerList.add(Pair.of(2, "minecraft:dirt"));
/* 177 */       layerList.add(Pair.of(1, "minecraft:grass_block"));
/* 178 */       structuresOptions.put("village", Maps.newHashMap());
/*     */     } 
/*     */     
/* 181 */     T layers = (T)ops.createList(layerList.stream().map(layer -> ops.createMap((Map)ImmutableMap.of(ops.createString("height"), ops.createInt((Integer)layer.getFirst()), ops.createString("block"), ops.createString((String)layer.getSecond())))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     T structures = (T)ops.createMap((Map)structuresOptions.entrySet().stream().map(entry -> Pair.of(ops.createString(((String)entry.getKey()).toLowerCase(Locale.ROOT)), ops.createMap((Map)((Map)entry.getValue()).entrySet().stream().map(()).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 193 */         .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
/*     */     
/* 195 */     return new Dynamic(ops, ops.createMap((Map)ImmutableMap.of(
/* 196 */             ops.createString("layers"), layers, 
/* 197 */             ops.createString("biome"), ops.createString(biome), 
/* 198 */             ops.createString("structures"), structures)));
/*     */   }
/*     */   
/*     */   private static Pair<Integer, String> getLayerInfoFromString(String input) {
/*     */     int height;
/* 203 */     String[] parts = input.split("\\*", 2);
/*     */ 
/*     */     
/* 206 */     if (parts.length == 2) {
/*     */       try {
/* 208 */         height = Integer.parseInt(parts[0]);
/* 209 */       } catch (NumberFormatException ignored) {
/* 210 */         return null;
/*     */       } 
/*     */     } else {
/* 213 */       height = 1;
/*     */     } 
/*     */     
/* 216 */     String block = parts[parts.length - 1];
/* 217 */     return Pair.of(height, block);
/*     */   }
/*     */   
/*     */   private static List<Pair<Integer, String>> getLayersInfoFromString(String input) {
/* 221 */     List<Pair<Integer, String>> result = Lists.newArrayList();
/* 222 */     String[] depths = input.split(",");
/*     */     
/* 224 */     for (String depth : depths) {
/* 225 */       Pair<Integer, String> layer = getLayerInfoFromString(depth);
/* 226 */       if (layer == null) {
/* 227 */         return Collections.emptyList();
/*     */       }
/* 229 */       result.add(layer);
/*     */     } 
/*     */     
/* 232 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/LevelDataGeneratorOptionsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */