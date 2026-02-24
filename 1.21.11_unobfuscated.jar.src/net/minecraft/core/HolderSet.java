/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface HolderSet<T>
/*     */   extends Iterable<Holder<T>>
/*     */ {
/*     */   Stream<Holder<T>> stream();
/*     */   
/*     */   int size();
/*     */   
/*     */   boolean isBound();
/*     */   
/*     */   Either<TagKey<T>, List<Holder<T>>> unwrap();
/*     */   
/*     */   Optional<Holder<T>> getRandomElement(RandomSource paramRandomSource);
/*     */   
/*     */   Holder<T> get(int paramInt);
/*     */   
/*     */   boolean contains(Holder<T> paramHolder);
/*     */   
/*     */   boolean canSerializeIn(HolderOwner<T> paramHolderOwner);
/*     */   
/*     */   Optional<TagKey<T>> unwrapKey();
/*     */   
/*     */   public static abstract class ListBacked<T>
/*     */     implements HolderSet<T> {
/*     */     protected abstract List<Holder<T>> contents();
/*     */     
/*     */     public int size() {
/*  43 */       return contents().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Holder<T>> spliterator() {
/*  48 */       return contents().spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Holder<T>> iterator() {
/*  53 */       return contents().iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<Holder<T>> stream() {
/*  58 */       return contents().stream();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Holder<T>> getRandomElement(RandomSource random) {
/*  63 */       return Util.getRandomSafe(contents(), random);
/*     */     }
/*     */ 
/*     */     
/*     */     public Holder<T> get(int index) {
/*  68 */       return contents().get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> owner) {
/*  73 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Direct<T> extends ListBacked<T> {
/*  78 */     private static final Direct<?> EMPTY = new Direct(List.of());
/*     */     
/*     */     private final List<Holder<T>> contents;
/*     */     
/*     */     private Set<Holder<T>> contentsSet;
/*     */ 
/*     */     
/*     */     private Direct(List<Holder<T>> contents) {
/*  86 */       this.contents = contents;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Holder<T>> contents() {
/*  91 */       return this.contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBound() {
/*  96 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 101 */       return Either.right(this.contents);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TagKey<T>> unwrapKey() {
/* 106 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Holder<T> value) {
/* 111 */       if (this.contentsSet == null) {
/* 112 */         this.contentsSet = Set.copyOf(this.contents);
/*     */       }
/* 114 */       return this.contentsSet.contains(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 119 */       return "DirectSet[" + String.valueOf(this.contents) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 124 */       if (this == obj) {
/* 125 */         return true;
/*     */       }
/* 127 */       if (obj instanceof Direct) { Direct<?> direct = (Direct)obj; if (this.contents.equals(direct.contents)); }  return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 132 */       return this.contents.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Named<T>
/*     */     extends ListBacked<T> {
/*     */     private final HolderOwner<T> owner;
/*     */     private final TagKey<T> key;
/*     */     private List<Holder<T>> contents;
/*     */     
/*     */     Named(HolderOwner<T> owner, TagKey<T> key) {
/* 143 */       this.owner = owner;
/* 144 */       this.key = key;
/*     */     }
/*     */     
/*     */     void bind(List<Holder<T>> contents) {
/* 148 */       this.contents = List.copyOf(contents);
/*     */     }
/*     */     
/*     */     public TagKey<T> key() {
/* 152 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<Holder<T>> contents() {
/* 157 */       if (this.contents == null) {
/* 158 */         throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 160 */       return this.contents;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBound() {
/* 165 */       return (this.contents != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 170 */       return Either.left(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TagKey<T>> unwrapKey() {
/* 175 */       return Optional.of(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Holder<T> value) {
/* 180 */       return value.is(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 185 */       return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> context) {
/* 190 */       return this.owner.canSerializeIn(context);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   static <T> Named<T> emptyNamed(HolderOwner<T> owner, TagKey<T> key) {
/* 202 */     return new Named<T>(owner, key)
/*     */       {
/*     */         protected List<Holder<T>> contents() {
/* 205 */           throw new UnsupportedOperationException("Tag " + String.valueOf(key()) + " can't be dereferenced during construction");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> HolderSet<T> empty() {
/* 212 */     return (HolderSet)Direct.EMPTY;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <T> Direct<T> direct(Holder<T>... values) {
/* 217 */     return new Direct<>(List.of(values));
/*     */   }
/*     */   
/*     */   static <T> Direct<T> direct(List<? extends Holder<T>> values) {
/* 221 */     return new Direct<>(List.copyOf(values));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <E, T> Direct<T> direct(Function<E, Holder<T>> holderGetter, E... elements) {
/* 226 */     return direct(Stream.<E>of(elements).<Holder<T>>map(holderGetter).toList());
/*     */   }
/*     */   
/*     */   static <E, T> Direct<T> direct(Function<E, Holder<T>> holderGetter, Collection<E> elements) {
/* 230 */     return direct(elements.stream().<Holder<T>>map(holderGetter).toList());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/HolderSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */