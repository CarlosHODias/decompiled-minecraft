/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public final class Ingredient implements java.util.function.Predicate<ItemStack>, StackedContents.IngredientInfo<Holder<Item>> {
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC;
/*     */   
/*     */   static {
/*  27 */     CONTENTS_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM).map(Ingredient::new, i -> i.values);
/*     */ 
/*     */     
/*  30 */     OPTIONAL_CONTENTS_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM).map(ingredient -> (ingredient.size() == 0) ? Optional.empty() : Optional.<Ingredient>of(new Ingredient(ingredient)), ingredient -> (HolderSet)ingredient.map(()).orElse(HolderSet.direct(new Holder[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Ingredient>> OPTIONAL_CONTENTS_STREAM_CODEC;
/*  35 */   public static final Codec<HolderSet<Item>> NON_AIR_HOLDER_SET_CODEC = net.minecraft.resources.HolderSetCodec.create(Registries.ITEM, Item.CODEC, false); public static final Codec<Ingredient> CODEC; static {
/*  36 */     CODEC = ExtraCodecs.nonEmptyHolderSet(NON_AIR_HOLDER_SET_CODEC).xmap(Ingredient::new, i -> i.values);
/*     */   }
/*     */   private final HolderSet<Item> values;
/*     */   
/*     */   private Ingredient(HolderSet<Item> values) {
/*  41 */     values.unwrap().ifRight(directValues -> {
/*     */           if (directValues.isEmpty()) {
/*     */             throw new UnsupportedOperationException("Ingredients can't be empty");
/*     */           }
/*     */           
/*     */           if (directValues.contains(Items.AIR.builtInRegistryHolder())) {
/*     */             throw new UnsupportedOperationException("Ingredient can't contain air");
/*     */           }
/*     */         });
/*  50 */     this.values = values;
/*     */   }
/*     */   
/*     */   public static boolean testOptionalIngredient(Optional<Ingredient> ingredient, ItemStack stack) {
/*  54 */     Objects.requireNonNull(stack); return (Boolean)ingredient.<Boolean>map(value -> value.test(stack)).orElseGet(stack::isEmpty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Stream<Holder<Item>> items() {
/*  62 */     return this.values.stream();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  66 */     return (this.values.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean test(ItemStack input) {
/*  71 */     return input.is(this.values);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsItem(Holder<Item> item) {
/*  76 */     return this.values.contains(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  81 */     if (o instanceof Ingredient) { Ingredient other = (Ingredient)o;
/*  82 */       return Objects.equals(this.values, other.values); }
/*     */     
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public static Ingredient of(ItemLike itemLike) {
/*  88 */     return new Ingredient((HolderSet<Item>)HolderSet.direct(new Holder[] { (Holder)itemLike.asItem().builtInRegistryHolder() }));
/*     */   }
/*     */   
/*     */   public static Ingredient of(ItemLike... items) {
/*  92 */     return of(Arrays.stream(items));
/*     */   }
/*     */   
/*     */   public static Ingredient of(Stream<? extends ItemLike> stream) {
/*  96 */     return new Ingredient((HolderSet<Item>)HolderSet.direct(stream.map(e -> e.asItem().builtInRegistryHolder()).toList()));
/*     */   }
/*     */   
/*     */   public static Ingredient of(HolderSet<Item> tag) {
/* 100 */     return new Ingredient(tag);
/*     */   }
/*     */   
/*     */   public SlotDisplay display() {
/* 104 */     return (SlotDisplay)this.values.unwrap().map(net.minecraft.world.item.crafting.display.SlotDisplay.TagSlotDisplay::new, l -> new SlotDisplay.Composite(l.stream().map(Ingredient::displayForSingleItem).toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SlotDisplay optionalIngredientToDisplay(Optional<Ingredient> ingredient) {
/* 112 */     return (SlotDisplay)ingredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE);
/*     */   }
/*     */   
/*     */   private static SlotDisplay displayForSingleItem(Holder<Item> item) {
/* 116 */     SlotDisplay.ItemSlotDisplay itemSlotDisplay = new SlotDisplay.ItemSlotDisplay(item);
/*     */     
/* 118 */     ItemStack remainderStack = ((Item)item.value()).getCraftingRemainder();
/* 119 */     if (!remainderStack.isEmpty()) {
/* 120 */       SlotDisplay.ItemStackSlotDisplay itemStackSlotDisplay = new SlotDisplay.ItemStackSlotDisplay(remainderStack);
/* 121 */       return (SlotDisplay)new SlotDisplay.WithRemainder((SlotDisplay)itemSlotDisplay, (SlotDisplay)itemStackSlotDisplay);
/*     */     } 
/* 123 */     return (SlotDisplay)itemSlotDisplay;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/Ingredient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */