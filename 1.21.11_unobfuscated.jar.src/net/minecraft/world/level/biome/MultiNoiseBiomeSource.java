/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ 
/*     */ public class MultiNoiseBiomeSource extends BiomeSource {
/*  20 */   private static final MapCodec<Holder<Biome>> ENTRY_CODEC = Biome.CODEC.fieldOf("biome");
/*     */ 
/*     */   
/*  23 */   public static final MapCodec<Climate.ParameterList<Holder<Biome>>> DIRECT_CODEC = Climate.ParameterList.<T>codec((MapCodec)ENTRY_CODEC).fieldOf("biomes");
/*     */   
/*  25 */   private static final MapCodec<Holder<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());
/*     */   public static final MapCodec<MultiNoiseBiomeSource> CODEC;
/*     */   private final Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters;
/*     */   
/*     */   static {
/*  30 */     CODEC = Codec.mapEither(DIRECT_CODEC, PRESET_CODEC).xmap(MultiNoiseBiomeSource::new, o -> o.parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MultiNoiseBiomeSource(Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters) {
/*  38 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public static MultiNoiseBiomeSource createFromList(Climate.ParameterList<Holder<Biome>> parameters) {
/*  42 */     return new MultiNoiseBiomeSource(Either.left(parameters));
/*     */   }
/*     */   
/*     */   public static MultiNoiseBiomeSource createFromPreset(Holder<MultiNoiseBiomeSourceParameterList> preset) {
/*  46 */     return new MultiNoiseBiomeSource(Either.right(preset));
/*     */   }
/*     */   
/*     */   private Climate.ParameterList<Holder<Biome>> parameters() {
/*  50 */     return (Climate.ParameterList<Holder<Biome>>)this.parameters.map(direct -> direct, preset -> ((MultiNoiseBiomeSourceParameterList)preset.value()).parameters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/*  58 */     return parameters().values().stream().map(Pair::getSecond);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends BiomeSource> codec() {
/*  63 */     return (MapCodec)CODEC;
/*     */   }
/*     */   
/*     */   public boolean stable(ResourceKey<MultiNoiseBiomeSourceParameterList> expected) {
/*  67 */     Optional<Holder<MultiNoiseBiomeSourceParameterList>> preset = this.parameters.right();
/*  68 */     return (preset.isPresent() && ((Holder)preset.get()).is(expected));
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
/*  73 */     return getNoiseBiome(sampler.sample(quartX, quartY, quartZ));
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Holder<Biome> getNoiseBiome(Climate.TargetPoint target) {
/*  78 */     return parameters().findValue(target);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDebugInfo(List<String> result, BlockPos feetPos, Climate.Sampler sampler) {
/*  83 */     int quartX = QuartPos.fromBlock(feetPos.getX());
/*  84 */     int quartY = QuartPos.fromBlock(feetPos.getY());
/*  85 */     int quartZ = QuartPos.fromBlock(feetPos.getZ());
/*  86 */     Climate.TargetPoint sampleQuantized = sampler.sample(quartX, quartY, quartZ);
/*     */     
/*  88 */     float continentalness = Climate.unquantizeCoord(sampleQuantized.continentalness());
/*  89 */     float erosion = Climate.unquantizeCoord(sampleQuantized.erosion());
/*  90 */     float temperature = Climate.unquantizeCoord(sampleQuantized.temperature());
/*  91 */     float humidity = Climate.unquantizeCoord(sampleQuantized.humidity());
/*  92 */     float weirdness = Climate.unquantizeCoord(sampleQuantized.weirdness());
/*     */     
/*  94 */     double peaksAndValleys = NoiseRouterData.peaksAndValleys(weirdness);
/*     */     
/*  96 */     OverworldBiomeBuilder biomeBuilder = new OverworldBiomeBuilder();
/*  97 */     result.add("Biome builder PV: " + 
/*  98 */         OverworldBiomeBuilder.getDebugStringForPeaksAndValleys(peaksAndValleys) + " C: " + 
/*  99 */         biomeBuilder.getDebugStringForContinentalness(continentalness) + " E: " + 
/* 100 */         biomeBuilder.getDebugStringForErosion(erosion) + " T: " + 
/* 101 */         biomeBuilder.getDebugStringForTemperature(temperature) + " H: " + 
/* 102 */         biomeBuilder.getDebugStringForHumidity(humidity));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/MultiNoiseBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */