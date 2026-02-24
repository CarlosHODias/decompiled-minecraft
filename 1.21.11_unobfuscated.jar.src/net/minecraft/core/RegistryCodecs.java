/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.resources.HolderSetCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistryCodecs {
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
/* 11 */     return homogeneousList(registryKey, elementCodec, false);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean alwaysUseList) {
/* 15 */     return HolderSetCodec.create(registryKey, (Codec)RegistryFileCodec.create(registryKey, elementCodec), alwaysUseList);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey) {
/* 19 */     return homogeneousList(registryKey, false);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, boolean alwaysUseList) {
/* 23 */     return HolderSetCodec.create(registryKey, (Codec)RegistryFixedCodec.create(registryKey), alwaysUseList);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/RegistryCodecs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */