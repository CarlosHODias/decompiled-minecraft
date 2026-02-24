/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetEnchantmentsFunction extends LootItemConditionalFunction {
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Codec.unboundedMap(Enchantment.CODEC, NumberProviders.CODEC).optionalFieldOf("enchantments", Map.of()).forGetter(()), (App)Codec.BOOL.fieldOf("add").orElse(false).forGetter(()))).apply((Applicative)i, SetEnchantmentsFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetEnchantmentsFunction> CODEC;
/*    */   private final Map<Holder<Enchantment>, NumberProvider> enchantments;
/*    */   private final boolean add;
/*    */   
/*    */   private SetEnchantmentsFunction(List<LootItemCondition> predicates, Map<Holder<Enchantment>, NumberProvider> enchantments, boolean add) {
/* 34 */     super(predicates);
/* 35 */     this.enchantments = Map.copyOf(enchantments);
/* 36 */     this.add = add;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetEnchantmentsFunction> getType() {
/* 41 */     return LootItemFunctions.SET_ENCHANTMENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 46 */     return (Set<ContextKey<?>>)this.enchantments.values().stream().flatMap(m -> m.getReferencedContextParams().stream()).collect(com.google.common.collect.ImmutableSet.toImmutableSet());
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 51 */     if (itemStack.is(Items.BOOK)) {
/* 52 */       itemStack = itemStack.transmuteCopy((net.minecraft.world.level.ItemLike)Items.ENCHANTED_BOOK);
/*    */     }
/*    */     
/* 55 */     net.minecraft.world.item.enchantment.EnchantmentHelper.updateEnchantments(itemStack, enchantments -> {
/*    */           if (this.add) {
/*    */             this.enchantments.forEach(());
/*    */           } else {
/*    */             this.enchantments.forEach(());
/*    */           } 
/*    */         });
/* 62 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 66 */     private final ImmutableMap.Builder<Holder<Enchantment>, NumberProvider> enchantments = ImmutableMap.builder();
/*    */     private final boolean add;
/*    */     
/*    */     public Builder() {
/* 70 */       this(false);
/*    */     }
/*    */     
/*    */     public Builder(boolean add) {
/* 74 */       this.add = add;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 79 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withEnchantment(Holder<Enchantment> enchantment, NumberProvider levelProvider) {
/* 83 */       this.enchantments.put(enchantment, levelProvider);
/* 84 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 89 */       return new SetEnchantmentsFunction(getConditions(), (Map<Holder<Enchantment>, NumberProvider>)this.enchantments.build(), this.add);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetEnchantmentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */