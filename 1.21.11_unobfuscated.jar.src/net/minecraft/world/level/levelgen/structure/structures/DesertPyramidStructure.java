/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.SortedArraySet;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ 
/*    */ public class DesertPyramidStructure extends SinglePieceStructure {
/* 27 */   public static final MapCodec<DesertPyramidStructure> CODEC = simpleCodec(DesertPyramidStructure::new);
/*    */   
/*    */   public DesertPyramidStructure(Structure.StructureSettings settings) {
/* 30 */     super(DesertPyramidPiece::new, 21, 21, settings);
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, PiecesContainer pieces) {
/* 35 */     SortedArraySet<BlockPos> sortedArraySet = SortedArraySet.create(Vec3i::compareTo);
/* 36 */     for (StructurePiece piece : (Iterable<StructurePiece>)pieces.pieces()) {
/* 37 */       if (piece instanceof DesertPyramidPiece) { DesertPyramidPiece desertPyramidPiece = (DesertPyramidPiece)piece;
/* 38 */         sortedArraySet.addAll(desertPyramidPiece.getPotentialSuspiciousSandWorldPositions());
/*    */         
/* 40 */         placeSuspiciousSand(chunkBB, level, desertPyramidPiece.getRandomCollapsedRoofPos()); }
/*    */     
/*    */     } 
/*    */     
/* 44 */     ObjectArrayList<BlockPos> shuffledSandPlacements = new ObjectArrayList(sortedArraySet.stream().toList());
/* 45 */     RandomSource positionalRandom = RandomSource.create(level.getSeed()).forkPositional().at(pieces.calculateBoundingBox().getCenter());
/* 46 */     Util.shuffle((java.util.List)shuffledSandPlacements, positionalRandom);
/* 47 */     int suspiciousSandToPlace = Math.min(sortedArraySet.size(), positionalRandom.nextInt(5, 8));
/* 48 */     for (ObjectListIterator<BlockPos> objectListIterator = shuffledSandPlacements.iterator(); objectListIterator.hasNext(); ) { BlockPos blockPos = objectListIterator.next();
/* 49 */       if (suspiciousSandToPlace > 0) {
/* 50 */         suspiciousSandToPlace--;
/* 51 */         placeSuspiciousSand(chunkBB, level, blockPos); continue;
/* 52 */       }  if (chunkBB.isInside((Vec3i)blockPos)) {
/* 53 */         level.setBlock(blockPos, Blocks.SAND.defaultBlockState(), 2);
/*    */       } }
/*    */   
/*    */   }
/*    */   
/*    */   private static void placeSuspiciousSand(BoundingBox chunkBB, WorldGenLevel level, BlockPos blockPos) {
/* 59 */     if (chunkBB.isInside((Vec3i)blockPos)) {
/* 60 */       level.setBlock(blockPos, Blocks.SUSPICIOUS_SAND.defaultBlockState(), 2);
/* 61 */       level.getBlockEntity(blockPos, BlockEntityType.BRUSHABLE_BLOCK).ifPresent(entity -> entity.setLootTable(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY, blockPos.asLong()));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 67 */     return StructureType.DESERT_PYRAMID;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/DesertPyramidStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */