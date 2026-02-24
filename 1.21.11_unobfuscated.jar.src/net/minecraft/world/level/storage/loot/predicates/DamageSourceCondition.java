/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.DamageSourcePredicate;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class DamageSourceCondition extends Record implements LootItemCondition {
/*    */   private final Optional<DamageSourcePredicate> predicate;
/*    */   public static final com.mojang.serialization.MapCodec<DamageSourceCondition> CODEC;
/*    */   
/* 15 */   public DamageSourceCondition(Optional<DamageSourcePredicate> predicate) { this.predicate = predicate; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition; } public Optional<DamageSourcePredicate> predicate() { return this.predicate; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/DamageSourceCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)DamageSourcePredicate.CODEC.optionalFieldOf("predicate").forGetter(DamageSourceCondition::predicate)).apply((com.mojang.datafixers.kinds.Applicative)i, DamageSourceCondition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 24 */     return LootItemConditions.DAMAGE_SOURCE_PROPERTIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 29 */     return java.util.Set.of(LootContextParams.ORIGIN, LootContextParams.DAMAGE_SOURCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 34 */     DamageSource damageSource = (DamageSource)context.getOptionalParameter(LootContextParams.DAMAGE_SOURCE);
/* 35 */     Vec3 pos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/* 36 */     if (pos == null || damageSource == null) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     return (this.predicate.isEmpty() || ((DamageSourcePredicate)this.predicate.get()).matches(context.getLevel(), pos, damageSource));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasDamageSource(DamageSourcePredicate.Builder builder) {
/* 44 */     return () -> new DamageSourceCondition(Optional.of(builder.build()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/DamageSourceCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */