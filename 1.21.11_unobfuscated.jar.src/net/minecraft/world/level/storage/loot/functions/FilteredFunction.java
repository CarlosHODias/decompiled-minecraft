/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class FilteredFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ItemPredicate.CODEC.fieldOf("item_filter").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.optionalFieldOf("on_pass").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.optionalFieldOf("on_fail").forGetter(()))).apply((Applicative)i, FilteredFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<FilteredFunction> CODEC;
/*    */   
/*    */   private final ItemPredicate filter;
/*    */   private final Optional<LootItemFunction> onPass;
/*    */   private final Optional<LootItemFunction> onFail;
/*    */   
/*    */   private FilteredFunction(List<LootItemCondition> predicates, ItemPredicate filter, Optional<LootItemFunction> onPass, Optional<LootItemFunction> onFail) {
/* 27 */     super(predicates);
/* 28 */     this.filter = filter;
/* 29 */     this.onPass = onPass;
/* 30 */     this.onFail = onFail;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<FilteredFunction> getType() {
/* 35 */     return LootItemFunctions.FILTERED;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 40 */     Optional<LootItemFunction> function = this.filter.test(itemStack) ? this.onPass : this.onFail;
/* 41 */     if (function.isPresent()) {
/* 42 */       return ((LootItemFunction)function.get()).apply(itemStack, context);
/*    */     }
/* 44 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 49 */     super.validate(context);
/* 50 */     this.onPass.ifPresent(f -> f.validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("on_pass"))));
/* 51 */     this.onFail.ifPresent(f -> f.validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("on_fail"))));
/*    */   }
/*    */   
/*    */   public static Builder filtered(ItemPredicate predicate) {
/* 55 */     return new Builder(predicate);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final ItemPredicate itemPredicate;
/* 60 */     private Optional<LootItemFunction> onPass = Optional.empty();
/* 61 */     private Optional<LootItemFunction> onFail = Optional.empty();
/*    */     
/*    */     private Builder(ItemPredicate itemPredicate) {
/* 64 */       this.itemPredicate = itemPredicate;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 69 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onPass(Optional<LootItemFunction> onPass) {
/* 73 */       this.onPass = onPass;
/* 74 */       return this;
/*    */     }
/*    */     
/*    */     public Builder onFail(Optional<LootItemFunction> onFail) {
/* 78 */       this.onFail = onFail;
/* 79 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 84 */       return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/FilteredFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */