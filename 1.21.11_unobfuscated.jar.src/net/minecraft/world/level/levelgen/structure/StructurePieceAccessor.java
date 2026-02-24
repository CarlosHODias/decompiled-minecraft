package net.minecraft.world.level.levelgen.structure;

public interface StructurePieceAccessor {
  void addPiece(StructurePiece paramStructurePiece);
  
  StructurePiece findCollisionPiece(BoundingBox paramBoundingBox);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/StructurePieceAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */