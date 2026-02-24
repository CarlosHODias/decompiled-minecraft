/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Cursor3D
/*    */ {
/*    */   public static final int TYPE_INSIDE = 0;
/*    */   public static final int TYPE_FACE = 1;
/*    */   public static final int TYPE_EDGE = 2;
/*    */   public static final int TYPE_CORNER = 3;
/*    */   private final int originX;
/*    */   private final int originY;
/*    */   private final int originZ;
/*    */   private final int width;
/*    */   private final int height;
/*    */   private final int depth;
/*    */   private final int end;
/*    */   private int index;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public Cursor3D(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/* 24 */     this.originX = minX;
/* 25 */     this.originY = minY;
/* 26 */     this.originZ = minZ;
/*    */     
/* 28 */     this.width = maxX - minX + 1;
/* 29 */     this.height = maxY - minY + 1;
/* 30 */     this.depth = maxZ - minZ + 1;
/* 31 */     this.end = this.width * this.height * this.depth;
/*    */   }
/*    */   
/*    */   public boolean advance() {
/* 35 */     if (this.index == this.end) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     this.x = this.index % this.width;
/* 40 */     int slice = this.index / this.width;
/* 41 */     this.y = slice % this.height;
/* 42 */     this.z = slice / this.height;
/*    */     
/* 44 */     this.index++;
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public int nextX() {
/* 49 */     return this.originX + this.x;
/*    */   }
/*    */   
/*    */   public int nextY() {
/* 53 */     return this.originY + this.y;
/*    */   }
/*    */   
/*    */   public int nextZ() {
/* 57 */     return this.originZ + this.z;
/*    */   }
/*    */   
/*    */   public int getNextType() {
/* 61 */     int type = 0;
/* 62 */     if (this.x == 0 || this.x == this.width - 1) {
/* 63 */       type++;
/*    */     }
/* 65 */     if (this.y == 0 || this.y == this.height - 1) {
/* 66 */       type++;
/*    */     }
/* 68 */     if (this.z == 0 || this.z == this.depth - 1) {
/* 69 */       type++;
/*    */     }
/* 71 */     return type;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/Cursor3D.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */