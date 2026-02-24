/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ 
/*    */ public final class FireworkPredicate extends Record implements java.util.function.Predicate<FireworkExplosion> {
/*    */   private final Optional<FireworkExplosion.Shape> shape;
/*    */   private final Optional<Boolean> twinkle;
/*    */   private final Optional<Boolean> trail;
/*    */   public static final com.mojang.serialization.Codec<FireworkPredicate> CODEC;
/*    */   
/* 14 */   public FireworkPredicate(Optional<FireworkExplosion.Shape> shape, Optional<Boolean> twinkle, Optional<Boolean> trail) { this.shape = shape; this.twinkle = twinkle; this.trail = trail; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate; } public Optional<FireworkExplosion.Shape> shape() { return this.shape; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Boolean> twinkle() { return this.twinkle; } public Optional<Boolean> trail() { return this.trail; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)FireworkExplosion.Shape.CODEC.optionalFieldOf("shape").forGetter(FireworkPredicate::shape), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("has_twinkle").forGetter(FireworkPredicate::twinkle), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("has_trail").forGetter(FireworkPredicate::trail)).apply((com.mojang.datafixers.kinds.Applicative)i, FireworkPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(FireworkExplosion fireworkExplosion) {
/* 27 */     if (this.shape.isPresent() && this.shape.get() != fireworkExplosion.shape()) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     if (this.twinkle.isPresent() && (Boolean)this.twinkle.get() != fireworkExplosion.hasTwinkle()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     if (this.trail.isPresent() && (Boolean)this.trail.get() != fireworkExplosion.hasTrail()) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/FireworkExplosionPredicate$FireworkPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */