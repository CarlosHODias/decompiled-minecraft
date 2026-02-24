/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ public interface NumericTag
/*    */   extends PrimitiveTag {
/*    */   byte byteValue();
/*    */   
/*    */   short shortValue();
/*    */   
/*    */   int intValue();
/*    */   
/*    */   long longValue();
/*    */   
/*    */   float floatValue();
/*    */   
/*    */   double doubleValue();
/*    */   
/*    */   Number box();
/*    */   
/*    */   default Optional<Number> asNumber() {
/* 22 */     return Optional.of(box());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Byte> asByte() {
/* 27 */     return Optional.of(byteValue());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Short> asShort() {
/* 32 */     return Optional.of(shortValue());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Integer> asInt() {
/* 37 */     return Optional.of(intValue());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Long> asLong() {
/* 42 */     return Optional.of(longValue());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Float> asFloat() {
/* 47 */     return Optional.of(floatValue());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Double> asDouble() {
/* 52 */     return Optional.of(doubleValue());
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Boolean> asBoolean() {
/* 57 */     return Optional.of((byteValue() != 0));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/NumericTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */