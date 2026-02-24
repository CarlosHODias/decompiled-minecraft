/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DynamicUniformStorage<T extends DynamicUniformStorage.DynamicUniform>
/*     */   implements AutoCloseable
/*     */ {
/*  17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  19 */   private final List<MappableRingBuffer> oldBuffers = new ArrayList<>();
/*     */   private final int blockSize;
/*     */   private MappableRingBuffer ringBuffer;
/*     */   private int nextBlock;
/*     */   private int capacity;
/*     */   private T lastUniform;
/*     */   private final String label;
/*     */   
/*     */   public DynamicUniformStorage(String label, int uboSize, int initialCapacity) {
/*  28 */     GpuDevice device = RenderSystem.getDevice();
/*     */     
/*  30 */     this.blockSize = Mth.roundToward(uboSize, device.getUniformOffsetAlignment());
/*  31 */     this.capacity = Mth.smallestEncompassingPowerOfTwo(initialCapacity);
/*  32 */     this.nextBlock = 0;
/*  33 */     this.ringBuffer = new MappableRingBuffer(() -> label + " x" + label, 130, this.blockSize * this.capacity);
/*  34 */     this.label = label;
/*     */   }
/*     */   
/*     */   public void endFrame() {
/*  38 */     this.nextBlock = 0;
/*  39 */     this.lastUniform = null;
/*  40 */     this.ringBuffer.rotate();
/*     */     
/*  42 */     if (!this.oldBuffers.isEmpty()) {
/*  43 */       for (MappableRingBuffer oldBuffer : this.oldBuffers) {
/*  44 */         oldBuffer.close();
/*     */       }
/*  46 */       this.oldBuffers.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resizeBuffers(int newCapacity) {
/*  51 */     this.capacity = newCapacity;
/*  52 */     this.nextBlock = 0;
/*  53 */     this.lastUniform = null;
/*  54 */     this.oldBuffers.add(this.ringBuffer);
/*  55 */     this.ringBuffer = new MappableRingBuffer(() -> this.label + " x" + this.label, 130, this.blockSize * this.capacity);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice writeUniform(T uniform) {
/*  59 */     if (this.lastUniform != null && this.lastUniform.equals(uniform)) {
/*  60 */       return this.ringBuffer.currentBuffer().slice(((this.nextBlock - 1) * this.blockSize), this.blockSize);
/*     */     }
/*     */     
/*  63 */     if (this.nextBlock >= this.capacity) {
/*  64 */       int newCapacity = this.capacity * 2;
/*  65 */       LOGGER.info("Resizing {}, capacity limit of {} reached during a single frame. New capacity will be {}.", new Object[] { this.label, this.capacity, newCapacity });
/*  66 */       resizeBuffers(newCapacity);
/*     */     } 
/*     */     
/*  69 */     int offset = this.nextBlock * this.blockSize;
/*  70 */     GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.ringBuffer.currentBuffer().slice(offset, this.blockSize), false, true); 
/*  71 */     try { uniform.write(view.data());
/*  72 */       if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/*  73 */         try { view.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  this.nextBlock++;
/*  74 */     this.lastUniform = uniform;
/*  75 */     return this.ringBuffer.currentBuffer().slice(offset, this.blockSize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GpuBufferSlice[] writeUniforms(T[] uniforms) {
/*  81 */     if (uniforms.length == 0) {
/*  82 */       return new GpuBufferSlice[0];
/*     */     }
/*     */     
/*  85 */     if (this.nextBlock + uniforms.length > this.capacity) {
/*  86 */       int newCapacity = Mth.smallestEncompassingPowerOfTwo(Math.max(this.capacity + 1, uniforms.length));
/*  87 */       LOGGER.info("Resizing {}, capacity limit of {} reached during a single frame. New capacity will be {}.", new Object[] { this.label, this.capacity, newCapacity });
/*  88 */       resizeBuffers(newCapacity);
/*     */     } 
/*     */     
/*  91 */     int firstOffset = this.nextBlock * this.blockSize;
/*  92 */     GpuBufferSlice[] result = new GpuBufferSlice[uniforms.length];
/*  93 */     GpuBuffer.MappedView view = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.ringBuffer.currentBuffer().slice(firstOffset, (uniforms.length * this.blockSize)), false, true); 
/*  94 */     try { ByteBuffer byteBuffer = view.data();
/*  95 */       for (int i = 0; i < uniforms.length; i++) {
/*  96 */         T uniform = uniforms[i];
/*  97 */         result[i] = this.ringBuffer.currentBuffer().slice((firstOffset + i * this.blockSize), this.blockSize);
/*  98 */         byteBuffer.position(i * this.blockSize);
/*  99 */         uniform.write(byteBuffer);
/*     */       } 
/* 101 */       if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/* 102 */         try { view.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  this.nextBlock += uniforms.length;
/* 103 */     this.lastUniform = uniforms[uniforms.length - 1];
/* 104 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 109 */     for (MappableRingBuffer oldBuffer : this.oldBuffers) {
/* 110 */       oldBuffer.close();
/*     */     }
/* 112 */     this.ringBuffer.close();
/*     */   }
/*     */   
/*     */   public static interface DynamicUniform {
/*     */     void write(ByteBuffer param1ByteBuffer);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/DynamicUniformStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */