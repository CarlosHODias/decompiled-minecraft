/*    */ package net.minecraft.advancements.criterion;public final class MovementPredicate extends Record { private final MinMaxBounds.Doubles x; private final MinMaxBounds.Doubles y; private final MinMaxBounds.Doubles z; private final MinMaxBounds.Doubles speed;
/*    */   private final MinMaxBounds.Doubles horizontalSpeed;
/*    */   private final MinMaxBounds.Doubles verticalSpeed;
/*    */   private final MinMaxBounds.Doubles fallDistance;
/*    */   public static final com.mojang.serialization.Codec<MovementPredicate> CODEC;
/*    */   
/*  7 */   public MovementPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles speed, MinMaxBounds.Doubles horizontalSpeed, MinMaxBounds.Doubles verticalSpeed, MinMaxBounds.Doubles fallDistance) { this.x = x; this.y = y; this.z = z; this.speed = speed; this.horizontalSpeed = horizontalSpeed; this.verticalSpeed = verticalSpeed; this.fallDistance = fallDistance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MovementPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MovementPredicate; } public MinMaxBounds.Doubles x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MovementPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MovementPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MovementPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MovementPredicate;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Doubles y() { return this.y; } public MinMaxBounds.Doubles z() { return this.z; } public MinMaxBounds.Doubles speed() { return this.speed; } public MinMaxBounds.Doubles horizontalSpeed() { return this.horizontalSpeed; } public MinMaxBounds.Doubles verticalSpeed() { return this.verticalSpeed; } public MinMaxBounds.Doubles fallDistance() { return this.fallDistance; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 16 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::x), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::y), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::z), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::speed), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("horizontal_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::horizontalSpeed), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("vertical_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::verticalSpeed), (com.mojang.datafixers.kinds.App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("fall_distance", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::fallDistance)).apply((com.mojang.datafixers.kinds.Applicative)i, MovementPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MovementPredicate speed(MinMaxBounds.Doubles bounds) {
/* 27 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static MovementPredicate horizontalSpeed(MinMaxBounds.Doubles bounds) {
/* 31 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static MovementPredicate verticalSpeed(MinMaxBounds.Doubles bounds) {
/* 35 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static MovementPredicate fallDistance(MinMaxBounds.Doubles bounds) {
/* 39 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds);
/*    */   }
/*    */   
/*    */   public boolean matches(double x, double y, double z, double fallDistance) {
/* 43 */     if (!this.x.matches(x) || !this.y.matches(y) || !this.z.matches(z)) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     double speedSqr = net.minecraft.util.Mth.lengthSquared(x, y, z);
/* 48 */     if (!this.speed.matchesSqr(speedSqr)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     double horizontalSpeedSqr = net.minecraft.util.Mth.lengthSquared(x, z);
/* 53 */     if (!this.horizontalSpeed.matchesSqr(horizontalSpeedSqr)) {
/* 54 */       return false;
/*    */     }
/*    */     
/* 57 */     double verticalSpeed = Math.abs(y);
/* 58 */     if (!this.verticalSpeed.matches(verticalSpeed)) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     if (!this.fallDistance.matches(fallDistance)) {
/* 63 */       return false;
/*    */     }
/* 65 */     return true;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/MovementPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */