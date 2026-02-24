/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class BredAnimalsTrigger extends SimpleCriterionTrigger<BredAnimalsTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 18 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, Animal parent, Animal partner, AgeableMob child) {
/* 22 */     LootContext parentContext = EntityPredicate.createContext(player, (Entity)parent);
/* 23 */     LootContext partnerContext = EntityPredicate.createContext(player, (Entity)partner);
/* 24 */     LootContext childContext = (child != null) ? EntityPredicate.createContext(player, (Entity)child) : null;
/*    */     
/* 26 */     trigger(player, t -> t.matches(parentContext, partnerContext, childContext));
/*    */   }
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Optional<ContextAwarePredicate> parent; private final Optional<ContextAwarePredicate> partner; private final Optional<ContextAwarePredicate> child; public static final Codec<TriggerInstance> CODEC;
/* 29 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> parent, Optional<ContextAwarePredicate> partner, Optional<ContextAwarePredicate> child) { this.player = player; this.parent = parent; this.partner = partner; this.child = child; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 29 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/BredAnimalsTrigger$TriggerInstance;
/* 29 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<ContextAwarePredicate> parent() { return this.parent; } public Optional<ContextAwarePredicate> partner() { return this.partner; } public Optional<ContextAwarePredicate> child() { return this.child; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 35 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("parent").forGetter(TriggerInstance::parent), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("partner").forGetter(TriggerInstance::partner), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("child").forGetter(TriggerInstance::child)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static Criterion<TriggerInstance> bredAnimals() {
/* 43 */       return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> bredAnimals(EntityPredicate.Builder child) {
/* 47 */       return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(child))));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> bredAnimals(Optional<EntityPredicate> parent1, Optional<EntityPredicate> parent2, Optional<EntityPredicate> child) {
/* 51 */       return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(parent1), EntityPredicate.wrap(parent2), EntityPredicate.wrap(child)));
/*    */     }
/*    */     
/*    */     public boolean matches(LootContext parent, LootContext partner, LootContext child) {
/* 55 */       if (this.child.isPresent() && (child == null || !((ContextAwarePredicate)this.child.get()).matches(child))) {
/* 56 */         return false;
/*    */       }
/*    */       
/* 59 */       return ((matches(this.parent, parent) && matches(this.partner, partner)) || (matches(this.parent, partner) && matches(this.partner, parent)));
/*    */     }
/*    */     
/*    */     private static boolean matches(Optional<ContextAwarePredicate> predicate, LootContext context) {
/* 63 */       return (predicate.isEmpty() || ((ContextAwarePredicate)predicate.get()).matches(context));
/*    */     }
/*    */ 
/*    */     
/*    */     public void validate(CriterionValidator validator) {
/* 68 */       super.validate(validator);
/* 69 */       validator.validateEntity(this.parent, "parent");
/* 70 */       validator.validateEntity(this.partner, "partner");
/* 71 */       validator.validateEntity(this.child, "child");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/BredAnimalsTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */