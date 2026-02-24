/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ 
/*    */ public class CheckerboardColumnBiomeSource extends BiomeSource {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Biome.LIST_CODEC.fieldOf("biomes").forGetter(()), (App)Codec.intRange(0, 62).fieldOf("scale").orElse(2).forGetter(())).apply((Applicative)i, CheckerboardColumnBiomeSource::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<CheckerboardColumnBiomeSource> CODEC;
/*    */   private final HolderSet<Biome> allowedBiomes;
/*    */   private final int bitShift;
/*    */   private final int size;
/*    */   
/*    */   public CheckerboardColumnBiomeSource(HolderSet<Biome> allowedBiomes, int size) {
/* 22 */     this.allowedBiomes = allowedBiomes;
/* 23 */     this.bitShift = size + 2;
/* 24 */     this.size = size;
/*    */   }
/*    */ 
/*    */   
/*    */   protected java.util.stream.Stream<Holder<Biome>> collectPossibleBiomes() {
/* 29 */     return this.allowedBiomes.stream();
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 34 */     return (MapCodec)CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
/* 39 */     return this.allowedBiomes.get(Math.floorMod((quartX >> this.bitShift) + (quartZ >> this.bitShift), this.allowedBiomes.size()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/biome/CheckerboardColumnBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */