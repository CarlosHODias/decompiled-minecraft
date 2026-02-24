/*     */ package net.minecraft.world.item.crafting.display;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextMap;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ 
/*     */ public interface SlotDisplay {
/*  33 */   public static final Codec<SlotDisplay> CODEC = BuiltInRegistries.SLOT_DISPLAY.byNameCodec().dispatch(SlotDisplay::type, Type::codec);
/*  34 */   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay> STREAM_CODEC = ByteBufCodecs.registry(Registries.SLOT_DISPLAY).dispatch(SlotDisplay::type, Type::streamCodec);
/*     */ 
/*     */   
/*     */   <T> Stream<T> resolve(ContextMap paramContextMap, DisplayContentsFactory<T> paramDisplayContentsFactory);
/*     */   
/*     */   Type<? extends SlotDisplay> type();
/*     */   
/*     */   default boolean isEnabled(FeatureFlagSet enabledFeatures) {
/*  42 */     return true;
/*     */   }
/*     */   public static final class Type<T extends SlotDisplay> extends Record { private final MapCodec<T> codec; private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*  45 */     public Type(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) { this.codec = codec; this.streamCodec = streamCodec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  45 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>; } public MapCodec<T> codec() { return this.codec; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  45 */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Type<TT;>; } public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*     */      }
/*     */ 
/*     */   
/*     */   public static class ItemStackContentsFactory
/*     */     implements DisplayContentsFactory.ForStacks<ItemStack>
/*     */   {
/*  52 */     public static final ItemStackContentsFactory INSTANCE = new ItemStackContentsFactory();
/*     */ 
/*     */     
/*     */     public ItemStack forStack(ItemStack stack) {
/*  56 */       return stack;
/*     */     }
/*     */   }
/*     */   
/*     */   default List<ItemStack> resolveForStacks(ContextMap context) {
/*  61 */     return resolve(context, ItemStackContentsFactory.INSTANCE).toList();
/*     */   }
/*     */   
/*     */   default ItemStack resolveForFirstStack(ContextMap context) {
/*  65 */     return resolve(context, ItemStackContentsFactory.INSTANCE).findFirst().orElse(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public static class Empty implements SlotDisplay {
/*  69 */     public static final Empty INSTANCE = new Empty();
/*     */     
/*  71 */     public static final MapCodec<Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */     
/*  73 */     public static final StreamCodec<RegistryFriendlyByteBuf, Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/*  75 */     public static final SlotDisplay.Type<Empty> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<Empty> type() {
/*  82 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  87 */       return "<empty>";
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/*  92 */       return Stream.empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AnyFuel implements SlotDisplay {
/*  97 */     public static final AnyFuel INSTANCE = new AnyFuel();
/*     */     
/*  99 */     public static final MapCodec<AnyFuel> MAP_CODEC = MapCodec.unit(INSTANCE);
/*     */     
/* 101 */     public static final StreamCodec<RegistryFriendlyByteBuf, AnyFuel> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*     */     
/* 103 */     public static final SlotDisplay.Type<AnyFuel> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<AnyFuel> type() {
/* 110 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 115 */       return "<any fuel>";
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 120 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 121 */         FuelValues fuelValues = (FuelValues)context.getOptional(SlotDisplayContext.FUEL_VALUES);
/* 122 */         if (fuelValues != null) {
/* 123 */           Objects.requireNonNull(stacks); return fuelValues.fuelItems().stream().map(stacks::forStack);
/*     */         }  }
/*     */       
/* 126 */       return Stream.empty();
/*     */     } }
/*     */   public static final class SmithingTrimDemoSlotDisplay extends Record implements SlotDisplay { private final SlotDisplay base; private final SlotDisplay material; private final Holder<TrimPattern> pattern; public static final MapCodec<SmithingTrimDemoSlotDisplay> MAP_CODEC;
/*     */     
/* 130 */     public SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<TrimPattern> pattern) { this.base = base; this.material = material; this.pattern = pattern; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #130	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$SmithingTrimDemoSlotDisplay;
/* 130 */       //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay base() { return this.base; } public SlotDisplay material() { return this.material; } public Holder<TrimPattern> pattern() { return this.pattern; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 136 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("base").forGetter(SmithingTrimDemoSlotDisplay::base), (App)SlotDisplay.CODEC.fieldOf("material").forGetter(SmithingTrimDemoSlotDisplay::material), (App)TrimPattern.CODEC.fieldOf("pattern").forGetter(SmithingTrimDemoSlotDisplay::pattern)).apply((Applicative)i, SmithingTrimDemoSlotDisplay::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimDemoSlotDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::base, SlotDisplay.STREAM_CODEC, SmithingTrimDemoSlotDisplay::material, TrimPattern.STREAM_CODEC, SmithingTrimDemoSlotDisplay::pattern, SmithingTrimDemoSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     public static final SlotDisplay.Type<SmithingTrimDemoSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<SmithingTrimDemoSlotDisplay> type() {
/* 153 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 158 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 159 */         HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 160 */         if (registries != null) {
/*     */           
/* 162 */           RandomSource randomSource = RandomSource.create(System.identityHashCode(this));
/* 163 */           List<ItemStack> bases = this.base.resolveForStacks(context);
/* 164 */           if (bases.isEmpty()) {
/* 165 */             return Stream.empty();
/*     */           }
/* 167 */           List<ItemStack> materials = this.material.resolveForStacks(context);
/* 168 */           if (materials.isEmpty()) {
/* 169 */             return Stream.empty();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 178 */           Objects.requireNonNull(stacks); return Stream.generate(() -> { ItemStack base = (ItemStack)Util.getRandom(bases, randomSource), material = (ItemStack)Util.getRandom(materials, randomSource); return SmithingTrimRecipe.applyTrim(registries, base, material, this.pattern); }).limit(256L).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
/*     */         }  }
/*     */       
/* 181 */       return Stream.empty();
/*     */     } }
/*     */   public static final class ItemSlotDisplay extends Record implements SlotDisplay { private final Holder<Item> item; public static final MapCodec<ItemSlotDisplay> MAP_CODEC;
/*     */     
/* 185 */     public ItemSlotDisplay(Holder<Item> item) { this.item = item; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #185	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemSlotDisplay;
/* 185 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<Item> item() { return this.item; } static {
/* 186 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Item.CODEC.fieldOf("item").forGetter(ItemSlotDisplay::item)).apply((Applicative)i, ItemSlotDisplay::new));
/*     */     }
/*     */ 
/*     */     
/* 190 */     public static final StreamCodec<RegistryFriendlyByteBuf, ItemSlotDisplay> STREAM_CODEC = StreamCodec.composite(Item.STREAM_CODEC, ItemSlotDisplay::item, ItemSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     public static final SlotDisplay.Type<ItemSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<ItemSlotDisplay> type() {
/* 199 */       return TYPE;
/*     */     }
/*     */     
/*     */     public ItemSlotDisplay(Item item) {
/* 203 */       this((Holder<Item>)item.builtInRegistryHolder());
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 208 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 209 */         return Stream.of(stacks.forStack(this.item)); }
/*     */       
/* 211 */       return Stream.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 216 */       return ((Item)this.item.value()).isEnabled(enabledFeatures);
/*     */     } }
/*     */   public static final class ItemStackSlotDisplay extends Record implements SlotDisplay { private final ItemStack stack; public static final MapCodec<ItemStackSlotDisplay> MAP_CODEC;
/*     */     
/* 220 */     public ItemStackSlotDisplay(ItemStack stack) { this.stack = stack; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #220	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #220	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 220 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$ItemStackSlotDisplay; } public ItemStack stack() { return this.stack; } static {
/* 221 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemStack.STRICT_CODEC.fieldOf("item").forGetter(ItemStackSlotDisplay::stack)).apply((Applicative)i, ItemStackSlotDisplay::new));
/*     */     }
/*     */ 
/*     */     
/* 225 */     public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackSlotDisplay> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC, ItemStackSlotDisplay::stack, ItemStackSlotDisplay::new);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     public static final SlotDisplay.Type<ItemStackSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<ItemStackSlotDisplay> type() {
/* 234 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 239 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 240 */         return Stream.of(stacks.forStack(this.stack)); }
/*     */       
/* 242 */       return Stream.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 247 */       if (this != o) { if (o instanceof ItemStackSlotDisplay) { ItemStackSlotDisplay that = (ItemStackSlotDisplay)o; if (ItemStack.matches(this.stack, that.stack)); }  return false; }
/*     */     
/*     */     }
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 252 */       return this.stack.getItem().isEnabled(enabledFeatures);
/*     */     } }
/*     */   public static final class TagSlotDisplay extends Record implements SlotDisplay { private final TagKey<Item> tag; public static final MapCodec<TagSlotDisplay> MAP_CODEC;
/*     */     
/* 256 */     public TagSlotDisplay(TagKey<Item> tag) { this.tag = tag; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$TagSlotDisplay;
/* 256 */       //   0	8	1	o	Ljava/lang/Object; } public TagKey<Item> tag() { return this.tag; } static {
/* 257 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(TagSlotDisplay::tag)).apply((Applicative)i, TagSlotDisplay::new));
/*     */     }
/*     */ 
/*     */     
/* 261 */     public static final StreamCodec<RegistryFriendlyByteBuf, TagSlotDisplay> STREAM_CODEC = StreamCodec.composite(
/* 262 */         TagKey.streamCodec(Registries.ITEM), TagSlotDisplay::tag, TagSlotDisplay::new);
/*     */ 
/*     */ 
/*     */     
/* 266 */     public static final SlotDisplay.Type<TagSlotDisplay> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<TagSlotDisplay> type() {
/* 270 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 275 */       if (factory instanceof DisplayContentsFactory.ForStacks) { DisplayContentsFactory.ForStacks<T> stacks = (DisplayContentsFactory.ForStacks<T>)factory;
/* 276 */         HolderLookup.Provider registries = (HolderLookup.Provider)context.getOptional(SlotDisplayContext.REGISTRIES);
/* 277 */         if (registries != null) {
/* 278 */           return registries.lookupOrThrow(Registries.ITEM)
/* 279 */             .get(this.tag)
/* 280 */             .map(t -> { Objects.requireNonNull(stacks); return t.stream().map(stacks::forStack);
/* 281 */               }).stream().flatMap(s -> s);
/*     */         } }
/*     */       
/* 284 */       return Stream.empty();
/*     */     } }
/*     */   public static final class Composite extends Record implements SlotDisplay { private final List<SlotDisplay> contents; public static final MapCodec<Composite> MAP_CODEC;
/*     */     
/* 288 */     public Composite(List<SlotDisplay> contents) { this.contents = contents; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$Composite;
/* 288 */       //   0	8	1	o	Ljava/lang/Object; } public List<SlotDisplay> contents() { return this.contents; } static {
/* 289 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.listOf().fieldOf("contents").forGetter(Composite::contents)).apply((Applicative)i, Composite::new));
/*     */     }
/*     */ 
/*     */     
/* 293 */     public static final StreamCodec<RegistryFriendlyByteBuf, Composite> STREAM_CODEC = StreamCodec.composite(
/* 294 */         SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), Composite::contents, Composite::new);
/*     */ 
/*     */ 
/*     */     
/* 298 */     public static final SlotDisplay.Type<Composite> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<Composite> type() {
/* 302 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 307 */       return this.contents.stream().flatMap(d -> d.resolve(context, factory));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 312 */       return this.contents.stream().allMatch(c -> c.isEnabled(enabledFeatures));
/*     */     } }
/*     */   public static final class WithRemainder extends Record implements SlotDisplay { private final SlotDisplay input; private final SlotDisplay remainder; public static final MapCodec<WithRemainder> MAP_CODEC;
/*     */     
/* 316 */     public WithRemainder(SlotDisplay input, SlotDisplay remainder) { this.input = input; this.remainder = remainder; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #316	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SlotDisplay$WithRemainder;
/* 316 */       //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay input() { return this.input; } public SlotDisplay remainder() { return this.remainder; }
/*     */ 
/*     */     
/*     */     static {
/* 320 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("input").forGetter(WithRemainder::input), (App)SlotDisplay.CODEC.fieldOf("remainder").forGetter(WithRemainder::remainder)).apply((Applicative)i, WithRemainder::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 325 */     public static final StreamCodec<RegistryFriendlyByteBuf, WithRemainder> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, WithRemainder::input, SlotDisplay.STREAM_CODEC, WithRemainder::remainder, WithRemainder::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     public static final SlotDisplay.Type<WithRemainder> TYPE = new SlotDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*     */ 
/*     */     
/*     */     public SlotDisplay.Type<WithRemainder> type() {
/* 335 */       return TYPE;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
/* 340 */       if (factory instanceof DisplayContentsFactory.ForRemainders) { DisplayContentsFactory.ForRemainders<T> remainders = (DisplayContentsFactory.ForRemainders<T>)factory;
/* 341 */         List<T> resolvedRemainders = this.remainder.<T>resolve(context, factory).toList();
/* 342 */         return this.input.<T>resolve(context, factory).map(input -> remainders.addRemainder(input, resolvedRemainders)); }
/*     */       
/* 344 */       return this.input.resolve(context, factory);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 349 */       return (this.input.isEnabled(enabledFeatures) && this.remainder.isEnabled(enabledFeatures));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/SlotDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */