/*    */ package net.minecraft.world.attribute.modifier;
/*    */ public final class FloatWithAlpha extends Record { private final float value;
/*    */   private final float alpha;
/*    */   private static final com.mojang.serialization.Codec<FloatWithAlpha> FULL_CODEC;
/*    */   public static final com.mojang.serialization.Codec<FloatWithAlpha> CODEC;
/*    */   
/*  7 */   public FloatWithAlpha(float value, float alpha) { this.value = value; this.alpha = alpha; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha; } public float value() { return this.value; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/modifier/FloatWithAlpha;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public float alpha() { return this.alpha; } static {
/*  8 */     FULL_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.fieldOf("value").forGetter(FloatWithAlpha::value), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).optionalFieldOf("alpha", 1.0F).forGetter(FloatWithAlpha::alpha)).apply((com.mojang.datafixers.kinds.Applicative)i, FloatWithAlpha::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 13 */     CODEC = com.mojang.serialization.Codec.either((com.mojang.serialization.Codec)com.mojang.serialization.Codec.FLOAT, FULL_CODEC).xmap(either -> (FloatWithAlpha)either.map(FloatWithAlpha::new, ()), parameter -> (parameter.alpha() == 1.0F) ? com.mojang.datafixers.util.Either.left(parameter.value()) : com.mojang.datafixers.util.Either.right(parameter));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FloatWithAlpha(float value) {
/* 19 */     this(value, 1.0F);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/attribute/modifier/FloatWithAlpha.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */