/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public final class ItemCost extends Record {
/*    */   private final Holder<Item> item;
/*    */   private final int count;
/*    */   private final DataComponentExactPredicate components;
/*    */   private final ItemStack itemStack;
/*    */   public static final com.mojang.serialization.Codec<ItemCost> CODEC;
/*    */   
/* 18 */   public ItemCost(Holder<Item> item, int count, DataComponentExactPredicate components, ItemStack itemStack) { this.item = item; this.count = count; this.components = components; this.itemStack = itemStack; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/trading/ItemCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/item/trading/ItemCost; } public Holder<Item> item() { return this.item; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/trading/ItemCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/trading/ItemCost; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/trading/ItemCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/trading/ItemCost;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public int count() { return this.count; } public DataComponentExactPredicate components() { return this.components; } public ItemStack itemStack() { return this.itemStack; }
/*    */    static {
/* 20 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Item.CODEC.fieldOf("id").forGetter(ItemCost::item), (App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("count").orElse(1).forGetter(ItemCost::count), (App)DataComponentExactPredicate.CODEC.optionalFieldOf("components", DataComponentExactPredicate.EMPTY).forGetter(ItemCost::components)).apply((com.mojang.datafixers.kinds.Applicative)i, ItemCost::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ItemCost> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Item.STREAM_CODEC, ItemCost::item, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, ItemCost::count, DataComponentExactPredicate.STREAM_CODEC, ItemCost::components, ItemCost::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, java.util.Optional<ItemCost>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional);
/*    */   
/*    */   public ItemCost(ItemLike item) {
/* 35 */     this(item, 1);
/*    */   }
/*    */   
/*    */   public ItemCost(ItemLike item, int count) {
/* 39 */     this((Holder<Item>)item.asItem().builtInRegistryHolder(), count, DataComponentExactPredicate.EMPTY);
/*    */   }
/*    */   
/*    */   public ItemCost(Holder<Item> item, int count, DataComponentExactPredicate components) {
/* 43 */     this(item, count, components, createStack(item, count, components));
/*    */   }
/*    */   
/*    */   public ItemCost withComponents(java.util.function.UnaryOperator<DataComponentExactPredicate.Builder> components) {
/* 47 */     return new ItemCost(this.item, this.count, ((DataComponentExactPredicate.Builder)components.apply(DataComponentExactPredicate.builder())).build());
/*    */   }
/*    */   
/*    */   private static ItemStack createStack(Holder<Item> item, int count, DataComponentExactPredicate components) {
/* 51 */     return new ItemStack(item, count, components.asPatch());
/*    */   }
/*    */   
/*    */   public boolean test(ItemStack itemStack) {
/* 55 */     return (itemStack.is(this.item) && this.components.test((net.minecraft.core.component.DataComponentGetter)itemStack));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/trading/ItemCost.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */