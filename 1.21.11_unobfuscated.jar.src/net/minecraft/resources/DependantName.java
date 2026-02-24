/*   */ package net.minecraft.resources;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface DependantName<T, V> {
/*   */   V get(ResourceKey<T> paramResourceKey);
/*   */   
/*   */   static <T, V> DependantName<T, V> fixed(V value) {
/* 8 */     return id -> value;
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/DependantName.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */