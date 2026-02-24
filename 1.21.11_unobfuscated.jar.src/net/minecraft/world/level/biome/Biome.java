/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.level.GrassColor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public final class Biome {
/*     */   public static final Codec<Biome> DIRECT_CODEC;
/*     */   public static final Codec<Biome> NETWORK_CODEC;
/*     */   
/*     */   static {
/*  38 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClimateSettings.CODEC.forGetter(()), (App)EnvironmentAttributeMap.CODEC_ONLY_POSITIONAL.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(()), (App)BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(()), (App)BiomeGenerationSettings.CODEC.forGetter(()), (App)MobSpawnSettings.CODEC.forGetter(())).apply((Applicative)i, Biome::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ClimateSettings.CODEC.forGetter(()), (App)EnvironmentAttributeMap.NETWORK_CODEC.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(()), (App)BiomeSpecialEffects.CODEC.fieldOf("effects").forGetter(())).apply((Applicative)i, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public static final Codec<Holder<Biome>> CODEC = (Codec<Holder<Biome>>)RegistryFileCodec.create(Registries.BIOME, DIRECT_CODEC);
/*  53 */   public static final Codec<HolderSet<Biome>> LIST_CODEC = net.minecraft.core.RegistryCodecs.homogeneousList(Registries.BIOME, DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final PerlinSimplexNoise TEMPERATURE_NOISE = new PerlinSimplexNoise((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(1234L)), (List)ImmutableList.of(0));
/*  60 */   private static final PerlinSimplexNoise FROZEN_TEMPERATURE_NOISE = new PerlinSimplexNoise((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(3456L)), (List)ImmutableList.of(-2, -1, 0));
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  63 */   public static final PerlinSimplexNoise BIOME_INFO_NOISE = new PerlinSimplexNoise((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(2345L)), (List)ImmutableList.of(0)); private static final int TEMPERATURE_CACHE_SIZE = 1024; private final ClimateSettings climateSettings;
/*     */   private final BiomeGenerationSettings generationSettings;
/*     */   private final MobSpawnSettings mobSettings;
/*     */   private final EnvironmentAttributeMap attributes;
/*     */   private final BiomeSpecialEffects specialEffects;
/*     */   private final ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache;
/*     */   
/*  70 */   public enum Precipitation implements StringRepresentable { NONE("none"),
/*  71 */     RAIN("rain"),
/*  72 */     SNOW("snow");
/*     */ 
/*     */     
/*  75 */     public static final Codec<Precipitation> CODEC = (Codec<Precipitation>)StringRepresentable.fromEnum(Precipitation::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Precipitation(String name) {
/*  80 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  85 */       return this.name;
/*     */     } }
/*     */ 
/*     */   
/*     */   public enum TemperatureModifier implements StringRepresentable {
/*  90 */     NONE("none")
/*     */     {
/*     */       public float modifyTemperature(BlockPos pos, float baseTemperature) {
/*  93 */         return baseTemperature;
/*     */       }
/*     */     },
/*  96 */     FROZEN("frozen")
/*     */     {
/*     */       public float modifyTemperature(BlockPos pos, float baseTemperature) {
/*  99 */         double groundValueLargeVariation = Biome.FROZEN_TEMPERATURE_NOISE.getValue(pos.getX() * 0.05D, pos.getZ() * 0.05D, false) * 7.0D;
/* 100 */         double groundValueEdgeVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.2D, pos.getZ() * 0.2D, false);
/* 101 */         double icePatches = groundValueLargeVariation + groundValueEdgeVariation;
/* 102 */         if (icePatches < 0.3D) {
/* 103 */           double groundValueSmallVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.09D, pos.getZ() * 0.09D, false);
/* 104 */           if (groundValueSmallVariation < 0.8D) {
/* 105 */             return 0.2F;
/*     */           }
/*     */         } 
/*     */         
/* 109 */         return baseTemperature;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TemperatureModifier(String name) {
/* 118 */       this.name = name;
/*     */     }
/*     */     
/* 121 */     public static final Codec<TemperatureModifier> CODEC = (Codec<TemperatureModifier>)StringRepresentable.fromEnum(TemperatureModifier::values); private final String name;
/*     */     
/*     */     public String getName() {
/* 124 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getSerializedName()
/*     */     {
/* 129 */       return this.name;
/*     */     }
/*     */     public abstract float modifyTemperature(BlockPos param1BlockPos, float param1Float);
/*     */   }
/*     */   enum null { public float modifyTemperature(BlockPos pos, float baseTemperature) {
/*     */       return baseTemperature;
/*     */     } }
/* 136 */   private Biome(ClimateSettings climateSettings, EnvironmentAttributeMap attributes, BiomeSpecialEffects specialEffects, BiomeGenerationSettings generationSettings, MobSpawnSettings mobSettings) { this.temperatureCache = ThreadLocal.withInitial(() -> {
/*     */           Long2FloatLinkedOpenHashMap map = new Long2FloatLinkedOpenHashMap(1024, 0.25F)
/*     */             {
/*     */               protected void rehash(int newN) {}
/*     */             };
/*     */ 
/*     */ 
/*     */           
/*     */           map.defaultReturnValue(Float.NaN);
/*     */ 
/*     */ 
/*     */           
/*     */           return map;
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 153 */     this.climateSettings = climateSettings;
/* 154 */     this.generationSettings = generationSettings;
/* 155 */     this.mobSettings = mobSettings;
/*     */     
/* 157 */     this.attributes = attributes;
/* 158 */     this.specialEffects = specialEffects; } enum null {
/*     */     public float modifyTemperature(BlockPos pos, float baseTemperature) { double groundValueLargeVariation = Biome.FROZEN_TEMPERATURE_NOISE.getValue(pos.getX() * 0.05D, pos.getZ() * 0.05D, false) * 7.0D; double groundValueEdgeVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.2D, pos.getZ() * 0.2D, false); double icePatches = groundValueLargeVariation + groundValueEdgeVariation; if (icePatches < 0.3D) { double groundValueSmallVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.09D, pos.getZ() * 0.09D, false); if (groundValueSmallVariation < 0.8D)
/*     */           return 0.2F;  }
/*     */        return baseTemperature; } }
/* 162 */   public MobSpawnSettings getMobSettings() { return this.mobSettings; }
/*     */ 
/*     */   
/*     */   public boolean hasPrecipitation() {
/* 166 */     return this.climateSettings.hasPrecipitation();
/*     */   }
/*     */   
/*     */   public Precipitation getPrecipitationAt(BlockPos pos, int seaLevel) {
/* 170 */     if (!hasPrecipitation()) {
/* 171 */       return Precipitation.NONE;
/*     */     }
/* 173 */     return coldEnoughToSnow(pos, seaLevel) ? Precipitation.SNOW : Precipitation.RAIN;
/*     */   }
/*     */   
/*     */   private float getHeightAdjustedTemperature(BlockPos pos, int seaLevel) {
/* 177 */     float adjustedTemperature = this.climateSettings.temperatureModifier.modifyTemperature(pos, getBaseTemperature());
/* 178 */     int snowLevel = seaLevel + 17;
/*     */     
/* 180 */     if (pos.getY() > snowLevel) {
/*     */       
/* 182 */       float v = (float)(TEMPERATURE_NOISE.getValue((pos.getX() / 8.0F), (pos.getZ() / 8.0F), false) * 8.0D);
/* 183 */       return adjustedTemperature - (v + pos.getY() - snowLevel) * 0.05F / 40.0F;
/*     */     } 
/* 185 */     return adjustedTemperature;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   private float getTemperature(BlockPos pos, int seaLevel) {
/* 191 */     long key = pos.asLong();
/* 192 */     Long2FloatLinkedOpenHashMap cache = this.temperatureCache.get();
/* 193 */     float cached = cache.get(key);
/* 194 */     if (!Float.isNaN(cached)) {
/* 195 */       return cached;
/*     */     }
/* 197 */     float temp = getHeightAdjustedTemperature(pos, seaLevel);
/* 198 */     if (cache.size() == 1024) {
/* 199 */       cache.removeFirstFloat();
/*     */     }
/* 201 */     cache.put(key, temp);
/* 202 */     return temp;
/*     */   }
/*     */   
/*     */   public boolean shouldFreeze(LevelReader level, BlockPos pos) {
/* 206 */     return shouldFreeze(level, pos, true);
/*     */   }
/*     */   
/*     */   public boolean shouldFreeze(LevelReader level, BlockPos pos, boolean checkNeighbors) {
/* 210 */     if (warmEnoughToRain(pos, level.getSeaLevel())) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
/* 215 */       BlockState blockState = level.getBlockState(pos);
/* 216 */       FluidState fluidState = level.getFluidState(pos);
/* 217 */       if (fluidState.getType() == net.minecraft.world.level.material.Fluids.WATER && blockState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
/* 218 */         if (!checkNeighbors) {
/* 219 */           return true;
/*     */         }
/*     */         
/* 222 */         boolean surroundedByWater = (level.isWaterAt(pos.west()) && level.isWaterAt(pos.east()) && level.isWaterAt(pos.north()) && level.isWaterAt(pos.south()));
/* 223 */         if (!surroundedByWater) {
/* 224 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   public boolean coldEnoughToSnow(BlockPos pos, int seaLevel) {
/* 232 */     return !warmEnoughToRain(pos, seaLevel);
/*     */   }
/*     */   
/*     */   public boolean warmEnoughToRain(BlockPos pos, int seaLevel) {
/* 236 */     return (getTemperature(pos, seaLevel) >= 0.15F);
/*     */   }
/*     */   
/*     */   public boolean shouldMeltFrozenOceanIcebergSlightly(BlockPos pos, int seaLevel) {
/* 240 */     return (getTemperature(pos, seaLevel) > 0.1F);
/*     */   }
/*     */   
/*     */   public boolean shouldSnow(LevelReader level, BlockPos pos) {
/* 244 */     if (getPrecipitationAt(pos, level.getSeaLevel()) != Precipitation.SNOW) {
/* 245 */       return false;
/*     */     }
/*     */     
/* 248 */     if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
/* 249 */       BlockState state = level.getBlockState(pos);
/*     */ 
/*     */       
/* 252 */       if ((state.isAir() || state.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(level, pos)) {
/* 253 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiomeGenerationSettings getGenerationSettings() {
/* 264 */     return this.generationSettings;
/*     */   }
/*     */   
/*     */   public int getGrassColor(double x, double z) {
/* 268 */     int baseGrassColor = getBaseGrassColor();
/* 269 */     return this.specialEffects.grassColorModifier().modifyColor(x, z, baseGrassColor);
/*     */   }
/*     */   
/*     */   private int getBaseGrassColor() {
/* 273 */     Optional<Integer> colorOverride = this.specialEffects.grassColorOverride();
/*     */     
/* 275 */     if (colorOverride.isPresent()) {
/* 276 */       return (Integer)colorOverride.get();
/*     */     }
/* 278 */     return getGrassColorFromTexture();
/*     */   }
/*     */   
/*     */   private int getGrassColorFromTexture() {
/* 282 */     double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
/* 283 */     double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
/*     */     
/* 285 */     return GrassColor.get(temp, rain);
/*     */   }
/*     */   
/*     */   public int getFoliageColor() {
/* 289 */     return (Integer)this.specialEffects.foliageColorOverride().orElseGet(this::getFoliageColorFromTexture);
/*     */   }
/*     */   
/*     */   private int getFoliageColorFromTexture() {
/* 293 */     double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
/* 294 */     double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
/* 295 */     return net.minecraft.world.level.FoliageColor.get(temp, rain);
/*     */   }
/*     */   
/*     */   public int getDryFoliageColor() {
/* 299 */     return (Integer)this.specialEffects.dryFoliageColorOverride().orElseGet(this::getDryFoliageColorFromTexture);
/*     */   }
/*     */   
/*     */   private int getDryFoliageColorFromTexture() {
/* 303 */     double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
/* 304 */     double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
/* 305 */     return net.minecraft.world.level.DryFoliageColor.get(temp, rain);
/*     */   }
/*     */   
/*     */   public float getBaseTemperature() {
/* 309 */     return this.climateSettings.temperature;
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeMap getAttributes() {
/* 313 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public BiomeSpecialEffects getSpecialEffects() {
/* 317 */     return this.specialEffects;
/*     */   }
/*     */   
/*     */   public int getWaterColor() {
/* 321 */     return this.specialEffects.waterColor();
/*     */   }
/*     */   public static class BiomeBuilder { private boolean hasPrecipitation; private Float temperature; private Biome.TemperatureModifier temperatureModifier; private Float downfall; private final EnvironmentAttributeMap.Builder attributes; private BiomeSpecialEffects specialEffects; private MobSpawnSettings mobSpawnSettings; private BiomeGenerationSettings generationSettings;
/*     */     public BiomeBuilder() {
/* 325 */       this.hasPrecipitation = true;
/*     */       
/* 327 */       this.temperatureModifier = Biome.TemperatureModifier.NONE;
/*     */       
/* 329 */       this.attributes = EnvironmentAttributeMap.builder();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BiomeBuilder hasPrecipitation(boolean hasPrecipitation) {
/* 335 */       this.hasPrecipitation = hasPrecipitation;
/* 336 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder temperature(float temperature) {
/* 340 */       this.temperature = temperature;
/* 341 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder downfall(float downfall) {
/* 345 */       this.downfall = downfall;
/* 346 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder putAttributes(EnvironmentAttributeMap attributes) {
/* 350 */       this.attributes.putAll(attributes);
/* 351 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder putAttributes(EnvironmentAttributeMap.Builder attributes) {
/* 355 */       return putAttributes(attributes.build());
/*     */     }
/*     */     
/*     */     public <Value> BiomeBuilder setAttribute(EnvironmentAttribute<Value> attribute, Value value) {
/* 359 */       this.attributes.set(attribute, value);
/* 360 */       return this;
/*     */     }
/*     */     
/*     */     public <Value, Parameter> BiomeBuilder modifyAttribute(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Parameter> modifier, Parameter value) {
/* 364 */       this.attributes.modify(attribute, modifier, value);
/* 365 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder specialEffects(BiomeSpecialEffects specialEffects) {
/* 369 */       this.specialEffects = specialEffects;
/* 370 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder mobSpawnSettings(MobSpawnSettings mobSpawnSettings) {
/* 374 */       this.mobSpawnSettings = mobSpawnSettings;
/* 375 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder generationSettings(BiomeGenerationSettings generationSettings) {
/* 379 */       this.generationSettings = generationSettings;
/* 380 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder temperatureAdjustment(Biome.TemperatureModifier temperatureModifier) {
/* 384 */       this.temperatureModifier = temperatureModifier;
/* 385 */       return this;
/*     */     }
/*     */     
/*     */     public Biome build() {
/* 389 */       if (this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 395 */         throw new IllegalStateException("You are missing parameters to build a proper biome\n" + String.valueOf(this));
/*     */       }
/*     */       
/* 398 */       return new Biome(new Biome.ClimateSettings(this.hasPrecipitation, this.temperature, 
/* 399 */             this.temperatureModifier, this.downfall), 
/* 400 */           this.attributes.build(), this.specialEffects, this.generationSettings, this.mobSpawnSettings);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 409 */       return "BiomeBuilder{\nhasPrecipitation=" + this.hasPrecipitation + ",\ntemperature=" + this.temperature + ",\ntemperatureModifier=" + String.valueOf(this.temperatureModifier) + ",\ndownfall=" + this.downfall + ",\nspecialEffects=" + String.valueOf(this.specialEffects) + ",\nmobSpawnSettings=" + String.valueOf(this.mobSpawnSettings) + ",\ngenerationSettings=" + String.valueOf(this.generationSettings) + ",\n}";
/*     */     } }
/*     */   
/*     */   private static final class ClimateSettings extends Record { private final boolean hasPrecipitation; private final float temperature; private final Biome.TemperatureModifier temperatureModifier; private final float downfall; public static final com.mojang.serialization.MapCodec<ClimateSettings> CODEC;
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/*     */     }
/*     */     
/* 421 */     private ClimateSettings(boolean hasPrecipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) { this.hasPrecipitation = hasPrecipitation; this.temperature = temperature; this.temperatureModifier = temperatureModifier; this.downfall = downfall; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/* 421 */       //   0	8	1	o	Ljava/lang/Object; } public boolean hasPrecipitation() { return this.hasPrecipitation; } public float temperature() { return this.temperature; } public Biome.TemperatureModifier temperatureModifier() { return this.temperatureModifier; } public float downfall() { return this.downfall; } static {
/* 422 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("has_precipitation").forGetter(()), (App)Codec.FLOAT.fieldOf("temperature").forGetter(()), (App)Biome.TemperatureModifier.CODEC.optionalFieldOf("temperature_modifier", Biome.TemperatureModifier.NONE).forGetter(()), (App)Codec.FLOAT.fieldOf("downfall").forGetter(())).apply((Applicative)i, ClimateSettings::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/Biome.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */