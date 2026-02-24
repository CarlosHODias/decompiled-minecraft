/*     */ package net.minecraft.util.random;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WeightedList<E>
/*     */ {
/*     */   private static final int FLAT_THRESHOLD = 64;
/*     */   private final int totalWeight;
/*     */   private final List<Weighted<E>> items;
/*     */   private final Selector<E> selector;
/*     */   
/*     */   private WeightedList(List<? extends Weighted<E>> items) {
/*  28 */     this.items = List.copyOf(items);
/*  29 */     this.totalWeight = WeightedRandom.getTotalWeight(items, Weighted::weight);
/*  30 */     if (this.totalWeight == 0) {
/*  31 */       this.selector = null;
/*  32 */     } else if (this.totalWeight < 64) {
/*  33 */       this.selector = new Flat<>(this.items, this.totalWeight);
/*     */     } else {
/*  35 */       this.selector = new Compact<>(this.items);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of() {
/*  40 */     return new WeightedList<>(List.of());
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(E value) {
/*  44 */     return new WeightedList<>(List.of(new Weighted<>(value, 1)));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> WeightedList<E> of(Weighted<E>... items) {
/*  49 */     return new WeightedList<>(List.of(items));
/*     */   }
/*     */   
/*     */   public static <E> WeightedList<E> of(List<Weighted<E>> items) {
/*  53 */     return new WeightedList<>(items);
/*     */   }
/*     */   
/*     */   public static <E> Builder<E> builder() {
/*  57 */     return new Builder<>();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  61 */     return this.items.isEmpty();
/*     */   }
/*     */   
/*     */   public <T> WeightedList<T> map(Function<E, T> mapper) {
/*  65 */     return new WeightedList(Lists.transform(this.items, e -> e.map(mapper)));
/*     */   }
/*     */   
/*     */   public Optional<E> getRandom(RandomSource random) {
/*  69 */     if (this.selector == null) {
/*  70 */       return Optional.empty();
/*     */     }
/*  72 */     int selection = random.nextInt(this.totalWeight);
/*  73 */     return Optional.of(this.selector.get(selection));
/*     */   }
/*     */   
/*     */   public E getRandomOrThrow(RandomSource random) {
/*  77 */     if (this.selector == null) {
/*  78 */       throw new IllegalStateException("Weighted list has no elements");
/*     */     }
/*  80 */     int selection = random.nextInt(this.totalWeight);
/*  81 */     return this.selector.get(selection);
/*     */   }
/*     */   
/*     */   public List<Weighted<E>> unwrap() {
/*  85 */     return this.items;
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> codec(Codec<E> elementCodec) {
/*  89 */     return Weighted.<E>codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> codec(MapCodec<E> elementCodec) {
/*  93 */     return Weighted.<E>codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(Codec<E> elementCodec) {
/*  97 */     return ExtraCodecs.nonEmptyList(Weighted.<E>codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E> Codec<WeightedList<E>> nonEmptyCodec(MapCodec<E> elementCodec) {
/* 101 */     return ExtraCodecs.nonEmptyList(Weighted.<E>codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public static <E, B extends io.netty.buffer.ByteBuf> StreamCodec<B, WeightedList<E>> streamCodec(StreamCodec<B, E> elementCodec) {
/* 105 */     return Weighted.<B, T>streamCodec((StreamCodec)elementCodec).apply(ByteBufCodecs.list()).map(WeightedList::of, WeightedList::unwrap);
/*     */   }
/*     */   
/*     */   public boolean contains(E value) {
/* 109 */     for (Weighted<E> item : this.items) {
/* 110 */       if (item.value().equals(value)) {
/* 111 */         return true;
/*     */       }
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 119 */     if (this == obj) {
/* 120 */       return true;
/*     */     }
/* 122 */     if (obj instanceof WeightedList) { WeightedList<?> list = (WeightedList)obj;
/* 123 */       return (this.totalWeight == list.totalWeight && Objects.equals(this.items, list.items)); }
/*     */     
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 130 */     int result = this.totalWeight;
/* 131 */     result = 31 * result + this.items.hashCode();
/* 132 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */   {
/* 143 */     private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();
/*     */     
/*     */     public Builder<E> add(E item) {
/* 146 */       return add(item, 1);
/*     */     }
/*     */     
/*     */     public Builder<E> add(E item, int weight) {
/* 150 */       this.result.add(new Weighted<>(item, weight));
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     public WeightedList<E> build() {
/* 155 */       return new WeightedList<>((List<? extends Weighted<E>>)this.result.build());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Flat<E> implements Selector<E> {
/*     */     private final Object[] entries;
/*     */     
/*     */     private Flat(List<Weighted<E>> entries, int totalWeight) {
/* 163 */       this.entries = new Object[totalWeight];
/* 164 */       int i = 0;
/* 165 */       for (Weighted<E> entry : entries) {
/* 166 */         int weight = entry.weight();
/* 167 */         Arrays.fill(this.entries, i, i + weight, entry.value());
/* 168 */         i += weight;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E get(int selection) {
/* 175 */       return (E)this.entries[selection];
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Compact<E> implements Selector<E> {
/*     */     private final Weighted<?>[] entries;
/*     */     
/*     */     private Compact(List<Weighted<E>> entries) {
/* 183 */       this.entries = (Weighted<?>[])entries.toArray(x$0 -> new Weighted[x$0]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E get(int selection) {
/* 189 */       for (Weighted<?> entry : this.entries) {
/* 190 */         selection -= entry.weight();
/* 191 */         if (selection < 0) {
/* 192 */           return (E)entry.value();
/*     */         }
/*     */       } 
/* 195 */       throw new IllegalStateException("" + selection + " exceeded total weight");
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface Selector<E> {
/*     */     E get(int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/random/WeightedList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */