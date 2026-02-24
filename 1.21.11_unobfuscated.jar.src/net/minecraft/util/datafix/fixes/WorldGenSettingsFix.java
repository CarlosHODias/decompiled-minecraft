/*     */ package net.minecraft.util.datafix.fixes;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class WorldGenSettingsFix extends com.mojang.datafixers.DataFix {
/*     */   private static final String VILLAGE = "minecraft:village";
/*     */   private static final String DESERT_PYRAMID = "minecraft:desert_pyramid";
/*     */   private static final String IGLOO = "minecraft:igloo";
/*     */   private static final String JUNGLE_TEMPLE = "minecraft:jungle_pyramid";
/*     */   
/*     */   public WorldGenSettingsFix(Schema parent) {
/*  28 */     super(parent, true);
/*     */   }
/*     */   private static final String SWAMP_HUT = "minecraft:swamp_hut"; private static final String PILLAGER_OUTPOST = "minecraft:pillager_outpost"; private static final String END_CITY = "minecraft:endcity"; private static final String WOODLAND_MANSION = "minecraft:mansion"; private static final String OCEAN_MONUMENT = "minecraft:monument";
/*     */   
/*     */   protected com.mojang.datafixers.TypeRewriteRule makeRule() {
/*  33 */     return fixTypeEverywhereTyped("WorldGenSettings building", getInputSchema().getType(References.WORLD_GEN_SETTINGS), settings -> settings.update(DSL.remainderFinder(), WorldGenSettingsFix::fix));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> noise(long seed, DynamicLike<T> input, Dynamic<T> noiseGeneratorSettings, Dynamic<T> biomeSource) {
/*  37 */     return input.createMap((Map)ImmutableMap.of(
/*  38 */           input.createString("type"), input.createString("minecraft:noise"), 
/*  39 */           input.createString("biome_source"), biomeSource, 
/*  40 */           input.createString("seed"), input.createLong(seed), 
/*  41 */           input.createString("settings"), noiseGeneratorSettings));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> input, long seed, boolean legacyBiomeInitLayer, boolean largeBiomes) {
/*  46 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder()
/*  47 */       .put(input.createString("type"), input.createString("minecraft:vanilla_layered"))
/*  48 */       .put(input.createString("seed"), input.createLong(seed))
/*  49 */       .put(input.createString("large_biomes"), input.createBoolean(largeBiomes));
/*     */     
/*  51 */     if (legacyBiomeInitLayer) {
/*  52 */       builder.put(input.createString("legacy_biome_init_layer"), input.createBoolean(legacyBiomeInitLayer));
/*     */     }
/*     */     
/*  55 */     return input.createMap((Map)builder.build());
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
/*  68 */   private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder()
/*  69 */     .put("minecraft:village", new StructureFeatureConfiguration(32, 8, 10387312))
/*  70 */     .put("minecraft:desert_pyramid", new StructureFeatureConfiguration(32, 8, 14357617))
/*  71 */     .put("minecraft:igloo", new StructureFeatureConfiguration(32, 8, 14357618))
/*  72 */     .put("minecraft:jungle_pyramid", new StructureFeatureConfiguration(32, 8, 14357619))
/*  73 */     .put("minecraft:swamp_hut", new StructureFeatureConfiguration(32, 8, 14357620))
/*  74 */     .put("minecraft:pillager_outpost", new StructureFeatureConfiguration(32, 8, 165745296))
/*  75 */     .put("minecraft:monument", new StructureFeatureConfiguration(32, 5, 10387313))
/*  76 */     .put("minecraft:endcity", new StructureFeatureConfiguration(20, 11, 10387313))
/*  77 */     .put("minecraft:mansion", new StructureFeatureConfiguration(80, 20, 10387319))
/*  78 */     .build();
/*     */   private static final class StructureFeatureConfiguration { public static final Codec<StructureFeatureConfiguration> CODEC; private final int spacing; private final int separation; private final int salt;
/*     */     static {
/*  81 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("spacing").forGetter(()), (App)Codec.INT.fieldOf("separation").forGetter(()), (App)Codec.INT.fieldOf("salt").forGetter(())).apply((Applicative)i, StructureFeatureConfiguration::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StructureFeatureConfiguration(int spacing, int separation, int salt) {
/*  92 */       this.spacing = spacing;
/*  93 */       this.separation = separation;
/*  94 */       this.salt = salt;
/*     */     }
/*     */     
/*     */     public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
/*  98 */       return new Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap()));
/*     */     } }
/*     */   
/*     */   private static <T> Dynamic<T> fix(Dynamic<T> input) {
/*     */     Dynamic<T> generator;
/* 103 */     DynamicOps<T> ops = input.getOps();
/*     */     
/* 105 */     long seed = input.get("RandomSeed").asLong(0L);
/*     */     
/* 107 */     Optional<String> name = input.get("generatorName").asString().map(n -> n.toLowerCase(Locale.ROOT)).result();
/*     */     
/* 109 */     Optional<String> legacyCustomOptions = input.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> name.equals(Optional.of("customized")) ? input.get("generatorOptions").asString().result() : Optional.empty());
/*     */ 
/*     */ 
/*     */     
/*     */     boolean caves = false;
/*     */ 
/*     */ 
/*     */     
/* 117 */     if (name.equals(Optional.of("customized"))) {
/* 118 */       generator = defaultOverworld(input, seed);
/* 119 */     } else if (name.isEmpty()) {
/* 120 */       generator = defaultOverworld(input, seed);
/*     */     } else {
/* 122 */       OptionalDynamic<T> flatSettings; Map<Dynamic<T>, Dynamic<T>> structureBuilder; OptionalDynamic<T> settings; OptionalDynamic<?> chunkGeneratorObject; Optional<String> type; Dynamic<T> noiseGeneratorSettings, biomeSource, fixedSource; boolean normal, legacyBiomeInitLayer, isAmplified, largeBiomes; switch ((String)name.get()) {
/*     */         case "flat":
/* 124 */           flatSettings = input.get("generatorOptions");
/* 125 */           structureBuilder = fixFlatStructures(ops, flatSettings);
/*     */           
/* 127 */           generator = input.createMap((Map)ImmutableMap.of(
/* 128 */                 input.createString("type"), input.createString("minecraft:flat"), 
/* 129 */                 input.createString("settings"), input.createMap((Map)ImmutableMap.of(
/* 130 */                     input.createString("structures"), input.createMap(structureBuilder), 
/* 131 */                     input.createString("layers"), flatSettings.get("layers").result().orElseGet(() -> input.createList(Stream.of(new Dynamic[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                             
/*     */                             input.createMap((Map)ImmutableMap.of(input.createString("height"), input.createInt(1), input.createString("block"), input.createString("minecraft:bedrock"))), input.createMap((Map)ImmutableMap.of(input.createString("height"), input.createInt(2), input.createString("block"), input.createString("minecraft:dirt"))), input.createMap((Map)ImmutableMap.of(input.createString("height"), input.createInt(1), input.createString("block"), input.createString("minecraft:grass_block")))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                           
/* 145 */                           }))), input.createString("biome"), input.createString(flatSettings.get("biome").asString("minecraft:plains"))))));
/*     */           break;
/*     */ 
/*     */         
/*     */         case "debug_all_block_states":
/* 150 */           generator = input.createMap((Map)ImmutableMap.of(
/* 151 */                 input.createString("type"), input.createString("minecraft:debug")));
/*     */           break;
/*     */         
/*     */         case "buffet":
/* 155 */           settings = input.get("generatorOptions");
/* 156 */           chunkGeneratorObject = settings.get("chunk_generator");
/*     */           
/* 158 */           type = chunkGeneratorObject.get("type").asString().result();
/*     */ 
/*     */           
/* 161 */           if (java.util.Objects.equals(type, Optional.of("minecraft:caves"))) {
/* 162 */             noiseGeneratorSettings = input.createString("minecraft:caves");
/* 163 */             caves = true;
/* 164 */           } else if (java.util.Objects.equals(type, Optional.of("minecraft:floating_islands"))) {
/* 165 */             noiseGeneratorSettings = input.createString("minecraft:floating_islands");
/*     */           } else {
/* 167 */             noiseGeneratorSettings = input.createString("minecraft:overworld");
/*     */           } 
/*     */           
/* 170 */           biomeSource = settings.get("biome_source").result().orElseGet(() -> input.createMap((Map)ImmutableMap.of(input.createString("type"), input.createString("minecraft:fixed"))));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 175 */           if (biomeSource.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) {
/*     */             
/* 177 */             String biome = biomeSource.get("options")
/* 178 */               .get("biomes")
/* 179 */               .asStream()
/* 180 */               .findFirst()
/* 181 */               .flatMap(b -> b.asString().result())
/* 182 */               .orElse("minecraft:ocean");
/*     */             
/* 184 */             fixedSource = biomeSource.remove("options").set("biome", input.createString(biome));
/*     */           } else {
/* 186 */             fixedSource = biomeSource;
/*     */           } 
/*     */           
/* 189 */           generator = noise(seed, (DynamicLike<T>)input, noiseGeneratorSettings, fixedSource);
/*     */           break;
/*     */         default:
/* 192 */           normal = ((String)name.get()).equals("default");
/* 193 */           legacyBiomeInitLayer = (((String)name.get()).equals("default_1_1") || (normal && input.get("generatorVersion").asInt(0) == 0));
/* 194 */           isAmplified = ((String)name.get()).equals("amplified");
/* 195 */           largeBiomes = ((String)name.get()).equals("largebiomes");
/* 196 */           generator = noise(seed, (DynamicLike<T>)input, input.createString(isAmplified ? "minecraft:amplified" : "minecraft:overworld"), vanillaBiomeSource(input, seed, legacyBiomeInitLayer, largeBiomes));
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 201 */     boolean generateMapFeatures = input.get("MapFeatures").asBoolean(true);
/* 202 */     boolean generateBonusChest = input.get("BonusChest").asBoolean(false);
/*     */     
/* 204 */     ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
/* 205 */     builder.put(ops.createString("seed"), ops.createLong(seed));
/* 206 */     builder.put(ops.createString("generate_features"), ops.createBoolean(generateMapFeatures));
/* 207 */     builder.put(ops.createString("bonus_chest"), ops.createBoolean(generateBonusChest));
/* 208 */     builder.put(ops.createString("dimensions"), vanillaLevels(input, seed, generator, caves));
/* 209 */     legacyCustomOptions.ifPresent(o -> builder.put(ops.createString("legacy_custom_options"), ops.createString(o)));
/*     */     
/* 211 */     return new Dynamic(ops, ops.createMap((Map)builder.build()));
/*     */   }
/*     */   
/*     */   protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> input, long seed) {
/* 215 */     return noise(seed, (DynamicLike<T>)input, input.createString("minecraft:overworld"), vanillaBiomeSource(input, seed, false, false));
/*     */   }
/*     */   
/*     */   protected static <T> T vanillaLevels(Dynamic<T> input, long seed, Dynamic<T> overworldGenerator, boolean caves) {
/* 219 */     DynamicOps<T> ops = input.getOps();
/* 220 */     return (T)ops.createMap((Map)ImmutableMap.of(
/* 221 */           ops.createString("minecraft:overworld"), ops.createMap((Map)ImmutableMap.of(
/* 222 */               ops.createString("type"), ops.createString("minecraft:overworld" + (caves ? "_caves" : "")), 
/* 223 */               ops.createString("generator"), overworldGenerator.getValue())), 
/*     */           
/* 225 */           ops.createString("minecraft:the_nether"), ops.createMap((Map)ImmutableMap.of(
/* 226 */               ops.createString("type"), ops.createString("minecraft:the_nether"), 
/* 227 */               ops.createString("generator"), noise(seed, (DynamicLike<T>)input, input.createString("minecraft:nether"), input.createMap((Map)ImmutableMap.of(
/* 228 */                     input.createString("type"), input.createString("minecraft:multi_noise"), 
/* 229 */                     input.createString("seed"), input.createLong(seed), 
/* 230 */                     input.createString("preset"), input.createString("minecraft:nether"))))
/* 231 */               .getValue())), 
/*     */           
/* 233 */           ops.createString("minecraft:the_end"), ops.createMap((Map)ImmutableMap.of(
/* 234 */               ops.createString("type"), ops.createString("minecraft:the_end"), 
/* 235 */               ops.createString("generator"), noise(seed, (DynamicLike<T>)input, input.createString("minecraft:end"), input.createMap((Map)ImmutableMap.of(
/* 236 */                     input.createString("type"), input.createString("minecraft:the_end"), 
/* 237 */                     input.createString("seed"), input.createLong(seed))))
/* 238 */               .getValue()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> ops, OptionalDynamic<T> settings) {
/* 244 */     MutableInt strongholdDistance = new MutableInt(32);
/* 245 */     MutableInt strongholdSpread = new MutableInt(3);
/* 246 */     MutableInt strongholdCount = new MutableInt(128);
/* 247 */     MutableBoolean hasStronghold = new MutableBoolean(false);
/* 248 */     Map<String, StructureFeatureConfiguration> structureConfig = com.google.common.collect.Maps.newHashMap();
/*     */     
/* 250 */     if (settings.result().isEmpty()) {
/* 251 */       hasStronghold.setTrue();
/* 252 */       structureConfig.put("minecraft:village", (StructureFeatureConfiguration)DEFAULTS.get("minecraft:village"));
/*     */     } 
/*     */     
/* 255 */     settings.get("structures").flatMap(Dynamic::getMapValues).ifSuccess(map -> map.forEach(()));
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
/*     */ 
/*     */ 
/*     */     
/* 318 */     ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> structureBuilder = ImmutableMap.builder();
/* 319 */     structureBuilder.put(settings.createString("structures"), settings.createMap((Map)structureConfig.entrySet().stream().collect(java.util.stream.Collectors.toMap(e -> settings.createString((String)e.getKey()), e -> ((StructureFeatureConfiguration)e.getValue()).serialize(ops)))));
/*     */ 
/*     */ 
/*     */     
/* 323 */     if (hasStronghold.isTrue()) {
/* 324 */       structureBuilder.put(settings.createString("stronghold"), settings.createMap((Map)ImmutableMap.of(
/* 325 */               settings.createString("distance"), settings.createInt(strongholdDistance.intValue()), 
/* 326 */               settings.createString("spread"), settings.createInt(strongholdSpread.intValue()), 
/* 327 */               settings.createString("count"), settings.createInt(strongholdCount.intValue()))));
/*     */     }
/*     */     
/* 330 */     return (Map<Dynamic<T>, Dynamic<T>>)structureBuilder.build();
/*     */   }
/*     */   
/*     */   private static int getInt(String input, int def) {
/* 334 */     return org.apache.commons.lang3.math.NumberUtils.toInt(input, def);
/*     */   }
/*     */   
/*     */   private static int getInt(String input, int def, int min) {
/* 338 */     return Math.max(min, getInt(input, def));
/*     */   }
/*     */   
/*     */   private static void setSpacing(Map<String, StructureFeatureConfiguration> structureConfig, String structure, String optionValue, int min) {
/* 342 */     StructureFeatureConfiguration config = structureConfig.getOrDefault(structure, (StructureFeatureConfiguration)DEFAULTS.get(structure));
/* 343 */     int spacing = getInt(optionValue, config.spacing, min);
/* 344 */     structureConfig.put(structure, new StructureFeatureConfiguration(spacing, config.separation, config.salt));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/WorldGenSettingsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */