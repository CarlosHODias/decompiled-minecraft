/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface HolderGetter<T>
/*    */ {
/*    */   default Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 16 */     return get(id).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 22 */     return get(id).orElseThrow(() -> new IllegalStateException("Missing tag " + String.valueOf(id)));
/*    */   }
/*    */   Optional<Holder.Reference<T>> get(ResourceKey<T> paramResourceKey);
/*    */   default Optional<Holder<T>> getRandomElementOf(TagKey<T> tag, RandomSource random) {
/* 26 */     return get(tag).flatMap(holderSet -> holderSet.getRandomElement(random));
/*    */   }
/*    */   
/*    */   Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey);
/*    */   
/*    */   public static interface Provider {
/*    */     default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> key) {
/* 33 */       return (HolderGetter<T>)lookup(key).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(key.identifier()) + " not found"));
/*    */     }
/*    */     
/*    */     default <T> Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 37 */       return lookup(id.registryKey()).flatMap(l -> l.get(id));
/*    */     }
/*    */     
/*    */     default <T> Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 41 */       return (Holder.Reference<T>)lookup(id.registryKey()).flatMap(l -> l.get(id)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(id)));
/*    */     }
/*    */     
/*    */     <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/HolderGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */