/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer.Usage;
/*    */ import com.mojang.blaze3d.buffers.GpuFence;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MappableRingBuffer
/*    */   implements AutoCloseable
/*    */ {
/*    */   private static final int BUFFER_COUNT = 3;
/* 16 */   private final GpuBuffer[] buffers = new GpuBuffer[3];
/* 17 */   private final GpuFence[] fences = new GpuFence[3];
/*    */   private final int size;
/* 19 */   private int current = 0;
/*    */   
/*    */   public MappableRingBuffer(Supplier<String> label, @GpuBuffer.Usage int usage, int size) {
/* 22 */     GpuDevice device = RenderSystem.getDevice();
/* 23 */     if ((usage & 0x1) == 0 && (usage & 0x2) == 0) {
/* 24 */       throw new IllegalArgumentException("MappableRingBuffer requires at least one of USAGE_MAP_READ or USAGE_MAP_WRITE");
/*    */     }
/*    */     
/* 27 */     for (int i = 0; i < 3; i++) {
/* 28 */       int finalI = i;
/* 29 */       this.buffers[i] = device.createBuffer(() -> (String)label.get() + " #" + (String)label.get(), usage, size);
/* 30 */       this.fences[i] = null;
/*    */     } 
/* 32 */     this.size = size;
/*    */   }
/*    */   
/*    */   public int size() {
/* 36 */     return this.size;
/*    */   }
/*    */   
/*    */   public GpuBuffer currentBuffer() {
/* 40 */     GpuFence fence = this.fences[this.current];
/* 41 */     if (fence != null) {
/* 42 */       fence.awaitCompletion(Long.MAX_VALUE);
/* 43 */       fence.close();
/* 44 */       this.fences[this.current] = null;
/*    */     } 
/* 46 */     return this.buffers[this.current];
/*    */   }
/*    */   
/*    */   public void rotate() {
/* 50 */     if (this.fences[this.current] != null) {
/* 51 */       this.fences[this.current].close();
/*    */     }
/* 53 */     this.fences[this.current] = RenderSystem.getDevice().createCommandEncoder().createFence();
/* 54 */     this.current = (this.current + 1) % 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 59 */     for (int i = 0; i < 3; i++) {
/* 60 */       this.buffers[i].close();
/* 61 */       if (this.fences[i] != null)
/* 62 */         this.fences[i].close(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/MappableRingBuffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */