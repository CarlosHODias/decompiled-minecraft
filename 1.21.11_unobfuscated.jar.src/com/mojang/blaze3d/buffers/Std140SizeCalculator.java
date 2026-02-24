/*    */ package com.mojang.blaze3d.buffers;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Std140SizeCalculator
/*    */ {
/*    */   private int size;
/*    */   
/*    */   public int get() {
/* 14 */     return this.size;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator align(int alignment) {
/* 18 */     this.size = Mth.roundToward(this.size, alignment);
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putFloat() {
/* 23 */     align(4);
/* 24 */     this.size += 4;
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putInt() {
/* 29 */     align(4);
/* 30 */     this.size += 4;
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putVec2() {
/* 35 */     align(8);
/* 36 */     this.size += 8;
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putIVec2() {
/* 41 */     align(8);
/* 42 */     this.size += 8;
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putVec3() {
/* 47 */     align(16);
/* 48 */     this.size += 16;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putIVec3() {
/* 53 */     align(16);
/* 54 */     this.size += 16;
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putVec4() {
/* 59 */     align(16);
/* 60 */     this.size += 16;
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putIVec4() {
/* 65 */     align(16);
/* 66 */     this.size += 16;
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public Std140SizeCalculator putMat4f() {
/* 71 */     align(16);
/* 72 */     this.size += 64;
/* 73 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/buffers/Std140SizeCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */