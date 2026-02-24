/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShufflingList<U>
/*     */   implements Iterable<U>
/*     */ {
/*     */   protected final List<WeightedEntry<U>> entries;
/*  25 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   public ShufflingList() {
/*  28 */     this.entries = Lists.newArrayList();
/*     */   }
/*     */   
/*     */   private ShufflingList(List<WeightedEntry<U>> entries) {
/*  32 */     this.entries = Lists.newArrayList(entries);
/*     */   }
/*     */   
/*     */   public static <U> Codec<ShufflingList<U>> codec(Codec<U> elementCodec) {
/*  36 */     return WeightedEntry.<E>codec((Codec)elementCodec).listOf().xmap(ShufflingList::new, l -> l.entries);
/*     */   }
/*     */   
/*     */   public ShufflingList<U> add(U data, int weight) {
/*  40 */     this.entries.add(new WeightedEntry<>(data, weight));
/*  41 */     return this;
/*     */   }
/*     */   
/*     */   public ShufflingList<U> shuffle() {
/*  45 */     this.entries.forEach(k -> k.setRandom(this.random.nextFloat()));
/*  46 */     this.entries.sort(Comparator.comparingDouble(WeightedEntry::getRandWeight));
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public Stream<U> stream() {
/*  51 */     return this.entries.stream().map(WeightedEntry::getData);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<U> iterator() {
/*  56 */     return Iterators.transform(this.entries.iterator(), WeightedEntry::getData);
/*     */   }
/*     */   
/*     */   public static class WeightedEntry<T> {
/*     */     private final T data;
/*     */     private final int weight;
/*     */     private double randWeight;
/*     */     
/*     */     private WeightedEntry(T data, int weight) {
/*  65 */       this.weight = weight;
/*  66 */       this.data = data;
/*     */     }
/*     */     
/*     */     private double getRandWeight() {
/*  70 */       return this.randWeight;
/*     */     }
/*     */     
/*     */     private void setRandom(float random) {
/*  74 */       this.randWeight = -Math.pow(random, (1.0F / this.weight));
/*     */     }
/*     */     
/*     */     public T getData() {
/*  78 */       return this.data;
/*     */     }
/*     */     
/*     */     public int getWeight() {
/*  82 */       return this.weight;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  87 */       return "" + this.weight + ":" + this.weight;
/*     */     }
/*     */     
/*     */     public static <E> Codec<WeightedEntry<E>> codec(final Codec<E> elementCodec) {
/*  91 */       return new Codec<WeightedEntry<E>>()
/*     */         {
/*     */           public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  94 */             Dynamic<T> map = new Dynamic(ops, input);
/*     */             
/*  96 */             Objects.requireNonNull(elementCodec); return map.get("data").flatMap(elementCodec::parse)
/*  97 */               .map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1)))
/*  98 */               .map(r -> Pair.of(r, ops.empty()));
/*     */           }
/*     */           
/*     */           public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix)
/*     */           {
/* 103 */             return ops.mapBuilder()
/* 104 */               .add("weight", ops.createInt(input.weight))
/* 105 */               .add("data", elementCodec.encodeStart(ops, input.data))
/* 106 */               .build(prefix); } }; } } class null implements Codec<WeightedEntry<E>> { public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> input, DynamicOps<T> ops, T prefix) { return ops.mapBuilder().add("weight", ops.createInt(input.weight)).add("data", elementCodec.encodeStart(ops, input.data)).build(prefix); }
/*     */     
/*     */     public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> ops, T input) {
/*     */       Dynamic<T> map = new Dynamic(ops, input);
/*     */       Objects.requireNonNull(elementCodec);
/*     */       return map.get("data").flatMap(elementCodec::parse).map(data -> new ShufflingList.WeightedEntry(data, map.get("weight").asInt(1))).map(r -> Pair.of(r, ops.empty()));
/*     */     } }
/*     */   public String toString() {
/* 114 */     return "ShufflingList[" + String.valueOf(this.entries) + "]";
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/ShufflingList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */