/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class AlternativesEntry extends CompositeEntryBase {
/* 14 */   public static final MapCodec<AlternativesEntry> CODEC = createCodec(AlternativesEntry::new);
/*    */   
/* 16 */   public static final ProblemReporter.Problem UNREACHABLE_PROBLEM = new ProblemReporter.Problem()
/*    */     {
/*    */       public String description() {
/* 19 */         return "Unreachable entry!";
/*    */       }
/*    */     };
/*    */   
/*    */   AlternativesEntry(List<LootPoolEntryContainer> children, List<LootItemCondition> conditions) {
/* 24 */     super(children, conditions);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 29 */     return LootPoolEntries.ALTERNATIVES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries) {
/* 34 */     switch (entries.size()) { case 0: case 1: case 2: default: break; }  return (context, output) -> {
/*    */         for (ComposableEntryContainer entry : (Iterable<ComposableEntryContainer>)entries) {
/*    */           if (entry.expand(context, output)) {
/*    */             return true;
/*    */           }
/*    */         } 
/*    */         return false;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 51 */     super.validate(context);
/*    */     
/* 53 */     for (int i = 0; i < this.children.size() - 1; i++) {
/*    */       
/* 55 */       if (((LootPoolEntryContainer)this.children.get(i)).conditions.isEmpty())
/* 56 */         context.reportProblem(UNREACHABLE_PROBLEM); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     extends LootPoolEntryContainer.Builder<Builder> {
/* 62 */     private final ImmutableList.Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
/*    */     
/*    */     public Builder(LootPoolEntryContainer.Builder<?>... entries) {
/* 65 */       for (LootPoolEntryContainer.Builder<?> entry : entries) {
/* 66 */         this.entries.add(entry.build());
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 72 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder otherwise(LootPoolEntryContainer.Builder<?> other) {
/* 77 */       this.entries.add(other.build());
/* 78 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootPoolEntryContainer build() {
/* 83 */       return new AlternativesEntry((List<LootPoolEntryContainer>)this.entries.build(), getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder alternatives(LootPoolEntryContainer.Builder<?>... entries) {
/* 88 */     return new Builder(entries);
/*    */   }
/*    */   
/*    */   public static <E> Builder alternatives(Collection<E> items, Function<E, LootPoolEntryContainer.Builder<?>> provider) {
/* 92 */     java.util.Objects.requireNonNull(provider); return new Builder((LootPoolEntryContainer.Builder<?>[])items.stream().map(provider::apply).toArray(x$0 -> new LootPoolEntryContainer.Builder[x$0]));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/AlternativesEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */