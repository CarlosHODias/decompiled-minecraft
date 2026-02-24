/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ public class RegistryOps<T> extends DelegatingOps<T> {
/*     */   private final RegistryInfoLookup lookupProvider;
/*     */   
/*     */   public static final class RegistryInfo<T> extends Record {
/*     */     private final HolderOwner<T> owner;
/*     */     private final HolderGetter<T> getter;
/*     */     private final com.mojang.serialization.Lifecycle elementsLifecycle;
/*     */     
/*  20 */     public RegistryInfo(HolderOwner<T> owner, HolderGetter<T> getter, com.mojang.serialization.Lifecycle elementsLifecycle) { this.owner = owner; this.getter = getter; this.elementsLifecycle = elementsLifecycle; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  20 */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; } public HolderOwner<T> owner() { return this.owner; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #20	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  20 */       //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>; } public HolderGetter<T> getter() { return this.getter; } public com.mojang.serialization.Lifecycle elementsLifecycle() { return this.elementsLifecycle; }
/*     */      public static <T> RegistryInfo<T> fromRegistryLookup(HolderLookup.RegistryLookup<T> registry) {
/*  22 */       return new RegistryInfo<>((HolderOwner<T>)registry, (HolderGetter<T>)registry, registry.registryLifecycle());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> RegistryOps<T> create(DynamicOps<T> parent, HolderLookup.Provider lookupProvider) {
/*  34 */     return create(parent, new HolderLookupAdapter(lookupProvider));
/*     */   }
/*     */   
/*     */   public static <T> RegistryOps<T> create(DynamicOps<T> parent, RegistryInfoLookup lookupProvider) {
/*  38 */     return new RegistryOps<>(parent, lookupProvider);
/*     */   }
/*     */   
/*     */   public static <T> Dynamic<T> injectRegistryContext(Dynamic<T> dynamic, HolderLookup.Provider lookupProvider) {
/*  42 */     return new Dynamic(lookupProvider.createSerializationContext(dynamic.getOps()), dynamic.getValue());
/*     */   }
/*     */   
/*     */   private RegistryOps(DynamicOps<T> parent, RegistryInfoLookup lookupProvider) {
/*  46 */     super(parent);
/*  47 */     this.lookupProvider = lookupProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> RegistryOps<U> withParent(DynamicOps<U> parent) {
/*  52 */     if (parent == this.delegate) {
/*  53 */       return (RegistryOps)this;
/*     */     }
/*  55 */     return new RegistryOps((DynamicOps)parent, this.lookupProvider);
/*     */   }
/*     */   
/*     */   public <E> Optional<HolderOwner<E>> owner(ResourceKey<? extends Registry<? extends E>> registryKey) {
/*  59 */     return this.lookupProvider.<E>lookup(registryKey).map(RegistryInfo::owner);
/*     */   }
/*     */   
/*     */   public <E> Optional<HolderGetter<E>> getter(ResourceKey<? extends Registry<? extends E>> registryKey) {
/*  63 */     return this.lookupProvider.<E>lookup(registryKey).map(RegistryInfo::getter);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  68 */     if (this == obj) {
/*  69 */       return true;
/*     */     }
/*  71 */     if (obj == null || getClass() != obj.getClass()) {
/*  72 */       return false;
/*     */     }
/*  74 */     RegistryOps<?> ops = (RegistryOps)obj;
/*  75 */     return (this.delegate.equals(ops.delegate) && this.lookupProvider.equals(ops.lookupProvider));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  80 */     return this.delegate.hashCode() * 31 + this.lookupProvider.hashCode();
/*     */   }
/*     */   
/*     */   public static <E, O> com.mojang.serialization.codecs.RecordCodecBuilder<O, HolderGetter<E>> retrieveGetter(ResourceKey<? extends Registry<? extends E>> registryKey) {
/*  84 */     return net.minecraft.util.ExtraCodecs.retrieveContext(ops -> {
/*     */           if (ops instanceof RegistryOps) {
/*     */             RegistryOps<?> registryOps = (RegistryOps)ops;
/*     */             
/*     */             return registryOps.lookupProvider.lookup(registryKey).map(()).orElseGet(());
/*     */           } 
/*     */           return DataResult.error(());
/*  91 */         }).forGetter(e -> null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E, O> com.mojang.serialization.codecs.RecordCodecBuilder<O, net.minecraft.core.Holder.Reference<E>> retrieveElement(ResourceKey<E> key) {
/*  99 */     ResourceKey<? extends Registry<E>> registryKey = (ResourceKey)ResourceKey.createRegistryKey(key.registry());
/* 100 */     return net.minecraft.util.ExtraCodecs.retrieveContext(ops -> {
/*     */           if (ops instanceof RegistryOps) {
/*     */             RegistryOps<?> registryOps = (RegistryOps)ops;
/*     */             
/*     */             return registryOps.lookupProvider.lookup(registryKey).flatMap(()).map(DataResult::success).orElseGet(());
/*     */           } 
/*     */           
/*     */           return DataResult.error(());
/* 108 */         }).forGetter(e -> null);
/*     */   }
/*     */   public static interface RegistryInfoLookup {
/*     */     <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey); }
/*     */   private static final class HolderLookupAdapter implements RegistryInfoLookup { private final HolderLookup.Provider lookupProvider;
/* 113 */     private final java.util.Map<ResourceKey<? extends Registry<?>>, Optional<? extends RegistryOps.RegistryInfo<?>>> lookups = new java.util.concurrent.ConcurrentHashMap<>();
/*     */     
/*     */     public HolderLookupAdapter(HolderLookup.Provider lookupProvider) {
/* 116 */       this.lookupProvider = lookupProvider;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <E> Optional<RegistryOps.RegistryInfo<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) {
/* 122 */       return (Optional<RegistryOps.RegistryInfo<E>>)this.lookups.computeIfAbsent(registryKey, this::createLookup);
/*     */     }
/*     */     
/*     */     private Optional<RegistryOps.RegistryInfo<Object>> createLookup(ResourceKey<? extends Registry<?>> key) {
/* 126 */       return this.lookupProvider.lookup(key).map(RegistryOps.RegistryInfo::fromRegistryLookup);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 131 */       if (this == obj) {
/* 132 */         return true;
/*     */       }
/* 134 */       if (obj instanceof HolderLookupAdapter) { HolderLookupAdapter adapter = (HolderLookupAdapter)obj; if (this.lookupProvider.equals(adapter.lookupProvider)); }  return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 139 */       return this.lookupProvider.hashCode();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/RegistryOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */