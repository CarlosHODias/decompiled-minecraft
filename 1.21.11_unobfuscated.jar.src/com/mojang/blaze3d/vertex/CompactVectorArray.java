/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class CompactVectorArray
/*    */ {
/*    */   private final float[] contents;
/*    */   
/*    */   public CompactVectorArray(int count) {
/* 11 */     this.contents = new float[3 * count];
/*    */   }
/*    */   
/*    */   public int size() {
/* 15 */     return this.contents.length / 3;
/*    */   }
/*    */   
/*    */   public void set(int index, Vector3fc v) {
/* 19 */     set(index, v.x(), v.y(), v.z());
/*    */   }
/*    */   
/*    */   public void set(int index, float x, float y, float z) {
/* 23 */     this.contents[3 * index + 0] = x;
/* 24 */     this.contents[3 * index + 1] = y;
/* 25 */     this.contents[3 * index + 2] = z;
/*    */   }
/*    */   
/*    */   public Vector3f get(int index, Vector3f output) {
/* 29 */     return output.set(this.contents[3 * index + 0], this.contents[3 * index + 1], this.contents[3 * index + 2]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getX(int index) {
/* 37 */     return this.contents[3 * index + 0];
/*    */   }
/*    */   
/*    */   public float getY(int index) {
/* 41 */     return this.contents[3 * index + 1];
/*    */   }
/*    */   
/*    */   public float getZ(int index) {
/* 45 */     return this.contents[3 * index + 1];
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/CompactVectorArray.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */