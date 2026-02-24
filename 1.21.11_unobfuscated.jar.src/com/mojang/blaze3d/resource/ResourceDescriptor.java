/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ public interface ResourceDescriptor<T>
/*    */ {
/*    */   T allocate();
/*    */   
/*    */   default void prepare(T resource) {}
/*    */   
/*    */   void free(T paramT);
/*    */   
/*    */   default boolean canUsePhysicalResource(ResourceDescriptor<?> other) {
/* 12 */     return equals(other);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/resource/ResourceDescriptor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */