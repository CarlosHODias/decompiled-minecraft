/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ 
/*    */ public class CubePointRange extends AbstractDoubleList {
/*    */   private final int parts;
/*    */   
/*    */   public CubePointRange(int parts) {
/*  9 */     if (parts <= 0) {
/* 10 */       throw new IllegalArgumentException("Need at least 1 part");
/*    */     }
/* 12 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int index) {
/* 17 */     return index / this.parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 22 */     return this.parts + 1;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/CubePointRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */