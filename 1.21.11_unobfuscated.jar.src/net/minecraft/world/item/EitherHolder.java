/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class EitherHolder<T> extends Record {
/*    */   private final Either<Holder<T>, ResourceKey<T>> contents;
/*    */   
/* 18 */   public EitherHolder(Either<Holder<T>, ResourceKey<T>> contents) { this.contents = contents; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/EitherHolder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/EitherHolder;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/item/EitherHolder<TT;>; } public Either<Holder<T>, ResourceKey<T>> contents() { return this.contents; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/EitherHolder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/EitherHolder;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/EitherHolder<TT;>; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/EitherHolder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/EitherHolder;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 20 */     //   0	8	0	this	Lnet/minecraft/world/item/EitherHolder<TT;>; } public static <T> Codec<EitherHolder<T>> codec(ResourceKey<Registry<T>> registry, Codec<Holder<T>> holderCodec) { return Codec.either(holderCodec, 
/*    */         
/* 22 */         ResourceKey.codec(registry).comapFlatMap(key -> DataResult.error(()), Function.identity()))
/* 23 */       .xmap(EitherHolder::new, EitherHolder::contents); }
/*    */ 
/*    */   
/*    */   public static <T> StreamCodec<RegistryFriendlyByteBuf, EitherHolder<T>> streamCodec(ResourceKey<Registry<T>> registry, StreamCodec<RegistryFriendlyByteBuf, Holder<T>> streamHolderCodec) {
/* 27 */     return StreamCodec.composite(
/* 28 */         net.minecraft.network.codec.ByteBufCodecs.either(streamHolderCodec, ResourceKey.streamCodec(registry)), EitherHolder::contents, EitherHolder::new);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EitherHolder(Holder<T> holder) {
/* 34 */     this(Either.left(holder));
/*    */   }
/*    */   
/*    */   public EitherHolder(ResourceKey<T> key) {
/* 38 */     this(Either.right(key));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<T> unwrap(Registry<T> registry) {
/* 44 */     java.util.Objects.requireNonNull(registry); return (Optional<T>)this.contents.map(holder -> Optional.of(holder.value()), registry::getOptional);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Holder<T>> unwrap(HolderLookup.Provider provider) {
/* 49 */     return (Optional<Holder<T>>)this.contents.map(Optional::of, key -> provider.get(key).map(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<ResourceKey<T>> key() {
/* 56 */     return (Optional<ResourceKey<T>>)this.contents.map(Holder::unwrapKey, Optional::of);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/EitherHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */