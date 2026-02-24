/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JavaOps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class Cloner<T>
/*    */ {
/*    */   private final Codec<T> directCodec;
/*    */   
/*    */   private Cloner(Codec<T> directCodec) {
/* 16 */     this.directCodec = directCodec;
/*    */   }
/*    */   
/*    */   public T clone(T value, HolderLookup.Provider from, HolderLookup.Provider to) {
/* 20 */     RegistryOps<?> registryOps1 = from.createSerializationContext((DynamicOps<?>)JavaOps.INSTANCE);
/* 21 */     RegistryOps<?> registryOps2 = to.createSerializationContext((DynamicOps<?>)JavaOps.INSTANCE);
/*    */     
/* 23 */     Object serialized = this.directCodec.encodeStart((DynamicOps)registryOps1, value).getOrThrow(error -> new IllegalStateException("Failed to encode: " + error));
/* 24 */     return (T)this.directCodec.parse((DynamicOps)registryOps2, serialized).getOrThrow(error -> new IllegalStateException("Failed to decode: " + error));
/*    */   }
/*    */   
/*    */   public static class Factory {
/* 28 */     private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap<>();
/*    */     
/*    */     public <T> Factory addCodec(ResourceKey<? extends Registry<? extends T>> key, Codec<T> codec) {
/* 31 */       this.codecs.put(key, new Cloner(codec));
/* 32 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> Cloner<T> cloner(ResourceKey<? extends Registry<? extends T>> key) {
/* 37 */       return (Cloner<T>)this.codecs.get(key);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/Cloner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */