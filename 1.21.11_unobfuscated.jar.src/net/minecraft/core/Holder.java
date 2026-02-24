/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Holder<T>
/*     */ {
/*     */   default String getRegisteredName() {
/*  55 */     return unwrapKey().<String>map(key -> key.identifier().toString()).orElse("[unregistered]");
/*     */   }
/*     */   
/*     */   public enum Kind {
/*  59 */     REFERENCE, DIRECT; } T value(); boolean isBound(); boolean is(Identifier paramIdentifier); boolean is(ResourceKey<T> paramResourceKey);
/*     */   boolean is(Predicate<ResourceKey<T>> paramPredicate);
/*     */   boolean is(TagKey<T> paramTagKey);
/*     */   static <T> Holder<T> direct(T value) {
/*  63 */     return new Direct<>(value);
/*     */   } @Deprecated
/*     */   boolean is(Holder<T> paramHolder); Stream<TagKey<T>> tags(); Either<ResourceKey<T>, T> unwrap(); Optional<ResourceKey<T>> unwrapKey(); Kind kind(); boolean canSerializeIn(HolderOwner<T> paramHolderOwner); public static final class Direct<T> extends Record implements Holder<T> { private final T value; public Direct(T value) {
/*  66 */       this.value = value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Holder$Direct;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/Holder$Direct<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/Holder$Direct;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  66 */       //   0	8	0	this	Lnet/minecraft/core/Holder$Direct<TT;>; } public T value() { return this.value; }
/*     */     
/*     */     public boolean isBound() {
/*  69 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(Identifier key) {
/*  74 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(ResourceKey<T> key) {
/*  79 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(TagKey<T> tag) {
/*  84 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(Holder<T> holder) {
/*  89 */       return this.value.equals(holder.value());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(Predicate<ResourceKey<T>> predicate) {
/*  94 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<ResourceKey<T>, T> unwrap() {
/*  99 */       return Either.right(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<ResourceKey<T>> unwrapKey() {
/* 104 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Holder.Kind kind() {
/* 109 */       return Holder.Kind.DIRECT;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 114 */       return "Direct{" + String.valueOf(this.value) + "}";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> registry) {
/* 119 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<TagKey<T>> tags() {
/* 124 */       return Stream.of((TagKey<T>[])new TagKey[0]);
/*     */     } }
/*     */   
/*     */   public static class Reference<T> implements Holder<T> { private final HolderOwner<T> owner;
/*     */     private Set<TagKey<T>> tags;
/*     */     private final Type type;
/*     */     private ResourceKey<T> key;
/*     */     private T value;
/*     */     
/* 133 */     protected enum Type { STAND_ALONE, INTRUSIVE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Reference(Type type, HolderOwner<T> owner, ResourceKey<T> key, T value) {
/* 146 */       this.owner = owner;
/* 147 */       this.type = type;
/* 148 */       this.key = key;
/* 149 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static <T> Reference<T> createStandAlone(HolderOwner<T> owner, ResourceKey<T> key) {
/* 156 */       return new Reference<>(Type.STAND_ALONE, owner, key, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public static <T> Reference<T> createIntrusive(HolderOwner<T> owner, T value) {
/* 165 */       return new Reference<>(Type.INTRUSIVE, owner, null, value);
/*     */     }
/*     */     
/*     */     public ResourceKey<T> key() {
/* 169 */       if (this.key == null) {
/* 170 */         throw new IllegalStateException("Trying to access unbound value '" + String.valueOf(this.value) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 172 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public T value() {
/* 177 */       if (this.value == null) {
/* 178 */         throw new IllegalStateException("Trying to access unbound value '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner));
/*     */       }
/* 180 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(Identifier key) {
/* 185 */       return key().identifier().equals(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(ResourceKey<T> key) {
/* 190 */       return (key() == key);
/*     */     }
/*     */     
/*     */     private Set<TagKey<T>> boundTags() {
/* 194 */       if (this.tags == null) {
/* 195 */         throw new IllegalStateException("Tags not bound");
/*     */       }
/* 197 */       return this.tags;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(TagKey<T> tag) {
/* 202 */       return boundTags().contains(tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(Holder<T> holder) {
/* 207 */       return holder.is(key());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean is(Predicate<ResourceKey<T>> predicate) {
/* 212 */       return predicate.test(key());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> context) {
/* 217 */       return this.owner.canSerializeIn(context);
/*     */     }
/*     */ 
/*     */     
/*     */     public Either<ResourceKey<T>, T> unwrap() {
/* 222 */       return Either.left(key());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<ResourceKey<T>> unwrapKey() {
/* 227 */       return Optional.of(key());
/*     */     }
/*     */ 
/*     */     
/*     */     public Holder.Kind kind() {
/* 232 */       return Holder.Kind.REFERENCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBound() {
/* 237 */       return (this.key != null && this.value != null);
/*     */     }
/*     */     
/*     */     void bindKey(ResourceKey<T> key) {
/* 241 */       if (this.key != null && key != this.key) {
/* 242 */         throw new IllegalStateException("Can't change holder key: existing=" + String.valueOf(this.key) + ", new=" + String.valueOf(key));
/*     */       }
/* 244 */       this.key = key;
/*     */     }
/*     */     
/*     */     protected void bindValue(T value) {
/* 248 */       if (this.type == Type.INTRUSIVE && this.value != value) {
/* 249 */         throw new IllegalStateException("Can't change holder " + String.valueOf(this.key) + " value: existing=" + String.valueOf(this.value) + ", new=" + String.valueOf(value));
/*     */       }
/* 251 */       this.value = value;
/*     */     }
/*     */     
/*     */     void bindTags(Collection<TagKey<T>> tags) {
/* 255 */       this.tags = Set.copyOf(tags);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<TagKey<T>> tags() {
/* 260 */       return boundTags().stream();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 265 */       return "Reference{" + String.valueOf(this.key) + "=" + String.valueOf(this.value) + "}";
/*     */     } }
/*     */ 
/*     */   
/*     */   protected enum Type {
/*     */     STAND_ALONE, INTRUSIVE;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/Holder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */