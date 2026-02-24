/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeLootItemCondition implements LootItemCondition {
/*    */   protected final List<LootItemCondition> terms;
/*    */   private final Predicate<LootContext> composedPredicate;
/*    */   
/*    */   protected CompositeLootItemCondition(List<LootItemCondition> terms, Predicate<LootContext> composedPredicate) {
/* 21 */     this.terms = terms;
/* 22 */     this.composedPredicate = composedPredicate;
/*    */   }
/*    */   
/*    */   protected static <T extends CompositeLootItemCondition> MapCodec<T> createCodec(Function<List<LootItemCondition>, T> factory) {
/* 26 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)LootItemCondition.DIRECT_CODEC.listOf().fieldOf("terms").forGetter(())).apply((Applicative)i, factory));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static <T extends CompositeLootItemCondition> Codec<T> createInlineCodec(Function<List<LootItemCondition>, T> factory) {
/* 32 */     return LootItemCondition.DIRECT_CODEC.listOf().xmap(factory, condition -> condition.terms);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean test(LootContext context) {
/* 37 */     return this.composedPredicate.test(context);
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 42 */     super.validate(output);
/*    */     
/* 44 */     for (int i = 0; i < this.terms.size(); i++)
/* 45 */       ((LootItemCondition)this.terms.get(i)).validate(output.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("terms", i))); 
/*    */   }
/*    */   
/*    */   public static abstract class Builder
/*    */     implements LootItemCondition.Builder {
/* 50 */     private final ImmutableList.Builder<LootItemCondition> terms = ImmutableList.builder();
/*    */     
/*    */     protected Builder(LootItemCondition.Builder... terms) {
/* 53 */       for (LootItemCondition.Builder term : terms) {
/* 54 */         this.terms.add(term.build());
/*    */       }
/*    */     }
/*    */     
/*    */     public void addTerm(LootItemCondition.Builder term) {
/* 59 */       this.terms.add(term.build());
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemCondition build() {
/* 64 */       return create((List<LootItemCondition>)this.terms.build());
/*    */     }
/*    */     
/*    */     protected abstract LootItemCondition create(List<LootItemCondition> param1List);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/CompositeLootItemCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */