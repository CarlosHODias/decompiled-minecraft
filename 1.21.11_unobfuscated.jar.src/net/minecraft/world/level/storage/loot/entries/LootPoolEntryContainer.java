/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class LootPoolEntryContainer implements ComposableEntryContainer {
/*    */   protected final List<LootItemCondition> conditions;
/*    */   private final Predicate<LootContext> compositeCondition;
/*    */   
/*    */   protected LootPoolEntryContainer(List<LootItemCondition> conditions) {
/* 21 */     this.conditions = conditions;
/* 22 */     this.compositeCondition = Util.allOf(conditions);
/*    */   }
/*    */   
/*    */   protected static <T extends LootPoolEntryContainer> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 26 */     return i.group((App)
/* 27 */         LootItemCondition.DIRECT_CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(e -> e.conditions));
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 32 */     for (int i = 0; i < this.conditions.size(); i++) {
/* 33 */       ((LootItemCondition)this.conditions.get(i)).validate(output.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("conditions", i)));
/*    */     }
/*    */   }
/*    */   
/*    */   protected final boolean canRun(LootContext context) {
/* 38 */     return this.compositeCondition.test(context);
/*    */   }
/*    */   
/*    */   public abstract LootPoolEntryType getType();
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>> implements ConditionUserBuilder<T> {
/* 44 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public T when(LootItemCondition.Builder condition) {
/* 50 */       this.conditions.add(condition.build());
/* 51 */       return getThis();
/*    */     }
/*    */ 
/*    */     
/*    */     public final T unwrap() {
/* 56 */       return getThis();
/*    */     }
/*    */     
/*    */     protected List<LootItemCondition> getConditions() {
/* 60 */       return (List<LootItemCondition>)this.conditions.build();
/*    */     }
/*    */     
/*    */     public AlternativesEntry.Builder otherwise(Builder<?> other) {
/* 64 */       return new AlternativesEntry.Builder((Builder<?>[])new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     public EntryGroup.Builder append(Builder<?> other) {
/* 68 */       return new EntryGroup.Builder((Builder<?>[])new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     public SequentialEntry.Builder then(Builder<?> other) {
/* 72 */       return new SequentialEntry.Builder((Builder<?>[])new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     protected abstract T getThis();
/*    */     
/*    */     public abstract LootPoolEntryContainer build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */