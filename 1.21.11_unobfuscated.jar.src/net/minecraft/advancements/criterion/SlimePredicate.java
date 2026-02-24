/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.monster.Slime;
/*    */ 
/*    */ public final class SlimePredicate extends Record implements EntitySubPredicate {
/*    */   private final MinMaxBounds.Ints size;
/*    */   public static final com.mojang.serialization.MapCodec<SlimePredicate> CODEC;
/*    */   
/* 11 */   public SlimePredicate(MinMaxBounds.Ints size) { this.size = size; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SlimePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SlimePredicate; } public MinMaxBounds.Ints size() { return this.size; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SlimePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SlimePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SlimePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/SlimePredicate;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("size", MinMaxBounds.Ints.ANY).forGetter(SlimePredicate::size)).apply((com.mojang.datafixers.kinds.Applicative)i, SlimePredicate::new)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public static SlimePredicate sized(MinMaxBounds.Ints size) {
/* 17 */     return new SlimePredicate(size);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.world.entity.Entity entity, net.minecraft.server.level.ServerLevel level, net.minecraft.world.phys.Vec3 position) {
/* 22 */     if (entity instanceof Slime) { Slime slime = (Slime)entity;
/* 23 */       return this.size.matches(slime.getSize()); }
/*    */     
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<SlimePredicate> codec() {
/* 30 */     return EntitySubPredicates.SLIME;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/SlimePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */