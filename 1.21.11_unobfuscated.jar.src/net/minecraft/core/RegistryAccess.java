/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface RegistryAccess
/*     */   extends HolderLookup.Provider {
/*  15 */   public static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <E> Registry<E> lookupOrThrow(ResourceKey<? extends Registry<? extends E>> name) {
/*  22 */     return (Registry<E>)lookup(name).orElseThrow(() -> new IllegalStateException("Missing registry: " + String.valueOf(name)));
/*     */   }
/*     */   public static final class RegistryEntry<T> extends Record { private final ResourceKey<? extends Registry<T>> key; private final Registry<T> value;
/*  25 */     public RegistryEntry(ResourceKey<? extends Registry<T>> key, Registry<T> value) { this.key = key; this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistryAccess$RegistryEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  25 */       //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry<TT;>; } public ResourceKey<? extends Registry<T>> key() { return this.key; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistryAccess$RegistryEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistryAccess$RegistryEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  25 */       //   0	8	0	this	Lnet/minecraft/core/RegistryAccess$RegistryEntry<TT;>; } public Registry<T> value() { return this.value; }
/*     */      private static <T, R extends Registry<? extends T>> RegistryEntry<T> fromMapEntry(Map.Entry<? extends ResourceKey<? extends Registry<?>>, R> e) {
/*  27 */       return fromUntyped(e.getKey(), (Registry)e.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static <T> RegistryEntry<T> fromUntyped(ResourceKey<? extends Registry<?>> key, Registry<?> value) {
/*  33 */       return new RegistryEntry<>((ResourceKey)key, (Registry)value);
/*     */     }
/*     */     
/*     */     private RegistryEntry<T> freeze() {
/*  37 */       return new RegistryEntry(this.key, this.value.freeze());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
/*  45 */     return registries().map(e -> e.key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ImmutableRegistryAccess
/*     */     implements RegistryAccess
/*     */   {
/*     */     private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries;
/*     */ 
/*     */     
/*     */     public ImmutableRegistryAccess(List<? extends Registry<?>> registries) {
/*  57 */       this.registries = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>)registries.stream().collect(Collectors.toUnmodifiableMap(Registry::key, v -> v));
/*     */     }
/*     */     
/*     */     public ImmutableRegistryAccess(Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries) {
/*  61 */       this.registries = Map.copyOf(registries);
/*     */     }
/*     */     
/*     */     public ImmutableRegistryAccess(Stream<RegistryAccess.RegistryEntry<?>> entries) {
/*  65 */       this.registries = entries.collect(ImmutableMap.toImmutableMap(RegistryAccess.RegistryEntry::key, RegistryAccess.RegistryEntry::value));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <E> Optional<Registry<E>> lookup(ResourceKey<? extends Registry<? extends E>> registryKey) {
/*  71 */       return Optional.<Registry>ofNullable(this.registries.get(registryKey)).map(r -> r);
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<RegistryAccess.RegistryEntry<?>> registries() {
/*  76 */       return this.registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry);
/*     */     }
/*     */   }
/*     */   
/*     */   static Frozen fromRegistryOfRegistries(final Registry<? extends Registry<?>> registries) {
/*  81 */     return new Frozen()
/*     */       {
/*     */         public <T> Optional<Registry<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryKey)
/*     */         {
/*  85 */           Registry<T> registry = registries;
/*  86 */           return (Optional)registry.getOptional((ResourceKey)registryKey);
/*     */         }
/*     */ 
/*     */         
/*     */         public Stream<RegistryAccess.RegistryEntry<?>> registries() {
/*  91 */           return registries.entrySet().stream().map(RegistryAccess.RegistryEntry::fromMapEntry);
/*     */         }
/*     */ 
/*     */         
/*     */         public RegistryAccess.Frozen freeze() {
/*  96 */           return this;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 101 */   public static final Frozen EMPTY = new ImmutableRegistryAccess(Map.of()).freeze();
/*     */   
/*     */   default Frozen freeze() {
/*     */     class FrozenAccess extends ImmutableRegistryAccess implements Frozen {
/*     */       protected FrozenAccess(RegistryAccess this$0, Stream<RegistryAccess.RegistryEntry<?>> entries) {
/* 106 */         super(entries);
/*     */       }
/*     */     };
/*     */     
/* 110 */     return new FrozenAccess(this, registries().map(RegistryEntry::freeze));
/*     */   }
/*     */   
/*     */   <E> Optional<Registry<E>> lookup(ResourceKey<? extends Registry<? extends E>> paramResourceKey);
/*     */   
/*     */   Stream<RegistryEntry<?>> registries();
/*     */   
/*     */   public static interface Frozen extends RegistryAccess {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/RegistryAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */