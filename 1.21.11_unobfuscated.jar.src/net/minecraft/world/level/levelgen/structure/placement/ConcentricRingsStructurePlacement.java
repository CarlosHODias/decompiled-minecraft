/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function9;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class ConcentricRingsStructurePlacement extends StructurePlacement {
/*    */   public static final com.mojang.serialization.MapCodec<ConcentricRingsStructurePlacement> CODEC;
/*    */   private final int distance;
/*    */   
/*    */   private static Products.P9<RecordCodecBuilder.Mu<ConcentricRingsStructurePlacement>, Vec3i, StructurePlacement.FrequencyReductionMethod, Float, Integer, Optional<StructurePlacement.ExclusionZone>, Integer, Integer, Integer, HolderSet<Biome>> codec(RecordCodecBuilder.Instance<ConcentricRingsStructurePlacement> i) {
/* 20 */     Products.P5<RecordCodecBuilder.Mu<ConcentricRingsStructurePlacement>, Vec3i, StructurePlacement.FrequencyReductionMethod, Float, Integer, Optional<StructurePlacement.ExclusionZone>> placement = placementCodec(i);
/* 21 */     Products.P4<RecordCodecBuilder.Mu<ConcentricRingsStructurePlacement>, Integer, Integer, Integer, HolderSet<Biome>> rings = i.group(
/* 22 */         (App)Codec.intRange(0, 1023).fieldOf("distance").forGetter(ConcentricRingsStructurePlacement::distance), 
/* 23 */         (App)Codec.intRange(0, 1023).fieldOf("spread").forGetter(ConcentricRingsStructurePlacement::spread), 
/* 24 */         (App)Codec.intRange(1, 4095).fieldOf("count").forGetter(ConcentricRingsStructurePlacement::count), 
/* 25 */         (App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BIOME).fieldOf("preferred_biomes").forGetter(ConcentricRingsStructurePlacement::preferredBiomes));
/*    */     
/* 27 */     return new Products.P9(placement.t1(), placement.t2(), placement.t3(), placement.t4(), placement.t5(), rings.t1(), rings.t2(), rings.t3(), rings.t4());
/*    */   } private final int spread; private final int count; private final HolderSet<Biome> preferredBiomes;
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.mapCodec(i -> codec(i).apply((Applicative)i, ConcentricRingsStructurePlacement::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConcentricRingsStructurePlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<StructurePlacement.ExclusionZone> exclusionZone, int distance, int spread, int count, HolderSet<Biome> preferredBiomes) {
/* 38 */     super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
/* 39 */     this.distance = distance;
/* 40 */     this.spread = spread;
/* 41 */     this.count = count;
/* 42 */     this.preferredBiomes = preferredBiomes;
/*    */   }
/*    */   
/*    */   public ConcentricRingsStructurePlacement(int distance, int spread, int count, HolderSet<Biome> preferredBiomes) {
/* 46 */     this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, 0, Optional.empty(), distance, spread, count, preferredBiomes);
/*    */   }
/*    */   
/*    */   public int distance() {
/* 50 */     return this.distance;
/*    */   }
/*    */   
/*    */   public int spread() {
/* 54 */     return this.spread;
/*    */   }
/*    */   
/*    */   public int count() {
/* 58 */     return this.count;
/*    */   }
/*    */   
/*    */   public HolderSet<Biome> preferredBiomes() {
/* 62 */     return this.preferredBiomes;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPlacementChunk(net.minecraft.world.level.chunk.ChunkGeneratorStructureState generatorState, int sourceX, int sourceZ) {
/* 67 */     List<ChunkPos> positions = generatorState.getRingPositionsFor(this);
/* 68 */     if (positions == null) {
/* 69 */       return false;
/*    */     }
/* 71 */     return positions.contains(new ChunkPos(sourceX, sourceZ));
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePlacementType<?> type() {
/* 76 */     return StructurePlacementType.CONCENTRIC_RINGS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/placement/ConcentricRingsStructurePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */