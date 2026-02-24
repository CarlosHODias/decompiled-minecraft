/*    */ package net.minecraft.advancements.criterion;public final class DistancePredicate extends Record { private final MinMaxBounds.Doubles x; private final MinMaxBounds.Doubles y;
/*    */   private final MinMaxBounds.Doubles z;
/*    */   private final MinMaxBounds.Doubles horizontal;
/*    */   private final MinMaxBounds.Doubles absolute;
/*    */   public static final com.mojang.serialization.Codec<DistancePredicate> CODEC;
/*    */   
/*  7 */   public DistancePredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles horizontal, MinMaxBounds.Doubles absolute) { this.x = x; this.y = y; this.z = z; this.horizontal = horizontal; this.absolute = absolute; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/DistancePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DistancePredicate; } public MinMaxBounds.Doubles x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/DistancePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/DistancePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/DistancePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/DistancePredicate;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Doubles y() { return this.y; } public MinMaxBounds.Doubles z() { return this.z; } public MinMaxBounds.Doubles horizontal() { return this.horizontal; } public MinMaxBounds.Doubles absolute() { return this.absolute; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 14 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::x), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::y), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::z), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("horizontal", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::horizontal), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("absolute", MinMaxBounds.Doubles.ANY).forGetter(DistancePredicate::absolute)).apply((com.mojang.datafixers.kinds.Applicative)i, DistancePredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DistancePredicate horizontal(MinMaxBounds.Doubles horizontal) {
/* 23 */     return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, horizontal, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static DistancePredicate vertical(MinMaxBounds.Doubles y) {
/* 27 */     return new DistancePredicate(MinMaxBounds.Doubles.ANY, y, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static DistancePredicate absolute(MinMaxBounds.Doubles absolute) {
/* 31 */     return new DistancePredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, absolute);
/*    */   }
/*    */   
/*    */   public boolean matches(double x0, double y0, double z0, double x1, double y1, double z1) {
/* 35 */     float xd = (float)(x0 - x1);
/* 36 */     float yd = (float)(y0 - y1);
/* 37 */     float zd = (float)(z0 - z1);
/* 38 */     if (!this.x.matches(net.minecraft.util.Mth.abs(xd)) || !this.y.matches(net.minecraft.util.Mth.abs(yd)) || !this.z.matches(net.minecraft.util.Mth.abs(zd))) {
/* 39 */       return false;
/*    */     }
/* 41 */     if (!this.horizontal.matchesSqr((xd * xd + zd * zd))) {
/* 42 */       return false;
/*    */     }
/* 44 */     if (!this.absolute.matchesSqr((xd * xd + yd * yd + zd * zd))) {
/* 45 */       return false;
/*    */     }
/* 47 */     return true;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/DistancePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */