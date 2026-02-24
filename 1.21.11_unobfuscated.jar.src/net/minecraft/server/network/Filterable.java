/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class Filterable<T> extends Record {
/*    */   private final T raw;
/*    */   private final Optional<T> filtered;
/*    */   
/* 12 */   public Filterable(T raw, Optional<T> filtered) { this.raw = raw; this.filtered = filtered; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/Filterable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable<TT;>; } public T raw() { return this.raw; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/Filterable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/Filterable<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/Filterable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/Filterable;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/server/network/Filterable<TT;>; } public Optional<T> filtered() { return this.filtered; }
/*    */    public static <T> Codec<Filterable<T>> codec(Codec<T> valueCodec) {
/* 14 */     Codec<Filterable<T>> fullCodec = RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)valueCodec.fieldOf("raw").forGetter(Filterable::raw), (com.mojang.datafixers.kinds.App)valueCodec.optionalFieldOf("filtered").forGetter(Filterable::filtered)).apply((com.mojang.datafixers.kinds.Applicative)i, Filterable::new));
/*    */ 
/*    */ 
/*    */     
/* 18 */     Codec<Filterable<T>> simpleCodec = valueCodec.xmap(Filterable::passThrough, Filterable::raw);
/* 19 */     return Codec.withAlternative(fullCodec, simpleCodec);
/*    */   }
/*    */   
/*    */   public static <B extends io.netty.buffer.ByteBuf, T> net.minecraft.network.codec.StreamCodec<B, Filterable<T>> streamCodec(net.minecraft.network.codec.StreamCodec<B, T> valueCodec) {
/* 23 */     return net.minecraft.network.codec.StreamCodec.composite(valueCodec, Filterable::raw, 
/*    */         
/* 25 */         valueCodec.apply(net.minecraft.network.codec.ByteBufCodecs::optional), Filterable::filtered, Filterable::new);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Filterable<T> passThrough(T value) {
/* 31 */     return new Filterable<>(value, Optional.empty());
/*    */   }
/*    */   
/*    */   public static Filterable<String> from(FilteredText text) {
/* 35 */     return new Filterable<>(
/* 36 */         text.raw(), 
/* 37 */         text.isFiltered() ? Optional.<String>of(text.filteredOrEmpty()) : Optional.<String>empty());
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(boolean filterEnabled) {
/* 42 */     if (filterEnabled) {
/* 43 */       return this.filtered.orElse(this.raw);
/*    */     }
/* 45 */     return this.raw;
/*    */   }
/*    */   
/*    */   public <U> Filterable<U> map(Function<T, U> function) {
/* 49 */     return new Filterable((T)
/* 50 */         function.apply(this.raw), 
/* 51 */         this.filtered.map((Function)function));
/*    */   }
/*    */ 
/*    */   
/*    */   public <U> Optional<Filterable<U>> resolve(Function<T, Optional<U>> function) {
/* 56 */     Optional<U> newRaw = function.apply(this.raw);
/* 57 */     if (newRaw.isEmpty()) {
/* 58 */       return Optional.empty();
/*    */     }
/* 60 */     if (this.filtered.isPresent()) {
/* 61 */       Optional<U> newFiltered = function.apply(this.filtered.get());
/* 62 */       if (newFiltered.isEmpty()) {
/* 63 */         return Optional.empty();
/*    */       }
/* 65 */       return Optional.of(new Filterable((T)newRaw.get(), (Optional)newFiltered));
/*    */     } 
/* 67 */     return Optional.of(new Filterable((T)newRaw.get(), Optional.empty()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/Filterable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */