/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FeaturePlaceContext<FC extends FeatureConfiguration>
/*    */ {
/*    */   private final Optional<ConfiguredFeature<?, ?>> topFeature;
/*    */   private final WorldGenLevel level;
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   private final RandomSource random;
/*    */   private final BlockPos origin;
/*    */   private final FC config;
/*    */   
/*    */   public FeaturePlaceContext(Optional<ConfiguredFeature<?, ?>> topFeature, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin, FC config) {
/* 23 */     this.topFeature = topFeature;
/* 24 */     this.level = level;
/* 25 */     this.chunkGenerator = chunkGenerator;
/* 26 */     this.random = random;
/* 27 */     this.origin = origin;
/* 28 */     this.config = config;
/*    */   }
/*    */   
/*    */   public Optional<ConfiguredFeature<?, ?>> topFeature() {
/* 32 */     return this.topFeature;
/*    */   }
/*    */   
/*    */   public WorldGenLevel level() {
/* 36 */     return this.level;
/*    */   }
/*    */   
/*    */   public ChunkGenerator chunkGenerator() {
/* 40 */     return this.chunkGenerator;
/*    */   }
/*    */   
/*    */   public RandomSource random() {
/* 44 */     return this.random;
/*    */   }
/*    */   
/*    */   public BlockPos origin() {
/* 48 */     return this.origin;
/*    */   }
/*    */   
/*    */   public FC config() {
/* 52 */     return this.config;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/FeaturePlaceContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */