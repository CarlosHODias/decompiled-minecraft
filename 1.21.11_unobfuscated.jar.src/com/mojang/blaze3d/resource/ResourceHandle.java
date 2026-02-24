/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ public interface ResourceHandle<T>
/*    */ {
/*    */   public static final ResourceHandle<?> INVALID_HANDLE = () -> {
/*    */       throw new IllegalStateException("Cannot dereference handle with no underlying resource");
/*    */     };
/*    */   
/*    */   static <T> ResourceHandle<T> invalid() {
/* 10 */     return (ResourceHandle)INVALID_HANDLE;
/*    */   }
/*    */   
/*    */   T get();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/resource/ResourceHandle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */