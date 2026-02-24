/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class TransmuteResult extends Record {
/*    */   private final Holder<Item> item;
/*    */   private final int count;
/*    */   private final DataComponentPatch components;
/*    */   private static final com.mojang.serialization.Codec<TransmuteResult> FULL_CODEC;
/*    */   public static final com.mojang.serialization.Codec<TransmuteResult> CODEC;
/*    */   
/* 17 */   public TransmuteResult(Holder<Item> item, int count, DataComponentPatch components) { this.item = item; this.count = count; this.components = components; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/TransmuteResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/TransmuteResult; } public Holder<Item> item() { return this.item; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/TransmuteResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/TransmuteResult; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/TransmuteResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/TransmuteResult;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public int count() { return this.count; } public DataComponentPatch components() { return this.components; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 22 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Item.CODEC.fieldOf("id").forGetter(TransmuteResult::item), (App)net.minecraft.util.ExtraCodecs.intRange(1, 99).optionalFieldOf("count", 1).forGetter(TransmuteResult::count), (App)DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(TransmuteResult::components)).apply((com.mojang.datafixers.kinds.Applicative)i, TransmuteResult::new));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     CODEC = com.mojang.serialization.Codec.withAlternative(FULL_CODEC, Item.CODEC, item -> new TransmuteResult((Item)item.value())).validate(TransmuteResult::validate);
/*    */   }
/* 33 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, TransmuteResult> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Item.STREAM_CODEC, TransmuteResult::item, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, TransmuteResult::count, DataComponentPatch.STREAM_CODEC, TransmuteResult::components, TransmuteResult::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static com.mojang.serialization.DataResult<TransmuteResult> validate(TransmuteResult result) {
/* 41 */     return ItemStack.validateStrict(new ItemStack(result.item, result.count, result.components)).map(ignored -> result);
/*    */   }
/*    */   
/*    */   public TransmuteResult(Item item) {
/* 45 */     this((Holder<Item>)item.builtInRegistryHolder(), 1, DataComponentPatch.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack apply(ItemStack input) {
/* 50 */     ItemStack result = input.transmuteCopy((net.minecraft.world.level.ItemLike)this.item.value(), this.count);
/* 51 */     result.applyComponents(this.components);
/* 52 */     return result;
/*    */   }
/*    */   
/*    */   public boolean isResultUnchanged(ItemStack input) {
/* 56 */     ItemStack result = apply(input);
/*    */     
/* 58 */     return (result.getCount() == 1 && ItemStack.isSameItemSameComponents(input, result));
/*    */   }
/*    */   
/*    */   public net.minecraft.world.item.crafting.display.SlotDisplay display() {
/* 62 */     return (net.minecraft.world.item.crafting.display.SlotDisplay)new net.minecraft.world.item.crafting.display.SlotDisplay.ItemStackSlotDisplay(new ItemStack(this.item, this.count, this.components));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/TransmuteResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */