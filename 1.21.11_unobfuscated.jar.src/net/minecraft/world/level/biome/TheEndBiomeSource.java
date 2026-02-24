/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.QuartPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.world.level.levelgen.DensityFunction;
/*    */ 
/*    */ public class TheEndBiomeSource extends BiomeSource {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryOps.retrieveElement(Biomes.THE_END), (App)RegistryOps.retrieveElement(Biomes.END_HIGHLANDS), (App)RegistryOps.retrieveElement(Biomes.END_MIDLANDS), (App)RegistryOps.retrieveElement(Biomes.SMALL_END_ISLANDS), (App)RegistryOps.retrieveElement(Biomes.END_BARRENS)).apply((com.mojang.datafixers.kinds.Applicative)i, i.stable(TheEndBiomeSource::new)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<TheEndBiomeSource> CODEC;
/*    */   
/*    */   private final Holder<Biome> end;
/*    */   
/*    */   private final Holder<Biome> highlands;
/*    */   
/*    */   private final Holder<Biome> midlands;
/*    */   private final Holder<Biome> islands;
/*    */   private final Holder<Biome> barrens;
/*    */   
/*    */   public static TheEndBiomeSource create(HolderGetter<Biome> biomes) {
/* 31 */     return new TheEndBiomeSource((Holder<Biome>)
/* 32 */         biomes.getOrThrow(Biomes.THE_END), (Holder<Biome>)
/* 33 */         biomes.getOrThrow(Biomes.END_HIGHLANDS), (Holder<Biome>)
/* 34 */         biomes.getOrThrow(Biomes.END_MIDLANDS), (Holder<Biome>)
/* 35 */         biomes.getOrThrow(Biomes.SMALL_END_ISLANDS), (Holder<Biome>)
/* 36 */         biomes.getOrThrow(Biomes.END_BARRENS));
/*    */   }
/*    */ 
/*    */   
/*    */   private TheEndBiomeSource(Holder<Biome> end, Holder<Biome> highlands, Holder<Biome> midlands, Holder<Biome> islands, Holder<Biome> barrens) {
/* 41 */     this.end = end;
/* 42 */     this.highlands = highlands;
/* 43 */     this.midlands = midlands;
/* 44 */     this.islands = islands;
/* 45 */     this.barrens = barrens;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/* 50 */     return Stream.of((Holder<Biome>[])new Holder[] { this.end, this.highlands, this.midlands, this.islands, this.barrens });
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 55 */     return (MapCodec)CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
/* 60 */     int blockX = QuartPos.toBlock(quartX);
/* 61 */     int blockY = QuartPos.toBlock(quartY);
/* 62 */     int blockZ = QuartPos.toBlock(quartZ);
/*    */     
/* 64 */     int chunkX = SectionPos.blockToSectionCoord(blockX);
/* 65 */     int chunkZ = SectionPos.blockToSectionCoord(blockZ);
/*    */     
/* 67 */     if (chunkX * chunkX + chunkZ * chunkZ <= 4096L) {
/* 68 */       return this.end;
/*    */     }
/*    */     
/* 71 */     int weirdBlockX = (SectionPos.blockToSectionCoord(blockX) * 2 + 1) * 8;
/* 72 */     int weirdBlockZ = (SectionPos.blockToSectionCoord(blockZ) * 2 + 1) * 8;
/*    */     
/* 74 */     double heightValue = sampler.erosion().compute((DensityFunction.FunctionContext)new DensityFunction.SinglePointContext(weirdBlockX, blockY, weirdBlockZ));
/* 75 */     if (heightValue > 0.25D) {
/* 76 */       return this.highlands;
/*    */     }
/*    */     
/* 79 */     if (heightValue >= -0.0625D) {
/* 80 */       return this.midlands;
/*    */     }
/*    */     
/* 83 */     if (heightValue < -0.21875D) {
/* 84 */       return this.islands;
/*    */     }
/*    */     
/* 87 */     return this.barrens;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/TheEndBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */