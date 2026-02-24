/*    */ package net.minecraft.util;
/*    */ public final class Keyframe<T> extends Record {
/*    */   private final int ticks;
/*    */   private final T value;
/*    */   
/*  6 */   public Keyframe(int ticks, T value) { this.ticks = ticks; this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/Keyframe;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Keyframe;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/Keyframe<TT;>; } public int ticks() { return this.ticks; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Keyframe;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Keyframe;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/Keyframe<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/Keyframe;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/Keyframe;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  6 */     //   0	8	0	this	Lnet/minecraft/util/Keyframe<TT;>; } public T value() { return this.value; }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> com.mojang.serialization.Codec<Keyframe<T>> codec(com.mojang.serialization.Codec<T> valueCodec) {
/* 11 */     return com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks").forGetter(Keyframe::ticks), (com.mojang.datafixers.kinds.App)valueCodec.fieldOf("value").forGetter(Keyframe::value)).apply((com.mojang.datafixers.kinds.Applicative)i, Keyframe::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/Keyframe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */