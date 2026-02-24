/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class Tuple<A, B> {
/*    */   private A a;
/*    */   private B b;
/*    */   
/*    */   public Tuple(A a, B b) {
/*  8 */     this.a = a;
/*  9 */     this.b = b;
/*    */   }
/*    */   
/*    */   public A getA() {
/* 13 */     return this.a;
/*    */   }
/*    */   
/*    */   public void setA(A a) {
/* 17 */     this.a = a;
/*    */   }
/*    */   
/*    */   public B getB() {
/* 21 */     return this.b;
/*    */   }
/*    */   
/*    */   public void setB(B b) {
/* 25 */     this.b = b;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/Tuple.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */