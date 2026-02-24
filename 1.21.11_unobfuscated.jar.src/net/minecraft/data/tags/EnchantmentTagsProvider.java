/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ 
/*    */ public abstract class EnchantmentTagsProvider
/*    */   extends KeyTagProvider<Enchantment> {
/*    */   public EnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 18 */     super(output, Registries.ENCHANTMENT, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void tooltipOrder(HolderLookup.Provider registries, ResourceKey<Enchantment>... order) {
/* 22 */     tag(EnchantmentTags.TOOLTIP_ORDER).add(order);
/* 23 */     Set<ResourceKey<Enchantment>> set = Set.of(order);
/* 24 */     List<String> unlisted = (List<String>)registries.lookupOrThrow(Registries.ENCHANTMENT)
/* 25 */       .listElements()
/* 26 */       .filter(e -> !set.contains(e.unwrapKey().get()))
/* 27 */       .map(Holder::getRegisteredName)
/* 28 */       .collect(Collectors.toList());
/* 29 */     if (!unlisted.isEmpty())
/* 30 */       throw new IllegalStateException("Not all enchantments were registered for tooltip ordering. Missing: " + String.join(", ", unlisted)); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/EnchantmentTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */