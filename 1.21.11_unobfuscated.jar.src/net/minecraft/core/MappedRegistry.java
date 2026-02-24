/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ public class MappedRegistry<T>
/*     */   implements WritableRegistry<T>
/*     */ {
/*     */   private final ResourceKey<? extends Registry<T>> key;
/*  34 */   private final ObjectList<Holder.Reference<T>> byId = (ObjectList<Holder.Reference<T>>)new ObjectArrayList(256); private final Reference2IntMap<T> toId; private final Map<Identifier, Holder.Reference<T>> byLocation; private final Map<ResourceKey<T>, Holder.Reference<T>> byKey; private final Map<T, Holder.Reference<T>> byValue; private final Map<ResourceKey<T>, RegistrationInfo> registrationInfos; private Lifecycle registryLifecycle; private final Map<TagKey<T>, HolderSet.Named<T>> frozenTags; private TagSet<T> allTags; private boolean frozen; private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders; public MappedRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle initialLifecycle, boolean intrusiveHolders) {
/*  35 */     this.toId = (Reference2IntMap<T>)Util.make(new Reference2IntOpenHashMap(), t -> t.defaultReturnValue(-1));
/*     */     
/*  37 */     this.byLocation = new HashMap<>();
/*  38 */     this.byKey = new HashMap<>();
/*  39 */     this.byValue = new IdentityHashMap<>();
/*     */     
/*  41 */     this.registrationInfos = new IdentityHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     this.frozenTags = new IdentityHashMap<>();
/*     */ 
/*     */ 
/*     */     
/*  50 */     this.allTags = TagSet.unbound();
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
/*  69 */     this.key = key;
/*  70 */     this.registryLifecycle = initialLifecycle;
/*  71 */     if (intrusiveHolders)
/*  72 */       this.unregisteredIntrusiveHolders = new IdentityHashMap<>(); 
/*     */   } public Stream<HolderSet.Named<T>> listTags() {
/*     */     return getTags();
/*     */   } public MappedRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
/*     */     this(key, lifecycle, false);
/*     */   } public ResourceKey<? extends Registry<T>> key() {
/*  78 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return "Registry[" + String.valueOf(this.key) + " (" + String.valueOf(this.registryLifecycle) + ")]";
/*     */   }
/*     */   
/*     */   private void validateWrite() {
/*  87 */     if (this.frozen) {
/*  88 */       throw new IllegalStateException("Registry is already frozen");
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateWrite(ResourceKey<T> key) {
/*  93 */     if (this.frozen) {
/*  94 */       throw new IllegalStateException("Registry is already frozen (trying to add key " + String.valueOf(key) + ")");
/*     */     }
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> register(ResourceKey<T> key, T value, RegistrationInfo registrationInfo) {
/*     */     Holder.Reference<T> holder;
/* 100 */     validateWrite(key);
/* 101 */     Objects.requireNonNull(key);
/* 102 */     Objects.requireNonNull(value);
/*     */     
/* 104 */     if (this.byLocation.containsKey(key.identifier())) {
/* 105 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Adding duplicate key '" + String.valueOf(key) + "' to registry"));
/*     */     }
/*     */     
/* 108 */     if (this.byValue.containsKey(value)) {
/* 109 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Adding duplicate value '" + String.valueOf(value) + "' to registry"));
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (this.unregisteredIntrusiveHolders != null) {
/*     */       
/* 115 */       holder = this.unregisteredIntrusiveHolders.remove(value);
/* 116 */       if (holder == null) {
/* 117 */         throw new AssertionError("Missing intrusive holder for " + String.valueOf(key) + ":" + String.valueOf(value));
/*     */       }
/* 119 */       holder.bindKey(key);
/*     */     } else {
/*     */       
/* 122 */       holder = this.byKey.computeIfAbsent(key, k -> Holder.Reference.createStandAlone(this, k));
/*     */     } 
/*     */     
/* 125 */     this.byKey.put(key, holder);
/* 126 */     this.byLocation.put(key.identifier(), holder);
/* 127 */     this.byValue.put(value, holder);
/*     */     
/* 129 */     int newId = this.byId.size();
/* 130 */     this.byId.add(holder);
/* 131 */     this.toId.put(value, newId);
/*     */     
/* 133 */     this.registrationInfos.put(key, registrationInfo);
/* 134 */     this.registryLifecycle = this.registryLifecycle.add(registrationInfo.lifecycle());
/* 135 */     return holder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getKey(T thing) {
/* 140 */     Holder.Reference<T> holder = this.byValue.get(thing);
/* 141 */     return (holder != null) ? holder.key().identifier() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ResourceKey<T>> getResourceKey(T thing) {
/* 146 */     return Optional.<Holder.Reference>ofNullable(this.byValue.get(thing)).map(Holder.Reference::key);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId(T thing) {
/* 151 */     return this.toId.getInt(thing);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getValue(ResourceKey<T> key) {
/* 156 */     return getValueFromNullable(this.byKey.get(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public T byId(int id) {
/* 161 */     if (id < 0 || id >= this.byId.size()) {
/* 162 */       return null;
/*     */     }
/* 164 */     return ((Holder.Reference<T>)this.byId.get(id)).value();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(int id) {
/* 169 */     if (id < 0 || id >= this.byId.size()) {
/* 170 */       return Optional.empty();
/*     */     }
/* 172 */     return Optional.ofNullable((Holder.Reference<T>)this.byId.get(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(Identifier id) {
/* 177 */     return Optional.ofNullable(this.byLocation.get(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 182 */     return Optional.ofNullable(this.byKey.get(id));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<T>> getAny() {
/* 187 */     return this.byId.isEmpty() ? Optional.<Holder.Reference<T>>empty() : Optional.<Holder.Reference<T>>of((Holder.Reference<T>)this.byId.getFirst());
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<T> wrapAsHolder(T value) {
/* 192 */     Holder.Reference<T> existingHolder = this.byValue.get(value);
/* 193 */     return (existingHolder != null) ? existingHolder : Holder.<T>direct(value);
/*     */   }
/*     */   
/*     */   private Holder.Reference<T> getOrCreateHolderOrThrow(ResourceKey<T> key) {
/* 197 */     return this.byKey.computeIfAbsent(key, id -> {
/*     */           if (this.unregisteredIntrusiveHolders != null) {
/*     */             throw new IllegalStateException("This registry can't create new holders without value");
/*     */           }
/*     */           validateWrite(id);
/*     */           return Holder.Reference.createStandAlone(this, id);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 208 */     return this.byKey.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RegistrationInfo> registrationInfo(ResourceKey<T> element) {
/* 213 */     return Optional.ofNullable(this.registrationInfos.get(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public Lifecycle registryLifecycle() {
/* 218 */     return this.registryLifecycle;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 223 */     return Iterators.transform((Iterator)this.byId.iterator(), Holder::value);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getValue(Identifier key) {
/* 228 */     Holder.Reference<T> result = this.byLocation.get(key);
/* 229 */     return getValueFromNullable(result);
/*     */   }
/*     */   
/*     */   private static <T> T getValueFromNullable(Holder.Reference<T> result) {
/* 233 */     return (result != null) ? result.value() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Identifier> keySet() {
/* 238 */     return Collections.unmodifiableSet(this.byLocation.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ResourceKey<T>> registryKeySet() {
/* 243 */     return Collections.unmodifiableSet(this.byKey.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
/* 248 */     return Collections.unmodifiableSet(Util.mapValuesLazy(this.byKey, Holder::value).entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<Holder.Reference<T>> listElements() {
/* 253 */     return this.byId.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<HolderSet.Named<T>> getTags() {
/* 258 */     return this.allTags.getTags();
/*     */   }
/*     */   
/*     */   private HolderSet.Named<T> getOrCreateTagForRegistration(TagKey<T> tag) {
/* 262 */     return this.frozenTags.computeIfAbsent(tag, this::createTag);
/*     */   }
/*     */   
/*     */   private HolderSet.Named<T> createTag(TagKey<T> tag) {
/* 266 */     return new HolderSet.Named<>(this, tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 271 */     return this.byKey.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Holder.Reference<T>> getRandom(RandomSource random) {
/* 276 */     return Util.getRandomSafe((List)this.byId, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Identifier key) {
/* 281 */     return this.byLocation.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(ResourceKey<T> key) {
/* 286 */     return this.byKey.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Registry<T> freeze() {
/* 291 */     if (this.frozen) {
/* 292 */       return this;
/*     */     }
/* 294 */     this.frozen = true;
/* 295 */     this.byValue.forEach((value, holder) -> holder.bindValue(value));
/*     */     
/* 297 */     List<Identifier> unboundEntries = this.byKey.entrySet().stream().filter(e -> !((Holder.Reference)e.getValue()).isBound()).map(e -> ((ResourceKey)e.getKey()).identifier()).sorted().toList();
/* 298 */     if (!unboundEntries.isEmpty()) {
/* 299 */       throw new IllegalStateException("Unbound values in registry " + String.valueOf(key()) + ": " + String.valueOf(unboundEntries));
/*     */     }
/* 301 */     if (this.unregisteredIntrusiveHolders != null) {
/* 302 */       if (!this.unregisteredIntrusiveHolders.isEmpty()) {
/* 303 */         throw new IllegalStateException("Some intrusive holders were not registered: " + String.valueOf(this.unregisteredIntrusiveHolders.values()));
/*     */       }
/* 305 */       this.unregisteredIntrusiveHolders = null;
/*     */     } 
/*     */     
/* 308 */     if (this.allTags.isBound())
/*     */     {
/* 310 */       throw new IllegalStateException("Tags already present before freezing");
/*     */     }
/* 312 */     List<Identifier> unboundTags = this.frozenTags.entrySet().stream().filter(e -> !((HolderSet.Named)e.getValue()).isBound()).map(e -> ((TagKey)e.getKey()).location()).sorted().toList();
/* 313 */     if (!unboundTags.isEmpty()) {
/* 314 */       throw new IllegalStateException("Unbound tags in registry " + String.valueOf(key()) + ": " + String.valueOf(unboundTags));
/*     */     }
/*     */     
/* 317 */     this.allTags = TagSet.fromMap(this.frozenTags);
/* 318 */     refreshTagsInHolders();
/* 319 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder.Reference<T> createIntrusiveHolder(T value) {
/* 324 */     if (this.unregisteredIntrusiveHolders == null) {
/* 325 */       throw new IllegalStateException("This registry can't create intrusive holders");
/*     */     }
/* 327 */     validateWrite();
/* 328 */     return this.unregisteredIntrusiveHolders.computeIfAbsent(value, v -> Holder.Reference.createIntrusive(this, (T)v));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 333 */     return this.allTags.get(id);
/*     */   }
/*     */   
/*     */   private Holder.Reference<T> validateAndUnwrapTagElement(TagKey<T> id, Holder<T> value) {
/* 337 */     if (!value.canSerializeIn(this)) {
/* 338 */       throw new IllegalStateException("Can't create named set " + String.valueOf(id) + " containing value " + String.valueOf(value) + " from outside registry " + String.valueOf(this));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 343 */     if (value instanceof Holder.Reference) { Holder.Reference<T> reference = (Holder.Reference<T>)value;
/* 344 */       return reference; }
/*     */     
/* 346 */     throw new IllegalStateException("Found direct holder " + String.valueOf(value) + " value in tag " + String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindTag(TagKey<T> id, List<Holder<T>> values) {
/* 352 */     validateWrite();
/* 353 */     getOrCreateTagForRegistration(id).bind(values);
/*     */   }
/*     */   
/*     */   private void refreshTagsInHolders() {
/* 357 */     Map<Holder.Reference<T>, List<TagKey<T>>> tagsForElement = new IdentityHashMap<>();
/* 358 */     this.byKey.values().forEach(h -> tagsForElement.put(h, new ArrayList()));
/* 359 */     this.allTags.forEach((id, values) -> {
/*     */           for (Holder<T> value : (Iterable<Holder<T>>)values) {
/*     */             Holder.Reference<T> reference = validateAndUnwrapTagElement(tagsForElement, value);
/*     */             ((List<TagKey>)tagsForElement.get(reference)).add(tagsForElement);
/*     */           } 
/*     */         });
/* 365 */     tagsForElement.forEach(Holder.Reference::bindTags);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindAllTagsToEmpty() {
/* 372 */     validateWrite();
/* 373 */     this.frozenTags.values().forEach(e -> e.bind(List.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   public HolderGetter<T> createRegistrationLookup() {
/* 378 */     validateWrite();
/* 379 */     return new HolderGetter<T>()
/*     */       {
/*     */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 382 */           return Optional.of(getOrThrow(id));
/*     */         }
/*     */ 
/*     */         
/*     */         public Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 387 */           return MappedRegistry.this.getOrCreateHolderOrThrow(id);
/*     */         }
/*     */ 
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 392 */           return Optional.of(getOrThrow(id));
/*     */         }
/*     */ 
/*     */         
/*     */         public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 397 */           return MappedRegistry.this.getOrCreateTagForRegistration(id);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Registry.PendingTags<T> prepareTagReload(TagLoader.LoadResult<T> tags) {
/* 404 */     if (!this.frozen) {
/* 405 */       throw new IllegalStateException("Invalid method used for tag loading");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     ImmutableMap.Builder<TagKey<T>, HolderSet.Named<T>> pendingTagsBuilder = ImmutableMap.builder();
/* 414 */     final Map<TagKey<T>, List<Holder<T>>> pendingContents = new HashMap<>();
/*     */     
/* 416 */     tags.tags().forEach((id, contents) -> {
/*     */           HolderSet.Named<T> tagToAdd = this.frozenTags.get(pendingTagsBuilder);
/*     */           
/*     */           if (tagToAdd == null) {
/*     */             tagToAdd = createTag(pendingTagsBuilder);
/*     */           }
/*     */           pendingTagsBuilder.put(pendingTagsBuilder, tagToAdd);
/*     */           pendingTagsBuilder.put(pendingTagsBuilder, List.copyOf(contents));
/*     */         });
/* 425 */     final ImmutableMap<TagKey<T>, HolderSet.Named<T>> pendingTags = pendingTagsBuilder.build();
/*     */     
/* 427 */     final HolderLookup.RegistryLookup<T> patchedHolder = new HolderLookup.RegistryLookup.Delegate<T>()
/*     */       {
/*     */         public HolderLookup.RegistryLookup<T> parent() {
/* 430 */           return MappedRegistry.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 435 */           return Optional.ofNullable((HolderSet.Named<T>)pendingTags.get(id));
/*     */         }
/*     */ 
/*     */         
/*     */         public Stream<HolderSet.Named<T>> listTags() {
/* 440 */           return pendingTags.values().stream();
/*     */         }
/*     */       };
/*     */     
/* 444 */     return new Registry.PendingTags<T>()
/*     */       {
/*     */         public ResourceKey<? extends Registry<? extends T>> key() {
/* 447 */           return MappedRegistry.this.key();
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 452 */           return pendingContents.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public HolderLookup.RegistryLookup<T> lookup() {
/* 457 */           return patchedHolder;
/*     */         }
/*     */ 
/*     */         
/*     */         public void apply() {
/* 462 */           pendingTags.forEach((id, tag) -> {
/*     */                 List<Holder<T>> values = (List<Holder<T>>)pendingContents.getOrDefault(id, List.of());
/*     */                 tag.bind(values);
/*     */               });
/* 466 */           MappedRegistry.this.allTags = MappedRegistry.TagSet.fromMap((Map<TagKey<T>, HolderSet.Named<T>>)pendingTags);
/* 467 */           MappedRegistry.this.refreshTagsInHolders();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static interface TagSet<T> {
/*     */     static <T> TagSet<T> unbound() {
/* 474 */       return new TagSet<T>()
/*     */         {
/*     */           public boolean isBound() {
/* 477 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 482 */             throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id));
/*     */           }
/*     */ 
/*     */           
/*     */           public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 487 */             throw new IllegalStateException("Tags not bound");
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<HolderSet.Named<T>> getTags() {
/* 492 */             throw new IllegalStateException("Tags not bound");
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static <T> TagSet<T> fromMap(final Map<TagKey<T>, HolderSet.Named<T>> tags) {
/* 498 */       return new TagSet<T>()
/*     */         {
/*     */           public boolean isBound() {
/* 501 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 506 */             return Optional.ofNullable((HolderSet.Named<T>)tags.get(id));
/*     */           }
/*     */ 
/*     */           
/*     */           public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 511 */             tags.forEach((BiConsumer)action);
/*     */           }
/*     */           
/*     */           public Stream<HolderSet.Named<T>> getTags()
/*     */           {
/* 516 */             return tags.values().stream(); } }; } boolean isBound(); Optional<HolderSet.Named<T>> get(TagKey<T> param1TagKey); void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> param1BiConsumer); Stream<HolderSet.Named<T>> getTags(); } class null implements TagSet<T> { public boolean isBound() { return false; } public Optional<HolderSet.Named<T>> get(TagKey<T> id) { throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id)); } public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) { throw new IllegalStateException("Tags not bound"); } public Stream<HolderSet.Named<T>> getTags() { throw new IllegalStateException("Tags not bound"); } } class null implements TagSet<T> { public Stream<HolderSet.Named<T>> getTags() { return tags.values().stream(); }
/*     */ 
/*     */     
/*     */     public boolean isBound() {
/*     */       return true;
/*     */     }
/*     */     
/*     */     public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*     */       return Optional.ofNullable((HolderSet.Named<T>)tags.get(id));
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/*     */       tags.forEach((BiConsumer)action);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/MappedRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */