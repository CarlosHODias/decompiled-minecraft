/*    */ package com.mojang.math;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Divisor
/*    */   implements IntIterator
/*    */ {
/*    */   private final int denominator;
/*    */   private final int quotient;
/*    */   private final int mod;
/*    */   private int returnedParts;
/*    */   private int remainder;
/*    */   
/*    */   public Divisor(int numerator, int denominator) {
/* 35 */     this.denominator = denominator;
/* 36 */     if (denominator > 0) {
/* 37 */       this.quotient = numerator / denominator;
/* 38 */       this.mod = numerator % denominator;
/*    */     } else {
/* 40 */       this.quotient = 0;
/* 41 */       this.mod = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 47 */     return (this.returnedParts < this.denominator);
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextInt() {
/* 52 */     if (!hasNext()) {
/* 53 */       throw new NoSuchElementException();
/*    */     }
/* 55 */     int next = this.quotient;
/* 56 */     this.remainder += this.mod;
/* 57 */     if (this.remainder >= this.denominator) {
/* 58 */       this.remainder -= this.denominator;
/* 59 */       next++;
/*    */     } 
/* 61 */     this.returnedParts++;
/* 62 */     return next;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public static Iterable<Integer> asIterable(int numerator, int denominator) {
/* 67 */     return () -> new Divisor(numerator, denominator);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/math/Divisor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */