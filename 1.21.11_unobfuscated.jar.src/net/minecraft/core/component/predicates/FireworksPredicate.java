/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ 
/*    */ public final class FireworksPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<Fireworks> {
/*    */   private final java.util.Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions;
/*    */   private final MinMaxBounds.Ints flightDuration;
/*    */   public static final com.mojang.serialization.Codec<FireworksPredicate> CODEC;
/*    */   
/* 15 */   public FireworksPredicate(java.util.Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions, MinMaxBounds.Ints flightDuration) { this.explosions = explosions; this.flightDuration = flightDuration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/FireworksPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworksPredicate; } public java.util.Optional<CollectionPredicate<FireworkExplosion, FireworkExplosionPredicate.FireworkPredicate>> explosions() { return this.explosions; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/FireworksPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/FireworksPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/FireworksPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/FireworksPredicate;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints flightDuration() { return this.flightDuration; }
/*    */ 
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CollectionPredicate.codec(FireworkExplosionPredicate.FireworkPredicate.CODEC).optionalFieldOf("explosions").forGetter(FireworksPredicate::explosions), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("flight_duration", MinMaxBounds.Ints.ANY).forGetter(FireworksPredicate::flightDuration)).apply((com.mojang.datafixers.kinds.Applicative)i, FireworksPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<Fireworks> componentType() {
/* 26 */     return net.minecraft.core.component.DataComponents.FIREWORKS;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Fireworks value) {
/* 31 */     if (this.explosions.isPresent() && !((CollectionPredicate)this.explosions.get()).test(value.explosions())) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     if (!this.flightDuration.matches(value.flightDuration())) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/FireworksPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */