/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AllOfCondition extends CompositeLootItemCondition {
/* 10 */   public static final MapCodec<AllOfCondition> CODEC = createCodec(AllOfCondition::new);
/* 11 */   public static final Codec<AllOfCondition> INLINE_CODEC = createInlineCodec(AllOfCondition::new);
/*    */   
/*    */   private AllOfCondition(List<LootItemCondition> terms) {
/* 14 */     super(terms, Util.allOf(terms));
/*    */   }
/*    */   
/*    */   public static AllOfCondition allOf(List<LootItemCondition> terms) {
/* 18 */     return new AllOfCondition(List.copyOf(terms));
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 23 */     return LootItemConditions.ALL_OF;
/*    */   }
/*    */   
/*    */   public static class Builder extends CompositeLootItemCondition.Builder {
/*    */     public Builder(LootItemCondition.Builder... terms) {
/* 28 */       super(terms);
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder and(LootItemCondition.Builder term) {
/* 33 */       addTerm(term);
/* 34 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     protected LootItemCondition create(List<LootItemCondition> terms) {
/* 39 */       return new AllOfCondition(terms);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder allOf(LootItemCondition.Builder... terms) {
/* 44 */     return new Builder(terms);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/AllOfCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */