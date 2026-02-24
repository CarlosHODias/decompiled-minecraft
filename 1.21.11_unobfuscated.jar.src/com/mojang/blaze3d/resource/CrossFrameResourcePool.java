/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Collection;
/*    */ import java.util.Deque;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CrossFrameResourcePool
/*    */   implements GraphicsResourceAllocator, AutoCloseable
/*    */ {
/*    */   private final int framesToKeepResource;
/* 18 */   private final Deque<ResourceEntry<?>> pool = new ArrayDeque<>();
/*    */   
/*    */   public CrossFrameResourcePool(int framesToKeepResource) {
/* 21 */     this.framesToKeepResource = framesToKeepResource;
/*    */   }
/*    */   
/*    */   public void endFrame() {
/* 25 */     Iterator<? extends ResourceEntry<?>> iterator = this.pool.iterator();
/* 26 */     while (iterator.hasNext()) {
/* 27 */       ResourceEntry<?> entry = iterator.next();
/* 28 */       if (entry.framesToLive-- == 0) {
/* 29 */         entry.close();
/* 30 */         iterator.remove();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T acquire(ResourceDescriptor<T> descriptor) {
/* 37 */     T resource = acquireWithoutPreparing(descriptor);
/* 38 */     descriptor.prepare(resource);
/* 39 */     return resource;
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> T acquireWithoutPreparing(ResourceDescriptor<T> descriptor) {
/* 44 */     Iterator<? extends ResourceEntry<?>> iterator = this.pool.iterator();
/* 45 */     while (iterator.hasNext()) {
/* 46 */       ResourceEntry<?> entry = iterator.next();
/* 47 */       if (descriptor.canUsePhysicalResource(entry.descriptor)) {
/* 48 */         iterator.remove();
/* 49 */         return entry.value;
/*    */       } 
/*    */     } 
/* 52 */     return descriptor.allocate();
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void release(ResourceDescriptor<T> descriptor, T resource) {
/* 57 */     this.pool.addFirst(new ResourceEntry(descriptor, resource, this.framesToKeepResource));
/*    */   }
/*    */   
/*    */   public void clear() {
/* 61 */     this.pool.forEach(ResourceEntry::close);
/* 62 */     this.pool.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 67 */     clear();
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected Collection<ResourceEntry<?>> entries() {
/* 72 */     return this.pool;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected static final class ResourceEntry<T> implements AutoCloseable {
/*    */     private final ResourceDescriptor<T> descriptor;
/*    */     private final T value;
/*    */     private int framesToLive;
/*    */     
/*    */     private ResourceEntry(ResourceDescriptor<T> descriptor, T value, int framesToLive) {
/* 82 */       this.descriptor = descriptor;
/* 83 */       this.value = value;
/* 84 */       this.framesToLive = framesToLive;
/*    */     }
/*    */ 
/*    */     
/*    */     public void close() {
/* 89 */       this.descriptor.free(this.value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/resource/CrossFrameResourcePool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */