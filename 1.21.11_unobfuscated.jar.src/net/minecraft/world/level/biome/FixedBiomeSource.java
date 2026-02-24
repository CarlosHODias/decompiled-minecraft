/*    */ package net.minecraft.world.level.biome;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class FixedBiomeSource extends BiomeSource implements BiomeManager.NoiseBiomeSource {
/*    */   public static final MapCodec<FixedBiomeSource> CODEC;
/*    */   
/*    */   static {
/* 18 */     CODEC = Biome.CODEC.fieldOf("biome").xmap(FixedBiomeSource::new, s -> s.biome).stable();
/*    */   }
/*    */   private final Holder<Biome> biome;
/*    */   
/*    */   public FixedBiomeSource(Holder<Biome> biome) {
/* 23 */     this.biome = biome;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/* 28 */     return Stream.of(this.biome);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 33 */     return (MapCodec)CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
/* 38 */     return this.biome;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/* 43 */     return this.biome;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int originX, int originY, int originZ, int r, int skipStep, Predicate<Holder<Biome>> allowed, RandomSource random, boolean findClosest, Climate.Sampler sampler) {
/* 48 */     if (allowed.test(this.biome)) {
/* 49 */       if (findClosest) {
/* 50 */         return Pair.of(new BlockPos(originX, originY, originZ), this.biome);
/*    */       }
/* 52 */       return Pair.of(new BlockPos(originX - r + random.nextInt(r * 2 + 1), originY, originZ - r + random.nextInt(r * 2 + 1)), this.biome);
/*    */     } 
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos origin, int searchRadius, int sampleResolutionHorizontal, int sampleResolutionVertical, Predicate<Holder<Biome>> allowed, Climate.Sampler sampler, LevelReader level) {
/* 60 */     return allowed.test(this.biome) ? Pair.of(origin.atY(Mth.clamp(origin.getY(), level.getMinY() + 1, level.getMaxY() + 1)), this.biome) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Holder<Biome>> getBiomesWithin(int x, int y, int z, int r, Climate.Sampler sampler) {
/* 65 */     return Sets.newHashSet(Set.of(this.biome));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/FixedBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */