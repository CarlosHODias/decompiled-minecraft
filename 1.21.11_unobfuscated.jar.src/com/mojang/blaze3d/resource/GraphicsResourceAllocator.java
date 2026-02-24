/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ public interface GraphicsResourceAllocator {
/*  4 */   public static final GraphicsResourceAllocator UNPOOLED = new GraphicsResourceAllocator()
/*    */     {
/*    */       public <T> T acquire(ResourceDescriptor<T> descriptor) {
/*  7 */         T resource = descriptor.allocate();
/*  8 */         descriptor.prepare(resource);
/*  9 */         return resource;
/*    */       }
/*    */ 
/*    */       
/*    */       public <T> void release(ResourceDescriptor<T> descriptor, T resource) {
/* 14 */         descriptor.free(resource);
/*    */       }
/*    */     };
/*    */   
/*    */   <T> T acquire(ResourceDescriptor<T> paramResourceDescriptor);
/*    */   
/*    */   <T> void release(ResourceDescriptor<T> paramResourceDescriptor, T paramT);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/resource/GraphicsResourceAllocator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */