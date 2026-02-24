/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderOwner;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RegistryFileCodec<E>
/*    */   implements Codec<Holder<E>>
/*    */ {
/*    */   private final ResourceKey<? extends Registry<E>> registryKey;
/*    */   private final Codec<E> elementCodec;
/*    */   private final boolean allowInline;
/*    */   
/*    */   public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
/* 26 */     return create(registryKey, elementCodec, true);
/*    */   }
/*    */   
/*    */   public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean allowInline) {
/* 30 */     return new RegistryFileCodec<>(registryKey, elementCodec, allowInline);
/*    */   }
/*    */   
/*    */   private RegistryFileCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean allowInline) {
/* 34 */     this.registryKey = registryKey;
/* 35 */     this.elementCodec = elementCodec;
/* 36 */     this.allowInline = allowInline;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Holder<E> input, DynamicOps<T> ops, T prefix) {
/* 41 */     if (ops instanceof RegistryOps) { RegistryOps<?> registryOps = (RegistryOps)ops;
/* 42 */       Optional<HolderOwner<E>> maybeOwner = registryOps.owner(this.registryKey);
/* 43 */       if (maybeOwner.isPresent()) {
/* 44 */         if (!input.canSerializeIn(maybeOwner.get())) {
/* 45 */           return DataResult.error(() -> "Element " + String.valueOf(input) + " is not valid in current registry set");
/*    */         }
/* 47 */         return (DataResult<T>)input.unwrap().map(id -> Identifier.CODEC.encode(id.identifier(), ops, prefix), value -> this.elementCodec.encode(prefix, ops, ops));
/*    */       }  }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     return this.elementCodec.encode(input.value(), ops, prefix);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Holder<E>, T>> decode(DynamicOps<T> ops, T input) {
/* 59 */     if (ops instanceof RegistryOps) { RegistryOps<?> registryOps = (RegistryOps)ops;
/* 60 */       Optional<HolderGetter<E>> maybeLookup = registryOps.getter(this.registryKey);
/* 61 */       if (maybeLookup.isEmpty()) {
/* 62 */         return DataResult.error(() -> "Registry does not exist: " + String.valueOf(this.registryKey));
/*    */       }
/*    */       
/* 65 */       HolderGetter<E> lookup = maybeLookup.get();
/*    */       
/* 67 */       DataResult<Pair<Identifier, T>> decoded = Identifier.CODEC.decode(ops, input);
/* 68 */       if (decoded.result().isEmpty()) {
/* 69 */         if (!this.allowInline) {
/* 70 */           return DataResult.error(() -> "Inline definitions not allowed here");
/*    */         }
/* 72 */         return this.elementCodec.decode(ops, input).map(p -> p.mapFirst(Holder::direct));
/*    */       } 
/*    */       
/* 75 */       Pair<Identifier, T> pair = decoded.result().get();
/* 76 */       ResourceKey<E> elementKey = ResourceKey.create(this.registryKey, (Identifier)pair.getFirst());
/* 77 */       return ((DataResult)lookup.get(elementKey)
/* 78 */         .map(DataResult::success).orElseGet(() -> DataResult.error(())))
/* 79 */         .map(h -> Pair.of(h, pair.getSecond())).setLifecycle(Lifecycle.stable()); }
/*    */     
/* 81 */     return this.elementCodec.decode(ops, input).map(p -> p.mapFirst(Holder::direct));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return "RegistryFileCodec[" + String.valueOf(this.registryKey) + " " + String.valueOf(this.elementCodec) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/RegistryFileCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */