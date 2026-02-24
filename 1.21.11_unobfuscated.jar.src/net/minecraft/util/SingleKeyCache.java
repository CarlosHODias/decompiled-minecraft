/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleKeyCache<K, V>
/*    */ {
/*    */   private final Function<K, V> computeValue;
/* 15 */   private K cacheKey = null;
/*    */   private V cachedValue;
/*    */   
/*    */   public SingleKeyCache(Function<K, V> computeValue) {
/* 19 */     this.computeValue = computeValue;
/*    */   }
/*    */   
/*    */   public V getValue(K cacheKey) {
/* 23 */     if (this.cachedValue == null || !Objects.equals(this.cacheKey, cacheKey)) {
/* 24 */       this.cachedValue = this.computeValue.apply(cacheKey);
/* 25 */       this.cacheKey = cacheKey;
/*    */     } 
/* 27 */     return this.cachedValue;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/SingleKeyCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */