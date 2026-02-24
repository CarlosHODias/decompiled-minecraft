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
/*    */ public class CachedOrthoProjectionMatrixBuffer
/*    */   implements AutoCloseable {
/*    */   private final GpuBuffer buffer;
/*    */   private final GpuBufferSlice bufferSlice;
/*    */   private final float zNear;
/*    */   private final float zFar;
/*    */   private final boolean invertY;
/*    */   private float width;
/*    */   private float height;
/*    */   
/*    */   public CachedOrthoProjectionMatrixBuffer(String name, float zNear, float zFar, boolean invertY) {
/* 24 */     this.zNear = zNear;
/* 25 */     this.zFar = zFar;
/* 26 */     this.invertY = invertY;
/* 27 */     GpuDevice device = RenderSystem.getDevice();
/* 28 */     this.buffer = device.createBuffer(() -> "Projection matrix UBO " + name, 136, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
/* 29 */     this.bufferSlice = this.buffer.slice(0L, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
/*    */   }
/*    */   
/*    */   public GpuBufferSlice getBuffer(float width, float height) {
/* 33 */     if (this.width != width || this.height != height) {
/* 34 */       Matrix4f projectionMatrix = createProjectionMatrix(width, height);
/* 35 */       MemoryStack stack = MemoryStack.stackPush(); 
/* 36 */       try { ByteBuffer byteBuffer = Std140Builder.onStack(stack, RenderSystem.PROJECTION_MATRIX_UBO_SIZE).putMat4f((Matrix4fc)projectionMatrix).get();
/* 37 */         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), byteBuffer);
/* 38 */         if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/* 39 */           try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  this.width = width;
/* 40 */       this.height = height;
/*    */     } 
/* 42 */     return this.bufferSlice;
/*    */   }
/*    */   
/*    */   private Matrix4f createProjectionMatrix(float width, float height) {
/* 46 */     return new Matrix4f().setOrtho(0.0F, width, this.invertY ? height : 0.0F, this.invertY ? 0.0F : height, this.zNear, this.zFar);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 51 */     this.buffer.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/CachedOrthoProjectionMatrixBuffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */