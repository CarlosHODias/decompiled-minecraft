/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ 
/*    */ public final class LightPredicate extends Record {
/*    */   private final MinMaxBounds.Ints composite;
/*    */   public static final com.mojang.serialization.Codec<LightPredicate> CODEC;
/*    */   
/*  8 */   public LightPredicate(MinMaxBounds.Ints composite) { this.composite = composite; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LightPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LightPredicate; } public MinMaxBounds.Ints composite() { return this.composite; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LightPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LightPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LightPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/LightPredicate;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)MinMaxBounds.Ints.CODEC.optionalFieldOf("light", MinMaxBounds.Ints.ANY).forGetter(LightPredicate::composite)).apply((com.mojang.datafixers.kinds.Applicative)i, LightPredicate::new)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.server.level.ServerLevel level, net.minecraft.core.BlockPos pos) {
/* 14 */     if (!level.isLoaded(pos)) {
/* 15 */       return false;
/*    */     }
/* 17 */     if (!this.composite.matches(level.getMaxLocalRawBrightness(pos))) {
/* 18 */       return false;
/*    */     }
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 24 */     private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;
/*    */     
/*    */     public static Builder light() {
/* 27 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder setComposite(MinMaxBounds.Ints composite) {
/* 31 */       this.composite = composite;
/* 32 */       return this;
/*    */     }
/*    */     
/*    */     public LightPredicate build() {
/* 36 */       return new LightPredicate(this.composite);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/LightPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */