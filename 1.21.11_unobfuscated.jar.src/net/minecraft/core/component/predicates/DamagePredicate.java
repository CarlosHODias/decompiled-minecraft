/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ 
/*    */ public final class DamagePredicate extends Record implements DataComponentPredicate {
/*    */   private final net.minecraft.advancements.criterion.MinMaxBounds.Ints durability;
/*    */   private final net.minecraft.advancements.criterion.MinMaxBounds.Ints damage;
/*    */   public static final com.mojang.serialization.Codec<DamagePredicate> CODEC;
/*    */   
/*  9 */   public DamagePredicate(net.minecraft.advancements.criterion.MinMaxBounds.Ints durability, net.minecraft.advancements.criterion.MinMaxBounds.Ints damage) { this.durability = durability; this.damage = damage; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/DamagePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate; } public net.minecraft.advancements.criterion.MinMaxBounds.Ints durability() { return this.durability; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/DamagePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/DamagePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/DamagePredicate;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.advancements.criterion.MinMaxBounds.Ints damage() { return this.damage; } static {
/* 10 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.advancements.criterion.MinMaxBounds.Ints.CODEC.optionalFieldOf("durability", net.minecraft.advancements.criterion.MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::durability), (com.mojang.datafixers.kinds.App)net.minecraft.advancements.criterion.MinMaxBounds.Ints.CODEC.optionalFieldOf("damage", net.minecraft.advancements.criterion.MinMaxBounds.Ints.ANY).forGetter(DamagePredicate::damage)).apply((com.mojang.datafixers.kinds.Applicative)i, DamagePredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.core.component.DataComponentGetter components) {
/* 17 */     Integer damage = (Integer)components.get(net.minecraft.core.component.DataComponents.DAMAGE);
/* 18 */     if (damage == null) {
/* 19 */       return false;
/*    */     }
/*    */     
/* 22 */     int maxDamage = (Integer)components.getOrDefault(net.minecraft.core.component.DataComponents.MAX_DAMAGE, 0);
/*    */     
/* 24 */     if (!this.durability.matches(maxDamage - damage)) {
/* 25 */       return false;
/*    */     }
/* 27 */     if (!this.damage.matches(damage)) {
/* 28 */       return false;
/*    */     }
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public static DamagePredicate durability(net.minecraft.advancements.criterion.MinMaxBounds.Ints range) {
/* 34 */     return new DamagePredicate(range, net.minecraft.advancements.criterion.MinMaxBounds.Ints.ANY);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/DamagePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */