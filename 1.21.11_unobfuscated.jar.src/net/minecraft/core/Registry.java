/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Registry<T>
/*     */   extends IdMap<T>, Keyable, HolderLookup.RegistryLookup<T>
/*     */ {
/*     */   default Codec<T> byNameCodec() {
/*  30 */     return referenceHolderWithLifecycle().flatComapMap(Holder.Reference::value, value -> safeCastToReference(wrapAsHolder((T)value)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Codec<Holder<T>> holderByNameCodec() {
/*  37 */     return referenceHolderWithLifecycle().flatComapMap(holder -> holder, this::safeCastToReference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Codec<Holder.Reference<T>> referenceHolderWithLifecycle() {
/*  44 */     Codec<Holder.Reference<T>> referenceCodec = Identifier.CODEC.comapFlatMap(name -> (DataResult)get(name).<DataResult>map(DataResult::success).orElseGet(()), holder -> holder.key().identifier());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     return ExtraCodecs.overrideLifecycle(referenceCodec, e -> (Lifecycle)registrationInfo(e.key()).<Lifecycle>map(RegistrationInfo::lifecycle).orElse(Lifecycle.experimental()));
/*     */   }
/*     */   
/*     */   private DataResult<Holder.Reference<T>> safeCastToReference(Holder<T> holder) {
/*  53 */     Holder.Reference<T> reference = (Holder.Reference<T>)holder; return (holder instanceof Holder.Reference) ? DataResult.success(reference) : DataResult.error(() -> "Unregistered holder in " + String.valueOf(key()) + ": " + String.valueOf(holder));
/*     */   }
/*     */ 
/*     */   
/*     */   default <U> Stream<U> keys(DynamicOps<U> ops) {
/*  58 */     return keySet().stream().map(k -> ops.createString(k.toString()));
/*     */   }
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
/*     */   default Optional<T> getOptional(Identifier key) {
/*  75 */     return Optional.ofNullable(getValue(key));
/*     */   }
/*     */   
/*     */   default Optional<T> getOptional(ResourceKey<T> key) {
/*  79 */     return Optional.ofNullable(getValue(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T getValueOrThrow(ResourceKey<T> key) {
/*  91 */     T value = getValue(key);
/*  92 */     if (value == null) {
/*  93 */       throw new IllegalStateException("Missing key in " + String.valueOf(key()) + ": " + String.valueOf(key));
/*     */     }
/*  95 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Stream<T> stream() {
/* 107 */     return StreamSupport.stream(spliterator(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> T register(Registry<? super T> registry, String name, T value) {
/* 115 */     return register(registry, Identifier.parse(name), value);
/*     */   }
/*     */   
/*     */   static <V, T extends V> T register(Registry<V> registry, Identifier location, T value) {
/* 119 */     return register(registry, ResourceKey.create(registry.key(), location), value);
/*     */   }
/*     */   
/*     */   static <V, T extends V> T register(Registry<V> registry, ResourceKey<V> key, T value) {
/* 123 */     ((WritableRegistry<V>)registry).register(key, (V)value, RegistrationInfo.BUILT_IN);
/* 124 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   static <R, T extends R> Holder.Reference<T> registerForHolder(Registry<R> registry, ResourceKey<R> key, T value) {
/* 129 */     return ((WritableRegistry)registry).register((ResourceKey)key, value, RegistrationInfo.BUILT_IN);
/*     */   }
/*     */   
/*     */   static <R, T extends R> Holder.Reference<T> registerForHolder(Registry<R> registry, Identifier location, T value) {
/* 133 */     return registerForHolder(registry, ResourceKey.create(registry.key(), location), value);
/*     */   }
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
/*     */   default Iterable<Holder<T>> getTagOrEmpty(TagKey<T> id) {
/* 152 */     return (Iterable<Holder<T>>)DataFixUtils.orElse(get(id), List.of());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default IdMap<Holder<T>> asHolderIdMap() {
/* 158 */     return new IdMap<Holder<T>>()
/*     */       {
/*     */         public int getId(Holder<T> thing) {
/* 161 */           return Registry.this.getId(thing.value());
/*     */         }
/*     */ 
/*     */         
/*     */         public Holder<T> byId(int id) {
/* 166 */           return Registry.this.get(id).orElse(null);
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 171 */           return Registry.this.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<Holder<T>> iterator() {
/* 176 */           return Registry.this.listElements().map(e -> e).iterator();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   ResourceKey<? extends Registry<T>> key();
/*     */   
/*     */   Identifier getKey(T paramT);
/*     */   
/*     */   Optional<ResourceKey<T>> getResourceKey(T paramT);
/*     */   
/*     */   int getId(T paramT);
/*     */   
/*     */   T getValue(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   T getValue(Identifier paramIdentifier);
/*     */   
/*     */   Optional<RegistrationInfo> registrationInfo(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   Optional<Holder.Reference<T>> getAny();
/*     */   
/*     */   Set<Identifier> keySet();
/*     */   
/*     */   Set<Map.Entry<ResourceKey<T>, T>> entrySet();
/*     */   
/*     */   Set<ResourceKey<T>> registryKeySet();
/*     */   
/*     */   Optional<Holder.Reference<T>> getRandom(RandomSource paramRandomSource);
/*     */   
/*     */   boolean containsKey(Identifier paramIdentifier);
/*     */   
/*     */   boolean containsKey(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   Registry<T> freeze();
/*     */   
/*     */   Holder.Reference<T> createIntrusiveHolder(T paramT);
/*     */   
/*     */   Optional<Holder.Reference<T>> get(int paramInt);
/*     */   
/*     */   Optional<Holder.Reference<T>> get(Identifier paramIdentifier);
/*     */   
/*     */   Holder<T> wrapAsHolder(T paramT);
/*     */   
/*     */   Stream<HolderSet.Named<T>> getTags();
/*     */   
/*     */   PendingTags<T> prepareTagReload(TagLoader.LoadResult<T> paramLoadResult);
/*     */   
/*     */   public static interface PendingTags<T> {
/*     */     ResourceKey<? extends Registry<? extends T>> key();
/*     */     
/*     */     HolderLookup.RegistryLookup<T> lookup();
/*     */     
/*     */     void apply();
/*     */     
/*     */     int size();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/Registry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */