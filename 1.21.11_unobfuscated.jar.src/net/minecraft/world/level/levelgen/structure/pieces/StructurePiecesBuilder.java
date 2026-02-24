/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ 
/*    */ public class StructurePiecesBuilder
/*    */   implements StructurePieceAccessor
/*    */ {
/* 13 */   private final List<StructurePiece> pieces = Lists.newArrayList();
/*    */ 
/*    */   
/*    */   public void addPiece(StructurePiece piece) {
/* 17 */     this.pieces.add(piece);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePiece findCollisionPiece(BoundingBox box) {
/* 22 */     return StructurePiece.findCollisionPiece(this.pieces, box);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public void offsetPiecesVertically(int dy) {
/* 30 */     for (StructurePiece piece : this.pieces) {
/* 31 */       piece.move(0, dy, 0);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public int moveBelowSeaLevel(int seaLevel, int minY, RandomSource random, int offset) {
/* 40 */     int maxY = seaLevel - offset;
/*    */ 
/*    */     
/* 43 */     BoundingBox boundingBox = getBoundingBox();
/* 44 */     int y1Pos = boundingBox.getYSpan() + minY + 1;
/*    */     
/* 46 */     if (y1Pos < maxY) {
/* 47 */       y1Pos += random.nextInt(maxY - y1Pos);
/*    */     }
/*    */ 
/*    */     
/* 51 */     int dy = y1Pos - boundingBox.maxY();
/* 52 */     offsetPiecesVertically(dy);
/* 53 */     return dy;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void moveInsideHeights(RandomSource random, int lowestAllowed, int highestAllowed) {
/*    */     int y0Pos;
/* 60 */     BoundingBox boundingBox = getBoundingBox();
/* 61 */     int heightSpan = highestAllowed - lowestAllowed + 1 - boundingBox.getYSpan();
/*    */ 
/*    */     
/* 64 */     if (heightSpan > 1) {
/* 65 */       y0Pos = lowestAllowed + random.nextInt(heightSpan);
/*    */     } else {
/* 67 */       y0Pos = lowestAllowed;
/*    */     } 
/*    */ 
/*    */     
/* 71 */     int dy = y0Pos - boundingBox.minY();
/* 72 */     offsetPiecesVertically(dy);
/*    */   }
/*    */   
/*    */   public PiecesContainer build() {
/* 76 */     return new PiecesContainer(this.pieces);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 81 */     this.pieces.clear();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 85 */     return this.pieces.isEmpty();
/*    */   }
/*    */   
/*    */   public BoundingBox getBoundingBox() {
/* 89 */     return StructurePiece.createBoundingBox(this.pieces.stream());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pieces/StructurePiecesBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */