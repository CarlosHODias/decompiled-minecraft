/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.google.common.cache.LoadingCache;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ public class EncoderCache
/*    */ {
/*    */   private final LoadingCache<Key<?, ?>, DataResult<?>> cache;
/*    */   
/*    */   public EncoderCache(int maximumSize) {
/* 17 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 22 */       .cache = CacheBuilder.newBuilder().maximumSize(maximumSize).concurrencyLevel(1).softValues().build(new CacheLoader<Key<?, ?>, DataResult<?>>(this)
/*    */         {
/*    */           public DataResult<?> load(EncoderCache.Key<?, ?> key) {
/* 25 */             return key.resolve();
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public <A> Codec<A> wrap(final Codec<A> codec) {
/* 31 */     return new Codec<A>()
/*    */       {
/*    */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 34 */           return codec.decode(ops, input);
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 40 */           return ((DataResult)EncoderCache.this.cache.getUnchecked(new EncoderCache.Key<>(codec, input, ops))).map(value -> {
/*    */                 if (value instanceof Tag) {
/*    */                   Tag tag = (Tag)value;
/*    */                   return tag.copy();
/*    */                 } 
/*    */                 return value;
/*    */               });
/*    */         }
/*    */       };
/*    */   }
/*    */   private static final class Key<A, T> extends Record { private final Codec<A> codec; private final A value; private final DynamicOps<T> ops;
/* 51 */     private Key(Codec<A> codec, A value, DynamicOps<T> ops) { this.codec = codec; this.value = value; this.ops = ops; } public Codec<A> codec() { return this.codec; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/EncoderCache$Key;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/EncoderCache$Key;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 51 */       //   0	7	0	this	Lnet/minecraft/util/EncoderCache$Key<TA;TT;>; } public A value() { return this.value; } public DynamicOps<T> ops() { return this.ops; }
/*    */      public DataResult<T> resolve() {
/* 53 */       return this.codec.encodeStart(this.ops, this.value);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object obj) {
/* 58 */       if (this == obj) {
/* 59 */         return true;
/*    */       }
/* 61 */       if (obj instanceof Key) { Key<?, ?> key = (Key<?, ?>)obj;
/* 62 */         return (this.codec == key.codec && this.value.equals(key.value) && this.ops.equals(key.ops)); }
/*    */       
/* 64 */       return false;
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 69 */       int result = System.identityHashCode(this.codec);
/* 70 */       result = 31 * result + this.value.hashCode();
/* 71 */       result = 31 * result + this.ops.hashCode();
/* 72 */       return result;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/EncoderCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */