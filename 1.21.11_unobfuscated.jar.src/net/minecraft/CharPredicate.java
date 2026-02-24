/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CharPredicate
/*    */ {
/*    */   default CharPredicate and(CharPredicate other) {
/* 10 */     Objects.requireNonNull(other);
/* 11 */     return value -> (test(other) && other.test(other));
/*    */   }
/*    */   
/*    */   default CharPredicate negate() {
/* 15 */     return value -> !test(value);
/*    */   }
/*    */   
/*    */   default CharPredicate or(CharPredicate other) {
/* 19 */     Objects.requireNonNull(other);
/* 20 */     return value -> (test(other) || other.test(other));
/*    */   }
/*    */   
/*    */   boolean test(char paramChar);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/CharPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */