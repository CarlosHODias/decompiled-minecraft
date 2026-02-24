/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.tags.BiomeTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.RandomSupport;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class OceanMonumentStructure extends Structure {
/* 25 */   public static final MapCodec<OceanMonumentStructure> CODEC = simpleCodec(OceanMonumentStructure::new);
/*    */   
/*    */   public OceanMonumentStructure(Structure.StructureSettings settings) {
/* 28 */     super(settings);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 35 */     int offsetX = context.chunkPos().getBlockX(9);
/* 36 */     int offsetZ = context.chunkPos().getBlockZ(9);
/*    */     
/* 38 */     Set<Holder<Biome>> biomesRange = context.biomeSource().getBiomesWithin(offsetX, context.chunkGenerator().getSeaLevel(), offsetZ, 29, context.randomState().sampler());
/* 39 */     for (Holder<Biome> biome : biomesRange) {
/* 40 */       if (!biome.is(BiomeTags.REQUIRED_OCEAN_MONUMENT_SURROUNDING)) {
/* 41 */         return Optional.empty();
/*    */       }
/*    */     } 
/*    */     
/* 45 */     return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private static StructurePiece createTopPiece(ChunkPos chunkPos, WorldgenRandom random) {
/* 49 */     int west = chunkPos.getMinBlockX() - 29;
/* 50 */     int north = chunkPos.getMinBlockZ() - 29;
/* 51 */     Direction orientation = Direction.Plane.HORIZONTAL.getRandomDirection((RandomSource)random);
/* 52 */     return new OceanMonumentPieces.MonumentBuilding((RandomSource)random, west, north, orientation);
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 56 */     builder.addPiece(createTopPiece(context.chunkPos(), context.random()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static PiecesContainer regeneratePiecesAfterLoad(ChunkPos chunkPos, long seed, PiecesContainer savedPieces) {
/* 62 */     if (savedPieces.isEmpty()) {
/* 63 */       return savedPieces;
/*    */     }
/* 65 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 66 */     random.setLargeFeatureSeed(seed, chunkPos.x, chunkPos.z);
/*    */     
/* 68 */     StructurePiece oldTopPiece = savedPieces.pieces().get(0);
/* 69 */     BoundingBox oldBoundingBox = oldTopPiece.getBoundingBox();
/*    */ 
/*    */     
/* 72 */     int west = oldBoundingBox.minX();
/* 73 */     int north = oldBoundingBox.minZ();
/* 74 */     Direction defaultOrientation = Direction.Plane.HORIZONTAL.getRandomDirection((RandomSource)random);
/* 75 */     Direction orientation = Objects.<Direction>requireNonNullElse(oldTopPiece.getOrientation(), defaultOrientation);
/*    */     
/* 77 */     StructurePiece topPiece = new OceanMonumentPieces.MonumentBuilding((RandomSource)random, west, north, orientation);
/* 78 */     StructurePiecesBuilder result = new StructurePiecesBuilder();
/* 79 */     result.addPiece(topPiece);
/* 80 */     return result.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 85 */     return StructureType.OCEAN_MONUMENT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/OceanMonumentStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */