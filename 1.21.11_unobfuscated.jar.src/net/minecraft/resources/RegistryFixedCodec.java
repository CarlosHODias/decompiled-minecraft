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
/*    */ public final class RegistryFixedCodec<E>
/*    */   implements Codec<Holder<E>> {
/*    */   private final ResourceKey<? extends Registry<E>> registryKey;
/*    */   
/*    */   public static <E> RegistryFixedCodec<E> create(ResourceKey<? extends Registry<E>> registryKey) {
/* 19 */     return new RegistryFixedCodec<>(registryKey);
/*    */   }
/*    */   
/*    */   private RegistryFixedCodec(ResourceKey<? extends Registry<E>> registryKey) {
/* 23 */     this.registryKey = registryKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Holder<E> input, DynamicOps<T> ops, T prefix) {
/* 28 */     if (ops instanceof RegistryOps) { RegistryOps<?> registryOps = (RegistryOps)ops;
/* 29 */       Optional<HolderOwner<E>> maybeOwner = registryOps.owner(this.registryKey);
/* 30 */       if (maybeOwner.isPresent()) {
/* 31 */         if (!input.canSerializeIn(maybeOwner.get())) {
/* 32 */           return DataResult.error(() -> "Element " + String.valueOf(input) + " is not valid in current registry set");
/*    */         }
/* 34 */         return (DataResult<T>)input.unwrap().map(id -> Identifier.CODEC.encode(id.identifier(), ops, prefix), value -> DataResult.error(()));
/*    */       }  }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     return DataResult.error(() -> "Can't access registry " + String.valueOf(this.registryKey));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Holder<E>, T>> decode(DynamicOps<T> ops, T input) {
/* 46 */     if (ops instanceof RegistryOps) { RegistryOps<?> registryOps = (RegistryOps)ops;
/* 47 */       Optional<HolderGetter<E>> lookup = registryOps.getter(this.registryKey);
/* 48 */       if (lookup.isPresent()) {
/* 49 */         return Identifier.CODEC.decode(ops, input).flatMap(pair -> {
/*    */               Identifier id = (Identifier)lookup.getFirst();
/*    */               
/*    */               return ((DataResult)((HolderGetter)lookup.get()).get(ResourceKey.create(this.registryKey, id)).map(DataResult::success).orElseGet(())).map(()).setLifecycle(Lifecycle.stable());
/*    */             });
/*    */       } }
/*    */ 
/*    */     
/* 57 */     return DataResult.error(() -> "Can't access registry " + String.valueOf(this.registryKey));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "RegistryFixedCodec[" + String.valueOf(this.registryKey) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/RegistryFixedCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */