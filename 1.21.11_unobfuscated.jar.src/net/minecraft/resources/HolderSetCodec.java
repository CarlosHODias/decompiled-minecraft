/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class HolderSetCodec<E>
/*     */   implements Codec<HolderSet<E>> {
/*     */   private final ResourceKey<? extends Registry<E>> registryKey;
/*     */   private final Codec<Holder<E>> elementCodec;
/*     */   private final Codec<List<Holder<E>>> homogenousListCodec;
/*     */   private final Codec<Either<TagKey<E>, List<Holder<E>>>> registryAwareCodec;
/*     */   
/*     */   private static <E> Codec<List<Holder<E>>> homogenousList(Codec<Holder<E>> elementCodec, boolean alwaysUseList) {
/*  27 */     Codec<List<Holder<E>>> listCodec = elementCodec.listOf().validate(ExtraCodecs.ensureHomogenous(Holder::kind));
/*     */     
/*  29 */     if (alwaysUseList) {
/*  30 */       return listCodec;
/*     */     }
/*     */     
/*  33 */     return ExtraCodecs.compactListCodec(elementCodec, listCodec);
/*     */   }
/*     */   
/*     */   public static <E> Codec<HolderSet<E>> create(ResourceKey<? extends Registry<E>> registryKey, Codec<Holder<E>> elementCodec, boolean alwaysUseList) {
/*  37 */     return new HolderSetCodec<>(registryKey, elementCodec, alwaysUseList);
/*     */   }
/*     */   
/*     */   private HolderSetCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<Holder<E>> elementCodec, boolean alwaysUseList) {
/*  41 */     this.registryKey = registryKey;
/*  42 */     this.elementCodec = elementCodec;
/*  43 */     this.homogenousListCodec = homogenousList(elementCodec, alwaysUseList);
/*  44 */     this.registryAwareCodec = Codec.either(
/*  45 */         TagKey.hashedCodec(registryKey), this.homogenousListCodec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> DataResult<Pair<HolderSet<E>, T>> decode(DynamicOps<T> ops, T input) {
/*  52 */     if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps<T>)ops;
/*  53 */       Optional<HolderGetter<E>> registryOptional = registryOps.getter(this.registryKey);
/*  54 */       if (registryOptional.isPresent()) {
/*  55 */         HolderGetter<E> registry = registryOptional.get();
/*  56 */         return 
/*  57 */           this.registryAwareCodec.decode(ops, input)
/*  58 */           .flatMap(p -> {
/*     */               DataResult<HolderSet<E>> result = (DataResult<HolderSet<E>>)((Either)p.getFirst()).map((), ());
/*     */ 
/*     */               
/*     */               return result.map(());
/*     */             });
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/*  68 */     return decodeWithoutRegistry(ops, input);
/*     */   }
/*     */   
/*     */   private static <E> DataResult<HolderSet<E>> lookupTag(HolderGetter<E> registry, TagKey<E> key) {
/*  72 */     return registry.get(key)
/*  73 */       .map(DataResult::success)
/*  74 */       .orElseGet(() -> DataResult.error(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<T> encode(HolderSet<E> input, DynamicOps<T> ops, T prefix) {
/*  79 */     if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps<T>)ops;
/*  80 */       Optional<HolderOwner<E>> maybeOwner = registryOps.owner(this.registryKey);
/*  81 */       if (maybeOwner.isPresent()) {
/*  82 */         if (!input.canSerializeIn(maybeOwner.get())) {
/*  83 */           return DataResult.error(() -> "HolderSet " + String.valueOf(input) + " is not valid in current registry set");
/*     */         }
/*  85 */         return this.registryAwareCodec.encode(input.unwrap().mapRight(List::copyOf), ops, prefix);
/*     */       }  }
/*     */ 
/*     */ 
/*     */     
/*  90 */     return encodeWithoutRegistry(input, ops, prefix);
/*     */   }
/*     */   
/*     */   private <T> DataResult<Pair<HolderSet<E>, T>> decodeWithoutRegistry(DynamicOps<T> ops, T input) {
/*  94 */     return this.elementCodec.listOf().decode(ops, input).flatMap(p -> {
/*     */           List<Holder.Direct<E>> directHolders = new ArrayList<>();
/*     */           for (Holder<E> holder : (Iterable<Holder<E>>)p.getFirst()) {
/*     */             if (holder instanceof Holder.Direct) {
/*     */               Holder.Direct<E> direct = (Holder.Direct<E>)holder;
/*     */               directHolders.add(direct);
/*     */               continue;
/*     */             } 
/*     */             return DataResult.error(());
/*     */           } 
/*     */           return DataResult.success(new Pair(HolderSet.direct(directHolders), p.getSecond()));
/*     */         });
/*     */   }
/*     */   private <T> DataResult<T> encodeWithoutRegistry(HolderSet<E> input, DynamicOps<T> ops, T prefix) {
/* 108 */     return this.homogenousListCodec.encode(input.stream().toList(), ops, prefix);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/HolderSetCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */