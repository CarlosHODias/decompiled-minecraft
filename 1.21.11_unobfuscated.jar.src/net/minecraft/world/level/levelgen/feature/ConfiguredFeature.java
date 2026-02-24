/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public final class ConfiguredFeature<FC extends FeatureConfiguration, F extends Feature<FC>> extends Record {
/*    */   private final F feature;
/*    */   private final FC config;
/*    */   public static final Codec<ConfiguredFeature<?, ?>> DIRECT_CODEC;
/*    */   
/* 18 */   public ConfiguredFeature(F feature, FC config) { this.feature = feature; this.config = config; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature<TFC;TF;>; } public F feature() { return this.feature; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 18 */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature<TFC;TF;>; } public FC config() { return this.config; }
/*    */ 
/*    */   
/*    */   static {
/* 22 */     DIRECT_CODEC = net.minecraft.core.registries.BuiltInRegistries.FEATURE.byNameCodec().dispatch(f -> f.feature, Feature::configuredCodec);
/*    */   }
/* 24 */   public static final Codec<Holder<ConfiguredFeature<?, ?>>> CODEC = (Codec<Holder<ConfiguredFeature<?, ?>>>)net.minecraft.resources.RegistryFileCodec.create(Registries.CONFIGURED_FEATURE, DIRECT_CODEC);
/* 25 */   public static final Codec<net.minecraft.core.HolderSet<ConfiguredFeature<?, ?>>> LIST_CODEC = net.minecraft.core.RegistryCodecs.homogeneousList(Registries.CONFIGURED_FEATURE, DIRECT_CODEC);
/*    */   
/*    */   public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
/* 28 */     return this.feature.place(this.config, level, chunkGenerator, random, origin);
/*    */   }
/*    */   
/*    */   public Stream<ConfiguredFeature<?, ?>> getFeatures() {
/* 32 */     return Stream.concat(Stream.of(this), this.config.getFeatures());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     return "Configured: " + String.valueOf(this.feature) + ": " + String.valueOf(this.config);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/ConfiguredFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */