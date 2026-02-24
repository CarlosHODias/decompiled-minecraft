/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public interface LerpFunction<T> {
/*    */   static LerpFunction<Float> ofFloat() {
/*  8 */     return Mth::lerp;
/*    */   }
/*    */   
/*    */   static LerpFunction<Float> ofDegrees(float maxDelta) {
/* 12 */     return (alpha, from, to) -> {
/*    */         float delta = Mth.wrapDegrees(to - from);
/*    */         return (Math.abs(delta) >= maxDelta) ? to : (from + alpha * delta);
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> LerpFunction<T> ofConstant() {
/* 22 */     return (alpha, from, to) -> from;
/*    */   }
/*    */   
/*    */   static <T> LerpFunction<T> ofStep(float threshold) {
/* 26 */     return (alpha, from, to) -> (alpha >= threshold) ? to : from;
/*    */   }
/*    */   
/*    */   static LerpFunction<Integer> ofColor() {
/* 30 */     return ARGB::srgbLerp;
/*    */   }
/*    */   
/*    */   T apply(float paramFloat, T paramT1, T paramT2);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/LerpFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */