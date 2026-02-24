/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ContextAwarePredicate {
/*    */   static {
/* 15 */     CODEC = LootItemCondition.DIRECT_CODEC.listOf().xmap(ContextAwarePredicate::new, predicate -> predicate.conditions);
/*    */   }
/*    */   
/*    */   public static final Codec<ContextAwarePredicate> CODEC;
/*    */   
/*    */   ContextAwarePredicate(List<LootItemCondition> conditions) {
/* 21 */     this.conditions = conditions;
/* 22 */     this.compositePredicates = Util.allOf(conditions);
/*    */   }
/*    */   private final List<LootItemCondition> conditions; private final Predicate<LootContext> compositePredicates;
/*    */   public static ContextAwarePredicate create(LootItemCondition... conditions) {
/* 26 */     return new ContextAwarePredicate(List.of(conditions));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext context) {
/* 30 */     return this.compositePredicates.test(context);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 34 */     for (int i = 0; i < this.conditions.size(); i++) {
/* 35 */       LootItemCondition condition = this.conditions.get(i);
/* 36 */       condition.validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedPathElement(i)));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/ContextAwarePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */