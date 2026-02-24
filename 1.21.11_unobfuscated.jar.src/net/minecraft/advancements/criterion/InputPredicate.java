/*    */ package net.minecraft.advancements.criterion;public final class InputPredicate extends Record { private final java.util.Optional<Boolean> forward; private final java.util.Optional<Boolean> backward;
/*    */   private final java.util.Optional<Boolean> left;
/*    */   private final java.util.Optional<Boolean> right;
/*    */   private final java.util.Optional<Boolean> jump;
/*    */   private final java.util.Optional<Boolean> sneak;
/*    */   private final java.util.Optional<Boolean> sprint;
/*    */   public static final com.mojang.serialization.Codec<InputPredicate> CODEC;
/*    */   
/*  9 */   public InputPredicate(java.util.Optional<Boolean> forward, java.util.Optional<Boolean> backward, java.util.Optional<Boolean> left, java.util.Optional<Boolean> right, java.util.Optional<Boolean> jump, java.util.Optional<Boolean> sneak, java.util.Optional<Boolean> sprint) { this.forward = forward; this.backward = backward; this.left = left; this.right = right; this.jump = jump; this.sneak = sneak; this.sprint = sprint; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InputPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InputPredicate; } public java.util.Optional<Boolean> forward() { return this.forward; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InputPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InputPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InputPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/InputPredicate;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<Boolean> backward() { return this.backward; } public java.util.Optional<Boolean> left() { return this.left; } public java.util.Optional<Boolean> right() { return this.right; } public java.util.Optional<Boolean> jump() { return this.jump; } public java.util.Optional<Boolean> sneak() { return this.sneak; } public java.util.Optional<Boolean> sprint() { return this.sprint; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 18 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("forward").forGetter(InputPredicate::forward), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("backward").forGetter(InputPredicate::backward), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("left").forGetter(InputPredicate::left), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("right").forGetter(InputPredicate::right), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("jump").forGetter(InputPredicate::jump), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("sneak").forGetter(InputPredicate::sneak), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("sprint").forGetter(InputPredicate::sprint)).apply((com.mojang.datafixers.kinds.Applicative)i, InputPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.world.entity.player.Input input) {
/* 29 */     return (matches(this.forward, input.forward()) && 
/* 30 */       matches(this.backward, input.backward()) && 
/* 31 */       matches(this.left, input.left()) && 
/* 32 */       matches(this.right, input.right()) && 
/* 33 */       matches(this.jump, input.jump()) && 
/* 34 */       matches(this.sneak, input.shift()) && 
/* 35 */       matches(this.sprint, input.sprint()));
/*    */   }
/*    */   
/*    */   private boolean matches(java.util.Optional<Boolean> match, boolean value) {
/* 39 */     return (Boolean)match.<Boolean>map(b -> (b == value)).orElse(true);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/InputPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */