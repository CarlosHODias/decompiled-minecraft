/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AnyOfCondition extends CompositeLootItemCondition {
/*  9 */   public static final MapCodec<AnyOfCondition> CODEC = createCodec(AnyOfCondition::new);
/*    */   
/*    */   private AnyOfCondition(List<LootItemCondition> terms) {
/* 12 */     super(terms, Util.anyOf(terms));
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 17 */     return LootItemConditions.ANY_OF;
/*    */   }
/*    */   
/*    */   public static class Builder extends CompositeLootItemCondition.Builder {
/*    */     public Builder(LootItemCondition.Builder... terms) {
/* 22 */       super(terms);
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder or(LootItemCondition.Builder term) {
/* 27 */       addTerm(term);
/* 28 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     protected LootItemCondition create(List<LootItemCondition> terms) {
/* 33 */       return new AnyOfCondition(terms);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder anyOf(LootItemCondition.Builder... terms) {
/* 38 */     return new Builder(terms);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/predicates/AnyOfCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */