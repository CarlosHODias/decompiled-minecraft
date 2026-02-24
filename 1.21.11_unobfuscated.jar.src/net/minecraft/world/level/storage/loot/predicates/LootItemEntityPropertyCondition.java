/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.EntityPredicate;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LootItemEntityPropertyCondition extends Record implements LootItemCondition {
/*    */   private final Optional<EntityPredicate> predicate;
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   public static final com.mojang.serialization.MapCodec<LootItemEntityPropertyCondition> CODEC;
/*    */   
/* 15 */   public LootItemEntityPropertyCondition(Optional<EntityPredicate> predicate, LootContext.EntityTarget entityTarget) { this.predicate = predicate; this.entityTarget = entityTarget; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition; } public Optional<EntityPredicate> predicate() { return this.predicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public LootContext.EntityTarget entityTarget() { return this.entityTarget; }
/*    */ 
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(LootItemEntityPropertyCondition::predicate), (App)LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(LootItemEntityPropertyCondition::entityTarget)).apply((com.mojang.datafixers.kinds.Applicative)i, LootItemEntityPropertyCondition::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 26 */     return LootItemConditions.ENTITY_PROPERTIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 31 */     return java.util.Set.of(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, this.entityTarget.contextParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 36 */     net.minecraft.world.entity.Entity entity = (net.minecraft.world.entity.Entity)context.getOptionalParameter(this.entityTarget.contextParam());
/* 37 */     Vec3 pos = (Vec3)context.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN);
/* 38 */     return (this.predicate.isEmpty() || ((EntityPredicate)this.predicate.get()).matches(context.getLevel(), pos, entity));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder entityPresent(LootContext.EntityTarget target) {
/* 42 */     return hasProperties(target, EntityPredicate.Builder.entity());
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget target, EntityPredicate.Builder predicate) {
/* 46 */     return () -> new LootItemEntityPropertyCondition(Optional.of(predicate.build()), target);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget target, EntityPredicate predicate) {
/* 50 */     return () -> new LootItemEntityPropertyCondition(Optional.of(predicate), target);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */