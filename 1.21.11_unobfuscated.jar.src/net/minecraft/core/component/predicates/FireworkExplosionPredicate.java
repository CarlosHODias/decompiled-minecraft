/*    */ package net.minecraft.core.component.predicates;
/*    */ public final class FireworkExplosionPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<net.minecraft.world.item.component.FireworkExplosion> {
/*    */   private final FireworkPredicate predicate;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate;
/*    */   }
/*    */   
/* 13 */   public FireworkExplosionPredicate(FireworkPredicate predicate) { this.predicate = predicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public FireworkPredicate predicate() { return this.predicate; } public static final class FireworkPredicate extends Record implements java.util.function.Predicate<net.minecraft.world.item.component.FireworkExplosion> { private final java.util.Optional<net.minecraft.world.item.component.FireworkExplosion.Shape> shape; private final java.util.Optional<Boolean> twinkle; private final java.util.Optional<Boolean> trail; public static final com.mojang.serialization.Codec<FireworkPredicate> CODEC;
/* 14 */     public FireworkPredicate(java.util.Optional<net.minecraft.world.item.component.FireworkExplosion.Shape> shape, java.util.Optional<Boolean> twinkle, java.util.Optional<Boolean> trail) { this.shape = shape; this.twinkle = twinkle; this.trail = trail; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;
/* 14 */       //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<net.minecraft.world.item.component.FireworkExplosion.Shape> shape() { return this.shape; } public java.util.Optional<Boolean> twinkle() { return this.twinkle; } public java.util.Optional<Boolean> trail() { return this.trail; }
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 19 */       CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.item.component.FireworkExplosion.Shape.CODEC.optionalFieldOf("shape").forGetter(FireworkPredicate::shape), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("has_twinkle").forGetter(FireworkPredicate::twinkle), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("has_trail").forGetter(FireworkPredicate::trail)).apply((com.mojang.datafixers.kinds.Applicative)i, FireworkPredicate::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean test(net.minecraft.world.item.component.FireworkExplosion fireworkExplosion) {
/* 27 */       if (this.shape.isPresent() && this.shape.get() != fireworkExplosion.shape()) {
/* 28 */         return false;
/*    */       }
/*    */       
/* 31 */       if (this.twinkle.isPresent() && (Boolean)this.twinkle.get() != fireworkExplosion.hasTwinkle()) {
/* 32 */         return false;
/*    */       }
/*    */       
/* 35 */       if (this.trail.isPresent() && (Boolean)this.trail.get() != fireworkExplosion.hasTrail()) {
/* 36 */         return false;
/*    */       }
/*    */       
/* 39 */       return true;
/*    */     } }
/*    */ 
/*    */   
/* 43 */   public static final com.mojang.serialization.Codec<FireworkExplosionPredicate> CODEC = FireworkPredicate.CODEC.xmap(FireworkExplosionPredicate::new, FireworkExplosionPredicate::predicate);
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<net.minecraft.world.item.component.FireworkExplosion> componentType() {
/* 47 */     return net.minecraft.core.component.DataComponents.FIREWORK_EXPLOSION;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(net.minecraft.world.item.component.FireworkExplosion value) {
/* 52 */     return this.predicate.test(value);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/FireworkExplosionPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */