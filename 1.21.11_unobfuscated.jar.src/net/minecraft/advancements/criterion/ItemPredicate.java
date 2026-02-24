/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public final class ItemPredicate extends Record implements java.util.function.Predicate<ItemStack> {
/*    */   private final Optional<HolderSet<Item>> items;
/*    */   private final MinMaxBounds.Ints count;
/*    */   private final DataComponentMatchers components;
/*    */   public static final com.mojang.serialization.Codec<ItemPredicate> CODEC;
/*    */   
/* 17 */   public ItemPredicate(Optional<HolderSet<Item>> items, MinMaxBounds.Ints count, DataComponentMatchers components) { this.items = items; this.count = count; this.components = components; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/ItemPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemPredicate; } public Optional<HolderSet<Item>> items() { return this.items; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/ItemPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/ItemPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/ItemPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/ItemPredicate;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints count() { return this.count; } public DataComponentMatchers components() { return this.components; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.ITEM).optionalFieldOf("items").forGetter(ItemPredicate::items), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("count", MinMaxBounds.Ints.ANY).forGetter(ItemPredicate::count), (App)DataComponentMatchers.CODEC.forGetter(ItemPredicate::components)).apply((com.mojang.datafixers.kinds.Applicative)i, ItemPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(ItemStack itemStack) {
/* 30 */     if (this.items.isPresent() && !itemStack.is(this.items.get())) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (!this.count.matches(itemStack.getCount())) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (!this.components.test((net.minecraft.core.component.DataComponentGetter)itemStack)) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 45 */     private Optional<HolderSet<Item>> items = Optional.empty();
/* 46 */     private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
/* 47 */     private DataComponentMatchers components = DataComponentMatchers.ANY;
/*    */     
/*    */     public static Builder item() {
/* 50 */       return new Builder();
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder of(net.minecraft.core.HolderGetter<Item> lookup, ItemLike... items) {
/* 55 */       this.items = Optional.of(HolderSet.direct(i -> i.asItem().builtInRegistryHolder(), (Object[])items));
/* 56 */       return this;
/*    */     }
/*    */     
/*    */     public Builder of(net.minecraft.core.HolderGetter<Item> lookup, net.minecraft.tags.TagKey<Item> tag) {
/* 60 */       this.items = Optional.of(lookup.getOrThrow(tag));
/* 61 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withCount(MinMaxBounds.Ints count) {
/* 65 */       this.count = count;
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withComponents(DataComponentMatchers components) {
/* 70 */       this.components = components;
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     public ItemPredicate build() {
/* 75 */       return new ItemPredicate(this.items, this.count, this.components);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/ItemPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */