/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ 
/*    */ public final class PiecesContainer extends Record {
/*    */   private final List<StructurePiece> pieces;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;
/*    */   }
/*    */   
/* 20 */   public List<StructurePiece> pieces() { return this.pieces; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */ 
/*    */   
/* 24 */   private static final Identifier JIGSAW_RENAME = Identifier.withDefaultNamespace("jigsaw");
/* 25 */   private static final Map<Identifier, Identifier> RENAMES = (Map<Identifier, Identifier>)com.google.common.collect.ImmutableMap.builder()
/* 26 */     .put(Identifier.withDefaultNamespace("nvi"), JIGSAW_RENAME)
/* 27 */     .put(Identifier.withDefaultNamespace("pcp"), JIGSAW_RENAME)
/* 28 */     .put(Identifier.withDefaultNamespace("bastionremnant"), JIGSAW_RENAME)
/* 29 */     .put(Identifier.withDefaultNamespace("runtime"), JIGSAW_RENAME)
/* 30 */     .build();
/*    */   
/*    */   public PiecesContainer(List<StructurePiece> pieces) {
/* 33 */     this.pieces = List.copyOf(pieces);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 37 */     return this.pieces.isEmpty();
/*    */   }
/*    */   
/*    */   public boolean isInsidePiece(BlockPos startPos) {
/* 41 */     for (StructurePiece piece : this.pieces) {
/* 42 */       if (piece.getBoundingBox().isInside((Vec3i)startPos)) {
/* 43 */         return true;
/*    */       }
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   public Tag save(StructurePieceSerializationContext context) {
/* 50 */     ListTag childrenTags = new ListTag();
/* 51 */     for (StructurePiece piece : this.pieces) {
/* 52 */       childrenTags.add(piece.createTag(context));
/*    */     }
/* 54 */     return (Tag)childrenTags;
/*    */   }
/*    */   
/*    */   public static PiecesContainer load(ListTag children, StructurePieceSerializationContext context) {
/* 58 */     List<StructurePiece> pieces = com.google.common.collect.Lists.newArrayList();
/* 59 */     for (int i = 0; i < children.size(); i++) {
/* 60 */       CompoundTag pieceTag = children.getCompoundOrEmpty(i);
/* 61 */       String oldId = pieceTag.getStringOr("id", "").toLowerCase(java.util.Locale.ROOT);
/* 62 */       Identifier oldPieceKey = Identifier.parse(oldId);
/* 63 */       Identifier pieceId = RENAMES.getOrDefault(oldPieceKey, oldPieceKey);
/*    */       
/* 65 */       StructurePieceType pieceType = (StructurePieceType)net.minecraft.core.registries.BuiltInRegistries.STRUCTURE_PIECE.getValue(pieceId);
/*    */       
/* 67 */       if (pieceType == null) {
/* 68 */         LOGGER.error("Unknown structure piece id: {}", pieceId);
/*    */       } else {
/*    */ 
/*    */         
/*    */         try {
/* 73 */           StructurePiece piece = pieceType.load(context, pieceTag);
/* 74 */           pieces.add(piece);
/* 75 */         } catch (Exception e) {
/* 76 */           LOGGER.error("Exception loading structure piece with id {}", pieceId, e);
/*    */         } 
/*    */       } 
/* 79 */     }  return new PiecesContainer(pieces);
/*    */   }
/*    */   
/*    */   public net.minecraft.world.level.levelgen.structure.BoundingBox calculateBoundingBox() {
/* 83 */     return StructurePiece.createBoundingBox(this.pieces.stream());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pieces/PiecesContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */