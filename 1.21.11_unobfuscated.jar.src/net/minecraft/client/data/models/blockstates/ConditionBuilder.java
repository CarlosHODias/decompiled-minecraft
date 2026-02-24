/*    */ package net.minecraft.client.data.models.blockstates;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.renderer.block.model.multipart.Condition;
/*    */ import net.minecraft.client.renderer.block.model.multipart.KeyValueCondition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ConditionBuilder {
/* 12 */   private final ImmutableMap.Builder<String, KeyValueCondition.Terms> terms = ImmutableMap.builder();
/*    */   
/*    */   private <T extends Comparable<T>> void putValue(Property<T> property, KeyValueCondition.Terms term) {
/* 15 */     this.terms.put(property.getName(), term);
/*    */   }
/*    */   
/*    */   public final <T extends Comparable<T>> ConditionBuilder term(Property<T> property, T value) {
/* 19 */     putValue(property, new KeyValueCondition.Terms(List.of(new KeyValueCondition.Term(property.getName((Comparable)value), false))));
/* 20 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   @SafeVarargs
/*    */   public final <T extends Comparable<T>> ConditionBuilder term(Property<T> property, T value, T... values) {
/* 26 */     Objects.requireNonNull(property); List<KeyValueCondition.Term> terms = Stream.concat(Stream.of(value), Stream.of((Object[])values)).map(property::getName)
/* 27 */       .sorted()
/* 28 */       .distinct()
/* 29 */       .map(v -> new KeyValueCondition.Term(v, false))
/* 30 */       .toList();
/* 31 */     putValue(property, new KeyValueCondition.Terms(terms));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public final <T extends Comparable<T>> ConditionBuilder negatedTerm(Property<T> property, T value) {
/* 36 */     putValue(property, new KeyValueCondition.Terms(List.of(new KeyValueCondition.Term(property.getName((Comparable)value), true))));
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public Condition build() {
/* 41 */     return (Condition)new KeyValueCondition((Map)this.terms.buildOrThrow());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/blockstates/ConditionBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */