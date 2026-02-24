/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Queue;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class SectionBufferBuilderPool
/*    */ {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Queue<SectionBufferBuilderPack> freeBuffers;
/*    */   private volatile int freeBufferCount;
/*    */   
/*    */   private SectionBufferBuilderPool(List<SectionBufferBuilderPack> buffers) {
/* 19 */     this.freeBuffers = Queues.newArrayDeque(buffers);
/* 20 */     this.freeBufferCount = this.freeBuffers.size();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static SectionBufferBuilderPool allocate(int maxWorkers) {
/* 26 */     int maxBuffers = Math.max(1, (int)(Runtime.getRuntime().maxMemory() * 0.3D) / SectionBufferBuilderPack.TOTAL_BUFFERS_SIZE);
/* 27 */     int targetBufferCount = Math.max(1, Math.min(maxWorkers, maxBuffers));
/*    */     
/* 29 */     List<SectionBufferBuilderPack> buffers = new ArrayList<>(targetBufferCount);
/*    */     try {
/* 31 */       for (int i = 0; i < targetBufferCount; i++) {
/* 32 */         buffers.add(new SectionBufferBuilderPack());
/*    */       }
/* 34 */     } catch (OutOfMemoryError e) {
/* 35 */       LOGGER.warn("Allocated only {}/{} buffers", buffers.size(), targetBufferCount);
/*    */       
/* 37 */       int buffersToDrop = Math.min(buffers.size() * 2 / 3, buffers.size() - 1);
/* 38 */       for (int i = 0; i < buffersToDrop; i++) {
/* 39 */         ((SectionBufferBuilderPack)buffers.remove(buffers.size() - 1)).close();
/*    */       }
/*    */     } 
/*    */     
/* 43 */     return new SectionBufferBuilderPool(buffers);
/*    */   }
/*    */   
/*    */   public SectionBufferBuilderPack acquire() {
/* 47 */     SectionBufferBuilderPack buffer = this.freeBuffers.poll();
/* 48 */     if (buffer != null) {
/* 49 */       this.freeBufferCount = this.freeBuffers.size();
/* 50 */       return buffer;
/*    */     } 
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public void release(SectionBufferBuilderPack buffer) {
/* 56 */     this.freeBuffers.add(buffer);
/* 57 */     this.freeBufferCount = this.freeBuffers.size();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 61 */     return this.freeBuffers.isEmpty();
/*    */   }
/*    */   
/*    */   public int getFreeBufferCount() {
/* 65 */     return this.freeBufferCount;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SectionBufferBuilderPool.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */