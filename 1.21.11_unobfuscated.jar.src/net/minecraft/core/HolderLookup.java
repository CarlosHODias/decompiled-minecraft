/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface HolderLookup<T>
/*     */   extends HolderGetter<T>
/*     */ {
/*     */   Stream<Holder.Reference<T>> listElements();
/*     */   
/*     */   default Stream<ResourceKey<T>> listElementIds() {
/*  25 */     return listElements().map(Holder.Reference::key);
/*     */   }
/*     */   
/*     */   Stream<HolderSet.Named<T>> listTags();
/*     */   
/*     */   default Stream<TagKey<T>> listTagIds() {
/*  31 */     return listTags().map(HolderSet.Named::key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface RegistryLookup<T>
/*     */     extends HolderLookup<T>, HolderOwner<T>
/*     */   {
/*     */     default RegistryLookup<T> filterFeatures(FeatureFlagSet enabledFeatures) {
/*  43 */       if (FeatureElement.FILTERED_REGISTRIES.contains(key())) {
/*  44 */         return filterElements(t -> ((FeatureElement)t).isEnabled(enabledFeatures));
/*     */       }
/*     */       
/*  47 */       return this;
/*     */     }
/*     */     
/*     */     default RegistryLookup<T> filterElements(final Predicate<T> filter) {
/*  51 */       return new Delegate<T>()
/*     */         {
/*     */           public HolderLookup.RegistryLookup<T> parent() {
/*  54 */             return HolderLookup.RegistryLookup.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*  59 */             return parent().get(id).filter(holder -> filter.test(holder.value()));
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<Holder.Reference<T>> listElements() {
/*  64 */             return parent().listElements().filter(e -> filter.test(e.value()));
/*     */           }
/*     */         };
/*     */     }
/*     */     ResourceKey<? extends Registry<? extends T>> key();
/*     */     Lifecycle registryLifecycle();
/*     */     
/*     */     public static interface Delegate<T> extends RegistryLookup<T> { HolderLookup.RegistryLookup<T> parent();
/*     */       
/*     */       default ResourceKey<? extends Registry<? extends T>> key() {
/*  74 */         return parent().key();
/*     */       }
/*     */ 
/*     */       
/*     */       default Lifecycle registryLifecycle() {
/*  79 */         return parent().registryLifecycle();
/*     */       }
/*     */ 
/*     */       
/*     */       default Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*  84 */         return parent().get(id);
/*     */       }
/*     */ 
/*     */       
/*     */       default Stream<Holder.Reference<T>> listElements() {
/*  89 */         return parent().listElements();
/*     */       }
/*     */ 
/*     */       
/*     */       default Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*  94 */         return parent().get(id);
/*     */       }
/*     */       
/*     */       default Stream<HolderSet.Named<T>> listTags()
/*     */       {
/*  99 */         return parent().listTags(); } } } class null implements RegistryLookup.Delegate<T> { public HolderLookup.RegistryLookup<T> parent() { return HolderLookup.RegistryLookup.this; } public Optional<Holder.Reference<T>> get(ResourceKey<T> id) { return parent().get(id).filter(holder -> filter.test(holder.value())); } public Stream<Holder.Reference<T>> listElements() { return parent().listElements().filter(e -> filter.test(e.value())); } } public static interface Delegate<T> extends RegistryLookup<T> { default Stream<HolderSet.Named<T>> listTags() { return parent().listTags(); } HolderLookup.RegistryLookup<T> parent(); default ResourceKey<? extends Registry<? extends T>> key() { return parent().key(); } default Lifecycle registryLifecycle() { return parent().registryLifecycle(); } default Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/*     */       return parent().get(id);
/*     */     } default Stream<Holder.Reference<T>> listElements() {
/*     */       return parent().listElements();
/*     */     } default Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*     */       return parent().get(id);
/*     */     } }
/*     */    public static interface Provider extends HolderGetter.Provider {
/*     */     default Stream<HolderLookup.RegistryLookup<?>> listRegistries() {
/* 108 */       return listRegistryKeys().map(this::lookupOrThrow);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 116 */       return (HolderLookup.RegistryLookup<T>)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*     */     }
/*     */     
/*     */     default <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/* 120 */       return RegistryOps.create(parent, this);
/*     */     }
/*     */     
/*     */     static Provider create(Stream<HolderLookup.RegistryLookup<?>> lookups) {
/* 124 */       final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> map = lookups.collect(Collectors.toUnmodifiableMap(HolderLookup.RegistryLookup::key, e -> e));
/* 125 */       return new Provider()
/*     */         {
/*     */           public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/* 128 */             return map.keySet().stream();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 134 */             return Optional.ofNullable((HolderLookup.RegistryLookup<T>)map.get(key));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default Lifecycle allRegistriesLifecycle() {
/* 146 */       return listRegistries().<Lifecycle>map(HolderLookup.RegistryLookup::registryLifecycle).reduce(Lifecycle.stable(), Lifecycle::add);
/*     */     }
/*     */     
/*     */     Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys();
/*     */     
/*     */     <T> Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey);
/*     */   }
/*     */   
/*     */   class null implements Provider {
/*     */     public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/*     */       return map.keySet().stream();
/*     */     }
/*     */     
/*     */     public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/*     */       return Optional.ofNullable((HolderLookup.RegistryLookup<T>)map.get(key));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/HolderLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */