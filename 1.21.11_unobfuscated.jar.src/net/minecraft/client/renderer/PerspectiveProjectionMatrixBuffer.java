/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ 
/*    */ public class PerspectiveProjectionMatrixBuffer implements AutoCloseable {
/*    */   private final GpuBuffer buffer;
/*    */   private final GpuBufferSlice bufferSlice;
/*    */   
/*    */   public PerspectiveProjectionMatrixBuffer(String name) {
/* 18 */     GpuDevice device = RenderSystem.getDevice();
/* 19 */     this.buffer = device.createBuffer(() -> "Projection matrix UBO " + name, 136, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
/* 20 */     this.bufferSlice = this.buffer.slice(0L, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
/*    */   }
/*    */   
/*    */   public GpuBufferSlice getBuffer(Matrix4f projectionMatrix) {
/* 24 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 25 */     try { ByteBuffer byteBuffer = Std140Builder.onStack(stack, RenderSystem.PROJECTION_MATRIX_UBO_SIZE).putMat4f((Matrix4fc)projectionMatrix).get();
/* 26 */       RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), byteBuffer);
/* 27 */       if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/* 28 */         try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return this.bufferSlice;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 33 */     this.buffer.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/PerspectiveProjectionMatrixBuffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */