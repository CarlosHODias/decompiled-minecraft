/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class SubShape extends DiscreteVoxelShape {
/*    */   private final DiscreteVoxelShape parent;
/*    */   private final int startX;
/*    */   private final int startY;
/*    */   private final int startZ;
/*    */   private final int endX;
/*    */   private final int endY;
/*    */   private final int endZ;
/*    */   
/*    */   protected SubShape(DiscreteVoxelShape parent, int startX, int startY, int startZ, int endX, int endY, int endZ) {
/* 16 */     super(endX - startX, endY - startY, endZ - startZ);
/* 17 */     this.parent = parent;
/* 18 */     this.startX = startX;
/* 19 */     this.startY = startY;
/* 20 */     this.startZ = startZ;
/* 21 */     this.endX = endX;
/* 22 */     this.endY = endY;
/* 23 */     this.endZ = endZ;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull(int x, int y, int z) {
/* 28 */     return this.parent.isFull(this.startX + x, this.startY + y, this.startZ + z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fill(int x, int y, int z) {
/* 33 */     this.parent.fill(this.startX + x, this.startY + y, this.startZ + z);
/*    */   }
/*    */ 
/*    */   
/*    */   public int firstFull(Direction.Axis axis) {
/* 38 */     return clampToShape(axis, this.parent.firstFull(axis));
/*    */   }
/*    */ 
/*    */   
/*    */   public int lastFull(Direction.Axis axis) {
/* 43 */     return clampToShape(axis, this.parent.lastFull(axis));
/*    */   }
/*    */   
/*    */   private int clampToShape(Direction.Axis axis, int parentResult) {
/* 47 */     int start = axis.choose(this.startX, this.startY, this.startZ);
/* 48 */     int end = axis.choose(this.endX, this.endY, this.endZ);
/* 49 */     return Mth.clamp(parentResult, start, end) - start;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/SubShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */