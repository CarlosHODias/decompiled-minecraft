/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class LootItemConditionalFunction implements LootItemFunction {
/*    */   protected final List<LootItemCondition> predicates;
/*    */   private final Predicate<LootContext> compositePredicates;
/*    */   
/*    */   protected LootItemConditionalFunction(List<LootItemCondition> predicates) {
/* 23 */     this.predicates = predicates;
/* 24 */     this.compositePredicates = Util.allOf(predicates);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static <T extends LootItemConditionalFunction> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 31 */     return i.group((App)
/* 32 */         LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(f -> f.predicates));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final ItemStack apply(ItemStack itemStack, LootContext context) {
/* 38 */     return this.compositePredicates.test(context) ? run(itemStack, context) : itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 45 */     super.validate(context);
/*    */     
/* 47 */     for (int i = 0; i < this.predicates.size(); i++)
/* 48 */       ((LootItemCondition)this.predicates.get(i)).validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("conditions", i))); 
/*    */   }
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>>
/*    */     implements LootItemFunction.Builder, ConditionUserBuilder<T> {
/* 53 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */ 
/*    */     
/*    */     public T when(LootItemCondition.Builder condition) {
/* 57 */       this.conditions.add(condition.build());
/* 58 */       return getThis();
/*    */     }
/*    */ 
/*    */     
/*    */     public final T unwrap() {
/* 63 */       return getThis();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected List<LootItemCondition> getConditions() {
/* 69 */       return (List<LootItemCondition>)this.conditions.build();
/*    */     }
/*    */     
/*    */     protected abstract T getThis();
/*    */   }
/*    */   
/*    */   private static final class DummyBuilder extends Builder<DummyBuilder> {
/*    */     public DummyBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) {
/* 77 */       this.constructor = constructor;
/*    */     }
/*    */     private final Function<List<LootItemCondition>, LootItemFunction> constructor;
/*    */     
/*    */     protected DummyBuilder getThis() {
/* 82 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 87 */       return this.constructor.apply(getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   protected static Builder<?> simpleBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) {
/* 92 */     return new DummyBuilder(constructor);
/*    */   }
/*    */   
/*    */   public abstract LootItemFunctionType<? extends LootItemConditionalFunction> getType();
/*    */   
/*    */   protected abstract ItemStack run(ItemStack paramItemStack, LootContext paramLootContext);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */