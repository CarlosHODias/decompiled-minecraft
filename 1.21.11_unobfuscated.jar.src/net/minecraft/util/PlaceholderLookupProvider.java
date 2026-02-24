/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public class PlaceholderLookupProvider
/*     */   implements HolderGetter.Provider {
/*     */   private final HolderLookup.Provider context;
/*  24 */   private final UniversalLookup lookup = new UniversalLookup();
/*     */   
/*  26 */   private final Map<ResourceKey<Object>, Holder.Reference<Object>> holders = new HashMap<>();
/*  27 */   private final Map<TagKey<Object>, HolderSet.Named<Object>> holderSets = new HashMap<>();
/*     */   
/*     */   public PlaceholderLookupProvider(HolderLookup.Provider context) {
/*  30 */     this.context = context;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
/*  35 */     return Optional.of(this.lookup.castAsLookup());
/*     */   }
/*     */   
/*     */   public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> parent) {
/*  39 */     return RegistryOps.create(parent, new RegistryOps.RegistryInfoLookup()
/*     */         {
/*     */           public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
/*  42 */             return PlaceholderLookupProvider.this.context.lookup(registryKey)
/*  43 */               .map(RegistryOps.RegistryInfo::fromRegistryLookup)
/*  44 */               .or(() -> Optional.of(new RegistryOps.RegistryInfo(PlaceholderLookupProvider.this.lookup.castAsOwner(), PlaceholderLookupProvider.this.lookup.castAsLookup(), Lifecycle.experimental())));
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public RegistryContextSwapper createSwapper() {
/*  50 */     return new RegistryContextSwapper()
/*     */       {
/*     */         public <T> DataResult<T> swapTo(Codec<T> codec, T value, HolderLookup.Provider newContext) {
/*  53 */           return 
/*  54 */             codec.encodeStart((DynamicOps)PlaceholderLookupProvider.this.createSerializationContext((DynamicOps<?>)JavaOps.INSTANCE), value)
/*  55 */             .flatMap(v -> codec.parse((DynamicOps)newContext.createSerializationContext((DynamicOps)JavaOps.INSTANCE), v));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean hasRegisteredPlaceholders() {
/*  61 */     return (!this.holders.isEmpty() || !this.holderSets.isEmpty());
/*     */   }
/*     */   
/*     */   private class UniversalLookup
/*     */     implements HolderGetter<Object>, HolderOwner<Object> {
/*     */     public Optional<Holder.Reference<Object>> get(ResourceKey<Object> id) {
/*  67 */       return Optional.of(getOrCreate(id));
/*     */     }
/*     */ 
/*     */     
/*     */     public Holder.Reference<Object> getOrThrow(ResourceKey<Object> id) {
/*  72 */       return getOrCreate(id);
/*     */     }
/*     */     
/*     */     private Holder.Reference<Object> getOrCreate(ResourceKey<Object> id) {
/*  76 */       return PlaceholderLookupProvider.this.holders.computeIfAbsent(id, k -> Holder.Reference.createStandAlone(this, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<HolderSet.Named<Object>> get(TagKey<Object> id) {
/*  81 */       return Optional.of(getOrCreate(id));
/*     */     }
/*     */ 
/*     */     
/*     */     public HolderSet.Named<Object> getOrThrow(TagKey<Object> id) {
/*  86 */       return getOrCreate(id);
/*     */     }
/*     */     
/*     */     private HolderSet.Named<Object> getOrCreate(TagKey<Object> id) {
/*  90 */       return PlaceholderLookupProvider.this.holderSets.computeIfAbsent(id, k -> HolderSet.emptyNamed(this, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> HolderGetter<T> castAsLookup() {
/*  95 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> HolderOwner<T> castAsOwner() {
/* 100 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/PlaceholderLookupProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */