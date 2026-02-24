/*    */ package net.minecraft.util;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BoundedFloatFunction<C>
/*    */ {
/*    */   public static final BoundedFloatFunction<Float> IDENTITY;
/*    */   
/*    */   static BoundedFloatFunction<Float> createUnlimited(final Float2FloatFunction function) {
/* 15 */     return new BoundedFloatFunction<Float>()
/*    */       {
/*    */         public float apply(Float aFloat) {
/* 18 */           return (Float)function.apply(aFloat);
/*    */         }
/*    */ 
/*    */         
/*    */         public float minValue() {
/* 23 */           return Float.NEGATIVE_INFINITY;
/*    */         }
/*    */ 
/*    */         
/*    */         public float maxValue() {
/* 28 */           return Float.POSITIVE_INFINITY;
/*    */         }
/*    */       };
/*    */   }
/*    */   static {
/* 33 */     IDENTITY = createUnlimited(input -> input);
/*    */   }
/*    */   default <C2> BoundedFloatFunction<C2> comap(final Function<C2, C> function) {
/* 36 */     final BoundedFloatFunction<C> outer = this;
/* 37 */     return new BoundedFloatFunction<C2>(this)
/*    */       {
/*    */         public float apply(C2 c2) {
/* 40 */           return outer.apply(function.apply(c2));
/*    */         }
/*    */ 
/*    */         
/*    */         public float minValue() {
/* 45 */           return outer.minValue();
/*    */         }
/*    */ 
/*    */         
/*    */         public float maxValue() {
/* 50 */           return outer.maxValue();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   float apply(C paramC);
/*    */   
/*    */   float minValue();
/*    */   
/*    */   float maxValue();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/BoundedFloatFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */