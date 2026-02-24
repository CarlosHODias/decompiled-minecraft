/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NumberProviders.CODEC.fieldOf("levels").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(()))).apply((Applicative)i, EnchantWithLevelsFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<EnchantWithLevelsFunction> CODEC;
/*    */   private final NumberProvider levels;
/*    */   private final Optional<HolderSet<Enchantment>> options;
/*    */   
/*    */   private EnchantWithLevelsFunction(List<LootItemCondition> predicates, NumberProvider levels, Optional<HolderSet<Enchantment>> options) {
/* 35 */     super(predicates);
/* 36 */     this.levels = levels;
/* 37 */     this.options = options;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<EnchantWithLevelsFunction> getType() {
/* 42 */     return LootItemFunctions.ENCHANT_WITH_LEVELS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 47 */     return this.levels.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 52 */     RandomSource random = context.getRandom();
/* 53 */     RegistryAccess registryAccess = context.getLevel().registryAccess();
/* 54 */     return net.minecraft.world.item.enchantment.EnchantmentHelper.enchantItem(random, itemStack, this.levels.getInt(context), registryAccess, this.options);
/*    */   }
/*    */   
/*    */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*    */     private final NumberProvider levels;
/* 59 */     private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*    */     
/*    */     public Builder(NumberProvider levels) {
/* 62 */       this.levels = levels;
/*    */     }
/*    */ 
/*    */     
/*    */     protected Builder getThis() {
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public Builder fromOptions(HolderSet<Enchantment> tag) {
/* 71 */       this.options = Optional.of(tag);
/* 72 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public LootItemFunction build() {
/* 77 */       return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder enchantWithLevels(HolderLookup.Provider registries, NumberProvider levels) {
/* 82 */     return new Builder(levels).fromOptions((HolderSet<Enchantment>)registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */