/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public final class InclusiveRange<T extends Comparable<T>> extends Record {
/*    */   private final T minInclusive;
/*    */   private final T maxInclusive;
/*    */   
/*  8 */   public T minInclusive() { return this.minInclusive; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/InclusiveRange;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/InclusiveRange;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/InclusiveRange<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/InclusiveRange;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/InclusiveRange;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  8 */     //   0	8	0	this	Lnet/minecraft/util/InclusiveRange<TT;>; } public T maxInclusive() { return this.maxInclusive; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends Comparable<T>> com.mojang.serialization.Codec<InclusiveRange<T>> codec(com.mojang.serialization.Codec<T> elementCodec) {
/* 13 */     return ExtraCodecs.intervalCodec(elementCodec, "min_inclusive", "max_inclusive", InclusiveRange::create, InclusiveRange::minInclusive, InclusiveRange::maxInclusive);
/*    */   }
/*    */   
/*    */   public static <T extends Comparable<T>> com.mojang.serialization.Codec<InclusiveRange<T>> codec(com.mojang.serialization.Codec<T> elementCodec, T minAllowedInclusive, T maxAllowedInclusive) {
/* 17 */     return codec(elementCodec).validate(value -> (value.minInclusive().compareTo(minAllowedInclusive) < 0) ? com.mojang.serialization.DataResult.error(()) : ((value.maxInclusive().compareTo(maxAllowedInclusive) > 0) ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(value)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends Comparable<T>> com.mojang.serialization.DataResult<InclusiveRange<T>> create(T minInclusive, T maxInclusive) {
/* 29 */     if (minInclusive.compareTo(maxInclusive) <= 0) {
/* 30 */       return com.mojang.serialization.DataResult.success(new InclusiveRange<>(minInclusive, maxInclusive));
/*    */     }
/* 32 */     return com.mojang.serialization.DataResult.error(() -> "min_inclusive must be less than or equal to max_inclusive");
/*    */   }
/*    */   
/*    */   public InclusiveRange(T minInclusive, T maxInclusive) {
/* 36 */     if (minInclusive.compareTo(maxInclusive) > 0)
/* 37 */       throw new IllegalArgumentException("min_inclusive must be less than or equal to max_inclusive"); 
/*    */     this.minInclusive = minInclusive;
/*    */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */   public InclusiveRange(T value) {
/* 42 */     this(value, value);
/*    */   }
/*    */   
/*    */   public <S extends Comparable<S>> InclusiveRange<S> map(java.util.function.Function<? super T, ? extends S> mapper) {
/* 46 */     return new InclusiveRange((T)mapper.apply(this.minInclusive), (T)mapper.apply(this.maxInclusive));
/*    */   }
/*    */   
/* 49 */   public static final com.mojang.serialization.Codec<InclusiveRange<Integer>> INT = codec((com.mojang.serialization.Codec<Integer>)com.mojang.serialization.Codec.INT);
/*    */   
/*    */   public boolean isValueInRange(T value) {
/* 52 */     return (value.compareTo(this.minInclusive) >= 0 && value.compareTo(this.maxInclusive) <= 0);
/*    */   }
/*    */   
/*    */   public boolean contains(InclusiveRange<T> subRange) {
/* 56 */     return (subRange.minInclusive().compareTo(this.minInclusive) >= 0 && 
/* 57 */       subRange.maxInclusive.compareTo(this.maxInclusive) <= 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "[" + String.valueOf(this.minInclusive) + ", " + String.valueOf(this.maxInclusive) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/InclusiveRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */